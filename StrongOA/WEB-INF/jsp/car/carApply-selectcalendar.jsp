<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>

<html>
	<head>
	<title>日程组件</title>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/calendar_properties.css" type=text/css rel=stylesheet>
	
	<link rel="stylesheet" href="<%=frameroot %>/css/calendar/calendar.css" type="text/css">
	<link rel="stylesheet" href="<%=frameroot %>/css/calendar/window.css" type="text/css">
	<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz_lib/scriptaculous.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz_lib/spinelz_util.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz_lib/resize.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz/calendar.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz/window.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz/window_resizeEx.js" type="text/javascript"></script>
	
	<style type="text/css">
		 table, tr, td, div{
			    margin:0px;
			}
		body{
			    margin-bottom: 0px;
			    margin-left: 2px;
			    margin-right: 2px;
			    margin-top: 0px;
			}
	    .image1 {
	      list-style-image: url('images/cal1.gif');
	      margin-left: 15px;
	    }
	    .image2 {
	      list-style-image: url('images/cal2.gif');
	      margin-left: 15px;
	    }
	</style>
	<script>
		function gotoList(){
			 location = "<%=path%>/car/carApply!list1.action?carId="+carId.value+"&carno="+encodeURI(encodeURI(carno.value));
		}
	</script>
</head>
<body >
  <div style="font-size: 14px; height: 250px; margin-bottom: 0px;">
  <input type="hidden" id="insetDay" value="${setDate}">
    <input type="hidden" id="inputType" value="${inputType}">
  <input type="hidden" id="carId" value="${carId}">
  <input type="hidden" id="carno" value="${carno}">
  <div onclick="closeAddwindow()">
  		<table height="40" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr valign="top">
				<td height="40"	style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
						startColorStr=#ededed,endColorStr=#ffffff);">
						<table height="40" width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
				            <td>&nbsp;</td>
				            <td width="30%">
				            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
				          		  车辆${carno}审批情况列表
				            </td>
				            <td>&nbsp;</td>
				            <td width="70%">
				            <table border="0" align="right" cellpadding="00" cellspacing="0">
				                <tr>
				                 	<td ><a class="Operation" href="javascript:calendar.changeDisplayType('day');"><img src="<%=root%>/images/ico/cal_day.gif"   class="img_s">日历&nbsp;</a></td>
				                 	<td width="5"></td>
				                 	<td ><a class="Operation" href="javascript:calendar.changeDisplayType('week');"><img src="<%=root%>/images/ico/cal_week.gif"  class="img_s">周历&nbsp;</a></td>
				                 	<td width="5"></td>
				                 	<td ><a class="Operation" href="javascript:calendar.changeDisplayType('month');"><img src="<%=root%>/images/ico/cal_month.gif"  class="img_s">月历&nbsp;</a></td>
				                 	<td width="5"></td>
				                 	<td ><a class="Operation" href="javascript: gotoList();"><img src="<%=root%>/images/ico/cal_list.gif"  class="img_s">列表&nbsp;</a></td>
				                 	<td width="5"></td>
				                </tr>
			            	</table>
			            	</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
  </div>
  
  <div id="calendar1" style="margin-bottom: 0px;"></div>
  <script type="text/javascript">
  //加载指定时间指定车辆的用车情况
  	function setSchedules(startD,endD){
   		//alert("setSchedules()-->\n\n开始时间为:"+startD+"\n结束时间为:"+endD);
   		var myAjax = new Ajax.Request(
                 scriptroot+'/car/carApply!changeCalendar.action', // 请求的URL
                {
                    parameters : 'termStart='+startD+'&termEnd='+endD+"&carId="+carId.value,
                    method:  'post', 
                    onComplete: function(response){
                    		//alert(response.responseText);
                    		this.calendar.options.schedules=[];
                    		var msg = ""+response.responseText;
                    		var JSONobj = eval('('+msg+')');
                    		this.calendar.options.schedules=JSONobj;
                    		
                    		this.calendar.refreshSchedule();
                    	}
                }
            );
   	}
  
  
    var maxId = 1;
    var today;
	var insetDate = insetDay.value.split("-");
	if(insetDate.length=="1"){
    	today = new Date();
	}else{
		today = new Date(insetDate[0],insetDate[1]-1,insetDate[2]);
	}
    
//初始化所有事务
   //   var schedules = ${jsonArr};
//init日程视图
    var calendar = new Calendar(
        'calendar1', 
        {
          size: 'large',	//模式(small)
          dblclickListener: function(date, element) {alert(date)},	
          initDate: today,		//初始化当前时间
          displayTime:        [{hour: 7, min: 00}, {hour: 19, min: 30}],
          displayType:        'week',			//视图显示模式(month,week,day)
          weekIndex:          0,
          holidays:           [],
          schedules:          [],		//定义事务数据
          dblclickSchedule:   function(schedule) {		//双击事务图标执行 函数
          	addwindow.style.display='none'
          	var calId = schedule.id;
          	alert(schedule.description);
          },
          getWeekHeaderText: function(first, last) {
            return [first.getDate(), '=', last.getDate()];
          }
        });

    var changeDisplay = function(element) {
      if (element.checked)
        calendar.showDayOfWeek(element.value);
      else
        calendar.hideDayOfWeek(element.value);
    }

    var getHoliday = function() {
      var result = calendar.getHoliday();
      if (result) result.each(function(r) { alert(r.name) });
    }

    var getSchedule = function() {
      
    }

//添加事务
    var addSchedule = function() {
      var term = calendar.getSelectedTerm();//(term="DateStart,DateEnd")开始时间到结束时间
      if (!term){
		alert("请在视图中选中要添加的时间段");      
       return;
      }

      if (calendar.options.displayType == 'month') {
        term[0].setHours(12);
        term[1].setHours(14);
      }

      var calStartTime = term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate()+" "+term[0].getHours()+":"+term[0].getMinutes()+":"+term[0].getSeconds();
      var calEndTime = term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate()+" "+term[1].getHours()+":"+term[1].getMinutes()+":"+term[0].getSeconds();
	  var scheduleId = "";
	//添加事务
	if(""==carId.value|null==carId.value){
		alert("没有车辆信息，无法申请！\n请联系车辆管理员录入车辆信息！");
		return;
	}
	window.parent.parent.parent.location="<%=path%>/car/carApply!input.action?carId="+carId.value+"&termStart="+calStartTime+"&termEnd="+calEndTime;
	   
    }
        
//添加操作ajax返回值
        function addResponse(response,term) {
            $('divResult').innerHTML = response.responseText||"no response text";
            var msg = $('divResult').innerText;
            $('divResult').innerHTML = "";
            
    		var JSONobj = eval('('+msg+')');
    		//alert("添加成功")
			alert( $(JSONobj).msg.trim())
	 // 绘制视图事物
	      
	      var schedule = {
	        id:          $(JSONobj).id,
	        description: $('calTitle').value
	      };
	      schedule.start =  {
	          year:  term[0].getFullYear(),
	          month: term[0].getMonth(),
	          day:   term[0].getDate(),
	          hour:  term[0].getHours(),
	          min:   term[0].getMinutes()
	      }
	      schedule.finish = {
	        year:  term[1].getFullYear(),
	        month: term[1].getMonth(),
	        day:   term[1].getDate(),
	        hour:  term[1].getHours(),
	        min:   term[1].getMinutes()
	      }
	      calendar.addSchedule(schedule);
	      calendar.clearSelected();
        }
        
        
//执行删除操作到数据库
    function deleteSchedule(scheduleId){
    /*	var myAjax = new Ajax.Request(
                 '<%=path%>/calendar/calendar!delete.action', // 请求的URL
                {
                    //参数
                    parameters : 'calendarId='+scheduleId,
                    // 使用GET方式发送HTTP请求
                    method:  'get', 
                    // 指定请求成功完成时需要执行的js方法
                    onComplete: function(response){
		                    $('divResult').innerHTML = response.responseText||"no response text";;
                    		alert($('divResult').innerText.trim());
                    		$('divResult').innerHTML = "";
                    	}
                }
            );
      */
    }
    
        
//修改日程事务
	function updateTerm(term){
	var termStart = term.start.year+"-"+(term.start.month+1)+"-"+term.start.day+" "+term.start.hour+":"+term.start.min;
	var termEnd = term.finish.year+"-"+(term.finish.month+1)+"-"+term.finish.day+" "+term.finish.hour+":"+term.finish.min;
	var myAjax = new Ajax.Request(
                 '<%=path%>/calendar/calendar!add.action', // 请求的URL
                {
                    //参数
                    parameters : 'calendarId='+term.id+'&termStart='+termStart+'&termEnd='+termEnd,
                    // 使用GET方式发送HTTP请求
                    method:  'get', 
                    // 指定请求成功完成时需要执行的js方法
                    onComplete: function(response){
		                    $('divResult').innerHTML = response.responseText||"no response text";
                    		var JSONobj = eval('('+$('divResult').innerText+')');
							alert( $(JSONobj).msg.trim())
                    		$('divResult').innerHTML = "";
                    	}
                }
            );
	}

//弹出添加窗口 
function showAddwindow(x,y){
	//term选中的日程单元格
	var term = calendar.getSelectedTerm();//(term="DateStart,DateEnd")开始时间到结束时间
    if (!term){
     return;
    } else {
     var starttime=term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate();
     if (term[0].getHours()<10){
     starttime=starttime+" "+"0"+term[0].getHours();
     }else{
     starttime=starttime+" "+term[0].getHours();
     }
     if (term[0].getMinutes()<10){
     starttime=starttime+":"+"0"+term[0].getMinutes();
     }else{
     starttime=starttime+":"+term[0].getMinutes();
     }
     
     var endtime=term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate();
     if (term[1].getHours()<10){
        endtime=endtime+" "+"0"+term[1].getHours();
     }else{
        endtime=endtime+" "+term[1].getHours();
     }
     if (term[1].getMinutes()<10){
        endtime=endtime+":"+"0"+term[1].getMinutes();
     }else{
        endtime=endtime+":"+term[1].getMinutes();
     }
    }
      
    
    addwindow.style.display="";
    var rightedge = document.body.clientWidth-x;
    if (rightedge < addwindow.clientWidth){
			addwindow.style.left = document.body.scrollLeft + x - addwindow.offsetWidth;
		}else{
			addwindow.style.left = document.body.scrollLeft + x;
		}
	var clientHeight = document.body.clientHeight;
	var topedge = document.body.clientHeight-y;
	if(topedge<100){
		addwindow.style.top=y-100;
	}else{
		addwindow.style.top=y;
	}
	addwindow.innerHTML='<TABLE CELLPADDING="2" CELLSPACING="0" WIDTH="220" BORDER="0" BGCOLOR="#B0C4DE"><TR><TD>' 
    + '<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0"><TR><TD STYLE="padding-top:2px;padding-bottom:2px;font:8pt Arial,Verdana,Tahoma;color:#FFF8F0">选中时段</TD><td width="18"STYLE="padding-top:2px;padding-bottom:2px;font:9pt Arial,Verdana,Tahoma;color:#FFF8F0" title="关闭"><img onclick="closeAddwindow();" src="<%=root%>/images/ico/shanchu.gif"/></td></TR></TABLE>' 
    + '<TABLE CELLPADDING="2" CELLSPACING="0" WIDTH="100%" BORDER="0" BGCOLOR="#ffffff"><TR>'
    +'<TD STYLE="color:#3F3F38;font:normal 9pt Arial,Verdana,Tahoma">'
    +starttime+'&nbsp;&nbsp;至&nbsp;&nbsp;'+endtime+'<br>&nbsp;&nbsp;<font class=\"wx\">在选中时段内申请使用车辆？</font>'
    +'</TD></TR>'
    +'<TR><TD align=\"center\">'
     +'<input type=\"submit\" value=\"确定\" onclick=\"addSchedule();addwindow.style.display=\'none\'\">'
    +'&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" value=\"取消\" onclick=\"closeAddwindow();\">'
    +'</TD></TR>'
    +'</TABLE></TD></TR></TABLE>'; 
}

//取消关闭快速添加窗口
function closeAddwindow(){
	addwindow.style.display="none";
	calendar.clearSelected();
}
  String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }

  </script>
  <s:form >
  	<label id="message" style="display: none"><s:actionmessage/></label>
  </s:form>
  <div id="addwindow" style="border:5px; width:220px; display:none; position: absolute; background-color:#FFFF99;">
  </div>
  <div id="divResult" class="divResult" style="display: none"></div>
  </body>
</html>

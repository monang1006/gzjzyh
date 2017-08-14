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
	<script src="<%=path %>/oa/js/meetingroom/meetingroom_calendar.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz/window.js" type="text/javascript"></script>
	<script src="<%=path %>/oa/js/calendar/spinelz/window_resizeEx.js" type="text/javascript"></script>
	
	<style type="text/css">
		 body, table, tr, td, div{
			    margin:0px;
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
			//location = "<%=path%>/fileNameRedirectAction.action?toPage=meetingroom/meetingroom-apply.jsp";
			location = "<%=path%>/meetingroom/meetingApply.action?inputType=${canEdit}&model.toaMeetingroom.mrId=${mrId}";
		}
	</script>
</head>
<body onclick="parent.hideDetail();">
  <div style="font-size: 14px; margin-bottom: 0px;">
  <input type="hidden" id="insetDay" value="${setDate}">
  <input type="hidden" id="inputType" value="${canEdit}">
  <input type="hidden" id="mrId" value="${mrId}">
  <div onclick="closeAddwindow();">
  		<table height="40" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr valign="top">
				<td height="40"	style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
						startColorStr=#ededed,endColorStr=#ffffff);">
						<table height="40" width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
				            <td width="30%">
				            	&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
				            	<script>
				            	var mrName = "${model.mrName}";
				            	if(""==mrName|null==mrName){
				            		document.write("所有会议室的使用情况");
				            	}else{
				            		document.write("会议室（<b>"+mrName+"</b>）的使用情况");
				            	}
				            	</script>
				            </td>
				            <td>&nbsp;</td>
				            <td width="70%">
				            <table border="0" align="right" cellpadding="00" cellspacing="0">
				                <tr>
				                	<td ><a class="Operation" href="javascript:newApp();"><img src="<%=frameroot%>/images/tianjia.gif"   class="img_s">新申请&nbsp;</a></td>
				                 	<td width="5"></td>
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
  
  <div id="calendar1" style="margin-bottom: 0px; margin-right: 2px;"></div>
  <script type="text/javascript">
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
          displayType:        "${displayType}",			//视图显示模式(month,week,day)
          weekIndex:          0,
          holidays:           [],
          schedules:          [],		//定义事务数据
          dblclickSchedule:   function(schedule) {		//双击事务图标执行 函数
          	addwindow.style.display='none'
          	var maId = schedule.id;
          	//alert("id = " + schedule.id+"\ncanEdit = ${canEdit}"+"\n"+schedule.edit);
          	viewApply(maId);
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

	function newApp(){
		var url = "<%=path%>/meetingroom/meetingApply!input.action"//?mrId=${mrId}&model.maAppstarttime="+calStartTime+"&model.maAppendtime="+calEndTime;
	  	closeAddwindow();
		var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');
		if("reload"==a){
			parent.changeRoom("${mrId}");
		}
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

      var calStartTime = term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate()+" "+term[0].getHours()+":"+term[0].getMinutes()+":00";
      var calEndTime = term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate()+" "+term[1].getHours()+":"+term[1].getMinutes()+":00";
	  var scheduleId = "";
	  
	  	var url = "<%=path%>/meetingroom/meetingApply!input.action?mrId=${mrId}&model.maAppstarttime="+calStartTime+"&model.maAppendtime="+calEndTime;
	  	closeAddwindow();
		var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');
		if("reload"==a){
			parent.changeRoom("${mrId}");
		}
			
	//添加事务
    /*  var myAjax = new Ajax.Request(
                 '<%=path%>/calendar/calendar!add.action', // 请求的URL
                {
                    //参数
                    parameters : 'model.calTitle='+encodeURI(encodeURI($('calTitle').value))+'&termStart='+calStartTime+'&termEnd='+calEndTime,
                   
                    // 使用GET方式发送HTTP请求
                    method:  'post', 
                    // 指定请求成功完成时需要执行的js方法
                    onComplete: function(response){
                    		addResponse(response,term);
                    	}
                }
            );
     */
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
    	var myAjax = new Ajax.Request(
                 '<%=path%>/meetingroom/meetingApply!delete.action', // 请求的URL
                {
                    //参数
                    parameters : 'maId='+scheduleId,
                    // 使用GET方式发送HTTP请求
                    method:  'get', 
                    // 指定请求成功完成时需要执行的js方法
                    onComplete: function(response){
		                    var msg = response.responseText||"no response text";;
                    		if("reload"!=msg){
                    			alert(msg);
                    		} 
                    	}
                }
            );
    }
    
        
//修改日程事务
	function updateTerm(term){
	var termStart = term.start.year+"-"+(term.start.month+1)+"-"+term.start.day+" "+term.start.hour+":"+term.start.min;
	var termEnd = term.finish.year+"-"+(term.finish.month+1)+"-"+term.finish.day+" "+term.finish.hour+":"+term.finish.min;
	var myAjax = new Ajax.Request(
                 '<%=path%>/meetingroom/meetingApply!moveApplySchedules.action', // 请求的URL
                {
                    //参数
                    parameters : 'maId='+term.id+'&termStart='+termStart+'&termEnd='+termEnd,
                    // 使用GET方式发送HTTP请求
                    method:  'get', 
                    // 指定请求成功完成时需要执行的js方法
                    onComplete: function(response){
		                    var msg = response.responseText||"no response text";
                    		if("success"!=msg){
                    			alert("保存到数据库失败！");
                    		}
                    	}
                }
            );
	}

//弹出添加窗口 
function showAddwindow(x,y){

	var mrId = "${mrId}";
	//term选中的日程单元格
	var term = calendar.getSelectedTerm();//(term="DateStart,DateEnd")开始时间到结束时间
    if (!term){
     return;
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
    
    var roomHtml = "";
    if(""=="${model.mrName}"|null=="${model.mrName}"){
    	roomHtml = "&nbsp;&nbsp;<font class=\"wx\">会议室名：</font>&nbsp;&nbsp;未选择会议室";
    }else{
    	roomHtml = "&nbsp;&nbsp;<font class=\"wx\">会议室名：</font>&nbsp;&nbsp;${model.mrName}";
    }
    var strSH = ""+term[0].getHours();
    var strSM = ""+term[0].getMinutes();
    var strEH = ""+term[1].getHours();
    var strEM = ""+term[1].getMinutes();
    if(term[0].getHours()<10){
    	strSH = "0"+term[0].getHours();
    }
    if(term[0].getMinutes()<10){
    	strSM = "0"+term[0].getMinutes();
    }
    if(term[1].getHours()<10){
    	strEH = "0"+term[1].getHours();
    }
    if(term[1].getMinutes()<10){
    	strEM = "0"+term[1].getMinutes();
    }
	addwindow.innerHTML='<TABLE CELLPADDING="2" CELLSPACING="0" WIDTH="200" BORDER="0" BGCOLOR="#B0C4DE" ><TR><TD>' 
    + '<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0"><TR><TD STYLE="padding-top:2px;padding-bottom:2px;font:8pt Arial,Verdana,Tahoma;color:#FFF8F0">&nbsp;填写会议室申请单</TD><td width="18"STYLE="padding-top:2px;padding-bottom:2px;font:9pt Arial,Verdana,Tahoma;color:#FFF8F0" title="关闭"><img onclick="closeAddwindow();" src="<%=root%>/images/ico/shanchu.gif"/></td></TR></TABLE>' 
    + '<TABLE CELLPADDING="2" CELLSPACING="0" WIDTH="100%" BORDER="0" BGCOLOR="#ffffff"><TR>'
    +'<TD STYLE="color:#3F3F38;font:normal 8pt Arial,Verdana,Tahoma">'
    //+'&nbsp;&nbsp;<font class=\"wx\">申请在选中时段使用会议室</font><br>&nbsp;&nbsp;"'+term+'"<input type=\"submit\" value=\"确定\" onclick=\"addSchedule();addwindow.style.display=\'none\'\">'
    +roomHtml
    +'<br>&nbsp;&nbsp;<font class=\"wx\">开始时间：</font>&nbsp;&nbsp;'+term[0].getYear()+'-'+(term[0].getMonth()+1)+'-'+term[0].getDate()+' '+strSH+':'+strSM
    +'<br>&nbsp;&nbsp;<font class=\"wx\">结束时间：</font>&nbsp;&nbsp;'+term[1].getYear()+'-'+(term[1].getMonth()+1)+'-'+term[1].getDate()+' '+strEH+':'+strEM
    +'<br>&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" value=\"确定\" class=\"input_bg\" onclick=\"addSchedule();addwindow.style.display=\'none\'\">'
    +'&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"submit\" value=\"取消\" class=\"input_bg\" onclick=\"closeAddwindow();\">'
    +'</TD></TR></TABLE></TD></TR></TABLE>'; 
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
   
   
   
   	function setSchedules(startD,endD){
 //  		alert("setSchedules()-->\n\n开始时间为:"+startD+"\n结束时间为:"+endD);
   		var myAjax = new Ajax.Request(
                 scriptroot+'/meetingroom/meetingApply!setSchedules.action', // 请求的URL
                {
                    parameters : 'termStart='+startD+'&termEnd='+endD+"&inputType="+inputType.value+"&mrId=${mrId}",
                    method:  'post', 
                    onComplete: function(response){
                    		//alert(response.responseText);
                    		this.calendar.options.schedules=[];
                    		var msg = ""+response.responseText;
                    		var JSONobj = eval('('+msg+')');
	                    		
                    		var d = startD.split("-");
                    		var ind = new Date(d[0],d[1],d[2]);
                    		this.calendar.options.initDate=ind;
	                    		
                    		this.calendar.options.schedules=JSONobj;
                    		this.calendar.refreshSchedule();
                    	}
                }
            );
   	}
   
   
   //查看处理 申请单
  	 	function viewApply(maId){
  	 		var url = "<%=path%>/meetingroom/meetingApply!view.action?maId="+maId+"&inputType=${canEdit}";
   			var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');
			if(undefined==a){
				return;
			}
			if("reload"==a){
			//	location = "<%=path%>/calendar/calendar!viewpage.action?ifleader="+$(ifleader).value;
				parent.changeRoom("${mrId}");
			}
			if(a.indexOf("swich,")==0){
				var aa = a.split(",");
				viewApply(aa[1]);
				
			}
  	 	}
  	 	
  	 	
    function getCalendarDate(){
	   		var idate = this.calendar.options.initDate;
			return idate.getYear()+"-"+(idate.getMonth()+1)+"-"+idate.getDate();
	   }
  </script>
  <s:form >
  	<label id="message" style="display: none"><s:actionmessage/></label>
  </s:form>
  <div id="addwindow" style="border:5px; width:200px; display:none; position: absolute; background-color:#FFFF99;">
  </div>
  <div id="divResult" class="divResult" style="display: none"></div>
  </body>
</html>

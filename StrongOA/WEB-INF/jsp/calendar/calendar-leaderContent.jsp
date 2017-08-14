<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
	
	String frameH = "280px";
	if(frameroot.indexOf("12")>0){
		frameH = "240px";
	}
	
%>
<html>
	<head>
	<title>日程组件</title>
	<%@include file="/common/include/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows_list.css"/>
		<link rel="stylesheet" href="<%=path %>/oa/css/calendar/calendar.css" type="text/css">
		<link rel="stylesheet" href="<%=path %>/oa/css/calendar/window.css" type="text/css">
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz_lib/scriptaculous.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz_lib/spinelz_util.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz_lib/resize.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz/calendar.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz/window.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz/window_resizeEx.js" type="text/javascript"></script>
	
	<style type="text/css">
		body{
			    width: 99.5%;
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
		//跳转页面 到搜索列表
			function gotoList(){
				location = "<%=path%>/calendar/calendar!searchlist.action?inputType="+"leader";
			}
			

	</script>
</head>
<body style="overflow-y: auto;overflow-x:hidden ">
  <input type="hidden" id="insetDay" value="${setDate}">
    <script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script> 
			<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr valign="top">
				<td height="40"	style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
						startColorStr=#ededed,endColorStr=#ffffff);">
						<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
				            <td colspan="3" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
												<tr>
													<td colspan="3" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
												<tr>
													<td class="table_headtd_img" >
														<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
													</td>
													<td align="left">
														<strong>查看领导日程</strong>
													</td>
													<td align="right">
														<table border="0" align="right" cellpadding="00" cellspacing="0">
												            <tr>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="change('day');"><img src="<%=root%>/images/operationbtn/cal_day.png"/>&nbsp;日&nbsp;历&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="change('week');"><img src="<%=root%>/images/operationbtn/cal_week.png"/>&nbsp;周&nbsp;历&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                 								<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="change('month');"><img src="<%=root%>/images/operationbtn/cal_month.png"/>&nbsp;月&nbsp;历&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="gotoList();"><img src="<%=root%>/images/operationbtn/cal_list.png"/>&nbsp;列&nbsp;表&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="2%"></td>
											                 	</tr>
											            </table>
													</td>
												</tr>
											</table>
										</td>
												</tr>
											</table>
										</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
  					<div id="calendar1" style="margin-bottom: 15px;"></div>
				</td>	
			</tr>
			</table>
  <script type="text/javascript">
	function change(type){
		calendar.changeDisplayType(type);
	}
  function setSchedules(startD,endD){
//   		alert("setSchedules()-->\n\n开始时间为:"+startD+"\n结束时间为:"+endD);
  		var myAjax = new Ajax.Request(
                 scriptroot+'/calendar/calendar!changeCalendar.action', // 请求的URL
                {
                    parameters : 'termStart='+startD+'&termEnd='+endD+"&inputType=leader&userId=${userId}",
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
      var schedules = ${jsonArr};
//init日程视图
    var calendar = new Calendar(
        'calendar1', 
        {
          size: 'large',	//模式(small)
          dblclickListener: function(date, element) {alert(date)},	
//          regularHoliday: [0],
          initDate: today,		//初始化当前时间
          displayTime:        [{hour: 6, min: 50}, {hour: 23, min: 35}],
          displayType:        'month',			//视图显示模式(month,week,day)
          weekIndex:          0,
          holidays:           [],
          schedules:          [],		//定义事务数据
          dblclickSchedule:   function(schedule) {		//双击事务图标执行 函数
          
          	var calId = schedule.id;
          	calId = calId.replace("repeatBy","");
          	var url = "<%=path%>/calendar/calendar!readonly.action?calendarId="+calId;
			var a = window.showModalDialog(url,window,'dialogWidth:600px;dialogHeight:400px;help:no;status:no;scroll:no');
			if("reload"==a){
				location = "<%=path%>/calendar/calendar!viewpage.action"
			}
          },
//          getMonthHeaderText: function(date) {
//            return [date.getFullYear(), date.getMonth() + 1];
//          },
//          getMonthSubHeaderText: function(wday) {alert("wday="+wday);
//            return 'text' + wday;
//          },
          getWeekHeaderText: function(first, last) {
            return [first.getDate(), '=', last.getDate()];
          }
//          getWeekSubHeaderText: function(date) {
//            return 'text' + date.getDay();
//          }
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

      var calStartTime = term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate()+" "+term[0].getHours()+":"+term[0].getMinutes();
      var calEndTime = term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate()+" "+term[1].getHours()+":"+term[1].getMinutes();
	  var scheduleId = "";
	//添加事务
      var myAjax = new Ajax.Request(
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
    }
    
        
//修改日程事务
	function updateTerm(term){ 
	}

//弹出快速添加窗口 
function showAddwindow(x,y){ 
}

//取消关闭快速添加窗口
function closeAddwindow(){ 
            }
   
   	
  </script>
  
  <s:form >
  	<label id="message" style="display: none"><s:actionmessage/></label>
  </s:form>
  <div id="addwindow" style="c border:5px; display:none; position: absolute; background-color:#FFFF99;">
  </div>
  <div id="divResult" class="divResult" style="display: none"></div>
  </body>
</html>

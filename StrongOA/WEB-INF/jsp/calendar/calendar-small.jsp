<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
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
		.calendar_header_small {
			FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0, startColorStr=#ededed,endColorStr=#ffffff);
		}
		.calendar_small{width:99%;}
	</style>
</head>
<body id ="calbody" style=" background-color:#eeeff3">
  <input type="hidden" id="insetDay" value="${setDate}">
  			<div id="calendar1" style="margin : 0px;"></div>
			  <script type="text/javascript">
			    var maxId = 1;
			    var today;
			    var date ='${today}';
				if(date!=""){
					var today = date.split("-");
					today = new Date(today[0],today[1]-1,today[2]);
				}else{
					today = new Date();
				}
			//初始化所有事务
			      var schedules = ${jsonArr};
			//init日程视图
			    var calendar = new Calendar(
			        'calendar1', 
			        {
			          size: 'small',	//模式(large|small)
			          dblclickListener: function(date, element) {alert(date)},	
			          initDate: today,		//初始化当前时间
			          displayTime:        [{hour: 6, min: 50}, {hour: 23, min: 35}],
			          displayType:        'month',			//视图显示模式(month,week,day)
			          weekIndex:          0,
			          holidays:           [],
			          schedules:          schedules,		//定义事务数据
			          dblclickSchedule:   function(schedule) {},		//双击事务图标执行 函数
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
			//添加事务
			    var addSchedule = function() {}
			//添加操作ajax返回值
			 function addResponse(response,term) { }
			//执行删除操作到数据库
			  function deleteSchedule(scheduleId){ }
			//修改日程事务
				function updateTerm(term){}
			//弹出快速添加窗口 
			function showAddwindow(x,y){ }
			//取消关闭快速添加窗口
			function closeAddwindow(){ }
			
		//初始化事务数据
		    function setSchedules(startD,endD){}
		//选中日期
			   function selectInSmall(date){
			   	activityId = parent.document.getElementById("activityId").value;
			   	 var setDate = date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
			   	 var daycontent = parent.document.getElementById("daycontent");
			   	 if(daycontent!=undefined){
						 //daycontent.src = "<%=path%>/calendar/calendar!viewpage.action?ifleader=${ifleader}&setDate="+setDate+"&activityId="+activityId;
						 parent.operatebyclick(setDate,activityId);
			   	 }
			   }
			   
			   function nextMonth(startDate,endDate,type){
			   		//alert("月初 = "+startDate+"\n 月末 = "+endDate+"\n type = "+type);
			   		location = "<%=path%>/calendar/calendar!desktop.action?ifleade=${ifleader}&inputType=small&termStart="+startDate+"&termEnd="+endDate;
			   	}
			   	function mouseoverToShow(date,element,event){
			   	}
			   	
/*			   	function displaynone(ev){
			   		parent.document.getElementById('detail').style.display = "none"; 
			   	}
			   	document.onmousewheel = displaynone;
			   	parent.document.getElementById("contentborder").document.onmousewheel = displaynone;
			   	document.body.onclick = displaynone;
			   	parent.document.body.onclick = displaynone;
			   	parent.parent.document.body.onclick = displaynone;
			   	parent.parent.parent.document.body.onclick = displaynone;
*/			  
		</script>
  <s:form >
  	<label id="message" style="display: none"><s:actionmessage/></label>
  </s:form>
  	
  <div id="addwindow" style="c border:5px; display:none; position: absolute; background-color:#FFFF99;">
  </div>
  <div id="divResult" class="divResult" style="display: none"></div>
  </body>
</html>

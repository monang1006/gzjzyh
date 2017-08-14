<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<title>日程组件</title>
		<%@include file="/common/include/meta.jsp"%>
	<LINK href="<%=frameroot%>/css/calendar_properties.css" type=text/css rel=stylesheet>

		<link rel="stylesheet" href="<%=path%>/oa/css/calendar/calendar.css"
			type="text/css">
		<link rel="stylesheet" href="<%=path%>/oa/css/calendar/window.css"
			type="text/css">
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/scriptaculous.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/spinelz_util.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/resize.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz/calendar.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz/window.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz/window_resizeEx.js"
			type="text/javascript"></script>

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
	</head>
	<body id="calbody">
		<div style="font-size: 14px; height: 250px; margin: 0px;">
			<input type="hidden" id="insetDay" value="${setDate}">
			<div id="calendar1" style="margin-bottom: 50px;"></div>
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
          dblclickSchedule:   function(schedule) {		//双击事务图标执行 函数
          	var url = "<%=path%>/calendar/calendar!readonly.action?calendarId="+schedule.id;
			var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:600px;help:no;status:no;scroll:no');
			if("reload"==a){
				location = "<%=path%>/calendar/calendar!viewpage.action"
			}
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
   
   
   //选中日期
   function selectInSmall(date){
   	 var setDate = date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
   	 window.parent.parent.refreshWorkByTitle('calendar/calendar.action?setDate='+setDate, '个人日程');
   	 //window.parent.parent.refreshWorkByTitle('calendar/calendar!viewpage.action?setDate='+todate, '个人日程');
   }
   
   function setSchedules(startD,endD){
   	}
   	
   	function nextMonth(startDate,endDate,type){
   		//alert("月初 = "+startDate+"\n 月末 = "+endDate+"\n type = "+type);
   		location = "<%=path%>/calendar/calendar!desktop.action?termStart="+startDate+"&termEnd="+endDate;
   	}
   
   	function mouseoverToShow(date,element,event){
		parent.mouseoverToShow(date,element,event);
   	}
   	
   	function displaynone(ev){
   		parent.document.getElementById('detail').style.display = "none"; 
   	}
   	document.onmousewheel = displaynone;
   	parent.document.getElementById("contentborder").document.onmousewheel = displaynone;
   	document.body.onclick = displaynone;
   	parent.document.body.onclick = displaynone;
   	parent.parent.document.body.onclick = displaynone;
   	parent.parent.parent.document.body.onclick = displaynone;
  </script>

			<s:form>
				<label id="message" style="display: none">
					<s:actionmessage />
				</label>
			</s:form>

			<div id="addwindow"
				style="c border:5px; display:none; position: absolute; background-color:#FFFF99;">
			</div>
			<div id="divResult" class="divResult" style="display: none"></div>
	</body>
</html>

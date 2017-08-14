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
	<script src="<%=path%>/oa/js/calendar/spinelz_lib/scriptaculous.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/calendar/spinelz_lib/spinelz_util.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/calendar/spinelz_lib/resize.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/workLog/calendar.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/calendar/spinelz/window.js" type="text/javascript"></script>
	<script src="<%=path%>/oa/js/calendar/spinelz/window_resizeEx.js" type="text/javascript"></script>
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
<body >
  <div style="font-size: 14px; height: 250px; margin-bottom: 20px;">
  <input type="hidden" id="insetDay" value="${setDate}"> 
  <input type="hidden" id="inputType" value="${inputType}"> 
  <div id="calendar1" style="margin-bottom: 50px; margin-right: 3px;"></div>
  <script type="text/javascript">
  		var nowDate="${setDate}";
     	//初始化事务
     	function setSchedules(startD,endD){
	   		var myAjax = new Ajax.Request(
	                 scriptroot+'/worklog/workLog!changeWorkLog.action', // 请求的URL
	                {
	                    parameters : 'setDate='+startD,
	                    method:  'post', 
	                    onComplete: function(response){
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
         	initDate: today,		//初始化当前时间
         	displayTime:        [{hour: 6, min: 50}, {hour: 23, min: 35}],
         	displayType:        inputType.value,			//视图显示模式(month,week,day)
         	weekIndex:          0,
         	holidays:           [],
         	schedules:          [],		//定义事务数据
         	dblclickSchedule:   function(schedule) {		//双击事务图标执行 函数
	         	addwindow.style.display='none'
	         	var calId = schedule.id;
	         	var url = "<%=path%>/worklog/workLog!input.action?setDate="+nowDate+"&operateType=viewEdit&workLogId="+calId+"&timestamp="+new Date();
		  		var a = window.showModalDialog(url,window,'dialogWidth:850px;dialogHeight:650px;help:no;status:no;scroll:no'); 
		  		if(undefined!=a){
		  		
					if(a.indexOf("reload")==0){
						a = a.replace("reload","");
						var activ = a.split(",");
						var actId = activ[0];
						var actName = activ[1];
						parent.document.getElementById("cal_page_title").innerText ="日程安排 （"+ actName+"）";
						return;
					}else if(a=="OK"){
						//window.dialogArguments.submitForm();
						window.parent.location.reload();
						//location = "<%=path%>/fileNameRedirectAction.action?toPage=worklog/workLog-list.jsp";
					}
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
			alert("请在视图中选中要添加的时间段！");      
       		return;
      }
      if (calendar.options.displayType == 'month') {
        	term[0].setHours(12);
        	term[1].setHours(14);
      }
      var calStartTime = term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate()+" "+term[0].getHours()+":"+term[0].getMinutes();
      var calEndTime = term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate()+" "+term[1].getHours()+":"+term[1].getMinutes();
	 //添加事务
      var myAjax = new Ajax.Request(
                 '<%=path%>/worklog/workLog!add.action', // 请求的URL
                {
                    //参数
                    parameters : 'model.wlogTitle='+encodeURI(encodeURI($('wlogTitle').value))+'&startTime='+calStartTime+'&endTime='+calEndTime,
                   
                    // 使用GET方式发送HTTP请求
                    method:  'post', 
                    // 指定请求成功完成时需要执行的js方法
                    onComplete: function(response){
                    	alert(response);
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
		 	// 绘制视图事物
		     var schedule = {
		        id:          $(JSONobj).id,
		        description: $('wlogTitle').value
		     };
		     schedule.start =  {
		          	year:  term[0].getFullYear(),
		          	month: term[0].getMonth(),
		          	day:   term[0].getDate(),
		          	hour:  '0',
		         	min:   '0'
		      }
		      schedule.finish = {
			       	year:  term[0].getFullYear(),
		          	month: term[0].getMonth(),
		          	day:   term[0].getDate(),
		          	hour:  '0',
		         	min:   '0'
		      }
		      schedule.publicity=true;
		      calendar.addSchedule(schedule);
		      calendar.clearSelected();
	      }
        
    function addTask(){
    	var term = calendar.getSelectedTerm();//(term="DateStart,DateEnd")开始时间到结束时间
      	if (!term){
			alert("请在视图中选中要添加的时间段！");      
       		return;
      	}
      	if (calendar.options.displayType == 'month') {
        	term[0].setHours(12);
        	term[1].setHours(14);
      	}
      	var calStartTime = term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate()+" "+term[0].getHours()+":"+term[0].getMinutes();
      	var calEndTime = term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate()+" "+term[1].getHours()+":"+term[1].getMinutes();
      	//alert("calStartTime:"+calStartTime+"&&&calEndTime:"+calEndTime);
    	parent.addTask(calStartTime,calEndTime);
    }
     
	//执行删除操作到数据库
    function deleteSchedule(scheduleId){
    	var myAjax = new Ajax.Request(
        	'<%=path%>/worklog/workLog!delete.action', // 请求的URL
             {
                 //参数
                 parameters : 'workLogId='+scheduleId,
                 // 使用GET方式发送HTTP请求
                 method:  'get', 
                 // 指定请求成功完成时需要执行的js方法
                 onComplete: function(response){
                   $('divResult').innerHTML = response.responseText||"no response text";;
                 		//alert($('divResult').innerText.trim());//alert删除成功
                 		$('divResult').innerHTML = "";
                 	}
             }
         );
    }


	//弹出快速添加窗口 
	function showAddwindow(x,y){
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
			addwindow.style.top=y;
		}else{
			addwindow.style.top=y;
		}
		addwindow.innerHTML='<TABLE CELLPADDING="2" CELLSPACING="0"  WIDTH="200" BORDER="0" BGCOLOR="#B0C4DE"><TR><TD>' 
	    + '<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0"><TR><TD STYLE="padding-top:4px;padding-bottom:4px;font:14px Arial,Verdana,Tahoma;color:#FFF8F0">快速新建</TD><td width="18"STYLE="padding-top:2px;padding-bottom:2px;font:14px Arial,Verdana,Tahoma;color:#FFF8F0" title="关闭"><img onclick="closeAddwindow();" src="<%=root%>/images/ico/shanchu.gif"/></td></TR></TABLE>' 
	    + '<TABLE CELLPADDING="2" CELLSPACING="0" WIDTH="100%" BORDER="0" BGCOLOR="#ffffff"><TR WIDTH=\"100%\">'
	    +'<TD STYLE="color:#3F3F38;font:normal 8pt Arial,Verdana,Tahoma" WIDTH=\"100%\" >'
	   // +'&nbsp;&nbsp;<font class=\"wx\">主题：</font>&nbsp;&nbsp;<input type=\"text\" id=\"wlogTitle\" WIDTH=\"100%\" name=\"model.wlogTitle\" value=\"无主题\" maxlength=\"45\">&nbsp;&nbsp;'
	   // +'</TD></TR><TR><TD>'
	   // +'&nbsp;&nbsp;<input type=\"submit\" value=\"快速新建\" class=\"input_bg\" onclick=\"addSchedule();addwindow.style.display=\'none\'\">'
	    +'<a herf="#" onclick="addTask()"><span style="font-size:14px;font-family:"宋体"">新建日志</span></a>'
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
   
 	function getCalendarDate(){
  		var idate = this.calendar.options.initDate;
		return idate.getYear()+"-"+(idate.getMonth()+1)+"-"+idate.getDate();
  	}
   
	function updateTerm(term){
		var termStart = term.start.year+"-"+(term.start.month+1)+"-"+term.start.day+" "+term.start.hour+":"+term.start.min;
		var termEnd = term.finish.year+"-"+(term.finish.month+1)+"-"+term.finish.day+" "+term.finish.hour+":"+term.finish.min;
		var myAjax = new Ajax.Request(
	                '<%=path%>/worklog/workLog!add.action', // 请求的URL
	                {
	                     //参数
                    parameters : 'workLogId='+term.id+'&startTime='+termStart+'&endTime='+termEnd,
	                    // 使用GET方式发送HTTP请求
	                     // 使用GET方式发送HTTP请求
                    method:  'post', 
                    // 指定请求成功完成时需要执行的js方法
	                    onComplete: function(response){
			                    $('divResult').innerHTML = response.responseText||"no response text";
	                    		var JSONobj = eval('('+$('divResult').innerText+')');
								//alert( $(JSONobj).msg.trim())//alert操作成功
	                    		
	                    		$('divResult').innerHTML = "";
	                    	}
	                }
	            );
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

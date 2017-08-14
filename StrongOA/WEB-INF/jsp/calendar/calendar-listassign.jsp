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
%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows.css"/>
		
		<link rel="stylesheet" href="<%=path %>/oa/css/calendar/calendar.css" type="text/css">
		<link rel="stylesheet" href="<%=path %>/oa/css/calendar/window.css" type="text/css">
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz_lib/scriptaculous.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz_lib/spinelz_util.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz_lib/resize.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz/calendar.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz/window.js" type="text/javascript"></script>
		<script src="<%=path %>/oa/js/calendar/spinelz/window_resizeEx.js" type="text/javascript"></script>
		
		<% 
		String ifLeader=request.getParameter("ifLeader");//是否领导日程 ${ifLeader }
		%>
		<TITLE>日程查看——日视图</TITLE>
		<script type="text/javascript">
		   var scriptroot='<%=path%>';
		   //跳转页面 查看某日期下的视图
		   function sd_operate(dp){
		 		var daycontent = document.getElementById("daycontent");
		 		daycontent.src = "<%=path%>/calendar/calendar!viewpage.action?ifleader=${ifleader}&setDate="+dp.cal.getDateStr();
			}
			//跳转页面 到搜索列表
			function gotoList(){
				location = "<%=path%>/calendar/calendar!searchlist.action?ifleader=${ifleader}&inputType="+"edit";
			}
			//添加新事务
			function addTask(){
				var activityId = document.getElementById("activityId").value;
				var term = daycontent.calendar.getSelectedTerm();//(term="DateStart,DateEnd")开始时间到结束时间
				var url = "";
				if (term){
				    var calStartTime = term[0].getFullYear()+"-"+(term[0].getMonth()+1)+"-"+term[0].getDate()+" "+term[0].getHours()+":"+term[0].getMinutes();
				    var calEndTime = term[1].getFullYear()+"-"+(term[1].getMonth()+1)+"-"+term[1].getDate()+" "+term[1].getHours()+":"+term[1].getMinutes();
	      
			    	url = "<%=path%>/calendar/calendar!input.action?activityId="+activityId+"&ifleader=${ifleader}&termStart="+calStartTime+"&termEnd="+calEndTime;
			      }else{
					url = "<%=path%>/calendar/calendar!input.action?activityId="+activityId+"&ifleader=${ifleader}";
			      }
				var a = window.showModalDialog(url,window,'dialogWidth:750px;dialogHeight:630px;help:no;status:no;scroll:no');
		//		if("reload"==a){
					//location = "<%=path%>/calendar/calendar.action?ifLeader=${ifLeader}";
		//			document.frames["activitylist"].document.location.reload();//分类
					//document.frames[2].document.location.reload();//视图
		//			changeActivity(activityId);
		//		}
				if(undefined!=a){
					if(a.indexOf("reload")==0){
						a = a.replace("reload","");
						var activ = a.split(",");
						var actId = activ[0];
						var actName = activ[1];
						document.frames["activitylist"].document.location.reload();//分类
						document.getElementById("cal_page_title").innerText ="日程安排 （"+ actName+"）";
						changeActivity(actId);
						return ;
					}
				}
				document.frames["activitylist"].document.location.reload();//分类
				changeActivity(activityId);
			}
			
			//查看某分类的活动
			function changeActivity(activityId){
				var frame = document.frames["daycontent"];
				if("undefined"==typeof frame.calendar){
					return ;
				}
				var displayType = frame.calendar.options.displayType;
				//var initDate = frame.calendar.options.initDate;
				var initDate = frame.getCalendarDate();
//				alert("displayType="+displayType+"\ninitDate="+initDate);
				
				var daycontent = document.getElementById("daycontent");
				daycontent.src = "<%=path%>/calendar/calendar!viewpage.action?ifleader=${ifleader}&activityId="+activityId+"&setDate="+initDate+"&inputType="+displayType;
				
				document.getElementById("activityId").value=activityId;
			}
			
			//更换日程视图 日 周 月
			function changeDisplayView(type){
				var daycontent = document.getElementById("daycontent");
				if(daycontent.calendar==undefined){
					daycontent.calendar.changeDisplayType(type);
				}else{
					return;
				}
			}
		
		</script>

		 <style type="text/css">
			body, table, tr, td,div{
			    margin:0px;
			}
			#cal, #activity{
				background-color:#cccccc;
			}
		 </style>
	</HEAD>
	<BODY class=contentbodymargin onclick="if(daycontent.addwindow!=undefined){daycontent.addwindow.style.display='none';}">
		<input type="hidden" id="activityId" value="${activityId}">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script> 
		<DIV id=contentborder >
			<table  width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr valign="top" >
						<td id="calendar"  align="center"  width="200" rowspan="3">
							<br><br><br><br><br>
							<table width="100%" id="cal" height="180px">
								<tr align="center" valign="middle">
									<td>
										<%--<strong:newdate name="small_calendar" id="small_calendar" skin="whyGreen" isplam="true" ></strong:newdate>
										--%>
										<iframe id="smallCal" src='<%=path%>/calendar/calendar!desktop.action?ifleader=${ifleader}&inputType=small'  
										frameborder="0" scrolling="no" height="200px" width="100%" align="top" style="border:0px solid #CCCCCC;">				
										</iframe>
									</td>
								</tr>
							</table>
							<br><br>
							<table id="activity" width="90%">
								<tr>
									<td>
									<iframe id="activitylist" src='<%=path%>/calendar/calendarActivity.action'  
										frameborder="0" scrolling="no" height="190px" width="100%" align="top" style="border:1px solid #CCCCCC;">				
									</iframe>
									<%--
									<td title="双击分类查看该分类下的活动">
									<s:select multiple="true"  size="8"  name="activityList" label="请选择" labelposition="top" cssStyle="width: 100%;"
										list="activitys" listKey="activityId+','+activityName" listValue="activityName" ondblclick="viewOfType(this.value);" >
									</s:select>
									<input type="button" class="input_bg"  value="新建" onclick="addActivity();">
									<input type="button" class="input_bg" value="删除" onclick="delActivity(activityList.value);">
									<input type="button" class="input_bg"  value="活动" onclick="editActivity(activityList.value);">
									--%>
									</td>
								</tr>
							</table>
						</td>
						<td valign="top"> 	
							<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr valign="top">
									<td height="40"	style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
											startColorStr=#ededed,endColorStr=#ffffff);">
											<table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
									            <td>&nbsp;</td>
									            <td id="cal_page_title" width="30%">
									            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									           		日程安排
									            </td>
									            <td width="70%">
									            <table border="0" align="right" cellpadding="00" cellspacing="0">
									                <tr>
									                	<td ><a class="Operation" href="javascript:addTask();"><img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">新建&nbsp;</a></td>
									                 	<td width="5"></td>
									                 	<td ><a class="Operation" href="javascript: daycontent.calendar.changeDisplayType('day');"><img src="<%=root%>/images/ico/cal_day.gif"   class="img_s">日历&nbsp;</a></td>
									                 	<td width="5"></td>
									                 	<td ><a class="Operation" href="javascript: daycontent.calendar.changeDisplayType('week');"><img src="<%=root%>/images/ico/cal_week.gif"  class="img_s">周历&nbsp;</a></td>
									                 	<td width="5"></td>
									                 	<td ><a class="Operation" href="javascript: daycontent.calendar.changeDisplayType('month');"><img src="<%=root%>/images/ico/cal_month.gif"  class="img_s">月历&nbsp;</a></td>
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
						</td> 	
					</tr>
					<tr>
						<td>
							<iframe id="daycontent" src=''  
								frameborder="0" scrolling="no" height="990px" width="100%" align="top" style="border:4px solid #CCCCCC;">				
							</iframe>
							<script type="text/javascript">
							var sdate = "${setDate}";
							var daycon = document.getElementById("daycontent");
							if(sdate!=null&&sdate!=""){
									daycon.src = "<%=path%>/calendar/calendar!viewpage.action?ifleader=${ifleader}&setDate="+sdate;
							}else{
									daycon.src = "<%=path%>/calendar/calendar!viewpage.action?ifleader=${ifleader}";
							}
							</script>
						</td>
					</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

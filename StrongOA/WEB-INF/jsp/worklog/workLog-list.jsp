<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows_add.css" />

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
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src="<%=path%>/common/js/common/common.js"></script>
		<TITLE>工作日志查看——日视图</TITLE>
		<script type="text/javascript">
	   var scriptroot='<%=path%>';
	   
	   //跳转页面 查看某日期下的视图
	   function sd_operate(dp){
	 		var daycontent = document.getElementById("daycontent");
	 		daycontent.src = "<%=path%>/worklog/workLog!viewpage.action?setDate="+dp.cal.getDateStr();
		}
		//跳转页面 到搜索列表
		function gotoList(){
			location = "<%=path%>/worklog/workLog.action";
		}
		
		//添加新事务
		function addTask(startTime,endTime){
			var setDate = window.frames[0].getCurrentMonth();	//获取当前时间
			var url = "<%=path%>/worklog/workLog!input.action?operateType=viewAdd&setDate="+setDate+"&startTime="+startTime+"&endTime="+endTime;			
			var a= OpenWindow(url,650,500,window);
			var sdata=startTime.substring(0,9);
			//alert(sdata);
			if(a=="OK"){
				//document.getElementById("daycontent").src= "<%=path%>/worklog/workLog!viewpage.action?setDate="+sdata;
				var daycon = document.getElementById("daycontent");
				daycon.src = "<%=path%>/worklog/workLog!viewpage.action?setDate="+sdata;
			}
		} 
		
</script>

		<style type="text/css">
body,table,tr,td,div {
	margin: 0px;
}

#cal,#activity {
	background-color: #cccccc;
}
</style>
	</HEAD>
	<BODY class=contentbodymargin
		onclick="if(daycontent.addwindow!=undefined){daycontent.addwindow.style.display='none';}">
		<input type="hidden" id="activityId" value="${activityId}">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr valign="top">
					<td id="calendar" align="center" width="200" rowspan="3">
						<table  border="0" cellpadding="0" cellspacing="0">
							<%--<tr height="40"
								style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
								<td>
								&nbsp;
								</td>
								<td id="cal_page_title" width="90%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									工作日志视图
								</td>
							</tr>
							--%><tr align="center" valign="middle">
								<td colspan="2">
									<iframe id="smallCal"
										src='<%=path%>/worklog/workLog!desktop.action'
										frameborder="0" scrolling="no" height="110px" width="100%"
										align="top" style="border: 0px solid #CCCCCC;">
									</iframe>
								</td>
							</tr>
							</table>
							<table>
							<%--<tr>
								<td align="center" >
									<a class="Operation" href="javascript:addTask('','');"><img
											src="<%=root%>/images/ico/tianjia.gif" width="14"
											height="14" class="img_s">新建工作日志&nbsp;</a>
								<a class="Operation" href="javascript: gotoList();"><img
											src="<%=root%>/images/ico/cal_list.gif" class="img_s">日志列表明细&nbsp;</a>
								
								</td>
							</tr>
							
							--%><tr><td>
								<a  href="#" class="button6" onclick="addTask('','');">新建工作日志</a>&nbsp;
							<a   href="#" class="button6" onclick="gotoList();">日志列表明细</a>&nbsp;
								</td></tr>	
						</table>
					</td>
				</tr>

                <tr>			
								<td colspan="3" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
												<tr>
													<td class="table_headtd_img" >
														<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
													</td>
													<td align="left">
														<strong >工作日志视图</strong>
													</td>
													<%--<td align="right">
														<table border="0" align="right" cellpadding="00" cellspacing="0">
												            <tr>
												            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="addTask();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="daycontent.calendar.changeDisplayType('day');"><img src="<%=root%>/images/operationbtn/cal_day.png"/>&nbsp;日&nbsp;历&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="daycontent.calendar.changeDisplayType('week');"><img src="<%=root%>/images/operationbtn/cal_week.png"/>&nbsp;周&nbsp;历&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="daycontent.calendar.changeDisplayType('month');"><img src="<%=root%>/images/operationbtn/cal_month.png"/>&nbsp;月&nbsp;历&nbsp;</td>
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
												--%></tr>
											</table>
										</td>
									</tr>






				<tr>
				<td>
						<iframe id="daycontent" src='' frameborder="0" scrolling="no"
							height="650px" width="100%" align="top"
							style="border: 4px solid #CCCCCC;">
						</iframe>
						<script type="text/javascript">
							var sdate = "${setDate}";
							var daycon = document.getElementById("daycontent");
							if(sdate!=null&&sdate!=""){
								daycon.src = "<%=path%>/worklog/workLog!viewpage.action?setDate="+sdate;
							}else{
								daycon.src = "<%=path%>/worklog/workLog!viewpage.action?";
							}
							
							function refreshDayContent(sdate){
								var daycon = document.getElementById("daycontent");
								daycon.src = "<%=path%>/worklog/workLog!viewpage.action?setDate="+sdate;
							}
						</script>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

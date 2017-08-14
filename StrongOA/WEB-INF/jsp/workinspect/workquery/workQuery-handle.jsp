<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<HTML>
	<HEAD>
		<TITLE>我的办理件查询__已不用</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<%
			String reportType = (String) request.getParameter("reportType");
		%>
		<SCRIPT type="text/javascript">
		//统计方法
		function query(){
		   $("#reporttongji").val("1");
		   window.parent.onsub("",$("#reportType").val(),$("#worktaskTitle").val(),$("#worktaskUnitName").val(),$("#worktaskNo").val(),$("#managetState").val(),$("#worktaskUser").val(),$("#worktaskEmerlevel").val(),$("#worktaskEtime").val(),$("#startTime").val(),$("#endTime").val(),0);
		}	
	</SCRIPT>
	</HEAD>
	<body class="contentbodymargin"
		style="overflow: hidden; BACKGROUND: #ffffff">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%"
					style="FILTER: progid :DXImageTransform.Microsoft.Gradient (gradientType = 0, startColorStr =#ededed, endColorStr =#ffffff );">
					<s:form id="myTableForm"
						action="/workinspect/workquery/workQuery!getBorrowReport.action">
						<input type="hidden" id="exportType" name=exportType />
						<input type="hidden" id="reporttongji" name="reporttingji" />
						<input type="hidden" id="reportType" name="reportType"
							value="<%=reportType%>" />
						<table width="100%" height="25">
							<tr>
								<td width="50%">
									&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 任务标题:
									<input typte="text" id="worktaskTitle" name="worktaskTitle"
										value="${worktaskTitle}" style="width: 250px">
							</tr>
							<td width="50%">
								发起单位:
								<input typte="text" id="worktaskUnitName"
									name="worktaskUnitName" value="${worktaskUnitName}"
									style="width: 250px">
							</td>
							</tr>
							<tr>
								<td width="50%">
									&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 任务编号:
									<input typte="text" id="worktaskNo" name="worktaskNo"
										value="${worktaskNo}" style="width: 250px">
								</td>
								<td width="50%">
									工作状态:
									<s:select name="managetState" id="managetState"
										list="#{'':'全部','0':'待签收','1':'办理中','2':'已办结'}" listKey="key"
										listValue="value" cssStyle="width:100px" />
								</td>
							</tr>
							<tr>
								<td width="50%">
									&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
									&nbsp;&nbsp;&nbsp; 发送人:
									<input typte="text" id="worktaskUser" name="worktaskUser"
										value="${worktaskUser}" style="width: 250px">
								</td>
								<td width="50%">
									紧急程度:
									<s:select name="worktaskEmerlevel" id="worktaskEmerlevel"
										list="#{'':'全部','0':'紧急','1':'快速','2':'普通'}" listKey="key"
										listValue="value" cssStyle="width:100px" />
								</td>
							</tr>
							<tr>
								<td width="50%">
									&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 办理时限:
									<strong:newdate id="worktaskEtime" width="120px;"
										name="worktaskEtime" dateform="yyyy-MM-dd" isicon="true"
										dateobj="<%=new Date() %>" title="办理时限" />
								</td>
								<td width="50%">
									发送时间:
									<strong:newdate id="startTime" width="120px;" name="startTime"
										dateform="yyyy-MM-dd" isicon="true" dateobj="<%=new Date() %>"
										title="开始时间" />
									<strong:newdate id="endTime" width="120px;" name="endTime"
										dateform="yyyy-MM-dd" isicon="true" dateobj="<%=new Date() %>"
										title="结束日期" />
								</td>
								<tr width="20%">
									<td align="right">
										<input id="column" name="column" type="button"
											class="input_bg" value="查询" onclick="query()"
											style="width: 60px">
									</td>
								</tr>
						</table>
					</s:form>
					<iframe id="annexFrame" style="display: none"></iframe>
				</td>
			</tr>
		</table>
	</body>
</HTML>
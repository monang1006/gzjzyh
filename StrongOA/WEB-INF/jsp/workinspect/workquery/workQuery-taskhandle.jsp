<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<HTML>
	<HEAD>
		<TITLE>办理件情况统计情况（未用到！！）</TITLE>
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
		   window.parent.onsub("",$("#reportType").val(),$("#org").val(),0);
		}	
	</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow: hidden; BACKGROUND: #ffffff">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%"
					style="FILTER: progid :DXImageTransform.Microsoft.Gradient (gradientType = 0, startColorStr =#ededed, endColorStr =#ffffff );">
					<s:form id="myTableForm"
						action="/workinspect/workquery/workQuery!getTaskHadle.action">
						<input type="hidden" id="exportType" name=exportType />
						<input type="hidden" id="reporttongji" name="reporttingji" />
						<input type="hidden" id="reportType" name="reportType"
							value="<%=reportType%>" />
						<table width="100%" height="15">
							<tr>
								<td width="35%">
									<span class="wz">科室名称：</span>
									<select id="yj" style="width:150px;">
									<option value="0">&lt;选择科室&gt;</option>
									<s:iterator value="orgList">
										<option value="${orgName }">${orgName }</option>
									</s:iterator>
									</select>
								</td>
								<td>
										<input id="column" name="column" type="button"
											class="input_bg" value="查询" onclick="query()"
											style="width: 60px">
									</td>
						</table>
					</s:form>
					<iframe id="annexFrame" style="display: none"></iframe>
				</td>
			</tr>
		</table>
	</body>
</HTML>
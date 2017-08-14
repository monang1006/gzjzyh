<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
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
		       String reportType=(String)request.getParameter("reportType");
			
			
			 %>
		<SCRIPT type="text/javascript">
	
		
		
		
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow:hidden;BACKGROUND:#ffffff">
			<script language="javascript" type="text/javascript" src="<%=path%>/common/script/My97DatePicker-4.72/WdatePicker.js"></script>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="">
							<input type="hidden" id="exportType" name=exportType />
							<input type="hidden" id="reporttongji" name="reporttingji"/>
							<input type="hidden" id="reportType" name="reportType" value="<%=reportType %>"/>
							<table width="100%" height="25">
								<tr>
									
									<td align="left">
								<%--<s:select name="reportType" id="reportType"
										list="#{'0':'出差考勤记录','1':'培训出差记录','2':'出差伙食补贴','3':'笔记本补贴'}"
										listKey="key" listValue="value"/>
								    统计日期：
								    --%>
								   机构:<input typte="text"  id="person" name="person" value="${person}" style="width:120px">
							 	   部门：<input typte="text"  id="ownerperson" name="ownerperson" value="${ownerperson}" style="width:120px">
							 	  </tr>
							 	  <tr>
							 	   <td  align="left">
									<input id="column" name="column" type="button"
											class="input_bg" value="查询" onclick="query()" style="width:60px">
									<input id="column" name="column" type="button"
											class="input_bg" value="取消" onclick="query()" style="width:60px">
									</td>
									</tr>
						</table>
						</s:form>
							<iframe id="annexFrame" style="display:none"></iframe>
					</td>
				</tr>
			</table>
	</body>
</HTML>
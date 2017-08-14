<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title></title>
		<link href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
			<%
			String type=request.getParameter("type");
			%>
	</head>
	<script type="text/javascript">
			function submitForm(tables,condition,dictCode){
				document.getElementById("tables").value=tables;
				document.getElementById("condition").value=condition;
				document.getElementById("dictCode").value=dictCode;
				document.getElementById("myTableForm").submit();
			}
	</script>
	<BODY class=contentbodymargin  oncontextmenu="return false;">
			<DIV id=contentborder align=center>
			<table>
				<tr>
					<td>
						
					</TD>
				</tr>
				<tr>
					<td></td>
				</tr>
				<s:form id="myTableForm"
					action="/personnel/baseperson/person!getDutyDetails.action">
					<input type="hidden" id="tables" name="tables" value="${tables}" />
					<input type="hidden" id="condition" name="condition"
						value="${condition}" />
					<input type="hidden" id="dictCode" name="dictCode"
						value="${dictCode}" />
				</s:form>
				<tr>
					<td>
						<%
							if("statistic".equals(type)){
						%>
						请选择统计记录！
						<%
						}else{%>
						请选择人员！
						<%} %>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

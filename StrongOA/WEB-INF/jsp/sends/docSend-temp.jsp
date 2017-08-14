<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<title>打印密码</title>
		<script type="text/javascript">
		$(document).ready(function(){
		alert("保存成功！");
		document.getElementById("myForm").submit();
    });
		</script>
	</head>
	<body class=contentbodymargin>
	<s:form id="myForm" action="/sends/docSend!printPassword.action">
	</s:form>
	</body>
</html>

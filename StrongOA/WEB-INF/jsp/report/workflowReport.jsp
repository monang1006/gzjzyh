<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="strong" uri="/tags/web-workflow"%>
<%--<%@include file="/common/include/rootPath.jsp"%>--%>
<html>
	<head>
		<title>流程报表</title>
		<%@include file="/common/include/meta.jsp"%>
		<%--<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>--%>
	</head>
	<body>
		<strong:workflow title="流程报表" isPop="false" href="/report/reportDefine!query.action" typeList="${reportSortList}" workflowMap="${reportMap}"/>
	</body>
</html>

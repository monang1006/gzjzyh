<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<script>
	window.opener.location = "<%=root%>/workflowDesign/action/processFile.action";
	window.close();
</script>
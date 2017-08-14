<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<script type="text/javascript">
			window.dialogArguments.location = "<%=path%>/config/action/workflowConfig.action";
			window.close();
		</script>
	</head>
</html>

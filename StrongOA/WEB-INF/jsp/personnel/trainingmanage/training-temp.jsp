<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
<HEAD>
	<%@include file="/common/include/meta.jsp"%>
	<TITLE>操作内容</TITLE>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
</HEAD>
<BODY>
<SCRIPT>
	// alert("保存成功");
	parent.location = "<%=root%>/fileNameRedirectAction.action?toPage=personnel/trainingmanage/columnContent.jsp";
</SCRIPT>
</BODY>
</HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ page import="java.util.*"%>
<HTML><HEAD><TITLE>操作内容</TITLE>
</HEAD>
<BODY>
<SCRIPT>
	var redtempGroupId="${redtempGroupId}";
	window.location="<%=path%>/docredtemplate/docreditem/docRedItem.action?redtempGroupId="+redtempGroupId;
</SCRIPT>
</BODY>
</HTML>

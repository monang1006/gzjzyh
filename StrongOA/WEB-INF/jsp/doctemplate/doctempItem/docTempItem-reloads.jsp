<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ page import="java.util.*"%>
<HTML><HEAD><TITLE>操作内容</TITLE>
</HEAD>
<BODY>
<SCRIPT>
	var docgroupId="${docgroupId}";
	window.location="<%=path%>/doctemplate/doctempItem/docTempItem.action?docgroupId="+docgroupId;
</SCRIPT>
</BODY>
</HTML>

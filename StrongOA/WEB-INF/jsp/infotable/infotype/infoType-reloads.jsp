<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<BODY>
		<SCRIPT>
			var infoSetCode="${infoSetCode}";
			window.location="<%=path%>/infotable/infotype/infoType.action?infoSetCode="+infoSetParentid;
		</SCRIPT>
	</BODY>
</HTML>

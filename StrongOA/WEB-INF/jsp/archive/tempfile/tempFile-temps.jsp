<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<SCRIPT LANGUAGE="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
	<%
		String folderId=request.getParameter("folderId");
	 %>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
	</HEAD>
	<BODY>
		<DIV id="msgdiv" style="display: none;">
			<s:actionmessage theme="simple" />
		</DIV>
<%--		<input id="folderId" name="folderId" type="hidden" size="30" value="${folderId}">--%>
	<SCRIPT>
	location="<%=path%>/archive/archivefolder/archiveFolder!input.action?forward=viewFile&folderId=<%=folderId%>";
</SCRIPT>
	</BODY>
</HTML>

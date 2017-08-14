<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@include file="/common/include/meta.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<HTML><HEAD><TITLE>操作内容</TITLE>
</HEAD>
<BODY>
<DIV id="msgdiv" style="display: none;"><s:actionmessage theme="simple"/></DIV>
<SCRIPT>
<%--	alert(document.getElementById("msgdiv").children[0].children[0].children[0].innerHTML);--%>
	window.dialogArguments.submitForm();
	window.close();
</SCRIPT>
</BODY>
</HTML>

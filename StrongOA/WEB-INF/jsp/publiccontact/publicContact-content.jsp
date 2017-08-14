<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>公共联系人管理</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="20%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME id="publiccontact_tree" name="publiccontact_tree" marginWidth="0" marginHeight="0" src="<%=path%>/publiccontact/publicContact!tree.action" frameBorder="0" scrolling="no">
		<FRAME id="publiccontact_content" name="publiccontact_content" marginWidth="0" marginHeight="0" src="<%=path%>/publiccontact/publicContact.action" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>

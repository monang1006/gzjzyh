<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>邮件管理容器</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="20%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="mail_manager_tree" marginWidth="0" marginHeight="0" src="<%=root %>/mymail/mailBox.action" frameBorder="0" scrolling="no">
		<FRAME name="mail_content" marginWidth="0" marginHeight="0" src="fileNameRedirectAction.action?toPage=mymail/getmail_container.jsp" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>


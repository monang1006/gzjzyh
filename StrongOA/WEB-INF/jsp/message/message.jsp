<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>消息管理容器</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="17%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="msg_manager_tree" marginWidth="0" marginHeight="0"  src="<%=path%>/message/messageFolder.action" frameBorder="0" scrolling="no">
		<FRAME name="msg_content" marginWidth="0" marginHeight="0" float="left" src="fileNameRedirectAction.action?toPage=message/message-content.jsp" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>

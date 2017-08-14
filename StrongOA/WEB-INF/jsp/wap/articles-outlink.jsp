<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>外部页面链接</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="100%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="mail_manager_tree" marginWidth="0" marginHeight="0" src="${outlink }" frameBorder="0" scrolling="yes">
	</FRAMESET>
	<noframes></noframes>
</html>
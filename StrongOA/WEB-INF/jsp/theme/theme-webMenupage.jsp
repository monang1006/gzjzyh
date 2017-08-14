<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>

<% 
	String privCode = request.getParameter("privCode");// 资源权限CODE

%>
<html>
	<head>
		<title>消息管理容器</title>
	</head>
	<FRAMESET id="container" border="1" frameSpacing="1" cols="180,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="privil_tree" marginWidth="0" marginHeight="0" src="<%=basePath%>/theme/theme!gotoBgtMenutree.action?privCode=<%=privCode%>" frameBorder="0" scrolling="no">
		<FRAME name="privil_content" marginWidth="0" marginHeight="0" src="<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-webMenuContent.jsp" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>

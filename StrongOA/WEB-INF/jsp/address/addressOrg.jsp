<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>系统通讯录</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="17%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="project_work_tree" marginWidth="0" marginHeight="0" src="<%=root%>/address/addressOrg.action" frameBorder="0" scrolling="no">
		<FRAME name="project_work_content" marginWidth="0" marginHeight="0" src="<%=root%>/address/addressOrg!orguserlist.action?usershow=1"  frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>
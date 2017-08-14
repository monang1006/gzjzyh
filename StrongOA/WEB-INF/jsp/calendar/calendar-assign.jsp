<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>日程管理_分配日程权限</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="22%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="assignmain_usertree" marginWidth="0" marginHeight="0" src="<%=path%>/calendar/calendar!newtree.action" frameBorder="0" scrolling="no">
		<FRAME name="assignmain_content" marginWidth="0" marginHeight="0" src="<%=path%>/calendar/calendar!assignUserList.action?treeUserId=" frameBorder="0" scrolling="auto">
	</FRAMESET>
	<noframes></noframes>
</html>

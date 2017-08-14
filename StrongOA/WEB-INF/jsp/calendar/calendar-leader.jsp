<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>日程管理_查看领导日程</title>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="15%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="cal_usertree" marginWidth="0" marginHeight="0" src="<%=path%>/calendar/calendar!leaderList.action" frameBorder="0" scrolling="no">
		<FRAME name="cal_content" marginWidth="0" marginHeight="0" src="<%=path%>/calendar/calendar!viewleader.action?inputType=leader" frameBorder="0" scrolling="auto">
	</FRAMESET>
	<noframes></noframes>
</html>

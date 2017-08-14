<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>选择车辆</title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	</head>
	<FRAMESET id=container  border=0 frameSpacing=0 rows="100%" frameBorder=0>
	<%--	<FRAME name=personal_status_toolbar marginWidth=0 marginHeight=0 
			src="<%=path %>/fileNameRedirectAction.action?toPage=car/car-carsearchbar.jsp" frameBorder=0 noResize scrolling=no>
	--%>
		<FRAME name=personal_status_content marginWidth=0 marginHeight=0
			src="<%=path %>/fileNameRedirectAction.action?toPage=car/car-status_frame.jsp" frameBorder=0 noResize scrolling=no>
	</FRAMESET>
	<noframes></noframes>

<%--
	<FRAMESET border="1" frameSpacing="1" cols="20%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="cal_usertree" marginWidth="0" marginHeight="0" src="<%=path%>/meetingroom/meetingroom!selectRoom.action" frameBorder="0" scrolling="no">
		<FRAME name="cal_content" marginWidth="0" marginHeight="0" src="<%=path%>/meetingroom/meetingroom!selectCalender.action" frameBorder="0" scrolling="auto">
	</FRAMESET>
	<noframes></noframes>
--%>
</html>
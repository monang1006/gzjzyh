<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>车辆使用 日程视图</title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	</head>
	<FRAMESET border=0 frameSpacing=0 rows="30,*" frameBorder=0>
		<FRAME name=personal_status_toolbar marginWidth=0 marginHeight=0
			src="<%=path %>/fileNameRedirectAction.action?toPage=car/car-status_toolbar.jsp" frameBorder=0 noResize scrolling=no>
		<%--<FRAME name=personal_status_content marginWidth=0 marginHeight=0
			src="<%=path %>/fileNameRedirectAction.action?toPage=car/car-selectview.jsp" frameBorder=0 noResize scrolling=auto>
	--%>
		<FRAME name=personal_status_content marginWidth=0 marginHeight=0
			src="<%=path %>/car/car!selecttree.action" frameBorder=0 noResize scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</html>

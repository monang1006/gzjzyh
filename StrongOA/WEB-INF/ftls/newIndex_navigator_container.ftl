﻿<!--工作区框架-->
<!--newIndex_navigator_container.ftl-->
<!--首页/主框架工作区/工作区框架-->
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<HEAD>
		<TITLE>导航器容器</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET border=0 frameSpacing=0 rows=100%,* frameBorder=0>
	    <!--工作区框架-主工作区-->
		<FRAME name=navigator_toolbar marginWidth=0 marginHeight=0
			src="${S1}"
			frameBorder=0 noResize scrolling=no>
		<!--底部-->
		<FRAME name=navigator_content marginWidth=0 marginHeight=0
<%--			src="<%=path%>/fileNameRedirectAction.action?toPage=frame/perspective_content/navigator_container/navigator_content.jsp" --%>
			src="${S2}"
<%--			src=""--%>
			frameBorder=0 noResize scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</html>

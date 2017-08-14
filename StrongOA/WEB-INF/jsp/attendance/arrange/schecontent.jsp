<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>Future页面内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<%String moduletype = request.getParameter("moduletype");

			%>
	</HEAD>
	<FRAMESET rows=50%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=shceGroup marginWidth=0 marginHeight=0 src="<%=path%>/attendance/arrange/scheGroup.action" frameBorder=0 scrolling=no>	
		<FRAME name=schedules marginWidth=0 marginHeight=0 src="<%=path%>/fileNameRedirectAction.action?toPage=/attendance/arrange/temp.jsp" frameBorder=0 scrolling=no>	
	</FRAMESET>
	<noframes></noframes>
</HTML>

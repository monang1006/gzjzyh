<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<!-- saved from url=(0067)http://192.168.2.83:8080/chinaspis/personal/perspective_content.jsp -->
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=15%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=recordTree marginWidth=0 marginHeight=0
			src="<%=root%>/attendance/report/attendReport!orgTree.action?reportType=${param.reportType }" frameBorder=0 scrolling=no>
		<FRAME name=recordList marginWidth=0 marginHeight=0
			src="<%=root%>/fileNameRedirectAction.action?toPage=attendance/report/attendReport-content.jsp?reportType=${param.reportType }" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

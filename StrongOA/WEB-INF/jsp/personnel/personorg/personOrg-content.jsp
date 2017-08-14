<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<!-- saved from url=(0067)http://192.168.2.83:8080/chinaspis/personal/perspective_content.jsp -->
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=organiseTree marginWidth=0 marginHeight=0
			src="<%=path%>/personnel/personorg/personOrg!tree.action" frameBorder=0 scrolling=no>
		<FRAME name=organiseInfo marginWidth=0 marginHeight=0
			src="<%=path%>/personnel/personorg/personOrg!addchild.action?orgSysCode=""" 
			frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

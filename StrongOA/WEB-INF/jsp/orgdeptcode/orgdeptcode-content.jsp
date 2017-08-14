<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=userTree marginWidth=0 marginHeight=0
			src="<%=path%>/orgdeptcode/orgdeptcode!tree.action" frameBorder=0 scrolling=no>
		<FRAME name=userlist marginWidth=0 marginHeight=0
			src="<%=path%>/orgdeptcode/orgdeptcode.action" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

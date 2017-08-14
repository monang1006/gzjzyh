<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
	<HEAD>
		<TITLE>HelpEdit</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="yes" bordercolor="#d4d0c7">
		<FRAME name=HelpTree marginWidth=0 marginHeight=0
			src="<%=path %>/helpedit/helpedit!helptree.action" frameBorder=0 scrolling=no>
		<FRAME name=HelpContent marginWidth=0 marginHeight=0
			src="<%=path %>/helpedit/helpedit!input.action?helpTreeId=402882101eba1e2f011eba6482540001" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

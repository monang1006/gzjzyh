<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
	<HEAD>
	
	<% 
	  String id=(String)request.getParameter("personId");
%>
	<TITLE>老干部基本信息</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET rows=45%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name="veteranPersonInfo" marginWidth=0 marginHeight=0
			src="<%=path%>/personnel/veteranmanage/veteran!view.action?personId=<%=id %>" 
			frameBorder=0 scrolling=no>
		<FRAME name="veteranPersonRegards" marginWidth=0 marginHeight=0
			src="<%=path%>/personnel/veteranmanage/veteran!regardList.action?personId=<%=id %>" 
			frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<!-- saved from url=(0067)http://192.168.2.83:8080/chinaspis/personal/perspective_content.jsp -->
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	<%
			String orgId=request.getParameter("orgId");
			String personId=request.getParameter("personId");
			String keyid=request.getParameter("keyid");
	 %>
	</HEAD>
	<FRAMESET rows=56%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=baseInfo marginWidth=0 marginHeight=0 
			src="<%=path%>/personnel/baseperson/person!initEditPerson.action?forward=view&orgId=<%=orgId%>&personId=<%=personId%>&infoSetCode=40288239230c361b01230c7a60f10015&keyid=<%=keyid%>"; 
			frameBorder=0 scrolling=no>
		<FRAME name=relationInfo marginWidth=0 marginHeight=0
			src="<%=path%>/personnel/baseperson/person!initViewAddTool.action?forward=initviewedit&orgId=<%=orgId%>&personId=<%=personId%>";
			frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>


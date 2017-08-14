
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>

<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<%
	     String conferId =(String) request.getAttribute("conferId");// 会议ID
	    if(conferId!=null && !conferId.equals("")){
	  %>
	  <FRAMESET cols=0%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=propertyTree marginWidth=0 marginHeight=0
			src="" 
			frameBorder=0 scrolling="auto">
		<FRAME name=leaveInfo marginWidth=0 marginHeight=0
			src="<%=path%>/leave/leave!content.action?conId=<%=conferId %>" 
			frameBorder=0 scrolling=no>
	</FRAMESET>
	  <%   
	    }else{
	 %>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=propertyTree marginWidth=0 marginHeight=0
			
			src="<%=path%>/fileNameRedirectAction.action?toPage=leave/leave-tree.jsp"
			frameBorder=0 scrolling="auto">
		<FRAME name=leaveInfo marginWidth=0 marginHeight=0
			src="<%=path%>/leave/leave!content.action" 
			frameBorder=0 scrolling=no>
	</FRAMESET>
	<%}
	 %>
	<noframes></noframes>
</HTML>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>选择归入的案卷</TITLE>
		
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
	<% String modeuletype=request.getParameter("moduletype");
	   String forward=request.getParameter("forward");
	     out.print(
	     request.getParameter("orgId"));
	 if("pige".equals(modeuletype)){%>
		<FRAME name=folderSortTree marginWidth=0 marginHeight=0
			src="<%=path%>/archive/sort/archiveSort!tree.action?forwardStr=filetree&orgId=<%=request.getParameter("orgId") %>"
			frameBorder=0 scrolling=no>
			<%}else if("pige1".equals(modeuletype)){%>
			<FRAME name=folderSortTree marginWidth=0 marginHeight=0
			src="<%=path%>/archive/sort/archiveSort!tree.action?forwardStr=filetree1&orgId=<%=request.getParameter("orgId") %>"
			frameBorder=0 scrolling=no>
			<%}else{%>
			<FRAME name=folderSortTree marginWidth=0 marginHeight=0
			src="<%=path%>/archive/sort/archiveSort!tree.action?forwardStr=filepigetree&orgId=<%=request.getParameter("orgId") %>"
			frameBorder=0 scrolling=no>
			<%} %>
	<%if("selected".equals(forward)){%>
	    <FRAME name=folderSortList marginWidth=0 marginHeight=0
			src="<%=path%>/archive/archivefolder/archiveFolder.action?forward=selected&moduletype=<%=request.getParameter("moduletype") %>&orgId=<%=request.getParameter("orgId") %>"
			frameBorder=0 scrolling=no>
			<%}else{%>
		<FRAME name=folderSortList marginWidth=0 marginHeight=0
			src="<%=path%>/archive/archivefolder/archiveFolder.action?forward=rujuan&moduletype=<%=request.getParameter("moduletype") %>&orgId=<%=request.getParameter("orgId") %>"
			frameBorder=0 scrolling=no>
			<%} %>
	</FRAMESET>
	<noframes></noframes>
</HTML>

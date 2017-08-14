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
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<%if ("manage".equals(moduletype)) {

				%>
		<FRAME name=folderSortTree marginWidth=0 marginHeight=0 src="<%=path%>/archive/sort/archiveSort!tree.action?forwardStr=fodermanagetree" frameBorder=0 scrolling=no>
		<%} else if ("searchborrowFile".equals(moduletype)) {

				%>
		<FRAME name=folderSortTree marginWidth=0 marginHeight=0 src="<%=path%>/archive/sort/archiveSort!tree.action?forwardStr=searchborrowFile" frameBorder=0 scrolling=no>
		<%} else {

				%>
		<FRAME name=folderSortTree marginWidth=0 marginHeight=0 src="<%=path%>/archive/sort/archiveSort!tree.action?forwardStr=fodertree" frameBorder=0 scrolling=no>
		<%}

			%>
		<FRAME name=folderSortList marginWidth=0 marginHeight=0 src="<%=path%>/archive/archivefolder/archiveFolder.action?moduletype=<%=moduletype%>" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

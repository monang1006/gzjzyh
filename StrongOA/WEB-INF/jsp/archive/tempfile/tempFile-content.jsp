<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<TITLE>档案管理</TITLE>
	</HEAD>
	<FRAMESET cols="20%,80%" frameborder="no" border="0" framespacing="0">
		<%--<FRAMESET rows="61px,420px" id="leftframe" frameborder="no" border="0"
			framespacing="0">--%>
			<FRAME name="project_work_choose" marginWidth=0 marginHeight=0
				src="<%=root%>/fileNameRedirectAction.action?toPage=archive/tempfile/choose.jsp?treeType=<%=request.getParameter("treeType")%>"
				frameBorder=0 scrolling=no>
			<%--<FRAME name="project_work_tree" marginWidth=0 marginHeight=0
				src="<%=root%>/archive/tempfile/tempFile!tree.action?treeType=<%=request.getParameter("treeType")%>"
				frameBorder=0 scrolling=no>
		</FRAMESET>--%>
		<FRAME name="project_work_content" marginWidth=0 marginHeight=0
			src="<%=root%>/archive/tempfile/tempFile.action?treeType=<%=request.getParameter("treeType")%>"
			frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>
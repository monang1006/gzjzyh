<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<META content="MSHTML 6.00.2800.1106" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=20%,* frameborder="no" border="0" framespacing="0">
		<FRAME name="project_work_tree" marginWidth=0 marginHeight=0
			src="<%=root%>/archive/sort/archiveSort!tree.action?forwardStr=tree"
			frameBorder=0 scrolling=yes>
		<FRAME name="project_work_content" marginWidth=0 marginHeight=0
			src="<%=root%>/fileNameRedirectAction.action?toPage=archive/sort/sort_init.jsp"
			frameBorder=1 scrolling=no>
		<%--<FRAME name="project_work_content" marginWidth=0 marginHeight=0 src="<%=root%>/archive/sort/archiveSort!input.action" frameBorder=1  scrolling=no>--%>
	</FRAMESET>
</HTML>

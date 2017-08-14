<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>Future页面内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<FRAMESET cols="25%,75%" id="allframe" frameborder="no" border="0" framespacing="0">

		<FRAMESET rows="8%,92%" id="leftframe" frameborder="no" border="0" framespacing="0">
			<FRAME name="project_work_choose" marginWidth=0 marginHeight=0 src="<%=root%>/fileNameRedirectAction.action?toPage=dict/dictItem/choose.jsp" frameBorder=0 scrolling=no>
			<FRAME name="project_work_tree" marginWidth=0 marginHeight=0 src="<%=root%>/dict/dictType/dictType!tree.action" frameBorder=0 scrolling=no>
		</FRAMESET>
		<FRAME name="project_work_content" marginWidth=0 marginHeight=0 src="<%=root%>/fileNameRedirectAction.action?toPage=dict/dictItem/dictItem_init.jsp" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>


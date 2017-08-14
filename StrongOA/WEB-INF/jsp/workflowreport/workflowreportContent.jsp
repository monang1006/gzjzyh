<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>流程类别</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<FRAMESET cols="20%,80% frameborder="no"  order="0" framespacing="0">
		
		<FRAME name="project_work_tree" marginWidth=0 marginHeight=0
			src="<%=root%>/workflowreport/workFlowReport!tree.action" frameBorder=0
			scrolling=yes>

		<FRAME name="project_work_content" marginWidth=0 marginHeight=0
			src="<%=root%>/fileNameRedirectAction.action?toPage=workflowreport/workflowreport_init.jsp"
			frameBorder=1 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>


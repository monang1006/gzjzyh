<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
	</head>
	<frameset cols="0,*" id="contentframe" frameborder="no" border="0"
		framespacing="0">
		<frame name="project_work_tree" marginwidth="0" marginheight="0"
			src="<%=path%>/workflowDesign/action/processMonitor!tree.action" frameborder="0"
			scrolling="no">
	
		<frame name="project_work_content" marginwidth="0" marginheight="0"
			src="<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processMonitor-contentFrame.jsp"
			frameborder="0" scrolling="auto" bordercolor="#F2F3F4" style="width:100%">
	</frameset>
	<noframes>
	</noframes>
</html>
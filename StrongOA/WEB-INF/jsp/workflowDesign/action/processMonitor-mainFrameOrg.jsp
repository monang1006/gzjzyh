<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="java.net.URLEncoder"/>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
	</head>
	<%
	    String privilNamebak = URLEncoder.encode(URLEncoder.encode(privilName,"utf-8"),"utf-8");
	%>
	<frameset cols="0,*" id="contentframe" frameborder="no" border="0"
		framespacing="0">
		<frame name="project_work_tree" marginwidth="0" marginheight="0"
			src="" frameborder="0"
			scrolling="no" noresize="noresize">
		<frame name="project_work_content" marginwidth="0" marginheight="0"
			src="<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processMonitor-contentFrameOrg.jsp?mytype=${mytype}&state=${state}&doneYear=<%=request.getParameter("doneYear")==null?"":request.getParameter("doneYear")%>&excludeWorkflowType=<%=request.getParameter("excludeWorkflowType") %>&workflowType=<%=request.getParameter("workflowType") %>&privilNamebak=<%=privilNamebak %>"
			frameborder="0" scrolling="auto" bordercolor="#F2F3F4" style="width:100%">
	</frameset>
	<noframes>
	</noframes>
</html>
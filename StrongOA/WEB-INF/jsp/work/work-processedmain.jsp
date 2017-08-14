<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>已办工作</title>
		<%@include file="/common/include/meta.jsp" %>
	</head>
	<FRAMESET border="0" frameSpacing="0" cols="17%,*" frameBorder="0">
		<FRAME name="project_work_tree" marginWidth="0" marginHeight="0"
			src="<%=root%>/work/work!workFlowTypeTree.action?listMode=3" frameBorder="0" scrolling="no">

		<FRAME name="project_work_content" marginWidth="0" marginHeight="0"
			src="<%=root%>/work/work.action?listMode=3"
			frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>
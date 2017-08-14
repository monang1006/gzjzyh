<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>导入系统人员</title>
		<%@include file="/common/include/meta.jsp" %>
	</head>
	<FRAMESET border="0" frameSpacing="0" cols="17%,*" frameBorder="0">
		<FRAME name="project_work_tree" marginWidth="0" marginHeight="0"
			src="<%=root%>/address/addressOrg.action?type=import" frameBorder="0" scrolling="no">

		<FRAME name="project_work_content" marginWidth="0" marginHeight="0"
			src="<%=root%>/address/addressOrg!importOrgUserList.action?orgId=1&groupId=${groupId}"
			frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>
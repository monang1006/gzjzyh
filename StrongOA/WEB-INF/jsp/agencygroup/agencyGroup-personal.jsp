<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>发文组信息</title>
		<%@include file="/common/include/meta.jsp" %>
	</head>
	<FRAMESET border="1" frameSpacing="1" cols="17%,*" frameBorder="1" bordercolor="#d4d0c7">	
		<input id="groupId" type="hidden" name="groupId" value="${groupId }"/>
		<FRAME name="project_work_tree" marginWidth="0" marginHeight="0" style="height: 100%"
			src="<%=root %>/agencygroup/agencyGroup.action?groupId=<%=request.getParameter("groupId")%>" frameBorder="0" scrolling="no">

		<FRAME name="project_work_content" marginWidth="0" marginHeight="0"
			src="<%=root %>/agencygroup/groupDet.action?groupId=<%=request.getParameter("groupId")%>" 
			frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>
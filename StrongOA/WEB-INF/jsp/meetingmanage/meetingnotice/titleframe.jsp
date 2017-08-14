<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>会议议题信息</title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<script language="javascript">
		</script>
	</head>
	<%
		String id=request.getParameter("meetingId");
	 %>
		
	<FRAMESET border=0 frameSpacing=0 rows="55%,*" frameBorder=0>
		<FRAME name=personal_status_toolbar marginWidth=0 marginHeight=0
			src="<%=path%>/meetingmanage/meetingnotice/meetingnotice!addNew.action?meetingId=<%=id %>" frameBorder=0 noResize scrolling=no>
		<FRAME name=personal_status_titleview marginWidth=0 marginHeight=0
			src="<%=path%>/meetingmanage/meetingtopic/meetingtopic!displayview.action?meetingId=<%=id %>" frameBorder=0 noResize scrolling=auto>
	</FRAMESET>
	<noframes></noframes>
</html>

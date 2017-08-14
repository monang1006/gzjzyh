<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>填写通知单</title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<script language="javascript">
		</script>
	</head>
	<%
		String id=request.getParameter("meetingId");
	 %>
	
	<FRAMESET id=container  border=0 frameSpacing=0 rows="55%,*" frameBorder=0>
		<FRAME name=personal_status_content marginWidth=0 marginHeight=0
			src="<%=path%>/meetingmanage/meetingnotice/meetingnotice!input.action?meetingId=<%=id %>" frameBorder=0 noResize scrolling=no>
		<FRAME name=personal_status_view marginWidth=0 marginHeight=0
			src="<%=path%>/meetingmanage/meetingtopic/meetingtopic!displayview.action?meetingId=<%=id %>" frameBorder=0 noResize scrolling=no>
	</FRAMESET>
</html>

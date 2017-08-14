<html>
	<head>
		<title>mailinfo.html</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	</head>
	<%
		String type = request.getParameter("type");
		String url = "";
		if (type != null && type.equals("start"))
			url = "addUseRecord.jsp";
		else
			url = "addUseRecord.jsp";
	%>
	<FRAMESET border=0 frameSpacing=0 rows=30%,* frameBorder=0>
		<FRAME name=personal_toolbar marginWidth=0 marginHeight=0
			src="<%=url%>" frameBorder=0 noResize scrolling=no>
		<FRAME name=personal_content marginWidth=0 marginHeight=0
			src="status_toolbar.jsp?type=<%=type%>" frameBorder=0 noResize
			scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</html>

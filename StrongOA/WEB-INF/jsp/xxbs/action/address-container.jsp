<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>主操作区页面</title>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet" type="text/css" />
	</head>

	<frameset rows=100%,* frameborder="no" border="0" framespacing="0">
		<frame src="<%=path%>/fileNameRedirectAction.action?toPage=/xxbs/action/address-usermanage.jsp" name="topFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="topFrame" style="border-left:1px solid #0a5a9e;"/>
		<frame src="" name="downFrame" scrolling="no"
			marginwidth="0" marginheight="0" id="downFrame" style="border-left:1px solid #0a5a9e;"/>
	</frameset>
	
</html>
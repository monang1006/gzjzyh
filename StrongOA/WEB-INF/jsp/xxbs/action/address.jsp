<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>主操作区页面</title>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet" type="text/css" />
	</head>

	<frameset cols="18%,*" frameborder="no" border="0" framespacing="0">
		<frame src="<%=root%>/fileNameRedirectAction.action?toPage=/xxbs/action/address-tree.jsp" name="leftFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="leftFrame"/>
		<frame src="<%=path%>/fileNameRedirectAction.action?toPage=/xxbs/action/address-container.jsp" name="mainFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="mainFrame" />
	</frameset>
	<noframes>
		<body>
		</body>
	</noframes>
</html>
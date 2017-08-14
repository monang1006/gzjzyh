<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>主操作区页面</title>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet" type="text/css" />
	</head>

	<frameset cols="20%,*" frameborder="no" border="0" framespacing="0">
		<s:if test="flag=='guoban'">
		<frame src="<%=root%>/fileNameRedirectAction.action?toPage=/xxbs/action/piece-tree.jsp?flag='guoban'" name="leftFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="leftFrame"/>
		</s:if>
		<s:elseif test="flag=='shengji'">
		<frame src="<%=root%>/fileNameRedirectAction.action?toPage=/xxbs/action/piece-tree.jsp?flag='shengji'" name="leftFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="leftFrame"/>
		</s:elseif>
		<s:elseif test="flag=='jiafen'">
		<frame src="<%=root%>/fileNameRedirectAction.action?toPage=/xxbs/action/piece-tree.jsp?flag='jiafen'" name="leftFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="leftFrame"/>
		</s:elseif>
		<s:else>
		<frame src="<%=root%>/fileNameRedirectAction.action?toPage=/xxbs/action/piece-tree.jsp" name="leftFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="leftFrame"/>
		</s:else>
		<s:if test="flag=='guoban'">
		<frame src="<%=path%>/xxbs/action/piece!content.action?flag=guoban" name="mainFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="mainFrame" />
		</s:if>
		
		<s:elseif test="flag=='shengji'">
		<frame src="<%=path%>/xxbs/action/piece!content.action?flag=shengji" name="mainFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="mainFrame" />
		</s:elseif>
		<s:elseif test="flag=='jiafen'">
		<frame src="<%=path%>/xxbs/action/piece!content.action?flag=jiafen" name="mainFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="mainFrame" />
		</s:elseif>
		<s:else>
		<frame src="<%=path%>/xxbs/action/piece!content.action" name="mainFrame" scrolling="auto"
			marginwidth="0" marginheight="0" id="mainFrame" />
		</s:else>
	</frameset>
	<noframes>
		<body>
		</body>
	</noframes>
</html>
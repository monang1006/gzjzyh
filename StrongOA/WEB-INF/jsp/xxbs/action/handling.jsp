<%@include file="/common/include/rootPath_cb.jsp"%>

<HTML>
	<HEAD>
		<TITLE>Future页面内容</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
	</HEAD>
	<FRAMESET id="handlingSet" cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name="propertiesTree" marginWidth=0 marginHeight=0 src="<%=path%>/xxbs/action/handling!tree.action" frameBorder=0 scrolling=auto>
		<FRAME name="propertiesList" marginWidth=0 marginHeight=0 src="<%=path%>/xxbs/action/handling!content.action" frameBorder=0 scrolling=auto>
	</FRAMESET>
	<noframes></noframes>
</HTML>

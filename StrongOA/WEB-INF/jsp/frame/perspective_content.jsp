<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<HEAD>
		<TITLE>Future页面内容</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET cols=180,6,* name="title"  framespacing="0" border="0" frameborder="no">
		<FRAME name=navigator_container marginWidth=0 marginHeight=0	
			src="<%=path%>//fileNameRedirectAction.action?toPage=frame/perspective_content/navigator_container.jsp" 
			frameBorder=0 scrolling=no  noresize="noresize">
			
		<FRAME name=personal_navigator_container marginWidth=0 marginHeight=0 
		 	src="<%=path%>//fileNameRedirectAction.action?toPage=frame/perspective_content/navigator_select.jsp"  
			 frameBorder=no scrolling=no  noresize="noresize">

		<FRAME name=actions_container marginWidth=0 marginHeight=0
			src="<%=path%>//fileNameRedirectAction.action?toPage=frame/perspective_content/actions_container.jsp"
			 frameBorder=0 scrolling=no  noresize="noresize">
	</FRAMESET>
	<noframes></noframes>
</html>

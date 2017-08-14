<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>mailinfo.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

  </head>
  
	<FRAMESET border=0 frameSpacing=0 rows=33,* frameBorder=0>
		<FRAME name=personal_status_toolbar marginWidth=0 marginHeight=0 src="fileNameRedirectAction.action?toPage=message/status_toolbar.jsp" frameBorder=0 noResize scrolling=no>
		<FRAME name=personal_status_content marginWidth=0 marginHeight=0 src="fileNameRedirectAction.action?toPage=message/info.jsp" frameBorder=0 noResize scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</html>

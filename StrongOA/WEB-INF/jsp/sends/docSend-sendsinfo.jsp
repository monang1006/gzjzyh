<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>mailinfo.html</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
	<script type="text/javascript">
		$(function(){
			$('toolbarborder').bind('selectstart', function() { return false; });
		});
	</script>
  </head>
  
	<FRAMESET border=0 frameSpacing=0 rows=15,* frameBorder=0>
		<FRAME name=status_toolbar marginWidth=0 marginHeight=0 src="<%=path%>/fileNameRedirectAction.action?toPage=sends/docSend-toolbar.jsp" frameBorder=0 noResize scrolling=no>
		<FRAME name=status_content marginWidth=0 marginHeight=0 src="<%=path%>/sends/docSend!sendslist.action" frameBorder=0 noResize scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</html>

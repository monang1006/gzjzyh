<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<html>
	<head>
	</head>
	<body  id="anchor1">
		<div class="dhjmpb">
			<a class="dhjmpb" href="<%=root%>/wap/login!getMainInfo.action">导航</a>&nbsp;|&nbsp;
			<!-- <a href="<%=root%>/sms/sms!wapInput.action">短信</a>&nbsp;|&nbsp; -->
			<!--  <a href="<%=root%>/search/search!input.action">搜索</a>&nbsp;|&nbsp;-->
			<a href="<%=root%>/j_spring_security_logout">退出</a>
		</div>
	</body>
</html>

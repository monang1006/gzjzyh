<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.text.SimpleDateFormat,java.util.Date"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<html>
	<head>
	</head>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		String now = sdf.format(new Date());
	%>
	<body>
		<div id="footer">
			<a href="<%=root%>/wap/work.action?listMode=10&currentPage=${currentPage}&businessTitle=${businessTitle}&userName=${userName}&worktype=${worktype}">列表</a>&nbsp;|&nbsp;
			<a class="cur" href="<%=root%>/wap/login!getMainInfo.action">导航</a>&nbsp;|&nbsp;
			<!--<a class="cur" href="#anchor1">置顶</a>-->
		</div>
		<p></p>
		<small>[<%=now%>]</small>
	</body>
</html>

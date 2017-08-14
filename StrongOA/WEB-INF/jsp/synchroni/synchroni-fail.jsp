<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>ERROR!</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
  </head>
  
 <body class=contentbodymargin oncontextmenu="return false;">
	
		<DIV id=contentborder align=center>
		<Br><Br><Br><Br><Br><Br><Br><Br>
		<p style="font-family:verdana;font-size:100%;color:red">Webservice调用错误，请检查远程服务器是否开启！</p>
			</DIV>
  </body>
</html>

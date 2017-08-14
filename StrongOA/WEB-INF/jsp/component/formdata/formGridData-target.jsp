<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:directive.page import="sun.misc.BASE64Decoder"/>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String url = request.getParameter("url");
	BASE64Decoder base64 = new BASE64Decoder();
	url = new String(base64.decodeBuffer(url));
	url = root + url;
%>
<html>
	<head>
		<script>
			var width=screen.availWidth-10;
   			var height=screen.availHeight-30;
			window.open("<%=url%>",'sendDoc','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 
		  							'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');	  							
		</script>
	</head>
</html>
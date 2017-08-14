<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<html>
	<head>
	</head>

	<body>
		<table width="100%" height="100%" border="0">
			<tr>
			  <td width="10" rowspan="2">&nbsp;</td>
			  <td align="left" valign="top">${helpDesc}</td>
			  <td width="10" rowspan="2">&nbsp;</td>
			</tr>
		</table>
	</body>
</html>

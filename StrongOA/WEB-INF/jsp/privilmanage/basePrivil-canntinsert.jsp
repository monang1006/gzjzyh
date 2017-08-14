<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-statictree" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统资源树</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
</head>
<BODY  class=contentbodymargin leftmargin="2" topmargin="5">
<DIV id=treecontentborder>
</DIV>
</BODY>
</html>
<script>
	alert("当前用户为普通用户,不具有操作系统资源管理的权限!");
</script>
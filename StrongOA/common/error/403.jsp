<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>您没有权限</title>
<link href="<%=root%>/common/error/error.css" type=text/css rel=stylesheet>
</head>

<body>
<div class="erro_bg">
<div class="erro_content">
<p class="word1">您访问的页面出错了</p>
<p class="word2">很抱歉，您没有该页面的<span>访问权限</span>！</p>
<div class="erro_back"><input type="button" class="erro_back_bt"  onclick="window.location='index.shtml'"/></div>
</div>
</div>
</body>
</html>


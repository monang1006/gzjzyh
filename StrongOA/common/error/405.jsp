<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>您的访问出错了</title>
<link href="<%=root%>/common/error/error.css" type=text/css rel=stylesheet>
</head>

<body>
<div class="erro_bg">
<div class="erro_content">
<p class="word1">您访问的页面出错了</p>
<p class="word2">很抱歉，您<span>无权访问</span>，请与管理员联系。</p>
<div class="erro_back"><input type="button" class="erro_back_bt"  onclick="window.location='index.shtml'"/></div>
</div>
</div>
</body>
</html>

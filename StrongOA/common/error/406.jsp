<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录超时页面出错了</title>
<link href="<%=root%>/common/error/error.css" type=text/css rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
<script type="text/javascript">
	function longiAgin(){
		　　if(window.parent){
			top.location.href="<%=path%>/index.shtml"
	　            　}else{
	　            	window.location = "<%=path%>/index.shtml";
	　            　}
	}

</script>
</head>

<body>
<div class="erro_bg">
<div class="erro_content">
<p class="word1">您访问的页面出错了</p>
<p class="word2">很抱歉，登录超时，请重新登录!</p>
<div class="erro_back"><input type="button" class="erro_back_dl" onclick="longiAgin();" /></div>
</div>
</div>
</body>
</html>

<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>您的访问出错了</title>
<link href="<%=root%>/common/error/error.css" type=text/css rel=stylesheet>
<script type="text/javascript">
	$(document).ready(function() {	
	var historyValue= window.history.length;
	if (historyValue== undefined || historyValue ==null ){
		$("#close").show();
		$("#back").hide();
	}else{
		$("#close").hide();
		$("#back").show();	
	}


});
	

</script>

</head>
<body>
<div class="erro_bg">
<div class="erro_content">
<p class="word1">您访问的页面出错了</p>
<p class="word2">很抱歉，您输入的<span>用户名</span>或<span>密码</span>不正确！</p>
<div class="erro_back"><input type="button" class="erro_back_bt" onclick="window.location='index.shtml'" /></div>
</div>
</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<script type="text/javascript">
			function init(){
				var uname = "<%=request.getParameter("j_username")%>";
				if(uname != "" && uname != "null"){
					document.getElementById("j_username").value = uname;
					document.getElementById("j_password").value = "<%=request.getParameter("j_password")%>";
				} else {
					var parentWin = window.opener;
					var j_username = parentWin.document.getElementById("j_username");
					var j_password = parentWin.document.getElementById("j_password");
					document.getElementById("j_username").value = j_username.value;
					document.getElementById("j_password").value = j_password.value;
				}
				document.getElementById("form").submit();
			}
		</script>
  </head>
  <body scroll="auto" oncontextmenu="return false;" onload="init();">
		<form id="form" action="<%=root%>/j_spring_security_check" method="post">
			<input type="hidden" name="j_username" id="j_username"/>
			<input type="hidden" name="j_password" id="j_password"/>
		</form>
  </body>
</html>

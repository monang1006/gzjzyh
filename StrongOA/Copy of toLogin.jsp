<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String remoteAddr = request.getRemoteAddr();
	boolean inIp = remoteAddr.startsWith("127");
	String ipType = String.valueOf(inIp);
%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var uname = "<%=request.getParameter("j_username")%>";
				if(uname != "" && uname != "null"){
					$("#j_username").val(uname);
					$("#j_password").val("<%=request.getParameter("j_password")%>");
					var inIp = "<%=ipType%>";
					if(inIp == "true"){
						System.out.println("内网访问...");
					} else {
						System.out.println("外网访问...");
					}
				} else {
					var parentWin = window.opener;
					var j_username = parentWin.document.getElementById("j_username");
					var j_password = parentWin.document.getElementById("j_password");
					$("#j_username").val(j_username.value);
					$("#j_password").val(j_password.value);
					$("form").submit();
				}
			});
		</script>
  </head>
  <body scroll="auto" oncontextmenu="return false;">
		<form action="<%=root%>/j_spring_security_check" method="post">
			<input type="hidden" name="j_username" id="j_username"/>
			<input type="hidden" name="j_password" id="j_password"/>
		</form>
  </body>
</html>

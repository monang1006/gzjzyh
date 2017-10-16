<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script id="script" type="text/javascript"></script>
<script>
	alert("注册成功");
	
	var script = document.getElementById("script");
	if (script.attachEvent) {
		script.attachEvent("onreadystatechange", function() {
			parent.window.opener = null;
			parent.window.open(' ', '_self', ' '); 
			parent.window.close();
		});
	} else {
		script.addEventListener("load", function(e) {
			parent.window.opener = null;
			parent.window.open(' ', '_self', ' '); 
			parent.window.close();
		}, false);
	}
	script.setAttribute("src", "<%=request.getContextPath()%>/j_spring_security_logout");
</script>
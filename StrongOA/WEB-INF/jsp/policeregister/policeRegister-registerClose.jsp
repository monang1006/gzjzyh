<script id="script" type="text/javascript"></script>
<script>
	var script = document.getElementById("script");
	if (script.attachEvent) {
		script.attachEvent("onreadystatechange", function() {
			parent.window.close();
		});
	} else {
		script.addEventListener("load", function(e) {
			parent.window.close();
		}, false);
	}
	script.setAttribute("src", "<%=request.getContextPath()%>/j_spring_security_logout");
	alert("注册成功");
</script>
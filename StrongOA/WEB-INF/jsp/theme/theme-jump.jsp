<%@ page contentType="text/html; charset=utf-8"%>
<html>
	<head>
	<%
	 String url=(String)request.getAttribute("url");
	 String userName=(String)request.getAttribute("userName");
	 String password=(String)request.getAttribute("password");
	 System.out.println(url+" "+userName+" ");
	 %>

		<title>
		
		</title>
			</head>
		
	<body onLoad="jump();">
		
		<form id="form1" action="<%=url%>" method="POST">
								
		<input type="hidden" id="userLonginName" name="userLonginName" value="<%=userName%>"/>
  
       <input type="hidden" id="password" name="password" value="<%=password%>" />
        <input type="hidden" id="system" name="system" value="oa" />
       <input type="hidden" name="year" value="2009" />
					</form>
	
	</body>
		<SCRIPT type="text/javascript">
		function jump(){
			
			document.getElementById("form1").submit();
		}
		</script>
</html>

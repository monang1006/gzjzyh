        <!-- HTTP 1.1 -->
        <meta http-equiv="Cache-Control" content="no-store"/>
        <!-- HTTP 1.0 -->
        <meta http-equiv="Pragma" content="no-cache"/>
        <!-- Prevents caching at the Proxy Server -->
        <meta http-equiv="Expires" content="0"/>
        
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
        <s:set var="ctxPath" value="pageContext.request.contextPath" scope="request"/>
        <meta name="author" content=""/>
        <% 
			response.setHeader("Cache-Control","no-store");
			response.setHeader("Pragrma","no-cache");
			response.setDateHeader("Expires",0);
			String cp = request.getContextPath();
			if(cp.endsWith("/")) cp="";
			String cpBase = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cp+"/";
		%>
		<link rel="Shortcut Icon" href="<%=cpBase %>favicon.ico" />

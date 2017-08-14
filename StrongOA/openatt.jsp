<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<%
String ret = request.getParameter("ret");
%>
<html>
  <head>
  	<TITLE>查看附件</TITLE>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
  </head>
  
  <body>
   <div id="acropdf" style="WIDTH: 100%; HEIGHT: 98%";>
			
				<iframe name="surenIframe" style="display: block" src="<%=ret%>" WIDTH="100%" HEIGHT="100%"></iframe>
			</div>

  </body>
</html>



<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="com.strongit.oa.pdf.PDFreaderManager" %>

<%
PDFreaderManager officeServer = new PDFreaderManager();
officeServer.ExecuteRun(request,response);


out.clear();
out = pageContext.pushBody(); 
%>
<%@ page contentType="text/html; charset=utf-8" %>
<jsp:directive.page import="java.io.PrintWriter"/>
<%
	//手机OA登录成功或失败后会跳转到此页面
	String result = (String)request.getAttribute("result");
	PrintWriter pw = response.getWriter();
	response.setContentType("text/plain;charset=UTF-8");
	response.setHeader("Cache-Control", "no-cache");
	if(result != null){
		pw.write(result);
		System.out.println(result);
	}
%>
							
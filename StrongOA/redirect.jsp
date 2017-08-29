<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<jsp:directive.page import="org.springframework.security.userdetails.UserDetails"/>
<jsp:directive.page import="org.springframework.security.context.SecurityContextHolder"/>
<%
	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	String userLoginName = userDetails.getUsername();
	if("anonymous".equals(userLoginName)){
		response.sendRedirect(path + "/policeregister/policeRegister!goRegister.action");
	}else{
		response.sendRedirect(path + "/default.jsp");
	}
%>

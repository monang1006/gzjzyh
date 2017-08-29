<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	String userLoginName = userDetails.getUsername();
	if("anonymous".equals(userLoginName)){
		response.sendRedirect(path + "/policeregister/policeRegister!input.action");
	}else{
		response.sendRedirect(path + "/default.jsp");
	}
%>

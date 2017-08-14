<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE></TITLE>
			<%@include file="/common/include/meta.jsp"%>
		
          <%
       String sType = request.getParameter("structureType");
             
			%>
	</HEAD>
		<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
	
			<% if(sType!=null){ 
			%>
			<FRAME name=sortlist marginWidth=0 marginHeight=0
			src="org-structuretree.jsp?type=0" frameBorder=0 scrolling=no>
			<FRAME name=mykmlist marginWidth=0 marginHeight=0
	     src="structure-auditing.jsp" frameBorder=0 scrolling=no>
	     <% 
	     }else{
	      %>
	      <FRAME name=sortlist marginWidth=0 marginHeight=0
			src="org-structuretree.jsp?type=1" frameBorder=0 scrolling=no>
	      <FRAME name=mykmlist marginWidth=0 marginHeight=0
	   src="structure-xiada.jsp" frameBorder=0 scrolling=no>
           <% }%>

	</FRAMESET>
	<noframes></noframes>
	
	<script type="text/javascript">
	
	</script>
</HTML>

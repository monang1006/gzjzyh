<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<TITLE>
	<%
		ToaSysmanageBase sysTheme = request.getSession()
		.getAttribute("sysTheme")==null?null:(ToaSysmanageBase)request.getSession()
				.getAttribute("sysTheme");
		if(sysTheme!=null&&sysTheme.getBaseWindowsTitle()!=null&&sysTheme.getBaseWindowsTitle().length()>0){
	%>
		<%=sysTheme.getBaseWindowsTitle() %>
	<%}else{ %>
		思创数码科技股份有限公司协同办公软件
	<%} %>
</TITLE>
</head>

<frameset rows="190,30,392" cols="*" framespacing="0" frameborder="no" border="0">
  <frame src="${F1}" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />

  <frame src="${F2}" name="mainFrame" id="mainFrame"   scrolling="no"  noresize="noresize" title="mainFrame" />
  
  <frame src="${F3}" scrolling="no" name="bottomFrame"  noresize="noresize" id="bottomFrame" title="bottomFrame" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

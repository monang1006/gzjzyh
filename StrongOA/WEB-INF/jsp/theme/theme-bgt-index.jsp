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

<frameset rows="120,42,*"  name="title" cols="*" framespacing="0" frameborder="no" border="0">
  <frame src="<%=path%>/fileNameRedirectAction.action?toPage=theme/bgt-top.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" />

  <frame src="<%=path%>/theme/theme!RefreshToolbar.action?pageModelId=4028822d336e365d01336e5af1c50003&limitType=6" name="mainFrame" id="mainFrame"   scrolling="no"  noresize="noresize" />
  
  <frame src="<%=path%>/fileNameRedirectAction.action?toPage=theme/bgt-content.jsp" scrolling="no" name="bottomFrame"  noresize="noresize" id="bottomFrame" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

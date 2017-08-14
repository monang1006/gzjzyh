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

<frameset rows="170,*"  name="title" cols="*" framespacing="0" frameborder="no" border="0">
  <frame src="<%=path%>/theme/theme!RefreshToolbar.action?pageModelId=40280cf137316b41013731a3ca24000a&limitType=6" name="mainFrame" id="mainFrame"   scrolling="no"  noresize="noresize" title="mainFrame" />
  
  <frame src="<%=path%>/fileNameRedirectAction.action?toPage=theme/web-content.jsp" scrolling="no" name="bottomFrame"  noresize="noresize" id="bottomFrame" title="bottomFrame" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

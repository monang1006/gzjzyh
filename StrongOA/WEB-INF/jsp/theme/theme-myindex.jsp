<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<%@include file="/common/include/rootPath.jsp"%>
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

<frameset rows="95,30,392" cols="*"  name="title" framespacing="0" frameborder="no" border="0">
  <frame src="<%=path%>/fileNameRedirectAction.action?toPage=theme/mytop.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />

  <frame src="<%=path%>//theme/theme!RefreshToolbar.action?pageModelId=402882192aeb66db012aeb89d3f00004" name="mainFrame" id="mainFrame"   scrolling="no"  noresize="noresize" />
  
  <frame src="<%=path%>/fileNameRedirectAction.action?toPage=frame/perspective_content/actions_container/mycontent.jsp" scrolling="no" name="bottomFrame"  noresize="noresize" id="bottomFrame"  />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

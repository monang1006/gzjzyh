<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<HTML>
	<FRAMESET rows=70%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=userlist marginWidth=0 marginHeight=0
			src="<%=request.getContextPath()%>/usermanage/usermanage.action" frameBorder=0 scrolling=no>
		<FRAME name=userinfo marginWidth=0 marginHeight=0
			src="<%=request.getContextPath()%>/fileNameRedirectAction.action?toPage=/usermanage/usermanage-info.jsp" frameBorder=0 scrolling=no>
			
	</FRAMESET>
	<noframes></noframes>
</HTML>

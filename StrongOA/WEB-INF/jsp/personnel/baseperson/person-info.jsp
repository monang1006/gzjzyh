<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>Future页面内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<FRAMESET rows=68%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=personinfo marginWidth=0 marginHeight=0 src="<%=path%>/personnel/baseperson/person!getPersonInfoList.action" frameBorder=0 scrolling=no>	
		<FRAME name=otherinfo marginWidth=0 marginHeight=0 src="<%=path%>/fileNameRedirectAction.action?toPage=/personnel/baseperson/temp.jsp" frameBorder=0 scrolling=no>	
	</FRAMESET>
	<noframes></noframes>
</HTML>

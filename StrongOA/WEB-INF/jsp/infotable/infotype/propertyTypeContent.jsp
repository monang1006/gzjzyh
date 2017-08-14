<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>Future页面内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=propertyTypeTree marginWidth=0 marginHeight=0 src="<%=root%>/infotable/infoset/infoSet!typetree.action" frameBorder=0 scrolling=no>
		<FRAME name=propertyTypeList marginWidth=0 marginHeight=0 src="<%=root%>/fileNameRedirectAction.action?toPage=infotable/infotype/infoType_init.jsp" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

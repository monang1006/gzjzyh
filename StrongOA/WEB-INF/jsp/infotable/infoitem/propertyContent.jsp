<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>信息项页面内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=propertyTree marginWidth=0 marginHeight=0 src="<%=root%>/infotable/infoset/infoSet!itemtree.action" frameBorder=0 scrolling=no>
		<FRAME name=propertyList marginWidth=0 marginHeight=0 src="<%=root%>/fileNameRedirectAction.action?toPage=infotable/infoitem/infoItem_init.jsp" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

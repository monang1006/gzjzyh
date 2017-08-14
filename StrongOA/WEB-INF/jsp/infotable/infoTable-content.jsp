<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>页面内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<FRAMESET cols=20%,* id="workframe" framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name="treeframe" marginWidth=0 marginHeight=0 src="<%=root%>/infotable/infoTable!tree.action?otherPro=${otherPro}&fpro=${fpro}&functable=${functable}&tableDesc=${tableDesc}&pkey=${pkey}&struct=${struct}" frameBorder=0 scrolling=no>
		<s:if test="funcurl!=null">
			<FRAME name="indexframe" marginWidth=0 marginHeight=0 src="<%=root%>/infotable/infoTable.action?tableName=${functable}&fpro=${keyid}&fid=0" frameBorder=0 scrolling=no>
		</s:if>
		<s:else>
			<FRAME name="indexframe" marginWidth=0 marginHeight=0 src="<%=root%>/fileNameRedirectAction.action?toPage=/infotable/infoTable_init.jsp" frameBorder=0 scrolling=no>
		</s:else>
	</FRAMESET>
	<noframes></noframes>
</HTML>

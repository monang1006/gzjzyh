<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>桌面选择</title>
	</head>
	<body>
		<s:iterator value="#request.objList" status="statu" id="po">
			<div align="center" class="panelcon">
				<img src="<%=path%>/oa/image/desktop/menu/1/icon/8.gif" align="absmiddle" class="panelicon"><s:property value="#po.name"/> <img src="<%=path%>/oa/image/desktop/index_i/add2.gif" align="absmiddle" class="paneladdimg" onclick="addBlock(<s:property value="#po.toaDeskSectionsel.privilId"/>,'<s:property value="#po.name"/>');">
			</div>
		</s:iterator>
	</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css
			rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
	</head>
	<body>
		<DIV id=contentborder align=center>
		<tree:strongtree title="栏目名称" check="false"
			dealclass="com.strongit.oa.infopub.articles.DealTreeNode"
			data="${columnList}" target="recycledList"
			iconpath="frame/theme_gray/images/" />
			
		</DIV>
	</body>
</html>

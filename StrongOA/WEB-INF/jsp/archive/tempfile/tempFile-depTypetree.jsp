<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
	</head>
	<body>
		<DIV id=contentborder align=center style="background-color: #ffffff" >
		<tree:strongtree title="文件类型" check="false"
			dealclass="com.strongit.oa.archive.tempfile.RecDealDepTreeType"
			data="${treeList}" target="project_work_content"
			iconpath="frame/theme_gray/images/" />
		</DIV>
	</body>
</html>

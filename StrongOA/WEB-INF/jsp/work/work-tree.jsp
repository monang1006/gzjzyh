<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>流程类型</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder cellpadding="0">
			<tree:strongtree title="流程类型" showTitle="false" check="false" dealclass="com.strongit.oa.work.util.WorkDeal" data="${typeList}" target="project_work_content"  />	
		</DIV>
	</BODY>
</HTML>

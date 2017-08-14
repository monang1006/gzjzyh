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
	<script type="text/javascript">
	function treeNodeClick(workflowName,formId,workflowTyp){
		window.parent.showDocList(workflowName,formId,workflowTyp);
	}
	
	</script>
		<DIV id=contentborder align=center>
			<tree:strongtree title="公文类型" check="false"
				dealclass="com.strongit.oa.docbacktracking.deal.WorklowNameTree"
				data="${workflowInfoVolist}" 
				iconpath="frame/theme_gray/images/" />
		</DIV>
	</body>
</html>

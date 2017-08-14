<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<title>流程选择树形结构</title>
		<script type="text/javascript">
			function getFlow(flowname){
				window.returnValue=flowname;
				window.close();
			}
		</script>
	</head>
	<body>
		<tree:strongtree title="流程选择"  check="false" dealclass="com.strongit.oa.docafterflow.util.FlowNode" data="${list}"/>
	</body>
</html>

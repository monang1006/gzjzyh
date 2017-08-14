<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>组织机构树</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<%--		<script type="text/javascript">
		$(document).ready(function(){
			window.tree = new MzTreeView("tree");
			var tree=$("MzTreeView");
			alert(tree);			
			var id="1";
			var sureExpand="true";
		
			tree.expand(id,sureExpand);
									
		});
			
		</script>--%>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder cellpadding="0">
<%--			<tree:strongtree title="组织机构"  check="false" dealclass="com.strongit.oa.address.util.AddressOrgPersonDeal" data="${groupLst}" target="project_work_content"  />	--%>
			<tree:strongtree title="组织机构"  check="false"  titlecheck="false"  dealclass="com.strongit.oa.address.util.AddressOrgPersonDeal" data="${groupLst}" target="project_work_content"  iconpath="frame/theme_gray/images/"/>	
		</DIV>
	</BODY>
</HTML>

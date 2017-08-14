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
	</HEAD>
	<BODY  oncontextmenu="return false;">
		<DIV id=contentborder style="border-right-color: #d4d0c7" cellpadding="0">
			<tree:strongtree title="系统通讯录"  check="false" dealclass="com.strongit.oa.address.util.AddressOrgDeal" data="${orgList}" target="project_work_content"  />	
		</DIV>
	</BODY>
</HTML>

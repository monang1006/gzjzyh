<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-statictree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>类目树</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<script type="text/javascript">
			var imageRootPath='<%=frameroot%>';
		</script>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path %>/common/js/mztree_check/mztreeview_check.js"></script>
	</HEAD>
	<BODY class=contentbodymargin>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="95%" height="100%">
				<tr>
					<td valign=top>
						<tree:strongtree title="${infoSetName}" check="false" dealclass="com.strongit.oa.infotable.infoitem.DealTreeNode" data="${infoitemlist}" target="project_work_content"/>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

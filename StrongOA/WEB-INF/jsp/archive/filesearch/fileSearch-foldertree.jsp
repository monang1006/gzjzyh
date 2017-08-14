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
		<script type="text/javascript">
		
		function goFileList(id){
		var url="<%=root%>/archive/filesearch/fileSearch!filelist.action?"+
					      "treeValue="+id+
					      "&groupType="+"${groupType }"+
					      "&fileNo="+"${fileNo }"+
					      "&fileTitle="+"${fileTitle }"+
					      "&fileAuthor="+"${fileAuthor }"+
					      "&fileFolder="+"${fileFolder }"+
					      "&filepage="+"${filepage }"+
					      "&fileType="+"${fileType }"+
					      "&orgId=${orgId }&disLogo=${disLogo}&fileDate=${fileDate }";
					      parent.project_work_content.location=encodeURI(url);
					      
		}
		</script>
	</head>
	<body>
		<DIV id=contentborder align=center  style="background-color: #ffffff">
		<tree:strongtree title="案卷名称" check="false"
			dealclass="com.strongit.oa.archive.filesearch.RecDealTreeTypeTree"
			data="${treeList}" target="project_work_content"
			iconpath="frame/theme_gray/images/" />
		</DIV>
	</body>
</html>

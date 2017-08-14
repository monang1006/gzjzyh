<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base target="_self"/>
		<%@include file="/common/include/meta.jsp"%>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css
			rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
	</head>

	<body>
	<script type="text/javascript">
	function treeNodeClick(nodeName,taskId,processId,disable){
		//window.parent.showApprovalInput(nodeName,taskId,processId,disable);
		/*
		var param = "?1=1"+ "&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
					"&nodeName="+encodeURI(encodeURI($("#nodeName").val()));
		var url = "<%=root%>/docbacktracking/docBackTracking!nextstep.action"+param;
		*/
	}
	
	$(document).ready(function() {
		var nodeName = "${lastestworkflowNodeVo.nodeName}";
		var taskId = "${lastestworkflowNodeVo.taskId}";
		var processId = "${lastestworkflowNodeVo.processId}";
		var disable = "${lastestworkflowNodeVo.disable}";
		if(disable == "false"){
			disable = "0";
			window.parent.showApprovalInput(nodeName,taskId,processId,disable);
		}else{
			//disable = "1";
			top.returnValue = "OK";
			top.close();
		}
	});
	</script>
		<DIV id=contentborder align=center>
			<s:hidden name="workflowName" id="workflowName"></s:hidden>
			<tree:strongtree title="已完成环节" check="false"
				dealclass="com.strongit.oa.docbacktracking.deal.WorklowNodeTree"
				data="${workflowNodeVolist}" 
				iconpath="frame/theme_gray/images/" />
		</DIV>
	</body>
</html>

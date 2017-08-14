<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base target="_self"/>
		<%@include file="/common/include/meta.jsp"%>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
				<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	</head>

	<body >
	<script type="text/javascript">
		function backCall(){
			var treeUrl = "<%=path%>/docbacktracking/docBackTracking!nodetree.action?1=1"
						+ ("&workflowName="+encodeURI(encodeURI($("#workflowName").val())))
						+ ("&tableName="+$("#tableName").val())
						+ ("&pkFieldName="+$("#pkFieldName").val())
						+ ("&pkFieldValue="+$("#pkFieldValue").val());
			var gridviewUrl = "<%=path%>/docbacktracking/docBackTracking!doclist.action";
			$("#tree").attr("src",treeUrl);
			$("#gridview").attr("src",gridviewUrl);
		}	
	
		function showApprovalInput(nodeName,taskId,processId,disable){
			if(disable == "0"){
				var param = "?1=1"+ "&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
							+"&nodeName="+encodeURI(encodeURI(nodeName))
							+"&tableName="+$("#tableName").val()
							+"&pkFieldName="+$("#pkFieldName").val()
							+"&pkFieldValue="+$("#pkFieldValue").val()
							+"&instanceId="+processId
							+"&taskId="+taskId;
				var url = "<%=root%>/docbacktracking/docBackTracking!nextstep.action"+param;
				$("#gridview").attr("src",url);
			}
		}
		
		function showApprovalList(nodeName){
			//alert("showApprovalList():	"+nodeName+"|"+nodeId+"|"+pkValue+"|"+isTaskNode);
			$("#currentNodeName").val(nodeName);
			$("#transitionName").val(transitionName);
			var gridviewUrl = "<%=path%>/docbacktracking/docBackTracking!approvallist.action?1=1"
					+ "&workflowSateId="+pkValue;
			$("#gridview").attr("src",gridviewUrl);
		}
		$(document).ready(function() {
			backCall();
		});
		
		function getCurrentNodeName(){
			return $("#currentNodeName").val();
		}
		function getTransitionName(){
			return $("#transitionName").val();
		}
		
	</script>
		<!-- 当前环节名称 -->
		<input id="currentNodeName" name="currentNodeName" type="hidden">
		<!-- 迁移线名称 -->
		<input id="transitionName" name="transitionName" type="hidden">
		<DIV id=contentborder align=center  style="width: 100%;height: 100%;overflow: auto;">
			<!-- 流程名称 -->
			<s:hidden id="workflowName" name="workflowName"></s:hidden>
			<!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- 是否要验证领导批示扫描附件上传 -->
			<input id="isValidate" name="isValidate" value="" type="hidden" />
			<table style="width: 100%;height: 100%;">
				<tr style="height: 100%;">
					<td style="width: 20%">
						<div>
							<iframe name="tree" id="tree"
								src=""
								style="width: 20%;height: 100%;" />
						</div>
					</td>
					<td style="width: 20%">
						<div>
							<iframe name="gridview" id="gridview"
								src=""
								style="width: 80%;height: 100%;" />
						</div>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

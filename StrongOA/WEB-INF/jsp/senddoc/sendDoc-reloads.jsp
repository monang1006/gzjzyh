<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</HEAD>
	<base target="_self" />
	<BODY>
		<DIV id="msgdiv" style="display: none;">
			<s:actionmessage theme="simple" />
		</DIV>
		<SCRIPT>
			var formId="${formId}";
			var type="${type}";
			var workflowName="${workflowName}";
			var workflowType="${workflowType}";
			var excludeWorkflowType="";
			var handleKind="${handleKind}";
			var taskId="${taskId}"
			window.close();
			if(taskId==null||"null"==taskId||''==taskId){
			    window.opener.location="<%=path%>/senddoc/sendDoc!draft.action?handleKind="+handleKind+"&workflowName="+workflowName+"&formId="+formId+"&workflowType="+workflowType;
			}else{
				window.opener.location="<%=path%>/work/work!listtodo.action?workflowType="+workflowType+"&handleKind="+handleKind+"&type="+type+"&formId="+formId+"&excludeWorkflowType="+excludeWorkflowType;
			}
		</SCRIPT>
	</BODY>
</HTML>

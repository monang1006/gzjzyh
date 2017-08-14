<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>机构数据导入返回</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
	</head>
<html>
<script>
	<s:if test="#request.msg != null && #request.msg != \"\"">
		alert('<s:property value="#request.msg" />');
		parent.showLoading(false);
		parent.resetButton();
	</s:if>
	<s:else>
		<s:if test="#request.smsg != null && #request.smsg != \"\"">
			alert('导入机构数据成功。/n'+'<s:property value="#request.smsg" />');
	   </s:if>
	   <s:else>
			alert('导入机构数据成功。');
		</s:else>
		parent.window.dialogArguments.parent.parent.organiseTree.location = "<%=path%>/organisemanage/orgmanage!tree.action";
		parent.window.close();
	</s:else>
</script>
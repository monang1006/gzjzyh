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
	   alert('导入字典项数据成功！');
	   var dictCode="${dictCode}";
		parent.window.dialogArguments.location = "<%=path%>/dict/dictItem/dictItem.action?dictCode="+dictCode;
		parent.window.close();
	</s:else>
</script>
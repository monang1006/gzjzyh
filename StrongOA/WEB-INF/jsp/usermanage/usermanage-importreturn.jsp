<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>用户数据导入返回</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
	</head>
<html>
<script>
   var id="${excelOrgId}"
	<s:if test="#request.msg != null && #request.msg != \"\"">
		alert('<s:property value="#request.msg" />');
		parent.showLoading(false);
		parent.resetButton();
	</s:if>
	<s:else>
	   alert('导入用户数据成功！');
		parent.window.dialogArguments.parent.userInfotContext.userlist.location ="<%=path%>/usermanage/usermanage!ogrlist.action?extOrgId="+id;
		parent.window.close();
	</s:else>
</script>
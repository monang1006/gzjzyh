<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<!--公用脚本，因为在这里用了OpenWindow方法，如果没有用到这个脚本里面的方法可以不引用 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</head>
	<BODY>
		<DIV id="msgdiv" style="display: none;">
			<s:actionmessage />
		</DIV>
		<SCRIPT>
			alert(document.getElementById("msgdiv").children[0].children[0].children[0].innerHTML);
			window.dialogArguments.submitForm();
			window.close();
		</SCRIPT>
	</BODY>
</html>

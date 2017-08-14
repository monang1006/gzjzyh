<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</HEAD>
	<base target="_self" />
	<BODY>
		<DIV id="msgdiv" style="display: none;">
			<s:actionmessage theme="simple" />
		</DIV>
		<SCRIPT>
			var msg = document.getElementById("msgdiv").children[0].children[0].children[0].innerHTML;
			window.dialogArguments.reloadLocation(msg);
			window.close();
		</SCRIPT>
	</BODY>
</HTML>

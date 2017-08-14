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
	<BODY>
		<DIV id="msgdiv" style="display: none;">
			<s:actionmessage theme="simple" />
		</DIV>
		<SCRIPT>
			var infoSetParentid="${infoSetParentid}";
			var msg = document.getElementById("msgdiv").children[0].children[0].children[0].innerHTML;
			if(msg.indexOf("成功")==-1)
				alert(msg);
			window.location="<%=path%>/infotable/infoset/infoSet.action?infoSetParentid="+infoSetParentid;
		</SCRIPT>
	</BODY>
</HTML>

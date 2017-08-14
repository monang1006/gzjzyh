<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></script>
		<SCRIPT>
			function setNameValue(name,type){
				document.getElementById("name").value=name;
				document.getElementById("type").value=type;
			}
			function submitForm(){
				document.getElementById("DictypeForm").submit();
			}
		</SCRIPT>
	</head>
	<body class=contentbodymargin>
		<div id=contentborder>
		<s:form id="DictypeForm" theme="simple" action="/dict/dictType/dictType!tree.action">
			<s:hidden name="name" id="name"></s:hidden>
			<s:hidden name="type" id="type"></s:hidden>
		</s:form>
		<tree:strongtree title="字典类" check="false" dealclass="com.strongit.oa.dict.dictType.DealTreeNode" data="${dictTypeList}" target="project_work_content"  />
		</div>
	</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>事件动作</title>
<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />

<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
<script type="text/javascript" src="<%=jsroot%>/ztree/jquery.ztree.js"></script>

<script type="text/javascript">

var setting = {
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: selectNode
	}
};

function selectNode(event, treeId, treeNode){
	if(treeNode.pId != null){
		var ret = {};
		ret.eventName=treeNode.name;
		ret.eventClass=treeNode.eaClass;
		returnValue = ret;
		close();
	}
}


var zNodes = $.parseJSON('${ztreeNodes}') || [];
$(document).ready(function(){
	$.fn.zTree.init($("#ztree"), setting, zNodes);
});


</script>

</head>

<body>
	<div>
		<ul id="ztree" class="ztree"></ul>
	</div>
</body>
</html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<title>查询分类</title>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="tree_box">
			<div id="moduleTree"></div>
			<web:tree name="moduleTree"
				lazyUrl="${root}/xxbs/action/handling!showTree.action"
				iconDir="${themePath}/image/tree/" title="信息查询分类"
				nodeClick="selectGroup" contextMenu="buton_list"></web:tree>
		</div>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">
function selectGroup(item){
	if(item.pid != -1 || item.value == "my"){
		var val = item.value;
		var url = "<%=root%>/xxbs/action/handling!content.action?qs=";
		parent.propertiesList.location = url + val;
	}
}

function reloadData() {
    $('#moduleTree').reLoadData();
}

</script>
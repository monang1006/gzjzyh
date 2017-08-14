<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title>期刊发布</title>
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
				lazyUrl="${root}/xxbs/action/release!treeList.action"
				iconDir="${themePath}/image/tree/" title="期刊发布"
				nodeClick="selectGroup"></web:tree>
		</div>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">
function selectGroup(item){
	if((item.pid != -1)&&(item.pid.length!=32)){
		var val = item.value;
		var url = "<%=root%>/xxbs/action/release!content.action?toId="+val;
		parent.propertiesList.location = url;
	}
}

function reloadData() {
    $('#moduleTree').reLoadData();
}

</script>
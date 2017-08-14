<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<title>会议</title>
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
        <link rel="stylesheet" type="text/css" href="<%=themePath%>/css/dxxk.css" />
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="tree_box">
			<div id="moduleTree"></div>
			<web:tree name="moduleTree"
				lazyUrl="${root}/leave/leave!showTree.action"
				iconDir="${themePath}/image/tree/" title="通讯录管理"
				nodeClick="selectGroup" contextMenu=""></web:tree>
		</div>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">
function selectGroup(item){
	if(item.pid != -1 || item.id != null){
		var val = item.id;
		//alert(val);
		var url = "<%=root%>/leave/leave!content.action?conId="+val;
		parent.leaveInfo.location = url;
	}
}

function reloadData() {
    $('#moduleTree').reLoadData();
}

</script>
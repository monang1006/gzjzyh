<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>选择负责人</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="windows_title">
			请选择负责人
		</div>
		<div class="information_out" id="information_out">
			<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
				<table id="list"></table>
				<div id="pager"></div>
			</div>
		</div>



	</body>
</html>
<script type="text/javascript">
	
$(function(){
	$("#list").jqGrid({
		url:'${root}/xxbs/info/user!showList.action',
		colModel :[ 
			{label:'userId',name:'userId', hidden:true}, 
			{label:'用户名',name:'userName', width:80}, 
			{label:'登录名',name:'userLoginname', width:80}
			],
		multiboxonly: true,
		onSelectRow: selectRow,
		gridComplete: G.resize,
		height: '100%'
	});
});


var selectRow = function(rowId){
	var ret = {};
	ret.status = "success";
	ret.userId = rowId;
	ret.userName = $("#list").jqGrid("getCell", rowId, "userName");
	window.returnValue = ret;
	window.close();
};

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

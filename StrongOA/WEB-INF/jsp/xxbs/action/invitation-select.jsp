<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>约稿信息</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
			.ui-jqgrid-resize-ltr{width: 0px;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="windows_title">
			请选择约稿信息
		</div>
		<div class="information_out" id="information_out">
			<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 0px;">
				<table id="list"></table>
				<div id="pager"></div>
			</div>
		</div>

		<div class="information_list_choose_pagedown">
			<input type="submit" class="information_list_choose_button9"
				value="取消约稿" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>


	</body>
</html>
<script type="text/javascript">
	
$(function(){
	$("#list").jqGrid({
	    url:'${root}/xxbs/action/invitation!showList.action?isSinceToday=true',
	    colModel :[ 
	      {label:'aptId',name:'aptId', hidden:true}, 
	      {label:'约稿标题',name:'aptTitle'}, 
	      {label:'发布时间',name:'aptDate',width:50, align:'center'}, 
	      {label:'截止时间',name:'aptDuedate', width:50, align:'center'}
	    ],
	    onSelectRow: selectRow,
	    multiselect: false,
	    gridComplete: gl.resize
	});
	
	$("#save").click(function(){
		var ret = {};
		ret.status = "success";
		ret.id = "";
		ret.title = "";
		window.returnValue = ret;
		window.close();
	});
	
	$("#cancel").click(function(){
		window.close();
	});

});

var selectRow = function(id){	
	var ret = {};
	ret.status = "success";
	ret.id = id;
	ret.title = $("#list").jqGrid("getCell", id, "aptTitle");
	window.returnValue = ret;
	window.close();
};

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>备稿单位</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="windows_title">
			已选备稿单位
		</div>
		<div class="information_out" id="information_out">
			<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 0px;">
				<table id="list"></table>
				<div id="pager"></div>
			</div>
		</div>

		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="选择" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>


	</body>
</html>
<script type="text/javascript">

var selectRow = function(){
	gl.resize();
	var orgIds = "${orgIds}";
	orgIds = orgIds.split(",");
	if(orgIds){
		for(var i=0;i<orgIds.length;i++){
			$("#list").setSelection($.trim(orgIds[i]));
		}
	}
};
	
$(function(){
	$("#list").jqGrid({
	    url:'${root}/xxbs/action/org!showList.action',
	    colModel :[ 
	      {label:'orgId',name:'orgId', hidden:true}, 
	      {label:'备稿单位',name:'orgName'}
	    ],
	    gridComplete: selectRow
	});
	
	$("#save").click(function(){
		var ids = $("#list").getGridParam('selarrrow');
		window.returnValue = ids;
		window.close();
	});
	
	$("#cancel").click(function(){
		window.close();
	});

});

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>合并信息</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="information_out" id="information_out">
			<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 0px;">
					<table id="list"></table>
					<div id="pager"></div>
			</div>
		</div>

		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>
	</body>
</html>
<script type="text/javascript">
function reloadData(){
	$('#list').trigger("reloadGrid");
}

$(function(){

	var onCellSelect = function(rowid, iCol ,el){
		var url = "<%=root%>/xxbs/action/handling!viewIsMerge.action?toId="+rowid+"&op=flag";
		var ret = gl.showDialog(url,1000,800);
		gl.msg(ret, "更新采用成功");	
	};

	
	$("#list").jqGrid({
	    url:'${root}/xxbs/action/handling!mergeOrg.action?flag=${toId}',
	    colModel :[ 
	      {label:'pubId',name:'pubId', hidden:true}, 
	      {label:'原始标题',name:'pubTitle'}, 
	      {label:'上报时间',name:'pubDate'}, 
	      {label:'机构名称',name:'orgName'}, 
	    ],
	    onCellSelect: onCellSelect,
		gridComplete: gl.resize,
		sortname: 'pubDate',
	});
	

	
	
	
	
	$("#cancel").click(function(){
		window.close();
	});

});





</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

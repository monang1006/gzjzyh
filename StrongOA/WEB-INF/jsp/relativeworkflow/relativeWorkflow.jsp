<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/root_path.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<TITLE>关联流程</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/jqgrid/jqgrid.js"></script>

		
		
<style>
#main-table{
	width: 100%;
}
#main-title{
	float:left;
}
a.rw_link{
	color:blue;
}
</style>

	</head>
<body>
<table id="main-table">
	<tr>
		<td class="tool-top">
			<div id="main-title"><img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;<strong>已经关联流程列表</strong></div>
			<table style="float:right" border="0" align="right" cellpadding="00" cellspacing="0">
				<tr>
					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="add();">
						<img src="<%=root%>/images/operationbtn/add.png" />&nbsp;关&nbsp;联&nbsp;
					</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="del();">
						<img src="<%=root%>/images/operationbtn/del.png" />&nbsp;删&nbsp;除&nbsp;
					</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="list"></table>
		</td>
	</tr>
</table>	
</body>
<script type="text/javascript">
$(function(){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};

	$("#list").jqGrid({
		url:'<%=root%>/relativeworkflow/relativeWorkflow!showList.action?toPiId=${toPiId}',
		colModel :[ 
			{label:'processInstanceId',name:'processInstanceId', hidden:true},
			{label:'标题',name:'businessName',formatter:toLink,sortable:false},
			{label:'发起人',name:'startUserName',sortable:false, align:'center'},
			{label:'发起时间',name:'startDate',sortable:false, align:'center'},
			{label:'流程名称',name:'processName',sortable:false, align:'center'}
			],
		rowNum:10000,
		height:'331',
		onRightClickRow: rightMenu,
		loadComplete: function(){
			globalJqgrid.fillRows(this);
			gl.resize(this);
		}

	});
	
	$("#search").click(function(){
		var params = {};
		params.eaName = $.trim($("#eaName").val());
		params.eaClass = $.trim($("#eaClass").val());
		params.eatId = $.trim($("#eatId").val());
		params.isSearch = true;
		$("#list").setGridParam({postData: params,page:1}).trigger("reloadGrid");
	});
	
});

function reloadGrid(){
	$('#list').trigger("reloadGrid");
}

function toLink(cellvalue, options, rowObject){
	var val = "";
	if(cellvalue == "" || cellvalue == undefined){
		cellvalue = "<span style='font-style:italic;color:#ddd'>无</span>";
	}
	
	if(rowObject.processInstanceId){
		val = "<a class='rw_link' target=\"_blank\" href=\"<%=root%>/relativeworkflow/relativeWorkflow!viewProcessed.action?tempShow=1&toPiId="+rowObject.processInstanceId+"\">"+cellvalue+"</a>";
	}
	else{
		val = "<a class='rw_link' href=\"javascript:alert('该流程已删除，不能查看。');\">"+cellvalue+"</a>";
	}
	return val;
}

var option = {
		width : 150,
		items : [ 
		 		{
					text : "关联",
					icon : "<%=root%>/images/operationbtn/add.png",
					alias : "1-1",
					action :add	
				},
				{
					text : "删除",
					icon : "<%=root%>/images/operationbtn/del.png",
					alias : "1-3",
					action :del	
				}
		]
};

function add(){
	//location = "<%=root%>/relativeworkflow/relativeWorkflow!input.action?toPiId=${toPiId}";
	var ret = showModalDialog("<%=root%>/relativeworkflow/relativeWorkflow!input.action?toPiId=${toPiId}", 
            window,"dialogWidth:900px;dialogHeight:420px;status:no;help:no;scroll:yes;");
	if(ret == "success"){
		reloadGrid();
	}

}

function del(){
	var ids = $("#list").jqSelectedIds();
	if(ids != "" && confirm("确定要删除吗？")){
		$.get("<%=root%>/relativeworkflow/relativeWorkflow!delete.action?toIds="+ids)
			.done(function(res){
				if(res == "success"){
					//location.reload();
					reloadGrid();
				}
			});
	}
}
</script>
</html>
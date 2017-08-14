<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/root_path.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<TITLE>已办自由流程</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
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
.ui-pg-input{background-color:#ffffff;}
</style>

	</head>
	
	
	
<body style="background-color:#ffffff;">




<table id="main-table">
	<tr>
		<td class="table_headtd">
			<div id="main-title"><img style="vertical-align: middle;" src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;&nbsp;<strong>已办自由流程</strong></div>
			<table style="float:right" border="0" align="right" cellpadding="0" cellspacing="0">
				<tr>

					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="view();"><img
						src="<%=root%>/images/operationbtn/view.png" />&nbsp;查&nbsp;看&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>

					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="addTask();"><img
						src="<%=root%>/images/operationbtn/add.png" />&nbsp;追加任务&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="monitor();"><img
						src="<%=root%>/images/operationbtn/Monitor.png" />&nbsp;监&nbsp;控&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
				</tr>

			</table>
		</td>
	</tr>
	<tr>
		<td class="search-top">
			任务名称：<input id="ftTitle" name="ftTitle" type="text" size="20"/>&nbsp;&nbsp;
			<input type="button" id="img_search" />
		</td>
	</tr>
	<tr>
		<td>
			<table id="list"></table>
			<div id="pager"></div>
		</td>
	</tr>
</table>	
	
	


<a id="hidden-a" href="" target="_blank"></a>
</body>
<script type="text/javascript">

$(function(){
	var rightMenu = function(rowid, iRow, iCol, e){
		if(!rowid){
			return;
		}
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	var nextHandler = function(val){
		if(val == ""){
			val = "<i>无</i>";
		}
		return val;
	};

	$("#list").jqGrid({
		url:'<%=root%>/freedomworkflow/freedomWorkflow!doneShowList.action',
		colModel :[ 
			{label:'ftId',name:'ftId', hidden:true},
			{label:'fwId',name:'fwId', hidden:true},
			{label:'formId',name:'formId', hidden:true},
			{label:'任务名称',name:'ftTitle',sortable:false,resizable:false},
			{label:'流程名称',name:'fwTitle',sortable:false,resizable:false},
			{label:'当前处理人',name:'nextHandler',formatter:nextHandler,sortable:false,align:'center',resizable:false,width:50},
			{label:'发起人',name:'fwCreator',sortable:false,align:'center',resizable:false,width:50},
			{label:'任务完成时间',name:'ftEndTime',sortable:false,align:'center',resizable:false,width:80}
			],
		onRightClickRow: rightMenu,
		loadComplete: function(){
			globalJqgrid.fillRows(this);
			gl.resize(this);
		}
	});
	
	var search = function(){
		var params = {};
		params.ftTitle = $.trim($("#ftTitle").val());
		params.isSearch = true;
		$("#list").setGridParam({postData: params,page:1}).trigger("reloadGrid");
	};
	
	$("#img_search").click(function(){
		search();
	});
	
	$(".search-top").keydown(function(e){
		if(e.keyCode == 13){
			search();
		}
	});
	
});


var option = {
		width : 150,
		items : [ 
			 		{
						text : "查看",
						icon : "<%=root%>/images/operationbtn/view.png",
						alias : "1-1",
						action :view	
					},
			 		{
						text : "追加任务",
						icon : "<%=root%>/images/operationbtn/add.png",
						alias : "1-3",
						action :addTask	
					},
			 		{
						text : "监控",
						icon : "<%=root%>/images/operationbtn/Monitor.png",
						alias : "1-2",
						action :monitor	
					}
		]
};


function view(){
	var ftId = $("#list").getGridParam('selarrrow');		
	if(ftId.length < 1){
		alert('请选择要查看的记录。');
		return false;
	}
	else if(ftId.length >1){
		alert('只可以查看一条记录。');
		return false;
	}
	var row = $("#list").getRowData(ftId);
	var fwId = row.fwId;
	var formId = row.formId;
	if(fwId){
		open("<%=root%>/freedomworkflow/freedomWorkflow!view.action?formId="+formId+"&fwId="+fwId);
	}	
}

function addTask(){
	var ftId = $("#list").getGridParam('selarrrow');		
	if(ftId.length < 1){
		alert('请选择要追加任务的记录。');
		return false;
	}
	else if(ftId.length >1){
		alert('只可以追加任务一条记录。');
		return false;
	}
	var row = $("#list").getRowData(ftId);
	var fwId = row.fwId;
	if(fwId){
		var ret = gl.showDialog("<%=root%>/freedomworkflow/freedomWorkflowTask!input.action?toFwId="+fwId, 690, 600);
		if(ret){
			$("#list").trigger("reloadGrid");
		}
	}	
}

function monitor(){
	var ftId = $("#list").getGridParam('selarrrow');		
	if(ftId.length < 1){
		alert('请选择要监控的记录。');
		return false;
	}
	else if(ftId.length >1){
		alert('只可以监控一条记录。');
		return false;
	}
	var row = $("#list").getRowData(ftId);
	var fwId = row.fwId;
	if(fwId){
		location = "<%=root%>/freedomworkflow/freedomWorkflow!monitor.action?fwId="+fwId;
	}
}

</script>

</html>

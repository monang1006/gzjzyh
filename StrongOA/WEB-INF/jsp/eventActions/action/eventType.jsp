<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/include/root_path.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>事件动作类型</title>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet" />
<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />

<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>
<script type="text/javascript" src="<%=jsroot%>/jqgrid/jqgrid.js"></script>

<script type="text/javascript">

$(function(){
	//事件动作类型】IE10非兼容模式下查询条件输入不了内容  定位输入
	$("#eatName").focus();
	var rightMenu = function(rowid, iRow, iCol, e){
		if(!rowid){
			return;
		}
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};

	$("#list").jqGrid({
		url:'<%=root%>/eventActions/action/eventType!showList.action',
		colModel :[ 
			{label:'eatId',name:'eatId', hidden:true},
			{label:'类型名称',name:'eatName'},
			{label:'创建者',name:'creator',sortable:false,align:'center'},
			{label:'创建时间',name:'eatCreateDate',align:'center',sortable:false}
			],
		onRightClickRow: rightMenu,
		sortname: "eatCreateDate",
		sortorder: "desc",
		loadComplete: function(){
			globalJqgrid.fillRows(this);
			gl.resize(this);
		}
	});
	
	$("#img_search").click(function(){
		var params = {};
		params.eatName = $.trim($("#eatName").val());
		params.isSearch = true;
		$("#list").setGridParam({postData: params,page:1}).trigger("reloadGrid");
	});
});

var option = {
		width : 150,
		items : [ 
		 		{
					text : "新建",
					icon : "<%=root%>/images/operationbtn/add.png",
					alias : "1-1",
					action :add	
				},
				{
					text : "编辑",
					icon : "<%=root%>/images/operationbtn/edit.png",
					alias : "1-2",
					action :edit
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
	var url = "<%=root%>/eventActions/action/eventType!input.action";
	var ret = gl.showDialog(url,650,300);
	gl.msg(ret);

}

function edit(){
	var id = $("#list").jqSelectedId();
	if(id){
		var url = "<%=root%>/eventActions/action/eventType!input.action?toId="+id;
		var ret = gl.showDialog(url,650,300);
		gl.msg(ret);
	}
}

function del(){
	var id = $("#list").jqSelectedIds();
	if(id && confirm("如果删除事件动作类型，则该类型下的事件动作也会一起删除。\r\n确定要删除？")){
		$.ajax({
			url: "<%=root%>/eventActions/action/eventType!delete.action?toId="+id,
			cache: false,
			success :function(res){
				gl.msg(res);
			}
		});
	}
}

function reloadData(){
	$('#list').trigger("reloadGrid");
}

</script>

</head>

<body>
	<div id="contentborder" align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="15%">
											<img src="<%=frameroot%>/images/ico/ico.gif"/>
											<strong>事件动作类型列表</strong>
										</td>
										<td  class="tool-top">
			<table style="float:right" border="0" align="right" cellpadding="0" cellspacing="0">
				<tr>

					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="add();"><img
						src="<%=root%>/images/operationbtn/add.png" />&nbsp;新&nbsp;建&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="edit();"><img
						src="<%=root%>/images/operationbtn/edit.png" />&nbsp;编&nbsp;辑&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="del();"><img
						src="<%=root%>/images/operationbtn/del.png" />&nbsp;删&nbsp;除&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
				</tr>

			</table>
										</td>
									</tr>
									<tr height="30px" align="left">
										<td colspan="2" class="search-top">
											类型名称：<input id="eatName" name="eatName" type="text" size="20"/>
											<input type="button" id="img_search" value="" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					<tr>
					<td>

	<table id="list"></table>
	<div id="pager"></div>

					</td>
					</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
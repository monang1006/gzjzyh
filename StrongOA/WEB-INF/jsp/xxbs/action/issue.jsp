<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>期号管理</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<!--  <script type="text/javascript" src="<%=scriptPath%>/search.js"></script>-->
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							<button id="add" class="input_button_4"><img src="<%=themePath%>/image/ico_add.gif"/>新建</button>
						</li>
						<li>
							<button id="edit" class="input_button_4"><img src="<%=themePath%>/image/ico_edit.gif"/>修改</button>
						</li>
						<li>
							<button id="delete" class="input_button_4"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
		<s:form theme="simple" id="myTableForm" action="">
					<table id="list"></table>
					<div id="pager"></div>
		</s:form>
				</div>
				</div>

<div id="mask"></div>
	</body>
</html>
<script type="text/javascript">
var add = function(){
	var url = "<%=root%>/xxbs/action/issue!input.action?op=add&toId=${toId}";
	var ret = gl.showSubDialog(url,600,350);
	gl.msg(ret, "保存成功");
};
$("#add").click(add);

var edit = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/action/issue!input.action?op=edit&toId="+id;
		var ret = gl.showSubDialog(url,600,350);
		gl.msg(ret, "保存成功");
	}
};
$("#edit").click(edit);

var del = function(){
	var id = $("#list").jqDeleteId();
	if(id=="")
	{
		alert("最少选择一项!");
		return false;
	}
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/issue!delete.action?toId="+id, function(response){
			if(response == "noDelete"){
				alert("期号已经使用，不能删除。");
			}
			if(response == "success"){
				gl.msg(response, "删除成功");
			}
		});
	}
};
$("#delete").click(del);


var option = {
	width : 150,
	items : [{
		text : "新建期号",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "修改期号",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-5",
		action : edit
	}, 
	{
		text : "删除期号",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	}
	]
};

$(function(){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	$("#list").jqGrid({
		url:'${root}/xxbs/action/issue!showList.action?toId=${toId}',
		colModel :[ 
			{label:'issId',name:'issId', hidden:true}, 
			{label:'期号',name:'issNumber',align:'center',sortable:false,width:50},
			{label:'所属期刊',name:'jourName',align:'center',sortable:false,width:50},
			{label:'期号时间',name:'issTime',align:'center',sortable:false,width:50}
			],
		onRightClickRow: rightMenu,
		gridComplete: gl.resize,
		sortname: 'issNumber',
		sortorder: 'desc'
	});
			
	$("#img_sousuo").click(function(){
  	 	var colName = $("#colName").val();
       	var searchParam = {};
	 	searchParam.colName = $.trim(colName);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam}).trigger("reloadGrid");		
	});
});



function reloadData(){
	$('#list').trigger("reloadGrid");
}

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

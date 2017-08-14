<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>期刊管理</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<!--  <script type="text/javascript" src="<%=scriptPath%>/search.js"></script>-->
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							<button id="add" class="input_button_6"><img src="<%=themePath%>/image/ico_add.gif"/>新建期刊</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								期刊名称：
								<s:textfield name="jourName" id="jourName"
									cssClass="main_search_input search" title="请输入期刊名称"
									theme="simple"></s:textfield>
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo">
							</td>
						</tr>
					</table>
					<table id="list"></table>
					<div id="pager"></div>
				</div>
				</div>



	</body>
</html>
<script type="text/javascript">
var add = function(){
	var url = "<%=root%>/xxbs/action/journal!input.action?op=add";
	var ret = gl.showDialog(url,600,350);
	gl.msg(ret, "保存成功");
};
$("#add").click(add);

var del = function(){
	var id = $("#list").getGridParam('selrow');
	if(confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/journal!delete.action?toId="+id, function(response){
			if(response == "noDelete"){
				alert("期刊已经使用，不能删除。");
			}
			if(response == "success"){
				gl.msg(response, "删除成功");
			}
		});
	}
};
$("#delete").click(del);

var edit = function(){
	var id = $("#list").getGridParam('selrow');
	var url = "<%=root%>/xxbs/action/journal!input.action?op=edit&toId="+id;
	var ret = gl.showDialog(url,600,350);
	gl.msg(ret, "保存成功");		
};
$("#edit").click(edit);


var addIssue = function(){
	var id = $("#list").getGridParam('selrow');
	var url = "<%=root%>/xxbs/action/issue!input.action?op=add&toId="+id;
	var ret = gl.showDialog(url,600,350);
	gl.msg(ret, "保存成功");
};

var listIssue = function(){
	var id = $("#list").getGridParam('selrow');
	var url = "${root}/xxbs/action/issue.action?toId="+id;
	gl.showDialog(url,600,350);
};

var addColumn = function(){
	var id = $("#list").getGridParam('selrow');
	var url = "<%=root%>/xxbs/action/column!input.action?op=add&toId="+id;
	var ret = gl.showDialog(url,600,350);
	gl.msg(ret, "保存成功");
};

var listColumn = function(){
	var id = $("#list").getGridParam('selrow');
	var url = "${root}/xxbs/action/column.action?toId="+id;
	gl.showDialog(url,600,350);
};



var option = {
	width : 150,
	items : [{
		text : "新建期刊",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "修改期刊",
		icon : "<%=themePath%>/image/ico_edit.gif",
		alias : "1-2",
		action : edit
	}, 
	{
		text : "删除期刊",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	},
	{
		type:'splitLine'
	},
	{
		text : "新建期号",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-4",
		action : addIssue
	}, 
	{
		text : "管理期号",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-5",
		action : listIssue
	}, 
	{
		type:'splitLine'
	},
	{
		text : "新建栏目",
		icon : "<%=themePath%>/image/ico_edit.gif",
		alias : "1-6",
		action : addColumn
	}, 
	{
		text : "管理栏目",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-7",
		action : listColumn
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
		url:'${root}/xxbs/action/journal!showList.action',
		colModel :[ 
			{label:'jourId',name:'jourId', hidden:true}, 
			{label:'期刊名称',name:'jourName', width:90,sortable:false}, 
			{label:'期刊得分',name:'jourCode', width:50, align:"center",sortable:false}, 
			{label:"添加时间", name:"jourDate", align:"center", width:40,sortable:false}
			],
		onRightClickRow: rightMenu,
		gridComplete: gl.resize,
		sortname: 'jourDate',
		multiselect: false,
		onCellSelect: listIssue
	});
	
	$("#img_sousuo").click(function(){
  	 	var jourName = $("#jourName").val();
       	var searchParam = {
       		jourName : $.trim(jourName),
       		isSearch : true
      	};
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");		
	});
});


function reloadData(){
	$('#list').trigger("reloadGrid");
}

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>栏目管理</title>

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
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								栏目名称：
								<s:textfield name="colName" id="colName"
									cssClass="main_search_input search" title="请输入栏目名称"
									theme="simple"></s:textfield>
								期刊名称：
								
								<s:select id="jourName" list="%{jourMap}" headerKey="all" headerValue="全部" name="jourName"/>
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo">
							</td>
						</tr>
					</table>
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
	var url = "<%=root%>/xxbs/action/column!input.action?op=add&toId=${toId}";
	var ret = gl.showSubDialog(url,700,500);
	gl.msg(ret, "保存成功");
};
$("#add").click(add);

var edit = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/action/column!input.action?op=edit&toId="+id;
		var ret = gl.showSubDialog(url,700,500);
		gl.msg(ret, "保存成功");
	}
};
$("#edit").click(edit);

var del = function(){
	var id = $("#list").jqDeleteId();
	if(id==""){
		alert("请选择一项进行操作。");
		return false;
	}
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/column!delete.action?toId="+id, function(response){
			if(response == "noDelete"){
				alert("栏目已经使用，不能删除。");
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
		text : "新建栏目",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "修改栏目",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-5",
		action : edit
	}, 
	{
		text : "删除栏目",
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
		url:'${root}/xxbs/action/column!showList.action?toId=${toId}',
		colModel :[ 
			{label:'colId',name:'colId', hidden:true}, 
			{label:'栏目名称',name:'colName',sortable:false},
			{label:'所属期刊',name:'jourName',sortable:false},
			{label:'栏目得分',name:'colCode',sortable:false,width:50,align:'center'}
			],
		onRightClickRow: rightMenu,
		gridComplete: gl.resize,
		sortname: 'colSort',
		sortorder: 'asc'
	});
			
	$("#img_sousuo").click(function(){
  	 	var colName = $("#colName").val();
  	 	var jourName = $("#jourName").val();
       	var searchParam = {};
	 	searchParam.colName = $.trim(colName);
	 	searchParam.jourName = $.trim(jourName);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");		
	});
});



function reloadData(){
	$('#list').trigger("reloadGrid");
}


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

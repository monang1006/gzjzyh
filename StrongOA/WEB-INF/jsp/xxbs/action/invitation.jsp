<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>约稿管理</title>

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
			 <s:if test="%{submitStatus=='all'}">
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							<button id="add" class="input_button_4" onclick="add();"><img src="<%=themePath%>/image/ico_add.gif"/>新建</button>
						</li>
						<li>
							<button id="edit" class="input_button_4" onclick="edit();"><img src="<%=themePath%>/image/ico_edit.gif"/>编辑</button>
						</li>
						<li>
							<button id="delete" class="input_button_4" onclick="del();"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
						</li>
						<li>
							<button id="view" class="input_button_4" onclick="view();"><img src="<%=themePath%>/image/ico_view.gif"/>查看</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>
			</s:if>
			<s:else>
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							<button id="view" class="input_button_4" onclick="view();"><img src="<%=themePath%>/image/ico_view.gif"/>查看</button>
						</li>
					</ul>
					<br style="clear:both" />
				</div>
			</s:else>
				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
		<s:form theme="simple" id="myTableForm" action="">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								约稿标题：
								<s:textfield name="aptTitle" id="aptTitle"
									cssClass="main_search_input search" title="请输入约稿标题"
									theme="simple"></s:textfield>
								发布时间：
								<s:textfield name="aptDate" id="aptDate"
									cssClass="main_search_input search" title="请输入发布时间"
									theme="simple"></s:textfield>
								<web:datetime format="yyyy-MM-dd" readOnly="true" id="aptDate" />
								截止时间：
								<s:textfield name="aptDuedate" id="aptDuedate"
									cssClass="main_search_input search" title="请输入截止时间"
									theme="simple"></s:textfield>
								<web:datetime format="yyyy-MM-dd" readOnly="true" id="aptDuedate" />					
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo">
							</td>
						</tr>
					</table>
					<table id="list"></table>
					<div id="pager"></div>
		</s:form>
				</div>
				</div>



	</body>
</html>
<script type="text/javascript">

var rightMenu;
var option = {
	width : 150,
	items : [{
		text : "新建",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "编辑",
		icon : "<%=themePath%>/image/ico_edit.gif",
		alias : "1-2",
		action : edit
	}, 
	{
		text : "删除",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-4",
		action : del
	},
	{
		text : "查看",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-3",
		action : view
	}
	]
};
	
$(function(){
	var s = "${submitStatus}";
	if(s!=1){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	$("#list").jqGrid({
		url:'${root}/xxbs/action/invitation!showList.action',
		colModel :[ 
			{label:'aptId',name:'aptId', hidden:true}, 
			{label:'约稿标题',name:'aptTitle'}, 
			{label:"发布时间", name:"aptDate", align:"center", width:80},
			{label:"截止时间", name:"aptDuedate", align:"center", width:80}
			],
		onRightClickRow: rightMenu,
		onCellSelect: onCellSelect,
		gridComplete: gl.resize,
		sortname:'aptDate'
		});
	}
	else{
	$("#list").jqGrid({
		url:'${root}/xxbs/action/invitation!showList.action?isSinceToday=true',
		colModel :[ 
			{label:'aptId',name:'aptId', hidden:true}, 
			{label:'约稿标题',name:'aptTitle'}, 
			{label:"发布时间", name:"aptDate", align:"center", width:80},
			{label:"截止时间", name:"aptDuedate", align:"center", width:80}
			],
		onRightClickRow: rightMenu,
		onCellSelect: onCellSelect,
		gridComplete: gl.resize,
		sortname:'aptDate'
	});
	}
	
	$("#img_sousuo").click(function(){
  	 	var aptTitle = $("#aptTitle").val();
  	 	var aptDate = $("#aptDate").val();
  	 	var aptDuedate = $("#aptDuedate").val();
  	 	if(aptDate == "" && aptDuedate !=""){
  	 		alert("请选择发布时间。");
  	 		return;
  	 	}
  	 	if(aptDate != "" && aptDuedate ==""){
  	 		alert("请选择截止时间。");
  	 		return;
 	 	}
  	 	if(aptDate > aptDuedate){
  	 		alert("发布时间不能大于截止时间。");
  	 		return;
  	 	}
       	var searchParam = {};
	 	searchParam.aptTitle = $.trim(aptTitle);
	 	searchParam.aptDate = $.trim(aptDate);
	 	searchParam.aptDuedate = $.trim(aptDuedate);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");		
	});
});


var onCellSelect = function(rowid, iCol){
	var submitStatus = '${submitStatus}';
	if(submitStatus=='all'){
	if(iCol > 1){
		commonEdit(rowid);
	}
	}
};

function add(){
	var ret = gl.showDialog("<%=root%>/xxbs/action/invitation!input.action?op=add",800,700);
	gl.msg(ret, "保存成功");
}

function edit(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		commonEdit(id);
	}
}

function view(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		gl.showDialog("<%=root%>/xxbs/action/invitation!input.action?op=view&toId="+id,800,700);
	}
}

function commonEdit(id){
	var ret = gl.showDialog("<%=root%>/xxbs/action/invitation!input.action?op=edit&toId="+id,800,700);
	gl.msg(ret, "保存成功");
}

function del(){
	var id = $("#list").jqDeleteId();
	if(id==""){
		alert("请选择一项进行操作。");
		return false;
	}
	if(confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/invitation!delete.action?toId="+id, function(response){
			if(response == "notDelete"){
				alert("已报使用的约稿信息不能删除！");
			}
			else if(response == "success"){
				gl.msg(response, "删除成功");
			}
		});
	}
}

function reloadData(){
	$('#list').trigger("reloadGrid");
}

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

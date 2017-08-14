<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>呈阅件</title>


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
								标题：
								<s:textfield name="pieceTitle" id="pieceTitle"
									cssClass="main_search_input search" title="请输入标题"
									theme="simple"></s:textfield>
								<img src="<%=imgPath%>/sousuo.gif" width="17"
									id="img_sousuo" height="16" class="main_search_img_button"
									title="单击搜索">
							</td>
						</tr>
					</table>
					<table id="list"></table>
					<div id="pager"></div>
		</s:form>
				</div>
				</div>

		<input type="hidden" value="${orgId}" name="orgId" id="orgId"/>

	</body>
</html>
<script type="text/javascript">
var flag = "${flag}";
var add = function(){
	var flag = "${flag}";
	var orgId = $("#orgId").val();
	if(orgId==''){
		alert("请先选择机构，再进行添加！");
		return false;
	}
	
	if(flag=="guoban"){
		var url = "<%=root%>/xxbs/action/piece!input.action?op=add&orgId="+orgId+"&flag=guoban";
	}
	else if(flag=="shengji"){
		var url = "<%=root%>/xxbs/action/piece!input.action?op=add&orgId="+orgId+"&flag=shengji";
	}
	else{
	var url = "<%=root%>/xxbs/action/piece!input.action?op=add&orgId="+orgId;
	}
	var ret = gl.showDialog(url,600,450);
	gl.msg(ret, "保存成功");
};
$("#add").click(add);

var edit = function(){
	var orgId = $("#orgId").val();
	var flag = "${flag}";
	var id = $("#list").jqSelectedId();
	if(id != false){
		if(flag=="guoban"){
		var url = "<%=root%>/xxbs/action/piece!input.action?op=edit&toId="+id+"&flag=guoban";
		}
		else if(flag=="shengji"){
			var url = "<%=root%>/xxbs/action/piece!input.action?op=edit&toId="+id+"&flag=shengji";
		}
		else{
			var url = "<%=root%>/xxbs/action/piece!input.action?op=edit&toId="+id;
		}
		var ret = gl.showDialog(url,600,450);
		gl.msg(ret, "保存成功");
	}
};

var view = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		if(flag=="guoban"){
		var url = "<%=root%>/xxbs/action/piece!input.action?op=view&toId="+id+"&flag=guoban";
		}
		else{
			var url = "<%=root%>/xxbs/action/piece!input.action?op=view&toId="+id;
		}
		var ret = gl.showDialog(url,800,450);
	}
};
$("#view").click(view);

$("#edit").click(edit);

var del = function(){
	var id = $("#list").jqDeleteId();
	if(id.length<1){
		alert("最少选择一项");
		return;
	}
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/piece!delete.action?toId="+id, function(response){
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
		text : "新建",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "修改",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-5",
		action : edit
	}, 
	{
		text : "删除",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	}
	]
};

var option = {
		width : 150,
		items : [{
			text : "新建",
			icon : "<%=themePath%>/image/ico_add.gif",
			alias : "1-1",
			action : add
		}, 
		{
			text : "修改",
			icon : "<%=themePath%>/image/ico_edit.gif",
			alias : "1-5",
			action : edit
		}, 
		{
			text : "删除",
			icon : "<%=themePath%>/image/ico_del.gif",
			alias : "1-3",
			action : del
		}
		]
	};

$(function(){
	var orgId = $("#orgId").val();
	var flag = "${flag}";
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	if(flag=="guoban"){
		$("#list").jqGrid({
			url:'${root}/xxbs/action/piece!showList.action?orgId='+orgId+'&flag=guoban',
			colModel :[ 
				{label:'pieceId',name:'pieceId', hidden:true,sortable:false}, 
				{label:'呈国办标题',name:'pieceTitle',sortable:false},
				{label:'呈国办时间',name:'pieceTime',sortable:false},
				{label:'得分',name:'pieceCode',align:'center',sortable:false},
				{label:'机构名称',name:'orgName',align:'center',sortable:false}
				],
			onRightClickRow: rightMenu,
			gridComplete: gl.resize,
			sortname: 'pieceTime'
		});
		
	}
	else if(flag=="shengji"){
		$("#list").jqGrid({
			url:'${root}/xxbs/action/piece!showList.action?orgId='+orgId+'&flag=shengji',
			colModel :[ 
				{label:'pieceId',name:'pieceId', hidden:true,sortable:false}, 
				{label:'省级标题',name:'pieceTitle',sortable:false},
				{label:'上报时间',name:'pieceTime',sortable:false},
				{label:'得分',name:'pieceCode',align:'center',sortable:false},
				{label:'省级类型',name:'pieceOpen', formatter:fStatus,align:'center',sortable:false},
				{label:'是否批示',name:'isInstruction', formatter:isYes,align:'center',sortable:false},
				{label:'机构名称',name:'orgName',align:'center',sortable:false}
				],
			onRightClickRow: rightMenu,
			gridComplete: gl.resize,
			sortname: 'pieceTime'
		});
	}
	
	else{
		$("#list").jqGrid({
			url:'${root}/xxbs/action/piece!showList.action?orgId='+orgId,
			colModel :[ 
				{label:'pieceId',name:'pieceId', hidden:true,sortable:false}, 
				{label:'呈阅件标题',name:'pieceTitle',sortable:false},
				{label:'呈阅件时间',name:'pieceTime',sortable:false},
				{label:'得分',name:'pieceCode',align:'center',sortable:false},
				{label:'机构名称',name:'orgName',align:'center',sortable:false}
				],
			onRightClickRow: rightMenu,
			gridComplete: gl.resize,
			sortname: 'pieceTime'
		});
	}
	
			
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	$("#img_sousuo").click(function(){
  	 	var pieceTitle = $("#pieceTitle").val();
       	var searchParam = {};
	 	searchParam.pieceTitle = $.trim(pieceTitle);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam}).trigger("reloadGrid");		
	});
});

var fStatus = function(el, cellval, opts) {
	temp = "";
	if (el == "0") {
		temp = "每日要情";
	}
	else if (el == "1") {
		temp = "江西政务";
	}
	return temp;
};

var isYes = function(val) {
	var temp = "";
	if (val == 1) {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='是'>";
	}
	return temp;
};

function reloadData(){
	$('#list').trigger("reloadGrid");
}

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

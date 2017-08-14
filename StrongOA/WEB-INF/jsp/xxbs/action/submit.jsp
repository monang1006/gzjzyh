<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>已报信息</title>

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
		<style type="text/css">
		.ui-jqgrid-resize-ltr{width: 0px;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
							<button id="add" class="input_button_4" onclick="add();"><img src="<%=themePath%>/image/ico_add.gif"/>新建</button>
						</li>
						<li>
							<button id="show" class="input_button_4" onclick="del();"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid" >
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="60%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								信息标题：
								<s:textfield name="pubTitle" id="pubTitle"
									cssClass="main_search_input search" title="请输入信息标题"
									theme="simple"></s:textfield>
								报送时间：
								<s:textfield name="startDate" id="startDate"
									cssClass="search Ndate main_search_input" title="请输入开始时间"
									theme="simple"></s:textfield>-
								<s:textfield name="endDate" id="endDate"
									cssClass="search Ndate main_search_input" title="请输入结束时间"
									theme="simple"></s:textfield>
								<web:datetime format="yyyy-MM-dd" readOnly="false" id="startDate" />
								<web:datetime format="yyyy-MM-dd" readOnly="false" id="endDate" />									
								<input type="button" value="搜索"  style="width: 40px" id="img_sousuo1">
							</td>
							<td width="39%" align="right">
								过滤：<s:select id="submitStatus" name="submitStatus" list="#{\"all\":'所有', \"1\":'已报送',\"0\":'待报送',\"3\":'已点评',\"2\":'已共享'}" value="%{submitStatus}"></s:select>
								<!--  <strong>（采用数：${usedNum} 报送数：${submittedNum}）</strong>-->
							</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					<table id="list"></table>
					<div id="pager"></div>
				</div>
				</div>



	</body>
</html>
<script type="text/javascript">

var option = {
	width : 150,
	items : [{
		text : "新建报送信息",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "查看/编辑报送信息",
		icon : "<%=themePath%>/image/ico_edit.gif",
		alias : "1-2",
		action : showOrEdit
	}, 
	{
		text : "删除报送信息",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	}	]
};
	
$(function(){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	$("#list").jqGrid({
		url:'${root}/xxbs/action/submit!listSubmit.action?submitStatus=${submitStatus}&isShared=${isShared}',
		colModel :[ 
			{label:'pubId',name:'pubId', hidden:true}, 
			{label:'submitStatus',name:'submitStatus', hidden:true}, 
			{label:'信息标题',name:'pubTitle',sortable:false,width:400}, 
			{label:"报送时间", name:"pubDate", align:"center", formatter:fDate,sortable:false,width:150},
			{label:"报送", name:"pubSubmitStatus", formatter:isYes, align:"center",sortable:false},
			{label:"约稿", name:"isAppoint", formatter:isYes, align:"center",sortable:false},
			{label:"采用", name:"pubUseStatus", formatter:isYes, align:"center",sortable:false},
			{label:"共享", name:"pubIsShare", formatter:isYes, align:"center",sortable:false},
			{label:"批示",name:'pubIsInstruction', formatter:isYes,  align:'center',sortable:false}, 
			{label:"点评", name:"pubIsComment", formatter:isYes, align:"center",sortable:false}
			],
		sortname:'pubDate',
		onRightClickRow: rightMenu,
		onCellSelect: onCellSelect,
		gridComplete: gl.resize,
		grouping:true, 
		sortorder: 'desc'
	});
	
	$("#img_sousuo1").click(function(){
  	 	var startDate = $("#startDate").val();
  	 	var endDate = $("#endDate").val();
  	 	if(startDate == "" && endDate !=""){
  	 		alert("请选择开始时间。");
  	 		return;
  	 	}
  	 	if(startDate != "" && endDate ==""){
  	 		alert("请选择结束时间。");
  	 		return;
 	 	}
  	 	if(startDate > endDate){
  	 		alert("开始时间不能大于结束时间。");
  	 		return;
  	 	}
       	var searchParam = {};
	 	searchParam.pubTitle = $.trim($("#pubTitle").val());
	 	searchParam.startDate = startDate;
	 	searchParam.endDate = endDate;
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");		
	});
	
});

var onCellSelect = function(rowid, iCol){
	if(iCol > 1){
		var rowData = $(this).getRowData(rowid);
		if(rowData.submitStatus == "1"){
			commonShow(rowid);
		}
		else{
			commonEdit(rowid);
		}
	}
};

function showOrEdit(){
	var rowid = $("#list").jqLastId();
	var rowData = $("#list").getRowData(rowid);
	if(rowData.submitStatus == "1"){
		commonShow(rowid);
	}
	else{
		commonEdit(rowid);
	}	
} 

function add(){
	var url = "<%=root%>/xxbs/action/submit!input.action?op=add";
	var w = gl.windowWidth();
	var h = $(window).height();
	var ret = gl.showDialog(url,1000,800);
	gl.msg(ret, "保存成功");
}

function edit(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		commonEdit(id);
	}
}

function commonEdit(id){
	var url = "<%=root%>/xxbs/action/submit!input.action?op=edit&toId="+id;
	var w = gl.windowWidth();
	var h = $(window).height();
	var ret = gl.showDialog(url,1000,800);
	gl.msg(ret, "保存成功");	
}

function show(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		commonShow(id);
	}
}

function commonShow(id){
	var url = "<%=root%>/xxbs/action/submit!view.action?toId="+id;
	var w = gl.windowWidth();
	var h = $(window).height();
	gl.showDialog(url,1000,800);
}

function del(){
	var id = $("#list").jqDeleteId();
	if(id=="")
	{
		alert("最少选择一项!");
		return false;
	}
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/action/submit!deleteNotSubmitted.action?toId="+id, function(response){
			if(response == "notDelete"){
				alert("已报送的信息不能删除！");
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

var infotype = function(val) {
	var temp = "";
	if(val == 0){
		temp = "普通信息";
	}
	else if (val == 1) {
		temp = "涉密信息";
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

var fDate = function(val,opts, obj){
	var temp = val;
	if (obj.submitStatus == 0) {
		temp = "";
	}
	return temp;
};


$("#submitStatus").change(function(){
	location = "<%=root%>/xxbs/action/submit.action?submitStatus="+$(this).val();
});


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

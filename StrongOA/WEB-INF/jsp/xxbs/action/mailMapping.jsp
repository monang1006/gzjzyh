<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>信息处理</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
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
							<button id="use" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>采用</button>
						</li>
						<li>
							<button id="assign" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>指派</button>
						</li>
						<li>
							<button id="share" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>共享</button>
						</li>
						<li>
							<button id="show" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>查看</button>
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
var add = function(){
	var url = "<%=root%>/xxbs/info/submit!input.action?op=add";
	var w =	$(window).width() * 0.9;
	var h = $(window).height();
	var ret = showDialog(url,w,h);
	GShowOk(ret, "保存成功");
};
$("#add").click(add);

var del = function(){
	var id = $("#list").jqSelectedId();
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/info/submit!deleteSubmitted.action?toId="+id, function(response){
			if(response == "success"){
				GShowOk(response, "删除成功");
			}
			else if(response == "notDelete"){
				alert("已被预采用或采用的报送信息不允许删除。");
			}
		});
	}
};
$("#delete").click(del);

var use = function(){
	aaa();
};
$("#use").click(use);

var share = function(){
	var id = $("#list").getGridParam('selarrrow');
	var url = "<%=root%>/xxbs/info/handling!viewShare.action?toId="+id;
	ret = showDialog(url,420,180);
	G.msg(ret, "更新共享成功");
};
$("#share").click(share);

var assign = function(){
	var ret = "";
	var id = $("#list").getGridParam('selarrrow');
	if(id.length <=1){
		var url = "<%=root%>/xxbs/info/handling!inputAssign.action?toId="+id;
		ret = showDialog(url,580,350);
	}
	else{
		
	}
	G.msg(ret, "更新指派成功");
};
$("#assign").click(assign);

var show = function(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/info/submit!view.action?toId="+id;
		var w = $(window).width();
		var h = $(window).height();
		showDialog(url,w,h);
	}
};
$("#show").click(show);

var option = {
	width : 150,
	items : [{
		text : "新建报送信息",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "查看报送信息",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-2",
		action : show
	}, 
	{
		text : "采用",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-4",
		action : use
	}, 
	{
		text : "共享",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-5",
		action : share
	}, 
	{
		text : "指派",
		icon : "<%=themePath%>/image/ico_view.gif",
		alias : "1-6",
		action : assign
	}, 
	{
		text : "删除报送信息",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	}
	]
};

var aaa = function(){
	$("#list").setGridParam({scrollrows:true});
	$("#list").setSelection("402882b93565674801356567c24d0001");
};
	
$(function(){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	$("#list").jqGrid({
		url:'${root}/xxbs/info/handling!showList.action?qs=${qs}',
		colModel :[ 
			{label:'pubId',name:'pubId', hidden:true}, 
			{label:'信息标题',name:'pubTitle', width:70}, 
			{label:'报送单位',name:'orgName', width:60}, 
			{label:'信息类型',name:'pubInfotype', formatter: infotype, width:30, align:'center'}, 
			{label:"报送时间", name:"pubDate", align:"center", width:30},
			{label:"是否采用", name:"pubUsestatus", formatter:isYes, align:"center", width:20},
			{label:"是否共享", name:"pubIsshare", formatter:isYes, align:"center", width:20},
			{label:"是否指派", name:"pubIsassign", formatter:isYes, align:"center", width:20}
			],
		onRightClickRow: rightMenu,
		gridComplete: G.resize,
		height: '100%',
	    grouping:true, 
	    groupingView : { 
	       groupField : ['orgName'],
	       groupOrder: ['desc'],
	       groupDataSorted : false
	    }
	});
			
	$("#img_sousuo").click(function(){
  	 	var aptTitle = $("#aptTitle").val();
  	 	var aptDate = $("#aptDate").val();
  	 	var aptDuedate = $("#aptDuedate").val();
       	var searchParam = {};
	 	searchParam.aptTitle = $.trim(aptTitle);
	 	searchParam.aptDate = $.trim(aptDate);
	 	searchParam.aptDuedate = $.trim(aptDuedate);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam}).trigger("reloadGrid");		
	});
});



function reloadData(){
	$('#list').trigger("reloadGrid");
}

var infotype = function(el, cellval, opts) {
	temp = "";
	if(el == 0){
		temp = "普通信息";
	}
	else if (el == 1) {
		temp = "涉密信息";
	}
	return temp;
};

var isYes = function(el, cellval, opts) {
	temp = "";
	if (el == "1") {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='是'>";
	}
	return temp;
};

var selectRow = function(rowid){
	$('#list').resetSelection();
	$('#list').setSelection(rowid);
};

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>待报信息</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
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
							<button id="edit" class="input_button_4" onclick="edit();"><img src="<%=themePath%>/image/ico_edit.gif"/>编辑</button>
						</li>
						<li>
							<button id="delete" class="input_button_4" onclick="del();"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
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
								信息标题：
								<s:textfield name="pubTitle" id="pubTitle"
									cssClass="main_search_input search" title="请输入信息标题"
									theme="simple"></s:textfield>
								添加时间：
								<s:textfield name="pubDate" id="pubDate"
									cssClass="main_search_input search" title="请输入添加时间"
									theme="simple"></s:textfield>
								<web:datetime format="yyyy-MM-dd" readOnly="true" id="pubDate" />
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



	</body>
</html>
<script type="text/javascript">

var option = {
	width : 150,
	items : [{
		text : "新建信息报送",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : add
	}, 
	{
		text : "编辑待报信息",
		icon : "<%=themePath%>/image/ico_edit.gif",
		alias : "1-2",
		action : edit
	}, 
	{
		text : "删除待报信息",
		icon : "<%=themePath%>/image/ico_del.gif",
		alias : "1-3",
		action : del
	}
	]
};
	
$(function(){
	//rightMenu = $.fn.contextmenu(option);
	
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	
	$("#list").jqGrid({
	    url:'${root}/xxbs/info/submit!listNotSubmitted.action',
	    colModel :[ 
	      {label:'pubId',name:'pubId', hidden:true}, 
	      {label:'信息标题',name:'pubTitle', width:90}, 
	      {label:'信息类型',name:'pubInfoType', 
				formatter: infotype, width:80, align:'center'}, 
	      {label:'是否约稿',name:'pubIsAppoint', 
				formatter: isappoint, width:80, align:'center'}, 
	      {label:'添加时间',name:'pubDate', width:80, align:'center'}
	    ],
	    sortname:'pubDate',
	    onRightClickRow: rightMenu,
	    gridComplete: gl.resize
	});
	
	$("#img_sousuo").click(function(){
  	 	var pubTitle = $("#pubTitle").val();
  	 	var pubDate = $("#pubDate").val();
       	var searchParam = {};
	 	searchParam.pubTitle = $.trim(pubTitle);
	 	searchParam.pubDate = $.trim(pubDate);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam}).trigger("reloadGrid");		
	});
});

function add(){
	var url = "<%=root%>/xxbs/info/submit!input.action?op=add";
	var w =	760;
	var h = 550;
	var ret = gl.showDialog(url,w,h);
	gl.msg(ret, "保存成功");
}

function edit(){
	var id = $("#list").jqSelectedId();
	if(id != false){
		var url = "<%=root%>/xxbs/info/submit!input.action?op=edit&toId="+id;
		var w =	760;
		var h = 550;
		var ret = gl.showDialog(url,w,h);
		gl.msg(ret, "保存成功");
	}
}

function del(){
	var id = $("#list").jqSelectedId();
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/info/submit!deleteNotSubmitted.action?toId="+id, function(response){
			gl.msg(response, "删除成功");
		});
	}
}

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

var isappoint = function(el, cellval, opts) {
	temp = "";
	if (el == 1) {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='是'>";
	}
	return temp;
};

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

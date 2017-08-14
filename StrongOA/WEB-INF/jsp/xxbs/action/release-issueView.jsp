<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>上报端更多期刊</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
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
							<button id="view" class="input_button_4"><img src="<%=themePath%>/image/ico_add.gif"/>查看</button>
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
var view = function(){
	var id = $("#list").jqSelectedId();
	if(id!=false){
		var h = $(window).height();
		var url = "<%=root%>/xxbs/action/release!content1.action?selView=1&toId="+id;
		gl.showDialog(url,990,h);
	}
};
$("#view").click(view);


var option = {
	width : 150,
	items : [{
		text : "查看",
		icon : "<%=themePath%>/image/ico_add.gif",
		alias : "1-1",
		action : view
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
		url:'${root}/xxbs/action/release!showJdbcList.action',
		colModel :[ 
			{label:'issId',name:'issId', hidden:true}, 
			{label:'期号',name:'issNumber'},
			{label:'所属期刊',name:'jourName'}
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

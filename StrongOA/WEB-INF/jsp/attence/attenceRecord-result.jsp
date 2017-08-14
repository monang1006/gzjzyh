<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>

<HTML style="height:100%;">
	<HEAD>

		<TITLE>考勤信息列表</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=themePath%>/css/global.css" rel="stylesheet"
			type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />

		<style type="text/css">
.input_button {
	background: url("../image/button_4.jpg") no-repeat scroll 0 0
		transparent;
	border: 1px solid #999999;
	cursor: pointer;
	float: left;
	font-size: 12px;
	height: 20px;
	line-height: 20px;
	text-align: center;
	width: 85px;
}
</style>

		<script type="text/javascript"
			src="<%=scriptPath%>/jquery-1.4.1.min.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js">
</script>
		<script type="text/javascript"
			src="<%=scriptPath%>/grid/grid.locale-en.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/global.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_zh_CN.js">
</script>
		<script language="javascript" src="<%=scriptPath%>/component-min.js">
</script>
		<script language="javascript" src="<%=scriptPath%>/search.js">
</script>

		<script language="javascript">

var hasResize = false;//判断是否已经响应了窗口resize事件，IE6 sp3版本之前使用
var initWidth;

$(document).ready(function() {

	var query = location.search.substring(1);//获取前一页面传过来的url参数，并赋给隐藏域
		var pairs = query.split("?");
		if (pairs.length > 1) {
			var params = pairs[1];
			var param = params.split("&");
			$("#startTime").val(param[0].split("=")[1]);
			$("#endTime").val(param[1].split("=")[1]);
			$("#userID").val(param[2].split("=")[1]);
		}

		initWidth = $("#menu_top").outerWidth()
				- ($("#menu_top").outerWidth() - $("#menu_top").width()) / 2;

		$(window).bind(
				"resize",
				function() {
					if ($.browser.msie && ($.browser.version == "6.0")
							&& !$.support.style) {//处理IE6浏览器
						if (!hasResize) {
							hasResize = true;
							var width = $("#menu_top").outerWidth()
									- ($("#menu_top").outerWidth() - $(
											"#menu_top").width()) / 2;
							$("#list").setGridWidth(width, true);
						}
						setTimeout(function() {
							hasResize = false;
						}, 1000);
					} else {//其它浏览器
						var width = $("#menu_top").outerWidth()
								- ($("#menu_top").outerWidth() - $("#menu_top")
										.width()) / 2;
						$("#list").setGridWidth(width, true);
					}
				});
	});

function getValue() {
	var id = $("#list").getGridParam('selarrrow');
	return id;
}

function reloadData() {
	$('#list').trigger("reloadGrid");
}
</script>

	</HEAD>
	<body style="height: 100%; overflow: auto;">

		<script type="text/javascript" src="<%=scriptPath%>/wait.js">
</script>

		<script type="text/javascript">

$(function() {

	var rightMenu = function(rowid, iRow, iCol, e) {
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};
	$("#list")
			.jqGrid(
					{
						url : '<%=path%>/attence/attenceRecord!showResult.action?startTime='
								+ $("#startTime").val()
								+ '&endTime='
								+ $("#endTime").val()
								+ '&userID='
								+ $("#userID").val(),
						colModel : [ {
							label : 'id',
							name : 'id',
							hidden : true
						}, {
							label : '姓名',
							name : 'userName',
							width : 100,
							align : 'left'
						}, {
							label : '日期',
							name : 'time',
							width : 100,
							align : 'center'
						}, {
							label : '上班时间',
							name : 'worktime',
							width : 100,
							align : 'center'
						}, {
							label : '下班时间',
							name : 'leavetime',
							width : 100,
							align : 'center'
						}, {
							label : '迟到(分钟)',
							name : 'latetime',
							width : 100,
							align : 'center'
						}, {
							label : '早退(分钟)',
							name : 'leaveearly',
							align : 'center',
							width : 100
						}, {
							label : '旷工（h）',
							name : 'nowoke',
							align : 'center',
							width : 100
						} ],
						onRightClickRow : rightMenu,
						gridComplete : gl.resize
					});

});
</script>
		<div class="main_up_out" style="overflow: hidden;">
			<input type="hidden" id="startTime" name="startTime">
			<input type="hidden" id="endTime" name="endTime">
			<input type="hidden" id="userID" name="userID">
			<div id="menu_top" style="width: auto;">
				<ul>

					<%--<li>
						<button id="add" class="input_button_4" onclick="print();">
							<img src="<%=themePath%>/image/ico_add.gif" />
							打印
						</button>
					</li>


					--%><%--<li>
						<button id="excel" class="input_button" onclick="printAll();">
							<img src="<%=themePath%>/image/ico_edit.gif" />
							全部打印
						</button>

					</li>--%>

				</ul>
				<br style="clear: both" />
			</div>

			<div id="gridDiv" class="grid"
				style="width: auto; display: block; padding: 1px; height: 100%; overflow: auto;">

				<table id="list"></table>
				<div id="pager"></div>
			</div>


		</DIV>
	</body>
</HTML>
<script type="text/javascript" src="<%=path%>/common/script/stopwait.js">
</script>
<style type="text/css" media="print">
#accordion h3,#vcol,div.loading,div.ui-tabs-hide,ul.ui-tabs-nav li,td.HeaderRight
	{
	display: none
}

.ui-jqgrid-titlebar,.ui-jqgrid-title {
	display: none
}

.ui-jqgrid-bdiv_self {
	position: relative;
	margin: 0em;
	padding: 0;
	text-align: left;
}

#pager {
	display: none;
	z-index: -1;
}
</style>
<script type="text/javascript">
function print() {
	window.focus();
	window.print();
	return false;
}
function printAll() {
	alert("全部打印");
}

var GridHeight;
function window.onbeforeprint() {
   //打印前事件    
   var jqgridObj=jQuery("#list");
    GridHeight = jqgridObj.jqGrid('getGridParam', 'height');//获取高度    
    jqgridObj.jqGrid('setGridHeight', '100%');//将其高度设置成100%,主要是为了jqgrid 中有Scroll条时  能把该scroll条内内容都打印出来    
    $("#gview_jqgridlist .ui-jqgrid-bdiv").removeClass().addClass("ui-jqgrid-bdiv_self"); //去除掉overflow属性
    }
function window.onafterprint() {//打印后事件    //放开隐藏的元素
    $("#gview_jqgridlist .ui-jqgrid-bdiv_self").removeClass().addClass("ui-jqgrid-bdiv");//恢复overflow属性，否则会导致jqgrid中scroll条消失    
    jQuery("#list").jqGrid('setGridHeight', GridHeight);//设置成打印前的高度
}
</script>

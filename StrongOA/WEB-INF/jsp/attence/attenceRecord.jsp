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

$(document).ready(
		function() {

			var query = location.search.substring(1);//获取前一页面传过来的url参数，并赋给隐藏域
			var pairs = query.split("?");
			if(pairs.length>1){
				var params=pairs[1];
				var param=params.split("&");
				$("#startTime").val(param[0].split("=")[1]);
				$("#endTime").val(param[1].split("=")[1]);
			}
		

			initWidth = $("#menu_top").outerWidth()
					- ($("#menu_top").outerWidth() - $("#menu_top").width())
					/ 2;

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
									- ($("#menu_top").outerWidth() - $(
											"#menu_top").width()) / 2;
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
	
	$("#list").jqGrid(
			{
				url : '<%=path%>/attence/attenceRecord.action?startTime='
						+ $("#startTime").val() + '&endTime='
						+ $("#endTime").val(),
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
					label : '迟到（分钟）',
					name : 'latetime',
					width : 100,
					align : 'center'
				}, {
					label : '早退（分钟）',
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
			<div id="menu_top" style="width: auto;">
				<ul>

					<li>
						<button id="add" class="input_button_4" onclick="punchin();">
							<img src="<%=themePath%>/image/ico_add.gif" />
							打卡
						</button>
					</li>


					<%--<li>
						<button id="excel" class="input_button_4" onclick="print();">
							<img src="<%=themePath%>/image/ico_edit.gif" />
							打印
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

<script type="text/javascript">

var GridHeight;

function punchin(){
	var ret =window.showModalDialog("<%=path%>/attence/attenceRecord!input.action",window,'help:no;status:no;scroll:no;dialogWidth:700px; dialogHeight:500px');
	gl.msg(ret, "打卡成功"); 
	reloadData();
};

function print() {
	window.focus();
	window.print();
	return false;
}
</script>

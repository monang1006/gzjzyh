<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>日志管理</title>


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
				<!--<div id="menu_top" style="width:auto;">
					<ul>
						  <li>
							<button id="delete" class="input_button_4"><img src="<%=themePath%>/image/ico_del.gif"/>删除</button>
						</li>
						<li>
							<button id="view" class="input_button_4"><img src="<%=themePath%>/image/ico_edit.gif"/>查看</button>
						</li>
					</ul>
					<br style="clear:both" />
				</div>-->
				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
		<s:form theme="simple" id="myTableForm" action="">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								日志信息：
								<s:textfield name="blTitle" id="blTitle"
									cssClass="main_search_input search" title="请输入日志信息"
									theme="simple"></s:textfield>
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


$(function(){
	
	$("#list").jqGrid({
		url:'${root}/xxbs/action/myLog!showList.action',
		colModel :[ 
			{label:'logId',name:'logId', hidden:true}, 
			{label:'日志信息',name:'logInfo',sortable:false,sortable:false},
			{label:'操作人',name:'openUser',sortable:false},
			{label:'操作IP地址',name:'openIp',sortable:false},
			{label:'日期',name:'opeTime',sortable:false}
			],
		//onRightClickRow: rightMenu,
		gridComplete: gl.resize,
		sortname: 'opeTime',
		sortorder: 'desc'
	});
			
	$("#img_sousuo").click(function(){
  	 	var blTitle = $("#blTitle").val();
       	var searchParam = {};
	 	searchParam.blTitle = $.trim(blTitle);
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam,page:1}).trigger("reloadGrid");		
	});
});



function reloadData(){
	$('#list').trigger("reloadGrid");
}

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

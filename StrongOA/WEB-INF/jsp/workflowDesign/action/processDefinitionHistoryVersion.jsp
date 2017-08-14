<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/root_path.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet" />
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />

<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>
<script type="text/javascript" src="<%=jsroot%>/jqgrid/jqgrid.js"></script>

<script type="text/javascript">

$(function(){
	var rightMenu = function(rowid, iRow, iCol, e){
		$(this).resetSelection();
		$(this).setSelection(rowid);
		var menu = $.fn.contextmenu(option);
		menu.displayMenu(e, e.target);
	};

	$("#list").jqGrid({
		url:'<%=root%>/workflowDesign/action/processHistoryVersion!showList.action?pfId=<s:property value="#parameters.pfId"/>',
		colModel :[ 
			{label:'wfId',name:'wfId', hidden:true},
			{label:'流程名称',name:'wfName',sortable:false},
			{label:'流程版本号',name:'wfVersion',sortable:false},
			{label:'流程类型',name:'ptName',sortable:false},
			{label:'部署时间',name:'wfDeployDate',sortable:false}
			],
		onRightClickRow: rightMenu,
		loadComplete: function(){
			globalJqgrid.fillRows(this);
			gl.resize(this);
		}
	});
	
	var search = function(){
		var params = {};
		params.wfVersion = $.trim($("#wfVersion").val());
		params.isSearch = true;
		$("#list").setGridParam({postData: params,page:1}).trigger("reloadGrid");
	};
	
	$("#img_search").click(function(){
		search();
	});
	
	$(".search-top").keydown(function(e){
		if(e.keyCode == 13){
			search();
		}
	});

});

var option = {
		width : 150,
		items : [ 
		{
			text : "设计",
			icon : "<%=root%>/images/operationbtn/Design.png",
			alias : "1-1",
			action :editProcess	
		}
		]
};

function returnProcess(){
	location = "<%=path%>/workflowDesign/action/processFile.action";
}

function editProcess(){
	var wfId = $("#list").jqSelectedId();
	var pfId = 7558;
	if(wfId != false){
		var url = scriptroot + "/workflowDesign/action/processHistoryVersion!input.action?type=edit&wfId="+wfId;		  		
		 window.open(url,'processDefinitionEdit','height=600, width=1000, top=0, left=0, toolbar=no, ' + 
			'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');		  							
	}

	
}



</script>

</head>

<body>
	<div id="contentborder" >
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>
										&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif"/>
											&nbsp;&nbsp;<strong>流程模型历史版本列表</strong>
										</td>
										<td class="tool-top">
			<table style="float:right" border="0" align="right" cellpadding="0" cellspacing="0">
				<tr>

					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="editProcess();"><img
						src="<%=root%>/images/operationbtn/Design.png" />&nbsp;设&nbsp;计&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
					<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="returnProcess();"><img
						src="<%=root%>/images/operationbtn/back.png" />&nbsp;返&nbsp;回&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
					
				</tr>

			</table>

										</td>
										</tr>
									<tr height="30px">
										<td colspan="2" class="search-top">
											流程版本号：<input id="wfVersion" name="wfVersion" type="text" size="20"/>
											<input type="button" id="img_search" value="" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					<tr>
					<td>

	<table id="list"></table>
	<div id="pager"></div>

					</td>
					</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
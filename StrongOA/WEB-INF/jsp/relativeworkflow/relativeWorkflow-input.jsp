<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/root_path.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		
		<title>选择流程</title>

		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />
		
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/jqgrid/jqgrid.js"></script>

<style>
#main-table{
	width: 100%;
}
#main-title{
	float:left;
}
</style>
	</head>
	
	
<body>



<table id="main-table">
	<tr>
		<td class="tool-top">
			<div id="main-title"><img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;<strong>选择流程</strong></div>
				<table border="0" align="right" cellpadding="0" cellspacing="0">
	                <tr>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                  		<td width="5"></td>
	                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
	                 	<td class="Operation_input1" onclick="back();">&nbsp;返&nbsp;回&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
                  		<td width="6"></td>
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


<script type="text/javascript">

$(function(){
	
	var curList = $("#list");

	curList.jqGrid({
		url:'<%=root%>/relativeworkflow/relativeWorkflow!processInstances.action?toPiId=${toPiId}',
		colModel :[
			{label:'processInstanceId',name:'processInstanceId', hidden:true},
			{label:'标题',name:'businessName',sortable:false},
			{label:'发起人',name:'startUserName',sortable:false, align:'center'},
			{label:'发起时间',name:'startDate',sortable:false, align:'center'},
			{label:'流程名称',name:'processName',sortable:false, align:'center'}
			],
		gridComplete: gridComplete,
		onSelectRow: onSelectRow,
		onSelectAll: onSelectAll,
		loadComplete: function(){
			globalJqgrid.fillRows(this);
			gl.resize(this);
		}

	});



	function gridComplete(){
		var selIds = funcSelIds.getIds().split(",");
		
		for(var i=0,len=selIds.length;i<len;i++){
			curList.setSelection(selIds[i]);
		}
		
		curList.setRowData("${toPiId}", false, {"font-style":"italic"});
		curList.setCell("${toPiId}", "cb", "", false, {disabled:true});
	}
	
	function onSelectRow(rowid, status){
		if("${toPiId}" == rowid){
			curList.setSelection(rowid, false);
			return;
		}
		if(status){
			funcSelIds.putId(rowid);
		}
		else{
			funcSelIds.delId(rowid);
		}
	}
	
	function onSelectAll(rowids, status){
		if(status){
			for(var i=0,len=rowids.length;i<len;i++){
				funcSelIds.putId(rowids[i]);
			}
			curList.setSelection("${toPiId}", false);
		}
		else{
			for(var i=0,len=rowids.length;i<len;i++){
				funcSelIds.delId(rowids[i]);
			}
		}
	}

});

var funcSelIds = (function(){		
	var prefix = "id_";
	var selIds = $.parseJSON('${jsonSelPiIds}') || {};

	return {
		"getIds": function(){
			var strIds = "";
			for(var one in selIds){
				if(selIds.hasOwnProperty(one)){
					strIds = strIds.concat(","+selIds[one]);
				}
			}
			if(strIds.length > 0){
				strIds = strIds.substring(1);
			}
			return strIds;
		},
		"putId": function(id){
			selIds[prefix+id] = id;
		},
		"delId": function(id){
			delete selIds[prefix+id];
		}
	};
})();


function save(){
	if(funcSelIds.getIds() == ""){
		alert("请选择要关联的记录。");
		return;
	}
	
	$.post("<%=root%>/relativeworkflow/relativeWorkflow!save.action",
			{toPiId:"${toPiId}",toIds:funcSelIds.getIds()}
	).done(function(res){
			window.returnValue = "success";
			close();
			//location = "<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId=${toPiId}";
		});
	
}

function back(){
	//location = "<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId=${toPiId}";
	close();
}

</script>
</body>
</html>
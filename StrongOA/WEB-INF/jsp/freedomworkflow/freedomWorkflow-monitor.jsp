<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/root_path.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		
		<title>自由流程监控</title>

		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />
		
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.json-2.4.min.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>

<style>
#main-table{
	width: 100%;
	color:#454953;
}
#main-title{
	float:left;
}

table#handle-table{
	width: 100%;
	height: 100%;
	background: #c5dbec;
}

table#handle-table th, table#handle-table td{
	padding: 0px 5px;
	line-height:30px;
	height:30px;
	background: #fff;
}

table#handle-table th{
	background:url("<%=frameroot%>/images/list_bg.jpg");
	color: #000;
}

button{
	width: 20px;
}

.td-hightlight{
	blackground: red;
}
.ft-title{
word-break:break-all
}

</style>
	</head>
	
	
<body style=" background-color:#ffffff;">

<table id="main-table">
	<tr>
		<td class="tool-top" style="color:#000;">
			<div id="main-title"><img style="vertical-align: middle;" src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;&nbsp;<strong>自由流程监控</strong></div>
				<table border="0" align="right" cellpadding="0" cellspacing="0">
	                <tr>
	 				<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg" /></td>
					<td class="Operation_list" onclick="back();"><img
						src="<%=root%>/images/operationbtn/back.png" />&nbsp;返&nbsp;回&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg" /></td>
					<td width="5"></td>
 	                </tr>
	            </table>
		</td>
	</tr>
	<tr height="100%">
		<td>
			<table id="handle-table" cellpadding="0" cellspacing="1">
				<tr id="handle-not-row">
					<th>任务名称</th>
					<th width="20%">备注</th>
					<th width="10%">处理人</th>
					<th width="15%">开始时间</th>
					<th width="15%">结束时间</th>
					<th width="8%">状态</th>
				</tr>
				<s:iterator value="monitorTasks">
				<tr>
					<td class="ft-title">${ftTitle}</td>
					<td class="ft-handler" title="${ftMemo}">
						<s:if test="ftMemo.length()>24">
							<s:property value="ftMemo.substring(0,24)" />...
						</s:if>
						<s:else>
							${ftMemo}
						</s:else>
					</td>
					<td class="ft-handler" align="center">${ftHandler}</td>
					<td class="ft-handler" align="center">${ftStartTime}</td>
					<td class="ft-handler" align="center">${ftEndTime}</td>
					<td class="ft-op" align="center">${ftStatus}</td>
				</tr>
				</s:iterator>
			</table>
		</td>
	</tr>
</table>


    
<script type="text/javascript">



function save(){
	var trs = $("#handle-table tr:not(#handle-not-row,.ft-done)");
	var handles = [];
	trs.each(function(){
		var row = {};
		row.ftTitle = $(this).find(".ft-title").text();
		row.ftHandler = $(this).find(".ft-handler").attr("handler");
		handles.push(row);
	});
	
	var jsonHandles = $.toJSON(handles);
	
	if("${toFwId}" == ""){
		if(trs.length < 1){
			alert("至少要添加一个任务。");	
			return;
		}
		var ret = {
				jsonHandles:jsonHandles,
				saveFlag: "new"
		};
		window.returnValue = ret;
		window.close();
		return;
	}
	
	var param = {
			toFwId:"${toFwId}",
			jsonHandles:jsonHandles
	};
	
	if(window.dialogArguments != undefined){
		param.formId = window.dialogArguments.formId || "";
		param.ftMemo = window.dialogArguments.ftMemo || "";
	}
	
	$.post("<%=root%>/freedomworkflow/freedomWorkflowTask!save.action",param)
		.done(function(res){
			var next = {
					saveFlag: "edit"
			};
			if("${toFwId}" != "" && handles.length > 1){
				next = handles[0];
			}
			window.returnValue = next;
			window.close();
		});
	
}

function back(){
	history.back(-1);
}

</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/root_path.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		
		<title>自由流程</title>

		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		
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
	margin-bottom: 10px;
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

table#handle-table th span{
	font-weight: normal;
	color: red;
}

button{
	width: 20px;
}

.td-hightlight{
	blackground: red;
}

.ft-title{
	word-break:break-all;
}

.ft-handler{
	text-align:center;
}

.ft-op{
	text-align:center;
	padding-top:5px !important;
	height:25px !important;
	line-height:25px !important;
}

.ft-op img{
	cursor:pointer;
}

.select-user{
	background:url("<%=root%>/common/images/but_hs.png");
	border:0px;
	width:73px;
	height:26px;
}

.ft-title-input{
	border: 1px solid #c5dbec;
}

td.altRow{
	background:rgb(245,245,245) !important;
}

.Operation_input, .Operation_input1{
	padding-bottom:4px;
}

.re-select-user{
	cursor: pointer;
}

</style>
	</head>
	
	
<body style="background-color:#ffffff;">
<div style="height:580px;padding:10px;overflow:scroll">
<table id="main-table">
	<tr>
		<td with="60%" style="padding-bottom:7px">
			<div id="main-title"></div>
				<table border="0" align="right" cellpadding="0" cellspacing="0">
	                <tr>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	                 	<td class="Operation_input" id="btn-add">&nbsp;增&nbsp;加&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                  		<td width="5"></td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                  		<td width="5"></td>
	                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
	                 	<td class="Operation_input1" onclick="back();">&nbsp;取&nbsp;消&nbsp;</td>
	                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
                  		<td width="6"></td>
	                </tr>
	            </table>
		</td>
	</tr>
	<tr height="100%">
		<td>
			<table id="handle-table" cellpadding="0" cellspacing="1">
				<thead>
				<tr id="handle-not-row">
					<th width="50%"><span>*</span> 任务名称</th>
					<th width="25%"><span>*</span> 处理人</th>
					<th width="25%">状态/操作</th>
				</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</td>
	</tr>
</table>
</div>

<table id="handle-tpl" style="display:none;">
	<tbody>
	<tr>
		<td class="ft-title" align="left"></td>
		<td class="ft-handler" handler="" handlername=""></td>
		<td class="ft-op" valign="center">
			<img src="<%=root%>/common/images/bc.png" class="op-save" title="保存" style="display:none"/>
			<img src="<%=root%>/common/images/qb.png" class="op-edit" title="修改"/>
			<img src="<%=root%>/common/images/lj.png" class="op-del" title="删除"/>
			<img src="<%=root%>/common/images/xs.png" class="op-up" title="上移"/>
			<img src="<%=root%>/common/images/xx.png" class="op-down" title="下移"/>
		</td>
	</tr>
	</tbody>
</table>

    
<script type="text/javascript">

$(function(){
	
	//标题过滤
	var filterTitle = function(title){
		title = title.replace(/\'/g, "＇");
		title = title.replace(/\"/g, "＂");
		title = title.replace(/\,/g, "，");
		title = title.replace(/\{/g, "｛");
		title = title.replace(/\}/g, "｝");
		title = title.replace(/\:/g, "：");
		title = title.replace(/\\/g, "＼");
		title = title.replace(/\//g, "／");
		return title;
	};
	
	//保存任务
	$("#handle-table").on("click", ".op-save", function(){
		var nodeTitle = $(this).parent().parent().children(".ft-title");
		var valTitle = nodeTitle.find(".ft-title-input").val() || "";
		valTitle = $.trim(valTitle);
		valTitle = filterTitle(valTitle);
		if(valTitle.length > 100){
			alert("任务名称不能超过100个字。");
			return;
		}
		
		if(valTitle == ""){
			alert("任务名称不能为空。");
			return;
		}
		
		var isRepeat = false;
		var trs = $("#handle-table tr:not(#handle-not-row)").find(".ft-title");
		trs.each(function(){
			if($(this).text() == valTitle){
				isRepeat = true;
				return false;
			}
		});
		if(isRepeat){
			alert("任务名称已经存在。");
			return;
		}
		
		var nodeHandler = $(this).parent().parent().children(".ft-handler");
		var valHandlerName = nodeHandler.attr("handlername");
		if(valHandlerName == ""){
			alert("处理人不能为空。");
			return;
		}		
		
		nodeTitle.text(valTitle);
		nodeHandler.text(valHandlerName);
		$(this).hide();
	});
	
	//编辑任务
	$("#handle-table").on("click", ".op-edit", function(){
		var nodeTitle = $(this).parent().parent().children(".ft-title");
		var nodeHandler = $(this).parent().parent().children("td.ft-handler");
		if(nodeTitle.find("input").length < 1){
			nodeTitle.html("<input type='text' class='ft-title-input' style='width:100%' value='"+nodeTitle.text()+"'/>");
			$(this).parent().children(".op-save").show();
		}
		if(nodeHandler.find("input").length < 1){
			nodeHandler.html("<input type='button' class='select-user' value='选择用户'/>");
			$(this).parent().children(".op-save").show();
		}
	});
	
	//删除任务
	$("#handle-table").on("click", ".op-del", function(){
		if(confirm("确定要删除吗？")){
			$(this).parent().parent().remove();
		}
	});
	
	//上移任务
	$("#handle-table").on("click", ".op-up", function(){
		var tr = $(this).parent().parent();
		var prev = tr.prev();		
		if(prev.is("tr:not(#handle-not-row,.ft-done)")){
			prev.insertAfter(tr);
		}
		else{
			alert("已经是[未开始]的第一个任务。");
		}
	});
	
	//下移任务
	$("#handle-table").on("click", ".op-down", function(){
		var tr = $(this).parent().parent();
		var next = tr.next();
		if(next.is("tr")){
			next.insertBefore(tr);
		}
		else{
			alert("已经是[未开始]的最后一个任务。");
		}
	});
	
	//添加任务
	$("#btn-add").click(function(){
		var tpl = $("#handle-tpl tr").clone();
		tpl.find(".ft-title").html("<input type='text' class='ft-title-input' style='width:100%' value=''/>");
		tpl.find(".ft-handler").html("<input type='button' class='select-user' value='选择用户'/>");
		tpl.find(".op-save").show();
		$("#handle-table tbody").append(tpl);
	});
	
	//选中任务
	$("#handle-table").on("click", "tr:not(#handle-not-row)", function(){
		//$("#handle-table td").css("background", "#fff");
		//$(this).find("td").css("background", "#ccc");
	});
	
	var selectUser = function(_this){
		var ret = gl.showDialog("<%=root%>/freedomworkflow/freedomWorkflowTask!selectUser.action", 400, 550);
		if(ret){
			var nodeHandler = $(_this).parent().parent().children(".ft-handler");
			nodeHandler.attr("handler", ret[0]);
			nodeHandler.attr("handlername", ret[1]);
			nodeHandler.html("<span class='re-select-user' title='点击重新选择用户'>"+ret[1]+"</span>");
		}
	};
	
	//选择处理人
	$("#handle-table").on("click", ".select-user", function(){
		selectUser(this);
	});
	
	$("#handle-table").on("click", ".re-select-user", function(){
		selectUser(this);
	});
	
	$("#handle-table").on("mouseenter", ".re-select-user", function(){
		$(this).css("color", "#000");
	});
	
	$("#handle-table").on("mouseleave", ".re-select-user", function(){
		$(this).css("color", "#454953");
	});
	
	
	//初始化任务列表
	var jh = "";
	//新增自由流时的初始化
	if("${toFwId}" == ""){
		jh = window.dialogArguments.jsonHandles;
	}
	//提交一下步时的初始化
	else{
		jh = '${jsonHandles}';
	}
	var handles = $.parseJSON(jh) || [];
	for(var i=0,len=handles.length;i<len;i++){
		var row = handles[i];
		var tr = $("#handle-tpl tr").clone();
		if(i%2==1){
			tr.find("td").addClass("altRow");
		}
		tr.attr("align","center");
		tr.find(".ft-title").text(row.ftTitle);
		tr.find(".ft-handler").text(row.handlerName);
		tr.find(".ft-handler").attr("handler", row.ftHandler);
		tr.find(".ft-handler").attr("handlername", row.handlerName);
		if(row.ftStatus == 1){
			tr.find(".ft-op").html("处理中");
			tr.addClass("ft-done");
			tr.attr("align","center");
		}
		if(row.ftStatus == 2){
			tr.find(".ft-op").html("已处理");
			tr.addClass("ft-done");
			tr.attr("align","center");
		}
		$("#handle-table tbody").append(tr);
	}
	
});


var nextHandlerId = "";
var nextHandlerName = "";

//保存全部任务
function save(){
	var trs = $("#handle-table tr:not(#handle-not-row,.ft-done)");
	
	var isTaskSaveFail = false;
	trs.find(".op-save").each(function(){
		if($(this).css("display") != "none"){
			 $(this).trigger("click");
			 if($(this).css("display") != "none"){
				 isTaskSaveFail = true;
				 return false;
			 }
		}
	});
	
	if(isTaskSaveFail){
		return;
	}
	
	
	var handles = [];
	trs.each(function(i){
		var row = {};
		row.ftTitle = $(this).find(".ft-title").text();
		row.ftHandler = $(this).find(".ft-handler").attr("handler");
		row.handlerName = $(this).find(".ft-handler").attr("handlername");
		if(i == 0){
			nextHandlerId = $(this).find(".ft-handler").attr("handler");
			nextHandlerName = $(this).find(".ft-handler").attr("handlername");
		}
		handles.push(row);
	});
	
	var jsonHandles = $.toJSON(handles);
	
	//新建自由流时，保存任务
	if("${toFwId}" == ""){
		if(trs.length < 1){
			alert("至少要添加一个任务。");	
			return;
		}
		var ret = {
				jsonHandles:jsonHandles,
				nextHandlerId:nextHandlerId,
				saveFlag: "new"
		};
		window.returnValue = ret;
		window.close();
		return;
	}
	
	
	//提交下一步时，保存任务
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
					saveFlag: "edit",
					ftTitle: "",
					handlerId: "",
					handlerName: ""
			};
			if("${toFwId}" != "" && handles.length > 0){
				next.ftTitle = handles[0].ftTitle;
				next.handlerId = nextHandlerId;
				next.handlerName = nextHandlerName;
			}
			window.returnValue = next;
			window.close();
		});
	
}

function back(){
	window.close();
}

</script>
</body>
</html>
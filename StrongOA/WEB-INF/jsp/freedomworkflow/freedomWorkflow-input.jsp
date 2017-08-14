<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/root_path.jsp"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@ include file="/common/OfficeControl/version.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		
		<title>自由流程</title>

		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />
		
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>
		
		<script src="<%=path%>/oa/js/eform/fw_eform.js" type="text/javascript"></script>

<style>

body{
	/*overflow: auto;*/
	background:rgb(252,251,249);
}
#main-table{
	width: 100%;
	height: 100%;
}
#main-title{
	float:left;
}

#toolbar{
	width: 100%;
	padding: 10px 0;
}


#fw-right-tool{
	padding: 5px;
	line-height: 22px;
	overflow:auto;
}

#fw-rtool-new,#fw-rtool-handle{
	padding: 5px 10px;
}

#fw-rtool-new div,#fw-rtool-handle div{
	padding: 0px 10px;
}

fieldset{
	padding: 10px 8px;
	margin-top: 15px;
}

#onlyRemainArea fieldset{
	padding: 0px 8px 10px 8px;
}

#msg input{
	vertical-align:baseline;
	margin-right:5px;
}

</style>
	</head>
	
	
<body onload="window.focus();">
<script type="text/javascript" src="<%=path%>/common/js/Silverlight/Silverlight.js"></script>
<script type="text/javascript">

var bussinessId = "${model.fwFormBizTable};${model.fwFormBizPk};${model.fwFormBizId}";
var fwFormBizTable = "${model.fwFormBizTable}";
var fwFormBizPk = "${model.fwFormBizPk}";
var fwFormBizId = "${model.fwFormBizId}";
 
</script>

<div id="toolbar">
		<table border="0" align="right" cellpadding="0" cellspacing="0">
			<tr>
			<s:if test="model.fwId!=null">
				<td width="7" height="24" background="<%=frameroot%>/images/ch_h_l.gif" style="background-repeat:no-repeat; background-position: 0% 50%;">&nbsp;</td>
				<td class="Operation_input" onclick="editHandler();" style=" background-position: 0% 50%;">&nbsp;编辑流程&nbsp;</td>
				<td width="7" background="<%=frameroot%>/images/ch_h_r.gif" style="background-repeat:no-repeat; background-position: 0% 50%;">&nbsp;</td>
				<td width="5"></td>
			</s:if>
				<%--<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif" /></td>
				<td class="Operation_input" onclick="save();">&nbsp;提&nbsp;交&nbsp;</td>
				<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif" /></td>
				<td width="5"></td>
				
				--%>
				<td width="7" height="24" background="<%=frameroot%>/images/ch_h_l.gif" style="background-repeat:no-repeat; background-position: 0% 50%;">&nbsp;</td>
				<td class="Operation_input" onclick="save();" style=" background-position: 0% 50%;">&nbsp;提&nbsp;交&nbsp;</td>
				<td width="7" background="<%=frameroot%>/images/ch_h_r.gif" style="background-repeat:no-repeat; background-position: 0% 50%;">&nbsp;</td>
				<td width="5"></td>
				
				<td width="7" height="24" background="<%=frameroot%>/images/ch_z_l.gif" style="background-repeat:no-repeat; background-position: 0% 50%;">&nbsp;</td>
				<td class="Operation_input1" onclick="back();" style=" background-position: 0% 50%;">&nbsp;取&nbsp;消&nbsp;</td>
				<td width="7" background="<%=frameroot%>/images/ch_z_r.gif" style="background-repeat:no-repeat; background-position: 0% 50%;">&nbsp;</td>
				<td width="5"></td>
			</tr>
		</table>
</div>

<table id="main-table">
	<tr height="100%">
		<td width="70%" height="100%" id="out-td" valign="top">
			<form id="form1" name="form1">
			<!-- 电子表单模板id -->
			<input type="hidden" id="formId" name="formId" value="${formId}"/>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<input type="hidden" id="formAction" name="formAction"/>
			
			<input type="hidden" id="fwId" name="fwId" value="${model.fwId}"/>
			<input type="hidden" id="ftId" name="ftId" value="${ftId}"/>
			<input type="hidden" id="jsonHandles" name="jsonHandles"/>
			<input type="hidden" id="fwTitle" name="fwTitle" value="${model.fwTitle}"/>
			<input type="hidden" id="hidFtMemo" name="ftMemo"/>
							
			<input type="hidden" id="remindTypes" name="remindTypes"/>
			<input type="hidden" id="remindMsg" name="remindMsg"/>
			<input type="hidden" id="nextHandlerId" name="nextHandlerId"/>

			<!--是否可以上传PDF收文文件是否必填 0：不能上传；1：能上传 -->
			<input type="hidden" id="PDFFunction" name="PDFFunction"
			isPermitUploadPDF="1" isPermitUploadSMJ="1" isFirstNode="1">
			
			<div style="position: relative; height: 100%">
				<object data="data:application/x-silverlight-2,"
					type="application/x-silverlight-2" width="100%" height="100%"
					id="plugin">
					<param name="source"
						value="<%=path%>/FormReader/StrongFormReader.xap" />
					<param name="onError" value="onSilverlightError" />
					<param name="onLoad" value="onSilverlightLoad" />
					<param name="background" value="white" />
					<param name="minRuntimeVersion" value="4.0.50401.0" />
					<param name="autoUpgrade" value="true" />
					<a href="<%=path%>/detection/lib/Silverlight.exe"
						style="text-decoration: none"> <img
							src="<%=path%>/detection/images/SLMedallion_CHS.png"
							alt="Get Microsoft Silverlight" style="border-style: none" />
					</a>
				</object>
			</div>
				<iframe id="_sl_historyFrame"
					style="visibility: hidden; height: 0px; width: 0px; border: 0px"></iframe>
			</form>
		</td>
		<td valign="top" id="fw-right-tool">
			<div id="fw-rtool-new">
				<fieldset>
					<legend>流程概览</legend>
					<div>流程名称：</div>
					<div><input type="text" id="fw-title" style="width:300px"/></div>
					<div id="fw-title-readonly">${model.fwTitle}</div>
				</fieldset>
			</div>
			<div id="fw-rtool-handle">
				<fieldset style="margin-top:0px">
					<legend>当前任务</legend>
					<div>标题：${ftTitle}</div>
					<div>备注：</div>
					<div><textarea style="width:97%" rows="5" id="ftMemo" name="ftMemo"></textarea>
						<span style="color:#999;font-size:12px">&nbsp;已输入<span id="cur-memo-num">0</span>个字，剩余<span id="sur-memo-num">100</span>个字，最多输入100个字</span>
					</div>
				</fieldset>
				<fieldset>
					<legend>下一步</legend>
					<div style="word-break:break-all">标题：<span id="next-title">${nextFtTitle}</span></div>
					<div>处理者：<span id="next-handler" next-handler-id="${nextFtHandlerId}">${nextFtHandler}</span></div>
				</fieldset>
				<!-- <input type="button" onclick="SaveTemplateData();" value="savedata"/>
				<input type="button" onclick="office2pdf();" value="toPdf"/> -->
			</div>
			<div id="onlyRemainArea">
				<strong:remind isDisplayContent="true" isShowButton="false" includeRemind="SMS,MSG"  isDisplayInfo="true" defaultRemindContent="自由流工作提醒：请查阅《${model.fwTitle}》。${userName}" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
			</div>
		</td>
	</tr>
</table>	

    
<script type="text/javascript">

$(function(){
	
	if("${model.fwId}" == ""){
		$("#fw-rtool-handle").hide();
		$("#fw-title").show();
		$("#fw-title-readonly").hide();
	}
	else{
		$("#fw-rtool-handle").show();
		$("#fw-title").hide();
		$("#fw-title-readonly").show();
	}
	
	//调节表单的高宽度
	var autoHeight = function(){
		$("#plugin").height($("#main-table").height() - 50);
	};
	
	autoHeight();
	$(window).resize(function(){
		autoHeight();
	});
	
	$("#fw-title").keyup(function(){
		var title = $(this).val();
		var rmTitle = "自由流工作提醒：请查阅《"+title+"》。${userName}";
		$("#handlerMes").val(rmTitle);
	});
	
	$("#ftMemo").keyup(function(){
		var memo = $(this).val();
		if(memo.length > 100){
			memo = memo.substring(0,100);
			$(this).val(memo);
		}
		var cur = memo.length + 0;
		var sur = 100 - cur;
		$("#cur-memo-num").text(cur);
		$("#sur-memo-num").text(sur);
	});
	
	
	$("#remind").hide();
	var tmpcount=0;
	tmpcount= $("#onlyRemainArea input:checked").length;
	if(tmpcount>0){
		$("#remind").show();
	}
   	$("#msg input:checkbox").click(function(){
   		var icount=0;	
   		icount= $("#msg input:checked").length;
   		if (icount>0)
   		{
   			$("#remind").show();
   			//$("#handlerMes").show();
   		}else{
   			$("#remind").hide();
   			//$("#handlerMes").hide();
   		}
   	});
	
});

//编辑或保存流程任务
function editHandler(){
	var param = {
			formId: $("#formId").val(),
			ftMemo: $("#ftMemo").val(),
			jsonHandles: $("#jsonHandles").val()
	};
	var ret = gl.showDialog("<%=root%>/freedomworkflow/freedomWorkflowTask!input.action?toFwId=${model.fwId}", 690, 600, param);
	
	if(ret){
		//保存流程任务
		if(ret.saveFlag == "new"){
			$("#jsonHandles").val(ret.jsonHandles);
			var title = $.trim($("#fw-title").val()) || "";
			if(title.length < 1){
				alert("流程名称不能为空。");
				return;
			}
			if(title.length > 100){
				alert("流程名称不能超过100个字。");
				return;
			}
			$("#fwTitle").val(title);
			
			var remindTypes = $("#msg input:checked").map(function(){
				return $(this).val();
			}).get();
			$("#remindTypes").val(remindTypes.join(","));
			
			var msg = "";
			if(remindTypes.length > 0){
				msg = $("#handlerMes").val() || "";
			}
			$("#remindMsg").val(msg);
			
			$("#nextHandlerId").val(ret.nextHandlerId);

			SaveFormData();
		}
		//编辑流程任务
		else if(ret.saveFlag == "edit"){
			$("#next-title").text(ret.ftTitle);
			$("#next-handler").text(ret.handlerName);
			$("#next-handler").attr("next-handler-id", ret.handlerId);
		}
	}
}

//提交
function save(){
	
	$("#hidFtMemo").val($("#ftMemo").val());

	//保存新建自由流程，先建任务再保存
	if("${model.fwId}" == ""){
		editHandler();
	}
	//提交下一步
	else{
		var memo = $("#ftMemo").val();
		if(memo.length > 100){
			alert("备注不能超过100个字。");
			return;
		}
		
		var remindTypes = $("#msg input:checked").map(function(){
			return $(this).val();
		}).get();
		
		var msg = "";
		if(remindTypes.length > 0){
			msg = $("#handlerMes").val() || "";
		}
		
		$("#remindTypes").val(remindTypes.join(","));
		$("#remindMsg").val(msg);		
		$("#nextHandlerId").val($("#next-handler").attr("next-handler-id"));
		
		SaveFormData();
		/*
		var param = {
				formId: $("#formId").val(),
				ftMemo: $("#ftMemo").val(),
				ftId: $("#ftId").val(),
				remindTypes:remindTypes.join(","),
				remindMsg: msg,
				nextHandler:$("#next-handler").attr("next-handler-id")
		};
		$.post("<%=root%>/freedomworkflow/freedomWorkflowTask!doNext.action",param)
			.done(function(res){
				if(res === "success"){
					SaveFormData();
					//opener.location.reload();
					//window.close();
				}
			});*/

	}
}

function back(){
	window.close();
}

</script>
</body>
</html>
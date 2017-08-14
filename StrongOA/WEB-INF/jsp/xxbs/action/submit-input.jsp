<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if> <s:else>修改</s:else>报送信息</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript"
			src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript"
			src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript"
			src="<%=scriptPath%>/upload/ajaxfileupload.js"></script>
		<script type="text/javascript"
			src="<%=scriptPath%>/jquery.serializeObject.js"></script>
		<style type="text/css">
html {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 40px 0px 40px 0px;
	overflow: hidden;
}

html,body {
	height: 100%;
}
</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
	
			<div class="windows_title">
				请填写报送信息
			</div>
		<div class="information_out" id="information_out"
			style="overflow-y: scroll">
			<form id="myform" name="myform" method="post"
				enctype="multipart/form-data">
				<s:if test="%{op==\"edit\"}">
					<input type="hidden" id="pubId" name="pubId" value="${model.pubId}" />
				</s:if>
				<input type="hidden" id="pubUseStatus" name="pubUseStatus"
					value="${model.pubUseStatus}" />
				<input type="hidden" id="pubIsShare" name="pubIsShare"
					value="${model.pubIsShare}" />
				<input type="hidden" id="pubIsRead" name="pubIsRead" value="" />
				<input type="hidden" id="pubIsInstruction" name="pubIsInstruction"
					value="${model.pubIsInstruction}" />
				<input type="hidden" id="pubIsComment" name="pubIsComment"
					value="${model.pubIsComment}" />
				<input type="hidden" id="pubIsMailInfo" name="pubIsMailInfo"
					value="${model.pubIsMailInfo}" />
				<input type="hidden" id="pubSyncStatus" name="pubSyncStatus"
					value="${model.pubSyncStatus}" />
				<input type="hidden" id="op" name="op" value="${op}" />
				<input type="hidden" value="0" name="model.pubInfoType">
				<table class="information_list" style="width: 99%;" cellspacing="0"
					cellpadding="0">
					<tr>
						<td class="labelTd">
							<font color="#FF0000">*</font> 标题：
						</td>
						<td class="contentTd" colspan="3">
							<textarea style="width: 800px; height: 50px" rows="2"
								class="information_out_input_words" id="pubTitle"
								name="pubTitle">${model.pubTitle}</textarea>
						</td>
					</tr>
					<!--  <tr id="trAttach" style="display:none">
					<td class="labelTd">
						<font color="#FF0000"></font> 附件：
					</td>
					<td class="contentTd" colspan="3">
						<input type="file" id="upload" name="upload" class="information_out_input" />
						<div>只能上传txt格式文件。</div>
					</td> 
				</tr>-->
					


					<iframe id="attachDownLoad" src=''
						style="display: none; border: 4px solid #CCCCCC;"></iframe>

					<tr id="trText">
						<td class="labelTd" valign="top">
							<font color="#FF0000">*</font> 正文：
						</td>
						<td class="contentTd" colspan="3">
							<textarea style="width: 800px; height: 400px" rows="5"
								class="information_out_input_words" id="pubRawContent"
								name="pubRawContent">${model.pubRawContent}</textarea>
						</td>
					</tr>
					<tr>
						<td class="labelTd">
							约稿信息：
						</td>
						<td class="contentTd">
							<input type="hidden" id="pubAppointId"
								name="TInfoBaseAppoint.aptId"
								value="${model.TInfoBaseAppoint.aptId}" />
							<input type="text" id="appointTitle"
								class="information_out_input"
								value="${model.TInfoBaseAppoint.aptTitle}" readonly="readonly" />
						</td>
						<!--   <td class="labelTd">
						<font color="#FF0000"></font> 内容形式：
					</td>
					<td class="contentTd">
						<s:radio name="pubIsText" id="pubIsText" list="#{'0':'文本','1':'附件'}" value="%{model.pubIsText}"/>
					</td>-->
						<input type="hidden" name="pubIsText" id="pubIsText" value="0" />
						<td class="labelTd">
							报送员：
						</td>
						<td class="contentTd">
							<input type="hidden" id="pubPublisherId" name="pubPublisherId"
								value="${model.pubPublisherId}" />
							<input class="information_out_input" id="" type="text"
								readonly="readonly" value="${userName}" />
						</td>
					</tr>
					<tr>
						<td class="labelTd">
							<font color="#FF0000"></font> 报送单位：
						</td>
						<td class="contentTd">
							<input class="information_out_input" readonly="readonly"
								type="text" id="orgCode" value="${orgName}" />
							<input type="hidden" id="orgId" name="orgId"
								value="${model.orgId}" />

						</td>
						<!--  <td class="labelTd">
						<font color="#FF0000"></font> 信息类型
					</td>
					<td class="contentTd">
						<s:select cssClass="formin" name="pubInfoType" list="#{'0':'普通信息','1':'涉密信息'}"
							listKey="key" listValue="value" value="%{model.pubInfoType}" />
					</td>-->
						<td class="labelTd">
							联系电话：
						</td>
						<td class="contentTd">
							<input class="information_out_input" type="text"
								readOnly="readOnly" value="${userTelephone}" />
						</td>
					</tr>
					<tr>
						<td class="labelTd">
							<font color="#FF0000">*</font> 签发领导：
						</td>
						<td class="contentTd">
							<input class="information_out_input" id="pubSigner"
								name="pubSigner" type="text" value="${IssuePeople}"
								required="true" requiredMsg="getText(errors_required, ['签发领导'])" />
						</td>

					</tr>
					<tr>
						<td class="labelTd">
							<font color="#FF0000">*</font> 责任编辑：
						</td>
						<td class="contentTd">
							<input class="information_out_input" id="pubEditor"
								name="pubEditor" type="text" value="${editor}" required="true"
								requiredMsg="getText(errors_required, ['责任编辑'])" />
						</td>
						<!--
					<s:if test="%{model.pubFile1!=null||model.pubFile2!=null}">
					<td class="labelTd">
						附件：
					</td>
					<td class="contentTd" width="300px">
						<s:if test="%{model.pubFile1!=null}">
						<a href="#" onclick='down("${model.pubFile1}","${model.pubFile1Name}")'><font color="blue">${model.pubFile1Name}</font></a>,
						</s:if>
						<s:if test="%{model.pubFile2!=null}">
						<a href="#" onclick='down("${model.pubFile2}","${model.pubFile2Name}")'><font color="blue">${model.pubFile2Name}</font></a>
						</s:if>
					</td>
					</s:if>
					-->
					</tr>
					<!--  <tr>
					<td class="labelTd">
					   	 附件1：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="file1" style="width:250px"
							name="file1" type="file"/>
						<input type="button" value="清空" id="clean1" align="left">
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						附件2：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="file2" style="width:250px"
							name="file2" type="file"/>
						<input type="button" value="清空" id="clean2" align="left"><br/>
					</td>
							
				</tr>
				<tr>
				<td class="labelTd"> </td>
				<td "contentTd">
				<font color="red">(附件最大上传大小为20M.)</font>
				</td>
				</tr>-->
				</table>
		</div>
		<div class="information_list_choose_pagedown">
			<input style="display: none" type="submit" value="m" id="m_save" />
			<input type="button" class="information_list_choose_button9"
				value="报送" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="待报" name="notSubmitted" id="notSubmitted" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>

		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">
function ajaxFileUpload(){		
	if(checkFileExt()){
		var data = $("#myform").serializeObject();
		//delete data.upload;
		$.ajaxFileUpload({
			url:"<%=root%>/xxbs/action/submit!saveNotSubmitted.action", 
			secureuri:false,
			fileElementId:'upload',
			dataType: 'json',
			data:data,
			success: function (data, status){
				if(data == "success"){
					alert(data);
					$("#upSuccess").html("上传成功");
				}
			},
			error: function (data, status, e){
				alert(e);
			}
		});
	}
}

function checkFileExt(){
	var isOk = false;
	var op = $("#op").val();
	var val = $("input[name='pubIsText']:checked").val();
	if(val == 1){
		var filename = $("#upload").val();
		if(filename!=""){			
		var pos = filename.lastIndexOf(".");
		var ext = filename.substring(pos+1);
		ext = ext.toLowerCase();
		//if(ext == "doc" || ext == "docx"){
		if(op=="edit"){
			isOk=true;
			if(ext != "txt"){
				isOk = false;
				alert("只能上传txt格式文件。");
			}
		}
		else{
		if(ext == "txt"){
			isOk = true;
		}
		else {
			isOk = false;
			alert("只能上传txt格式文件。");
		}
		}
	}
		else if(op=="edit"){
			isOk = true;
		}
		else{
			alert("附件内容不能为空！");
		}
	}
	else{
		isOk = true;
	}
	return isOk;
}

function goSubmit(flag){
	var isOk = false;
	if(validator.form()){
		$("#myform").attr("action", "<%=root%>/xxbs/action/submit!saveNotSubmitted.action");
		//ajaxFileUpload();
		//$("#myform").submit();
		isOk = true;
	}
	return isOk;
}

var isExistTitle = false;
function isExistTitle(){
	var title = $("#pubTitle").val();
	//$.ajax({
	//	  url: "<%=root%>/xxbs/action/submit!isExistTitle.action",
	//	  async: false,
	//	  dataType: 'text',
	//	  type: 'POST',
	//	  data:{title:title},
	//	  success: function(data){
	//		if(data == "true"){
	//			isExistTitle = true;
	//			alert("标题已经存在，标题不能重复。");
	//		}
	//	  }
	//});
	//salert(data);
}

$(function(){	
	//$("#pubTitle").blur(function(){
	//	var title = $("#pubTitle").val();
	//	$.ajax({
	//		  url: "<%=root%>/xxbs/action/submit!isExistTitle.action",
	//		  //async: false,
	//		  dataType: 'text',
	//		  type: 'POST',
	//		  data:{title:title},
	///		  success: function(data){
	//			if(data == "true"){
	//				isExistTitle = true;
	//				$("#pubTitle").attr("value", title+"（重复标题）");
	//				alert("标题已经存在，标题不能重复。");
	//			}
	//		  }
	//	});		
	//});
		
	//上报
	$("#save").click(function(){	
		var pubTitle = $("#pubTitle").val();
		
			if(pubTitle.trim()==""){
			alert("信息标题不能为空！");
			return false;
		}
		if(pubTitle.length>100){
			alert("信息标题过长！");
			return false;
		}
		var pubRawContent= $("#pubRawContent").val();
		if(pubRawContent.trim()=="" ){
			alert("信息正文不能为空！");
			return false;
		}
				
		
		var pubSigner = $("#pubSigner").val();
		if(pubSigner.length>10){
			alert("签发领导名称过长！");
			return false;
		}
		var pubEditor = $("#pubEditor").val();
		if(pubEditor.length>10){
			alert("责任编辑名称过长！");
			return false;
		}
		/*var file1 = $("#file1").val();
		if(file1.substring(file1.indexOf("."))==".exe"){
			alert("附件不能上传exe文件");
			return false;
		}
		var file2 = $("#file2").val();
		if(file2.substring(file2.indexOf("."))==".exe"){
			alert("附件不能上传exe文件");
			return false;
		}*/
		if(validator.form() && checkFileExt()){
			$("#myform").attr("action", "<%=root%>/xxbs/action/submit!save.action");
			//$("#myform").submit();
			$("#m_save").trigger("click");
			$("#save").attr("disabled","none");
		}
	});
	
	//待报
	$("#notSubmitted").click(function(){
		var pubTitle = $("#pubTitle").val();


			if(pubTitle.trim()==""){
			alert("信息标题不能为空！");
			return false;
		}
				if(pubTitle.length>100){
			alert("信息标题过长！");
			return false;
		}
		var pubRawContent= $("#pubRawContent").val();
		if(pubRawContent.trim()=="" ){
			alert("信息正文不能为空！");
			return false;
		}

		var pubSigner = $("#pubSigner").val();
		if(pubSigner.length>50){
			alert("签发领导名称过长！");
			return false;
		}
		var pubEditor = $("#pubEditor").val();
		if(pubEditor.length>50){
			alert("责任编辑名称过长！");
			return false;
		}
		/*var file1 = $("#file1").val();
		if(file1.substring(file1.indexOf("."))==".exe"){
			alert("附件不能上传exe文件");
			return false;
		}
		var file2 = $("#file2").val();
		if(file2.substring(file2.indexOf("."))==".exe"){
			alert("附件不能上传exe文件");
			return false;
		}*/
		if(validator.form() && checkFileExt()){
			$("#myform").attr("action", "<%=root%>/xxbs/action/submit!saveNotSubmitted.action");
			//$("#myform").submit();
			$("#m_save").trigger("click");
			$("#notSubmitted").attr("disabled","none");
		}
	});
	
	$("#cancel").click(function(){
		//$.get("<%=root%>/xxbs/action/submit!deleteTempFile.action", function(){
			//window.close();
		//});
		window.close();
	});
	
	$("#clean1").click(function(){
		$("#file1").val("");
	});
	
	$("#clean2").click(function(){
		$("#file2").val("");
	});
	
	//表单验证
	//$("#myform").validate({
	//	container:$(document.body)
	//});
		
	$("#appointTitle").click(function(){
		var ret = gl.showSubDialog("<%=root%>/xxbs/action/invitation!select.action",650,355);
		if(ret != undefined && ret.status == "success"){
			$("#pubAppointId").attr("value", ret.id);
			$("#appointTitle").attr("value", ret.title);
		}
	});
	
	var textSwitch = function(){
		var oText = $("#trText");
		var oAttach = $("#trAttach");
		var val = $("input[name='pubIsText']:checked").val();
		if(val == "0"){
			oText.show();
			oAttach.hide();
		}
		else if(val == "1"){
			oText.hide();
			oAttach.show();
		}
	};
	var textSwitchInit = function(){
		//$("#trAttach").hide();
	};
	
	$("input[name='pubIsText']").bind("click", textSwitch);
	$(window).bind("load", textSwitch);
	
	
});


//下载附件
function download(){
	var attachDownLoad = document.getElementById("attachDownLoad");
	attachDownLoad.src = "<%=root%>/xxbs/action/submit!officeStream.action?toId=${toId}";
}

function down(file,fileName){
	fileName = encodeURIComponent(fileName);
	fileName = encodeURIComponent(fileName);
	window.location = "<%=root%>/xxbs/action/submit!officeStream2.action?file="+file+"&fileName="+fileName;
}
</script>

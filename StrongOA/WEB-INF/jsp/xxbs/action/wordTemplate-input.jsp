<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>期刊模板</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			请填写模板信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" enctype="multipart/form-data" method="post"
			 action="/xxbs/action/wordTemplate!save.action?op=%{op}" theme="simple">
			
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="wtId" value="${model.wtId}"/>
			</s:if>
				<input type="hidden" name="toId" value="${toId}"/>
			<table width="100%" class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 模板名称
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="wtTitle"
							name="wtTitle" type="text"
							value="${model.wtTitle}" required="true" 
							requiredMsg="getText(errors_required, ['模板名称'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 头模板文件
					</td>
					<td class="contentTd">
						<input type="file" name="upload" id="upload"/>
						<s:if test="%{op==\"edit\"}">[已上传]</s:if>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 内容模板文件
					</td>
					<td class="contentTd">
						<input type="file" name="upload2" id="upload2"/>
						<s:if test="%{op==\"edit\"}">[已上传]</s:if>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="保存" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="返回" name="cancel" id="cancel" />
		</div>
		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript">
function checkFileExt(){
	
	var filename = $("#upload").val();
	var pos = filename.lastIndexOf(".");
	var ext = filename.substring(pos+1).toLowerCase();
	var filename2 = $("#upload2").val();
	var pos2 = filename2.lastIndexOf(".");
	var ext2 = filename2.substring(pos2+1).toLowerCase();
	
	if("${op}" == "edit"){
		if(filename != ""){
			return msgExt(ext);
		}
		else{
			return true;
		}
		if(filename2 != ""){
			return msgExt(ext2);
		}
		else{
			return true;
		}
	}
	else if("${op}" == "add"){
		return msgExt(ext) && msgExt(ext2);
	}
}

function msgExt(ext){
	var isOk = true;
	if(ext != "doc"){
		isOk = false;
		alert("只能上传doc格式文件。");
	}
	return isOk;
}

$(function(){
	
	//表单提交操作
	$("#save").click(function(){
		if(validator.form() && checkFileExt()){
			$("#myform").submit();
		}
	});
	
	$("#cancel").click(function(){
		window.close();
	});
	
	//表单验证
	//$("#myform").validate({
	//	container:$(document.body)
	//});
});

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

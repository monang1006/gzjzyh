<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>评分设置</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			评分设置
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" theme="simple">
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 默认采用评分
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="scoreUse"
							name="scoreUse" type="text" digits="true"
							 digitsMsg="getText(errors_digits, ['默认采用评分'])"
							value="${scoreUse}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 默认批示评分
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="scoreInstruction"
							name="scoreInstruction" type="text" digits="true" 
							 digitsMsg="getText(errors_digits, ['默认批示评分'])"
							value="${scoreInstruction}"/>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="保存" name="save" id="save" />
		</div>
		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">

$(function(){

	//表单提交操作
	$("#save").click(function(){
		if(validator.form()){
			//$("#myform").submit();
			var scoreUse = $("#scoreUse").val();
			var scoreInstruction = $("#scoreInstruction").val();
			if(scoreUse==""){
				alert("默认采用评分不能为空！");
				return false;
			}
			if(scoreUse.length>5){
				alert("默认采用评分过长！");
				return false;
			}
			if(scoreInstruction==""){
				alert("默认批示评分不能为空！");
				return false;
			}
			if(scoreInstruction.length>5){
				alert("默认批示评分过长！");
				return false;
			}
			var data = $("#myform").serialize();
			var url = "<%=root%>/xxbs/action/adoption!saveScoreSetting.action";
			$.ajax({
				type: 'POST',
				url: url,
				data: data,
				success: function(ret){
					if(ret == "success"){
						alert("保存成功");
					}
				}
			});
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

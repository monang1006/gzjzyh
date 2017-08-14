<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>期刊</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
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
			请填写期刊信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/journal!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" id="jourId" name="jourId" value="${model.jourId}"/>
			</s:if>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 期刊名称
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="jourName"
							name="jourName" type="text"
							value="${model.jourName}" required="true"
							requiredMsg="getText(errors_required, ['期刊名称'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 期刊得分
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="jourCode"
							name="jourCode" type="text"
							value="${model.jourCode}" required="true" digits="true"
							requiredMsg="getText(errors_required, ['期刊得分'])" digitsMsg="getText(errors_digits, ['期刊得分'])"/>
					</td>
				</tr>
				
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 期刊模板
					</td>
					<td class="contentTd">
						<s:select id="jourId" name="TInfoBaseWordTemplate.wtId" list="wordTemplates" 
						 listKey="wtId" listValue="wtTitle" value="%{model.TInfoBaseWordTemplate.wtId}" />
					</td>
				</tr>
				<!-- <tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 默认期刊
					</td>
					<td class="contentTd">
						<s:radio name="isDefault" list="#{\"1\":'是',\"0\":'否'}" value="%{model.jourIsDefault}"/>
					</td>
				</tr> -->
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 期刊描述
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words"
							id="jourDetail" name="jourDetail">${model.jourDetail}</textarea>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="保存" name="save" id="save" />
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

$(function(){

	//表单提交操作
	$("#save").click(function(){
		var jourName = $("#jourName").val();
		if(jourName.length>100){
			alert("期刊名称过长！");
			return false;
		}
		var jourDetail = $("#jourDetail").val();
		if(jourDetail.length>500){
			alert("期刊描述过长！");
			return false;
		}
		var jourCode = $("#jourCode").val();
		if(jourCode.length>2){
			alert("期刊得分过长！");
			return false;
		}
		if(validator.form()){
			$("#save").attr("disabled","none");
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

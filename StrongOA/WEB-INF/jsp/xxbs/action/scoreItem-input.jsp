<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>加分项</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>
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
			请填写加分项信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/scoreItem!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="scId" value="${model.scId}"/>
			</s:if>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 加分项名称
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="scName"
							name="scName" type="text"
							value="${model.scName}" required="true"
							requiredMsg="getText(errors_required, ['加分项名称'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 加分分数
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="scScore"
							name="scScore" type="text" digits="加分分数"
							value="${model.scScore}" required="true"
							requiredMsg="getText(errors_required, ['加分项名称'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 加分项状态
					</td>
					<td class="contentTd">
					<s:radio name="scState" id="scState" list="#{\"1\":'已启用',\"0\":'未启用'}" value="%{model.scState}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 加分项描述
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words"
							id="scDetail" name="scDetail">${model.scDetail}</textarea>
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
		var scName = $("#scName").val();
		var scScore = $("#scScore").val();
		var scDetail = $("#scDetail").val();
		if(scName.length>20){
			alert("加分项名称过长！");
			return false;
		}
		if(scScore.length>5){
			alert("加分分数过长！");
			return false;
		}
		if(scDetail.length>500){
			alert("加分项描述过长！");
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

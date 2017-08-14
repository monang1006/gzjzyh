<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>期号</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			请填写期号信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/issue!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="issId" value="${model.issId}"/>
			</s:if>
			<input type="hidden" name="issIsPublish" value="${model.issIsPublish}"/>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 期号：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="issNumber"
							name="issNumber" type="text"
							value="${model.issNumber}" required="true"
							requiredMsg="getText(errors_required, ['期号'])"/>
					</td>
				</tr>
				
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 期号时间：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="issTime"
							name="issTime" type="text"
							value="${date1}" required="true"
							requiredMsg="getText(errors_required, ['期号时间'])" readonly="readonly"/>
						<web:datetime format="yyyy-MM-dd" readOnly="false" id="issTime" />
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 所属期刊：
					</td>
					<td class="contentTd">
						<s:select id="jourId" name="TInfoBaseJournal.jourId" list="journals" 
						 listKey="jourId" listValue="jourName" value="%{model.TInfoBaseJournal.jourId}" />
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
		var issNumber = $("#issNumber").val();
		if(issNumber.length>10){
			alert("期数长度过长！");
			return false;
		}
		if(validator.form()){
			//$("#jourId").attr("disabled", false);
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

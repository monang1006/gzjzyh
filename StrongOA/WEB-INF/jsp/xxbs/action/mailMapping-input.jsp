<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>约稿</title>

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
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			请填写约稿信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/info/invitation!save.action" theme="simple">
			<input type="hidden" id="aptId" name="aptId" value="${model.aptId}"/>
			<input type="hidden" id="aptUserid" name="aptUserid" value="${model.aptUserid}"/>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 约稿标题
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="aptTitle"
							name="aptTitle" type="text"
							value="${model.aptTitle}" required="true"
							requiredMsg="getText(errors_required, ['约稿标题'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 发布时间
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="aptDate"
							name="aptDate" type="text" readonly="readonly"
							value="${model.aptDate}" required="true"
							requiredMsg="getText(errors_required, ['发布时间'])"/>
					</td>
					<web:datetime format="yyyy-MM-dd" readOnly="false" id="aptDate" />
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 截止时间
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="aptDuedate"
							name="aptDuedate" type="text" readonly="readonly"
							value="${model.aptDuedate}" required="true"
							requiredMsg="getText(errors_required, ['截止时间'])"/>
					</td>
					<web:datetime format="yyyy-MM-dd" readOnly="false" id="aptDuedate" />
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 约稿内容
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words"
							id="aptContent" name="aptContent" required="true"
							requiredMsg="getText(errors_required, ['约稿内容'])">${model.aptContent}</textarea>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						 备稿单位
					</td>
					<td class="contentTd">
						<s:select class="information_out_input" multiple="true"
					 	name="orgIds" id="orgIds" list="%{orgsMap}"/>
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
		if(validator.form()){
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

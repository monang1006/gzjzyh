<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>公告</title>

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
			请填写公告信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/bulletin!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="blId" value="${model.blId}"/>
			</s:if>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 公告标题：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="blTitle"
							name="blTitle" type="text"
							value="${model.blTitle}" required="true" style="width: 450px"
							requiredMsg="getText(errors_required, ['公告标题'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 公告内容：
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words"
							id="blContent" name="blContent">${model.blContent}</textarea>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="确定" name="save" id="save" />
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
			var bl = $("#blContent").val().length;
			var bt = $("#blTitle").val().length;
			if(bl>800){
				alert("公告内容过长！");
				return false;
			}
			if(bt>100){
				alert("公告标题过长！");
				return false;
			}
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

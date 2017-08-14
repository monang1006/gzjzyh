<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>网上发布</title>

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
			网上发布
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/handling!saveOA.action?toId=%{toId}" theme="simple">
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 信息标题：
					</td>
					<td class="contentTd">${model.pubTitle}</td>
				</tr>
				<s:if test="%{model.TInfoBaseIssue.issIsPublish==\"1\"}">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 是否网上发布：
					</td>
					<td class="contentTd">
						<s:radio name="isOA" id="isOA" list="#{\"1\":'是',\"0\":'否'}" value="%{model.isOA}"/>
					</td>
				</tr>
				</s:if>
			</table>
			</s:form>
		</div>
		<div class="information_list_choose_pagedown">
			<s:if test="%{model.pubUseStatus==\"1\"}">
			<input type="button" class="information_list_choose_button9"
				value="保存" name="save" id="save" />
			</s:if>
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
	
	var isOA = "${model.isOA}";
	if(isOA==""){
		document.getElementsByName('isOA')[1].checked=true;
	}

	
});

</script>

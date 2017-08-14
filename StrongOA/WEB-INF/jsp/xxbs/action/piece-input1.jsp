<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>加分</title>

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
			请填写信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/piece!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="pieceId" value="${model.pieceId}"/>
			</s:if>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 加分标题：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="pieceTitle"
							name="pieceTitle" type="text" style="width: 300px"
							value="${model.pieceTitle}" required="true"
							requiredMsg="getText(errors_required, ['加分标题'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 加分类型：
					</td>
					<td class="contentTd">
					<s:select cssClass="formin" id="pieceOpen" name="pieceOpen" value="%{model.pieceOpen}"
									list="#{\"0\":'微博',\"1\":'创先争优简报',\"2\":'跟班学习',\"3\":'省外约稿',\"4\":'国办约稿'}"
									 listKey="key" listValue="value" />
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 加分时间：
					</td>
					<td class="contentTd">
						<input name="pieceTime" id="pieceTime" title="请输入加分时间" value="${pieceDate}" readonly="readonly">
						<web:datetime format="yyyy-MM-dd" readOnly="true" id="pieceTime" />
					</td>
					
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 得分：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="pieceCode" 
							name="pieceCode" type="text"
							value="${model.pieceCode}" required="true" digits="true" digitsMsg="getText(errors_digits, ['得分'])"
							requiredMsg="getText(errors_required, ['得分'])"/>
					</td>
				</tr>
			</table>
			<input type="hidden" name="pieceFlags" value="4" name="pieceFlags"/>
			<input type="hidden" value="${model.orgId}" name="orgId" id="orgId">
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
			var flag= "${flag}";
			var pt = $("#pieceTitle").val().length;
			if(pt>100){
				alert("加分标题过长！");
				return false;
			}
			var pieceTime = $("#pieceTime").val();
			if(pieceTime==""){
				alert("加分时间不能为空！");
				return false;
			}
			var pieceCode = $("#pieceCode").val();
			if(pieceCode.length>2){
				alert("得分过大！");
				return;
			}
			if(pieceCode==""){
				alert("得分不能为空！");
				return;
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

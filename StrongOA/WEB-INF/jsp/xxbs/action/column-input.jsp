<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title><s:if test="%{op==\"add\"}">新建</s:if><s:else>修改</s:else>栏目</title>

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
			请填写栏目信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/column!save.action" theme="simple">
			
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" name="colId" value="${model.colId}"/>
			</s:if>
				<input type="hidden" name="toId" value="${toId}"/>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 栏目名称：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="colName"
							name="colName" type="text"
							value="${model.colName}" required="true" 
							requiredMsg="getText(errors_required, ['栏目名称'])"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 栏目序号：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="colSort"
							name="colSort" type="text"
							value="${model.colSort}" required="true" digits="true"
							requiredMsg="getText(errors_required, ['栏目序号'])" digitsMsg="getText(errors_digits, ['栏目序号'])"/>
					</td>	
				</tr>
				
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 栏目得分：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="colCode"
							name="colCode" type="text"
							value="${model.colCode}" required="true" digits="true"
							requiredMsg="getText(errors_required, ['栏目得分'])" digitsMsg="getText(errors_digits, ['栏目得分'])"/>
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
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 栏目描述：
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words"
							id="colDetail" name="colDetail">${model.colDetail}</textarea>
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

	/*$("#colSort").blur(function(){
		var sore = $("#colSort").val();
		$.ajax({
			  url: "<%=root%>/xxbs/action/column!isExistSore.action",
			  //async: false,
			  dataType: 'text',
			  type: 'POST',
			  data:{sore:sore},
			  success: function(data){
				if(data == "true"){
					isExistTitle = true;
					//$("#colSort").attr("value", title+"（重复标题）");
					alert("序号已经存在，序号不能重复。");
				}
			  }
		});		
	});*/
	
	//表单提交操作
	$("#save").click(function(){
		var op = "${op}";
		if($("#colName").val().length>50){
			alert("栏目名称过长！")
			return false;
		}
		if($("#colDetail").val().length>200){
			alert("栏目描述过长！")
			return false;
		}
		var colCode = $("#colCode").val();
		if(colCode.length>2){
			alert("栏目得分过大！");
			return false;
		}
		if(validator.form()){
			var sore = $("#colSort").val();
			$.get("<%=root%>/xxbs/action/column!isExistSore.action?sore="+sore,function(ret){
						if(ret == "true"&&op!="edit"){
							alert("序号已经存在，序号不能重复。");
							return false;
						}
						else{
							$("#save").attr("disabled","none");
							$("#myform").submit();
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

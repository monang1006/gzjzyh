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
			请填写约稿信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/invitation!save.action" theme="simple">
			<s:if test="%{op==\"edit\"}">
				<input type="hidden" id="aptId" name="aptId" value="${model.aptId}"/>
			</s:if>
			<input type="hidden" id="aptUserid" name="aptUserid" value="${model.aptUserid}"/>
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000">*</font> 约稿标题：
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
						<font color="#FF0000">*</font> 发布时间：
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
						<font color="#FF0000">*</font> 截止时间：
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
						<font color="#FF0000">*</font> 约稿内容：
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words" style="height:150px"
							id="aptContent" name="aptContent" required="true"
							requiredMsg="getText(errors_required, ['约稿内容'])">${model.aptContent}</textarea>
					</td>
				</tr>
				<tr>
					<td class="labelTd" valign="top">
						 约稿单位：
					</td>
					<td class="contentTd">
						<s:radio name="aptIsAllOrg" id="aptIsAllOrg" list="#{'0':'全部单位','1':'部分单位'}"
						 value="%{model.aptIsAllOrg}"/>
						<div id="orgs">
					   <input type="hidden" id="orgIds" name="orgIds"
							value=""/>
						<textarea rows="3" cols="40" id="appointTitle" readonly="readonly" class="information_out_input_words" style="height:60px">
						</textarea>
						</div>
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
			<input type="hidden" value="${treeId}" name="treeId" id="treeId"/>
			<input type="hidden" value="${checkId}" name="checkId" id="checkId"/>
			<input type="hidden" value="${loadTitle}" name="loadTitle" id="loadTitle"/>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">

$(function(){

	//表单提交操作
	$("#save").click(function(){
		if($("#aptDate").val()==""){
			alert("发布时间不能为空。");
			return;
		}
		if($("#aptDuedate").val()==""){
			alert("截止时间不能为空。");
			return;
		}
		if($("#aptDate").val() > $("#aptDuedate").val()){
			alert("发布时间不能大于截止时间。");
			return;
		}
		if($("#aptTitle").val().length>50){
			alert("约稿标题过长！")
			return;
		}
		if($("#aptContent").val().length>500){
			alert("约稿内容过长！")
			return;
		}
		var val = $("input[name='aptIsAllOrg']:checked").val();
		if(val == "1"){
			var orgIds = $("#orgIds").val();
			if(orgIds==""){
				alert("部分约稿单位不能为空！");
				return;
			}
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
	
	var selectOrg = function(){
		var orgIds = $("#orgs");
		var appointTitle = $("#appointTitle");
		var val = $("input[name='aptIsAllOrg']:checked").val();
		if(val == "0"){
			orgIds.css("display", "none");
			appointTitle.css("display", "none");
		}
		else if(val == "1"){
			var checkId = $("#treeId").val();
			var loadTitle = $("#loadTitle").val();
			var obj = new Object();
			obj.checkId=checkId;
			obj.loadTitle = loadTitle;
			var ret = window.showModalDialog("<%=root%>/xxbs/action/invitation!tree.action",obj,"dialogWidth=600px;dialogHeight=500px");
			//var ret = window.showModalDialog("<%=root%>/xxbs/action/invitation!selectOrg.action",obj,"dialogWidth=600px;dialogHeight=500px");
			//var ret = gl.showSubDialog("<%=root%>/xxbs/action/invitation!tree.action?loadId="+checkId,650,500);
			if(ret != undefined && ret.status == "success"){
				$("#treeId").val(ret.treeId);
				$("#checkId").val(ret.id);
				$("#loadTitle").val(ret.title);
				$("#orgIds").attr("value", ret.id);
				$("#appointTitle").attr("value", ret.title);
			}
			orgIds.css("display", "block");
			appointTitle.css("display", "block");
		}
	};	
	
	var onloadOrg = function(){
		var orgTitle = "${loadTitle}";
		var orgId = "${loadId}";
		var orgIds = $("#orgs");
		var appointTitle = $("#appointTitle");
		var val = $("input[name='aptIsAllOrg']:checked").val();
		if(val == "0"){
			orgIds.css("display", "none");
			appointTitle.css("display", "none");
		}
		else if(val == "1"){
			$("#orgIds").attr("value", orgId);
			$("#appointTitle").attr("value", orgTitle);
			orgIds.css("display", "block");
			appointTitle.css("display", "block");
		}
	};
	$("input[name='aptIsAllOrg']").bind("click", selectOrg);
	$(window).bind("load", onloadOrg);
});
</script>

<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>查看约稿</title>

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
			查看约稿信息
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" action="/xxbs/action/invitation!save.action" theme="simple">
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 约稿标题
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="aptTitle"
							name="aptTitle" type="text"
							value="${model.aptTitle}" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 发布时间
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="aptDate"
							name="aptDate" type="text" readonly="readonly"
							value="${model.aptDate}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 截止时间
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="aptDuedate"
							name="aptDuedate" type="text" readonly="readonly"
							value="${model.aptDuedate}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 约稿内容
					</td>
					<td class="contentTd">
						<textarea rows="5" class="information_out_input_words" style="height:150px"
							id="aptContent" name="aptContent" readonly="readonly">${model.aptContent}</textarea>
					</td>
				</tr>
				<tr>
					<td class="labelTd" valign="top">
						 备稿单位
					</td>
					<td class="contentTd">
						<s:radio name="aptIsAllOrg" id="aptIsAllOrg" list="#{'0':'全部单位','1':'部分单位'}"
						 value="%{model.aptIsAllOrg}" disabled="true"/>
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

	
	$("#cancel").click(function(){
		window.close();
	});
	
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
	
	$(window).bind("load", onloadOrg);
	

});
</script>

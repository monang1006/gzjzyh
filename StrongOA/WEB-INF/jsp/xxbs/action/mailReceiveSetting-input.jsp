<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>邮件接收设置</title>

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

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>

		<div class="windows_title">
			邮件接收设置
		</div>
		<div class="information_out" id="information_out">
			<s:form id="myform" theme="simple">
			<table class="information_list" cellspacing="0" cellpadding="0" align="left">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> pop3地址：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="url"
							name="url" type="text"
							value="${model.url}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> pop3端口：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="port"
							name="port" type="text" digits="true"
							value="${model.port}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 邮件接收地址：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="username"
							name="username" type="text"
							value="${model.username}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 邮件接收密码：
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="password"
							name="password" type="password"
							value="${model.password}"/>
					</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 是否删除邮件：
					</td>
					<td class="contentTd">
						<s:radio name="isdelete" id="isdelete" list="#{true:'是',false:'否'}" value="%{model.isdelete}"/>
					</td>
				</tr>
				<tr>
				<td class="labelTd"></td>
				<td class="contentTd">
					<input type="button" class="information_list_choose_button9"
				value="保存" name="save" id="save" />
				</td>
				</tr>
				<!-- <tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 开始接收时间
					</td>
					<td class="contentTd">
						<s:date var="sdate" name="%{model.startdate}" format="yyyy-MM-dd" />
						<input class="information_out_input" id="startdate"
							name="startdate" type="text"
							value="${sdate}"/>设置邮件接收的起始日期，留空则从当日起接收。
					</td>
					<web:datetime format="yyyy-MM-dd" readOnly="true" id="startdate" />
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 定时接收周期
					</td>
					<td class="contentTd">
						<input class="information_out_input" id="interval"
							name="interval" type="text" digits="true"
							value="${model.interval}"/>设置邮件接收的间隔时间，单位是分钟，如每隔1小时到pop3邮箱收取邮件。
					</td>
				</tr> -->
			</table>
				<input type="hidden" id="interval"
							name="interval"
							value="60"/>
			</s:form>
		</div>
		<!--  <div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="保存" name="save" id="save" />-->
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
			//$("#myform").submit();
			var data = $("#myform").serialize();
			var url = "<%=root%>/xxbs/action/mailReceiveSetting!save.action";
			$.ajax({
				type: 'POST',
				url: url,
				data: data,
				success: function(ret){
					if(ret == "success"){
						alert("保存成功");
					}
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

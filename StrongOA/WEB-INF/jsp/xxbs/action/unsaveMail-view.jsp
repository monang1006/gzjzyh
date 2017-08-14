<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>查看邮件</title>

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
			查看邮件
		</div>
		<div class="information_out" id="information_out">
			<table class="information_list" cellspacing="0" cellpadding="0">
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 主题
					</td>
					<td class="contentTd">${model.subject}</td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 邮箱
					</td>
					<td class="contentTd"><input type="text" readOnly="readOnly" value="${model.mailAddress}"/></td>
				</tr>
				<tr>
					<td class="labelTd">
						<font color="#FF0000"></font> 时间
					</td>
					<td class="contentTd"><s:date name="%{model.sentDate}" format="yyyy-MM-dd HH:mm" /></td>
				</tr>
				<tr>
					<td class="labelTd" valign="top">
						<font color="#FF0000"></font> 内容
					</td>
					
					<td class="contentTd"><textarea style="height:280px" rows="5" class="information_out_input_words" readOnly="readOnly"
					>${model.content}</textarea>
					</td>
				</tr>
			</table>
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
	
});

</script>

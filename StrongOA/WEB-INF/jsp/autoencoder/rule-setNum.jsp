<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>部门文号规则</title>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script type="text/javascript" language="javascript"
			src="<%=root%>/uums/js/md5.js"></script>
		<style type="text/css">
body,table,tr,td,div {
	margin: 0px;
}
</style>
		<script type="text/javascript">
		
	String.prototype.trim = function() {
		var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
		strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, "");
		return strTrim;
	}
	
		//判断为小于12位的正整数
	function iszzs(number){
		var r =/^[1-9]\d*$/;  // /^\d+$/   // /[1-9]d*|0$/;
		if(r.test(number)==false){
			return true;
		}else{
			//alert("length:"+number.length);
			if(number.length>12){
				return true;
			}else{
				return false;
			}
		}
	}

	function save() {
		//var id = document.getElementById("id").value;
		var rstart = document.getElementById("rstart").value;
		var rstep = document.getElementById("rstep").value;
		var rend = document.getElementById("rend").value;
		var rnow = document.getElementById("rnow").value;
		var nowYear = document.getElementById("nowYear").value;

		if(iszzs(rstart)){
			alert("开始字段不能为空且只能为小于12位的非负整数，请您填写开始数值");
			return false;
		}
		if(iszzs(rend)){
			alert("结束字段不能为空且只能为小于12位的非负整数，请您填写结束数值");
			return false;
		}
		if(Number(rstart)>Number(rend)){
			alert("对不起，您填写的结束值小于开始值,不符合编号生成规则");
			return false;
		}
		if(iszzs(rstep)){
			alert("步长字段不能为空且只能为小于12位的非负整数，请您填写步长数值");
			return false;
		}
		if(Number(rstart) + Number(rstep)>= Number(rend)){
			alert("您设置的编号开始值加步长值已经大于了结束值,不能生成下一个编号");
			return false;
		}
		if(Number(rnow)>Number(rend)){
			alert("您设置的编号当前值已经大于了结束值,不符合编号生成规则");
			return false;
		}
		if(Number(rnow) + Number(rstep)>= Number(rend)){
			alert("您设置的编号当前值加步长值已经大于了结束值,不能生成下一个编号");
			return false;
		}
		var JSONStr = '{ "rstart":"' + rstart + '", "rstep":"' + rstep+ '", "rend":"' + rend + '", "rnow":"' + rnow+ '", "nowYear":"' + nowYear+ '"}';
		var JSONObj = eval('(' + JSONStr + ')');
		window.returnValue=JSONObj;    
		window.close();


	}
</script>
	</head>
	<base target="_self" />
	<body>
		<DIV align="center">
			<table border="0" cellpadding="0" cellspacing="1" width="100%"
				height="100%">
				<tr>
					<td valign="top" align="left" width="70%">
						<s:form action="/rule/rule!input.action" name="form" method="post"
							enctype="multipart/form-data">
							<input type="hidden" id="id" name="orgId" value="${orgId}" />
							<input type="hidden" id="num" name="num" value="${ruleName}" /> 
							<table border="0" cellpadding="0" cellspacing="1" class="table1"
								width="100%">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">部门名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${orgName}
									</td>
								</tr>

								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">开始：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="rstart" name="rstart" type="text" value="${rstart}" size="30"
											maxlength="16">
									</td>
								</tr>

								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">结束值：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="rend" name="rend" type="text" value="${rend}" size="30"
											maxlength="16">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">步长：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="rstep" name="rstep" type="text" value="${rstep}" size="30"
											maxlength="16">
									</td>
								</tr>


								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">当前值：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="rnow" name="rnow" readonly="true" value="${rnow}" type="text"
											size="30" maxlength="16">
									</td>
								</tr>

								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">当前年份：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="nowYear" name="nowYear" value="${nowYear}"
											readonly="true" type="text" size="30" maxlength="16">
									</td>
								</tr>
							</table>
						</s:form>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="30%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%">
												<input name="Submit2" type="submit" class="input_bg"
													value="保 存" onclick=save();>

											</td>
											<td width="30%">
												<input name="button" type="button" class="input_bg"
													value="关 闭" onclick=window.close();>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

		</DIV>
	</body>
</html>

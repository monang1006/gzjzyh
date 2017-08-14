<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.strongit.oa.util.GlobalBaseData"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-remind" prefix="remind"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<title>输入意见</title>
<link href="<%=frameroot%>/css/properties_windows_add.css"
	type="text/css" rel="stylesheet">
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
	type="text/javascript">
</script>
<script type="text/javascript">

String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}

//获取处理意见
function getSuggestion() {
	var suggestionValue = $.trim($("#suggestion").val());
	if (suggestionValue == "") {
		//if(confirm("您确定不输入处理意见吗？")){
		//suggestionValue = "您的任务被退回";
		//}else{
		//	return ;
		//}
		alert("请输入处理意见！");
		return;
	}
	var remindType = "";
	$("input:checkbox:checked").each(function() {
		remindType = remindType + $(this).val() + ",";
	});
	if (remindType != "") {
		remindType = remindType.substring(0, remindType.length - 1);
	}
	/*2011-11-07 yanjian 处理一些特殊字符，这些字符导致无法正常查看流程图*/
	/**/
	if (suggestionValue.indexOf("\r") != -1) { //处理回车
		suggestionValue = suggestionValue.replaceAll("\r", "");
	}
	if (suggestionValue.indexOf("\n") != -1) { //处理换行
		suggestionValue = suggestionValue.replaceAll("\n", "＜BR＞");
	}

	if (suggestionValue.indexOf("\"") != -1) { //处理英文形式的双引号
		suggestionValue = suggestionValue.replaceAll("\"", "“");
	}
	if (suggestionValue.indexOf("\'") != -1) { //处理英文形式的单引号
		suggestionValue = suggestionValue.replaceAll("\'", "’");
	}
	if (suggestionValue.indexOf("<") != -1) { //处理英文形式的<
		suggestionValue = suggestionValue.replaceAll("<", "＜");
	}
	if (suggestionValue.indexOf(">") != -1) { //处理英文形式的>
		suggestionValue = suggestionValue.replaceAll(">", "＞");
	}
	if (suggestionValue.indexOf("\\") != -1) { //处理英文形式的\
		suggestionValue = suggestionValue.replace(/[\\]/gm, "＼");
	}
	if (suggestionValue.indexOf("%") != -1) { // 处理英文形式的的%
		suggestionValue = suggestionValue.replace(/[%]/gm, "％");
	}
	var returnValue = "{suggestion:'" + suggestionValue + "',remindType:'"
			+ remindType + "'}";
	window.returnValue = returnValue;
	window.close();
}
var cap_max = 200;//可发送的字数
function onCharsChange(varField, dd) {
	var suggestion = document.getElementById("suggestion");
	var charsmonitor1 = document.getElementById("charsmonitor1");
	var charsmonitor2 = document.getElementById("charsmonitor2");
	var leftChars = getLeftChars(varField);
	if (leftChars >= 0) {
		//charsmonitor1.value=cap_max-leftChars;
		//charsmonitor2.value=leftChars;
		charsmonitor1.innerHTML = cap_max - leftChars;
		charsmonitor2.innerHTML = leftChars;
		return true;
	} else {
		charsmonitor1.value = cap_max;
		charsmonitor2.value = "0";
		window.alert("意见内容超过字数限制!");
		var len = suggestion.value.length + leftChars;
		suggestion.value = suggestion.value.substring(0, len);
		leftChars = getLeftChars(suggestion);
		if (leftChars >= 0) {
			charsmonitor1.innerHTML = cap_max - leftChars;
			charsmonitor2.innerHTML = leftChars;
		}
		return false;
	}
	$('#suggestion').focus();
}
function getLeftChars(varField) {
	var cap = cap_max;
	var leftchars = cap - varField.value.length;
	return (leftchars);
}
</script>
</head>
<base target="_self">
<body class="contentbodymargin">
	<DIV id=contentborder>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
			<tr>

				<form id="rendForm" theme="simple" action="" method="POST">
					<%--<table height="30" width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													请输入意见
												</td>
												<td width="15%">
													&nbsp;
												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
								--%>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="3" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif"> &nbsp;</td>
										<td align="left"><strong>请输入意见</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<%--<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="getSuggestion();">
														&nbsp;确&nbsp;定&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close();">
														&nbsp;取&nbsp;消&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													<td width="6"></td>

												</tr>
											--%>
											<tr>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="getSuggestion();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
									</tr>
											
											
											</table></td>
									</tr>
								</table></td>
						</tr>




						<tr>
							<td>&nbsp; <textarea onpaste="return onCharsChange(this);"
									onpropertychange="return onCharsChange(this);"
									onfocus="return onCharsChange(this);" id="suggestion" rows="17"
									cols="62" name="suggestion"></textarea></td>
						</tr>
						<tr>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
									<tr>
										<span class="wz"><font color="#999999">
												&nbsp;已输入<font color="green"><span id="charsmonitor1">0</span>
											</font>个字，剩余<font color="blue"><span id="charsmonitor2">200</span>
											</font>个字，最多输入<font color="red">200</font>个字 </font> </span>
										<!-- 用不同颜色表示不同状态的字数 -->
									</tr>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="20%" height="21" class="biao_bg1">&nbsp;提醒方式：
											&nbsp; <%--<remind:remind msgChecked="checked"  isOnlyRemindInfo="true" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>--%>
											<remind:remind isShowButton="false" isOnlyRemindInfo="true"
												includeRemind="RTX,SMS" rtxChecked="checked"
												code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>" /></td>
									</tr>
									<tr>
										<td class="table_td"></td>
										<td></td>
									</tr>

								</table>
					</table>

					<br />
					<%--<div align="center">
									<input id="sb" type="button" class="input_bg" value="确 定" onclick="getSuggestion();">&nbsp;&nbsp;
									<input name="Submit2" type="button" class="input_bg" value="关 闭" onclick="window.close();">
								</div>
						--%>
				</form>

			</tr>
		</table>
	</DIV>
</body>
</html>
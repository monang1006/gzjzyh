<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>增加模块</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
		<script type="text/javascript">
$(document).ready(function(){
	$("#save").click(function(){
		document.getElementById("save").submit();
	});
});
</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTableForm"
				action="/updatebadwords/phrasefilter/phraseFilter!save.action"
				theme="simple">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													添加过滤模块
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td width="35%">
													<br>
													<br>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="filtrateModuleId" name="filtrateModuleId"
								type="hidden" size="32" value="${modle.filtrateModuleId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">过滤模块：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<%--					<select id="moduleId" name="moduleId">--%>
										<%--						<option value="0">内部邮件</option>--%>
										<%--						<option value="1">内部短信</option>--%>
										<%--						<option value="2">手机短信</option>--%>
										<%--					</select>      --%>
										<input type="text" id="moduleId" value="${modle.moduleId}"
											size="30">

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否开启：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<%--					<select id="filtrateOpenstate" name="filtrateOpenstate">--%>
										<%--						<option value="0">关闭</option>--%>
										<%--						<option value="1">开启</option>--%>
										<%--					</select>  --%>
										<input type="text" id="filtrateOpenstate"
											value="${modle.filtrateOpenstate}" size="30">
									</td>
								</tr>
								<%--			<tr>--%>
								<%--				<td width="15%" height="21" class="biao_bg1" align="right">--%>
								<%--					<span class="wz">审核人员：</span>--%>
								<%--				</td>--%>
								<%--				<td class="td1" colspan="3" align="left">--%>
								<%--					<input id="name" name="name" type="text" size="22"><input name="gorscelet" type="submit" class="input_bg" value="选 择">--%>
								<%--				</td>--%>
								<%--			</tr>--%>
								<%--			<tr>--%>
								<%--				<td width="15%" height="21" class="biao_bg1" align="right">--%>
								<%--					<span class="wz">短信提醒：</span>--%>
								<%--				</td>--%>
								<%--				<td class="td1" colspan="3" align="left">--%>
								<%--					<input name="method" type="checkbox" value="0">使用内部短信提醒审核人员--%>
								<%--					<input name="method" type="checkbox" value="1">使用手机短信提醒审核人员--%>
								<%--				</td>--%>
								<%--			</tr>--%>
								<%--			<tr>--%>
								<%--				<td width="15%" height="21" class="biao_bg1" align="right">--%>
								<%--					<span class="wz">禁止提示：</span>--%>
								<%--				</td>--%>
								<%--				<td class="td1" colspan="3" align="left">--%>
								<%--					<textarea rows="3" cols="40"></textarea>&nbsp;内容被禁止时提示的消息，为空则不提示--%>
								<%--				</td>--%>
								<%--			</tr>--%>
								<%--			<tr>--%>
								<%--				<td width="15%" height="21" class="biao_bg1" align="right">--%>
								<%--					<span class="wz">审核提示：</span>--%>
								<%--				</td>--%>
								<%--				<td class="td1" colspan="3" align="left">--%>
								<%--					<textarea rows="3" cols="40"></textarea>&nbsp;内容需先审核才可通过时提示的消息，为空则不提示--%>
								<%--				</td>--%>
								<%--			</tr>--%>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">过滤提示：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="filtrateMsg" name="filtrateMsg" rows="5"
											cols="50" style="overflow: auto;">${modle.filtrateMsg}</textarea>
										<%--					&nbsp;内容被过滤时提示的消息，为空则不提示--%>
									</td>
								</tr>
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<%--							<td width="29%">--%>
												<%--								<input name="save" type="submit" class="input_bg" value="保 存">--%>
												<%--							</td>--%>
												<td width="37%">
													<input name="back" type="submit" class="input_bg"
														value="关 闭" onclick="window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>

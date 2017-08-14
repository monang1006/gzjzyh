<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>查看允许访问IP段</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm"
							action="">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													&nbsp;&nbsp;&nbsp;查看允许访问IP段
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="loginId" name="loginId" type="hidden" size="32"
								value="${toaSysmanageLogin.loginId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">起始IP(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="loginBeginIp" name="loginBeginIp" type="text"
											size="32" value="${toaSysmanageLogin.loginBeginIp}"
											readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">结束IP(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="loginEndIp" name="loginEndIp" type="text" size="32"
											value="${toaSysmanageLogin.loginEndIp}" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">备注：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="loginDesc" name="loginDesc" rows="5" cols="30"
											style="overflow: auto;" >${toaSysmanageLogin.loginDesc}</textarea>
									</td>
								</tr>
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													
												</td>
												<td width="37%">
													<input id="close()" name="close" type="button"
														class="input_bg" value="关 闭" onclick="window.close()">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>查看班次</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/attendance/arrange/schedules!save.action">
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
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													查看班次
												</td>
												<td width="70">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="groupId" name="groupId" type="hidden" size="32"
								value="${groupId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">班次名称：(<FONT color="red">*</FONT>)</span>
									</td>
									<td class="td1" align="left">
										<input id="schedulesName" name="model.schedulesName"
											maxlength="25" type="text" size="49"
											value="${model.schedulesName}" readonly="readonly">
										<input id="isrest" name="model.isrest" maxlength="25"
											type="hidden" size="20" value="0">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">上班时间：(<FONT color="red">*</FONT>)</span>
									</td>
									<td class="td1" align="left">
										<input id="workStime" name="model.workStimee"
											maxlength="25" type="text" size="20"
											value="${model.workStime}" readonly="readonly">		  
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">上班登记有效时间段(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="registerStimeOn" name="model.registerStimeOn"
											maxlength="25" type="text" size="20"
											value="${model.registerStimeOn}" readonly="readonly">
										至
										<input id="registerEtimeOn" name="model.registerEtimeOn"
											maxlength="25" type="text" size="20"
											value="${model.registerEtimeOn}" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">下班时间(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="workEtime" name="model.workEtime"
											maxlength="25" type="text" size="20"
											value="${model.workEtime}" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">下班登记有效时间段(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="registerStimeOff" name="model.registerStimeOff"
											maxlength="25" type="text" size="20"
											value="${model.registerStimeOff}" readonly="readonly">
										至
										<input id="registerEtimeOff" name="model.registerEtimeOff"
											maxlength="25" type="text" size="20"
											value="${model.registerEtimeOff}" readonly="readonly">		
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">迟到（分）：</span>
									</td>
									<td class="td1" align="left">
										<input id="laterTime" name="model.laterTime" maxlength="25"
											type="text" size="20" value="${model.laterTime}" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">早退（分）：</span>
									</td>
									<td class="td1" align="left">
										<input id="earlyTime" name="model.earlyTime" maxlength="25"
											type="text" size="20" value="${model.earlyTime}"  readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">旷工（分）(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="skipTime" name="model.skipTime" maxlength="25"
											type="text" size="20" value="${model.skipTime}" readonly="readonly">	
									</td>
								</tr>
							  	<s:if test="scheGroup.logo!=null&&scheGroup.logo==\"1\"">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">班次所在班组序号(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="schedulesOrder" name="model.schedulesOrder" maxlength="25"
											type="text" size="20" value="${model.schedulesOrder}" readonly="readonly">		
									</td>
								</tr>
								</s:if>
							</table>
							<br>
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
													<input id="close" type="button" class="input_bg"
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

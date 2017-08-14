<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>法定假日</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
		
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin onload="" oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>

		<s:form id="mykmForm" theme="simple"  action="/attendance/holiday/holiday!save.action" modth="post">
		<input type="hidden" name="model.holidayId" id="holidayId" value="${model.holidayId }">
		<input type="hidden" name="model.isEnable" id="isEnable" value="${model.isEnable}">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
												查看法定假日
												</td>
												<td width="10%">&nbsp;
												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">假日名称：</span>								</td>
									<td width="79%" colspan="3" align="left" class="td1">
									<input id="holidayName" size="42" maxlength="25" name="model.holidayName" type="text" readonly="readonly" value="${model.holidayName}" >&nbsp;
												

								  </td>
								</tr>
								
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">规定时间：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<input id="holidayStime" size="42" maxlength="25" style="width:110px;" name="model.holidayStime" type="text" readonly="readonly" value="${model.holidayStime}" >&nbsp;
										
										至
											<input id="holidayEtime" size="42"  style="width:110px;"  maxlength="25" name="model.holidayEtime" type="text" readonly="readonly" value="${model.holidayEtime}" >&nbsp;
									
									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">生效时间：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<strong:newdate id="henableTime" name="model.henableTime"
											dateform="yyyy-MM-dd" width="110"  disabled="true"
											dateobj="${model.henableTime}" isicon="false" />

									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">失效时间：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<strong:newdate id="hdisableTime" name="model.hdisableTime"
											dateform="yyyy-MM-dd" width="110"  disabled="true"
											dateobj="${model.hdisableTime}" isicon="false"/>
									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否启用：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<s:select id="isEnable" style="width: 110px;" disabled="true"
											name="model.isEnable" list="#{'是':'0','否':'1'}"
											listKey="value"  listValue="key"></s:select>
											</td>
								</tr>
						    	<tr>
									<td height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">描    述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
									<textarea  id="holidayDesc" rows="5" cols="35" readonly="readonly"  name="model.holidayDesc" maxlength="200"  >${model.holidayDesc}</textarea>
									

									</td>
								</tr>
								
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
						<tr><td></td></tr>
							<tr>
								<td align="center" valign="middle">
                                              &nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;
												<input name="Submit2" type="button" class="input_bg" value="关    闭" onClick="window.close();">
									
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

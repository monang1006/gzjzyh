<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>档案销毁申请单</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
			<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
			<script type="text/javascript">
			window.onload = function()
			{
			  document.getElementById('testearea').onkeydown = function()
			  {    
			    if(this.value.length >=200)
			      event.returnValue = false;
			  }
			}
			</script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" name="myTableForm" theme="simple"
				action="/archive/archiveDestr/archiveDestr!save.action">
				<s:hidden id="folderId" name="folderId"></s:hidden>
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
													填写档案销毁申请单
												</td>
												<td width="70%"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="90%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">案卷编号：</span>
									</td>
									<td class="td1" align="left">
										<input id="destroyFolderNo" name="destroyFolderNo" type="text"
											size="30" value="${model.destroyFolderNo}"
											readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">案卷名称：</span>
									</td>
									<td class="td1" align="left">
										<input id="destroyFolderName" name="destroyFolderName"
											maxlength="25" type="text" size="30" value="${model.destroyFolderName}"
											readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">销毁申请人(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="destroyApplyName" name="destroyApplyName"
											type="text" maxlength="25" readonly="readonly" size="30" value="${model.destroyApplyName }" class="required" maxlength="10">
									</td>
								</tr>
								
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="center"
										colspan="2">
										<span class="wz">销毁申请原因(<FONT color="red">*</FONT>)：</span>
									</td>
								</tr>
								<tr>
									<td class="td1" colspan="2" align="left">
										<textarea  id="testearea" rows="10" maxlength="400"  class="required"  cols="80" name="model.destroyApplyDesc">${model.destroyApplyDesc}</textarea>
									<br><font color="red">请不要超过200字</font>
									</td>
								</tr>
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input name="Submit" type="submit" class="input_bg"
														value="申 请">
												</td>
												<td width="37%">
													<input name="Submit2" type="button" class="input_bg"
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

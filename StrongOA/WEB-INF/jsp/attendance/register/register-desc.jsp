<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>情况说明</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript" language="javascript"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
		
		function onSub(){
		   if($("#attendDesc").val()==""){
			   alert("请填写情况说明！");
			   return ;
		   }
		   if($("#attendDesc").val().length>200){
			   alert("说明请不要超过200字！");
			   return;
		   }
		   $("#mykmForm").submit();
		}
 		</SCRIPT>
	</head>
	<base target="_self" />
	<body class=contentbodymargin onload="" oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js"
			type="text/javascript"></script>
		<DIV id="contentborder" align="center">
			<s:form id="mykmForm" theme="simple"
				action="/attendance/register/register!saveDesc.action" method="post">
				<input type="hidden" name="attendId" id="attendId"
					value="${model.attendId}">
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
													情况说明
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">情况说明(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="attendDesc" rows="5" cols="35" name="attendDesc">${model.attendDesc}</textarea>
									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td></td>
								</tr>
								<tr>
									<td align="center" valign="middle">
										<s:if test="model.attendId!=null&&model.attendId!=\"\"">
											<input name="Submit" type="button" class="input_bg"
												value="保  存" onClick="onSub();">
										&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
										<input name="Submit2" type="button" class="input_bg"
												value="关 闭" onClick="window.close();">
										</s:if>
										<s:else>
											<input name="Submit2" type="button" class="input_bg"
												value="没有找到相应的考勤记录，请点击按钮关闭窗口！" onClick="window.close();">
										</s:else>
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

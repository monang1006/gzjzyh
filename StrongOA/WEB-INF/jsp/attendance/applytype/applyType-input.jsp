<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>申请单类型</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
		function onSub(){
		   if($.trim($("#typeName").val())==""){
		   alert("请填写申请类型名称！");
		   return ;
		   }
		   if($("#typeDesc").val().length>200){
		   alert("描述请不要超过200字！");
		   return;
		   }
		   $("#mykmForm").submit();
		}
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin onload="" oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
        <s:form id="mykmForm" theme="simple"  action="/attendance/applytype/applyType!save.action" modth="post">
		<input type="hidden" name="model.typeId" id="typeId" value="${model.typeId }">
		<input type="hidden" name="model.isSystem" id="isSystem" value="${model.isSystem }">
		<input type="hidden" name="model.isApplyType" id="isApplyType" value="${model.isApplyType }">
		<input type="hidden" name="model.typeCreateDate" id="typeCreateDate" value="${model.typeCreateDate}">
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
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
												<s:if test="model.typeId==null||model.typeId==''">
													添加申请单类型
												</s:if>
												<s:else>
													编辑申请单类型
												</s:else>
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								
								<tr>
									<td width="40%" height="21" class="biao_bg1" align="right">
										<span class="wz">类型名称(<font color="red">*</font>)：</span>								</td>
									<td width="60%" colspan="3" align="left" class="td1">
										<s:if test="model.typeId==null||model.typeId==''">
											<input id="typeName" maxlength="25" name="model.typeName"
												type="text" value="${model.typeName}">&nbsp;
										</s:if>
										<s:else>
											<input id="typeName" maxlength="25" readonly="readonly"
												name="model.typeName" type="text" size="50"
												value="${model.typeName}">&nbsp;
										</s:else>

									</td>
								</tr>
								
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">补填状态(<font color="red">*</font>)：</span>									</td>
									<td class="td1" colspan="3" align="left">
										<s:select id="canRewriter" style="width: 110px;"
											name="model.canRewriter" list="#{'能补填':'0','不能补填':'1'}"
											listKey="value" listValue="key"></s:select>
									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">缺勤状态(<font color="red">*</font>)：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<s:select id="isAbsence" style="width: 110px;"
											name="model.isAbsence" list="#{'统计缺勤':'0','不统计缺勤':'1'}"
											listKey="value" listValue="key"></s:select>

									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否启用(<font color="red">*</font>)：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<s:select id="isEnable" style="width: 110px;"
											name="model.isEnable" list="#{'是':'0','否':'1'}"
											listKey="value" listValue="key"></s:select>
									</td>
								</tr>
						    	<tr>
									<td height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">类型描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
									<textarea  id="typeDesc" rows="5" cols="35"   name="model.typeDesc" maxlength="200"  >${model.typeDesc}</textarea>
									

									</td>
								</tr>
								
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
						<tr><td></td></tr>
							<tr>
								<td align="center" valign="middle">

												<input name="Submit" type="button" class="input_bg" value="保  存" onClick="onSub();">
											&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;
												<input name="Submit2" type="button" class="input_bg" value="取  消" onClick="window.close();">
									
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

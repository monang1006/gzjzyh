<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存套红类别</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<SCRIPT type="text/javascript">
		function validatesubmit(){
			var form=document.getElementById("docredTypeForm");
			return true;
		}
		setPageListenerEnabled(true);
	</SCRIPT>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="docredTypeForm" theme="simple"
							action="<%=root%>/docredtemplate/docredtype/docRedType!save.action" onSubmit="return validatesubmit();" method="POST">
							<table height="30" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													保存公文套红类别
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="redtempGroupId" name="model.redtempGroupId" type="hidden"
								size="32" value="${model.redtempGroupId}">
							<input id="redtempGroupParentId" name="model.redtempGroupParentId" type="hidden"
								size="32" value="${model.redtempGroupParentId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
<%--								<tr>--%>
<%--									<td width="30%" height="21" class="biao_bg1" align="right">--%>
<%--										<span class="wz">所属模板类别：</span>--%>
<%--									</td>--%>
<%--									<td class="td1" colspan="3" align="left">--%>
<%--										<s:if test="doctempTypeName==null">--%>
<%--											公文模板类别--%>
<%--										</s:if>--%>
<%--										<s:else>--%>
<%--											${doctempTypeName}--%>
<%--										</s:else>--%>
<%--										--%>
<%--									</td>--%>
<%--								</tr>--%>
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">模板套红名称(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="redtempGroupName" name="model.redtempGroupName" type="text" maxlength="25" title="请将字数控制在50以内"
											size="32" value="${model.redtempGroupName}" class="required">
									</td>
								</tr>

							</table>
							<br>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="50%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="33%">
													<input id="sb" type="submit" class="input_bg" value="保 存">
												</td>
												<td width="33%">
													<input name="Submit2" type="button" class="input_bg"
														value="关 闭" onclick="window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

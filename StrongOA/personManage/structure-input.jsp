<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>编制编辑</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
			
		//关闭窗体
		function col(){
		window.close();
		}
		
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin onload="" oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>

		<s:form id="mykmForm" theme="simple"  action="/knowledge/mykm/mykm!save.action" modth="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
							
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="5%" align="center">
													<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
												</td>
												<td width="50%">
												
													保存编制
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
										<span class="wz">所属机构(<font color="red">*</font>)：</span>								</td>
									<td width="79%" colspan="3" align="left" class="td1">
									<input id="mykmName" maxlength="25" name="model.mykmName" type="text" size="50" value="${model.mykmName}" >&nbsp;
												

								  </td>
								</tr>
								
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">编制类型(<font color="red">*</font>)：</span>									</td>
									<td class="td1" colspan="3" align="left">
										<input id="mykmSource" name="model.mykmSource" maxlength="100" type="text" size="50" value="${model.mykmSource}" > 

									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">编制数量(<font color="red">*</font>)：</span>									</td>
									<td class="td1" colspan="3" align="left">
									<s:if test="model.mykmUrl!=null&&model.mykmUrl!=''">
										<input id="mykmUrl" disabled="disabled" name="model.mykmUrl"  size="50" value="${model.mykmUrl}" >
										</s:if>
										<s:else>
										<input id="mykmUrl"  name="model.mykmUrl" maxlength="100"  size="50" value="${model.mykmUrl}" >
										</s:else>
									</td>
								</tr>
								
						
								<tr>
									<td height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
									<textarea  id="mykmDesc" rows="5" cols="35"  onkeydown="" name="model.mykmDesc" maxlength="200"  class="required">${model.mykmDesc}</textarea>
									

									</td>
								</tr>
								
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
						<tr><td></td></tr>
							<tr>
								<td align="center" valign="middle">

												<input name="Submit" type="button" class="input_bg" value="保  存" onClick="onSub()">
											&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;
												<input name="Submit2" type="button" class="input_bg" value="取  消" onClick="col();">
									
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

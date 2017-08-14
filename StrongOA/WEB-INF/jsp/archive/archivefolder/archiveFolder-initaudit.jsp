<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>审核案卷</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			
			function cit(){
				if(document.getElementById("folderArchiveNo").value.length > 15){
	        		alert("归档号太长！");
					return;
	        	}
	        	if($("#folderAuditingContent").val().length>200){
	        	    alert("审核已太迟太长！请不要超过200字!");
					return;
	        	}

	        	document.getElementById("archiveFolderForm").submit();
			}
			
		
		</script>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="archiveFolderForm" theme="simple" action="/archive/archivefolder/archiveFolder!audit.action">
				<input id="folderId" name="folderId" type="hidden" size="22" value="${folderId}">
<%--				<input id="folderAuditingContent" name="model.folderAuditingContent" type="hidden" size="22">--%>
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
													档案案卷归案审核
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
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否通过：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input type="radio" id="folderAuditing" name="model.folderAuditing" value="1" checked="checked">
										通过
										<input type="radio" id="folderAuditing" name="model.folderAuditing" value="3">
										不通过
									</td>
								</tr>
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">归档号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
										<input id="folderArchiveNo" name="model.folderArchiveNo" type="text" size="22" class="required">
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1"  align="right">
										<span class="wz">审核意见：</span>
									</td>
									<td class="td1" align="left" colspan="2">
									<textarea  id="folderAuditingContent" rows="10"   cols="50" name="model.folderAuditingContent"></textarea><br>
										
								</tr>
								
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input name="Submit1" type="button" class="input_bg" value="确 定" onclick="cit();">
												</td>
												<td width="37%">
													<input name="Submit2" type="button" class="input_bg" value="取 消" onclick="window.close();">
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

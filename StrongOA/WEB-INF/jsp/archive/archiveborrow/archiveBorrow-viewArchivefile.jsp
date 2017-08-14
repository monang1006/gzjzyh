<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>查看文件</title>
		<%@include file="/common/include/meta.jsp"%>
				<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			function back(){
				location = "<%=path%>/archive/archiveborrow/archiveBorrow.action";
			}
			
			function view(value){
				 var Width=screen.availWidth-10;
              	 var Height=screen.availHeight-30;
              	 var fileId=document.getElementById("fileId").value;
				 var ReturnStr=OpenWindow("<%=root%>/archive/archivefile/achiveFile!readAnnex.action?fileId="+fileId+"&appendId="+value,Width, Height, window);
			}
			function viewAnnex(value){	//查看附件
				 var fileId=document.getElementById("fileId").value;
	           var frame=document.getElementById("annexFrame");
	           frame.src="<%=path%>/archive/archivefile/achiveFile!downloadAppend.action?appendId="+value+"&fileId="+fileId;
            }
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<input type="hidden" id="folderId" name="folderId" value="${folderId}">
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
												查看档案文件
											</td>
											
											<td width="70%">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<input type="hidden" id="fileId" name="model.fileId" value="${toaarchivefile.fileId}">
						<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							<tr>
								<td width="18%" height="21" class="biao_bg1" align="right">
									<span class="wz">文件编号：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${toaarchivefile.fileNo}
								</td>
							</tr>
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">作者名：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${toaarchivefile.fileAuthor}
								</td>
							</tr>
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">文件题名：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${toaarchivefile.fileTitle}
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件创建日期：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${createDateFile }
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件页号：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${toaarchivefile.filePage}
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件所属部门：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${fileDepartmentName}
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件备注：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${toaarchivefile.fileDesc}
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件内容：</span>
								</td>
								
								<td class="td1" colspan="3" align="left">
								<s:if test="append!=null&&append.appendId!=null">
										${appendNames }
										</s:if>
									<s:else>
										暂无附件
									</s:else>
									
								</td>
							</tr>
						</table>
						<iframe id="annexFrame" style="display:none"></iframe>
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="50%" align="center">
												<input name="back" type="button" class="input_bg" value="返 回" onclick="back()">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

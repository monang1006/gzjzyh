<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<SCRIPT>
 			function userselect(){
 				alert("选择用户！");
 			}
 			
 			window.onload = function()
			{
			   var value="${model.borrowAuditing }";
			   if(value=="0"){
			   $("#diting ").append("待审");
			   }else if(value=="1"){
			   $("#diting ").append("已审");
			   }else if(value=="3"){
			    $("#diting ").append("驳回");
			   }
			}
 		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>

						<s:form id="archiveBorrowForm" theme="simple" action="/archive/archiveborrow/archiveBorrow!save.action">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
							<input id="fileId" name="fileId" type="hidden" size="22" value="${fileId}">
							<input id="folderId" name="folderId" type="hidden" size="22" value="${folderId}">
							<input id="borrowId" name="model.borrowId" type="hidden" size="22" value="${model.borrowId}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>案卷文件借阅申请单</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="history.go(-1);">&nbsp;返&nbsp;回&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
			
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">所属案卷名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.toaArchiveFile.toaArchiveFolder.folderName}</span>
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">文件编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<span class="wz">${model.toaArchiveFile.fileNo}</span>
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">文件名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									<span class="wz">${model.toaArchiveFile.fileTitle}</span>
										
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">借阅人：</span>
										<input id="borrowPersonid" disabled="disabled" name="model.borrowPersonid" type="hidden" size="22" value="${model.borrowPersonid}">
									</td>
									<td class="td1" style="word-break:break-all;line-height: 1.4" align="left">
									  <span class="wz">${model.borrowPersonname}</span>
										
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">借阅开始时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									  <s:date name="model.borrowFromtime" format="yyyy年MM月dd日 HH点mm分"/>
										
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">借阅归还时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									  <s:date name="model.borrowEndtime" format="yyyy年MM月dd日 HH点mm分"/>
										
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">申请时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
									  <s:date name="model.borrowTime" format="yyyy年MM月dd日 HH点mm分"/>
										
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">审核情况：</span>
									</td>
									<td class="td1" id="diting" colspan="3" align="left">
									
									</td>
								</tr>
								<s:if test="model.borrowAuditing!='0'">
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">审核意见：</span>
									</td>
									<td class="td1" style="word-break:break-all;line-height: 1.4" align="left">
										
									  <span class="wz">${model.borrowAuditingDesc}</span>
									</td>
								</tr>
								</s:if>
								<s:else>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">借阅申请说明：</span>
									</td>
									<td class="td1" style="word-break:break-all;line-height: 1.4" align="left">
									  <span class="wz">${model.borrowDesc}</span>
										
									
									</td>
								</tr>
								</s:else>
								
								
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="29%">
												
											</td>
											<td width="37%">
											
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

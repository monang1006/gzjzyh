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
			function view(value){
				 var Width=screen.availWidth-10;
              	 var Height=screen.availHeight-30;
              	 var fileId=document.getElementById("fileId").value;
				 var ReturnStr=OpenWindow("<%=root%>/archive/archivefile/achiveFile!readAnnex.action?appendId="+value+"&fileId="+fileId, 
                                   Width, Height, window);
			}
			function viewAnnex(value){	//查看附件
				 var fileId=document.getElementById("fileId").value;
	           var frame=document.getElementById("annexFrame");
	           frame.src="<%=path%>/archive/archivefile/achiveFile!downloadAppend.action?appendId="+value+"&fileId="+fileId;
            }
function cancel(){	 //返回
        var searchType=document.getElementById("searchType").value;
        //alert(searchType);
       if(searchType!=null&&searchType!=''){
	    var fileNo=document.getElementById("fileNo").value;
	    var disLogo=document.getElementById("disLogo").value;
	    var groupType=document.getElementById("groupType").value;
	    var fileTitle=document.getElementById("fileTitle").value;
	    var fileFolder=document.getElementById("fileFolder").value;
	    var tempfileDeadline=document.getElementById("tempfileDeadline").value;
	    var orgId=document.getElementById("orgId").value;
	    var year=document.getElementById("year").value;
	    var month=document.getElementById("month").value;
	    location="<%=path%>/archive/filesearch/fileSearch.action?groupType="+groupType+"&disLogo="+disLogo+"&fileNo="+fileNo+"&fileTitle="+fileTitle+"&tempfileDeadline="+tempfileDeadline+"&year="+year+"&month="+month+"&orgId="+orgId+"&fileFolder="+fileFolder;
	}else{
	   var archiveSortId=document.getElementById("archiveSortId").value;
	    var folderId=document.getElementById("folderId").value;
	   //var archiveSortId=document.getElementById("archiveSortId").value;
	  // alert(archiveSortId);
	   location="<%=path%>/archive/archivefolder/archiveFolder!input.action?forward=viewFile"+"&folderId="+folderId+"&moduletype=pige"+"&archiveSortId="+archiveSortId;
	}
       }
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
					<input type="hidden" id="archiveSortId" name="archiveSortId" value="${archiveSortId}">
						<input type="hidden" id="folderId" name="folderId" value="${folderId}">
						<input type="hidden" id="fileNo" name="fileNo" value="${fileNo }">
							<input id="tempfileDeadline" name="tempfileDeadline" type="hidden" value="${tempfileDeadline}">
							<input type="hidden" id="fileTitle" name="fileTitle" value="${fileTitle }">
							<input type="hidden" id="fileFolder" name="fileFolder" value="${fileFolder }">
							<input type="hidden" id="orgId" name="orgId" value="${orgId }">
							<input type="hidden" id="month" name="month1" value="${month1 }">
							<input type="hidden" id="year" name="year1" value="${year1}">
							<input type="hidden" id="disLogo" name="disLogo1" value="${disLogo1 }">
							<input type="hidden" id="groupType" name="groupType" value="${groupType }">
							<input type="hidden" id="searchType" name="searchType" value="${searchType}">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="30%">
												<img src="<%=path%>/common/images/ico.gif" width="9" height="9">&nbsp;
												查看档案文件
											</td>
											<td width="70%">
											
												<table border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="*">
															&nbsp;
														</td>
														<s:if test="searchType!=null&&searchType!=''">
														<td>
															<a class="Operation" href="javascript:cancel();"><img
																	src="<%=root%>/images/ico/ht.gif"
																	width="15" height="15" class="img_s">返回&nbsp;</a>
															
														</td>
														</s:if>
														<td width="5">
														</td>
														
													</tr>
												</table>
											
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<input type="hidden" id="fileId" name="model.fileId" value="${filemodel.fileId}">
						<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">文号：</span>
								</td>
								<td class="td1"  align="left">
									${filemodel.fileNo}
								</td>

								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">责任者：</span>
								</td>
								<td class="td1"  align="left">
									${filemodel.fileAuthor}
								</td>
							</tr>
							
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">题名：</span>
								</td>
								<td class="td1" align="left">
									${filemodel.fileTitle}
								</td>

								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件日期：</span>
								</td>
								<td class="td1"  align="left">
									<s:date format="yyyy-MM-dd" name="filemodel.fileDate" />
								</td>
							</tr>
							
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">页数：</span>
								</td>
								<td class="td1" align="left">
									${filemodel.filePage}
								</td>

								<td height="21" class="biao_bg1" align="right">
									<span class="wz">所属机构：</span>
								</td>
								<td class="td1"  align="left">
									${fileDepartmentName}
								</td>
							</tr>
							<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">顺序号：</span>
									</td>
									<td class="td1" align="left">
										${filemodel.file_sortorder}
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">件号：</span>
									</td>
									<td class="td1" align="left">
										${filemodel.filePieceNo}
									</td>
							</tr>
							<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">保管期限：</span>
										<br>
									</td>
									<td class="td1" align="left">
										${filemodel.fileDeadline}
										
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">地点：</span>
									</td>
									<td class="td1" align="left">
										${filemodel.filePlace}
									</td>
							</tr>
							<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">人（物）：</span>
									</td>
									<td class="td1" align="left">
										${filemodel.fileFigure}
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">事由：</span>
									</td>
									<td class="td1" align="left">
										${filemodel.fileReasons}
									</td>
								</tr>
								<tr>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">颁奖单位：</span>
									</td>
									<td class="td1" align="left">
										${fileAwardsOrgLevel}
										
									</td>
									<td width="18%" height="21" class="biao_bg1" align="right">
										<span class="wz">获奖内容：</span>
									</td>
									<td class="td1" align="left">
										${filemodel.fileAwardsContent}
									</td>
								</tr>
							
							
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件备注：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${filemodel.fileDesc}
								</td>
							</tr>
							
							
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件附件：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									<s:if test="filemodel.toaArchiveFileAppends!=null&&filemodel.toaArchiveFileAppends.size()>0">
										<s:iterator id="vo" value="filemodel.toaArchiveFileAppends">

											<a  id="fujian" href="#" onclick="view('${vo.appendId}');"  style='cursor: hand;'><font color="blue">${vo.appendName}</font></a>
                                              <a href="#" onclick="viewAnnex('${vo.appendId}');"  style='cursor: hand;'>下载</a>
											<br>
											</s:iterator>
										<!-- 	<a href="javascript:view();"><font color="blue">正文</font></a>
									 --></s:if>
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
											<s:if test="searchType==null||searchType==''">
												<input name="back" type="button" class="input_bg" value="返 回" onclick="history.go(-1)">
											</s:if>
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

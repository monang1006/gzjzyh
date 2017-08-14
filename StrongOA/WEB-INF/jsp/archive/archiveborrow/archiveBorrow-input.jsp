<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加借阅记录</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
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
 			
 			
			function showlength(){
			     var value=$("#borrowDesc").text();
			  
			     if(value.length>=200){
			        event.returnValue = false;
			     }
			}
			function onSub(){
				var begintime=document.getElementById("borrowFromtime").value;
		        var endtime=document.getElementById("borrowEndtime").value;
		        if($("#borrowFromtime").val() == ""){
		        	alert("借阅开始时间不能为空");
		        	 return false;
		        }
		        if($("#borrowEndtime").val() == ""){
		        	alert("借阅归还时间不能为空");
		        	 return false;
		        }
		        var compareborrowFromtime=new Date($("#borrowFromtime").val().replace(/-/g,"/"));
		        var compareborrowEndtime=new Date($("#borrowEndtime").val().replace(/-/g,"/"));
		        if(compareborrowEndtime - compareborrowFromtime < 0){
		        	alert("借阅归还时间不能早于借阅开始时间");
		        	 return false;
		        }
		        document.getElementById("archiveBorrowForm").submit();
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
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
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
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onSub();">&nbsp;申&nbsp;请&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="history.go(-1)">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
						
					<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">所属案卷名称：</span>
									</td>
									<td class="td1"  align="left">
										<span class="wz">${model.toaArchiveFile.toaArchiveFolder.folderName}</span>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">文件编号：</span>
									</td>
									<td class="td1"  align="left">
										<span class="wz">${model.toaArchiveFile.fileNo}</span>
									</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">文件名称：</span>
									</td>
									<td class="td1" align="left">
										<span class="wz">${model.toaArchiveFile.fileTitle}</span>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;借阅人：</span>
										<input id="borrowPersonid" name="model.borrowPersonid" type="hidden" size="22" value="${model.borrowPersonid}">
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="borrowPersonname" name="model.borrowPersonname" type="text" size="22" value="${model.borrowPersonname}" class="required">
										<input name="userscelet" type="button" class="input_bg" value="选 择" onclick="userselect()" disabled="disabled">
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;借阅开始时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate mindate="${model.borrowFromtime}" id="borrowFromtime" classtyle="required" name="model.borrowFromtime" dateform="yyyy-MM-dd" width="133" dateobj="${model.borrowFromtime}"/>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;借阅归还时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="borrowEndtime" classtyle="required" name="model.borrowEndtime" dateform="yyyy-MM-dd" width="133" dateobj="${model.borrowEndtime}" />
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">申请时间：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="borrowTime" name="model.borrowTime" dateform="yyyy-MM-dd" width="133" dateobj="${model.borrowTime}"/>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">借阅申请说明：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="10" cols="50" id="borrowDesc" maxlength="200"   name="model.borrowDesc">${model.borrowDesc}</textarea>
										
									</td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						
					</td>
				</tr>
			</table>
						</s:form>
		</DIV>
	</body>
</html>

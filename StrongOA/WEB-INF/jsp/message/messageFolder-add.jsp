<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%> 
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
		<title>管理消息文件夹</title>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows_add.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		 function save(){
		 	var msgFolderName = document.getElementById("msgFolderName");
		 	var folderName = $.trim(msgFolderName.value);
		 	msgFolderName.value = folderName;
		 	if(folderName==null|folderName==''){
		 		alert('请输入文件夹名称。');
		 		return;
		 	}
		 	var url = "<%=path%>/message/messageFolder!save.action";
		 	$.ajax({
						type:"post",
						url:url,
						data:{
							'model.msgFolderName':encodeURI(folderName),
							'model.msgFolderId':$("#msgFolderId ").val()
						},
						success:function(data){
		                    	if(data=="succ"){
								 	returnValue ="reload";
									window.close();
		                    	}else{
		                    		alert("文件夹名称重复，不能添加。");
		                    	}
	                    	},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				   });
		 }
		</script>
	</head>
	<base target="_self"/>
	<body>
	<DIV id=contentborder align=center>
	<s:form action="" name="form">
	<input type="hidden" id="msgFolderId" name="model.msgFolderId" value="${msgFolderId}">
	<table width="100%" border="0" cellspacing="0" cellpadding="00">
		<tr>
			<td colspan="3" class="table_headtd">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td class="table_headtd_img" >
							<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
						</td>
						<td align="left">
							<script>
							 var id = document.getElementById("msgFolderId").value;
								 if(id==null|id==""|id==" "){
								 	document.write("新建消息文件夹");
								 }else{
								 	document.write("重命名消息文件夹");
								 }
							</script>
						</td>
						<td align="right">
							<table border="0" align="right" cellpadding="00"
								cellspacing="0">
								<tr>
									<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
				                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
			                  		<td width="5"></td>
				                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
				                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
			                  		<td width="6"></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
		
	<table border="0" cellpadding="0" cellspacing="0" width="100%" height="60%">
		<tr>
		<td valign="top" align="left" width="70%">
			<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
				<tr>
					<td class="biao_bg1" width="25%" align="right">
						<span class="wz">文件夹名称：</span>
					</td>
					<td class="td1" width="75%">
						&nbsp;<input id="msgFolderName" name="model.msgFolderName" type="text" value="${model.msgFolderName}"  onkeydown="if(event.keyCode==13){save(); return false;}"
							 size="45" maxlength="12">
					</td>
				</tr>
				<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
			</table>
		</td>
		</tr>
	</table>
	</s:form>
	</DIV>
	</body>
</html>

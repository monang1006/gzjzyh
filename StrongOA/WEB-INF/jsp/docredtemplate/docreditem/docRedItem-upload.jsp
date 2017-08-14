<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>    
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>上传套红</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var parentWin = window.dialogArguments;
				if(parentWin){
					var groupId = parentWin.document.getElementById("redtempGroupId");
					if(groupId){
						document.getElementById("redtempGroupId").value = groupId.value;
					}
				}
				var message = $(".actionMessage").text();
				if(message){
					if(message == "ok"){
						alert("上传成功！")
						window.returnValue="ok";
						window.close();
					}else if(message == "error"){
						alert("对不起，上传失败，请检查文件类型是否正确！")
						window.returnValue="error";
						window.close();
					}
				}
			});
			
			
		
			function doSubmit(){
				var isSelectFile = false;
				$("input:file.multi").each(function(){
				   if($(this).val()!=""){
				     isSelectFile = true;
				   }
				});
				if(!isSelectFile){
				    alert("对不起，请选择附件！");
					return ;
				}
				$("form").submit();
			}
		</script>
	</head>
	<base target="_self"/>
	<body>
		<s:form action="/docredtemplate/docreditem/docRedItem!upload.action" enctype="multipart/form-data">
			<input type="hidden" name="model.toaDocredGroup.redtempGroupId" id="redtempGroupId" />
			<label id="l_actionMessage" style="display: none;">
				<s:actionmessage />
			</label>
			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td colspan="4" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;上传套红文件</td>
				</tr>
			<tr>
				<td width="15%" height="21" class="biao_bg1" align="right"><span class="wz">附件：</span></td>
				<td  class="td1">
					<s:file id="upload" onkeydown="return false;" accept="doc" name="upload" cssClass="multi"></s:file>
				</td>
				<td  class="td1" colspan="2">
					<font color='red'>
						说明：可以批量上传文件,只能上传WORD类型。<BR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</font>
				</td>
			</tr>
			<tr>
				<td colspan="4"  class="td1" align="center">
        			<input onclick="doSubmit();" type="button" class="input_bg" value="确 定" />&nbsp;&nbsp;&nbsp;&nbsp;
        			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        			<input id="btnCancel" type="button" class="input_bg" onclick="window.close();" value="取 消" />
				</td>
			</tr>
		</s:form>
	</body>
</html>
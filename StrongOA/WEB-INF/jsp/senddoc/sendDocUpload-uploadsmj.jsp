<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%
			String isPermitUploadSMJ = (String) request
					.getAttribute("isPermitUploadSMJ");

			String smjPath = (String) request.getParameter("smjPath");
		%>
		<title>扫描件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				//alert("smjPath: "+"<%=smjPath%>");
				//alert(window.parent.formReader.GetFormControl("Edit_AdobeSMJName").Value);
				var isPermitUploadSMJ = 0;
				if(window.parent.document.getElementById('PDFFunction') != null&&window.parent.document.getElementById('PDFFunction') != ''){
					if(window.parent.document.getElementById('PDFFunction').isPermitUploadSMJ != null&&window.parent.document.getElementById('PDFFunction').isPermitUploadSMJ != ''){
						isPermitUploadSMJ=window.parent.document.getElementById('PDFFunction').isPermitUploadSMJ;
					}
				}
				//alert("isPermitUploadSMJ: "+isPermitUploadSMJ);
				 if(isPermitUploadSMJ=='0'){
					$("#uploadSMJtable").hide();
				}
			});
			
			function doSubmit(){
				var filter = "jpg,gif,png";
				var filters = filter.split(",");
				var file = document.getElementById("upload").value;
				if(file != ""){
					var index = file.lastIndexOf(".");
					var ext = file.substring(index+1, file.length);
					ext = ext.toLowerCase();
					var istrue = false;
					for(var ii=0;ii<filters.length;ii++){
						if(ext == filters[ii]){
							istrue = true;
						}
					}
					if(!istrue){
						alert("文件类型不正确，请重新选择。");
						return false;
					}
				}else{
					alert("请选择需要上传的文件。");
					return false;
				}
				var index = file.lastIndexOf("\\");
				var ext = file.substring(index+1, file.length);
				document.getElementById("attachName").value = ext;
				$("body").mask("操作处理中,请稍候...");
				document.getElementById("smjform").submit();
				//return true;
			}
			
			function callback(msg,path){
				 $("body").unmask();
				 window.parent.formReader.GetFormControl("Edit_SMJName").SetProperty("Value",path);
				 if(msg == "true"){
				 	//success
					//alert("附件上传-pdfValue:"+pdfValue);
				 	alert("上传成功。");
				 	//document.getElementById("acro").src = path;
				 	//alert("isPermitUploadSMJ: "+isPermitUploadSMJ);
					var isPermitUploadSMJ = 0;
					if(window.parent.document.getElementById('PDFFunction') != null&&window.parent.document.getElementById('PDFFunction') != ''){						
						if(window.parent.document.getElementById('PDFFunction').isPermitUploadSMJ != null&&window.parent.document.getElementById('PDFFunction').isPermitUploadSMJ != ''){
							isPermitUploadSMJ=window.parent.document.getElementById('PDFFunction').isPermitUploadSMJ;
						}
					}
				 	location = "<%=path%>/senddoc/sendDocUpload!showSMJ.action?smjPath="+path+"&isPermitUploadSMJ="+ isPermitUploadSMJ;
				 } else if(msg == "false"){
				 	//error
				 	alert("对不起，系统出现错误，请与管理员联系。");
				 }
			}
		</script>
	</head>
	<body scroll="no">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id=contentborder align=center style="WIDTH: 100%; HEIGHT: 100%";>
		
			<%
					if ((isPermitUploadSMJ).equals("1")) {
			%>
			<fieldset id="uploadSMJ" style="width: 100%">
				<table id="uploadSMJtable" style="width: 100%; height: 100%">
					<tr>
						<td>
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;
							<span class="wz">扫描件</span>
						</td>
					</tr>
					<tr>
						<td>
							<s:form action="/senddoc/sendDocUpload!saveUploadSMJ.action" id="smjform" method="post"
								enctype="multipart/form-data" target="myIframe">
								<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="1">
									<tr>
										<td class=td1>
											<input type="hidden" id="attachName" onkeydown="return false;" name="attachName"
												value="${attachName}" readonly="readonly" size="60">
											<%--<input type="button" value="上传" onclick="uploadimg()">--%>
											&nbsp;
											<input type="file" onkeydown="return false;" id="upload" name="upload"
												style="width: 50%;" />
											<input id="save" name="save" onclick="doSubmit();" type="button" value="上传"
												style="height: 22px; width: 80px;" />
											(
											<span style="color: red">只能上传.jpg、.gif和.png文件</span>)
											<%--<input id="upload" name="upload"  type="file"  size="60"/>--%>
										</td>
									</tr>
								</table>
							</s:form>
						</td>
					</tr>
				</table>
			</fieldset>
			<%
					}
			%>
			<%
				if (smjPath != null && !"".equals(smjPath)) {
			%>
			<%
					if ((isPermitUploadSMJ).equals("0")) {
			%>
			<div id="acrosmj" style="WIDTH: 100%; HEIGHT: 100%";>
			<%
					} else {
			%>
			<div id="acrosmj" style="WIDTH: 100%; HEIGHT: 90%";>
			<%
					}
			%>
				<iframe name="surenIframe" style="display: block" src="${smjPath }" WIDTH="100%" HEIGHT="100%"></iframe>
			</div>
			<%
				}
			%>
			</div>
			<iframe name="myIframe" style="display: none"></iframe>
	</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%
			String isPermitUploadPDF = (String) request
					.getAttribute("isPermitUploadPDF");

			String pdfPath = (String) request.getParameter("pdfPath");
		%>
		<title>办结文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
		var pdfInfo="";
			$(document).ready(function(){
				//alert("pdfPath: "+"<%=pdfPath%>");
				//alert(window.parent.formReader.GetFormControl("Edit_AdobePDFName").Value);
				var isPermitUploadPDF = 0;
				if(window.parent.document.getElementById('PDFFunction') != null&&window.parent.document.getElementById('PDFFunction') != ''){
					if(window.parent.document.getElementById('PDFFunction').isPermitUploadPDF != null&&window.parent.document.getElementById('PDFFunction').isPermitUploadPDF != ''){
						isPermitUploadPDF=window.parent.document.getElementById('PDFFunction').isPermitUploadPDF;
					}
				}
				pdfInfo = window.parent.formReader.GetFormControl("Edit_AdobePDFName").Value;
				window.parent.setPdfContentInfo(pdfInfo);
				//alert("isPermitUploadPDF: "+isPermitUploadPDF);
				 if(isPermitUploadPDF=='0'){
					$("#uploadPDFtable").hide();
				}
			});

			
			function doSubmit(){
				var filter = "pdf,gw,gd,sep";
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
				document.getElementById("pdfform").submit();
				//return true;
			}
			
			function callback(msg,path){
				$("body").unmask();
				pdfInfo = path+";";
				window.parent.setPdfContentInfo(pdfInfo);
				 window.parent.formReader.GetFormControl("Edit_AdobePDFName").SetProperty("Value",path);
				 if(msg == "true"){
				 	//success
					//alert("附件上传-pdfValue:"+pdfValue);
				 	alert("正文上传成功。");
				 	//document.getElementById("acro").src = path;
				 	//alert("isPermitUploadPDF: "+isPermitUploadPDF);
					var isPermitUploadPDF = 0;
					if(window.parent.document.getElementById('PDFFunction') != null&&window.parent.document.getElementById('PDFFunction') != ''){
						if(window.parent.document.getElementById('PDFFunction').isPermitUploadPDF != null&&window.parent.document.getElementById('PDFFunction').isPermitUploadPDF != ''){
							isPermitUploadPDF=window.parent.document.getElementById('PDFFunction').isPermitUploadPDF;
						}
					}
				 	location = "<%=path%>/senddoc/sendDocUpload!showPDF.action?pdfPath="+path+"&isPermitUploadPDF="+ isPermitUploadPDF;
				 } else if(msg == "false"){
				 	//error
				 	alert("对不起，系统出现错误，请与管理员联系。");
				 }
			}
		 function getFormIframeValue() {}
		</script>
	</head>
	<body scroll="no">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id=contentborder align=center style="WIDTH: 100%; HEIGHT: 100%";>
		
			<%
					if ((isPermitUploadPDF).equals("1")) {
			%>
			<fieldset id="uploadPDF" style="width: 100%">
				<table id="uploadPDFtable" style="width: 100%; height: 100%">
					<tr>
						<td>
							<img src="<%=root%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;
							<span class="wz">收文正文</span>
						</td>
					</tr>
					<tr>
						<td>
							<s:form action="/senddoc/sendDocUpload!saveUploadPDF.action" id="pdfform" method="post"
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
											<span style="color: red">只能上传.pdf、.sep、.gd和.gw文件</span>)
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
				if (pdfPath != null && !"".equals(pdfPath)) {
			%>
			<%
					if ((isPermitUploadPDF).equals("0")) {
			%>
			<div id="acropdf" style="WIDTH: 100%; HEIGHT: 100%";>
			<%
					} else {
			%>
			<div id="acropdf" style="WIDTH: 100%; HEIGHT: 100%";>
			<%
					}
			%>
				<iframe name="surenIframe" style="display: block" src="${pdfPath }" WIDTH="100%" HEIGHT="100%"></iframe>
			</div>
			<%
				}
			%>
			</div>
			<iframe name="myIframe" style="display: none"></iframe>
	</body>
</html>

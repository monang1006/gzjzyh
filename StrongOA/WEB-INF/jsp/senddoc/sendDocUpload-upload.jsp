<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
  	<head>
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
			//去掉左空格
			function ltrim(s){ 
					return s.replace(/(^\s*)/g, "");
 			} 
 			
 			//去右空格; 
			function rtrim(s){ 
  				return s.replace(/(\s*$)/g, "");
 			} 
			
			//去掉字符串的左右空格
			function trim(s){ 
 				return rtrim(ltrim(s)); 
 			} 
		
			function doSubmit(){
				var filter = "png,jpg";
				var filters = filter.split(",");
				
				/*
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
						alert("图片类型不正确，请重新选择。");
						return false;
					}
				}else{
					alert("请上传领导批示单");
					return false;
				}
				
				 var index = file.lastIndexOf("\\");
				 var ext = file.substring(index+1, file.length);
				 document.getElementById("attachName").value = ext;
				*/
				var uploadsFileNames = document.getElementsByName("uploads");
				
				if(uploadsFileNames.length<2){
					alert("请上传领导批示单");
					return false;
				}
				
				
				 var doneSuggestion=trim(document.getElementById("doneSuggestion").value);	//办结意见
				 if(doneSuggestion.length>600){
				 	alert("办结意见字数不能多于600字");
				 	return false;
				 }
				 if(doneSuggestion == ""){
					alert("请填写办结意见");
					return false;
				 }
				$("body").mask("操作处理中,请稍候...");
				return true;
			}
			
			
			function callback(msg){
			 $("body").unmask();	
			 if(msg == "true"){
			 	var doneSuggestion=trim(document.getElementById("doneSuggestion").value);	//办结意见
			 	window.returnValue = doneSuggestion;
			 	
			 	window.close();
			 } else if(msg == "false"){
			 	alert("对不起，系统出现错误，请与管理员联系。");
			 }else  if(msg == "nullFile"){
			 	alert("上传的文件不能为空，请检查您选择的文件中是否有空文件！");
			 }
			}
		</script>
  	</head>
  	<body class=contentbodymargin>
  		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
  		<div id=contentborder align=center>
  		<fieldset style="width: 94%;">
	  					<legend>
							
						</legend>
  		<table style="width: 100%;height: 100%">
  		<tr>
  		<td><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">
										<span class="wz">办结文件</span></td>
  		</tr>
  		<tr>
  		<td>
  		<s:form action="/senddoc/sendDocUpload!saveUploads.action" method="post" enctype="multipart/form-data" target="myIframe">
  			<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
				<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
						<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<tr>
							<td height="21" class="biao_bg1" align="right" width="18%">
								<span class="wz">上传领导批示单:&nbsp;&nbsp;</span>
							</td>
							<td class=td1>
								<input type="hidden" id="attachName" onkeydown="return false;"
									name="attachName" value="${attachName}" readonly="readonly"
									size="60">
								<br />
								<%--<input type="button" value="上传" onclick="uploadimg()">--%>
								<%--<input id="upload" name="upload"  type="file"  size="60"/>
								
								<input type="file" onkeydown="return false;" id="upload"
									name="upload" style="width: 100%;" />
									(<span style="color:red">只能上传.png和.jpg类型的图片文件</span>)
								--%>
									
								<div style="margin-left: 15px;">									
									<input type="file" style="width: 76%;" onkeydown="return false;" class="multi" name="uploads" accept="png|jpg"/>
									(<span style="color:red">只能上传.png和.jpg类型的图片文件</span>)
								</div>
							</td>
							<td class=td1>
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz">办结意见:&nbsp;&nbsp;</span>
							</td>
							<td class=td1>
								<textarea id="doneSuggestion" name="doneSuggestion" rows="18"
									cols="60" style="width: 100%">${doneSuggestion}</textarea>
							</td>
							<td class=td1>
							</td>
						</tr>
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="34%" height="34">
												&nbsp;
											</td>
											
											<td width="29%">
												&nbsp;
											</td>
										
											<td width="29%">
												<input id="save" name="save" onclick=" return doSubmit();" type="submit" class="input_bg" value="确定">
											</td>
											<td>
												&nbsp;&nbsp;
											</td>
											<td width="37%">
												<input name="button" type="button" onclick="window.close();" class="input_bg" value="关闭">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
  			</s:form>
  		</td>
  		</tr>
  		</table>
  			
  			</fieldset>
  		</div>
  		<iframe name="myIframe" style="display:none"></iframe>
  	</body>
</html>

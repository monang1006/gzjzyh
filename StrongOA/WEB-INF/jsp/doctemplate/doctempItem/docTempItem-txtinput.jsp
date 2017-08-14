<html>
	<head>
		<title>保存模板</title>
	<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<%@ taglib uri="/struts-tags" prefix="s"%>
	<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
	<%@include file="/common/include/rootPath.jsp"%> 
	<%@include file="/common/include/meta.jsp" %>
	<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		String remindData = request.getParameter("remindData");
	%>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
			.wz{
				float:right;}
			.biao_bg9 {
	HEIGHT: 30px; COLOR: #454953; LINE-HEIGHT: 30px; PADDING-RIGHT: 18px;  BACKGROUND-COLOR: #eff0f3
}	
		</style>
		<script type="text/javascript">
		
		
		
		function getChildData(value){
			var obj = document.getElementsByName("upload")[0];
			var text = obj.innerText;
				alert(text);
			if(text.indexOf("图片大小超出最大范围。")!=-1){
				alert("图片大小超出最大范围。");
			}
		}
		
		//提交表单
		function saveDoc(){
			//标题验证
			 var doctemplateId=$("#doctemplateId").val();
            var personImg=$("#personImg").attr("src");
        	if($("#doctemplateTitle").val() == "") {
          		alert("请填写模板名称。");
          		$("#doctemplateTitle").focus();
        	}
<%--        	else if(doctemplateId==""&&personImg==""){
        		alert("请选择图标！");
        	}--%>
        	else if($("#doctemplateRemark").val()==""){
        		alert("请选择简介。");
        	}else{
				//获取编辑内容
				getCalContent(form);
				
				$("form").submit();				
        		
        	}

		}
		
	//关闭窗口
		function closeDoc(){
			window.returnValue = "OK";
	        window.close();
		}
		
	function showImage(value){
			var suburlStr=value.substring(value.lastIndexOf(".")+1);//得到文件类型
			var flag=true;
			switch(suburlStr){
				case "jpg" : flag=true; break;
				case "JPG" : flag=true; break;
				case "jpeg": flag=true; break;
				case "JPEG": flag=true; break;
				case "bmp": flag=true; break;
				case "BMP": flag=true; break;
				case "gif": flag=true; break;
				case "GIF": flag=true; break;
				case "png": flag=true; break;
				case "PNG": flag=true; break;
				case "psd": flag=true; break;
				case "PSD": flag=true; break;
				case "dxf": flag=true; break;
				case "DXF": flag=true; break;
				case "cdr": flag=true; break;
				case "CDR": flag=true; break;
				default: flag=false;break;
			}
			if(flag){
				//document.getElementById("myDiv").style.visibility="visible";
				//document.getElementById("personImg").src=value;
			}else {
				alert("上传的文件中包含非图片文件，请选择图片文件。");
			}
		}
		
	$(document).ready(function() {
<%--		window.setInterval("getChildData()",5000);--%>
		init()//初始化页面
		var message = $(".actionMessage").text();
		if(message!=null && message!=""){
			if(message.indexOf("error")>-1){
				message = message.replace("error","");				
				alert(message);
				}else if(message.indexOf("success")>-1){
				eval("var doc = "+message);
				if (doc.success == "yes") {
	            	$("#doctemplateTitle").val(doc.title);
	            	$("#doctemplateId").val(doc.id);
	            	var r=confirm("保存文档成功，是否继续编辑？");
	            	if (r == false) {
	               	 	window.returnValue = "OK";
	                	window.close();
	            	}
	        	}else{
	        		if(doc.fail!=null&&doc.fail!=""){
	        			alert(doc.fail);
	        		}else{
	        			alert("保存文档失败。");
	        		}
	        	}
			}else{
				alert("保存文档失败。");
			}
		}
	});
	
	//初始化页面
	function init(){
		 var id = $("#doctemplateId").val()
			  var path=document.getElementById("path").value;
			  if ((id != "") && (id != null)) {
			     if(path!=null&&path!=""){
			     	//document.getElementById("myDiv").style.visibility="visible";
			     	//document.getElementById("personImg").src=path;
			     }
			  }  
	}
		
		//获取说明内容
         function getCalContent(form) {
             var oEditor = FCKeditorAPI.GetInstance('content');
             var acontent = oEditor.GetXHTML();
			 form.doctemplateTxtContent.value = acontent;
         }
         
         
        
        
       
 		//删除附件
		 function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 //下载附件
		 function download(id){
		  var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/calendar/calendar!down.action?attachId="+id;
		 }
		 
		 
		   
	</script>
	</head>
	<base target="_self"/>
	<body >
	<DIV id=contentborder align=center>
	<a id="reload" style="display: none" href=""></a>
	<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
	<s:form action="/doctemplate/doctempItem/docTempItem!save.action" name="form" method="post" enctype="multipart/form-data" >
	
	<s:hidden id="doctemplateId" name="model.doctemplateId"></s:hidden>
	<s:hidden id="logo" name="model.logo"></s:hidden>
	<s:hidden id="path" name="path"></s:hidden>
	<input type="hidden" name="model.toaDoctemplateGroup.docgroupId" id="docgroupId" value="${docgroupId}"/>
			
	<input type="hidden" id="doctemplateTxtContent" name="model.doctemplateTxtContent" value="${model.doctemplateTxtContent}">
	<div id="desc" style="display: none">${model.doctemplateTxtContent}</div>
	<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
							
								<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>保存模板</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="saveDoc();">&nbsp;保存至服务器&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="closeDoc();">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
							</tr>
						</table>
		
		<table border="0" cellpadding="0" cellspacing="1" width="100%" height="100%">
		<tr>
		<td valign="top" align="left" width="70%">
			<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
				<tr>
									<td width="15%" nowrap class="biao_bg9" align="right">
										<span class="wz">所属模板类别：</span>
									</td>
									<td width="35%" class="td1" align="left">${doctempTypeName}</td>	
									<td width="15%" nowrap class="biao_bg9" align="right">
										<span class="wz"><span style="color: red;">*</span> 模板名称：</span>
									</td>
									<td  width="35%" nowrap class="td1">
										<s:textfield id="doctemplateTitle" name="model.doctemplateTitle"
											maxlength="25" cssStyle="width:50%;" ></s:textfield>
										<input style="display:none ;" />	
										&nbsp;
									</td>				
								</tr>
								<tr>
									<td width="15%" nowrap class="biao_bg9" align="right" valign="top">
										<span class="wz"><span style="color: red;">*</span> 模板简介：</span>
									</td>
									<td width="35%" nowrap class="td1">
										<s:textarea id="doctemplateRemark" name="model.doctemplateRemark"
											cols="45" rows="5"></s:textarea>
										&nbsp;
									</td>

									<!--<td width="15%" nowrap class="biao_bg9" align="right">

									<td width="15%" nowrap class="biao_bg9" align="right" valign="top">

										<span class="wz">图标：</span>
									</td>
									<td width="35%" class="td1">
										<div id="myDiv" style="visibility: hidden">
											<img src='' id='personImg' style='width: 100px; height:70px;align:top;'>
										</div>
										<input type="file" id="file" name="file" onChange="showImage(this.value)"> 图片大小不超过1M
									</td>-->
								</tr>
								<tr>
									<td width="15%" nowrap class="biao_bg9" align="right" valign="top">
												<span class="wz">说明：</span>
											</td>
												
							
											<td colspan="3" valign="top" height="100%">
												<script type="text/javascript" src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
												<script type="text/javascript">
													var oFCKeditor = new FCKeditor( 'content' );
													oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
													oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
													oFCKeditor.Width = '100%' ;
						                            oFCKeditor.Height = '400' ;
													oFCKeditor.Value = document.getElementById("desc").innerText;
													oFCKeditor.Create() ;
												</script>
							
									</td>
								</tr>

			</table>
		</td>
		</tr>
		</table>
	</s:form>
<%--	<iframe name="upload" src="<%=root%>/fileNameRedirectAction.action?toPage=doctemplate/doctempItem/docTempItem-upload.jsp" sytle="display:block ;"></iframe>--%>
	</DIV>
	</body>
</html>
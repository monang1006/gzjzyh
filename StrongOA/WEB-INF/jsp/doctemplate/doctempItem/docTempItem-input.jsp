<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存模板</title>
		<LINK href="<%=frameroot%>/css/properties_windows_special.css"
			type=text/css rel=stylesheet>
		<%--<link rel="stylesheet" type="text/css" href="debug.css" media="all" />
		--%><script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<%--<script src="<%=path%>/oa/js/doctemplate/uploadPreview.js"
			type="text/javascript"></script>
		--%><script src="<%=path%>/common/js/jquery/jquery.imgareaselect-0.6.2.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/OfficeControl/officecontrol.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style media="screen" type="text/css">
		    .tabletitle {
		      FILTER:progid:DXImageTransform.Microsoft.Gradient(
		                            gradientType = 0, 
		                            startColorStr = #ededed, 
		                            endColorStr = #ffffff);
		    }
		    
		    .hand {
		      cursor:pointer;
		    }
		    .textare {font-size:14px;}
			.biao_bg1{
				BACKGROUND: #e0e0e0;
				}
			
				.biao_bg1 h1{
									
									
									font-size:16px;
									color: #281d2d;
									font-weight:bolder;
									line-height:30px;

									}
								.td1 a{
									display:block;
									height:25px;
									line-height:25px;				
									padding-right:30px;
									font-weight:100;
									font-size:14px;
									BORDER-BOTTOM: #cccccc 0px solid;
									COLOR: #281d2d;
									background:#f1f1f1;
									}	
								.td1 a:hover{
									background-color: #3a73ad;}	
							    .cc input,.cc select,.cc textarea{border:1px solid #b3bcc3;background-color:#ffffff;}
								
                                
                                
                             
	    </style>
		<script type="text/javascript">
   
      function saveDoc() {
            var doctemplateId=$("#doctemplateId").val();
            var personImg=$("#personImg").attr("src");
            var docNumber = $("#docNumber").val();
            if(docNumber != ""){
            	if(isNaN(docNumber)){
            		alert("请输入正确的排序号。");
            		return ;
            	}
            }
        	if($("#doctemplateTitle").val() == "") {
          		alert("请填写模板名称！");
          		$("#doctemplateTitle").focus();
        	}
<%--        	else if(doctemplateId==""&&personImg==""){--%>
<%--        		alert("请选择图标！");
        	}--%>
        	else if($("#doctemplateRemark").val()==""){
        		alert("请填写简介。");
        	}else{
	        	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	        	var ret = TANGER_OCX_OBJ.SaveToUrl(doctempForm.action,
	                                           "wordDoc",
	                                           "",
	                                           $("#doctemplateTitle").val(),
	                                           "doctempForm");
	          
	        	eval("var doc = "+ret);
	        	if (doc.success == "yes") {
	            	$("#doctemplateTitle").val(doc.title);
	            	$("#doctemplateId").val(doc.id);
	            	var r=confirm("保存文档成功，是否继续编辑？");
	            	if (r == false) {
	               	 	window.returnValue = "Save";
	                	window.close();
	            	}
	        	}else{
	        		if(doc.fail!=null&&doc.fail!=""){
	        			alert(doc.fail);
	        		}else{
	        			alert("保存文档失败。");
	        		}
	        	}
        	}
      	}
      
      //关闭
      function closeDoc(){
      		window.returnValue = "Close";
	        window.close();
      }
      
      //打开文档
      function openFromURL(doctemplateId) {
          var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          TANGER_OCX_OBJ.OpenFromURL("<%=root%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+doctemplateId);
      }            	
      
      //插入模板
      function TANGER_OCX_AddTemplateFromURL(){
		  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
		  //待实现url
		  var ReturnStr=OpenWindow("<%=root%>/doctemplate/doctempType/docTempType!officetree.action", 
                                   "300", "300", window);
          if(ReturnStr!="" && ReturnStr!=null ){
          	TANGER_OCX_OBJ.AddTemplateFromURL("<%=root%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr,true); 
          }
      }
      
      //插入套红
      function TANGER_OCX_AddDocRedFromURL(){
      	  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
      	  //待实现url
		  var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docredtype/docRedType!officetree.action", 
                                   "300", "300", window);
          if(ReturnStr!="" && ReturnStr!=null ){
          	TANGER_OCX_OBJ.AddTemplateFromURL("<%=root%>/docredtemplate/docreditem/docRedItem!opendoc.action?redstempId="+ReturnStr,true);
          }                         
      }
      
      //从服务器插入印章
      function TANGER_OCX_AddSignFromURL(){
      	  var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
      	  //待实现url
      	  var ReturnStr=OpenWindow("<%=root%>", 
                                   "200", "300", window);
      }    
      
      $(document).ready(function(){			  
			  //initWordOCX();
			  var id = $("#doctemplateId").val()
			  var path=document.getElementById("path").value;
			  if ((id != "") && (id != null)) {
			     openFromURL($("#doctemplateId").val());
			     if(path!=null&&path!=""){
			     	//document.getElementById("myDiv").style.visibility="visible";
			     	//document.getElementById("personImg").src=path;
			     }
			  } else {
					var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			  		try{
						TANGER_OCX_OBJ.CreateNew("Word.Document");   
					}catch(e){
					}

			  		var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
			  		var fullContextPath = $("form").attr("action");
			  		var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
				  	TANGER_OCX_OBJ.OpenFromURL(httpPath + contextPath + "!openEmptyDocFromUrl.action?docType="+type);
				  	TANGER_OCX_OBJ.WebFileName='新建文档';
			  } 
		}); 
		
		function doSelect(){		
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			var docType = document.getElementById("docType").value;
			var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
			if(docType == type){
				return;
			}else{
				var fullContextPath = $("form").attr("action");
		  		var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
			  	TANGER_OCX_OBJ.OpenFromURL(httpPath + contextPath + "!openEmptyDocFromUrl.action?docType="+docType);
			  	TANGER_OCX_OBJ.WebFileName='新建文档';
			}
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
				alert("上传的文件中包含非图片文件！请选择图片文件。");
			}
		}
		
		//定义书签
		function TANGER_OCX_SetBookMark(){
			var ret = OpenWindow("<%=root%>/bookmark/bookMark!initAddBookMarkToWord.action","250","200",window);
		}
		
    </script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" style="background-color:#ffffff">
		<s:form id="doctempForm" name="doctempForm"
			action="/doctemplate/doctempItem/docTempItem!save.action" enctype="multipart/form-data"
			method="post">
			<s:hidden id="doctemplateId" name="model.doctemplateId"></s:hidden>
			<s:hidden id="logo" name="model.logo"></s:hidden>
			<s:hidden id="path" name="path"></s:hidden>
			<input type="hidden" name="model.toaDoctemplateGroup.docgroupId" id="docgroupId" value="${docgroupId}"/>
			<s:file id="wordDoc" name="wordDoc" cssStyle="display:none;"></s:file>
			<div id="contentborder" align="center">
				<table width="100%" height="100%" border="0" cellspacing="0"
					cellpadding="0" style="vertical-align: top;">
					<tr>
						<td colspan="3" class="tabletitle">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
	<td height="8px;"></td>
</tr>
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
					                 	<td class="Operation_input" onClick="TANGER_OCX_ShowDialog(2);">&nbsp;保存至本地&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="saveDoc();">&nbsp;保存至服务器&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="closeDoc();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
								</tr>
								<tr>
	<td height="8px;"></td>
</tr>
					</table>
				</td>
				</tr>
					<tr>
						<td valign="top" width="160px" height="100%">
							<table width="100%" align="center" border="0" cellpadding="0"
								cellspacing="1" class="table1">
								
								
								<tr>
									<td nowrap align="center" class="biao_bg1">
										<h1>文件操作<h1>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_ShowDialog(1)" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">打开本地文件</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_ShowDialog(5);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">设置页面布局</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_ShowDialog(6);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">设置文件属性</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_PrintDoc(true);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">打印控制</a>
									</td>
								</tr>
								
								<tr>
									<td nowrap align="center" class="biao_bg1">
										<h1>文件编辑</h1>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_SetBookMark();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">定义标签</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_SetMarkModify(true);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">保留痕迹</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_SetMarkModify(false);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">不留痕迹</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_ShowRevisions(true);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">显示痕迹</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_ShowRevisions(false);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">隐藏痕迹</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_AddPicFromLocal(false);" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">插入图片</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_AddTemplateFromURL();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">插入模板</a>
									</td>
								</tr>
								<!-- <tr onClick="TANGER_OCX_AddDocRedFromURL();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">插入套红</a>
									</td>
								</tr> -->
								<tr>
									<td nowrap align="center" class="biao_bg1">
										<h1>电子认证</h1>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_DoHandSign2();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">全屏手写签名</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_DoHandDraw2();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">全屏手工绘图</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_DoCheckSign();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">签名验证信息</a>
									</td>
								</tr>
								<tr onClick="TANGER_OCX_AddSignFromLocal();" style="cursor: pointer; line-height: 20px;">
									<td align="right" nowrap class="td1">
										<a href="#">加盖电子印章</a>
									</td>
								</tr>
							</table>
						</td>
                       
						<td width="*" style="padding:0 10px;">
							<table width="100%" height="100%" align="center" border="0"
								cellpadding="0" cellspacing="1" class="cc">
								<tr height="30">
									<th style="padding-right:10px;" width="15%" nowrap class="biao_bg1" align="right">
										<span class="wz"><span style="color: red;">*</span>&nbsp;模板名称：</span>
									</th>
									<td  width="35%" nowrap class="td1">
										<s:textfield id="doctemplateTitle" style="background-color:#ffffff" name="model.doctemplateTitle"
											maxlength="25" cssStyle="width:50%;" cssClass="textare"></s:textfield>
											<input style="display:none ;" />
									</td>		
									<th style="padding-right:10px;" width="10%" nowrap class="biao_bg1" align="right">
										<span class="wz">所属模板：</span>
									</th>
									<td width="35%" class="td1" align="left">${doctempTypeName}</td>	
	
								</tr>
								<tr height="30">
									<th style="padding-right:10px;" width="15%" nowrap class="biao_bg1" align="right">
										<span class="wz">排序号：</span>
									</th>
									<td  width="35%" nowrap class="td1">
										<s:textfield id="docNumber" name="model.docNumber"
											maxlength="25" cssStyle="width:50%;" cssClass="textare"></s:textfield>
											<input style="display:none ;" />
									</td>		
									<th style="padding-right:10px;" width style="padding-right:10px;"="10%" nowrap class="biao_bg1" align="right">
										<span class="wz">模板类型：</span>
									</th>
									
									<td width="35%" class="td1" align="left">
										<s:select onchange="doSelect();" id="docType" name="model.docType"
												list="#{'':'请选择类型','1':'Word','2':'Excel','3':'PowerPoint','4':'Visio','5':'Project','6':'Wps'}"
												listKey="key" listValue="value" style="width:7.5em" />
									</td>	
	
								</tr>
								<tr height="100">
									<th style="padding-right:10px;" width="15%" nowrap class="biao_bg1" align="right"  valign="top">
										<span class="wz"><span style="color: red;">*</span>&nbsp;模板简介：</span>
									</th>
									<td width="35%" nowrap class="td1">
										<s:textarea id="doctemplateRemark" name="model.doctemplateRemark"
											cols="49" rows="5" cssClass="textare"></s:textarea>
										&nbsp;
									</td>
									<!-- <th style="padding-right:10px;" width="15%" nowrap class="biao_bg1" align="right" valign="top">
										<span class="wz">图标：</span>
									</th>
									<td width="35%" class="td1">
										<div id="myDiv" style="visibility: hidden">
											<img src='' id='personImg' style='width: 100px; height:70px;align:top;'>
										</div>
										<input type="file" id="file" name="file" onChange="showImage(this.value)"> 图片大小不超过1M
									</td> -->
								</tr>
								<tr>
									<td colspan="4" valign="top" height="100%" style="padding-top: 10px;">
										<%--<object id="TANGER_OCX_OBJ"
											classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
											codebase="<%=root%>/common/OfficeControl/OfficeControl.cab<%=OCXVersion%>"
											width="100%" height="850">
											<param name="ProductCaption" value="思创数码科技股份有限公司">
											<param name="ProductKey"
												value="B339688E6F68EAC253B323D8016C169362B3E12C">
											<param name="BorderStyle" value="1">
											<param name="TitlebarColor" value="42768">
											<param name="TitlebarTextColor" value="0">
											<param name="TitleBar" value="false">
											<param name="MenuBar" value="true">
											<param name="Toolbars" value="true">
											<param name="IsResetToolbarsOnOpen" value="true">
											<param name="IsUseUTF8URL" value="true">
											<param name="IsUseUTF8Data" value="true">
											<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
										</object>
									--%>
										<script type="text/javascript">
											document.write(OfficeTabContent);
										</script>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</s:form>
	</body>
</html>

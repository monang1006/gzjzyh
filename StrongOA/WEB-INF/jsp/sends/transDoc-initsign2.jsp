<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/eformOCX/version.jsp"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>公文签章</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		var isDoSave = false;//是否已经点击保存按钮
		var TANGER_OCX_Username = '${userName}';
		var tp1 ;
		var loaded = false;
		var oldSignInfo;	//保存的签章信息
		var SignatureAPI; //金格电子签章
		function custom(tabpage){
			if(tabpage.index != 1){//处理正文Tab
				return ;
			}
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
		}
		
		function openDoc(){
			var id = $("#id").val();
			if(!loaded){
				var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
				if(id == ""){
					var width=screen.availWidth-55;
		   			var height=screen.availHeight-130;
			  		var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
				  	TANGER_OCX_OBJ.OpenFromURL(basePath + "sends/transDoc!openEmptyDocFromUrl.action?docType="+type);
				  	TANGER_OCX_OBJ.WebFileName='新建文档';
				  	TANGER_OCX_OBJ.width = width;
				  	TANGER_OCX_OBJ.height = height;
				  	loaded = true;
				}else{
					var width=screen.availWidth-55;
		   			var height=screen.availHeight-130;
			  		var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
				  	TANGER_OCX_OBJ.OpenFromURL(basePath + "sends/transDoc!openWordDocFromUrl.action?model.docId="+id);
				  	TANGER_OCX_OBJ.width = width;
				  	TANGER_OCX_OBJ.height = height;
				  	loaded = true;
				}
				if(TANGER_OCX_OBJ.ActiveDocument){
					with (TANGER_OCX_OBJ.ActiveDocument.Application) {
					    UserName = '${userName}';
					}
				}
				loaded = true;
				tp1.setSelectedIndex(1);
				TANGER_OCX_OBJ.Activate(false); 
			}
		}
		var _flag_ret = true;//标记保存之后是否关闭窗口
		//保存表单数据
		function doSave(){
			var docIssueDepartSigned = $("#docIssueDepartSigned").val();
			if($.trim(docIssueDepartSigned) == ""){
				alert("发文单位不能为空，请填写");
				return ;
			}
			var docTitle = $("#docTitle").val();
			if($.trim(docTitle) == ""){
				alert("公文标题不能为空，请填写。");
				return ;
			}
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
		
			//var checkedInstall = TANGER_OCX_OBJ.IsNTKOSecSignInstalled();
			//if(!checkedInstall){
			//	if(confirm("系统检测到您没有安装电子签章系统，是否下载电子签章系统。")){
			//		location='<%=root%>/doc/sends/dianziqianzhang.rar';
			//	}
			//	return ;
			//}
			//校验是否锁定了文档
			var docType = TANGER_OCX_OBJ.DocType;//OFFICE类型
			var isqz = true;
			var signInfo = "[";
			var isLocked = false;//是否用印章锁定了公文
			if(docType == 1){	//	WORD
				var shapes = TANGER_OCX_OBJ.ActiveDocument.shapes;//得到所有印章
				
				
				for(i = 1;i<=shapes.Count;i++){
				   // alert("shapes.Count=");
				   // alert(shapes.Count);
				    
					if (12 == shapes(i).Type){ //如果是控件,判断控件类型 
					if("ISIGNATUREOFFICE.SIGNATURECTRL".toUpperCase() == shapes(i).OLEFormat.ClassType.toUpperCase()) {
						isqz =true;
						 SignatureAPI.InitSignatureItems();
					//alert(shapes(i).OLEFormat.ClassType.toUpperCase());
					  var XmlObj = new ActiveXObject("Microsoft.XMLDOM");
					  var XmlText;
					  for(j=0;j<SignatureAPI.SignatureCount;j++)
					  {
					    //alert(webform.SignatureAPI.SignatureItem(i));
					    XmlObj.async = false;
					    LoadOk=XmlObj.loadXML(SignatureAPI.SignatureItem(j));
					    ErrorObj = XmlObj.parseError;
					    if (ErrorObj.errorCode != 0){
					       alert("返回信息错误..." + ErrorObj.reason);
					    }
					    else{
					      var CurNodes=XmlObj.getElementsByTagName("Signature");
					      for (var iXml=0;iXml<CurNodes.length;iXml++){
					        var TmpNodes=CurNodes.item(iXml);
					        var signLock = TmpNodes.selectSingleNode("SignatureLocked").text;
							if(signLock){
								isLocked = true;
							}
							
							
				 			signInfo += "{isLockDoc:'"+isLocked+"',signer:'"+TmpNodes.selectSingleNode("SignatureUser").text+"'";
				 			signInfo += ",signtime='"+TmpNodes.selectSingleNode("SignatureDate").text+"',signname:'"+TmpNodes.selectSingleNode("SignatureName").text+"'},";
				 			
					      }
					    }
					  }
					  delete XmlObj;
					////////////////////
							
						}
					}
				}
				signInfo = signInfo.substring(0,signInfo.length-1);
				signInfo += "]";
				if(isqz)
				{
					signInfo = "";
				}
			}
        	var ret = TANGER_OCX_OBJ.SaveToUrl("<%=root %>/sends/transDoc!save.action",
                                           "wordDoc",
                                           "model.docState=1&signInfo=" + signInfo + "&oldSignInfo=" + oldSignInfo,
                                           "新建文档.doc",
                                           "form");
            if(ret.substring(0,1) == "0"){
            	if(!confirm("保存成功，是否继续编辑？")){
            		window.returnValue = "0";
            		window.close();
            	}else{
            		$("#id").val(ret.substring(2));
            		_flag_ret = false;
            	}
            }  
            if(ret == "-1"){
            	alert("正文数据读取失败。");
            	return ;
            }           
            if(ret == "-2"){
            	alert("对不起，发生未知异常，请与管理员联系。");
            	return ;
            }  
            if(ret == "-3"){
            	alert("公文状态已经改变，您无法对其操作,页面即将关闭！");
				window.close();
				return;
            }  
		}
		
		//提交
		function doSubmit(){
			var docIssueDepartSigned = $("#docIssueDepartSigned").val();
			if($.trim(docIssueDepartSigned) == ""){
				alert("发文单位不能为空，请填写");
				return ;
			}
			var docTitle = $("#docTitle").val();
			if($.trim(docTitle) == ""){
				alert("公文标题不能为空，请填写");
				return ;
			}
			//提交确认
			if(!confirm("您确认已经完成对公文签章并提交发文吗？\n单击“确定”继续，单击“取消”停止。")){
				return ;
			}
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
			//校验是否锁定了文档
			var docType = TANGER_OCX_OBJ.DocType;//OFFICE类型
			var signInfo = "[";
			var isLocked = false;//是否用印章锁定了公文
			var isqz = true;
			if(docType == 1){	//	WORD
				var shapes = TANGER_OCX_OBJ.ActiveDocument.shapes;//得到所有印章
				var count = shapes.Count;
				if(count == 0){
					alert("请加盖电子印章！");
					return ;
				}
				for(i = 1;i<=shapes.Count;i++){
					if (12 == shapes(i).Type){ //如果是控件,判断控件类型 
						if("ISIGNATUREOFFICE.SIGNATURECTRL".toUpperCase() == shapes(i).OLEFormat.ClassType.toUpperCase()) {
							 SignatureAPI.InitSignatureItems();
					  isqz = false;
					  var XmlObj = new ActiveXObject("Microsoft.XMLDOM");
					  var XmlText;
					  for(j=0;j<SignatureAPI.SignatureCount;j++)
					  {
					    //alert(webform.SignatureAPI.SignatureItem(i));
					    XmlObj.async = false;
					    LoadOk=XmlObj.loadXML(SignatureAPI.SignatureItem(j));
					    ErrorObj = XmlObj.parseError;
					    if (ErrorObj.errorCode != 0){
					       alert("返回信息错误..." + ErrorObj.reason);
					    }
					    else{
					      var CurNodes=XmlObj.getElementsByTagName("Signature");
					      for (var iXml=0;iXml<CurNodes.length;iXml++){
					        var TmpNodes=CurNodes.item(iXml);
					        var signLock = TmpNodes.selectSingleNode("SignatureLocked").text;
							if(signLock){
								isLocked = true;
							}
				 			signInfo += "{isLockDoc:'"+isLocked+"',signer:'"+TmpNodes.selectSingleNode("SignatureUser").text+"'";
				 			signInfo += ",signtime='"+TmpNodes.selectSingleNode("SignatureDate").text+"',signname:'"+TmpNodes.selectSingleNode("SignatureName").text+"'},";
				 			
					      }
					    }
					  }
					  delete XmlObj;
							
							
						}
					}
				}
				if(signInfo != "["){
					signInfo = signInfo.substring(0,signInfo.length-1);
				}
				signInfo += "]";
			}
			if(isqz)
				{
					alert("请加盖电子印章！");
					return ;
				}
			if(!isLocked){
				alert("请先用印章锁定公文！");
				return ;
			}
        	var ret = TANGER_OCX_OBJ.SaveToUrl("<%=root %>/sends/transDoc!save.action",
                                           "wordDoc",
                                           "model.docState=2&type=1&signInfo="+signInfo+"&oldSignInfo=" + oldSignInfo,
                                           "新建文档.doc",
                                           "form");
            if(ret.substring(0,1) == "0"){
           		//alert("操作成功！");
           		window.returnValue = "0";
           		window.close();
            }  
            if(ret == "-1"){
            	alert("正文数据读取失败。");
            	return ;
            }           
            if(ret == "-2"){
            	alert("对不起，发生未知异常，请与管理员联系。");
            	return ;
            }
            if(ret == "-3"){
            	alert("公文状态已经改变，您无法对其操作,页面即将关闭！");
				window.close();;
				return;
            }  
		}
		
		//删除附件,记录要删除的附件ID，多个以逗号隔开
		function deldbobj(id){
			var delIds = $("#deledAttachId").val();
			delIds = delIds + "," + id;
			$("#deledAttachId").val(delIds)
			$("#div"+id).hide();
		}
		//下载附件,改为链接实现
		function download(id){
			
		}
		
		//关闭
		function closeWindow(){
			if(!_flag_ret){
				window.returnValue = "0";
			}
			window.close();
		}
		//退回
		function doBack(){
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			openDoc();
			if(TANGER_OCX_OBJ == ""){
				loaded = false;
				openDoc();
			}
			
			var docType = TANGER_OCX_OBJ.DocType;//OFFICE类型
			if(docType == 1){	//	WORD
				var shapes = TANGER_OCX_OBJ.ActiveDocument.shapes;//得到所有印章
				var count = shapes.Count;
				if(count > 0){
					alert("该公文已签章，不可退回！");
					return ;
				}
			}else if(docType == 2){//excel
				var shapes = TANGER_OCX_OBJ.ActiveDocument.ActiveSheet.OLEObjects;
				var count = shapes.Count
				if(count > 0){
					alert("该公文已签章，不可退回！");
					return ;
				}
			}
			if(!confirm("确定要退回公文吗？")){
				return ;
			}
			var ret = TANGER_OCX_OBJ.SaveToUrl("<%=root %>/sends/transDoc!save.action",
                                           "wordDoc",
                                           "model.docState=1&oldSignInfo="+oldSignInfo,
                                           "新建文档.doc",
                                           "form");
            if(ret.substring(0,1) == "0"){
            	/*if(!confirm("保存成功，是否继续编辑？")){
            		window.returnValue = "0";
            		window.close();
            	}else{
            		$("#id").val(ret.substring(2));
            	}*/
           		_flag_ret = false;
            }  
            if(ret == "-1"){
            	alert("正文数据读取失败。");
            	return ;
            }           
            if(ret == "-2"){
            	alert("对不起，发生未知异常，请与管理员联系。");
            	return ;
            }
            if(ret == "-3"){
				alert("公文状态已经改变，您无法对其操作,页面即将关闭！");
				window.close();
				return;
            }  
			var ret = OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=sends/transDoc-initback.jsp&id="+$("#id").val(),"400","300",window);
			if(ret){
				window.opener.location.reload();
				window.close();
			}else{
				alert("操作错误，请关闭后重新操作！");
			}
		}
		
		$(document).ready(function(){
			openDoc();
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			
			SignatureAPI = document.getElementById("SignatureAPI");
						var b=SignatureAPI.SignatureInstalled(); //检测电子签章是否安装在本机上。
						
						if(b){
							SignatureAPI.ActiveDocument=TANGER_OCX_OBJ.ActiveDocument;   //设置WORD对象
						}
						else{	
						  if(confirm("系统检测到您没有安装电子签章系统，是否下载电子签章系统。")){
								location='<%=root%>/doc/sends/dianziqianzhang.rar';
							}
						}
			//进入时得到签章信息
			var docType = TANGER_OCX_OBJ.DocType;//OFFICE类型
			var signInfo = "[";
			var isLocked = false;//是否用印章锁定了公文
			if(docType == 1){	//	WORD
				var shapes = TANGER_OCX_OBJ.ActiveDocument.shapes;//得到所有印章
				
				for(i = 1;i<=shapes.Count;i++){
					if (12 == shapes(i).Type){ //如果是控件,判断控件类型 
						if("ISIGNATUREOFFICE.SIGNATURECTRL".toUpperCase() == shapes(i).OLEFormat.ClassType.toUpperCase()) {
							
					  SignatureAPI.InitSignatureItems();
					
					  var XmlObj = new ActiveXObject("Microsoft.XMLDOM");
					  var XmlText;
					  for(j=0;j<SignatureAPI.SignatureCount;j++)
					  {
					    //alert(webform.SignatureAPI.SignatureItem(i));
					    XmlObj.async = false;
					    LoadOk=XmlObj.loadXML(SignatureAPI.SignatureItem(j));
					    ErrorObj = XmlObj.parseError;
					    if (ErrorObj.errorCode != 0){
					       alert("返回信息错误..." + ErrorObj.reason);
					    }
					    else{
					      var CurNodes=XmlObj.getElementsByTagName("Signature");
					      for (var iXml=0;iXml<CurNodes.length;iXml++){
					        var TmpNodes=CurNodes.item(iXml);
					        var signLock = TmpNodes.selectSingleNode("SignatureLocked").text;
							if(signLock){
								isLocked = true;
							}
				 			signInfo += "{isLockDoc:'"+isLocked+"',signer:'"+TmpNodes.selectSingleNode("SignatureUser").text+"'";
				 			signInfo += ",signtime='"+TmpNodes.selectSingleNode("SignatureDate").text+"',signname:'"+TmpNodes.selectSingleNode("SignatureName").text+"'},";
				 			
					      }
					    }
					  }
					  delete XmlObj;
							
							
							
						}
					}
				}
				if(signInfo != "["){
					signInfo = signInfo.substring(0,signInfo.length-1);
				}
				signInfo += "]";
			}
			oldSignInfo = signInfo ;
		});

		
	</script>

	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" scroll="auto">
			<form id="form" name="form" action="<%=root %>/sends/transDoc!save.action" method="post" enctype="multipart/form-data">
				<input type="file" style="display: none ;" name="wordDoc"/><!-- WORD正文 -->
				<s:hidden id="id" name="model.docId"></s:hidden>
				<s:hidden id="deledAttachId" name="model.deledAttachId"></s:hidden>
				<s:hidden name="model.docSealPeople"></s:hidden>
				<s:hidden name="model.docSealTime"></s:hidden>
				<s:hidden name="model.ddocEntryPeople"></s:hidden>
				<s:hidden name="model.isdelete"></s:hidden>
				<s:hidden name="model.rest3"></s:hidden>
				<s:hidden name="model.docCcDepart_id"></s:hidden>
				<s:hidden name="model.docSubmittoDepart_id"></s:hidden>

				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td>
										&nbsp;
									</td>
									<td width="30%">
									<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
										审批签章
									</td>
									<td width="70%">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
											<td>
													<a class="Operation" onclick="doSave();" href="#">
														<img src="<%=frameroot%>/images/baocun.gif" width="15"
															height="15" class="img_s">保存</a>
												</td>
											  <td width="5"></td>
											 <td>
													<a class="Operation" onclick="doSubmit();" href="#">
														<img src="<%=frameroot%>/images/songshen.gif" width="15"
															height="15" class="img_s">提交</a>
												</td>
												<td width="5"></td>
												<td>
														<a class="Operation" onclick="doBack();" href="#">
															<img src="<%=frameroot%>/images/cexiao.gif" width="15"
																height="15" class="img_s">退回</a>
													</td>
												 <td width="5"></td>
												<td>
													<a class="Operation" onclick="closeWindow();" href="#">
														<img src="<%=frameroot%>/images/guanbi.gif" width="15"
															height="15" class="img_s">关闭</a>
												</td>
												<td width="5">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<table width="100%" border="0" cellpadding="0"
					cellspacing="1" class="">
					<tr>
			            <td>
							<DIV style="100%" class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
									tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										<font size=3>公文摘要</font>
									</H2>
									<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">发文单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield title="发文单位" maxlength="25" readonly="true" id="docIssueDepartSigned" name="model.docIssueDepartSigned" cssStyle="width:83%"></s:textfield>
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">发文文号：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield title="发文文号" readonly="true" id="docCode" name="model.docCode" cssStyle="width:83%"></s:textfield>
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文标题：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textfield title="公文标题" readonly="true" maxlength="250" id="docTitle" name="model.docTitle" cssStyle="width:83%"></s:textfield>
											</td>
										</tr>
										
										<!--
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文种类：</span>
											</td>
											<td class="td1">
												<s:select title="公文种类" disabled="true" id="docClass" name="model.docClass" cssStyle="width:85%" list="#{'0':'上行文','1':'平行文','2':'下行文'}" listKey="key" listValue="value"></s:select>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">秘密等级：</span>
											</td>
											<td class="td1">
												<s:select title="秘密等级" disabled="true" id="docSecretLvl" name="model.docSecretLvl" cssStyle="width:63%" list="mmdjItems" listKey="dictItemName" listValue="dictItemName"></s:select>
											</td>
										</tr>
										-->
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">紧急程度：</span>
											</td>
											<td class="td1">
												<s:select title="紧急程度" disabled="true" id="docEmergency" name="model.docEmergency" cssStyle="width:85%" list="jjcdItems" listKey="dictItemName" listValue="dictItemName"></s:select>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">成文日期：</span>
											</td>
											<td class="td1">
												<input title="成文日期" name="model.docOfficialTime" disabled="disabled" value="<s:date name="model.docOfficialTime" format="yyyy-MM-dd"/>" id="docOfficialTime" style="width:63%"/>
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">主送单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textarea maxlength="500" readonly="true" title="主送单位" id="docSubmittoDepart" name="model.docSubmittoDepart" cssStyle="width:83%"></s:textarea>
												
												
											</td>
										</tr>
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">抄送单位：</span>
											</td>
											<td class="td1" colspan="3">
												<s:textarea maxlength="500" readonly="true" title="抄送单位" id="docCcDepart" name="model.docCcDepart" cssStyle="width:83%"></s:textarea>
												
											</td>
										</tr>
										<!--
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">会签人：</span>
											</td>
											<td class="td1">
												<s:textfield title="会签人" readonly="true" maxlength="100" id="docCountersigner" name="model.docCountersigner" cssStyle="width:85%"></s:textfield>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">签发人：</span>
											</td>
											<td class="td1">
												<s:textfield title="签发人" readonly="true" maxlength="100" id="docIssuer" title="签发人" name="model.docIssuer" cssStyle="width:63%"></s:textfield>
											</td>
										</tr>
										-->
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文创建日期：</span>
											</td>
											<td width="41%" class="td1">
												<input title="公文创建日期" disabled="disabled" value="<s:date name="model.docEntryTime" format="yyyy-MM-dd"/>" id="docEntryTime" name="model.docEntryTime" style="width:85%"/>
											</td>
											<td width="8%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文状态：</span>
											</td>
											<td width="43%" class="td1">
												<s:i18n name="ApplicationMessage">
													<input title="公文状态" disabled="disabled" value="<s:text name="transdoc.state.sign"/>" id="docState" style="width:63%" />
												</s:i18n>
											</td>
										</tr>
										
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">需要签章处理：</span>
											</td>
											<td class="td1" colspan="3">
												<s:if test="model.docSealIs!=null&&model.docSealIs.equals(\"1\")">
													<input id="docSealIs" disabled="disabled" checked="checked" value="1" name="model.docSealIs" type="checkbox" />
												</s:if>
												<s:else>
													<input id="docSealIs" disabled="disabled" value="1" name="model.docSealIs" type="checkbox" />
												</s:else>
											</td>
										</tr>
										<%--<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												短信通知：
											</td>
											<td class="td1" colspan="3">
													<s:textfield title="短信通知"  disabled="true" maxlength="70" id="rest10" name="model.rest10" cssStyle="width:83%"></s:textfield>
											</td>
										</tr>--%>
									</table>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage1>
									<H2 class=tab>
										<font size=3>公文正文</font>
									</H2>
									<object id="TANGER_OCX_OBJ" 
										classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
										codebase="<%=root%>/common/OfficeControl/OfficeControl.cab<%=OCXVersion%>"
										width="100%" height="100%">
										<param name='MakerCaption' value="思创数码科技股份有限公司">
										<param name='MakerKey' value='5C1FF1F1177246B272DB34DD8ADA318222D19F65'>
										<param name='ProductCaption' value='南昌市政府办公厅'>
										<param name='ProductKey' value='FD6357E9840E880F0B72EAC5357E7303379848AB'>
									    <param name='TitleBar' value='false'>
										<param name='BorderStyle' value='1'>
										<param name='TitlebarColor' value='42768'/>
										<param name='TitlebarTextColor' value='0'>
										<param name='IsResetToolbarsOnOpen' value='false'>
										<param name='IsUseUTF8URL' value='true'>
										<param name='IsUseUTF8Data' value='true'>
										<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
									</object>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
								</DIV>
								<DIV class=tab-page id=tabPage3>
									<H2 class=tab>
										<font size=3>附件</font>
									</H2>
									<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
										<tr>
											<td width="10%" height="28" class="biao_bg1" align="right">
												<span class="wz">公文附件：</span>
											</td>
											<td class="td1" colspan="3">
												<s:iterator value="model.ttransDocAttaches" status="statu" id="att">
													<div id=div<s:property value="docAttachId"/>>
														<a href="<%=path %>/sends/transDoc!downLoad.action?model.deledAttachId=<s:property value="docAttachId"/>"
															target="myIframe"><s:property value="attachFileName" />
														</a>
													</div>
												</s:iterator>
											</td>
										</tr>
									</table>	
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</SCRIPT>
								</DIV>
							</DIV>
						</td>
					</tr>
				</table>
			</form>
	</body>
	<OBJECT id=SignatureAPI
	  		classid="clsid:79F9A6F8-7DBE-4098-A040-E6E0C3CF2001"
	  		codebase="<%=root%>/common/goldgridOCX/iSignatureAPI.ocx#version=5,1,0,18"
	  		width=0
	  		height=0
	  		align=center
	  		hspace=0
	  		vspace=0>
		</OBJECT>
</html>
<iframe name="myIframe" style="display:none"></iframe>
<%--<script language="JScript" for="TANGER_OCX_OBJ" event="OnSecSignSetInfo(UserName,SignType,SecSignObject)">
	alert();
</script>
<script language="JScript" for="TANGER_OCX_OBJ" event="OnBeforeDoSecSign(UserName,SignName,SignUser,SignSN,IsCancel)">	
	alert("UserName="+UserName+",SignName="+SignName+",SignUser="+SignUser+",SignSN="+SignSN+",IsCancel="+IsCancel);
</script>

<script language="JScript" for="TANGER_OCX" event="OnSecSignDeleted(UserName,SignName,SignUser,SignSN,EkeySN ,UserData)">	
	alert();
</script>--%>
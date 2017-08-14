<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>

		<title>增加文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
				<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript">
			var formReader = null;
			//得到表单模板对象
			function getFormPlugin() {
			    return document.getElementById("plugin").Content;
			}
			
			//得到表单读写器
			function getFormReader() {
			    return getFormPlugin().FormReader;
			}
			//控件加载完成之后调用此事件
			function onSilverlightLoad(){
				formReader = getFormReader();
				var url= formReader.FormServiceAddress + ".action";
				formReader.FormServiceAddress = url;
				if(OfficeTabContent && OfficeTabContent != ""){
					formReader.OfficeTabContent = OfficeTabContent;
				}
				//装载模板
				loadFormTemplate();
			}
			
			//装载模板
			function loadFormTemplate(){
				var actionUri = basePath +  "senddoc/eFormTemplate.action";
				document.getElementById("formAction").value = "LoadFormTemplate";
				if(formReader.LoadFormTemplate(actionUri,"form","loadFormTemplateRequestCompleted")){
					//方法调用成功
				} else {
					//调用失败
				}
			}
			var result = null;
			//装载模板成功后的回调函数
			/**
			0 – 提示图标
			1 – 询问图标
			2 – 警告图标
			3 – 错误图标
			*/
			function loadFormTemplateRequestCompleted(bResult, strResult,strDetail){
				if (bResult) {
			    	//加载表单模板成功
			    	//加载模板数据
			    	var docType = '${filemodel.fileDocType}';
  					if(docType == "1"){//通过OCX#GetData
			    		formReader.ShowMessageBox("出错啦","这是历史数据，不支持打开.","",0);//0:提示
			    		return ;
			    	}
  					result = $("#eformContent").val();
  					var businessId = '${filemodel.fileDocId}';
  					result = eval('('+result+')')
  					var data = businessId.split(";");
					formReader.ClearLoadFormDataFilter();
					formReader.SetLoadFormDataFilter(data[0], data[1]+"=?");
					formReader.AddLoadFormDataFilterParameter(data[0],data[1],data[2]);
					var actionUri = basePath +  "senddoc/eFormTemplate.action";
					document.getElementById("formAction").value = "LoadFormData";
					if(formReader.LoadFormData(actionUri,"form","loadFormDataRequestCompleted")){
						//方法调用成功
					} else {
						//调用失败
					}    		
				} else {
			    	//加载表单模板失败
					//失败信息:strResult
					formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
				}
			
			}
			
			function loadFormDataRequestCompleted(bResult, strResult,strDetail) {
				if(bResult){
					//加载模板数据成功
					if(result != null){
					 	var instanceId = $("#instanceId").val();
						formReader = getFormReader();
 						var actionUri = basePath +  "senddoc/eFormTemplate.action";
 						formReader.LoadAuditOpinion(actionUri, $("#instanceId").val(), "");
				 		var ret = formReader.SetFormReadOnly(true);// 设置表单只读
				 		var control = formReader.GetFormControl("Button_viewformdata");//将“查看原表单按钮”不设置为只读,"Button_viewformdata"为“查看原表单按钮”特有的name
						if(control!=null){
							control.SetProperty ('ReadOnly',false);
						}
					}
				} else {
					//加载模板数据失败
					formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
				}
			}
			
			/**
		   *@param id 	附件id
		   @param name	附件名称
		   */	   
		   function editAttach(id,name,readOnly){
		   		if(id == ""){//附件数据未作保存
		   			if(confirm("附件数据未保存入库，是否先保存到库中？")){
						var actionUri = basePath +  "senddoc/sendDoc.action";
						document.getElementById("formAction").value = "saveFormData";
						accId=id;
						accName=name;
						if(formReader.SaveFormData(actionUri,"form","editAttachRequestCompleted")){
							//方法调用成功
						} else {
							//调用失败
						}
		   			}
				} else {
					var prifix = name.substring(name.lastIndexOf(".")+1,name.length);
				   	if(prifix == ""){
				   		alert("请选择您要修改的附件！");
				   	 	return ;
				   	}
				   	if(prifix!="doc" && prifix!="xls" && prifix!="mpp" && prifix!="docx"){
				   		/*alert("附件只支持修改OFFICE类型文档！");
				   	 	return ;*/
				   	 	download(id);
				   	 	return ;
				   	}
				   	 var Width=screen.availWidth-10;
				     var Height=screen.availHeight-30;
					 var ReturnStr=OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/workflow-editattach.jsp?bussinessId="+id
					 						+"&contextPath="+contextPath + "&readOnly=" + readOnly, 
				                                   Width, Height, window); 
				   	 /*if(ReturnStr){
				   	 	if(ReturnStr == "OK"){
				   	 			loadFormTemplate();
				   	 	}
				   	 }*/
				}	
		   }
   
		   function editAttachRequestCompleted(bResult, strResult,strDetail){
		   	var data=strResult;
			if(bResult){
				//保存成功
				if(data == "-1"){
					alert("读取电子表单信息失败，请检查是否上传的文件过大！");
				}else if(data == "-2"){
					alert("保存表单数据失败！请检查表单域字段是否绑定到对应表字段！");
				}else{//返回业务数据：表名;主键名;主键值
					var name=accName;
					var id=accId;
					loadFormTemplate();				   	 
				}
			} else {
				//保存失败
				formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
			}
		}
			
				//下载附件
			function download(id){				
				$("body").append("<iframe id='frame_attach' frameborder='0' scrolling='no' style='width:100%; height:100%;' style='display:none;' />");
   				var url = scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id="+id;
   				$("#frame_attach").attr("src", scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id="+id);
			}
			
			
			/**
			  * 设置PDF正文临时文件信息 by luosy
			  * */
			function setPdfContentInfo(info){$("#pdfContentInfo").val(info);}
			function saveformiframe(){}
			
			function initForm(){ return "1";}
			function initOrgName(){}
	   	    //得到拟稿人,此函数供电子表单自动调用
	   	    function initUserName(){}
	        function getDocNumber(){};
	        function selectOrg(){};
	        function selectOrgFromDict(param){};
	        function editFile(id,name){};
	        function getWorkflowCode(){};
	        function getAutoCode(){}
	        function checkDocNumber(){}
			//查看流程图
			function viewWorkflow(){
			  var workflow="${workflow}";
			  workflow = workflow.split(";")[0];
			  var width=screen.availWidth-10;;
	          var height=screen.availHeight-30;
	          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/archive/tempfile/tempFile-container.jsp?instanceId="+workflow, 
	                                   width, height, window);
			}
			   /**
			    * 查看表单数据
			    * @param {} formId				表单模板id
			    * @param {} tableName			表名称
			    * @param {} pkFieldName			主键名称
			    * @param {} pkFieldValue		主键值
			    */
			   function viewFormData(formId,tableName,pkFieldName,pkFieldValue) {
			   	var width=screen.availWidth-10;
			   	var height=screen.availHeight-30;
			   	var ReturnStr=OpenWindow(scriptroot + "/senddoc/sendDoc!viewFormData.action?formId="+formId
			   							+"&tableName="+tableName+"&pkFieldName="+pkFieldName
			   							+"&pkFieldValue="+pkFieldValue,width, height, window);
			   }
			   
			 /**
 			* 打印处理单(挂接模板)
 			*/
			function goToPrint(){
				
				if(temp==0){//判断TAB页是否展现的是电子表单的页面,是：true 否:false
			    	$("#tabPage2").show();
			    	$("#tabPage1").hide();
			    }
    			var width=screen.availWidth-10;;
    			var height=screen.availHeight-30;
				$.post("<%=root%>/senddoc/sendDoc!findTemplateByFormId.action",{formId:$("#formId").val()},function(ret){
					if(ret != "notconfig"){
						var ReturnStr=OpenWindow(scriptroot+"/fileNameRedirectAction.action?toPage=senddoc/sendDoc-printform.jsp?template="+ret, 
								1360, 700, new Array(formReader,$("#formId").val()));
					} else {
						formReader.PrintForm();
					}
				});
			}  
			 /**
			 * 是否要显示查看原表单按钮
			 * @author 严建 2011-12-1 18:05
			 * @return 
			 */
			function isExistPersonDemo(){
				if($("#parentInstanceId").val() == ""){//父流程实例ID不存在时，不显示原表单按钮，return false，否则return true
					return false;
				}else{
					return true;
				}
			}
			
			/**
			 * 查看原表单按钮
			 * @author 严建 2011-12-1 18:05
			 * @return 
			 */
			function viewParentFormData() {
			   	var width=screen.availWidth-10;
			   	var height=screen.availHeight-30;
			   	if($("#personDemo").val() == null || $("#personDemo").val() == ""){
			   		alert("该流程表单无原表单数据！")
			   		return;
			   	}
			   		var ReturnStr=OpenWindow(scriptroot + "/senddoc/sendDoc!viewParentFormData.action?instanceId="+$("#parentInstanceId").val(),width, height, window);					
			   }
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		scroll="auto">
		<form id="form">
		<input type="hidden" id="formId" name="formId" value="${appendFormId}">
		<input type="hidden" id="formAction" name="formAction">
		<s:hidden id="eformContent" name="eformContent"></s:hidden>
		<s:hidden id="formData" name="businessName"></s:hidden>
		<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
		<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
		<!-- 业务表名称 -->
		<s:hidden id="tableName" name="tableName"></s:hidden>
		<!-- 业务表主键名称 -->
		<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
		 <!-- 查看原表单相关 -->
		 <s:hidden id="personDemo" name="personDemo"></s:hidden>
		 <!-- 流程实例ID -->
		<s:hidden id="instanceId" name="instanceId"></s:hidden>
		<!-- 父流程实例ID -->
		<s:hidden id="parentInstanceId" name="parentInstanceId"></s:hidden>
		<!-- 业务表主键值 -->
		<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
		 <!-- PDF正文信息描述 -->
		 <s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
		</form>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<input type="hidden" id="forwardStr" name="forwardStr"
							value="${forwardStr}">
						<input type="hidden" id="folderId" name="folderId"
							value="${folderId}">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												查看档案文件
											</td>
											<td width="25%">

												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="*">
															&nbsp;
														</td>
														<td>
															<a class="Operation" href="javascript:viewWorkflow();"><img
																	src="<%=root%>/images/ico/quanxankongzhi.gif"
																	width="15" height="15" class="img_s">查看流程&nbsp;</a>
															
														</td>
														<td>
															<a class="Operation" href="javascript:goToPrint();"><img
																	src="<%=root%>/images/ico/baocun.gif"
																	width="15" height="15" class="img_s">打印处理单&nbsp;</a>
															
														</td>
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

						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							style="vertical-align: top;">
							<tr>
								<td height="100%">
									<DIV class=tab-pane id=tabPane1>
										<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById("tabPane1") ,false );
								</SCRIPT>
										<DIV class=tab-page id=tabPage1 style="height: 22px;">
											<H2 class=tab>
												基本信息
											</H2>
											<input type="hidden" id="fileId" name="model.fileId" value="${filemodel.fileId}">
						<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">文件编号：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${filemodel.fileNo}
								</td>
							</tr>
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">作者名：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${filemodel.fileAuthor}
								</td>
							</tr>
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">文件题名：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${filemodel.fileTitle}
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件创建日期：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									<s:date format="yyyy-MM-dd" name="filemodel.fileDate" />
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件页号：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${filemodel.filePage}
								</td>
							</tr>
							<tr>
								<td height="21" class="biao_bg1" align="right">
									<span class="wz">文件所属部门：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									${fileDepartmentName}
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
							
						</table>
											<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
										</DIV>
										
										<DIV class=tab-page id=tabPage2 style="height:500;">
												<H2 class=tab>
													表单展示
												</H2>
												<table width="100%" height="100%" border="0" cellpadding="0"
													cellspacing="0" align="center">
													<tr>
														<td width="100%">
												<div id="silverlightControlHost" style="position:relative;width: 100%;height: 100%">
												   	<object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%" id="plugin">
												  <param name="source" value="<%=path%>/FormReader/StrongFormReader.xap"/>
												  <param name="onError" value="onSilverlightError" />
												  <param name="onLoad" value="onSilverlightLoad" />
												  <param name="background" value="white" />
												  <param name="minRuntimeVersion" value="4.0.50401.0" />
												  <param name="autoUpgrade" value="true" />
												  <a href="<%=path %>/detection/lib/Silverlight.exe" style="text-decoration:none">
														  <img src="<%=path %>/detection/images/SLMedallion_CHS.png" alt="Get Microsoft Silverlight" style="border-style:none"/>
												  </a>
												   </object><iframe id="_sl_historyFrame" style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
												</div>
											    		</td>
													</tr>
												</table>
											<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>
										</DIV>
										<table width="90%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td align="center" valign="middle">
													<table width="27%" border="0" cellspacing="0"
														cellpadding="00">
														<tr>
															<td width="50%" align="center">
																<s:if test="searchType==null||searchType==''">
																	<input name="back" type="button" class="input_bg"
																		value="返 回" onclick="history.go(-1)">
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



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<title>查看文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript">
			var formReader = null;
			var loadFinish = false;
			
			function view(value){
					var Width=screen.availWidth-10;
              	 	var Height=screen.availHeight-30;
              	 	var tempfileId=document.getElementById("tempfileId").value;
              	 	$.ajax({
              	 		type:"post",
              	 		url:"<%=path%>/archive/tempfile/tempFile!getTempFileExt.action",
              	 		data:{
							tempfileId:tempfileId,
							tfileAppedId:value			
						},
						success:function(data){
							if(data!=null&&data!=""&&data!="null"){	
								if(data=="doc"||data=="docx"){
									var ReturnStr=OpenWindow("<%=root%>/archive/tempfile/tempFile!readAnnex.action?tempfileId="+tempfileId+"&tfileAppedId="+value, 
                                   		Width, Height, window);
								}else{
									if(confirm("对不起，该附件不是word文档，如果需要查看，请点击下载！")){
										//var frame=document.getElementById("annexFrame");	
										// frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
									}				
								}			
							}else{
								alert("对不起，该附件格式被破环！");
							}
						},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
              	 	});
				 }
				function viewAnnex(value){	//查看附件
		           var frame=document.getElementById("annexFrame");
		           frame.src="<%=path%>/archive/tempfile/tempFile!download.action?tfileAppedId="+value+"&tempfileId=${model.tempfileId}";
	            }
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
			    	var docType = '${model.tempfileDocType}';
  					if(docType == "1"){//通过OCX#GetData
			    		formReader.ShowMessageBox("出错啦","这是历史数据，不支持打开.","",0);//0:提示
			    		return ;
			    	}
  					result = $("#fileFileName").val();
  					var businessId = '${model.tempfileDocId}';
  					//result = eval('('+result+')')
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
						formReader = getFormReader();
						var tabSelectedName = "";
						if(formReader.TabSelectedName){
							tabSelectedName = formReader.TabSelectedName;
						}

						if(tabSelectedName == "正文" || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单"){
							initWord();
						}
						loadFinish = true;						
					 	var instanceId = $("#instanceId").val();
					 	//alert(instanceId);
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
			 * 打开一个空文档（2003格式），用于不同版本的WORD兼容 调用千航的OpenFromURL需指定路径为完整URL路径.
			 * 
			 * 邓志城 修改：2010年6月30日15:14:43 增加默认痕迹保留功能。
			 */
			function initWord() {		
				TANGER_OCX_OBJ = formReader.GetOfficeControl();
				if (!TANGER_OCX_OBJ) {// 校验是否存在WORD
					return;
				}
				var type = TANGER_OCX_OBJ.DocType;// 得到OFFICE类型.
				if (type == 0) {// 未初始化任何文档类型，默认初始化word
					if (TANGER_OCX_OBJ.GetOfficeVer() != 100) {// 如果客户端安装了OFFICE软件,则优先用OFFICE创建			
						type = 1;
					} else {// 如果未安装OFFICE,则验证客户端是否安装了WPS
						if (TANGER_OCX_OBJ.GetWPSVer() != 100) {// 安装了WPS软件,用WPS软件创建
							type = 6;// WPS
						} else {
							TANGER_OCX_OBJ.ShowTipMessage("信息提示", "很抱歉，您没有正确安装OFFICE软件",false);
							return;
						}
					}
				}		
				TANGER_OCX_OBJ.Activate(false);// 被叫方拒绝接收呼叫
				formReader = getFormReader();
				var tabSelectedName = "";
				if(formReader.TabSelectedName){
					tabSelectedName = formReader.TabSelectedName;
				}
				if(tabSelectedName == "正文" || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单"){
					var businessId = '${model.tempfileDocId}';
	  				var data = businessId.split(";");
					var bussinessId = data[0] + ";" + data[1] + ";" + data[2];
					var formId = $("#formId").val();
					TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));	
					
				}else{
					
					if(tabSelectedName == "公文征求意见单"){
						TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty_yjzx.doc");
					}else if(tabSelectedName == "公文转办单"){
						TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty_gwzb.doc");
					}else{	
						if(tabSelectedName == "正文"){
							TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty.jsp?docType=" + type);
						}
					}
			
				}

				TANGER_OCX_OBJ.WebFileName = '新建文档';
				if (typeof(initWordAfter) != "undefined") {
					initWordAfter();
				}
			}
			
		   	/**申仪玲 20121023
			 * 第一次初始化office对象时供表单调用的方法
			 * @param plugin 银光插件对象
			 * @param form  表单对象
			 * @param officeControl 千航控件对象
			 * @param documentType   初始化的office类型
			*/
			function NewOfficeDocument(plugin, form, officeControl, documentType) {
				if(loadFinish == false){
					return;
				}
				TANGER_OCX_OBJ = officeControl;
				var type=0;
				if(documentType=="None"){
					type=0;
				}else if(documentType=="Word"){
					type=1;
				}else if(documentType=="Excel"){
					type=2;
				}else if(documentType=="Visio"){
					type=4;
				}else if(documentType=="Project"){
					type=5;
				}else if(documentType=="WPS"){
					type=6;
				}
				if (type == 0) {// 未初始化任何文档类型，默认初始化word
					if (officeControl.GetOfficeVer() != 100) {// 如果客户端安装了OFFICE软件,则优先用OFFICE创建			
						type = 1;
					} else {// 如果未安装OFFICE,则验证客户端是否安装了WPS
						if (officeControl.GetWPSVer() != 100) {// 安装了WPS软件,用WPS软件创建
							type = 6;// WPS
						} else {
							officeControl.ShowTipMessage("信息提示", "很抱歉，您没有正确安装OFFICE软件",false);
							return;
						}
					}
				}		
				officeControl.Activate(false);// 被叫方拒绝接收呼叫
				var tabSelectedName = "";
				if(formReader.TabSelectedName){
					tabSelectedName = formReader.TabSelectedName;
				}
				if(tabSelectedName == '公文征求意见单'){
					officeControl.OpenFromURL(scriptroot + "/empty_yjzx.doc");
				}else if(tabSelectedName == '公文转办单'){
					officeControl.OpenFromURL(scriptroot + "/empty_gwzb.doc");
				}else{		
					officeControl.OpenFromURL(scriptroot + "/empty.jsp?docType=" + type);
				}				
				
				officeControl.WebFileName = '新建文档';
				TANGER_OCX_OBJ = officeControl;
				if (typeof(initWordAfter) != "undefined") {
					initWordAfter();
				}
				
				// 模板数据装载完成后调用
				if (typeof(AfterLoadFormData) != "undefined") {
					AfterLoadFormData();
				}
				
			}
   
			/**申仪玲 20121023
			 * 存在office对象时供表单调用的方法
			 * @param plugin 银光插件对象
			 * @param form  表单对象
			 * @param officeControl 千航控件对象
			 */
			function OpenOfficeDocument(plugin, form, officeControl){	
				if(loadFinish == false){
					return;
				}
  				var businessId = '${model.tempfileDocId}';
  				var data = businessId.split(";");
				var bussinessId = data[0] + ";" + data[1] + ";" + data[2];
				var formId = $("#formId").val();
				
				var tabSelectedName = "";
				if(formReader.TabSelectedName){
					tabSelectedName = formReader.TabSelectedName;
				}
				
				officeControl.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));
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
		   
		   /**
			 * 针对两张附件表
			 * @param id 
			 *            附件id
			 * @param name
			 *            附件名称
			 * @param tableNames
			 *            附件表名
			 * @param idName
			 *            附件主键名
			 */
			function editAttachs(id,name,readOnly,tableNames,idName) {
				//alert(readOnly);
				if (id == "") {// 附件数据未作保存
					if (confirm("附件数据未保存入库，是否先保存到库中？")) {
						var actionUri = basePath + "senddoc/sendDoc.action";
						document.getElementById("formAction").value = "saveFormData";
						accId = id;
						accName = name;
						if (formReader.SaveFormData(actionUri, "form",
								"editAttachRequestCompleted")) {
							// 方法调用成功
						} else {
							// 调用失败
						}
					}
				} else {
					var prifix = name.substring(name.lastIndexOf(".") + 1, name.length);
					if (prifix == "") {
						alert("请选择您要修改的附件！");
						return;
					}
					if (prifix != "doc" && prifix != "docx" && prifix != "xls" && prifix != "xlsx" && prifix != "mpp"
							) {
						/*
						 * alert("附件只支持修改OFFICE类型文档！"); return ;
						 */
						download(id,tableNames,idName);
						return;
					}
					var Width = screen.availWidth - 10;
					var Height = screen.availHeight - 30;
					var ReturnStr = WindowOpen(
							scriptroot
									+ "/fileNameRedirectAction.action?toPage=workflow/workflow-editattachs.jsp?bussinessId="
									+ id + "&contextPath=" + contextPath + "&readOnly=" + readOnly+"&tableNames="+tableNames
									+"&idName="+idName,'view', Width, Height,
							'查看');
					/*
					 * if(ReturnStr){ if(ReturnStr == "OK"){ loadFormTemplate(); } }
					 */
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
					//设置表单展示页面为选中tab页				
					tp1.setSelectedIndex(1);
			    }
			    if(formReader == null){
			    	alert("请等待表单加载完全!");
			    	return;
			    }
			    var fid = $("#formId").val();
				var fids = fid.split(",");
				if(fids.length>1){
					fid = fids[1];
				}
    			var width=screen.availWidth-10;;
    			var height=screen.availHeight-30;
				$.post("<%=root%>/senddoc/sendDoc!findTemplateByFormId.action",{formId:fid},function(ret){
					if (ret != "notconfig") {
	                    var templates = ret.split(";");
	                    if(templates.length>1){//该表单挂接了不止一个模板，用户选择一个模板后打开
							var ReturnStr = OpenWindow(scriptroot
											+ "/doctemplate/doctempItem/docTempItem!printformDocList.action?doctemplateId="
											+ ret, 550, 300, new Array(formReader,
											fid));
	
	                        if((ReturnStr+"")!="undefined"){
		                        openPrintDoc(ReturnStr,fid);
		                    }
	                    }else{//该表单只挂接了一个模板直接打开
	                        var templateInfo = templates[0].split(",");
	                        openPrintDoc(templateInfo[0]);
	                    }
					} else {
						formReader.PrintForm();
					}
				});
			}  
			
			function openPrintDoc(ret,formId){
			    var ReturnStr = OpenWindow(scriptroot+ "/fileNameRedirectAction.action?toPage=senddoc/sendDoc-printform.jsp?template="
			            + ret, 1360, 700, new Array(formReader,
			            formId));
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
function cancel(){	 //返回
	
	//var archiveSearch="<%=request.getParameter("archiveSearch")%>";
	var archiveSearch=document.getElementById("archiveSearch").value;
	 //alert(archiveSearch);
	if(!archiveSearch||"null"== archiveSearch){
	
	    var depLogo=document.getElementById("depLogo").value;
	    var treeValue=document.getElementById("treeValue").value;
	    var treeType=document.getElementById("treeType").value;
	    //alert(treeValue);
	   //location="<%=path%>/fileNameRedirectAction.action?toPage=archive/tempfile/tempFile-content1.jsp&treeValue="+treeValue+"&depLogo="+depLogo+"&treeType="+treeType;
	   location="<%=path%>/archive/tempfile/tempFile.action?treeValue="+treeValue+"&depLogo="+depLogo+"&treeType="+treeType;
	   }
	else{
	    var fileNo=document.getElementById("fileNo").value;
	    
	    var disLogo=document.getElementById("disLogo").value;
	    var groupType=document.getElementById("groupType").value;
	    var fileTitle=document.getElementById("fileTitle").value;
	    var fileFolder=document.getElementById("fileFolder").value;
	    var tempfileDeadline=document.getElementById("tempfileDeadline").value;
	    var orgId=document.getElementById("orgId").value;
	    var year=document.getElementById("year").value;
	    var month=document.getElementById("month").value;
	   location="<%=path%>/archive/filesearch/fileSearch.action?groupType="+groupType+"&disLogo="+disLogo+"&fileNo="+fileNo+"&fileTitle="+fileTitle+"&tempfileDeadline="+tempfileDeadline+"&year="+year+"&month="+month+"&orgId="+orgId+"&fileFolder="+fileFolder;
	}
}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		scroll="auto">
		<form id="form">
		<input type="hidden" id="formId" name="formId" value="${appendFormId}">
		<input type="hidden" id="formAction" name="formAction">
		<s:hidden id="fileFileName" name="fileFileName"></s:hidden>
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
						<input type="hidden" id="forwardStr" name="forwardStr" value="${forwardStr}">
						<input type="hidden" id="folderId" name="folderId" value="${folderId}">
						<input type="hidden" id="depLogo" name="depLogo" value="${depLogo }">
						<!-- 在添加和编辑档案中心文件时，设置返回参数 -->
						<input type="hidden" id="treeValue" name="treeValue" value="${treeValue }">
						<input type="hidden" id="treeType" name="treeType" value="${treeType }">
						<input type="hidden" id="fileNo" name="fileNo" value="${fileNo }">
						<input id="tempfileDeadline" name="tempfileDeadline" type="hidden" value="${tempfileDeadline}">
						<input type="hidden" id="fileTitle" name="fileTitle" value="${fileTitle }">
						<input type="hidden" id="fileFolder" name="fileFolder" value="${fileFolder }">
						<input type="hidden" id="orgId" name="orgId" value="${orgId }">
						<input type="hidden" id="month" name="month1" value="${month1 }">
						<input type="hidden" id="year" name="year1" value="${year1}">
						<input type="hidden" id="disLogo" name="disLogo1" value="${disLogo1 }">
						<input type="hidden" id="groupType" name="groupType" value="${groupType }">
						<input type="hidden" id="archiveSearch" name="archiveSearch" value="${archiveSearch}">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td width="30%" align="left">
												<strong>查看档案中心文件</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="0" cellspacing="0">
													<tr>
														<s:if test="#request.appendFormId != null">
															<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="viewWorkflow();">&nbsp;查看流程&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="goToPrint();">&nbsp;打印处理单&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
								                  		</s:if>
								                  		<s:if test="searchType != null&&searchType!=''">
															<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="cancel();">&nbsp;返&nbsp;回&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
														</s:if>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
							<tr>
								<td height="100%">
									<DIV class=tab-pane id=tabPane1>
										<SCRIPT type="text/javascript">
											tp1 = new WebFXTabPane( document.getElementById("tabPane1") ,false );
										</SCRIPT>
										<DIV class=tab-page id=tabPage1 style="height: 22px;">
											<H2 class=tab id="tab1">
												基本信息
											</H2>
											<input type="hidden" id="tempfileId" name="model.tempfileId"
												value="${model.tempfileId}">
											<table width="100%" height="10%" border="0" cellpadding="0"
												cellspacing="1" align="center" class="table1">
												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">文件编号：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<span class="wz">${model.tempfileNo}</span>
													</td>
												</tr>
												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">拟办人：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<span class="wz">${model.tempfileAuthor}</span>
													</td>
												</tr>
												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">文件题名：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<span class="wz">${model.tempfileTitle}</span>
													</td>
												</tr>
												<tr>
													<td  width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">文件创建日期：</span>
													</td>
													<td class="td1" colspan="3"  align="left">
													 <s:date name="model.tempfileDate" format="yyyy年MM月dd日 HH点mm分"/>
														
													</td>
												</tr>
<!--												<tr>-->
<!--													<td width="20%" height="21" class="biao_bg1" align="right">-->
<!--														<span class="wz">文件页数：</span>-->
<!--													</td>-->
<!--													<td class="td1" colspan="3" align="left">-->
<!--														<span class="wz">${model.tempfilePage}</span>-->
<!--													</td>-->
<!--												</tr>-->
												<tr>
													<td width="20%" height="21" class="biao_bg1" align="right">
														<span class="wz">文件所属部门：</span>
														<input id="tempfileDepartment"
															name="model.tempfileDepartment" type="hidden" size="30"
															value="${model.tempfileDepartment}" readonly="readonly">
													</td>
													<td class="td1" colspan="3" align="left">
														<span class="wz">${tempfileDepartmentName}</span>
													</td>
												</tr>
												<tr>
													<td width="20%"  height="21" class="biao_bg1" align="right">
														<span class="wz">文件备注：</span>
													</td>
													<td class="td1" colspan="3" align="left">
														<span class="wz">${model.tempfileDesc}</span>
													</td>
												</tr>
												<tr>
													<td height="21" class="biao_bg1" align="right">
														<span class="wz">附件：</span>
													</td>
													<td class="td1" colspan="3" align="left">
															<s:if
															test="model.toaArchiveTfileAppends!=null&&model.toaArchiveTfileAppends.size()>0">
															<s:iterator id="vo" value="model.toaArchiveTfileAppends">
					                                         	  	<a id="fujian" href="#" onclick="view('${vo.tempappendId}');"  style='cursor: hand;'><font color="blue">${vo.tempappendName}</font></a>
					                                              	<a href="#" onclick="viewAnnex('${vo.tempappendId}');"  style='cursor: hand;'>下载</a>
																	<br>
															</s:iterator>
														</s:if>
													</td>
												</tr>
											</table>
											<iframe id="annexFrame" style="display:none"></iframe>
											<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
										</DIV>
										<s:if test="#request.appendFormId != null">
										<DIV class=tab-page id=tabPage2 style="height:500;">
												<H2 class=tab id="tab2">
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
									</s:if>
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



<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<title>查看表单</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">

var formReader = null;
var formData = null;
var contextPath = "";//定义获取调用此JS的上下文路径,从form的action属性获取
var formId = null
			
$(document).ready(function(){
	 var fullContextPath = $("form").attr("action");
  	 contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  	 formId = $("#formId").val();   
});
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
				formData = document.getElementById("formData").value;
				var url= formReader.FormServiceAddress + ".action";
				formReader.FormServiceAddress = url;
				if (OfficeTabContent && OfficeTabContent != "") {
					formReader.OfficeTabContent = OfficeTabContent;
					// alert(OfficeTabContent);
				}
				//装载模板
				loadFormTemplate();
			}
			var result = null;
			var data = null;
			//装载模板
			function loadFormTemplate(){
				var index = formData.indexOf("@begin@");
  				var businessId = formData.substring(0,index);
  				var info = formData.substring(index + 7,formData.length);
  				data = businessId.split(";");
  				document.getElementById("formId").value = data[3];
  				//result = eval('('+info+')')
  				result = info;
				var actionUri = basePath +  "senddoc/eFormTemplate.action";
				document.getElementById("formAction").value = "LoadFormTemplate";
				if(formReader.LoadFormTemplate(actionUri,"form","loadFormTemplateRequestCompleted")){
					//方法调用成功
				} else {
					//调用失败
				}
			}
			
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
			    	
			    	var formData = document.getElementById("formData").value;
			    	if($("#formData").val().indexOf("@begin@") ==-1) {//通过加载方式
			    		formReader.ShowMessageBox("出错啦","这是历史数据，不支持打开.","",0);//0:提示
			    		return ;
			    	}
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
					formReader.ShowMessageBox("出错啦",strResult,strDetail,3);//0:提示
				}
			
			}
			
			function loadFormDataRequestCompleted(bResult, strResult,strDetail) {
				if(bResult){
					//加载模板数据成功
					formReader = getFormReader();
 					var actionUri = basePath +  "senddoc/eFormTemplate.action";
 					//alert($("#instanceId").val());
 					formReader.LoadAuditOpinion(actionUri, "$0"+result, "");
			 		
					var ret = formReader.SetFormReadOnly(true);// 设置表单只读
					var control = formReader.GetFormControl("Button_viewformdata");//将“查看原表单按钮”不设置为只读,"Button_viewformdata"为“查看原表单按钮”特有的name
					if(control!=null){
						control.SetProperty ('ReadOnly',false);
					}
						/*
					 * 上传领导批示单不再使用附件控件形式，去掉对附件控件为空的判断 yanjian 2012-02-24 12:56
					 *modify yanjian 2012-11-28 23:49 解决——转换续办公文后查看关联文件可以看到办结意见页，但是文还没有到办结这一步
					 * */
					 
					if(formReader.GetFormControl("doneSuggestion_content")){
						var doneSuggestion_contentIsExist = (formReader.GetFormControl("doneSuggestion_content").Value != ""?true:false)
						if(doneSuggestion_contentIsExist){
							formReader.SetFormTabVisibility("attachName",true);
							formReader.SetFormTabVisibility("doneSuggestion",true);
						}else{
							formReader.SetFormTabVisibility("attachName",false);
							formReader.SetFormTabVisibility("doneSuggestion",false);
						}
					}
				} else {
					//加载模板数据失败
					formReader.ShowMessageBox("出错啦",strResult,strDetail,3);//0:提示
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
				officeControl.OpenFromURL(scriptroot + "/empty.jsp?docType=" + type);
				
				officeControl.WebFileName = '新建文档';
				TANGER_OCX_OBJ = officeControl;
				if (typeof(initWordAfter) != "undefined") {
					initWordAfter();
				}
				
			}
			
			
			/**申仪玲 20121023
			 * 存在office对象时供表单调用的方法
			 * @param plugin 银光插件对象
			 * @param form  表单对象
			 * @param officeControl 千航控件对象
			 */
			function OpenOfficeDocument(plugin, form, officeControl){
				var index = formData.indexOf("@begin@");
  				var businessId = formData.substring(0,index);
  				data = businessId.split(";");
  				document.getElementById("formId").value = data[3]; 				
				var bussinessId = data[0] + ";" + data[1] + ";" + data[2];
				formId = data[3];
				officeControl.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId);
				
				officeControl.WebFileName = '新建文档';
				TANGER_OCX_OBJ = officeControl;
				if (typeof(initWordAfter) != "undefined") {
					initWordAfter();
				}
			}

			// 打开附件，主要为PDF，EXCLE
			function openLoadAtt(id) {
				$("body")
						.append("<iframe id='frame_attach' frameborder='0' scrolling='no' style='width:100%; height:100%;' style='display:none;' />");
				var url = scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id="
						+ id;
				$.post(scriptroot + "/senddoc/sendDoc!openLoadAttachment.action", {
							id : id
						}, function(ret) {
							var width = screen.availWidth - 10;
							var height = screen.availHeight - 30;
							var ReturnStr = OpenWindow(scriptroot+"/openatt.jsp?ret="
									+ ret, width, height, window);
						});
			}


			/**
		   *@param id 	附件id
		   @param name	附件名称
		   */	   
			function editAttach(id, name,readOnly) {
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
					 						+"&contextPath="+contextPath +"&readOnly=" + readOnly, 
				                                   Width, Height, window); 
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
			
			function initForm(){}
			function initOrgName(){}
	   	    //得到拟稿人,此函数供电子表单自动调用
	   	    function initUserName(){}
	        function getDocNumber(){}
	        function selectOrg(){}
	        function selectOrgFromDict(param){}
	        function editFile(id,name){}
	        function getAutoCode(){}
	        
	        /**
			 * 是否要显示查看原表单按钮
			 * @author 严建 2011-12-1 18:05
			 * @return 
			 */
			function isExistPersonDemo(){
				/**
				 * yanjian 查看原表单添加业务处理代码
				 * 
				 * */
				if(getPersonDemo() != null && getPersonDemo() != ""){
					var personDemoJSON = eval($("#personDemo").val());
					var flag = false;
					for(var i=0;i<personDemoJSON.length;i++){
						var tempbusid = personDemoJSON[i].businessId;
						var tempbusid = personDemoJSON[i].businessId;
						if(tempbusid.split(";")[2] != $("#pkFieldValue").val() 
								|| "处理收文办理"==$("#workflowName").val()
								|| "快速办文流程"==$("#workflowName").val()){//所有父流程中，存在与当前流程业务id不一样的数据
							$("#parentInstanceId").val(personDemoJSON[i].instanceId);
							flag = true;
							break;
						}
					}
					if(!flag){
						$("#parentInstanceId").val("");
					}
				}
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
			   	var time=new Date();
			   	if($("#personDemo").val() == null || $("#personDemo").val() == ""){
			   		alert("该流程表单无原表单数据！")
			   		return;
			   	}
//			   		var ReturnStr=window.open(scriptroot + "/senddoc/sendDoc!viewParentFormData.action?instanceId="+$("#parentInstanceId").val()+"&time="+time.getTime(),500, 600, window);	
			   		var url = scriptroot + "/senddoc/sendDoc!viewParentFormData.action?instanceId="+$("#parentInstanceId").val()+"&time="+time.getTime();
			   		var ReturnStr=window.open(url,'window'+(new Date()).getTime(),'height='+height+',width='+width+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no')	
			   }
	        
	        
	        function getPersonDemo(){
				return $("#personDemo").val();
			}
			
			function getPersonDemoByBussId(tableName,pkName,pkValue){
			
				var thisformData = $("#formData").val();
				var buss = thisformData.split(";");
				alert(buss[0]+";"+buss[1]+";"+buss[2])
			
			    var personDemo = $.ajax({url: contextPath+"!viewPersonDemo.action?tableName="+buss[0] +"&pkFieldName="+buss[1]+"&pkFieldValue="+buss[2]
			            ,async: false}).responseText; 
			
			    if(personDemo != ""){
			        $("#personDemo").val(personDemo);
			    } else {
			         $("#personDemo").val("");
			    }
			    //alert("in jsp personDemo.value:\n"+$("#personDemo").val());
			    return $("#personDemo").val();
			}
	        
			//打印处理
			function goToPrint(){
		/*
			    var width=screen.availWidth-10;;
    			var height=screen.availHeight-30;
				$.post(contextPath+"!findTemplateByFormId.action",{formId:$("#formId").val()},function(ret){
					if(ret != "notconfig"){
						var ReturnStr=OpenWindow(scriptroot+"/fileNameRedirectAction.action?toPage=senddoc/sendDoc-printform.jsp?template="+ret, 
								1360, 700, new Array(formReader,$("#formId").val()));
					} else {
						formReader.PrintForm();
					}
				});
			}
		*/					
	
					var fid = $("#formId").val();
					var fids = fid.split(",");
					if(fids.length>1){
						fid = fids[1];
					}
					$.post(contextPath + "!findTemplateByFormId.action", {
								formId : fid
							}, function(ret) {
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
		</script>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;" scroll="auto">
		<form id="form" name="form"
		action="<%=root%>/senddoc/sendDoc!viewFormData.action" method="post">
		<s:hidden id="formData" name="businessName"></s:hidden>
		<input type="hidden" id="formAction" name="formAction">
		 <!-- 查看原表单相关 -->
		 <s:hidden id="personDemo" name="personDemo"></s:hidden>
		 <!-- 流程实例ID -->
		<s:hidden id="instanceId" name="instanceId"></s:hidden>
		<!-- 父流程实例ID -->
		<s:hidden id="parentInstanceId" name="parentInstanceId"></s:hidden>
		<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
		<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
		<!-- 业务表名称 -->
		<s:hidden id="tableName" name="tableName"></s:hidden>
		<!-- 业务表主键名称 -->
		<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
		<s:hidden id="workflowName" name="workflowName"></s:hidden>
		<!-- 业务表主键值 -->
		<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
		 <!-- PDF正文信息描述 -->
		 <s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
		<input id="formId" name="formId" type="hidden"/>
		</form>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>

											<td width="50%" >
											<table width="140" align="right" border="0" cellspacing="0" cellpadding="0">
											<tr><td onclick="goToPrint();" >
															<a class="Operation" href="#"><img
																	src="<%=root%>/images/ico/tb-print16.gif"
																	width="15" height="15" class="img_s">打印处理单&nbsp;</a>
															
														</td>
														</tr></table></td>
														<td width="5">
														</td>
							</tr>														
						</table>
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
	</body>
</html>



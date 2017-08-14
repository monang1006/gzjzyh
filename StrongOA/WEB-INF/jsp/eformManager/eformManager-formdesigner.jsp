<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<title>电子表单设计器</title>
		<%@include file="/common/include/meta.jsp"%>
		<style type="text/css">
			body{
				margin-top: 0;
				margin-left: 0;
				margin-bottom: 0;
				margin-right: 0;
			}
		</style>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script type="text/javascript">
	var operating="<%=request.getParameter("operating")%>";//operating(用户当前操作)返回结果：add-新建，edit-修改，view-查看
	var formDesigner = null;
	function onSilverlightError(sender, args) {
		var appSource = "";
		if (sender != null && sender != 0) {
			appSource = sender.getHost().Source;
		}

		var errorType = args.ErrorType;
		var iErrorCode = args.ErrorCode;

		if (errorType == "ImageError" || errorType == "MediaError") {
			return;
		}

		var errMsg = "Unhandled Error in Silverlight Application " + appSource
				+ "\n";

		errMsg += "Code: " + iErrorCode + "    \n";
		errMsg += "Category: " + errorType + "       \n";
		errMsg += "Message: " + args.ErrorMessage + "     \n";

		if (errorType == "ParserError") {
			errMsg += "File: " + args.xamlFile + "     \n";
			errMsg += "Line: " + args.lineNumber + "     \n";
			errMsg += "Position: " + args.charPosition + "     \n";
		} else if (errorType == "RuntimeError") {
			if (args.lineNumber != 0) {
				errMsg += "Line: " + args.lineNumber + "     \n";
				errMsg += "Position: " + args.charPosition + "     \n";
			}
			errMsg += "MethodName: " + args.methodName + "     \n";
		}

		throw new Error(errMsg);
	}

	function getFormPlugin() {
		return document.getElementById("plugin").Content;
	}

	function getFormDesigner() {
		return getFormPlugin().FormDesigner;
	}


	//控件加载完成之后调用此事件
	function onSilverlightLoad(){
		formDesigner = getFormDesigner();
		var url=formDesigner.FormServiceAddress + ".action";
		formDesigner.FormTemplateSaveCommand="SaveFormTemplate";
		formDesigner.FormTemplateLoadCommand="LoadFormTemplate";
		formDesigner.FormTemplatePreviewCommand = "PreviewFormTemplate"
		formDesigner.FormServiceAddress=url;
		//formDesigner.IsFormServiceAddressSetupEnabled=false;
		//设置/得到是否允许数据表管理
		formDesigner.IsAllowTableManage=false;
		//init();
		formDesigner.FormLoadCommand = "init";
	}
	
	function PreviewFormTemplate(xmlFormTemplate){
		document.getElementById("xmlFormTemplate").value = xmlFormTemplate;
        window.showModalDialog("<%=root%>/fileNameRedirectAction.action?toPage=eformManager/StrongFormPreviewPage.jsp", 
        						window, "dialogWidth:800px;dialogHeight:600px;center:yes;help:no;resizable:yes;status:no;scroll:no");
		
	}
	
	function modify(){	
		var actionUri = basePath + "senddoc/eFormTemplate.action";
		document.getElementById("formAction").value = "LoadFormTemplate";
	    if(formDesigner.LoadFormTemplate(actionUri,"form","LoadFormTemplateRequestCompleted")){
				//方法调用成功
		} else {
				//调用失败
		}	
	}

	function LoadFormTemplate() {
		if(formDesigner.IsFormTemplateChanged==true){
			if(!confirm("当前表单模板已被修改,确认将放弃修改。")){
				return ;
			}
		}
		var result=OpenWindow("<%=path%>/eformManager/eformManager.action?operating=open",800,458);
		if(result!=null){
			document.getElementById("formId").value = result;
			var actionUri = basePath +  "senddoc/eFormTemplate.action";
		    document.getElementById("formAction").value = "LoadFormTemplate";
		    if(formDesigner.LoadFormTemplate(actionUri,"form","LoadFormTemplateRequestCompleted")){
					//方法调用成功
			} else {
					//调用失败
			}
		}
	}

	function LoadFormTemplateRequestCompleted(bResult, strResult,strDetail) {
		if (bResult) {
			   var id=$("#formId").val();
			  
			  	$.getJSON("<%=path%>/eformManager/eformManager!getEFormTemplateInfo.action",{"id":id},
				 function(json){
				 	if(json){
				 		$("#title").val(json.title);
						$("#type").val(json.type);
						$("#orgCode").val(json.orgCode);
						formDesigner.IsFormTemplateChanged=false;
						document.title = $("#title").val();
				 	}else{
				 		formDesigner.IsFormTemplateChanged=true;
				 	}
				 });			  
			//alert("成功：" + strResult);
		} else {
			formDesigner.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系",strDetail,3);//0:提示
		}
	}
	
	 //mode="Save" / "SaveAs"
     function SaveFormTemplate(mode) {
	 	var json = "";
	    var actionUri = basePath +  "senddoc/eFormTemplate.action";
		document.getElementById("formAction").value = "saveFormTemplate";
	  	if(mode == "Save"){
	  		    var id=$("#formId").val();
	  			json=OpenWindow("<%=path%>/eformManager/eformManager!input.action?id="+id,400,200);
       	} else if(mode == "SaveAs"){
       			var id="";
	  			json=OpenWindow("<%=path%>/eformManager/eformManager!input.action?id="+id,400,200);
	  			if(json){
	  				$("#formId").val("");
	  			}
       	}
       	if(json!=null){
       		if(json.title!=null&&json.title!=""){
				$("#title").val(encodeURIComponent(encodeURIComponent(json.title)));
       		}
			$("#type").val(json.type);
			$("#orgCode").val(json.orgCode);
	       	if(formDesigner.SaveFormTemplate(actionUri,"form","SaveFormTemplateRequestCompleted")){
					//方法调用成功
					window.returnValue = "reload";
			} else {
					//调用失败
					
			}
       	}
       	
    }
	
	function SaveFormTemplateRequestCompleted(bResult, strResult,strDetail) {
		if (bResult) {
			formDesigner.IsFormTemplateChanged=false;
			document.getElementById("formId").value = strResult;
			document.title = $("#title").val();
			
      		window.opener.window.location.reload();
			window.close();

		} else {
			formDesigner.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系",strDetail,3);//0:提示
		}
	}
	
	function onbeforeunload_handler(){
		var warning="";
		window.returnValue="reload";   
		if(formDesigner.IsFormTemplateChanged==true){
			warning="当前模板已经被改动，直接退出模板将无法保存！";
			return warning;
		}
		
	 } 	
	function OpenWindow(Url, Width, Height, WindowObj) {
		var ReturnStr = showModalDialog(Url, 
		                                WindowObj, 
		                                "dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
		                                "status:no;help:no;scroll:no;");
		return ReturnStr;
	}
	
	function init(){
		window.onbeforeunload = onbeforeunload_handler; 
		var id="${model.id}";
		if(id!=null){
			 document.getElementById("formId").value=id; 
		}
		if(operating=="edit"){
			//alert();
			modify();
		}
	}
</script>
	</head>

	<body>
		<form id="form" style="height: 100%">
			<s:hidden id="formAction" name="formAction"></s:hidden>
			<s:hidden id="title" name="title"></s:hidden>
			<s:hidden id="type" name="type"></s:hidden>
			<s:hidden id="orgCode" name="orgCode"></s:hidden>
			<s:hidden id="formId" name="formId"></s:hidden>
			<input type="hidden" id="xmlFormTemplate" >
			<div id="silverlightControlHost">
	        <object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%" id="plugin">
			  <param name="source" value="<%=path%>/FormDesigner/StrongFormDesigner.xap"/>
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
		</form>
	</body>
</html>

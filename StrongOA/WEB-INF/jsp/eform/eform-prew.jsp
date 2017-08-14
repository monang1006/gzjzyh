<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<title>表单预览</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
		<script type="text/javascript">
		var formReader = null;
		
function initForm(){
	return $("#formId").val();
}			
function initOrgName(){}
function initUserName(){}
	
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
	//装载模板
	//loadFormTemplate();
	formReader.FormLoadCommand = "loadFormTemplate";
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

//装载模板成功后的回调函数
function loadFormTemplateRequestCompleted(bResult, strResult,strDetail){
		if (bResult) {
			initial();
		} else {
			//formReader.ShowMessageBox("出错啦",strResult,strDetail,3);//0:提示
			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
		}
}

function initial(){
			formReader = getFormReader();
			var ret = formReader.SetFormReadOnly(true);// 设置表单只读
		}
		</script>
	</head>
	<body>
		<!-- 电子表单模板id -->
			<form id="form">
			<input id="formId" type="hidden" name="formId" value="<%=request.getParameter("formId") %>"/>
			<s:hidden id="formAction" name="formAction"></s:hidden>
		</form>
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

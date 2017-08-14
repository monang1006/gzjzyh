<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<title><s:if test="taskId != null && taskId !=''">
				流程办理
			</s:if> <s:else>
				新建流程
			</s:else>
		</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=path%>/frame/theme_gray/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript"
			src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<SCRIPT Language="JScript"> 
function RunScanPdf(strPath) { 
	try{
		var   objShell   =   new   ActiveXObject( "Wscript.Shell");
		var oExec = objShell.Exec(strPath);
		}catch(e){alert( e+'\n找不到文件 " '+strPath+ ' "(或它的组件之一)。\n1、请确定已注册\'regsvr32 WSHom.Ocx\'\n2、请确定软件已经正确安装。 ') 
	}
}
</SCRIPT>
		<script type="text/javascript">
var TANGER_OCX_OBJ = null;
var formReader = null;
	// 表单模板初始化失败时调用
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
// 得到表单模板对象
function getFormPlugin() {
	return document.getElementById("plugin").Content;
}

// 得到表单读写器
function getFormReader() {
	return getFormPlugin().FormReader;
}
// 控件加载完成之后调用此事件
function onSilverlightLoad() {
	formReader = getFormReader();
	var url = formReader.FormServiceAddress + ".action";
	formReader.FormServiceAddress = url;
	if (OfficeTabContent && OfficeTabContent != "") {
		formReader.OfficeTabContent = OfficeTabContent;
		// alert(OfficeTabContent);
	}
	// 装载模板
	loadFormTemplate();
}

// 装载模板
function loadFormTemplate() {
	var actionUri = basePath + "senddoc/eFormTemplate.action";
	document.getElementById("formAction").value = "LoadFormTemplate";
	if (formReader.LoadFormTemplate(actionUri, "form",
			"loadFormTemplateRequestCompleted")) {
		// 方法调用成功
	} else {
		// 调用失败
	}
}
// 装载模板成功后的回调函数
/**
 * 0 – 提示图标 1 – 询问图标 2 – 警告图标 3 – 错误图标
 */
function loadFormTemplateRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		// 加载表单模板成功 modify 严建 将该段初始化代码 删除，
		//这个过程延迟到表单属于初始化完毕(调用initFieldSet())之后 再进行调用
		//if (typeof(initialHtml) != "undefined") {
		//	initialHtml();
		//}
		var formControl = formReader.GetFormControl($("#tableName").val(),
				"WORKFLOWCODE");// 标题
		if(formControl){
				formControl.SetProperty ('ReadOnly',false);
		}
		formControl = formReader.GetFormControl($("#tableName").val(),
				"RECV_NUM");// 标题
		if(formControl){
				formControl.SetProperty ('ReadOnly',false);
		}
		TANGER_OCX_OBJ = formReader.GetOfficeControl();
		if (TANGER_OCX_OBJ) {
			if (document.getElementById("fileOperation")) {
				document.getElementById("fileOperation").style.display = "";
			}
		}
		// 加载模板数据
		if ($("#pkFieldValue").val() != "") {
			formReader.ClearLoadFormDataFilter();
			formReader.SetLoadFormDataFilter($("#tableName").val(),
					$("#pkFieldName").val() + "=?");
			formReader.AddLoadFormDataFilterParameter($("#tableName").val(),
					$("#pkFieldName").val(), $("#pkFieldValue").val());
			var actionUri = basePath + "senddoc/eFormTemplate.action";
			document.getElementById("formAction").value = "LoadFormData";
			if (formReader.LoadFormData(actionUri, "form",
					"loadFormDataRequestCompleted")) {
				// 方法调用成功
			} else {
				// 调用失败
			}
		} 

	} else {
		// 加载表单模板失败
		// 失败信息:strResult
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}

}

function loadFormDataRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		
	} else {
		// 加载模板数据失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

var isSaveReturn = false;
function saveFormData(isReturn) {

	saveformiframe();
	isSaveReturn = isReturn;
	var actionUri = basePath + "senddoc/eFormTemplate.action";
	document.getElementById("formAction").value = "SaveFormData";
	document.getElementById("workflowState").value = "3";
	if (formReader.SaveFormData(actionUri, "form",
			"saveFormDataRequestCompleted")) {
	} else {
		// 调用失败
	}
}
// 保存模板数据完成事件
function saveFormDataRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		// 保存成功
		AfterSaveFormData(isSaveReturn);// 回调函数

	} else {
		// 保存失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
		if (typeof(OpenIframe) != "undefined") {
			OpenIframe();
		}
	}
}
	    //保存表单成功以后的回调函数
	    function AfterSaveFormData(isReturn){
	        if(isReturn){
		    	//alert("保存成功！");
		   		window.returnValue = "OK" ;
		   		
		   		var parentWin = window.opener;//父窗口
		   		if(parentWin != null){
					if(typeof(parentWin.reloadPage) != "undefined"){
						parentWin.reloadPage();
					}
		   		}
				
		    	window.close();
	    	}else{
	    	    //alert("保存成功！");
	    	    window.returnValue = "OK" ;
	    	}
	    }
	//由eform.js中定义的doNext()回调
		function goBack(){
		aelrt(111);
			var parentWin = window.opener;//父窗口
			if(typeof(parentWin.reloadPage) != "undefined"){
				parentWin.reloadPage();
			}
	      	window.close();
		}

/**
 * 保存PDF正文临时文件 by luosy
 */
function saveformiframe() {
	var frame = document.frames["pdfFrame"];
	if ("undefined" != (typeof(frame)) && "function" == (typeof(frame.SaveDoc))) {
		frame.SaveDoc();
	}
}
/**
 * 获取文号
 * 
 * @return {文号}
 * @2010 10-14 10：10 郑志斌 @ 添加type 参数 ，以区分发文和收文规则 /senddoc/sendDoc
 *       /recvdoc/recvDoc
 */
function getDocNumber(type) {
	// var type = contextPath.replace(scriptroot,"");
	var ret = OpenWindow(scriptroot
					+ "/serialnumber/number/number!show.action?regulationSort="
					+ type, 400, 300, window);
	if (ret)
		return ret;
}

/**
 * 获取编号
 * 
 * @return {文号}
 * @2011-06-02 09:46 luosy @ 添加type 参数 ，规则id
 */
function getAutoCode(type) {
	// var type = contextPath.replace(scriptroot,"");
	var ret = OpenWindow(scriptroot + "/autocode/autoCode!input.action?id="
					+ type, 600, 450, window);
	if (ret)
		return ret;
}

/**
 * 表单上选择关联的字典项
 * 
 * @param param
 *            字典名
 */
function selectOrgFromDict(param) {
	// var ret =
	// OpenWindow("<%=root%>/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="+param,420,
	// 370, window);
	var ret = window
			.showModalDialog(
					scriptroot
							+ "/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="
							+ param,
					window,
					'dialogWidth:420pt ;dialogHeight:370pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ret)
		return ret;
}

/**
 * 初始化拟稿单位
 * 
 * @return {拟稿单位名称}
 */
function initOrgName() {
	// 可能action没传值过来
	if ($("#orgName").val() == "" || $("#orgName").val() == null) {
		return null
	} else {
		return $("#orgName").val();
	}
}

/**
 * 初始化拟稿人
 * 
 * @return {拟稿人姓名}
 */
function initUserName() {
	// 可能action没传值过来
	if ($("#userName").val() == "" || $("#userName").val() == null) {
		return null
	} else {
		return $("#userName").val();
	}
}

/**
 * 初始化拟稿人id
 * 
 * @author 胡丽丽
 * @return {拟稿人ID}
 */
function initUserId() {
	return $("#userId").val();
}


/**
 * 是否要显示查看原表单按钮
 * 
 * @author 严建 2011-12-1 18:05
 * @return
 */
function isExistPersonDemo() {
	return false;
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
	var bussinessId = $("#tableName").val() + ";" + $("#pkFieldName").val() + ";" + $("#pkFieldValue").val();
	officeControl.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + $("#formId").val());
	
	officeControl.WebFileName = '新建文档';
	TANGER_OCX_OBJ = officeControl;
	if (typeof(initWordAfter) != "undefined") {
		initWordAfter();
	}
}

//加载完word之后调用该函数
function initWordAfter(){	
	//initCustomMenus();
	//2011-12-21 20:55 是否默认保留痕迹 yanjian
	if (typeof(TANGER_OCX_SetMarkModify) != "undefined"){
		var doMarkModify = $("#officeFunction").attr("doMarkModify");
		if(doMarkModify == "1" ){
			TANGER_OCX_SetMarkModify(false);
		}else{
			TANGER_OCX_SetMarkModify(true);
		}
	}
	
	//2011-12-21 20:55 是否默认显示痕迹 yanjian
	if (typeof(TANGER_OCX_ShowRevisions) != "undefined"){
		var doShowRevisions = $("#officeFunction").attr("doShowRevisions");
    	if(doShowRevisions == "1"){
    		TANGER_OCX_ShowRevisions(true);
	   	}else{
	   		TANGER_OCX_ShowRevisions(false);
	   	}
	}	

}

/**
 * @param id
 *            附件id
 * @param name
 *            附件名称
 */
function editAttach(id, name,readOnly) {
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
			download(id);
			return;
		}
		var Width = screen.availWidth - 10;
		var Height = screen.availHeight - 30;
		var ReturnStr = WindowOpen(
				scriptroot
						+ "/fileNameRedirectAction.action?toPage=workflow/workflow-editattach.jsp?bussinessId="
						+ id + "&contextPath=" + contextPath + "&readOnly=" + readOnly,'view', Width, Height,
				'查看');
		/*
		 * if(ReturnStr){ if(ReturnStr == "OK"){ loadFormTemplate(); } }
		 */
	}
}

function editAttachRequestCompleted(bResult, strResult, strDetail) {
	var data = strResult;
	if (bResult) {
		// 保存成功
		if (data == "-1") {
			alert("读取电子表单信息失败，请检查是否上传的文件过大！");
		} else if (data == "-2") {
			alert("保存表单数据失败！请检查表单域字段是否绑定到对应表字段！");
		} else {// 返回业务数据：表名;主键名;主键值
			var name = accName;
			var id = accId;
			loadFormTemplate();
			/*
			 * var prifix = name.substring(name.lastIndexOf(".")+1,name.length);
			 * if(prifix == ""){ //alert("请选择您要修改的附件！"); return ; }
			 * if(prifix!="doc" && prifix!="xls" && prifix!="mpp" &&
			 * prifix!="docx"){ alert("附件只支持修改OFFICE类型文档！"); return ; } var
			 * Width=screen.availWidth-10; var Height=screen.availHeight-30; var
			 * ReturnStr=OpenWindow(scriptroot +
			 * "/fileNameRedirectAction.action?toPage=workflow/workflow-editattach.jsp?bussinessId="+id
			 * +"&contextPath="+contextPath, Width, Height, window);
			 */
			/*
			 * if(ReturnStr){ if(ReturnStr == "OK"){ loadFormTemplate();
			 * window.returnValue = "OK"; } }
			 */

		}
	} else {
		// 保存失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

// 下载附件
function download(id) {
	$("body")
			.append("<iframe id='frame_attach' frameborder='0' scrolling='no' style='width:100%; height:100%;' style='display:none;' />");
	var url = scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id="
			+ id;
	$("#frame_attach").attr("src",
			scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id=" + id);
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

// 得到流程流水号
function getWorkflowCode() {
	return $("#workflowCode").val();
}

/**
 * 处理办公厅意见征询业务 严建 2012-06-19 13:42
 * 
 */
function doNewYjzx() {
	var formControl = formReader.GetFormControl($("#tableName").val(),
			"WORKFLOWTITLE");// 标题
	var title = "";
	if (formControl) {
		title = formControl.Value;
		title = EE_EncodeCode(title);
	}
	var ret = OpenWindow(
			scriptroot
					+ "/fileNameRedirectAction.action?toPage=senddoc/sendDoc-writeyjzx.jsp?docId="
					+ $("#bussinessId").val() + "&instanceId="
					+ $("#instanceId").val() + "&businessName=" + title, "500",
			"300", window);
	if (ret) {
		if (ret == "ok") {//意见征询完毕,接下来进行数据提交
			alert("征求意见成功，系统将关闭本窗口，如需进行意见征询反馈请重新打开该公文！");
			saveFormData(true);
		}
	}
}
// 处理意见征询反馈，在在办文件中查看,上传意见征询反馈文件,并恢复挂起的流程
function doYjfk() {
	var ret = OpenWindow(scriptroot
					+ "/bgt/senddoc/sendDoc!yjzxfk.action?model.id="
					+ $("#pkFieldValue").val() + "&model.instanceId="
					+ $("#instanceId").val(), "400", "300", window);
	if (ret) {
		if (ret == "ok") {
			var parentWin = window.opener;// 父窗口
			if (typeof(parentWin.reloadPage) != "undefined") {
				parentWin.reloadPage();
			}
			window.close();
		}
	}
}
//查看意见征询登记信息
  function viewYjzx(){
  	var ret=OpenWindow(scriptroot + "/bgt/senddoc/sendDoc!input.action?model.id="+$("#pkFieldValue").val(),"500","300",window);
  }
  function EE_EncodeCode(stringObj) {
	if (stringObj == "" || stringObj == undefined || stringObj == "undefined") {
		return stringObj;
	}
	var result = "";
	var charTemp = "";
	if (typeof(stringObj) != "string") {
		alert("EE_EncodeCode()方法的参数必须是字符串!");
		return;
	}
	for (var i = 0; i < stringObj.length; i++) {
		charTemp = stringObj.charAt(i);
		if (charTemp == encodeURI(encodeURI(charTemp))) {
			if (charTemp == escape(escape(charTemp))) {
				result += encodeURIComponent(encodeURIComponent(charTemp));
			} else {
				result += escape(escape(charTemp));
			}
		} else {
			result += encodeURI(encodeURI(charTemp));
		}
	}
	return result;
}
	</script>
		<script language="JScript" for="TANGER_OCX"
			event="AfterOpenFromURL(doc)">
		doc.TrackRevisions = false; //进入痕迹保留状态
	</script>

		</script>
		<script language="JScript" for="TANGER_OCX_OBJ"
			event="OnCustomMenuCmd2(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID)">
		alert("第" + menuPos +","+ submenuPos +","+ subsubmenuPos +"个菜单项,menuID="+menuID+",菜单标题为\""+menuCaption+"\"的命令被执行.");
	</script>

	</head>
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="window.focus();">
		<!--初始化流程控制功能
					isShowlcqx(显示流程期限设置【0:不显示,1:显示】)
					suggestionrequired(审批意见是否必填 0：不控制必填；1：控制必填)
					showrepeal(是否启用废除功能 0:不启用；1：启用)
		-->
		<input type="hidden" id="workflowFunction" name="workflowFunction"
			isShowlcqx="${isShowlcqx }"
			suggestionrequired="${suggestionrequired}" showrepeal="${showrepeal}">
		<!--初始化office功能-->
		<input type="hidden" id="officeFunction" name="officeFunction"
			doShowRevisions="${doShowRevisions}" doMarkModify="${doMarkModify }">
		<!--是否可以上传PDF收文文件是否必填 0：不能上传；1：能上传 -->
		<input type="hidden" id="PDFFunction" name="PDFFunction"
			isPermitUploadPDF="${doPermitUploadPDF}" isFirstNode="${isFirstNode}">
		<form id="form" name="form"
			action="<%=root%>/senddoc/sendDoc!save.action" method="post">
			<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
			<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
			<!-- 业务数据名称 -->
			<s:hidden id="businessName" name="businessName"></s:hidden>
			<!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- 电子表单模板id -->
			<s:hidden id="formId" name="formId"></s:hidden>
			<!-- 任务id -->
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<!-- 拟稿单位 -->
			<s:hidden id="orgName" name="orgName"></s:hidden>
			<!-- 拟稿人 -->
			<s:hidden id="userName" name="userName"></s:hidden>
			<!-- 流程名称 -->
			<s:hidden id="workflowName" name="workflowName"></s:hidden>
			<!-- 流程实例ID -->
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<!-- 父流程实例ID -->
			<s:hidden id="parentInstanceId" name="parentInstanceId"></s:hidden>
			<!-- 节点上挂接的工作流插件信息 -->
			<s:hidden id="pluginInfo" name="pluginInfo"></s:hidden>
			<!-- 流程类别 -->
			<s:hidden id="workflowType" name="workflowType"></s:hidden>
			<!-- 流程流水号 -->
			<s:hidden id="workflowCode" name="workflowCode"></s:hidden>
			<!-- 用户职务 -->
			<s:hidden id="userJob" name="userJob"></s:hidden>
			<!-- CA签名信息 -->
			<s:hidden id="CASignInfo" name="CASignInfo"></s:hidden>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<s:hidden id="formAction" name="formAction"></s:hidden>
			<!-- workflow-nextstep.jsp 中的参数 -->
			<!-- 提醒方式 -->
			<s:hidden id="handlerMes" name="handlerMes"></s:hidden>
			<!-- 审批意见 -->
			<s:hidden id="suggestion" name="suggestion"></s:hidden>
			<!-- 迁移线名称 -->
			<s:hidden id="transitionName" name="transitionName"></s:hidden>
			<!-- 迁移线ID -->
			<s:hidden id="transitionId" name="transitionId"></s:hidden>
			<!-- 任务处理人 -->
			<s:hidden id="strTaskActors" name="strTaskActors"></s:hidden>
			<!-- 提醒方式 -->
			<s:hidden id="remindType" name="remindType"></s:hidden>
			<!-- 定时提醒设置 -->
			<s:hidden id="remindSet" name="remindSet"></s:hidden>
			<!-- 选择协办处室迁移线标志 -->
			<s:hidden id="returnFlag" name="returnFlag"></s:hidden>
			<!-- 流程状态 -->
			<s:hidden id="workflowState" name="workflowState"></s:hidden>
			<!-- 审批意见数据 -->
			<s:hidden id="approveData" name="approveData"></s:hidden>
			<s:hidden id="isGenzong" name="isGenzong"></s:hidden>
			<!-- 查看原表单相关 -->
			<s:hidden id="personDemo" name="personDemo"></s:hidden>
			<s:hidden id="userId" name="userId"></s:hidden>
			<!-- 会话剩余时间 -->
			<s:hidden id="timeout" name="timeout"></s:hidden>
			<!-- 流程期限值 -->
			<s:hidden id="processOutTime" name="processOutTime"></s:hidden>
			<!-- PDF正文信息描述 -->
			<s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
			<!-- 办结意见 -->
			<input id="doneSuggestion" name="doneSuggestion" secondname="处理情况"
				value="${doneSuggestion}" type="hidden" />
			<!-- 领导批示扫描附件 -->
			<input id="attachName" name="attachName" secondname="领导批示意见"
				value="${attachName}" type="hidden" />
			<!-- 是否要验证领导批示扫描附件上传 -->
			<input id="isValidate" name="isValidate" value="" type="hidden" />
			<input id="showBgtTags" name="showBgtTags" value="showBgtTags"
				style="display: none;" type="button" onclick="showBgtTagsbak()" />
			<input id="hideBgtTags" name="hideBgtTags" value="hideBgtTags"
				style="display: none;" type="button" onclick="hideBgtTagsbak()" />
			<!--意见输入模式 -->
			<s:hidden id="suggestionStyle" name="suggestionStyle"></s:hidden>
			<!--意见征询标识 0：已经进行过意见征询-->
			<s:hidden id="businessType" name="businessType"></s:hidden>
		</form>
		<DIV id=contentborder align=center>
			<table width="100%" height="100%" border="0" cellspacing="0"
				cellpadding="00">
				<tr>
					<td height="40">

						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr id="privilege" >
							<s:if test="instanceId != null">
								<s:if test="workflowType == \"3\"">
									<s:if test="businessType == \"0\"">
										<td>
											<a class="Operation" href="#"
												onclick="JavaScript:viewYjzx();">&nbsp;<img
													src="<%=root%>/images/ico/chakan.gif" width="15"
													height="15" class="img_s">意见征询登记信息&nbsp;</a>
										</td>
										<td width="5"></td>
										<td>
											<a class="Operation" href="#"
												onclick="JavaScript:doYjfk();">&nbsp;<img
													src="<%=root%>/images/ico/search.gif" width="15"
													height="15" class="img_s">意见征询反馈&nbsp;</a>
										</td>
										<td width="5"></td>
									</s:if>
									<s:else>
										<td>
											<a class="Operation" href="#"
												onclick="JavaScript:doNewYjzx();">&nbsp;<img
													src="<%=root%>/images/ico/search.gif" width="15"
													height="15" class="img_s">征求意见&nbsp;</a>
										</td>
										<td width="5"></td>
									</s:else>
								</s:if>
							</s:if>
								<TD style="DISPLAY: block" id=toSave>
									<A class=Operation onclick=javascript:saveFormData(true);
										href="#"><IMG class=img_s alt=""
											src="/oa/frame/theme_red/images/guanbi.gif" width=15
											height=15>&nbsp;保存并关闭&nbsp;</A>
								</TD>
								<td width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<table width="100%" height="100%" border="0" cellspacing="0"
							cellpadding="00">
							<tr>
								<td width="70%" height="100%">
									<div id="silverlightControlHost"
										style="position:relative;height: 100%">
										<object data="data:application/x-silverlight-2,"
											type="application/x-silverlight-2" width="100%" height="100%"
											id="plugin">
											<param name="source"
												value="<%=path%>/FormReader/StrongFormReader.xap" />
											<param name="onError" value="onSilverlightError" />
											<param name="onLoad" value="onSilverlightLoad" />
											<param name="background" value="white" />
											<param name="minRuntimeVersion" value="4.0.50401.0" />
											<param name="autoUpgrade" value="true" />
											<a href="<%=path%>/detection/lib/Silverlight.exe"
												style="text-decoration:none"> <img
													src="<%=path%>/detection/images/SLMedallion_CHS.png"
													alt="Get Microsoft Silverlight" style="border-style:none" />
											</a>
										</object>
										<iframe id="_sl_historyFrame"
											style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>

		<OBJECT id=SignatureAPI
			classid="clsid:79F9A6F8-7DBE-4098-A040-E6E0C3CF2001"
			codebase="<%=root%>/common/goldgridOCX/iSignatureAPI.ocx#version=5,1,0,18"
			width=0 height=0 align=center hspace=0 vspace=0>
		</OBJECT>
	</body>
</html>

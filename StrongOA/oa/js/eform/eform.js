/**
 * 抽取电子表单的一些共性操作 1、加载电子表单 2、利用电子表单加载数据 3、保存电子表单
 * 
 */
var contextPath = "";// 定义获取调用此JS的上下文路径,从form的action属性获取
var FormInputOCX = null;// 电子表单控件
var formId = null;// 表单模板id
var newFormId = null;// 新表单模板id
var taskId = null;// 任务id
var instanceId = null;// 流程实例id,查看流程图时用到
var businessName = "";// 业务标题,从代办任务中获取,传递到流程审批页面,控件对应字段为WORKFLOWTITLE
var isDone = false;// 是否重复提交
//var map = new Map();
var formReader = null;
var TANGER_OCX_OBJ = null;
var workflowName = null;// 流程名
var workflowId = null;// 流程ID
var accName = null;// 附件名
var accID = null;// 附件ID
var needLoadSuggestion = true;
var readonly = "false";
var loadFinish = false;
var isInitOffice=false;//判断office控件是否已经加载
var officeVb=false;//默认是没有安装Office插件
var documentAct=false;//默认正文未被激活
$(document).ready(function() {
	var fullContextPath = $("form").attr("action");
	contextPath = fullContextPath.substring(0, fullContextPath.indexOf("!"));// 得到上下文路径
	formId = $("#formId").val();
	if (formId != "" && formId != "0") {
		taskId = $("#taskId").val();
		instanceId = $("#instanceId").val();
		if (!taskId)
			taskId = "";
		if (!instanceId)
			instanceId = "";
		// 初始化启动新流程的按钮
		var flagFlow = $("#pluginInfo").val();
		if (flagFlow && flagFlow != "[]") {
			$("#td_startworkflow").show();
			var jsonParams = eval('(' + flagFlow + ')');
			if (jsonParams.length == 1) {// 只挂接了一个流程
				$("#td_startworkflow")
						.html("<a class=\"button6\" href=\"javascript:showForm();\" title=\""+jsonParams[0].flowName+"\">"
								+ jsonParams[0].flowName.substring(0,4)+"..." + "&nbsp;</a>");
			}
		}
	}
	/* 处理已读和未读图标显示*/
		if(window.parent){
      		var parentWin = window.opener;
      		if(parentWin){
      			if($("#isReceivedIco"+taskId,parentWin.document).length>0){
					$("#isReceivedIco"+taskId,parentWin.document).attr({"src":scriptroot+"/oa/image/desktop/littlegif/received.gif"});	      				
      			}
      		}
      	}
});
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
	//loadFormTemplate();
	//修改表单加载方式 
	formReader.FormLoadCommand = "loadFormTemplate";
}


function SaveTemplateDataRequestCompleted(bResult, strResult, strDetail) {
    if (bResult) {
        //alert("成功：" + strResult);
    } else {
        alert("错误：" + strResult + "\n\t" + strDetail);
    }
}


//保存数据快照，用于生成pdf
function SaveTemplateData(){
    var formReader = getFormReader();			
    var retCode = formReader.SaveTemplateData("SaveTemplateDataRequestCompleted");
}

 function creatpdf(){
 	var formReader = getFormReader();			
    var retCode = formReader.SaveTemplateData("SaveTemplateDataRequestCompleted");
    //延迟0.2秒钟加载生成PDF方法
	setTimeout(function() { 
	 topdf(); 
	}, 500); 
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

//初始化Office控件
function iniOffice(){
	if(isInitOffice==false){
		 TANGER_OCX_OBJ = formReader.GetOfficeControl();
		 /**tj  下载安装office控件--start**/
		 var t = TANGER_OCX_OBJ;
		 if(t!=null){
			 //alert(t.readyState);			
			 if(t.readyState==0){
				 /*if(confirm("检测到你电脑没有安装office控件，是否现在去下载安装？（注：必须安装才能进行操作！）")){
					alert('123');										var width=screen.availWidth-10;
		  			var height=screen.availHeight-30;
		  			var ret=OpenWin(scriptroot+"/install.jsp",width, height);
		  			OpenWin(scriptroot+"/detection/lib/NtkoOfficeControlSetup.zip",width,height);
		  			window.close()						  		
		  			}else{
					 window.close()
				 	}*/
				 alert("检测到您电脑没有安装office控件，请点击页面上的提示进行控件安装。");
			 }
		 }
		 /**tj  下载安装office控件--end**/
			isInitOffice=true;
	}
}

// 装载模板成功后的回调函数
/**
 * 0 – 提示图标 1 – 询问图标 2 – 警告图标 3 – 错误图标
 */
function loadFormTemplateRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		//TANGER_OCX_OBJ = formReader.GetOfficeControl();
		iniOffice();
		//表单控件验证
		officeValidate(TANGER_OCX_OBJ);
		if (TANGER_OCX_OBJ) {
			if (document.getElementById("fileOperation")) {
				document.getElementById("fileOperation").style.display = "";
			}
		}
		// 加载模板数据
		if ($("#pkFieldValue").val() != "") {
			formReader.ClearLoadFormDataFilter();
			var hasTableName = formReader.SetLoadFormDataFilter($("#tableName").val(),
							   $("#pkFieldName").val() + "=?");
			//如果表单存在
			if(hasTableName){
					formReader.AddLoadFormDataFilterParameter($("#tableName").val(),
					$("#pkFieldName").val(), $("#pkFieldValue").val());
			}
			var actionUri = basePath + "senddoc/eFormTemplate.action";
			document.getElementById("formAction").value = "LoadFormData";
			if (formReader.LoadFormData(actionUri, "form",
					"loadFormDataRequestCompleted")) {
				// 方法调用成功
			} else {
				// 调用失败
			}
		} else {// 创建表单模板时
			initFieldSet();
		}
		loadFinish = true;
		if (typeof(AfterLoadFormTemplate) != "undefined") {
			AfterLoadFormTemplate();
		}
	} else {
		// 加载表单模板失败
		// 失败信息:strResult
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

function loadFormDataRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		// 加载模板数据成功
		// 做一些初始化设置
		initHideBgtTags();
		initial();
		initFieldSet();
		formReader = getFormReader();
		var tabSelectedName = "";
		if(formReader.TabSelectedName){
			tabSelectedName = formReader.TabSelectedName;
		}
//alert(tabSelectedName+"\nloadFormDataRequestCompleted:"+(tabSelectedName.indexOf("正文")>0  || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单"));
		if(tabSelectedName == "正文" || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单"){
			initWord();
		}
		// 添加TANGER_OCX_OBJ.ReadOnly==false 判断条件 作用：该文档必须不为只读状态
		if (taskId != "" && TANGER_OCX_OBJ != null && TANGER_OCX_OBJ != ""
				&& TANGER_OCX_OBJ.ActiveDocument != null) {// 办理时,痕迹保留
			try {
				if ($("#userName").val() != undefined) {
					with (TANGER_OCX_OBJ.ActiveDocument.Application) {
						UserName = $("#userName").val();
					}
				}
				TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = true;
			} catch (e) {
			}
		}
		//自动保存草稿
		autoSaveDraft();
	} else {
		// 加载模板数据失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

/**
 * 保存电子表单数据
 */
var isSaveReturn = false;
function saveFormData(isReturn) {
    if(!officeVb){
		alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
		return;
	}
	var currentId= $("#currentId").val();
	var currentId2= getCurrntId();
	if(currentId2=="-1"){/**添加了判断是否是当前用户  niwy*/
		alert("用户已退出系统，请关闭表单。");
	}else{
		if(currentId!=currentId2){
			alert("该表单处理人已经退出了该系统。")
			window.close();
	}else{
	saveformiframe();
	var validateResult = formReader.CheckFormData(true);
	if (!validateResult) {// 验证不通过
		return;
	}
	isSaveReturn = isReturn;
	var actionUri = basePath + "senddoc/eFormTemplate.action";
	document.getElementById("formAction").value = "SaveFormData";
	if (taskId == "") {
		state = "0";
	} else {
		state = "1";
	}
	document.getElementById("workflowState").value = state;
	
	$("#workflowName").val(encodeURIComponent($("#workflowName").val()));
	$("#transitionName").val(encodeURIComponent($("#transitionName").val()));
	
	if (formReader.SaveFormData(actionUri, "form",
			"saveFormDataRequestCompleted")) {
		// 方法调用成功
		if (typeof(CloseIframe) != "undefined") {
			CloseIframe();
		}
	} else {
		// 调用失败
	}
		}
	}
}


/**
*   表单控件验证,点击正文tab的office对象验证分别在回调函数内NewOfficeDocument与OpenOfficeDocument内进行判断,这里对没有安装或安装失败office控件的进行验证
*   没安装成功office插件的对象的值
* 1、officeControl都是object,因为office组件是以<object ...>方式嵌入了html内
* 2、officeControl.Value为undefined或officeControl是[object]但是officeControl[0]是undefined
* 3、officeControl.ActiveDocument为undefined
*/
function officeValidate(officeControl){
	if(officeControl==null){
		officeVb=true;
	}else{
		if( officeControl!=null && (typeof(officeControl.Value)=="undefined"||typeof(officeControl.ActiveDocument)=="undefined")){
			alert("OFFICE控件加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}else{
			//安装了office控件,但是没有激活(没有点击正文tab装载office)
			if(officeControl!=null && (officeControl.ActiveDocument==null&&officeControl.DocSize==0)){
				officeVb=true;
			}
		}
	}
}



/**
 * 提交到流程第一个环节
 */
function startProcessToFistNode() {
	// 保存PDF临时文件
	saveformiframe();
	var validateResult = formReader.CheckFormData(true);
	if (!validateResult) {// 验证不通过
		return;
	}
	// 兼容早期调用getTitle函数
	businessName = $("#businessName").val();
	if (businessName == "") {
		var formControl = formReader.GetFormControl($("#tableName").val(),
				"WORKFLOWTITLE");// 标题
		if (formControl) {
			businessName = formControl.Value;
		}
	}
	if (businessName) {
		businessName = businessName.replace(/\+/g, "%2B");
	}
	if (suggestionRequiredVerification($("#workflowFunction").attr("suggestionrequired"),
			$("#suggestion").val(), $("#suggestionStyle").val())) {// 审批意见必填验证
		// yanjian
		// 2012-02-28
		$("#suggestion").val(EE_EncodeCode($("#suggestion").val()));// modify
		// yanjian 2011-11-06 处理百分号问题 bug-2635
		$("#businessName").val(businessName);
		CloseIframe();
		var actionUri = basePath + "senddoc/sendDoc.action";
		document.getElementById("formAction").value = "startProcessToFistNode";
		if (formReader.SaveFormData(actionUri, "form",
				"saveFormDataAndWorkflowRequestCompleted")) {
			// 方法调用成功
		} else {
			// 调用失败
		}
	}
}
/**
 * 保存电子表单数据或者提交到流程第一个环节
 */
function toSaveOrToNext(isReturn){
	if(typeof($("#taskId")) =="undefined"){
		alert("当前页面上下文中不存在HTML元素id为taskId的关键内容，无法进行此操作！");
		return ;
	}
	var taskId = $("#taskId").val();
	if(taskId =="" || taskId=="undefined" || taskId=="null" ){// 满足此条件，提交数据
		startProcessToFistNode();
	}else{// 否则保存数据
		saveFormData(isReturn);
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

/**
 * 保存电子表单流程数据数据
 */
var objOpener = null;
function saveFormDataAndWorkflow(objOpenerWindow) {
	objOpener = objOpenerWindow;
	objOpener.close();// 关闭弹出窗口
	$("#suggestion").val(EE_EncodeCode($("#suggestion").val()));// modify
		businessName = $("#businessName").val();
	if (businessName == "") {
		var formControl = formReader.GetFormControl($("#tableName").val(),
				"WORKFLOWTITLE");// 标题
		if (formControl) {
			businessName = formControl.Value;
		}
	}
	if (businessName) {
		businessName = businessName.replace(/\+/g, "%2B");
	}
	if (businessName) {
		businessName = businessName.replace(/\%/g, "%25");
	}
	$("#businessName").val(businessName);
	// yanjian
	// 2011-11-06
	// 处理百分号问题
	// bug-2635
	var actionUri = basePath + "senddoc/sendDoc.action";
	document.getElementById("formAction").value = "handleNextStep";
	// var actionUri = basePath + "senddoc/sendDoc!handleNextStep.action";
	
	$("#workflowName").val(encodeURIComponent($("#workflowName").val()));
	$("#transitionName").val(encodeURIComponent($("#transitionName").val()));
	
	
	if (formReader.SaveFormData(actionUri, "form",
			"saveFormDataAndWorkflowRequestCompleted")) {
		// 方法调用成功
	} else {
		// 调用失败
	}
}

// 保存模板和流程数据完成事件
function saveFormDataAndWorkflowRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		returnValue=strResult;
		// 保存成功
		// objOpener.close();//关闭弹出窗口
		//非模态窗口获取父窗口对象
		var parentWin = window.opener;
		if (typeof(parentWin) != 'undefined') {
			if (typeof(parentWin.reloadPage) != "undefined") {
				try{					
					parentWin.reloadPage();					
				}catch(e){
					
				}
				window.close();// 关闭本窗口
			}else if (typeof(parentWin.closeIt) != "undefined") {
				try{					
					parentWin.closeIt();					
				}catch(e){
					
				}				
				window.close();// 关闭本窗口
			}else{
                try{//刷新工作区（content页面）的当前操作区
	                var idx = parentWin.parent.getSysConsole().getSelectedIndex();
	                if(idx!=-1){
	                    parentWin.parent.getSysConsole().frames(idx).location.reload();
	                }
                }catch(e){
                }
				window.close();// 关闭本窗口   
			}
		}else{
			//模态窗口获取父窗口对象
			parentWin = window.dialogArguments;
			if (typeof(parentWin) != 'undefined'){
				try{					
					parentWin.closeIt();					
				}catch(e){
					
				}				
				window.close();// 关闭本窗口
			}			
		}
		
	} else {
		// 保存失败
		if( strDetail && strDetail.indexOf("task instance") != -1 && strDetail.indexOf("is already ended")!=-1){
			alert("该任务已被其他人处理，请查阅详细处理记录！");
			window.close();
		}else{
			alert(strDetail);
			formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
			$("body").unmask();
			if (typeof(OpenIframe) != "undefined") {
				OpenIframe();
			}
		}
	}
}

//初始化自定义菜单
function initCustomMenus(){
	try{
		var myobj = TANGER_OCX_OBJ;	
		myobj.AddCustomMenu2(0,"协同办公(X)");
		myobj.AddCustomMenuItem2(0,0,-1,false,"保留痕迹",false,1000);
		//禁用"保留痕迹"菜单项
		if($("td#showMark").length == 0){
			myobj.EnableCustomMenuItem2(0,0,-1,false);
		}
		myobj.AddCustomMenuItem2(0,1,-1,false,"不保留痕迹",false,1001);
		if($("td#hideMark").length == 0){
			myobj.EnableCustomMenuItem2(0,1,-1,false);
		}
		myobj.AddCustomMenuItem2(0,2,-1,false,"-",true);
		myobj.AddCustomMenuItem2(0,3,-1,false,"显示痕迹",false,1003);
		if($("td#showRevisions").length == 0){
			myobj.EnableCustomMenuItem2(0,3,-1,false);
		}
		myobj.AddCustomMenuItem2(0,4,-1,false,"隐藏痕迹",false,1004);
		if($("td#hideRevisions").length == 0){
			myobj.EnableCustomMenuItem2(0,4,-1,false);
		}
		myobj.AddCustomMenuItem2(0,5,-1,false,"-",true);
		myobj.AddCustomMenuItem2(0,6,-1,false,"擦除痕迹",false,1006);
		if($("td#acceptRevisions").length == 0){
			myobj.EnableCustomMenuItem2(0,6,-1,false);
		}
		myobj.AddCustomMenuItem2(0,7,-1,false,"-",true);
		myobj.AddCustomMenuItem2(0,8,-1,false,"文件套红",false,1008);
		if($("td#addTemplate").length == 0){
			myobj.EnableCustomMenuItem2(0,8,-1,false);
		}
	}catch(e){
		
	}
}

/**
 * 打开一个空文档（2003格式），用于不同版本的WORD兼容 调用千航的OpenFromURL需指定路径为完整URL路径.
 * 
 * 邓志城 修改：2010年6月30日15:14:43 增加默认痕迹保留功能。
 */
function initWord() {
	if (!TANGER_OCX_OBJ) {// 校验是否存在WORD
		return;
	}
	var type = TANGER_OCX_OBJ.DocType;// 得到OFFICE类型.
	if (type == 0) {// 未初始化任何文档类型，默认初始化word
		if (TANGER_OCX_OBJ.GetOfficeVer() != 100) {// 如果客户端安装了OFFICE软件,则优先用OFFICE创建			
			type = 1;
		} else {// 如果未安装OFFICE,则验证客户端是否安装了WPS
			if (TANGER_OCX_OBJ.GetWPSVer() != 100) {// 安装了WPS软件,用WPS软件创建
//				type = 6;// WPS
				TANGER_OCX_OBJ.ShowTipMessage("信息提示", "很抱歉，您安装的是WPS软件，请正确安装OFFICE软件,请联系管理员。",false);
				return;
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
	if (loadFinish && ( tabSelectedName != "公文征求意见单" && tabSelectedName != "公文转办单")) {
		return;
	}
//alert(tabSelectedName +"\nloadFinish:"+loadFinish+"\nloadFormDataRequestCompleted:"+(tabSelectedName.indexOf("正文")>0  || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单"));
	if($("#pkFieldValue").val() != "" && (tabSelectedName == "正文" || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单")){
		var bussinessId = $("#tableName").val() + ";" + $("#pkFieldName").val() + ";" + $("#pkFieldValue").val();
		TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));	
	}else{
		if(tabSelectedName == '公文征求意见单'){
			TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty_yjzx.doc");
		}else if(tabSelectedName == '公文转办单'){
			TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty_gwzb.doc");
		}else{	
			if(tabSelectedName == "正文"){
				TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty.jsp?docType=" + type);
			}
		}
	}
	loadFinish = true;
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
	documentAct = true;
	if(typeof(officeControl.ActiveDocument)=="undefined"){
		officeVb = false;
		return;
	}
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
	}else if(documentType=="PowerPoint"){
		type=3;
	}else if(documentType=="Visio"){
		type=4;
	}else if(documentType=="Project"){
		type=5;
	}else if(documentType=="WPS"){
		type=6;
	}
	if(officeControl.GetWPSVer() != 100) {// 安装了WPS软件,不能用WPS软件创建
			officeControl.ShowTipMessage("信息提示", "很抱歉，您安装的是WPS软件，请正确安装OFFICE软件,请联系管理员。",false);
			officeVb=false;//WPS文档也不能提交
			return;	
	}
	if (type == 0) {// 未初始化任何文档类型，默认初始化word
		if (officeControl.GetOfficeVer() != 100) {// 如果客户端安装了OFFICE软件,则优先用OFFICE创建			
			type = 1;
		} else {// 如果未安装OFFICE,则验证客户端是否安装了WPS
			if (officeControl.GetWPSVer() != 100) {// 安装了WPS软件,用WPS软件创建
				//type = 6;// WPS
				officeControl.ShowTipMessage("信息提示", "很抱歉，您安装的是WPS软件，请正确安装OFFICE软件,请联系管理员。",false);
				officeVb=false;//WPS文档也不能提交
				return;
			} else {
				officeControl.ShowTipMessage("信息提示", "很抱歉，您没有正确安装OFFICE软件",false);
				officeVb=false;
				return;
			}
		}
	}			
	officeControl.Activate(false);// 被叫方拒绝接收呼叫
	formReader = getFormReader();
	var tabSelectedName = "";
	if(formReader.TabSelectedName){
		tabSelectedName = formReader.TabSelectedName;
	}
	if($("#pkFieldValue").val() != ""){
		var bussinessId = $("#tableName").val() + ";" + $("#pkFieldName").val() + ";" + $("#pkFieldValue").val();
		officeControl.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));	
	}else{
		
		if(tabSelectedName == '公文征求意见单'){
			officeControl.OpenFromURL(scriptroot + "/empty_yjzx.doc");
		}else if(tabSelectedName == '公文转办单'){
			officeControl.OpenFromURL(scriptroot + "/empty_gwzb.doc");
		}else{		
			officeControl.OpenFromURL(scriptroot + "/empty.jsp?docType=" + type);
		}
	}
	officeControl.WebFileName = '新建文档';
	TANGER_OCX_OBJ = officeControl;
	if (typeof(initWordAfter) != "undefined") {
		initWordAfter();
	}	
	officeVb=true;
}

/**申仪玲 20121023
 * 存在office对象时供表单调用的方法
 * @param plugin 银光插件对象
 * @param form  表单对象
 * @param officeControl 千航控件对象
 */
function OpenOfficeDocument(plugin, form, officeControl){
	documentAct = true;
	if(typeof(officeControl.ActiveDocument)=="undefined"){
		officeVb = false;
		return;
	}
	if(loadFinish == false){
		return;
	}
	formReader = getFormReader();
	var tabSelectedName = "";
	if(formReader.TabSelectedName){
		tabSelectedName = formReader.TabSelectedName;
	}
	var bussinessId = $("#tableName").val() + ";" + $("#pkFieldName").val() + ";" + $("#pkFieldValue").val();
	if(officeControl.GetWPSVer() != 100){// 安装了WPS软件,不能用WPS软件打开word正文
		officeControl.ShowTipMessage("信息提示", "很抱歉，您安装的是WPS软件，请正确安装OFFICE软件,请联系管理员。",false);
		officeVb=false;//WPS文档也不能提交
		return;
    }
    try{
	    if(officeControl.GetOfficeVer() != 100){
			officeControl.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));	
		}
	}catch(e){
		alert("word加载失败，请关闭该页面，尝试再次打开。\n如有问题，请联系系统管理员！\n");
		officeVb=false;//有问题文档都不能提交
		return;
	}
	if(officeControl.GetOfficeVer()==100 && officeControl.GetWPSVer()==100){
		officeVb=false;
		return;
	}
	if(officeControl.StatusCode!=0){
		officeVb=false;
		return;
	}
	officeControl.WebFileName = '新建文档';
	TANGER_OCX_OBJ = officeControl;
	//如果if成立，则说明有多个office控件
	//此时的readonly格式为"false,office1|false,office2|false,..."
	if(readonly != "false"){
		var r = readonly.split(",");
		for(i=1 ; i<r.length; i++){
			var a = r[i].split("|");
			//匹配office控件名
			if(tabSelectedName == a[0]){
				TANGER_OCX_OBJ.SetReadOnly(a[1], "");
			}
		}
	}else{
		TANGER_OCX_OBJ.SetReadOnly(readonly, "");// 是否只读
	}
	if (typeof(initWordAfter) != "undefined") {
		initWordAfter();
	}
	officeVb=true;
}

//加载完word之后调用该函数
function initWordAfter(){	
	initCustomMenus();
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
 * 保存草稿 用于公文流转过程中，将有痕迹的公文 以附件的形式保存草稿到附件中.
 */
function saveDraft() {
	if (!TANGER_OCX_OBJ) {// 校验是否存在WORD
		alert("未找到OFFICE控件.");
		return;
	}
	$.post(contextPath + "!checkDraft.action", {
				id : $("#pkFieldValue").val()
			}, function(retCode) {
				 if(retCode == "0" ){//草稿不存在
					 if(!documentAct){
							alert("请先点击Word正文,再进行保存草稿操作。");
							return false;
						}
				 }
				 
				var actionUri = basePath + "senddoc/sendDoc.action";
				document.getElementById("formAction").value = "saveDraft";
				document.getElementById("workflowType").value = retCode;
				if (formReader.SaveFormData(actionUri, "form",
						"saveDraftRequestCompleted")) {
					// 方法调用成功
				} else {
					// 调用失败
				}
			});
}

function saveDraftRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		// 保存成功
		// var data = strResult.split(";");
		loadFormTemplate();
	} else {
		// 保存失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

// 处理状态（查看流程图）
function workflowView(flag) {
	var width = screen.availWidth - 10;;
	var height = screen.availHeight - 30;
	var ReturnStr = WindowOpen(contextPath + "!PDImageView.action?instanceId="
					+ $("#instanceId").val() + "&flag=" + flag, width, height,
			"查看流程图");
}

// 查看办理记录
function annal() {
	//var taskId = $("#taskId").val();
	//OpenWindow(contextPath + "!annallist.action?taskId=" + taskId, 500, 300,
	//		window);
	var width = screen.availWidth - 10;;
	var height = screen.availHeight - 30;
	var taskId = $("#taskId").val();
	OpenWindow(contextPath + "!annallist.action?taskId=" + taskId, width,height,
			window);
}

// 取回任务
function fetchTask() {
	if(!officeVb){
		alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
		return;
	}
	//正文被激活 且office控件不应为空内容
	if(documentAct && TANGER_OCX_OBJ == ""){
		if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
			if(doNextBefore() == false){
				return;
			}else{
				alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
				return;
			}
		}else{
			alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}
	}
	if (confirm("取回此任务，确定？")) {
		$.post(contextPath + "!fetchTask.action", {
					taskId : taskId
				}, function(data) {
					if (data == "error") {
						alert("对不起，出错了，请重试或与管理员联系。");
					} else {
						switch (data) {
							case '0' : {
								alert('该流程实例已经结束，无法取回');
							}
								break;
							case '1' : {
								alert('该任务的后续任务已经被处理或正在处理，不能取回。');
							}
								break;
							case '2' : {
								alert('任务已成功取回');
								goBack();
							}
						}
					}
				});
		var workfname = $("#workflowName").val();
		var bussinessId = $("#bussinessId").val();
		if(workfname=="收文办件登记"||workfname=="呈阅件登记"){
			$.post(contextPath + "!isdjfb.action", {
						taskId : taskId,workflowName :workfname,bussinessId : bussinessId
					}, function(data) {
						if (data == "error") {
							alert("对不起，出错了，请重试或与管理员联系。");
						}
					});
		}
	}
}

/**
 * 启动新流程,如果当前任务节点只挂接了一个流程 页面直接跳转到目标表单页面，并将父流程数据复制到子例程中。
 * 如果当前节点挂接了多个流程和表单，则弹出表单页面供用户选择。 作者：邓志城 2010年3月23日15:07:25
 */
function showForm() {
	var flagFlow = $("#pluginInfo").val();
	if (flagFlow && flagFlow != "") {
		var jsonParams = eval('(' + flagFlow + ')');
		if (jsonParams.length == 1) {// 只挂接了一个流程
			isDone = true;
			newFormId = jsonParams[0].formId;
			//"0"表示流程启动表单
			if(newFormId == "0"){
				newFormId = $("#formId").val();
			}
			//启动新流程 新表单id
			$("#newFormId").val(newFormId);
			workflowName = jsonParams[0].flowName;
			workflowId = jsonParams[0].flowId;
			// alert(newFormId+"********"+workflowId);
			var instanceId = $("#instanceId").val();
			var actionUri = basePath + "senddoc/sendDoc.action?newId=" + newFormId+"&instanceId="+instanceId;
			document.getElementById("formAction").value = "copyData";
			if (formReader.SaveFormData(actionUri, "form",
					"copyDataRequestCompleted")) {
				// 方法调用成功
			} else {
				// 调用失败
			}
		} else {// 挂接了多个流程
			var ret = OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/showform.jsp", "300", "200", window);
			if (ret) {
				if (ret != "") {
					var result = ret.split(",");
					
					newFormId = result[0];
					//"0"表示流程启动表单
					if(newFormId == "0"){
						newFormId = $("#formId").val();
					}
					//启动新流程 新表单id
					$("#newFormId").val(newFormId);
					workflowName = result[1];
					workflowId = result[2];
					var actionUri = basePath + "senddoc/sendDoc.action";
					document.getElementById("formAction").value = "copyData";
					if (formReader.SaveFormData(actionUri, "form",
							"copyDataRequestCompleted")) {
						// 方法调用成功
					} else {
						// 调用失败
					}
				}
			}
		}
	} else {
		alert("此节点未设置新流程信息！");
		return;
	}
}

function copyDataRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		// 保存成功
		var code = strResult;
		isDone = false;
		if (code == "-1") {
			alert("数据复制过程中出现异常，请检查流程设计和表单字段映射是否正确。");
			return;
		}
		var args = code.split(";");
		var tableName = args[0];
		var pkFieldName = args[1];
		var pkFieldValue = args[2];
		var Width = screen.availWidth - 10;
		var Height = screen.availHeight - 30;
		// alert(newFormId); workflowId
		var parentinstanceId = args[3];
		var ReturnStr = OpenWindow(contextPath + "!input.action?formId="
						+ newFormId + "&pkFieldName=" + pkFieldName
//						+ newFormId + "&parentInstanceId=" + parentinstanceId + "&pkFieldName=" + pkFieldName
						+ "&pkFieldValue=" + pkFieldValue + "&tableName="
						+ tableName + "&workflowName="
						+ encodeURI(encodeURI(workflowName)), Width, Height,
				window);
		//启动新流程时保存父表单关联信息
		if(ReturnStr!=""&&ReturnStr!="OK"&&ReturnStr!="ok"&&ReturnStr!="undefined"&&ReturnStr!=undefined){
			var subFormId = newFormId;
			var instanceId = ReturnStr;
			var bussId = tableName+";"+pkFieldName+";"+pkFieldValue;
			//alert("copyDataRequestCompleted \nsubFormId:"+subFormId+"\nparentinstanceId:"+parentinstanceId+"\ninstanceId:"+instanceId+"\nbussId:"+bussId);
			startNewinstanceSetPersonDemo(subFormId,instanceId,parentinstanceId,bussId);
		}
	} else {
		// 保存失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

/**
 * author:luosy 2013-7-18
 * description: 启动新流程是设置父表单数据
 * modifyer:
 * description:
 * @return
 */
function startNewinstanceSetPersonDemo(subFormId,instanceId,parentinstanceId,bussId){
	$.ajax({
	  		type:"post",
	  		dataType:"json",
	  		async:false,
	  		url:contextPath+"!startNewinstanceSetPersonDemo.action?timeStamp="+new Date(),
	  		data:"subFormId="+subFormId+"&instanceId="+ instanceId+"&parentinstanceId="+ parentinstanceId+"&bussId="+ bussId,
	  		success:function(retCode){
				if (retCode == "0") {
					result = false;
				} 
	  		}
	  	});
}

// 初始化意见设置
function initFieldSet() {
	// 加载意见
	if ($("#instanceId").val() != "" && $("#instanceId").val() != "null") {
		insIdAndTasId = $("#instanceId").val() + "," + $("#taskId").val();
		// alert(insIdAndTasId);
		var actionUri = basePath + "senddoc/eFormTemplate.action";
		formReader.LoadAuditOpinion(actionUri, insIdAndTasId, "");
	}else{
		//yanjian 2013-5-14 10:54 添加加载拟稿环节意见 \WEB-INF\jsp\senddoc\sendDoc-input.jsp方法AfterLoadFormData()
		if(typeof(AfterLoadFormData) != 'undefined'){
			AfterLoadFormData();
		}
	}
	// 设置属性
	$.getJSON(contextPath + "!getInstro.action?timeStamp=" + new Date(), {
				taskId : $("#taskId").val(),
				instanceId : $("#instanceId").val(),
				workflowName:EE_EncodeCode($("#workflowName").val())
			}, function(json) {
				var control = null;
				var fieldName = json.fieldName;// 绑定的意见控件名称
				if (fieldName != null && fieldName != "") {
					var AuditOptionControl = formReader
							.GetFormControl(fieldName);
					// 这里设置意见控件的笔形图片显示
					var suggestionStyle = $("#suggestionStyle").val(); // 意见输入模式为笔形图标的时候才显示笔形
					if (suggestionStyle != "1") {
						if (AuditOptionControl != null
								&& AuditOptionControl != "") {
							AuditOptionControl.ShowAddButton = true;
						}
					}
				}
				// 设置在任务节点设置的属性应用到表单中
				var otherField = json.otherField;
				//严建 2012-05-31 当文未进入流程时，支持流程设计模型针对表单控件进行设置
				//if (otherField && $("#taskId").val() != null
					//	&& $("#taskId").val() != "") {
				if (otherField) {
					var readonlyTemp = readonly;
					$.each(otherField, function(i, field) {
						// 表单卡是否可见
						if (field.SetFormTabVisibility) {
							var res = field.SetFormTabVisibility;
							if (res == "False") {
								formReader.SetFormTabVisibility(
										field.fieldName, false);
							}
						}
						if (field.fieldName != null && field.fieldName != "") {
							control = formReader
									.GetFormControl(field.fieldName);
						}
						try{
							// 通过formReader.GetFormControl(field.fieldName)无法得到Office控件
							// dengzc 2012年2月15日18:18:06
							// 原因是在流程设计中保存后,又去修改了Office控件名称为“正文”,原节点设计中控件名称已经存储为EFOffice_84
							if (field.SetReadOnly) {// 有这个属性,说明是OFFICE控件
								// 得到千航Office控件
								//alert("field.SetReadOnly:"+field.SetReadOnly+"\nfield.fieldName:"+field.fieldName)
								control = formReader.GetOfficeControl(field.fieldName);
								control.FileNew = field.FileNew;// 是否允许新建
								control.FileSave = field.FileSave;// 是否允许保存
								control.FilePrint = field.FilePrint;// 是否允许打印
								control.FileOpen = field.FileOpen;// 是否允许打开
								control.FileClose = field.FileClose;// 是否允许关闭
								control.FileSaveAs = field.FileSaveAs;// 是否允许另存为
								//临时存放是否只读设置
								readonlyTemp = field.SetReadOnly;
								readonly = readonly + "," + field.fieldName + "|" + field.SetReadOnly;
								control.SetReadOnly(field.SetReadOnly, "");// 是否只读
							}
							if (control != null) {
								if (field.ReadOnly) {// 控件是否只读
									control.SetProperty("ReadOnly", field.ReadOnly);
								}
								if (field.Visible) {// 控件是否可显示
									control.SetProperty("Visible", field.Visible);
								}
								if (field.Enabled) {// 控件是否可用
									control.SetProperty("Enabled", field.Enabled);
								}
								if (field.Required) {// 控件是否必填
									var Required = field.Required;
									if (Required == "N") {
										Required = false;
									} else {
										Required = true;
									}
									// yanjian 2011-10-03 14:07 解决收文流程走到子流程，左下脚报错
									control.SetProperty("Required", Required);
									// control.Required = Required;
									// control.SetProperty("Required",field.Required);
								}
								// 是否允许添加附件
								if (field.IsAllowAdd) {
									control.SetProperty("IsAllowAdd",
											field.IsAllowAdd);
								}
								// 是否允许删除附件
								if (field.IsAllowDelete) {
									control.SetProperty("IsAllowDelete",
											field.IsAllowDelete);
								}
								if(field.IsReadOnly){								
									control.SetProperty("ReadOnly",
											field.IsReadOnly);
								}
							} else {// 找不到指定控件,可能是HTML控件,这里用JQUERY来捕捉对象
								if (document.getElementById(field.fieldName)) {
									$("#" + field.fieldName).css("display",
											field.visible);
								}
							}
							
						}catch(e){}
					});
					//如果如下if成立，则说明只有一个office控件
					if(readonly.indexOf("|") == readonly.lastIndexOf("|")){
						readonly = readonlyTemp;
					}
				}
			});
			// 加载表单模板属性成功之后调用
		if (typeof(initialHtml) != "undefined") {
			initialHtml();
		}
}

// 定义一个空函数,由调用此JS的页面重载
function initial() {
}

// 加载意见完成之后执行
function afterLoadInfomation() {

}

/**
 * 提交下一办理人 1、给出操作成功与否的提示 2、页面跳转
 * doNext()函数定义在BaseManager#getOperationHtml(taskId)中
 */
function doNext(objThis) {
	if(!officeVb){
		alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
		return;
	}
	//正文被激活 且office控件不应为空内容
	if(documentAct && TANGER_OCX_OBJ == ""){
		if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
			if(doNextBefore() == false){
				return;
			}else{
				alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
				return;
			}
		}else{
			alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}
	}
	var currentId= $("#currentId").val();
	var currentId2= getCurrntId();
	if(currentId2=="-1"){/**添加了判断是否是当前用户  niwy*/
		alert("用户已退出系统，请关闭表单。");
	}else{
		if(currentId!=currentId2){
			alert("该表单处理人已经退出了该系统")
			window.close();
		}else{
			if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
				if(doNextBefore() == false){
					return;
				}
			}
	// 保存PDF临时文件
	saveformiframe();

	var height = 473;// screen.availHeight-50;
	var width = 600;// screen.availWidth/2;
	var returnValue = "-1";// 提交流程以后的返回值
	// 验证表单必填项是否已经输入了
	var validateResult = formReader.CheckFormData(true);
	if (!validateResult) {// 验证不通过
		return;
	}
	//保存草稿
	if(!draftRequiredVerification()){
		alert("对不起，请先保存草稿！");
		flagnext = false;
		return;
	}
	var isSuggestionButton = $("#toNext").attr("isSuggestionButton");// 是否快捷办理按钮
	if (document.getElementById('iframe_nextstep')) {// 弹出窗口模式时,此对象不存在
		// 文件跟踪
		var genzon = document.getElementById('iframe_nextstep').contentWindow.document
				.getElementById("genzong");
		if (genzon.checked == true) {
			$("#isGenzong").val('1');
		} else {
			$("#isGenzong").val('0');
		}
		document.getElementById('iframe_nextstep').contentWindow.document
				.getElementById("isGenzong").value = $("#isGenzong").val();

		
		if (!document.getElementById('iframe_nextstep').contentWindow
				.doPreSubmit()) {// 工作流选择验证
			OpenIframe();
			return;
		}
		if (isSuggestionButton == "1") {
			if (typeof(objThis) != "undefined") {
				if(typeof($("#iframe_nextstep").contents().find("suggestion")) != "undefined"){
					$("#iframe_nextstep").contents().find("suggestion").val(objThis.buttonName);
				}
				var suggestion = document.getElementById("suggestion").value;
				if(suggestion == null || suggestion == ""){
					
					document.getElementById("suggestion").value = objThis.buttonName;
				}
			}
		}
	}
	var isPopWin = $("#toNext").attr("isPop");// 是否弹出窗口处理
	var flagnext = true;
	if(!draftRequiredVerification()){
		alert("对不起，请先保存草稿！");
		flagnext = false;
		return;
	}
	if(flagnext){
			$("body").mask("");//数据提交之后 遮盖整个办理界面
		// 兼容早期调用getTitle函数
						businessName = $("#businessName").val();
						//if (businessName == "") { //modify yanjian 2012-07-06 09:56 去除该判断
							var formControl = formReader.GetFormControl(
									$("#tableName").val(), "WORKFLOWTITLE");// 标题
							if (formControl) {
								businessName = formControl.Value;
							}else{
								if(!confirm("该表单标题绑定的数据字段不是【WORKFLOWTITLE】,将导致流程标题无法正常显示，是否要继续办理？")){
									$("body").unmask();
									return;
								}
							}
						//}
						if (businessName) {
							businessName = businessName.replace(/\+/g, "%2B");
							businessName = encodeURIComponent(encodeURIComponent(businessName));
						}
						if (isPopWin == "1") {
							var daiBan = 0;
							if($("#daiBan").length >0){
								daiBan = 1;
							}
							
							returnValue = OpenWindow(
									contextPath
											+ "!nextstep.action?formId="
											+ formId
											+ "&workflowType="
											+ $("#workflowType").val()
											+ "&taskId="
											+ taskId
											+ "&instanceId="
											+ instanceId
											+ "&daiBan="
											+ daiBan
											+ "&businessName="
											+ encodeURI(encodeURI(businessName))
											+ "&workflowName="
											+ encodeURI(encodeURI($("#workflowName")
													.val()))
											+ "&suggestion="
											+ EE_EncodeCode($("#suggestion").val())
											+"&suggestionrequired="		//弹出窗口模式下控制审批意见不能为空
											+encodeURI(encodeURI($("#workflowFunction")
												.attr("suggestionrequired"))), width, height,
									window);
									if( returnValue != "OK"){
										$("body").unmask();
									}
						} else {
							// 审批意见必填验证
							// yanjian
							// 2012-02-28
							if (suggestionRequiredVerification(
									$("#workflowFunction").attr("suggestionrequired"),
									$("#suggestion").val(),
									$("#suggestionStyle").val())) {
								$("#suggestion").val(EE_EncodeCode($("#suggestion").val()));//modify
								// yanjian 2011-11-06 处理百分号问题 bug-2635
								$("#businessName").val(businessName);
								$("#handlerMes").val(encodeURIComponent($("#handlerMes").val()));
								CloseIframe();
								var actionUri = basePath
										+ "senddoc/sendDoc.action";
								document.getElementById("formAction").value = "handleNextStep";
								
								
								$("#workflowName").val(encodeURIComponent($("#workflowName").val()));
								$("#transitionName").val(encodeURIComponent($("#transitionName").val()));
								
								
								if (formReader
										.SaveFormData(actionUri, "form",
												"saveFormDataAndWorkflowRequestCompleted")) {
									// 方法调用成功
								} else {
									// 调用失败
								}
							}
						}
					}
	// ------ End ------------
		}
	}
}

/**
 * @author yanjian 2012-07-04 09:54 是否保存了草稿验证
 * 
 */
function draftRequiredVerification(){
	var result = true;
	var draftRequired = false;
	// 增加对是否保存了草稿验证 dengzc 2011年7月2日11:05:43
	if (document.getElementById("tr_saveDraft")) {// 存在保存草稿按钮
		if (document.getElementById("draftRequired")) {// 草稿为必填项
			draftRequired = true;
		}
	}
	if (draftRequired) {
		$.ajax({
		  		type:"post",
		  		dataType:"json",
		  		async:false,
		  		url:contextPath+"!checkDraft.action?timeStamp="+new Date(),
		  		data:"id="+$("#pkFieldValue").val()+"&fileName="+ encodeURI("草稿.doc"),
		  		success:function(retCode){
					if (retCode == "0") {// word存在, 草稿不存在
						result = false;
					} 
		  		}
		  	});
	}
	return result;
}
/**
 * @author yanjian 2012-02-28 12:32 审批意见必填验证
 * @param suggestionrequired
 *            是否进行必填验证
 * @param suggestion
 *            审批意见
 * @param suggestionStyle
 *            意见录入方式
 * 
 */
function suggestionRequiredVerification(suggestionrequired, suggestion,
		suggestionStyle) {
	var result = true;
	if("1" == suggestionrequired){//启用必填验证
		if ("1" == suggestionStyle) {//意见录入方式不是笔形
			if ("" == suggestion) {
				result = false;
			 }
		}else{	//笔形意见录入方式
			if(taskId == ""){//未进入流程
				if($("#pkFieldValue").val() == ""){//新建流程
					if ("" == suggestion) {
						result = false;
					}
				}else{	//草稿中
					//校验是否填写了意见
					$.ajax({
				  		type:"post",
				  		dataType:"json",
				  		async:false,
				  		url:scriptroot+"/senddoc/sendDocWorkflow!findSuggestionInDraftByBid.action?timeStamp="+new Date(),
				  		data:"bussinessId="+$("#pkFieldValue").val()+"&userId="+initUserId(),
				  		success:function(json){
							var status = json.status;
			 				if(status == "-1"){
			 					alert("对不起，系统发生错误，请与管理员联系。");
			 				} else if(status == "0"){//找到了记录
			 				} else if(status == "1"){//未找到记录
			 					result = false;
			 				}
				  		}
				  	});
				}
			}else{	//进入流程
				//校验是否填写了意见
				$.ajax({
				  		type:"post",
				  		dataType:"json",
				  		async:false,
				  		url:scriptroot+"/senddoc/sendDocWorkflow!findSuggestionInDraftByBid.action?timeStamp="+new Date(),
				  		data:"bussinessId="+taskId,
				  		success:function(json){
							var status = json.status;
			 				if(status == "-1"){
			 					alert("对不起，系统发生错误，请与管理员联系。");
			 				} else if(status == "0"){//找到了记录
			 				} else if(status == "1"){//未找到记录
			 					result = false;
			 				}
				  		}
				  	});
			}
		}
	}
	if(!result){
		alert("请先填写审批意见。");
		$("body").unmask();
	}
	return result;
}

/**
 * @author yanjian 2012-02-28 12:59 获取办理模式
 * @return (1)"isPop":弹出模式 (2)"isMenuButton":菜单模式 (3)"isOpen":展开模式
 *         (4)"isSuggestionButton":快捷模式 (5)"isNomal":正常模式
 */
function getDoNextModel() {
	if ("1" == $("#toNext").attr("isPop")) {// 弹出模式
		return "isPop";
	}
	if ("1" == $("#toNext").attr("isMenuButton")) {// 菜单模式
		return "isMenuButton";
	}
	if ("1" == $("#toNext").attr("isOpen")) {// 展开模式
		return "isOpen";
	}
	if ("1" == $("#toNext").attr("isSuggestionButton")) {// 快捷模式
		return "isSuggestionButton";
	}
	return "isNomal";// 正常模式
}

/**
 * 退回上一办理人 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现系统异常返回-3,一般是电子表单数据未获取到引起 4、出现异常返回-2；
 */
function doBackSpacePrev(buttonName) {
    if(!officeVb){
		alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
		return;
	}
	//正文被激活 且office控件不应为空内容
	if(documentAct && TANGER_OCX_OBJ == ""){
		if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
			if(doNextBefore() == false){
				return;
			}else{
				alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
				return;
			}
		}else{
			alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}
	}
	if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
		if(doNextBefore() == false){
			return;
		}
	}
	saveformiframe();
	//退回上一步，判断上一步是否是不可退回的节点，如果是，不允许退文。
	var taskId=$("#taskId").val();
	$.post(scriptroot + "/senddoc/sendDoc!checkIsBackspace.action", {
		taskId : taskId
	}, function(data) {
		var datas=data.split(";");
		if(datas[0]=="1"){
			alert("【"+datas[1]+"】节点设置是不可退回的节点,不允许退文。");
			return;
		}else{
			var message = "退回给上一办理人员，确定？";
			if (buttonName != "" && buttonName != null && buttonName != "null") {
				message = buttonName + "，确定？";
			}
			if (confirm(message)) {
				var ret = OpenWindow(
						scriptroot
								+ "/fileNameRedirectAction.action?toPage=workflow/initback.jsp",
						410, 300, window);
				if (ret) {
					var actionUri = basePath + "senddoc/sendDoc.action";
					document.getElementById("formAction").value = "backlast";
					// document.getElementById("suggestion").value = encodeURI(ret);
					document.getElementById("suggestion").value = EE_EncodeCode(ret);// modify
					// yanjian
					// 2011-11-06
					// 处理百分号问题
					// bug-2635
					if (formReader.SaveFormData(actionUri, "form",
							"doBackSpacePrevRequestCompleted")) {
						// 方法调用成功
					} else {
						// 调用失败
					}
				}
			}
		}
	});

}

/**
 * 退回上一办理人 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现系统异常返回-3,一般是电子表单数据未获取到引起 4、出现异常返回-2；
 * 中间要填写退文单
 * xlj  肖利建
 */
function doBackSpacePrevByxlj(info) {
	url = scriptroot + "/sends/transDoc!tuiwen.action";
	//var ret = OpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:760px',info);
	var ret = showModalDialog(url,info,"dialogWidth:600px;dialogHeight:760pxpt;"+
											"status:no;help:no;scroll:auto;");
	if (ret) {
		var actionUri = basePath + "senddoc/sendDoc.action";
		document.getElementById("formAction").value = "backlast";
		document.getElementById("suggestion").value = EE_EncodeCode(ret);// modify
		// yanjian
		// 2011-11-06
		// 处理百分号问题
		// bug-2635
		if (formReader.SaveFormData(actionUri, "form",
				"doBackSpacePrevRequestCompleted")) {
			// 方法调用成功
		} else {
			// 调用失败
		}
	}
}

// 退回上一办理人、退回、驳回、指派、指派返回的回调事件
function doBackSpacePrevRequestCompleted(bResult, strResult, strDetail) {
	if (bResult) {
		// 保存成功
		goBack();// 调用页面上提供的函数
	} else {
		// 保存失败
		formReader.ShowMessageBox("出错啦", "很抱歉,系统出现错误,请与管理员联系.", strDetail, 3);// 0:提示
	}
}

/**
 * 退回任务 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现系统异常返回-3,一般是电子表单数据未获取到引起 4、出现异常返回-2；
 */
function doBackSpace(buttonName) {
	if(!officeVb){
		alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
		return;
	}
	//正文被激活 且office控件不应为空内容
	if(documentAct && TANGER_OCX_OBJ == ""){
		if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
			if(doNextBefore() == false){
				return;
			}else{
				alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
				return;
			}
		}else{
			alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}
	}
	if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
		if(doNextBefore() == false){
			return;
		}
	}
	saveformiframe();
	var width = screen.availWidth - 10;
	var height = screen.availHeight - 30;
	/*
	 * var ReturnStr=OpenWindow(scriptroot +
	 * "/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=return",
	 * width, height, window);
	 */
	var width = screen.availWidth - 10;
	var height = screen.availHeight - 30;
	var ReturnStr = OpenWindow(
			scriptroot
					+ "/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-backspacestatus.jsp?instanceId="
					+ $("#instanceId").val() + "&taskId=" + $("#taskId").val(),
			width, height, window);
	if (ReturnStr) {
		var ret = OpenWindow(
				scriptroot
						+ "/fileNameRedirectAction.action?toPage=workflow/initback.jsp",
				400, 350, window);
		if (ret) {
			var actionUri = basePath + "senddoc/sendDoc.action";
			document.getElementById("formAction").value = "back";
			// document.getElementById("suggestion").value = encodeURI(ret);
			document.getElementById("suggestion").value = EE_EncodeCode(ret);// modify
			// yanjian
			// 2011-11-06
			// 处理百分号问题
			// bug-2635
			document.getElementById("bussinessId").value = ReturnStr;// 退回的节点id,指定退回用户id
			if (formReader.SaveFormData(actionUri, "form",
					"doBackSpacePrevRequestCompleted",false)) {
				// 方法调用成功
			} else {
				// 调用失败
			}
		}
	}
}

/**
 * 驳回任务 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现系统异常返回-3,一般是电子表单数据未获取到引起 4、出现异常返回-2；
 */
function doOverRule(buttonName) {
    if(!officeVb){
		alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
		return;
	}
	//正文被激活 且office控件不应为空内容
	if(documentAct && TANGER_OCX_OBJ == ""){
		if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
			if(doNextBefore() == false){
				return;
			}else{
				alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
				return;
			}
		}else{
			alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}
	}
	if(typeof(doNextBefore) != "undefined"){//表单数据提交之前的进行数据处理操作
		if(doNextBefore() == false){
			return;
		}
	}
	saveformiframe();
	var width = screen.availWidth - 10;;
	var height = screen.availHeight - 30;
	var ReturnStr = OpenWindow(
			scriptroot
					+ "/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-overrulestatus.jsp?taskId="
					+ taskId + "&type=bohui",
			width, height, window);
	/*
	var ReturnStr = OpenWindow(
			scriptroot
					+ "/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="
					+ taskId + "&type=bohui", width, height, window);
	*/
	if (ReturnStr) {
		var ret = OpenWindow(
				scriptroot
						+ "/fileNameRedirectAction.action?toPage=workflow/initback.jsp",
				400, 300, window);
		if (ret) {
			var actionUri = basePath + "senddoc/sendDoc.action";
			document.getElementById("formAction").value = "bohui";
			// document.getElementById("suggestion").value = encodeURI(ret);
			document.getElementById("suggestion").value = EE_EncodeCode(ret); // modify
			// yanjian
			// 2011-11-06
			// 处理百分号问题
			// bug-2635
			document.getElementById("bussinessId").value = ReturnStr;// 退回的节点id
			if (formReader.SaveFormData(actionUri, "form",
					"doBackSpacePrevRequestCompleted",false)) {
				// 方法调用成功
			} else {
				// 调用失败
			}
		}
	}
}

/**
 * 查看流程图
 * 
 * @param contextPath
 *            上下文路径 返回值如：/StrongOA/work/work
 * @param instanceId
 *            流程实例id
 */
function viewPDImage() {
	var width = screen.availWidth - 10;
	var height = screen.availHeight - 30;
	var ReturnStr = OpenWindow(contextPath + "!PDImageView.action?instanceId="
					+ instanceId, width, height, window);
}

/**
 * 任务指派,返回操作结果 1、操作成功返回0； 2、任务实例不存在返回-1； 3、业务数据传输错误返回-3； 4、出现异常返回-2；
 */
function assignTask() {
	var url = scriptroot
			+ "/workflowRun/action/runUserSelect!input.action?dispatch=reassign&nodeId=0&taskId="
			+ taskId;
	var userstr = OpenWindow(url, 420, 450, window);
	// 用户ID|用户名称,指派是否需要返回（0：否、1：是）
	if (userstr != null && userstr != '') {
		var actionUri = basePath + "senddoc/sendDoc.action";
		document.getElementById("formAction").value = "reAssign";
		document.getElementById("suggestion").value = encodeURI(userstr);
		if (formReader.SaveFormData(actionUri, "form",
				"doBackSpacePrevRequestCompleted")) {
			// 方法调用成功
		} else {
			// 调用失败
		}
	}
}
/**
 * @author	yanjian 2012-03-09
 * 
 * 主办变更,返回操作结果 1、操作成功返回0； 2、任务实例不存在返回-1； 3、业务数据传输错误返回-3； 4、出现异常返回-2；
 */
function changeMainActor(){
	var url = scriptroot
			+ "/workflowRun/action/runUserSelect!input.action?dispatch=reassign&allowChangeMainActor=1&nodeId=0&taskId="
			+ taskId;
	var userstr = OpenWindow(url, 420, 450, window);
	// 用户ID|用户名称,指派是否需要返回（0：否、1：是）
	if (userstr != null && userstr != '') {
		var actionUri = basePath + "senddoc/sendDoc.action";
		document.getElementById("formAction").value = "reAssign";
		//动态添加是否允许主办变更信息
		$("#formAction").after("<input id=\"allowChangeMainActor\" name=\"allowChangeMainActor\" value=\"1\" type=\"hidden\" />"); 
		document.getElementById("suggestion").value = encodeURI(userstr);
		if (formReader.SaveFormData(actionUri, "form",
				"doBackSpacePrevRequestCompleted")) {
			// 方法调用成功
		} else {
			// 调用失败
		}
	}
}

/**
 * 任务指派返回 1、操作成功返回0； 2、任务实例不存在返回-1； 3、出现异常返回-2；
 */
function doAssignAndReturn() {
	if (!confirm("返回给工作指派人员，确定？")) {
		return;
	}
	var actionUri = basePath + "senddoc/sendDoc.action";
	document.getElementById("formAction").value = "returnReAssign";
	if (formReader.SaveFormData(actionUri, "form",
			"doBackSpacePrevRequestCompleted")) {
		// 方法调用成功
	} else {
		// 调用失败
	}
}

/**
 * 打印处理单
 */
function doPrintForm() {
	// var width=screen.availWidth-10;
	// var height=screen.availHeight-30;
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
                        openPrintDoc(templateInfo[0],fid);
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
 * 恢复会签挂起的任务
 */
function resumeConSignTask() {
	if (taskId != "") {
		$.post(contextPath + "!resumeConSignTask.action?taskId=" + taskId);
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
					+ type, 1000, 500, window);
	if (ret)
		return ret;
}
// ----------------------------初始化相关函数
/**
 * 初始化表单
 * 
 * @return {表单id}
 */
function initForm() {
	return $("#formId").val();
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
 * 得到原表单数据
 * 
 * @author 申仪玲
 * @return {personDemo}
 */
function getPersonDemo() {
	return $("#personDemo").val();
}

/**
 * 是否要显示查看原表单按钮
 * 
 * @author 严建 2011-12-1 18:05
 * @return
 */
function isExistPersonDemo() {
	/**
	 * yanjian 查看原表单添加业务处理代码
	 * 
	 * */
	if(getPersonDemo() != null && getPersonDemo() != ""){
		var personDemoJSON = eval($("#personDemo").val());
		var flag = false;
		for(var i=0;i<personDemoJSON.length;i++){
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
	if ($("#parentInstanceId").val() == "") {// 父流程实例ID不存在时，不显示原表单按钮，return
		// false，否则return true
		return false;
	} else {
		return true;
	}
}

/**
 * 查看原表单
 * 
 * @author 严建 2011-12-1 18:05
 * @return
 */
function viewParentFormData() {
	var width = screen.availWidth - 10;
	var height = screen.availHeight - 30;
	if ($("#personDemo").val() == null || $("#personDemo").val() == "") {
		alert("该流程表单无原表单数据！")
		return;
	}
	//var ReturnStr = OpenWindow(scriptroot
					//+ "/senddoc/sendDoc!viewParentFormData.action?instanceId="
					//+ $("#parentInstanceId").val(), width, height, window);
	
//	var ReturnStr = window.open(scriptroot
//			+ "/senddoc/sendDoc!viewParentFormData.action?instanceId="
//			+ $("#parentInstanceId").val(), width, height, window);

	var url = scriptroot + "/senddoc/sendDoc!viewParentFormData.action?instanceId=" + $("#parentInstanceId").val();
	var ReturnStr=window.open(url,'window'+(new Date()).getTime(),'height='+height+',width='+width+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
}

/**
 * 得到原表单数据
 * 
 * @author 申仪玲
 * @return {personDemo}
 */
function getPersonDemoByBussId(tableName, pkName, pkValue) {
	/*
	 * $.post(contextPath+"!viewPersonDemo.action",{tableName:$("#tableName").val(),pkFieldName:$("#pkFieldName").val(),pkFieldValue:$("#pkFieldValue").val()}
	 * ,function(ret){ if(ret != ""){ $("#personDemo").val(ret); } else {
	 * $("#personDemo").val(""); }
	 * 
	 * alert("personDemo.value"+$("#personDemo").val());
	 * 
	 * return $("#personDemo").val(); });
	 * 
	 * var personDemo = $.ajax({url:
	 * contextPath+"!viewPersonDemo.action?tableName="+$("#tableName").val()+"&pkFieldName="+$("#pkFieldName").val()+"&pkFieldValue="+$("#pkFieldValue").val()
	 * ,async: false}).responseText;
	 * 
	 * if(personDemo != ""){ $("#personDemo").val(personDemo); } else {
	 * $("#personDemo").val(""); }
	 */
	// alert("in js personDemo.value:\n"+$("#personDemo").val());
	return $("#personDemo").val();
}

/**
 * 通过在电子表单设计器的ValidateFormData中调用此JS函数 将控件中的标题内容传递到此页面的控件域中.
 * 通过判断任务id（taskId）是否为空来设置标题。
 * 
 * @param title
 *            标题内容
 */
function getTitle(title) {
	if (title && title != "") {
		$("#businessName").val(title);
	}
}
// 得到流程流水号
function getWorkflowCode() {
	return $("#workflowCode").val();
}
// 得到当前用户职务
function getUserJob() {
	return $("#userJob").val();
}

// 获取第二级机构
function getSecondOrgName() {
	return $("#secondOrgName").val();
}

/**
 * 选择人员
 * 
 * @param {}
 *            type 选择树形结构类型,默认全部人员0:本部门人员;1:本机构人员;2:全部人员；3：岗位人员；4：用户组人员；5：机构
 * @param {}
 *            id 之前选择过的数据
 * @param {}
 *            zf 需要返回的数据隔开字符。默认逗号隔开
 * @return {} 共3个参数 1、树形结构类型,2表示全部人员 2、回传的值,表示再次打开树时需要选中人员 3、返回的数据隔开字符
 */
function chooseUser(type, zf) {
	if (!type && !zf) {// 只有一个参数,则为类型
		type = "2";
		zf = ",";
	}
	if (type && !zf) {// 两个参数
		zf = ",";
	}
	zf = decodeURI(zf);
	var ret = window
			.showModalDialog(
					scriptroot
							+ "/address/addressOrg!chooseForType.action?chooseType="
							+ type,
					zf,
					'dialogWidth:420pt ;dialogHeight:370pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
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
 * 表单上选择关联的字典项
 * 
 * @param param
 *            字典名
 *   换一个方法  
 */
function selectOrgFromDict2(param) {
	// var ret =
	// OpenWindow("<%=root%>/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="+param,420,
	// 370, window);
	var ret = window
			.showModalDialog(
					scriptroot
							+ "/address/addressOrg!showTree.action?type="
							+ param,
					window,
					'dialogWidth:420pt ;dialogHeight:200pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ret)
		return ret;
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
						+ id + "&contextPath=" + contextPath + "&readOnly=" + readOnly + "&name=" + encodeURI(encodeURI(name)),'view', Width, Height,
				'查看');
		/*
		 * if(ReturnStr){ if(ReturnStr == "OK"){ loadFormTemplate(); } }
		 */
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
function editAttachs(id, name,readOnly,tableNames,idName) {
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
/**
 * 查看表单数据
 * 
 * @param index
 *            查看父流程表单数据的层次 1：表示查看父流程表单数据 2：表示查看父流程的父流程表单数据 3：... 依此类推
 */
function viewFormData(index) {
	if (!index) {
		index = 1;
	}
	var width = screen.availWidth - 10;
	var height = screen.availHeight - 30;
	if ($("#personDemo").val() == null || $("#personDemo").val() == "") {
		alert("该流程表单无原表单数据！")
		return;
	}
	var ReturnStr = OpenWindow(scriptroot
					+ "/senddoc/sendDoc!viewFormData.action?index=" + index
					+ "&instanceId=" + instanceId, width, height, window);
}

/**
 * 字符编码，混合使用encodeURI()、escape()、encodeURIComponent()进行编码， 处理一些特殊字符所引发的问题
 * 
 * @author 严建
 * @date 2011年9月24日 15:12
 * @param {}
 *            stringObj:被编码的字符串
 * @return temp 编码之后的字符串
 */
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

/**
 * 过滤特殊字符
 * 
 * @author 严建
 * @date 2011年11月22日 15:12
 * @param {}
 *            suggestionValue:待处理字符
 * @return temp 处理之后的字符串
 */
function EE_Filter(suggestionValue) {
	var temp = "";
	if ("undefined" != (typeof(suggestionValue))) {
		if (suggestionValue.indexOf("\r") != -1) { // 处理回车
			suggestionValue = suggestionValue.replace(new RegExp("\r", "gm"),
					"");
		}
		if (suggestionValue.indexOf("\n") != -1) { // 处理换行
			suggestionValue = suggestionValue.replace(new RegExp("\n", "gm"),
					"＜BR＞");
		}
		if (suggestionValue.indexOf("\"") != -1) { // 处理英文形式的双引号
			suggestionValue = suggestionValue.replace(new RegExp("\"", "gm"),
					"“");
		}
		if (suggestionValue.indexOf("\'") != -1) { // 处理英文形式的单引号
			suggestionValue = suggestionValue.replace(new RegExp("\'", "gm"),
					"’");
		}
		if (suggestionValue.indexOf("<") != -1) { // 处理英文形式的单引号
			suggestionValue = suggestionValue.replace(new RegExp("<", "gm"),
					"＜");
		}
		if (suggestionValue.indexOf(">") != -1) { // 处理英文形式的单引号
			suggestionValue = suggestionValue.replace(new RegExp(">", "gm"),
					"＞");
		}
		if (suggestionValue.indexOf("\\") != -1) { // 处理英文形式的的\
			suggestionValue = suggestionValue.replace(/[\\]/gm, "＼");
		}
		if (suggestionValue.indexOf("%") != -1) { // 处理英文形式的的%
			suggestionValue = suggestionValue.replace(/[%]/gm, "％");
		}
		temp = suggestionValue;
	}
	return temp;
}

/**
 * 判断是否存在特殊字符
 * 
 * @author 严建
 * @date 2011年11月22日 15:12
 * @param {}
 *            suggestionValue:待处理字符
 * @return flag true:"存在特殊字符";false:"存在特殊字符"
 */
function isExistEE_Char(suggestionValue) {
	var EE_Char = new Array("\r", "\n", "\"", "\'", "<", ">", "\\");
	var flag = false;
	for (var i = 0; i < EE_Char.length; i++) {
		if (suggestionValue.indexOf(EE_Char[i]) != -1) {
			flag = true;
			return flag;
		}
	}
	return flag;
}

/**
 * 设置控件的属性
 * 
 * @author 严建
 * @date 2011年12月21日 20:04
 * @param {}
 *            FormControl 控件名称
 * @param {}
 *            PropertyName 控件属性名称
 * @param {}
 *            PropertyValue 控件属性值
 * @return flag true:"存在特殊字符";false:"存在特殊字符"
 */
function SetProperty(FormControl, PropertyName, PropertyValue) {
	var control = formReader.GetFormControl(FormControl);// 获取控件
	if (control != null) {
		control.SetProperty(PropertyName, PropertyValue);
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
/**
 * 
 * * @param tableNames
 *            附件表名
 * @param idName
 *            附件主键名
 */
// 下载附件 针对两张附件表
function downloads(id,attName,tableNames,idName) {
	$("body")
			.append("<iframe id='frame_attach' frameborder='0' scrolling='no' style='width:100%; height:100%;' style='display:none;' />");
	var url = scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id="
			+ id;
	$("#frame_attach").attr("src",
			scriptroot + "/senddoc/sendDoc!downLoad.action?id=" + id+"&attName="+attName+"&tableNames="+tableNames+"&idName="+idName);
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
 * 保存PDF正文临时文件 by luosy
 */
function saveformiframe() {
	var frame = document.frames["pdfFrame"];
	if ("undefined" != (typeof(frame)) && "function" == (typeof(frame.SaveDoc))) {
		frame.SaveDoc();
	}
	var frame2 = document.frames["adobePdfFrame"];
	if ("undefined" != (typeof(frame2)) && "function" == (typeof(frame2.SaveDoc))) {
		frame2.SaveDoc();
	}
}

/**
 * 设置PDF正文临时文件信息 by luosy
 */
function setPdfContentInfo(info) {
	$("#pdfContentInfo").val(info);
}

/**
 * 初始化隐藏领导意见和办结意见选项卡并作初始化 by yanjian
 */
function initHideBgtTags() {
	formReader.SetFormTabVisibility("attachName", false);
	formReader.SetFormTabVisibility("doneSuggestion", false);
	if (formReader.GetFormControl("attachName_content")
			&& formReader.GetFormControl("doneSuggestion_content")) {
		formReader.GetFormControl("attachName_content").Required = false;
		formReader.GetFormControl("doneSuggestion_content").Required = false;
	}
}
/**
 * 验证来文号唯一性
 * 
 * @author:张磊
 * @date: 2012-02-14 上午10:30:59
 * @param tableName
 *            表名
 * @param fieldName
 *            字段名
 * @param fieldValue
 *            验证字段的值
 * @param keyName
 *            主键名
 * @param keyValue
 *            主键的值
 * @return
 */
function checkDocNumber(tableName, fieldName, fieldValue, keyName, keyValue) {
	var ret = false;
	if($("#isBackspace").length>0 && "1" == $("#isBackspace").val()){
		//退回的时候不做验证
		ret = true;
	}else{
		$.ajax({
					type : "post",
					url : scriptroot + "/senddoc/sendDoc!checkDocNumber.action",
					data : "tableName=" + tableName + "&fieldName=" + fieldName
							+ "&fieldValue=" + fieldValue + "&keyName=" + keyName
							+ "&keyValue=" + keyValue,
					async : false,
					success : function(data) {
						if (data == 1) {
							// alert("OK");
							ret = true;
						} else if (data == 0) {
							// alert("来文号不唯一.请确认!");
							ret = false;
						}
					}
				});
	}
	return ret;
}

// 处理意见征询,在收文办理时,挂起流程，办理意见征询的业务.
function doYjzx() {
	var formControl = formReader.GetFormControl($("#tableName").val(),
			"WORKFLOWTITLE");// 标题
	var title = "";
	if (formControl) {
		title = formControl.Value;
		title = encodeURI(encodeURI(title));
	}
	var ret = OpenWindow(
			scriptroot
					+ "/fileNameRedirectAction.action?toPage=senddoc/sendDoc-writeyjzx.jsp?docId="
					+ $("#bussinessId").val() + "&instanceId="
					+ $("#instanceId").val() + "&businessName=" + title, "500",
			"300", window);
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
			$(window.frames["iframe_nextstep"].document).find("#suggestion").val("进入意见征询环节");
			$("#suggestion").val("用户进行意见征询环节");
			 $(window.frames["iframe_nextstep"].document).find("#doNext").click();
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
/**
 * @author yanjian	2012-09--04 16:57
 * 湖北科技厅OA项目业务方法
 * 是否自动保草稿
 * */
function autoSaveDraft() {
	if (!draftRequiredVerification()) {// 是否已经存在草稿
		if (confirm("确定要自动保存草稿吗？")) {
			saveDraft();
		}
	}
}
/**
 * @author yanjian 2012-07-04 09:54 是否保存了草稿验证
 * 
 */
function draftRequiredVerification(){
	var result = true;
	var draftRequired = false;
	// 增加对是否保存了草稿验证 dengzc 2011年7月2日11:05:43
	if (document.getElementById("tr_saveDraft")) {// 存在保存草稿按钮
		if (document.getElementById("draftRequired")) {// 草稿为必填项
			draftRequired = true;
		}
	}
	if (draftRequired) {
		$.ajax({
		  		type:"post",
		  		dataType:"json",
		  		async:false,
		  		url:contextPath+"!checkDraft.action?timeStamp="+new Date(),
		  		data:"id="+$("#pkFieldValue").val()+"&fileName="+ encodeURI("草稿.doc"),
		  		success:function(retCode){
					if (retCode == "0") {// word存在, 草稿不存在
						result = false;
					} 
		  		}
		  	});
	}
	return result;
}

/**
 * 表单上选择单位   公文传输tj
 */
function selectOrg(orgId,orgName) {
	var orgIds = orgId+":"+orgName
	//var orgIds = orgId+":"+orgName+":"+orgName1;
	var ret = window
			.showModalDialog(
					scriptroot+ "/sends/docSend!orgTrees.action",orgIds,
					'dialogWidth:500pt ;dialogHeight:350pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ret){
		return ret;
	}else{
		return "";
	}
}

/**
 * 表单上选择主送、抄送
 * @param orgId		默认要选中的单位ID
 * @param orgName	默认要选中的单位名称
 * @param orgId2	默认不可选择的单位ID
 * @param orgName2	默认不可选择的单位名称
 * @returns
 */
function selectOrgCs(orgId,orgName,orgId2,orgName2) {
	var orgIds = orgId+":"+orgName;
	var orgIdss = orgId2+":"+orgName2;
	var mxh1 = new Array(orgIds,orgIdss);
	var ret = window
			.showModalDialog(
					scriptroot+ "/sends/docSend!orgTrees.action",mxh1,
					'dialogWidth:500pt ;dialogHeight:350pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ret){
		return ret;
	}else{
		return "";
	}
}

/**
 * author:luosy 
 * date：2013-05-04
 * description:	生成意见征询word
 * 		事件触发生产word将公文摘要的信息直接插入到word模板中；每次操作会重新调用新的模板并插入内容
 * modifyer: 
 * description:
 */
function genYjzxWord(){
	//跳转到意见征询的office
	formReader.TabSelectedIndex=1;
	
	var zbcontent=formReader.GetOfficeControl("zbcontent");
	TANGER_OCX_OBJ = zbcontent;
	
	//验证摘要信息是否齐全
//	TANGER_OCX_OBJ = officeControl;
	var zqbh = formReader.GetFormControl("Edit36").Value;				//意见征询文号
	var swdw = formReader.GetFormControl("Edit31").Value;				//收文单位
	var fwdw = formReader.GetFormControl("Edit34").Value;				//发文单位
	var bt = formReader.GetFormControl("Edit30").Value;					//标题
	var lwh = formReader.GetFormControl("Edit29").Value;				//来文号
	var jzrq = formReader.GetFormControl("DateTimePicker2").Value;		//截至日期
	var zqrq = formReader.GetFormControl("DateTimePicker3").Value;		//征求日期
	var cbcs = formReader.GetFormControl("Edit33").Value;				//承办处室
	var genRemind = "";
	
	if(zqbh==null||zqbh==""||zqbh=="洪府厅    转〔2013〕   号"){ genRemind +=" 意见征询文号 "; }
	if(swdw==null||swdw==""){ genRemind +=" 来文单位 "; }
	if(fwdw==null||fwdw==""){ genRemind +=" 征询单位 "; }
	if(bt==null||bt==""){ genRemind +=" 标题 "; }
	if(lwh==null||lwh==""){ genRemind +=" 来文号"; }
	if(jzrq==null||jzrq==""){ genRemind +=" 截至日期 "; }else{ jzrq=jzrq.replace("-","年").replace("-","月")+"日"; }
	if(zqrq==null||zqrq==""){ genRemind +=" 征求日期 "; }else{ zqrq=zqrq.replace("-","年").replace("-","月")+"日"; }
	if(cbcs==null||cbcs==""){ genRemind +=" 承办处室 "; }
	if(genRemind!=""){
		if (confirm("公文摘要中的以下信息没有输入，是否现在就生成新的征求意见单。\n\n"+genRemind) == false) {
			return;
		}
	}else{
		if (confirm("生成征求意见单将替换原有的word内容，是否现在就生成新的征求意见单。\n\n"+genRemind) == false) {
			return;
		}
	}

	TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty_yjzx.doc");

	//替换公文摘要书签 
	//var value = TANGER_OCX_OBJ.GetBookmarkValue(name);
	TANGER_OCX_OBJ.SetBookmarkValue("征询文号",zqbh);
	TANGER_OCX_OBJ.SetBookmarkValue("发文单位",swdw);
	TANGER_OCX_OBJ.SetBookmarkValue("发文单位文号",lwh);
	TANGER_OCX_OBJ.SetBookmarkValue("处室名称",cbcs);
	TANGER_OCX_OBJ.SetBookmarkValue("截止时间",jzrq);
	TANGER_OCX_OBJ.SetBookmarkValue("发起时间",zqrq);
	TANGER_OCX_OBJ.SetBookmarkValue("收文单位",fwdw);
	TANGER_OCX_OBJ.SetBookmarkValue("标题",bt);
}

/**
 * author:luosy 
 * date：2013-05-11
 * description:	生成公文转办word
 * 		事件触发生产word将公文摘要的信息直接插入到word模板中；每次操作会重新调用新的模板并插入内容
 * modifyer: 
 * description:
 */
function genGwzbWord(){
	//跳转到意见征询的office
	formReader.TabSelectedIndex=1;
	
	var zbcontent=formReader.GetOfficeControl("zbcontent");
	TANGER_OCX_OBJ = zbcontent;
	
	//验证摘要信息是否齐全
//	TANGER_OCX_OBJ = officeControl;
	var zqbh = formReader.GetFormControl("Edit36").Value;				//意见征询文号
	var swdw = formReader.GetFormControl("Edit31").Value;				//收文单位
	var fwdw = formReader.GetFormControl("Edit34").Value;				//发文单位
	var bt = formReader.GetFormControl("Edit30").Value;					//标题
	var lwh = formReader.GetFormControl("Edit29").Value;				//来文号
//	var jzrq = formReader.GetFormControl("DateTimePicker2").Value;		//截至日期
	var zqrq = formReader.GetFormControl("DateTimePicker3").Value;		//征求日期
	var cbcs = formReader.GetFormControl("Edit33").Value;				//承办处室
	var swwh = formReader.GetFormControl("Edit37").Value;				//收文文号
	var ldps = formReader.GetFormControl("Edit39").Value;				//收文文号
	var genRemind = "";
	
	if(zqbh==null||zqbh==""||zqbh=="洪府厅    转〔2013〕   号"){ genRemind +=" 公文转办文号 "; }
	if(swwh==null||swwh==""){ genRemind +=" 收文文号"; }
	if(swdw==null||swdw==""){ genRemind +=" 来文单位 "; }
	if(fwdw==null||fwdw==""){ genRemind +=" 转办单位 "; }
	if(bt==null||bt==""){ genRemind +=" 标题 "; }
	if(lwh==null||lwh==""){ genRemind +=" 来文号"; }
//	if(jzrq==null||jzrq==""){ genRemind +=" 截至日期 "; }else{ jzrq=jzrq.replace("-","年").replace("-","月")+"日"; }
	if(zqrq==null||zqrq==""){ genRemind +=" 征求日期 "; }else{ zqrq=zqrq.replace("-","年").replace("-","月")+"日"; }
	if(cbcs==null||cbcs==""){ genRemind +=" 承办处室 "; }
	if(genRemind!=""){
		if (confirm("公文摘要中的以下信息没有输入，是否现在就生成新的公文转办单。\n\n"+genRemind) == false) {
			return;
		}
	}else{
		if (confirm("生成公文转办单将替换原有的word内容，是否现在就生成新的公文转办单。\n\n"+genRemind) == false) {
			return;
		}
	}
	TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty_gwzb.doc");
	//替换公文摘要书签 
	//var value = TANGER_OCX_OBJ.GetBookmarkValue(name);
	TANGER_OCX_OBJ.SetBookmarkValue("征询文号",zqbh);
	TANGER_OCX_OBJ.SetBookmarkValue("发文单位",swdw);
	TANGER_OCX_OBJ.SetBookmarkValue("发文单位文号",lwh);
	TANGER_OCX_OBJ.SetBookmarkValue("处室名称",cbcs);
	TANGER_OCX_OBJ.SetBookmarkValue("发起时间",zqrq);
	TANGER_OCX_OBJ.SetBookmarkValue("收文单位",fwdw);
	TANGER_OCX_OBJ.SetBookmarkValue("收文文号",swwh);
	TANGER_OCX_OBJ.SetBookmarkValue("标题",bt);
	TANGER_OCX_OBJ.SetBookmarkValue("领导批示内容",ldps);
}

/**
 * 主送/抄送 单位 改变时，比对原有值，将手动添加的单位放在备注中 
 * @param editbakname 修改的主送、抄送单位的修改之前的名称
 * @returns {String}
 */
function syncZSdept(){
	var retStr = "";
	var newzsdw = formReader.GetFormControl("Edit3").Value;			//主送单位的值
	var oldzsdw = formReader.GetFormControl("Edit_zs_bak").Value;		//主送单位的原有值
	var oldzsdwname = formReader.GetFormControl("Edit_zs_name").Value;		//抄送单位的名称
	var oldzsdwid = formReader.GetFormControl("Edit_zs_id").Value;		//抄送单位的id
	var newcsdw = formReader.GetFormControl("Edit5").Value;			//抄送单位的值
	var oldcsdw = formReader.GetFormControl("Edit_cs_bak").Value;		//抄送单位的原有值
	var oldcsdwname = formReader.GetFormControl("Edit_cs_name").Value;		//抄送单位的名称
	var oldcsdwid = formReader.GetFormControl("Edit_cs_id").Value;		//抄送单位的id
	//alert("oldzsdw:"+oldzsdw+"\nnewzsdw:"+newzsdw);
	//将主送单位的名称逐一比对，将与原有值不匹配的文字放在发文登记的备注栏里
	var ozs = oldzsdw.split(";");
	for(var i=0;i<ozs.length;i++){
		newzsdw = newzsdw.replace(ozs[i],"");
	}
	var nzs = newzsdw.split(";");
	for(var j=0;j<nzs.length;j++){
		if(nzs[j]!=""){
			retStr +=nzs[j]+";";
		}
	}
	//将抄送单位的名称逐一比对，将与原有值不匹配的文字放在发文登记的备注栏里
	var ocs = oldcsdw.split(";");
	for(var i=0;i<ocs.length;i++){
		newcsdw = newcsdw.replace(ocs[i],"");
	}
	var ncs = newcsdw.split(";");
	for(var j=0;j<ncs.length;j++){
		if(ncs[j]!=""){
			retStr +=ncs[j]+";";
		}
	}
	/**
	 * Edit16 发文登记单	发送单位id
	 * Edit12 发文登记单 发送单位名称
	 * Edit13 发文登记单 备注
	 * */
	formReader.GetFormControl("Edit12").Value = oldzsdwname+";"+oldcsdwname;	
	formReader.GetFormControl("Edit16").Value = oldzsdwid+";"+oldcsdwid;	
	formReader.GetFormControl("Edit13").Value = retStr;	
	//formReader.GetFormControl("Edit17").Value = retStr;
	//alert("Edit12:"+formReader.GetFormControl("Edit12").Value+"\nEdit13:"+formReader.GetFormControl("Edit13").Value+"\nEdit16:"+formReader.GetFormControl("Edit16").Value);
	return retStr;
}
 function topdf(){
      		 var bussinessId = $("#bussinessId").val();
      		 var businessName = $("#businessName").val();
//      	 var frame=document.getElementById("pdfFrame");
		     window.open(scriptroot+"/senddoc/sendDoc!topdf.action?bussinessId="+bussinessId+"&businessName="+businessName);
      }
 /**
  * 获取当前用户的id
  * @data  2014年2月20日09:06:55 
  */
 function getCurrntId(){
		var currntId ;
				$.ajax({
					type: "post",
					url: scriptroot + "/senddoc/sendDoc!getCurrentId.action",
					async: false,
					success: function(data) {
					if (data !="" ) {
						//得到当前用户的ID
						currntId = data;
						}
					}
				});
				return currntId;
		}
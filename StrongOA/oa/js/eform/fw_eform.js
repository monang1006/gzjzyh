/**
 * 自由流程中电子表单的设置，空函数是为了兼容eform.js
 * 
 */

var TANGER_OCX_OBJ = null;
var loadFinish = false;

//是否是查看表单
var isView = false;

var siteName = location.href;
var pos = siteName.indexOf("http://");
siteName = siteName.substring(pos+7);
pos = siteName.indexOf("/");
siteName = "http://" + siteName.substring(0, pos);

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

    var errMsg = "Unhandled Error in Silverlight Application " +  appSource + "\n" ;

    errMsg += "Code: "+ iErrorCode + "    \n";
    errMsg += "Category: " + errorType + "       \n";
    errMsg += "Message: " + args.ErrorMessage + "     \n";

    if (errorType == "ParserError") {
        errMsg += "File: " + args.xamlFile + "     \n";
        errMsg += "Line: " + args.lineNumber + "     \n";
        errMsg += "Position: " + args.charPosition + "     \n";
    }
    else if (errorType == "RuntimeError") {           
        if (args.lineNumber != 0) {
            errMsg += "Line: " + args.lineNumber + "     \n";
            errMsg += "Position: " +  args.charPosition + "     \n";
        }
        errMsg += "MethodName: " + args.methodName + "     \n";
    }

    throw new Error(errMsg);
}

function getFormPlugin() {
    return document.getElementById("plugin").Content;
}

function getFormReader() {
    return getFormPlugin().FormReader;
}

function SetFormTemplate() {
    var text1 = document.getElementById("Text1");
    var xmlFormTemplate = text1.value;
    var formReader = getFormReader();
    var ret = formReader.SetFormTemplate(xmlFormTemplate);
    //alert(ret);
}

function GetFormData() {
    var text1 = document.getElementById("Text1");
    var formReader = getFormReader();
    var xmlFormData = formReader.GetFormData();
    text1.value = xmlFormData;
}

function SetFormData() {
    var text1 = document.getElementById("Text1");
    var xmlFormData = text1.value;
    var formReader = getFormReader();
    var retCode = formReader.SetFormData(xmlFormData, false);
}

function SetFormReadOnly() {
    var formReader = getFormReader();
    var retCode = formReader.SetFormReadOnly(true);
}

function PrintForm() {
    var formReader = getFormReader();
    var retCode = formReader.PrintForm();
}

function SaveFormDataRequestCompleted(bResult, strResult, strDetail) {
	opener.location.reload();
	window.close();
	
	return;
    if (bResult) {
		opener.location.reload();
		window.close();

        //alert("成功：" + strResult);
    }
    else {
        alert("错误：" + strResult + "\n\t" + strDetail);
    }
}

function SaveFormData() {
    var formReader = getFormReader();
    //var retCode = formReader.SaveFormData("http://192.168.2.121:8080/testAction", "form1", "SaveFormDataRequestCompleted");
   	var url = basePath + "senddoc/eFormTemplate.action";
	document.getElementById("formAction").value = "saveFormData";
	
	$("#form1 input[type=hidden]").each(function(){
		$(this).val(encodeURIComponent($(this).val()));
	});
    
    var retCode = formReader.SaveFormData(url, "form1", "SaveFormDataRequestCompleted");
 }

 function LoadFormDataRequestCompleted(bResult, strResult, strDetail) {
     if (bResult) {
         //alert("成功：" + strResult);
     }
     else {
         //alert("错误：" + strResult + "\n\t" + strDetail);
     }
 }

 function LoadFormData() {
	 var formReader = getFormReader();
     formReader.ClearLoadFormDataFilter();

     formReader.AddLoadFormDataFilterParameter(fwFormBizTable, fwFormBizPk, fwFormBizId);
    
     var url = basePath + "senddoc/eFormTemplate.action";
     document.getElementById("formAction").value = "loadFormData";

     var retCode = formReader.LoadFormData(url, "form1", "LoadFormDataRequestCompleted");
     
     if(isView){
    	 formReader.SetFormReadOnly(true);
     }
}

function LoadFormTemplateRequestCompleted(bResult, strResult, strDetail) {
    if (bResult) {
    	
	 	var formReader = getFormReader();
		TANGER_OCX_OBJ = formReader.GetOfficeControl();
		var tabSelectedName = "";
		if(formReader.TabSelectedName){
			tabSelectedName = formReader.TabSelectedName;
		}

		if($("#fwId").val() != ""){
    		LoadFormData();
//
//    		if(TANGER_OCX_OBJ != null){
//				var formId = $("#formId").val();
//				TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));	
//    		}
    	}
//    	else{
//    		initWord();
//    	}

        //alert("成功：" + strResult);
    }
    else {
        //alert("错误：" + strResult + "\n\t" + strDetail);
        alert("错误：" + strResult);
    }
}

function LoadFormTemplate() {
    var formReader = getFormReader();
    var url = basePath + "senddoc/eFormTemplate.action";
	document.getElementById("formAction").value = "LoadFormTemplate";

    var bRet = formReader.LoadFormTemplate(url, "form1", "LoadFormTemplateRequestCompleted");   
    //var bRet = formReader.LoadFormTemplate("LoadFormTemplateRequestCompleted");
}

function ResetFormData() {
    var formReader = getFormReader();
    formReader.ResetFormData();
}

function onSilverlightLoad() {
	formReader = getFormReader();
	var url = formReader.FormServiceAddress + ".action";
	formReader.FormServiceAddress = url;
	
	if (OfficeTabContent && OfficeTabContent != "") {
		formReader.OfficeTabContent = OfficeTabContent;
		// alert(OfficeTabContent);
	}

	// 装载模板
	LoadFormTemplate();
}

function SaveTemplateDataRequestCompleted(bResult, strResult, strDetail) {
    if (bResult) {
        //alert("成功：" + strResult);
    } else {
        alert("错误：" + strResult + "\n\t" + strDetail);
    }
}

//保存数据快照，为生成pdf做准备
function SaveTemplateData(){
    var formReader = getFormReader();			
    var retCode = formReader.SaveTemplateData("SaveTemplateDataRequestCompleted");
}

function office2pdf(){
	 var formReader = getFormReader();
	TANGER_OCX_OBJ = formReader.GetOfficeControl();
	alert(TANGER_OCX_OBJ.IsPDFCreatorInstalled());

	TANGER_OCX_OBJ.PublishAsPDFToURL(scriptroot + "/senddoc/eFormTemplate!savePdf.action", "formData");
}

/**
 * 第一次初始化office对象时供表单调用的方法
 * @param plugin 银光插件对象
 * @param form  表单对象
 * @param officeControl 千航控件对象
 * @param documentType   初始化的office类型
 */
function NewOfficeDocument(plugin, form, officeControl, documentType) {
	if(loadFinish == false){
		//return;
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
	formReader = getFormReader();
	var tabSelectedName = "";
	if(formReader.TabSelectedName){
		tabSelectedName = formReader.TabSelectedName;
	}
	if(fwFormBizTable != ""){
		var formId = $("#formId").val();
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
}

/**
 * 存在office对象时供表单调用的方法
 * @param plugin 银光插件对象
 * @param form  表单对象
 * @param officeControl 千航控件对象
 */
function OpenOfficeDocument(plugin, form, officeControl){
	if(loadFinish == false){
		//return;
	}
	formReader = getFormReader();
	var tabSelectedName = "";
	if(formReader.TabSelectedName){
		tabSelectedName = formReader.TabSelectedName;
	}

	var formId = $("#formId").val();
	officeControl.OpenFromURL(scriptroot + "/senddoc/eFormTemplate!loadWordFromUrl.action?bussinessId=" + bussinessId +"&formId=" + formId+"&tabSelectedName=" + encodeURI(encodeURI(tabSelectedName)));	
	officeControl.WebFileName = '新建文档';
	TANGER_OCX_OBJ = officeControl;
//	TANGER_OCX_OBJ.SetReadOnly(readonly, "");// 是否只读
//	if (typeof(initWordAfter) != "undefined") {
//		initWordAfter();
//	}
}



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
	if (( tabSelectedName != "公文征求意见单" && tabSelectedName != "公文转办单")) {
		return;
	}

	if(bussinessId != "" && (tabSelectedName == "正文" || tabSelectedName == "公文征求意见单" || tabSelectedName == "公文转办单")){
		var formId = $("#formId").val();
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
     }

function isExistPersonDemo() {return false;}

function initOrgName() {}

function initUserName() {}

function toSuggestion(){}

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
					'dialogWidth:420pt ;dialogHeight:180pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
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


//下载附件
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
//下载附件 针对两张附件表
function downloads(id,attName,tableNames,idName) {
	$("body")
			.append("<iframe id='frame_attach' frameborder='0' scrolling='no' style='width:100%; height:100%;' style='display:none;' />");
	var url = scriptroot + "/senddoc/sendDoc!downLoadAttachment.action?id="
			+ id;
	$("#frame_attach").attr("src",
			scriptroot + "/senddoc/sendDoc!downLoad.action?id=" + id+"&attName="+attName+"&tableNames="+tableNames+"&idName="+idName);
}
//打开附件，主要为PDF，EXCLE
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

function selectOrgSendInput(){}

//表单模板初始化失败时调用
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
	var errMsg = "Unhandled Error in Silverlight Application " + appSource + "\n";
	errMsg += "Code: " + iErrorCode + "    \n";
	errMsg += "Category: " + errorType + "       \n";
	errMsg += "Message: " + args.ErrorMessage + "     \n";
	if (errorType == "ParserError") {
		errMsg += "File: " + args.xamlFile + "     \n";
		errMsg += "Line: " + args.lineNumber + "     \n";
		errMsg += "Position: " + args.charPosition + "     \n";
	} else {
		if (errorType == "RuntimeError") {
			if (args.lineNumber != 0) {
				errMsg += "Line: " + args.lineNumber + "     \n";
				errMsg += "Position: " + args.charPosition + "     \n";
			}
			errMsg += "MethodName: " + args.methodName + "     \n";
		}
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
function onSilverlightLoad() {
	formReader = getFormReader();
	var url = formReader.FormServiceAddress + ".action";
	formReader.FormServiceAddress = url;
	//装载模板
	loadFormTemplate();
}
//装载模板
function loadFormTemplate() {
	var actionUri = basePath + "senddoc/eFormTemplate.action";
	document.getElementById("formAction").value = "LoadFormTemplate";
	if (formReader.LoadFormTemplate(actionUri, "form", "loadFormTemplateRequestCompleted")) {
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
    	//加载右键菜单 抛出给具体的页面实现
    	if(typeof(loadContentMenu)!="undefined"){
    		var gridControl = formReader.GetGridControl();
    		loadContentMenu(gridControl);
    	}
    	//加载列表数据
    	loadFormGridData();
	} else {
    	//加载表单模板失败
		//失败信息:strResult
		formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
	}

}

function GridPopupMenuCallbackCommand(key) {
   eval(key+"();");
}
 
//加载列表数据
function loadFormGridData(){
	var actionUri = basePath +  "component/formtemplate/formGridTemplate.action";
	document.getElementById("formAction").value = "loadFormGridData";
	var gridControl = formReader.GetGridControl();
	if(gridControl){
		gridControl.LoadData(actionUri,"form");
	}
}

//得到选中项,多个选中项以逗号隔开
//gridControl.DataPrimaryKeys 		得到主键名称
//gridControl.DataRowCount       		得到每页显示的记录总数
//gridControl.FormGridColumnNames	得到字段名称
//gridControl.GetCellText(i, gridControl.DataPrimaryKeys) 得到第i行的主键值
function getValue(columnName){
	var gridControl = formReader.GetGridControl();
	if(!columnName){
		columnName = gridControl.DataPrimaryKeys;
	}
	var ret = "";
    if (gridControl != null) {
        var pageSize = gridControl.DataRowCount;//每页显示的记录数
        for(var i=0;i<pageSize;i++){
	       if(gridControl.IsRowSelected(i) == 1){
	       	ret = ret + gridControl.GetCellText(i, columnName) + ",";
	       }
        }
    }
    if(ret != ""){
    	ret = ret.substring(0,ret.length - 1);
    }
    return ret;
}
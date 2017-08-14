<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>外部分发统计</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet"
			type="text/css">
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css"
			type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css"
			rel="stylesheet" type="text/css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>			
			
			<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
				<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		<style type="text/css">		
.AutoNewline {
	word-break: break-all; /*必须*/
}

.tabletitle {
	FILTER: progid : DXImageTransform.Microsoft.Gradient ( 
		                             gradientType =   0, startColorStr =  
		#ededed, endColorStr =   #ffffff );
}

.hand {
	cursor: pointer;
}
</style>
		<script type="text/javascript">
 var TANGER_OCX_Username="<s:property value = 'TANGER_OCX_Username' />";
var contextPath = "";//定义获取调用此JS的上下文路径,从form的action属性获取
var formId = null;//表单模板id
var businessName = "";//业务标题,从代办任务中获取,传递到流程审批页面,控件对应字段为WORKFLOWTITLE
var formReader = null;
var TANGER_OCX_OBJ = null;
var workflowName = null;//流程名
var workflowId = null;//流程ID
var accName=null;//附件名
var accID=null;//附件ID
$(document).ready(function(){
 var fullContextPath = $("form").attr("action");
  	 contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  	 formId = $("#formId").val();
     
});
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
    	var ret = formReader.SetFormReadOnly(true);
		var recvid='${docId}';
		//alert(recvid);
    	if(recvid !=null && recvid != ""){
			formReader.ClearLoadFormDataFilter();
			formReader.SetLoadFormDataFilter("T_OA_DOCDIS","SENDDOC_ID=?");
			formReader.AddLoadFormDataFilterParameter("T_OA_DOCDIS","SENDDOC_ID",recvid);
			var actionUri = basePath +  "senddoc/eFormTemplate.action";
			document.getElementById("formAction").value = "LoadFormData";
			if(formReader.LoadFormData(actionUri,"form","loadFormDataRequestCompleted")){
				//方法调用成功

			} else {
				//调用失败
			}    		
    	} else {//创建表单模板时
    		//initWord();
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

	} else {
		//加载模板数据失败
		formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
	}
}




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 打开一个空文档（2003格式），用于不同版本的WORD兼容
 * 调用千航的OpenFromURL需指定路径为完整URL路径.
 * 
 * 邓志城 修改：2010年6月30日15:14:43
 * 增加默认痕迹保留功能。
 */
function initWord(){
	if(!TANGER_OCX_OBJ){//校验是否存在WORD
		return ;
	} 
	/*var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
  	TANGER_OCX_OBJ.OpenFromURL(httpPath + contextPath + "!openEmptyDocFromUrl.action?docType="+type+"&formId="+formId);
  	TANGER_OCX_OBJ.WebFileName='新建文档';*/
  	var type = TANGER_OCX_OBJ.DocType;//得到OFFICE类型.
  	if(type == 0){//未初始化任何文档类型
  		if(TANGER_OCX_OBJ.GetOfficeVer() != 100){//如果客户端安装了OFFICE软件,则优先用OFFICE创建
  			type = "1";
  		} else {//如果未安装OFFICE,则验证客户端是否安装了WPS
	  		if(TANGER_OCX_OBJ.GetWPSVer() != 100){//安装了WPS软件,用WPS软件创建
	  			type = 6;//WPS
	  		} else {
	  			TANGER_OCX_OBJ.ShowTipMessage("信息提示","很抱歉，您没有正确安装OFFICE软件",false);
	  			return ;
	  		}
  		}
  	}
  	TANGER_OCX_OBJ.Activate(false);//被叫方拒绝接收呼叫
	TANGER_OCX_OBJ.OpenFromURL(scriptroot + "/empty.jsp?docType="+type);
  	TANGER_OCX_OBJ.WebFileName='新建文档';
}


/**申仪玲 20121019
 * 表单控件调用的初始化OFFICE事件
 * @param plugin 银光插件对象
 * @param form  表单对象
 * @param officeControl 千航控件对象
 * @param documentType   初始化的office类型
 */
function InitialOfficeDocument(plugin, form, officeControl, documentType) {

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
 
/**
 * 打印处理单
 */
function doPrintForm() {
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


//获取文号
function getDocNumber(){
	OpenWindow("<%=root%>/serialnumber/number/number!input.action",400,300,window);
}

/**
 * 获取编号
 * @return {文号}
 * @2011-06-02 09:46 luosy
 * @ 添加type 参数 ，规则id
 */
function getAutoCode(type){
    //var type = contextPath.replace(scriptroot,"");
    var ret = OpenWindow(scriptroot + "/autocode/autoCode!input.action?id="+type,600,450,window);
    if(ret)
        return ret;
}


//----------------------------初始化相关函数

//得到拟稿单位,此函数供电子表单自动调用
function initOrgCode(){
	return '${orgCode}';
}	

//得到拟稿人,此函数供电子表单自动调用
function getCurrentUserName(){
	return '${TANGER_OCX_Username}';
}
//初始化电子表单formId
function getFormId(){
	if('${docId}'==""){
		return "9050"; 
	}
}

function getIsDistribute(){
	if('${docId}'==""){
		return "0"; 
	}
}
function getIsSend(){
	if('${docId}'==""){
		return "0"; 
	}
}
function getState(){
	if('${docId}'==""){
		return "0"; 
	}
}

/**
   表单选择发文单位
    @param param 字典名
*/
   function selectOrgFromDict(param){
//        var ret = OpenWindow("<%=root%>/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="+param,420, 370, window);
        var ret = window.showModalDialog(scriptroot+"/address/addressOrg!showDictOrgTreeWithCheckbox.action?type="+param,window,'dialogWidth:420pt ;dialogHeight:370pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
        if(ret)
            return ret;
   }

   /**
   *@param id 	附件id
   @param name	附件名称
   */	   
   function editAttach(id,name){
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
			var prifix = name.substring(name.lastIndexOf(".")+1,name.length);
		   	if(prifix == ""){
		   		alert("请选择您要修改的附件！");
		   	 	return ;
		   	}
		   	if(prifix!="doc" && prifix!="xls" && prifix!="mpp" && prifix!="docx"){
		   		alert("附件只支持修改OFFICE类型文档！");
		   	 	return ;
		   	}
		   	 var Width=screen.availWidth-10;
		     var Height=screen.availHeight-30;
			 var ReturnStr=OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/workflow-editattach.jsp?bussinessId="+id
			 						+"&contextPath="+contextPath, 
		                                   Width, Height, window); 
		   	 if(ReturnStr){
		   	 	if(ReturnStr == "OK"){
		   	 			loadFormTemplate();
  						window.returnValue = "OK";
		   	 	}
		   	 }
		   	 
		}
	} else {
		//保存失败
		formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.",strDetail,3);//0:提示
	}
}
   
   
   //返回按钮
function returnfirst(){
	location="<%=root%>/docdis/docDis.action";
}
		</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<form id="form" name="form"
			action="<%=root%>/senddoc/sendDoc!save.action" method="post">
			<s:hidden id="docId" name="docId"></s:hidden>
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
			<s:hidden id="formId" name="formId" value="9050"></s:hidden>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<s:hidden id="formAction" name="formAction"></s:hidden>			
		</form>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="45"
						style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									外部分发统计
								</td>
								<td width="70%">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<DIV class=tab-pane id=tabPane1>
				<SCRIPT type="text/javascript">
					tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
				</SCRIPT>
				<div class=tab-page id=tabPage1>
					<H2 class=tab>
						外部分发统计
					</H2>
					<tbody>
						<table width="100%" border="0" cellpadding="0" cellspacing="1"
							class="table1">
							<tr>
								<td class="biao_bg1" width="15%" align="right">
									<span class="wz">接收状态：</span>
								</td>
								<td class="td1" width="75%">
									<div id="allre" style="display: ;">
										<span class="wz">接收文档总数：<font color=red>${hasreply+unreply}</font>人，已确认签收：<font
											color=red>${hasreply}</font>人，尚未签收：<font color=red>${unreply}</font>人</span>
									</div>
								</td>
							</tr>
							<tr>
								<td class="biao_bg1" width="15%" align="right">
									<span class="wz">标&nbsp;&nbsp;&nbsp;&nbsp;题：</span>
								</td>
								<td class="td1" width="75%">
									<span class="wz">${docDis.senddocTitle}</span>
								</td>
							</tr>
							<tr>
								<td class="biao_bg1" width="15%" align="right">
									<span class="wz">发送日期：</span>
								</td>
								<td class="td1" width="75%">
									<span class="wz"><s:date name="afterflow.getDocDate"
											format="yyyy-MM-dd" />
									</span>
								</td>
							</tr>
							<tr>
								<td class="biao_bg1" width="15%" align="right">
									<span class="wz">未进行签收单位：</span>
								</td>
								<td class="td1 AutoNewline" width="75%">
									<span class="wz">${unreplyOrg}</span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" name="more" id="more" class="button"
										style="display: none;" value="更多">
								</td>
							</tr>
							<tr>
								<td class="td1" width="100%" colspan="2">
									<%--
								<webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="replyMessageId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="null" getValueType="getValueByProperty" collection="${replyList}" isShowMenu="false">
									<webflex:flexTextCol caption="回复人" property="replyUser" showValue="replyUser" width="20%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
									<webflex:flexTextCol caption="移动电话" property="mobileNumber" showValue="mobileNumber" width="25%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
									<webflex:flexTextCol caption="回复内容"  property="replyContent" showValue="replyContent" width="35%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
									<webflex:flexDateCol caption="回复时间" property="replyTime" showValue="replyTime" width="30%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexDateCol>
								</webflex:flexTable>
							--%>
									<iframe align="top" width="98%" height="300" frameborder="0"
										src="<%=root%>/docdis/docDis!viewflow.action?docId=${docId}"></iframe>
								</td>
							</tr>
							<tr>
								<td class="td1" width="100%" colspan="2" align="center"
									cosplan=2>
									<input type="button" class="input_bg" id="print" name="print"
										value="打印" onclick="window.print()">
								</td>
							</tr>
						</table>
					</tbody>
					<SCRIPT type="text/javascript">tp1.addTabPage(document.getElementById( "tabPage1" ));</SCRIPT>
				</div>
				<div class=tab-page id=tabPage2 style="display: none;height: 450px;">
					<H1 class=tab>
						文档内容
					</H1>
					<SCRIPT type="text/javascript">
						tp2 = new WebFXTabPane( document.getElementById( "tabPane2") ,false );
					</SCRIPT>
					<div id="formef">
						<table width="100%" height="100%" border="0" cellspacing="0"
							cellpadding="0" style="vertical-align: top;">
							<tr>
								<td height="10%" class="tabletitle">
									<table width="100%" border="0" align="right" cellpadding="0"
										cellspacing="0">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9" alt="">
												发文处理单
											</td>
											<td width="70%">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td height="90%" align="center">
									<div id="silverlightControlHost"
										style="position: relative; height: 100%">
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
												style="text-decoration: none"> <img
													src="<%=path%>/detection/images/SLMedallion_CHS.png"
													alt="Get Microsoft Silverlight" style="border-style: none" />
											</a>
										</object>
										<iframe id="_sl_historyFrame"
											style="visibility: hidden; height: 0px; width: 0px; border: 0px"></iframe>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</DIV>
		</div>
	</body>
</html>

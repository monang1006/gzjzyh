<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.SimpleDateFormat" />
<jsp:directive.page import="com.strongit.oa.common.workflow.IWorkflowService" />
<jsp:directive.page import="com.strongit.oa.approveinfo.ApproveinfoManager" />
<jsp:directive.page import="com.strongmvc.service.ServiceLocator" />
<jsp:directive.page import="com.strongit.workflow.bo.TwfInfoApproveinfo" />
<jsp:directive.page import="com.strongit.oa.bo.ToaApproveinfo" />
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ include file="/common/OfficeControl/version.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "No-cache");
	response.setDateHeader("Expires", -10);

	IWorkflowService workflowService = (IWorkflowService) ServiceLocator
			.getService("workflowService");
	//ApproveinfoManager approveinfoManager = (ApproveinfoManager)ServiceLocator.getService("approveinfoManager");
	String taskId = (String) request.getAttribute("taskId");
	//String recordId = "";
	String isBackTask = "";
	if (taskId != null && taskId.length() > 0) {
		/*
		//流程中查找当前意见记录
		TwfInfoApproveinfo approveInfo = workflowService.getApproveinfoByTaskId(taskId);
		if(approveInfo != null){
			recordId = approveInfo.getAiId().toString();
		}else{
			//临时意见表中查找意见记录
			if(!approveinfoManager.findApproveInfo(taskId).isEmpty()){			
				ToaApproveinfo toaapproveInfo = approveinfoManager.findApproveInfo(taskId).get(0);
				recordId = toaapproveInfo.getAiId().toString();
			}
		}
		 */
		isBackTask = workflowService.isBackTask(taskId);
	}
	//公文传输分发id  
	//判断是否公文传输转来
	String senddocid = request.getParameter("senddocid");
%>
<html>
	<head>
		<title><s:if test="taskId != null && taskId !=''">
				${workflowName }
			</s:if> <s:else>
				${workflowName }
			</s:else>
		</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	    <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js?version=1111"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script src="<%=path%>/oa/js/workflow/bgtyjzx.js"
			type="text/javascript"></script>
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

//区别提交页面与查看页面的变量  
//用于控制收文登记单 不执行onchang事件的变量
var justRead = true;
</SCRIPT>
		<script type="text/javascript">
		var refresh = false;	//add  标识是否执行了父页面刷新 yanjian 2012-09-12 09:16
		//window.setInterval("initFormTemplate()",100);
		var isDoSave = false;//是否已经点击保存按钮
		var TANGER_OCX_Username = '${userName}';
		//表单数据提交之前调用此方法(预留方法)
		function doNextBefore(){
			//正文大小限定为70M:  1024*1024*50=52428800
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ != "" && typeof(TANGER_OCX_OBJ) != "undefined" && TANGER_OCX_OBJ.DocSize>52428800){
				alert("正文内容过大,请控制在50M以内");
				return false;
				}
			//验证流程期限是否早于服务器当前时间
			if($("#processOutTime").val() != ""){
				var compareTime=new Date($("#processOutTime").val().replace(/-/g,"/"));
				if(compareTime.getTime()<=new Number("<%=new Date().getTime()%>")){
					alert("设置的流程期限时间不能比服务器当前时间早,请重新设置。");
					return;
				}
			}
		}
		
		//退文 --- 退到收发室 --- xiaolj
		function tuiwen(){
			//by  tj
			var url = "<%=root%>/receives/recvDoc!listGwcs.action?docId="+$("#pkFieldValue").val();
			$.post(url, function(data) {
			 	if(data=="false"){
			 		//alert("此数据不是公文传输转来，不能退回！");
					//return;
					doBackSpacePrev();
			 	}else{
			 		var laiwentitle=formReader.GetFormControl("Edit5").Value;
					var laiwenDW = formReader.GetFormControl("Edit4").Value;
					//alert("我是：" + $("#bussinessId").val());
					var info = new Array();
					info[0] = laiwenDW;
					info[1] = laiwentitle;
					info[2] = $("#bussinessId").val();
					doBackSpacePrevByxlj(info);
			 	}
			});
		}
		
		//退文 --- 退到公文传输  ---xiaolj
		function tuiwentogwcs(){
			//by  tj
			var url = "<%=root%>/receives/recvDoc!listGwcs.action?docId="+$("#pkFieldValue").val();
			$.post(url, function(data) {
			
			 	if(data=="false"){
			 		//alert("此数据不是公文传输转来，不能退回！");
			 		//return ;
			 		if(confirm("该文件不是公文传输签收的文件，退文后将结束流程，并记录在收文处理的办结来文中。\n\n是否执行退文操作？")==true){
						doReturnDoc();
					}
					return ;
			 	}else{
			 		var laiwentitle=formReader.GetFormControl("Edit5").Value;
					var laiwenDW = formReader.GetFormControl("Edit4").Value;
					//alert("我是：" + $("#bussinessId").val());
					//判断bussinessId是否为空,为空说明为第一次走，就不要保存到TdocSendRet这个退文单表
					bussinessId = $("#bussinessId").val();
					pkFieldValue = $("#pkFieldValue").val();
					tableName = $("#tableName").val();
					var a ="";
					//alert(tableName);
					//return;
					if(bussinessId == null || bussinessId == ""){
						var info = new Array();
						info[0] = laiwenDW;
						info[1] = laiwentitle;
						info[2] = $("#taskId").val();
						info[3] = pkFieldValue;
						url = "<%=root%>/sends/transDoc!firsttuiwenret.action";
						//var a = OpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:760px',info);
						a = showModalDialog(url,info,"dialogWidth:600px;dialogHeight:760pxpt;"+
													"status:no;help:no;scroll:auto;");
					}else{
						var info = new Array();
						info[0] = laiwenDW;
						info[1] = laiwentitle;
						info[2] = $("#taskId").val();
						
						url = "<%=root%>/sends/transDoc!tuiwenret.action?bussinessId=" + bussinessId;
						//var a = OpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:760px',info);
						a = showModalDialog(url,info,"dialogWidth:600px;dialogHeight:760pxpt;"+
													"status:no;help:no;scroll:auto;");
					}
					if(a=="true"){
						//window.opener.location.reload();
						//window.close();
						doReturnDoc();
					}
			 	}
			});
		}
		
		//选择退文结束流程
		function doReturnDoc(){
			HiddenIframe();
     		$("#td_iframe_nextstep").remove();
		 	$("#transitionName").val("退文");
			doNext();
		}
		
		//展示机构列表，选择迁移线，分发下一步
		function distribution() {
			var orgName = OpenWindow('<%=root%>/senddoc/sendDoc!orgTree.action?', '550', '400', window);
			if(orgName!="0" && orgName!=null && orgName!=""){
				$("#transitionName").val(orgName);
				doNext();
			}else{
				return ;
			}
		}
		
		//展示机构列表，选择迁移线，分发下一步
		function yijian() {
		var width=screen.availWidth-155;
	   	var height=screen.availHeight-230;
		var id = $("#pkFieldValue").val();
			var orgName = OpenWindow('<%=root%>/senddoc/sendDoc!yijian.action?docid='+id, width, height, window);
		//	alert(orgName);
			if(orgName!="0" && orgName!=null && orgName!=""){
				$("#transitionName").val(orgName);
				doNext();
			}else{
				return ;
			}
		}
		
		//签收未签收文件
		function signdoc() {
			var taskId = $("#taskId").val();
			if(confirm("确认要签收?")){
			var url = "<%=path%>/senddoc/sendDoc!singdoc.action?taskId="+taskId;
				$.post(url,function(ret){
					if(ret == "1"){
						window.opener.location.reload();
						window.close();
					}	
				});
			}
		}
			
		//模板加载完成后调用此函数 || ($("#isReceived").val() == null || $("#isReceived").val()=="")
        function initialHtml(){
        if($("#currentNodeName").val() == "主办人员"){
      		document.getElementById("iframe_nextstep").src = contextPath + "!workflow.action?formId="+formId+"&taskId="+taskId
      								+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
      								+"&businessName="+encodeURI(encodeURI($("#businessName").val()))
      								+"&time="+new Date()+"&number="+Math.random();//添加时间戳和随机数去除IE缓存
        	$(window.document).find("#privilege").show();
   			showAllDiv();
      		OpenIframe();
        	return;
        }
        
        // 新建时不显示意见征询反馈  taoji
        var fankui = ${fankui}+"";
        if(fankui=="1"){
        	formReader.SetFormTabVisibility("FormTab_szfk",false);
        }
        
        //alert("我是isReceived：" + $("#isReceived").val());
      	//之前的主办人员拟办才不显示，现在是主办人员才显示
      	//alert($("#orgName").val());
      	//alert("我是流程名：" + $("#workflowName").val());
      	//alert("我是节点名：" + $("#currentNodeName").val());
      	if(($("#workflowName").val() == "公文转办流程" && $("#currentNodeName").val() == "分办") || ($("#workflowName").val() == "征求意见流程" && $("#currentNodeName").val() == "分办")){
      		//alert("1");
      		$(window.document).find("#privilege").show();//显示节点设置的按钮
      		$("#td_menu").remove();
      		$("#td_iframe_nextstep").remove();
      	//if(($("#workflowName").val() == "公文转办流程" && $("#currentNodeName").val() != "主办人员") || ($("#workflowName").val() == "征求意见流程" && $("#currentNodeName").val() != "主办人员")){
      	//	$(window.document).find("#privilege").show();//显示节点设置的按钮
      	//	$("#td_menu").remove();
      	//	$("#td_iframe_nextstep").remove();
      	}else if(($("#workflowName").val() == "公文转办流程" && ($("#isReceived").val() == null || $("#isReceived").val()=="") && ($("#currentNodeName").val() != "主办人员拟办" && $("#currentNodeName").val() != "秘书处盖章")) || ($("#workflowName").val() == "征求意见流程" && ($("#isReceived").val() == null || $("#isReceived").val()=="")  && ($("#currentNodeName").val() != "主办人员拟办" && $("#currentNodeName").val() != "秘书处盖章"))){
      		//alert("2");
      		$(window.document).find("#privilege").show();//显示节点设置的按钮
      		$("#td_menu").remove();
      		$("#td_iframe_nextstep").remove();
      	}else{
      	//如果是办理按钮触发的是弹出窗口,则不加载此Iframe
      	//alert("3");
      	var isPopWin = $("#toNext").attr("isPop");
      	var isMenuWin = $("#toNext").attr("isMenuButton");//是否为菜单模式
      	var isOpenWin = $("#toNext").attr("isOpen");//是否为展开模式
      	if(isPopWin != "1"){//非弹出
      		document.getElementById("iframe_nextstep").src = contextPath + "!workflow.action?formId="+formId+"&taskId="+taskId
      								+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
      								+"&businessName="+encodeURI(encodeURI($("#businessName").val()))
      								+"&time="+new Date()+"&number="+Math.random();//添加时间戳和随机数去除IE缓存
      		if(isMenuWin == "1"){
      			$("#toNext").hide();
      			$("#menu").show();
      			HiddenIframe();
      		}else{
      			if(isOpenWin =="1"){
      				showAllDiv();
      				HiddenIframe();
      			}else{
	      			showAllDiv();
		      		OpenIframe();
      			}
      		}
      	} else {
      		$(window.document).find("#privilege").show();//显示节点设置的按钮
      		$("#td_menu").remove();
      		$("#td_iframe_nextstep").remove();
      	}
      	}
      }

		//由eform.js中定义的doNext()回调
		function goBack(){
			refresh = true;
//			alert("发送成功！");
			var parentWin = window.opener;//父窗口
			if(typeof(parentWin.reloadPage) != "undefined"){
				parentWin.reloadPage();
			}
	   		//window.returnValue = "OK" ;
	      	window.close();
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
	    
		//导出正文  shenyl   2011.9.16
		function saveContent(){			
			try{
		   		TANGER_OCX_OBJ.ShowDialog(2);      
		  	}catch(e){}
		}
		
	    function TANGER_OCX_ShowDialog(dType){
	    	try{
		      	TANGER_OCX_OBJ.ShowDialog(dType);      
	      	}catch(e){}
	    	}
	    
	    function TANGER_OCX_PrintDoc(booValue) {
	    	//判断文档是否被激活  
	    	if(TANGER_OCX_OBJ==""){
	    		initWord();//激活文档
	    	}
	      try{
		      TANGER_OCX_OBJ.PrintOut(booValue);      
	      }catch(e){}
	    }
    
    	//进入或退出痕迹保留状态
		function TANGER_OCX_SetMarkModify(boolvalue) {
		
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){					
							UserName = $("#userName").val();				
						}
					}		
			    	//TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = true;
			    	TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
		    	}catch(e){}
			}
			//TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
		}
		
		//显示/不显示痕迹
		function TANGER_OCX_ShowRevisions(boolvalue) {
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
				var flag = TANGER_OCX_OBJ.IsReadOnly;
				if(flag){
					TANGER_OCX_OBJ.setReadOnly(false,"");
				}
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){					
							UserName = $("#userName").val();				
						}
					}
				TANGER_OCX_OBJ.ActiveDocument.ShowRevisions = boolvalue;
				if(flag){
					TANGER_OCX_OBJ.setReadOnly(flag,"");
				}			
		    	}catch(e){}
			}
		}
		
		//清除痕迹
		function TANGER_OCX_AcceptAllRevisions(){
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){					
							UserName = $("#userName").val();				
						}
					}			
				TANGER_OCX_OBJ.ActiveDocument.AcceptAllRevisions();//接受所有的修订
		    	}catch(e){}
			}
		}
		
		//从本地增加图片到文档指定位置
		//是否浮动文件,true为浮动，false为非浮动
		function TANGER_OCX_AddPicFromLocal(value){
		    var ret = TANGER_OCX_OBJ.AddPicFromURL("", //路径
														true,//是否提示选择文件
														value,//是否浮动图片
														100,//如果是浮动图片，相对于左边的Left 单位磅
														100,
														1,
														100,
														1); //如果是浮动图片，相对于当前段落Top	
		}
		
		//插入模板
        function TANGER_OCX_AddTemplateFromURL(){
        	//alert("插入模板");
        	//如果当前文档为只读，则无法套红
        	if(formReader.GetOfficeControl(formReader.TabSelectedName)){
        		TANGER_OCX_OBJ = formReader.GetOfficeControl(formReader.TabSelectedName);
        	}
			if(readonly != "false"){
				var r = readonly.split(",");
				for(i=1 ; i<r.length; i++){
					var a = r[i].split("|");
					//匹配office控件名
					if(formReader.TabSelectedName == a[0] && a[1] == "true" ){
						alert("当前文档已被设置为只读，因此无法套红。");
						return;
					}
				}
			}
        	if(window.confirm("文件套红是否要清除痕迹？")){
        		addTemplateFromURL(true);
        	}else{
        		addTemplateFromURL(false);
        	}
        }
        function addTemplateFromURL(isClearRevision){
        	var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!templateTree.action","400", "350", window);
           if(ReturnStr){
           		if(isClearRevision){
           			//清除痕迹
			        TANGER_OCX_AcceptAllRevisions();
           		}
				if(!TANGER_OCX_OBJ){//校验是否存在WORD
					return ;
				} 
				if($("#taskId").val() != ""){
					//保存WORD内容到本地
					TANGER_OCX_OBJ.SaveToLocal("c:\\tempword\\temp.doc",true);
				}
				//清空word内容
				var curSel = TANGER_OCX_OBJ.ActiveDocument.Application.Selection;
				curSel.WholeStory();
				curSel.Cut();

				//查找标签与电子表的映射关系.
				$.getJSON("<%=root%>/senddoc/sendDoc!readBookMarkInfo.action?timestamp="+new Date(),{formId:$("#formId").val()},function(ret){
					if(ret == "-1"){
						alert("读取标签与电子表单映射时异常。");
						return ;
					}
					TANGER_OCX_OBJ.Activate(false);// 被叫方拒绝接收呼叫
					//是否要清除痕迹
					if(isClearRevision){
						TANGER_OCX_OBJ.OpenFromURL("<%=basePath%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr); 
	           		}else{
	           			TANGER_OCX_OBJ.AddTemplateFromURL("<%=basePath%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr); 
	           		}
	           		var doc =TANGER_OCX_OBJ.ActiveDocument;
					var bks = doc.Bookmarks;
					var bksCount = bks.Count;
					var contentBookMarkName = "" ;//正文的标签名称
					for(i=1;i<=bksCount ;i++){
						try{
							var name = bks(i).Name ;
							var value = TANGER_OCX_OBJ.GetBookmarkValue(name);
							TANGER_OCX_OBJ.SetBookmarkValue(name,"");//先清空原书签的值
							if(value.indexOf("正文")!=-1){
								contentBookMarkName = name ;
							}else{
								/*
								$.each(ret,function(i,json){
									if(name == json.bookMarkName){
									var controlValue = formReader.GetFormControl(json.componentName).Value;
		
									TANGER_OCX_OBJ.SetBookmarkValue(name,controlValue);
									}
								});*/
								
								$.each(ret,function(i,json){
									if(name == json.bookMarkName){
										var control = formReader.GetFormControl(json.componentName);
										if(control != null){
											var controlValue = control.Value;
											if(control.GetProperty("SelectedName")){//下拉列表
												controlValue = control.SelectedName;
											}
											TANGER_OCX_OBJ.SetBookmarkValue(name,controlValue);
										}
									}
								});
							}
						}catch(e){}
					} 
					if(contentBookMarkName != ""){//存在正文标签
						//设置正文内容
						var bkmkObj = TANGER_OCX_OBJ.ActiveDocument.BookMarks(contentBookMarkName);	
						var saverange = bkmkObj.Range
						saverange.Paste();
						TANGER_OCX_OBJ.ActiveDocument.Bookmarks.Add(contentBookMarkName,saverange);	

					}
				});
           }
        }
		
		//全屏手写签名
		function TANGER_OCX_DoHandSign2(){
			var ret = TANGER_OCX_OBJ.DoHandSign2 (TANGER_OCX_Username,//当前登录用户 必须
													  "", //SignKey
													  0,//left//可选参数
													  0,//top
													  0,//relative=0，表示按照屏幕位置批注
													  100); //缩放100%，表示原大小
		}
		
		//全屏手工绘图，与以上区别，不添加验证信息
		function TANGER_OCX_DoHandDraw2(){
		  var ret = TANGER_OCX_OBJ.DoHandDraw2(0,//left//可选参数
											       0,//top
											 	   0,//relative=0，表示按照屏幕位置批注
											       100); //缩放100%，表示原大小
		}
		
		//加盖本地电子印章
		function TANGER_OCX_AddSignFromLocal(){		
			try{
				var ret = TANGER_OCX_OBJ.AddSignFromLocal(TANGER_OCX_Username,
															 "",
															 true,
															 0,
															 0,
															 "", 
															 1,
															 100,
															 1);
			}catch(e){}	
		}
		
		function TANGER_OCX_CreateBarCode(){
			$.post("<%=root%>/senddoc/sendDoc!getBarParam.action",{tableName:$("#tableName").val(),pkFieldName:$("#pkFieldName").val(),pkFieldValue:$("#pkFieldValue").val()},
				function(barParam){
					var BarCodePath = "";
					//把界面转到word文档编辑页面,现在使用表单二版，此方法有待改进，目前不存在
					//var FormInputOCX = document.getElementById("FormInputOCX");
					//FormInputOCX.SetOfficePageActive();
					var PDF417Manager = document.getElementById("PDF417Manager");
					PDF417Manager.LimitWidth(0, 0);
           		 	PDF417Manager.LimitHeight(0, 0);
					PDF417Manager.CopyRight = "金格公文二维条码中间件[演示版]";
           			//设置图片宽度和高度范围,
            		//设置最小和最大均为0，则使用当前图片宽度,高度自动设置
           			//var minWidth = 150;
            		//var maxWidth = 200;
            		//var minHeight = 30;
            		//var maxHeight = 60;
            		var minWidth = 280;
            		var maxWidth = 560;
            		var minHeight = 240;
            		var maxHeight = 300;
            		PDF417Manager.LimitWidth(maxWidth, minWidth);
            		PDF417Manager.LimitHeight(maxHeight, minHeight);
  					//设置图片宽度，实际宽度将接近且不大于设置值；
  					//设置为0，则使用当前图片宽度,高度自动设置
  					//PDF417Manager.BestWidth = 300;
  					barParam = barParam + "[自定义字段]^|";
  					mResult = PDF417Manager.SetBarCodeString(barParam,false);	
					BarCodePath = PDF417Manager.GetBarCodeFile(".gif");
		  			//alert(PDF417Manager.GetImgWidth());  //获取条码图片宽度
		  			//alert(PDF417Manager.GetImgHeight());  //获取条码图片高度
		  			//alert(BarCodePath);  //获取条码图片数据_路径
		  			alert("二维码信息~~~：" + barParam);
					TANGER_OCX_OBJ.AddPicFromLocal(BarCodePath,
											 	  false,
										 		  false,
										 		  0,
												  0);
				});
		}
		var state = 0 ;
		
		//隐藏显示文件操作菜单栏
		function hidemenu(){
			if(state == 0){
				state = 1;
				CloseIframe();
			}else if(state == 1){
				state = 0;
				OpenIframe();
			}
		}
		
		function workflowView(){
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-processedstatus.jsp?instanceId="+$("#instanceId").val()+"&taskId="+$("#taskId").val(),'Cur_workflowView',width, height, "办理记录");
      }
      
      //在表单模板的标题控件中OnChange事件中调用此事件
      function setTitle(title){
      	try{
      		title = EE_Filter(title);
	        $("#businessName").val(title);
	      	document.getElementById('iframe_nextstep').contentWindow.document.getElementById("handlerMes").value = "工作提醒：请查阅《"+title+"》。${userName}";
	        /*yanjian 2011-11-28 14:03  处理标题中存在的特殊字符 */
	        var formControl = formReader.GetFormControl($("#tableName").val(),"WORKFLOWTITLE");//标题
	        var currentSelectionStart = formControl.SelectionStart ;//获取重新设置属性前，控件的光标位置
	        formControl.Value = title;		//重新设置属性光标会自动移到控件末尾
	        formControl.SelectionStart = currentSelectionStart;//将控件的光标位置重新定位到currentSelectionStart位置
      	}catch(e){
      	}
      }
      
      //模板加载完成后调用此事件
      //在拟稿环节,需要控制哪些意见控件可以输入意见
      function AfterLoadFormTemplate(){
      	if(taskId == ""){
      		var firstNodeControlName = '${firstNodeControlName}';
      		if(firstNodeControlName != ""){
      			var AuditOptionControl = formReader.GetFormControl(firstNodeControlName);
 				//这里设置意见控件的笔形图片显示
 				var suggestionStyle = $("#suggestionStyle").val(); //意见输入模式为笔形图标的时候才显示笔形 				
 				if(suggestionStyle != "1"){
	 				if(AuditOptionControl != null && AuditOptionControl != ""){
	 					AuditOptionControl.ShowAddButton = true;
	 				}
 				}
      		}
      	}
      }
      
      //模板数据加载完成后调用此函数(在拟稿环节加载意见)
      function AfterLoadFormData(){
      	if(taskId == "" && instanceId == ""){
      		var actionUri = basePath +  "senddoc/eFormTemplate.action";
 			formReader.LoadAuditOpinion(actionUri, $("#pkFieldValue").val(), "");
      	}
      }
      
      function OpenIframe(){
      	$("#td_iframe_nextstep").width("30%");
		$("#iframe_nextstep").show();
		if(document.pic){
			document.pic.src="<%=root%>/images/ico/jiantou_2.jpg";
			document.pic.title="点击隐藏菜单栏";
		}
      }
      
      function CloseIframe(){
      	$("#td_iframe_nextstep").width("0");
		$("#iframe_nextstep").hide();
		if($("#toNext").attr("isMenuButton") == "1"){
			$("#td_menu").hide();
		}
		if(document.pic){
			document.pic.src="<%=root%>/images/ico/jiantou.jpg";
			document.pic.title="点击打开菜单栏";
		}
      }
      
      function HiddenIframe(){
      	$("#td_iframe_nextstep").width("0");
		$("#iframe_nextstep").hide();
		$("#td_menu").hide();
		//$("#HiddenShow").hide();
      }
      
       function ShowIframe(){
      	$("#td_iframe_nextstep").width("30%");
		$("#iframe_nextstep").show();
		$("#td_menu").show();
      }
      
      //menu模式下按钮点击触发事件
      function selecttransGroupById(groupid){
      	  var enable = false;
      	  if(typeof(setEnableYjzx) != "undefined"){
      	  	setEnableYjzx(groupid);
      	  	enable = isEnableYjzx();
      	  }
	      if(enable){//意见征询 省办公厅需求
	       HiddenIframe();
	       //hideAllDiv();
			// $(window.frames["iframe_nextstep"].document).find("#div_"+groupid).show();
			 //$(window.frames["iframe_nextstep"].document).find("#h3_"+groupid).hide();
			 $(window.frames["iframe_nextstep"].document).find("#input_"+groupid).click();
	       //$("#td_menu").hide();
      		// state = 1;
	      }else{
      		 $("#td_menu").show();
      		 state = 0;
    		 $("#hideBgtTags").click();
      		 //$("#HiddenShow").show();
      		 OpenIframe();
			 hideAllDiv();
			 $(window.frames["iframe_nextstep"].document).find("#div_"+groupid).show();
			 $(window.frames["iframe_nextstep"].document).find("#h3_"+groupid).hide();
			 $(window.frames["iframe_nextstep"].document).find("#input_"+groupid).click();
	      }
		}
		
		//隐藏迁移线信息 
		function hideAllDiv(){
			$(window.frames["iframe_nextstep"].document).find("div[id^='div_']").hide();		
		}
		
		function showAllDiv(){
			$(window.frames["iframe_nextstep"].document).find("div[id^='div_']").show();		
		}
      
      	function OpenSubmit(){
      		 $("#Open_Submit").hide();
      		 $("#Open_Submits").show();
      		 ShowIframe();
      	}
      	
      	function hideSubmit(){
      		 $("#Open_Submits").hide();
      		 $("#Open_Submit").show();
      	     HiddenIframe();
      	}
      
      	function getInputCkecked(){
      		
      	}
      
      var objId = null;
      
      //删除意见
      var bIsDeleteSuggestion = false;
      //退回流程
      var isBackTask = "1";
      //添加意见,在控件中调用
      //@param objAuditOpinion 意见控件名称
      //@param recordId		   记录id
      //@param opinion		   意见内容	
      function toSuggestion(objAuditOpinion){
      	if(taskId == ""){//未进入流程
      		SaveSuggestionInDraft(objAuditOpinion);
      	} else {
      		SaveSuggestionInWorkflow(objAuditOpinion);
      	}
      }
      
	   //编辑意见
      function toEditSuggestion(objAuditOpinion,recordId,opinion,actorId){ 
      	var suggestionStyle = $("#suggestionStyle").val(); //意见输入模式不为笔形图标的时候，不能双击进行编辑
		if(suggestionStyle == "1"){
			return;
		}
      	if(taskId == ""){//未进入流程时编辑意见
      	    if(actorId != '${userId}'){//不是当前用户填写的意见不能修改
      			return ;
      		}
      		EditSuggestionInDraft(objAuditOpinion,recordId,opinion,actorId);
      	} else {//进入流程时编辑意见
      		var recId = "";
      		//得到临时意见表中该记录ID的意见
      		$.getJSON("<%=root%>/senddoc/sendDocWorkflow!findSuggestionInDraftByAid.action?timeStamp="+new Date(),
 				{aid:recordId},function(json){
 				var status = json.status;
 				if(status == "-1"){
 					alert("对不起，系统发生错误，请与管理员联系。");
 					return;
 				} else if(status == "0"){//找到了记录
 				    if(actorId != '${userId}' ){
 				    //不是当前用户填写的意见不能修改
 				    	if(json.atoPersonId != '${userId}' ){
 				    	//当前用户不是委托人,意见不允许编辑
      						return ;
 				    	}
      				}
					//不是当前任务的意见
      				if(json.taskId != taskId){
      					//不是退回的任务或者不是自己的意见，不允许编辑
      					if(isBackTask != "<%=isBackTask%>" || actorId != '${userId}'){
	      					return;
      					}
      				}
		      		EditSuggestionInTask(objAuditOpinion,recordId,opinion,actorId);
 				} else if(status == "1"){//未找到记录
		      		//退回任务并且是当前用户写的意见允许编辑
					if(isBackTask == "<%=isBackTask%>" && actorId == '${userId}'){
		      			EditSuggestionInTask(objAuditOpinion,recordId,opinion,actorId);
		      		}else{
		      			return;
		      		}
 				}
 			});
      	}
      }
  	  //删除意见
      function DeleteSuggestion(objAuditOpinion,recordId){
      	formReader.GetFormControl(objAuditOpinion).RemoveOpinionItem(recordId);
      	bIsDeleteSuggestion = true;
      }
      
      //提交流程之前的意见处理
      function SaveSuggestionInDraft(objAuditOpinion){
      	if($("#pkFieldValue").val() == ""){//新建流程
      		AddSuggestionInCreateWorkflow(objAuditOpinion);
      	} else {//编辑草稿
      		AddSuggestionInDraft(objAuditOpinion);
      	}
      }
      
      //在草稿中编辑或添加意见
      function AddSuggestionInDraft(objAuditOpinion){
      	//校验是否填写了意见
      	$.getJSON("<%=root%>/senddoc/sendDocWorkflow!findSuggestionInDraftByBid.action?timeStamp="+new Date(),
 				{bussinessId:$("#pkFieldValue").val(),userId:'${userId}'},function(json){
 				var status = json.status;
 				if(status == "-1"){
 					alert("对不起，系统发生错误，请与管理员联系。");
 				} else if(status == "0"){//找到了记录
 					EditSuggestionInDraft(objAuditOpinion,json.aiId,json.aiContent,'${userId}');
 				} else if(status == "1"){//未找到记录
 					AddSuggestionInCreateWorkflow(objAuditOpinion);
 				}
 		});
      }
      
       //在办理流程意见处理
      function SaveSuggestionInWorkflow(objAuditOpinion){
      	//校验是否填写了意见
      	    $.getJSON("<%=root%>/senddoc/sendDocWorkflow!findSuggestionInDraftByBid.action?timeStamp="+new Date(),
 				{bussinessId:taskId},function(json){
 				var status = json.status;
 				if(status == "-1"){
 					alert("对不起，系统发生错误，请与管理员联系。");
 				} else if(status == "0"){//找到了记录
 					EditSuggestionInTask(objAuditOpinion,json.aiId,json.aiContent,'${userId}');
 				} else if(status == "1"){//未找到记录
 					AddSuggestionInWorkflow(objAuditOpinion);
 				}
 		});
      }
      
      //新建流程时添加意见
      function AddSuggestionInCreateWorkflow(objAuditOpinion){
      	var data = document.getElementById("suggestion").value;
      	var width=(screen.availWidth-10)/2;
	    var height=(screen.availHeight-30)/2;
	    var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!toSuggestion.action", 
	                                   width, height, new Array($("#toNext").attr("nodeName"),data));
	    if(ReturnStr != null){	                                   
	        ReturnStr = EE_Filter(ReturnStr);
	        var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.format(new Date())%>";
	        var approveData = "{aiControlName:'" + objAuditOpinion + "',aiContent:'" + ReturnStr+"',aiDate:'"+strDate+"',atDate:''}";
	        
	        //异步保存意见到临时意见表
	        if($("#pkFieldValue").val() != null && $("#pkFieldValue").val() != ""){
		        $.post("<%=root%>/senddoc/sendDocApproveinfo!saveApproveinfo.action",{businessName:approveData,businessId:$("#pkFieldValue").val(),userId:'${userId}'},function(ret){
			        if(ret == "0"){
			        }else {
	        			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系。","",3);//0:提示
	        			return;
	        		}
	       		});
	        }
	       	document.getElementById("approveData").value = approveData;
	       	document.getElementById("suggestion").value = ReturnStr.replace(new RegExp("＜BR＞","gm"),"\r\n");
			if(ReturnStr == ""){//为空时表示删除意见
				DeleteSuggestion(objAuditOpinion,'${userId}');
			}
	       	if(taskId == "" && instanceId == "" && $("#pkFieldValue").val() != "" && $("#pkFieldValue").val() != null){
	    		var actionUri = basePath +  "senddoc/eFormTemplate.action";
				formReader.LoadAuditOpinion(actionUri, $("#pkFieldValue").val(), "");
	      	}else{     	
		       	var info = "鼠标左键双击处理人，可以查看或修改意见。\r\n";
		       	info += "处理人：" + '${userName}\r\n';
		       	info += "录入时间：" + strDate + "\r\n";
		       	info += "所在部门：${orgName}";
		       	var userName = "${userName}";
		       	if(objAuditOpinion.indexOf("showdep")>-1){
		       		userName = "[${orgName}]  "+userName;
		       	}   
		      	if(data == ""){
					if(ReturnStr != null && ReturnStr != ""){
			        	formReader.GetFormControl(objAuditOpinion).AddOpinionItem('${userId}', ReturnStr.replace(new RegExp("＜BR＞","gm"),"\r\n"),  '${userId}', userName,strDate , info);
			        	bIsDeleteSuggestion = false;
					}	                                   
		      	} else {					
		        	if(ReturnStr != ""){
		        		DeleteSuggestion(objAuditOpinion,'${userId}');
	   					formReader.GetFormControl(objAuditOpinion).AddOpinionItem('${userId}', ReturnStr.replace(new RegExp("＜BR＞","gm"),"\r\n"),  '${userId}', userName, strDate, info);
	  				}
		      	}
	     	}
     	}
      }
      
       //在草稿中编辑意见
      function EditSuggestionInDraft(objAuditOpinion,recordId,opinion,actorId){
      	var width=(screen.availWidth-10)/2;
        var height=(screen.availHeight-30)/2;
        var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!toSuggestion.action", 
                                   width, height, new Array($("#toNext").attr("nodeName"),opinion,recordId,objAuditOpinion));   
    	if(ReturnStr != null){
			ReturnStr = EE_Filter(ReturnStr);
	        var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.format(new Date())%>";
	        var approveData = "{aiControlName:'" + objAuditOpinion + "',aiContent:'" + ReturnStr+"',aiDate:'"+strDate+"',atDate:''}";
	        //异步保存意见到临时意见表
	        if($("#pkFieldValue").val() != null && $("#pkFieldValue").val() != ""){
		        $.post("<%=root%>/senddoc/sendDocApproveinfo!saveApproveinfo.action",{businessName:approveData,businessId:$("#pkFieldValue").val(),userId:'${userId}'},function(ret){
			        if(ret == "0"){
			        
			        }else {
	        			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系。","",3);//0:提示
	        			return;
	        		}
	       		});
	        }
	        document.getElementById("approveData").value = approveData;
     		document.getElementById("suggestion").value = ReturnStr.replace(new RegExp("＜BR＞","gm"),"\r\n");
			if(ReturnStr == ""){//为空时表示删除意见
				DeleteSuggestion(objAuditOpinion,recordId);
			}	
			if($("#pkFieldValue").val() != ""){
      			var actionUri = basePath +  "senddoc/eFormTemplate.action";
 				formReader.LoadAuditOpinion(actionUri, $("#pkFieldValue").val(), "");
      		}else{
		       	var info = "鼠标左键双击处理人，可以查看或修改意见。\r\n";
		       	info += "处理人：" + '${userName}\r\n';
		       	info += "录入时间：" + strDate + "\r\n";
		       	info += "所在部门：${orgName}";
		       	
		       	var userName = "${userName}";
		       	if(objAuditOpinion.indexOf("showdep")>-1){
		       		userName = "[${orgName}]  "+userName;
		       	}
		       	if(ReturnStr != ""){
		       		DeleteSuggestion(objAuditOpinion,'${userId}');
	   				formReader.GetFormControl(objAuditOpinion).AddOpinionItem('${userId}', ReturnStr.replace(new RegExp("＜BR＞","gm"),"\r\n"),  '${userId}', userName, strDate, info);
				}
      		}
		}
      }

      //在流程中添加意见
      function AddSuggestionInWorkflow(objAuditOpinion){
		var data = document.getElementById("suggestion").value;
      	var width=(screen.availWidth-10)/2;
	    var height=(screen.availHeight-30)/2;
	    var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!toSuggestion.action", 
	                                   width, height, new Array($("#toNext").attr("nodeName"),data));
		ReturnStr = EE_Filter(ReturnStr);
        var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.format(new Date())%>";
        var approveData = "{aiControlName:'" + objAuditOpinion + "',aiContent:'" + ReturnStr+"',aiDate:'"+strDate+"',atDate:''}";
        //异步保存意见到临时意见表
        $.post("<%=root%>/senddoc/sendDocApproveinfo!saveApproveinfo.action",{businessName:approveData,businessId:taskId,userId:'${userId}'},function(ret){
	        if(ret == "0"){
			    document.getElementById("approveData").value = approveData;
		       	document.getElementById("suggestion").value = ReturnStr;
		       	if($("#instanceId").val()!=""){
					insIdAndTasId = $("#instanceId").val() + "," + $("#taskId").val();
					//alert(insIdAndTasId);
				 	var actionUri = basePath +  "senddoc/eFormTemplate.action";
				 	formReader.LoadAuditOpinion(actionUri, insIdAndTasId, "");
				} 
	        }else {
       			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系。","",3);//0:提示
       			return;
       		}
      	});           
      }
      
      //在流程中编辑意见
      function EditSuggestionInTask(objAuditOpinion,recordId,opinion,actorId){
		var width=(screen.availWidth-10)/2;
        var height=(screen.availHeight-30)/2;
        var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!toSuggestion.action", 
                                   width, height, new Array($("#toNext").attr("nodeName"),opinion,recordId,objAuditOpinion));       
    	if(ReturnStr != null){
			ReturnStr = EE_Filter(ReturnStr);
	        var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.format(new Date())%>";
	        var approveData = "{aiControlName:'" + objAuditOpinion + "',aiContent:'" + ReturnStr+"',aiDate:'"+strDate+"',atDate:''}";
	        //异步保存意见到临时意见表
	        $.post("<%=root%>/senddoc/sendDocApproveinfo!saveApproveinfo.action",{businessName:approveData,businessId:taskId,userId:'${userId}',recordId:recordId},function(ret){
		        if(ret == "0"){
		        	document.getElementById("approveData").value = approveData;
			       	document.getElementById("suggestion").value = ReturnStr; 
					if(ReturnStr == ""){//为空时表示删除意见
						DeleteSuggestion(objAuditOpinion,recordId);
					}					
			       	if($("#instanceId").val()!=""){
						insIdAndTasId = $("#instanceId").val() + "," + $("#taskId").val();
						//alert(insIdAndTasId);
					 	var actionUri = basePath +  "senddoc/eFormTemplate.action";
					 	formReader.LoadAuditOpinion(actionUri, insIdAndTasId, "");
					}
		        }else {
	       			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系。","",3);//0:提示
	       			return;
	       		}
	      	});
	     }                          
      }   
      //session失效前提醒保存表单
      function autoSave(){     
     	 var maxtime =document.getElementById("timeout").value;
        //alert(maxtime);
      	if(maxtime>60){
      		maxtime = maxtime-60;
      		document.getElementById("timeout").value = maxtime;
      		//minutes = Math.floor(maxtime/60);   
			//seconds = Math.floor(maxtime%60);   
			//msg = "距离会话结束还有"+minutes+"分"+seconds+"秒"; 
			//alert(msg); 
			if(maxtime <= 60){
				var currentId= $("#currentId").val();
				var currentId2= getCurrntId();
				if(currentId2=="-1"){/**添加了判断是否是当前用户  niwy*/
					alert("用户已退出系统，请关闭表单。");
				}else{
					if(currentId!=currentId2){
						alert("该表单处理人已经退出了该系统。")
						window.close();
					}else{
						
					clearInterval(res);		
					var actionUri = basePath +  "senddoc/eFormTemplate.action";
					document.getElementById("formAction").value = "SaveFormData";
					if(taskId == ""){
						state = "0";
					}else{
						state = "1";
					}
					document.getElementById("workflowState").value=state;
					if(formReader.SaveFormData(actionUri,"form","saveFormDataRequestFinished", false)){
						//方法调用成功
						if(typeof(CloseIframe)!="undefined"){
							CloseIframe();
						}
					} else {
						//调用失败
					}												
			} 		
				}}
      	}
      }
      
      //保存模板数据完成事件
		function saveFormDataRequestFinished(bResult, strResult,strDetail){
			if(bResult){
				//保存成功
				alert('距离会话结束还有1分钟,已自动保存表单数据。');   
			} else {
				//保存失败
				formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系。",strDetail,3);//0:提示
				if(typeof(OpenIframe) != "undefined"){
					OpenIframe();
				}
			}
		}
      
      var res = setInterval("autoSave()",60000);
      
       //领导批示
      function lingdaopishi(){
      		if($("#bussinessId").val()==""){
      			alert("流程未启动，不能进行此操作。");
      			return;
      		}
	     	var width=(screen.availWidth-10)/2;
	       var height=(screen.availHeight-30)/2;
	       var ReturnStr=window.open("<%=root%>/senddoc/sendDocUpload!upload.action?bussinessId="
	       									+$("#bussinessId").val()+"&attachName="+ $("#attachName").val()
	       									+"&doneSuggestion="+$("#doneSuggestion").val(), 
	                                width, height, "");
	                                /*
	        $("#attachName").val(ReturnStr[0]);
	        $("#doneSuggestion").val(ReturnStr[1]);
	        */
      /*
      	var r = true;
      	if($("#doneSuggestion").val() !="" && $("#attachName").val() !=""){
      		r=confirm("您已经上传过相关信息，是否要继续修改相关信息!");
      	}
      	if (r==true) {
			var width=(screen.availWidth-10)/2;
	        var height=(screen.availHeight-30)/2;
	        var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDocUpload!upload.action?bussinessId="
	        									+$("#bussinessId").val()+"&attachName="+ $("#attachName").val()
	        									+"&doneSuggestion="+$("#doneSuggestion").val(), 
                                  width, height, "");
            $("#attachName").val(ReturnStr[0]);
            $("#doneSuggestion").val(ReturnStr[1]);
		}	
		*/
      }
      
	/**
    * 显示领导意见和办结意见选项卡并作初始化 by yanjian
    * */
   function showBgtTagsbak(){
   		formReader.SetFormTabVisibility("attachName",true);
		formReader.SetFormTabVisibility("doneSuggestion",true);
		if( formReader.GetFormControl("attachName_content") && formReader.GetFormControl("doneSuggestion_content")){
			formReader.GetFormControl("attachName_content").Required = true;
			formReader.GetFormControl("doneSuggestion_content").Required = true;
		}
   }
   
    /**
    * 隐藏领导意见和办结意见选项卡并作初始化 by yanjian
    * */
   function hideBgtTagsbak(){
   		formReader.SetFormTabVisibility("attachName",false);
		formReader.SetFormTabVisibility("doneSuggestion",false);
		if( formReader.GetFormControl("attachName_content") && formReader.GetFormControl("doneSuggestion_content")){
			formReader.GetFormControl("attachName_content").Required = false;
			formReader.GetFormControl("doneSuggestion_content").Required = false;
		}
   }
   
		/**
		 * author:luosy
		 * description: 直接结束流程
		 *	instanceId 流程实例Id
		*/
		function endProcessInstance(){
			var instanceId =$("#instanceId").val();
			if(confirm("确定要直接结束流程吗？")){
	   		 	$.post("<%=root%>/senddoc/sendDoc!endProcessInstance.action",
				{instanceId:instanceId},
				function(data){
				    if(data=="true"){
				       alert("结束流程成功。");
					   window.opener.location.reload();
	   		 		   window.close();
				    }else{
						alert("直接结束流程失败。");
						return;
					}
				});
	   		 }
		}
		   
   	   //废除公文,挂起流程实例
      function repeal(){
       	var bussinessId = $("#bussinessId").val();
		var instanceId =$("#instanceId").val();
   		 	if(confirm("确定要废除吗？")){
	   		 	$.post("<%=root%>/senddoc/sendDoc!repealProcess.action",
				{instanceId:instanceId,bussinessId:bussinessId},
				function(data){
				    if(data=="true"){
				       alert("废除成功。");
					   window.opener.location.reload();
	   		 		   window.close();
				    }else{
						alert("废除失败。");
						return;
					}
				});
	   		 }else {
	   		 	return
	   		 } 
      } 
   // yanjian 设置流程期限
   	function setProcessTimeOut(){
   		var url = "<%=path%>/fileNameRedirectAction.action?toPage=senddoc/processTimeOut.jsp?processOutTime="+$("#processOutTime").val();
		var a = window.showModalDialog(url,window,'dialogWidth:550px;dialogHeight:240px;help:no;status:no;scroll:no');
		if(typeof(a) != "undefined"){
			$("#processOutTime").val(a);
			$("#td_clearprocessTimeOut").show();
			$("#td_processTimeOut").hide();
		}
   	}
   
   /*
    *设置流程期限,该方法不负责进行日期格式的验证
    *日期格式，如：2012-12-06 16:58:31
    */
   function setProcessTimer(timer){
   	 $("#processOutTime").val(timer);
   }
   
   function clearProcessTimeOut(){
   		$("#processOutTime").val("");
   		alert("流程期限时间设置删除完毕。");
   		$("#td_clearprocessTimeOut").hide();
   		$("#td_processTimeOut").show();
   }
   //遮盖办理按钮区域
   function maskPrivilege(){
   		$("body").mask("");
   }
    //去除办理按钮区域遮盖
   function unmaskPrivilege(){
   		$("body").unmask();
   }

   //签发
   function qianfa(){
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
			alert("word正文加载失败，请关闭该页面，重新打开办理。\n如有问题，请联系系统管理员！\n");
			return;
		}
	   if (typeof(doNextBefore) != "undefined") { //表单数据提交之前的进行数据处理操作
		   if(doNextBefore() == false){
				return;
			}
		}
		// 保存PDF临时文件
		saveformiframe();

		var height = 473; // screen.availHeight-50;
		var width = 600; // screen.availWidth/2;
		var returnValue = "-1"; // 提交流程以后的返回值
		// 验证表单必填项是否已经输入了
		var validateResult = formReader.CheckFormData(true);
		if (!validateResult) { // 验证不通过
			return;
		}
		var isSuggestionButton = $("#toNext").attr("isSuggestionButton"); // 是否快捷办理按钮
		if (document.getElementById('iframe_nextstep')) { // 弹出窗口模式时,此对象不存在
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
				.doPreSubmit()) { // 工作流选择验证
				OpenIframe();
				return;
			}
			if (isSuggestionButton == "1") {
				if (typeof(objThis) != "undefined") {
					if (typeof($("#iframe_nextstep").contents().find("suggestion")) != "undefined") {
						$("#iframe_nextstep").contents().find("suggestion").val(objThis.buttonName);
					}
					var suggestion = document.getElementById("suggestion").value;
					if (suggestion == null || suggestion == "") {

						document.getElementById("suggestion").value = objThis.buttonName;
					}
				}
			}
		}
		var isPopWin = $("#toNext").attr("isPop"); // 是否弹出窗口处理
		var flagnext = true;
		if (!draftRequiredVerification()) {
			alert("对不起，请先保存草稿！");
			flagnext = false;
			return;
		}
		if (flagnext) {
			//点签发更新来文时间为当前时间
			//var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd").format(new Date())%>";
			//formReader.GetFormControl("DateTimePicker2").Value = strDate;//注释掉这两行，签发的时候不需要改变拟办日期  heqj 2014年2月24日
			$("body").mask(""); //数据提交之后 遮盖整个办理界面
			// 兼容早期调用getTitle函数
			businessName = $("#businessName").val();
			//if (businessName == "") { //modify yanjian 2012-07-06 09:56 去除该判断
			var formControl = formReader.GetFormControl(
				$("#tableName").val(), "WORKFLOWTITLE"); // 标题
			if (formControl) {
				businessName = formControl.Value;
			} else {
				if (!confirm("该表单标题绑定的数据字段不是【WORKFLOWTITLE】,将导致流程标题无法正常显示，是否要继续办理？")) {
					$("body").unmask();
					return;
				}
			}
			//}
			if (businessName) {
				businessName = businessName.replace(/\+/g, "%2B");
			}
			// 审批意见必填验证
			// yanjian
			// 2012-02-28
			if (true) {
				//意见增加签发标志 
				var sug = ""
				if ($("#suggestion").val() == "") {
					sug = "<签发>发";
				} else {
					sug = "<签发>" + $("#suggestion").val();
				}
				$("#suggestion").val(EE_EncodeCode(sug)); //modify
				// yanjian 2011-11-06 处理百分号问题 bug-2635
				$("#businessName").val(businessName);
				CloseIframe();
				var actionUri = basePath + "senddoc/sendDoc.action";
				document.getElementById("formAction").value = "qianfa";
				if (formReader
					.SaveFormData(actionUri, "form",
						"saveFormDataAndWorkflowRequestCompleted")) {
					// 方法调用成功
				} else {
					// 调用失败
				}
			}

		}
		// ------ End ------------
   }
   
   /**
	 * author:luosy 2014-4-3
	 * description:	判断当前页面父窗口是否存在 如不存在则提示关闭当前页面
	 * modifyer:
	 * description:
	 * @param nodeId
	 * @return
	 */
	function checkParentWindow(){
		var parentLength ;
		var dialogParentLength ;
		try{
			parentLength = $("body",opener.document).length;
		}catch(e){
			parentLength=0;
		}
		try{
			dialogParentLength = $("body",window.dialogArguments.opener.document).length ;
		}catch(e){
			dialogParentLength=0;
		}
		if(parentLength>0||dialogParentLength>0){
			//alert("parentLength:"+parentLength);
		}else{
			var alertInfo = "父窗口页面已发生变化，或被关闭，或已退出本系统，不能继续操作该页面，请重新登录打开本页面后操作!\n\n是否关闭当前页面？";
			if(confirm(alertInfo)){
				window.close();
				return false;
			}
		}
	}
	</script>
	<script language="JScript" for="TANGER_OCX" event="AfterOpenFromURL(doc)">
		doc.TrackRevisions = false; //进入痕迹保留状态
	</script>
	</script>
	<script language="JScript" for="TANGER_OCX_OBJ" event="OnCustomMenuCmd2(menuPos,submenuPos,subsubmenuPos,menuCaption,menuID)">
		alert("第" + menuPos +","+ submenuPos +","+ subsubmenuPos +"个菜单项,menuID="+menuID+",菜单标题为\""+menuCaption+"\"的命令被执行.");
	</script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;" onmousedown="checkParentWindow()"
		onload="window.focus();" onunload="resumeConSignTask();">
		<!--节点名称 -->
		<s:hidden id="currentNodeName" name="nodeName"></s:hidden>
		<input type="hidden" name="isBackspace" id="isBackspace"
			value="${isBackspace }">
		<!--初始化流程控制功能
					isShowlcqx(显示流程期限设置【0:不显示,1:显示】)
					suggestionrequired(审批意见是否必填 0：不控制必填；1：控制必填)
					showrepeal(是否启用废除功能 0:不启用；1：启用)
					modifySend(是否为签发节点 0：不是签发节点；1：是签发节点)
		-->
		<input type="hidden" id="workflowFunction" name="workflowFunction"
			isShowlcqx="${isShowlcqx }"
			suggestionrequired="${suggestionrequired}" showrepeal="${showrepeal}" modifySend="${modifySend}">
		<!--初始化office功能-->
		<input type="hidden" id="officeFunction" name="officeFunction"
			doShowRevisions="${doShowRevisions}" doMarkModify="${doMarkModify }">
		<!-- 用于pdf批注的问题 -->
		<security:authorize ifAllGranted="001-00010012">
		<input id="pdfPz" type="hidden" name ="pdfPz" value="pdfPz"/>
		</security:authorize>
		<!--是否可以上传PDF收文文件是否必填 0：不能上传；1：能上传 -->
		<input type="hidden" id="PDFFunction" name="PDFFunction"
			isPermitUploadPDF="${doPermitUploadPDF}" isPermitUploadSMJ="${doPermitUploadSMJ}" isPermitUploadLINK="${doPermitUploadLINK}" isFirstNode="${isFirstNode}">
		<form id="form" name="form"
			action="<%=root%>/senddoc/sendDoc!save.action" method="post">
			<input id="isFirstNode" name="isFirstNode" 
				value="${isFirstNode}" type="hidden" />
			<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
			<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
				<!-- 当前用户的ID-->
			<input type="hidden" id="currentId" name="currentId"  value="${currentId }"/>
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
			<!-- 启动新流程电子表单模板id -->
			<s:hidden id="newFormId" name="newFormId"></s:hidden>
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
			<!--是否签收 -->
			<s:hidden id="isReceived" name="isReceived"></s:hidden>
			<!-- 关联文件 -->
			<s:hidden id="toPiId" name="toPiId"></s:hidden>
		</form>
		<DIV id=contentborder align=center>
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="40">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr id="privilege" style="display: none;">
								<td id="lingdaopishi" style="display: none;">
									<a class="button6" href="javascript:lingdaopishi();"> 领导批示意见</a>&nbsp;
								</td>
								<td width="5"></td>
								<td id="Open_Submit" style="display: none;">
									<a class="button6" href="javascript:OpenSubmit();">显示办理界面</a>&nbsp;
								</td>
								<td id="Open_Submits" style="display: none;">
									<a class="button6" href="javascript:hideSubmit();">隐藏办理界面</a>&nbsp;
								</td>
								<td width="5"></td>
								<td id="td_startworkflow" style="display: none;">
									<a class="button6" href="javascript:showForm();">启动新流程</a>&nbsp;
								</td>
								<td width="5"></td>
								<s:if test="#request.isShowlcqx == 1">
									<td id="td_clearprocessTimeOut" style="display: none;">
										<a class="button6" href="javascript:clearProcessTimeOut();">删除流程期限</a>&nbsp;
									</td>
									<td id="td_processTimeOut">
										<a class="button6" href="javascript:setProcessTimeOut();">设置流程期限</a>&nbsp;
									</td>
									<td width="5"></td>
								</s:if>
								<p id="fileOperation" style="display: none;">
									<s:if test="#request.modifySend == \"1\"">
									<!-- 
										<td id=""  >
											<a class="Operation" style="color: black;"  href="javascript:doBackSpacePrev('退回');">&nbsp;<img
													src="<%=root%>/images/ico/cal_month.gif" width="15"
													height="15" class="img_s"> 退回&nbsp;</a>
										</td>
										<td width="5"></td>
									 -->
										<td id="" >
											<a class="button" href="javascript:qianfa();">&nbsp;签发&nbsp;</a>
										</td>
										<td width="5"></td>
									</s:if>
									${privilegeInfo}
								</p>
								<%--
								<td>
									<table id="fileOperation" style="display: none;">
										<tr>
											${privilegeInfo}
										</tr>
									</table>
								</td>
								<td width="5"></td>
								--%>
								<p id="menu" style="display: none;">
								</p>
								<%--
								<td>
								<table border="0" align="right" cellpadding="00" cellspacing="0">
									<tr id="menu" style="display: none;">
										<td >
											<a id="HiddenShow" style="display: none;" class="Operation" href="#" onclick="HiddenIframe()">隐藏办理窗口&nbsp;</a>
										</td>
										<td width="5"></td>
									</tr>
								</table>
								</td>
								<td width="5"></td>
								<s:if test="workflowName == '收文办件登记' && nodeName == '公文分发' && bussinessId != null">--%>
								<s:if test="workflowName == '收文办件登记' && nodeName == '公文分发' && bussinessId != null">
									<td id="submitdoc">
										<a class="button" href="javascript:tuiwentogwcs();">退文</a>&nbsp;
									</td>
									<td width="5"></td>
									<td id="submitdoc">
										<a class="button" href="javascript:endProcessInstance();"> 结束流程</a>&nbsp;
									</td>
									<td width="5"></td>
								</s:if>
								<s:if test="workflowName == '收文办件登记' && nodeName.substring(nodeName.length()-2,nodeName.length()) == '签收'">
									<td id="submitdoc">
										<a class="button" href="javascript:tuiwen();">退文</a>&nbsp;
									</td>
									<td width="5"></td>
								</s:if>
								<s:if test="(workflowName == '征求意见流程' && (nodeName != '主办人员拟办' && nodeName != '秘书处盖章' && nodeName != '主办人员')) || (workflowName == '公文转办流程' && (nodeName != '主办人员拟办' && nodeName != '秘书处盖章' && nodeName != '主办人员'))">
									<%-- test="(workflowName == '征求意见流程' || workflowName == '公文转办流程') && (nodeName != '主办人员')"> --%>
									<s:if
										test="(workflowName == '征求意见流程' && nodeName == '分办') || (workflowName == '公文转办流程' && nodeName == '分办')">
										<td id="distribution">
											<a class="button" href="javascript:distribution();">分发</a>&nbsp;
										</td>
										<td width="5"></td>
									</s:if>
									<s:elseif
										test="(isReceived=='' || isReceived==null)&&(nodeName != '主办人员')">
										<td id="signdoc">
											<a class="button" href="javascript:signdoc();">签收</a>&nbsp;
										</td>
										<td width="5"></td>
									</s:elseif>
									<s:else>
										<td id="submitdoc">
											<a class="button" href="javascript:yijian();"> 反馈意见</a>&nbsp;
										</td>
										<td width="5"></td>
									</s:else>
									<s:if test="nodeName != '主办人员拟办'">
										<td id="blrecord">
											<a class="button" onclick="javascript:annal();" href="#">
												办理记录</a>&nbsp;
										</td>
										<td width="5"></td>
									</s:if>
									<td id="saveclose">
										<a class="button" onclick="javascript:saveFormData(true);"
											href="#">保存并关闭</a>&nbsp;
									</td>
									<td width="5"></td>
									<td id="print">
										<a class="button" onclick="javascript:doPrintForm();"
											href="#">打印</a>&nbsp;
									</td>
									<td width="5"></td>
								</s:if>
								<s:else>
									${returnFlag }
								</s:else>
								<%--
								<td id="toSave">
									<a class="Operation" href="javascript:saveFormData(true);"><img
											src="<%=root%>/images/ico/baocun.gif" width="15" height="15"
											class="img_s">保存并关闭&nbsp;</a>
								</td>
								<td width="5"></td>
								--%>
								<s:if test="(flag=='true') && (#request.showrepeal == 1)">
									<td>
										<a class="button" href="javascript:repeal();">废除</a>&nbsp;
									</td>
									<td width="5"></td>
								</s:if>
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
								<td height="10" style="cursor: hand;" id="td_menu"
									onclick="hidemenu();" width="1%" valign="middle">
									<img name=pic src="<%=root%>/images/ico/jiantou_2.jpg"
										width="6" height="56" border="0" title="点击打开菜单栏" />
								</td>
								<td id="td_iframe_nextstep" width="0">
									<iframe id="iframe_nextstep" name="iframe_nextstep" 
										style="width: 100%; height: 100%; display: none; border: 0"
										frameborder="0" scrolling="no" src=""></iframe>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<iframe id="pdfFrame" style="display:none"></iframe>
		</DIV>
		<!-- 
		<OBJECT id=SignatureAPI
	  		classid="clsid:79F9A6F8-7DBE-4098-A040-E6E0C3CF2001"
	  		codebase="<%=root%>/common/goldgridOCX/iSignatureAPI.ocx#version=5,1,0,18"
	  		width=0
	  		height=0
	  		align=center
	  		hspace=0
	  		vspace=0>
		</OBJECT>
		 -->
		<%--<object id="PDF417Manager" width="200" height="100"
			style="display: none;"
			classid="CLSID:8AA64ECD-DFCB-4B88-A2B0-6A5C465D3F15"
			codebase="<%=root%>/common/goldgridOCX/PDF417Manager.dll#version=6,0,0,28">
		</object>
	--%>
	</body>
</html>
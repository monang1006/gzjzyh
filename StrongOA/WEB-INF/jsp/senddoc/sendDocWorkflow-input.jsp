<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="com.strongit.oa.common.workflow.IWorkflowService"/>
<jsp:directive.page import="com.strongit.oa.approveinfo.ApproveinfoManager"/>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<jsp:directive.page import="com.strongit.workflow.bo.TwfInfoApproveinfo"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaApproveinfo"/>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);

	IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
	String taskId = (String)request.getAttribute("taskId");
	String isBackTask = "";
	if(taskId != null && taskId.length() > 0){
		isBackTask = workflowService.isBackTask(taskId);
	}

%> 
<html>
	<head>
		<title><s:if test="taskId != null && taskId !=''">
				流程办理
			</s:if> <s:else>
				新建流程
			</s:else></title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=path%>/frame/theme_gray/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		 <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		 <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script type="text/javascript">

//区别提交页面与查看页面的变量  
//用于控制收文登记单 不执行onchang事件的变量
var justRead = true;

		//window.setInterval("initFormTemplate()",100);
		var isDoSave = false;//是否已经点击保存按钮
		var TANGER_OCX_Username = '${userName}';
		//由eform.js中定义的doNext()回调
		function goBack(){
			//alert("发送成功！");
	   		window.close();
	   		if(typeof(window.opener.closeIt()) != "undefined"){	   		
	 			window.window.opener.closeIt();	
	 		}
		}
		
		/*function onbeforeunload_handler(){
			var parentWin = window.opener;//父窗口
			if(typeof(parentWin.reloadPage) != "undefined"){
				parentWin.reloadPage();
			}
	 	} 	
		
		window.onbeforeunload = onbeforeunload_handler;*/
	
	    //保存表单成功以后的回调函数
	    function AfterSaveFormData(isReturn){
	        if(isReturn){
		    	//alert("保存成功！");
		   		window.returnValue = "OK";	
		   		window.close();
		   		if(typeof(window.opener.closeIt()) != "undefined"){	   		
		 			window.opener.closeIt();	
		 		}			
		    	
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
	      try{
		      TANGER_OCX_OBJ.PrintOut(booValue);      
	      }catch(e){}
	    }
    
    	//进入或退出痕迹保留状态
		function TANGER_OCX_SetMarkModify(boolvalue) {	
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){
				try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){
					   			//var parWin = window.dialogArguments;
					   			var parWin = window.opener 
	     						var userTask = parWin.jsonObj;
								$.each(userTask,function(i,jobj){
									//alert(jobj.userName);
									if(jobj.userName != null && jobj.userName != ""){
										UserName = jobj.userName;								
									}else{
										UserName = $("#userName").val();	
									}
								});			
						}
					}			
			    	TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = boolvalue;
		    	}catch(e){}
	    	}
		}
		
		//显示/不显示痕迹
		function TANGER_OCX_ShowRevisions(boolvalue) {		
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){
				   			//var parWin = window.dialogArguments;
				   			var parWin = window.opener 
     						var userTask = parWin.jsonObj;
							$.each(userTask,function(i,jobj){
								//alert(jobj.userName);
								if(jobj.userName != null && jobj.userName != ""){
									UserName = jobj.userName;								
								}else{
									UserName = $("#userName").val();	
								}
							});			
						}
					}
				TANGER_OCX_OBJ.ActiveDocument.ShowRevisions = boolvalue;
			
		    	}catch(e){}
			}
		}
		
		//清除痕迹
		function TANGER_OCX_AcceptAllRevisions(){
			if(TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
		    	try{
					if($("#userName").val() != undefined){
						with(TANGER_OCX_OBJ.ActiveDocument.Application){
				   			//var parWin = window.dialogArguments;
				   			var parWin = window.opener 
     						var userTask = parWin.jsonObj;
							$.each(userTask,function(i,jobj){
								//alert(jobj.userName);
								if(jobj.userName != null && jobj.userName != ""){
									UserName = jobj.userName;								
								}else{
									UserName = $("#userName").val();	
								}
							});			
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
		   var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDoc!templateTree.action","400", "350", window);
           if(ReturnStr){
				if(!TANGER_OCX_OBJ){//校验是否存在WORD
					return ;
				} 
				if($("#taskId").val() != ""){
					//保存WORD内容到本地
					TANGER_OCX_OBJ.SaveToLocal("c:\\tempword\\temp.doc",true);
				}
				initWord();
				TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = false;
				//查找标签与电子表的映射关系.
				$.getJSON("<%=root%>/senddoc/sendDoc!readBookMarkInfo.action?timestamp="+new Date(),{formId:$("#formId").val()},function(ret){
					if(ret == "-1"){
						alert("读取标签与电子表单映射时异常!");
						return ;
					}
					TANGER_OCX_OBJ.AddTemplateFromURL("<%=basePath%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+ReturnStr,true); 
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
	          	  		TANGER_OCX_OBJ.ActiveDocument.BookMarks(contentBookMarkName).Select();
	          	  		if($("#taskId").val() != ""){
		          	  		TANGER_OCX_OBJ.AddTemplateFromLocal("c:\\tempword\\temp.doc",false);
	          	  		}
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
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-processedstatus.jsp?instanceId="+$("#instanceId").val()+"&taskId="+$("#taskId").val(), 
                                   width, height, window);
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
 				AuditOptionControl.ShowAddButton = true;
      		}
      	}
      }
      
      //模板加载完成后调用此函数
      function initialHtml(){
      	//初始化委托人Id
      	var parWin = window.opener   //非模态窗口获取父窗口对象方法
      	//var parWin = window.dialogArguments; //模态窗口获取父窗口对象方法
     	var userTask = parWin.jsonObj;
     	$.each(userTask,function(i,jobj){
			//alert(jobj.userName);
			if(jobj.userId != null && jobj.userId != ""){			
				document.getElementById("rUserId").value = jobj.userId;								
			}
		});			
      
      	//如果是办理按钮触发的是弹出窗口,则不加载此Iframe
      	var isPopWin = $("#toNext").attr("isPop");
      	var isMenuWin = $("#toNext").attr("isMenuButton");//是否为菜单模式
      	var isOpenWin = $("#toNext").attr("isOpen");//是否为展开模式
      	if(isPopWin != "1"){//非弹出
      		document.getElementById("iframe_nextstep").src = contextPath + "!workflow.action?formId="+formId+"&taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
      		if(isMenuWin == "1"){
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
      //模板数据加载完成后调用此函数(在拟稿环节加载意见)
      function AfterLoadFormData(){
      	//痕迹保留时，名称换成委托人的名称
		if(taskId != "" && TANGER_OCX_OBJ != null && TANGER_OCX_OBJ!="" && TANGER_OCX_OBJ.ActiveDocument != null){//办理时,痕迹保留
	    	try{
				if($("#userName").val() != undefined){
					with(TANGER_OCX_OBJ.ActiveDocument.Application){
				   			//var parWin = window.dialogArguments;
				   			var parWin = window.opener 
     						var userTask = parWin.jsonObj;
							$.each(userTask,function(i,jobj){
								//alert(jobj.userName);
								if(jobj.userName != null && jobj.userName != ""){
									UserName = jobj.userName;								
								}else{
									UserName = $("#userName").val();	
								}
							});			
					}
				}			
		    	TANGER_OCX_OBJ.ActiveDocument.TrackRevisions = true;
	    	}catch(e){}
		}
		
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
			document.pic.title="点击隐藏菜单栏";
		}
      }
      
      
      
      function CloseIframe(){
      	$("#td_iframe_nextstep").width("0");
		$("#iframe_nextstep").hide();
		if(document.pic){
			document.pic.src="<%=root%>/images/ico/jiantou.jpg";
			document.pic.title="点击打开菜单栏";
		}
      }
      
      function HiddenIframe(){
      	$("#td_iframe_nextstep").width("0");
		$("#iframe_nextstep").hide();
		$("#td_menu").hide();
		$("#HiddenShow").hide();
      }
      
       function ShowIframe(){
      	$("#td_iframe_nextstep").width("30%");
		$("#iframe_nextstep").show();
		$("#td_menu").hide();
      }
      
      //menu模式下按钮点击触发事件
      function selecttransGroupById(groupid){
      		 $("#HiddenShow").show();
      		 OpenIframe();
			 hideAllDiv();
			 $(window.frames["iframe_nextstep"].document).find("#div_"+groupid).show();
			 $(window.frames["iframe_nextstep"].document).find("#h3_"+groupid).hide();
			 $(window.frames["iframe_nextstep"].document).find("#input_"+groupid).click();
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
      		 ShowIframe();
      	}
      	
      	  var objId = null;
      	  //退回流程
      	  var isBackTask = "1";
	      //填写意见,在控件中调用
	      //@param objAuditOpinion 意见控件名称
	      //@param recordId		   记录id
	      //@param opinion		   意见内容	
	      function toSuggestion(objAuditOpinion){
	      	SaveSuggestionInWorkflow(objAuditOpinion);
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
	 					var aiDate = json.aiDate;
		 				var atDate = json.atDate;
		 				var sDate = "";
	 					if(atDate != ""){
	 						sDate = atDate
	 					}else{
	 						sDate = aiDate;
	 					}
	 					EditSuggestionInTask(objAuditOpinion,json.aiId,json.aiContent,'${userId}',sDate);
	 				} else if(status == "1"){//未找到记录
	 					AddSuggestionInWorkflow(objAuditOpinion);
	 				}
	 		});
	      }
	      
	      //修改意见
	      function toEditSuggestion(objAuditOpinion,recordId,opinion,actorId){
	      	  //该意见是否在临时意见表中，存在，则为当前待办任务意见
	      	  $.getJSON("<%=root%>/senddoc/sendDocWorkflow!findSuggestionInDraftByAid.action?timeStamp="+new Date(),
 				{aid:recordId},function(json){
	 				var status = json.status;
	 				if(status == "-1"){
	 					alert("对不起，系统发生错误，请与管理员联系。");
	 				} else if(status == "0"){//找到了记录
		 				var aiDate = json.aiDate;
		 				var atDate = json.atDate;
		 				var sDate = "";
	 					if(atDate != ""){
	 						sDate = atDate
	 					}else{
	 						sDate = aiDate;
	 					}
	 					
	 					if(json.taskId != taskId){
		 					//不是退回的任务或者不是委托人的意见，不允许编辑
			      			var rUserId = document.getElementById("rUserId").value;
		 					if(isBackTask != "<%=isBackTask%>" || actorId != rUserId){   
			      				return;
				      		}
	 					}
	 					
	 					EditSuggestionInTask(objAuditOpinion,json.aiId,json.aiContent,actorId,sDate);
	 				} else if(status == "1"){//未找到记录

		      			//退回任务并且是委托人的意见允许编辑
		      			var rUserId = document.getElementById("rUserId").value;
	 					if(isBackTask == "<%=isBackTask%>" && actorId == rUserId){
	 						var sDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>";	    
		      				EditSuggestionInTask(objAuditOpinion,recordId,opinion,actorId,sDate);
			      		}else{
			      			return;
			      		}
	 				}
	 		});
	      }
	      
	      //删除意见
	      function DeleteSuggestion(objAuditOpinion,recordId){
	      	formReader.GetFormControl(objAuditOpinion).RemoveOpinionItem(recordId);
	      }
	      
	      //办理流程时添加意见
	      function AddSuggestionInWorkflow(objAuditOpinion){
	      	//模态窗口	      	      	      
      		//var parWin = window.dialogArguments;
      		//非模态窗口
      		var parWin = window.opener 
      		var userTask = parWin.jsonObj;
      		var data = document.getElementById("suggestion").value;
      		var width=(screen.availWidth-10)/2;
	        var height=(screen.availHeight-30)/2;
	        var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDocWorkflow!toSuggestion.action", 
	                                   width, height, new Array($("#toNext").attr("nodeName"),data,userTask));
	        if(ReturnStr != null){
	        	var returnSuggestion = ReturnStr[0];
	        	var rTaskId = ReturnStr[1];
	        	var rUserId = ReturnStr[2];
	        	var rUserName = ReturnStr[3];
	        	var sDate = ReturnStr[4];
	        	returnSuggestion = EE_Filter(returnSuggestion);
	        	var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>";	        	
	        	var approveData = "{aiControlName:'" + objAuditOpinion + "',aiContent:'" + returnSuggestion+"',aiDate:'"+strDate+"',atDate:'"+sDate+"'}";	        		        	
	        	if(taskId != ""){//在办理环节添加意见
	        	
	        	        //异步保存意见到临时意见表
			        $.post("<%=root%>/senddoc/sendDocApproveinfo!saveApproveinfo.action",{businessName:approveData,businessId:rTaskId,userId:'${userId}',ruserId:rUserId},function(ret){
				        if(ret == "0"){
				        	if($("#instanceId").val()!=""){
								insIdAndTasId = $("#instanceId").val() + "," + rTaskId;
								//alert(insIdAndTasId);
							 	var actionUri = basePath +  "senddoc/eFormTemplate.action";
							 	formReader.LoadAuditOpinion(actionUri, insIdAndTasId, "");
							}
				        }else {
			       			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.","",3);//0:提示
			       			return;
			       		}
			      	});
			      	


	        	}
	        } 	                                   
	      }
	       //在流程中修改意见
	      function EditSuggestionInTask(objAuditOpinion,recordId,opinion,actorId,sDate){
	      	var width=(screen.availWidth-10)/2;
	        var height=(screen.availHeight-30)/2;
	        //var parWin = window.dialogArguments;
	        var parWin = window.opener;
  			var userTask = parWin.jsonObj;
      		var width=(screen.availWidth-10)/2;
        	var height=(screen.availHeight-30)/2;
        	var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDocWorkflow!toSuggestion.action?sDate ="+sDate, 
                                   width, height, new Array($("#toNext").attr("nodeName"),opinion,userTask,taskId));
	        if(ReturnStr != null){
	        	var returnSuggestion = ReturnStr[0];
        		var rTaskId = ReturnStr[1];
	        	var rUserId = ReturnStr[2];
	        	var rUserName = ReturnStr[3];
	        	var sDate = ReturnStr[4];
	        	returnSuggestion = EE_Filter(returnSuggestion);
        		var strDate = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%>";
        		var approveData = "{aiControlName:'" + objAuditOpinion + "',aiContent:'" + returnSuggestion+"',aiDate:'"+strDate+"',atDate:'"+sDate+"'}";
	        	 $.post("<%=root%>/senddoc/sendDocApproveinfo!saveApproveinfo.action",{businessName:approveData,businessId:rTaskId,userId:'${userId}',ruserId:rUserId,recordId:recordId},function(ret){
	       			if(ret == "0"){
			       		if(returnSuggestion == ""){//为空时表示删除意见
				       	
							DeleteSuggestion(objAuditOpinion,recordId);
						}
	       		
						if($("#instanceId").val()!=""){
							insIdAndTasId = $("#instanceId").val() + "," + rTaskId;
							//alert(insIdAndTasId);
						 	var actionUri = basePath +  "senddoc/eFormTemplate.action";
						 	formReader.LoadAuditOpinion(actionUri, insIdAndTasId, "");
						}
	       			} else {
	        			formReader.ShowMessageBox("出错啦","很抱歉,系统出现错误,请与管理员联系.","",3);//0:提示
	        			return;
	        		}
	       		});
	       			       		
	        }                           
	      }

   /*
    *设置流程期限,该方法不负责进行日期格式的验证
    *日期格式，如：2012-12-06 16:58:31
    */
   function setProcessTimer(timer){
   	 $("#processOutTime").val(timer);
   }
      
	</script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;" onload="window.focus();"
		onunload="resumeConSignTask();">
		<!--初始化office功能-->
		<input type="hidden" id="officeFunction" name="officeFunction" doShowRevisions="${doShowRevisions}" doMarkModify="${doMarkModify }">
		<form id="form" name="form"
			action="<%=root%>/senddoc/sendDoc!save.action" method="post">
			<!-- 代办标识，值固定为1，其他办理界面不得出现该标识  标识代入意见提交-->
			<input name="daiBan" type="hidden" id="daiBan" value="1">
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
			 <!-- PDF正文信息描述 -->
			 <s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
			 <!--意见输入模式 -->
			 <s:hidden id="suggestionStyle" name="suggestionStyle"></s:hidden>
			  <!--委托人ID -->
			  <s:hidden id="rUserId" name="rUserId"></s:hidden>
			   <!-- 流程期限值 -->
			 <s:hidden id="processOutTime" name="processOutTime"></s:hidden>
		</form>
		<DIV id=contentborder align=center>
			<table width="100%" height="100%" border="0" cellspacing="0"
				cellpadding="00">
				<tr>
					<td height="40">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr id="menu" style="display: none;">
								<td >
									<a id="HiddenShow" style="display: none;" class="Operation" href="#" onclick="HiddenIframe()">隐藏办理窗口&nbsp;</a>
								</td>
								<td width="5"></td>
							</tr>
						</table>
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr  id="privilege" style="display: none;">
								<td id="Open_Submit" style="display: none ;">
									<a class="Operation" href="javascript:OpenSubmit();"><img
											src="<%=root%>/images/ico/songshen.gif" width="15"
											height="15" class="img_s"> 提交&nbsp;</a>
								</td>
								<td width="5"></td>
								<td id="td_startworkflow" style="display: none ;">
									<a class="Operation" href="javascript:showForm();"><img
											src="<%=root%>/images/ico/songshen.gif" width="15"
											height="15" class="img_s"> 启动新流程&nbsp;</a>
								</td>
								<td width="5"></td>
								<td>
									<table id="fileOperation" style="display: none;">
										<tr>
											${privilegeInfo}
										</tr>
									</table>
								</td>
								<td width="5"></td>
								${returnFlag }
								<%--<td id="toSave">
									<a class="Operation" href="javascript:saveFormData(true);"><img
											src="<%=root%>/images/ico/baocun.gif" width="15" height="15"
											class="img_s">保存并关闭&nbsp;</a>
								</td>
								<td width="5"></td>
							--%></tr>
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
								<td height="10" style="cursor:hand;" id="td_menu"
									onclick="hidemenu();" width="1%" valign="middle">
									<img name=pic
											src="<%=root%>/images/ico/jiantou_2.jpg" width="6"
											height="56" border="0" title="点击打开菜单栏" />
								</td>
								<td id="td_iframe_nextstep" width="0">
									<iframe id="iframe_nextstep" name="iframe_nextstep"
										style="width: 100%;height: 100%;display: none;border: 0"
										frameborder="0"  scrolling="no" src=""></iframe>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<%--<object id="PDF417Manager" width="200" height="100"
			style="display: none;"
			classid="CLSID:8AA64ECD-DFCB-4B88-A2B0-6A5C465D3F15"
			codebase="<%=root%>/common/goldgridOCX/PDF417Manager.dll#version=6,0,0,28">
		</object>
	--%></body>
</html>

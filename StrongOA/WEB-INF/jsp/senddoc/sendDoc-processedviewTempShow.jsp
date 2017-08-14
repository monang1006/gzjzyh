<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<%
String busType = (String) request.getParameter("businessType");
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看流程</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
.tabletitle {
	FILTER: progid :       DXImageTransform.Microsoft.Gradient (    
	                                gradientType =         0, startColorStr
		= 
		   
		   #ededed, endColorStr =         #ffffff );
}

.hand {
	cursor: pointer;
}
</style>
		<script type="text/javascript">
		function closeIt(){
			window.opener.location.reload();
			window.close();
		}
		
		function showMustFetchTask(){
			$("#mustFetchTask").show();
			$("#mustFetchTasksplit").show();
		}
		
		//查看时所有控件都要设为只读
		function initial(){
			$("#tskId").val("") ;
			//alert($("#tskId").val());
			$("#taskId").val("");//通过流程实例id得到意见,避免通过任务id去获取
			formReader = getFormReader();
			var ret = formReader.SetFormReadOnly(true);// 设置表单只读
			var control = formReader.GetFormControl("Button_viewformdata");//将“查看原表单按钮”不设置为只读,"Button_viewformdata"为“查看原表单按钮”特有的name
					if(control!=null){
						control.SetProperty ('ReadOnly',false);
			}
			var pdfcontrol = formReader.GetFormControl("Button_pdf");//将“导出PDF按钮”不设置为只读,"Button_pdf"为“导出表单按钮”特有的name
					if(pdfcontrol!=null){
						pdfcontrol.SetProperty ('ReadOnly',false);
			}
			/*
			 * 上传领导批示单不再使用附件控件形式，去掉对附件控件为空的判断 yanjian 2012-02-24 12:56
			 * */
			if(formReader.GetFormControl("doneSuggestion_content")){
				var doneSuggestion_contentIsExist = (formReader.GetFormControl("doneSuggestion_content").Value != ""?true:false)
				if(doneSuggestion_contentIsExist){
					formReader.SetFormTabVisibility("attachName",true);
					formReader.SetFormTabVisibility("doneSuggestion",true);
				}
			}
			//转办文件反馈栏  显示/隐藏
			var t=false;
			<security:authorize ifAnyGranted="000700010004">
			t=true;
			</security:authorize>	
			formReader.SetFormTabVisibility("FormTab_szfk",t);
			
			/*已办强制取回操作*/
			if($("#workflowFunction").attr("mustFetchTask")=="1"){
				showMustFetchTask();
			}
		}
	    //查看办理记录
	  function annal(){
		 var taskId = $("#taskId").val();
		 OpenWindow(contextPath+"!annallist.action?taskId=${taskId}", 
	                                   500, 300, window);
	  }
	  
	  function validation(){
	  	var mytableName = $("#tableName").val();
	  	var mypkFieldName = $("#pkFieldName").val();
	  	var mypkFieldValue = $("#pkFieldValue").val();
	  	OpenWindow(contextPath+"!validation.action?tableName="+mytableName+"&pkFieldName="+mypkFieldName+"&pkFieldValue="+mypkFieldValue,
	  			750,360,window);
	  }
	  
	   //废除公文,挂起流程实例
      function repeal(){
      	var bussinessId = "";
        var busType = <%=busType%>;
        if(busType == 0){//该文件正在走意见征询
       		bussinessId = $("#bussinessId").val();
        }
		var instanceId =$("#instanceId").val();
		var businessName =$("#businessName").val();
		//alert(instanceId);
		var flag = $("#flag").val();
		//alert(flag);
		//alert(bussinessId);
		if(flag == "true"){
   		 	if(confirm("确定要废除文件吗？")){
	   		 	$.post("<%=root%>/senddoc/sendDoc!repealProcess.action",
				{instanceId:instanceId,bussinessId:bussinessId,businessName:businessName},
				function(data){
				    if(data=="true"){
				       alert("废除文件成功。");
					   var parentWin = window.opener;//父窗口
				   		if(parentWin != null){
							if(typeof(parentWin.reloadPage) != "undefined"){
								parentWin.reloadPage();
							}
				   		}
		    			window.close();
				    }else{
						alert("废除文件失败。");
						return;
					}
				});
	   		 }else {
	   		 	return
	   		 } 
	   	}else{
	   		alert("该流程主办人员不是你，无权废除文件。")
	   	} 		   		 
      } 
      	  
	  //返回	
      function goBack(){
      /**/
			window.opener.location.reload();
			window.close();
      }
      
      //处理状态（查看流程图）
      function Cur_workflowView(flag){
      	 var width=screen.availWidth-10;;
     	 var height=screen.availHeight-30;
     	 WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+$("#instanceId").val()+"&taskId="+${taskId}+"&flag="+flag+"&type=processurgency",'Cur_workflowView',width, height, "办理记录");
      }
      
		function doGotoReDone(){
	      var taskId = $("#tskId").val();
	      var bussinessId = $("#bussinessId").val();
	      //alert(bussinessId);
		  var instanceId = $("#instanceId").val();
		  $.getJSON("<%=root%>/senddoc/sendDocWorkflow!getCurrentTaskHandler.action?timeStamp="+new Date(),
				{instanceId:instanceId,taskId:taskId},function(json){
					var status = json.status;
					if(status == "-1"){//发生错误
						alert("对不起，系统发生错误，请与管理员联系。");
					}else if(status == "0" || status == "1"){
						if(json.isBackspace != "1"){
							alert("对不起，此文不是由上一办理人退回的，不能重新办理。");
							return ;
						}
						//确认是否重新办理
						if(confirm("您确定要重新办理[" + json.businessName + "]吗？\n点击确定该文将被废除,同时新建一条[" + json.businessName + "]。")){						
							$.post("<%=root%>/senddoc/sendDoc!repealProcess.action",
								{instanceId:instanceId,businessName:json.businessName},function(data){
								    if(data=="true"){				   		 		   
							   		 	$.post("<%=root%>/senddoc/sendDoc!reTodo.action",
											{bussinessId:bussinessId,workflowName:json.workflowName,businessName:json.businessName,formId:formId},function(data){
												if(data != null){
													var insId = data.split("@")[0];
													var tasId = data.split("@")[1]; 
													window.opener.location.reload();
										    	    window.location.href = "<%=root%>/senddoc/sendDoc!CASign.action?taskId="+tasId+"&instanceId="+insId+"&workflowName="+encodeURI(encodeURI(json.workflowName));
												}
											});
								    }else{
										alert("重新办理失败。");
										return;
									}
								});
						}
					} else if(status == "2"){//主办人员和当前用户不一致
						alert("对不起，您不是主办人员，不能重新办理此文。");
					}else if(status == "3"){//流程已结束，不能办理
						alert("对不起，流程已结束，不能重新办理。");
					}
				});
	      }
		function cancel(){   
			    var ua = navigator.userAgent;   
			    var ie = navigator.appName == "Microsoft Internet Explorer"?true:false;   
			    if(ie){   
			        var IEversion = parseFloat(ua.substring(ua.indexOf("MSIE")+5,ua.indexOf(";",ua.indexOf("MSIE"))));   
			        if(IEversion < 5.5){   
			            var str = "<object id = 'noTipClose' classid='clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11'>";   
			            str += "<param name='Command' value='Close'/></object>";   
			            document.body.insertAdjacentHTML("beforeEnd",str);   
			            document.all.noTipClose.Click();   
			        }else{   
			            window.opener = null;   
			           window.open('','_self','');   
			            window.close();   
			        }   
			   }else{   
			        widow.close();   
			    }   
			    }  

	      var jsonObj = null;
	      //代办意见
	      function gotoDone(){
	      	  var taskId = $("#taskId").val();
			  var instanceId = $("#instanceId").val();
			  $.getJSON("<%=root%>/senddoc/sendDocWorkflow!getCurrentTaskHandler.action?timeStamp="+new Date(),
 				{instanceId:instanceId,taskId:taskId},function(json){
 					var status = json.status;
 					if(status == "1"){
 						$("#td_todone").show();
 					}
 					if(status == "0" || status == "1"){
	 					if(json.isBackspace == "1"){
							$("#td_toredone").show();
						}
 					}
 				});
	      }
	      
	      function doGottoDone(){
	      	   var taskId = $("#tskId").val();
			   var instanceId = $("#instanceId").val();
			  $.getJSON("<%=root%>/senddoc/sendDocWorkflow!getCurrentTaskHandler.action?timeStamp="+new Date(),
 				{instanceId:instanceId,taskId:taskId},function(json){
 					var status = json.status;
 					if(status == "-1"){//发生错误
 						//alert("对不起，系统发生错误，请与管理员联系。");
 					}else if(status == "0"){//不存在主办人员代办
 						alert("对不起，["+json.businessName+"]的处理人未设置主办人员代办，操作失败。");
 					}else if(status == "1"){//存在主办人员代办
 						jsonObj = json.user;//传递此对象到子窗口中
 						var width=screen.availWidth-10;
		  				var height=screen.availHeight-30;
		  				var ret=WindowOpen("<%=root%>/senddoc/sendDocWorkflow!input.action?taskId="+json.taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI(json.workflowName)),'todo',width, height, "待办事宜");
		  				//var ret=OpenWindow("<%=root%>/senddoc/sendDocWorkflow!input.action?taskId="+json.taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI(json.workflowName)),width, height,window);
 					} else if(status == "2"){//发起人和当前用户不一致
 						alert("对不起，您不是主办人员，不能办理此文。");
 					}else if(status == "3"){//流程已结束，不能办理
 						alert("对不起，流程已结束，不能继续办理。");
 					}
 				});
	      }
	      
	      //查看意见征询登记信息
	      function viewYjzx(){
	      	var ret=OpenWindow(scriptroot + "/bgt/senddoc/sendDoc!input.action?model.id="+$("#pkFieldValue").val()
				 ,"500","300",window);
	      }
	      //强制取回
	      function mustFetchTask(){
	      	var isExsitTodo = "${isExsitTodo }";
	      		if(isExsitTodo == "0"){
	      		if(confirm("您确定要进行强制取回操作？")){
		      	var remindCount=0;//添加定时提醒显示div的id增量
	    		var remindContent="";//添加定时提醒显示的div的内容
	    		var remindType=""; //提醒方式
		        var url = "<%=path%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-timerRemind.jsp?instanceId="+$("#instanceId").val();
				var a = window.showModalDialog(url,window,'dialogWidth:550px;dialogHeight:400px;help:no;status:no;scroll:no');
				if((typeof a)!="undefined"){
					a[0]="id"+remindCount+";"+a[0];
					if(remindContent!=""&&remindContent!=null){
						remindContent+="##"
					}
					remindContent+=a[0];//提醒内容
					remindType+=a[1];//提醒方式
					$("#remindSet").val(remindContent);//提醒内容
					$("#remindType").val(remindType);//提醒方式
					var actionUri = basePath + "senddoc/sendDoc.action";
					document.getElementById("formAction").value = "mustFetchTask";
					document.getElementById("bussinessId").value = $("#nodeId").val();// 退回的节点id,指定退回用户id
					if (formReader.SaveFormData(actionUri, "form",
							"doBackSpacePrevRequestCompleted")) {
						// 方法调用成功
					} else {
						// 调用失败
					}
				}
		      }
	      		}else{
	      			alert("该文已在您的待办事宜中，不能强制取回。");
	      		}
	      }
	      
	        //催办
      function sendReminder(){
			var instanceId = $("#instanceId").val();
			var overDate = $("#state").val();
		 	if(overDate == "1"){
		 		alert("流程已结束，无需催办。");
		 		return;
		 	}
		 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
		}
		
	   //查询转办记录(只针对"征求意见流程"和"公文转办流程")
   function transmitting(){
   		 var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-processedstatus.jsp?instanceId="+$("#instanceId").val()+"&taskId="+$("#taskId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&isTransmitting=1",'Cur_workflowView',width, height, "转办记录");
   }
    </script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" onload="window.focus();gotoDone();">
		<!--初始化流程控制功能
					mustFetchTask(对已办文可以进行强制取回【默认值"0"：表示不能进行强制取回，"1"：表示可以强制取回】）
					showrepeal(是否启用废除功能 0:不启用；1：启用)
		-->
		<input type="hidden" id="workflowFunction" name="workflowFunction"
			mustFetchTask="${mustFetchTask }" showrepeal="${showrepeal}">
		<form id="form" action="<%=root%>/senddoc/sendDoc!list.action"
			method="post">
			<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
			<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
			<!-- 业务数据名称 -->
			<s:hidden id="businessName" name="businessName"></s:hidden>
			<!-- 电子表单模板id -->
			<s:hidden id="formId" name="formId"></s:hidden>
			<!-- 任务id initial方法之后该任务Id会置空 -->
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<!-- 任务id initial方法置空taskId之前将其赋值到tskId -->
			<s:hidden id="tskId" name="tskId"></s:hidden>
			<!-- 任务对应的节点id  -->
			<s:hidden id="nodeId" name="nodeId"></s:hidden>
			<!-- 是否主办人员 -->
			<s:hidden id="flag" name="flag"></s:hidden>
			<!-- 流程是否结束 -->
			<s:hidden id="state" name="state"></s:hidden>
			<!-- 流程实例ID -->
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<!-- 父流程实例ID -->
			<s:hidden id="parentInstanceId" name="parentInstanceId"></s:hidden>
			<!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- PDF正文信息描述 -->
			<s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
			<!-- 查看原表单相关 -->
			<s:hidden id="personDemo" name="personDemo"></s:hidden>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<s:hidden id="formAction" name="formAction"></s:hidden>
			<!-- 审批意见 -->
			<s:hidden id="suggestion" name="suggestion"></s:hidden>
			<!-- 提醒设置 -->
			<s:hidden id="remindSet" name="remindSet"></s:hidden>
			<s:hidden id="remindType" name="remindType"></s:hidden>
			<!-- 流程名称 -->
			<s:hidden id="workflowName" name="workflowName"></s:hidden>
		</form>
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0"
				cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
									<strong>流程处理单</strong> 
								</td>
								<td>
									<table align="right">
										<tr>
											<td>
												<a class="button" href="javascript:cancel();">关闭</a>&nbsp;
											</td>
											
											<td width="5">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<div id="silverlightControlHost"
							style="position: relative; width: 100%; height: 100%">
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
				<iframe id="pdfFrame" style="display:none"></iframe>
			</table>
		</div>
	</body>
</html>

<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>我的请求</title>
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
	<script src="<%=path%>/oa/js/eform/grid.js" type="text/javascript"></script>
    <script type="text/javascript">
   	 //右键菜单实现
   	  function loadContentMenu(gridControl){
   	  	if(gridControl != null){
   	  		gridControl.PopupMenuCallbackCommand = "GridPopupMenuCallbackCommand";
   	  		gridControl.PopupMenuFontSize=18;
   	  		gridControl.AddPopupMenuItem("查阅","gotoView");
            gridControl.AddPopupMenuItem("办理状态", "workflowView");
            gridControl.AddPopupMenuItem("取回流程", "fetchTask");
            gridControl.AddPopupMenuSeparator();
            gridControl.AddPopupMenuItem("导出Excel", "exportfils");
   	  	}
   	  }
      function exportfils(){
      
      }	
    
      //重定向到此页面
      function reloadPage() {
	    window.location = "<%=root%>/component/formdata/formGridData!processed.action?type="+$("#type").val()+"&formId="+$("#formId").val()
      				+"&tableName="+$("#tableName").val();
      }	
      
       /**
		 * 任务取回
		 * @param taskId 任务id
		 * @return retCode
		 * 	1、流程实例已结束 0；
		 *  2、任务已被签收处理1；
		 * 	3、任务取回成功2；
		 * 	4、任务实例不存在返回-1
		 * 	5、抛出异常返回-2
		 */
		function doFetchTask(taskId){
			$.post("<%=root%>/senddoc/sendDoc!fetchTask.action",
			      　{taskId:taskId},
			      　function(retCode){
			      		if(retCode == "-1"){
			      			alert("该任务不存在或已删除！");
			      		}else if(retCode == "0"){
			      			alert('该流程实例已经结束，无法取回');	
			      		}else if(retCode == "1"){
			      			alert('该任务的后继任务已被签收处理，不允许取回');
			      		}else if(retCode == "2"){
			      			alert('流程已成功取回');
			      			reloadPage();
			      		}else if(retCode == "-2"){
			      			alert("取回流程失败！");
			      		}	
			      }		
			);
		}
		
		//查阅
      function gotoView(){
      	var TaskIdAndInstanceId = getValue();
          if(TaskIdAndInstanceId == ""){
          	alert("请选择要查看的流程！");
          	return ;
          }else{
          	var taskIds = TaskIdAndInstanceId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一个流程！");
          		return ;
          	}
          }  
		var taskId = getValue("taskId");
		var instanceId = TaskIdAndInstanceId;
		var state = "";
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
      	var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");
     	  if(ret){
     		if(ret == "OK"){
     			reloadPage();
     		}
     	  }
      }
       
       //处理状态（查看流程图）
      function workflowView(){
	      var instanceId = getValue();
          if(instanceId == ""){
          	alert("请选择要查看的流程！");
          	return ;
          }else{
          	var taskIds = instanceId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查看一个流程！");
          		return ;
          	}
          }  
		  var taskId = getValue("taskId");
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId, 
                                   width, height, window);
      }
      
      //取回任务
      function fetchTask(){
      	var TaskIdAndInstanceId = getValue("taskId");
      	if(TaskIdAndInstanceId == ""){
      		alert("请选择要取回的流程！");
      	}else{
      		var taskIds = TaskIdAndInstanceId.split(",");
      		if(taskIds.length>1){
      			alert("一次只能取回一个流程！");
      			return ;
      		}
      		if(confirm("取回选定的流程，确定？")){
      			var TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		  		var taskId = TaskIdAndInstanceId[0];
	      		doFetchTask(taskId);
	      	}		
      	}
      } 
    </script>
  </head>
  <body class="contentbodymargin" scroll="no">
  <form id="form" name="form" method="post">
			<s:hidden name="formId"></s:hidden>
			<input id="formAction" type="hidden" name="formAction" />
			<s:hidden name="tableName"></s:hidden>
			<s:hidden name="type"></s:hidden>
			<s:hidden name="todoType"></s:hidden>
			<s:hidden name="model.workflowStatus"></s:hidden>
			<s:hidden name="model.workflowType"></s:hidden>
		</form>
    <div id="contentborder"  align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="35">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>
								<table align="right">
									<tr>
										<td >
					                  <a class="Operation" href="javascript:gotoView();"><img
					                      src="<%=root%>/images/ico/page.gif" width="15" height="15"
					                      class="img_s">查阅&nbsp;</a> 
					                </td>
					                <td width="5"></td>
					                <td >
					                  <a class="Operation" href="javascript:workflowView();"><img
					                      src="<%=root%>/images/ico/chakan.gif" width="15" height="15"
					                      class="img_s">办理状态&nbsp;</a> 
					                </td>
						                <td width="5"></td>
						                <s:if test="state!=1">
											<td >
							                  <a class="Operation" href="javascript:fetchTask();"><img
							                      src="<%=root%>/images/ico/cexiao.gif" width="15" height="15"
							                      class="img_s">取回流程&nbsp;</a> 
							                </td>
					                
										<td width="5">
											&nbsp;
										</td>
						                </s:if>
						                 <s:if test="state!=1">
						                <td>
												<a class="Operation" href="javascript:exportfils();"><img
														src="<%=root%>/images/ico/daochu.gif" width="15"
														height="15" class="img_s">导出为EXCEL&nbsp;</a>
											</td>

											<td width="5">
												&nbsp;
											</td>
											</s:if>
					                </tr>
					           </table> 
									</td>
              </tr>
            </table>
          </td>
        </tr>
        </table>
          <div id="silverlightControlHost"
			style="position:relative;height: 93%">
			<object data="data:application/x-silverlight-2,"
				type="application/x-silverlight-2" width="100%" height="100%"
				id="plugin">
				<param name="source"
					value="<%=path%>/GridFormReader/StrongFormReader.xap" />
				<param name="onError" value="onSilverlightError" />
				<param name="onLoad" value="onSilverlightLoad" />
				<param name="background" value="white" />
				<param name="minRuntimeVersion" value="4.0.50401.0" />
				<param name="autoUpgrade" value="true" />
				<a href="<%=path%>/detection/lib/Silverlight.exe"
					style="text-decoration:none"> <img
						src="<%=path%>/detection/images/SLMedallion_CHS.png"
						alt="Get Microsoft Silverlight" style="border-style:none" /> </a>
			</object>
			<iframe id="_sl_historyFrame"
				style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
		</div>
      
    </div>
    <iframe name="myiframe" src="" style="display:none;"> </iframe>
    <script language="javascript">
      //催办
      function sendReminder(){
		     var id=getValue();
			 var returnValue = "";
			 if(id == ""){
			 	alert("请选择要催办的流程！");
			 	return ;
			 }else{
			 	var ids = id.split(",");
			 	if(ids.length>1){
			 		alert("一次只能催办一个流程！");
			 		return ;
			 	}
			 }
		 	var result = id.split("$");
			var instanceId = result[0];
		 	var overDate = getValue("processEndDate");
		 	if(overDate != ""){
		 		overDate = "1";
		 	}
		 	if(overDate == "1"){
		 		alert("流程已结束，无需催办！");
		 		return;
		 	}
		 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
		}
   	
 	/**
		@param InstanceIdAndStatus 流程实例id$流程状态
	*/
	function ViewFormAndWorkflow(InstanceIdAndStatus) {
		var result = InstanceIdAndStatus.split("$");
		var instanceId = result[0];
		//var fullContextPath = $("form").attr("action");
  		//var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  		//document.getElementById('blank').contentWindow.setWorkId("",instanceId,contextPath); 
  		
  		var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+result[1],'hostedby',width, height,"");
  		
	}  
    </script>
  </body>
</html>

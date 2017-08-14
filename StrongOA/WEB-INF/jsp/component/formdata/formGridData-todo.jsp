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
   	  		gridControl.AddPopupMenuItem("查阅","processDoc");
            gridControl.AddPopupMenuItem("办理状态", "workflowView");
            gridControl.AddPopupMenuItem("指派", "zp");
   	  	}
   	  }
    		//重定向到此页面
      function reloadPage() {
      	 var privilName = "";
	    if("${privilName}" !=""){
	    	privilName = encodeURI(encodeURI("${privilName}"));
	    }else{
		    if($("#privilName").val() != ""){
		    	privilName = encodeURI(encodeURI($("#privilName").val()));
		    }
	    }
	    var workflowType = "";
	    if("${workflowType}" !=""){
	    	workflowType = encodeURI(encodeURI("${workflowType}"));
	    }else{
		    if($("#workflowType").val() != ""){
		    	workflowType = encodeURI(encodeURI($("#workflowType").val()));
		    }
	    }
	    //http://127.0.0.1:8080/bgt/component/formdata/formGridData!todo.action?formId=13050&type=1&tableName=T_OARECVDOC
      	window.location = "<%=root%>/component/formdata/formGridData!todo.action?type="+$("#type").val()+"&formId="+$("#formId").val()
      				+"&tableName="+$("#tableName").val();
      	/*window.location = "<%=root%>/senddoc/sendDoc!todo.action?type=${type}&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
      				+"&formId="+$("#formId").val()+"&privilName="+privilName+"&workflowType="+workflowType;
      				*/
      }	
      //处理  
      function onDblClickProcess(theValue) {
      	//document.getElementById("noshow").innerHTML=theValue;
      	 var taskId=$(theValue).find("input").val();
          if(taskId == ""){
          	alert("请选择要处理的流程！");
          	return ;
          }
          
          var TaskIdAndInstanceId = taskId.split("$");
		  taskId = TaskIdAndInstanceId[0];
		  var instanceId = TaskIdAndInstanceId[1];
    	  var width=screen.availWidth-10;
		  var height=screen.availHeight-30;
		  //更改为CA认证Action dengzc 2011年4月2日16:05:09 
		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height);
      }
      //查阅 
      function processDoc() {
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要查阅的流程！");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("一次只能查阅一个流程！");
          		return ;
          	}
          }
		  var instanceId = getValue("processInstanceId");
    	  var width=screen.availWidth-10;
		  var height=screen.availHeight-30;
		   //更改为CA认证Action dengzc 2011年4月2日16:05:09 
		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
      }
       //处理状态（查看流程图）
      function workflowView(){
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
		  var instanceId = getValue("processInstanceId");
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+TaskIdAndInstanceId, 
                                   width, height, window);
      }
      
      function doHide(){
      	var d = $("#searchtable").css("display");
      	if(d == "none"){
	      	$("#searchtable").show();
	      	$("#label_search").text("隐藏查询条件");
      	} else {
      		$("#searchtable").hide();
      		$("#label_search").text("显示查询条件");
      	}
      }
      
      
        //指派
       function zp(){
       	var taskId = getValue();
       	if(taskId == ""){
       		alert("请选择要指派的流程！");
       		return ;
       	}else{
       		if(taskId.split(",").length>1){
       			alert("一次只能指派一个流程！");
       			return;
       		}
       	}
       	var TaskIdAndInstanceId = taskId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = getValue("processInstanceId");
       	$.post("<%=root%>/senddoc/sendDoc!checkCanReturn.action",
       			{taskId:taskId},
       			function(data){
       				if(data == "-1"){
       					alert("此任务不存在或已删除，操作失败！");
       				}else if(data == "-2"){
       					alert("操作失败，请与管理员联系！");
       				}else{
       					var flags = data.split("|");
       					var flagzp = flags[2];//指派
       					if(flagzp == "1"){//允许指派
       						var taskActors = "";
							var url = scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=reassign&nodeId=0&taskId=" + taskId + "&taskActors=" + taskActors;
							var userstr = OpenWindow(url, 420, 450, window);          
							//用户ID|用户名称,指派是否需要返回（0：否、1：是）
							if(userstr!=null && userstr!=''){
								$.post("<%=root%>/senddoc/sendDoc!reAssign.action",{taskId:taskId,suggestion:encodeURI(userstr)},
									   function(retCode){
									   		if(retCode == "0"){
									   			alert("任务指派成功！");
									   			reloadPage();
									   		}else if(retCode == "-1"){
									   			alert("任务实例不存在或已删除！");
									   		}else if(retCode == "-2"){
									   			alert("指派过程中出现异常！");
									   		}else if(retCode == "-3"){
									   			alert("参数传输错误！");
									   		}
									   }			
								);
							}
       					}else if(flagzp == "0"){//不允许指派
       						alert("对不起，此任务不允许指派！");
       					}else{
       						alert("对不起，出现未知错误！请与管理员联系！");
       					}
       				}
       			});
       }
       
         //流程送审
      function sendDoc() {
       	  var height=473;//screen.availHeight-50;
       	  var width=480;//screen.availWidth/2;
          var bussinessId = getValue();
          var TaskIdAndInstanceId = bussinessId.split("$");
		  var taskId = TaskIdAndInstanceId[0];
		  var instanceId = getValue("processInstanceId");
          
          if(bussinessId == ""){
   		 	alert("请选择要送审的流程！");
   		 	return ;
   		 }else{
   		 	var docIds = bussinessId.split(",");
   		 	if(docIds.length>1){
   		 		alert("一次只能送审一个流程！");
   		 		return ;
   		 	}
   		 }
   		 var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
   		 if("${page.pageSize}"!=""){
         	param += "&page.pageSize=${page.pageSize}";
	     }
         var ret = OpenWindow("<%=root%>/senddoc/sendDoc!nextstep.action?tableName="+$("#tableName").val()
         			+"&taskId="+taskId+"&formId="+$("#formId").val()
         			+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())), 
                               width, height,window);
        if(ret){
        	if(ret == "OK"){
        		//alert("发送成功！");
        		reloadPage();
        		//window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;
        	}else if(ret == "NO"){
        		alert("发送失败！");
        	}
        }
      }
      //废除公文,挂起流程实例
      function repeal(){
		var bussinessId = getValue();
		var TaskIdAndInstanceId = bussinessId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = getValue("processInstanceId");
		if(bussinessId == ""){
   		 	alert("请选择要废除的文件！");
   		 	return ;
   		 }else{
   		 	//alert(instanceId);
   		 	$.post("<%=root%>/senddoc/sendDoc!repealProcess.action",
			{instanceId:instanceId},
			function(data){
			    if(data=="true"){
			       alert("废除文件成功!");
			       reloadPage();
   		 		   //window.location = "<%=root%>/senddoc/sendDoc!todo.action?workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&formId="+$("#formId").val();
			    }else{
					alert("废除文件失败!");
				}
			});
   		 	   		 	   		   		 
   		 }   		   		 
      }
       
       
       function exportfils(){
	       var ids = "";
	       var taskId = "";
	       $("input[id^=chkButton]").each(function(){
	       		taskId = $(this).val().split("$")[0];
	       		ids += taskId+"@";
	       });
			if(confirm("您是否要将信息导出为EXCEL？")){
				 var url="<%=root%>/senddoc/sendDocUpload!exportTodoExcel.action";
				 var pama="?1=1";
			 	 pama += ("&processIds="+ids.substring(0,ids.length-1));
			 	 pama += ("&workflowName="+encodeURI(encodeURI($("#workflowName").val())));
			 	 pama += ("&formId="+$("#formId").val());
			 	 pama += ("&type="+$("#type").val());
			 	 pama += ("&privilName="+encodeURI(encodeURI($("#privilName").val())));
			 	//pama += ("&page.pageSize=${page.pageSize}");
			 	 //pama += ("&page.pageNo=${page.pageNo}");
				 url=(url+pama);
				 document.getElementById('tempframe').src=url;
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
	                <td>
												<a class="Operation" href="javascript:processDoc();"><img
														src="<%=root%>/images/ico/page.gif" width="15"
														height="15" class="img_s">查阅&nbsp;</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:workflowView();"><img
														src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s">办理状态&nbsp;</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:zp();"><img
														src="<%=root%>/images/ico/weituo.gif" width="15"
														height="15" class="img_s">指派&nbsp;</a>
											</td>

											<td width="5">
												&nbsp;
											</td>
											<s:if test="todoType=='sign'">
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

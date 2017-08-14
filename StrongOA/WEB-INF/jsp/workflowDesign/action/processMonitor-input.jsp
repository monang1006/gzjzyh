<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<style>
.contentbodymargin {
	BACKGROUND: #dae6f2; MARGIN: 0 2px 2px 0;
}
#contentborder {
	BACKGROUND: white;
	OVERFLOW: auto;
	WIDTH: 100%;
	POSITION: absolute;
	HEIGHT: 100%;
}
a{	
	border:1px #b3b3b3 solid;
	height:20px;
	line-height:20px;
	color:#000000;
	text-align:center;
	padding:0 5px;
	
	font-size:12px;
	background:url(<%=frameroot%>/images/view_button.png) repeat-x;
}
#nodeinfo{
	padding-left:4px;
	line-height:28px;
	font-family: "宋体";
	font-size: 14px;
}

		</style>
		
		<script type="text/javascript">
		// 改变流程状态，挂起、恢复、结束
        function changeStatus(processId,flag) {
			//判断流程名称是否冲突
		  $.ajax({ url: scriptroot + "/workflowDesign/action/processMonitor!status.action",
             type:"post",
             dataType:"text",
             data: "processId=" + processId + "&flag=" + flag,
             success:function(msg){ 
                alert(msg);
                location = scriptroot+ "/workflowDesign/action/processMonitor!input.action?instanceId=" + processId;
             } 
          });            
        }	
        
        function changeInstance(processId,nodeId) {
            var strDid = "";
            var p = new Array();
            strDid += processId;
            p[0] = strDid;
            p[1] = nodeId;
            var buffalo = new Buffalo(scriptroot+"/ajaxAction.do");
            buffalo.remoteCall("processManagerService.changeProInstance", p,
            function(reply){
               var result = reply.getResult();
               alert(result);
               location=scriptroot+ "/workflowmanager/viewMonitorData.do?instanceId="+strDid;
             });  
        }
                	
		//挂起流程
		function suspendPro(){
			var status=document.getElementsByName("statustd")[0].innerHTML;
			if(status=="&nbsp;暂停"){
				alert("流程已经处于挂起状态。");
			}else if(status=="&nbsp;结束"){
				alert("该流程已经结束。");
			}else{
				var processId=document.getElementsByName("processId")[0].value;
				changeStatus(processId,"1");
			}
		}
		//恢复流程
		function resumePro(){
			var status=document.getElementsByName("statustd")[0].innerHTML;
			if(status=="&nbsp;执行"){
				alert("流程已经处于运行状态。");
			}else if(status=="&nbsp;结束"){
				alert("该流程已经结束。");
			}else{
				var processId=document.getElementsByName("processId")[0].value;
				changeStatus(processId,"2");
			}			
		}
		//结束流程
		function endPro(){
			var status=document.getElementsByName("statustd")[0].innerHTML;
			if(status=="&nbsp;结束"){
				alert("流程已经被结束。");
			}else{
				var processId=document.getElementsByName("processId")[0].value;
				changeStatus(processId,"3");				
			}			
		}
		
		//催办
		function urge(instanceId,nodeId){
			$.post("<%=path%>/workflowDesign/action/processMonitor!findCurrentTask.action",
					{instanceId:instanceId,nodeId:nodeId},function(ret){
						if(ret != ""){
						 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=work/work-urgchoose.jsp?instanceId="+instanceId+"&nodeId="+nodeId,400, 300, window);	
						}else{
							alert("只能对处理中的节点进行【催办】。");
							return ;
						}
					});	
		}
		
		//查看表单
		function viewForm(processInstanceId,nodeId){
            $.ajax({ url: "<%=path%>/workflowDesign/action/processMonitor!hasForm.action",
             type:"post",
             dataType:"text",
             data: "instanceId="+processInstanceId+"&nodeId="+nodeId+"&processId=${processId}&proName="+encodeURI(encodeURI("${ProName}")),
             success:function(msg){ 
                if("yes"==msg){
                	var url="<%=path%>/workflowDesign/action/processMonitor!viewform.action?instanceId="+processInstanceId+"&nodeId="+nodeId
						+"&processId=${processId}&proName="+encodeURI(encodeURI("${ProName}"));
                	var width=screen.availWidth-10;
		   			var height=screen.availHeight-30;
		   			var ret=OpenWindow(url,width, height, window);
		            if(ret){
		            	if(ret == "OK"){
		            	}
		            }    
                }else{
                	alert("该流程没有挂接表单，无法查看。");
                }
             } 
          }); 
		}
	
		//返回列表
		function goback(){
			var processId="${processId}";
			if(window.parent.document.getElementById("isShowparentBtn") != undefined){
				window.parent.document.getElementById("isShowparentBtn").value = "true";
			}
			if(processId!=""){
				var forward = "<%=request.getParameter("forward")%>";
				if(forward == "org"){
					location = "<%=path%>/workflowDesign/action/processMonitor!monitorList.action?processId=${processId}&proName="+encodeURI(encodeURI("${ProName}")) + "&workflowName="+encodeURI(encodeURI("${workflowName}"));
				} else {
					location = "<%=path%>/workflowDesign/action/processMonitor.action?processId=${processId}&proName="+encodeURI(encodeURI("${ProName}")) + "&workflowName="+encodeURI(encodeURI("${workflowName}"));
				}
			}else{
				window.history.go(-1);
			}
		}
		
		
		function wirteTimeout(isTimeout){
			if("1"==isTimeout){
				document.write("<font color='red'>是</font>");
			}else{
				document.write("否");
			}
		}
		//退回
		function doBackSpace(processInstanceId,nodeId) {
			//alert("首度进入");
			$.post("<%=path%>/workflowDesign/action/processMonitor!getTypeByNodeId.action",
					{nodeId:nodeId},function(rtnType){
						if(rtnType =="subProcessNode"){
							//alert("进入驳回");
							$.post("<%=path%>/workflowDesign/action/processMonitor!findCurrentTaskWithSubProcess.action",
								{instanceId:processInstanceId,nodeId:nodeId},function(ret){
								if(ret != ""){
									var taskIds = ret.split(",");
									var taskId = taskIds[0];
									
									var width=screen.availWidth-10;;
    								var height=screen.availHeight-30;
									var ReturnStr=OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=bohui", 
		                                   width, height, window);
								if(ReturnStr){
										var ret = OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/initback.jsp", 
		                                   400, 300, window);
									if(ret){
		    							//$.post("<%=path%>/workflowDesign/action/processMonitor!bohui.action",{taskId:taskId,returnNodeId:ReturnStr,suggestion:encodeURI(ret)},
		    							$.post("<%=path%>/workflowDesign/action/processMonitor!bohui.action",{taskId:taskId,returnNodeId:ReturnStr,suggestion:EE_EncodeCode(ret)},
				   								function(retCode){
				   								if(retCode == "0"){
				   								alert("退回成功。");
				   								location = scriptroot + "/workflowDesign/action/processMonitor!input.action?instanceId=" + processInstanceId+"&modelType="+'${modelType}'
										+"&proName="+encodeURI(encodeURI("${proName}"))+"&processId=${processId}";
				   							}else if(retCode == "-1"){
				   								alert("任务实例不存在或已删除。");
				   							}else if(retCode == "-2"){
				   								alert("任务驳回过程中出现异常。");
				   							}else if(retCode == "-3"){
				   								alert("电子表单数据读取失败。");
				   							}//end if
				  				  		}//function		
										);//post
			  						}//end if                                 
								}//end if
							}//end if ret!=""
							else{
								alert("请在流程图中框中区域点击【退回】。");
								return ;
							}
						});//post
					}else if(rtnType =="endNode"){
						alert("当前流程已结束,不能执行退回操作。")
					}
					else{
						//alert("进入退回");
						$.post("<%=path%>/workflowDesign/action/processMonitor!findCurrentTask.action",
							{instanceId:processInstanceId,nodeId:nodeId},function(ret){
							if(ret != ""){
								var taskIds = ret.split(",");
								var taskId = taskIds[0];
								var width=screen.availWidth-10;
						    	var height=screen.availHeight-30;
								var ReturnStr=OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=return", 
								                                   width, height, window);
								if(ReturnStr){
									var ret = OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/initback.jsp", 
								                                   400, 340, window);
									if(ret){
								    	//$.post("<%=path%>/workflowDesign/action/processMonitor!back.action",{taskId:taskId,returnNodeId:ReturnStr,suggestion:encodeURI(ret),instanceId:processInstanceId,nodeId:nodeId},
								    	$.post("<%=path%>/workflowDesign/action/processMonitor!back.action",{taskId:taskId,returnNodeId:ReturnStr,suggestion:EE_EncodeCode(ret),instanceId:processInstanceId,nodeId:nodeId},
										   function(retCode){
										   		if(retCode == "0"){
										   			alert("退回成功。");
										   			location = scriptroot + "/workflowDesign/action/processMonitor!input.action?instanceId=" + processInstanceId+"&modelType="+'${modelType}'
								+"&proName="+encodeURI(encodeURI("${proName}"))+"&processId=${processId}";
										   		}else if(retCode == "-1"){
										   			alert("任务实例不存在或已删除。");
										   		}else if(retCode == "-2"){
										   			alert("任务退回过程中出现异常。");
										   		}else if(retCode == "-3"){
										   			alert("任务退回过程中出现异常。");
										   		}
										   }			
										);
									}                                   
								}	
							}else{
								alert("请在当前处理中的节点点击【退回】。");
								return ;
							}
						})//post
                 	}//else    
					
				});
			          
		}//function
		
		//编辑处理意见
		function editDesc(processInstanceId,nodeId) {
			$.post("<%=path%>/workflowDesign/action/processMonitor!findCurrentTask.action",
					{instanceId:processInstanceId,nodeId:nodeId},function(ret){
					if(ret != ""){
						var ret = OpenWindow("<%=path%>/workflowDesign/action/processMonitor!annalList.action?processId="+processInstanceId+
								 "&nodeId="+nodeId,
								 500, 400, window);
						if(ret){
							location.reload();
						}	
				}else{	
						alert("请在当前处理中的节点点击【编辑意见】。");
						return ;	
					}
				});
				
		}
		//查看子流程
		function viewSub(processInstanceId,nodeId,subProcessInstanceId){
		   var mytype=window.document.getElementById("mytype").value;
			window.location.href="<%=path%>/workflowDesign/action/processMonitor!input.action?instanceId="+subProcessInstanceId+"&mytype="+mytype;
		}
		
		function cancelTask(taskId){
			if(!confirm("您确定要取消此任务吗？")){
				return ;
			}
			//取消任务
			$.ajax({
	    		type:"post",
	    		url:"<%=root%>/workflowDesign/action/processMonitor!cancelTask.action",
	    		data:{
		    		taskId: taskId
	    		},
	    		success:function(result){
	    			location = "<%=root%>/workflowDesign/action/processMonitor!refreshMonitorPage.action?taskId=" + taskId;
	    		},
	    		error:function(){
	    			alert("异步出错！");
	    		}
	    	});
		}

		function cancelConcurrencyTask(taskId){
			if(!confirm("您确定要取消此任务吗？")){
				return ;
			}
			//取消并发任务
			var url = "<%=root%>/workflowDesign/action/processMonitor!getConcurrencyTask.action?taskId=" + taskId;
			window.showModalDialog(url,window,"dialogWidth:350pt;dialogHeight:450pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		}
	/**
	 * 字符编码，混合使用encodeURI()、escape()、encodeURIComponent()进行编码，
	 * 处理一些特殊字符所引发的问题
	 * @author 严建
	 * @date 2011年9月24日 15:12
	 * @param {} stringObj:被编码的字符串
	 * @return temp 编码之后的字符串
	 */
	function EE_EncodeCode(stringObj){
		if(stringObj==""||stringObj==undefined||stringObj=="undefined"){
			return stringObj;
		}
		var result = "";
		var charTemp = "";
		if(typeof(stringObj)!="string"){
			alert("EE_EncodeCode()方法的参数必须是字符串!");
			return;
		}
		for(var i=0;i<stringObj.length;i++){
			charTemp = stringObj.charAt(i);
			if(charTemp==encodeURI(encodeURI(charTemp))){
				if(charTemp==escape(escape(charTemp))){
					result += encodeURIComponent(encodeURIComponent(charTemp));
				}else{
					result += escape(escape(charTemp));
				}
			}else{
				result += encodeURI(encodeURI(charTemp));
			}
		}
		return result;
	}
	
	function formatDate(date){
		date = date.replace(".0","");
		document.write(date);
	}
	
		/**
		 * author:luosy
		 * description: 直接结束流程
		 *	instanceId 流程实例Id
		*/
		function endProcessInstance(){
			var instanceId ="${model[0].processInstanceId }";
			if(confirm("确定要直接结束流程吗?")){
	   		 	$.post("<%=root%>/senddoc/sendDoc!endProcessInstance.action",
				{instanceId:instanceId},
				function(data){
				    if(data=="true"){
				       alert("结束流程成功!");
					   window.location.reload();
	   		 		   window.close();
				    }else{
						alert("直接结束流程失败!");
						return;
					}
				});
	   		 }
		}
		
</script>

	</head>
	<body class="contentbodymargin">
		<!--  oncontextmenu="return false;"> -->
		<div id="contentborder" align="center">

			<table width="100%">
				<tr>
					<td height="40">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>
									&nbsp;
								</td>
								<input type="hidden" name="mytype" id="mytype" value="${mytype}" />
								<td width="25%">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td style="background:url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;font-weight: bold;color:white;cursor: pointer;" onclick="goback();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
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
			</table>
			<table width="98%" >
				<tr>
					<td align="center">
						<%
						String tokenInstanceId = (String) request.getAttribute("token");
						%>
						<c:if test="${mytype ==0}">
						<security:authorize ifAllGranted="001-0001000100160001" >	             
					  		<strongit:processImagePopToken token="<%=Long.parseLong(tokenInstanceId)%>"  isurger="0" canCancel="true" subprocessFunction="viewSub"
								extendsFunction="催办,urge,查看表单,viewForm,编辑意见,editDesc" />
					  	</security:authorize>
					  	<security:authorize ifAllGranted="001-0001000100160002">	             
					  		<strongit:processImagePopToken token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" canCancel="true" subprocessFunction="viewSub"
								extendsFunction="催办,urge,查看表单,viewForm,退回,doBackSpace" />
					  	</security:authorize>
					  	<security:authorize ifAllGranted="001-0001000100160001,001-0001000100160002">	             
					  		<strongit:processImagePopToken token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" canCancel="true" subprocessFunction="viewSub"
								extendsFunction="催办,urge,查看表单,viewForm,退回,doBackSpace,编辑意见,editDesc" />
					  	</security:authorize>
					  	
					  	<security:authorize ifNotGranted="001-0001000100160001">	             
					  		<strongit:processImagePopToken token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" subprocessFunction="viewSub"
								extendsFunction="催办,urge,查看表单,viewForm" /> 
					  	</security:authorize>
						</c:if>
					   <c:if test="${mytype !=0}">
					   	<strongit:processImagePopToken token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" canCancel="true" subprocessFunction="viewSub"
							extendsFunction="催办,urge,查看表单,viewForm,退回,doBackSpace,编辑意见,editDesc" /> 
					   </c:if>
					</td>
				</tr>
			</table>
			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="6" align="center" valign="bottom">
						<h4>
							流程监控
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD" align="center">
						&nbsp;流程名称
					</td>
					<td class="titleTD" align="center">
						&nbsp;流程号
					</td>
					<td class="titleTD" align="center">
						&nbsp;流程开始时间
					</td>
					<td class="titleTD" align="center">
						&nbsp;流程结束时间
					</td>
					<td class="titleTD" align="center">
						&nbsp;流程状态
					</td>
					<td class="titleTD" align="center">
						&nbsp;是否超期
					</td>
				</tr>
				<tr>
					<td align="center">
						&nbsp;${model[0].processName }
					</td>
					<td align="center">
						&nbsp;${model[0].processInstanceId }
						<input type="hidden" name="processId"
							value="${model[0].processInstanceId }">
					</td>
					<td align="center">
						&nbsp;${model[0].startDate }
					</td>
					<td align="center">
						&nbsp;${model[0].endDate }
					</td>
					<td id="statustd" name="statustd" align="center">
						&nbsp;${model[0].status }
					</td>
					<td id="statustd" name="statustd" align="center">
						&nbsp;
						<script> wirteTimeout("${model[0].isTimeout }");</script>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>

			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="5" align="center" valign="bottom">
						<h4>
							任&nbsp;&nbsp;&nbsp;&nbsp;务
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD" align="center">
						&nbsp;任务号
					</td>
					<td class="titleTD" align="center">
						&nbsp;任务名称
					</td>
					<td class="titleTD" align="center">
						&nbsp;处理人
					</td>
					<td class="titleTD" align="center">
						&nbsp;完成时间
					</td>
					<td class="titleTD" align="center">
						&nbsp;是否超期
					</td>
				</tr>
				<c:forEach items="${model[1]}" var="task" varStatus="status">
					<tr>
						<td align="center">
							&nbsp;
							<c:out value="${task.taskId}" />
						</td>
						<td align="center">
							&nbsp;
							<c:out value="${task.taskName}" />
						</td>
						<td align="center">
							&nbsp;
							<c:out value="${task.taskActorName}" />
						</td>
						<td align="center">
							&nbsp;
							<c:out value="${task.endDate}" />
						</td>
						<td align="center">
							&nbsp;
						<script> wirteTimeout("${task.isTimeout}");</script>
						</td>
					</tr>
				</c:forEach>
			</table>

			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>
			<c:if test="${modelType != 'person'}">
			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="2" align="center" valign="bottom">
						<h4>
							参&nbsp;&nbsp;&nbsp;&nbsp;数
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD"align="center">
						&nbsp;参数名称
					</td>
					<td class="titleTD" align="center">
						&nbsp;参数值
					</td>
				</tr>
				<c:forEach items="${model[2]}" var="variour" varStatus="status">
					<c:if test="${variour.name!='currentTransition' }">
						<tr>
							<td align="center">
								&nbsp;
								<c:out value="${variour.name }" />
							</td>
							<td align="center">
								&nbsp;
								<c:out value="${variour.value }" />
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>
			</c:if>
			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="6" align="center" valign="bottom">
						<h4>
							流程日志
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD" nowrap align="center">
						&nbsp;序号
					</td>
					<td class="titleTD" nowrap align="center">
						&nbsp;公文名称
					</td>
					<td class="titleTD" nowrap align="center">
						&nbsp;相关任务
					</td>
					<td class="titleTD" nowrap align="center">
						&nbsp;相关处理人
					</td>
					<td class="titleTD" nowrap align="center">
						&nbsp;处理时间
					</td>
					<td class="titleTD" nowrap align="center">
						&nbsp;处理动作
					</td>
				</tr>
				<c:forEach items="${model[6]}" var="log" varStatus="status">
					<tr>
						<td nowrap align="center">
							&nbsp;
							<c:out value="${status.count }" />
						</td>
						<td nowrap align="center">
							&nbsp;
							<c:out value="${log.plPiname }" />
						</td>
						<td nowrap align="center">
							&nbsp;
							<c:out value="${log.plTaskname }" />
						</td>
						<td nowrap align="center">
							&nbsp;
							<c:out value="${log.plActorname }" />
						</td>
						<td nowrap align="center">
							&nbsp;
							<script>formatDate('<c:out value="${log.plDate}" />')</script>
							
						</td>
						<td nowrap align="center">
							&nbsp;
							<c:out value="${log.plAction }" />
						</td>
					</tr>
				</c:forEach>
			</table>
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>

			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="2" align="center" valign="bottom">
						<h4>
							父&nbsp;&nbsp;流&nbsp;&nbsp;程
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD" align="center">
						&nbsp;流程实例ID
					</td>
				</tr>
				<c:forEach items="${model[4]}" var="variour" varStatus="status">
					<tr>
						<td align="center">
							&nbsp;
							<a
								href='<%=root%>/workflowDesign/action/processMonitor!input.action?instanceId=<c:out value="${variour[0] }"/>'><c:out
									value="${variour[2] }" />【<c:out value="${variour[1] }" />】</a>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>

			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="2" align="center" valign="bottom">
						<h4>
							子&nbsp;&nbsp;流&nbsp;&nbsp;程
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD" align="center">
						&nbsp;流程实例ID
					</td>
				</tr>
				<c:forEach items="${model[5]}" var="variour" varStatus="status">
					<tr>
						<td align="center">
							&nbsp;
							<a
								href='<%=root%>/workflowDesign/action/processMonitor!input.action?instanceId=<c:out value="${variour[0] }"/>'><c:out
									value="${variour[2] }" />【<c:out value="${variour[1] }" />】</a>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>
		</div>
	</body>
</html>
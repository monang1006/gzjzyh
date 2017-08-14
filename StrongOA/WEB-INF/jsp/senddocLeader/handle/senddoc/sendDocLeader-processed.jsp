<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="com.strongit.oa.common.service.BaseWorkflowManager" />
<jsp:directive.page import="com.strongit.oa.senddoc.manager.SendDocManager" />
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-query" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>已办事宜</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/senddoc/exportExcel.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
			.tabletitle {
				FILTER: progid : DXImageTransform.Microsoft.Gradient ( 
			                             gradientType = 0, 
			                             startColorStr = #ededed, 
			                             endColorStr =   #ffffff );
			}
			.hand {
				cursor: pointer;
			}
		</style>
		<script type="text/javascript">
		//重定向到此页面
		function reloadPage() {
	      	var privilName = "";
	      	var sortType = "";
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
   		try{
	    	 sortType = $('input[name="sortType"]:checked').val();
	    }catch(e){
	    }
      	window.location = "<%=root%>/senddoc/sendDoc!processed.action?state="
      				+$("#state").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
      				+"&formId="+$("#formId").val()+"&privilName="+privilName+"&workflowType="+workflowType
      				+"&handleKind="+$("#handleKind").val()+"&showSignUserInfo="+$("#showSignUserInfo").val()+"&sortType="+sortType;
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
			TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
			var taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			var width=screen.availWidth-10;;
			var height=screen.availHeight-30;
			WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId+"&type=processurgency",'Cur_workflowView',width, height, "办理记录");
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
	      //重新办理
	      function gotoReDone(){
	      	  var TaskIdAndInstanceId = getValue();
	          if(TaskIdAndInstanceId == ""){
	          	alert("请选择要重新办理的流程！");
	          	return ;
	          }else{
	          	var taskIds = TaskIdAndInstanceId.split(",");
	          	if(taskIds.length>1){
	          		alert("一次只能办理一个流程！");
	          		return ;
	          	}
	          }  
	      	  TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
			  var taskId = TaskIdAndInstanceId[0];
			  var instanceId = TaskIdAndInstanceId[1];
			  $.getJSON("<%=root%>/senddoc/sendDocWorkflow!getCurrentTaskHandler.action?timeStamp="+new Date(),
 				{instanceId:instanceId},function(json){
 					var status = json.status;
 					if(status == "-1"){//发生错误
 						alert("对不起，系统发生错误，请与管理员联系。");
 					}else if(status == "0" || status == "1"){
 						if(json.isBackspace != "1"){
 							alert("对不起，此文不是由上一办理人退回的，不能重新办理。");
 							return ;
 						}
 						//确认是否重新办理
 						if(confirm("您确定要重新办理[" + json.businessName + "]吗？\n点击确定将清空此文的所有办理记录。")){
 							$.post("<%=root%>/senddoc/sendDocWorkflow!clearProcessInstance.action?timeStamp="+new Date(),
 							{instanceId:instanceId},function(retCode){
 								if(retCode == "0"){
			 						var width=screen.availWidth-10;
					  				var height=screen.availHeight-30;
			 						var ret=WindowOpen("<%=root%>/senddoc/sendDoc!input.action?taskFlag=notNeedCheck&taskId="+json.taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI(json.workflowName)),'todo',width, height, "待办事宜");
 								}else{
 									alert("对不起，系统发生错误，请与管理员联系。");
 								}
 							});
 						}
 					} else if(status == "2"){//发起人和当前用户不一致
 						alert("对不起，您不是主办人员，不能重新办理此文。");
 					}else if(status == "3"){//流程已结束，不能办理
 						alert("对不起，流程已结束，不能重新办理。");
 					}
 				});
	      }
	      
	      var jsonObj = null;
	      //代办意见
	      function gotoDone(){
	      	  var TaskIdAndInstanceId = getValue();
	          if(TaskIdAndInstanceId == ""){
	          	alert("请选择要代办的流程！");
	          	return ;
	          }else{
	          	var taskIds = TaskIdAndInstanceId.split(",");
	          	if(taskIds.length>1){
	          		alert("一次只能代办一个流程！");
	          		return ;
	          	}
	          }  
	      	  TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
			  var taskId = TaskIdAndInstanceId[0];
			  var instanceId = TaskIdAndInstanceId[1];
			  $.getJSON("<%=root%>/senddoc/sendDocWorkflow!getCurrentTaskHandler.action?timeStamp="+new Date(),
 				{instanceId:instanceId},function(json){
 					var status = json.status;
 					if(status == "-1"){//发生错误
 						alert("对不起，系统发生错误，请与管理员联系。");
 					}else if(status == "0"){//不存在主办人员代办
 						alert("对不起，["+json.businessName+"]的处理人未设置主办人员代办，操作失败。");
 					}else if(status == "1"){//存在主办人员代办
 						jsonObj = json.user;//传递此对象到子窗口中
 						var width=screen.availWidth-10;
		  				var height=screen.availHeight-30;
 						var ret=WindowOpen("<%=root%>/senddoc/sendDocWorkflow!input.action?taskId="+json.taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI(json.workflowName)),'todo',width, height, "待办事宜");
 					} else if(status == "2"){//发起人和当前用户不一致
 						alert("对不起，您不是主办人员，不能办理此文。");
 					}else if(status == "3"){//流程已结束，不能办理
 						alert("对不起，流程已结束，不能继续办理。");
 					}
 				});
	      }
	      
	       function exportfils(){
			if(confirm("您是否要将信息导出为EXCEL？")){
				var action = $("#myTableForm").attr("action");
				var url="<%=root%>/senddoc/sendDocUpload!exportProcessedExcel.action";
				$("#myTableForm").attr("target","tempframe");
				$("#myTableForm").attr("action",url);
				$("#button").click();
				$("#myTableForm").removeAttr("target");
				$("#myTableForm").attr("action",action);
			}
		}
    </script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
		<iframe scr='' id='tempframe' name='tempframe' style='display: none'></iframe>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td width="30%">
									&nbsp;<img src="<%=frameroot%>/images/ico.gif" width="9" height="9" alt="">
									<s:if test="workflowName == null || workflowName.length() == 0">
										<%=privilName%>${privilName}
									</s:if>
									<s:else>
										${workflowName }
									</s:else>
								</td>
								<td width=70%>
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td>
												<a class="Operation" href="javascript:gotoView();">
													<img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">
													查阅&nbsp;</a>
											</td>
											<td width="5">
											</td>
											<s:if test="state!=1">
												<td>
													<a class="Operation" href="javascript:sendReminder();">
														<img src="<%=root%>/images/ico/emergency.gif" width="15" height="15" class="img_s">
														催办&nbsp;</a>
												</td>
												<td width="5"></td>
											</s:if>
											<td>
												<a class="Operation" href="javascript:workflowView();">
													<img src="<%=root%>/images/ico/chakanlishi.gif" width="15" height="15" class="img_s">
													办理记录&nbsp;</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="#" onclick="doHide();">
													<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s"> 
													<label id="label_search">
														显示查询条件&nbsp;
													</label> </a>
											</td>
											<td width="5">
											</td>
											<%----%>
											<!--<s:if test="state!=1">
												<td>
													<a class="Operation" href="javascript:fetchTask();">
														<img src="<%=root%>/images/ico/cexiao.gif" width="15" height="15" class="img_s">
														取回流程&nbsp;</a>
												</td>
												<td width="5">
												</td>
											</s:if>-->
											
											<s:if test="state!=1">
												<td>
													<%--
													<a class="Operation" href="javascript:exportfils();">
														<img src="<%=root%>/images/ico/weituo.gif" width="15" height="15" class="img_s">
														导出为EXCEL&nbsp;</a>
													--%>
													<a class="Operation" href="javascript:CellAreaExcel();">
														<img src="<%=root%>/images/ico/daochu.gif" width="15" height="15" class="img_s">
														导出为EXCEL&nbsp;</a>
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
				<tr>
					<td height="100%">
						<s:form name="myTableForm" id="myTableForm" action="/senddoc/sendDoc!processed.action">
							<!--发文处理已办显示列 【文件标题	  发文类别	发文单位	当前办理人	当前部门】	  -->
							<!--发文处理办结显示列 【文件标题	  发文类别	发文单位	办结日期	     主办人】	  -->
							<%
								List showColumnList = new ArrayList((List) request.getAttribute("showColumnList"));
									String[] column1 = (String[]) showColumnList.get(0);//value
									String[] column2 = null;
									String[] column3 = null;
									String[] column4 = null;
									String[] column5 = null;
									String[] column6 = null;
									String[] column7 = null;
									String[] column8 = null;
									String[] column9 = null;
									String[] column10 = null;
									String[] column11 = null;
									for (int i = 0; i < showColumnList.size(); i++) {
										Object[] column = (Object[]) showColumnList.get(i);
										String columnName = (String) column[0];
										if (columnName.toUpperCase().equals("PNG".toUpperCase())) {
											column2 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"WORKFLOWTITLE".toUpperCase())) {
											column3 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"WORKFLOWNAME".toUpperCase())) {
											column4 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"SENDDOC_ISSUE_DEPART_SIGNED".toUpperCase())) {
											column5 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"CRUUENTUSERNAME".toUpperCase())) {
											column6 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"CRUUENTUSERDEPT".toUpperCase())) {
											column7 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"PROCESSENDDATE".toUpperCase())) {
											column8 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"STARTUSERNAME".toUpperCase())) {
											column9 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
									}
							%>
							<webflex:flexTable name="myTable" width="100%" height="200px" wholeCss="table1"
								property="senddocId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<input name="button" id="button" type="submit" style="display: none">
								<!-- 时间戳 -->
								<input id="shijianchuo" name="shijianchuo" type="hidden" value="<%=new Date()%>">
								<!-- 流程名称 -->
								<s:hidden id="workflowName" name="workflowName"></s:hidden>
								<!-- 表单id -->
								<s:hidden id="formId" name="formId"></s:hidden>
								<!-- 表名称 -->
								<s:hidden id="tableName" name="tableName"></s:hidden>
								<!-- 公文处理类别 0：个人办公 1：发文处理 2：收文处理-->
								<s:hidden id="handleKind" name="handleKind"></s:hidden>
								<!-- 流程状态 0:未办结；1:办结 -->
								<s:hidden id="state" name="state"></s:hidden>
								<!-- 流程类型 -->
								<s:hidden name="workflowType"></s:hidden>
								<!-- 是否过滤签收数据 -->
								<s:hidden name="filterSign"></s:hidden>
								<!-- 流程类型 -->
								<s:hidden name="showSignUserInfo" id="showSignUserInfo"></s:hidden>
								<!-- 资源名称 -->
								<input type="hidden" name="privilName" id="privilName" value="<%=privilName%>" />
								<strong:query queryColumnList="${queryColumnList}" />
								<input type="hidden" id="sortTypeHidden" value="">
								<input type="hidden" id="filterYJZXHidden" value="">
								<div>
									<s:if test="%{state!=\"1\"}">
	               						${sortHtml}
	              					</s:if>
									<%--
									<s:if test="workflowType == 3 ">
										<s:if test="state!=1">
              								${yjzxHtml }
										</s:if>
									</s:if>
									--%>
								</div>
								<s:if test="state!=1">
									<%
										if (column1 != null) {
									%>
									<webflex:flexCheckBoxCol caption="选择" property="<%=column1[0]%>"
										showValue="<%=column1[3]%>" width="10%" isCheckAll="true" isCanDrag="false"
										isCanSort="false"></webflex:flexCheckBoxCol>
									<%
										}
									%>
									<%
										if (column2 != null) {
									%>
									<webflex:flexFlagCol caption="" property="<%=column1[0]%>" showValue="<%=column2[3]%>"
										isCanDrag="false" isCanSort="false"></webflex:flexFlagCol>
									<%
										}
									%>
									<%
										if (column3 != null) {
									%>
									<webflex:flexTextCol caption="<%=column3[1]%>" property="<%=column1[0]%>"
										showValue="<%=column3[0]%>" width="45%" isCanDrag="false"
										onclick="ViewFormAndWorkflow(this.value);" isCanSort="false" showsize="25"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column4 != null) {
									%>
									<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column4[3]%>"
										showValue="<%=column4[0]%>" width="15%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column5 != null) {
									%>
									<webflex:flexTextCol caption="<%=column5[1]%>" property="<%=column5[3]%>"
										showValue="<%=column5[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column6 != null) {
									%>
									<webflex:flexTextCol caption="<%=column6[1]%>" property="<%=column6[3]%>"
										showValue="<%=column6[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column7 != null) {
									%>
									<webflex:flexTextCol caption="<%=column7[1]%>" property="<%=column7[3]%>"
										showValue="<%=column7[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
								</s:if>
								<s:else>
									<%
										if (column1 != null) {
									%>
									<webflex:flexCheckBoxCol caption="选择" property="<%=column1[0]%>"
										showValue="<%=column1[3]%>" width="3%" isCheckAll="true" isCanDrag="false"
										isCanSort="false"></webflex:flexCheckBoxCol>
									<%
										}
									%>
									<%
										if (column3 != null) {
									%>
									<webflex:flexTextCol caption="<%=column3[1]%>" property="<%=column1[0]%>"
										showValue="<%=column3[0]%>" width="47%" isCanDrag="false"
										onclick="ViewFormAndWorkflow(this.value);" isCanSort="false" showsize="27"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column4 != null) {
									%>
									<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column4[3]%>"
										showValue="<%=column4[0]%>" width="15%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column5 != null) {
									%>
									<webflex:flexTextCol caption="<%=column5[1]%>" property="<%=column5[3]%>"
										showValue="<%=column5[0]%>" width="15%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column8 != null) {
									%>
									<webflex:flexDateCol caption="<%=column8[1]%>" property="<%=column8[3]%>"
										showValue="<%=column8[0]%>" width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd"
										isCanSort="true"></webflex:flexDateCol>
									<%
										}
									%>
									<%
										if (column9 != null) {
									%>
									<webflex:flexTextCol caption="<%=column9[1]%>" property="<%=column9[3]%>"
										showValue="<%=column9[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
									<%
										}
									%>
								</s:else>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
		correctPNG(); 	//IE6中正常显示透明PNG 
		var sMenu = new Menu();
		
		function initMenuT(){
	        sMenu.registerToDoc(sMenu);
	        var item = null;
	        item = new MenuItem("<%=root%>/images/ico/page.gif","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	   		<s:if test="state!=1">
		   		item = new MenuItem("<%=root%>/images/ico/emergency.gif","催办","sendReminder",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
	        </s:if>
	    	item = new MenuItem("<%=root%>/images/ico/chakanlishi.gif","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
	       // <s:if test="state!=1">
		      //  item = new MenuItem("<%=root%>/images/ico/cexiao.gif","取回流程","fetchTask",1,"ChangeWidthTable","checkMoreDis");
		        //sMenu.addItem(item);
	        //</s:if>
	        sMenu.addShowType("ChangeWidthTable");
	        registerMenu(sMenu);
		}
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
			var instanceId = result[1];
			var overDate = result[2];
		 	if(overDate == "1"){
		 		alert("流程已结束，无需催办！");
		 		return;
		 	}
		 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
		}
        
       $(document).ready(function(){
	 		if('${workflowName}' != ""){
        
	        }else{
	        	var privilName = "<%=privilName%>";
	        	if(privilName ==""){
		       		privilName = "${privilName}";
		       		if(privilName == "" && $("#type").val()=='notsign'){
		       			privilName = "待签收文件";
		       		}
	        	}
	        	$("#privilName").val(privilName);
	        	$("#privilTitle").html($("#privilName").val());
			}
	 		<s:if test="state!=1">
		   		//$("input[name='WORKFLOWTITLE']:eq(0)").after("&nbsp;<span class=\"wz\">流程状态</span><input style=\"height:20px\"  id=\"nodeName\" name=\"nodeName\" value=\"${nodeName}\" type=\"text\" />")
			</s:if>
		});
       
		/**
			@param TaskIdAndInstanceId 任务id$流程实例id
		*/
		function ViewFormAndWorkflow(TaskIdAndInstanceId) {
			TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
			var taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			var state = "${state}";
			//alert(state);
	      	var width=screen.availWidth-10;
	   		var height=screen.availHeight-30;
	      	var ret=WindowOpen("<%=root%>/senddocLeader/sendDocLeader!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");
     		if(ret){
     			if(ret == "OK"){
     				reloadPage();
     			}
			}
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
	      	TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
			var taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			var state = "${state}";
	      	var width=screen.availWidth-10;
	   		var height=screen.availHeight-30;
	      	var ret=WindowOpen("<%=root%>/senddocLeader/sendDocLeader!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");
     		if(ret){
     			if(ret == "OK"){
     				reloadPage();
     			}
     	  	}
		}
      
		//取回任务
		function fetchTask(){
	      	var TaskIdAndInstanceId = getValue();
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
	</body>
</html>

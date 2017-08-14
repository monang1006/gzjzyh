<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="com.strongit.oa.common.service.BaseWorkflowManager" />
<jsp:directive.page import="com.strongit.oa.senddoc.manager.SendDocManager" />
<jsp:directive.page import="com.strongit.oa.component.formtemplate.util.FormGridDataHelper" />
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-query" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>已办事宜</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
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
			                             endColorStr = #ffffff );
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
		          	alert("请选择要查看的办理记录。");
		          	return ;
          		}else{
		          	var taskIds = TaskIdAndInstanceId.split(",");
		          	if(taskIds.length>1){
		          		alert("只可以查看一条办理记录。");
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
				      			alert("该任务不存在或已删除。");
				      		}else if(retCode == "0"){
				      			alert('该流程实例已经结束，无法取回。');	
				      		}else if(retCode == "1"){
				      			alert('该任务的后继任务已被处理或正在处理，不允许取回。');
				      		}else if(retCode == "2"){
				      			alert('流程已成功取回。');
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
			    	alert("请选择要重新办理的记录。");
			      	return ;
			    }else{
			    	var taskIds = TaskIdAndInstanceId.split(",");
			      	if(taskIds.length>1){
			      		alert("只可以重新办理一条记录。");
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
				 	alert("请选择要代办的记录。");
				 	return ;
				}else{
					var taskIds = TaskIdAndInstanceId.split(",");
				 	if(taskIds.length>1){
				 		alert("只可以代办一条记录。");
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
			if(confirm("确定要将当前页内容导出为EXCEL？")){
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
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>
									<s:if test="workflowName == null || workflowName.length() == 0">
										<%=privilName%>${privilName}
									</s:if>
									<s:else>
										${workflowName }
									</s:else>
									</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="relativeWf();"><img src="<%=root%>/images/operationbtn/urge.png"/>&nbsp;关&nbsp;联&nbsp;流&nbsp;程&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<s:if test="state!=1">
												<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 	<td class="Operation_list" onclick="sendReminder();"><img src="<%=root%>/images/operationbtn/urge.png"/>&nbsp;催&nbsp;办&nbsp;</td>
							                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                 	<td width="5"></td>
							                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 	<td class="Operation_list" onclick="feedBack();"><img src="<%=root%>/images/operationbtn/Reply.png"/>&nbsp;反&nbsp;馈&nbsp;</td>
							                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                 	<td width="5"></td>
											</s:if>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<s:if test="state!=1">
												<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 	<td class="Operation_list" onclick="fetchTask();"><img src="<%=root%>/images/operationbtn/Retrieve_the_process.png"/>&nbsp;取&nbsp;回&nbsp;</td>
							                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                 	<td width="5"></td>
											</s:if>
						                 	<s:if test="state!=1">
						                 		<%--
													<a class="Operation" href="javascript:exportfils();">
														<img src="<%=root%>/images/ico/weituo.gif" width="15" height="15" class="img_s">
														导出为EXCEL&nbsp;</a>
													--%>
												<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 	<td class="Operation_list" onclick="CellAreaExcel();"><img src="<%=root%>/images/operationbtn/Export_to_excel.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;EXCEL&nbsp;</td>
							                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                 	<td width="5"></td>
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
						<s:form name="myTableForm" id="myTableForm" action="/senddoc/sendDoc!processed.action" >
							<!--收文处理已办显示列 【收文编号	文件标题	来文单位	来文字号	来文日期	当前办理人	当前部门】	  -->
							<!--收文处理办结显示列 【收文编号	文件标题	来文单位	来文字号	来文日期	办结日期	主办人】	  -->
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
												"RECV_NUM".toUpperCase())) {
											column3 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"WORKFLOWTITLE".toUpperCase())) {
											column4 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"ISSUE_DEPART_SIGNED".toUpperCase())) {
											column5 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"DOC_NUMBER".toUpperCase())) {
											column6 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"RECV_TIME".toUpperCase())) {
											column7 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"CRUUENTUSERNAME".toUpperCase())) {
											column8 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"CRUUENTUSERDEPT".toUpperCase())) {
											column9 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"PROCESSENDDATE".toUpperCase())) {
											column10 = (String[]) column;
											showColumnList.remove(i);
											i--;
											continue;
										}
										if (columnName.toUpperCase().equals(
												"STARTUSERNAME".toUpperCase())) {
											column11 = (String[]) column;
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
									<webflex:flexTextCol caption="<%=column3[1]%>" property="<%=column3[3]%>" align="center"
										showValue="<%=column3[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column4 != null) {
									%>
									<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column1[0]%>"
										showValue="<%=column4[0]%>" width="28%" isCanDrag="false" isCanSort="false"
										onclick="ViewFormAndWorkflow(this.value);" showsize="17"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column5 != null) {
									%>
									<webflex:flexTextCol caption="<%=column5[1]%>" property="<%=column5[3]%>" align="center"
										showValue="<%=column5[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column6 != null) {
									%>
									<webflex:flexTextCol caption="<%=column6[1]%>" property="<%=column6[3]%>" align="center"
										showValue="<%=column6[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column7 != null) {
									%>
									<webflex:flexDateCol caption="<%=column7[1]%>" property="<%=column7[3]%>"
										showValue="<%=column7[0]%>" width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd"
										isCanSort="true"></webflex:flexDateCol>
									<%
										}
									%>
									<%
										if (column8 != null) {
									%>
									<webflex:flexTextCol align="center"  caption="<%=column8[1]%>" property="<%=column8[3]%>" align="center"
										showValue="<%=column8[0]%>" width="12%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column8 != null) {
									%>
									<webflex:flexTextCol  align="center" caption="<%=column9[1]%>" property="<%=column9[3]%>" align="center"
										showValue="<%=column9[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
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
									<webflex:flexTextCol caption="<%=column3[1]%>" property="<%=column3[3]%>" align="center"
										showValue="<%=column3[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column4 != null) {
									%>
									<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column1[0]%>"
										showValue="<%=column4[0]%>" width="35%" isCanDrag="false" isCanSort="false"
										onclick="ViewFormAndWorkflow(this.value);" showsize="14"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column5 != null) {
									%>
									<webflex:flexTextCol caption="<%=column5[1]%>" property="<%=column5[3]%>" align="center"
										showValue="<%=column5[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column6 != null) {
									%>
									<webflex:flexTextCol caption="<%=column6[1]%>" property="<%=column6[3]%>" align="center"
										showValue="<%=column6[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
									<%
										}
									%>
									<%
										if (column7 != null) {
									%>
									<webflex:flexDateCol caption="<%=column7[1]%>" property="<%=column7[3]%>"
										showValue="<%=column7[0]%>" width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd"
										isCanSort="true"></webflex:flexDateCol>
									<%
										}
									%>
									<%
										if (column10 != null) {
									%>
									<webflex:flexDateCol caption="<%=column10[1]%>" property="<%=column10[3]%>"
										showValue="<%=column10[0]%>" width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd"
										isCanSort="true"></webflex:flexDateCol>
									<%
										}
									%>
									<%
										if (column11 != null) {
									%>
									<webflex:flexTextCol caption="<%=column11[1]%>" property="<%=column11[3]%>" align="center"
										showValue="<%=column11[0]%>" width="12%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
										}
									%>
								</s:else>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			<div style="display: none;">
				<s:select id="nodeName" name="nodeName" list="#{'':'全部','0':'办文','1':'送领导','2':'会文','3':'意见征询','4':'办结'}" listKey="key" listValue="value" style="height:10px;width:150px;" onchange='$("#img_sousuo").click();'/>
			</div>
		</div>
		<script language="javascript">
			correctPNG(); 	//IE6中正常显示透明PNG
			var sMenu = new Menu();
			
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				 item = new MenuItem("<%=root%>/images/operationbtn/urge.png","关联流程","relativeWf",1,"ChangeWidthTable","checkMoreDis");
			        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		        <s:if test="state!=1">
		        	item = new MenuItem("<%=root%>/images/operationbtn/urge.png","催办","sendReminder",1,"ChangeWidthTable","checkOneDis");
		        	sMenu.addItem(item);
		        	item = new MenuItem("<%=root%>/images/operationbtn/Reply.png","反馈","feedBack",1,"ChangeWidthTable","checkOneDis");
			        sMenu.addItem(item);
		        </s:if>
	    		item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
	        	sMenu.addItem(item);
				<s:if test="state!=1">
			        item = new MenuItem("<%=root%>/images/operationbtn/Retrieve_the_process.png","取回","fetchTask",1,"ChangeWidthTable","checkMoreDis");
			    	sMenu.addItem(item);
				</s:if>
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
			
			 //关联流程
		      function relativeWf(){
		          var taskId = getValue();
		          if(taskId == ""){
		          	alert("请选择要关联的记录。");
		          	return ;
		          }else{
		          	var taskIds = taskId.split(",");
		          	if(taskIds.length>1){
		          		alert("只可以关联一条记录。");
		          		return ;
		          	}
		          }
		      	var TaskIdAndInstanceId = getValue();
		          TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
					var instanceId = TaskIdAndInstanceId[1];
		          //var iWidth=1200; //弹出窗口的宽度;
					//var iHeight=450; //弹出窗口的高度;
					//var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
					//var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
					//WindowOpen("<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId="+instanceId,'todo',iWidth, iHeight,iTop,iLeft);
					showModalDialog("<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId="+instanceId, 
		                  window,"dialogWidth:750px;dialogHeight:420px;status:no;help:no;scroll:yes;");
		     }
			//催办
			function sendReminder(){
				var id=getValue();
				var returnValue = "";
				if(id == ""){
					alert("请选择要催办的记录。");
					return ;
				}else{
					var ids = id.split(",");
					if(ids.length>1){
						alert("只可以催办一条记录。");
						return ;
					}
				}
			 	var result = id.split("$");
				var instanceId = result[1];
				var overDate = result[2];
			 	if(overDate == "1"){
			 		alert("流程已结束，无需催办。");
			 		return;
			 	}
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 215, window);
			}
			//反馈
			function feedBack(){
				var id=getValue();
				var returnValue = "";
				if(id == ""){
					alert("请选择要反馈的记录。");
					return ;
				}else{
					var ids = id.split(",");
					if(ids.length>1){
						alert("只可以反馈一条记录。");
						return ;
					}
				}
			 	var result = id.split("$");
				var instanceId = result[1];
				var overDate = result[2];
			 	if(overDate == "1"){
			 		alert("流程已结束，无需反馈。");
			 		return;
			 	}
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose1.jsp?instanceId="+instanceId+"&feedBack=true",560, 290, window);
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
					<%
						String enabelYzjx = FormGridDataHelper.getColorSetProperties().getProperty("enableYjzx");
						if("1".equals(enabelYzjx)){
							%>
								<s:if test="state!=1">
							    	//$("input[name='WORKFLOWTITLE']:eq(0)").after("&nbsp;<span class=\"wz\">流程状态</span><input style=\"height:20px\"  id=\"nodeName\" name=\"nodeName\" value=\"${nodeName}\" type=\"text\" />")
							    	$("input[name='WORKFLOWTITLE']:eq(0)").after($("#nodeName")).after("&nbsp;<span class=\"wz\">流程状态:&nbsp;</span>");
								</s:if>
							<%
						}
					%>
			}); 
			/**
				@param TaskIdAndInstanceId 任务id$流程实例id
			*/
			function ViewFormAndWorkflow(TaskIdAndInstanceId) {
			TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
			var taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			var businessType = TaskIdAndInstanceId[3];
			var state = "${state}";
			//alert(state);
	      	var width=screen.availWidth-10;
	   		var height=screen.availHeight-30;
	      	var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId+"&businessType="+businessType,'processed',width, height, "");
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
					alert("请选择要查阅的记录。");
					return ;
				}else{
					var taskIds = TaskIdAndInstanceId.split(",");
					if(taskIds.length>1){
						alert("只可以查阅一条记录。");
						return ;
					}
        		}  
		      	TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
				var taskId = TaskIdAndInstanceId[0];
				var instanceId = TaskIdAndInstanceId[1];
				var businessType = TaskIdAndInstanceId[3];
				var state = "${state}";
		      	var width=screen.availWidth-10;
		   		var height=screen.availHeight-30;
		      	var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId+"&businessType="+businessType,'processed',width, height, "");
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
		      		alert("请选择要取回的记录。");
		      	}else{
		      		var taskIds = TaskIdAndInstanceId.split(",");
		      		if(taskIds.length>1){
		      			alert("只可以取回一条记录。");
		      			return ;
		      		}
		      		if(confirm("确定要取回吗？")){
		      			var TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
				  		var taskId = TaskIdAndInstanceId[0];
			      		doFetchTask(taskId);
			      	}		
		      	}
			}
    	</script>
	</body>
</html>

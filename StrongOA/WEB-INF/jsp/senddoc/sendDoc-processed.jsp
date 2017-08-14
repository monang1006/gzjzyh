<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="com.strongit.oa.common.service.BaseWorkflowManager" />
<jsp:directive.page import="com.strongit.oa.senddoc.manager.SendDocManager" />
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-query" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>已办事宜</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/senddoc/exportExcel.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
<style media="screen" type="text/css">
.tabletitle {
	FILTER: progid : DXImageTransform.Microsoft.Gradient ( 
                     	gradientType = 0, startColorStr = #ededed, endColorStr =   #ffffff );
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
	      				+"&formId="+$("#formId").val()+"&showSignUserInfo="+$("#showSignUserInfo").val()
	      				+"&privilName="+privilName+"&workflowType="+workflowType+"&sortType="+sortType;
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
			var width=screen.availWidth-10;
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
			      			alert('该任务的后继任务已被签收处理，不允许取回。');
			      		}else if(retCode == "2"){
			      			alert('流程已成功取回。');
			      			reloadPage();
			      		}else if(retCode == "-2"){
			      			alert("取回流程失败。");
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
				    /*
					 var url="<%=root%>/senddoc/sendDocUpload!exportProcessedExcel.action";
					 var pama="?1=1";
				 	 pama += ("&workflowName="+encodeURI(encodeURI($("#workflowName").val())));
				 	 pama += ("&formId="+$("#formId").val());
				 	 pama += ("&state="+$("#state").val());
				 	 pama += ("&privilName="+encodeURI(encodeURI($("#privilName").val())));
				 	 pama += ("&page.pageNo=${page.pageNo}");
				 	 pama += ("&page.pageSize=${page.pageSize}");
				 	 pama += ("&showSignUserInfo="+$("#showSignUserInfo").val());
					 url=(url+pama);
					 document.getElementById('tempframe').src=url;
					 */
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
											</s:if>
											<%--
											<td >
							                	<a class="Operation" href="javascript:gotoDone();">
							                		<img src="<%=root%>/images/ico/banli.gif" width="15" height="15" class="img_s">
							                		代办意见&nbsp;</a> 
							                </td>
							                <td width="5"></td>
							                <td >
							                  <a class="Operation" href="javascript:gotoReDone();">
							                  	<img src="<%=root%>/images/ico/banli.gif" width="15" height="15" class="img_s">
							                  	重新办理&nbsp;</a> 
							                </td>
							                <td width="5"></td>
								            --%>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	 <s:if test="state!=1">
												<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 	<td class="Operation_list" onclick="fetchTask();"><img src="<%=root%>/images/operationbtn/Retrieve_the_process.png"/>&nbsp;取&nbsp;回&nbsp;流&nbsp;程&nbsp;</td>
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
						<s:form name="myTableForm" id="myTableForm" action="/senddoc/sendDoc!processed.action" method ="post">
							<%
								List showColumnList = (List) request.getAttribute("showColumnList");
									if (showColumnList != null) {
										showColumnList = new ArrayList(showColumnList);
									}
									double size = showColumnList.size() - 1;//减掉第一列（主键列）
									String otherLength = "20%";
									String titleLength = "32%";
									int showSize = 21;
									String tablewidth = "100%";
									double otherwidth = 0;
									if (size <= 7) {
										otherwidth = (100 - 40) / size;
										otherLength = otherwidth + "%";
									} else {
										tablewidth = (((size - 7) * 20) + 100) + "%";
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
								<%
									Object[] pngColumn = null;
									for (int i = 0; i < showColumnList.size(); i++) {
										Object[] column = (Object[]) showColumnList.get(i);
										String columnName = (String) column[0];
										if (columnName.equals("png")) {
											pngColumn = column;
											showColumnList.remove(i);
											i--;
										}
									}
									Object[] recv_num = null;
									for (int i = 0; i < showColumnList.size(); i++) {
										Object[] column = (Object[]) showColumnList.get(i);
										String columnName = (String) column[0];
										if (columnName.toUpperCase().equals(
												"RECV_NUM".toUpperCase())) {
											recv_num = column;
											showColumnList.remove(i);
											i--;
										}
									}
									String pkFieldName = "";
									for (int i = 0; i < showColumnList.size(); i++) {
										if (i == showColumnList.size() - 1) {
											showSize = 200;
										}
										Object[] column = (Object[]) showColumnList.get(i);
										String columnName = (String) column[0];
										String showColumn = (String) column[3];
										String caption = (String) column[1];
										String type = (String) column[2];//字段类型
										if (i == 0) {
											pkFieldName = columnName;
											showColumn = showColumn.replace("\\r\\n", " ");
											showColumn = showColumn.replace("\\n", " ");
								%>
								<s:if test="%{state!=\"1\"}">
									<webflex:flexCheckBoxCol caption="选择" property="<%=columnName %>"showValue="<%=showColumn %>" 
										width="10%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								</s:if>
								<s:else>
									<webflex:flexCheckBoxCol caption="选择" property="<%=columnName %>"showValue="<%=showColumn %>" 
										width="3%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								</s:else>
								<%
											String state = (String) request.getParameter("state");
											if (pngColumn != null && !"1".equals(state)) {//已办事宜显示预警图标
												String pngcolumnName = (String) pngColumn[0];
												String pngshowColumn = (String) pngColumn[3];
												String pngcaption = (String) pngColumn[1];
												String pngtype = (String) pngColumn[2];//字段类型
								%>
								<webflex:flexFlagCol caption="<%=pngcaption%>" property="<%=columnName%>"showValue="<%=pngshowColumn%>" 
									isCanDrag="false" isCanSort="false"></webflex:flexFlagCol>
								<%
									}
												if (recv_num != null) {
													String recv_numcolumnName = (String) recv_num[0];
													String recv_numshowColumn = (String) recv_num[3];
													String recv_numcaption = (String) recv_num[1];
													String recv_numtype = (String) recv_num[2];//字段类型
								%>
								<webflex:flexTextCol caption="<%=recv_numcaption %>" property="<%=recv_numcolumnName %>"showValue="<%=recv_numshowColumn %>" 
									width="8%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
								<%
												}
											} else if (SendDocManager.DATE_TYPE.equals(type)
													|| "5".equals(type)) {//日期类型
								%>
								<webflex:flexDateCol caption="<%=caption %>" property="<%=columnName %>"showValue="<%=showColumn %>" 
									width="10%" isCanDrag="false" dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>
								<%
											} else {
													if (BaseWorkflowManager.WORKFLOW_TITLE.equals(columnName)) {//标题字段
								%>
								<webflex:flexTextCol caption="<%=caption %>" property="<%=pkFieldName %>"showValue="<%=showColumn %>" 
									width="<%=titleLength %>" isCanDrag="false" onclick="ViewFormAndWorkflow(this.value);" 
									isCanSort="false" showsize="17"></webflex:flexTextCol>
								<%
														} else {
																String state = (String) (request
																		.getAttribute("state") == null
																		? ""
																		: request.getAttribute("state"));
																int pageResultSize = request
																		.getAttribute("pageResultSize") == null
																		? 0
																		: Integer.parseInt(request
																				.getAttribute("pageResultSize")
																				.toString());
																showSize = 5;
																if (!state.equals("1")) {
																	if (pageResultSize == 0) {
																		if (i > 2) {
																			otherLength = "12%";
																		}
																	} else {
																		otherLength = "100px";
																	}
																} else {
																	if (i == 2) {
																		otherLength = "20%";
																	}
																	if (i > 2) {
																		otherLength = "16%";
																	}
																}
								%>
								<webflex:flexTextCol caption="<%=caption %>" property="<%=columnName %>"showValue="<%=showColumn %>" 
									width="10%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
								<%
															}
														}
													}
								%>
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
	        item = new MenuItem("<%=root%>/images/operationbtn/urge.png","关联流程","relativeWf",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		        <s:if test="state!=1">
			    	item = new MenuItem("<%=root%>/images/operationbtn/urge.png","催办","sendReminder",1,"ChangeWidthTable","checkOneDis");
			        sMenu.addItem(item);
		        </s:if>
		    	item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		        <s:if test="state!=1">
			    	item = new MenuItem("<%=root%>/images/operationbtn/Retrieve_the_process.png","取回流程","fetchTask",1,"ChangeWidthTable","checkMoreDis");
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
	      	if(document.getElementById("YJZX_TRUE")!= null){
	   			if(document.getElementById("YJZX_TRUE").checked){
	   				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&businessType=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");			
	   			}
	   		}else{   		
	      		var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");
	   		}
	   		
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
			var state = "${state}";
			//alert(state);
	      	var width=screen.availWidth-10;
	   		var height=screen.availHeight-30;
	   		if(document.getElementById("YJZX_TRUE")!= null){
	   			if(document.getElementById("YJZX_TRUE").checked){
	   				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&businessType=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");			
	   			}
	   		}else{   		
	      		var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&processSuspend=0&state="+state+"&searchType=1&taskId="+taskId,'processed',width, height, "");
	   		}
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

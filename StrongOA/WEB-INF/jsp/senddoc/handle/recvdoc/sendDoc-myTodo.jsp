<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="com.strongit.oa.common.service.BaseWorkflowManager" />
<jsp:directive.page import="com.strongit.oa.senddoc.manager.SendDocManager" />
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>我的请求</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>

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
		      	var workflowType = "";
			    if("${workflowType}" !=""){
			    	workflowType = encodeURI(encodeURI("${workflowType}"));
			    }else{
				    if($("#workflowType").val() != ""){
				    	workflowType = encodeURI(encodeURI($("#workflowType").val()));
				    }
			    }
		      	window.location = "<%=root%>/senddoc/sendDoc!myTodo.action?workflowName="+encodeURI(encodeURI($("#workflowName").val()))
		      					+"&formId="+$("#formId").val()+"&workflowType="+workflowType+"&handleKind="+$("#handleKind").val()+"&state="+$("#state").val();
			}
			//查看
			function openDoc() {
				var bussinessId = getValue();
				var state=$("#state").val();
				if(bussinessId == ""){
					alert("请选择要查阅的记录。");
					return ;
				}else{
					var bids = bussinessId.split(",");
					if(bids.length>1){
						alert("只可以查阅一条记录。");
						return ;
					}
				}
				var result = bussinessId.split("$");
				var instanceId = result[1];
				var width=screen.availWidth-10;
				var height=screen.availHeight-30;
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+state,'hostedby',width, height,"");
			}
			//处理状态（查看流程图）
			function workflowView(){   
			var bussinessId = getValue();
			if(bussinessId == ""){
				alert("请选择要查看的办理记录。");
				return ;
			}else{
		    	var bids = bussinessId.split(",");
		    	if(bids.length>1){
		    		alert("只可以查看一条办理记录。");
		    		return ;
		    	}
			}
			var result = bussinessId.split("$");
			var instanceId = result[1];
			var width=screen.availWidth-10;;
			var height=screen.availHeight-30;
			WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId=${taskId}",'Cur_workflowView',width, height, "办理记录");
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
				      			alert('流程已取回成功。');
				      			reloadPage();
				      		}else if(retCode == "-2"){
				      			alert("取回流程失败。");
				      		}	
				      }		
				);
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
		      		var TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		      			var taskId = TaskIdAndInstanceId[0];
		      		var isExsitTodo=TaskIdAndInstanceId[5];
		      	if(isExsitTodo == "0"){
		      		if(confirm("确定要取回吗？")){
			      		doFetchTask(taskId);
			      	}
			      }else{
			          alert("该文已在您的待办事宜中，不能取回。");
			      }		
		      	}
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
		//废除公文,挂起流程实例
		function repeal(){
			var bussinessId = getValue();
			var TaskIdAndInstanceId = bussinessId.split("$");
			var taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			if(bussinessId == ""){
	   		 	alert("请选择要取消的记录。");
	   		 	return ;
	   		 }else{
	   		 	var taskIds = bussinessId.split(",");
		      		if(taskIds.length>1){
		      			alert("只可以取消一条记录。");
		      			return ;
		      		}
		      	if(confirm("确定要取消吗？")){
		   		 	$.post("<%=root%>/senddoc/sendDoc!repealProcess.action",
						{instanceId:instanceId},
						function(data){
						    if(data=="true"){
						       //alert("取消文件成功。");
						       reloadPage();
						    }else{
								alert("取消文件失败。");
							}
						});  
				} 		   		 
			}   		   		 
		}
		 //强制取回
	      function mustFetchTask(){
	    	   var bussinessId = getValue();
	    	    if(bussinessId == ""){
						alert("请选择要强制取回的记录。");
						return ;
					}else{
				    	var bids = bussinessId.split(",");
				    	if(bids.length>1){
				    		alert("只可以强制取回一条记录。");
				    		return ;
				    	}
					}
				var TaskIdAndInstanceId = bussinessId.split("$");
				var taskId = TaskIdAndInstanceId[0];
				var instanceId = TaskIdAndInstanceId[1];
				var isExsitTodo=TaskIdAndInstanceId[5];
		if(isExsitTodo == "0"){
	      	if(confirm("您确定要强制取回吗？")){
		      	var remindCount=0;//添加定时提醒显示div的id增量
	    		var remindContent="";//添加定时提醒显示的div的内容
	    		var remindType=""; //提醒方式
		        var url = "<%=path%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-timerRemind.jsp?instanceId="+TaskIdAndInstanceId[1];
				var a = window.showModalDialog(url,window,'dialogWidth:550px;dialogHeight:600px;help:no;status:no;scroll:no');
				if((typeof a)!="undefined"){
					a[0]="id"+remindCount+";"+a[0];
					if(remindContent!=""&&remindContent!=null){
						remindContent+="##"
					}
					remindContent+=a[0];//提醒内容
					remindType+=a[1];//提醒方式
					$("#remindSet").val(remindContent);//提醒内容
					$("#remindType").val(remindType);//提醒方式
					var actionUri = basePath + "senddoc/sendDoc!mustFetchTask1.action";
					//document.getElementById("bussinessId").value = TaskIdAndInstanceId[4];// 退回的节点id,指定退回用户id
					$.post(actionUri,
							{bussinessId:TaskIdAndInstanceId[4],remindSet:remindContent,remindType:remindType,instanceId:instanceId},
							function(data){
							    if(data=="true"){
							       alert("文件强制取回成功。");
							       reloadPage();
							    }else{
									alert("文件强制取回失败。");
								}
							});  
					
				}
		      }
	      	}else{
	      			alert("该文已在您的待办事宜中，不能强制取回。");
	      		}
	      		
	      }
    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
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
										主办公文列表
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
						                 	<td class="Operation_list" onclick="openDoc();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
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
							                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="mustFetchTask();"><img src="<%=root%>/images/operationbtn/Return.png"/>&nbsp;强&nbsp;制&nbsp;取&nbsp;回&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						               
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="fetchTask();"><img src="<%=root%>/images/operationbtn/Retrieve_the_process.png"/>&nbsp;取&nbsp;回&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="repeal();"><img src="<%=root%>/images/operationbtn/Repeal.png"/>&nbsp;取&nbsp;消&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
							                </s:if>
											<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<!--个人办公主办显示列 【标题	发起时间	当前办理人	当前部门】	  -->
						<%
							List showColumnList = new ArrayList(
									(List) request.getAttribute("showColumnList"));
							String[] column1 = (String[]) showColumnList.get(0);
							String[] column2 = null;
							String[] column3 = null;
							String[] column4 = null;
							String[] column5 = null;
							for (int i = 0; i < showColumnList.size(); i++) {
								Object[] column = (Object[]) showColumnList.get(i);
								String columnName = (String) column[0];
								if (columnName.toUpperCase().equals(
										"WORKFLOWTITLE".toUpperCase())) {
									column2 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"PROCESSSTARTDATE".toUpperCase())) {
									column3 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"STARTUSERNAME".toUpperCase())) {
									column4 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"PROCESSENDDATE".toUpperCase())) {
									column5 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
							}
						%>
						<s:form name="myTableForm" action="/senddoc/sendDoc!myTodo.action">
							<webflex:flexTable name="myTable" width="100%" height="200px" wholeCss="table1"
								property="senddocId" isCanDrag="false" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<!-- 流程名称 -->
								<s:hidden id="workflowName" name="workflowName"></s:hidden>
								<!-- 表单id -->
								<s:hidden id="formId" name="formId"></s:hidden>
								<s:hidden id="state" name="state"></s:hidden>
								<!-- 电子表单V2.0 调用方法名参数 -->
			                    <s:hidden id="formAction" name="formAction"></s:hidden>
			                    <!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
			                    <s:hidden id="bussinessId" name="bussinessId"></s:hidden>
								<!-- 表名称 -->
								<s:hidden id="tableName" name="tableName"></s:hidden>
								<!-- 公文处理类别 0：个人办公 1：发文处理 2：收文处理-->
								<s:hidden id="handleKind" name="handleKind"></s:hidden>
								<!-- 流程类型 -->
								<s:hidden name="workflowType"></s:hidden>
								<strong:query queryColumnList="${queryColumnList}" />
								<%
									if (column1 != null) {
								%>
								<webflex:flexCheckBoxCol caption="选择" property="<%=column1[0]%>" showValue="<%=column1[3]%>"
									width="10%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<%
									}
								%>
								<%
									if (column2 != null) {
								%>
								<webflex:flexTextCol caption="<%=column2[1]%>" property="<%=column1[0]%>"
									showValue="<%=column2[3]%>" width="52%" isCanDrag="false" isCanSort="false"
									onclick="ViewFormAndWorkflow(this.value);" showsize="32"></webflex:flexTextCol>
								<%
									}
								%>
								<%
									if (column3 != null) {
								%>
								<webflex:flexDateCol caption="<%=column3[1]%>" property="<%=column3[0]%>"
									showValue="<%=column3[3]%>" width="15%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm:ss"
									isCanSort="true"></webflex:flexDateCol>
								<%
									}
								%>
								<%
									if (column4 != null) {
								%>
								<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column4[0]%>" align="center"
									showValue="<%=column4[3]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
								<%
									}
								%>
								<s:if test="state==1">
								<%
									if (column5 != null) {
								%>
								<webflex:flexDateCol  caption="<%=column5[1]%>" property="<%=column5[0]%>" isCanSort="true"
									showValue="<%=column5[3]%>" width="13%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
								<%
									}
								%>
								</s:if>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
    	  var state=$("#state").val();
        sMenu.registerToDoc(sMenu);
         var item = null;
	        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","openDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        
	        if(state!="1"){
	        	item = new MenuItem("<%=root%>/images/operationbtn/urge.png","催办","sendReminder",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
	        	item = new MenuItem("<%=root%>/images/operationbtn/Reply.png","反馈","feedBack",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/Return.png","强制取回","mustFetchTask",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/Retrieve_the_process.png","取回","fetchTask",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/Repeal.png","取消","repeal",1,"ChangeWidthTable","checkOneDis");
		        sMenu.addItem(item);
	        }
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
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
   	
 	/**
		@param InstanceIdAndStatus 流程实例id$流程状态
	*/
	function ViewFormAndWorkflow(InstanceIdAndStatus) {
		var result = InstanceIdAndStatus.split("$");
		var instanceId = result[1];
		var state=$("#state").val();
		//var fullContextPath = $("form").attr("action");
  		//var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  		//document.getElementById('blank').contentWindow.setWorkId("",instanceId,contextPath); 
  		
  		var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+ instanceId + "&state="+state , 'hostedby', width, height, "");

	}
</script>
	</body>
</html>

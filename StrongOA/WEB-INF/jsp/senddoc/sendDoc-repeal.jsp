<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page
	import="com.strongit.oa.common.service.BaseWorkflowManager" />
<jsp:directive.page import="com.strongit.oa.senddoc.manager.SendDocManager" />
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>公文回收站</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
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
      	window.location = "<%=root%>/senddoc/sendDoc!repeal.action?state="
      				+$("#state").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
      				+"&formId="+$("#formId").val()+"&privilName="+privilName+"&workflowType="+workflowType
      				+"&showSignUserInfo="+$("#showSignUserInfo").val();
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
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId, 
                                   width, height, window);
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
    </script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td >
									&nbsp;
								</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									<s:if test="workflowName == null || workflowName.length() == 0">
										<%=privilName %>${privilName}
									</s:if>
									<s:else>
										${workflowName }
									</s:else>
								</td>
								<td width="75%">
									<table border="0" align="right" cellpadding="00"cellspacing="0">
										<tr>
											<td>
												<a class="Operation" href="#" onclick="doHide();"><img
														src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"> <label id="label_search">
														显示查询条件
													</label> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="javascript:gotoView();"><img
														src="<%=root%>/images/ico/page.gif" width="15"
														height="15" class="img_s">查阅&nbsp;</a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="javascript:workflowView();"><img
														src="<%=root%>/images/ico/chakanlishi.gif" width="15"
														height="15" class="img_s">办理记录&nbsp;</a>
											</td>
											<td width="5"></td>
											
												<td>
													<a class="Operation" href="javascript:returnProcess();"><img
															src="<%=root%>/images/ico/cexiao.gif" width="15"
															height="15" class="img_s">还原&nbsp;</a>
												</td>
												
												<td width="5"></td>
											
												<td>
													<a class="Operation" href="javascript:delProcess();"><img
															src="<%=root%>/images/ico/shanchu.gif" width="15"
															height="15" class="img_s">删除&nbsp;</a>
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
						<s:form name="myTableForm" action="/senddoc/sendDoc!repeal.action">
							<webflex:flexTable name="myTable" width="" height="200px"
								wholeCss="table1" property="senddocId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
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
								<s:hidden name="showSignUserInfo" id="showSignUserInfo"></s:hidden>
								<strong:query queryColumnList="${queryColumnList}" />
								<input type="hidden" id="sortTypeHidden" value="">
								<%
									List showColumnList = (List) request
													.getAttribute("showColumnList");
											double size = showColumnList.size() - 1;//减掉第一列（主键列）
											String width = "20%";

											String otherLength = (String) request
													.getAttribute("otherLength");
											String titleLength = (String) request
													.getAttribute("titleLength");
											int showSize = Integer.parseInt(titleLength) / 2;
											if (showSize < 15) {
												showSize = 15;
											}
											switch (showColumnList.size()) {
												case 1 :
													titleLength = "100%";
													break;
												case 2 :
													titleLength = "90%";
													break;
												case 3 :
													titleLength = "80%";
													break;
												case 4 :
													titleLength = "70%";
													break;
												case 5 :
													titleLength = "70%";
													break;
												case 6 :
													titleLength = "60%";
													break;
												case 7 :
													titleLength = "50%";
													break;
												case 8 :
													titleLength = "40%";
													break;
												case 9 :
													titleLength = "40%";
													break;
											}
											otherLength = "200px";
											// titleLength=titleLength+"%";
											// otherLength=otherLength+"%";

											NumberFormat nf = NumberFormat.getPercentInstance();
											if (size > 0) {
												size = 100 / size;
												size = size / 100;
												width = nf.format(size);
											}

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
								<webflex:flexCheckBoxCol caption="选择"
									property="<%=columnName %>" showValue="<%=showColumn %>"
									width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<%
									String state = (String) request
															.getParameter("state");
													if (pngColumn != null && state.equals("2")) {//已办事宜显示预警图标
														String pngcolumnName = (String) pngColumn[0];
														String pngshowColumn = (String) pngColumn[3];
														String pngcaption = (String) pngColumn[1];
														String pngtype = (String) pngColumn[2];//字段类型
								%>
								<webflex:flexTextCol caption="<%=pngcaption %>"
									property="<%=pngcolumnName %>" showValue="<%=pngshowColumn %>"
									width="" isCanDrag="false" isCanSort="false"></webflex:flexTextCol>
								<%
									}
												} else if (SendDocManager.DATE_TYPE.equals(type)
														|| "5".equals(type)) {//日期类型
								%>
								<webflex:flexDateCol caption="<%=caption %>"
									property="<%=columnName %>" showValue="<%=showColumn %>"
									width="<%=otherLength %>" isCanDrag="true"
									dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>
								<%
									} else {
													if (BaseWorkflowManager.WORKFLOW_TITLE
															.equals(columnName)) {//标题字段
								%>
								<webflex:flexTextCol caption="<%=caption %>"
									property="<%=pkFieldName %>" showValue="<%=showColumn %>"
									width="<%=titleLength %>" isCanDrag="false"
									onclick="ViewFormAndWorkflow(this.value);" isCanSort="false"
									showsize="<%=showSize %>"></webflex:flexTextCol>
								<%
									} else {
								%>
								<webflex:flexTextCol caption="<%=caption %>"
									property="<%=columnName %>" showValue="<%=showColumn %>"
									width="<%=otherLength %>" isCanDrag="false" isCanSort="false"
									showsize="<%=showSize %>"></webflex:flexTextCol>
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
        item = new MenuItem("<%=root%>/images/ico/page.gif","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
    	item = new MenuItem("<%=root%>/images/ico/chakan.gif","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
        sMenu.addItem(item);

        item = new MenuItem("<%=root%>/images/ico/fankuihuizhi.gif","还原","returnProcess",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        
       	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delProcess",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);

        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }

	/**
		@param TaskIdAndInstanceId 任务id$流程实例id
	*/
	function ViewFormAndWorkflow(TaskIdAndInstanceId) {
		TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
      	var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewrepeal.action?instanceId="+instanceId+"&processSuspend=1&searchType=1&taskId="+taskId,'processed',width, height, "");
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
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
      	 var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewrepeal.action?instanceId="+instanceId+"&processSuspend=1&searchType=1&taskId="+taskId,'processed',width, height, "");
     	  if(ret){
     		if(ret == "OK"){
     			reloadPage();
     		}
     	  }
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
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId, 
                                   width, height, window);
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
      
      
      //还原公文,重新启动流程实例
      function returnProcess(){
        var TaskIdAndInstanceId = getValue();
      	//alert(TaskIdAndInstanceId);
      	if(TaskIdAndInstanceId == ""){
        	alert("请选择要还原的记录。");
         	return ;
         }
         var TaskIdAndInstanceIds = TaskIdAndInstanceId.split(",");
         var instanceId = "";
         for ( var i = 0; i < TaskIdAndInstanceIds.length; i++) {
			instanceId += TaskIdAndInstanceIds[i].split("$")[1] + ",";
			
		}
		instanceId = instanceId.substring(0,instanceId.length-1);
		//alert(instanceId);
		if(confirm("确定要还原吗？")){
		  		 $.post("<%=root%>/senddoc/sendDoc!returnProcess.action",
				{instanceId:instanceId},
				function(data){
				    if(data=="true"){
				       //alert("还原成功。");
		  		 	   document.forms(0).submit();
				    }else{
						alert("还原失败。");
						return;
					}
				});
		   }else{
		   		return;
		   } 
             		 	   		 	   		   		 	   	   		 	   	 		   		 
      } 
      
      function delProcess(){
      	var TaskIdAndInstanceId = getValue();
      	//alert(TaskIdAndInstanceId);
      	if(TaskIdAndInstanceId == ""){
        	alert("请选择要删除的记录。");
         	return ;
         }
         var TaskIdAndInstanceIds = TaskIdAndInstanceId.split(",");
         var instanceId = "";
         for ( var i = 0; i < TaskIdAndInstanceIds.length; i++) {
			instanceId += TaskIdAndInstanceIds[i].split("$")[1] + ",";
			
		}
		instanceId = instanceId.substring(0,instanceId.length-1);
		//alert(instanceId);
		if(confirm("你确定彻底删除流程吗?")){
		  		 $.post("<%=root%>/senddoc/sendDoc!delProcess.action",
				{instanceId:instanceId},
				function(data){
				    if(data=="true"){
				       //alert("删除成功。");
					   document.forms(0).submit();
				    }else{
						alert("删除失败。");
						return;
					}
				});
		   }else{
		   		return;
		   }  		 	   	
		  
      }
      
    </script>
	</body>
</html>

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
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
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
      				+"&handleKind="+$("#handleKind").val()+"&showSignUserInfo="+$("#showSignUserInfo").val();
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
			      			alert("该任务不存在或已删除！");
			      		}else if(retCode == "0"){
			      			alert('该流程实例已经结束，无法取回。');	
			      		}else if(retCode == "1"){
			      			alert('该任务的后继任务已被签收处理，不允许取回。');
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
    </script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
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
						                 	<td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="returnProcess();"><img src="<%=root%>/images/operationbtn/Reduction.png"/>&nbsp;还&nbsp;原&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="delProcess();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
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
						<s:form name="myTableForm" action="/senddoc/sendDoc!repeal.action">
						<!--发文处理显示列 【文件标题	  发文类别	发文单位	当前办理人	当前部门】	  -->
							<%
										List showColumnList = new ArrayList((List) request
										.getAttribute("showColumnList"));
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
							<webflex:flexTable name="myTable" width="100%" height="200px"
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
								<!-- 公文处理类别 0：个人办公 1：发文处理 2：收文处理-->
								<s:hidden id="handleKind" name="handleKind"></s:hidden>
								<!-- 流程类型 -->
								<s:hidden name="workflowType" id="workflowType"></s:hidden>
								<s:hidden name="showSignUserInfo" id="showSignUserInfo"></s:hidden>
								<strong:query queryColumnList="${queryColumnList}" />
								<input type="hidden" id="sortTypeHidden" value="">
								<!-- 资源名称 -->
								<input type="hidden" name="privilName" id="privilName"
									value="<%=privilName%>" />
								<s:if test="state!=1">
									<%
									if (column1 != null) {
									%>
									<webflex:flexCheckBoxCol caption="选择"
										property="<%=column1[0]%>" showValue="<%=column1[3]%>"
										width="5%" isCheckAll="true" isCanDrag="false"
										isCanSort="false"></webflex:flexCheckBoxCol>
									<%
									}
									%>
									<%
									if (column2 != null) {
									%>
									<webflex:flexFlagCol caption=""
									property="<%=column1[0]%>" showValue="<%=column2[3]%>"
									isCanDrag="false" isCanSort="false"></webflex:flexFlagCol>
									<%
									}
									%>
									<%
									if (column3 != null) {
									%>
									<webflex:flexTextCol caption="<%=column3[1]%>"
										property="<%=column1[0]%>" showValue="<%=column3[0]%>"
										width="35%" isCanDrag="false"
										onclick="ViewFormAndWorkflow(this.value);" isCanSort="false" showsize="25"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column4 != null) {
									%>
									<webflex:flexTextCol caption="<%=column4[1]%>"
										property="<%=column4[3]%>" showValue="<%=column4[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column5 != null) {
									%>
									<webflex:flexTextCol caption="<%=column5[1]%>"
										property="<%=column5[3]%>" showValue="<%=column5[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column6 != null) {
									%>
									<webflex:flexTextCol caption="<%=column6[1]%>"
										property="<%=column6[3]%>" showValue="<%=column6[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column7 != null) {
									%>
									<webflex:flexTextCol caption="<%=column7[1]%>"
										property="<%=column7[3]%>" showValue="<%=column7[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
									}
									%>
								</s:if>
								<s:else>
									<%
									if (column1 != null) {
									%>
									<webflex:flexCheckBoxCol caption="选择"
										property="<%=column1[0]%>" showValue="<%=column1[3]%>"
										width="5%" isCheckAll="true" isCanDrag="false"
										isCanSort="false"></webflex:flexCheckBoxCol>
									<%
									}
									%>
									<%
									if (column3 != null) {
									%>
									<webflex:flexTextCol caption="<%=column3[1]%>"
										property="<%=column1[0]%>" showValue="<%=column3[0]%>"
										width="47%" isCanDrag="false"
										onclick="ViewFormAndWorkflow(this.value);" isCanSort="false" showsize="27"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column4 != null) {
									%>
									<webflex:flexTextCol caption="<%=column4[1]%>"
										property="<%=column4[3]%>" showValue="<%=column4[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column5 != null) {
									%>
									<webflex:flexTextCol caption="<%=column5[1]%>"
										property="<%=column5[3]%>" showValue="<%=column5[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
									<%
									}
									%>
									<%
									if (column8 != null) {
									%>
									<webflex:flexDateCol caption="<%=column8[1]%>"
										property="<%=column8[3]%>" showValue="<%=column8[0]%>"
										width="15%" isCanDrag="true" dateFormat="yyyy-MM-dd"
										isCanSort="true"></webflex:flexDateCol>
									<%
									}
									%>
									<%
									if (column9 != null) {
									%>
									<webflex:flexTextCol caption="<%=column9[1]%>"
										property="<%=column9[3]%>" showValue="<%=column9[0]%>"
										width="15%" isCanDrag="false" isCanSort="false" showsize="4"></webflex:flexTextCol>
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
        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
    	item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
        sMenu.addItem(item);

        item = new MenuItem("<%=root%>/images/operationbtn/Reduction.png","还原","returnProcess",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        
       	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delProcess",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);

        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
	       var privilName = "<%=privilName%>";
	       if(privilName ==""){
	       		privilName = "${privilName}";
	       }
	       $("#privilName").val(privilName);
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
		if(confirm("确定要删除吗？")){
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

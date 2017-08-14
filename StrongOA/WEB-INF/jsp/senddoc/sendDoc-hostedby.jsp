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
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
			.tabletitle {
				FILTER: progid : DXImageTransform.Microsoft.Gradient ( 
			                             gradientType =   0, startColorStr =  
					#ededed, endColorStr =   #ffffff );
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
	      	window.location = "<%=root%>/senddoc/sendDoc!hostedby.action?workflowName="+encodeURI(encodeURI($("#workflowName").val()))
	      					+"&formId="+$("#formId").val()+"&workflowType="+workflowType;
	      }
    	  //查看
	      function openDoc() {
              var bussinessId = getValue();
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
			  var instanceId = result[0];
              var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+
   					  		instanceId+"&state="+result[1],'hostedby',width, height,"");
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
			  var instanceId = result[0];
	          var width=screen.availWidth-10;;
	          var height=screen.availHeight-30;
	          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+
	        		  		instanceId+"&taskId=${taskId}",'Cur_workflowView',width, height, "办理记录");
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
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td width="30%">
									&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">
									<s:if test="workflowName == null || workflowName.length() == 0">
										主办公文列表
									</s:if>
									<s:else>
										${workflowName }
									</s:else>
								</td>
								<td>
									<table align="right">
										<tr>
											<td>
												<a class="Operation" href="javascript:openDoc();">
													<img src="<%=root%>/images/ico/page.gif" width="15" height="15" class="img_s">
													查阅&nbsp;</a>
											</td>
											<td width="5">
											</td>
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
														显示查询条件
													</label> 
												</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:sendReminder();">
													<img src="<%=root%>/images/ico/emergency.gif" width="15" height="15" class="img_s">
													催办&nbsp;</a>
											</td>
											<td width="5">
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
						<s:form name="myTableForm" action="/senddoc/sendDoc!hostedby.action">
							<webflex:flexTable name="myTable" width="100%" height="200px" wholeCss="table1"
								property="senddocId" isCanDrag="false" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<!-- 流程名称 -->
								<s:hidden id="workflowName" name="workflowName"></s:hidden>
								<!-- 表单id -->
								<s:hidden id="formId" name="formId"></s:hidden>
								<!-- 表名称 -->
								<s:hidden id="tableName" name="tableName"></s:hidden>
								<!-- 流程类型 -->
								<s:hidden name="workflowType"></s:hidden>
								<strong:query queryColumnList="${queryColumnList}" />
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
											showSize = 16;
											titleLength = titleLength + "%";
											otherLength = otherLength + "%";

											NumberFormat nf = NumberFormat.getPercentInstance();
											if (size > 0) {
												size = 100 / size;
												size = size / 100;
												width = nf.format(size);
											}
											String pkFieldName = "";

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

											for (int i = 0; i < showColumnList.size(); i++) {
												if (i == showColumnList.size() - 1) {
													otherLength = "100%";
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
								<webflex:flexCheckBoxCol caption="选择" property="<%=columnName %>"
									showValue="<%=showColumn %>" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<%
									if (recv_num != null) {
														String recv_numcolumnName = (String) recv_num[0];
														String recv_numshowColumn = (String) recv_num[3];
														String recv_numcaption = (String) recv_num[1];
														String recv_numtype = (String) recv_num[2];//字段类型
								%>
								<webflex:flexTextCol caption="<%=recv_numcaption %>" property="<%=recv_numcolumnName %>"
									showValue="<%=recv_numshowColumn %>" width="<%=otherLength %>" isCanDrag="false" align="center"
									isCanSort="false" showsize="7"></webflex:flexTextCol>
								<%
									}

												} else if (SendDocManager.DATE_TYPE.equals(type)
														|| "5".equals(type)) {//日期类型
								%>
								<webflex:flexDateCol caption="<%=caption %>" property="<%=columnName %>"
									showValue="<%=showColumn %>" width="<%=otherLength %>" isCanDrag="true"
									dateFormat="yyyy-MM-dd" isCanSort="true"></webflex:flexDateCol>
								<%
									} else {
													if (BaseWorkflowManager.WORKFLOW_TITLE
															.equals(columnName)) {//标题字段
								%>
								<webflex:flexTextCol caption="<%=caption %>" property="<%=pkFieldName %>" 
									showValue="<%=showColumn %>" width="<%=titleLength %>" isCanDrag="false" 
									onclick="ViewFormAndWorkflow(this.value);" isCanSort="false" showsize="<%=showSize %>"></webflex:flexTextCol>
								<%
									} else {
														showSize = 5;
								%>
								<webflex:flexTextCol caption="<%=caption %>" property="<%=columnName %>" align="center"
									showValue="<%=showColumn %>" width="<%=otherLength %>" isCanDrag="false" isCanSort="false"
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
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
	        item = new MenuItem("<%=root%>/images/ico/page.gif","查阅","openDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/chakanlishi.gif","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/emergency.gif","催办","sendReminder",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
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
			var instanceId = result[0];
		 	var overDate = result[1];
		 	if(overDate == "1"){
		 		alert("流程已结束，无需催办。");
		 		return;
		 	}
		 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 215, window);
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
   			  var ret=WindowOpen("<%=root%>
	/senddoc/sendDoc!viewHostedBy.action?instanceId="
				+ instanceId + "&state=" + result[1], 'hostedby', width, height, "");

	}
</script>
	</body>
</html>

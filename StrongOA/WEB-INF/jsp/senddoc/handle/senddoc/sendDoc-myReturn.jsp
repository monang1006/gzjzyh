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
		      	window.location = "<%=root%>/senddoc/sendDoc!myReturn.action?workflowName="+encodeURI(encodeURI($("#workflowName").val()))
		      					+"&formId="+$("#formId").val()+"&workflowType="+workflowType+"&handleKind="+$("#handleKind").val();
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
				var instanceId = result[1];
				var width=screen.availWidth-10;
				var height=screen.availHeight-30;
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state=0&viewReturn=1",'hostedby',width, height,"");
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
						                 	
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<!--个人办公待办显示列 【标题	发起时间	上步办理人	所在部门】	  -->
						<%
							List showColumnList = new ArrayList((List) request.getAttribute("showColumnList"));
							String[] column1 = (String[]) showColumnList.get(0);
							String[] column2 = null;
							String[] column3 = null;
							String[] column4 = null;
							String[] column5 = null;
							String[] column6 = null;
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
										"CURRENTUSERNAME".toUpperCase())) {
									column4 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"CURRENTUSERDEPT".toUpperCase())) {
									column5 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals("PNG".toUpperCase())) {
									column6 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
							}
						%>
						<s:form name="myTableForm" id="myTableForm" action="/senddoc/sendDoc!myReturn.action">
							<webflex:flexTable name="myTable" width="100%" height="200px" wholeCss="table1"
								property="senddocId" isCanDrag="false" ondblclick="onDblClickProcess(this.outerHTML);"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}" page="${page}">
								<input name="button" id="button" type="submit" style="display: none">
								<!-- 流程名称 -->
								<s:hidden id="workflowName" name="workflowName"></s:hidden>
								<!-- 表单id -->
								<s:hidden id="formId" name="formId"></s:hidden>
								<!-- 表名称 -->
								<s:hidden id="tableName" name="tableName"></s:hidden>
								<!-- 公文处理类别 0：个人办公 1：发文处理 2：收文处理-->
								<s:hidden id="handleKind" name="handleKind"></s:hidden>
								<!-- 签收状态 -->
								<s:hidden name="type" id="type"></s:hidden>
								<!-- 是否签收 -->
								<input type="hidden" name="isReceived" id="isReceived" value="${isReceived }" />
								<!-- 资源名称 -->
								<input type="hidden" name="privilName" id="privilName" value="" />
								<!-- 流程类别 -->
								<input type="hidden" name="workflowType" id="workflowType" value="${workflowType }" />
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
									if (column6 != null) {
								%>
								<webflex:flexFlagCol caption="" property="<%=column1[0]%>" showValue="<%=column6[3]%>"
									isCanDrag="false" isCanSort="false"></webflex:flexFlagCol>
								<%
									}
								%>
								<%
									if (column2 != null) {
								%>
								<webflex:flexTextCol caption="<%=column2[1]%>" property="<%=column1[0]%>"
									showValue="<%=column2[0]%>" width="55%" isCanDrag="false" isCanSort="false"
									onclick="ViewFormAndWorkflow(this.value);" showsize="30"></webflex:flexTextCol>
								<%
									}
								%>
								<%
									if (column3 != null) {
								%>
								<webflex:flexDateCol caption="<%=column3[1]%>" property="<%=column3[3]%>"
									showValue="<%=column3[0]%>" width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd"
									isCanSort="true"></webflex:flexDateCol>
								<%
									}
								%>
								<%
									if (column4 != null) {
								%>
								<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column4[3]%>" align="center"
									showValue="<%=column4[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
								<%
									}
								%>
								<%
									if (column5 != null) {
								%>
								<webflex:flexTextCol caption="<%=column5[1]%>" property="<%=column5[3]%>" align="center"
									showValue="<%=column5[0]%>" width="15%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
								<%
									}
								%>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			<div id="noshow" style="display: none">
			</div>
		</div>
		<script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
	        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","openDoc",1,"ChangeWidthTable","checkOneDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录","workflowView",1,"ChangeWidthTable","checkOneDis");
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
		var instanceId = result[1];
		//var fullContextPath = $("form").attr("action");
  		//var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  		//document.getElementById('blank').contentWindow.setWorkId("",instanceId,contextPath); 
  		
  		var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+ instanceId + "&state=0&viewReturn=1" , 'hostedby', width, height, "");

	}
</script>
	</body>
</html>

<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></script>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程草稿</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
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
      		//新建流程
		function newDoc(){
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&workflowType="+$("#workflowType").val();
			var ret = OpenWin("<%=root%>/docbacktracking/docBackTracking!input.action"+param,
			width, height, window);
		}
		
		function sendDoc(){
			var bussinessId = getValue();
			$.post("<%=root%>/docbacktracking/docBackTracking!startProcess.action",
			      　{workflowName:encodeURI(encodeURI($("#workflowName").val())),tableName:$("#tableName").val(),pkFieldValue:bussinessId},
			      　function(retCode){
			      alert(retCode);
			      }		
			);
		}
		function deleteDoc(){
			var bussinessId = getValue();
			if(bussinessId == ""){
				alert("请选择要删除的公文！");
       		 	return ;
			}else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("一次只能删除一份公文！");
       		 		return ;
       		 	}
			}
			if(confirm("确定要删除该公文？")){
				$.post("<%=root%>/docbacktracking/docBackTracking!deleteDoc.action",
				      　{workflowName:encodeURI(encodeURI($("#workflowName").val())),tableName:$("#tableName").val(),pkFieldValue:bussinessId},
				      　function(retCode){
				      	if(retCode == "1"){
				      		reloadPage();
				      	}
				      }		
				);
			}
		}
		//编辑草稿
		function editDoc() {         
			var bussinessId = getValue();
			if(bussinessId == ""){
				alert("请选择要修改的公文！");
       		 	return ;
			}else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("一次只能修改一份公文稿！");
       		 		return ;
       		 	}
			}
			$.post("<%=root%>/docbacktracking/docBackTracking!isEnd.action",
			      　{workflowName:encodeURI(encodeURI($("#workflowName").val())),tableName:$("#tableName").val(),pkFieldValue:bussinessId},
			      　function(retCode){
			      		if(retCode === "1"){//流程已解决
			      			alert("该公文已经办结，不能修改");
			      		}else{
							var width=screen.availWidth-10;
							var height=screen.availHeight-30;
							var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
							param = param+"&pkFieldValue="+bussinessId+"&tableName="+$("#tableName").val()+"&workflowType="+$("#workflowType").val();
							var ret = OpenWin("<%=root%>/docbacktracking/docBackTracking!input.action"+param,width, height, window);
			      		}
			      }		
			);
		}
		
		function reloadPage() {
			$("#myTableForm").submit();
	     }
	  	/*审批意见录入界面*/
	  	function addApprovalInput(bussinessId){
	  		var workflowName = encodeURI(encodeURI($("#workflowName").val()));
	  		var tableName = $("#tableName").val();
	  		var pkFieldValue = bussinessId;
	  		var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&pkFieldValue="+bussinessId+"&tableName="+$("#tableName").val();
			var arg = window.showModalDialog("<%=root%>/docbacktracking/docBackTracking!approvalinput.action"+param, 'a', "dialogWidth ="+width+"px;dialogHeight = "+height+"px;help=0",window);
			//var ret = WindowOpen("<%=root%>/docbacktracking/docBackTracking!approvalinput.action"+param,"审批意见录",width, height);
	  		if(arg && arg == "OK"){
	  			reloadPage();
	  		}
	  	}
	  	
	  	/*回调函数*/
	  	function clearDataSucess(){
	  		alert("clearDataSucess");
	  	}
	  	/*回调函数*/
	  	function clearDataError(){
	  		alert("clearDataError");
	  	}
	  	
	  	/*审批意见添加*/
	  	function addApproval(){
	  		var bussinessId = getValue();
	  		if(bussinessId == ""){
				alert("请先选择一个公文！");
       		 	return ;
			}else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("一次只能选择一个公文！");
       		 		return ;
       		 	}
			}
			$.post("<%=root%>/docbacktracking/docBackTracking!isEnd.action",
			      　{workflowName:encodeURI(encodeURI($("#workflowName").val())),tableName:$("#tableName").val(),pkFieldValue:bussinessId},
			      　function(retCode){
			      		if(retCode === "1"){//流程已解决
			      			alert("该公文已经办结，不能添加意见");
			      		}else{
			      			addApprovalInput(bussinessId);
			      		}
			      }		
			);
		}
		
		
	  	
	</script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: middle;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td width="30%">
									&nbsp;
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">
									<s:if test="workflowName == null || workflowName.length() == 0">
										<%=privilName%>${privilName}
									</s:if>
									<s:else>
										${workflowName }
									</s:else>
								</td>
								<td width=70%>
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
											<td>
											<a class="Operation" href="javascript:newDoc();"> <img
													src="<%=root%>/images/ico/tianjia.gif" width="15"
													height="15" class="img_s"> 公文添加&nbsp;</a>
											</td>
											<td width="5"></td>
											<td>
											<a class="Operation" href="javascript:editDoc();"> <img
													src="<%=root%>/images/ico/bianji.gif" width="15"
													height="15" class="img_s"> 公文编辑&nbsp;</a>
											</td>
											<td width="5"></td>
											<td>
											<a class="Operation" href="javascript:deleteDoc();"> <img
													src="<%=root%>/images/ico/shanchu.gif" width="15"
													height="15" class="img_s"> 公文删除&nbsp;</a>
											</td>
											<td width="5"></td>
											<td>
											<a class="Operation" href="javascript:addApproval();"> <img
													src="<%=root%>/images/ico/write.gif" width="15"
													height="15" class="img_s"> 意见添加&nbsp;</a>
											</td>
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
						<s:form id="myTableForm"
							action="/docbacktracking/docBackTracking!gridview.action">
							<!-- 流程名称 -->
							<s:hidden id="workflowName" name="workflowName"></s:hidden>
							<!-- 表单id -->
							<s:hidden id="formId" name="formId"></s:hidden>
							<s:hidden id="workflowType" name="workflowType"></s:hidden>
							<!-- 表名称 -->
							<s:hidden id="tableName" name="tableName"></s:hidden>
							<%
								List showColumnList = (List) request.getAttribute("showColumnList");
								double size = showColumnList.size() - 1;//减掉第一列（主键列）
								String otherLength = "12%";
								String titleLength = "40%";
								int showSize = 16;
								String tablewidth = "100%";
								double otherwidth = 0;
								Object[] recv_num = null;
								for (int i = 0; i < showColumnList.size(); i++) {
									Object[] column = (Object[]) showColumnList.get(i);
									String columnName = (String) column[0];
									if (columnName.toUpperCase().equals("RECV_NUM".toUpperCase())) {
										recv_num = column;
										showColumnList.remove(i);
										i--;
									}
								}
							%>
							<webflex:flexTable name="myTable" width="<%=tablewidth%>"
								height="200px" wholeCss="table1" property="senddocId"
								isCanDrag="false" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty"
								collection="${page.result}" page="${page}">
								<strong:query queryColumnList="${queryColumnList}" />
								<%
										for (int i = 0; i < showColumnList.size(); i++) {
										if (i == 1) {
											otherLength = "30%";
										}
										if (i == 2) {
											otherLength = "20%";
										}
										if (i > 2) {
											otherLength = "16%";
										}
										Object[] column = (Object[]) showColumnList.get(i);
										String columnName = (String) column[0];
										String showColumn = (String) column[3];
										String caption = (String) column[1];
										String type = (String) column[2];//字段类型  
										System.out.println(showColumn + "--" + type);
										if (i == 0) {
											showColumn = showColumn.replace("\\r\\n", " ");
											showColumn = showColumn.replace("\\n", " ");
								%>
								<webflex:flexCheckBoxCol caption="选择"
									property="<%=columnName%>" showValue="<%=showColumn%>"
									width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<%
										if (recv_num != null) {
										String recv_numcolumnName = (String) recv_num[0];
										String recv_numshowColumn = (String) recv_num[3];
										String recv_numcaption = (String) recv_num[1];
										String recv_numtype = (String) recv_num[2];//字段类型
								%>
								<webflex:flexTextCol caption="<%=recv_numcaption%>"
									property="<%=recv_numcolumnName%>"
									showValue="<%=recv_numshowColumn%>" width="7%"
									isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
								<%
										}
										} else if ("5".equals(type)) {//日期类型
											System.out.print(type);
								%>
								<webflex:flexDateCol caption="<%=caption%>"
									property="<%=columnName%>" showValue="<%=showColumn%>"
									width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd"
									isCanSort="true"></webflex:flexDateCol>
								<%
											} else {
											if (i != 1) {
										showSize = 6;
											}
								%>
								<webflex:flexTextCol caption="<%=caption%>"
									property="<%=columnName%>" showValue="<%=showColumn%>"
									width="<%=otherLength%>" isCanDrag="false" isCanSort="false"
									showsize="<%=showSize%>"></webflex:flexTextCol>
								<%
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
		        /**/
		         var item = null;
	          	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","公文添加","newDoc",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
	          	item = new MenuItem("<%=root%>/images/ico/bianji.gif","公文编辑","editDoc",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
	          	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","公文删除","deleteDoc",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
	          	item = new MenuItem("<%=root%>/images/ico/write.gif","意见添加","addApproval",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		        sMenu.addShowType("ChangeWidthTable");
		        registerMenu(sMenu);
			}
		</script>
	</body>
</html>

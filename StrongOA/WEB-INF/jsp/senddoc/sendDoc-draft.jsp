<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-query" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程草稿</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
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
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			var ret = OpenWin("<%=root%>/senddoc/sendDoc!input.action"+param,
			width, height, window);
		}
      
		//编辑草稿
		function editDoc() {         
			var bussinessId = getValue();
			if(bussinessId == ""){
				alert("请选择要修改的草稿。");
       		 	return ;
			}else{
       		 	var docIds = bussinessId.split(",");
       		 	if(docIds.length>1){
       		 		alert("一次只能修改一份草稿。");
       		 		return ;
       		 	}
			}
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&pkFieldValue="+bussinessId+"&tableName="+$("#tableName").val();
			var ret = WindowOpen("<%=root%>/senddoc/sendDoc!input.action"+param,'draft',width, height);
		}
      
		//删除草稿
		function deleteDoc() {
			var bussinessId = getValue();
			if (bussinessId != "") {
				if(confirm("删除选定的草稿，确定？")){
          	  		$.post("<%=root%>/senddoc/sendDoc!delete.action",
          	  			{pkFieldValue:bussinessId,tableName:$("#tableName").val()},function(ret){
          	  				if(ret == "0"){
          	  					reloadPage() ;
          	  					//window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;    
          	  				}else if(ret == "-1"){
          	  					alert("对不起，操作出错，请与管理员联系。");
          	  					return ;
          	  				}
          	  			});
				}
			} else {
				alert("请选择要删除的草稿。");
			}
		}
      
		//流程送审
		function sendDoc() {
			var height=473;//screen.availHeight-50;
			var width=480;//screen.availWidth/2;
			var bussinessId = getValue();
			if(bussinessId == ""){
	   		 	alert("请选择要送审的流程。");
	   		 	return ;
			}else{
	   		 	var docIds = bussinessId.split(",");
	   		 	if(docIds.length>1){
	   		 		alert("一次只能送审一个流程。");
	   		 		return ;
	   		 	}
			}
			var ret = OpenWindow("<%=root%>/senddoc/sendDoc!nextstep.action?tableName="+$("#tableName").val()
	         			+"&pkFieldValue="+bussinessId+"&formId="+$("#formId").val()
	         			+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())), 
	                               width, height,window);
	        if(ret){
	        	if(ret == "OK"){
	        		//alert("发送成功。");
	        		reloadPage() ;
	        		//window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;
	        	}else if(ret == "NO"){
	        		alert("发送失败。");
	        	}
	        }
		}
      
		function reloadPage() {
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&tableName="+$("#tableName").val()+"&showSignUserInfo="+$("#showSignUserInfo").val();
			if("${page.pageSize}"!=""){
	         	param += "&page.pageSize=${page.pageSize}";
		    }
		    var privilName = "";
		    if("${privilName}" !=""){
		    	privilName = encodeURI(encodeURI("${privilName}"));
		    	param += "&privilName="+privilName;
		    }else{
			    if($("#privilName").val() != ""){
			    	privilName = encodeURI(encodeURI($("#privilName").val()));
			    	param += "&privilName="+privilName;
			    }
		    }
	      	window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;
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
		//草拟
		function doCreate(){
	  		getSysConsole().refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=2','公文草拟');
		}
		//登记
		function doDengJi(){
			getSysConsole().refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=370020,413460','登记分发');
		}
	 function recive(){
	  	var width=screen.availWidth*2/3+30;
        var height=screen.availHeight*2/3+30;
      	//var ret =  window.open ('<%=root%>/Receive/receive!query.action', 'receivequery', 'height='+height+', width='+width+', top=100, left='+(screen.availWidth/2-screen.availWidth/3)+', toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no') ;
	  	var ret = WindowOpen("<%=root%>/Receive/receive!query.action","receivequery" ,width, height,100,(screen.availWidth/2-screen.availWidth/3));
	  }
	  
	</script>
	</head>
	<body class="contentbodymargin" onload="initMenuT();">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: middle;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td width="30%">
									&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">
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
											<s:if test="formId == 't_oarecvdoc'">
											<security:authorize ifAllGranted="001-000200090009"> 
												<td >
					                  <a class="Operation" href="javascript:recive();"><img
					                                    src="<%=root%>/images/ico/daoru.gif" width="15"
					                                    height="15" class="img_s">接收公文&nbsp;</a>
					                </td>
					                <td width="5"></td>
					                </security:authorize>
												<td>
													<a class="Operation" href="javascript:doDengJi();"> 
														<img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">
														登记&nbsp;</a>
												</td>
												<td width="5"></td>
											</s:if>
											<s:if test="formId == 't_oa_senddoc'">
												<td>
													<a class="Operation" href="javascript:doCreate()1;"> 
														<img src="<%=root%>/images/ico/tianjia.gif" width="15" height="15" class="img_s">
														草拟&nbsp;</a>
												</td>
												<td width="5"></td>
											</s:if>
											<td>
												<a class="Operation" href="#" onclick="doHide();"> 
													<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s"> <label
														id="label_search">
														显示查询条件&nbsp;
													</label>
												</a>
											</td>
											<td width="5"></td>
											<s:if test="formId != 't_oarecvdoc' && formId != 't_oa_senddoc'">
												<td>
													<a class="Operation" href="javascript:newDoc();"> 
														<img src="<%=root%>/images/ico/write.gif" width="15" height="15" class="img_s">
														新建&nbsp;</a>
												</td>
												<td width="5"></td>
											</s:if>
											<td>
												<a class="Operation" href="javascript:sendDoc();"> 
													<img src="<%=root%>/images/ico/songshen.gif" width="15" height="15" class="img_s">
													提交&nbsp;</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:editDoc();"> 
													<img src="<%=root%>/images/ico/bianji.gif" width="15" height="15" class="img_s">
													修改&nbsp;</a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="javascript:deleteDoc();"> 
													<img src="<%=root%>/images/ico/shanchu.gif" width="15" height="15" class="img_s">
													删除&nbsp;</a>
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
						<s:form id="myTableForm" action="/senddoc/sendDoc!draft.action">
							<!-- 流程名称 -->
							<s:hidden id="workflowName" name="workflowName"></s:hidden>
							<!-- 表单id -->
							<s:hidden id="formId" name="formId"></s:hidden>
							<!-- 表名称 -->
							<s:hidden id="tableName" name="tableName"></s:hidden>
							<s:hidden id="showSignUserInfo" name="showSignUserInfo"></s:hidden>
							<!-- 资源名称 -->
							<input type="hidden" name="privilName" id="privilName" value="<%=privilName%>" />
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
										if (columnName.toUpperCase().equals(
												"RECV_NUM".toUpperCase())) {
											recv_num = column;
											showColumnList.remove(i);
											i--;
										}
									}
							%>
							<webflex:flexTable name="myTable" width="<%=tablewidth %>" height="200px" wholeCss="table1"
								property="senddocId" isCanDrag="false" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
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
								<webflex:flexCheckBoxCol caption="选择" property="<%=columnName %>"
									showValue="<%=showColumn %>" width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<%
											if (recv_num != null) {
												String recv_numcolumnName = (String) recv_num[0];
												String recv_numshowColumn = (String) recv_num[3];
												String recv_numcaption = (String) recv_num[1];
												String recv_numtype = (String) recv_num[2];//字段类型
								%>
								<webflex:flexTextCol caption="<%=recv_numcaption %>" property="<%=recv_numcolumnName %>"
									showValue="<%=recv_numshowColumn %>" width="7%" isCanDrag="false" isCanSort="false"
									showsize="5"></webflex:flexTextCol>
								<%
											}
										} else if ("5".equals(type)) {//日期类型
											System.out.print(type);
								%>
								<webflex:flexDateCol caption="<%=caption %>" property="<%=columnName %>"
									showValue="<%=showColumn %>" width="10%" isCanDrag="true" dateFormat="yyyy-MM-dd HH:mm"
									isCanSort="true"></webflex:flexDateCol>
								<%
										} else {
												if (i != 1) {
													showSize = 6;
												}
								%>
								<webflex:flexTextCol caption="<%=caption %>" property="<%=columnName %>"
									showValue="<%=showColumn %>" width="<%=otherLength %>" isCanDrag="false" isCanSort="false"
									showsize="<%=showSize %>"></webflex:flexTextCol>
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
		      var workflowName =$("#workflowName").val();
		      var formId =$("#formId").val();
		      var sMenu = new Menu();
		      function initMenuT(){
		        sMenu.registerToDoc(sMenu);
		        var item = null;
		        	if(formId != "t_oa_senddoc" && formId != 't_oarecvdoc'){
				        item = new MenuItem("<%=root%>/images/ico/write.gif","新建","newDoc",1,"ChangeWidthTable","checkMoreDis");
				        sMenu.addItem(item);
		        	}
			        if(formId == "t_oa_senddoc"){
				        item = new MenuItem("<%=root%>/images/ico/write.gif","草拟","doCreate",1,"ChangeWidthTable","checkOneDis");
				        sMenu.addItem(item);
			        }
			        if(formId == 't_oarecvdoc'){
				        item = new MenuItem("<%=root%>/images/ico/tianjia.gif","登记","doDengJi",1,"ChangeWidthTable","checkOneDis");
				        sMenu.addItem(item);
			        }
			        item = new MenuItem("<%=root%>/images/ico/songshen.gif","提交","sendDoc",1,"ChangeWidthTable","checkOneDis");
			        sMenu.addItem(item);
			        item = new MenuItem("<%=root%>/images/ico/bianji.gif","修改","editDoc",1,"ChangeWidthTable","checkMoreDis");
			        sMenu.addItem(item);
			        item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteDoc",1,"ChangeWidthTable","checkOneDis");
			        sMenu.addItem(item);
		        sMenu.addShowType("ChangeWidthTable");
		        registerMenu(sMenu);
		         var privilName = "<%=privilName%>";
				if (privilName == "") {
					privilName = "${privilName}";
				}
				$("#privilName").val(privilName);
			}
		</script>
	</body>
</html>

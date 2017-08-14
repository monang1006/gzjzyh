<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<title>待办事宜</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
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
      	window.location = "<%=root%>/senddoc/sendDoc!todo.action?type=${type}&workflowName="+encodeURI(encodeURI($("#workflowName").val()))
      				+"&formId="+$("#formId").val()+"&privilName="+privilName+"&workflowType="+workflowType;
      }	
      //处理  
      function onDblClickProcess(theValue) {
      	//document.getElementById("noshow").innerHTML=theValue;
      	 var taskId=$(theValue).find("input").val();
          if(taskId == ""){
          	alert("请选择要处理的流程！");
          	return ;
          }
          
          var TaskIdAndInstanceId = taskId.split("$");
		  taskId = TaskIdAndInstanceId[0];
		  var instanceId = TaskIdAndInstanceId[1];
    	  var width=screen.availWidth-10;
		  var height=screen.availHeight-30;
		  //更改为CA认证Action dengzc 2011年4月2日16:05:09 
		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height);
      }
      //查阅 
      function processDoc() {
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要查阅的记录。");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("只可以查阅一条记录。");
          		return ;
          	}
          }
          var TaskIdAndInstanceId = taskId.split("$");
		  var taskId = TaskIdAndInstanceId[0];
		  var instanceId = TaskIdAndInstanceId[1];
    	  var width=screen.availWidth-10;
		  var height=screen.availHeight-30;
		   //更改为CA认证Action dengzc 2011年4月2日16:05:09 
		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
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
          		alert("只可以查看一条记录。");
          		return ;
          	}
          }  
          TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		  var taskId = TaskIdAndInstanceId[0];
		  var instanceId = TaskIdAndInstanceId[1];
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId,'Cur_workflowView',width, height, "办理记录");
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
      
      
        //指派
       function zp(){
       	var taskId = getValue();
       	if(taskId == ""){
       		alert("请选择要指派的记录。");
       		return ;
       	}else{
       		if(taskId.split(",").length>1){
       			alert("只可以指派一条记录。");
       			return;
       		}
       	}
       	var TaskIdAndInstanceId = taskId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
       	$.post("<%=root%>/senddoc/sendDoc!checkCanReturn.action",
       			{taskId:taskId},
       			function(data){
       				if(data == "-1"){
       					alert("此任务不存在或已删除，操作失败！");
       				}else if(data == "-2"){
       					alert("操作失败，请与管理员联系！");
       				}else{
       					var flags = data.split("|");
       					var flagzp = flags[2];//指派
       					if(flagzp == "1"){//允许指派
       						var taskActors = "";
							var url = scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=reassign&nodeId=0&taskId=" + taskId + "&taskActors=" + taskActors;
							var userstr = OpenWindow(url, 420, 450, window);          
							//用户ID|用户名称,指派是否需要返回（0：否、1：是）
							if(userstr!=null && userstr!=''){
								$.post("<%=root%>/senddoc/sendDoc!reAssign.action",{taskId:taskId,suggestion:encodeURI(userstr)},
									   function(retCode){
									   		if(retCode == "0"){
									   			alert("任务指派成功。");
									   			reloadPage();
									   		}else if(retCode == "-1"){
									   			alert("任务实例不存在或已删除！");
									   		}else if(retCode == "-2"){
									   			alert("指派过程中出现异常！");
									   		}else if(retCode == "-3"){
									   			alert("参数传输错误！");
									   		}
									   }			
								);
							}
       					}else if(flagzp == "0"){//不允许指派
       						alert("对不起，此任务不允许指派！");
       					}else{
       						alert("对不起，出现未知错误！请与管理员联系！");
       					}
       				}
       			});
       }
       
         //流程送审
      function sendDoc() {
       	  var height=473;//screen.availHeight-50;
       	  var width=480;//screen.availWidth/2;
          var bussinessId = getValue();
          var TaskIdAndInstanceId = bussinessId.split("$");
		  var taskId = TaskIdAndInstanceId[0];
		  var instanceId = TaskIdAndInstanceId[1];
          
          if(bussinessId == ""){
   		 	alert("请选择要送审的记录。");
   		 	return ;
   		 }else{
   		 	var docIds = bussinessId.split(",");
   		 	if(docIds.length>1){
   		 		alert("只可以送审一条记录。");
   		 		return ;
   		 	}
   		 }
   		 var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
   		 if("${page.pageSize}"!=""){
         	param += "&page.pageSize=${page.pageSize}";
	     }
         var ret = OpenWindow("<%=root%>/senddoc/sendDoc!nextstep.action?tableName="+$("#tableName").val()
         			+"&taskId="+taskId+"&formId="+$("#formId").val()
         			+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())), 
                               width, height,window);
        if(ret){
        	if(ret == "OK"){
        		//alert("发送成功！");
        		reloadPage();
        		//window.location = "<%=root%>/senddoc/sendDoc!draft.action"+param;
        	}else if(ret == "NO"){
        		alert("发送失败！");
        	}
        }
      }
      //废除公文,挂起流程实例
      function repeal(){
		var bussinessId = getValue();
		var TaskIdAndInstanceId = bussinessId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
		if(bussinessId == ""){
   		 	alert("请选择要废除的记录。");
   		 	return ;
   		 }else{
   		 	//alert(instanceId);
   		 	$.post("<%=root%>/senddoc/sendDoc!repealProcess.action",
			{instanceId:instanceId},
			function(data){
			    if(data=="true"){
			       alert("废除文件成功。");
			       reloadPage();
   		 		   //window.location = "<%=root%>/senddoc/sendDoc!todo.action?workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&formId="+$("#formId").val();
			    }else{
					alert("废除文件失败！");
				}
			});
   		 	   		 	   		   		 
   		 }   		   		 
      }
       
       
       function exportfils(){
			if(confirm("确定要将当前页内容导出为EXCEL？")){
				var action = $("#myTableForm").attr("action");
				var url="<%=root%>/senddoc/sendDocUpload!exportTodoExcel.action";
				$("#myTableForm").attr("target","tempframe");
				$("#myTableForm").attr("action",url);
				$("#button").click();
				$("#myTableForm").removeAttr("target");
				$("#myTableForm").attr("action",action);
				/*
				 var url="<%=root%>/senddoc/sendDocUpload!exportTodoExcel.action";
				 var pama="?1=1";
			 	 pama += ("&workflowName="+encodeURI(encodeURI($("#workflowName").val())));
			 	 pama += ("&formId="+$("#formId").val());
			 	 pama += ("&type="+$("#type").val());
			 	 pama += ("&privilName="+encodeURI(encodeURI($("#privilName").val())));
			 	 pama += ("&page.pageNo=${page.pageNo}");
			 	 pama += ("&page.pageSize=${page.pageSize}");
				 url=(url+pama);
				 document.getElementById('tempframe').src=url;
				 */
			}
		
		}
       
    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<iframe scr='' id='tempframe' name='tempframe' style='display:none'></iframe>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td id="privilTitle" width="20%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;
									<s:if test="workflowName == null || workflowName.length() == 0">
										<%=privilName%>${privilName}
									</s:if>
									<s:else>
										${workflowName }
									</s:else>
								</td>
								<td width="75%">
									<table align="right">
										<tr>
											<%--
											<td >
											<a class="Operation" href="javascript:sendDoc();">
												<img src="<%=root%>/images/ico/songshen.gif" width="15" height="15" class="img_s">
												签收&nbsp;</a> 
											</td>
											<td width="5">
											</td>
											--%>
											<td>
												<a class="Operation" href="javascript:processDoc();">
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
											<%--
											<td>
												<a class="Operation" href="javascript:zp();">
													<img src="<%=root%>/images/ico/weituo.gif" width="15" height="15" class="img_s">
													指派&nbsp;</a>
											</td>
											<td width="5">
											</td>
											--%>
											<td>
												<a class="Operation" href="#" onclick="doHide();">
													<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s"> <label id="label_search">
													显示查询条件&nbsp;
													</label> </a>
											</td>
											<td width="5"></td>
											<s:if test="type=='sign'">
												<td>
													<%--
													<a class="Operation" href="javascript:exportfils();">
														<img src="<%=root%>/images/ico/daochu.gif" width="15" height="15" class="img_s">
														导出为EXCEL&nbsp;
													</a>
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
						<%
							List showColumnList = (List) request.getAttribute("showColumnList");
							double size = showColumnList.size() - 1;//减掉第一列（主键列）
							String otherLength = "10%";
							String titleLength = "30%";
							int showSize = 16;
							String tablewidth = "100%";
							double otherwidth = 0;
							if (size <= 7) {
								otherwidth = (100 - 40) / size;
								otherLength = otherwidth + "%";
							} else {
								tablewidth = (((size - 7) * 10) + 100) + "%";
							}
						%>
						<s:form name="myTableForm" id="myTableForm"
							action="/senddoc/sendDoc!todo.action" >
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="senddocId" isCanDrag="false"
								ondblclick="onDblClickProcess(this.outerHTML);"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<input name="button" id="button" type="submit" style="display: none">
								<!-- 流程名称 -->
								<s:hidden id="workflowName" name="workflowName"></s:hidden>
								<!-- 表单id -->
								<s:hidden id="formId" name="formId"></s:hidden>
								<!-- 表名称 -->
								<s:hidden id="tableName" name="tableName"></s:hidden>
								<!-- 签收状态 -->
								<s:hidden name="type" id="type"></s:hidden>
								<!-- 资源名称 -->
								<input type="hidden" name="privilName" id="privilName" value="" />
								<!-- 流程类别 -->
								<input type="hidden" name="workflowType" id="workflowType"
									value="${workflowType }" />
								<strong:query queryColumnList="${queryColumnList}" />
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
										if (columnName.toUpperCase().equals("RECV_NUM".toUpperCase())) {
											recv_num = column;
											showColumnList.remove(i);
											i--;
										}
									}

									String pkFieldName = "";
									for (int i = 0; i < showColumnList.size(); i++) {
										Object[] column = (Object[]) showColumnList.get(i);
										String columnName = (String) column[0];
										String showColumn = (String) column[3];
										String caption = (String) column[1];
										String type = (String) column[2];//字段类型
										System.out.println(columnName + " ---- " + type);
										if (i == showColumnList.size() - 1) {
											//otherLength = "100%";
										}
										if (i == 0) {
											pkFieldName = columnName;
											showColumn = showColumn.replace("\\r\\n", " ");
											showColumn = showColumn.replace("\\n", " ");
								%>

								<webflex:flexCheckBoxCol caption="选择" property="<%=columnName%>"
									showValue="<%=showColumn%>" width="10%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<%
										if (pngColumn != null) {
										String pngcolumnName = (String) pngColumn[0];
										String pngshowColumn = (String) pngColumn[3];
										String pngcaption = (String) pngColumn[1];
										String pngtype = (String) pngColumn[2];//字段类型
								%>
								<webflex:flexFlagCol caption="<%=pngcaption%>"
									property="<%=columnName%>" showValue="<%=pngshowColumn%>"
									isCanDrag="false" isCanSort="false"></webflex:flexFlagCol>
								<%
											if (recv_num != null) {
											String recv_numcolumnName = (String) recv_num[0];
											String recv_numshowColumn = (String) recv_num[3];
											String recv_numcaption = (String) recv_num[1];
											String recv_numtype = (String) recv_num[2];//字段类型
								%>
								<webflex:flexTextCol caption="<%=recv_numcaption%>"
									property="<%=recv_numcolumnName%>"
									showValue="<%=recv_numshowColumn%>" width="" isCanDrag="false"
									isCanSort="false" showsize="7"></webflex:flexTextCol>
								<%
											}

											}
										} else if (SendDocManager.DATE_TYPE.equals(type)
										|| "5".equals(type)) {//日期类型
								%>
								<webflex:flexDateCol caption="<%=caption%>"
									property="<%=columnName%>" showValue="<%=showColumn%>"
									width="<%=otherLength%>" isCanDrag="true"
									dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>
								<%
											} else {
											if (BaseWorkflowManager.WORKFLOW_TITLE.equals(columnName)) {//标题字段
								%>

								<webflex:flexTextCol caption="<%=caption%>"
									property="<%=pkFieldName%>" showValue="<%=showColumn%>"
									width="<%=titleLength%>" isCanDrag="false"
									onclick="ViewFormAndWorkflow(this.value);" isCanSort="false"
									showsize="<%=showSize%>"></webflex:flexTextCol>
								<%
										} else {
										showSize = 5;
										otherLength = "100px";
								%>
								<webflex:flexTextCol caption="<%=caption%>"
									property="<%=columnName%>" showValue="<%=showColumn%>"
									width="<%=otherLength%>" showsize="<%=showSize%>"
									isCanDrag="false" isCanSort="false"></webflex:flexTextCol>
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
			<div id="noshow" style="display: none">

			</div>
		</div>
		<script language="javascript">
    	
      correctPNG(); 	//IE6中正常显示透明PNG 
    
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
	        item = new MenuItem("<%=root%>/images/ico/page.gif","查阅","processDoc",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/ico/banli.gif","办理记录","workflowView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	       // item = new MenuItem("<%=root%>/images/ico/weituo.gif","指派","zp",1,"ChangeWidthTable","checkMoreDis");
	        //sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
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
		   //更改为CA认证Action dengzc 2011年4月2日16:05:09 
		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
	}
     
    function isdb(){
    	alert("goodtest");
    } 
    </script>
	</body>
</html>

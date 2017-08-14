<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="com.strongit.oa.common.service.BaseWorkflowManager" />
<jsp:directive.page import="com.strongit.oa.senddoc.manager.SendDocManager" />
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-query" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>待办事宜</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/senddoc/exportExcel.js" type="text/javascript"></script>
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
	      				+"&formId="+$("#formId").val()+"&privilName="+privilName+"&workflowType="+workflowType+"&handleKind="+$("#handleKind").val();
		}	
		//处理  
		function onDblClickProcess(theValue) {
      		var taskId=$(theValue).find("input").val();
        	if(taskId == ""){
	          	alert("请选择要处理的记录。");
	          	return ;
	        }
			var TaskIdAndInstanceId = taskId.split("$");
			taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			
			//判断是否公文传输转过来流程     tj
			//是否填写退文单   填写后不可再走流程
			var t = $("input:checked").parent().parent().children().eq(7).html();
			t = t.substring(t.indexOf("'")+1,t.lastIndexOf("'"));
			var n = t.split("'");
			if(n[2]=="2"){
				//更改为CA认证Action dengzc 2011年4月2日16:05:09 
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?gwcs=gwcs&taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height);
			}else{
				//更改为CA认证Action dengzc 2011年4月2日16:05:09 
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height);
			}
		}
		
		//退到公文传输
		function retDocTrans(){
			var taskId = getValue();
        	if(taskId == ""){
	          	alert("请选择要退回的记录。");
	          	return ;
        	}else{
	          	var taskIds = taskId.split(",");
	          	if(taskIds.length>1){
	          		alert("只可以退回一条记录。");
	          		return ;
	          	}
			}
        	
        	var TaskIdAndInstanceId = taskId.split("$");
			taskId = TaskIdAndInstanceId[0];
        	
			//获取docId
			var mydocIds = $("input:checked").parent().parent().children().eq(7).html();
			mydocIds = mydocIds.substring(mydocIds.indexOf("'")+1,mydocIds.lastIndexOf("'"));
			var n = mydocIds.split("'");
			if(n[0].length != 32){
				alert("此文件非来自公文传输！");
				return;
			}
			if(n[2] == "2"){
				alert("此公文已退回到公文传输！");
				return;
			}
			
			var laiwentitle = $("input:checked").parent().parent().children().eq(4).html();
			
			url = "<%=root%>/sends/transDoc!retdoctrans.action?taskId="+taskId+"&docSendId="+n[0]+"&laiwentitle="+laiwentitle;
			var a = OpenWindow(url,window,'help:no;status:no;scroll:auto;dialogWidth:600px; dialogHeight:760px');
			if(a=="true"){
				window.location.reload();
			}
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
	          var TaskIdAndInstanceId = taskId.split("$");
			  var taskId = TaskIdAndInstanceId[0];
			  var instanceId = TaskIdAndInstanceId[1];
            //var iWidth=1200; //弹出窗口的宽度;
			//var iHeight=450; //弹出窗口的高度;
			//var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
			//var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
			//WindowOpen("<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId="+instanceId,'todo',iWidth, iHeight,iTop,iLeft);
			showModalDialog("<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId="+instanceId, 
                    window,"dialogWidth:750px;dialogHeight:420px;status:no;help:no;scroll:yes;");
	     }

		//查阅 
		function processDoc() {
        	var taskId = getValue();
        	//alert("sss:"+taskId);
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
			//判断是否公文传输转过来流程     tj
			//是否填写退文单   填写后不可再走流程
			//n[0] 为docid   n[2]为  docflag
			var t = $("input:checked").parent().parent().children().eq(7).html();
			/*
			alert(t==""||t==null)
			if(t==""||t==null){
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
				return;
			}
			*/
			t = t.substring(t.indexOf("'")+1,t.lastIndexOf("'"));
			var n = t.split("'");
			if(n[2]=="2"){
				//更改为CA认证Action dengzc 2011年4月2日16:05:09 
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?gwcs=gwcs&taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
			}else{
				//更改为CA认证Action dengzc 2011年4月2日16:05:09 
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?senddocid="+n[0]+"&taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
			}
		}
		
		//点中流程标题查阅
		function processDoc1(value) {
			var TaskIdAndInstanceId = value.split("$");
			var taskId = TaskIdAndInstanceId[0];
			var instanceId = TaskIdAndInstanceId[1];
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var t = $(window.event.srcElement).siblings().eq(1).parent().parent().children().eq(7).html();
			
			t = t.substring(t.indexOf("'")+1,t.lastIndexOf("'"));
			var n = t.split("'");
			if(n[2]=="2"){
				//更改为CA认证Action dengzc 2011年4月2日16:05:09 
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?gwcs=gwcs&taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
			}else{
				//更改为CA认证Action dengzc 2011年4月2日16:05:09 
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?senddocid="+n[0]+"&taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI($("#workflowName").val())),'todo',width, height, "待办事宜");
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
	        		reloadPage();
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
				    }else{
						alert("废除文件失败!");
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
			}
		}
		function show(value,FLAG){
			if(value.length==32){
				return "是";
			}else{
				return "否";
			}
		}
    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
		<iframe scr='' id='tempframe' name='tempframe' style='display: none'></iframe>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder">
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
									<s:label id="privilName1" name="privilName1"></s:label>
									</s:if>
									<s:else>
										${workflowName }
									</s:else></strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
										    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								            <td class="Operation_list" onclick="sendMail();"><img src="<%=root%>/images/operationbtn/Send_email.png"/>&nbsp;转发邮件&nbsp;</td>
								            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								            <td width="5"></td>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="relativeWf();"><img src="<%=root%>/images/operationbtn/urge.png"/>&nbsp;关联流程&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="processDoc();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
						                 	<td width="5"></td>
						                 	<s:if test="type=='sign'">
												<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 	<td class="Operation_list" onclick="CellAreaExcel();"><img src="<%=root%>/images/operationbtn/Export_to_excel.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;EXCEL&nbsp;</td>
							                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                 	<td width="5"></td>
											</s:if>
											<%--<td>
												<a class="Operation" href="javascript:retDocTrans();">
													<img src="<%=root%>/images/ico/zhuanjiaoxiayibu.gif" width="15" height="15" class="img_s">
													退到公文传输&nbsp;</a>
											</td>--%>
											<%--<td>
												<a class="Operation" href="javascript:zp();">
													<img src="<%=root%>/images/ico/tool-reply.gif" width="15" height="15" class="img_s">
													指派&nbsp;</a>
											</td>
											<td width="5">
												&nbsp;
											</td>
											--%>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<!--收文处理待办（待签收）显示列 【收文编号	文件标题	来文单位	来文字号	来文日期	上步办理人	所在部门】	  -->
						<%
							List showColumnList = new ArrayList((List) request.getAttribute("showColumnList"));
							String[] column1 = (String[]) showColumnList.get(0);
							String[] column2 = null;
							String[] column3 = null;
							String[] column4 = null;
							String[] column5 = null;
							String[] column6 = null;
							String[] column7 = null;
							String[] column8 = null;
							String[] column9 = null;
							String[] column10 = null;
							for (int i = 0; i < showColumnList.size(); i++) {
								Object[] column = (Object[]) showColumnList.get(i);
								String columnName = (String) column[0];
								if (columnName.toUpperCase().equals("PNG".toUpperCase())) {
									column2 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals("RECV_NUM".toUpperCase())) {
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
								if (columnName.toUpperCase().equals("DOC_NUMBER".toUpperCase())) {
									column6 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals("RECV_TIME".toUpperCase())) {
									column7 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"CURRENTUSERNAME".toUpperCase())) {
									column8 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"CURRENTUSERDEPT".toUpperCase())) {
									column9 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
								if (columnName.toUpperCase().equals(
										"DOC_ID".toUpperCase())) {
									column10 = (String[]) column;
									showColumnList.remove(i);
									i--;
									continue;
								}
							}
						%>
						<s:form name="myTableForm" id="myTableForm" action="/senddoc/sendDoc!todo.action">
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
									if (column2 != null) {
								%>
								<webflex:flexFlagCol caption="" property="<%=column1[0]%>" showValue="<%=column2[3]%>"
									isCanDrag="false" isCanSort="false" width="10%"></webflex:flexFlagCol>
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
								<%--<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column1[0]%>"
									showValue="<%=column4[0]%>" width="25%" isCanDrag="false" isCanSort="false"
									onclick="ViewFormAndWorkflow(this.value);" showsize="12"></webflex:flexTextCol>--%>
								<webflex:flexTextCol caption="<%=column4[1]%>" property="<%=column1[0]%>" 
									showValue="<%=column4[0]%>" width="50%" isCanDrag="false" isCanSort="false" 
									onclick="processDoc1(this.value);" showsize="12"></webflex:flexTextCol>
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
								<webflex:flexTextCol caption="<%=column8[1]%>" property="<%=column8[3]%>" align="center" 
									showValue="<%=column8[0]%>" width="2%" isCanDrag="false" isCanSort="false" showsize="5"></webflex:flexTextCol>
								<%
									}
								%>
								<%
									if (column9 != null) {
								%>
								<webflex:flexTextCol caption="<%=column9[1]%>" property="<%=column9[3]%>" align="center"
									showValue="<%=column9[0]%>" width="10%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
								<%
									}
								%>
								<%
									if (column10 != null) {
								%>
								<webflex:flexTextCol caption="公文传输转入" property="<%=column10[3]%>" align="center"
									showValue="javascript:show(DOC_ID,DOC_FLAG);" width="2%" isCanDrag="false" isCanSort="false" showsize="7"></webflex:flexTextCol>
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
			correctPNG(); 	//IE6中正常显示透明PNG
			
			//用于隐藏列的
			//alert($("input:checkbox").parent().parent().next().html());
			//$("input:checkbox").parent().next().next().next().hide();
			
			var sMenu = new Menu();
			
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
        		var item = null;
        		//item = new MenuItem("<%=root%>/images/ico/zhuanjiaoxiayibu.gif","退到公文传输","retDocTrans",1,"ChangeWidthTable","checkMoreDis");
		        //sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/Send_email.png","转发邮件","sendMail",1,"ChangeWidthTable","checkOneDis");
            sMenu.addItem(item);
	        item = new MenuItem("<%=root%>/images/operationbtn/urge.png","关联流程","relativeWf",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","processDoc",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		        item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录","workflowView",1,"ChangeWidthTable","checkMoreDis");
		        sMenu.addItem(item);
		        //item = new MenuItem("<%=root%>/images/ico/tool-reply.gif","指派","zp",1,"ChangeWidthTable","checkMoreDis");
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

			        $("#privilName1").html(privilName);
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
				var ret=WindowOpen("<%=root%>/senddoc/sendDoc!CASign.action?taskId=" + taskId
							+ "&instanceId=" + instanceId + "&workflowName="
							+ encodeURI(encodeURI($("#workflowName").val())), 'todo', width, height, "待办事宜");
			}
	
			function isdb() {
				alert("goodtest");
			}
			
	 //转发邮件
		function sendMail(){
			var bussinessId = getValue();
			if(bussinessId == ""){
				alert("请选择要转发邮件的记录。");
				return ;
			}else{
				var docIds = bussinessId.split(",");
				if(docIds.length>1){
					alert("只可以转发邮件一条记录。");
					return ;
				}
			}
			var bussinessId = bussinessId.split("$");
            var instanceId   = bussinessId[1];
			var iWidth=700; //弹出窗口的宽度;
			var iHeight=480; //弹出窗口的高度;
			var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
			var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
			var param = "?formId="+$("#formId").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()));
			param = param+"&pkFieldValue="+bussinessId[0]+"&instanceId="+instanceId;
			 WindowOpen("<%=root%>/senddoc/sendDoc!sendMail.action"+param,'sendMail',700, 480,iTop,iLeft);  
		}
		</script>
	</body>
</html>

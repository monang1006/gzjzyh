<%@ page contentType="text/html; charset=utf-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html style="width:100%; height:100%;">
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			//切换显示
			function switchView(){
				if(window.parent.frames.contentframe.cols=="0,*"){
					window.parent.frames.contentframe.cols="18%,*";
				}else{
					window.parent.frames.contentframe.cols="0,*";
				}
			}
			function exportfils(){
					 var iframeWindow=document.getElementById("iframe").contentWindow;
					 var processId=iframeWindow.document.getElementById("processId").value;
					 var proName=iframeWindow.document.getElementById("proName").value;
					 var processName=iframeWindow.document.getElementById("processName").value;
					 var startUserName=iframeWindow.document.getElementById("startUserName").value;
					 var workflowName=iframeWindow.document.getElementById("workflowName").value;
					 var searchDate=iframeWindow.document.getElementById("searchDateTEXT").value;
					 var state=iframeWindow.document.getElementById("state").value;
					 var timeout=iframeWindow.document.getElementById("timeout").value;
					 var processDefinitionNames=iframeWindow.document.getElementById("processDefinitionNames").value;
					 var day=iframeWindow.document.getElementById("dayTEXT").value;
					  /**/
				if(confirm("您是否要将流程监控信息导出？")){
				     var isBool=false;
					 var url="<%=root%>/workflowDesign/action/processMonitor!exportfils.action";
					 var pama="?1=1";
					 if(processId!=""){
					 	isBool=true;
					 	pama=pama+("&processId="+processId);
					 }
					 if(proName!=""){
					 	isBool=true;
					 	pama=pama+("&proName="+encodeURI(encodeURI(proName)));
					 }
					 if(processName!=""){
					 	isBool=true;
					 	pama=pama+("&processName="+encodeURI(encodeURI(processName)));
					 }
					 if(startUserName!=""){
					 	isBool=true;
					 	pama=pama+("&startUserName="+encodeURI(encodeURI(startUserName)));
					 }
					 if(workflowName!=""){
					 	isBool=true;
					 	pama=pama+("&workflowName="+encodeURI(encodeURI(workflowName)));
					 }
					 if(searchDate!=""){
					 	isBool=true;
					 	pama=pama+("&searchDate="+searchDate);
					 }
					 if(state!=""){
					 	isBool=true;
					 	pama=pama+("&state="+state);
					 }
					 if(timeout!=""){
					 	isBool=true;
					 	pama=pama+("&timeout="+timeout);
					 }
					 if(processDefinitionNames!=""){
					 	isBool=true;
					 	pama=pama+("&processDefinitionNames="+processDefinitionNames);
					 }
					 if(day!=""){
					 	isBool=true;
					 	pama=pama+("&day="+day);
					 }
					 if(isBool==true){
					   url=(url+pama);
					 }
					 document.getElementById('tempframe').src=url;
				}
			}
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
		</script>
	</head>
	<body style="width:100%; height:100%; overflow:hidden;">
		<iframe scr='' id='tempframe' name='tempframe' style='display:none'></iframe>
		<div>
			<table width="100%"  border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									流程监控列表：${proName}
									<div id="div_instance" style="display:none;">
										流程实例id：
										<input size=8 id="tId"/>
										迁移线名称:
										<input size=10 id="transName" />
										<input type="button" value="结束" onclick="doLeave();">
									</div>
								</td>
								<td align="right">
									<input type="hidden" id="isShowparentBtn" value="true">
									<table id="parentBtn" border="0" align="right" cellpadding="0" cellspacing="0">
										<tr>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="switchView();"><img src="<%=root%>/images/operationbtn/Switching.png"/>&nbsp;切&nbsp;换&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="document.getElementById('iframe').contentWindow.viewProcess();"><img src="<%=root%>/images/operationbtn/Operation.png"/>&nbsp;运行情况&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="document.getElementById('iframe').contentWindow.viewMonitorData();"><img src="<%=root%>/images/operationbtn/Monitor.png"/>&nbsp;监&nbsp;控&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
					                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="document.getElementById('iframe').contentWindow.delProcess();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
											<%--
											<td>
												<a class="Operation" href="javascript:exportfils();"><img
														class="img_s" src="<%=root%>/images/ico/daochu.gif"
														width="15" height="15" alt=""> 导出&nbsp;</a>
											</td>
											--%>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
       	</div>
       	<div>
			<iframe id="iframe" height="92.5%" frameborder="0" width="100%" src="<%=root%>/workflowDesign/action/processMonitor.action" scrolling="auto"></iframe>
	  	</div>
	</body>
</html>
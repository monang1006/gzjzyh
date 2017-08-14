<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="java.net.URLDecoder"/>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<%
  String state =request.getParameter("state");
  String privilNamebak =(request.getParameter("privilNamebak")==null?"":request.getParameter("privilNamebak"));
  String title = "";
  if(privilNamebak != ""){
	  title = URLDecoder.decode(privilNamebak,"utf-8");
  }else{
	  if("0".equals(state)){
	  title = "在办文件";
	  }else if("1".equals(state)){
	  title = "办结文件";
	  }else{
	  title = "流程监控列表";
	  }
  }
 %>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		
		<script type="text/javascript">
			//切换显示
			function switchView(){
				if(window.parent.frames.contentframe.cols=="0,*"){
					window.parent.frames.contentframe.cols="18%,*";
				}else{
					window.parent.frames.contentframe.cols="0,*";
				}
			}
			
			function swichSearchtr(){
				var search_tr = document.frames["iframe"].search_tr;
				if($("#searchBTN").attr("flag")=="hide"){
					$(search_tr).hide();
					$("#searchBTN").attr("flag","show");
					$("#searchBTN").html("<img class=\"img_s\" src=\"<%=root%>/images/ico/yidong.gif\" width=\"14\" height=\"15\" alt=\"\"> 显示查询&nbsp;");
				}else{
					$(search_tr).show();
					$("#searchBTN").attr("flag","hide");
					$("#searchBTN").html("<img class=\"img_s\" src=\"<%=root%>/images/ico/yidong.gif\" width=\"14\" height=\"15\" alt=\"\"> 隐藏查询&nbsp;");
				}
			}
			function search(){
				var suosuo = document.frames['iframe'].sousuoClick();
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
					 var url="<%=root %>/workflowDesign/action/processMonitor!exportfils.action";
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
			
			
		$(document).ready(function() {
			
			var mytype='<%=request.getParameter("mytype") %>';
			if("org"==mytype){
				var iframe_wjtj_url= '<%=root %>/workflowDesign/action/documentHandle!todowjtj.action?mytype=<%=request.getParameter("mytype") %>&state=<%=request.getParameter("state") %>&workflowType=<%=request.getParameter("workflowType") %>&excludeWorkflowType=<%=request.getParameter("excludeWorkflowType") %>';
				$("#iframe_wjtj").attr("src",iframe_wjtj_url);
			}else{
				$("#iframe_wjtj").hide();
			}
		});
		
		function scrollIntoView(){
			var a=document.getElementById("iframe");
	        if (a){
	        	a.scrollIntoView(true);
	        }       
		}
		
		function setFramebyOrg(orgId){
			var conFrame = document.frames["iframe"];
			conFrame.setFramebyOrg(orgId);
		}
		
		</script>
	</head>
	<body style="overflow:auto">
	
	<iframe id="iframe_wjtj" height="100%" frameborder="0" width="100%" src=""></iframe>
	
	<iframe src='' id='tempframe' name='tempframe' style='display:none'></iframe>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
		<tr><td colspan="3" class="table_headtd">
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<%--<td width="5%" align="center">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9" alt="">
											</td>
											--%><%--<td >
												<%=title %>：${proName}
											</td>
											
											--%>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
											<strong><script>
																var mytype='<%=request.getParameter("mytype") %>';
																if(mytype=="user"){
																	window.document.write("流程任务查询");
																}else if(mytype=="dept"){
																	window.document.write("处内文件");
																}else if(mytype=="org"){
																	window.document.write("厅内文件");
																}
																else{
																	window.document.write("流程查询与监控");
																}
																</script>
												</strong>
											</td>
											<td width="55%">
												<input type="hidden" id="isShowparentBtn" value="true">
												<table id="parentBtn" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<%--<td >
															<a class="Operation" href="javascript:exportfils();"><img class="img_s" src="<%=root%>/images/ico/daochu.gif" width="15"
																height="15" alt="">
															导出&nbsp;</a>
														</td>
														<td width="5">
															&nbsp;
														</td>
														--%>
														<%-- 
														<td >
															<a class="Operation" href="javascript:switchView();"><img class="img_s" src="<%=root%>/images/ico/chakan.gif" width="15"
																height="15" alt="">
															切换&nbsp;</a>
														</td>
														<td width="5">
															&nbsp;
														</td>	
														--%>
														<%--<% if("0".equals(state)||"1".equals(state)) {%>
														<td >
															<a class="Operation" href="javascript:search();"><img class="img_s" src="<%=root%>/images/ico/search.gif" width="14"
																height="15" alt="">
															搜索&nbsp;</a>
														</td><td width="5">
															&nbsp;
														</td>	
														<td >
															<a id="searchBTN" flag='show' class="Operation" href="javascript:swichSearchtr();"><img class="img_s" src="<%=frameroot%>/images/yidong.gif" width="14"
																height="15" alt="">
															显示查询&nbsp;</a>
														</td><td width="5">
															&nbsp;
														</td>	
														<% }%>
														--%>
														<s:if test="empty mytype">
														<td >
															<a class="Operation" href="javascript:document.getElementById('iframe').contentWindow.viewMonitorData();"><img class="img_s" src="<%=root%>/images/ico/chakan.gif" width="14"
																height="15" alt="">
															查看&nbsp;</a>
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
		</td></tr>
		</table>
								
		<iframe id="iframe" height="100%" frameborder="0" width="100%" src="<%=root %>/bgt/documentview/documentView!monitorList.action?mytype=<%=request.getParameter("mytype") %>&state=<%=request.getParameter("state") %>&workflowType=<%=request.getParameter("workflowType") %>&excludeWorkflowType=<%=request.getParameter("excludeWorkflowType") %>"></iframe>							
	</body>
</html>
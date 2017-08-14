<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
	<head>
	<style type="text/css">
* { margin:0; padding:0; list-style:none;}
html { height:100%; overflow:hidden; background:#fff;}
body { height:100%; overflow:auto; background:#fff;}
  </style>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
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
			localwjtj();
		});
		
		function localwjtj(){
			var 	doneYear = $("#doneYear").val();
			var mytype='<%=request.getParameter("mytype") %>';
			if("org"==mytype){
				var iframe_wjtj_url= '<%=root %>/workflowDesign/action/documentHandle!wjtj.action?mytype=<%=request.getParameter("mytype") %>&doneYear='+doneYear+'&state=<%=request.getParameter("state") %>&workflowType=<%=request.getParameter("workflowType") %>&excludeWorkflowType=<%=request.getParameter("excludeWorkflowType") %>';
				$("#iframe_wjtj").attr("src",iframe_wjtj_url);
			}else{
				$("#div_wjtj").hide();
			}
		}
		
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
		function changeDoneYear(obj){
			 $("#doneYear").val(obj);
			if(!$('#iframe_wjtj').is(':hidden')){
				 localwjtj();
			}
		}
		</script>
	</head>
	<body class="contentbodymargin" >
	<div id=contentborder >
		<div id="div_wjtj" style="position:relative; overflow:hidden; height:100%;">
		<iframe id="iframe_wjtj" height="100%" frameborder="0" width="100%" src=""></iframe>
		</div>
		<div style="position:relative; overflow:hidden; height:100%; ">
			<div style="position:absolute; left:0; top:0px; right:0px; height:21px;">
			<input name="doneYear" id="doneYear" type="hidden" value=""/>
			<iframe src='' id='tempframe' name='tempframe' style='display:none'></iframe>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<strong><script>
												var mytype='<%=request.getParameter("mytype") %>';
												var workflowType='<%=request.getParameter("workflowType") %>';
												if(mytype=="user"){
													  if(workflowType=="3"){
													  window.document.write("来文查询列表");
													  }else{
														window.document.write("流程任务查询");
													  }
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
							<td align="right">
							<input type="hidden" id="isShowparentBtn" value="true"/>
								<table id="parentBtn" border="0" align="right" cellpadding="0" cellspacing="0">
										<tr>
											<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="document.getElementById('iframe').contentWindow.viewMonitorData();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</div>
			 <div style="position:absolute; left:0; top:40px; right:0px; bottom:0; overflow:auto; height:435px">
			 						
			<iframe id="iframe" frameborder="0" height="100%" width="100%" src="<%=root %>/workflowDesign/action/processMonitor!monitorList.action?mytype=<%=request.getParameter("mytype") %>&state=<%=request.getParameter("state") %>&workflowType=<%=request.getParameter("workflowType") %>&excludeWorkflowType=<%=request.getParameter("excludeWorkflowType") %>" scrolling="auto"></iframe>							
		</div>
		</div>
	</div>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>相关工作流</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				var parWin = window.dialogArguments;
				var workflowId=parWin.document.getElementById("definitionWorkflowid").value;
				if(workflowId!=null&&workflowId!=""){
					var checkobjs=document.getElementsByName("chkRadio");
					for(var i=0;i<checkobjs.length;i++){
						var currentTr=checkobjs[i].value;
						if(currentTr==workflowId){
							checkobjs[i].checked=true;
							break;
							
						}
					}
					
				}
				
			});
		
			//全选
			function checkAll(chkAll){//this,document.getElementById('myTable_td'),'#A9B2CA',true
				var checked = chkAll.checked;
				$("input:checkbox").attr("checked",checked);
				if(checked){
					$(".td1").css("background-color","#A9B2CA");
				}else{
					$(".td1").css("background-color","");
				}
			}
			//单击某行时修改其背景颜色
			function changeColor(currentTr){
				var chk = $("#"+currentTr+" td:first-child>input").attr("checked");
				var tagName = event.srcElement.tagName;//事件源,因为checkbox也在此tr中
				if(chk){
						if(tagName!="INPUT"){//如果单击的是tr中的某个TD，不是checkbox
							$("#"+currentTr+" td:first-child>input").attr("checked",false);
							$("#"+currentTr+">td").css("background-color","");
						}else{
							$("#"+currentTr+">td").css("background-color","#A9B2CA");
						}	
					}else{
						if(tagName!="INPUT"){
							$("#"+currentTr+" td:first-child>input").attr("checked",true);
							$("#"+currentTr+">td").css("background-color","#A9B2CA");
						}else{
							$("#"+currentTr+">td").css("background-color","");
						}	
						
					}
				var checkobjs=document.getElementsByName("chkRadio");
				
				//var sortName;
				//var chkvalue="";
				for(var i=0;i<checkobjs.length;i++){
					var currentTr=checkobjs[i].value;
					currentTr="tr"+currentTr;
					if(checkobjs[i].checked==true){
						var sortName = checkobjs[i].parentElement.parentElement.cells[1].innerHTML;
						$("#"+currentTr+">td").css("background-color","#A9B2CA");
					}else{
						$("#"+currentTr+">td").css("background-color","");
					}
				}
			}
			
			//确定流程
			function sureWorkflow(){
				var checkobjs=document.getElementsByName("chkRadio");
				
				var workflowName;
				var workflowId;
				var flag=false;
				
				for(var i=0;i<checkobjs.length;i++){
					workflowId=checkobjs[i].value;
					if(checkobjs[i].checked==true){
						workflowName = checkobjs[i].parentElement.parentElement.cells[1].innerHTML;
						flag=true;	
						break;				
					}
				}
				if(flag){
					var parWin = window.dialogArguments;
					parWin.document.getElementById("definitionWorkflowname").value=workflowName;
					parWin.document.getElementById("definitionWorkflowid").value=workflowId;
					window.close();
			 
				}else{
					alert("请选择流程！");
					return;
				}
			}
			
			// 消除流程
			function closeWorkflow(){
				window.close();
				
			}
			
		//双击返回选中的值
		function checkWorkflow(rowIndex){
			var rownum = document.getElementById("myTable").rows;		//获取
			var workflowId=rownum[rowIndex].all("chkRadio").value;
			var workflowName=rownum[rowIndex].all("chkRadio").parentElement.parentElement.cells[1].innerHTML;
			var parWin = window.dialogArguments;
			parWin.document.getElementById("definitionWorkflowname").value=workflowName;
			parWin.document.getElementById("definitionWorkflowid").value=workflowId;
			window.close();
		}
		
		</script>
	</HEAD>
	<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
						<td height="100%">
						 <table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>相关工作流列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="sureWorkflow();"><img src="<%=root%>/images/operationbtn/preserve.png"/>&nbsp;确&nbsp;定&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="closeWorkflow();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;取&nbsp;消&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="2%"></td>
												</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
		
				<tr>
					<td valign=top >
						<table id="myTable" style="vertical-align: top;" align="left" class="table1"  cellpadding=0 cellspacing=1 width="100%" height="100%"  >
							<thead>
								<tr>
<%--									<th width="3%" height="22" class="biao_bg2"><input id="checkall" type="checkbox" name="checkall" ></th>--%>
									<!-- <th width="8%" align="center" height="22" class='biao_bg2'>流水号</th>-->
									<th width="10%" align="center" class='biao_bg2'>选择</th>
									<th width="60%" align="center" height="22" class='biao_bg2'>流程名</th>
									
								</tr>
							</thead>
							<tbody>
								<s:iterator value="workflowList" >
									 <tr id="tr<s:property value='workFlowId'/>" onclick="changeColor(this.id);" ondblclick="checkWorkflow(this.rowIndex);">
										<td width="3%" id="chkButtonTd" class="td1" style="text-indent: 0px;" align="center">
											<input type="radio"  name="chkRadio" value="${workFlowId}">
										</td>
										<!-- <td width="8%" class="td1">${flowNum }</td>-->
										<td class="td1">${workFlowName}</td>
									
									</tr>
								</s:iterator>
								<s:if test="workflowList!=null && workflowList.size()>0">
									<tr style="background-color: #e3eef2;border: thick;">
										<td colspan="7">
											&nbsp;&nbsp;
											
											<span>
												共<FONT color="red" size="4"><b><s:property value="workflowList.size()"/></b></FONT>条&nbsp;&nbsp;
												<input type="button" class="input_bg" onclick="closeWorkflow();" value="返 回">
											</span>
										</td>
									</tr>	
								</s:if>
								<s:if test="workflowList.size()==0">
									<tr style="background-color: #e3eef2;border: thick;">
										<td colspan="7" align="center">
											<span>
												<FONT color="red" size="4"><b>未找到记录</b></FONT>&nbsp;&nbsp;
												<input type="button" class="input_bg" onclick="closeWorkflow();" value="返 回">
											</span>
										</td>
									</tr>	
								</s:if>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
		</DIV>	
		<script type="text/javascript">
			var sMenu = new Menu();
	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = null;
		item = new MenuItem("<%=root%>/images/operationbtn/preserve.png","确定","sureWorkflow",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/close.png","取消","closeWorkflow",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
		sMenu.addShowType("ChangeWidthTable");
	    registerMenu(sMenu);
	}
		</script>
		
 </BODY>
</HTML>

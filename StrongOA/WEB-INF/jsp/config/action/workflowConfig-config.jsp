<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>工作流系统设置</title>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/workflow/designer/css/style.css">
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/workflow/designer/js/webTab/webtab.css">
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script language=jscript
			src="<%=path%>/workflow/designer/js/webTab/webTab.js"></script>

		<style>
body {
	background-color: buttonface;
	scroll: no;
	margin: 7px, 0px, 0px, 7px;
	border: none;
	overflow: hidden;	
}
</style>
		<SCRIPT language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></SCRIPT>
		<base target="_self" />
		<script type="text/javascript">
       function submitForm(){
       		document.forms[0].submit();
       		alert("保存成功!");
       }
       
       function cancel(){
       	  window.close();
       }
       
		//指定人员
		function selActorSetting(flag){
			var vPageLink = scriptroot + "/config/action/workflowConfig!getActorSettingTree.action?pageType=" + flag;
			var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:280px;dialogHeight:360px;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");			
		}       

	//页面加载时初始化数据
	function initWindow(){
		if("${assignerSet}" != "" && "${assignerSet}" != "null"){
			resetAssignerSet(${assignerSet});
		}
		
		if("${reAssignerSet}" != "" && "${reAssignerSet}" != "null"){
			resetReAssignerSet(${reAssignerSet});
		}
		
		if("${managerSet}" != "" && "${managerSet}" != "null"){
			resetManagerSet(${managerSet});
		}
	}
	
	function getAssignerSet(){
		return $.trim($("#assignerSet").val());
	}
	
	function getReAssignerSet(){
		return $.trim($("#reAssignerSet").val());
	}
	
	function resetAssignerSet(assignerSetting){
		var assigner = document.getElementById("assigner");
		var assignerSet = "";
		var option;
		while(assigner.options.length > 0){
			assigner.options[0] = null;
		}
		for(var i=0; i<assignerSetting.length; i++){
			assignerSet = assignerSet + "," + assignerSetting[i].alias;
			option = new Option(assignerSetting[i].name, assignerSetting[i].name);
			assigner.options.add(option, assigner.options.length);
		}
		if(assignerSet != ""){
			assignerSet = assignerSet.substring(1);
		}
		$("#assignerSet").val(assignerSet);
	}
	
	function resetReAssignerSet(reAssignerSetting){
		var reAssigner = document.getElementById("reAssigner");
		var reAssignerSet = "";
		var option;
		while(reAssigner.options.length > 0){
			reAssigner.options[0] = null;
		}		
		for(var i=0; i<reAssignerSetting.length; i++){
			reAssignerSet = reAssignerSet + "," + reAssignerSetting[i].alias;
			option = new Option(reAssignerSetting[i].name, reAssignerSetting[i].name);
			reAssigner.options.add(option, reAssigner.options.length);
		}
		if(reAssignerSet != ""){
			reAssignerSet = reAssignerSet.substring(1);
		}
		$("#reAssignerSet").val(reAssignerSet);
	}
	
	function resetManagerSet(managerSetting){
		var manager = document.getElementById("manager");
		var managerSet = "";
		var option;
		while(manager.options.length > 0){
			manager.options[0] = null;
		}		
		for(var i=0; i<managerSetting.length; i++){
			managerSet = managerSet + "," + managerSetting[i].alias;
			option = new Option(managerSetting[i].name, managerSetting[i].name);
			manager.options.add(option, manager.options.length);
		}
		if(managerSet != ""){
			managerSet = managerSet.substring(1);
		}
		$("#managerSet").val(managerSet);
	}
	
	function toConfigPage() {
		location = "<%=root%>/config/action/workflowConfig.action";
	}
</script>
	</head>
	<body onload="initWindow();" class=contentbodymargin
		oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<form id="meetform"
				action="<%=root%>/config/action/workflowConfig!saveConfig.action"
				method="POST">
				<input id="assignerSet" name="assignerSet" type="hidden" value="" />
				<input id="reAssignerSet" name="reAssignerSet" type="hidden" value="" />
				<input id="managerSet" name="managerSet" type="hidden" value="" />
				<table width="100%" class="table_headtd">
					<tr>
						<td class="table_headtd_img" >
							<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
						</td>
						<td align="left">
							<strong>系统设置</strong>
						</td>
						<td align="right">
							<table border="0" align="right" cellpadding="00"cellspacing="0">
								<tr>
			                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
				                 	<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
			                  		<td width="5"></td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
				                 	<td class="Operation_input1" onclick="toConfigPage();">&nbsp;人员选择维护&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
			                  		<td width="5"></td>
				                </tr>
							</table>
						</td>
						<td width="5">&nbsp;</td>
					</tr>
				</table>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center">
					<tr>
						<td class="biao_bg1">
							<TABLE border=0 width="100%" height="100%">
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id="tabpage2_1">处理人员配置（设置任务处理人员）</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span style="border:0px;width:99%">
														<select name="assigner" id="assigner" size="5" 
															style="width:300;margin:-1;"></select>
														</span>
														&nbsp;&nbsp;
														 <a  href="#" class="button" onclick="selActorSetting('ag')">选择</a>
														
													</TD>
													<TD></TD>
												</TR>

												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>

								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id="tabpage2_1">指派人员配置（设置任务指派人员）</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span style="border:0px;width:99%">
														<select name="reAssigner" id="reAssigner" size="5"
															style="width:300;margin:-1;"></select>
														</span>
														&nbsp;&nbsp;
														<a  href="#" class="button" onclick="selActorSetting('ra')">选择</a>
														
													</TD>
													<TD></TD>
												</TR>

												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>

								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left style="font-size:9pt;">
												&nbsp;
												<span id="tabpage2_1">管理人员配置（设置流程设计、管理、启动者）</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												style="font-size:9pt;">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<span style="border:0px;width:99%"> 
														<select name="manager" id="manager" size="5"
															style="width:300;margin:-1;" disabled="true"></select>
														</span> 
													</TD>
													<TD></TD>
												</TR>

												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>
										&nbsp;
									</TD>
								</TR>

								<TR height="100%">
									<TD></TD>
									<TD></TD>
									<TD></TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</form>
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

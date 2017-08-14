<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<html>
	<head>
		<title>设置用户类型</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
				function tree(){
					window.showModalDialog("<%=path%>/usermanage/usermanage!getOrgTree.action",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:420px');
				}
				
				function onsubmitform(){
					var sysmanagerid= '';
					var managerid='';
					var dataManagerId='';
					var   o=document.getElementsByName("sysmanager");  
					var   p=document.getElementsByName("manager"); 
					var   data=document.getElementsByName("dataManager"); 
					for (var i=0;i<o.length;i++ ){
						if(o[i].checked){ //判断复选框是否选中 
							sysmanagerid=sysmanagerid+o[i].value + ","; //值的拼凑 .. 具体处理看你的需要, 
						} 
					}
					for (var i=0;i<p.length;i++ ){
						if(p[i].checked){ //判断复选框是否选中 
							managerid=managerid+p[i].value + ","; //值的拼凑 .. 具体处理看你的需要, 
						} 
					}
					for (var i=0;i<data.length;i++ ){
						if(data[i].checked){ //判断复选框是否选中 
							dataManagerId=dataManagerId+data[i].value + ","; //值的拼凑 .. 具体处理看你的需要, 
						} 
					}
					var fid = sysmanagerid.split(",");
					var sid = managerid.split(",");
					var did = dataManagerId.split(",");
					for (var i=0 ; i<fid.length - 1 ; i++ ){
						for (var j = 0 ; j<sid.length - 1 ; j++){
							if(fid[i]==sid[j]){
								alert("同一系统中管理员只能选一个。")
								return;
							}
						}
					}
					
					for (var i=0 ; i<fid.length - 1 ; i++ ){
						for (var k = 0 ; k<did.length - 1 ; k++){{
							if(fid[i]==did[k]){
								alert("同一系统中管理员只能选一个。")
								return;
							}
						}
					}
					}
					
					for (var i=0 ; i<sid.length - 1 ; i++ ){
						for (var k = 0 ; k<did.length - 1 ; k++){{
							if(sid[i]==did[k]){
								alert("同一系统中管理员只能选一个。")
								return;
							}
						}
					}
					}
					document.getElementById("sysmanagerid").value = sysmanagerid;
					document.getElementById("managerid").value = managerid;
					document.getElementById("dataManagerId").value = dataManagerId;
					document.forms[0].submit();
					window.close();
					//输出你选中的那些复选框的值 
					//document.getElementById("orgName").value = name ;
					//document.getElementById("orgId").value = sid ;
				}
				
				function init(){
					var sysmanagerid = '${sysmanagerid}';
					var managerid = '${managerid}';
					var dataManagerId = '${dataManagerId}';
					if(sysmanagerid != ''){
					 	var id = sysmanagerid.split(',');
					 	var o=document.getElementsByName("sysmanager");  
				 		for(var i = 0; i < o.length; i++){
				 			for(var j = 0; j <id.length; j++ ){
					 			if(o[i].value == id[j]){
					 				o[i].checked=true ;
				 				}
				 			}
				 		}
					 }
					 if(managerid != ''){
					 	var id = managerid.split(',');
					 	var p=document.getElementsByName("manager");  
				 		for(var i = 0; i < p.length; i++){
				 			for(var j = 0; j <id.length; j++ ){
					 			if(p[i].value == id[j]){
					 				p[i].checked=true ;
				 				}
				 			}
				 		}
					 }
					 if(dataManagerId != ''){
					 	var id = dataManagerId.split(',');
					 	var p=document.getElementsByName("dataManager");  
				 		for(var i = 0; i < p.length; i++){
				 			for(var j = 0; j <id.length; j++ ){
					 			if(p[i].value == id[j]){
					 				p[i].checked=true ;
				 				}
				 			}
				 		}
					 }
				}
			</script>
	</head>
	<base target="_self" />
	<iframe style="width:0;height:0;display:none" id="hiddenFrame" name="hiddenFrame">
	</iframe>
	<body class=contentbodymargin oncontextmenu="return false;" onload="init();">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img">
												<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
											</td>
											<td align="left">
												<strong>设置用户类型</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
									                <tr>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									                 	<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
									                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
									                 	<td class="Operation_input1" onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
								                  		<td width="6"></td>
									                </tr>
									            </table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="center">
									<span class="wz">&nbsp;<strong>系统名称</strong></span>
								</td>
								<td width="25%" height="21" class="biao_bg1" align="center">
									<span class="wz">&nbsp;<strong>系统管理员</strong></span>
								</td>
								<td width="25%" height="21" class="biao_bg1" align="center">
									<span class="wz">&nbsp;<strong>管理员</strong></span>
								</td>
								<td width="28%" height="21" class="biao_bg1" align="center">
									<span class="wz"><strong>分级授权管理员</strong></span>
								</td>
							</tr>
							<c:forEach items="${systemList}" var="systemList" varStatus="status">
								<tr>
									<td class="td1" align="center" align="center">
										<c:out value="${systemList.sysName}" />
									</td>
									<td class="td1" align="center">
										<input type="checkbox" id="sysmanager" name="sysmanager"
											value="${systemList.sysId}">
									</td>
									<td class="td1" align="center">
										<input type="checkbox" id="manager" name="manager"
											value="${systemList.sysId}">
									</td>
									<td class="td1" align="center">
										<input type="checkbox" id="dataManager" name="dataManager"
											value="${systemList.sysId}">
									</td>
								</tr>
							</c:forEach>
						</table>
						<br>
						&nbsp;&nbsp;&nbsp;&nbsp;<font color="#999999">注:&nbsp;同一系统下只能设置一种管理员;系统管理员拥有该系统的全部资源及资源赋于功能;而管理员只<br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可将自己受权的资源赋于别人;分级授权管理员可查看系统所有数据。</font>
						<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
						<s:form id="usermanagesave" action="/usermanage/usermanage!savaSystemManagers.action" theme="simple" target="hiddenFrame">
							<input type="hidden" id="userId" name="userId" value="${userId }" />
							<input type="hidden" id="sysmanagerid" name="sysmanagerid" />
							<input type="hidden" id="managerid" name="managerid" />
							<input type="hidden" id="dataManagerId" name="dataManagerId" />
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
									</td>
								</tr>
							</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
<script type="text/javascript">
<!--
 	init();
//-->
</script>

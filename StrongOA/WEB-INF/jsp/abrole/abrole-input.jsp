<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>编辑委托规则</title>

		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<script language="javascript">
			//表单提交	
			function formSubmit(){
				var abroleSrcUserid = document.getElementById("abroleSrcUserid").value;
				if(abroleSrcUserid == null || abroleSrcUserid == ""){
					alert("请选择委托人!");
					return false;
				}
				var abroleTargetUserid = document.getElementById("abroleTargetUserid").value;
				if(abroleTargetUserid == null || abroleTargetUserid == ""){
					alert("请选择被委托人!");
					return false;
				}
				var abroleStartTime = document.getElementById("abroleStartTime").value;
				if(abroleStartTime == null || abroleStartTime == ""){
					alert("请选择开始时间!");
					return false;
				}
				var abroleEndTime = document.getElementById("abroleEndTime").value;
				if(abroleEndTime == null || abroleEndTime == ""){
					alert("请选择结束时间!");	
					return false;
				}
				var privilIds = document.getElementById("privilIds").value;
				if(privilIds == null || privilIds == ""){
					alert("请选择要委托的资源权限!");
					return false;
				}
				document.forms[0].submit();
			}
			
			//选择委托人
			function selectSrcUser(){
				var vPageLink = scriptroot + "/abrole/abrole!initSelectSrcUser.action";
				var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:320pt;dialogHeight:360pt;status:yes;help:no;scroll:no;status:0;help:0;scroll:1;");
				if(returnValue != null && returnValue != ''){
					var strid = '';
					var strtext = '';
				    var returnits= returnValue.split(",");
				    strid = returnits[0];
				    strtext= returnits[1];

					document.getElementById("abroleSrcUserid").value=strid;
					document.getElementById("abroleSrcUsername").value=strtext;
				}				
			}
			
			//选择被委托人
			function selectTargetUser(){
				var vPageLink = scriptroot + "/abrole/abrole!initSelectTargetUser.action";
				var srcUserId = document.getElementById("abroleSrcUserid").value;
				//被委托人选择
				if(srcUserId == null || srcUserId == ""){
					alert("请先选择委托人!");
					return false;
				}
				vPageLink = vPageLink + "?srcUserid=" + srcUserId;
				var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:320pt;dialogHeight:360pt;status:yes;help:no;scroll:no;status:0;help:0;scroll:1;");
				if(returnValue != null && returnValue != ''){
					var strid = '';
					var strtext = '';
				    var returnits= returnValue.split(",");
				    strid = returnits[0];
				    strtext= returnits[1];

					document.getElementById("abroleTargetUserid").value=strid;
					document.getElementById("abroleTargetUsername").value=strtext;
				}
			}
			
			//选择要委托的资源权限
			function selectPrivil(){
				var abRolePrivil = document.getElementById("privilCodes").value;
				var vPageLink = scriptroot + "/abrole/abrole!initSelectPrivil.action?abRolePrivil=" + abRolePrivil + "&abroleId=${abroleId}";
				var srcUserId = document.getElementById("abroleSrcUserid").value;
				//被委托人选择
				if(srcUserId == null || srcUserId == ""){
					alert("请先选择委托人!");
					return false;
				}
				vPageLink = vPageLink + "&srcUserid=" + srcUserId;
				var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:320pt;dialogHeight:360pt;status:no;help:no;scroll:yes;status:0;help:0;scroll:1;");
				if(returnValue != null && returnValue != ''){
				    document.getElementById("privilIds").value = returnValue;
				    var returnValues = returnValue.split("|");
				    if(returnValues[0] == null || returnValues[0] == ""){
				    	document.getElementById("privilCodes").value = returnValues[1];
				    }else if(returnValues[1] == null || returnValues[1] == ""){
				    	document.getElementById("privilCodes").value = returnValues[0];
				    }else{
				    	document.getElementById("privilCodes").value = returnValues[0] + "," + returnValues[1];
				    }
				}
			}
		</script>
		<base target="_self" />
	</head>
	<%--<iframe id="hiddenFrame" style="width:0;height:0;display:none"></iframe>--%>
	<body class=contentbodymargin oncontextmenu="return false;">
		<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
		<DIV id=contentborder align=center>
			<s:form id="mytable" action="/abrole/abrole!save.action" method="POST">
				<input type="hidden" id="abroleId" name="abroleId"
					value="${model.abroleId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td width="5%" align="center">
													<img src="<%=path%>/common/images/ico.gif" width="9"
														height="9">
												</td>
												<td width="50%">
													编辑委托规则
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td width="35%">

												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">委托人(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="abroleSrcUsername" name="model.abroleSrcUsername"
											type="text" maxLength="21" size="22"
											value="${model.abroleSrcUsername}" readOnly="true"/>
										<input type="button" class="input_bg" value="选择"
											onclick="selectSrcUser()">
										<input id="abroleSrcUserid" name="model.abroleSrcUserid"
											type="hidden" value="${model.abroleSrcUserid }"/>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">被委托人(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="abroleTargetUsername" name="model.abroleTargetUsername"
											type="text" maxLength="21" size="22"
											value="${model.abroleTargetUsername}" readOnly="true"/>
										<input type="button" class="input_bg" value="选择"
											onclick="selectTargetUser()">
										<input id="abroleTargetUserid" name="model.abroleTargetUserid"
											type="hidden" value="${model.abroleTargetUserid }"/>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">开始时间(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="abroleStartTime"
											name="model.abroleStartTime"
											dateobj="${model.abroleStartTime}"
											dateform="yyyy-MM-dd HH:mm:ss" width="133" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">结束时间(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<strong:newdate id="abroleEndTime" name="model.abroleEndTime"
											dateobj="${model.abroleEndTime}"
											dateform="yyyy-MM-dd HH:mm:ss" width="133" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">委托资源(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<%--<select id="selectedPrivil" size="4" style="width:300;display:none"></select>--%>
										
										<input type="hidden" name="privilIds" id="privilIds" value="${abRolePrivil}"/>
										<input type="hidden" name="privilCodes" id="privilCodes" value="${abRolePrivil}"/>
										<input type="button" class="input_bg" value="选择"
											onclick="selectPrivil()">
									</td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>

							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="30%">
													<input type="button" class="input_bg" value="保  存"
														onclick="formSubmit();">
												</td>
												<td width="30%">
													<input type="button" class="input_bg" value="取  消"
														onclick="javascript:window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
<script type="text/javascript">
<!--
//初始化委托的资源权限
//$(document).ready(function(){
//		if("${privilNames}" != null && "${privilNames}" != ""){
//			var privilNames = "${privilNames}".split(",");
//			var sel = document.getElementById("selectedPrivil");
//	        for(var i = 0; i < privilNames.length; i++){
//				var privilName = privilNames[i];
//				var op = new Option(privilName, privilName);
//				sel.options.add(op, sel.options.length);
//			} 
//		}    
//     });
//-->
</script>

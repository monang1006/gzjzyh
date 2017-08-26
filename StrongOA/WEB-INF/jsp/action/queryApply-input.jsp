<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>账号注册</title>
<%@include file="/common/include/meta.jsp"%>
<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
<LINK href="<%=frameroot%>/css/properties_windows_special.css"
	type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
	rel=stylesheet>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
	rel="stylesheet">
<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
<script language='javascript'
	src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<script language="javascript"
	src="<%=path%>/common/js/common/windowsadaptive.js"></script>

<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
	type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/jquery.validate.js"
	type="text/javascript"></script>
<script src="<%=path%>/common/js/validate/formvalidate.js"
	type="text/javascript"></script>
<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
	type="text/javascript"></script>
<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
	language="javascript"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<script type="text/javascript">

	 
	function checkLoginName(obj,flag){
		var isValidated = false;
			 $.ajax({
				url:obj.url,
				type:"post",
				async:false,
				data:{loginname:obj.value},
				success:function(logininfo){
					if(logininfo!="111"){
						alert("登录名不唯一。");
						obj.focus();
					}
					else{
						isValidated = true;
					}						
				},
				error:function(logininfo){
					alert("异步出错。");
				}					
			});
		return isValidated;
	}
			
	//校验手机号码
function checkMobile(mobile){
	var flag = true;
	var reg_mobile13= /^(13\d{9})$/;
	var reg_mobile159=/^(15\d{9})$/;
	var reg_mobile189=/^(18\d{9})$/;
	if(mobile!=""){
		if(!reg_mobile13.test(mobile) && !reg_mobile159.test(mobile) && !reg_mobile189.test(mobile)){
			flag = false;
		}
	}
	return flag;
}	
	
function formsubmit(){
	document.getElementById("applySave").submit();
}

function impForm(accountStr, accId) {
	alert(accountStr);
	document.getElementById(accId).value = accountStr;
}

function accountImp(attrId){
	
	var width=screen.availWidth/4;
 	var height=screen.availHeight/4;
	var result=window.showModalDialog("<%=path%>/action/queryApply!imp.action?attrId="+ attrId, window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"
					+ "status:no;help:no;scroll:no;");

}


function change(){
	var width=screen.availWidth/2;
 	var height=screen.availHeight/2;
	var result=window.showModalDialog("<%=path%>/action/queryApply!casePage.action",window,	"dialogWidth:"
														+ width
														+ "pt;dialogHeight:"
														+ height
														+ "pt;"
														+ "status:no;help:no;scroll:no;");
}

function changeCaseF(caseCode,caseName,caseOrg,caseConfirmTime){
	document.getElementById("caseCode").value=caseCode;
	document.getElementById("caseName").value=caseName;
	document.getElementById("caseOrg").value=caseOrg;
	document.getElementById("caseConfirmTime").value=caseConfirmTime;
}

function disploy(){
	$("#tab_1").css("display","block");
}

$(function(){ 
	var appType = $("#appType").val();
	if(appType==0){
		$("#tr_1").css("display","block");
		$("#tr_2").css("display","block");
		$("#tr_3").css("display","none");
		$("#tr_4").css("display","none");
		$("#tr_5").css("display","none");
	}
	if(appType==1){
		$("#tr_1").css("display","none");
		$("#tr_2").css("display","none");
		$("#tr_3").css("display","block");
		$("#tr_4").css("display","none");
		$("#tr_5").css("display","none");
	}
	if(appType==2){
		$("#tr_1").css("display","none");
		$("#tr_2").css("display","none");
		$("#tr_3").css("display","none");
		$("#tr_4").css("display","block");
		$("#tr_5").css("display","none");
	}
	if(appType==3){
		$("#tr_1").css("display","none");
		$("#tr_2").css("display","none");
		$("#tr_3").css("display","none");
		$("#tr_4").css("display","none");
		$("#tr_5").css("display","block");
	}
	
});

function applyChange(){
	var appType = $("#appType").val();
	if(appType==0){
		$("#tr_1").css("display","block");
		$("#tr_2").css("display","block");
		$("#tr_3").css("display","none");
		$("#tr_4").css("display","none");
		$("#tr_5").css("display","none");
	}
	if(appType==1){
		$("#tr_1").css("display","none");
		$("#tr_2").css("display","none");
		$("#tr_3").css("display","block");
		$("#tr_4").css("display","none");
		$("#tr_5").css("display","none");
	}
	if(appType==2){
		$("#tr_1").css("display","none");
		$("#tr_2").css("display","none");
		$("#tr_3").css("display","none");
		$("#tr_4").css("display","block");
		$("#tr_5").css("display","none");
	}
	if(appType==3){
		$("#tr_1").css("display","none");
		$("#tr_2").css("display","none");
		$("#tr_3").css("display","none");
		$("#tr_4").css("display","none");
		$("#tr_5").css("display","block");
	}
	
}

function openImageUpload(domElementId, windowTitle, nowImageUrl){
	if(document.getElementById(domElementId).value != "" && document.getElementById(domElementId).value != null){
		nowImageUrl = document.getElementById(domElementId).value;
	}
	var imageUrl = window.showModalDialog("<%=path%>/policeregister/policeRegister!imageUpload.action?imageUrl="+nowImageUrl,window,'help:no;status:no;scroll:no;dialogWidth:1200px; dialogHeight:800px');
	if(imageUrl != null && imageUrl != ""){
		document.getElementById(domElementId).value = imageUrl;
		document.getElementById(domElementId + "Tmp").src = "<%=path%>" + imageUrl;
	}
	//window.open("<%=path%>/policeregister/policeRegister!imageUpload.action?domElementId="+domElementId+"&flag="+(new Date())+"&imageUrl="+nowImageUrl, "", "width=1200,height=800,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no");
}
</script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=center>
		<s:form id="applySave" target="hiddenFrame" enctype="multipart/form-data"
						action="/action/queryApply!save.action"  theme="simple" >
			<input type="hidden" id="appId" name="model.gzjzyhApplication.appId"
				value="${model.gzjzyhApplication.appId}">
			<input type="hidden" id="caseId"
				name="model.gzjzyhCase.caseId"
				value="${model.gzjzyhCase.caseId}">
			<input type="hidden" id="ueId" name="model.gzjzyhUserExtension.ueId"
				value="${model.gzjzyhUserExtension.ueId}">
				
			<input type="hidden" id="appAuditUserId" name="model.gzjzyhApplication.appAuditUserId"
				value="${model.gzjzyhApplication.appAuditUserId}">
			<input type="hidden" id="appReceiverId" name="model.gzjzyhApplication.appReceiverId"
				value="${model.gzjzyhApplication.appReceiverId}">
				
			
			<input type="hidden" id="appLawfile" name="model.gzjzyhApplication.appLawfile"
				value="${model.gzjzyhApplication.appLawfile}">
			<input type="hidden" id="appLawfileR" name="model.gzjzyhApplication.appLawfileR"
				value="${model.gzjzyhApplication.appLawfileR}">
			<input type="hidden" id="appAttachment" name="model.gzjzyhApplication.appAttachment"
				value="${model.gzjzyhApplication.appAttachment}">			
								
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>查询请求</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="formsubmit();">&nbsp;保&nbsp;存&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()()">&nbsp;关&nbsp;闭&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table> 
					
					<table width="100%" height="10%" border="0" cellpadding="0"
						cellspacing="0" align="center" class="table1">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;案件编号：</span>
							</td>
							<td class="td1" align="left" width="40%"><input
								id="caseCode"
								name="model.gzjzyhCase.caseCode" type="text" size="44" maxLength="50"
								value="${model.gzjzyhCase.caseCode}">&nbsp;<a href="#" class="button"
								onclick="change()">选择案件</a></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;案件名称：</span>
							</td>
							<td class="td1" align="left"><input
								id="caseName" name="model.gzjzyhCase.caseName" type="text"
								size="44" value="${model.gzjzyhCase.caseName}">&nbsp;
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;立案时间：</span>
							</td>
							<td class="td1" align="left"><strong:newdate
									id="caseConfirmTime"
									name="model.gzjzyhCase.caseConfirmTime"
									width="155"  dateform="yyyy-MM-dd HH:mm:ss" isicon="true"
									dateobj="${model.gzjzyhCase.caseConfirmTime}"
									classtyle="search" title="搜索结束时间" /></td>
									
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;经办单位：</span>
							</td>
							<td class="td1" align="left">
								<input	id="caseOrg" name="model.gzjzyhCase.caseOrg" type="text" size="44"
								value="${model.gzjzyhCase.caseOrg}">&nbsp;
							</td>
						</tr>
						
						
						<tr>
							
							
							
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">文书号：</span>
							</td>
							<td class="td1" align="left"><input
								id="appFileno" name="model.gzjzyhApplication.appFileno" type="text" size="44"
								value="${model.gzjzyhApplication.appFileno}"></td>
								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">查询申请类型：</span>
							</td>
							<td class="td1" align="left"><s:select									
									name="model.gzjzyhApplication.appType" id="appType"
									onclick="applyChange();"
									list="#{'0':'查询申请','1':'冻解申请','2':'续冻申请','3':'解冻申请'}"
									listKey="key" listValue="value" /></td>	
							
						</tr>
						<tr>				
							<td colspan="4" class="td1" align="center" style="height:20px;">
							</td>
						</tr>
						<tr>						
							<td colspan="4" class="td1" align="center">
								<table style="width:100%;">
									<tr>
										<td align="center">
											<img id="ueMainNo1Tmp" onclick="openImageUpload('lawDocTmp', '法律文书', '${model.gzjzyhApplication.appLawfile}')" src="<%=path %>${lawDocTmp}" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('lawDocTmp', '法律文书', '${lawDocTmp }')">法律文书</a></div>
										</td>
										<td align="center">
											<img id="ueMainNo2Tmp" onclick="openImageUpload('ueMainNo2', '法律文书回执', '${lawRecTmp }')" src="<%=path %>${lawRecTmp}" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('lawRecTmp', '法律文书回执', '${lawRecTmp }')">法律文书回执</a></div>
										</td>
										<td align="center">
											<img id="ueHelpNo1Tmp" onclick="openImageUpload('ueHelpNo1', '其它附件', '${otherTmp }')" src="<%=path %>${otherTmp}" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('otherTmp', '其它附件', '${otherTmp }')">其它附件</a></div>
										</td>

									</tr>
									
								</table>
							
							</td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
			<!-- 涉案对象 -->
			<tr id="tr_1">
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>涉案对象</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						
						<tr>
							<td height="21" class="biao_bg1_gz" align="right" width="200">
								<span class="wz"><font color="red">*</font>&nbsp;单位账号(证件号码)：</span>
							</td>
							<td class="td1" align="left" width="40%"><textarea rows="6"
									id="appOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="model.gzjzyhApplication.appOrgAccount">${model.gzjzyhApplication.appOrgAccount}</textarea>
									<span style="width:50px;margin-bottom: 5px">
								 		<a	href="#" class="button" onclick="accountImp('appOrgAccount')">导入</a>
								 		<a	href="#" class="button" onclick="accountClear()">清空</a>
									</span>
							</td>									
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;个人账号(证件号码)：</span>
							</td>
							<td class="td1" align="left"><textarea rows="6"
									id="appPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="model.gzjzyhApplication.appPersonAccount">${model.gzjzyhApplication.appPersonAccount}</textarea>
									<span style="width:50px;margin-bottom: 5px">
								 		<a	href="#" class="button" onclick="accountImp('appPersonAccount')">导入</a>
								 		<a	href="#" class="button" onclick="accountClear()">清空</a>
									</span>
							</td>		
						</tr>				
						
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
			<!-- 涉案账号 -->
			<tr id="tr_2">
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>涉案账号</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;单位开户明细(待查账号)：</span>
							</td>
							<td class="td1" align="left" width="40%"><textarea
									rows="6" id="appOrgDetail" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="model.gzjzyhApplication.appOrgDetail">${model.gzjzyhApplication.appOrgDetail}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('appOrgDetail')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;个人开户明细(证件号码)：</span>
							</td>
							<td class="td1" align="left"><textarea
									rows="6" id="appPersonDetail" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="model.gzjzyhApplication.appPersonDetail">${model.gzjzyhApplication.appPersonDetail}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('appPersonDetail')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>
							</td>
						</tr>				
						
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;交易明细(待查账号)：</span>
							</td>
							<td class="td1" align="left" width="40%"><textarea
									rows="6" id="appChadeDetail" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="model.gzjzyhApplication.appChadeDetail">${model.gzjzyhApplication.appChadeDetail}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('appChadeDetail')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>
							</td>
						</tr>
						
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;查询时间：</span>
							</td>
							<td colspan="3" align="left">
								<input name="timeSign"
								type="radio" value="0" />&nbsp;开启之日启至今
								
								<input name="timeSign"
								type="radio" value="1" />&nbsp;近一年
								
								<input name="timeSign"
								type="radio" value="2" />&nbsp;自定义
								
								<strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									skin="whyGreen" isicon="true" width="155"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
								&nbsp;&nbsp;至<strong:newdate width="155"
									name="model.gzjzyhApplication.appEndDate" id="appEndDate"
									skin="whyGreen" isicon="true"
									dateobj="${model.gzjzyhApplication.appEndDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
							</td>
					
								
						
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
			<!--冻解申请  -->			
			<tr id="tr_3" style="display: none">
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>线索查询</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;单位账号：</span>
							</td>
							<td class="td1" align="left" width="40%"><textarea
									rows="6" id="frozenAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="frozenAppOrgAccount">${frozenAppOrgAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('frozenAppOrgAccount')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;个人账号：</span>
							</td>
							<td class="td1" align="left"><textarea
									rows="6" id="frozenAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="frozenAppPersonAccount">${frozenAppPersonAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('frozenAppPersonAccount')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>
							</td>
						</tr>				
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;冻解时间：</span>
							</td>
							<td colspan="3" align="left">							
								<strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									skin="whyGreen" isicon="true" width="155"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
								&nbsp;&nbsp;至<strong:newdate width="155"
									name="model.gzjzyhApplication.appEndDate" id="appEndDate"
									skin="whyGreen" isicon="true"
									dateobj="${model.gzjzyhApplication.appEndDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
							</td>
					
								
						
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
		<!--续冻申请  -->			
			<tr id="tr_4" style="display: none">
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>线索查询</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;单位账号：</span>
							</td>
							<td class="td1" align="left" width="40%"><textarea
									rows="6" id="continueAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="continueAppOrgAccount">${continueAppOrgAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('continueAppOrgAccount')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;个人账号：</span>
							</td>
							<td class="td1" align="left"><textarea
									rows="6" id="continueAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="continueAppPersonAccount">${continueAppPersonAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('continueAppPersonAccount')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>
							</td>
						</tr>				
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;冻解时间：</span>
							</td>
							<td colspan="3" align="left">							
								<strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									skin="whyGreen" isicon="true" width="155"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
								&nbsp;&nbsp;至<strong:newdate width="155"
									name="model.gzjzyhApplication.appEndDate" id="appEndDate"
									skin="whyGreen" isicon="true"
									dateobj="${model.gzjzyhApplication.appEndDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
							</td>
					
								
						
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
			<!--解冻申请  -->			
			<tr id="tr_5" style="display: none">
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>线索查询</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;单位账号：</span>
							</td>
							<td class="td1" align="left" width="40%"><textarea
									rows="6" id="thawAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="thawAppOrgAccount">${thawAppOrgAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('thawAppOrgAccount')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;个人账号：</span>
							</td>
							<td class="td1" align="left"><textarea
									rows="6" id="thawAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="thawAppPersonAccount">${thawAppPersonAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								 	<a	href="#" class="button" onclick="accountImp('thawAppPersonAccount')">导入</a>
								 	<a	href="#" class="button" onclick="accountClear()">清空</a>
								</span>
							</td>
						</tr>				
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;冻解时间：</span>
							</td>
							<td colspan="3" align="left">							
								<strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									skin="whyGreen" isicon="true" width="155"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
							</td>
					
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
			<!--警官信息  -->
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>警官信息</strong></td>
										
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="disploy();">&nbsp;展&nbsp;开&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
												</tr>
											</table>
										</td>
										
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table id="tab_1" width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1" style="display: none">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><strong>主办警官：</strong></span>
							</td>
							<td class="td1" align="left" width="40%"></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><strong>协办警官：</strong></span>
							</td>
							<td class="td1" align="left"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left" width="40%"><input
								id="ueMainName"
								name="model.gzjzyhUserExtension.ueMainName" type="text" size="44" maxLength="50"
								value="${model.gzjzyhUserExtension.ueMainName}"></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left"><input
								id="ueHelpName" name="model.gzjzyhUserExtension.ueHelpName" type="text"
								size="44" maxLength="50" value="${model.gzjzyhUserExtension.ueHelpName}"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left" width="40%"><input
								id="ueMainNo" name="model.gzjzyhUserExtension.ueMainNo" type="text" maxLength="50"
								size="44" value="${model.gzjzyhUserExtension.ueMainNo}"></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left">
								<input
								id="ueHelpNo" name="model.gzjzyhUserExtension.ueHelpNo" type="text" size="44"
								maxLength="50" value="${model.gzjzyhUserExtension.ueHelpNo}">
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left" width="40%"><input id="ueMainId"
								name="model.gzjzyhUserExtension.ueMainId" type="text" size="44"
								value="${model.gzjzyhUserExtension.ueMainId}"></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left"><input
								id="ueHelpId" name="model.gzjzyhUserExtension.ueHelpId" type="text" size="44" value="${model.gzjzyhUserExtension.ueHelpId}"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left" width="40%"><input id="ueMainMobile"
								name="model.gzjzyhUserExtension.ueMainMobile" type="text" size="44"
								value="${model.gzjzyhUserExtension.ueMainMobile}"></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left"><input
								id="ueHelpMobile" name="model.gzjzyhUserExtension.ueHelpMobile" type="text" size="44" value="${model.gzjzyhUserExtension.ueHelpMobile}"></td>
						</tr>
						<tr>
							<td colspan="4" class="td1" align="center" style="height:20px;">
							</td>
						</tr>
						<tr>
							<!-- <td colspan="2" class="td1" align="center"> -->
							<td colspan="4" class="td1" align="center">
								<table style="width:100%;">
									<tr>
										<td align="center">
											<img id="ueMainNo1Tmp" onclick="openImageUpload('ueMainNo1', '主办警官警官证（正）', '${ueMainNo1Tmp }')" src="<%=path %>${ueMainNo1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainNo1', '主办警官警官证（正）', '${ueMainNo1Tmp }')">警官证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueMainNo2Tmp" onclick="openImageUpload('ueMainNo2', '主办警官警官证（反）', '${ueMainNo2Tmp }')" src="<%=path %>${ueMainNo2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainNo2', '主办警官警官证（反）', '${ueMainNo2Tmp }')">警官证（反）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpNo1Tmp" onclick="openImageUpload('ueHelpNo1', '协办警官警官证（正）', '${ueHelpNo1Tmp }')" src="<%=path %>${ueHelpNo1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpNo1', '协办警官警官证（正）', '${ueHelpNo1Tmp }')">警官证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpNo2Tmp" onclick="openImageUpload('ueHelpNo2', '协办警官警官证（反）', '${ueHelpNo2Tmp }')" src="<%=path %>${ueHelpNo2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpNo2', '协办警官警官证（反）', '${ueHelpNo2Tmp }')">警官证（反）</a></div>
										</td>
									</tr>
									<tr>
										<td align="center">
											<img id="ueMainId1Tmp" onclick="openImageUpload('ueMainId1', '主办警官身份证（正）', '${ueMainId1Tmp }')" src="<%=path %>${ueMainId1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainId1', '主办警官身份证（正）', '${ueMainId1Tmp }')">身份证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueMainId2Tmp" onclick="openImageUpload('ueMainId2', '主办警官身份证（反）', '${ueMainId2Tmp }')" src="<%=path %>${ueMainId2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainId2', '主办警官身份证（反）', '${ueMainId2Tmp }')">身份证（反）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpId1Tmp" onclick="openImageUpload('ueHelpId1', '协办警官身份证（正）', '${ueHelpId1Tmp }')" src="<%=path %>${ueHelpId1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpId1', '协办警官身份证（正）', '${ueHelpId1Tmp }')">身份证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpId2Tmp" onclick="openImageUpload('ueHelpId2', '协办警官身份证（反）', '${ueHelpId2Tmp }')" src="<%=path %>${ueHelpId2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpId2', '协办警官身份证（反）', '${ueHelpId2Tmp }')">身份证（反）</a></div>
										</td>
									</tr>
								</table>
							<!-- </td>
							<td class="td1" colspan="2" align="center"> -->
							</td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
		</table>
		</s:form>
	</DIV>
</body>
<iframe id="hiddenFrame" name="hiddenFrame" style="width:0px;height:0px;display:none;"></iframe>
</html>
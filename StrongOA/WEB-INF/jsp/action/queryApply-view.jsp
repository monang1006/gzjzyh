<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>查看申请</title>
<%@include file="/common/include/meta.jsp"%>
<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
<LINK href="<%=frameroot%>/css/properties_windows_special.css"
	type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
	rel=stylesheet>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
	rel="stylesheet">
<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
	type="text/javascript"></script>
<script language='javascript'
	src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<script language="javascript"
	src="<%=path%>/common/js/common/windowsadaptive.js"></script>

<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
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

function display(){
	$(".displayFlag").hide();
	$(".unDisplayFlag").show();
	$("#tab_1").css("display","block");
}

function unDisplay(){
	$(".unDisplayFlag").hide();
	$(".displayFlag").show();
	$("#tab_1").css("display","none");
}

$(function(){ 
	var appType = "${model.gzjzyhApplication.appType}";
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
	
	$(".unDisplayFlag").hide();
	
});

function viewImage(url){
	window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/viewimage/viewImage.jsp?imageUrl="+url,window,'help:no;status:no;scroll:no;dialogWidth:1200px; dialogHeight:800px');
}

function downloadAttachment(){
	document.getElementById("applySave").submit();
}
</script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=center>
		<s:form id="applySave" target="hiddenFrame" action="/action/queryApply!downloadAttachment.action"  theme="simple" >
		<input type="hidden" id="appResponsefile" name="appResponsefile" value="${model.gzjzyhApplication.appResponsefile}" />
		</s:form>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<!-- 处理状态 -->
			<s:if test="#request.model.gzjzyhApplication.appStatus==2 || #request.model.gzjzyhApplication.appStatus==3 || #request.model.gzjzyhApplication.appStatus==4 || #request.model.gzjzyhApplication.appStatus==5 || #request.model.gzjzyhApplication.appStatus==6">
			<tr>
				<td height="100%">
					<!-- 处理状态标题 -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>处理状态</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
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
					<!-- 处理状态信息内容 -->
					<table width="100%" height="10%" border="0" cellpadding="0"
						cellspacing="0" align="center" class="table1">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;当前状态：</span>
							</td>
							<td class="td1" align="left" width="35%">
								<s:if test="#request.model.gzjzyhApplication.appStatus==2 || #request.model.gzjzyhApplication.appStatus==4">
									已审核
								</s:if>
								<s:if test="#request.model.gzjzyhApplication.appStatus==3">
									已驳回
								</s:if>
								<s:if test="#request.model.gzjzyhApplication.appStatus==5">
									已处理
								</s:if>
								<s:if test="#request.model.gzjzyhApplication.appStatus==6">
									已拒签
								</s:if>
							</td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;处理人员：</span>
							</td>
							<td class="td1" align="left">
								<s:if test="#request.model.gzjzyhApplication.appStatus==2 || #request.model.gzjzyhApplication.appStatus==3 || #request.model.gzjzyhApplication.appStatus==4">
									${model.gzjzyhApplication.appAuditUser }
								</s:if>
								<s:if test="#request.model.gzjzyhApplication.appStatus==5">
									${model.gzjzyhApplication.appResponser }
								</s:if>
								<s:if test="#request.model.gzjzyhApplication.appStatus==6">
									${model.gzjzyhApplication.appReceiver }
								</s:if>
							</td>
						</tr>
						<s:if test="#request.model.gzjzyhApplication.appStatus==3 || #request.model.gzjzyhApplication.appStatus==6">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;处理意见：</span>
							</td>
							<td class="td1" align="left">
								${model.gzjzyhApplication.appNgReason }
							</td>
							<td height="21" class="biao_bg1_gz" align="right">
							</td>
							<td class="td1" align="left">
							</td>
						</tr>
						</s:if>
						<s:if test="#request.model.gzjzyhApplication.appStatus==5 && #request.model.gzjzyhApplication.appResponsefile != null && #request.model.gzjzyhApplication.appResponsefile != ''">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;反馈附件：</span>
							</td>
							<td class="td1" align="left">
								<a href="javascript:void(0);" class="button" onclick="downloadAttachment()">下载附件</a>
							</td>
							<td height="21" class="biao_bg1_gz" align="right">
							</td>
							<td class="td1" align="left">
							</td>
						</tr>
						</s:if>
					</table>
				</td>
			</tr>
			</s:if>
			<!-- 案件信息 -->
			<tr>
				<td height="100%">
					<!-- 案件信息标题 -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>案件信息</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<s:if test="#request.model.gzjzyhApplication.appStatus==0 || #request.model.gzjzyhApplication.appStatus==1">
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													</s:if>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table> 
					<!-- 案件信息内容 -->
					<table width="100%" height="10%" border="0" cellpadding="0"
						cellspacing="0" align="center" class="table1">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;案件编号：</span>
							</td>
							<td class="td1" align="left" width="35%">
								${model.gzjzyhCase.caseCode}
							</td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;案件名称：</span>
							</td>
							<td class="td1" align="left">
								${model.gzjzyhCase.caseName}
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;立案时间：</span>
							</td>
							<td class="td1" align="left" colspan="3">
								<s:date name="#request.model.gzjzyhCase.caseConfirmTime" format="yyyy-MM-dd HH:mm:ss" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- 申请信息 -->
			<tr>
				<td height="100%">
					<!-- 申请信息标题 -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>申请信息</strong></td>
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
					<!-- 申请信息内容 -->
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;请求银行：</span>
							</td>
							<td class="td1" align="left" width="35%">
								${bankUserName }
							</td>
								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;申请类型：</span>
							</td>
							<td class="td1" align="left">
								${appTypeName }
							</td>	
							
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;文书号：</span>
							</td>
							<td class="td1" align="left" width="35%">
								${model.gzjzyhApplication.appFileno}
							</td>
								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;申请时间：</span>
							</td>
							<td class="td1" align="left">
								<s:date name="#request.model.gzjzyhApplication.appDate" format="yyyy-MM-dd HH:mm:ss" />
							</td>	
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;经办单位：</span>
							</td>
							<td class="td1" align="left" width="35%">
								${model.gzjzyhApplication.appOrg}
							</td>
								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;联系地址：</span>
							</td>
							<td class="td1" align="left">
								${model.gzjzyhApplication.appAddress}
							</td>	
						</tr>
						<tr>						
							<td height="20px">
							</td>
						</tr>
						<tr>						
							<td colspan="4" class="td1" align="center">
								<table style="width:100%;">
									<tr>
										<td align="center">
											<img id="appLawfileTmp" onclick="viewImage('${appLawfileTmp }')" src="<%=path %>${appLawfileTmp}" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">法律文书</div>
										</td>
										<td align="center">
											<img id="appLawfileRTmp" onclick="viewImage('${appLawfileRTmp }')" src="<%=path %>${appLawfileRTmp}" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">法律文书回执</div>
										</td>
										<td align="center">
											<img id="appAttachmentTmp" onclick="viewImage('${appAttachmentTmp }')" src="<%=path %>${appAttachmentTmp}" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">其它附件</div>
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
								<span class="wz">单位账号（证件号码）：</span>
							</td>
							<td class="td1" align="left" width="35%"><textarea readOnly="readonly" rows="6"
									id="searchAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="searchAppOrgAccount">${searchAppOrgAccount}</textarea>
									<span style="width:50px;margin-bottom: 5px">
									</span>
							</td>									
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">个人账号（证件号码）：</span>
							</td>
							<td class="td1" align="left"><textarea rows="6" readOnly="readonly"
									id="searchAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="searchAppPersonAccount">${searchAppPersonAccount}</textarea>
									<span style="width:50px;margin-bottom: 5px">
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
								<span class="wz">单位开户明细（待查账号）：</span>
							</td>
							<td class="td1" align="left" width="35%"><textarea readOnly="readonly"
									rows="6" id="searchAppOrgDetail" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="searchAppOrgDetail">${searchAppOrgDetail}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">个人开户明细（证件号码）：</span>
							</td>
							<td class="td1" align="left"><textarea readOnly="readonly"
									rows="6" id="searchAppPersonDetail" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="searchAppPersonDetail">${searchAppPersonDetail}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>
							</td>
						</tr>				
						
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">交易明细（待查账号）：</span>
							</td>
							<td class="td1" align="left" width="35%"><textarea readOnly="readonly"
									rows="6" id="searchAppChadeDetail" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="searchAppChadeDetail">${searchAppChadeDetail}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>
							</td>
						</tr>
						
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;查询时间：</span>
							</td>
							<td colspan="3" align="left">
								${appDateDesc }
							</td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			
			<!--冻结申请  -->			
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
										<td align="left" width="140"><strong>查询线索</strong></td>
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
								<span class="wz">单位账号：</span>
							</td>
							<td class="td1" align="left" width="35%"><textarea readOnly="readonly"
									rows="6" id="frozenAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="frozenAppOrgAccount">${frozenAppOrgAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">个人账号：</span>
							</td>
							<td class="td1" align="left"><textarea readOnly="readonly"
									rows="6" id="frozenAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="frozenAppPersonAccount">${frozenAppPersonAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>
							</td>
						</tr>				
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;冻结时间：</span>
							</td>
							<td colspan="3" align="left">	
								<s:date name="#request.frozenAppStartDate" format="yyyy-MM-dd" />
								&nbsp;&nbsp;至&nbsp;&nbsp;
								<s:date name="#request.frozenAppEndDate" format="yyyy-MM-dd" />
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
										<td align="left" width="140"><strong>查询线索</strong></td>
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
								<span class="wz">单位账号：</span>
							</td>
							<td class="td1" align="left" width="35%"><textarea readOnly="readonly"
									rows="6" id="continueAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="continueAppOrgAccount">${continueAppOrgAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">个人账号：</span>
							</td>
							<td class="td1" align="left"><textarea readOnly="readonly"
									rows="6" id="continueAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="continueAppPersonAccount">${continueAppPersonAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>
							</td>
						</tr>				
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;续冻时间：</span>
							</td>
							<td colspan="3" align="left">
								<s:date name="#request.continueAppStartDate" format="yyyy-MM-dd" />
								&nbsp;&nbsp;至&nbsp;&nbsp;
								<s:date name="#request.continueAppEndDate" format="yyyy-MM-dd" />							
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
										<td align="left" width="140"><strong>查询线索</strong></td>
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
								<span class="wz">单位账号：</span>
							</td>
							<td class="td1" align="left" width="35%"><textarea readOnly="readonly"
									rows="6" id="thawAppOrgAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="thawAppOrgAccount">${thawAppOrgAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>	
							</td>	
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">个人账号：</span>
							</td>
							<td class="td1" align="left"><textarea readOnly="readonly"
									rows="6" id="thawAppPersonAccount" style="width: 290px;height: 150px;margin-top: 5px;margin-bottom: 5px"
									name="thawAppPersonAccount">${thawAppPersonAccount}</textarea>
								 <span style="width:50px;margin-bottom: 5px">
								</span>
							</td>
						</tr>				
						<tr>								
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;冻结时间：</span>
							</td>
							<td colspan="3" align="left">
								<s:date name="#request.thawAppStartDate" format="yyyy-MM-dd" />					
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
													<td width="7" class="displayFlag"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input displayFlag" onclick="display();">&nbsp;展&nbsp;开&nbsp;</td>
													<td class="displayFlag" width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td class="displayFlag" width="5"></td>
													<td class="unDisplayFlag" width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1 unDisplayFlag" onclick="unDisplay()">&nbsp;收&nbsp;起&nbsp;</td>
													<td class="unDisplayFlag" width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													<td class="unDisplayFlag" width="6"></td>
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
							<td class="td1" align="left" width="35%"></td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><strong>协办警官：</strong></span>
							</td>
							<td class="td1" align="left"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left" width="35%">${model.gzjzyhUserExtension.ueMainName}</td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left">${model.gzjzyhUserExtension.ueHelpName}</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left" width="35%">${model.gzjzyhUserExtension.ueMainNo}</td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left">
								${model.gzjzyhUserExtension.ueHelpNo}
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left" width="35%">${model.gzjzyhUserExtension.ueMainId}</td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left">${model.gzjzyhUserExtension.ueHelpId}</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left" width="35%">${model.gzjzyhUserExtension.ueMainMobile}</td>
							<td height="21" class="biao_bg1_gz" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left">${model.gzjzyhUserExtension.ueHelpMobile}</td>
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
											<img onclick="viewImage('${ueMainNo1Tmp }')" id="ueMainNo1Tmp" src="<%=path %>${ueMainNo1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">警官证（正）</div>
										</td>
										<td align="center">
											<img onclick="viewImage('${ueMainNo2Tmp }')" id="ueMainNo2Tmp" src="<%=path %>${ueMainNo2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">警官证（反）</div>
										</td>
										<td align="center">
											<img onclick="viewImage('${ueHelpNo1Tmp }')" id="ueHelpNo1Tmp" src="<%=path %>${ueHelpNo1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">警官证（正）</div>
										</td>
										<td align="center">
											<img onclick="viewImage('${ueHelpNo2Tmp }')" id="ueHelpNo2Tmp" src="<%=path %>${ueHelpNo2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">警官证（反）</div>
										</td>
									</tr>
									<tr>
										<td align="center">
											<img onclick="viewImage('${ueMainId1Tmp }')" id="ueMainId1Tmp" src="<%=path %>${ueMainId1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">身份证（正）</div>
										</td>
										<td align="center">
											<img onclick="viewImage('${ueMainId2Tmp }')" id="ueMainId2Tmp" src="<%=path %>${ueMainId2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">身份证（反）</div>
										</td>
										<td align="center">
											<img onclick="viewImage('${ueHelpId1Tmp }')" id="ueHelpId1Tmp" src="<%=path %>${ueHelpId1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">身份证（正）</div>
										</td>
										<td align="center">
											<img onclick="viewImage('${ueHelpId2Tmp }')" id="ueHelpId2Tmp" src="<%=path %>${ueHelpId2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;">身份证（反）</div>
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
	</DIV>
</body>
<iframe id="hiddenFrame" name="hiddenFrame" style="width:0px;height:0px;display:none;"></iframe>
</html>
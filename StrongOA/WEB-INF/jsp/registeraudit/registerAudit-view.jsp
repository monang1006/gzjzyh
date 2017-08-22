<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>账号审核</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>
<script type="text/javascript" language="javascript"
	src="<%=root%>/uums/js/md5.js"></script>
<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css
	rel=stylesheet>

<script type="text/javascript">
				
</script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>审核状态</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;重&nbsp;置&nbsp;</td>
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
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">审核状态：</span>
							</td>
							<td class="td1" align="left" width="40%">
								<script>
									var displayStatus = "";
									if("${model.ueStatus}" == "0"){
										displayStatus = "待审核";
									}else if("${model.ueStatus}" == "1"){
										displayStatus = "审核通过";
									}else if("${model.ueStatus}" == "2"){
										displayStatus = "已退回";
									}
									document.write(displayStatus);
								</script>
							</td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">审核人员：</span>
							</td>
							<td class="td1" align="left">
								<script>
									if("${model.ueStatus}" == "1" || "${model.ueStatus}" == "2"){
										document.write("${model.ueAuditUser}");
									}
								</script>
							</td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">退回意见：</span>
							</td>
							<td class="td1" align="left" colspan="3">
								<script>
									if("${model.ueStatus}" == "2"){
										document.write("${model.ueNgReason}");
									}
								</script>
							</td>
						</tr>
						<td class="table1_td"></td>
						<td></td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>账号信息</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;重&nbsp;置&nbsp;</td>
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
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;登录账号：</span>
								</td>
								<td class="td1" align="left" width="40%">${model.tuumsBaseUser.userLoginname}</td>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;登录密码：</span>
								</td>
								<td class="td1" align="left">${model.tuumsBaseUser.userPassword}</td>
							</tr>
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;用户姓名：</span>
								</td>
								<td class="td1" align="left"${model.tuumsBaseUser.userName}</td>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;所属单位：</span>
								</td>
								<td class="td1" align="left">${orgName}</td>
							</tr>
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz">手机号码：</span>
								</td>
								<td class="td1" align="left">${model.tuumsBaseUser.rest2}</td>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz">办公电话：</span>
								</td>
								<td class="td1" align="left">${model.tuumsBaseUser.userTel}</td>
							</tr>
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz">联系地址：</span>
								</td>
								<td class="td1" colspan="3" align="left">${model.tuumsBaseUser.userAddr}</td>
							</tr>
							<td class="table1_td"></td>
							<td></td>
							</tr>
						</table>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
				</td>
			</tr>
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>警官信息</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;重&nbsp;置&nbsp;</td>
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
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><strong>主办警官：</strong></span>
							</td>
							<td class="td1" align="left" width="40%"></td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><strong>协办警官：</strong></span>
							</td>
							<td class="td1" align="left"></td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left">${model.ueMainName}</td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left">${model.ueHelpName}</td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left">${model.ueMainNo}</td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left">${model.ueHelpNo}</td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left">${model.ueMainId}</td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left">${model.ueHelpId}</td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left">${model.ueMainMobile}</td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left">${model.ueHelpMobile}</td>
						</tr>
						<tr>
							<td colspan="2" class="td1" align="left">
								<table style="width:100%;">
									<tr>
										<td width="50%" align="center">
											<img id="ueMainNo1Tmp" src="<%=path %>${ueMainNo1Tmp }" style="width:100%;height:200px;">
											<div>警官证（正）</div>
										</td>
										<td width="50%" align="center">
											<img id="ueMainNo2Tmp" src="<%=path %>${ueMainNo2Tmp }" style="width:100%;height:200px;">
											<div>警官证（反）</div>
										</td>
									</tr>
									<tr>
										<td width="50%" align="center">
											<img id="ueMainId1Tmp" src="<%=path %>${ueMainId1Tmp }" style="width:100%;height:200px;">
											<div>身份证（正）</div>
										</td>
										<td width="50%" align="center">
											<img id="ueMainId2Tmp" src="<%=path %>${ueMainId2Tmp }" style="width:100%;height:200px;">
											<div>身份证（反）</div>
										</td>
									</tr>
								</table>
							</td>
							<td class="td1" colspan="2" align="left">
								<table style="width:100%;">
									<tr>
										<td width="50%" align="center">
											<img id="ueHelpNo1Tmp" src="<%=path %>${ueHelpNo1Tmp }" style="width:100%;height:200px;">
											<div>警官证（正）</div>
										</td>
										<td width="50%" align="center">
											<img id="ueHelpNo2Tmp" src="<%=path %>${ueHelpNo2Tmp }" style="width:100%;height:200px;">
											<div>警官证（反）</div>
										</td>
									</tr>
									<tr>
										<td width="50%" align="center">
											<img id="ueHelpId1Tmp" src="<%=path %>${ueHelpId1Tmp }" style="width:100%;height:200px;">
											<div>身份证（正）</div>
										</td>
										<td width="50%" align="center">
											<img id="ueHelpId2Tmp" src="<%=path %>${ueHelpId2Tmp }" style="width:100%;height:200px;">
											<div>身份证（反）</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<td class="table1_td"></td>
						<td></td>
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
</html>
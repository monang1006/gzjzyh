<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>图片上传</title>
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
													<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
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
		</table>
	</DIV>
</body>
<iframe id="hiddenFrame" name="hiddenFrame" style="width:0px;height:0px;display:none;"></iframe>
</html>
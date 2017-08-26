<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询申请</title>
<%@ include file="/common/include/meta.jsp"%>
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
<style type="text/css">
body {
	width: 100%;
	margin: 0px;
	height: 100%
}

#inHtml {
	CURSOR: default;
	width: 300px;
	height: 100px;
}

#setAllUser {
	border: none;
}
</style>

<base target="_self" />
<body style="background-color: #ffffff">
	<DIV id=contentborder align=center>
		<s:form action="/action/queryBank!doProcess.action"
			name="applySave" id="applySave" method="post" target="hiddenFrame"
			enctype="multipart/form-data">
			<input type="hidden" id="appId" name="model.gzjzyhApplication.appId"
				value="${model.gzjzyhApplication.appId}">
			<input type="hidden" id="caseId"
				name="model.gzjzyhCase.caseId"
				value="${model.gzjzyhCase.caseId}">
			<input type="hidden" id="ueId" name="model.gzjzyhUserExtension.ueId"
				value="${model.gzjzyhUserExtension.ueId}">
				
			<input type="hidden" id="appResponsefile" name="model.gzjzyhApplication.appResponsefile"
				value="${model.gzjzyhApplication.appResponsefile}">
			<!--查询请求  -->
			<div>
				<!--查询请求头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>查询请求</strong></td>
									</tr>
								</table>

							</td>

						</tr>
					</table>
				</div>
				<!--查询请求输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;案件编号：&nbsp;</span></td>
							<td class="td1" align="left"><input id="caseCode"
								value="${model.gzjzyhCase.caseCode}"
								name="model.gzjzyhCase.caseCode" type="text"
								maxlength="25" /></td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;案件名称：&nbsp;</span></td>
							<td class="td1" align="left" colspan="2"><input
								id="caseName"
								value="${model.gzjzyhCase.caseName}"
								name="model.gzjzyhCase.caseName" type="text"
								maxlength="25" /> &nbsp;</td>

						</tr>


						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;立案时间：&nbsp;</span></td>
							<td class="td1" align="left"><strong:newdate
									id="caseConfirmTime"
									name="model.gzjzyhCase.caseConfirmTime"
									width="40%" dateform="yyyy-MM-dd HH:mm:ss" isicon="true"
									dateobj="${model.gzjzyhCase.caseConfirmTime}"
									classtyle="search" title="搜索结束时间" /></td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;经办单位：&nbsp;</span></td>
							<td class="td1" align="left" colspan="3"><input id="caseOrg"
								value="${model.gzjzyhCase.caseOrg}"
								name="model.gzjzyhCase.caseOrg" type="text"
								maxlength="25" /> &nbsp;</td>

						</tr>

						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;查询银行：&nbsp;</span></td>
							<td class="td1" align="left"><s:select id="appBankuser"									
									name="model.gzjzyhApplication.appBankuser" list="userList"
									listKey="userId" listValue="userName" cssClass="search"
									title="请输入操作结果"></s:select> &nbsp;</td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;查询申请类型：&nbsp;</span></td>
							<td class="td1" align="left" colspan="3"><s:select									
									name="model.gzjzyhApplication.appType" id="appType"
									list="#{'0':'查询申请','1':'冻解申请','2':'续冻申请','3':'解冻申请'}"
									listKey="key" listValue="value" />&nbsp;</td>

						</tr>

						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;文书号：&nbsp;</span></td>
							<td class="td1" align="left"><input id="appFileno"
								value="${model.gzjzyhApplication.appFileno}"
								name="model.gzjzyhApplication.appFileno" type="text"
								maxlength="25" /> &nbsp;</td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;申请时间：&nbsp;</span></td>
							<td class="td1" align="left" colspan="3"><strong:newdate
									name="model.gzjzyhApplication.appDate" id="appDate"
									dateobj="${model.gzjzyhApplication.appDate}" width="40%"
									skin="whyGreen" isicon="true" classtyle="search" title="请输入日期"
									dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate></td>
						</tr>

						<tr style="display: none">
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;审核意见：&nbsp;</span></td>
							<td colspan="5"><textarea rows="6" id="appOrgAccount"
									style="width: 20%" 
									name="model.gzjzyhApplication.appNgReason">${model.gzjzyhApplication.appNgReason}</textarea></td>
						</tr>


						<tr>

							<td class="td1" align="left" height="200px" colspan="6"><img
								id="imgTable"
								style="width: 180px; height: 177px; border: 1px green solid;" />
								<table>
									<tr>
										<td><label for="d_daiPicDog"> <span
												style="font-size: 20px; color: #4687C2;">照片</span>
										</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
											onchange="startPreview();"
											style="width: 83px; height: 30px; display: none" />
										<td>
									</tr>
								</table></td>

						</tr>
					</table>
				</div>

			</div>


			<!--查询申请页  -->
			<div id="div0" style="display: block">
				<!--涉案对象头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>涉案对象</strong></td>
									</tr>
								</table>

							</td>

						</tr>
					</table>
				</div>

				<!--涉案对象输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right">&nbsp;单位账号</td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;证件号码：&nbsp;</span></td>

							<td width="20%" class="td1" align="left">&nbsp;<textarea
									rows="6" id="appOrgAccount" 
									name="model.gzjzyhApplication.appOrgAccount">${model.gzjzyhApplication.appOrgAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a></td>

							<td class="biao_bg1" align="right">&nbsp;个人账号</td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;证件号码：&nbsp;</span></td>
							<td class="td1">&nbsp;<textarea rows="6"
									id="appPersonAccount"
									name="model.gzjzyhApplication.appPersonAccount">${model.gzjzyhApplication.appPersonAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a>
							</td>
						</tr>
					</table>
				</div>
				<!--查询对象输入框结束  -->

				<!--涉案账号头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>涉案账号</strong></td>
									</tr>
								</table>

							</td>

						</tr>
					</table>
				</div>

				<!--涉案账号输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right">&nbsp;单位开户明细</td>

							<td class="biao_bg1" align="right">&nbsp;待查账号：&nbsp;</span></td>

							<td width="20%" class="td1" align="left">&nbsp;<textarea
									rows="6" id="appOrgDetail"
									name="model.gzjzyhApplication.appOrgDetail">${model.gzjzyhApplication.appOrgDetail}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a></td>

							<td class="biao_bg1" align="right">&nbsp;个人开户明细</td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;待查账号：&nbsp;</span></td>
							<td class="td1">&nbsp;<textarea rows="6"
									id="appPersonDetail"
									name="model.gzjzyhApplication.appPersonDetail">${model.gzjzyhApplication.appPersonDetail}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a>
							</td>
						</tr>

						<tr>
							<td class="biao_bg1" align="right">&nbsp;交易明细</td>

							<td class="biao_bg1" align="right">&nbsp;待查账号：&nbsp;</span></td>
							<td class="td1" align="left">&nbsp;<textarea rows="6"
									id="appChadeDetail"
									name="model.gzjzyhApplication.appChadeDetail">${model.gzjzyhApplication.appChadeDetail}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a>
							</td>
						</tr>

						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;查询时间：&nbsp;</span></td>

							<td class="biao_bg1" align="right"><input name="timeSign"
								type="radio" value="0" />&nbsp;开启之日启至今</td>
							<td class="biao_bg1" align="right"><input name="timeSign"
								type="radio" value="1" />&nbsp;近一年</td>
							<td class="biao_bg1" align="left"><input name="timeSign"
								type="radio" value="2" />&nbsp;自定义</td>
							<td><strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									skin="whyGreen" isicon="true"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
								&nbsp;&nbsp;至</td>

							<td align="left"><strong:newdate
									name="model.gzjzyhApplication.appEndDate" id="appEndDate"
									skin="whyGreen" isicon="true"
									dateobj="${model.gzjzyhApplication.appEndDate}"
									classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate></td>

						</tr>

					</table>
				</div>
				<!--涉案账号输入框结束  -->

			</div>

			<!--冻解申请页  -->
			<div id="div1" style="display: none">
				<!--线索查询头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>线索查询</strong></td>
									</tr>
								</table>

							</td>

						</tr>
					</table>
				</div>

				<!--线索查询输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right">&nbsp;单位账号</td>

							<td width="20%" class="td1" align="left">&nbsp;<textarea
									rows="6" id="frozenAppOrgAccount"
									name="frozenAppOrgAccount">${model.gzjzyhApplication.appOrgAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a></td>

							<td class="biao_bg1" align="right">&nbsp;个人账号</td>

							<td class="td1">&nbsp;<textarea rows="6"
									id="frozenAppPersonAccount" 
									name="frozenAppPersonAccount">${model.gzjzyhApplication.appPersonAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right"><span class="wz">
									&nbsp;冻解时间：&nbsp;</span></td>

							<td><strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									skin="whyGreen" isicon="true" classtyle="search" title="请输入日期"
									dateform="yyyy-MM-dd"></strong:newdate> <span class="wz">&nbsp;至&nbsp;</span>
								<strong:newdate name="model.gzjzyhApplication.appEndDate"
									id="appEndDate" dateobj="${model.gzjzyhApplication.appEndDate}"
									skin="whyGreen" isicon="true" classtyle="search" title="请输入日期"
									dateform="yyyy-MM-dd"></strong:newdate></td>


						</tr>

					</table>
				</div>
				<!--线索查询输入框结束  -->
			</div>


			<!--续冻申请页  -->
			<div id="div2" style="display: none">
				<!--线索查询头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>线索查询</strong></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>

				<!--线索查询输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right">&nbsp;单位账号</td>

							<td width="20%" class="td1" align="left">&nbsp;<textarea
									rows="6" id="continueAppOrgAccount" 
									name="continueAppOrgAccount">${model.gzjzyhApplication.appOrgAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a></td>

							<td class="biao_bg1" align="right">&nbsp;个人账号</td>

							<td class="td1">&nbsp;<textarea rows="6"
									id="continueAppPersonAccount"
									name="continueAppPersonAccount">${model.gzjzyhApplication.appPersonAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right"><span class="wz">
									&nbsp;续冻时间：&nbsp;</span></td>

							<td><strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									skin="whyGreen" isicon="true" classtyle="search" title="请输入日期"
									dateform="yyyy-MM-dd"></strong:newdate> <span class="wz">&nbsp;至&nbsp;</span>
								<strong:newdate name="model.gzjzyhApplication.appEndDate"
									id="appEndDate" dateobj="${model.gzjzyhApplication.appEndDate}"
									skin="whyGreen" isicon="true" classtyle="search" title="请输入日期"
									dateform="yyyy-MM-dd"></strong:newdate></td>


						</tr>

					</table>
				</div>
				<!--线索查询输入框结束  -->
			</div>

			<!--解冻申请页  -->
			<div id="div3" style="display: none">
				<!--线索查询头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>线索查询</strong></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>

				<!--线索查询输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right">&nbsp;单位账号</td>

							<td width="20%" class="td1" align="left">&nbsp;<textarea
									rows="6" id="thawAppOrgAccount"
									name="thawAppOrgAccount">${model.gzjzyhApplication.appOrgAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a></td>

							<td class="biao_bg1" align="right">&nbsp;个人账号</td>

							<td class="td1">&nbsp;<textarea rows="6"
									id="thawAppPersonAccount"
									name="thawAppPersonAccount">${model.gzjzyhApplication.appPersonAccount}</textarea> <a
								href="#" class="button" onclick="accountImp()">导入</a>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right"><span class="wz">
									&nbsp;解冻时间：&nbsp;</span></td>

							<td><strong:newdate
									name="model.gzjzyhApplication.appStartDate" id="appStartDate"
									dateobj="${model.gzjzyhApplication.appStartDate}"
									skin="whyGreen" isicon="true" classtyle="search" title="请输入日期"
									dateform="yyyy-MM-dd"></strong:newdate></td>


						</tr>

					</table>
				</div>
				<!--线索查询输入框结束  -->
			</div>
			<!--警官信息  -->
			<div>
				<!--警官信息头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>警官信息</strong></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
				<!--警官信息输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px; width: 100%;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td>
								<!--主办警官信息输入框  -->

								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="biao_bg1" align="right"><span class="wz">&nbsp;姓名：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueMainName}"
											id="ueMainName" name="model.gzjzyhUserExtension.ueMainName"
											type="text" maxlength="25" /> &nbsp;</td>

									</tr>


									<tr>
										<td class="biao_bg1" align="right">&nbsp;警号：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueMainNo}"
											id="ueMainNo" name="model.gzjzyhUserExtension.ueMainNo"
											type="text" maxlength="25" /></td>
										</td>

									</tr>

									<tr>
										<td class="biao_bg1" align="right"><span class="wz">&nbsp;身份证号：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueMainId}"
											id="ueMainId" name="model.gzjzyhUserExtension.ueMainId"
											type="text" maxlength="25" /></td>

									</tr>

									<tr>
										<td class="biao_bg1" align="right"><span class="wz">&nbsp;手机号码：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input
											id="ueMainMobile" value="${model.gzjzyhUserExtension.ueMainMobile}"
											name="model.gzjzyhUserExtension.ueMainMobile" type="text"
											maxlength="25" /></td>
									</tr>

									<tr>

										<td class="td1" align="left" height="200px"><img
											id="imgTable"
											style="width: 180px; height: 177px; border: 1px green solid;" />
											<table>
												<tr>
													<td><label for="d_daiPicDog"> <span
															style="font-size: 20px; color: #4687C2;">照片</span>
													</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
														onchange="startPreview();" 
														style="width: 83px; height: 30px; display: none" />
													<td>
												</tr>
											</table></td>

										<td class="td1" align="left" height="200px" colspan="4"><img
											id="imgTable"
											style="width: 180px; height: 177px; border: 1px green solid;" />
											<table>
												<tr>
													<td><label for="d_daiPicDog"> <span
															style="font-size: 20px; color: #4687C2;">照片</span>
													</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
														onchange="startPreview();"
														style="width: 83px; height: 30px; display: none" />
													<td>
												</tr>
											</table></td>

										<td class="td1" align="left" height="200px" colspan="4"><img
											id="imgTable"
											style="width: 180px; height: 177px; border: 1px green solid;" />
											<table>
												<tr>
													<td><label for="d_daiPicDog"> <span
															style="font-size: 20px; color: #4687C2;">照片</span>
													</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
														onchange="startPreview();"
														style="width: 83px; height: 30px; display: none" />
													<td>
												</tr>
											</table></td>
									</tr>
								</table>
							</td>

							<td style="border-bottom: 1px solid #eee">
								<!--协办警官信息输入框  -->
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="biao_bg1" align="right"><span class="wz">&nbsp;姓名：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueHelpName}"
											id="ueHelpName" name="model.gzjzyhUserExtension.ueHelpName"
											type="text" maxlength="25" /> &nbsp;</td>

									</tr>


									<tr>
										<td class="biao_bg1" align="right">&nbsp;警号：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueHelpNo}"
											id="ueHelpNo" name="model.gzjzyhUserExtension.ueHelpNo"
											type="text" maxlength="25" /></td>
										</td>

									</tr>

									<tr>
										<td class="biao_bg1" align="right"><span class="wz">&nbsp;身份证号：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueHelpId}"
											id="ueHelpId" name="model.gzjzyhUserExtension.ueHelpId"
											type="text" maxlength="25" /></td>

									</tr>

									<tr>
										<td class="biao_bg1" align="right"><span class="wz">&nbsp;手机号码：&nbsp;</span></td>
										<td class="td1" align="left" colspan="3"><input value="${model.gzjzyhUserExtension.ueHelpMobile}"
											id="ueHelpMobile"
											name="model.gzjzyhUserExtension.ueHelpMobile" type="text"
											maxlength="25" /></td>
									</tr>
									<tr>
										<td class="td1" align="left" height="200px"><img
											id="imgTable"
											style="width: 180px; height: 177px; border: 1px green solid;" />
											<table>
												<tr>
													<td><label for="d_daiPicDog"> <span
															style="font-size: 20px; color: #4687C2;">照片</span>
													</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
														onchange="startPreview();"
														style="width: 83px; height: 30px; display: none" />
													<td>
												</tr>
											</table></td>
										<td class="td1" align="left" height="200px"><img
											id="imgTable"
											style="width: 180px; height: 177px; border: 1px green solid;" />
											<table>
												<tr>
													<td><label for="d_daiPicDog"> <span
															style="font-size: 20px; color: #4687C2;">照片</span>
													</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
														onchange="startPreview();"
														style="width: 83px; height: 30px; display: none" />
													<td>
												</tr>
											</table></td>
										<td class="td1" align="left" height="200px"><img
											id="imgTable"
											style="width: 180px; height: 177px; border: 1px green solid;" />
											<table>
												<tr>
													<td><label for="d_daiPicDog"> <span
															style="font-size: 20px; color: #4687C2;">照片</span>
													</label> <input type="file" id="d_daiPicDog" name="d_daiPicDog"
														onchange="startPreview();"
														style="width: 83px; height: 30px; display: none" />
													<td>
												</tr>
											</table></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>

				<!--警官信息输入框结束  -->
			</div>

			<!--银行反馈  -->
			<div>
				<!--银行反馈头部  -->
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td colspan="2" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>银行反馈</strong></td>
									</tr>
								</table>

							</td>

						</tr>
					</table>
				</div>
				<!--银行反馈输入框  -->
				<div style="border-top: solid rgb(0, 0, 0) 1px;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;签收人：&nbsp;</span></td>
							<td class="td1" align="left"><input id="appReceiver"
								value="${model.gzjzyhApplication.appReceiver}"
								name="model.gzjzyhApplication.appReceiver" type="text"
								maxlength="25" /></td>

							<td class="biao_bg1" align="right"><span class="wz">&nbsp;签收时间：&nbsp;</span></td>
							<td class="td1" align="left"><strong:newdate
									id="caseConfirmTime"
									name="model.gzjzyhApplication.appReceiveDate"
									width="40%" dateform="yyyy-MM-dd HH:mm:ss" isicon="true"
									dateobj="${model.gzjzyhApplication.appReceiveDate}"
									classtyle="search" title="搜索结束时间" /></td>
						</tr>


						<tr>
						<td>
							<a href="<%=path%>/action/queryBank!download.action" style="color:red;text-decoration:underline;"  >下载银行文件</a>
						</td>
						</tr>

						<tr>
							<td class="biao_bg1" align="right"><span class="wz">&nbsp;上报人：&nbsp;</span></td>
							<td class="td1" align="left"><input id="appResponser"
								value="${model.gzjzyhApplication.appResponser}"
								name="model.gzjzyhApplication.appResponser" type="text"
								maxlength="25" /></td>

						</tr>


					</table>
				</div>

			</div>
			<!--银行反馈结束  -->
			<div>
				<table>
					<tr>
						<td colspan="3" class="table_headtd">
							<table border="0" align="center" cellpadding="00" cellspacing="0">
								<tr>
									<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif" /></td>
									<td class="Operation_input" onclick="process();">&nbsp;反&nbsp;馈&nbsp;</td>
									<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif" /></td>
									<td width="5"></td>
									<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif" /></td>
									<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
									<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif" /></td>

								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>


		</s:form>
	</DIV>
</body>

<iframe id="hiddenFrame" name="hiddenFrame"
	style="width: 0px; height: 0px; display: none;"></iframe>

<script language="javascript">

$(function(){ 
	var appType = $("#appType").val();
	if(appType==0){
		$("#div0").css("display","block");
		$("#div1").css("display","none");
		$("#div2").css("display","none");
		$("#div3").css("display","none");
	}
	if(appType==1){
		$("#div0").css("display","none");
		$("#div1").css("display","block");
		$("#div2").css("display","none");
		$("#div3").css("display","none");
	}
	if(appType==2){
		$("#div0").css("display","none");
		$("#div1").css("display","none");
		$("#div2").css("display","block");
		$("#div3").css("display","none");
	}
	if(appType==3){
		$("#div0").css("display","none");
		$("#div1").css("display","none");
		$("#div2").css("display","none");
		$("#div3").css("display","block"); 
	}
	
});

function process(){
	document.getElementById("applySave").submit();
}

	function startPreview() {
		PreviewImage(document.getElementsByName("d_daiPicDog")[0], 'imgTable');
	}

	function PreviewImage(fileObj, imgPreviewId, divPreviewId) {
		var allowExtention = ".jpg,.bmp,.gif,.png";//允许上传文件的后缀名document.getElementById("hfAllowPicSuffix").value;    
		var extention = fileObj.value.substring(
				fileObj.value.lastIndexOf(".") + 1).toLowerCase();
		var browserVersion = window.navigator.userAgent.toUpperCase();
		if (allowExtention.indexOf(extention) > -1) {
			if (fileObj.files) {//HTML5实现预览，兼容chrome、火狐7+等    
				if (window.FileReader) {
					var reader = new FileReader();
					reader.onload = function(e) {
						document.getElementById(imgPreviewId).setAttribute(
								"src", e.target.result);
					}
					if (fileObj.files[0] == null) {
						return;
					}
					reader.readAsDataURL(fileObj.files[0]);
				} else if (browserVersion.indexOf("SAFARI") > -1) {
					alert("不支持Safari6.0以下浏览器的图片预览!");
				}
			} else if (browserVersion.indexOf("MSIE") > -1) {
				if (browserVersion.indexOf("MSIE 6") > -1) {//ie6    
					document.getElementById(imgPreviewId).setAttribute("src",
							fileObj.value);
				} else {//ie[7-9]    
					fileObj.select();
					if (browserVersion.indexOf("MSIE 9") > -1)
						fileObj.blur();//不加上document.selection.createRange().text在ie9会拒绝访问    
					var newPreview = document.getElementById(divPreviewId
							+ "New");
					if (newPreview == null) {
						newPreview = document.createElement("div");
						newPreview.setAttribute("id", divPreviewId + "New");
						newPreview.style.width = document
								.getElementById(imgPreviewId).width
								+ "px";
						newPreview.style.height = document
								.getElementById(imgPreviewId).height
								+ "px";
						newPreview.style.border = "solid 1px #d2e2e2";
					}
					newPreview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='"
							+ document.selection.createRange().text + "')";
					var tempDivPreview = document.getElementById(divPreviewId);
					tempDivPreview.parentNode.insertBefore(newPreview,
							tempDivPreview);
					tempDivPreview.style.display = "none";
				}
			} else if (browserVersion.indexOf("FIREFOX") > -1) {//firefox    
				var firefoxVersion = parseFloat(browserVersion.toLowerCase()
						.match(/firefox\/([\d.]+)/)[1]);
				if (firefoxVersion < 7) {//firefox7以下版本    
					document.getElementById(imgPreviewId).setAttribute("src",
							fileObj.files[0].getAsDataURL());
				} else {//firefox7.0+                        
					document.getElementById(imgPreviewId).setAttribute("src",
							window.URL.createObjectURL(fileObj.files[0]));
				}
			} else {
				document.getElementById(imgPreviewId).setAttribute("src",
						fileObj.value);
			}
		} else {
			alert("仅支持" + allowExtention + "为后缀名的文件!");
			fileObj.value = "";//清空选中文件    
			if (browserVersion.indexOf("MSIE") > -1) {
				fileObj.select();
				document.selection.clear();
			}
			fileObj.outerHTML = fileObj.outerHTML;
		}
	}
</script>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
<HEAD>
<TITLE>账号审核</TITLE>
<%@include file="/common/include/meta.jsp"%>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
	rel="stylesheet">
<!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css"
	type=text/css rel=stylesheet>
<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
<script language='javascript'
	src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<script type="text/javascript" language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
<script language="javascript"
	src="<%=path%>/common/js/common/windowsadaptive.js"></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
<!--右键菜单脚本
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		-->

</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;"
	onload="initMenuT()">
	<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm"
			action="/registeraudit/registerAudit.action">
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
											<td align="left"><strong>注册账号列表</strong></td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td>
															<table border="0" align="right" cellpadding="00"
																cellspacing="0">
																<tr>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_l.jpg" /></td>
																		<td class="Operation_list" onclick="auditAccount();"><img
																			src="<%=root%>/images/operationbtn/edit.png">&nbsp;审&nbsp;核&nbsp;</td>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_r.jpg" /></td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table> 
						<webflex:flexTable name="myTable" width="100%" height="370px"
							wholeCss="table1" property="ueId" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							pageSize="10" getValueType="getValueByProperty"
							collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="table1_search">
								<tr>
									<td>
										&nbsp;&nbsp;账号：&nbsp;
										<input id="searchLoginName"
										name="searchLoginName" cssClass="search" title="请输入账号"
										maxlength="50"
										value="${searchLoginName}"> 
										&nbsp;&nbsp;姓名：&nbsp;
										<input
										id="searchName" name="searchName" cssClass="search"
										title="请输入姓名" maxlength="50"
										value="${searchName}"> 
										&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate id="appStartDate" name="appStartDate" dateform="yyyy-MM-dd HH:mm:ss" isicon="true" dateobj="${appStartDate}" width="155px" classtyle="search" title="申请起始日期"/>
							       		&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate id="appEndDate" name="appEndDate" dateform="yyyy-MM-dd HH:mm:ss" isicon="true"  dateobj="${appEndDate}" width="155px" classtyle="search" title="申请结束日期"/>
										&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="ueId"
								showValue="name" width="5%" isCheckAll="true"
								isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="账号" property="loginName"
								showValue="loginName" width="20%" isCanDrag="true"
								isCanSort="false"></webflex:flexTextCol>
							<webflex:flexTextCol caption="姓名" property="name"
								showValue="name" width="20%" isCanDrag="true"
								isCanSort="false"></webflex:flexTextCol>
							<webflex:flexTextCol caption="单位" property="orgName"
								showValue="orgName" width="25%" isCanDrag="true"
								isCanSort="false"></webflex:flexTextCol>
							<webflex:flexDateCol caption="申请时间" property="ueDate"
								showValue="ueDate" width="15%" isCanDrag="true" isCanSort="false"></webflex:flexDateCol>
							<webflex:flexEnumCol caption="申请状态" mapobj="${statusMap}"
								property="ueStatus" showValue="ueStatus" width="15%"
								isCanDrag="false" isCanSort="false"></webflex:flexEnumCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</s:form>
	</DIV>
	<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","审核","auditAccount",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function auditAccount(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要审核的账号。');
		return;
	}
	if(id.split(",").length >1){
		alert('只能审核一个账号。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/registeraudit/registerAudit!input.action?ueId="+id,window,'help:no;status:no;scroll:no;dialogWidth:1500px; dialogHeight:800px');
}


function submitForm() {
	document.getElementById("myTableForm").submit();
}
$(document).ready(
		function() {
			$("#img_sousuo")
					.click(
							function() {
								$("form").submit();
								gotoPage(1);
							});
		});
	</script>
	<br>
</BODY>
</HTML>
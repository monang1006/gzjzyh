<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
<HEAD>
<TITLE>查询申请列表</TITLE>
<%@include file="/common/include/meta.jsp"%>
<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
	rel=stylesheet>
<script type="text/javascript"
	src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
	rel="stylesheet">
<!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css"
	type=text/css rel=stylesheet>
<script language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
<script language='javascript'
	src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<script language="javascript"
	src="<%=path%>/common/js/common/windowsadaptive.js"></script>
<!--右键菜单脚本 -->
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></script>
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/workservice.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>

</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;"
	onLoad="initMenuT()">
	<script language="javascript" type="text/javascript"
		src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm"
			action="/action/queryAudit!auditedList.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img"><img
												src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
											<td align="left"><strong>已审核申请列表</strong></td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="viewApply();"><img
															src="<%=root%>/images/operationbtn/view.png" />&nbsp;查&nbsp;看&nbsp;</td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_r.jpg" /></td>
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
								<td><webflex:flexTable name="myTable" width="100%"
										height="370px" wholeCss="table1" property="dictCode"
										isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA"
										footShow="showCheck" getValueType="getValueByProperty"
										collection='${page.result}' page="${page}">
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
											class="table1_search">
											<tr>
												<td>&nbsp;&nbsp;查询需求：&nbsp; <s:select name="searchRequiredType"
														list="#{'':'请选择','0':'个人账号','1':'单位帐号','2':'个人开户明细','3':'单位开户明细','4':'交易明细'}"
														listKey="key" listValue="value" style="width:155px;" />
													&nbsp;&nbsp;文书编号：&nbsp; <input id="searchAppFileNo"
													name="searchAppFileNo" type="text" width="155px" value="${searchAppFileNo}">
													&nbsp;&nbsp;申请时间：&nbsp; <strong:newdate id="searchAppDateStart"
														name="searchAppDateStart" dateform="yyyy-MM-dd HH:mm:ss"
														isicon="true" width="155px"
														dateobj="${searchAppDateStart}"
														classtyle="search" />
													&nbsp;至&nbsp; <strong:newdate id="searchAppDateEnd"
														name="searchAppDateEnd" dateform="yyyy-MM-dd HH:mm:ss"
														isicon="true" width="155px"
														dateobj="${searchAppDateEnd}"
														classtyle="search" />
												</td>
											</tr>
											<tr>
												<td>
													&nbsp;&nbsp;请求银行：&nbsp; <s:select name="searchAppBankuser"
														list="userList" listKey="userId" listValue="userName"
														cssClass="search" headerKey="" headerValue="请选择"
														style="width:155px;"></s:select>
													&nbsp;&nbsp;经办单位：&nbsp; <input id="searchAppOrg"
													name="searchAppOrg" type="text" width="155px" value="${searchAppOrg}">
													&nbsp;<input
													id="img_sousuo" type="button" onClick="submitForm();" />
												</td>
											</tr>
										</table>
										<webflex:flexCheckBoxCol caption="选择" property="appId"
											showValue="appId" width="4%" isCheckAll="true"
											isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
										<webflex:flexEnumCol caption="申请类型" align="center"
											mapobj="${typeMap}" property="appType"
											showValue="appType" width="12%" isCanDrag="true"
											isCanSort="true"></webflex:flexEnumCol>
										<webflex:flexTextCol caption="文书编号" property="appFileno"
											align="center" showValue="appFileno" width="12%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										<webflex:flexEnumCol caption="请求银行" align="center"
											mapobj="${userMap}" property="appBankuser"
											showValue="appBankuser" width="18%"
											isCanSort="true" isCanDrag="true"></webflex:flexEnumCol>
										<webflex:flexDateCol caption="申请时间" property="appDate"
											showValue="appDate" width="18%" isCanDrag="true" 
											isCanSort="false" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
										<webflex:flexDateCol caption="审核时间" property="appAuditDate"
											showValue="appAuditDate" width="18%" dateFormat="yyyy-MM-dd HH:mm:ss"
											isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
										<webflex:flexEnumCol caption="当前状态" align="center"
											mapobj="${statusMap}" property="appStatus"
											showValue="appStatus" width="12%" isCanDrag="true"
											isCanSort="true"></webflex:flexEnumCol>
									</webflex:flexTable></td>
							</tr>

						</table> </s:form>
						</DIV> <script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewApply",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function viewApply(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要查看的申请。");
		return;
	}
	if(id.length >32){
		alert('只可以查看一条申请。');
		return;
	}
	var width=screen.availWidth;
 	var height=screen.availHeight;
	var result=window.showModalDialog("<%=path%>/action/queryAudit!getApplyView.action?appId="+id,
			window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+"status:no;help:no;scroll:no;");
}

function submitForm() {
	document.getElementById("myTableForm").submit();
}
	</script>
</BODY>
</HTML>

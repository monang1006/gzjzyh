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
			action="/action/queryApply.action">
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
											<td align="left"><strong>查询申请列表</strong></td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="addApply();"><img
															src="<%=root%>/images/operationbtn/add.png" />&nbsp;新&nbsp;建&nbsp;</td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_r.jpg" /></td>
														<td width="5"></td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="editApply();"><img
															src="<%=root%>/images/operationbtn/edit.png" />&nbsp;编&nbsp;辑&nbsp;</td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_r.jpg" /></td>
														<td width="5"></td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="deleteApply();"><img
															src="<%=root%>/images/operationbtn/del.png" />&nbsp;删&nbsp;除&nbsp;</td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_r.jpg" /></td>
															
														<td width="5"></td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="doCommit();"><img
															src="<%=root%>/images/operationbtn/edit.png" />&nbsp;提&nbsp;交&nbsp;</td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_r.jpg" /></td>
															
														<td width="5"></td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="doBack();"><img
															src="<%=root%>/images/operationbtn/edit.png" />&nbsp;撤&nbsp;销&nbsp;</td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_r.jpg" /></td>	
																													
														<td width="5"></td>
														<td width="4"><img
															src="<%=frameroot%>/images/bt_l.jpg" /></td>
														<td class="Operation_list" onclick="view();"><img
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
													&nbsp;&nbsp;当前状态：&nbsp; <s:select name="searchAppStatus"
														list="#{'':'请选择','0':'待提交','1':'待审核','2':'已审核','3':'已驳回','4':'已签收','5':'已处理','6':'已拒签'}"
														listKey="key" listValue="value" style="width:155px;" />
													&nbsp;&nbsp;关联案件：&nbsp; <input id="searchCaseName" readonly="readonly"
													name="searchCaseName" type="text" width="155px" value="${searchCaseName }">
													<input id="searchCaseId" name="searchCaseId" type="hidden" value="${searchCaseId }">
													<a href="javascript:void(0);" class="button" onclick="change()">选择案件</a> 
													<a href="javascript:void(0);" class="button" onclick="clearCase()">清空案件</a> <input
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editApply",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","deleteApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","提交","doCommit",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","撤销","doBack",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","view",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addApply(){
	var width=screen.availWidth;
 	var height=screen.availHeight;
	var result=window.showModalDialog("<%=path%>/action/queryApply!input.action",
										window,"dialogWidth:" + "1000" + "pt;dialogHeight:" + height + "pt;"+"status:no;help:no;scroll:no;");
}

function deleteApply(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要删除的申请。");
		return;
	}
	if(confirm("确定要删除吗？")){
		$.ajax({
			type : "post",
			dataType : "text",
			url : "<%=path%>/action/queryApply!del.action",
			data : "appId=" + id,
			success : function(msg) {
				if (msg == "true") {
					alert("删除成功");
					$("form").submit();
				} else {
					alert("删除失败，只能删除未提交的申请。");
				}
			}
		});
	}
}

function editApply(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要编辑的申请。");
		return;
	}
	if(id.length >32){
		alert('只可以编辑一条申请。');
		return;
	}
	$.ajax({
		type : "post",
		dataType : "text",
		url : "<%=path%>/action/queryApply!checkCanEdit.action",
		data : "appId=" + id,
		success : function(msg) {
			if (msg == "true") {
				var width=screen.availWidth;
			 	var height=screen.availHeight;
				var result=window.showModalDialog("<%=path%>/action/queryApply!input.action?appId="+id,
						window,"dialogWidth:" + "1000" + "pt;dialogHeight:" + height + "pt;"+"status:no;help:no;scroll:no;");
			} else {
				alert("当前申请不是未提交、已驳回或者已拒签状态，不允许编辑。");
			}
		}
	});
}

function change(){
	var width=screen.availWidth/2;
 	var height=screen.availHeight/2;
	var result=window.showModalDialog("<%=path%>/action/queryApply!casePage.action",
												window,
												"dialogWidth:"
														+ width
														+ "pt;dialogHeight:"
														+ height
														+ "pt;"
														+ "status:no;help:no;scroll:no;");

}

function doCommit(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要提交的申请。");
		return;
	}
	
	if(confirm("确定要提交吗？")){
		$.ajax({
			type : "post",
			dataType : "text",
			url : "<%=path%>/action/queryApply!doCommit.action",
			data : "appId=" + id,
			success : function(msg) {
				if (msg == "true") {
					alert("提交成功");
					$("form").submit();
				} else {
					alert("提交失败，只能提交尚未提交的申请。");
				}
			}
		});
	}
}

function doBack(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要撤消的申请。");
			return;
	}
	
	if(confirm("确定要撤消吗？")){
		$.ajax({
			type : "post",
			dataType : "text",
			url : "<%=path%>/action/queryApply!back.action",
			data : "appId=" + id,
			success : function(msg) {
				if (msg == "true") {
					alert("撤销成功！");
					$("form").submit();
				} else {
					alert("撤消失败，只能撤销未审核、审核退回及拒签的申请。");
				}
			}
		});
	}
}	
	
function view(){
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
	var result=window.showModalDialog("<%=path%>/action/queryApply!getApplyView.action?appId="+id,
			window,"dialogWidth:" + "1000" + "pt;dialogHeight:" + height + "pt;"+"status:no;help:no;scroll:no;");
}

function changeCaseF(caseId,caseCode,caseName){
	document.getElementById("searchCaseId").value=caseId;
	document.getElementById("searchCaseName").value=caseName;
}

function clearCase(){
	document.getElementById("searchCaseId").value="";
	document.getElementById("searchCaseName").value="";
}

function submitForm() {
	document.getElementById("myTableForm").submit();
}
	</script>
</BODY>
</HTML>

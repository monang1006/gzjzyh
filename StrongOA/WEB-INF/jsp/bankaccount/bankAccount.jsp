<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
<HEAD>
<TITLE>银行账号列表</TITLE>
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
<!--右键菜单脚本
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		-->

</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;"
	onload="initMenuT()">
	<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm"
			action="/bankaccount/bankAccount.action">
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
											<td align="left"><strong>银行账号列表</strong></td>
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
																		<td class="Operation_list" onclick="addUser();"><img
																			src="<%=root%>/images/operationbtn/add.png">&nbsp;新&nbsp;建&nbsp;</td>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_r.jpg" /></td>
																		<td width="3"></td>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_l.jpg" /></td>
																		<td class="Operation_list" onclick="editUser();"><img
																			src="<%=root%>/images/operationbtn/edit.png">&nbsp;编&nbsp;辑&nbsp;</td>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_r.jpg" /></td>
																		<td width="3"></td>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_l.jpg" /></td>
																		<td class="Operation_list" onclick="del();"><img
																			src="<%=root%>/images/operationbtn/del.png">&nbsp;删&nbsp;除&nbsp;</td>
																		<td width="4"><img
																			src="<%=frameroot%>/images/bt_r.jpg" /></td>
																		<td width="3"></td>
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
						</table> <webflex:flexTable name="myTable" width="100%" height="370px"
							wholeCss="table1" property="userId" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							pageSize="10" getValueType="getValueByProperty"
							collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="table1_search">
								<tr>
									<td>&nbsp;&nbsp;用户姓名：&nbsp;<input id="selectname"
										name="selectname" cssClass="search" title="请输入用户名称"
										maxlength="50"
										onkeyup="this.value=this.value.replace(/\s/g,'')"
										onafterpaste="this.value=this.value.replace(/\s/g,'')"
										value="${selectname}"> 
										&nbsp;&nbsp;登录账号：&nbsp;<input
										id="selectloginname" name="selectloginname" cssClass="search"
										title="请输入登录账号" maxlength="50"
										onkeyup="this.value=this.value.replace(/\s/g,'')"
										onafterpaste="this.value=this.value.replace(/\s/g,'')"
										value="${selectloginname}"> 
										&nbsp;&nbsp;是否启用：&nbsp;<s:select
											id="isActive" name="isActive"
											list="#{'':'是否启用','1':'已启用','0':'未启用'}"
											onchange='$("#img_sousuo").click();' listKey="key"
											listValue="value" style="width:14em" />
										&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="userId"
								showValue="userName" width="5%" isCheckAll="true"
								isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="用户姓名" property="userId"
								showValue="userName" width="30%" isCanDrag="true"
								isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
							<webflex:flexTextCol caption="登录账号" property="userLoginname"
								showValue="userLoginname" width="20%" isCanDrag="true"
								isCanSort="true"></webflex:flexTextCol>
							<webflex:flexEnumCol caption="是否启用" mapobj="${userActiveTypeMap}"
								property="userIsactive" showValue="userIsactive" width="20%"
								isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addUser",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//添加用户
function addUser(){
	var audit= window.showModalDialog("<%=path%>/bankaccount/bankAccount!input.action",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:300px');
}
//编辑用户
function editUser(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的用户。');
		return;
	}
	if(id.split(",").length >1){
		alert('只能编辑一个用户。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/bankaccount/bankAccount!input.action?userId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:300px');
}
//删除
function del(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的用户。');
		return;
	}
	
	if(confirm("删除选定的用户，确定？")) { 
		$.ajax({
			type : "post",
			dataType : "text",
			url : "<%=root%>/bankaccount/bankAccount!delete.action",
					data : "userId=" + id,
					success : function(msg) {
						if (msg == "true") {
							//alert("删除成功！");
							$("form").submit();
						} else {
							alert("删除失败，请您重新删除。");
						}
					}
				});
		}
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
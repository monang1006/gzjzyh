<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type="text/css" rel="stylesheet"
			href="<%=frameroot%>/css/properties_windows.css">
		<link type="text/css" rel="stylesheet"
			href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=jsroot%>/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=jsroot%>/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=jsroot%>/common/common.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()" >
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/ipaccess/setipscope/ipScope.action">
				<input type="hidden" id="IpScopeId" name="IpScopeId">
				<input type="hidden" id="disLogo" name="disLogo" value="${disLogo }">
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
												<td>&nbsp;</td>
												<td width="20%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													允许访问IP段列表
												</td>
												<td width="*">
													&nbsp;
												</td>
												
												<td >
													<a class="Operation" href="#" onclick="loginIPAdd()"> <img
															src="<%=root%>/images/ico/tianjia.gif" width="14"
															height="14" class="img_s"><span id="test"
														style="cursor:hand">增加&nbsp;</span> </a>
												</td>
												<td width="5"></td>
											
												<td >
													<a class="Operation" href="#" onclick="loginIPEdit()">
														<img src="<%=root%>/images/ico/bianji.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">编辑&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												
												<td >
													<a class="Operation" href="#" onclick="delLoginIp()"> <img
															src="<%=root%>/images/ico/shanchu.gif" width="15"
															height="15" class="img_s"><span id="test"
														style="cursor:hand">删除&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												
												<td>
													<a class="Operation" href="#" onclick="loginIPUser()">
														<img src="<%=root%>/images/ico/shezhibuxianip.gif"
															width="15" height="15" class="img_s"><span
														id="test" style="cursor:hand">不限制IP用户设置&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="loginId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" onclick="search()" style="cursor: hand;">
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<input id="loginBeginIp" name=loginBeginIp type="text"
												style="width:100%" class="search" value="${loginBeginIp}"
												title="请输入起始ip地址">
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<input id="loginEndIp" name="loginEndIp" type="text"
												style="width:100%" class="search" value="${loginEndIp }"
												title="请输入结束ip地址">
										</td>
										<td width="45%" align="center" class="biao_bg1">
											<input id="loginDesc" name="loginDesc" type="text"
												style="width:100%" class="search" value="${loginDesc }"
												title="请输入备注内容">
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="loginId"
									showValue="loginId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="起始IP" property="loginBeginIp"
									showValue="loginBeginIp" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="结束IP" property="loginEndIp"
									showValue="loginEndIp" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="备注" property="loginId"
									showValue="loginDesc" width="45%" isCanDrag="true"
									isCanSort="true" onclick="view(this.value)"></webflex:flexTextCol>
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
	
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","loginIPAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);

	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","loginIPEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delLoginIp",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);

	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function loginIPAdd(){
	var id="";
	window.showModalDialog("<%=path%>/ipaccess/setipscope/ipScope!input.action",window,'help:no;status:no;scroll:no;dialogWidth:530px; dialogHeight:270px');
}
function loginIPEdit(){
	var id=getValue();
	if(id==""){
		alert("请选择需编辑的记录！");
	}else if(id.indexOf(",")!=-1){
		alert("只能选择一条访问ip段记录！");
	}
	else
		window.showModalDialog("<%=path%>/ipaccess/setipscope/ipScope!input.action?IpScopeId="+id,window,'help:no;status:no;scroll:no;dialogWidth:530px; dialogHeight:270px');
}
function loginIPUser(){
	location="<%=path%>/fileNameRedirectAction.action?toPage=ipaccess/notrestruser/notRestrUser.jsp";
}
function delLoginIp(){
	var id=getValue();
	if(id=="")
		alert("请选择需删除的记录！");
	else{
		if(confirm("确定要删除吗？"))
			location="<%=path%>/ipaccess/setipscope/ipScope!delete.action?IpScopeId="+id;
	}
}
function submitForm(){
	document.getElementById("myTableForm").submit();
}

function view(value){
	window.showModalDialog("<%=path%>/ipaccess/setipscope/ipScope!view.action?IpScopeId="+value,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:260px');
}

function search(){
	document.getElementById("disLogo").value="search";
	submitForm();	
}
</script>
	</BODY>
</HTML>

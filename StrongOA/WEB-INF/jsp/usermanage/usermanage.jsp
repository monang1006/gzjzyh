<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>机构用户列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		-->
		
	</HEAD>
	<s:if test="module==\"orgType\"">
		<BODY class=contentbodymargin oncontextmenu="return false;">
	</s:if>
	<s:else>
		<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
	</s:else>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm" action="/usermanage/usermanage.action">
				<input type="hidden" name="extOrgId" id="extOrgId" value="${extOrgId}">
				<input type="hidden" name="module" id="module" value="${module}">
				<input type="hidden" id="type" name="type" value="setPersonPrivil">
				<input type="hidden" id="selectname" name="selectname" value="${selectname}">
				<input type="hidden" id="selectloginname" name="selectloginname" value="${selectloginname}">
				<input type="hidden" id="isActive" name="isActive" value="${isActive}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td class="table_headtd_img">
													<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
												</td>
												<td align="left">
													<strong>用户列表</strong>
												</td>
												<td align="right">
													<s:if test="module==\"orgType\"">
														<table border="0" align="right" cellpadding="00" cellspacing="0">
															<tr>
																<td width="450"></td>
															</tr>
														</table>
													</s:if>
													<s:else>
													<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
															<td>
																<table border="0" align="right" cellpadding="00" cellspacing="0">
																	<tr>
																		<security:authorize ifAllGranted="001-0005000100020001">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="addUser();"><img src="<%=root%>/images/operationbtn/add.png">&nbsp;新&nbsp;建&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																		<security:authorize ifAllGranted="001-0005000100020002">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="editUser();"><img src="<%=root%>/images/operationbtn/edit.png">&nbsp;编&nbsp;辑&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																		<!--<security:authorize ifAllGranted="001-0005000100020003">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="move();"><img src="<%=root%>/images/operationbtn/Move_file.png">&nbsp;移&nbsp;动&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																		--><security:authorize ifAllGranted="001-0005000100020004">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png">&nbsp;删&nbsp;除&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																		<security:authorize ifAllGranted="001-0005000100020005">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="reductUser();"><img src="<%=root%>/images/ico/cexiao.gif">&nbsp;还&nbsp;原&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																		<security:authorize ifAllGranted="001-0005000100020006">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setSystemAdmin();"><img src="<%=root%>/images/operationbtn/Administrator_setting.png">管理员设置&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>		
																		<td width="3"></td>
																		</security:authorize>
																	</tr>
																</table>
															</td>
														</tr>													
														<tr higth="20"></tr>
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
																	<tr>
																		<security:authorize ifAllGranted="001-0005000100020008">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setUserGroup();"><img src="<%=root%>/images/operationbtn/Group_settings.png">用户组设置&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>	
																		<td width="3"></td>
																		</security:authorize>
																		<security:authorize ifAllGranted="001-0005000100020009">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setRole();"><img src="<%=root%>/images/operationbtn/character_set.png">角色设置&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>	
                                                                        <td width="3"></td>
                                                                        </security:authorize>
                                                                        <security:authorize ifAllGranted="001-0005000100020010">
                                                                        <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setPost();"><img src="<%=root%>/images/operationbtn/Post_setting.png">岗位设置&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																	</tr>
																</table>
															</td>
														</tr>														
														<tr higth="20"></tr>
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
																	<tr>
																		<%--<security:authorize ifAllGranted="001-0005000100020007">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setPersonPrivil();"><img src="<%=root%>/images/operationbtn/Permissions_settings.png">人事人员权限设置&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>--%>
																		<security:authorize ifAllGranted="001-0005000100020011">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setPrivil();"><img src="<%=root%>/images/operationbtn/Resource_settings.png">资源设置&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>	
																		<td width="3"></td>
																		</security:authorize>
																		<security:authorize ifAllGranted="001-0005000100020012">
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
																		<td class="Operation_list" onclick="setCopy();"><img src="<%=root%>/images/operationbtn/Resource_replication.png">资源复制&nbsp;</td>
																		<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
																		<td width="3"></td>
																		</security:authorize>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
													</s:else>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="userId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" pageSize="10" 
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
									<tr>
										<td>
											 &nbsp;&nbsp;用户姓名：&nbsp;<input id="searchname" name="searchname" cssClass="search" title="请输入用户名称" style="width:110px;"  maxlength="50" onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')" value="${selectname}"> 										
											 &nbsp;&nbsp;登录账号：&nbsp;<input id="searchloginname" name="searchloginname"   cssClass="search" title="请输入登录账号" maxlength="50"  onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')" value="${selectloginname}">										
											 &nbsp;&nbsp;是否启用：&nbsp;<s:select id="searchActive" name="searchActive" style="width:80px;" list="#{'':'是否启用','1':'已启用','0':'未启用'}" onchange='$("#img_sousuo").click();' listKey="key" listValue="value" style="width:14em"/>
											 &nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
										<%--
										<td width="15%" align="center" class="biao_bg1">
											   <s:select name="isdel"  list="#{'':'是否删除','1':'已删除','0':'未删除'}" listKey="key" listValue="value" style="width:10.5em"/>
										</td>
										--%>
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="userId" showValue="userName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<s:if test="module==\"orgType\"">
									<webflex:flexTextCol caption="用户姓名" property="userId" showValue="userName" width="30%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
								</s:if>
								<s:else>
									<webflex:flexTextCol caption="用户姓名" property="userId" showValue="userName" width="30%" isCanDrag="true"
										isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
								</s:else>
								<webflex:flexTextCol caption="登录账号" property="userLoginname" showValue="userLoginname" width="20%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="是否启用" mapobj="${userActiveTypeMap}" property="userIsactive" showValue="userIsactive" 
									width="20%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<%--
								<webflex:flexTextCol caption="用户岗位" property="rest4" showValue="rest4" width="25%" 
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								--%>
								</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
<script language="javascript">
var sMenu = new Menu();
 var orgid = '${extOrgId}';
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	<security:authorize ifAllGranted="001-0005000100020001">
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addUser",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020002">
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	/*<security:authorize ifAllGranted="001-0005000100020003"> item = new MenuItem("<%=root%>/images/operationbtn/Move_file.png","移动","move",1,"ChangeWidthTable","checkOneDis"); sMenu.addItem(item); </security:authorize>*/
	<security:authorize ifAllGranted="001-0005000100020004">
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020005">
	item = new MenuItem("<%=root%>/images/ico/goujian.gif","还原","reductUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020006">
	item = new MenuItem("<%=root%>/images/operationbtn/Administrator_setting.png","管理员设置","setSystemAdmin",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	//<security:authorize ifAllGranted="001-0005000100020007">
	//item = new MenuItem("<%=root%>/images/operationbtn/Permissions_settings.png","人事人员权限设置","setPersonPrivil",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);	
	//</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020008">	
	item = new MenuItem("<%=root%>/images/operationbtn/Group_settings.png","用户组设置","setUserGroup",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020009">
	item = new MenuItem("<%=root%>/images/operationbtn/character_set.png","角色设置","setRole",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020010">
	item = new MenuItem("<%=root%>/images/operationbtn/Post_setting.png","岗位设置","setPost",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020011">
	item = new MenuItem("<%=root%>/images/operationbtn/Resource_settings.png","资源设置","setPrivil",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0005000100020012">
	item = new MenuItem("<%=root%>/images/operationbtn/Resource_replication.png","资源复制","setCopy",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//添加用户
function addUser(){
	//var audit= window.showModalDialog("<%=path%>/usermanage/usermanage!input.action?extOrgId="+orgid,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
	var audit= window.showModalDialog("<%=path%>/usermanage/usermanage!input.action?extOrgId="+orgid,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:600px');
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
	if(orgid == null || orgid == ""){
		var audit = window.showModalDialog("<%=path%>/usermanage/usermanage!input.action?userId="+id+"&audittype=get",window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:600px');
	}else{
		var audit = window.showModalDialog("<%=path%>/usermanage/usermanage!input.action?userId="+id+"&extOrgId=" + orgid + "&audittype=get",window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:600px');
	}
}
//删除
function del(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的用户。');
		return;
	}
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行删除操作。");
		return;
	}
	
		$.ajax( {
			type : "post",
			dataType : "text",
			url : "<%=root%>/usermanage/usermanage!isLeaderRelation.action",
			data : "userId=" + id ,
			success : function(msg) {
						if (msg !="") {
						var message="您选择的用户:";
							alert(message+msg+"已被设置为其他用户的领导联系人,不可以删除。");
							return;
							
						} else {
							if(confirm("删除选定的用户，确定？")) { 
								//	location = '<%=path%>/usermanage/usermanage!delete.action?userId='+id+'&extOrgId='+orgid+"page.pageNo=${page.pageNo}";
									$
									.ajax( {
										type : "post",
										dataType : "text",
										url : "<%=root%>/usermanage/usermanage!delete.action",
										data : "userId=" + id + "&&extOrgId=" + orgid,
										success : function(msg) {
											if (msg == "true") {
												//alert("删除成功！");
												$("#img_sousuo").click();
									} else {
										alert("删除失败请您重新删除。");
									}
								}
									});
								} 
						}
			}
		});
		

}
//还原用户
function reductUser(){
	var id = getValue();
	if(id == null||id == ''){
		alert('请选择要还原的用户。');
		return;
	}
	var isdel = checkUserIsNotDel();
	if(isdel != ""){
		alert(isdel + "未被删除，不允许进行还原操作。");
		return;
	}
	location = '<%=path%>/usermanage/usermanage!reduction.action?userId='+id+'&extOrgId='+orgid;
}
//移动用户
function move(){
	var id = getValue();
	if(id == null||id == ""){
		alert("请选择要移动的用户。")
		return;
	}
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行移动用户操作。");
		return;
	}
	var audit = window.showModalDialog("<%=path%>/usermanage/usermanage!movetree.action?userId="+id+"&extOrgId=${extOrgId}",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//设置角色
function setRole(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要设置角色的用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('一次只能设置一个用户。');
		return;
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行角色设置操作。");
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getUserRole.action?userId='+id+'&extOrgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//得到用户信息【通过用户姓名链接】
function getinfo(value){
	var module="${module}";
	if(module=="orgType"){
	}else{
		parent.userinfo.setUserId(value);
	}
}
//设置岗位
function setPost(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要设置岗位的用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('一次只能设置一个用户。');
		return;
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行岗位设置操作。");
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getUserPost.action?userId='+id+'&extOrgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//设置用户组
function setUserGroup(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要设置用户组的用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('一次只能设置一个用户。');
		return;
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行用户组设置操作。");
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getUserGroup.action?userId='+id+'&extOrgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//设置资源权限
function setPrivil(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要设置权限的用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('一次只能设置一个用户。');
		return;
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行资源权限设置操作。");
		return;
	}
	
	window.showModalDialog('<%=path%>/usermanage/usermanage!setUserPrivil.action?userId='+id+'&extOrgId='+orgid+"&timestamp="+new Date(),window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//设置操作权限
function setOptPrivil(){
	var id = getValue();
	if(id == ""){
		alert("请选择要设置权限的用户。");
		return ;
	}else{
		if(id.split(",").length>1){
			alert("一次只能为一个用户设置权限。");
			return ;
		}
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行操作权限设置操作。");
		return;
	}
	
	window.showModalDialog('<%=path%>/optprivilmanage/baseOptPrivil!getOptPrivil.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//资源权限复制
function setCopy(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('一次只能设置一个用户。');
		return;
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行资源权限复制操作。");
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getCopyPage.action?userId='+id+'&extOrgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//操作权限复制
function setOperCopy(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('只能选择一个被复制用户。');
		return;
	}
	var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行操作权限复制操作。");
		return;
	}
	
	window.showModalDialog('<%=path%>/optprivilmanage/baseOptPrivil!getProCopyPage.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
//设置人事人员权限
function setPersonPrivil(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('只能选择一个设置权限。');
		return;
	}
	OpenWindow("<%=path%>/attendance/arrange/scheGroup!getPersonPrivil.action?userId="+id,350,400,window);	
}

//设置人员对于应用系统的管理员信息
function setSystemAdmin(){
	var id = getValue();
	if(id == null||id == ''){
		alert('请选择用户。');
		return;
	}
	if(id.split(",").length>1){
		alert('只能设置一个用户。');
		return;
	}
var ss=$(":checked").parent().next().next().next().attr("value");
   if(ss=='0'){
   		alert('该用户尚未启用。');
   		return ;
   }
	var isdel = checkUserIsDel();
	if(isdel != ""){
		alert(isdel + "已删除，不允许进行管理员设置操作。");
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!initSystemAdminInfo.action?userId='+id,window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:300px');
}
 function checkUserIsDel(){
    var returnvalue = "";
 	$.each($(":checked[name!='checkall']"), function(i, obj){
 		if($(obj).parent().next().next().next().next().val() == "1"){
 			returnvalue = returnvalue + "，[" + $(obj).parent().next().text() + "]";
 		}
 	});
 	if(returnvalue != ""){
 		returnvalue = returnvalue.substring(1);
 	}
 	return returnvalue;
 }
 
 function checkUserIsNotDel(){
    var returnvalue = "";
 	$.each($(":checked[name!='checkall']"), function(i, obj){
 		if($(obj).parent().next().next().next().next().val() == "0"){
 			returnvalue = returnvalue + "，[" + $(obj).parent().next().text() + "]";
 		}
 	});
 	if(returnvalue != ""){
 		returnvalue = returnvalue.substring(1);
 	}
 	return returnvalue;
 }
 
function submitForm(){
	document.getElementById("myTableForm").submit();
}
 $(document).ready(function(){
 		$("#searchActive").val($("#isActive").val());
        $("#img_sousuo").click(function(){
        	$("#selectname").val(encodeURI($("#searchname").val()));
        	$("#selectloginname").val(encodeURI($("#searchloginname").val()));
        	$("#isActive").val($("#searchActive").val());
        	$("form").submit();
        	gotoPage(1);
        });     
      });
</script>
	<body>
	<br>
	</BODY>
</HTML>
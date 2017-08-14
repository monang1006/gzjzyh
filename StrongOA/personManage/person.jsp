<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>机构人员列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
			<%
			
			
			 %>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/usermanage/usermanage.action">
				<input type="hidden" name="orgId" id="orgId" value="${orgId}">
				<%--<input type="hidden" name="orgname" id="orgname" value="${orgname}">--%>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="60"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="5%" align="center">
													<img src="<%=frameroot%>/images/ico.gif" width="9"
														height="9">
												</td>
												<td width="10%">
													人员列表
												</td>
												<td width="5%">
													&nbsp;
												</td>
												<td width="85%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right"
																	cellpadding="0" cellspacing="0">

																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:addUser();"><img
																					src="<%=frameroot%>/images/chakan.gif" width="14"
																					height="14" class="img_s">查看</a>
																		</td>
																		
																		<td width="5"></td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:addUser();"><img
																					src="<%=frameroot%>/images/tianjia.gif" width="14"
																					height="14" class="img_s">添加</a>
																		</td>
																		<td width="5"></td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:editUser();">
																				<img src="<%=frameroot%>/images/bianji.gif"
																					width="15" height="15" class="img_s">编辑</a>
																		</td>
																		<td width="5"></td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:del();"> <img
																					src="<%=frameroot%>/images/shanchu.gif" width="15"
																					height="15" class="img_s">删除</a>
																		</td>
																		<td width="5"></td>
																		<td width="50">
																		<a class="Operation"
																			href="#">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">导入</a>
																	</td>
																	<td width="5"></td>
																	<td width="50">
																		<a class="Operation"
																			href="#">
																			<img src="<%=frameroot%>/images/yidong.gif"
																				width="14" height="14" class="img_s">导出</a>
																	</td>
																	
																		
																		<td width="5"></td>
																	
																		
																		
																		
																	</tr>
																</table>
															</td>

														</tr>
														
														<tr higth="20"></tr>

														<tr>


														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="userId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=frameroot%>/images/sousuo.gif" width="17" id="img_sousuo"
												height="16" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield name="selectname" cssClass="search" title="请输入姓名"></s:textfield>  
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield name="selectloginname" cssClass="search" title="请输入工号"></s:textfield> 
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="所属机构"></s:textfield>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入性别"></s:textfield>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入职位"></s:textfield>
										</td>
										<td width="10%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="籍贯"></s:textfield>
										</td>
										<td width="10%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="联系电话"></s:textfield>
										</td>
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="userId"
									showValue="userName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="人员姓名" property="userId"
									showValue="userName" width="15%" isCanDrag="true"
									isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
								<webflex:flexTextCol caption="工号" property="userLoginname"
									showValue="userLoginname" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="所属机构" property="baseOrg.orgId"
									showValue="baseOrg.orgName" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
									<webflex:flexEnumCol caption="性别" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="职位" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="籍贯" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								
								<webflex:flexEnumCol caption="联系电话" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
 var orgid = '${orgId}';
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addUser",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","修改","editUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/yidong.gif","移动","move",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/goujian.gif","还原","reductUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);	
	item = new MenuItem("<%=frameroot%>/images/setgroup.gif","用户组设置","setUserGroup",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/setrole.gif","角色设置","setRole",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/setpost.gif","岗位设置","setPost",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/setprivil.gif","操作权限设置","setOptPrivil",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/setprivil.gif","资源设置","setPrivil",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/copyprivil.gif","操作权限复制","setOperCopy",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/copyprivil.gif","资源复制","setCopy",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
    if(orgid!=""&& orgid!=null){
    document.getElementById('baseorg').value='${orgname}';
    document.getElementById('baseorg').readOnly=true;
    }
}


function addUser(){
	window.location.href="person-input.jsp";
	
}
function editUser(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length >32){
		alert('只能编辑一个用户！');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/usermanage/usermanage!input.action?userId="+id+"&orgId="+orgid+"&audittype=get",window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
	//if(audit=='ok'){
	//	location = '<%=path%>/usermanage/usermanage.action?orgId'+orgid;
	//}
}
function del(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(confirm("确定删除用户吗?")) 
	{ 
	location = '<%=path%>/usermanage/usermanage!delete.action?userId='+id+'&orgId='+orgid;
	} 
	
}

function reductUser(){
	var id = getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	location = '<%=path%>/usermanage/usermanage!reduction.action?userId='+id+'&orgId='+orgid;
}

function move(){
	var id = getValue();
	if(id == null||id == ""){
		alert("请选择需要移动的用户！")
		return;
	}
	var audit = window.showModalDialog("<%=path%>/usermanage/usermanage!movetree.action?userId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	//if(audit == "ok"){
	//	alert("用户移动成功");
	//	location = '<%=path%>/usermanage/usermanage.action?orgId'+orgid;
	//}
}

function setRole(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length>32){
		alert('只能设置一个用户！');
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getUserRole.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
function selectUser(){
	window.showModalDialog("selectUser.jsp",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
function getinfo(value){
	
	parent.userinfo.setUserId(value);
 	//parent.status_container.status_list.location = url;
}

function setPost(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length>32){
		alert('只能设置一个用户！');
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getUserPost.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}

function setUserGroup(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length>32){
		alert('只能设置一个用户！');
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getUserGroup.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}

function setPrivil(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length>32){
		alert('只能设置一个用户！');
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getPrivil.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}

//设置操作权限
function setOptPrivil(){
	var id = getValue();
	if(id == ""){
		alert("请选择要设置权限的用户！");
		return ;
	}else{
		if(id.split(",").length>1){
			alert("一次只能为一个用户设置权限！");
			return ;
		}
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getOptPrivil.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}

function setCopy(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length>32){
		alert('只能设置一个用户！');
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getCopyPage.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}

function setOperCopy(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length>32){
		alert('只能选择一个被复制用户！');
		return;
	}
	window.showModalDialog('<%=path%>/usermanage/usermanage!getProCopyPage.action?userId='+id+'&orgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	//alert(orgid);
}

function select(){
	if(event.keyCode==13){ 
  		alert("Enter!");   
  	}	
}
 $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
</script>
	</BODY>
</HTML>

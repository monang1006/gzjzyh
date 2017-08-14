<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
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
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/common.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<STYLE>
			@media All{
				input{ 
					behavior: url("<%=root%>/oa/js/personnel/textfield.htc");
				}
			}
		</STYLE>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
			<script type="text/javascript"
				src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/personnel/baseperson/person.action" method="post">
				<input type="hidden" name="orgId" id="orgId" value="${orgId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>&nbsp;</td>
												<td width="15%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													人员列表
												</td>
												<td width="85%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table  border="0" align="right"
																	cellpadding="0" cellspacing="0">
																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td width="5"></td>
																		<td align="right">
																			<a class="Operation" href="javascript:addUser();"><img
																					src="<%=root%>/images/ico/tianjia.gif" width="14"
																					height="14" class="img_s">添加</a>
																		</td>
																		<td width="5"></td>
																		<td align="right">
																			<a class="Operation" href="javascript:editUser();">
																				<img src="<%=root%>/images/ico/bianji.gif"
																					width="15" height="15" class="img_s">编辑人员</a>
																		</td>
																		<td width="5"></td>
																		<td align="right">
																			<a class="Operation"
																				href="javascript:editRelationInfo();"> <img
																					src="<%=root%>/images/ico/bianji.gif" width="15"
																					height="15" class="img_s">编辑子集</a>
																		</td>
																		<td width="5"></td>
																		<td align="right">
																			<a class="Operation" href="javascript:del();"> <img
																					src="<%=root%>/images/ico/shanchu.gif" width="15"
																					height="15" class="img_s">删除</a>
																		</td>
																		<td width="5"></td>
																		<td>
																			<a class="Operation" href="javascript:gochange();">
																				<img src="<%=root%>/images/ico/chuli.gif"
																					width="15" height="15" class="img_s">调配</a>
																		</td>
																		<td width="5"></td>
																		<td>
																			<a class="Operation" href="javascript:viewDeployInfo();">
																				<img src="<%=root%>/images/ico/chakanlishi.gif"
																					width="15" height="15" class="img_s">调配记录</a>
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
								wholeCss="table1" property="personid" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												id="img_sousuo" height="16" style="cursor: hand;"
												title="单击搜索">
										</td>
										<td width="12%" align="center" class="biao_bg1">
											<input  type="text" name="model.personName" value="${model.personName}"
												class="search" title="请输入姓名" />
										</td>
										<td width="8%" align="center" class="biao_bg1">
											<s:select list="sexMap" id="personSax" name="model.personSax"
												listKey="key" listValue="value" headerKey=""
												headerValue="" cssStyle="width:100%"
												onchange="$('#img_sousuo').click();" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<input  type="text" id="personNativePlace" name="model.personNativePlace"
												value="${model.personNativePlace}" class="search" title="请输入籍贯" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="personWorkTime" name="model.personWorkTime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												nowvalue="${model.personWorkTime}" classtyle="search" title="参加工作时间" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="personBorn" name="model.personBorn"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												nowvalue="${model.personBorn}" classtyle="search" title="出生日期" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:if test="orgId==null||orgId==\"\"">
												<input type="text" id="orgName" name="model.baseOrg.orgName"
													value="${model.baseOrg.orgName}" class="search"
													title="所属机构" />
											</s:if>
											<s:else>
												<s:select id="personStructId" name="model.personStructId"
													list="bianzhiMap" headerKey="" headerValue=""
													listKey="key" listValue="value" cssStyle="width:100%"
													onchange="$('#img_sousuo').click();" />
											</s:else>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:select id="personPersonKind" name="model.personPersonKind"
												list="personKindMap" listKey="key" listValue="value"
												headerKey="" headerValue="" cssStyle="width:100%"
												onchange="$('#img_sousuo').click();" />
										</td>
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="1" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="人员姓名" valuepos="0"
										valueshowpos="1" width="12%" isCanDrag="true"
									isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="性别" mapobj="${sexMap}"
									valuepos="2" valueshowpos="2" width="8%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="籍贯" valuepos="3"
										valueshowpos="3" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="参加工作时间" valuepos="4"
										valueshowpos="4" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="出生日期" valuepos="5"
										valueshowpos="5" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<s:if test="orgId==null||orgId==\"\"">
									<webflex:flexTextCol caption="所属机构" valuepos="8"
										valueshowpos="9" width="15%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
								</s:if>
								<s:else>
									<webflex:flexEnumCol caption="人员编制" mapobj="${bianzhiMap}"
										valuepos="7" valueshowpos="7"
										width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								</s:else>
								<webflex:flexEnumCol caption="人员类别" valuepos="6"
										valueshowpos="6" mapobj="${personKindMap}"
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
 //var orgid = '${orgId}';
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addUser",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑人员","editUser",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑子集","editRelationInfo",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chuli.gif","调配","gochange",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakanlishi.gif","调配记录","viewDeployInfo",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}


function addUser(){
	var orgId=$("#orgId").val();
	if(orgId==null||orgId==""||orgId=="null"){
		alert("请先选择组织机构！")
		return;
	}else{
 		OpenWindow("<%=path%>/personnel/baseperson/person!initViewAddTool.action?orgId="+orgId,550,475,window);
 	}
}

function editUser(){
    var orgId=$("#orgId").val();
    if(orgId==""||orgId==null){
    	alert("请选择组织机构！")
    	return;
    }
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length >32){
		alert('只能编辑一个用户！');
		return;
	}
	$.ajax({
		type:"post",
		data:{
			personId:id			
		},
		url:"<%=path%>/personnel/baseperson/person!isExistDeployType.action",
		success:function(data){	
		    if(data=="0"){ 
		    	alert("该人员是退休人员，不能编辑！");
				return false;	
		    }else{
		    	var url="<%=path%>/personnel/baseperson/person!initEditPerson.action?forward=viewedit&orgId="+orgId+"&personId="+id+"&infoSetCode=40288239230c361b01230c7a60f10015&keyid="+id;
				OpenWindow(url,550,460,window);
		    }
		},
		error:function(data){
			alert("对不起，操作异常"+data);
		}
	}); 
}

function editRelationInfo(){
    var orgId=$("#orgId").val();
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(id.length >32){
		alert('只能编辑一个用户！');
		return;
	}
	$.ajax({
		type:"post",
		data:{
			personId:id	
		},
		url:"<%=path%>/personnel/baseperson/person!isExistDeployType.action",
		success:function(data){	
		    if(data=="0"){ 
		    	alert("该人员是退休人员，不能编辑子集！");
				return false;	
		    }else{
		    	window.location="<%=path%>/personnel/baseperson/person!initViewAddTool.action?forward=initviewedit&orgId="+orgId+"&personId="+id+"&keyid="+id;
		    }
		},
		error:function(data){
			alert("对不起，操作异常"+data);
		}
	}); 
}

function del(){
	var id=getValue();
	var orgId=$("#orgId").val();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	if(confirm("确定删除用户吗?")) 
	{ 
	   window.location.href="<%=path%>/personnel/baseperson/person!delete.action?personId="+id+"&orgId="+orgId;
	} 
	
}



function selectUser(){
	window.showModalDialog("selectUser.jsp",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}
function getinfo(value){
	var orgId=$("#orgId").val();
	// window.location="<%=path%>/fileNameRedirectAction.action?toPage=/personnel/baseperson/person-frame.jsp?orgId="+orgId+"&personId="+id+"&keyid="+id;
	window.location="<%=path%>/personnel/baseperson/person!initViewAddTool.action?forward=initviewedit&disLogo=viewinfo&orgId="+orgId+"&personId="+value+"&keyid="+value;
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
    	$("Form").submit();
    });     
  });
      
  function submitForm(){
  	 document.getElementById("myTableForm").submit();
  }
  
  function gochange(){
		var id=getValue();
		var orgId=document.getElementById("orgId").value;
		if(id == null||id == ''){
			alert('请选择用户！');
			return;
		}
		if(id.length >32){
			alert('只能编辑一个用户！');
			return;
		}
		$.ajax({
			type:"post",
			data:{
				personId:id,
				readonly:"true"				
			},
			url:"<%=path%>/personnel/baseperson/person!isExistDeployType.action",
			success:function(data){	
			    if(data=="0"){ 
			    	alert("该人员是退休人员，不能进行人员调配操作！");
					return false;	
			    }else if(data=="1"){ 
			    	alert("没有设置调配类别，不能进行人员调配操作！");
					return false;	
			    }else{
			    	var url="<%=path%>/personnel/baseperson/person!viewChangeInfo.action?personId="+id+"&infoSetCode=40288239230c361b01230c7a60f10015&keyid="+id+"&orgId="+orgId;
					OpenWindow(url,470,330,window);
			    }
			},
			error:function(data){
				alert("对不起，操作异常"+data);
			}
		});  
  }
  
  function viewDeployInfo(){//查看调配信息
  		var id=getValue();
		var orgId=document.getElementById("orgId").value;
		if(id == null||id == ''){
			alert('请选择用户！');
			return;
		}
		if(id.length >32){
			alert('只能编辑一个用户！');
			return;
		}
		window.location="<%=path%>/personnel/baseperson/person!viewDeloyInfo.action?personId="+id+"&orgId="+orgId;
  }
  
</script>
	</BODY>
</HTML>

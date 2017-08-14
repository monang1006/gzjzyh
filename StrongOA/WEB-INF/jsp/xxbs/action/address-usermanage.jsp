<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>用户信息列表</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<!--  <script type="text/javascript" src="<%=scriptPath%>/search.js"></script>-->
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
	</head>
	<script type="text/javascript">
	var form=document.forms[0];
   var orgid = '${extOrgId}';
	$(document).ready(function(){
     
     $(window).bind("resize", function(){
     	if(!isIE6){//处理其它浏览器
     		var width = $("#menu_top").outerWidth() - ($("#menu_top").outerWidth() - $("#menu_top").width())/2;
     		$("#list").setGridWidth(width, true);
     	}
     });
     
     $("#menu_top")[0].onresize = function(){
     	if(isIE6){//处理IE6浏览器
     		var width = $("#menu_top").outerWidth() - ($("#menu_top").outerWidth() - $("#menu_top").width())/2;
     		$("#list").setGridWidth(width, true);
     	}
     };
 });
		
	</script>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div id="menu_top" style="width:auto;">

				</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
		<s:form theme="simple" id="myTableForm" action="/usermanage/usermanage.action">
		              <input type="hidden" name="extOrgId" id="extOrgId"
						value="${extOrgId}">
					<table width="100%" id="searchTable" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td width="100%" align="left" class="biao_bg1"
								style="padding-right:8px;padding-left:8px;">
								姓名：
								<s:textfield name="selectname" id="selectname"
									cssClass="main_search_input search" title="请输入用户名称"
									theme="simple"></s:textfield>
								<input type="button" value="搜索"  style="width: 50px" id="img_sousuo">
									</td>
						</tr>
					</table>
					<table id="list"></table>
					<div id="pager"></div>
		</s:form>
				</div>
				</div>



	</body>
</html>

<script type="text/javascript">

var fUseStatus1 = function(el, cellval, opts) {
	temp = "";
	
	if (el == "1") {
		temp = "负责人";
	}
	else if (el == "2") {
		temp = "分管领导";
	}
	else if(el == "0"){
		temp = "成员";
	}
	return temp;
};

var fUseStatus2 = function(el, cellval, opts) {
	temp = "";
	if (el == "1") {
		temp = "省政府副秘书长";
	}
	else if (el == "2") {
		temp = "处长";
	}
	if (el == "3") {
		temp = "副调研员";
	}
	else if (el == "4") {
		temp = "副处长";
	}
	if (el == "5") {
		temp = "调研员";
	}
	else if(el == "0"){
		temp = " ";
	}
	return temp;
};

$(function(){
	
$("#list").jqGrid({
		url:'${root}/xxbs/action/address!showList.action?extOrgId=${extOrgId}',
		colModel :[ 
			{label:'用户id',name:'uid', hidden:true}, 
			{label:'用户姓名',name:'username', sortable:false}, 
			{label:"岗位", name:"userepost", formatter:fUseStatus1, align:"center", sortable:false},
			{label:"职位", name:"userposition", formatter:fUseStatus2, align:"center", sortable:false},
			{label:'电话号码',name:'usertel',width:150, align:'center', sortable:false}, 
			{label:"联系地址", name:"useraddr", align:"center", sortable:false},
			{label:"Email", name:"useremail", align:"center", sortable:false}
			],
		
		gridComplete: gl.resize
	});
	
	

	
	
	$("#img_sousuo").click(function(){
  	 	var selectname = $("#selectname").val();
  	 	var selectloginname = $("#selectloginname").val();
  	 	var isActive = $("#isActive").val();
  	 	var isdel = $("#isdel").val();
       	var searchParam = {};
	 	if(selectname != null && selectname != ""){
	 	searchParam.selectname = selectname;
	 }else{
	 	searchParam.selectname = "";
	 }
	 if(selectloginname != null && selectloginname != ""){
	 	searchParam.selectloginname = selectloginname;
	 }else{
	 	searchParam.selectloginname = "";
	 }
	 if(isActive != null && isActive != ""){
	 	searchParam.isActive = isActive;
	 }else{
	 	searchParam.isActive = "";
	 }
	 if(isdel != null && isdel != ""){
	 	searchParam.isdel = isdel;
	 }else{
	 	searchParam.isdel = "";
	 }
       	searchParam.isSearch = true;
       	$("#list").setGridParam({postData: searchParam}).trigger("reloadGrid");		

	});
});





var isYes = function(val) {
	var temp = "";
	if (val == 1) {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='是'>";
	}
	return temp;
};

curformat = function(el, cellval, opts) {
		temp = "全部";
		if (el == 1) {
			temp = "已启用";
			temp = temp.fontcolor("#0000FF") 
			
		}
		if (el == 0) {
			temp = "未启用";			
		}		 
		return temp;
	}
	
				
curformat2 = function(el, cellval, opts) {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='未删除'>";
		if (el == 1) {
			temp = "<img src='<%=themePath%>/image/ico_fork.gif' title='已删除'>";			
		}
		return temp;
	}
	
function reloadData(){
	$('#list').trigger("reloadGrid");
}

function addUser(){
	
	var url = "<%=path%>/usermanage/usermanage!input.action?extOrgId="+orgid;
	var w =580;
	var h =580;
	var ret = gl.showsmgwDialog(url,w,h);
	//alert(ret);
	  gl.msg(ret, "保存成功");
	
	
 //var audit =window.showModalDialog("<%=path%>/usermanage/usermanage!input.action?extOrgId="+orgid,window,"dialogHeight:580px; dialogWidth: 580px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
	
	
}
function editUser(){
	var id = $("#list").getGridParam('selarrrow');
	//var id=getValue();
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择要编辑的用户.');
		return;
	}
	if(id.length >32){
		Error('一次只能选择一个要编辑的用户.');
		return;
	}
	var url;
		var w =580;
	    var h =580;
	if(orgid == null || orgid == ""){
	    url="<%=path%>/usermanage/usermanage!input.action?userId="+id+"&audittype=get";
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "保存成功");
	}else{
			 url="<%=path%>/usermanage/usermanage!input.action?userId="+id+"&extOrgId=" + orgid + "&audittype=get";
	    	var ret = gl.showmyDialog(url,w,h);
	       gl.msg(ret, "保存成功");
	   
	
	}
	
}
function del(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	//var id=getValue();
	if(id == null||id == ''){
		Error('请选择要删除的用户.');
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行删除操作!");
		return;
	}
	if(confirm("确定删除用户吗?")) 
	{ 
        var action = "<%=path%>/usermanage/usermanage!delete.action";
        var data = {userId:id,extOrgId:orgid};	   
	    $.post(action,data,function(){
	       reloadData();
	    });
	//	location = '<%=path%>/usermanage/usermanage!delete.action?userId='+id+'&extOrgId='+orgid;
	} 
}



function delss(){
	var id = $("#list").jqSelectedId();
	if(id != false && confirm("确定要删除吗？")){
		$.get("<%=root%>/xxbs/info/submit!deleteSubmitted.action?toId="+id, function(response){
			if(response == "success"){
				gl.msg(response, "删除成功");
			}
			else if(response == "notDelete"){
				alert("已被采用的报送信息不允许删除。");
			}
		});
	}
}
function reductUser(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择用户！');
		return;
	}
	//var isdel = checkUserIsNotDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "未被删除，不允许进行还原操作!");
		return;
	}
	location = '<%=path%>/usermanage/usermanage!reduction.action?userId='+id+'&extOrgId='+orgid;
}

function move(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ""){
		Error("请选择需要移动的用户！")
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行移动操作!");
		return;
	}
	  var w =450;
	    var h =480;
	    url="<%=path%>/fileNameRedirectAction.action?toPage=usermanage/usermanage-movetree.jsp?userId="+id+"&extOrgId=${extOrgId}";
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "移动成功");
	
}

function setRole(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择用户！');
		return;
	}
	if(id.length>32){
		Error('只能设置一个用户！');
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行角色设置操作!");
		return;
	}

    var w =450;
	    var h =480;
	    url='<%=path%>/usermanage/usermanage!getUserRole.action?userId='+id+'&extOrgId='+orgid;
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "用户角色设置成功");

 //var audit =window.showModalDialog('<%=path%>/usermanage/usermanage!getUserRole.action?userId='+id+'&extOrgId='+orgid,window,"dialogHeight:580px; dialogWidth: 580px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
	
}

function getinfo(value){
	
	parent.userinfo.setUserId(value);
 	//parent.status_container.status_list.location = url;
}

function setPost(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择用户！');
		return;
	}
	if(id.length>32){
		Error('只能设置一个用户！');
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行岗位设置操作!");
		return;
	}
       var w =450;
	    var h =480;
	    url='<%=path%>/usermanage/usermanage!getUserPost.action?userId='+id+'&extOrgId='+orgid;
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "用户岗位设置成功");

 //var audit =window.showModalDialog('<%=path%>/usermanage/usermanage!getUserPost.action?userId='+id+'&extOrgId='+orgid,window,"dialogHeight:353px; dialogWidth: 285px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
	
}

function setUserGroup(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择用户！');
		return;
	}
	if(id.length>32){
		Error('只能设置一个用户！');
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行用户组设置操作!");
		return;
	}
      var w =450;
	    var h =480;
	    url='<%=path%>/usermanage/usermanage!getUserGroup.action?userId='+id+'&extOrgId='+orgid;
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "用户用户组设置成功");
	
	// var audit =window.showModalDialog('<%=path%>/usermanage/usermanage!getUserGroup.action?userId='+id+'&extOrgId='+orgid,window,"dialogHeight:353px; dialogWidth: 285px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
	
}

function setPrivil(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择要设置资源的用户.');
		return;
	}
	if(id.length>32){
		Error('一次只能为一个用户设置资源.');
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行资源权限设置操作!");
		return;
	}
	//window.showModalDialog('<%=path%>/usermanage/usermanage!getUserPrivil.action?userId='+id+'&extOrgId='+orgid,window,'help:no;status:no;scroll:no;dialogWidth:480px; dialogHeight:360px');
	
	    var w =450;
	    var h =480;
	    url='<%=path%>/usermanage/usermanage!getUserPrivil.action?userId='+id+'&extOrgId='+orgid;
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "用户资源设置成功");
	
	// var audit =window.showModalDialog('<%=path%>/usermanage/usermanage!getUserPrivil.action?userId='+id+'&extOrgId='+orgid,window,"dialogHeight:353px; dialogWidth: 285px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
	
}

function setCopy(){
	var id = $("#list").getGridParam('selarrrow');
	id = id.join(",");
	if(id == null||id == ''){
		Error('请选择用户！');
		return;
	}
	if(id.length>32){
		Error('只能设置一个用户！');
		return;
	}
	//var isdel = checkUserIsDel();
	var isdel = "";
	if(isdel != ""){
		Error(isdel + "已删除，不允许进行资源权限复制操作!");
		return;
	}
		 var w =450;
	    var h =480;
	    url='<%=path%>/fileNameRedirectAction.action?toPage=usermanage/usermanage-copyprivil.jsp?userId='+id+'&extOrgId='+orgid;
	     var ret = gl.showmyDialog(url,w,h);
	      gl.msg(ret, "用户资源复制成功");
	
	// var audit =window.showModalDialog('<%=path%>/fileNameRedirectAction.action?toPage=usermanage/usermanage-copyprivil.jsp?userId='+id+'&extOrgId='+orgid,window,"dialogHeight:353px; dialogWidth: 285px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");

}

	//设置人员对于应用系统的管理员信息
	function setSystemAdmin(){
		var id = $("#list").getGridParam('selarrrow');
		id = id.join(",");
		if(id == null||id == ''){
			Error('请选择用户！');
			return;
		}
		if(id.length>32){
			Error('只能设置一个用户！');
			return;
		}
		//var isdel = checkUserIsDel();
		var isdel = "";
		if(isdel != ""){
			Error(isdel + "已删除，不允许进行管理员设置操作!");
			return;
		}
	 var w =450;
	    var h =480;
	    url='<%=path%>/usermanage/usermanage!initSystemAdminInfo.action?userId='+id;
	    var ret = gl.showmyDialog(url,w,h);
	    gl.msg(ret, "用户管理员设置成功");
	
	//window.open(url,'', 'height=480, width=450');
	// var audit =window.showModalDialog('<%=path%>/usermanage/usermanage!initSystemAdminInfo.action?userId='+id,window,"dialogHeight:393px; dialogWidth: 285px; dialogTop: "+xx+"; dialogLeft: "+yy+"; edge: Raised; center: Yes; help: Yes; resizable: Yes; status: Yes;");
		
	}

 function checkUserIsDel(){
    var returnvalue = "";
 	$.each($(":checked[name!='checkall']"), function(i, obj){
 		if($(obj).parent().next().next().next().next().next().val() == "1"){
 			returnvalue = returnvalue + "，[" + $(obj).parent().next().next().text() + "]";
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
 

</script>
<web:menu name="buton_list" width="150">
	<web:menuItem name="setPrivil"
		icon="${themePath}/image/ico_set.gif"
		action="setPrivil" text="资源设置" />
	<web:menuItem name="setRole"
		icon="${themePath}/image/ico_set.gif"
		action="setRole" text="角色设置" />	
	<web:menuItem name="setGroup"
		icon="${themePath}/image/ico_set.gif"
		action="setUserGroup" text="用户组设置" />
	<web:menuItem name="setPost"
		icon="${themePath}/image/ico_set.gif" action="setPost"
		text="岗位设置" />
	<web:menuItem name="privilCopy"
		icon="${themePath}/image/ico_copy.gif" action="setCopy"
		text="资源复制" />
	<web:menuItem name="setSystemAdmin"
		icon="${themePath}/image/ico_set.gif"
		action="setSystemAdmin" text="管理员设置" />	
</web:menu>



<web:navigationBar varname="m">
	<web:navigationBarMenu menuItemName="buton_list" varname="m"
		eleid="setButton" />
</web:navigationBar>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

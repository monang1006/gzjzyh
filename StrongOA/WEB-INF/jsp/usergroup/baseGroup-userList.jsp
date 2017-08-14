<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="java.util.*"%>
<HTML>
<HEAD>
	<TITLE>操作内容</TITLE>
	<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
	<META http-equiv=Content-Type content="text/html; charset=utf-8">
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/usergroup/baseGroup!userList.action"> 
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
     <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
     <table width="100%" border="0" cellspacing="0" cellpadding="00">     
    <input type="hidden" id="groupId" name="groupId" value="${model.groupId}">
    <input type="hidden" id="groupSyscode" name="model.groupSyscode" value="${model.groupSyscode}">  
          <tr>
             <td>
             &nbsp;
             </td>
             <td class="table_headtd_img">
				<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
			</td>
            <td align="left">
           	 <strong>组用户列表</strong>
            </td>
            <td align="right">
            <table  border="0" align="right" cellpadding="0" cellspacing="0">
               <tr>
	               <td width="100%" align="right">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								<td class="Operation_list" onclick="addTempFile();"><img src="<%=root%>/images/operationbtn/add.png">&nbsp;添&nbsp;加&nbsp;组&nbsp;</td>
								<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								<td width="3"></td>
								<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								<td class="Operation_list" onclick="editTempFile();"><img src="<%=root%>/images/operationbtn/edit.png">&nbsp;编&nbsp;辑&nbsp;组&nbsp;</td>
								<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								<td width="3"></td>
								<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								<td class="Operation_list" onclick="delTempFile();"><img src="<%=root%>/images/operationbtn/del.png">&nbsp;删&nbsp;除&nbsp;组&nbsp;</td>
								<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								<td width="3"></td>
			                </tr>
						</table>
					</td>
                </tr>
                <tr higth="20"></tr>
				<tr>
					<td width="100%" align="right">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								<td class="Operation_list" onclick="addUser();"><img src="<%=root%>/images/operationbtn/Add_user.png">&nbsp;添&nbsp;用&nbsp;户&nbsp;</td>
								<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								<td width="3"></td>
								<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								<td class="Operation_list" onclick="addClearance();"><img src="<%=root%>/images/operationbtn/Resource_settings.png">&nbsp;资&nbsp;源&nbsp;设&nbsp;置&nbsp;</td>
								<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								<td width="3"></td>
								<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								<td class="Operation_list" onclick="copyClearance();"><img src="<%=root%>/images/operationbtn/Resource_replication.png">&nbsp;资&nbsp;源&nbsp;复&nbsp;制&nbsp;</td>
								<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
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
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="userId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
     footShow="" getValueType="getValueByProperty" collection="${page.result}" page="${page}" showSearch="false">
     <webflex:flexCheckBoxCol caption="选择" property="userId" showValue="userName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
      	<webflex:flexTextCol caption="用户编号" property="userSyscode" showValue="userSyscode" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol> 
		<webflex:flexTextCol caption="用户姓名" property="userName" showValue="userName" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="登录账号" property="userLoginname" showValue="userLoginname" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
</s:form>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	$("input:checkbox").parent().hide();
	sMenu.registerToDoc(sMenu);
	//var item = null;
	//item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addUser",1,"ChangeWidthTable","checkMoreDis");
	//sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
</script>
<script language="javascript">
function addTempFile(){ 
     var id=document.getElementById("groupSyscode").value;
     if(id==null || id==""){
	id='';
	}

	var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!insert.action?code="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:340px');
	
}
function editTempFile(){
     var id=document.getElementById("groupId").value;
     if(id==null || id==""){
	alert("请选择需要编辑的用户组。");
	return;
	}
	 var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!input.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:340px');
 
}

function delTempFile(){
	 var id=document.getElementById("groupId").value;
	  if(id==null || id==""){
	alert("请选择用户组。");
	return;
	}
	if(confirm("执行此操作将要把他的子节点一块删除，您确定要删除吗？")){
		 parent.propertiesTree.location="<%=path%>/usergroup/baseGroup!delete.action?groupId="+id;
 	}
}
function addClearance(){
	 var id=document.getElementById("groupId").value;
	  if(id==null || id==""){
	alert("请选择用户组。");
	return;
	}
	 var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!setGroupPrivil.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	 if(result){
	 	if(result == "OK"){
		 	alert("权限设置成功。");
	 	} else if(result == "NO"){
		 	alert("权限设置失败。");
	 	}
	 } 
}
function addUser(){
	 var id=document.getElementById("groupId").value;
	  if(id==null || id==""){
	alert("请选择用户组。");
	return;
	}	
	//var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!adduser.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!adduerNew.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	if(result=='succss'){
     location="<%=path %>/usergroup/baseGroup!userList.action?groupId="+id;
     }

}
function copyClearance(){
		  var id=document.getElementById("groupId").value;
		   if(id==null || id==""){
	     alert("请选择用户组。");
	   return;
	}
	if(confirm("您要将此组的资源赋给其他组吗？")){	
	 window.showModalDialog("<%=path%>/usergroup/baseGroup!copyprivil.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}	
}
</script>
</BODY>
</HTML>

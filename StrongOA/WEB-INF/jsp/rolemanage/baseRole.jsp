<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ include file="/common/include/rootPath.jsp" %>
<%@ page import="java.util.*"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
	</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/rolemanage/baseRole.action">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="40" class="table_headtd">
         <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
          	<td class="table_headtd_img" >
				<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
			</td>
            <td algin="left">
            	<strong>角色列表</strong>
            </td>
            <td algin="right">
	            <table border="0" align="right" cellpadding="0" cellspacing="0">
	                <tr>
		               	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="addTempFile();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="editTempFile();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="delTempFile();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="addUser();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添用户&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="addClearance();"><img src="<%=root%>/images/operationbtn/Resource_settings.png"/>&nbsp;资源设置&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="copyClearance();"><img src="<%=root%>/images/operationbtn/Resource_replication.png"/>&nbsp;资源复制&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		<td width="5"></td>		
	                </tr>
	            </table>
            </td>
          </tr>
        </table>
        </td>
      </tr>
    </table>
     <webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="roleId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" 
     footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
		 <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
         	<tr>
	         	<td>
					<div style="float: left; ">
	         		&nbsp;&nbsp;角色编码：&nbsp;<input name="selectroleId" id="selectroleId" type="text" class="search"  title="请输入角色编码" value="${selectroleId}">
		       		</div>
					<div style="float: left; ">
		       		&nbsp;&nbsp;角色名称：&nbsp;<input name="selectrolename" id="selectrolename" type="text"   class="search" title="请输入角色名称" value="${selectrolename}">
		       		</div>
					<div style="float: left; padding-top:5px;width: 170px;">
		       		&nbsp;&nbsp;状态：&nbsp;<s:select name="roleisact"   list="#{'':'是否启用','1':'是','0':'否'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
		       		</div>
					<div style="float: left;width: 335px ">
		       		&nbsp;&nbsp;角色描述：&nbsp;<input name="roledesc" id="roledesc"   type="text" class="search" title="请输入角色名称" value="${roledesc}">
		       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button"/>
		       		</div>
	         	</td>
         	</tr>
     	</table>
		<webflex:flexCheckBoxCol caption="选择" property="roleId" showValue="roleSyscode" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="角色编码" property="roleSyscode" showValue="roleSyscode" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="角色名称" property="roleName" showValue="roleName" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexEnumCol caption="是否启用" mapobj="${useMap}" property="roleIsactive" showValue="roleIsactive" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
		<webflex:flexTextCol caption="角色描述" property="roleDescription" showValue="roleDescription" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="分配人员" property="rest3" showValue="rest3" width="55%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTempFile",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
//	sMenu.addLine();
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","添用户","addUser",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Resource_settings.png","资源设置","addClearance",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Resource_replication.png","资源复制","copyClearance",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addLine();
	//item = new MenuItem("<%=frameroot%>/images/bianji.gif","LDAP同步","",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);		
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addTempFile(){

	var result=window.showModalDialog("<%=path%>/rolemanage/baseRole!input.action",window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
}
function editTempFile(){
	var id=getValue();
	
	if(id==null || id==""){
		alert("请选择要编辑的记录。");
		return;
	}
	if(id.split(",").length > 1){
		alert("只可以编辑一条记录。");
		return;
	}
	var result=window.showModalDialog("<%=path%>/rolemanage/baseRole!input.action?roleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
	
}

function delTempFile(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录。");
			return;
		}
	if(confirm("确定要删除吗？")){
 		location="<%=path%>/rolemanage/baseRole!delete.action?roleId="+id;
 	}
}
function addClearance(){
	
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要资源设置的记录。");
			return;
		}
	if(id.split(",").length > 1){
		alert("只可以对一条记录进行资源设置。");
		return;
	}
 	window.showModalDialog("<%=path%>/rolemanage/baseRole!setRolePrivil.action?roleId="+id+"&timestamp="+new Date(),window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	
}
function copyClearance(){
          var id=getValue();
	if(id==null || id==""){
		alert("请选择要资源复制的记录。");
		return;
	}
	if(id.split(",").length > 1){
		alert("只可以对一条记录进行资源复制。");
		return;
	}
	if(confirm("确定要将此角色的资源赋给其他角色吗？")){
	   window.showModalDialog("<%=path%>/rolemanage/baseRole!copyprivil.action?roleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:900px; dialogHeight:500px');
	}	
}

function addUser(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要添加用户的记录。");
			return;
		}
	if(id.split(",").length > 1){
		alert("只可以对一条记录进行添加用户。");
		return;
	}
	var result=window.showModalDialog("<%=path%>/rolemanage/baseRole!adduerNew.action?roleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	//var result=window.showModalDialog("<%=path%>/rolemanage/baseRole!adduser.action?roleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px'); 
}

function submitForm(){
	document.getElementById("myTableForm").submit();
}
$(document).ready(function(){
    $("#img_sousuo").click(function(){
    	$("form").submit();
    	gotoPage(1);	
    	
    });     
 });
</script>
</BODY>
</HTML>

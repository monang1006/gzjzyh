<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<HTML><HEAD><TITLE>用户组树</TITLE>
<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
var hasSelectCode = "";
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
  	<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
	<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
	<script src="<%=path %>/uums/js/JTableTree.js"></script>
	<script src="<%=path %>/uums/js/JTableTreeHelp.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<style>
	
	 #treecontentborder{ background-color:#eeeff3 !important;border:none;}
     .JTT_STable{
	width:100%;}

</style>
</HEAD>
<BODY  class=contentbodymargin leftmargin="2" topmargin="5" style="background-color:#eeeff3;">
<script>
	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = new MenuItem("<%=root%>/images/operationbtn/add.png","添加组","addTempFile",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑组","editTempFile",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);

		item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除组","delTempFile",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/Add_user.png","添用户","addUser",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		
		item = new MenuItem("<%=root%>/images/operationbtn/Resource_settings.png","资源设置","addClearance",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		
		item = new MenuItem("<%=root%>/images/operationbtn/Resource_replication.png","资源复制","copyClearance",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		
		
		sMenu.addLine();

		item = new MenuItem("<%=root%>/images/operationbtn/Move.png","上移","moveUp",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/Down.png","下移","moveDown",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		//item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","LDAP同步","moveDown",1,"JTreeNode","checkSelected");
		//sMenu.addItem(item);
			sMenu.addShowType("JTreeNode");
		sMenu.addShowType("body");
	}
</script>
<table width="100%">
<tr>
<td>
<DIV id=treecontentborder style=" width:100%;">
	<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto; width:100%;"
							onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
</DIV>
</td>
</tr>
</table>
</BODY></HTML>

<script language="javascript">
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();


mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");


mTreeData.addHead(new Array("用户组"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("150");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");


window.onload = window_onload;

function window_onload()
{
	initMenuT();
	ReShowTree();

}

function ReShowTree() {
   if(mTree == null)
   {
		//mTree =new JTableTreeClass(divMain,mTreeData);
                mTree =new JTableTreeClass("mTree",divMain,mTreeData,true,true,false,1,false);
		mTree.SetMenu(sMenu);
   }

	mTree.RebuildTree();
	mBuild = true;
}

function shownode(aShow) {

}

function ClickNode() {
   if(mTree.mSelected == null) return;
}
function ClickPlusMinus()
{


}

</script>
<c:forEach items="${groupList}" var="groupList">

<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${groupList.groupSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${groupList.groupSyscode}" />',code,
	                    new Array("<c:out value='${groupList.groupName}' />"),
	                     'parent.propertiesList.location="<%=root %>/usergroup/baseGroup!userList.action?groupId=<c:out value="${groupList.groupId}" />"',
	                      true,
	                     "", "folder_closed.gif", lStyleArr, '<c:out value="${groupList.groupId}" />','2');                              	                     
	</script>
</c:forEach>
	<script>
		function checkSelected(id){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			//alert(sNode.others)
			return true;
		}
function addTempFile(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.id;
	   var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!insert.action?code="+id,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:360px');
}

function editTempFile(){
	var sNode = mTree.GetTreeNode(mTree.mSelected );
	var id=sNode.others;
	  window.showModalDialog("<%=path%>/usergroup/baseGroup!input.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:360px');
}

function delTempFile(){
	var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
	if(confirm("执行此操作将要把他的子节点一块删除，您确定要删除吗？")){
		location="<%=path%>/usergroup/baseGroup!delete.action?groupId="+id;	
		//parent.propertiesList.location="<%=path %>/usergroup/baseGroup!userList.action";
	}
}

function addClearance(){
	var sNode = mTree.GetTreeNode(mTree.mSelected );
	var id=sNode.others;
	var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!setGroupPrivil.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	 if(result){
	 	if(result == "OK"){
		 	alert("权限设置成功。");
	 	} else if(result == "NO"){
		 	alert("权限设置失败。");
	 	}
	 } 
	//var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!addprivil.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
}

function addUser(){
     var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
	//用户组管理，右键添加用户和点击按钮添加用户页面不相同   0000050436	
	 //var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!adduser.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	 var result=window.showModalDialog("<%=path%>/usergroup/baseGroup!adduerNew.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');

      parent.propertiesList.location="<%=path %>/usergroup/baseGroup!userList.action?groupId="+id;
 
}
function copyClearance(){
    var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
		if(confirm("您要将此组的资源赋给其他组吗？")){		
	window.showModalDialog("<%=path%>/usergroup/baseGroup!copyprivil.action?groupId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
	}
}
function moveUp(){
		 var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
      location="<%=path %>/usergroup/baseGroup1!moveUp.action?groupId="+id;
}
function moveDown(){
    var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
      location="<%=path %>/usergroup/baseGroup1!moveDown.action?groupId="+id;

}
	</script>
	
	<script>
		/**
		 * 上下移动提示信息
		 */
		if("${message}" != "" && "${message}" != null){
			alert("${message}");
		}
	</script>	

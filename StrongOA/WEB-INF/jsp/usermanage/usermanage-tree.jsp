<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统树</TITLE>
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
 <style type="text/css">
     #treecontentborder{ background-color:#eeeff3 !important}
	 table.JTT_STable{width:100%;}
     </style>
</HEAD>
<BODY  class=contentbodymargin leftmargin="2" topmargin="5">
<script>
	var sMenu = new Menu("menu140");

	//初始化方法必须在<body></body>中

	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = new MenuItem("<%=root%>/images/operationbtn/daoru.png","导 入","importExcels",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/daochu.png","导 出","exportExcels",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		
		sMenu.addShowType("JTreeNode");
		sMenu.addShowType("body");
	}
</script>
<DIV id=treecontentborder>
<iframe scr='' id='tempframe' name='tempframe' style='display:none'></iframe>
<table width="100%">
<tr>
	<td>
	<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
	</td>
</tr>
</table></DIV>
</BODY></HTML>

<script language="javascript">
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();


mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");


mTreeData.addHead(new Array("组织机构"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("150");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");


window.onload = window_onload;

function window_onload(){
     initMenuT();
	 ReShowTree();
}

function ReShowTree() {
   if(mTree == null){
       mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,false);
       mTree.SetMenu(sMenu);
   }
	mTree.RebuildTree();
	mBuild = true;
}

function shownode(aShow) {}

function ClickNode() {
   if(mTree.mSelected == null) return;
}

</script>
<%--<c:forEach items="${orgList}" var="orgList">

<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />','<c:out value="${topOrgcodelength}"/>');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     'select()', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />",'2');                              	                     
	</script>

</c:forEach>

	--%>
	
	<c:forEach items="${orgList}" var="orgList">

<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />','<c:out value="${topOrgcodelength}"/>');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     'select()', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />",'2');      
	</script>

</c:forEach>
	<script>
		function checkSelected(id){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			//alert(sNode.others)
			return true;
		}
		
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			parent.userInfotContext.userlist.location="<%=root%>/usermanage/usermanage.action?extOrgId="+id;
		}
		
	//导入导出EXCEL数据
	function importExcels(){
	
	var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
		//alert(id);
	  var ret=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=usermanage/usermanage-import.jsp&excelOrgId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
	 // window.open ('<%=path%>/fileNameRedirectAction.action?toPage=organisemanage/organisemanage-import.jsp', 'newwindow', 'height=350, width=600, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes'); //这句要写成一
	 }
	 
	//导出EXCEL数据 
	 function exportExcels(){
	  var sNode = mTree.GetTreeNode(mTree.mSelected );
	    var id=sNode.others;
	    document.getElementById('tempframe').src="<%=path%>/usermanage/usermanage!exportExcels.action?excelOrgId="+id;
	  }

	</script>

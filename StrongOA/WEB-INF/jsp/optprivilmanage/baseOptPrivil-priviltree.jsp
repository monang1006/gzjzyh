<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统权限树</TITLE>
<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
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
</HEAD>
<BODY  class=contentbodymargin leftmargin="2" topmargin="5">

<script>
	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addTempFile",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/bianji.gif","修改","editTempFile",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);

		item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delTempFile",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		

		item = new MenuItem("<%=root%>/images/ico/move-up.gif","上移","moveUp",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/move-down.gif","下移","moveDown",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);
		//item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","LDAP同步","",1,"JTreeNode","checkSelected");
		//sMenu.addItem(item);
			sMenu.addShowType("JTreeNode");
		sMenu.addShowType("body");
	}
</script>
<DIV id=treecontentborder>
<table>
<tr>
	<td>
	<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
	</td>
</tr>
</table>
</DIV>
</BODY></HTML>

<script language="javascript">
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();


mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");


mTreeData.addHead(new Array("系统权限"),
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

 
  
	<script language="JavaScript">
	mTreeData.addTreeNode('g0','0',
	                    new Array("思创数码科技股份有限公司协同办公软件"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "g0",'0'); 
	                                                  	                     

	</script>
	 
	 
<c:forEach items="${privilList}" var="privilList">

<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${codeType}" />','g0');
	mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />',code,
	                    new Array("<c:out value='${privilList.privilName}' />"),
	                     'parent.PrivilContent.location="<%=root %>/optprivilmanage/baseOptPrivil!input.action?privilId=<c:out value="${privilList.privilId}" />"',
	                      true,
	                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.privilId}" />','2');                              	                     
	</script>

</c:forEach>
	<script>
	function checkSelected(id){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
		
			return true;
		}
		
	
	function checkSelectedPrivil(id){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		if(sNode.fid == '0'){
			return false;	
		}else{
			return true;
		}
	}
	
function addTempFile(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.id;
		var xx='';
		if(id=='g0'){
		 	xx='';
		}else{
			xx = id;
		}
	    parent.PrivilContent.location="<%=path%>/optprivilmanage/baseOptPrivil!insert.action?code="+xx;
		
            
}
function editTempFile(){
	var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
		var ids=sNode.id;
		var ss=ids.split('-');
		var xx=ss[1];
		if(xx=='0'){
		 alert("这是用户系统，不属于权限模块!!");
		 return;
		 }
	  parent.PrivilContent.location="<%=path%>/optprivilmanage/baseOptPrivil!input.action?privilId="+id;
}

function delTempFile(){
	var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
		var ids=sNode.id;
		var ss=ids.split('-');
		var xx=ss[1];
		if(xx=='0'){
		 alert("这是用户系统，不能删除!!");
		 return;
		 }
	if(confirm("执行此操作将要把他的子节点一块删除，您确定要删除吗？")){
	location="<%=path%>/optprivilmanage/baseOptPrivil!delete.action?privilId="+id;
 parent.PrivilContent.location="<%=path%>/optprivilmanage/baseOptPrivil!input.action";
 }	
	
}

		function moveUp(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			location ="<%=root%>/optprivilmanage/baseOptPrivil!moveUp.action?privilId="+id;
		}
		
		function moveDown(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			location ="<%=root%>/optprivilmanage/baseOptPrivil!moveDown.action?privilId="+id;
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

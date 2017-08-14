<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统权限树</TITLE>
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
	
	 #treecontentborder{ background-color:#eeeff3 !important}
     .JTT_STable{
	width:100%;}
	
</style>
</HEAD>
<BODY  class=contentbodymargin leftmargin="2" topmargin="5">

<script>
	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建资源类型","addPrivilType",1,"JTreeNode","checkSelectedPrivilType");
		sMenu.addItem(item);
		var item = new MenuItem("<%=root%>/images/operationbtn/edit.png","修改资源类型","editPrivilType",1,"JTreeNode","checkSelectedPrivilType");
		sMenu.addItem(item);
		var item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除资源类型","delPrivilType",1,"JTreeNode","checkSelectedPrivilType");
		sMenu.addItem(item);
		var item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addPrivil",1,"JTreeNode","checkSelectedSystem");
		sMenu.addItem(item);
		var item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTempFile",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/edit.png","修改","editTempFile",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);

		item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delTempFile",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);
		
		

		item = new MenuItem("<%=root%>/images/operationbtn/Move.png","上移","moveUp",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/Down.png","下移","moveDown",1,"JTreeNode","checkSelectedPrivil");
		sMenu.addItem(item);
		//item = new MenuItem("<%=root%>/images/ico/quanxankongzhi.gif","LDAP同步","",1,"JTreeNode","checkSelected");
		//sMenu.addItem(item);
		sMenu.addShowType("JTreeNode");
		sMenu.addShowType("body");
	}
</script>
<DIV id=treecontentborder>
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
</table>
</DIV>
</BODY></HTML>

<script language="javascript">
	var mTree =null ;
	var mTreeData =null;
	var mBuild = true;
	var myObject = new Object();
	
	
	mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");
	
	
	mTreeData.addHead(new Array("系统资源"),
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

<c:forEach items="${systemList}" var="systemlist">
 
  
	<script language="JavaScript">
		mTreeData.addTreeNode('<c:out value="${systemlist.sysId}" />','0',
		                    new Array("<c:out value='${systemlist.sysName}' />"),
		                     '', true,
		                     "", "folder_closed.gif", lStyleArr, "-2",'0'); 
	                                                  	                     
	</script>
	<%--<c:forEach items="${systemlist.basePrivilTypes}" var="privilTypeList">
		<script language="JavaScript">
			mTreeData.addTreeNode('<c:out value="${privilTypeList.typeId}" />','<c:out value="${privilTypeList.baseSystem.sysId}" />',
			                    new Array("<c:out value='${privilTypeList.typeName}' />"),
			                     '', true,
			                     "", "folder_closed.gif", lStyleArr, "-1",'0'); 
		</script>
	</c:forEach>--%>
</c:forEach>
	 
	 
<c:forEach items="${privilList}" var="privilList">

	<script language="JavaScript">
		myObject = new Object();
		var code = findFatherCode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${codeType}" />','0');
		if(code == '0'){
			mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${privilList.basePrivilType.baseSystem.sysId}" />',
		                    new Array("<c:out value='${privilList.privilName}' />"),
		                     'parent.PrivilContent.location="<%=root %>/privilmanage/basePrivil!input.action?privilId=<c:out value="${privilList.privilId}" />"',
		                      true,
		                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.basePrivilType.typeId}" />-<c:out value="${privilList.privilId}" />','2');
		}else{
			mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />',code,
			                    new Array("<c:out value='${privilList.privilName}' />"),
			                     'parent.PrivilContent.location="<%=root %>/privilmanage/basePrivil!input.action?privilId=<c:out value="${privilList.privilId}" />"',
			                      true,
			                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.basePrivilType.typeId}" />-<c:out value="${privilList.privilId}" />','2');
		}
	</script>
</c:forEach>
	<script>
	//判断所选节点是否是资源类型节点
	function checkSelectedPrivil(id){
		var sNode = mTree.GetTreeNode(mTree.mSelected);
		var flag = sNode.others;
		if(flag == '-1' || flag == '-2'){
			return false;
		}
		return true;
	}
		
	//判断所选节点是否是资源权限节点
	function checkSelectedPrivilType(id){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var flag = sNode.others;
		if(flag == '-1'){
			return true;
		}
		return false;
	}
	
	//判断所选节点是否是应用系统节点
	function checkSelectedSystem(id){
		var sNode = mTree.GetTreeNode(mTree.mSelected);
		var flag = sNode.others;
		if(flag == '-2'){
			return true;
		}
		return false;
	}
	
	function addTempFile(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id = sNode.others;
		var code = sNode.id;

		parent.PrivilContent.location="<%=path%>/privilmanage/basePrivil!insert.action?viewChangeFlag=" + viewChangeFlag + "&code="+code;
	}
	
	function editTempFile(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id = sNode.others;
		var ss=id.split('-');
		var xx=ss[1];
		parent.PrivilContent.location="<%=path%>/privilmanage/basePrivil!input.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+xx;
	}
	
	function delTempFile(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var id=sNode.others;
		var ss=id.split('-');
		var xx=ss[1];

		if(confirm("执行此操作将要把他的子节点一块删除，您确定要删除吗？")){
			location="<%=path%>/privilmanage/basePrivil!delete.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+xx;
		 	parent.PrivilContent.location="<%=path%>/privilmanage/basePrivil!input.action?viewChangeFlag=" + viewChangeFlag;
	 	}	
	}

		function moveUp(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			var ss=id.split('-');
			var xx=ss[1];
			location ="<%=root%>/privilmanage/basePrivil1!moveUp.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+xx;
		}
		
		function moveDown(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			var ss=id.split('-');
			var xx=ss[1];
			location ="<%=root%>/privilmanage/basePrivil1!moveDown.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+xx;
		}
		function moveUp1(id){
			location ="<%=root%>/privilmanage/basePrivil1!moveUp.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+id;
			parent.PrivilContent.location="<%=path%>/privilmanage/basePrivil!insert.action";
		}
		
		function moveDown1(id){
			location ="<%=root%>/privilmanage/basePrivil1!moveDown.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+id;
			parent.PrivilContent.location="<%=path%>/privilmanage/basePrivil!insert.action";
		}
		//在资源类型下面添加资源权限信息
		function addPrivil(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var typeId = sNode.id;
	
			parent.PrivilContent.location="<%=path%>/privilmanage/basePrivil!insert.action?viewChangeFlag=" + viewChangeFlag;
		}
		function changPrivilTypeView(){
			parent.PrivilContent.changPrivilTypeView();
		}
	</script>
	
	<script>
		/**
		 * 上下移动提示信息
		 */
		if("${message}" != "" && "${message}" != null){
			alert("${message}");
		}
		var viewChangeFlag = "systemTree"; 
	</script>

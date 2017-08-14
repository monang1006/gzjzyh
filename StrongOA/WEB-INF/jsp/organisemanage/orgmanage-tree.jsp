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
	<script type="text/javascript" language="javascript"
	 	src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
     <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
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
		var item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addOrg",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		var item = new MenuItem("<%=root%>/images/operationbtn/Post_setting.png","设置岗位","selectOrgPost",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delOrg",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/Reduction.png","还原","reductOrg",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);		
		sMenu.addLine();
		item = new MenuItem("<%=root%>/images/operationbtn/Move.png","上移","moveUp",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/Down.png","下移","moveDown",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		//item = new MenuItem("<%=root%>/images/ico/move-down.gif","LDAP同步","",1,"JTreeNode","checkSelected");
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

function window_onload()
{
	initMenuT();
	ReShowTree();

}

function ReShowTree() {
   if(mTree == null)
   {
		//mTree =new JTableTreeClass(divMain,mTreeData);
                mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,false);
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
<c:forEach items="${orgList}" var="orgList">

<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />','<c:out value="${topOrgcodelength}"/>');
	
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     'select()', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />" + "_" + "<c:out value='${orgList.orgIsdel}' />"+ "_" + "<c:out value='${orgList.isOrg}' />",'2');                              	                     
	</script>

</c:forEach>

	<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split("_")[0];
			//parent.organiseInfo.location="<%=root%>/organisemanage/orgmanage!input.action?orgId="+id;
			parent.organiseInfo.setOrgId(id);
		}
		
		function addOrg(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var isDel = sNode.others.split("_")[1];
			if(isDel == "1"){
				alert("组织机构[" + sNode.TextArray[0] + "]已删除，不允许进行添加操作。");
				return false;
			}
			var code = sNode.id;
			var id = sNode.others.split("_")[0];
			 $.post("<%=path%>/organisemanage/orgmanage!isHasOrg.action",
		           {"orgId":id},
		           function(data){
		           		if(data=="true"){
							parent.organiseInfo.location ="<%=path%>/fileNameRedirectAction.action?toPage=/organisemanage/orgmanage-container.jsp?orgSysCode="+code+"&operate=add&orgId="+id+"&audittype=1";
							
						}else{
							alert("部门组织机构下不能创建组织机构。");
							return false;
						}
		           });
			
			//alert("code:"+code+"id:"+id);
			//parent.organiseInfo.location ="<%=root%>/organisemanage/orgmanage!addchild.action?orgSysCode="+code+"&orgId="+id;
		}
		
		function delOrg(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			
			if("001"==sNode.id){
				alert("不能删除顶级机构。");
				return;
			}
			var isDel = sNode.others.split("_")[1];
			if(isDel == "1"){
				alert("组织机构[" + sNode.TextArray[0] + "]已删除，不允许进行删除操作。");
				return false;
			}
			var id = sNode.others.split("_")[0];
			if(confirm("该操作将导致该组织机构下子级组织机构和用户也被删除,确定删除组织机构吗？")) 
			{ 
				location ="<%=root%>/organisemanage/orgmanage!delete.action?orgId="+id+"&audittype=1";
			} 
		}
		
		function reductOrg(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var isDel = sNode.others.split("_")[1];
			if(isDel == "0"){
				alert("组织机构[" + sNode.TextArray[0] + "]未被删除，不允许进行还原操作。");
				return false;
			}
			var id = sNode.others.split("_")[0];
			if(confirm("是否将该组织机构下的下级机构和所有用户一起还原？")){
				location ="<%=root%>/organisemanage/orgmanage!reduction.action?orgId="+id+"&audittype=1&userTogether=1";
			}else{
				location ="<%=root%>/organisemanage/orgmanage!reduction.action?orgId="+id+"&audittype=1&userTogether=0";
			}
		}
		
		function moveUp(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split("_")[0];
			location ="<%=root%>/organisemanage/orgmanage1!moveUp.action?orgId="+id;
		}
		
		function moveDown(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split("_")[0];
			location ="<%=root%>/organisemanage/orgmanage1!moveDown.action?orgId="+id;
		}
		
		//初始化组织机构岗位设置信息
		function selectOrgPost(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var isDel = sNode.others.split("_")[1];
			if(isDel == "1"){
				alert("组织机构[" + sNode.TextArray[0] + "]已删除，不允许进行岗位设置操作。");
				return false;
			}
			var id = sNode.others.split("_")[0];
			window.showModalDialog("<%=root%>/organisemanage/orgmanage!initSelectOrgPost.action?orgId="+id+"&audittype=get",window,'help:no;status:no;scroll:no;dialogWidth:520px; dialogHeight:250px');
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

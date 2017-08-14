<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>系统树</TITLE>
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
		var item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addOrg",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","eidtOrg",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","delOrg",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/goujian.gif","移动","tomove",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);		
		sMenu.addLine();
		item = new MenuItem("<%=root%>/images/ico/move-up.gif","合并","merge",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/move-down.gif","导入","moveDown",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/move-down.gif","导出","",1,"JTreeNode","checkSelected");
		sMenu.addItem(item);
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
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     'select()', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgid}' />",'2');       
	                     
	                                         	                     
	</script>

</c:forEach>

	<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split(',');
			parent.organiseInfo.location="<%=root%>/personnel/structure/personStructure.action?orgId="+id+"&audittype=structure";
		}
		
		function addOrg(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var code = sNode.id;
			var id = sNode.others.split(',');
			parent.organiseInfo.location ="<%=root%>/personnel/personorg/personOrg!addchild.action?orgSysCode="+code+"&orgId="+id[0];
		}
		
		function delOrg(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split(',');
			if(confirm("该操作将导致该组织机构下子级组织机构和用户也被删除,确定删除组织机构吗?")) 
			{ 
				location ="<%=root%>/personnel/personorg/personOrg!delete.action?orgId="+id[0]+"&audittype=1";
			} 
		}
		
		//合并机构
		function eidtOrg(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split(',');
			parent.organiseInfo.location="<%=root%>/personnel/personorg/personOrg!input.action?orgId="+id[0];
		}
		
		//移动机构
		function tomove(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split(',');
			var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!radiotree.action?orgId="+id[0],window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		    if(audit==undefined||audit==null){
		      return false;
		    }else  if(id[0]==audit[0]){
                 alert("不可以自己移动到自己下面！");
                 return false;		    
		    }
		    if(confirm(id[1]+"移动到"+audit[1]+"下，"+id[1]+"下的子机构也会跟着移动。确定要移动吗？")){
		      location="<%=root%>/personnel/personorg/personOrg!tomove.action?moveOrgId="+audit[0]+"&orgId="+id[0];
		    }else{
		    return false;
		    }
		}
		//合并机构
		function merge(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split(',');
			var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!selectTree.action?orgId="+id[0],window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		alert(audit);
		    if(audit==undefined||audit==null){
		      return false;
		    }
	      if(confirm("确定要合并吗？")){
		     parent.organiseInfo.location="<%=root%>/personnel/personorg/personOrg!merge.action?moveOrgId="+audit[0]+"&orgId="+id[0];
		    }else{
		    return false;
		    }
		}
		
	/*	function moveUp(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			location ="<%=root%>/organisemanage/orgmanage1!moveUp.action?orgId="+id;
		}
		
		function moveDown(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others;
			location ="<%=root%>/organisemanage/orgmanage1!moveDown.action?orgId="+id;
		}
		*/
	</script>
	
	<script>
		/**
		 * 上下移动提示信息
		 */
		if("${message}" != "" && "${message}" != null){
			alert("${message}");
		}
	</script>	

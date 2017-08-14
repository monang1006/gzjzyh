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
	<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script src="<%=path %>/uums/js/JTableTree.js"></script>
	<script src="<%=path%>/uums/js/JTableTreeHelp.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
</HEAD>
<BODY  class=contentbodymargin leftmargin="2" topmargin="5">
<script>
	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		
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


mTreeData.addHead(new Array("机构名称","机构编号"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("240","130");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");


window.onload = window_onload;

function window_onload()
{
	//initMenuT();
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
	                    new Array("<c:out value='${orgList.orgName}'/>","<c:out value='${orgList.orgSyscode}'/>"),
	                     'select()', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgid}' />,<c:out value='${orgList.orgName}' />",'2');       
	                     
	                                         	                     
	</script>

</c:forEach>

	<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var id = sNode.others.split(',');
			var code=id[0];
			var name=id[1];
		
  window.dialogArguments.document.getElementById("orgid").value=code;
   window.dialogArguments.document.getElementById("sorgName").value=name;
		   window.close();
		}
		
		function addOrg(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var code = sNode.id;
			var id = sNode.others.split(',');
			parent.organiseInfo.location ="<%=root%>/personnel/personorg/personOrg!addchild.action?orgSysCode="+code+"&orgId="+id[0];
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

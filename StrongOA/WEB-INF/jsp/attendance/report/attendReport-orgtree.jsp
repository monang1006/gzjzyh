<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>系统树</TITLE>
		<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
</script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/JTableTree.css"
			rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
		<script src="<%=path%>/uums/js/JTableTree.js"></script>
		<script src="<%=path%>/uums/js/JTableTreeHelp.js"></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
	</HEAD>
	<BODY class=contentbodymargin leftmargin="2" topmargin="5">

		<DIV id=treecontentborder>
			<table>
				<tr>
					<td>
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;" onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

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
	      mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,false);
		
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
		parent.recordList.location="<%=root%>/fileNameRedirectAction.action?toPage=attendance/report/attendReport-content.jsp?orgId="+id+"&reportType=${param.reportType }";
	}
	/**
	 * 上下移动提示信息
	 */
	if("${message}" != "" && "${message}" != null){
		alert("${message}");
	}
</script>

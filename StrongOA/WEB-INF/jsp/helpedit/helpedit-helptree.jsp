<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>系统帮助树</title>
		
		<script type="text/javascript">
		var imageRootPath='<%=path%>/common/frame';
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
		</script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type=text/css
			rel=stylesheet>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/navigator_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/JTableTree.css"
			rel=stylesheet>
		<link href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
		<link href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
		<script src="<%=path%>/uums/js/JTableTreeHelp.js"></script>
		<script src="<%=path%>/uums/js/JTableTree.js"></script>
		<script language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script>

<script language="javascript">
	var mTree =null ;
	var mTreeData =null;
	var mBuild = true;
	var myObject = new Object();
		
	mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");
		
	mTreeData.addHead(new Array("帮助设置"));
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
	
	function ClickPlusMinus(){	
	}
</script>
<style>
	
	 #treecontentborder{ background-color:#eeeff3 !important}
     .JTT_STable{
	width:100%;}
</style>
	</head>
	
	
	<body class=contentbodymargin leftmargin="2" topmargin="5">
		<script>
			var sMenu = new Menu("menu140");
			
			//初始化方法必须在<body></body>中
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				sMenu.addShowType("JTreeNode");
				sMenu.addShowType("body");
			}
		</script>
		
		<div id=treecontentborder>
			<table width="100%">
				<tr>
					<td>
						<div id="divMain" 
							style="overflow :auto;" onClick="return ( mTree.HTOnClick());">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>

<c:forEach items="${systemList}" var="systemlist">

	<script language="JavaScript">
		mTreeData.addTreeNode('<c:out value="${systemlist.sysId}" />','0',
		                    new Array("<c:out value='${systemlist.sysName}' />"),
		                     '', true,
		                     "", "folder_closed.gif", lStyleArr, "-2",'0'); 	                                                  	                     
	</script>
</c:forEach>


<c:forEach items="${privilList}" var="privilList">

	<script language="JavaScript">
		myObject = new Object();
		var code = findFatherCode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${codeType}" />','0');
		if(code == '0'){
			mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${privilList.basePrivilType.baseSystem.sysId}" />',
		                    new Array("<c:out value='${privilList.privilName}' />"),
		                     'parent.PrivilContent.location="<%=root%>/privilmanage/basePrivil!input.action?privilId=<c:out value="${privilList.privilName}" />"',
		                      true,
		                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.basePrivilType.typeId}" />-<c:out value="${privilList.privilId}" />','2');
		}else{
			mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />',code,
			                    new Array("<c:out value='${privilList.privilName}' />"),
			                     'parent.PrivilContent.location="<%=root%>/privilmanage/basePrivil!input.action?privilId=<c:out value="${privilList.privilId}" />"',
			                      true,
			                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.basePrivilType.typeId}" />-<c:out value="${privilList.privilId}" />','2');
		}
	</script>
</c:forEach>



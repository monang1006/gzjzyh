<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>请选择委托人</TITLE>
		<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
</script>

		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
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
	<BODY class=contentbodymargin>
		<script>
/** 显示/隐藏加载信息
 * @param bl true/false
 */
function showLoading(bl){
	var state = bl ? "block" : "none";
	LOADING.style.display = state;
}

	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	function initMenuT(){
		sMenu.registerToDoc(sMenu);

		sMenu.addShowType("JTreeNode");
		sMenu.addShowType("body");
	}
	
	//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
	var hasSelectCode = "";
</script>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%"
				height="100%" style="vertical-align: top;">
				<tr>
					<td height="40"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00"
							style="vertical-align: top;">
							<tr>
								<td width="5%" align="center">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
								</td>
								<td width="35%">
									请选择委托人
								</td>
								<td width="60%">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign=top align="center">
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;" onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
					</td>
				</tr>

				<tr>
					<td valign='top' height='10' colspan="2">
						<img src='/StrongUUMS/common/images/keyline.gif' height='2'
							width='100%' border=0 align='top' />
					</td>
				</tr>
				<tr>
					<td align="center">
						<table>
							<tr>
								<td>
									<input type="button" value="关闭" class="input_bg"
										onclick="javascript:window.close();" />
								</td>
							</tr>
						</table>
					<td>
				</tr>
			</table>
		</div>
		<!--bodyEnd-->
		<DIV id=LOADING onClick="showLoading(false)"
			style="position: absolute; top: 30%; left: 38%; display: none"
			align=center>
			<font color=#16387C><strong>正在加载，请稍候...</strong> </font>
			<br>
			<IMG src="<%=frameroot%>/images/tab/loading.gif">
		</DIV>

	</body>
</html>

<script language="javascript">
showLoading(true);
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();


mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");


mTreeData.addHead(new Array("人员姓名","人员账号"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("150","150");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");


window.onload = window_onload;

function window_onload()
{
	initMenuT();
	ReShowTree();
	showLoading(false);
}

function ReShowTree() {
   if(mTree == null)
   {
		//mTree =new JTableTreeClass(divMain,mTreeData);
        mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,true,null,null,null);
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
<c:forEach items="${userLst}" var="userLst">
	<script language="JavaScript">
	mTreeData.addTreeNode('<c:out value="${userLst.userId}" />','0',
	                    new Array("<c:out value='${userLst.userName}' />","<c:out value='${userLst.userLoginname}'/>"),
	                     'select();', true,
	                     "", "renyuan.gif", lStyleArr, '<c:out value="${userLst.userName}" />','2');                            	                     
	</script>
</c:forEach>

<script>
		function checkSelected(id){
			return true;
		}
		
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var sid = sNode.id + "," + sNode.others;
			window.returnValue = sid;
			window.close();
		}
	</script>

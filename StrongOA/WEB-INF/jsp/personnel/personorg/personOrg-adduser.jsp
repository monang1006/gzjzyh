<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>用户树</TITLE>
		<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
</script>

		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/JTableTree.css"
			rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
		<script src="<%=path%>/uums/js/JTableTree.js"></script>
		<script src="<%=path%>/uums/js/JTableTreeHelp.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<base target=_self>
	</HEAD>
	<BODY class=contentbodymargin leftmargin="2" topmargin="5">
		<script>
	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	function initMenuT(){
	}
</script>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%"
				height="100%" style="vertical-align: top;">
				<form action="<%=path%>"
					method="post">
					
				</form>
				<tr>
					<td height="40"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00"
							style="vertical-align: top;">
							<tr>
								<td>&nbsp;</td>
								<td width="60%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									请选择用户
								</td>
								<td width="5%">
									&nbsp;
								</td>
								<td width="30%">
									<table width="100%" border="0" align="right" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="*">
												&nbsp;
											</td>

											<td width="50">
												<input type="button" value="确 定" class="input_bg"
													onclick="selectprivils();" />
											</td>

											<td width="50">
												<input type="button" value="取 消" class="input_bg"
													onclick="javascript:window.close();" />
											</td>
											<td width="13">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign=top align="center">
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;" onclick="return ( mTree.OnClick());"
							onclicknode="ClickNode();" onclickplusminus="ClickPlusMinus();"
							onshownode="shownode(true);" onresizeend=""
							onhidenode="shownode(false);">
						</div>
					</td>
				</tr>

				<tr>
					<td valign='top' height='10' colspan="2">
						<img src='<%=frameroot%>/images/keyline.gif' height='2'
							width='100%' border=0 align='top' />
					</td>
				</tr>
				<tr>
					<td valign="top" align="center">
						<table>
							<tr>
								<td>
									<input type="button" value="确 定" class="input_bg"
										onclick="selectprivils();" />
								</td>
								<td>
									<input type="button" value="取 消" class="input_bg"
										onclick="javascript:window.close();" />
								</td>
							</tr>
						</table>
					<td>
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


mTreeData.addHead(new Array("组织机构"),null, null,null,null);
mTreeData.HeadCellWidthArr=new Array("360");

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
                mTree =new JTableTreeClass("mTree",divMain,mTreeData,true,true,false,1,true);
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
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgid}' />",'2'); 
	                                                  	                     

	</script>
</c:forEach>

<c:forEach items="${userList}" var="userList">

	<script language="JavaScript">
			
			mTreeData.addTreeNode('<c:out value="${userList.personid}" />','<c:out value="${userList.baseOrg.orgSyscode}" />',
	                    new Array("<c:out value='${userList.personName}' />"),
	                     '', true,
	                     "", "renyuan.gif", lStyleArr, "<c:out value='${userList.personName}' />",'0'); 
	                                                  	                     
	</script>


</c:forEach>
<script>
		function checkSelected(id){
			return true;
		}
		function selectprivils(){
			var id = getAllCheckedValue(mTree);
			
			 window.dialogArguments.document.getElementById("persons").value=id;
			
			  $.post(
	          "<%=path%>/personnel/personorg/personOrg!pubUsernames.action",
		     {personIds:id},
		     function(data){
		     
		   window.dialogArguments.document.getElementById("personsName").value=data;
		   window.close();
		}
		
	)

			
     
     
				//document.forms[0].submit();
				
			
		}
		
	
	</script>

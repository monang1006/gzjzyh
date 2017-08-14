<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>请选择机构负责人</TITLE>
		<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
var hasSelectCode = "";
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
		<script src="<%=path%>/uums/js/LazyLoadJTableTree.js"></script>
		<script src="<%=path%>/uums/js/JTableTreeHelp.js"></script>
	</HEAD>
	<BODY class=contentbodymargin>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%"
				height="100%" style="vertical-align: top;">
				<tr>
					<td height="40"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00"
							style="vertical-align: top;">
							<tr>
								<td>&nbsp;</td>
								<td width="35%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									请选择机构负责人
								</td>
								<td width="5%">
									&nbsp;
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


	</body>
</html>

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
mTreeData.HeadCellWidthArr=new Array("300");

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
                mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,false,null,null,true);
		//mTree.SetMenu(sMenu);
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
	//var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />');
	//mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	//                   new Array("<c:out value='${orgList.orgName}' />"),
	//                     '', true,
	//                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />",'2');
	//var code = '<c:out value="${orgList.orgParentId}" />';
	//if(code == null || code == ""){
	//	code = 0;
	//}
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />', '0',
	                   new Array("<c:out value='${orgList.orgName}' />"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />",'2'); 	                                                  	                     

	</script>
</c:forEach>

<%--<c:forEach items="${userList}" var="userList">	
	
		<script language="JavaScript">
			
			mTreeData.addTreeNode('<c:out value="${userList.userId}" />','<c:out value="${userList.orgId}" />',
	                    new Array("<c:out value='${userList.userName}' />"),
	                     'select()', true,
	                     "", "renyuan.gif", lStyleArr, "<c:out value='${userList.userName}' />",'2'); 
	                                                  	                     
	</script>
	

</c:forEach>--%>


<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var sid = sNode.id;
			window.dialogArguments.setUserIdName(sid.split(",")[0],sid.split(",")[1]);
			window.close();
		}
		
		//子级组织机构和人员树延迟加载
		function lazyLoadSubTree(aNode, aExpandOrClose, isFilter){
			if(aNode.others == "user"){
				AfterJTTLazyExpandOrClose(mTree, aNode, aExpandOrClose,isFilter);
			}else{
				$.getJSON(scriptroot + "/organisemanage/orgmanage!lazyLoadUserAndChildOrgs.action",{orgSysCode: aNode.id}, function(jsons){
					$.each(jsons,function(i,json){
						if(json.type == "org"){
							aNode.subtree.addTreeNode(json.code,aNode.id,
			                    new Array(json.name),
			                     '', true,
			                     "", "folder_closed.gif", lStyleArr, 'org', '2');
			            }
					});
					$.each(jsons,function(i,json){
						if(json.type == "user"){
							aNode.subtree.addTreeNode(json.id + "," + json.name,aNode.id,
			                    new Array(json.name),
			                     'select();', true,
			                     "", "renyuan.gif", lStyleArr, 'user', '2');
			            }
					});
					AfterJTTLazyExpandOrClose(mTree, aNode, aExpandOrClose,isFilter);
				});
			}
		}
				
	</script>

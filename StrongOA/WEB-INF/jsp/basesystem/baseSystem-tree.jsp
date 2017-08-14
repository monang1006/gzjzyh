<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>系统权限树</TITLE>
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
	<base target=_self>	
	</HEAD>
	<BODY class=contentbodymargin leftmargin="2" topmargin="5"  >
		<script>
	var sMenu = new Menu("menu140");


	//初始化方法必须在<body></body>中

	//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
	var hasSelectCode = "";
</script>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%"
				  style="vertical-align: top;">
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00"  style="vertical-align: top;">
          <tr>
            <td>&nbsp;</td>
            <td width="30%">
            <img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
            	请选择机构下面的用户
            </td>
            <td width="5%">&nbsp;</td>
            <td width="60%"><s:form id="getuserform" action="/basesystem/baseSystem!saveUsers.action">
			<input type="hidden" name="userId" id="userId" >
			<input type="hidden" id="sysId" name="sysId" >
</s:form>
            </td>
          </tr>
        </table>
        </td>
      </tr>			
				<tr>
					<td>
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());"
							onresizeend="">
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
								<td width="30%">
									<input name="Submit2" type="button" class="input_bg" value="保 存" onclick="selectuserid();">
								</td>
								<td width="30%">
									<input name="Submit" type="button" class="input_bg" value="取 消" onclick="javascript:window.close();">
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
mTreeData.HeadCellWidthArr=new Array("380");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");


window.onload = window_onload;

function window_onload()
{
	ReShowTree();
	setfirstId();
	
}

function ReShowTree() {
   if(mTree == null)
   {
		//mTree =new JTableTreeClass(divMain,mTreeData);
                mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,true);
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
		function setfirstId(){
			 var fId = '${userPrivil}';
			 if(fId != ''){
			 	var id = fId.split(',');
			 	
			 	var obj = getAllCheckedObj(mTree);
		 		for(var i = 0; i < obj.length; i++){
		 			for(var j = 0; j <id.length; j++ ){
			 			if(obj[i].value == id[j]){
			 				obj[i].checked=true ;
		 				}
		 			}
		 		}
			 }
		}
		function selectuserid(){
			var id = getAllCheckedValue(mTree);
			if(id == ""||id==null){
				alert('请选择用户！！！');
				return;
			}
			
			var arr=id.split(",");
			var ss='';
			
		for(var i=0;i<arr.length;i++){
			if(arr[i].length<32){
				arr.splice(i,1);	
			}
			}
			ss=arr.toString();
		
	    window.returnValue="RELOAD";
		document.getElementById("userId").value = ss;
		document.getElementById("getuserform").submit();
		window.close();
		 
		}
	
</script>

<c:forEach items="${orgList}" var="orgList">
 
  
	<script language="JavaScript">
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />",'0'); 
	                                                  	                     

	</script>
	 </c:forEach>
	 
	<c:forEach items="${userList}" var="userList">	
	
		<script language="JavaScript">
			
			mTreeData.addTreeNode('<c:out value="${userList.userId}" />','<c:out value="${userList.baseOrg.orgSyscode}" />',
	                    new Array("<c:out value='${userList.userName}' />"),
	                     '', true,
	                     "", "renyuan.gif", lStyleArr, "<c:out value='${userList.userId}' />",'0'); 
	                                                  	                     
	</script>
	

</c:forEach>



<%----%>
<%--<script>--%>
<%--		function checkSelected(id){--%>
<%--			return true;--%>
<%--		}--%>
<%--		function select(){--%>
<%--			var sNode = mTree.GetTreeNode(mTree.mSelected );--%>
<%--			var id = sNode.others;--%>
<%--			parent.organiseInfo.location="<%=root%>/organisemanage/orgmanage!input.action?orgId="+id;--%>
<%--		}--%>
<%--		--%>
<%----%>
<%----%>
<%--	</script>--%>

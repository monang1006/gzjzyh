<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<HTML><HEAD><TITLE>请选择机构所属的区域</TITLE>
<script type="text/javascript">
var imageRootPath='<%=path%>/common/frame';
</script>
<%@include file="/common/include/meta.jsp"%>
  	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
  	<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
	<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
	<script src="<%=path %>/uums/js/JTableTree.js"></script>
	<script src="<%=path %>/uums/js/JTableTreeHelp.js"></script>
</HEAD>
<BODY  class=contentbodymargin >
<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%"
				height="100%"  style="vertical-align: top;">
				
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00"  style="vertical-align: top;">
          <tr>
            <td width="5%" align="center"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"></td>
            <td width="35%">设置用户组</td>
            <td width="5%">&nbsp;</td>
            <td width="60%"><s:form id="getpostform" action="/usermanage/usermanage!setUserGroup.action">
	<input type="hidden" name="groupCode" id="groupCode">
	<input type="hidden" name="userId" id="userId">
	<input type="hidden" id="orgId" name="orgId" value="${orgId}">
</s:form>
 <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
                 <tr>
                  <td width="*">&nbsp;</td>
                  	<td width="5"></td>
                  	<td width="50">
					<input type="button" value="全 选" class="input_bg"
										onclick="allSelected();" />
					</td>
                  	<td width="3"></td>
                  		<td width="50">
					<input type="button" value="确 定" class="input_bg"
										onclick="selectpostid();" />
					</td>
                  	<td width="3"></td>
                  		<td width="50">
									<input type="button" value="取 消" class="input_bg"
										onclick="javascript:window.close();" />
				</td>
				<td width="11">&nbsp;</td>
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
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());"
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
							<td width="30%">
									<input name="Submit2" type="button" class="input_bg" value="全 选" onclick="allSelected();">
								</td>
								<td width="30%">
									<input name="Submit2" type="button" class="input_bg" value="确 定" onclick="selectpostid();">
								</td>
								<td width="30%">
									<input name="Submit" type="button" class="input_bg" value="取 消" onclick="javascript:window.close();">
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


mTreeData.addHead(new Array("机构名称","机构编码"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("150","300");

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
                mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,true);
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
	myObject = new Object();
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />","<c:out value="${orgList.orgSyscode}" />"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgid}' />",'0');                              	                     
	</script>

</c:forEach>

	<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var sid = sNode.others.split(',');
			window.returnValue=sid;
		//	window.dialogArguments.setAreaCodeId(sid[0],sid[1]);
			window.close();
		}
		
		function addOrg(){
			
			//location="charactarInfo.jsp"
			parent.frames[1].window.location ="clearanceInfo.jsp?viewType=tree&id=001002003"
		}
		function editTempFile(){
			parent.frames[1].window.location="clearanceEdit.jsp?viewType=tree";
			//window.showModalDialog("charactarEdit.jsp",window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
		}
		function deleteOrg(){
		}
		function moveUp(){
		}
		function moveDown(){
		}		
		function selectpostid(){
			//var id = getAllCheckedValue(mTree);
			var id = getAllCheckedOthers(mTree);
			var userId = '${userId}' ;
			document.getElementById("groupCode").value = id;
			document.getElementById("userId").value = userId;
			//document.getElementById("getpostform").submit();
			returnValue=id;
			window.close();
		}
		
		function setfirstId(){
			 var fId = '${initGroup}';
			 if(fId != ''){
			 	var id = fId.split(',');
			 	var obj = getAllCheckedObj(mTree);
		 		for(var i = 0; i < obj.length; i++){
		 			for(var j = 0; j <id.length; j++ ){
			 			var node = mTree.GetTreeNode(obj[i].value);
			 			if(node.others == id[j]){
			 				obj[i].checked=true ;
		 				}
		 			}
		 		}
			 }
		}
	</script>

			<script>
		var selectFlag = true;
		function allSelected() {
			selectAll(selectFlag, mTree);
			selectFlag = !selectFlag;
		}
	</script>
	
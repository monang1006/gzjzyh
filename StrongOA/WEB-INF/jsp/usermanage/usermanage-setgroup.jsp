<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>设置用户组</TITLE>
		<script type="text/javascript">
		var imageRootPath='<%=path%>/common/frame';
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
		</script>

		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
		<script src="<%=path%>/uums/js/JTableTree.js"></script>
		<script src="<%=path%>/uums/js/JTableTreeHelp.js"></script>
	</HEAD>
	<base target="_self" />
	<BODY class=contentbodymargin leftmargin="2" topmargin="5">
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="20%" style="vertical-align: top;">
				<tr>
					<td class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00" style="vertical-align: top;">
							<tr>
								<td>&nbsp;</td>
								<td class="table_headtd_img">
									<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
								</td>
								<td align="left">
									<strong>设置用户组</strong>
								</td>
								<td align="right">
									<s:form id="getpostform" action="/usermanage/usermanage!setUserGroup.action">
										<input type="hidden" name="groupCode" id="groupCode">
										<input type="hidden" name="userId" id="userId">
										<input type="hidden" id="extOrgId" name="extOrgId" value="${extOrgId}">
									</s:form>
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
											<td class="Operation_input" onclick="allSelected();">&nbsp;全&nbsp;选&nbsp;</td>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
					                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
											<td class="Operation_input" onclick="selectpostid();">&nbsp;确&nbsp;定&nbsp;</td>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
											<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
											<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
											<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
					                  		<td width="6"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign=top align="center">
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());" style="overflow :auto;" onclick="return ( mTree.OnClick());" onresizeend="">
						</div>
					</td>
				</tr>
				<tr>
					<td align="center">						
					<td>
				</tr>
			</table>
		</div>
		<!--bodyEnd-->
	</body>
</HTML>
<script language="javascript">
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();

mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");

mTreeData.addHead(new Array("用户组名称","用户组编号"), null, null, null, null);
mTreeData.HeadCellWidthArr=new Array("240","130");

// 见样式表 bar.css
var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");

window.onload = window_onload;

function window_onload()
{
	//initMenuT();
	ReShowTree();
	setfirstId();
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
<c:forEach items="${userGoupList}" var="userGoupList">
	<script language="JavaScript">
		myObject = new Object();
		var code = findFatherCode('<c:out value="${userGoupList.groupSyscode}" />','<c:out value="${codeType}" />');
		mTreeData.addTreeNode('<c:out value="${userGoupList.groupSyscode}" />',code,
		                    new Array("<c:out value='${userGoupList.groupName}' />","<c:out value='${userGoupList.groupSyscode}' />"),
		                     '',
		                      true,
		                     "", "folder_closed.gif", lStyleArr, '<c:out value="${userGoupList.groupId}" />','0');                              	                     
	</script>
</c:forEach>
<script>
		function checkSelected(id){
			return true;
		}
		function selectpostid(){
			//var id = getAllCheckedValue(mTree);
			var id = getAllCheckedOthers(mTree);
			var userId = '${userId}' ;
			document.getElementById("groupCode").value = id;
			document.getElementById("userId").value = userId;
			document.getElementById("getpostform").submit();
			//window.close();
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
</script>
<script>
		var selectFlag = true;
		function allSelected() {
			selectAll(selectFlag, mTree);
			selectFlag = !selectFlag;
		}
</script>

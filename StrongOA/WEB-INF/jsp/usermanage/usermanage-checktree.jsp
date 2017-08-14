<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>请选择机构所属的区域</TITLE>
		<script type="text/javascript">
		var imageRootPath='<%=path%>/common/frame';
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
		</script>
		<%@include file="/common/include/meta.jsp"%>
	  	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	  	<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
		<script src="<%=path %>/uums/js/JTableTree.js"></script>
		<script src="<%=path %>/uums/js/JTableTreeHelp.js"></script>
	</HEAD>
	<BODY class=contentbodymargin>
		<DIV id=treecontentborder  style="overflow:hidden;">
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" style="vertical-align: top;">
		      <tr>
		        <td class="table_headtd">
			        <table width="100%" border="0" cellspacing="0" cellpadding="00"  style="vertical-align: top;">
			          <tr>
			            <td>&nbsp;</td>
			            <td class="table_headtd_img">
							<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
						</td>
			            <td align="left">
			            	<strong>请选择机构</strong>
			            </td>
			            <td align="right">
			            	<table border="0" align="right" cellpadding="00" cellspacing="0">
								<tr>
									<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									<td class="Operation_input" onclick="window.close()">&nbsp;确&nbsp;定&nbsp;</td>
									<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
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
				     <div style="width:100%; overflow-y:auto; height:375px;">
					<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());" style="overflow :auto;" onclick="return ( mTree.OnClick());" onresizeend="">
					</div>
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
</html>

<script language="javascript">
	var mTree =null ;
	var mTreeData =null;
	var mBuild = true;
	var myObject = new Object();
	
	mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");
	
	mTreeData.addHead(new Array("机构名称","机构编码"), null, null, null, null);
	mTreeData.HeadCellWidthArr=new Array("150","300");
	
	// 见样式表 bar.css
	var lStyleArr = new Array("Treetaskname","Treetaskname","TreeTextCenter", "TreeTextCenter","TreeTextCenter");
	
	window.onload = window_onload;
	
	function window_onload()
	{
	//	initMenuT();
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
	var code = findFatherCode('<c:out value="${orgList.orgSyscode}" />','<c:out value="${codeType}" />','<c:out value="${topOrgcodelength}"/>');
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />',code,
	                    new Array("<c:out value='${orgList.orgName}' />","<c:out value="${orgList.orgSyscode}" />"),
	                     'select()', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${orgList.orgId}' />,<c:out value='${orgList.orgName}' />",'2');                              	                     
	</script>
</c:forEach>
<script>
	function checkSelected(id){
		return true;
	}
	function select(){
		var sNode = mTree.GetTreeNode(mTree.mSelected );
		var sid = sNode.others.split(',');
		window.dialogArguments.setAreaCodeId(sid[0],sid[1]);
		window.close();
	}
	
	function addOrg(){		
//		location="charactarInfo.jsp"
		parent.frames[1].window.location ="clearanceInfo.jsp?viewType=tree&id=001002003"
	}
	function editTempFile(){
		parent.frames[1].window.location="clearanceEdit.jsp?viewType=tree";
//		window.showModalDialog("charactarEdit.jsp",window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
	}
	function deleteOrg(){
	}
	function moveUp(){
	}
	function moveDown(){
	}		
</script>

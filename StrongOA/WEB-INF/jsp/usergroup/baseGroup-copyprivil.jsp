<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
	<TITLE>用户组树</TITLE>
	<script type="text/javascript">
		var imageRootPath='<%=path%>/common/frame';
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
	</script>
	<%@include file="/common/include/meta.jsp" %>
  	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
  	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
	<script src="<%=path %>/uums/js/JTableTree.js"></script>
	<script src="<%=path %>/uums/js/JTableTreeHelp.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
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
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="20%"  style="vertical-align: top;">
				<form action="<%=path%>/usergroup/baseGroup!saveCopyPrivil.action" method="post">
					<input type="hidden" id="groupCodes" name="groupCodes" value="">
					<input type="hidden" id="groupId" name="groupId" value="${groupId}">  
				</form>
			      <tr>   
			        <td class="table_headtd">
			         <table width="100%" border="0" cellspacing="0" cellpadding="00" style="vertical-align: top;">
			          <tr>
			            <td>&nbsp;</td>
			            <td class="table_headtd_img">
							<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
						</td>
			            <td align="left">
			        	    <strong>组资源复制</strong>
			            </td>
			            <td align="right">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
			                <tr>
			                	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
								<td class="Operation_input" onclick="selectprivils();">&nbsp;确&nbsp;定&nbsp;</td>
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
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());" onclicknode="ClickNode();"
							onclickplusminus="ClickPlusMinus();" onshownode="shownode(true);"
							onresizeend="" onhidenode="shownode(false);">
						</div>
					</td>
				</tr>
				<tr>
					<td valign="top" align="center">
					<td>
				</tr>
			</table>
		</div>
	</BODY>
</HTML>
<script language="javascript">
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();

mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");

mTreeData.addHead(new Array("用户组名称","用户编码"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("240","130");

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
<c:forEach items="${groupList}" var="groupList">
	<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${groupList.groupSyscode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${groupList.groupSyscode}" />',code,
	                    new Array("<c:out value='${groupList.groupName}' />","<c:out value='${groupList.groupSyscode}' />"),
	                     '',
	                      true,
	                     "", "folder_closed.gif", lStyleArr, '<c:out value="${groupList.groupId}" />','0');                              	                     
	</script>
</c:forEach>
<script>
	function checkSelected(id){
		return true;
	}
	function selectprivils(){
		var id = getAllCheckedOthers(mTree);
		
		if(id == ""||id==null){
			alert('请选择用户组！！！');
			return;
		}
		var group=document.getElementById("groupId").value;
		
		document.getElementById("groupCodes").value =id;
			document.forms[0].submit();				
	}		
</script>

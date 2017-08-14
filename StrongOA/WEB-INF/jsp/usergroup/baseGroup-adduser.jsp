<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@	include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>用户树</TITLE>
		<script type="text/javascript">
		var imageRootPath='<%=path%>/common/frame';
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "," + "${groupUsers}" + ",";
		</script>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
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
		<DIV id=treecontentborder style="overflow:hidden;">
			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="20%" style="vertical-align: top;">
				<form action="<%=path%>/usergroup/baseGroup!saveUser.action" method="post">
					<input type="hidden" id="userIds" name="userIds" value="" />
					<input type="hidden" id="groupId" name="groupId" value="${groupId}" />
				</form>
				<tr>
					<td class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>&nbsp;</td>
								<td class="table_headtd_img">
									<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
								</td>
								<td align="left">
									<strong>请选择用户</strong>
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
					 <div style="width:100%; height:455px; overflow-y:auto;">
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;" onclick="return ( mTree.OnClick());"
							onclicknode="ClickNode();" onclickplusminus="ClickPlusMinus();"
							onshownode="shownode(true);" onresizeend=""
							onhidenode="shownode(false);">
						</div>
					</div>
					</td>
				</tr>
				<tr>
					<td valign="top" align="center">
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
        mTree =new JTableTreeClass("mTree",divMain,mTreeData,true,true,false,1,true,null,null,true);
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
	//var code = findFatherCode('<c:out value="${orgList.orgId}" />','<c:out value="${codeType}" />');
	//var code = '<c:out value="${orgList.orgParentId}" />';
	//if(code == null || code == ""){
	//	code = "0";
	//}
	mTreeData.addTreeNode('<c:out value="${orgList.orgSyscode}" />','0',
	                    new Array("<c:out value='${orgList.orgName}' />"),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "org",'0'); 	                                                  	                     
	</script>
</c:forEach>
<%--
<c:forEach items="${userList}" var="userList">

	<script language="JavaScript">
			
			mTreeData.addTreeNode('<c:out value="${userList.userId}" />','<c:out value="${userList.orgId}" />',
	                    new Array("<c:out value='${userList.userName}' />"),
	                     '', true,
	                     "", "renyuan.gif", lStyleArr, "<c:out value='${userList.userId}' />",'0'); 
	                                                  	                     
	</script>


</c:forEach>
--%>
<script>
		function checkSelected(id){
			return true;
		}
		function selectprivils(){
			var id = getAllCheckedValue(mTree);
			document.getElementById("userIds").value = id;
			document.forms[0].submit();
		}
		
		//子级组织机构和人员树延迟加载
		function lazyLoadSubTree(aNode, aExpandOrClose, isFilter){
			if(aNode.others == "user"){
				AfterJTTLazyExpandOrClose(mTree, aNode, aExpandOrClose,isFilter);
			}else{
				$.getJSON(scriptroot + "/usergroup/baseGroup!lazyLoadUserAndChildOrgs.action",{orgSyscode: aNode.id}, function(jsons){
					$.each(jsons,function(i,json){
						if(json.type == "org"){
							aNode.subtree.addTreeNode(json.code,aNode.id,
			                    new Array(json.name),
			                     '', true,
			                     "", "folder_closed.gif", lStyleArr, 'org', '0');
			            }
					});
					$.each(jsons,function(i,json){
						if(json.type == "user"){
							aNode.subtree.addTreeNode(json.id,aNode.id,
			                    new Array(json.name),
			                     '', true,
			                     "", "renyuan.gif", lStyleArr, 'user', '0');
			            }
					});
					AfterJTTLazyExpandOrClose(mTree, aNode, aExpandOrClose,isFilter);

					//若父节点选中则默认全选
					if(document.getElementById("mTree_divMain_" + aNode.id).checked == true){
						for(var j=0; j<aNode.subtree.items.length; j++){
							document.getElementById("mTree_divMain_" + aNode.subtree.items[j].id).checked = true;
						}
					//否则按后台默认选中	
					}else{
						for(var j=0; j<aNode.subtree.items.length; j++){
							var pattern = new RegExp("\\," + aNode.subtree.items[j].id + "\\,");
							if(pattern.test(hasSelectCode)){
								document.getElementById("mTree_divMain_" + aNode.subtree.items[j].id).checked = true;
							}
						}
					}					
				});
			}
		}
		
		 //得到所有选中的节点id值
		 function getAllCheckedValue(aThis){
		 	var value = "";
		 	var fatherValue = "";
		 	var obj = document.getElementsByName(aThis.aCheckBoxn);
		 	for(var i = 0; i < obj.length; i++){
		 		//若是已经延迟加载过了，则作为单个选中节点使用
		 		if(obj[i].flag == "hasLazyLoaded" && obj[i].checked == true){
		 			//去除已延迟加载过的组织机构节点
		 			if(aThis.GetTreeNode(obj[i].value).others == "user"){
		 				value = value + "," + obj[i].value;
		 			}
		 		}else if(obj[i].checked == true){
		 			if(aThis.GetTreeNode(obj[i].value).others == "org"){
		 				fatherValue = fatherValue + "," + obj[i].value;
		 			}else{
		 				value = value + "," + obj[i].value;
		 			}
		 		}
		 		//去除hasSelectCode中重复的选项
		 		var pattern = new RegExp("\\," + obj[i].value + "\\,");
		 		hasSelectCode = hasSelectCode.replace(pattern, ",");
		    }
		    value = value + hasSelectCode;
		    if(value == "" || value == ","){
		    	value = "";
		    }else if(hasSelectCode != ""){
		    	value = value.substring(1, value.length - 1);
		    }else{
		    	value = value.substring(1);
		    }
		    if(fatherValue != ""){
		    	value = value + "|" + fatherValue.substring(1);
		    }else{
		    	value = value + "|";
		    }
		    return value;	
		 }
		 
		// select function
		 function gotoSelectCheck(checkFlag,nodeValue,aThis){
		   var node = aThis.GetTreeNode(nodeValue);
		   selectSubNode(checkFlag,node,aThis);
		 }
		 
		 function selectSubNode(checkFlag,node,aThis){
		 	var pattern = new RegExp("\\," + node.id + "\\,");
			if(hasSelectCode != null && hasSelectCode != ""){
				hasSelectCode = hasSelectCode.replace(pattern, ",");
			}
		 	var obj = document.getElementById("mTree_" + aThis.mObj.id + "_" + node.id);
		 	if(obj != null){
		 		obj.checked = checkFlag;
			 	if(node.subtree != null){
			   	for(var i=0; i<node.subtree.items.length; i++){
			   		selectSubNode(checkFlag,node.subtree.items[i],aThis);
			   	}
			   }
		   }
		 }
</script>

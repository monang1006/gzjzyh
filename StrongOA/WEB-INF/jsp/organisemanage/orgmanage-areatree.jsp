<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
<HEAD>
<TITLE>请选择机构所属的区域</TITLE>
	<script type="text/javascript">
		var imageRootPath='<%=path%>/common/frame';
	</script>
	<%@include file="/common/include/meta.jsp"%>
  	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
  	<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css" >
	<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
	<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
	<link href="<%=frameroot%>/css/JTableTree.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
	<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
	<script src="<%=path %>/uums/js/LazyLoadJTableTree.js"></script>
	<script src="<%=path %>/uums/js/JTableTreeHelp.js"></script>
	<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
</HEAD>
<BODY class=contentbodymargin >
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
<DIV id=treecontentborder style="overflow:hidden;">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%"  style="vertical-align: top;">
      <tr>
        <td height="40" class="table_headtd">
	        <table width="100%" border="0" cellspacing="0" cellpadding="00"  style="vertical-align: top;">
	          <tr>
	            <td class="table_headtd_img">
	            	<img src="<%=frameroot%>/images/ico/ico.gif">
	            </td>
	            <td align="left">
	            	<strong>请选择机构所属的区域</strong>
	            </td>
	            <td align="right">
					<table border="0" align="right" cellpadding="00" cellspacing="0">
		                <tr>
		                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
		                 	<td class="Operation_input1" onClick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
		                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
	                  		<td width="6"></td>
		                </tr>
		            </table>
				</td>
	            </td>
	          </tr>
	        </table>
        </td>
      </tr>
				<tr>
					<td valign=top align="center">
                       <div style="width:100%; overflow-y:auto; height:455px;">
						<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;"
							onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
                         </div>   
					</td>
				</tr>
				<tr>
					<td class="table1_td"></td>
					<td></td>
				</tr>
			</table>
		</div>
		<!--bodyEnd-->
			<DIV id=LOADING onClick="showLoading(false)"
		style="position: absolute; top: 30%; left: 38%; display: none"
		align=center>
		<font color=#16387C><strong>正在加载，请稍候...</strong></font>
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

mTreeData.addHead(new Array("区划名称","区划编码"),
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
        mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,true,null,null,true);
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
<c:forEach items="${areaCodeList}" var="areaCodeList">
<script language="JavaScript">
	myObject = new Object();
	var code = findFatherCode('<c:out value="${areaCodeList.acCode}" />','<c:out value="${codeType}" />');
	mTreeData.addTreeNode('<c:out value="${areaCodeList.acCode}" />',code,
	                    new Array("<c:out value='${areaCodeList.acName}' />","<c:out value='${areaCodeList.acCode}' />"),
	                     'select();', true,
	                     "", "folder_closed.gif", lStyleArr, "<c:out value='${areaCodeList.acName}' />,<c:out value='${areaCodeList.acId}' />",'2');                            	                     
	</script>
</c:forEach>
	<script>
		function checkSelected(id){
			return true;
		}
		function select(){
			var sNode = mTree.GetTreeNode(mTree.mSelected );
			var sid = sNode.others.split(',');
			window.dialogArguments.setAreaCodeId(sNode.id,sid[0],sid[1]);
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
		
		//区划代码树延迟加载
		function lazyLoadSubTree(aNode, aExpandOrClose, isFilter){
			//判断aNode节点下的子节点是否默认选中
			var initFlag = true;
			$.getJSON(scriptroot + "/organisemanage/orgmanage!lazyLoadArea.action",{areacode: aNode.id}, function(jsons){
				$.each(jsons,function(i,json){
						//判断是否有该节点的子节点已默认被选中，若有则不添加全选效果
						/**var pattern = new RegExp("\\," + json.code + "\\S+\\,");
						if(pattern.test(hasSelectCode)){
							initFlag = false;
						}**/
						aNode.subtree.addTreeNode(json.code,aNode.id,
		                    new Array(json.name,json.code),
		                     'select();', true,
		                     "", "folder_closed.gif", lStyleArr, json.name + "," + json.id, '2');
				});
				AfterJTTLazyExpandOrClose(mTree, aNode, aExpandOrClose,isFilter);
				//添加全选效果
				/**if(initFlag){
					var aNodeIsChecked = document.getElementById("mTree_divMain_" + aNode.id).checked;
					if(aNodeIsChecked == true){
						for(var j=0; j<aNode.subtree.items.length; j++){
							document.getElementById("mTree_divMain_" + aNode.subtree.items[j].id).checked = true;
						}
					}
				}else{
					//只选中默认选中的节点
					for(var j=0; j<aNode.subtree.items.length; j++){
						var pattern = new RegExp("\\," + aNode.subtree.items[j].id + "\\,");
						if(pattern.test(hasSelectCode)){
							document.getElementById("mTree_divMain_" + aNode.subtree.items[j].id).checked = true;
						}
					}					
				}**/
			});
		}
		
		 //得到所有选中的节点id值
		 /**function getAllCheckedValue(aThis){
		 	var value = "";
		 	var fatherValue = "";
		 	var obj = document.getElementsByName(aThis.aCheckBoxn);
		 	for(var i = 0; i < obj.length; i++){
		 		//若是已经延迟加载过了，则作为单个选中节点使用
		 		if(obj[i].flag == "hasLazyLoaded" && obj[i].checked == true){
		 			value = value + "," + obj[i].value;
		 		}else if(obj[i].check == true){
		 			var pattern = new RegExp("\\," + obj[i].value + "\\S+\\,");
		 			//如果hasSelectCode中有选中节点的子集
		 			if(pattern.test(hasSelectCode)){
		 				value = value + "," + obj[i].value;
		 			}else{
		 				fatherValue = fatherValue + "," + obj[i].value;
		 			}
		 		}
		 		//去除hasSelectCode中重复的选项
		 		var pattern = new RegExp("\\," + obj[i].value + "\\,");
		 		hasSelectCode = hasSelectCode.repalce(pattern, ",");
		    }
		    value = value + hasSelectCode;
		    if(value == "" || value == ","){
		    	value = "";
		    }else{
		    	value = value.substring(1, value.length - 1);
		    }
		    if(fatherValue != ""){
		    	value = value + "|" + fatherValue.substring(1);
		    }
		    return value;	
		 }**/
	</script>

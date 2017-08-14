<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>请选择岗位</TITLE>
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
			href="<%=frameroot%>/css/properties_windows_add.css">
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
	<base target="_self" />
	<BODY>
	<DIV cellpadding="0" style="overflow:hidden;">
          <div align="center">
         
            <table width="100%" border="0" cellspacing="0" cellpadding="00" height="44px;"  class="table_headtd" style="vertical-align: top;">
              <tr>
                <td class="table_headtd_img"><img src="<%=frameroot%>/images/ico/ico.gif"></td>
                <td align="left"><strong>请选择岗位</strong></td>
                 <td align="right">
                
                <table border="0" align="right" cellpadding="00" cellspacing="0">
                    <tr>
                   
					<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
                    <td class="Operation_input" onClick="allSelected()">&nbsp;全&nbsp;选&nbsp;</td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                    <td width="5"></td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
                    <td class="Operation_input" onClick="getAllCheckedPostIdAndName()">&nbsp;确&nbsp;定&nbsp;</td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
                    <td width="5"></td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
                    <td class="Operation_input1" onClick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
                    <td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
                    <td width="6"></td>
                  </tr>
                  </table>
               </td>
              </tr>
            </table>
            <div align="left" style="font-size: 14px;"><font color="#999999">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;全局岗位不允许修改</font><div>
            <div style="height:400px;  overflow-y:auto; overflow-x:hidden; widows:100%">
              <div style="padding:0 20px; text-align: left;">
                <div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
							style="overflow :auto;" onclick="return ( mTree.OnClick());"
							onresizeend="">
						</div>
              </div>
            </div>
          </div>
        </DIV>
	</body>
</HTML>

<script language="javascript">
var temp = "";//全局岗位不可修改
var mTree =null ;
var mTreeData =null;
var mBuild = true;
var myObject = new Object();


mTreeData = new JTableTreeDataClass("<%=path%>/uums/images/jTableTree/",true,"B_MODULE");


mTreeData.addHead(new Array("岗位名称"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("240");

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

<c:forEach items="${postList}" var="postList">

	<script language="JavaScript">
	var tempPpostIsoverall = ${postList.postIsoverall}+"";
	if(tempPpostIsoverall==1){
		temp = temp +","+'${postList.postId}';
		myObject = new Object();
		mTreeData.addTreeNode('<c:out value="${postList.postId}" />','0',
		                    new Array("<c:out value='${postList.postName}' />"),
		                     '', true,
		                     "", "folder_closed.gif", lStyleArr, "<c:out value='${postList.postName}' />",'0');
	}else{
		myObject = new Object();
		mTreeData.addTreeNode('<c:out value="${postList.postId}" />','0',
		                    new Array("<c:out value='${postList.postName}' />"),
		                     '', true,
		                     "", "folder_closed.gif", lStyleArr, "<c:out value='${postList.postName}' />",'0');                            	                     
	}
	</script>

</c:forEach>
<script>

		function checkSelected(id){
			return true;
		}
		
		function getAllCheckedPostIdAndName(){
		 	var value = "";//选中的id值
		 	var name = "";//选中的节点名称值
		 	var node = null;//临时保存选中的节点
		 	var obj = document.getElementsByName(mTree.aCheckBoxn);
		 	for(var i = 0; i < obj.length; i++){
		 		if(obj[i].checked == true){
		 			value = value + "," + obj[i].value;
		 			node = mTree.GetTreeNode(obj[i].value);
		 			name = name + "," + node.others;
		 		}
		    }
		    if(value != ""){
		    	value = value.substring(1);
		    	name = name.substring(1);
		    }
		    window.dialogArguments.setPostValue(value,name); 	
		    window.close();
		}
		
		function setfirstId(){
			 var fId = '${postId}';
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
			 //全局岗位不能修改
			 if(temp != ''){
			 	var tempid = temp.split(',');
			 	var obj = getAllCheckedObj(mTree);
		 		for(var i = 0; i < obj.length; i++){
		 			for(var j = 0; j <tempid.length; j++ ){
			 			if(obj[i].value == tempid[j]){
			 				obj[i].disabled=true;
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

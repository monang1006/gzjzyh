<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>设置权限</TITLE>
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
		<script src="<%=path%>/uums/js/JTableTreeHelp_fatherSelect.js"></script>
	</HEAD>
	<script type="text/javascript">
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
		if("${initPrivil}" != "" && "${initPrivil}" != null){
			hasSelectCode = "," + "${initPrivil}" + ",";
		}
	</script>
	<base target="_self" />
	<BODY class=contentbodymargin leftmargin="2" topmargin="5">
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
									设置资源
								</td>
								<td width="5%">
									&nbsp;
								</td>
								<td width="60%">
									<s:form id="getpostform"
										action="/usermanage/usermanage!setPrivil.action">
										<input type="hidden" name="privelCode" id="privelCode">
										<input type="hidden" name="userId" id="userId">
										<input type="hidden" id="extOrgId" name="extOrgId" value="${extOrgId}">
									</s:form>
									<table width="100%" border="0" align="right" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="*">
												&nbsp;
											</td>
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
											<td width="3">
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
									<input name="Submit2" type="button" class="input_bg"
										value="全 选" onclick="allSelected();">
								</td>
								<td width="30%">
									<input name="Submit2" type="button" class="input_bg"
										value="确 定" onclick="selectpostid();">
								</td>
								<td width="30%">
									<input name="Submit" type="button" class="input_bg" value="取 消"
										onclick="javascript:window.close();">
								</td>
							</tr>
						</table>
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


mTreeData.addHead(new Array("资源名称","资源编号"),
             null, null,
				null ,
             null);
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
                mTree =new JTableTreeClass('mTree',divMain,mTreeData,true,true,false,1,true,null,null,true);
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
<c:forEach items="${systemList}" var="systemlist">
 
  
	<script language="JavaScript">
		mTreeData.addTreeNode('<c:out value="${systemlist.sysSyscode}" />','0',
		                    new Array("<c:out value='${systemlist.sysName}' />",'系统'),
		                     '', true,
		                     "", "folder_closed.gif", lStyleArr, "system",'2'); 
	                                                  	                     
	</script>
	<%--<c:forEach items="${systemlist.basePrivilTypes}" var="privilTypeList">
		<script language="JavaScript">
			mTreeData.addTreeNode('<c:out value="${privilTypeList.typeId}" />','<c:out value="${privilTypeList.baseSystem.sysId}" />',
			                    new Array("<c:out value='${privilTypeList.typeName}' />",'资源类型'),
			                     '', true,
			                     "", "folder_closed.gif", lStyleArr, "-1",'2'); 
		</script>
	</c:forEach>--%>
</c:forEach>


<%--<c:forEach items="${privilList}" var="privilList">

	<script language="JavaScript">
		myObject = new Object();
		var code = findFatherCode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${codeType}" />','0');
		if(code == '0'){
			mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${privilList.basePrivilType.typeId}" />',
		                    new Array("<c:out value='${privilList.privilName}' />","<c:out value='${privilList.privilSyscode}' />"),
		                     '', true,
		                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.privilId}" />','0');
		}else{
			mTreeData.addTreeNode('<c:out value="${privilList.privilSyscode}" />',code,
			                    new Array("<c:out value='${privilList.privilName}' />",'资源权限'),
			                     '', true,
			                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.privilId}" />','0');
		}
	</script>

</c:forEach>--%>
<script>

		function checkSelected(id){
			return true;
		}
		function selectpostid(){
			//var id = getAllCheckedValue(mTree);
			var id = getAllCheckedValue(mTree);
			var userId = '${userId}' ;
			document.getElementById("privelCode").value = id;
			document.getElementById("userId").value = userId;
			document.getElementById("getpostform").submit();
			//window.close();
		}
		/**
			Jquery方式得到所有选中的节点
		*/
		function getAll(){
			var ids = "";
			$(":checked").each(function(){
				ids += ","+$(this).val();
			});
			return ids.substring(1);
		}
		function setfirstId(){
			 var fId = '${initPrivil}';
			 if(fId != ''){
			 	var id = fId.split(',');
			 	var obj = getAllCheckedObj(mTree);
		 		for(var i = 0; i < obj.length; i++){
		 			for(var j = 0; j <id.length; j++ ){
			 			var node = mTree.GetTreeNode(obj[i].value);
			 			if(node.id == id[j]){
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
		    var obj;
		  	for(var i = 0; i < mTree.mNodes.items.length; i++) {
		  		obj = document.getElementById("mTree_" + mTree.mObj.id + "_" + mTree.mNodes.items[i].id);
		  		if(obj != null) {
		  			obj.checked = selectFlag;
		  		}
		  	}
		  	for(var i=0; i<mTree.mTreeRoot.subtree.items.length; i++){
		  		var subtree = mTree.mTreeRoot.subtree.items[i];
		  		for(var j=0; j<subtree.subtree.items.length; j++){
				  	var pattern = new RegExp("\\," + subtree.subtree.items[j].id + "\\S*\\,");
					if(hasSelectCode != null && hasSelectCode != ""){
						hasSelectCode = hasSelectCode.replace(pattern, ",");
					}
				}
			}
			selectFlag = !selectFlag;
		}
		
		//资源权限树延迟加载
		function lazyLoadSubTree(aNode, aExpandOrClose, isFilter){
			//判断aNode节点下的子节点是否默认选中
			var initFlag = true;
			var url = scriptroot + "/usermanage/usermanage!lazyLoadPrivil.action";
			var privilCode = "";
			var systemCode = "";
			if(aNode.others == "system"){
				systemCode = aNode.id;
			}else{
				privilCode = aNode.id;
			}
			$.getJSON(url,{privilCode: privilCode,systemCode: systemCode}, function(jsons){
				$.each(jsons,function(i,json){
						//判断是否有该节点的子节点已默认被选中，若有则不添加全选效果
						var pattern = new RegExp("\\," + json.code + "\\S+\\,");
						if(pattern.test(hasSelectCode)){
							initFlag = false;
						}
						aNode.subtree.addTreeNode(json.code,aNode.id,
		                    new Array(json.name,json.code),
		                     "", true,
		                     "", "folder_closed.gif", lStyleArr, json.id, '0');
				});
				AfterJTTLazyExpandOrClose(mTree, aNode, aExpandOrClose,isFilter);
				//添加全选效果
				if(initFlag && aNode.others != "system"){
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
				}
			});
		}
		
		 //得到所有选中的节点id值
		 function getAllCheckedValue(aThis){
		 	var value = "";
		 	var fatherValue = "";
		 	var obj = document.getElementsByName(aThis.aCheckBoxn);
		 	for(var i = 0; i < obj.length; i++){
		 		//若是已经延迟加载过了，则作为单个选中节点使用
		 		if(obj[i].flag == "hasLazyLoaded" && obj[i].checked == true){
		 			value = value + "," + obj[i].value;
		 		}else if(obj[i].checked == true){
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
	</script>		

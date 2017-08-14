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
//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
var hasSelectCode = "";
</script>

		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/JTableTree.css"
			rel=stylesheet>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<LINK href="<%=path%>/uums/css/jTableTree/bar.css" rel=stylesheet>
		<LINK href="<%=path%>/uums/css/jTableTree/jtree.css" rel=stylesheet>
		<script src="<%=path%>/uums/js/LazyLoadJTableTree.js"></script>
		<script src="<%=path%>/uums/js/JTableTreeHelp_fatherSelect.js"></script>
	<script type="text/javascript">
		//页面缓存的已经选中的数据项,格式为,itemCode1,itemCode2,...
		var hasSelectCode = "";
		if("${groupPrivil}" != "" && "${groupPrivil}" != null){
			hasSelectCode = "," + "${groupPrivil}" + ",";
		}
	</script>
	</HEAD>
	<base target=_self>
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
								<td width="50%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									请选择资源
								</td>
								<td width="15%">
									&nbsp;
								</td>
								<td width="30%">
									<form action="<%=path%>/usergroup/baseGroup!savePrivil.action"
										method="post">
										<input type="hidden" id="privilIds" name="privilIds" value="">
										<input type="hidden" id="groupId" name="groupId"
											value="${groupId}">

									</form>
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
													onclick="selectprivils();" />
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
						<img src='<%=frameroot%>/images/keyline.gif' height='2'
							width='100%' border=0 align='top' />
					</td>
				</tr>
				<tr>
					<td valign="top" align="center">
						<table>
							<tr>
								<td>
									<input type="button" value="全 选" class="input_bg"
										onclick="allSelected();" />
								</td>
								<td>
									<input type="button" value="确 定" class="input_bg"
										onclick="selectprivils();" />
								</td>
								<td>
									<input type="button" value="取 消" class="input_bg"
										onclick="javascript:window.close();" />
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


mTreeData.addHead(new Array("资源名称","资源编码"),
             null, null,
				null ,
             null);
mTreeData.HeadCellWidthArr=new Array("240","130");

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
                mTree =new JTableTreeClass("mTree",divMain,mTreeData,true,true,false,1,true,null,null,true);
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
<c:forEach items="${systemList}" var="systemList">


	<script language="JavaScript">
	//var code = findFatherCode('<c:out value="${systemList.sysSyscode}" />','<c:out value="${codeType}" />','0');
	mTreeData.addTreeNode('<c:out value="${systemList.sysSyscode}" />','0',
	                    new Array("<c:out value='${systemList.sysName}' />",'系统'),
	                     '', true,
	                     "", "folder_closed.gif", lStyleArr, "system",'2'); 
	                                                  	                     

	</script>
</c:forEach>


<%--<c:forEach items="${privilList}" var="privilList">

	<script language="JavaScript">
	myObject = new Object();
		var code = findFatherCode('<c:out value="${privilList.privilSyscode}" />','<c:out value="${codeType}" />','0');
		mTreeData.addTreeNode("<c:out value='${privilList.basePrivilType.baseSystem.sysSyscode}' />-<c:out value='${privilList.privilSyscode}' />","<c:out value='${privilList.basePrivilType.baseSystem.sysSyscode}' />-"+code,
	                    new Array("<c:out value='${privilList.privilName}'/>","<c:out value='${privilList.privilSyscode}'/>"),
	                     '',
	                      true,
	                     "", "folder_closed.gif", lStyleArr, '<c:out value="${privilList.privilId}" />','0');                              	                     
	</script>

</c:forEach>--%>
<script>
		function checkSelected(id){
			return true;
		}
		function selectprivils(){
			var id = getAllCheckedValue(mTree);
/**			if(id == ""||id==null){
				alert('请选择资源！！！');
				return;
			}**/
			var group=document.getElementById("groupId").value;
			document.getElementById("privilIds").value =id;
			document.forms[0].submit();
		}
		
		function setfirstId(){
			 var fId = '${groupPrivil}';
		   // alert(fId);
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
			var url = scriptroot + "/usergroup/baseGroup!lazyLoadPrivil.action";
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

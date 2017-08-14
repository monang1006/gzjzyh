<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<title>查询分类</title>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="windows_title">约稿单位</div>
		<div class="information_out">
		<div class="tree_box">
			<div id="moduleTree"></div> 
			<script>
$(document).ready(function(){
	var obj = window.dialogArguments;
	var check = ","+obj.checkId+",";
var dfop = {
showradio: false,
showcheck: true,
cascadecheck:false,
displayDeep:false,
oncheckboxclick:false,
onnodeclick:false,
onnodedblclick:false,
onselect:false,
parentCascadecheck:false,theme:'bbit-tree-lines',
className:'bbit-tree',
aHasSelected:check,
aNeedLazy:false,
contextMenu:'buton_list',
url:'<%=root%>/xxbs/action/invitation!showTree.action',
cbiconpath:'<%=root%>/theme/theme_default/image/tree/',
title:'机构查询分类'
};
var initTree = function(){
$.ajax({
url:dfop.url,
type:'POST',
dataType:'json',
success:function(data){
if(data && data.length >= 0){
dfop.data = data;
$("#moduleTree").treeview(dfop);
}
},
error:function(error){
alert('error occur!');
}
});
};
initTree();
});
</script>
			
		</div>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
					value="保存" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="关闭" id="cancel" />
		</div>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
    $("#save").click(function(){
    	showLoadingTip("数据正在提交，请稍候...");  
    	var selectedNodes = $("#moduleTree").getTSNs(true);
    	var ids = "";
    	var id = "";
    	var ret={};
    	ret.status="success";
    	var names="";
    	if(selectedNodes && selectedNodes.length > 0){
    		var l = selectedNodes.length;
    		var selectedNode;
    		for(var i=0; i<l; i++){
    			selectedNode = selectedNodes[i];
    			ids = ids + "," + selectedNode.value;
    			names = names + "," + selectedNode.text;
    			
    			id = id + "," + selectedNode.id;
    		}
    		id = id.substring(1);
    		ids = ids.substring(1);
    		names = names.substring(1);
    	}
    	ret.title=names;
		ret.id = ids;
		ret.treeId = id;
		window.returnValue = ret;
		window.close();
    });
  
    $("#cancel").click(function(){
        window.close();
    });
});



function reloadData() {
    $('#moduleTree').reLoadData();
}

</script>
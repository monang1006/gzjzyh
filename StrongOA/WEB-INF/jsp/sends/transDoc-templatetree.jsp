<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>选择模板</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript">
			//确定
			function doSelect(){
				var nodeId = $("#nodeId").val();
				if(nodeId == ""){
					showTip('<div class="tip" id="loading">请选择套红！</div>');
					return ;
				}else{
					window.returnValue = nodeId;
					window.close();
				}
			}
			//取消
			function doCancel(){
				window.returnValue = "";
				window.close();
			}
			
			function select(nodeId,type){
				if(type == "item"){//单击的是套红节点
					$("#nodeId").val(nodeId);
				}else{
					$("#nodeId").val("");
				}
			}
		</script>
	</head>
<base target="_self"/>	  
<body  scroll="auto">
	<input id="nodeId" type="hidden"/>
	<tree:strongtree title="公文模板"  dealclass="com.strongit.doc.sends.util.DocRedTypeTreeDeal" data="${typeList}" iconpath="frame/theme_gray/images/"/>
	<br/>
	<div align="center">
		<input type="button" class="input_bg" onclick="doSelect();" value="确定" />&nbsp;&nbsp;
		<input type="button" class="input_bg" onclick="doCancel();" value="取消"/>
	</div>
</body>
</html>

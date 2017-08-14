<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<title>机构树形结构</title>
		<script type="text/javascript">
			function getCheckId(){
		        var chkTreeNode=document.getElementsByName("chkTreeNode");	//得到选择框对象
		        var checkvalue,checkid;
		        checkid='';
		        for(var i=0;i<chkTreeNode.length;i++){						//获取被选中的值
					if(chkTreeNode[i].checked==true){
						checkid=checkid+chkTreeNode[i].value+",";			//得到被选中的行的编号
					}
				}
				if(checkid!=''){
					checkid = checkid.substring(0,checkid.length-1);
				}
				return checkid;
			}
			
			function doSelect(){
				alert("确定进行分发");
			}
			
			function doCancel(){
				window.close();
			}
		</script>
	</head>
	<base target="_self"/>	  
	<body oncontextmenu="return false;" scroll="auto">
		<div align="center">
		<table><tr><td>
			<input type="button" class="input_bg" onclick="doSelect();" value="确定" />&nbsp;&nbsp; 
			</td><td><input type="button" class="input_bg" onclick="doCancel();" value="取消"/></td></tr></table>
		</div>
	</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></script>
	</head>
	<body>
	<div width="100%" height="100%" align="center"><table><tr><td>
		<input type="button" name="btnEmpty" value="确定" onClick="javascript:gotoEmpty();">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnClose" value="关闭" onClick="javascript:gotoClose();">
	</td></tr><tr><td>
		<tree:strongtree title="用户" check="true" dealclass="com.strongit.oa.archive.archivefolder.DealUserTreeNode" data="${userList}" chooseType="signle" hascheckedvalues="${hascheckedvalues}"/>
		</td></tr><tr><td>
		<input type="button" name="btnEmpty" value="确定" onClick="javascript:gotoEmpty();">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnClose" value="关闭" onClick="javascript:gotoClose();">
		</td></tr></table></div><SCRIPT>
	function gotoEmpty(){
        var chkTreeNode=document.getElementsByName("chkTreeNode");//得到选择框对象

        var flag=0;//记录有多少条记录被选中
        var checkvalue,checkid;
        for(var i=0;i<chkTreeNode.length;i++){//获取被选中的值

			if(chkTreeNode[i].checked==true){
				checkid=chkTreeNode[i].value;//得到被选中的行的编号

				//得到显示的值（编码.名称）

				checkvalue=chkTreeNode[i].nextSibling.nextSibling.innerHTML;
				flag++;
			}
		}
		if(flag!=1){
			alert("一次只能选择一个");
		}
		else {
			if(dialogArguments.document.getElementById("${objId}")!=undefined){
				dialogArguments.document.getElementById("${objId}").value=checkid;
     			dialogArguments.document.getElementById("${objName}").value=checkvalue;
     		}else{
     			dialogArguments.getvalue(checkid,checkvalue);
     		}
     		window.close();
		}
}
function gotoClose(){
    window.close();
}
  	</SCRIPT>
	</body>
</html>

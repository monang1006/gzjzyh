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
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</head>
	<body>
	<div align="center"><table><tr><td>
		<input type="button" name="btnEmpty" value="确定" onClick="javascript:gotoEmpty();">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnqingchu" value="清除" onClick="javascript:gotoqingchu();">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnClose" value="关闭" onClick="javascript:gotoClose();">
		</td></tr>
		<tr><td>
		<tree:strongtree title="组织机构" check="true" dealclass="com.strongit.oa.archive.archivefolder.DealForderTreeNode" data="${folderList}" chooseType="signle" />
		</td></tr><tr><td>
		<input type="button" name="btnEmpty" value="确定" onClick="javascript:gotoEmpty();">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnqingchu" value="清除" onClick="javascript:gotoqingchu();">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" name="btnClose" value="关闭" onClick="javascript:gotoClose();">
	</td></tr></table></div>	<SCRIPT>
	function gotoEmpty(){
        var chkTreeNode=document.getElementsByName("chkTreeNode");//得到选择框对象

        var flag=0;//记录有多少条记录被选中
        var checkvalue,checkid,checkname;
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
			returnValue=checkid+","+checkvalue;
     		window.close();
		}
     }
	function gotoqingchu(){
	 
				returnValue="";
	     		window.close();
	}
	function gotoClose(){
	    window.close();
	}
  	</SCRIPT>
	</body>
</html>

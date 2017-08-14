<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/treeview.css" type=text/css
			rel=stylesheet>
		<script src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
	</head>
	<body>
		<div width="100%" height="100%" align="center">
			<table>
				<tr>
					<td>
						<input type="button" name="btnEmpty" value="确定"
							onClick="javascript:gotoEmpty();">
						&nbsp;&nbsp;
						<input type="button" name="clear" value="清空"
							onClick="javascript:goclear();">
						&nbsp;&nbsp;
						<input type="button" name="btnClose" value="关闭"
							onClick="javascript:gotoClose();">
					</td>
				</tr>
				<tr>
					<td>
						<s:if test="objId==\"STRUC_ID\"">
							<tree:strongtree title="${dictName}" check="true"
								dealclass="com.strongit.oa.personnel.baseperson.DealTreeNode"
								data="${strucList}" chooseType="signle"
								hascheckedvalues="${hascheckedvalues}" />
						</s:if>
					    <s:elseif test="objId==\"ORG_ID\"">
					    	<tree:strongtree title="${dictName}" check="true"
								dealclass="com.strongit.oa.archive.archivefolder.DealOrgTreeNode"
								data="${orglist}" chooseType="signle"
								/>
						</s:elseif>
						<s:else>
						  	<tree:strongtree title="${dictName}" check="true"
								dealclass="com.strongit.oa.archive.archivefolder.DealDictTreeNode"
								data="${dictItemList}" chooseType="signle"
								hascheckedvalues="${hascheckedvalues}" />
						</s:else>
					</td>
				</tr>
				<tr>
					<td>
						<input type="button" name="btnEmpty" value="确定"
							onClick="javascript:gotoEmpty();">
						&nbsp;&nbsp;
						<input type="button" name="clear" value="清空"
							onClick="javascript:goclear();">
						&nbsp;&nbsp;
						<input type="button" name="btnClose" value="关闭"
							onClick="javascript:gotoClose();">
					</td>
				</tr>
			</table>
		</div>
		<SCRIPT>
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
				var objId="${objId}";
				dialogArguments.document.getElementById("${objId}").value=checkid;
     			dialogArguments.document.getElementById("${objName}").value=checkvalue;
     			if(objId=="ORG_ID"&&dialogArguments.document.getElementById("STRUC_ID")!=undefined){
	   				dialogArguments.document.getElementById("STRUC_ID").value="";
	   				dialogArguments.document.getElementById("STRUC_ID_name").value="";
   				}
     		}else{
     			dialogArguments.getvalue(checkid,checkvalue);
     		}
		}
	    window.close();
	}
	
	function goclear(){
		if(dialogArguments.document.getElementById("${objId}")!=undefined){
			var objId="${objId}";
			dialogArguments.document.getElementById("${objId}").value="";
   			dialogArguments.document.getElementById("${objName}").value="";
   			if(objId=="ORG_ID"&&dialogArguments.document.getElementById("STRUC_ID")!=undefined){
   				dialogArguments.document.getElementById("STRUC_ID").value="";
   				dialogArguments.document.getElementById("STRUC_ID_name").value="";
   			}
     	}else{
   			dialogArguments.getvalue("","");
   		}
   		window.close();
	}
	
	function gotoClose(){
	    window.close();
	}
  	</SCRIPT>
	</body>
</html>

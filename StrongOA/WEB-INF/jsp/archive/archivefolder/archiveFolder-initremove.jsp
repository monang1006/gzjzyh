<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<TITLE>移动案卷</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</head>
	<base target="_self" />
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder cellpadding="0">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<s:form id="archiveFolderForm" theme="simple" action="/archive/archivefolder/archiveFolder!remove.action">
					<input id="folderId" name="folderId" type="hidden" size="22" value="${folderId}">
					<input id="archiveSortId" name="archiveSortId" type="hidden" size="22">
					<input id="moduletype" name="moduletype" type="hidden" size="22" value="${moduletype}">
				</s:form>
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>设置类目</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
						                <tr>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="gotoEmpty();">&nbsp;确&nbsp;定&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
						                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
						                 	<td class="Operation_input1" onclick="gotoClose();">&nbsp;关&nbsp;闭&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
					                  		<td width="6"></td>
						                </tr>
						            </table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table align="center" width="90%">
				<tr height="5%">
					<td>
					</td>
				</tr>
				<tr>
					<td>
						<tree:strongtree title="类目" check="true" dealclass="com.strongit.oa.archive.archivefolder.DealForderRemoveTreeNode" data="${sortList}" chooseType="signle"/>
					</td>
				</tr>
			</table>
		</DIV>
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
					alert("请选择一个类目!");
				}
				else if(checkid=="${archiveSortId}"){
					alert("该案卷属于本类目，不需移动！");
				}
				else {
					document.getElementById("archiveSortId").value=checkid;
					archiveFolderForm.submit();
				}
			}
			function gotoClose(){
			    window.close();
			}
	  	</SCRIPT>
	</body>
</html>

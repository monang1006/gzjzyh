<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择所属部门</title>
		<link href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</head>
	<body>
	<DIV id=contentborder cellpadding="0">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>设置组织机构</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
						                <tr>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="gotoEmpty();">&nbsp;确&nbsp;定&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
					                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
						                 	<td class="Operation_input" onclick="gotoqingchu();">&nbsp;清&nbsp;除&nbsp;</td>
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
				<tr>
					<td>
						<tree:strongtree title="组织机构" check="true" dealclass="com.strongit.oa.archive.archivefolder.DealOrgTreeNode" data="${orgList}" chooseType="signle" hascheckedvalues="${hascheckedvalues}"/>
					</td>
				</tr>
			</table>
		</DIV>
		<SCRIPT>
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
					alert("请选择一个组织机构!");
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
			function gotoqingchu(){
						if(dialogArguments.document.getElementById("${objId}")!=undefined){
							dialogArguments.document.getElementById("${objId}").value="";
			     			dialogArguments.document.getElementById("${objName}").value="";
			     		}
			     		window.close();
			}
			function gotoClose(){
			    window.close();
			}
	  	</SCRIPT>
	</body>
</html>
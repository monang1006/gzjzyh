<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	<title>案卷类目</title>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
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
									<strong>设置所属类目</strong>
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
						<tree:strongtree title="案卷类目" check="true" chooseType="signle" dealclass="com.strongit.oa.archive.tempfile.DealSortTreeNode" data="${archiveSortList}"/>
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
					alert("请选择一个案卷类目!");
					return;
				}
				else {
					if(dialogArguments.document.getElementById("${objId}")!=undefined){
						dialogArguments.document.getElementById("${objId}").value=checkid;
		     			dialogArguments.document.getElementById("${objName}").value=checkvalue;
		     		}else{
		     			dialogArguments.getvalue(checkid,checkvalue);
		     		}
				}
			    window.close();
			}
			function gotoClose(){
			    window.close();
			}
	  	</SCRIPT>
	</body>
</html>

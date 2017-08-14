<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>选择模板</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
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
<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>公文模板</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td >
								<table align="right" border="0"  cellpadding="0" cellspacing="0">
					                <tr>
					                <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td id="btnOK_newLocalFile2" class="Operation_input" onClick="doSelect();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
                                           <td>&nbsp;
										   
											</td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="doCancel();">&nbsp;取&nbsp;消&nbsp;</td>
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
				</td>
				</tr>
				</table>
				
<div id=contentborder>
	<input id="nodeId" type="hidden"/>
	<tree:strongtree title="公文模板"  dealclass="com.strongit.oa.senddoc.DocRedTypeTreeDeal" data="${typeList}" iconpath="frame/theme_gray/images/"/>
	<br/>
	<%--<div align="center">
		<input type="button" class="input_bg" onclick="doSelect();" value="确定" />&nbsp;&nbsp;
		<input type="button" class="input_bg" onclick="doCancel();" value="取消"/>
	</div>
	--%></div>
</body>
</html>

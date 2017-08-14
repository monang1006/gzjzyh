<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>导入系统人员-选择目标组</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script type="text/javascript">
			//确定
			function doSelect(){
				var checkSelect = true;
				if($("input:radio:checked").size() == 0){
					checkSelect = false;
				}
				if(!checkSelect){
					showTip('<div class="tip" id="loading">请选择组！</div>');
					return ;
				}
				var groupId = $("input:radio:checked").val();
				var isDefault = $("#default").attr("checked");
				//如果选择了默认组,那么将此设置放入session中保存起来
				if(isDefault){
					$.post("<%=root%>/address/addressGroup!setGroupDefault.action",
						   {groupId:groupId},
						   function(data){
						   		if(data == "success"){
						   			window.returnValue = groupId;
									window.close();
						   		}else if(data == "error"){
						   			showTip('<div class="tip" id="loading">会话过程出错啦！</div>');
						   		}
						   });
				}else{
					window.returnValue = groupId;
					window.close();
				}
			}
			//当单击了复选框,给出提示
			function setTip(){
				var isDefault = $("#default").attr("checked");{
					if(isDefault){
						showTip('<div class="tip" id="loading">您可以重新登录系统修改这项设置！</div>');
						$("#default").attr("title","您可以重新登录系统修改这项设置！");
					}
				}
			}
			//取消
			function doCancel(){
				window.returnValue = "";
				window.close();
			}
		</script>
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;" scroll="auto">
	<div>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>选择目标组</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doSelect();">&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="doCancel();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
				<td>
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
						<tr>
	<tree:strongtree title="联系组" hascheckedvalues='${groupId}' chooseType="signle" check="true" dealclass="com.strongit.oa.address.util.AddressGroupTree" data="${groupLst}"/>
	<br/>
<%--	<input id="default" type="checkbox" value="yes" onclick="setTip();"/>默认添加当前选中的组--%>
	</tr>
	</table>
	</td>
	</tr>
	</table>
	</div>
</body>
</html>
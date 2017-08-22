<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>设置用户所属单位</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<LINK href="<%=path%>/common/css/tree.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/contextmenu.css" type=text/css	rel=stylesheet>
		<script type="text/javascript" src="<%=path%>/common/js/common/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/component.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/tree.js" type="text/javascript"></script>
<style type="text/css">
#d1 {
}

#d2 {
	margin: 20px 0px 20px 0px;
}

</style>
		<base target=_self>
	<script type="text/javascript">
		
		$(document).ready(function() {
			var o = {
				title : '所属组织机构',
				cbiconpath : '<%=path%>/common/images/tree/',
				showcheck : false,
				cascadecheck : false,
				aHasSelected : "",
				parentCascadecheck:false,
				onnodeclick: function(node){ 
					window.dialogArguments.setOrg(node.id, node.text);
					window.close();
				},
				theme : "bbit-tree-lines",
				data : ${treeNodes}
			};
			$("#moduleTree").treeview(o);
		//	$("#moduleTree").initStaticSelectedNode();
			
		});
		
	</script>	
	</HEAD>
	<base target="_self">
	<BODY>
	
	
	<form id="form" theme="simple" action="" method="post">
		<input type=hidden id="orgid" name="orgid" value="${orgid }"/>
	</form>
	<div style="overflow:auto; height:500px;">
		<!--<div id=d1 class="tree_box" style="position: relative;">

			<div id="moduleTree"></div>
			<web:tree name="moduleTree" 
				lazyUrl="${root}/usermanage/usermanage!orgTree.action"
				iconDir="${path}/common/images/tree/" title="组织机构"
				contextMenu="buton_list" cascadeCheck="false" parentCascadeCheck="false" checkBox="true" hasSelected="${orgid}"></web:tree>
		</div>

		-->
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="vertical-align: top;">
				<tr>
					<td class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00" style="vertical-align: top;">
							<tr>
								<td>&nbsp;</td>
								<td class="table_headtd_img">
									<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
								</td>
								<td align="left">
									<strong>设置所属单位</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
					                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
											<td></td>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
											<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
											<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
		<div align="left" style="height: 240px;width: 250px; padding: 10px 0px 0px 10px;">
				
				<div id="moduleTree"></div>
				
			</div>
		
	</div>	
	</BODY>
</HTML>
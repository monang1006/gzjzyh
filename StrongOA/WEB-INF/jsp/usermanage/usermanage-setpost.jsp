<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>设置岗位</TITLE>
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
	margin: 0px 0px 20px 0px;
}

</style>
	<base target=_self>
	<BODY style="background-color:#ffffff">
	<DIV>
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
									<strong>设置岗位</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
					                  		<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
											<td class="Operation_input" onclick="selectpostid()">&nbsp;确&nbsp;定&nbsp;</td>
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
			<iframe id="hiddenFrame" name="hiddenFrame" style="height:0; width:0; display:none;"></iframe>
			<s:form id="getpostform" target="hiddenFrame"
				action="/usermanage/usermanage!setUserPost.action">
				<input type="hidden" name="postId" id="postId">
				<input type="hidden" name="userId" id="userId">
				<input type="hidden" id="extOrgId" name="extOrgId" value="${extOrgId}">
			</s:form>
			<div style="overflow-y:auto; overflow-x:hidden; height:450px;margin-left: 20px">
				<div id=d1 class="tree_box" style="position: relative;">
					<div id="moduleTree"></div>
					<tr>
						<td valign=top align="center">
							<div id="divMain" oncontextmenu="return ( mTree.OnMenuShow());"
								style="overflow :auto;"
								onresizeend="">
								<font color = 'red' size="4">用户机构下未设置岗位</font>
							</div>
						</td>
					</tr>
				</div>
			</div>	
		</div>
	</BODY>
</HTML>

<script>
	$(document).ready(function() {
		var o = {
			title : '岗位信息',
			cbiconpath : '<%=path%>/common/images/tree/',
			showcheck : true,
			cascadecheck : true,
			theme : "bbit-tree-lines",
			aHasSelected : "${initPost}",
			data : ${treeNodes}
		};
		var fId = '${treeNodes}';
		if(fId != ''&&fId.indexOf("post")!=-1){
			$("#moduleTree").treeview(o);
			$("#divMain").css("display","none");
			$("#d3").css("display","none");
		}else{
			$("#d2").css("display","none");
		}
	});

	function selectpostid(){
		var id="";
		var items = $("#moduleTree").getTSNs();
		if(items.length > 0){
			for(var i=0; i<items.length; i++){
				var item = items[i];
				if(item.value != "org"){
					id = id + "," + item.id;
				}
			}
		}
		if(id != ""){
			id = id.substring(1);
		}
		var userId = '${userId}' ;
		document.getElementById("postId").value = id;
		document.getElementById("userId").value = userId;
		document.getElementById("getpostform").submit();
		//window.close();
	}
</script>
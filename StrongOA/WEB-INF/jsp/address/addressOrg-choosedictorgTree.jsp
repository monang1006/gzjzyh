<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<HTML>
	<HEAD>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<title>发文红头</title>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>			
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>	
		<style>
		/*查看或者添加页面上的顶部按钮样式——强调*/
			.Operation_input{
				background:url(<%=frameroot1%>/images/ch_h_m.gif) repeat-x;
				font-weight: bold;
				color:white;
				cursor: pointer;
			}
			/*查看或者添加页面上的顶部按钮样式——非强调*/
				.Operation_input1{
					background:url(<%=frameroot1%>/images/ch_z_m.gif) repeat-x;
					font-weight: bold;
					cursor: pointer;
					color:#454953;
				}
				.table_headtd{
					height: 41px;
					background:url(<%=frameroot1%>/images/ck_line.jpg) repeat-x top;
				}
				.table_headtd_img{
					padding-left : 20px;
					width: 24px;
					padding-top:3px;
				}
		</style>
	</HEAD>

	<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder cellpadding="0" style="overflow:hidden;">
	     <table border="0" cellpadding="0" cellspacing="0" width="100%" height="20%" style="vertical-align: top;">
				<tr>
					<td  class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00" style="vertical-align: top;">
							<tr>
								<td>&nbsp;</td>
								<td class="table_headtd_img">
									<img src="<%=frameroot1%>/images/ico/ico.gif">&nbsp;
								</td>
								<td align="left">
									<strong>发文红头</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
					                  		<td width="7"><img src="<%=frameroot1%>/images/ch_h_l.gif"/></td>
											<td class="Operation_input" id="save">&nbsp;保&nbsp;存&nbsp;</td>
											<td width="7"><img src="<%=frameroot1%>/images/ch_h_r.gif"/></td>
					                  		<td width="5"></td>
											<td width="7"><img src="<%=frameroot1%>/images/ch_z_l.gif"/></td>
											<td class="Operation_input1" onClick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
											<td width="6"><img src="<%=frameroot1%>/images/ch_z_r.gif"/></td>
					                  		<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign=top style="padding-left:40px;">
                    <div style="width:100%; height:220px; overflow-y:auto;">
					<div id="moduleTree" heght="80%"></div>
					<web:tree name="moduleTree" cascadeCheck="false" parentCascadeCheck="false"
						data="${orgList}" radio="true" 
						iconDir="${root}/theme/theme_default/image/tree/" title="${root1}"
						contextMenu="buton_list"></web:tree>
			<!--<tree:strongtree title="${root}"  check="true" chooseType="chooseOne" dealclass="com.strongit.oa.address.util.OrgTreeDeal" data="${orgList}" target="project_work_content"  />-->	
                     </div>   
					</td>
				</tr>
				<tr>
					<td align="center">
					<td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
<script type="text/javascript">
$(document).ready(function(){
    $("#save").click(function(){
 
    	var selectedNodes = $("#moduleTree").getTSNs(true);
    	if(selectedNodes.length==0)
    	{
    	  alert("请选择数据！");
			return ;
    	}
    	var ret = new Array();
		ret.push(selectedNodes[0].text);		
		if(ret.length == 0){
			alert("请选择数据！");
			return ;
		}
		var param = window.dialogArguments;
				var zf = "、";
				if(param){
					if(typeof(param) == "string"){
						zf = param;
					}
				}
		window.returnValue = ret.join(zf);
		window.close();
    });
  
    $("#cancel").click(function(){
        window.close();
    });
});



function reloadData() {
    $('#moduleTree').reLoadData();
}

</script>
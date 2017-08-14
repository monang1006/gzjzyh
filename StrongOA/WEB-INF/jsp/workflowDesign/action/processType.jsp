<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程类型</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<script language='javascript' src='<%=root%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=root%>/common/js/menu/menu.js" type="text/javascript"></script>
			<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script type="text/javascript">
			function showName(ptType){		
				if(ptType=='1'){
					return "<font color='red'>系统</font>";
				}else{	
					return "<font color='red'>非系统</font>";
				}
			}
		</script>
	</head>
	
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()">
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>流程类型列表</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="processTypeAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="processTypeEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="processTypeDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						<form id="myTableForm" action="<%=root%>/workflowDesign/action/processType.action">
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="ptId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" showSearch="false"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<webflex:flexCheckBoxCol caption="选择" property="ptId"
									showValue="ptName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="类型ID" property="ptName"
					                showValue="ptId" width="10%" isCanDrag="true"
					                isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="类型名称" property="ptName"
									showValue="ptName" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="类型说明" property="ptDescription"
									showValue="ptDescription" width="40%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否系统类型" property="ptType"
									showValue="javascript:showName(ptType)" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</form>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript" type="text/javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","processTypeAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","processTypeEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","processTypeDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addLine();
	<%--
	item = new MenuItem("<%=root%>/images/ico/tb-change.gif","冻结列","frezeColum",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	--%>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function processTypeAdd(){
	var url="<%=root%>/workflowDesign/action/processType!input.action"
	var returnvalue = window.showModalDialog(url,"processTypeAddWindow","dialogWidth:500pt;dialogHeight:250pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
	if(returnvalue == true){
		location = "<%=path%>/workflowDesign/action/processType.action";
	}
}
function processTypeEdit(){
	var id=getValue();
	if(id == ""){
		alert("请选择要编辑的记录。");
		return ;
	}else{
		var ids = id.split(",");
		if(ids.length>1){
			alert("只可以编辑一条记录。");
			return ;
		}
	}
	var url="<%=root%>/workflowDesign/action/processType!input.action?ptId="+id;
	var returnvalue = window.showModalDialog(url,"processTypeEditWindow","dialogWidth:500pt;dialogHeight:250pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
	if(returnvalue == true){
		location = "<%=path%>/workflowDesign/action/processType.action";
	}
}
function processTypeDel(){
	var id=getValue();
	if(id == ""){
		alert("请选择要删除的记录。");
		return ;
	}
	if(confirm("删除操作将导致该类型下的流程信息全部丢失，是否继续操作？")){
		location="<%=root%>/workflowDesign/action/processType!delete.action?ids="+id;
	}
}
</script>
	</body>
</html>

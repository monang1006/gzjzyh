<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>流程代理设置</title>
		<link href="<%=frameroot%>/css/strongitmenu.css"
			type="text/css" rel="stylesheet">
		 <LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script language="javascript"
			src="<%=root%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
<script language="javascript" type="text/javascript">

function showdestart(deIsStart){
	if("1"==deIsStart){
		return "已生效";
	}else{
		return "未生效";
	}
}

</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT()">
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								 <td colspan="3" class="table_headtd">
								 	<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>流程委派列表</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="addDelegation();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="viewDelegation();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="cancelDelegation();"><img src="<%=root%>/images/operationbtn/Cancel.png"/>&nbsp;删&nbsp;除&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
														<%--
														<td width="60">
															<a class="Operation" href="javascript:deleteDelegation();">
																<img class="img_s" src="<%=root%>/images/ico/bianji.gif" width="14" height="15" alt="">
																	删除</a>
														</td>
														<td width="5"></td>
														--%>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						
						<form id="myTableForm"
							action="<%=root%>/workflowDelegation/action/processDelegation.action">
							<webflex:flexTable name="myTable" width="100%" height="370px" showSearch="false"
								wholeCss="table1" property="deId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<!-- <table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" alt="">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<input name="model.deId" type="text" tyle="width:100%">
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>-->
								<webflex:flexCheckBoxCol caption="选择" property="deId"
									showValue="dhDeleRealname" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="委派人" align="center" showsize="35" property="dhDeleRealname"
									showValue="dhDeleRealname" width="35%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="deStartDate"
									showValue="deStartDate" width="30%" isCanDrag="true" showsize="35"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="deEndDate"
									showValue="deEndDate" width="30%" showsize="35" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm:ss"></webflex:flexDateCol>
								<webflex:flexTextCol caption="是否生效" align="center" showsize="35" property="deIsStart"
									showValue="javascript:showdestart(deIsStart);" width="35%" isCanDrag="true"
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addDelegation",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewDelegation",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Cancel.png","删除","cancelDelegation",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addDelegation(){
	var url="<%=root%>/workflowDelegation/action/processDelegation!input.action?type=add";
	var returnvalue = window.showModalDialog(url,window,"dialogWidth:470pt;dialogHeight:335pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnvalue == true){
		location = "<%=root%>/workflowDelegation/action/processDelegation.action";
	}
}
function viewDelegation(){
	var id=getValue();
	if(id == ""){
		alert("请选择要查看的记录。");
		return ;
	}else{
		var ids = id.split(",");
		if(ids.length>1){
			alert("只可以查看一条记录。");
			return ;
		}
	}
	var url="<%=root%>/workflowDelegation/action/processDelegation!input.action?type=edit&deId="+id;
	var returnvalue = window.showModalDialog(url,window,"dialogWidth:500pt;dialogHeight:300pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnvalue == true){
		location = "<%=root%>/workflowDelegation/action/processDelegation.action";
	}
}

/**
	@author 邓志城
	@comment 删除代理事项
	@date 2010年6月30日16:12:25
*/
/*function deleteDelegation(){
	var id = getValue();
	if(id == ""){
		alert("请选择要删除的委派事项。");
		return ;
	}
	if(confirm("确定要删除选定的委派事项？")){
		location="<%=root%>/workflowDelegation/action/processDelegation!delete.action?ids="+id;
	}
}*/

function cancelDelegation(){
	var id=getValue();
	if(id == ""){
		alert("请选择要删除的记录。");
		return ;
	}
	if(confirm("确定要删除吗？")){
		location="<%=root%>/workflowDelegation/action/processDelegation!delete.action?ids="+id;
	}
}

</script>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp"%>
		<title>人员绩效列表</title>		
		<link href="<%=frameroot%>/css/strongitmenu.css"
			type="text/css" rel="stylesheet">
		<link
      href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script language="javascript"
			src="<%=root%>/common/js/menu/menu.js"
			type="text/javascript"></script>		
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
								<td height="40"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="20%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9" alt="">&nbsp;
												人员绩效列表
											</td>
											<td width="20%">
												&nbsp;
											</td>
											<td width="55%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td>
															&nbsp;
														</td>
														<td width="5%">
															<img src="<%=root%>/images/ico/tianjia.gif" width="10"
																height="10" alt="">
														</td>
														<td width="7%">
															<a href="javascript:addDelegation();">添加</a>
														</td>
														<td width="5%">
															<img src="<%=root%>/images/ico/chakan.gif" width="14"
																height="15" alt="">
														</td>
														<td width="7%">
															<a href="javascript:viewDelegation();">查看</a>
														</td>
														<td width="5%">
															<img src="<%=root%>/images/ico/quxiao.gif" width="12"
																height="12" alt="">
														</td>
														<td width="7%">
															<a href="javascript:cancelDelegation();">取消 </a>
														</td>
														<td width="20">
															&nbsp;
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<form id="myTableForm"
							action="<%=root%>/workflowAnalyze/action/processAnalyze!ryjx.action">
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" alt="">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<input name="taskOperator" type="text" style="width: 100%">
										</td>
										<td class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" valuepos="0"
									valueshowpos="0" width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="操作者ID" valuepos="0"
									valueshowpos="0" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="任务数量" valuepos="1"
									valueshowpos="1" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="总耗时" valuepos="2" valueshowpos="2"
									width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="平均耗时" valuepos="3"
									valueshowpos="3" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript" type="text/javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tb-add.gif","添加","addDelegation",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/tb-change.gif","查看","viewDelegation",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/tb-delete3.gif","取消","cancelDelegation",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addLine();
	item = new MenuItem("<%=root%>/images/ico/tb-change.gif","冻结列","frezeColum",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addDelegation(){
	var url="<%=root%>/workflowDelegation/action/processDelegation!input.action?type=add";
	var returnvalue = window.showModalDialog(url,window,"dialogWidth:500pt;dialogHeight:250pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnvalue == true){
		location = "<%=root%>/workflowDelegation/action/processDelegation.action";
	}
}
function viewDelegation(){
	var id=getValue();
	var url="<%=root%>/workflowDelegation/action/processDelegation!input.action?type=edit&deId="+id;
	var returnvalue = window.showModalDialog(url,window,"dialogWidth:500pt;dialogHeight:250pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
	if(returnvalue == true){
		location = "<%=root%>/workflowDelegation/action/processDelegation.action";
	}
}
function cancelDelegation(){
	var id=getValue();
	if(confirm("取消操作将导致该代理任务全部返回，是否继续操作？")){
		location="<%=root%>/workflowDelegation/action/processDelegation!delete.action?ids="+id;
	}
}
</script>
	</body>
</html>
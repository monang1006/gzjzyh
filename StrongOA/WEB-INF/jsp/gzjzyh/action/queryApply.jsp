<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
<HEAD>
<TITLE>日志列表</TITLE>
<%@include file="/common/include/meta.jsp"%>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
	rel="stylesheet">
<!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css"
	type=text/css rel=stylesheet>
<script language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
<script language='javascript'
	src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<script language="javascript"
	src="<%=path%>/common/js/common/windowsadaptive.js"></script>
<!--右键菜单脚本 -->
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></script>
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/workservice.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>

</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;"
	onLoad="initMenuT()">
	<script language="javascript" type="text/javascript"
		src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/mylog/myLog.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img"><img
												src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
											<td align="left"><strong>查询申请列表</strong></td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="addTemplate();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="editTemplate();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="deleteTemplate();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="2%"></td>
													</tr>

												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><webflex:flexTable name="myTable" width="100%"
										height="370px" wholeCss="table1" property="dictCode"
										isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA"
										footShow="showCheck" getValueType="getValueByProperty"
										collection='${page.result}' page="${page}">
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
											class="table1_search">
											<tr>
												<td>&nbsp;&nbsp;查询需求：&nbsp; <s:select name="accoutType"
														list="#{'':'请选择','0':'个人账号','1':'单位帐号','2':'个人开户明细','3':'单位开户明细','4':'交易明细'}"
														listKey="key" listValue="value" /> 
														
													&nbsp;&nbsp;文书编号：&nbsp;<input
													id="appFileno" name="appFileno" type="text">


													&nbsp;&nbsp;开始时间：&nbsp; <strong:newdate id="appStartDate"
														name="appStartDate" dateform="yyyy-MM-dd HH:mm:ss"
														isicon="true" dateobj="${model.appStartDate}"
														classtyle="search" title="搜索开始时间" />

													&nbsp;&nbsp;结束时间：&nbsp; <strong:newdate id="appEndDate"
														name="appEndDate" dateform="yyyy-MM-dd HH:mm:ss"
														isicon="true" dateobj="${model.appEndDate}"
														classtyle="search" title="搜索结束时间" />

													&nbsp;&nbsp;请求银行：&nbsp; <s:select name="appBankuser"
														list="#{'':'请选择','1':'工行','0':'建行'}" cssClass="search"
														title="请输入操作结果" onchange='$("#img_sousuo").click();'></s:select>
													

												</td>
												<tr>
													<td>
													&nbsp;&nbsp;案件名称：&nbsp;<input
														id="appFileno" name="appFileno" type="text">
														<input class="Operation_list"
															type="button" onClick="getListBySta();" />
															
													
													</td>
													<td  onclick="deleteTemplate();">选择案件</td>
													<td>
														&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo"
													type="button" onClick="getListBySta();" />
													</td>
												</tr>
											</tr>
										</table>
										<webflex:flexCheckBoxCol caption="选择" property="appId"
											showValue="appFileno" width="4%" isCheckAll="true"
											isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
										<webflex:flexTextCol caption="查询需求" property="appId"
											showValue="appId" width="18%" isCanDrag="true"
											isCanSort="true"></webflex:flexTextCol>
										<webflex:flexTextCol caption="文书编号" property="appFileno"
											align="center" showValue="appFileno" width="12%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										<webflex:flexTextCol caption="请求银行" property="appBankuser"
											align="center" showValue="appBankuser" width="18%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										<webflex:flexTextCol caption="申请时间" property="appDate"
											align="center" showValue="appDate" width="18%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										<webflex:flexTextCol caption="审核时间" property="appDate"
											align="center" showValue="appDate" width="18%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										<webflex:flexTextCol caption="当前状态" property="appStatus"
											align="center" showValue="appStatus" width="12%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									</webflex:flexTable></td>
							</tr>

						</table> </s:form>
						</DIV> <script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","show",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item); 
    item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
    sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}


function del(){//废除日志信息
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的记录。');
		return;
	}
	if(confirm("确定删除吗？")) 
	{ 
	   location = '<%=path%>/mylog/myLog!delete.action?logId='+id;
	} 
	

	
}

function show(){//显示日志信息
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要查看的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以查看一条记录。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/mylog/myLog!input.action?logId="+ id, window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:250px');
							}

							function getListBySta() { //根据属性查询
								//document.getElementById("disLogo").value="search";
								document.getElementById("myTableForm").submit();
							}
						</script>
</BODY>
</HTML>

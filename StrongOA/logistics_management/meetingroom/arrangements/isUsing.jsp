<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<%--<title>会议计划列表</title>--%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("会议室");
			test.setMoney("安排人");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("编号");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="34" align="center">
												<img
													src="../../../common/frame/images/perspective_leftside/ico.gif"
													width="9" height="9">
											</td>
											<td width="150" align="right">
												信息显示列表
											</td>
											<td>
												&nbsp;
											</td>
											<td width="290">
												<table width="283" border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td width="89">
															&nbsp;
														</td>
														<td width="20">
															<img
																src="../../../common/frame/images/perspective_leftside/bianji.gif"
																width="14" height="15">
														</td>
														<td width="34">
															<a href="javascript:addArrangement();">添加</a>
														</td>
														<td width="20">
															<img
																src="../../../common/frame/images/perspective_leftside/shanchu.gif"
																width="14" height="15">
														</td>
														<td width="34">
															<a href="javascript:editArrangement();">调整</a>
														</td>
														<td width="20">
															<img
																src="../../../common/frame/images/perspective_leftside/tianjia.gif"
																width="14" height="15">
														</td>
														<td width="34">
															<a href="javascript:deleteArrangement();">删除</a>
														</td>
														<td width="20">
															<img
																src="../../../common/frame/images/perspective_leftside/tianjia.gif"
																width="14" height="15">
														</td>
														<td width="34">
															<a href="javascript:viewArrangement();">查看</a>
														</td>
														<td width="20">
															<img
																src="../../../common/frame/images/perspective_leftside/tianjia.gif"
																width="14" height="15">
														</td>
														<td width="34">
															<a href="javascript:useArrangement();">生效</a>
														</td>
														<td width="10">
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
						<webflex:flexTable name="titleTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${testList}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img
											src="../../../common/frame/images/perspective_leftside/sousuo.gif"
											width="17" height="16">
									</td>
									<td width="10%" class="biao_bg1">
										<input name="textfield1" type="text" size="11">
									</td>
									<td width="20%" align="center" class="biao_bg1">
										<input name="textfield2" type="text" size="25">
									</td>
									<td width="45%" align="center" class="biao_bg1"></td>
									<td width="18%" align="center" class="biao_bg1">
										<select id="status">
											<option value="0">
												已预订
											</option>
											<option value="1">
												已抵达
											</option>
											<option value="2">
												未抵达
											</option>
										</select>
										<input align="right" type="button" value="查 询">
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="申请单编号" property="item"
								showValue="item" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室" property="name"
								showValue="name" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="计划开始时间" property="date"
								showValue="date" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="计划结束时间" property="date"
								showValue="date" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexTextCol caption="安排人" property="money"
								showValue="money" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="状态" property="item"
								showValue="item" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=frameroot%>/images/tb-add.gif","添加","addArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/tb-change.gif","调整","editArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/tb-delete3.gif","删除","deleteArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/tb-add.gif","查看","viewArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/tb-add.gif","生效","useArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

</script>
	</BODY>
</HTML>

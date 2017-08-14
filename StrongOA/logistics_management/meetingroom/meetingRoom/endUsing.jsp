<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ page import="java.util.*"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<title>正在使用的会议室安排列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/upload/jquery.blockUI.js'></script>
		<base target="_self">
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("选择的会议室");
			test.setMoney("编号");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("接待人");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()" style="overflow: auto">
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
										<tr>
											<td width="5%" align="center">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">
											</td>
											<td width="20%">
												正在使用的会议室安排列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="80">
												<a class="Operation" href="#" onclick="endUsing()"> <img
														src="<%=frameroot%>/images/jieshu.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">结束使用</span> </a>
											</td>
											<td width="5"></td>
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
											src="<%=frameroot%>/images/perspective_leftside/sousuo.gif"
											width="17" height="16">
									</td>
									<td width="35%" align="center" class="biao_bg1">
										<input id="name" type="text" class="search" title="请输入会议室名称">
									</td>
									<td width="25%" align="center" class="biao_bg1">
										<strong:newdate id="startTime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="25%" align="center" class="biao_bg1">
										<strong:newdate id="endTime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="10%" class="biao_bg1"></td>
									<td class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexRadioCol caption="选择" property="id" showValue="name"
								width="5%" isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
							<webflex:flexTextCol caption="申请单编号" property="money"
								showValue="money" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室名称" property="name"
								showValue="name" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="计划开始时间" property="date"
								showValue="date" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="计划结束时间" property="date"
								showValue="date" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexTextCol caption="接待人" property="item"
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
	//var item = null;
	//item = new MenuItem("<%=frameroot%>/images/tb-add.gif","使用","endUsing",1,"ChangeWidthTable","checkMoreDis");
	//sMenu.addItem(item);
	//sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function endUsing(){
	alert("该会议室结束使用，并且判断该会议室有没有其他时间的安排，如果没有则将会以室状态改为空，否则改为预订！会议室安排状态为‘使用完毕’，将该会议室的安排记录的结束时间改为此刻！")
}
</script>
	</BODY>
</HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<title>会议计划列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<base target="_self">
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("名称");
			test.setMoney("类型");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("状态");
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
											<td width="5%" align="center">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">
											</td>
											<td width="20%" align="left">
												已审核的该会议室的申请单
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="50">
												<a class="Operation" href="#" onclick="reset()">
													<img
														src="<%=frameroot%>/images/queding.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">清 空</span> </a>
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
									<td width="35%" class="biao_bg1">
										<input id="name" type="text" class="search" title="请输入会议室名称">
									</td>
									<td width="25%" class="biao_bg1">
										<strong:newdate id="startTime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="35%" class="biao_bg1">
										<strong:newdate id="endTime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
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
	//item = new MenuItem("<%=frameroot%>/images/tb-add.gif","清 空","reset",1,"ChangeWidthTable","checkMoreDis");
	//sMenu.addItem(item);
	//sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function reset(){
	
}
</script>
	</BODY>
</HTML>

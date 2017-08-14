<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>待办工作列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		  <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("张三(" + i + ")");
			test.setMoney("待办工作。。。");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("待办...");
			test.setImg1("2009-03-20");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT();">
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
													width="9" height="9" align="center">
											</td>
											<td width="20%">
												信息显示列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="50">
												
											</td>
											<td width="5"></td>
											<td width="50">
												
											</td>
											<td width="5"></td>
											<td width="50">
												
											</td>
											<td width="5"></td>
											<td width="60">
												<a class="Operation" href="#" onclick="window.close()">
													<img
														src="<%=frameroot%>/images/shanchu.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">关 闭</span> </a>
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
									<td width="30%" class="biao_bg1">
										<input id="id" type="text" class="search" title="请输入工作名称">
									</td>
									<td width="25%" class="biao_bg1">
										<input id="name" type="text" class="search" title="请输入处理人姓名">
									</td>
									<td width="20%" class="biao_bg1">
										<input id="content" type="text" class="search" title="请输入处理状态">
									</td>
									
									<td class="biao_bg1" width="20%">
									<strong:newdate name="startDate" id="startDate"  width="100%"
                      skin="whyGreen" isicon="true" dateobj="${startDate}" dateform="yyyy-MM-dd"></strong:newdate>
									</td>
				
									<td class="biao_bg1" width="*">
									&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="工作名称" property="money" showValue="money"
								width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="处理人姓名" property="name"
								showValue="name" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="处理状态" property="item"
								showValue="item" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							
							<webflex:flexTextCol caption="处理时间" property="img1"
								showValue="img1" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
		var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	
    registerMenu(sMenu);
}
</script>
	</BODY>
</HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>会议议题列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>	
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<base target="_self" />
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("类型(" + i + ")");
			test.setMoney("议题内容和描述");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("类型描述(" + i + ")");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>

	<script type="text/javascript">
	function queding(){
		window.close();
	}
	function quxiao(){
		window.close();
	}
	function add(){
		window.location="../title/meeting_addTitle.jsp?param=selectTitle";
	}
</script>


	<%--<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>--%>
	<BODY oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
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
											<td width="20%">
												信息显示列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="50">
												<a class="Operation" href="#" onclick="queding()">
													<img src="<%=frameroot%>/images/queding.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">确定</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="quxiao()">
													<img src="<%=frameroot%>/images/quxiao.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">取消</span> </a>
											</td>
											<td width="5"></td>		
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="titleTable" width="100%" height="465px"
							showSearch="false" wholeCss="table1" property="id"
							isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA"
							footShow="showCheck" getValueType="getValueByProperty"
							collection="${testList}">
							<webflex:flexRadioCol caption="选择" property="id" showValue="name"
								width="5%" isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
							<webflex:flexTextCol caption="类型编号" property="id" showValue="id"
								width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="类型名称" property="name"
								showValue="name" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="类型描述" property="item"
								showValue="item" width="60%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</DIV>
	<body>
</HTML>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<html>
	<head>
		<title>会议计划列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>	<!--右键菜单样式 -->
		<script language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></script><!--右键菜单脚本 -->
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/upload/jquery.blockUI.js'></script>
	</head>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("会议");
			test.setMoney("申请人");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("议题分类");
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
													<img
														src="<%=frameroot%>/images/queding.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">确定</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="quxiao()">
													<img
														src="<%=frameroot%>/images/quxiao.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">取消</span> </a>
											</td>
											<td width="5"></td>			
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="titleTable" width="100%" height="364px" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"showSearch="false" getValueType="getValueByProperty" collection="${testList}">
							<webflex:flexRadioCol caption="选择" property="id" showValue="name" isCanDrag="false" isCanSort="false" width="5%"></webflex:flexRadioCol>
							<webflex:flexTextCol caption="会议室名称" property="name"	 showValue="name" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="申请人" property="name" showValue="name" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="申请时间" property="date" showValue="date" isCanDrag="true" isCanSort="true"  width="15%"></webflex:flexDateCol>
							<webflex:flexDateCol caption="开始使用时间" property="date" showValue="date" isCanDrag="true" isCanSort="true" width="15%"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束使用时间" property="date" showValue="date" isCanDrag="true" isCanSort="true" width="15%"></webflex:flexDateCol>
							<webflex:flexTextCol caption="状态" property="name" showValue="name" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
<%--	var item = null;--%>
<%--	item = new MenuItem("<%=frameroot%>/images/queding.gif","确定","queding",1,"ChangeWidthTable","checkMoreDis");--%>
<%--	sMenu.addItem(item);--%>
<%--	item = new MenuItem("<%=frameroot%>/images/quxiao.gif","取消","quxiao",1,"ChangeWidthTable","checkMoreDis");--%>
<%--	sMenu.addItem(item);--%>
<%--	sMenu.addShowType("ChangeWidthTable");--%>
    registerMenu(sMenu);
}
function queding(){

}
function quxiao(){
	alert("申请单的状态仍为‘已审核'")
}
</script>
	</BODY>
</HTML>

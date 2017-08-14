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
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
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
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
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
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
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
												<a class="Operation" href="#" onclick="addArrangement()">
													<img
														src="<%=frameroot%>/images/tianjia.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="editArrangement()">
													<img
														src="<%=frameroot%>/images/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">修改</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="deleteArrangement()">
													<img
														src="<%=frameroot%>/images/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="viewArrangement()">
													<img
														src="<%=frameroot%>/images/chakan.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
											</td>
											<td width="5"></td>									
											<td width="80">
												<a class="Operation" href="#" onclick="useArrangement()">
													<img
														src="<%=frameroot%>/images/kaishi.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">开始使用</span> </a>
											</td>
											<td width="5"></td>									
											<td width="80">
												<a class="Operation" href="#" onclick="endUseArrangement()">
													<img
														src="<%=frameroot%>/images/jieshu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">结束使用</span> </a>
											</td>
											<td width="5"></td>									
											<td width="50">
												<a class="Operation" href="#" onclick="backArrangement()">
													<img
														src="<%=frameroot%>/images/tianjia.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">驳回</span> </a>
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
									<td width="10%" class="biao_bg1">
										<input name="id" type="text" class="search" title="申请单编号">
									</td>
									<td width="25%" align="center" class="biao_bg1">
										<input name="name" type="text" title="请输入会议室名称" class="search">
									</td>
									<td width="20%" align="center" class="biao_bg1">
										<strong:newdate id="stime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="20%" align="center" class="biao_bg1">
										<strong:newdate id="etime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="20%" align="center" class="biao_bg1">
										<select id="status" class="search">
											<option value="0">
												-----请选择-----
											</option>
											<option value="1">
												已预订
											</option>
											<option value="2">
												使用中
											</option>
											<option value="3">
												未抵达
											</option>
											<option value="4">
												使用完毕
											</option>
										</select>
									</td>
									<td class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="申请单编号" property="item"
								showValue="item" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室" property="name"
								showValue="name" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="计划开始时间" property="date"
								showValue="date" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="计划结束时间" property="date"
								showValue="date" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexDateCol>
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
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","调整","editArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","deleteArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看","viewArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/kaishi.gif","开始使用","useArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/jieshu.gif","结束使用","endUseArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","驳回","backArrangement",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addArrangement(){
	alert("申请单的状态将改为‘处理’，并且会议室的状态改为预订");
	location="frame.jsp";
}
function editArrangement(){
	alert("申请单的状态将改为‘处理’，并且会议室的状态改为预订");
	location="frame.jsp?type=edit";
}
function deleteArrangement(){
	alert("删除预订和使用完毕状态的的会议室安排");
}
function viewArrangement(){
	OpenWindow("viewArrangement.jsp",'350pt','180pt','viewWindow');
}
function useArrangement(){
	alert("将会议室状态改为‘使用’，会议室安排状态改为'使用中'，并且会议室使用记录表中增加一条记录，将该会议室的安排记录拷贝到记录表中，并且时间开始时间为此刻，结束时间置空！");
}
function endUseArrangement(){
	alert("该会议室结束使用，并且判断该会议室有没有其他时间的安排，如果没有则将会议室状态改为空，否则改为预订！会议室安排状态为‘使用完毕’，将该会议室的安排记录的结束时间改为此刻！")
}
function backArrangement(){
	alert("驳回未抵达的会议室安排，并且将申请单的状态改为‘驳回’，将未抵达的会议室安排删除！");
}
</script>
	</BODY>
</HTML>

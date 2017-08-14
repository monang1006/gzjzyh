<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>值班记录列表</title>
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
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("会议");
			test.setMoney("13768521456");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("来访记录");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
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
												<a class="Operation" href="#" onclick="addWatchRecord()">
													<img src="<%=frameroot%>/images/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="editWatchRecord()">
													<img src="<%=frameroot%>/images/bianji.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">修改</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="deleteWatchRecord()">
													<img src="<%=frameroot%>/images/shanchu.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="viewWatchRecord()">
													<img src="<%=frameroot%>/images/chakan.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="sendWatchRecord()">
													<img src="<%=frameroot%>/images/fasong.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">发送</span> </a>
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
									<td width="15%" class="biao_bg1">
										<input id="name" type="text" class="search" title="请输入来访人姓名">
									</td>
									<td width="15%" class="biao_bg1">
										<strong:newdate id="time" width="100%"
											dateform="yyyy-MM-dd HH:mm"></strong:newdate>
									</td>
									<td width="20%" class="biao_bg1">
										<input id="place" type="text" class="search" title="请输入来访人地址">
									</td>
									<td width="15%" class="biao_bg1">
										<input id="tel" type="text" class="search" title="请输入来访人电话">
									</td>
									<td width="15%" class="biao_bg1">
										<input id="person1" type="text" class="search"
											title="请输入被访人姓名">
									</td>
									<td width="15%" class="biao_bg1">
										<input id="person2" type="text" class="search" title="请输入接待人">
									</td>
									<td class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="来访人姓名" property="name"
								showValue="name" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="来访时间" property="date"
								showValue="date" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexTextCol caption="来访人地址" property="item"
								showValue="item" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="来访人电话" property="money"
								showValue="money" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="被访人员" property="item"
								showValue="item" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="接待人" property="item"
								showValue="item" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addWatchRecord",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","编辑","editWatchRecord",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images//shanchu.gif","删除","deleteWatchRecord",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看","viewWatchRecord",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/fasong.gif","发送","sendWatchRecord",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addWatchRecord(){
	location="addWatch.jsp";
}
function editWatchRecord(){
	location="addWatch.jsp";
}
function deleteWatchRecord(){
		
}
function viewWatchRecord(){
	location="viewWatch.jsp";
}
function sendWatchRecord(){
	OpenWindow("sendWatch.jsp",'350pt','150pt','sendWindow');
}
</script>
	</BODY>
</HTML>

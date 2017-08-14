<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
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
											<td width="15%">
												信息显示列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="50">
												<a class="Operation" href="#" onclick="addMeetingRoom()">
													<img
														src="<%=frameroot%>/images/tianjia.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>	
											<td width="50">
												<a class="Operation" href="#" onclick=":editMeetingRoom()">
													<img
														src="<%=frameroot%>/images/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑</span> </a>
											</td>
											<td width="5"></td>	
											<td width="50">
												<a class="Operation" href="#" onclick="deleteMeetingRoom()">
													<img
														src="<%=frameroot%>/images/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>	
											<td width="50">
												<a class="Operation" href="#" onclick="viewMeetingRoom()">
													<img
														src="<%=frameroot%>/images/chakan.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
											</td>
											<td width="5"></td>	
											<td width="80">
												<a class="Operation" href="#" onclick="addApplication()">
													<img
														src="<%=frameroot%>/images/shengqing.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">申请使用</span> </a>
											</td>
											<td width="5"></td>	
											<td width="80">
												<a class="Operation" href="#" onclick="startUsing()">
													<img
														src="<%=frameroot%>/images/kaishi.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">开始使用</span> </a>
											</td>
											<td width="5"></td>	
											<td width="80">
												<a class="Operation" href="#" onclick="endUsing()">
													<img
														src="<%=frameroot%>/images/jieshu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">结束使用</span> </a>
											</td>
											<td width="5"></td>	
											<td width="110">
												<a class="Operation" href="#" onclick="viewHistory()">
													<img
														src="<%=frameroot%>/images/chakanlishi.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">查看历史记录</span> </a>
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
										<input id="id" type="text" class="search" title="请输入会议室编号">
									</td>
									<td width="35%" class="biao_bg1">
										<input id="name" type="text" title="请输入会议室名称" class="search">
									</td>
									<td width="30%" class="biao_bg1">
										<input id="type" type="text" title="请输入会议室类型" class="search">
									</td>
									<td width="15%" class="biao_bg1">
										<select id="status" class="search">
											<option value="0">
												--请选择--
											</option>
											<option value="1">
												空
											</option>
											<option value="2">
												预订
											</option>
											<option value="3">
												使用
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
							<webflex:flexTextCol caption="会议室编号" property="name"
								showValue="name" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室名称" property="name"
								showValue="name" isCanDrag="true" width="35%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室类型" property="item"
								showValue="item" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议室状态" property="money"
								showValue="money" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","调整","editMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","deleteMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看","viewMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shengqing.gif","申请使用","addApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/kaishi.gif","开始使用","startUsing",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/jieshu.gif","结束使用","endUsing",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/chakanlishi.gif","查看历史记录","viewHistory",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addMeetingRoom(){
	OpenWindow("addMeetingRoom.jsp",'450pt','350pt','addRoomWindow');
}
function editMeetingRoom(){
	OpenWindow("addMeetingRoom.jsp",'450pt','350pt','editRoomWindow');
}
function deleteMeetingRoom(){
	alert("只用会议室状态为空或预定的时候才可以删除会议室，删除会议室后，需删除对应的会议室申请单和会议室安排！");
}
function viewMeetingRoom(){
	OpenWindow("viewMeetingRoom.jsp",'450pt','250pt','editRoomWindow');
}
function addApplication(){
	OpenWindow("../application/addApplication.jsp",'480pt','270pt','addAppWindow');
}
function startUsing(){
	//OpenWindow("startUsing.jsp",'600pt','400pt','usingWindow');
	location="frame.jsp?type=start";
}
function endUsing(){
	OpenWindow("endUsing.jsp",'600pt','400pt','endUsingWindow');
}
function viewHistory(){
	location="viewHistory.jsp";
}
</script>
	</BODY>
</HTML>

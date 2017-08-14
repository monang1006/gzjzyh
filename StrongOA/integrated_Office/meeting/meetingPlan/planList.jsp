<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>会议计划列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<script type="text/javascript" language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT type="text/javascript" language="javascript"
			src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/checkboxvalidate.js"></script>
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("会议");
			test.setMoney("地点");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("议题分类");
			test.setImg1("未审核");
			test.setImg3("领导");
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
												<a class="Operation" href="#" onclick="addMeetingPlan()">
													<img
														src="<%=frameroot%>/images/tianjia.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="editMeetingPlan()">
													<img
														src="<%=frameroot%>/images/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">修改</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="deleteMeetingPlan()">
													<img
														src="<%=frameroot%>/images/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="viewMeetingPlan()">
													<img
														src="<%=frameroot%>/images/shenyue.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">送审</span> </a>
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
										<input id="name" title="输入会议名称" class="search" />
									</td>
									<td width="20%" class="biao_bg1">
										<strong:newdate id="time" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="20%" class="biao_bg1">
										<input name="place" type="text" class="search" title="请输入会议地点">
									</td>
									<td width="10%" class="biao_bg1">
										<input name="person" type="text" class="search" title="主持人">
									</td>
									<td width="20%" class="biao_bg1">
										<input name="content" type="text" class="search"
											title="请输入会议内容">
									</td>
									<td width="10%" class="biao_bg1">
										<select id="shenhe" onchange="display()" class="search">
											<option value="0">
												未审核
											</option>
											<option value="1">
												已审核
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
							<webflex:flexTextCol caption="会议名称" property="name"
								showValue="name" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="拟办时间" property="date"
								showValue="date" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexTextCol caption="会议地点" property="money"
								showValue="money" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="主持人" property="img3"
								showValue="img3" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="会议内容" property="item"
								showValue="item" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="审核状态" property="img1"
								showValue="img1" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addMeetingPlan",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","修改","editMeetingPlan",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","deleteMeetingPlan",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/chakan.gif","审核","viewMeetingPlan",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addMeetingPlan(){
	location="addnewplan.jsp";
}
function editMeetingPlan(){
	location="addMeetingPlan.jsp";
}
function deleteMeetingPlan(){
		
}
function viewMeetingPlan(){
	//location="viewMeetingPlan.jsp";
	OpenWindow("songshen.jsp",'400pt','230pt','addWindow');
}
function display(){
	location="noticeList.jsp"
}
</script>
	</BODY>
</HTML>

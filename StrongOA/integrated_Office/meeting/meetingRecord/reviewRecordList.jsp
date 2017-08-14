'<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<title>已审核的会议记录列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
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
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
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
			test.setImg1(frameroot + "/images/math.gif");
			test.setItem("议题分类");
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
											<td width="80">
												<a class="Operation" href="#" onclick="sendMeetingRecord()">
													<img
														src="<%=frameroot%>/images/fasong.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">发送会议记录</span> </a>
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
									<td width="20%" class="biao_bg1">
										<input id="id" type="text" class="search" title="请输入议题编号">
									</td>
									<td width="20%" class="biao_bg1">
										<input id="name" type="text" class="search" title="请输入议题名称">
									</td>
									<td width="35%" class="biao_bg1">
										<input id="content" type="text" class="search" title="请输入议题内容">
									</td>
									<td width="20%" class="biao_bg1">
										<select id="shenhe" onchange="display()" class="search">
											<option id="weishenhe" selected="selected" value="0">
												未审核
											</option>
											<option id="yishenhe" value="1">
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
							<webflex:flexImgCol caption="会议记录" property="img1"
								showValue="img1" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexImgCol>
							<webflex:flexTextCol caption="议题编号" property="id" showValue="id"
								width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题名称" property="name"
								showValue="name" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题内容" property="money"
								showValue="money" width="35%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="所属议题分类" property="item"
								showValue="item" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=frameroot%>/images/fasong.gif","发送会议记录","sendMeetingRecord",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function sendMeetingRecord(){
	location="sendRecord.jsp";
}
function display(){
	location="recordList.jsp"
}
</script>
	</BODY>
</HTML>

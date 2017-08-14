<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<html>
	<head>
		<title>会议计划列表</title>
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
		<base target="_self">
	</HEAD>
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
												<a class="Operation" href="#" onclick="addApplication()">
													<img
														src="<%=frameroot%>/images/tianjia.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="editApplication()">
													<img
														src="<%=frameroot%>/images/bianji.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">修改</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="deleteApplication()">
													<img
														src="<%=frameroot%>/images/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
												<td width="50">
												<a class="Operation" href="#" onclick="viewApplication()">
													<img
														src="<%=frameroot%>/images/chakan.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="submitApplication()">
													<img
														src="<%=frameroot%>/images/tijiao.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">提交</span> </a>
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
										<input id="name" title="输入会议室名称" class="search" />
									</td>
									<td width="25%" class="biao_bg1">
										<input id="person" type="text" class="search" title="请输入申请人">
									</td>
									<td width="20%" class="biao_bg1">
										<strong:newdate id="stime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="20%" class="biao_bg1">
										<strong:newdate id="etime" dateform="yyyy-MM-dd HH:ss"
											width="100%" />
									</td>
									<td width="10%" class="biao_bg1">
										<select id="status" class="search">
											<option value="0">
												--请选择--
											</option>
											<option value="1">
												待提交
											</option>
											<option value="2">
												已提交
											</option>
											<option value="3">
												已审批
											</option>
											<option value="4">
												处理
											</option>
											<option value="5">
												驳回
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
							<webflex:flexTextCol caption="会议室名称" property="name"
								showValue="name" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="申请人" property="name"
								showValue="name" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="申请时间" property="date"
								showValue="date" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="开始使用时间" property="date"
								showValue="date" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束使用时间" property="date"
								showValue="date" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexTextCol caption="状态" property="name"
								showValue="name" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","修改","editApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","deleteApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/chakan.gif","查看","viewApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/tijiao.gif","提交","submitApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addApplication(){
	OpenWindow("addApplication.jsp",'480pt','270pt','addAppWindow');
}
function editApplication(){
	OpenWindow("addApplication.jsp",'480pt','270pt','addAppWindow');
}
function deleteApplication(){
	alert("只有待提交和已提交的申请单能执行删除操作！");
}
function viewApplication(){
	OpenWindow("addApplication.jsp",'480pt','270pt','addAppWindow');
}
function submitApplication(){
	alert("提交申请单，更改申请单的状态为已提交");
}
</script>
	</BODY>
</HTML>

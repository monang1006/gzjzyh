<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>会议议题分类列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
	</HEAD>
	<%
		List testList = new ArrayList();
		for (int i = 1; i <= 20; i++) {
			ListTest test = new ListTest();
			test.setId(new Long(i));
			test.setName("议题分类(" + i + ")");
			test.setMoney("议题内容和描述");
			test.setDate(new Date());
			test.setNum(i * 50);
			test.setItem("议题分类(" + i + ")");
			testList.add(test);
		}
		request.setAttribute("testList", testList);
	%>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
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
													width="9" height="9"  align="center">
											</td>
											<td width="10%">
												会议分类列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="50">
												<a class="Operation" href="#" onclick="addClassificTitle()">
													<img src="<%=frameroot%>/images/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="editClassificTitle()">
													<img src="<%=frameroot%>/images/bianji.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑</span> </a>
											</td>
											<td width="50">
												<a class="Operation" href="#" onclick="deleteClassificTitle()">
													<img src="<%=frameroot%>/images/shanchu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onclick="viewClassificTitle()">
													<img src="<%=frameroot%>/images/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
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
									<td width="5%"  align="center" class="biao_bg1">
										<img
											src="<%=frameroot%>/images/perspective_leftside/sousuo.gif"
											width="17" height="16">
									</td>
									<td width="20%" class="biao_bg1">
										<input id="bianhao" type="text" class="search"
											title="请输分类编号 ">
									</td>
									<td width="30%" class="biao_bg1">
										<input id="name" type="text" class="search" title="请输入分类名称">
									</td>
									<td width="50%" class="biao_bg1">
										<input id="desc" type="text" class="search" title="请输入分类描述">
									</td>
									<td class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="分类编号" property="id" showValue="id"
								width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="分类名称" property="name"
								showValue="name" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="分类描述" property="item"
								showValue="item" width="50%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
		</DIV>
		<script type="text/javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addClassificTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","修改","editClassificTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","deleteClassificTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addClassificTitle(){
	OpenWindow("meeting_addClassific.jsp",'400pt','230pt','addWindow');
}
function editClassificTitle(){
	OpenWindow("meeting_editClassific.jsp",'400pt','230pt','addWindow');
}
function deleteClassificTitle(){
		
}
function viewClassificTitle(){
	OpenWindow("meeting_viewClassific.jsp",'400pt','230pt','addWindow');
}
</script>
	</BODY>
</HTML>

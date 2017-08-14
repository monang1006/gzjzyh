<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="com.strongit.bo.*"%>
<%@ page import="java.util.*"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->

		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=path%>/common/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<LINK href="<%=path%>/common/frame/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<%
	List testList = new ArrayList();
	for(int i=1;i<=11;i++){
		ListTest test = new ListTest();
		test.setId(new Long(i));
		test.setName("大连("+i+")");
		test.setMoney("￥"+(i*7));
		test.setDate(new Date());
		test.setNum(i*50);
		test.setItem("女");
		TreeNode node=new TreeNode();
		node.setId(String.valueOf(i));
		node.setName("节点"+i);
		node.setParentid("0");
		test.setNode(node);
		testList.add(test);
	}
	request.setAttribute("testList",testList);
%>
		<script type="text/JavaScript">
<!--
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
//-->
</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">

		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=path%>/common/images/ico.gif" width="9"
													height="9">&nbsp;
												信息显示列表
											</td>
											<td width="70%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="5%">
															<img src="<%=path%>/common/images/quanxuan.gif"
																width="15" height="15">
														</td>
														<td width="10%">
															全选
														</td>
														<td width="5%">
															<img src="<%=path%>/common/images/tianjia.gif" width="10"
																height="10">
														</td>
														<td width="10%">
															添加
														</td>
														<td width="5%">
															<img src="<%=path%>/common/images/bianji.gif" width="14"
																height="15">
														</td>
														<td width="10%">
															修改
														</td>
														<td width="5%">
															<img src="<%=path%>/common/images/shanchu.gif" width="12"
																height="12">
														</td>
														<td width="10%">
															删除
														</td>
														<td width="5%">
															<img src="<%=path%>/common/images/baocun.gif" width="14"
																height="14">
														</td>
														<td width="10%">
															保存
														</td>
														<td width="25%">
															&nbsp;
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="myTable" width="100%" height="250px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${testList}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="4%" align="center" class="biao_bg1">
										<img src="<%=path%>/common/images/sousuo.gif" width="17"
											height="16">
									</td>
									<td width="60%" class="biao_bg1">
										<input name="textfield" type="text" size="60">
									</td>
									<td width="14%" align="center" class="biao_bg1">
										<input name="textfield2" type="text" size="18">
									</td>
									<td width="14%" align="center" class="biao_bg1">
										<input name="textfield22" type="text" size="18">
									</td>
									<td width="8%" class="biao_bg1">
										&nbsp;
										<select name="menu1" onChange="MM_jumpMenu('parent',this,0)">
											<option>
												已读
											</option>
											<option>
												未读
											</option>
										</select>
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="id"
								showValue="name" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="文本列" property="name"
								showValue="name" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="货币列" property="money"
								showValue="money" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="日期列" property="date"
								showValue="date" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexNumricCol caption="数值列" property="num"
								showValue="num" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexNumricCol>
							<webflex:flexTextCol caption="枚举列" property="item"
								showValue="item" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="节点" property="node.id"
								showValue="node.name" width="20%" isCanDrag="true"
								isCanSort="true" onclick="clicknode(this)"></webflex:flexTextCol>
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
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-add.gif","增加","gotoAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-change.gif","编辑","gotoAdd",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-delete3.gif","删除","gotoAdd",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addLine();
	item = new MenuItem("<%=request.getContextPath()%>/common/images/tb-change.gif","冻结列","frezeColum",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function gotoAdd(){
	alert(getValue());
}
function clicknode(obj){
	alert(obj.value);
}
</script>
	</BODY>
</HTML>

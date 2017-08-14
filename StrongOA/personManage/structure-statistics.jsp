<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>机构编制列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
				<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="">
				
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="60"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="5%" align="center">
													<img src="<%=frameroot%>/images/ico.gif" width="9"
														height="9">
												</td>
												<td width="10%">
													编制情况统计列表
												</td>
												<td width="5%">
													&nbsp;
												</td>
												<td width="85%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right"
																	cellpadding="0" cellspacing="0">

																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		
																		<td width="90" align="right">
																			<a class="Operation" href="person-statistics.jsp"> <img
																					src="<%=frameroot%>/images/chakan.gif" width="15"
																					height="15" class="img_s">查看情况</a>
																		</td>
																		<td width="5"></td>
																	
																		
																		
																		
																	</tr>
																</table>
															</td>

														</tr>
														
														<tr higth="20"></tr>

														<tr>


														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="userId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=frameroot%>/images/sousuo.gif" width="17" id="img_sousuo"
												height="16" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="25%" align="center" class="biao_bg1">
											 <s:textfield name="selectname" cssClass="search" title="请输入机构名称"></s:textfield>  
										</td>
										<td width="25%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入编制类型"></s:textfield>
										</td>
										<td width="25%" align="center" class="biao_bg1">
											 <s:textfield name="selectloginname" cssClass="search" title="请输入编制数量"></s:textfield> 
										</td>
										
										
										<td width="20%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入状态"></s:textfield>
										</td>
									
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
							<webflex:flexCheckBoxCol caption="选择" property="userId"
									showValue="userName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="机构名称" property="userId"
									showValue="userName" width="25%" isCanDrag="true"
									isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="编制类型" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="25%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="编制人数" property="userLoginname"
									showValue="userLoginname" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							
								<webflex:flexEnumCol caption="状态" mapobj="${userTypeMap}"
									property="userIsdel" showValue="userIsdel" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
 var orgid = '${orgId}';
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=frameroot%>/images/tianjia.gif","添加","addStructure",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/bianji.gif","修改","editStructure",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/yidong.gif","移动","move",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=frameroot%>/images/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
   
}


</script>
	</BODY>
</HTML>

<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>机构人员列表</TITLE>
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
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/personnel/baseperson/person.action" method="post">
				<input type="hidden" name="orgId" id="orgId" value="${orgId}">
				<input type="hidden" name="strucId" value="${strucId }" id="strucId">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>&nbsp;</td>
												<td width="15%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													人员列表
												</td>
												<td>
													&nbsp;
												</td>
												<td width="85%">
													<table  border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td align="right">
																<table  border="0" align="right"
																	cellpadding="0" cellspacing="0">

																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td  align="right">
																			<a class="Operation" href="javascript:show();"><img
																					src="<%=root%>/images/ico/chakan.gif" width="14"
																					height="14" class="img_s">查看</a>
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
								wholeCss="table1" property="personid" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17" id="img_sousuo"
												height="16" style="cursor: hand;" onclick="sousuo()" title="单击搜索">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield name="selectname" cssClass="search" title="请输入姓名"></s:textfield>  
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield name="selectloginname" cssClass="search" title="请输入工号"></s:textfield> 
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="所属机构"></s:textfield>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入性别"></s:textfield>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="请输入职位"></s:textfield>
										</td>
										<td width="10%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="籍贯"></s:textfield>
										</td>
										<td width="10%" align="center" class="biao_bg1">
											 <s:textfield id="baseorg" name="selectorg" cssClass="search" title="联系电话"></s:textfield>
										</td>
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="personid"
									showValue="personName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="人员姓名" property="personid"
									showValue="personName" width="15%" isCanDrag="true"
									isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
								<webflex:flexTextCol caption="工号" property="personLabourno"
									showValue="personLabourno" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="所属机构" property="baseOrg.orgId"
									showValue="baseOrg.orgName" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
									<webflex:flexEnumCol caption="性别" mapobj="${personSax}"
									property="personSax" showValue="personSax" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="职位" mapobj="${personPset}"
									property="personPset" showValue="personPset" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="籍贯" mapobj="${personNativePlace}"
									property="personNativePlace" showValue="personNativePlace" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								
								<webflex:flexTextCol caption="联系方式" property="baseOrg.orgId"
									showValue="baseOrg.orgName" width="10%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","show",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
    sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
    if(orgid!=""&& orgid!=null){
    document.getElementById('baseorg').value='${orgname}';
    document.getElementById('baseorg').readOnly=true;
    }
}


  function sousuo(){
      $("form").submit();
      }
  function getinfo(value){
	var orgId=$("#orgId").val();
	var id=value;
	// window.location="<%=path%>/fileNameRedirectAction.action?toPage=/personnel/baseperson/person-frame.jsp?orgId="+orgId+"&personId="+id+"&keyid="+id;
	window.location="<%=path%>/personnel/baseperson/person!initViewAddTool.action?forward=initviewedit&disLogo=viewinfo&personId="+id+"&keyid="+id;
  }
   function show(){
	var orgId=$("#orgId").val();
	var id=getValue();
	// window.location="<%=path%>/fileNameRedirectAction.action?toPage=/personnel/baseperson/person-frame.jsp?orgId="+orgId+"&personId="+id+"&keyid="+id;
	window.location="<%=path%>/personnel/baseperson/person!initViewAddTool.action?forward=initviewedit&disLogo=viewinfo&personId="+id+"&keyid="+id;
  }
</script>
	</BODY>
</HTML>

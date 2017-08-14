<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>休假情况统计</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=path%>/common/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/address/address.js"
			type="text/javascript"></script>
		<SCRIPT type="text/javascript">
			function viewDetails(value){
				var tables=document.getElementById("tables").value;
				var forward=document.getElementById("forward").value;
				var condition=document.getElementById("condition").value;
				window.parent.parent.frames("detail").location="<%=path%>/personnel/baseperson/person!getHolidayDetails.action?personId="+value+"&tables="+tables+"&condition="+encodeURI(encodeURI(condition))+"&forward="+forward;
			}
			
			 $(document).ready(function(){
			    $("#img_sousuo").click(function(){
			    	$("Form").submit();
			    });     
			  });
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
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
											<td>&nbsp;</td>
											<td>
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												<s:if test="forward==\"norest\"">
													未休假人员列表
												</s:if>
												<s:else>
													已休假人员列表
												</s:else>

											</td>
											<td width="82%">

											</td>
										</tr>
									</table>
								</td>
							</tr>
							<s:form id="myTableForm"
								action="/personnel/baseperson/person!getHolidayStatistic.action">
								<input type="hidden" id="tables" name="tables" value="${tables}" />
								<input type="hidden" id="condition" name="condition"
									value="${condition}" />
								<input type="hidden" id="orgId" name="orgId" value="${orgId}" />
								<input type="hidden" id="forward" name="forward"
									value="${forward}" />
								<webflex:flexTable name="myTable" width="100%" height="370px"
									pagename="holidaypage" wholeCss="table1" property="0"
									isCanDrag="true" showSearch="true" isCanFixUpCol="true"
									clickColor="#A9B2CA" footShow="showCheck" isShowMenu="false"
									onclick="viewDetails(this.value)"
									getValueType="getValueByArray"
									collection="${holidaypage.result}" page="${holidaypage}">
									<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">
										<tr>
											<td width="5%" align="center" class="biao_bg1">
												<img src="<%=root%>/images/ico/sousuo.gif" width="17"
													id="img_sousuo" height="16" style="cursor: hand;"
													title="单击搜索">
											</td>
											<td width="35%" align="center" class="biao_bg1">
												<input type="text" id="objName" name="objName"
													class="search" value="${objName}" title="请输入姓名">
											</td>
											<td width="60%" align="center" class="biao_bg1">
												<input type="text" disabled="disabled" class="search">
											</td>
											<td class="biao_bg1">
												&nbsp;
											</td>
										</tr>
									</table>
									<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="1" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="姓名" valuepos="1" valueshowpos="1"
										width="35%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexNumricCol caption="已休假天数" valuepos="2"
										valueshowpos="2" width="30%"></webflex:flexNumricCol>
									<webflex:flexNumricCol caption="公休假天数" valuepos="3"
										valueshowpos="3" width="30%"></webflex:flexNumricCol>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

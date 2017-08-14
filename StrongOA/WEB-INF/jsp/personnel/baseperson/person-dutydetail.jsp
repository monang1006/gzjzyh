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
			function getValue(){
				return "${dictName}";
			}
				
			$(document).ready(function(){
			    $("#img_sousuo").click(function(){
			    	$("Form").submit();
			    });     
			});
			
			function submitForm(tables,condition,dictCode){
				document.getElementById("tables").value=tables;
				document.getElementById("condition").value=condition;
				document.getElementById("dictCode").value=dictCode;
				$("Form").submit();
			}
				
			function getinfo(value){
	       		var url="<%=path%>/personnel/baseperson/person!initEditPerson.action?forward=viewRelationInfo&personId="+value+"&infoSetCode=40288239230c361b01230c7a60f10015"+"&keyid="+value;
				OpenWindow(url,520,450,window);		
			}
			
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
											<td width="5%" align="center">
											</td>
											<td>
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												人员信息
											</td>
											<td width="82%">

											</td>
										</tr>
									</table>
								</td>
							</tr>
							<s:form id="myTableForm"
								action="/personnel/baseperson/person!getDutyDetails.action">
								<input type="hidden" id="tables" name="tables" value="${tables}" />
								<input type="hidden" id="condition" name="condition"
									value="${condition}" />
								<input type="hidden" id="dictCode" name="dictCode"
									value="${dictCode}" />
								<webflex:flexTable name="myTable" width="100%" height="370px"
									pagename="holidaypage" wholeCss="table1" property="0"
									isCanDrag="true" showSearch="false" isCanFixUpCol="true"
									clickColor="#A9B2CA" footShow="showCheck" isShowMenu="false"
									getValueType="getValueByArray"
									collection="${holidaypage.result}" page="${holidaypage}">
									<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="1" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="姓名" valuepos="0" valueshowpos="1"
										width="15%" isCanDrag="true" isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
									<webflex:flexTextCol caption="出生日期" valuepos="2"
										valueshowpos="2" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="参加工作时间" valuepos="3"
										valueshowpos="3" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="现任职职务" valuepos="4"
										valueshowpos="javascript:getValue()" width="20%"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="现任职时间" valuepos="5"
										valueshowpos="5" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="任同职级时间" valuepos="6"
										valueshowpos="6" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

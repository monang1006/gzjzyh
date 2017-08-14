<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ page import="java.util.*"%>
<%@ page import="com.strongit.oa.bo.ToaSysmanageDictitem"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>休假情况统计明细</TITLE>
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
			function viewDetails(dutyId){
				var tables=document.getElementById("tables").value;
				var condition=document.getElementById("condition").value;
				//window.parent.frames("detail").location="<%=path%>/personnel/baseperson/person!getDutyDetails.action?dictCode="+dutyId+"&tables="+tables+"&condition="+encodeURI(encodeURI(condition));
				window.parent.frames("detail").submitForm(tables,condition,dutyId);
			}
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<s:form id="myTableForm"
								action="/personnel/baseperson/person!getHolidayDetails.action">
								<input type="hidden" id="tables" name="tables" value="${tables}" />
								<input type="hidden" id="condition" name="condition"
									value="${condition}" />
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="0" isCanDrag="true"
									showSearch="false" isCanFixUpCol="true" clickColor="#A9B2CA"
									footShow="showCheck" isShowMenu="false"
									getValueType="getValueByArray" collection="${detailList}">
									<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="1" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="职务" valuepos="0" valueshowpos="1"
										width="45%" onclick="viewDetails(this.value)"></webflex:flexTextCol>
									<webflex:flexNumricCol caption="人数" valuepos="0"
										valueshowpos="2" width="50%"></webflex:flexNumricCol>
									<%--<%
										List<ToaSysmanageDictitem> dictItemList=(List<ToaSysmanageDictitem>)request.getAttribute("dictItemList");
										ToaSysmanageDictitem dict;
										String title="";
										String tempNum;
										int leng=100/dictItemList.size();
										String width=leng+"%";
										for(int i=0;i<dictItemList.size();i++){
											dict=dictItemList.get(i);
											title=dict.getDictItemShortdesc();
											tempNum=String.valueOf(i);
									%>
									<webflex:flexNumricCol caption="<%=title%>" valuepos="<%=tempNum%>"
										valueshowpos="<%=tempNum%>" width="<%=width%>"  ></webflex:flexNumricCol>
								   <%
										}
								   %>
								--%>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

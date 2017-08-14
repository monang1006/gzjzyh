<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
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
	</HEAD>
	<script type="text/javascript">
		function getinfo(value){
       		var url="<%=path%>/personnel/baseperson/person!initEditPerson.action?forward=viewRelationInfo&infoSetCode=402882272860cb56012860fb5fb60002"+"&keyid="+value;
			OpenWindow(url,520,450,window);		
		}
	</script>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<s:form id="myTableForm"
								action="/personnel/baseperson/person!getHolidayDetails.action">
								<input type="hidden" id="personId" name="personId"
									value="${personId}" />
								<input type="hidden" id="tables" name="tables" value="${tables}" />
								<input type="hidden" id="condition" name="condition"
									value="${condition}" />
								<input type="hidden" id="forward" name="forward"
									value="${forward}" />
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="0" isCanDrag="true"
									showSearch="false" isCanFixUpCol="true" clickColor="#A9B2CA"
									footShow="showCheck" isShowMenu="false"
									getValueType="getValueByArray" collection="${detailList}">
									<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="1" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="休假开始时间" valuepos="0"
										valueshowpos="1" width="15%" isCanDrag="true" isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
									<webflex:flexTextCol caption="休假结束时间" valuepos="2"
										valueshowpos="2" width="15%"></webflex:flexTextCol>
									<webflex:flexTextCol caption="休假事由" valuepos="3"
										valueshowpos="3" width="10%"></webflex:flexTextCol>
									<webflex:flexEnumCol caption="是否公休假" valuepos="4"
										valueshowpos="4" width="10%" mapobj="${judge}"></webflex:flexEnumCol>
									<webflex:flexNumricCol caption="已休假天数" valuepos="5"
										valueshowpos="5" width="10%"></webflex:flexNumricCol>
									<webflex:flexTextCol caption="备注" valuepos="6" valueshowpos="6"
										width="35%" showsize="25"></webflex:flexTextCol>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

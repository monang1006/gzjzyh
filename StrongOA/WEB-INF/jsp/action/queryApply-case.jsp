<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
<HEAD>
<TITLE>案件列表</TITLE>
<%@include file="/common/include/meta.jsp"%>
<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
	rel=stylesheet>
<script type="text/javascript"
	src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
	rel="stylesheet">
<!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css"
	type=text/css rel=stylesheet>
<script language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
<script language='javascript'
	src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
<script language="javascript"
	src="<%=path%>/common/js/common/windowsadaptive.js"></script>
<!--右键菜单脚本 -->
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></script>
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/workservice.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>

</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;">
	<script language="javascript" type="text/javascript"
		src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/action/queryApply!casePage.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img"><img
												src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
											<td align="left"><strong>案件列表</strong></td>

										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><webflex:flexTable name="myTable" width="100%"
										height="370px" wholeCss="table1" property="dictCode"
										isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA"
										footShow="showCheck" getValueType="getValueByProperty" 
										collection='${casePage.result}' page="${casePage}">
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
											class="table1_search">
											<input type="hidden" id="caseConfirmTime" name="model.gzjzyhCase.caseConfirmTime" value="${model.gzjzyhCase.caseConfirmTime}">
											<input type="hidden" id="caseOrg" name="model.gzjzyhCase.caseOrg" value="${model.gzjzyhCase.caseOrg}">
											<tr>
												<td>&nbsp;&nbsp;案件编号：&nbsp;<input id="caseCode"
													name="caseCode" type="text">
													&nbsp;&nbsp;案件名称：&nbsp;<input id="caseName"
													name="caseName" type="text">
													&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo"
													type="button" onClick="castList();" />
												</td>
											</tr>
										</table>
										<webflex:flexRadioCol caption="选择" property="caseId"
											showValue="caseId" width="4%" 
											isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
										<webflex:flexTextCol caption="案件编号" property="caseCode"
											align="center" showValue="caseCode" width="20%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										<webflex:flexTextCol caption="案件名称" property="caseName"
											align="center" showValue="caseName" width="50%"
											isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									</webflex:flexTable></td>
							</tr>
							<tr>
								<td colspan="3" class="table_headtd">
									<table border="0" align="center" cellpadding="00"
										cellspacing="0">
										<tr>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_l.gif" /></td>
											<td class="Operation_input" onclick="changeCase();">&nbsp;确&nbsp;定&nbsp;</td>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_r.gif" /></td>
											<td width="5"></td>
											<td width="8"><img
												src="<%=frameroot%>/images/ch_z_l.gif" /></td>
											<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_z_r.gif" /></td>

										</tr>
									</table>
								</td>
							</tr>
						</table> </s:form>
						</DIV> <script language="javascript">
							function changeCase() {
								var caseId = $("input[name='chkRadio'][checked]").attr("value");
								var tr = $("#radio" + caseId).parent().parent();
								var caseName = $("td:eq(3)", tr).attr("value");
								var parentWin=window.dialogArguments;
								//parentWin.document.getElementById("caseCode").value=caseId;
								//parentWin.document.getElementById("caseName").value=caseName;
								
								parentWin.changeCaseF(caseId,caseName,caseOrg,caseConfirmTime);
								window.close();
							}
						</script>
</BODY>
</HTML>

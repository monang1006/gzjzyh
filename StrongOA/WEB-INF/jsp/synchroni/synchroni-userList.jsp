<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>财政机构用户列表</TITLE>
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
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/synchroni/synchroni!saveUser.action">
				<input type="hidden" name="orgId" id="orgId" value="${orgId}">
				<%--<input type="hidden" name="orgname" id="orgname" value="${orgname}">--%>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: bottom;">
				<tr>
						<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
												<td>&nbsp;</td>
												<td width="10%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
													用户列表
												</td>
												<td width="5%">
													&nbsp;
												</td>
												<td width="85%">
													
														<table width="100%" border="0" align="right" cellpadding="0"
											cellspacing="0">
											<tr>
												<td width="*">
													&nbsp;
												</td>
																		
															<td width="5"></td>
																		<td align="right">
																			<a class="Operation"
																				href="javascript:setSynchronize();"> <img
																					src="<%=root%>/images/ico/copyprivil.gif"
																					width="15" height="15" class="img_s">用户同步&nbsp;</a>
															</td>
																		
																		
											</tr>
										</table>
												
															</td>

														</tr>
														</table>
												
															</td>

														</tr>
														</table>
							<webflex:flexTable name="myTable" width="100%" height="300px"
								wholeCss="table1" property="userId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${usersList}">
								<webflex:flexTextCol caption="用户ID" property="userId"
									showValue="userId" width="35%" isCanDrag="true"
									isCanSort="true" showsize="35"></webflex:flexTextCol>
								<webflex:flexTextCol caption="用户名称" property="userName"
									showValue="userName" width="40%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否管理员" property="isCompanyAdmin"
									showValue="isCompanyAdmin" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
//与财政系统同步
function setSynchronize(){
       //alert(document.getElementById("orgId").value);
	document.getElementById("myTableForm").submit();
}

function select(){
	if(event.keyCode==13){ 
  		alert("Enter!");   
  	}	
}
</script>
	</BODY>
</HTML>

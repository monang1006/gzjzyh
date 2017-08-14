<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>催办任务</title>
    <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
  	 <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
	 <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
  	<script src="<%=path%>/oa/js/workflow/common.js" type="text/javascript"></script>
  </head>
  <base target="_self">
  <body class="contentbodymargin">
  	<DIV id=contentborder align=center>
  		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form action="/senddoc/sendDoc!urgencyProcessByPerson.action">
							<input id="instanceId" type="hidden" name="instanceId" value="<%=request.getParameter("instanceId") %>" /><!-- 流程实例id -->
							<table height="30" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								                  <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                  </td>
												<td align="left">
												<strong>催办方式</strong>
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="urgencyProcessByPerson();">&nbsp;确&nbsp;定&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
							<tr>
							<td class="table1_td"></td>
							<td></td>
						    </tr>
						     <tr>
						     <td>
							<!-- 提醒方式标签 -->
		 					 	<strong:remind isShowButton="false" includeRemind="RTX,SMS" rtxChecked="checked" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
		 					 </td>
		 					 </tr>
		 					 <tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
		 					 </table>	
						</s:form>
					</td>
				</tr>
			</table>
  	</DIV>
  </body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title> 查看外部系统链接</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
					 <td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查看外部系统链接</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						
							<input id="linkId" name="linkId" type="hidden" size="32"
								value="${modle.linkId}">
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">系统名称：</span>
									</td>
									<td class="td1" >
										<span class="wz">${model.systemName}</span>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">系统链接：</span>
									</td>
									<td class="td1" >
											<span class="wz">${model.linkUrl}</span>
									</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right" valign="top">
										<span class="wz">系统描述：</span>
									</td>
									<td class="td1" style="word-break:break-all;line-height: 1.4">
										${model.systemDesc}
										<%--							<input id="filtrateRaplace" name="filtrateRaplace" type="text" size="30" value="${modle.filtrateRaplace}" class="required">--%>
									</td>
								</tr>
							</table>
							
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title> 查看不良词汇和替代词</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows_add.css">
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
				<%--<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
												<td>
													<strong>查看过滤词语</strong>
												</td>
												<td width="10%">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							
							
							--%><table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							    <strong>查看过滤词语</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
							
							<input id="filtrateId" name="filtrateId" type="hidden" size="32"
								value="${modle.filtrateId}">
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">添加日期：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<%--							<strong:newdate id="filtrateTime" dateform="yyyy-MM-dd" mindate="2008-12-02" maxdate="%y-%M-%ld" width="170" dateobj="${modle.filtrateTime}" />--%>
										<strong:newdate id="filtrateTime" name="modle.filtrateTime"
											dateform="yyyy-MM-dd" width="170"
											dateobj="${modle.filtrateTime}"  />
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right" valign="top">
										<span class="wz"><FONT color="red">*</FONT>不良词汇：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" cols="35" id="filtrateWord"
											name="filtrateWord" class="required" style="overflow: auto;">${modle.filtrateWord}</textarea>
										<%--							<input id="filtrateWord" name="filtrateWord" type="text" size="30" value="${modle.filtrateWord}">--%>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">代替词：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" cols="35" id="filtrateRaplace"
											name="filtrateRaplace" style="overflow: auto;">${modle.filtrateRaplace}</textarea>
										<%--							<input id="filtrateRaplace" name="filtrateRaplace" type="text" size="30" value="${modle.filtrateRaplace}" class="required">--%>
									</td>
								</tr>
							</table>
							<%--<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="37%">
													<input id="close" type="button" class="input_bg"
														value="关 闭" onclick="window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						--%></td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>

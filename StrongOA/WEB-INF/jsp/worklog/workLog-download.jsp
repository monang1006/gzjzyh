<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>附件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows_list.css">
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
		<SCRIPT type="text/javascript">
					 //下载附件
		 function download(id){
		  	var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/worklog/workLog!down.action?attachId="+id;
		 }
			
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="50%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												附件列表
											</td>
											<td width="50%">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>--%>
						
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							 <tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>附件列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												  <td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	          <td class="Operation_input1" onClick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	          <td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		          <td width="6"></td>
												</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
						
						
						
						
						
						
						
						
						
						<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="1" align="center" class="table1">
							<s:iterator value="attachments" var="attachment">
								<tr>
									<td class="td1" width="100%">
										<a href="javascript:download('<s:property value="attachId"/>')"><s:property value="attachName" /></a>
									</td>
								</tr>
							</s:iterator>
						</table>
						
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>


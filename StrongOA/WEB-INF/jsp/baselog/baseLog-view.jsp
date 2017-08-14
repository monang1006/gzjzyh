<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

	<head>

		<%@include file="/common/include/meta.jsp"%>
		<title>查看日志</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>

		<script type="text/javascript">
	</script>
		<base target="_self">
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" >
		<DIV id="contentborder" align="center">
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
													<strong>查看日志</strong>
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
						<s:form action="" method="post"
							id="22" theme="simple">
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作人：</span>
									</td>
									<td class="td1" >
									  <span class="wz">${baseLog.logOpname}</span>
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作人IP：</span>
									</td>
									<td class="td1" >
									 <span class="wz">${baseLog.logOpip}</span>
										
									</td>
								</tr>



								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">操作开始时间：</span>
									</td>
									<td class="td1" >
                                     <s:date name="baseLog.logStartDate" format="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作结束时间：</span>
									</td>
									<td class="td1" >
									<s:date name="baseLog.logEndDate" format="yyyy-MM-dd HH:mm:ss"/>
									 </td>
								</tr>

								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作描述：</span>
									</td>
									<td class="td1" >
									  <span class="wz">${baseLog.rest3}</span>
										
									</td>
								</tr>
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作结果：</span>
									</td>
									<td class="td1" >
									  <span class="wz">${result}</span>
										
									</td>
								</tr>
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作方法：</span>
									</td>
									<td class="td1" >
									   <span class="wz">${baseLog.rest4}</span>
										
									</td>
								</tr>
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">备注：</span>
									</td>
									<td class="td1" style="word-break:break-all;line-height: 1.4">
									  ${baseLog.logOpcontent}
										
									</td>
								</tr>
							</table>
						</s:form>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>

						
					</td>
				</tr>
			</table>
		</DIV>

	</body>
</html>

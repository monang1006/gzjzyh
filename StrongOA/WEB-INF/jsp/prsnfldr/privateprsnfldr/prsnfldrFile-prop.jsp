<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>查看个人文件柜属性</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	</head>
   <base/>	  
    <body oncontextmenu="return false;">
			<%--<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;个人文件柜属性</td>
				</tr>
			</table>--%>
			
			
			<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>个人文件柜属性</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
			
			
			
			
			
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">	
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right"><span class="wz">已用空间：</span></td>
					<td  class="td1">
						<s:property value="used"/>
					</td>
				</tr>
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right"><span class="wz">可用空间：</span></td>
					<td class="td1">
						<s:property value="rest"/>
					</td>	
				</tr>
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right"><span class="wz">容量：</span></td>
					<td  class="td1">
						<s:property value="allSize"/>
					</td>
				</tr>
				
		</table>
</body>
</html>
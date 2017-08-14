<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看消息</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_list.css" rel="stylesheet" type="text/css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<script language='javascript' src='<%=root%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<style type="text/css">
			.AutoNewline
			{
			  word-break: break-all;/*必须*/
			}
		</style> 
	</head>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="45" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>
								&nbsp;
								</td>
								<td width="20%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									手机短信回复统计
								</td>
								<td>
									&nbsp;
								</td>
								--%>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
				       <tr align="center" >
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								
							</td>
							<td >
							<strong> 手机短信回复统计 </strong>
						    </td>
						    
							
							<td align="right">
								<table border="0" align="right" cellpadding="0" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="window.print()">&nbsp;打&nbsp;印&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
                                           <td>
												&nbsp;
												</td>
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
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<input id="receiver" name="Receiver" value="${message.msgReceiver}" type="hidden">
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">接收状态：</span>
					</td>
					<td class="td1" width="75%">
						<div id="allre" style="display:;">
							<span class="wz">接收手机短信总人数：<font color=red>${hasreply+unreply}</font>人，已回复手机短信：<font color=red>${hasreply}</font>人，未回复手机短信：<font color=red>${unreply}</font>人</span>
						</div> 
						<input type="button" name="more" id="more" class="button" style="display: none;" value="更多">
					</td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">内&nbsp;&nbsp;&nbsp;&nbsp;容：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz">${smsrep.smsRepCon}</span>
					</td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">短信发送日期：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz"><s:date name="smsrep.smsRepSendTime" format="yyyy-MM-dd HH:mm"/></span>
					</td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">短信发送人：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz">${smsrep.smsSenderName}</span>
					</td>
				</tr>

				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">未回复短信人员：</span>
					</td>
					<td class="td1 AutoNewline" width="75%">
							<span class="wz">${unreplyPerson}</span>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="more" id="more" class="button" style="display: none;" value="更多">
					</td>
				</tr>
				
				<tr>
					<td class="td1" width="100%" colspan="2">
						<webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="replyMessageId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="null" getValueType="getValueByProperty" collection="${replyList}" isShowMenu="false">
					<br>&nbsp;&nbsp;&nbsp;&nbsp;已回复短信列表：<br>
							<webflex:flexTextCol caption="回复人" property="replyUser" showValue="replyUser" width="20%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
							<webflex:flexTextCol caption="移动电话" property="mobileNumber" showValue="mobileNumber" width="25%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
							<webflex:flexTextCol caption="回复内容"  property="replyContent" showValue="replyContent" width="35%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
							<webflex:flexDateCol caption="回复时间" property="replyTime" showValue="replyTime" width="30%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexDateCol>
						</webflex:flexTable>
					</td>
				</tr>
				<%--<tr>
					<td class="td1" width="100%" colspan="2" align="center" cosplan=2>
						<input type="button" class="input_bg" id="quxiao" name="quxiao" value="关闭" onclick="window.close();"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" class="input_bg" id="print" name="print" value="打印" onclick="window.print()">
					</td>
				</tr>
			--%></table>
			</table>
		</div>
	</body>
</html>

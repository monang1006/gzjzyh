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
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
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
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="45" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>
								&nbsp;
								</td>
								<td widtn="20%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									<strong>手机短信、站内消息统计表</strong>
								</td>
								
								<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="window.print();">&nbsp;打&nbsp;印&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                </tr>
					            </table>
							</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<DIV class=tab-pane id=tabPane1>
				<SCRIPT type="text/javascript">
					tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
				</SCRIPT>
				<div class=tab-page id=tabPage1>
					<H2 class=tab>
						手机短信
					</H2>
					<tbody>
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
						<input id="receiver" name="Receiver" value="${message.msgReceiver}" type="hidden">
						<tr>
							<td class="biao_bg1" width="10%" align="right" style="padding-right: 0px;padding-left: 20px;">
								<span class="wz">接收状态：</span>
							</td>
							<td class="td1" width="85%">
								<div id="allre" style="display:;">
									<span class="wz">接收手机短信总人数：<font color=red>${hasreply+unreply}</font>人，已回复手机短信：<font color=red>${hasreply}</font>人，未回复手机短信：<font color=red>${unreply}</font>人</span>
								</div> 
								<input type="button" name="more" id="more" class="button" style="display: none;" value="更多">
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;padding-left: 20px;">
								<span class="wz">标&nbsp;&nbsp;&nbsp;&nbsp;题：</span>
							</td>
							<td class="td1" width="85%">
								<span class="wz">${message.msgTitle}</span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;padding-left: 20px;">
								<span class="wz">短信发送日期：</span>
							</td>
							<td class="td1" width="85%">
								<span class="wz"><s:date name="message.msgDate" format="yyyy-MM-dd HH:mm"/></span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;padding-left: 20px;">
								<span class="wz">短信发送人：</span>
							</td>
							<td class="td1" width="75%">
								<span class="wz">${message.msgSender}</span>
							</td>
						</tr>
		
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;padding-left: 20px;">
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
									<%--<webflex:flexCheckBoxCol caption="序号" property="replyMessageId" showValue="replyMessageId" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									--%><webflex:flexTextCol caption="回复人" property="replyUser" showValue="replyUser" width="20%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
									<webflex:flexTextCol caption="移动电话" property="mobileNumber" showValue="mobileNumber" width="25%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
									<webflex:flexTextCol caption="回复内容"  property="replyContent" showValue="replyContent" width="35%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
									<webflex:flexDateCol caption="回复时间" property="replyTime" showValue="replyTime" width="30%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexDateCol>
								</webflex:flexTable>
							</td>
						</tr>
						<!--<tr>
							<td class="td1" width="100%" colspan="2" align="center" cosplan=2>
								 <input type="button" class="input_bg" id="quxiao" name="quxiao" value="关闭" onclick="window.close();"/>&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" class="input_bg" id="print" name="print" value="打印" onclick="window.print()">
							</td>
						</tr>-->
					</table>
					</tbody>
					<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
				</div>
				<div  class=tab-page id=tabPage2>
					<H2 class=tab>
						站内消息
					</H2>
					<SCRIPT type="text/javascript">
						tp2 = new WebFXTabPane( document.getElementById( "tabPane2") ,false );
					</SCRIPT>
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
						<tr>
							<td class="td1" colspan="2">
								<span class="wz"><font color="gray">接收人员：人员名称为红色表示未读，为黑色表示已读取站内消息</font></span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;padding-left: 100px;">
								<span class="wz">接收状态：</span>
							</td>
							<td class="td1" width="85%">
								<div>
									<span class="wz">接收站内消息总人数：<font color=red>${readMsgNum+noReadMsgNum}</font>人,已阅站内消息：<font color=red>${readMsgNum }</font>人,未阅站内消息:<font color=red>${noReadMsgNum }</font>人 </span></span>
								</div>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px; padding-left: 100px;">
								<span class="wz">标&nbsp;&nbsp;&nbsp;&nbsp;题：</span>
							</td>
							<td class="td1" width="85%">
								<span class="wz">${message.msgTitle}</span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1 AutoNewline" width="15%" align="right" style="padding-right: 0px;padding-left: 100px;" valign="top">
								<span class="wz">接收人员：</span>
							</td>
							<td class="td1" width="85%">
								<div id="allre" style="display:;">
									<span class="wz">
										<s:iterator value="#request.receiverList" id="vo" status="index">
											<s:if test="#vo.isRead==0">
												<s:if test="#index.index+1==receiverList.size()">
												<font color=red style="font-weight:bold"><s:property value="#vo.receiverName"/></font>
												</s:if>
												<s:else>
												<font color=red style="font-weight:bold"><s:property value="#vo.receiverName"/></font>,
												</s:else>
											</s:if>
											<s:else>
											<s:if test="#index.index+1==receiverList.size()">
												<font style="font-weight:bold"><s:property value="#vo.receiverName"/></font>
											</s:if>
											<s:else>
												<font style="font-weight:bold"><s:property value="#vo.receiverName"/></font>,
												</s:else>
											</s:else>
										</s:iterator>
									</span>
								</div>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" name="more" id="more" class="button" style="display: none;" value="更多">
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;padding-left: 100px;">
								<span class="wz">发送日期：</span>
							</td>
							<td class="td1" width="85%">
								<span class="wz"><s:date name="message.msgDate" format="yyyy-MM-dd HH:mm"/></span>
							</td>
						</tr>
					 <!--<tr>
						<td class="td1" width="100%" colspan="2" align="center" cosplan=2>
							  <input type="button" class="input_bg" id="quxiao" name="quxiao" value="关闭" onclick="window.close();"/>&nbsp;&nbsp;&nbsp;&nbsp;  <input type="button" class="input_bg" id="print" name="print" value="打印" onclick="window.print();">
						</td>
					</tr>-->
					</table>
				</div>
			</DIV>
		</div>
	</body>
</html>

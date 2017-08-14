<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>发送查询短信</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
		<link href="<%=frameroot%>/css/properties_windows_add.css" rel="stylesheet" type="text/css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#sendNumber").change(function(){
					$("#nowTel").val($("#sendNumber").val());
					chargeNumber($("#sendNumber").val());
				});
				$("#btnSend").click(function(){
					if($("#smsCon").val()==""){
						alert("信息内容不为空！");
					}else{
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/msgaccount/msgAccount!sendMsg.action",
							data:"nowTel="+$("#nowTel").val()+"&con="+$("#smsCon").val(),
							success:function(msg){
								if(msg=="success"){
									window.returnValue=true;
									window.close();
								}else if(msg=="error"){
									alert("发送异常!");
								}else if(msg=="no"){
									alert("号码问题!");
								}else{
									alert("发送失败!");
								}
							}
						});
					}
				});
			});
			
			function chargeNumber(number){
				if(number.length>=5){
					var preNum=number.substring(0,3);
					if(preNum=="130"||preNum=="131"||preNum=="132"||preNum=="133"||preNum=="134"||preNum=="153"||preNum=="155"||preNum=="185"||preNum=="186"){
						$("#smsCon").val($("#union").val());
					}else if(preNum=="135"||preNum=="136"||preNum=="137"||preNum=="138"||preNum=="139"||preNum=="150"||preNum=="151"||preNum=="152"||preNum=="156"||preNum=="157"||preNum=="158"||preNum=="159"||preNum=="187"||preNum=="188"){
						$("#smsCon").val($("#mobile").val());
					}else if(preNum=="180"||preNum=="189"){
						$("#smsCon").val($("#tel").val());
					}else{
						alert("位置号码段");
						$("#smsCon").val("");
					}
				}else{
					alert("您的号码有误");
				}
			}
		</script>
	</head>
	<body>
	<input type="hidden" name="mobile" id="mobile" value="${mobile }">
	<input type="hidden" name="tel" id="tel" value="${tel }">
	<input type="hidden" name="union" id="union" value="${union }">
	<input type="hidden" name="nowTel" id="nowTel">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center >
			<s:form action="sms!save.action" theme="simple" name="smsForm" id="smsForm">
				<%--<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td height="30" colspan="2"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
											<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													发送查询短信
												</td>
												<td>
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
								--%>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>发送查询短信</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="$('#btnSend').click()">&nbsp;发&nbsp;送&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	
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
								
								
								
								<tr>
									<td>
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz"><font color=red>*</font> 选择短信猫号码：</span>
										</td>
										<td  class="td1">
											<s:select list="sendList" listValue="smscomSimnum" listKey="smscomSimnum" name="sendNumber" id="sendNumber">
											</s:select>
										</td>
									</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right" valign="top">
											<span class="wz"><font color=red>*</font> 短信内容：</span>
										</td>
										<td  class="td1">
											<textarea cols=30 name="smsCon" id="smsCon" rows=5 class="BigInput"></textarea>
										</td>
									</tr>
									</table>
									</td>
								</tr>
							
					
					<tr align="center">
						<td>
							<input type="hidden" name="btnSend" id="btnSend" class="input_bg" value="发送" />
							<input type="hidden" name="btnCon" id="btnCon"  class="input_bg" value="关闭" onclick="window.close();" />
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script type="text/javascript">
			$("#nowTel").val($("#sendNumber").val());
			chargeNumber($("#sendNumber").val());
		</script>
	</body>
</html>
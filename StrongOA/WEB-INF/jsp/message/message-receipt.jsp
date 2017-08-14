<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>消息回执提示</TITLE>
		<%@include file="/common/include/meta.jsp"%>	
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
		<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<base target="_self">
		<script type="text/javascript">
		$(document).ready(function(){
			//document.getElementById("myTableForm").submit();
			$.post("<%=path%>/message/message!receipt.action",{msgId:$("#msgId").val(),sfhz:$("#sfhz").val()},function(data){
			});
		});
		</script>
	</HEAD>
	<%
		String msgId=request.getParameter("msgId");
	 %>
	<BODY>
		<div id="contentborder" align="center">
		<s:form theme="simple" id="myTableForm" action="/message/message!receipt.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="40%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												<span class="wz">消息回执提示</span>			
											</td>
											<td width="*%">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="100%" align="center">
				<tr align="center">
					<td valign="middle">
						<!-- <fieldset style="width: 90%;">
							<legend>请选择是否已读回执</legend>	
							<table align="center">
								<tr>
									
									<td class="td1" align="center" width="50%">	
										<input type="radio" id="yes" name="sfhz" value="1" checked="checked" align="middle" onclick="setRadioVal(this.value)"><span class="wz">回执</span>		
										<input type="hidden" id="msgId" name="msgId" value="<%=msgId %>">
										<input type="hidden" id="sfhz">		
									</td>
									
									<td class="td1" align="center" width="50%">
										<input type="radio" id="no" name="sfhz" value="0" align="middle" onclick="setRadioVal(this.value)"><span class="wz">不回执</span>	
									</td>
								</tr>
							</table>
						</fieldset>-->
						对方要求回执,消息已送出!
						<input type="hidden" id="msgId" name="msgId" value="<%=msgId %>">
						<input type="hidden" id="sfhz" name="sfhz" value="1">	
					</td>
				</tr>
			</table>		
			</s:form>	
			<table width="100%" align="center">
				<tr align="center">
					<td valign="middle">
						<input type="button"  class="input_bg" id="queding" name="queding" onclick="submitForm();" value="确定" /> &nbsp;&nbsp;
					</td>
				</tr>
			</table>			
		</div>
		<SCRIPT type="text/javascript">
			function setRadioVal(val){
				$("#sfhz").val(val);
			}
		
			function submitForm(){
				
				//window.returnValue="isgood";
				window.close();
			}
			
		</SCRIPT>
	</BODY>
</HTML>

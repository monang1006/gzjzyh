<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<title>手机运营商信息</title>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#save").click(function(){
					if($("#mobile").val()==""||isNaN($("#mobile").val())){
						alert("移动运营商号码为数字且不能为空！");
						$("#mobile").focus();
					}else if($.trim($("#mobiletext").val())==""){
						alert("移动查询话费发送内容不为空!");
					}else if($("#unicom").val()==""||isNaN($("#unicom").val())){
						alert("联通运营商号码为数字且不能为空！");
						$("#unicom").focus();
					}else if($.trim($("#unicomtext").val())==""){
						alert("联通查询话费发送内容不为空!");
					}else if($("#telecom").val()==""||isNaN($("#telecom").val())){
						alert("电信运营商号码为数字且不能为空！");
						$("#telecom").focus();
					}else if($.trim($("#telecomtext").val())==""){
						alert("联通查询话费发送内容不为空!");
					}else{
						$("#myForm").submit();
					}
				});
			});
		</script>
	
	</head>
	<body class=contentbodymargin>
		<DIV id=contentborder align=center>
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td>
						<table height="40px" width="100%"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<tr>
								<td>
									&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									<span class="wz">手机运营商信息</span>
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
								<strong>手机运营商信息</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="$('#save').click()">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	  
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="$('#res').click();">&nbsp;重&nbsp;置&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td> 
					                 	  
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				
				
				
				<tr width="100%">
				<s:form id="myForm" action="/smsplatform/smsPlatform!savePro.action">
					<table width="100%" height="80%" border="0" cellpadding="0"
				cellspacing="0" align="center" class="table1">
						<tr >
							<td   class="biao_bg1" align="right" style="width:180px">
								<span class="wz">移动运营商号码：</span>
							</td>
							
							<td class="td1" >
								<input id="mobile" name="mobile" value="${mobile }" type="text" size="30" maxlength="30">
							</td>
							
						</tr>
						<tr>
							<td  class="biao_bg1" align="right" style="width:180px">
								<span class="wz">移动查询话费发送内容：</span>
							</td>
							
							<td  class="td1">
								<input id="mobiletext" name="mobiletext" value="${mobiletext }" type="text" size="30"  maxlength="30">
							</td>
						</tr>
						<tr>
							<td   class="biao_bg1" align="right" style="width:180px">
								<span class="wz">联通运营商号码：</span>
							</td>
							<td class="td1">
								<input id="unicom" name="unicom" value="${unicom }" type="text" size="30"  maxlength="30">
							</td>
							
						</tr>
						<tr>
							<td   class="biao_bg1" align="right" style="width:180px">
								<span class="wz">联通查询话费发送内容：</span>
							</td>
							<td  class="td1">
								<input id="unicomtext" name="unicomtext" value="${unicomtext }" type="text" size="30"  maxlength="30">
							</td>
						</tr>
						<tr>
							<td   class="biao_bg1" align="right" style="width:180px">
								<span class="wz">电信运营商号码：</span>
							</td>
							<td class="td1">
								<input id="telecom" name="telecom" value="${telecom }" type="text" size="30"  maxlength="30">
							</td>
							
						</tr>
						<tr>
							<td   class="biao_bg1" align="right" style="width:180px">
								<span class="wz">电信查询话费发送内容：</span>
							</td>
							<td  class="td1">
								<input id="telecomtext" name="telecomtext" value="${telecomtext }" type="text" size="30"  maxlength="30">
							</td>
						</tr>
						<tr>
						
							<td  title="还原手机运营商信息配置到本次打开页面时是配置" style="display: none">
									<input name="button" type="reset" id="res" class="input_bg" value="重置">
								</td>
						</tr>
					</table>
			
			<table width="85%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="34%" height="34">
									&nbsp;
								</td>
							
								<td width="29%">
									<input id="save" name="save" type="hidden" class="input_bg"  value="保存">
								</td>
								
								<%--<td width="37%">
									<input name="button" type="reset" class="input_bg" value="重置">
								</td>
							--%></tr>
						</table>
					</td>
				</tr>
			</table>
			</s:form>
		</tr>
	</table>
		</DIV>
	</body>
</html>

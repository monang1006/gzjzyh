<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>

<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
	
		<title>移动通道配置文件</title>
		<script>
		function show(info){
			$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
			$.blockUI({ message: '<font color="#008000"><b>'+info+'</b></font>' });
		}
		$.unblockUI();
		
		$(document).ready(function(){
			var reg_mobile= /^(1\d{10})$/;
				$("#btnSaveConfig").click(function(){
					if(!isIP($("#chinamobile_dbip").val())){
						alert("数据库IP不能为空！");
						$("#chinamobile_dbip").focus();
						return ;
					}
					if(!isIP($("#chinamobile_dbusername").val())){
						alert("数据库名称不能为空！");
						$("#chinamobile_dbusername").focus();
						return ;
					}
					if(!isIP($("#chinamobile_dbpword").val())){
						alert("数据库密码！");
						$("#chinamobile_dbpword").focus();
						return ;
					}
					if(!isIP($("#chinamobile_masusername").val())){
							alert("MAS号码不能为空！");
							$("#chinamobile_masusername").focus();
							return ;
						}
					$("#span_smscomPort").hide();
					$("#span_smscomBps").hide();
					
					$("#smsConfForm").submit();
				
				});
				
				//check ip
				function isIP(sIPAddress)   { 
				 if($.trim(sIPAddress) == ""){
				 	return false;
				 }else{
				 	return true;
				 }
  				}
  				//check port
  				function isDigits(port){
  					if($.trim(port) == ""){
  						return false;
  					}else{
	  					var exp = /^\d+$/;
	  					return exp.test(port);
  					}
  				}
		});
		</script>
	</head>
	<body class="contentbodymargin">

		<DIV id=contentborder cellpadding="0" >
			<form  method="post" action="<%=root%>/sms/smsConf!saveConfig.action" theme="simple" name="smsConfForm"  target="myIframe" id="smsConfForm">
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="0" >
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00" >
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td  >
											&nbsp;
											</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												移动通道的参数
											</td>
											<td width="70%">

											</td>
										</tr>
									</table>
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
								<strong>移动通道的参数</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="$('#btnSaveConfig').click()">保存设置</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				
				
				<tr>
					<td>
					<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" align="center" class="table1">
					<tr>
						<td>
							<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> 数据库IP：</span>
									</td>
									<td class="td1" title="（保存设置后请将重起服务生效）">
										<input id="chinamobile_dbip" name="phoneConfig.chinamobile_dbip" value="${phoneConfig.chinamobile_dbip}" type="text" size="24">
									</td>
									
								</tr>
								<%--<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">全局开关：</span>
									</td>
									<td class="td1" width="90%" valign="top" title="（配置短信猫的全局开关，选择相应的开关状态）">
										<s:select label="选择短信猫开关状态" id="smscomOpen" name="model.smscomOpen" 
											 list="#{'1':'开启','0':'关闭'}"/>
									</td>
								</tr>
								--%>
								
								<tr>
								<td>
								</td>
								<td>
									<font color="#999999">(该配置的修改将在服务重起后生效)</font>
									</td>
								</tr>
								
								
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> 数据库名称：</span>
									</td>
									<td class="td1" title="（填入相应的数据库名称，如:WAVECOM）">
										<input id="chinamobile_dbusername" name="phoneConfig.chinamobile_dbusername" value="${phoneConfig.chinamobile_dbusername}" type="text" size="24">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> 数据库密码：</span>
									</td>
									<td class="td1" valign="top" title="（配置数据库密码）">
										<input id="chinamobile_dbpword" name="phoneConfig.chinamobile_dbpword" value="${phoneConfig.chinamobile_dbpword}" type="text" size="24">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> MAS用户名：</span>
									</td>
									<td class="td1" title="（配置MAS用户名）">
										<input id="chinamobile_masusername" name="phoneConfig.chinamobile_masusername" value="${phoneConfig.chinamobile_masusername}" type="text" size="24">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> MAS密码：</span>
									</td>
									<td class="td1" title="（填入MAS密码）">
										<input id="chinamobile_maspword" name="phoneConfig.chinamobile_maspword" value="${phoneConfig.chinamobile_maspword}" type="text" size="24">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="center">
						<td class="td1">
							<br>
								<input type="hidden" name="sendid" value="chinamobile">
							<%--<input type="submit" class="input_bg" value="保存设置"
							--%><input id="btnSaveConfig" type="hidden" class="input_bg" value="保存设置" />
						</td>
					</tr>
				</table>
				</td>
				</tr>
				
			</table>
			
			</form>
						<iframe name="myIframe" style="display:none"></iframe>
		</DIV>
	</body>

</html>

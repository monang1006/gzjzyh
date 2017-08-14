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
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
	
		<title>短信猫配置文件</title>
		<script>
		function show(info){
			$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
			$.blockUI({ message: '<font color="#008000"><b>'+info+'</b></font>' });
		}
		$.unblockUI();
		
		function testComm(){
			if (confirm("自动扫描短信猫配置，可能要花费一些时间，确定扫描？")){
				//location = "<%=path%>/sms/smsConf!testComm.action";
				show("正在扫描端口，请稍等...");
				
				$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=path%>/sms/smsConf!testComm.action",
					success:function(msg){
							//alert(msg);
							if(msg.indexOf("success")==0){
								var arg = msg.split(",");
								$("#smscomPort").val(arg[1]);
								$("#smscomBps").val(arg[2]);
								if("CDMA"==arg[3]){
									$("#smscomtype").val("1");
								}else{
									$("#smscomtype").val("0");
								}
								alert("已经找到短信猫，相关配置信息如下：\n通信端口为："+arg[1]+"\n传输比率为："+arg[2]+"\n短信猫类型为："+arg[3]);
								$("#testInfo").html("找到短信猫！参数信息："+arg[1]+"/"+arg[2]+"/"+arg[3]);
							}else{
								alert("没有找到短信猫，请确定短信猫已正确的与电脑连接！");
								$("#smscomPort").val("");
								$("#smscomBps").val("");
								$("#testInfo").html("没有找到短信猫，请确定短信猫已正确的与电脑连接！");
							}
							$.unblockUI();
					}
				});
				
			}
		}
		
		function getConfig(){
			$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=path%>/sms/smsConf!showStatus.action",
					success:function(msg){
							show(msg);
						if(msg<=100|msg=="null"){
							alert(msg);
							setTimeout("getConfig()",1000);
						}else{
							//hidden();
						}
					}
			});
		}
		$(document).ready(function(){
			var reg_mobile= /^(1\d{10})$/;
				$("#btnSaveConfig").click(function(){
					if(!isIP($("#smscomPort").val())){
						alert("通信端口不能为空！");
						$("#smscomPort").focus();
						return ;
					}
					if(!isDigits($("#smscomBps").val())){
						alert("传输比率不能为空且只能为数字！");
						$("#smscomBps").focus();
						return ;
					}
					if(!isDigits($("#smsSystemRate").val())){
						alert("发送时间间隔不能为空且只能为数字！");
						$("#smsSystemRate").focus();
						return ;
					}
					if(!reg_mobile.test($("#smscomSimnum").val())){
							alert("请输入11位数字的SIM卡号码！");
							$("#smscomSimnum").focus();
							return ;
						}
						
					if($("#smscomtype").val()==""){//验证短信猫类型
							alert("请选择短信猫类型！");
							$("#smscomtype").focus();
							return ;
						}
						
					$("#span_smscomPort").hide();
					$("#span_smscomBps").hide();
					$.post("<%=path%>/sms/smsConf!save.action",
						   {'model.smscomPort':$("#smscomPort").val(),'model.smscomSimnum':$("#smscomSimnum").val(),'model.smscomBps':$("#smscomBps").val(),'smsSystemRate':$("#smsSystemRate").val()},
						   function(data){
						   	if(data == "ok"){
						   		alert("短信猫配置信息已更新成功!");
						   	}else{
						   		alert("对不起,更新失败 !");
						   	}
						   }	
					);
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
			<s:form action="smsConf!save.action" theme="simple" name="smsConfForm" id="smsConfForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" >
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00" >
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td >
											&nbsp;
											</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												配置短信猫的参数
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
										<span class="wz">短信猫轮循间隔<font color=red>*</font>：</span>
									</td>
									<td class="td1" title="（填入短信猫轮循间隔时间，单位为秒,保存设置后请将重起服务生效）">
										<input id="smsSystemRate" name="smsSystemRate" value="${smsSystemRate}" type="text" size="24">&nbsp;秒
										<font color=red>(*该配置的修改将在服务重起后生效)</font>
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
								--%><tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">扫描端口：</span>
									</td>
									<td class="td1" valign="top" title="点击按钮，自动查找已连接短信猫的通信端口和传输比率">
										<input type="button" onclick="testComm();" value="扫描端口" class="input_bg" />
										<span id="testInfo"></span>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">通信端口(<font color=red>*</font>)：</span>
									</td>
									<td class="td1" valign="top" title="（配置短信猫的接入端口，选择相应的COM端口）">
										<input id="smscomPort" name="model.smscomPort" value="${model.smscomPort}" type="text" size="24">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">传输比率(<font color=red>*</font>)：</span>
									</td>
									<td class="td1" title="（配置短信猫的通信波特率，选择相应的通信波特率可为 9600或19200）">
										<input id="smscomBps" name="model.smscomBps" value="${model.smscomBps}" type="text" size="24">&nbsp;kps
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">SIM卡号码(<font color=red>*</font>)：</span>
									</td>
									<td class="td1" title="（填入短信猫内SIM卡的号码）">
										<input id="smscomSimnum" name="model.smscomSimnum" value="${model.smscomSimnum}" type="text" size="24">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">设备类型(<font color=red>*</font>)：</span>
									</td>
									<td class="td1" title="（选择短信猫的类型）">
										<s:select id="smscomtype" name="model.smscomtype" list="#{'':'全部','1':'CDMA短信猫','0':'GMS短信猫'}" listKey="key" listValue="value" style="width:100%"/>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">设备名称：</span>
									</td>
									<td class="td1" title="（填入相应的短信猫设备名，如:WAVECOM）">
										<input id="smscomName" name="model.smscomName" value="${model.smscomName}" type="text" size="24">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="center">
						<td class="td1">
							<br>
							<%--<input type="submit" class="input_bg" value="保存设置" />
							--%><input id="btnSaveConfig" type="button" class="input_bg" value="保存设置" />
						</td>
					</tr>
				</table>
					</td>
				</tr>
			</table>
			
			</s:form>
		</DIV>
	</body>

</html>

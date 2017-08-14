<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<title>配置文件</title>
		<script>
			$(document).ready(function(){
				$("#btnSaveConfig").click(function(){
					var imEnabled = $("#imEnabled").val();
					if(imEnabled == "1"){
						if(!isIP($("#ip").val())){
							$("#span_ip").show();
							return ;
						}
						if(!isDigits($("#clientport").val())){
							$("#span_clientport").show();
							return ;
						}
						if(!isDigits($("#port").val())){
							$("#span_port").show();
							return ;
						}
					}
					
					$("#span_ip").hide();
					$("#span_port").hide();
					$("#span_clientport").hide();
					if(isNaN($("#rest3").val())){
						alert("请输入正确的轮询频率。");
						return ;
					}
					$.post("<%=root%>/im/iM!saveServerConfig.action",
						   {"model.id":'${model.id}',"model.rest2":$("#rest2").val(),"model.rest3":$("#rest3").val(),"model.rest1":$("#type").find("option:selected").text(),"model.imconfigIp":$("#ip").val(),"model.imconfigClassName":$("#type").val(),"model.imconfigPort":$("#port").val(),"model.imconfigClientPort":$("#clientport").val(),"model.imconfigState":$("#imEnabled").val()},
						   function(data){
						   	if(data == "0"){
						   		alert("服务器配置信息已更新成功!");
						   		location = "<%=root%>/im/iM!serverConfig.action";
						   	}else{
						   		alert("对不起,更新失败!");
						   	}
						   }	
					);
				});
				//check ip
				function isIP(sIPAddress)   { 
				 if($.trim(sIPAddress) == ""){
				 	return false;
				 }else{
	          		 var   exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;     
					 return exp.test(sIPAddress);  			   
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
  				//初始化
  				function init(){
  					var rtxEnabled = '${model.imconfigState}';
  					if(rtxEnabled == "1"){
  						$("#optEnabled").attr("selected",true);
		  				showOrHideRtxConfig("1");
  					}else{
  						$("#optDisEnabled").attr("selected",true);
  						showOrHideRtxConfig("0");
  					}
  					
  					var imName = '${model.rest1}';
  					$("#type").find("option").each(function(){
  						if(this.text == imName){
  							this.selected = true;
  						}
  					});
  					
  					var loopEnabled = '${model.rest2}';
  					if(loopEnabled == "1"){
  						$("#loopEnabled").attr("selected",true);
  						showOrHideLoopConfig("1");
  					}else{
  						$("#loopDisEnabled").attr("selected",true);
  						showOrHideLoopConfig("0");
  					}
  				}
  				init();
			});
			//是否显示Rtx配置项
  			function showOrHideRtxConfig(isEnabled){
  				if(isEnabled == "1"){
  					for(var i=1;i<=4;i++){
  						$("#tr"+i).show();
  					}
  				}else{
  					for(var i=1;i<=4;i++){
  						$("#tr"+i).hide();
  					}
  				}
  			}
  			//是否显示轮询时间配置项
  			function showOrHideLoopConfig(isEnabled){
  				if(isEnabled == "1"){
  					$("#span_loop").show();
  				}
  				if(isEnabled == "0"){
  					$("#span_loop").hide();
  				}
  			}
		</script>
		
	</head>
	<body class="contentbodymargin">

		<DIV id=contentborder cellpadding="0" >
			<s:form action="smsConf!save.action" theme="simple" name="smsConfForm" id="smsConfForm">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td>
							<strong>配置即时通讯软件服务器的参数</strong>
							</td>
							
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="$('#btnSaveConfig').click()">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	
					                </tr>
					            </table>
							</td>
										
									</table>
								</td>
							</tr>
						
				
				
				
				<tr>
					<td>
					<table width="100%" height="100%" border="0" cellpadding="0"
				cellspacing="0" align="center" class="table1">
					<tr>
						<td>
							<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">软件类型：</span>
									</td>
									
									<td class="td1" valign="top">
										<select id="type" onchange="">
											
											<s:iterator value="baseServiceInfo" status="status">
												<option value="<s:property value="baseServiceInfo[#status.index][0]" />"><s:property value="baseServiceInfo[#status.index][1]" /> </option>
											</s:iterator>
										</select>
									</td>
								</tr>
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">是否启用：</span>
									</td>
									<td class="td1" valign="top">
										<select id="imEnabled" name="model.imconfigState" onchange="JavaScript:showOrHideRtxConfig(this.value);">
											<option value="1" id="optEnabled">启用</option>
											<option value="0" id="optDisEnabled" selected="selected">不启用</option>
										</select>
									</td>
								</tr>
								<tr id="tr4">
									<td  class="biao_bg1" align="right">
										<span class="wz">是否开启定时轮询：</span>
									</td>
									<td class="td1" valign="top">
										<select id="rest2" name="model.rest2" onchange="JavaScript:showOrHideLoopConfig(this.value);">
											<option value="1" id="loopEnabled">启用</option>
											<option value="0" id="loopDisEnabled" selected="selected">不启用</option>
										</select>
										<span id="span_loop" style="display:none ;">
											轮询频率：<s:textfield size="4" id="rest3" name="model.rest3"></s:textfield>&nbsp;分钟/次
										</span>
									</td>
								</tr>
								<tr id="tr1">
									<td  class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font> IP：</span>
									</td>
									<td class="td1" valign="top" title="（即时通讯软件服务器IP）">
										<s:textfield id="ip"  name="model.imconfigIp" size="24"></s:textfield>
										<span id="span_ip" style="display:none;" class="wz"><font color="red">请输入合法的IP地址.</font></span>
									</td>
								</tr>
								<tr id="tr2">
									<td  class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> 客户端登录端口：</span>
									</td>
									<td class="td1" title="（客户端登录端口）">
										<s:textfield id="clientport"  name="model.imconfigClientPort" size="24"></s:textfield>
										<span id="span_clientport" style="display:none;" class="wz"><font color="red">请输入合法的端口.</font></span>
									</td>
								</tr>
								<tr id="tr3">
									<td  class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> 服务调用端口：</span>
									</td>
									<td class="td1" title="（即时通讯软件服务器远程端口）">
										<s:textfield id="port"  name="model.imconfigPort" size="24"></s:textfield>
										<span id="span_port" style="display:none;" class="wz"><font color="red">请输入合法的端口.</font></span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="center">
						<td class="td1">
							<br>
							<input id="btnSaveConfig" type="hidden" class="input_bg" value="保存设置" />
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

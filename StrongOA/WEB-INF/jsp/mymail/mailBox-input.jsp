<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>建立新的邮箱账户</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<style type="text/css">
			body {
				margin-left: 0px;
				margin-top: 0px;
				margin-right: 0px;
				margin-bottom: 0px;
				height: 100%
			}
		</style>
		<script language="javascript">
			$(document).ready(function(){
				$("#senior").click(function(){
					if($("#setserver").css("display")=='none'){
						$("#setserver").show();
					}else{
						$("#setserver").hide();
					}
				});
				$("#mailaddress").blur(function(){
					reg=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
					var inputtext=$("#mailaddress").val();
					if (reg.test(inputtext)){
						$("#addressspan").css({color:'#000000'});
						//if($("#getserver").val().length==0){
							if($("#getsevertype").val()==1){
								$("#getserver").val('pop.'+inputtext.substring((inputtext.indexOf("@")+1)));
							}else if($("#getsevertype").val()==2){
								$("#getserver").val('imap.'+inputtext.substring((inputtext.indexOf("@")+1)));
							}
						//}
						if($("#mailzh").val().length==0){
							$("#mailzh").val(inputtext.substring(0,inputtext.indexOf("@")));
						}
						//if($("#sendserver").val().length==0){
							$("#sendserver").val('smtp.'+inputtext.substring((inputtext.indexOf("@")+1)));
						//}
						
						if($("#nextbtn").val()=='下一步'){
							$("#nextbtn").attr("disabled",false);
						}
					}else{
						$("#addressspan").css({color:'#FF0000'});
						if($("#nextbtn").val()=='下一步'){
							$("#nextbtn").attr("disabled",true);
						}
					}
				});
				$("#xsmc").keyup(function(){
					if($.trim($(this).val())==''){
						$("#xsmcspan").css({color:'#FF0000'});
						if($("#nextbtn").val()=='下一步'){
							$("#nextbtn").attr("disabled",true);
						}
					}else{
						$("#xsmcspan").css({color:'#000000'});
						if($("#nextbtn").val()=='下一步'){
							$("#nextbtn").attr("disabled",false);
						}
					}
				});
				$("#getsevertype").change(function(){
					reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
					var inputtext=$("#mailaddress").val();
					if($("#getsevertype").val()==1){
						$("#senddk").val("110");
						if($.trim($("#getserver").val()).length!=0){
							if (reg.test(inputtext))
								$("#getserver").val('pop.'+inputtext.substring((inputtext.indexOf("@")+1)));
						}
					}else if($("#getsevertype").val()==2){
						$("#senddk").val("143");
						if($.trim($("#getserver").val()).length!=0){
							if (reg.test(inputtext))
								$("#getserver").val('imap.'+inputtext.substring((inputtext.indexOf("@")+1)));
						}
					}
				});
				$("#nextbtn").click(function(){
					if($("#nextbtn").val()=='下一步'){
						reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
						var inputtext=$("#mailaddress").val();
						if(!reg.test(inputtext)){
							alert("电子邮件地址输入不正确，请确认输入！");	
							$("#mailaddress").focus();			
							return;
						}
						if($.trim($("#xsmc").val()).length==0){
							alert("账户名称不能为空！");	
							$("#xsmc").focus();			
							return;
						}
						
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/mymail/mailBox!charge.action",
							data:"name="+$.trim($("#xsmc").val()),
							success:function(msg){
								if(msg=="false"){
									alert("账户名称不为可重名！");
									$("#xsmc").focus();
									return;
								}else{
									$("#tabPage1").hide();
									$("#tabPage2").show();
									$("#nextbtn").val("上一步");
									$("#submitbtn").val("确定");
								}
							}
						});
						

					}else if($("#nextbtn").val()=='上一步'){
						$("#tabPage2").hide();
						$("#tabPage1").show();
						$("#nextbtn").val("下一步");
						$("#submitbtn").val("取消");
					}
				});
				$("#submitbtn").click(function(){
					if($("#submitbtn").val()=='取消'){
						window.close();
					}else if($("#submitbtn").val()=='确定'){
						if(confirm("您确认配置信息是正确的么？")==true){
							$.ajax({
								type:"post",
								dataType:"text",
								url:"<%=root%>/mymail/mailBox!input.action",
								data:"model.mailAddress="+$("#mailaddress").val()+"&model.pop3Pwd="+$("#mypassword").val()+"&model.mailboxUserName="+$("#xsmc").val()
									 +"&model.pop3Server="+$("#getserver").val()+"&model.pop3Account="+$("#mailzh").val()+"&model.smtpServer="+$("#sendserver").val()+"&model.getServerType="+$("#getsevertype").val()
									 +"&model.pop3Port="+$("#senddk").val()+"&model.pop3Ssl="+$("#sendssl").attr("checked")+"&model.smtpPort="+$("#receivedk").val()+"&model.smtpSsl="+$("#receivessl").attr("checked")+"&type=add",
								success:function(msg){
									if(msg=="true"){
										window.returnValue="true";
										window.close();
									}else{
										alert("添加失败，请您重新操作");
									}
								}
							});
						}
					}
				});
			});
			
		</script>
	</head>
	<body class="contentbodymargin">
		<div id="contentborder" style="overflow: hidden">
			<form>
				<table width="100%" height="38px;"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					<tr>
						<td>&nbsp;</td>
						<td width="30%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
							建立新的邮箱账户
						</td>
						<td width="70%">
							&nbsp;
						</td>
					</tr>
				</table>
				<table border="0" width="98%" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr height="80%">
						<td width="100%" class="wz">
							<DIV class=tab-pane id=tabPane1>
								<DIV class=tab-page id=tabPage1>
									<table border="0" width="100%" cellpadding="2" cellspacing="1">
										<tr>
											<td width="35%" class="biao_bg1" align="right">
												<span id=addressspan>电子邮件地址(<font color=red>*</font>)：</span>
											</td>
											<td width="65%" class="td1">
												<input type="text" name="mailaddress" id="mailaddress" style="width:100%" />
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span id="passspan" >密码：</span>
											</td>
											<td width="75%" class="td1">
												<input type="password" name="mypassword" id="mypassword" style="width:100%" />
											</td>
										</tr>
										<tr>
											<td width="100%" class="td1" colspan="2">
												<span  >“账户名称”是在本系统中显示的名称，以区分不同的邮件账户。“邮件采用名称”可填您的姓名或昵称，将包含在发出的邮件中。</span>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span  id="xsmcspan" >账户名称(<font color=red>*</font>)：</span>
											</td>
											<td width="75%" class="td1">
												<input type="text" name="xsmc" id="xsmc" style="width:100% "   size="25" maxlength="25"/>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span >邮件采用名称：</span>
											</td>
											<td width="75%" class="td1">
												<input type="text" name="yjcymc" id="yjcymc" style="width:100% "/>
											</td>
										</tr>
									</table>

								</DIV>
								<DIV class=tab-page id=tabPage2 style="display:none ">
									<table border="0" width="100%" cellpadding="2" border="1" cellspacing="1" align="center">
										<tr>
											<td width="100%" class="td1" colspan="2"><span >POP3（PostOffice Protocol3）服务器是用来接受邮件的服务器。您的邮件保存在其上。</span><br><br></td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right"><span >接受服务器类型：</span></td>
											<td width="75%" class="td1">
												<select class="selectlist" id="getsevertype">
													<option value="1" selected>POP3</option>
													<option value="2">IMAP</option>
												</select>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right"><span >接收邮件服务器：</span></td>
											<td width="75%" class="td1">
												<input type="text" name="getserver" id="getserver" style="width:100% "/>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right"><span >邮件账户：</span></td>
											<td width="75%" class="td1">
												<input type="text" name="mailzh" id="mailzh" style="width:100%"/>
											</td>
										</tr>
										<tr>
											<td width="100%" class="td1" colspan="2"><span >SMTP服务器用来中转发送您发出的邮件。SMTP服务器与POP3服务器可以不同</span></td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">发送邮件服务器：<br><br></td>
											<td width="75%" class="td1"><input type="text" name="sendserver" id="sendserver" style="width:80%"/> <input type="button" name="senior" id="senior" value="高级" /></td>
										</tr>
										
										<tr id="setserver" style="display:none ">
											<td colspan="2" width="100%" class="td1">
												<table width="95%">
													<tr>
														<td width="50%" class="td1">
															<fieldset>
																 <legend><span >收取服务器</span></legend>
																 <table width="100%">
																	<tr>
																		<td class="td1" width="30%"><span >端口：</span></td>
																		<td class="td1" width="70%"><input type="text" name="senddk" id="senddk" value="110"/></td>
																	</tr>
																	<tr>
																		<td class="td1" colspan="2">
																			<input type="checkbox" name="sendssl" id="sendssl" />用SSL连接
																		</td>
																	</tr>
																 </table>
															</fieldset>
														</td>
														<td width="50%" class="td1">
															<fieldset>
																 <legend><span >发送服务器</span></legend>
																 <table width="100%">
																	<tr>
																		<td class="td1" width="30%"><span >端口：</span></td>
																		<td class="td1" width="70%"><input type="text" name="receivedk" id="receivedk" value="25"/></td>
																	</tr>
																	<tr>
																		<td class="td1" colspan="2">
																			<input type="checkbox" name="receivessl" id="receivessl" />用SSL连接
																		</td>
																	</tr>
																 </table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
										</tr>
								    </table>
								</DIV>
								</DIV>
							</DIV>
						</td>
					</tr>
					<tr align="center">
						<td style="position: absolute; top: 300px; left: 220px;">
							<input type="button"  class="input_bg" name="nextbtn" id="nextbtn" value="下一步"/>
							<input type="button"  class="input_bg" name="submitbtn" id="submitbtn" value="取消"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>

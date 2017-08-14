<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>邮箱属性</title>
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
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
				$("#mailaddress").keyup(function(){
					reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
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
					}else{
						$("#addressspan").css({color:'#FF0000'});
					}
				});
				$("#xsmc").keyup(function(){
					if($.trim($(this).val())==''){
						$("#xsmcspan").css({color:'#FF0000'});
					}else{
						$("#xsmcspan").css({color:'#000000'});
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
				$("#subBtn").click(function(){
					reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
					var inputtext=$("#mailaddress").val();
					if(reg.test(inputtext)){
						if($.trim($("#xsmc").val())==''){
							alert("账户名称不为空！");	
						}else{
							$.ajax({
								type:"post",
								dataType:"text",
								url:"<%=root%>/mymail/mailBox!chargeNo.action",
								data:"id="+$("#myid").val()+"&name="+$.trim($("#xsmc").val()),
								success:function(message){
									if(message=="true"){
										$.ajax({
											type:"post",
											dataType:"text",
											url:"<%=root%>/mymail/mailBox!edit.action",
											data:"model.mailboxId="+$("#myid").val()+"&model.mailAddress="+$("#mailaddress").val()+"&model.pop3Pwd="+$("#mypassword").val()+"&model.mailboxUserName="+$("#xsmc").val()
												 +"&model.pop3Server="+$("#getserver").val()+"&model.pop3Account="+$("#mailzh").val()+"&model.smtpServer="+$("#sendserver").val()+"&model.getServerType="+$("#getsevertype").val()
												 +"&model.pop3Port="+$.trim($("#senddk").val())+"&model.pop3Ssl="+$("#sendssl").attr("checked")+"&model.smtpPort="+$.trim($("#receivedk").val())+"&model.smtpSsl="+$("#receivessl").attr("checked")+"&type=save",
											success:function(msg){
												if(msg=="true"){
													window.returnValue="true";
													//alert("邮箱修改成功！");
													window.close();
												}else{
													alert("修改失败，请您重新操作");
												}
											}
										});
									}else{
										alert("账户名称不为可重名!");
									}
								}
							});
						}
					}else{
						alert("邮箱格式不正确！");
					}
					
				});
				$("#canBtn").click(function(){
					window.close();
				})
			});
			
		</script>
	</head>
	<body class="contentbodymargin">
		<div id="contentborder"  style="overflow: hidden">
			<form>
				<input type="hidden" id="myid" value="${model.mailboxId }">
				<table width="100%" height="38px;"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					<tr>
						<td>&nbsp;</td>
						<td width="20%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
							邮箱账户设置
						</td>
						<td width="*">
							&nbsp;
							<td width="50"><a class="Operation" href="#" onclick="$('#subBtn').click()"><img src="<%=root%>/images/ico/queding.gif" width="15" height="15" class="img_s">确定</a></td>
		                 	<td width="5"></td>
		                  	<td width="50"><a class="Operation" href="#" onclick="$('#canBtn').click()"><img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">取消</a></td>
		                  	<td width="5"></td>
						</td>
					</tr>
				</table>
				<table border="0" width="98%" bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
					<tr height="80%">
						<td width="100%">
							<DIV class=tab-pane id=tabPane1>
								<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								
								<DIV class=tab-page id=tabPage1>
									<H2 class=tab>
										基本信息
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1">
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span id="addressspan" class="wz" >电子邮件地址(<font color=red>*</font>)：</span>
											</td>
											<td width="75%" class="td1">
												<input type="text" name="mailaddress" id="mailaddress" style="width:100% " id="mailaddress" value="${model.mailAddress }" />
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span id="passspan" class="wz">密码：</span>
											</td>
											<td width="75%" class="td1">
												<input type="password" name="mypassword" style="width:100%" id="mypassword" value="${model.pop3Pwd }"/>
											</td>
										</tr>
										<tr>
											<td width="100%" class="td1" colspan="2">
												<span class="wz">“账户名称”是在本系统中显示的名称，以区分不同的邮件账户。“邮件采用名称”可填您的姓名或昵称，将包含在发出的邮件中。</span>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span class="wz" id="xsmcspan" >账户名称(<font color=red>*</font>)：</span>
											</td>
											<td width="75%" class="td1">
												<input type="text" name="xsmc" id="xsmc" style="width:100% " value="${model.mailboxUserName }"/>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">
												<span class="wz">邮件采用名称：</span>
											</td>
											<td width="75%" class="td1">
												<input type="text" name="yjcymc" id="yjcymc" style="width:100% "/>
											</td>
										</tr>
									</table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>
								</DIV>
								
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										邮箱设置
									</H2>
									<table border="0" width="100%" cellpadding="2" border="1" cellspacing="1" align="center">
										<tr>
											<td width="100%" class="td1" colspan="2"><span class="wz">POP3（PostOffice Protocol3）服务器是用来接受邮件的服务器。您的邮件保存在其上。</span></td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right"><span class="wz">接受服务器类型：</span></td>
											<td width="75%" class="td1">
												<select class="selectlist" id="getsevertype">
													<s:if test="model.getServerType==\"1\"">
														<option value="1" selected>POP3</option>
													</s:if>
													<s:else>
														<option value="1">POP3</option>
													</s:else>
													<s:if test="model.getServerType==\"2\"">
														<option value="2" selected>IMAP</option>
													</s:if>
													<s:else>
														<option value="2">IMAP</option>
													</s:else>
												</select>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right"><span class="wz">接收邮件服务器：</span></td>
											<td width="75%" class="td1">
												<input type="text" name="getserver" id="getserver" style="width:100%" value="${model.pop3Server }"/>
											</td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right"><span class="wz">邮件账户：</span></td>
											<td width="75%" class="td1">
												<input type="text" name="mailzh" id="mailzh" style="width:100%" value="${model.pop3Account }"  maxlength="25"/>
											</td>
										</tr>
										<tr>
											<td width="100%" class="td1" colspan="2"><span class="wz">SMTP服务器用来中转发送您发出的邮件。SMTP服务器与POP3服务器可以不同</span></td>
										</tr>
										<tr>
											<td width="25%" class="biao_bg1" align="right">发送邮件服务器：</td>
											<td width="75%" class="td1"><input type="text" name="sendserver" id="sendserver" style="width:80%" value="${model.smtpServer }"/> <input type="button" name="senior" class="input_bg" id="senior" value="高级" /></td>
										</tr>
										
										<tr id="setserver" style="display:none ">
											<td colspan="2" width="100%" class="td1">
												<table width="95%">
													<tr>
														<td width="50%" class="td1">
															<fieldset>
																 <legend><span class="wz">收取服务器</span></legend>
																 <table width="100%">
																	<tr>
																		<td class="td1" width="30%"><span class="wz">端口：</span></td>
																		<td class="td1" width="70%"><input type="text" name="senddk" id="senddk" value="${model.pop3Port }"/></td>
																	</tr>
																	<tr>
																		<td class="td1" colspan="2">
																			<s:if test="model.pop3Ssl==\"1\"">
																				<input type="checkbox" name="sendssl" id="sendssl" checked="checked"/>用SSL连接
																			</s:if>
																			<s:else>
																				<input type="checkbox" name="sendssl" id="sendssl" />用SSL连接
																			</s:else>
																		</td>
																	</tr>
																 </table>
															</fieldset>
														</td>
														<td width="50%" class="td1">
															<fieldset>
																 <legend><span class="wz">发送服务器</span></legend>
																 <table width="100%">
																	<tr>
																		<td class="td1" width="30%"><span class="wz">端口：</span></td>
																		<td class="td1" width="70%"><input type="text" name="receivedk" id="receivedk" value="${model.smtpPort }"/></td>
																	</tr>
																	<tr>
																		<td class="td1" colspan="2">
																			<s:if test="model.smtpSsl==\"1\"">
																				<input type="checkbox" name="receivessl" id="receivessl" checked="checked"/>用SSL连接
																			</s:if>
																			<s:else>
																				<input type="checkbox" name="receivessl" id="receivessl"/>用SSL连接
																			</s:else>
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
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>
								</DIV>

							</DIV>
						</td>
					</tr>
					<tr align="center">
						<td>
						<div style="position: absolute; top: 340px; left: 220px;">
							<input type="button" id="subBtn"  class="input_bg" value="确定" />
							<input type="button" id="canBtn"  class="input_bg" value="取消" />
						</div>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>

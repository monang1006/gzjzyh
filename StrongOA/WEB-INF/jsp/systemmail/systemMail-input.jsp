<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<title>系统默认邮件配置</title>
		<script type="text/javascript">
//			function sendMail(){
//				var obj=document.getElementById("myForm");
//				obj.action="<%=path%>/systemmail/systemMail!sendMailTest.action";
//				obj.submit();
//			}
			$(document).ready(function(){
				$("#testmail").click(function(){
					if($("#defaultMail").val()==""||!CheckEmail($("#defaultMail").val())){
						alert("默认Email不能为空或者Email格式不正常,请您正确输入。");
						$("#defaultMail").focus();
					}else if($("#defaultMailUser").val()==""){
						alert("默认邮箱用户名不能为空。");
						$("#defaultMailUser").focus();
					}else if($("#defaultMailPsw").val()==""){
						alert("默认邮箱密码不能为空。");
						$("#defaultMailPsw").focus();
					}else if($("#defaultMailSys").val()==""){
						alert("发送服务器配置不能为空。");
						$("#defaultMailSys").focus();
					}else if($("#defaultMailPort").val()==""||!checkNum($("#defaultMailPort").val())){
						alert("默认端口为空或存在非数字。");
						$("#defaultMailPort").focus();
					}else{
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/systemmail/systemMail!configTest.action",
							data:"model.defaultMail="+$("#defaultMail").val()+"&model.defaultMailSsl="+$("#defaultMailSsl").val()
								  +"&model.defaultMailUser="+$("#defaultMailUser").val()+"&model.defaultMailPsw="+$("#defaultMailPsw").val()
								  +"&model.defaultMailSys="+$("#defaultMailSys").val()+"&model.defaultMailPort="+$("#defaultMailPort").val(),
							success:function(msg){
								if(msg=="true"){
									alert("邮件配置验证成功。");
									$("#save").attr("disabled",false);
									$("#save").attr("title","邮箱信息是否设置正确，点击按钮保存邮箱信息。");
								}else{
									alert("邮件配置验证失败,请您验证您的配置信息是否正确、网络连接是否正常。");
								}
							}
						});
					}
				});
				
	//初始化 页面
				//是否显示默认邮箱设置界面
				if("1"=="${model.defaultMailUseable}"){
					$("#mailconf").show();
				}else{
					$("#mailconf").hide();
				}
				//保存按钮不可用
				if("1"=="${model.defaultMailUseable}"){
					$("#save").attr("disabled",true);
					$("#save").attr("title","请先点击测试按钮，测试邮箱信息是否设置正确。");
				}
			});
			
			
			
			// 判断输入是否是有效的电子邮件   
			function CheckEmail(str)   
			{   
			    var result=str.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/);   
			    if(result==null) return false;   
			    return true;   
			}   
 			
 			//验证数字
 			function checkNum(str){
 				var reg=/^[0-9]*$/;
 				var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
 			}
 			
 			//保存系统邮箱配置
 			function saveConf(){
 				if($("#defaultMailUseable").val()=="1"){
 					$("#formUseable").val("1");
 					submitForm();
 				}else{
 					if(confirm("关闭邮箱相关功能，确定？")==true){
 						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/systemmail/systemMail!closeSysMail.action",
							data:"",
							success:function(msg){
								if(msg=="success"){
									//alert("邮件配置验证成功！");
								}else{
									alert(msg);
								}
							}
						});
 					}
 				}
 			}
 			
 			
 			//保存默认邮箱设置
			function submitForm(){
				var defaultMail=document.getElementById("defaultMail").value;	
				var defaultMailPort=document.getElementById("defaultMailPort").value;
				var defaultMailSys=document.getElementById("defaultMailSys").value;
				var defaultMailUser=document.getElementById("defaultMailUser").value;
				var defaultMail=document.getElementById("defaultMail").value;
				var defaultMailPsw=document.getElementById("defaultMailPsw").value;
				var defaultMailPort=document.getElementById("defaultMailPort").value;
				if(defaultMail!=""&&CheckEmail(defaultMail)==false){
					alert("默认系统邮件地址有误，格式如：example@gmail.com");
					document.getElementById("defaultMail").focus();
					return false;
				}else if(defaultMailPort!=""&&checkNum(defaultMailPort)==false){
					alert("默认系统邮件端口有误，只能输入数字。");
					document.getElementById("defaultMailPort").focus();
					return false;
				}
				
				if(defaultMail==""){						//默认Email
					alert("默认Email不能为空,请您正确输入。");
					document.getElementById("defaultMail").focus();
					return false;
				}
				if(defaultMailUser==""){					//默认邮箱用户名不能为空
					alert("默认邮箱用户名不能为空");
					document.getElementById("defaultMailUser").focus();
					return false;
				}
				if(defaultMailSys==""){						//发送服务器配置不能为空
					alert("发送服务器配置不能为空。");
					document.getElementById("defaultMailSys").focus();
					return false;
				}
				if(defaultMailPsw==""){
					alert("默认邮箱密码不为空。");
					document.getElementById("defaultMailPsw").focus();
					return false;
				}
				if(defaultMailPort==""){
					alert("发送服务器端口不为空。");
					document.getElementById("defaultMailPort").focus();
					return false;
				}
				document.getElementById("myForm").submit();	
			}
			
			//改变开关状态
			function changeState(state){
				if("1"==state){
					$("#mailconf").show();
					$("#save").attr("disabled",true);
					$("#save").attr("title","请先点击测试按钮，测试邮箱信息是否设置正确");
				}else{
					$("#mailconf").hide();
					$("#save").attr("disabled",false);
					$("#save").attr("title","点击按钮，关闭邮箱相关功能。");
				}
			}

		</script>
	</head>
	<body class=contentbodymargin>
		<DIV id=contentborder align=center>
			
				<div align=left style="width: 100%">
					<%--<table width="100%" border="0" cellspacing="0" cellpadding="00"  height="45"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<tr>
							<td>
							&nbsp;
							</td>
							<td width="20%">
								<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
								系统默认邮件配置
							</td>
							<td>
								&nbsp;
							</td>
							<td width="75%">
							</td>
						</tr>
					</table>
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
							<strong>系统默认邮件配置</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="saveConf()">&nbsp;保&nbsp;存&nbsp;</td>
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
				
				</div>
				
				
				<fieldset style="width: 100%">
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
				
					
					<div>
						<span class="wz">&nbsp邮件模块是否启用：</span>
					</div>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz">选择状态：</span>
							</td>
							<td class="td1">
							<s:select cssStyle="width: 175px" list="#{'关闭':'0','启用':'1'}" onchange="changeState(this.value);"
									listKey="value" listValue="key" id="defaultMailUseable" 
									name="model.defaultMailUseable"></s:select>
							</td>
						</tr>
					</table>
			<s:form id="myForm" action="/systemmail/systemMail!save.action">
			<input type="hidden" name="model.defaultMailUseable" id="formUseable" value="">
					<div id="mailconf">
					<div align="left">
						<span class="wz">配置系统邮箱：</span>
					</div>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">默认Email：</span>
								<input id="defaultMailId" name="model.defaultMailId"
									value="${model.defaultMailId}" type="hidden">
							</td>
							<td class="td1">
								&nbsp;<input id="defaultMail" name="model.defaultMail"
									value="${model.defaultMail}" type="text" size="30">&nbsp;<font color="gray">邮箱规则：</font><font color="#999999">××××@gmail.com</font>
							</td>
							
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">是否经过SSL验证：</span>
							</td>
							<td  class="td1">
								&nbsp;<s:select cssStyle="width: 175px" list="#{'否':'0','是':'1'}"
									listKey="value" listValue="key" id="defaultMailSsl"
									name="model.defaultMailSsl"></s:select>
							</td>
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">默认邮箱用户名：</span>
							</td>
							<td class="td1">
								&nbsp;<input id="defaultMailUser" name="model.defaultMailUser"
									value="${model.defaultMailUser}" type="text" size="30"
									>
							</td>
							
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">默认邮箱密码：</span>
							</td>
							<td  class="td1">
								&nbsp;<input id="defaultMailPsw" name="model.defaultMailPsw"
									value="${model.defaultMailPsw}" type="password" size="30"
									>
							</td>
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">发送服务器配置：</span>
							</td>
							<td  class="td1">
								&nbsp;<input id="defaultMailSys" name="model.defaultMailSys"
									value="${model.defaultMailSys }" type="text" size="30"
									>
							</td>
							
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">发送服务器端口：</span>
							</td>
							<td  class="td1">
								&nbsp;<input id="defaultMailPort" name="model.defaultMailPort"
									value="${model.defaultMailPort}" type="text" size="30"
									>
							</td>
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">测试系统邮箱：</span>
							</td>
							<td  style="display: none"   class="td1" title="用系统邮箱发送邮件进行测试">
								&nbsp;<input id="testmail" name="testmail" type="button" class="input_bg" value="测试" >
								
								<font color="gray">请在保存前先行测试邮箱，避免因为配置差错而出现的问题。</font>
							</td>
							
							<td  class="td1" title="用系统邮箱发送邮件进行测试">
								&nbsp;<a id="testmail"  href="#" class="button" onclick="$('#testmail').click()">测试</a>&nbsp;
								<font color="gray">请在保存前先行测试邮箱，避免因为配置差错而出现的问题。</font>
							</td>
						</tr>
						
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">重置系统邮箱：</span>
							</td>
							<td style="display: none" class="td1" title="还原系统邮箱配置到本次打开页面时的配置。">
								&nbsp;<input name="button" id="res" type="reset" class="input_bg" value="重置">
								<font color="gray">取消修改,返回到本次打开页面时的系统邮箱配置。</font>
							</td>
							<td class="td1" title="还原系统邮箱配置到本次打开页面时是配置">
								&nbsp;<a id="res"  href="#" class="button" onclick="$('#res').click()">重置</a>&nbsp;
								<font color="gray">取消修改,返回到本次打开页面时的系统邮箱配置。</font>
							</td>
							
						</tr>
						
					</table>
					</div>
					</s:form>
				
			</table>
			</fieldset>
			<%--<table width="85%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center">
									<input id="save" name="save" type="button" class="input_bg"
										onclick="saveConf()" value="保存">
									
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		--%></DIV>
		
	</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<title>修改密码</title>
<%@include file="/common/include/meta.jsp"%>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
	rel=stylesheet>
<script language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script type="text/javascript" src="<%=path%>/oa/js/myinfo/md5.js"></script>
<script type="text/javascript" src="<%=path%>/common/script/Password.js"></script>
<style type="text/css">
input{border:1px solid #b3bcc3;background-color:#ffffff;}
</style>
<script type="text/javascript">
			$(document).ready(function(){
					$.fn.password( {
						passwordInput : 'newPassword',
						checkInput : 'rePassword',
						strengthInfoText : 'infoText',
						strengthInfoBar : 'infoBar'
					});
				$("#save").click(function(){
					if($.trim($("#oldPassword").val())==""){
						alert("原始密码不能为空。");
						$("#oldPassword").focus();
					}else if($.trim($("#newPassword").val())==""){
						alert("新密码不能为空。");
						$("#newPassword").focus();
					}else if($.trim($("#newPassword").val())!=""){
						if($.trim($("#newPassword").val()).length<6 ||$.trim($("#newPassword").val()).length>20 || !isNaN($.trim($("#newPassword").val()))){
							alert("密码不能为纯数字,且长度不能低于6位或高于20位。");
							$("#newPassword").val("");
							$("#newPassword").focus();	
						}else if($("#newPassword").val()!=$("#rePassword").val()){
							alert("确认密码错误，请您重新输入。");
							$("#rePassword").focus();
						}else{
							$.ajax({
								type:"post",
								dataType:"text",
								url:"<%=root%>/myinfo/myInfo!chargePass.action",
								data:"md5pass="+hex_md5($("#oldPassword").val()),
								success:function(msg){
									if(msg=="true"){
										$.ajax({
											type:"post",
											dataType:"text",
											url:"<%=root%>/myinfo/myInfo!changePassword.action",
											data:"newPassword="+hex_md5($("#newPassword").val())+"&oldPassword="+hex_md5($("#oldPassword").val())+"&rePassword="+$("#rePassword").val(),
											success:function(message){
												if(message=="true"){
													alert("密码修改成功。");
													window.close();
												}else{
													alert("密码更新失败，请您重新修改。");
												}
											}
										});
									}else{
										alert("原始密码错误请您重新填写。");
									}
								}
							});
						}
					}
				});
			});
			
			//表单重置
			function backToList(){
			   document.forms[0].reset();
			}
			
		</script>
<style><%--
.btncc {
	height: 33px;
	width: 96px;
	text-align: center;
	color: #333;
	line-height: 20px;
	margin: 0px;
	vertical-align: middle;
	border: 0px;
	display: inline-block;
	filter: progid:DXImageTransform.Microsoft.gradient(enabled=false );
	cursor: pointer;
	border-radius: 4px;
	box-shadow: inset 0px 1px 0px rgba(255, 255, 255, 0.2), 0px 1px 2px
		rgba(0, 0, 0, 0.05);
	background-repeat: repeat-x;
	background: url("<%=root%>/frame/theme_red_12/images/btn_bg1.png" );
}

.btnccd {
	height: 33px;
	width: 136px;
	text-align: center;
	color: #333;
	line-height: 20px;
	margin: 0px;
	vertical-align: middle;
	border: 0px;
	display: inline-block;
	filter: progid:DXImageTransform.Microsoft.gradient(enabled=false );
	cursor: pointer;
	border-radius: 4px;
	box-shadow: inset 0px 1px 0px rgba(255, 255, 255, 0.2), 0px 1px 2px
		rgba(0, 0, 0, 0.05);
	background-repeat: repeat-x;
	background: url("<%=root%>/frame/theme_red_12/images/btn_bg2.png" );
}

.btncc a {
	border: 1px;
	font-size: 14px;
	color: #333;
	text-decoration: none;
	padding-left: 5px;
}

.table1 td {
	line-height: 25px;
	padding: 2px 0;
}
--%>

.biao_bg1{
width:110px;
}

</style>
</head>
<body class=contentbodymargin>
	<div id="contentborder" align="center" >
		<form>
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
			<tr align="center" >
				<td colspan="3" class="table_headtd">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>修改密码</strong>
							</td>
					<td align="right">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr>
								<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif" /></td>
								<td class="Operation_input" name="save" class="btncc" id="save">&nbsp;确&nbsp;定&nbsp;</td>
								<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif" /></td>
								<td width="5"></td>
								
								<td width="5"></td>
								<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif" /></td>
								<td class="Operation_input1" onclick="backToList();">&nbsp;重&nbsp;置&nbsp;</td>
								<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif" /></td>
								<td width="6"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</td>
			</tr>
			</table>
			</td>
			</tr>
			
			<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
			<tr>
				<td width="130" height="36" align="right" class="biao_bg1"><span
					class="wz"><font color=red>*</font>&nbsp;原始密码：</span>
				</td>
				<td class="td1"><input id="oldPassword" name="oldPassword"
					type="password" size="25" maxlength="20"
					height:26px; line-height:26px; margin-left:10px;">
				</td>
			</tr>
			<tr>
				<td width="130" height="36" align="right" class="biao_bg1"><span
					class="wz"><font color=red>*</font>&nbsp;新&nbsp;密&nbsp;码：</span>
				</td>
				<td class="TD1"><input id="newPassword" name="newPassword"
					type="password" size="25"
					 height:26px;  line-height:26px; margin-left:10px;"
					maxlength="20" required
					requiredMsg="getText(errors_required, ['新密码'])" byteMaxLength="25"
					byteMaxLengthMsg="getText(errors_byteMaxLength, ['新密码'])">

				</td>
			</tr>
			<tr>
				<td width="130" height="36" align="right" class="biao_bg1"><span
					class="wz"><font color=red>*</font>&nbsp;确认密码：</span>
				</td>
				<td class="td1"><input id="rePassword" name="rePassword"
					type="password"
					 height:26px; margin-left:10px;  line-height:26px;"
					size="25" maxlength="20">
				</td>
			</tr>
			<tr>
				<td width="130" height="36" align="right" class="biao_bg1"><span
					class="wz">&nbsp;密码强度：</span>
				</td>
				<td class="td1">
					<div
						style="clear: both; width: 70%;float:left; margin:8px 8px 8px 0; height:7px; font-size:7px; background: gray">
						<div id="infoBar"></div>
					</div>
					<div id="infoText" style=" width: 20%; float:right"></div>
				</td>
			</tr>
			</table>
			<table border="0" cellspacing="0" type="hidden" cellpadding="00">
				<tr>
					<td height="60" align="center" valign="middle">
						<table width="30%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center"><input id="save" name="save"
									type="hidden" class="btncc" value="确定">
								</td>
								<td align="center"><input name="button" type="hidden"
									class="btncc" value="重置">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
</table>
		
		
		</form>
	</div>
</body>
</html>

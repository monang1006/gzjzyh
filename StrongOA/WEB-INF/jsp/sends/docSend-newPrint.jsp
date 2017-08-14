<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<title>打印密码</title>
		<script type="text/javascript">
 			//验证密码
 			function checkPassword(){
 				if($("#password").val()!=""&$("#password1").val()!=""){
 					if($("#password").val()!=$("#password1").val()!=""){
 						$("#temp").val("两次密码不一致！");
 						$("#temp").disabled="";
 					}else{
 						$("#temp").disabled="true";
 						$("#temp").val("");
 					}
 				}
 				if($("#password").val()!=""&$("#password1").val()!=""){
 					if($("#password").val()!=$("#password1").val()!=""){
 						$("#temp").val("两次密码不一致！");
 						$("#temp").disabled="";
 					}else{
 						$("#temp").disabled="true";
 						$("#temp").val("");
 					}
 				}
			    return;   
 			}
 			//提交系统配置
			function submitForm(){ 
 				if($("#password").val().toString().length!=6|$("#password1").val().toString().length!=6){
						alert("密码必须是六位数！")
						return;
					}
 				if($("#password").val()!=""&$("#password1").val()!=""){
 					if($("#password").val()!=$("#password1").val()!=""){
 						$("#temp").val("两次密码不一致！");
 						$("#temp").disabled="";
 						alert("两次密码不一致！");
 						return;
 					}else{
 						$("#temp").disabled="true";
 						$("#temp").val("");
 					}
 				}
				if($("#password").val()==null||$("#password").val()==""){ 
					alert("请输入密码！");
					return;
					}
 				if($("#password1").val()==null||$("#password1").val()==""){ 
 					alert("请输入密码！");
 					return;
 					}
			 $.ajax({
				       type:"post",
				       url:"<%=root%>/sends/docSend!printPasswordSave.action",
				       data:{
							 temp:"temp",
							 password:$("#password").val()
							 },
				       success:function(info){
				       		if(info=="true"){
				       			alert("保存成功！");
				       			window.close();
				       		}else{
				       			alert("异常!");
				       		}
				       }
			      });
				//document.getElementById("myForm").submit();	
			}
		</script>
	</head>
	<body class=contentbodymargin>
		<DIV id=contentborder align=center>
			<div align=left style="width: 100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="00"
					height="45">
					<tr>
						<td width="5%" align="center">
							<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
						</td>
						<td width="50%">
							公文打印密码配置
						</td>
						<td>
							&nbsp;
						</td>
						<td width="50%">
						</td>
					</tr>
				</table>
			</div>
			<fieldset style="width: 85%">
				<legend>
					<span class="wz">公文打印密码配置</span>
				</legend>
				<s:form id="myForm" action="/sends/docSend!printPasswordSave.action?temp=temp">
					<div id="mailconf">
						<table width="100%" border="0" cellpadding="0" cellspacing="1"
							class="table1">
							<tr>
								<td width="30%" height="21" class="biao_bg1" align="right">
									<span class="wz">密码</span>
								</td>
								<td class="td1">
									<input id="password" name="password" type="password" onkeyup="checkPassword()"/>
								</td>
							</tr>
							<tr>
								<td width="30%" height="21" class="biao_bg1" align="right">
									<span class="wz">确定密码</span>
								</td>
								<td class="td1">
									<input id="password1" name="password1" type="password" onkeyup="checkPassword()"/>
								</td>
							</tr>
						</table>
					</div>
				</s:form>
			</fieldset>
							<tr>
								<td width="30%" height="18">
								</td>
								<td class="td1">
									<input id="temp" name="temp" style="border-left:0px;border-top:0px;border-right:0px;border-bottom:1px;"
									 type="text" disabled="true"/>
								</td>
							</tr>
			<table width="85%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center">
									<input id="save" name="save" type="button" class="input_bg"
										onclick="submitForm()" value="保存">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

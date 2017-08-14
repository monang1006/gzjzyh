<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%
	  String isSupman=request.getParameter("isSupman");
	 %>
<title>设置用户密码</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
<script type="text/javascript"
	src="<%=jsroot%>/validate/jquery.validate.js"></script>
<script type="text/javascript"
	src="<%=jsroot%>/validate/formvalidate.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=jsroot%>/newdate/WdatePicker.js"></script>
<script type="text/javascript" language="javascript"
	src="<%=root%>/uums/js/md5.js"></script>
<script type="text/javascript" src="<%=path%>/common/script/Password.js"></script>
<script type="text/javascript" src="<%=path%>/oa/js/myinfo/md5.js"></script>
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
		});
		
		function restPassword(){
		   if(document.getElementById("isSupman").value == "1" && parent.document.getElementById("isSupman").value != ""){
			   document.getElementById("oldPassword").value=window.dialogArguments.document.getElementById("oldPassword").value
			       		document.getElementById("oldPassword").readOnly=true;
			        }
			if(parent.document.getElementById("oldPassword").value == ""){
				row1.style.display="none";
			}			
	    	}
	    	
	    	
				function onsubmitform(){
					var parent = window.dialogArguments;
					if(document.getElementById("oldPassword").value == "" && parent.document.getElementById("oldPassword").value != ""){
			       		alert('旧密码不能为空！');
			       		document.getElementById("oldPassword").focus();
			  			return;
			        }					
					if(document.getElementById("newPassword").value == ""){
			       		alert('新密码不能为空！');
			       		document.getElementById("newPassword").focus();
			  			return;
			        }
			        if(document.getElementById("rePassword").value == ""){
			       		alert('请再次输入新密码！');
			       		document.getElementById("rePassword").focus();
			  			return;
			        }
			        var oldPassword = document.getElementById("oldPassword").value;
			        var md5Enable = "<%=request.getParameter("md5Enable")%>";
			        if(md5Enable == "1"){
			        	oldPassword = hex_md5(oldPassword);
			        }
					if(parent.document.getElementById("oldPassword").value != "" && oldPassword != window.dialogArguments.document.getElementById("oldPassword").value && document.getElementById("isSupman").value != "1"){
			       		alert('旧密码不正确！');
			       		document.getElementById("oldPassword").focus();
			  			return;
			        }		        
			        if(document.getElementById("password").value != document.getElementById("rePassword").value ){
			       		alert('两次输入的新密码不同,请重新输入！');
			       		document.getElementById("password").focus();
			  			return;
			        }			        
			        parent.setpassword(document.getElementById("password").value);
			        window.close();
				}
			</script>
			
			<style type="text/css">
			input{background-color:#ffffff;}
			</style>
			
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;"
	onLoad="restPassword();">
	<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>设置用户密码</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<%--<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" />
													</td>
													<td class="Operation_input" onclick="onsubmitform()">&nbsp;确&nbsp;定&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" />
													</td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" />
													</td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" />
													</td>
													<td width="6"></td>
												</tr>
											--%>
											
											  <tr>
										<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onsubmitform()">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
									</tr>
											
											</table></td>
									</tr>
								</table></td>
						</tr>
					</table> <s:form id="usermanagesave"
						action="/usermanage/usermanage!save.action" theme="simple">
						<input type="hidden" id="userId" name="userId"
							value="${model.userId}">
						<input type="hidden" id="isSupman" name="isSupman"
							value="<%=isSupman %>">
						<input type="hidden" id="extOrgId" name="extOrgId"
							value="${extOrgId}">
						<table border="0" cellpadding="0" cellspacing="0" class="table1"
							width="100%">
							<tr id="row1">
								<td width="40%" height="21" class="biao_bg1" align="right">
									<span class="wz">请输入旧密码：</span></td>
								<td class="td1" colspan="2" align="left"><input
									id="oldPassword" name="oldPassword" type="password" size="22"
									maxLength="50"></td>
							</tr>
							<tr>
								<td width="40%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;请输入新密码：</span>
								</td>
								<%--<td class="td1" colspan="2" align="left">
										<input id="password" name="password" type="password" size="22"
											maxLength="50">
									</td>
								--%>

								<td class="td1"><input id="newPassword"
									name="password" type="password" size="22" maxlength="20">

								</td>
							</tr>
							

							<tr>
								<td width="40%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;再次输入新密码：</span>
								</td>
								<td class="td1" colspan="2" align="left"><input
									id="rePassword" name="password2" type="password" size="22"
									maxLength="50"></td>
							</tr>
							
							<tr>
								<td width="120" height="36" class="biao_bg1" align="right"><span
									class="wz">&nbsp;&nbsp;密码强度：</span></td>
								<td class="td1">

									<div
										style="clear: both; width: 70%;float:left; margin:8px 8px 8px 1px; height:7px; font-size:7px; background: gray">
										<div id="infoBar"></div>
									</div>
									<div id="infoText" style=" width: 20%; float:right"></div></td>
							</tr>
							<tr>
								<td class="table1_td"></td>
								<td></td>
							</tr>
						</table>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
					</s:form>
					<table width="90%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td align="center" valign="middle"></td>
						</tr>
					</table></td>
			</tr>
		</table>
	</DIV>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>保存允许访问IP段</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
		<script type="text/javascript">
			function submitForm(){
				var ErrMsg="" 
				if(document.getElementById("loginBeginIp").value==null||document.getElementById("loginBeginIp").value==""){
					alert("请输入起始ip地址！");
					return false;
				}else{	
					var result=isIP(document.getElementById("loginBeginIp").value); 
					ErrMsg="起始ip地址是一个非法的IP地址段！\nIP段为：:xxx.xxx.xxx.xxx（xxx为0-255)！"; 
				 	if(result==null){     
				  		alert(ErrMsg);   
				  		return false;  
				  	}     
				}
				if(document.getElementById("loginEndIp").value==null||document.getElementById("loginEndIp").value==""){
					alert("请输入结束ip地址！");
					return false;
				}else{
					ErrMsg="结束ip地址是一个非法的IP地址段！\nIP段为：:xxx.xxx.xxx.xxx（xxx为0-255)！"; 
					var result=isIP(document.getElementById("loginEndIp").value);  
				 	if(result==null){     
				  		alert(ErrMsg);   
				  		return false;  
				  	}     
				}
				document.getElementById("myTableForm").submit();
			}
			
			function isIP(sIPAddress)   {  
          		 var   exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;     
				 var   reg   =   sIPAddress.match(exp);   
				 return reg;  			   
  			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form theme="simple" id="myTableForm"
							action="/ipaccess/setipscope/ipScope!save.action">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
												<s:if test="toaSysmanageLogin.loginId!=null">
													&nbsp;&nbsp;&nbsp;编辑允许访问IP段
												</s:if>
												<s:else>
													&nbsp;&nbsp;&nbsp;增加允许访问IP段
												</s:else>
													
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="loginId" name="loginId" type="hidden" size="32"
								value="${toaSysmanageLogin.loginId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">起始IP(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="loginBeginIp" name="loginBeginIp" type="text"
											size="32" value="${toaSysmanageLogin.loginBeginIp}">&nbsp;<font color="gray">IP规则:</font><font color="red">xxx.xxx.xxx.xxx（xxx为0-255)</font>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">结束IP(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="loginEndIp" name="loginEndIp" type="text" size="32"
											value="${toaSysmanageLogin.loginEndIp}">&nbsp;<font color="gray">IP规则:</font><font color="red">xxx.xxx.xxx.xxx（xxx为0-255)
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">备注：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="loginDesc" name="loginDesc" rows="5" cols="30"
											style="overflow: auto;">${toaSysmanageLogin.loginDesc}</textarea>
									</td>
								</tr>
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input id="save" name="save" type="button"
														class="input_bg" value="保 存" onclick="submitForm()">
												</td>
												<td width="37%">
													<input id="close()" name="close" type="button"
														class="input_bg" value="关 闭" onclick="window.close()">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

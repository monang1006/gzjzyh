<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	    
		<title>日志操作</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js" ></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js" ></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js" ></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<script language="javascript">
		function formsubmit(){
		     //判断开始日期不会大于系统日期
		if(document.all("logOpdate1").value>document.all("logOpdate2").value){
				alert("开始时间不能大于系统时间");
				return false;	
		}

			document.forms[0].submit();
			window.returnValue="RELOAD";
		   
		}
		function tree(){
			window.showModalDialog("<%=path%>/systemlog/systemLog!tree.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
		}		
	</script>
	<base target=_self>
	
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
	<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
	<%@include file="/common/include/meta.jsp" %>
		<DIV id=contentborder align=center>
		<s:actionmessage/>
			<s:form id="mytable" action="/systemlog/systemLog!export.action" method="POST" target="input">
				<input type="hidden" id="sysIds" name="voSystemLog.sysIds" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"	style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"	cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="50%">
													<img src="<%=path%>/common/images/ico.gif" width="9" height="9">&nbsp;
													导出条件
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
							<table width="100%" height="10%" border="0" cellpadding="0"	cellspacing="1" align="center" class="table1">
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">操作用户</span>
									</td>
									<td class="td1" colspan="4" align="left">
<%--										<input id="logOpIds" name="voSystemLog.logOpIds" type="text" size="22" value="${boBaseSystem.sysSyscode}">--%>
<%--										<input id="orgName" name="model.baseOrg.orgName" value="${model.baseOrg.orgName}" type="text" size="22" >--%>
<%--										<input type="button" name="btnChooseBank" value="..." onclick="tree();" class="input_bg" readonly="readonly" >--%>
										<input id="logOpIds" name="voSystemLog.logOpIds"  type="text" size="22">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">操作IP</span>
									</td>
									<td class="td1" colspan="4" align="left">
										<input id="logOpIp" name="voSystemLog.logOpIp" type="text" size="22" value="">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">启用时间</span>
									</td>
									
									<td class="td1"  align="left">
										<strong:newdate id="logOpdate1" name="voSystemLog.logOpdate1"  dateform="yyyy-MM-dd"  width="133"/>
									到	<strong:newdate id="logOpdate2" name="voSystemLog.logOpdate2"  dateform="yyyy-MM-dd"  width="133" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">日志级别：</span>
									</td>
									<td class="td1" colspan="4" align="left">
										<select name="voSystemLog.logGrade" value="" style="size:22" style="width:11.2em">
											<option value="">
																							
											<option value="0">
												操作日志
											</option>
											<option value="1">
												系统日志
											</option>
											<option value="2">
												安全日志
											</option>											
										</select>
										
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">操作结果：</span>
									</td>
									<td class="td1" colspan="4" align="left">
										<select name="voSystemLog.logOpresult" value=""  style="size:22" style="width:11.2em">
											<option value="">
											
											<option value="0">
												失败
											</option>
											<option value="1">
												成功
											</option>
										
										</select>
										
									</td>
								</tr>								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">操作内容：</span>
									</td>
									<td class="td1" colspan="4" align="left">
										<textarea id="logOpcontent" name="voSystemLog.logOpcontent" rows="6" cols="30"></textarea>
									</td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>			

							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="30%">
													<input type="button" class="input_bg" value="导  出" onclick="formsubmit();">
												</td>
												<td width="30%">
													<input type="button" class="input_bg" value="取  消" onclick="javascript:window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>	
			</s:form>
		</DIV>
		<iframe id="input" name="input" style="display:none"></iframe>
	</body>
</html>

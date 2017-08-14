<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%>
<html>
	<head>
		<title>界面设置</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/common/search.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		
		<script type="text/javascript">
			
		
		function submitForm(){		
			document.getElementById("myForm").submit();
		}
					
		</script>
	</head>
		
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form action="/iphoneset/iphoneset!save.action" id="myForm" theme="simple" enctype="multipart/form-data" >						
				<div align=left style="width: 95%">
					<table height="40px">
						<tr>
							<td class=td1>
								<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
								<span class="wz">Iphone界面设置</span>
							</td>
						</tr>
					</table>
				</div>
				
				<fieldset style="width: 95%">
					<legend>
						<span class="wz">登录选项</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">	
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">登陆界面背景：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneBgPic" id="iphoneBgPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneBgPic.jpg?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
																	
					</table>
				</fieldset>
				
				<fieldset style="width: 95%">
					<legend>
						<span class="wz">模块图标</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">	
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">待办事宜图标：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneDbsyPic" id="iphoneDbsyPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneDbsyPic.png?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">公文管理图标：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneGwglPic" id="iphoneGwglPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneGwglPic.png?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">通讯录图标：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneTxlPic" id="iphoneTxlPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneTxlPic.png?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">内部邮件图标：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneNbyjPic" id="iphoneNbyjPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneNbyjPic.png?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">通知公告图标：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneTzggPic" id="iphoneTzggPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneTzggPic.png?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
						
						<tr>
							<td width="30%" height="21" class="biao_bg1" align="right" title="上传图片规格为(1366×90)">
								<span class="wz">系统设置图标：</span>
							</td>
							<td class="td1" width="70%" align="left" title="上传图片规格为(1366×90)">
								<input class="upFileBtn" type="file" name="iphoneXtszPic" id="iphoneXtszPic">
								图片预览：<img src="<%=root%>/oa/image/iphone/iphoneXtszPic.png?tempid=<%=System.currentTimeMillis() %>" width="150" height="30"/ >
							</td>
						</tr>
																	
					</table>
				</fieldset>
				
				<table width="95%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="40%" height="34">
										&nbsp;
									</td>
									<td width="10%">
										<input id="save" name="save" type="button" class="input_bg"
											onclick="submitForm()" value="保存">
									</td>

									<td width="10%">
										<input name="button" type="reset" class="input_bg" value="重置">
									</td>
									<td width="40%" height="34">
										&nbsp;
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
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<title>新建邮件夹</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#btnOK").click(function(){
					if($.trim($("#folderName").val())==''){
						alert("对不起请您填写邮件夹名称");
						$("#folderName").focus();
					}else{
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/mymail/mailFolder!input.action",
							data:"parentid="+$("#parentid").val()+"&model.mailfolderName="+$.trim($("#folderName").val())+"&type=add",
							success:function(msg){
								if(msg=="true"){
									window.returnValue="true";
									//alert("邮件夹添加成功！");
									window.close();
								}else if(msg=="rename"){
									alert("自定义文件夹不允许重名");
								}else{
									alert("邮件夹添加失败");
								}
							}
						});
					}
				});
			
				$("#btnCan").click(function(){
					window.close();
				});
				
				$("#folderName").keydown(function(e){
					if(e.keyCode==13){
						$("#btnOK").click();
					}
				});
			});
		</script>
	</head>
	<base target="_self" />
	<body oncontextmenu="return false;">
		<label id="l_actionMessage" style="display: none;">
			<s:actionmessage />
		</label>
			<input type="hidden" name="parentid" id="parentid" value="${parentid }">
			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr height="30px">
					<td colspan="2"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
						&nbsp;&nbsp;&nbsp;&nbsp;新建邮件夹
					</td>
				</tr>
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right">
						<span class="wz">邮件夹名称(<font color='red'>*</font>)：</span>
					</td>
					<td class="td1">
						&nbsp;<input type="text" name="folderName" id="folderName" style="width:98%;"  size="25"  maxlength="25"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="td1" align="center">
						<input id="btnOK" type="button" class="input_bg"
							icoPath="<%=path%>/common/images/queding.gif" value="  确定" />
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input id="btnCan" type="button" class="input_bg"
							icoPath="<%=path%>/common/images/quxiao.gif" value="  取消" />
					</td>
				</tr>
			</table>
	</body>
</html>

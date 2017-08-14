<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>发送值班记录</title>
		<LINK href="../../common/frame/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script type="text/javascript">
			var date=new Date();
		 	function selectPerson(){
		 	}
		</script>
	</head>
	<body  oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"  style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
			        <td><img src="<%=frameroot%>/images/ico.gif" width="7" height="9"></td>
			        <td>发送值班记录</td>			    
				</tr>
			</table>
			<br>
			<fieldset style="width:100% ">
			<legend><font size="2">请选择发送方式</font></legend>
			<input type="radio" id="sendType1" name="sendType" value='0'>
						<font size="2">短信发送</font>	
			<input type="radio" id="sendType2" name="sendType" value='1'>
			<font size="2">邮件发送</font>	
			</fieldset>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="34%" height="34">
									&nbsp;
								</td>
								<td width="29%">
									<input name="Submit" type="submit" class="input_bg" value="发 送">
								</td>
								<td width="37%">
									<input name="Submit2" type="submit" class="input_bg"
										value="关 闭" onclick="javascript:window.close();">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

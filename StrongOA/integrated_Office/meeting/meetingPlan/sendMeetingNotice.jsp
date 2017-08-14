<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>发送通知</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						发送会议通知
					</td>
					<td width="*">
					 	&nbsp;
					</td>	
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">会议名称：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="name" name="name" type="text" size="30"
							readonly="readonly">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">拟办时间：</span>
					</td>
					<td class="td1" align="left">
						<input id="time" name="time" type="text" size="30"
							readonly="readonly">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议地点：</span>
					</td>
					<td class="td1" align="left">
						<input id="place" name="place" type="text" size="30" value=""
							readonly="readonly">
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">主持人：</span>
					</td>
					<td class="td1" align="left">
						<input id="moderator" name="moderator" type="text" size="30"
							value="" readonly="readonly">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">通知发送人：</span>
					</td>
					<td class="td1" align="left">
						<input id="sender" name="sender" type="text" size="30"
							readonly="readonly" value="用户">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议内容：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="4" cols="45" id="content" name="content"></textarea>
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">安排事宜：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="4" cols="45" id="things" name="things"></textarea>
					</td>
				</tr>

				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">基本议程：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="4" cols="45" id="agenda" name="agenda">需填写</textarea>
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">参会人员：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="4" cols="45" id="participants" name="participants"></textarea>
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">备注：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="4" cols="45" id="remarks" name="remarks"></textarea>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="10%" height="34">
									&nbsp;
								</td>
								<td width="40%">
									<input name="Submit" type="submit" class="input_bg" value="保 存">
								</td>
								<td width="25%">
									<input name="Submit" type="submit" class="input_bg"
										value="保存且发送">
								</td>
								<td width="25%">
									<input name="Submit2" type="submit" class="input_bg"
										value="返 回" onclick="history.go(-1)">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

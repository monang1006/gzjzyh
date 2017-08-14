<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看值班记录</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script type="text/javascript">
			var date=new Date();
		 	function selectPerson(){
		 	}
		 	function sendWatch(){
		 		OpenWindow("sendWatch.jsp",'350pt','150pt','sendWindow');
		 	}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						查看值班记录
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
						<span class="wz">来访人姓名：</span>
					</td>
					<td class="td1" align="left">
						<input id="interviewer" name="interviewer" type="text" size="30"
							readonly="readonly">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">来访人电话：</span>
					</td>
					<td class="td1" align="left">
						<input id="phone" name="phone" type="text" size="30"
							readonly="readonly">
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">来访时间：</span>
					</td>
					<td class="td1" align="left">
						<input id="time" name="time" type="text" size="30" value="当天"
							readonly="readonly">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">来访人地址：</span>
					</td>
					<td class="td1" align="left">
						<input id="place" name="place" type="text" size="30"
							readonly="readonly">
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">被访人员：</span>
					</td>
					<td class="td1" align="left">
						<input id="interviewed" name="interviewed" type="text" size="30"
							readonly="readonly" value="人员列表中选择">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">接待人：</span>
					</td>
					<td class="td1" align="left">
						<input id="reception" name="reception" type="text" size="30"
							value="用户" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">访问类型：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="type" name="type" type="text" size="30"
							readonly="readonly">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">访问内容：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea id="content" rows="15" cols="60" style="overflow:auto"></textarea>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="34%" height="34">
									&nbsp;
								</td>
								<td width="29%">
									<input name="button1" type="button" class="input_bg"
										value="发 送" onclick="sendWatch()">
								</td>
								<td width="37%">
									<input name="button2" type="button" class="input_bg"
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

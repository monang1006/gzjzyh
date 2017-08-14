<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看议题分类</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<base target="_self">
	</head>
	<body oncontextmenu="return false;" style="overflow: auto">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						查看议题分类
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">分类名称：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="name" name="name" type="text" size="22">
					</td>
				</tr>
					<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">审批流程：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="name" name="name" type="text" size="22">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">分类描述：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="10" cols="50" id="description" name="description"></textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit2" type="submit" class="input_bg" value="关闭"
							onclick="window.close();">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

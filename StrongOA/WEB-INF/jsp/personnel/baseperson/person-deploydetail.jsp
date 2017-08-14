<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/meta.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看调配记录信息</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
	</head>
	<body oncontextmenu="return false;" style="overflow: auto">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="40"
						style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0"
							cellpadding="00">
							<tr>
								<td>&nbsp;</td>
								<td width="20%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
										height="9">&nbsp;
									查看调配记录信息
								</td>
								<td width="*%">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td width="25%" height="21" class="biao_bg1" align="right">
						<span class="wz">调配时间：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="exchangeTime" name="deployinfo.exchangeTime"
							value="${deployinfo.exchangeTime}" type="text" size="35" maxlength="16">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">调配类别：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="pdepName" name="deployinfo.deployInfo.pdepName"
							value="${deployinfo.deployInfo.pdepName}" type="text" size="35" maxlength="16">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">调配前：</span>
					</td>
					<td class="td1" colspan="3" align="left">
				    	<textarea id="oldInfos" rows="5" cols="40" onkeydown=""
										name="deployinfo.oldInfos" maxlength="200">${deployinfo.oldInfos}</textarea>
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">调配后：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea id="newInfos" rows="5" cols="40" onkeydown=""
										name="deployinfo.newInfos" maxlength="200">${deployinfo.newInfos}</textarea>
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">调配原因：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea id="exchangeWhy" rows="5" cols="40" onkeydown=""
										name="deployinfo.exchangeWhy" maxlength="200">${deployinfo.exchangeWhy}</textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

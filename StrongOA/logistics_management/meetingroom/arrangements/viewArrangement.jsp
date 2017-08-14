<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看会议室安排</title>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/upload/jquery.blockUI.js'></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
	</head>
	<body oncontextmenu="return false;" style="overflow: auto">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%" >
						<img src="<%=frameroot%>/images/ico.gif" width="7" height="9">
					</td>
					<td width="20%" align="left">
						查看会议室安排
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">申请单：</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<input type="text" id="Application" size="30" value="申请单"
							readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议室名称：</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<input type="text" id="meetingRoom" size="30" readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">安排人：</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<input type="text" size="30" id="receiver" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">计划开始使用时间：</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<input type="text" id="stime" value="默认为申请开始使用时间" size="30"
							readonly="readonly" />
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">计划结束使用时间：</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<input type="text" id="etime" size="30" value="默认为申请结束使用时间"
							readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="5" align="center">
						<input id="back" type="submit" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</DIV>
		</script>
	</body>
</html>

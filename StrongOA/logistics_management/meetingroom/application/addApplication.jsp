<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加会议计划</title>
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
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						保存会议室申请
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td colspan="1" height="21" width="25%" class="biao_bg1"
						align="right">
						<span class="wz">会议室名称：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="meetingroom" name="meetingroom" type="text" size="30"
							value="选择的会议室" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">申请人：</span>
					</td>
					<td class="td1" align="left">
						<input type="text" id="applyTime" size="30" value="用户"
							readonly="readonly" />
					</td>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">申请时间：</span>
					</td>
					<td class="td1" align="left">
						<input type="text" id="applyTime" size="30" value="当天"
							readonly="readonly" />
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz" style="width:100%">申请开始使用时间：</span>
					</td>
					<td class="td1" align="left">
						<strong:newdate id="startTime" dateform="yyyy-MM-dd HH:ss"
							width="175" />
					</td>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">申请结束使用时间：</span>
					</td>
					<td class="td1" align="left">
						<strong:newdate id="endTime" dateform="yyyy-MM-dd HH:ss"
							width="175" />
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">说明：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="10" cols="60" id="description" name="description"
							style="overflow: auto;"></textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit" type="submit" class="input_bg" value="保 存">
						<input name="Submit2" type="submit" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</DIV>
		<script type="text/javascript">
		$(document).ready(function(){
			$("#select").click(function(){
				var ReturnStr=window.showModalDialog("meetingRoomList.jsp","meetingRoomWindow","dialogWidth:600pt;dialogHeight:400pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
			});
		});
	</script>
	</body>
</html>

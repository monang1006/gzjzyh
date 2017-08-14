<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<title>参数设置</title>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'>
</script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'>
</script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'>
</script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>

	</head>
	<script>
function save() {
	if ($.trim($("#worktime").val()) == "") {
		alert("请输入上班时间！");
		$("#worktime").val("");
		$("#worktime").focus();
		return;
	}
	if ($.trim($("#leavetime").val()) == "") {
		alert("请输入下班时间！");
		$("#leavetime").val("");
		$("#leavetime").focus();
		return;
	}
	var worktime=$.trim($("#worktime").val());
	var timeArray=worktime.split(":");
	if(timeArray.length<2||timeArray.length>2){
		alert("上班时间格式不正确，格式为：09:30的形式！");
		return;
	}
	if(timeArray.length==2){
		var hour=timeArray[0];
		var minute=timeArray[1];
		if(hour.length>2||minute.length>2){
			alert("上班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(hour.length==1||minute.length==1){
			alert("上班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(!/^\d+$/.test(hour)){
			alert("上班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(!/^\d+$/.test(minute)){
			alert("上班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(parseInt(hour)>=24||parseInt(hour)<0){
			alert("上班时间格式不正确，小时不能大于等于24或不能小于0！");
			return;
		}
		if(parseInt(minute)>=60||parseInt(hour)<0){
			alert("上班时间格式不正确，分钟不能大于或等于60或不能小于0！");
			return;
		}
	}
	var leavetime=$.trim($("#leavetime").val());
	var leavetimeArray=leavetime.split(":");
	if(leavetimeArray.length<2||leavetimeArray.length>2){
		alert("下班时间格式不正确，格式为：09:30的形式！");
		return;
	}
	if(leavetimeArray.length==2){
		var lthour=leavetimeArray[0];
		var ltminute=leavetimeArray[1];
		if(lthour.length>2||ltminute.length>2){
			alert("下班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(hour.length==1||minute.length==1){
			alert("上班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(!/^\d+$/.test(lthour)){
			alert("下班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(!/^\d+$/.test(ltminute)){
			alert("下班时间格式不正确，格式为：09:30的形式！");
			return;
		}
		if(parseInt(lthour)>=24||parseInt(hour)<0){
			alert("下班时间格式不正确，小时不能大于等于24或不能小于0！");
			return;
		}
		if(parseInt(ltminute)>=60||parseInt(hour)<0){
			alert("下班时间格式不正确，分钟不能大于等于60或不能小于0！");
			return;
		}
	}
	$("#form").submit();
}

function clearAll() {
	$("#worktime").val("");
	$("#leavetime").val("");
}
</script>

	<body>
		<form id="form" action='<%=path%>/attence/attenceTime!save.action'
			method="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td width="5">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
							width="9" height="9">
					</td>
					<td width="20%">
						考勤时间设置
					</td>


					<td width="5">
						&nbsp;
					</td>

				</tr>
				<tr>
					<td width="5">
						&nbsp;
					</td>
				</tr>
			</table>
			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						上班时间：
					</td>
					<td colspan="3" class="td1" align="left">
						<input type="text" id="worktime" name="model.worktime"
							value="${model.worktime}">
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						下班时间：
					</td>
					<td colspan="3" class="td1" align="left">
						<input type="text" id="leavetime" name="model.leavetime"
							value="${model.leavetime}">
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="td1" align="right">
						<input type="button" class="input_bg" id="confirm" value="确定"
							onclick="save();">
					</td>
					<td colspan="1" height="21" class="td1" align="left">
						<input type="button" class="input_bg" id="clear" value="重置"
							onclick="clearAll();">
					</td>
				</tr>
			</table>

		</form>
	</body>
</html>

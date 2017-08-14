<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>考勤查询</title>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'>
</script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=path%>/theme/theme_default/css/global.css"
			rel="stylesheet" type="text/css" />


	</head>

	<body>
		<script type="text/javascript"
			src="<%=path%>/common/script/My97DatePicker/WdatePicker.js">
</script>
		<DIV id=contentborder align=center>

			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER: progid :     DXImageTransform.Microsoft.Gradient (     gradientType =     0, startColorStr =     #ededed, endColorStr =     #ffffff );">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="5">
												&nbsp;
											</td>
										</tr>
										<tr>
											<td width="5%" align="center">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">
											</td>
											<td width="20%">
												考勤查询
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
									<input type="hidden" id="startTime" name="startTime">
									<input type="hidden" id="endTime" name="endTime">
									<input type="hidden" id="userID" name="userID">

									<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">

										<tr>
											<td width="5%" class="biao_bg1" align="right">
												月份&nbsp;:&nbsp;
											</td>
											<td width="15%" class="biao_bg1">
												<strong:newdate name="searchdate" id="searchdate"
													skin="whyGreen" isicon="true" dateobj="${searchdate}"
													dateform="yyyy-MM" width="40%"></strong:newdate>
											</td>

											<td width="1%" class="biao_bg1">
												<input type="button" id="selectpeople" name="selectpeople"
													value="选择人员" class="input_button_4" onclick="selectPersonTree();">
											</td>
											<td width="10%" class="biao_bg1">
												<input type="text" id="userName" name="userName" height="20px" onclick="selectPersonTree();">
											</td>


											<td width="5%" class="biao_bg1">
												<input type="button" value="开始查询" onclick="showReport();"
													class="input_button_4">
											</td>

										</tr>

									</table>
						</table>

					</td>

				</tr>
			</table>

			<div id="reportContent">
				<iframe id="reportList" height="100%" width="100%" frameborder="no"
					display="no" border="0" marginwidth="0" marginheight="0"
					scrolling="no"></iframe>
			</div>
		</DIV>
		<script language="javascript">
		
/**
*弹出选择人员树
**/
function selectPersonTree() {
	var ret = window
			.showModalDialog("<%=path%>/address/addressOrg!orgPersonTree.action"
							,window,
					'dialogWidth:420pt;dialogHeight:370pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ret){
		var userID=ret.split(",")[0];
		var userName=ret.split(",")[1];
		$("#userID").val(userID);
		$("#userName").val(userName);
	}
}

//转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
function date2string(stime) {
	var arrsDate1 = stime.split('-');
	stime = arrsDate1[0] + "" + arrsDate1[1] + "" + arrsDate1[2];
	var arrsDate2 = stime.split(' ');
	stime = arrsDate2[0] + "" + arrsDate2[1];
	var arrsDate3 = stime.split(':');
	stime = arrsDate3[0] + "" + arrsDate3[1] + "" + arrsDate3[2];
	return stime;
}

function showReport() {
	var searchdate = $("#searchdate").val();

	if (searchdate != '' && searchdate != null) {
		var ss = searchdate.toString();
		var s = ss.substring(5, 7);
		var y = ss.substring(0, 4);
		var d = new Date();
		d.setFullYear(y);
		d.setMonth(s - 1);
		var MonthFirstDay = new Date(d.getFullYear(), d.getMonth(), 1);
		var MonthNextFirstDay = new Date(d.getFullYear(), d.getMonth() + 1, 1);
		var MonthLastDay = new Date(MonthNextFirstDay - 86400000);
		mm = MonthFirstDay.getMonth() + 1;
		dd = MonthFirstDay.getDate();
		mm = mm < 10 ? "0" + mm : mm;
		dd = dd < 10 ? "0" + dd : dd;
		ml = MonthLastDay.getMonth() + 1;
		dl = MonthLastDay.getDate();
		ml = ml < 10 ? "0" + ml : ml;
		dl = dl < 10 ? "0" + dl : dl;
		var ff = MonthFirstDay.getFullYear() + '-' + mm + '-' + dd;
		var last = MonthLastDay.getFullYear() + '-' + ml + '-' + dl;
		document.getElementById("startTime").value = ff;
		document.getElementById("endTime").value = last;
	}

	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var userID;
	if($("#userName").val()!=""){
		userID=$("#userID").val();	
	}else{
		userID="";
	}
	document.getElementById("reportContent").style.display = "";//显示 

	document.getElementById("reportList").src = "<%=path%>/fileNameRedirectAction.action?toPage=attence/attenceRecord-result.jsp?startTime="
			+ startTime + "&endTime=" + endTime+"&userID="+userID;
}
</script>
	</body>
</html>

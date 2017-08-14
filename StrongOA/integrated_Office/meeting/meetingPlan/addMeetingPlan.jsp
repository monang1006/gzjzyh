<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加会议计划</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
	</head>
	<script type="text/javascript">
<%--		$(document).ready(function(){--%>
<%--			alert("111111111111")--%>
<%--				$("textarea").each(function(){--%>
<%--					var textcss={"style":"overflow:auto"};--%>
<%--					alert(textcss);--%>
<%--					${this}.css(textcss);--%>
<%--				});--%>
<%--		});--%>
		function selectTitle(){
			var ReturnStr=window.showModalDialog("selectTitle.jsp","selectTitleWindow","dialogWidth:600pt;dialogHeight:400pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		}
	</script>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						保存会议计划
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
						<input id="name" name="name" type="text" size="30">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">拟办时间：</span>
					</td>
					<td class="td1" align="left">
						<input id="time" name="time" type="text" size="30">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议地点：</span>
					</td>
					<td class="td1" align="left">
						<input id="place" name="place" type="text" size="30">
					</td>

				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议议题：</span>
					</td>
					<td class="td1" align="left">
						<input id="titleId" name="titleId" type="text" size="30"
							readonly="readonly">
						<input type="submit" value="选择" onclick="selectTitle()">
					</td>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">主持人：</span>
					</td>
					<td class="td1" align="left">
						<input id="moderator" name="moderator" type="text" size="30">
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">会议内容：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="6" cols="50" id="content" name="content"
							style="overflow:auto"></textarea>
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">安排事宜：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="6" cols="50" id="things" name="things"
							style="overflow:auto"></textarea>
					</td>
				</tr>
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">参会人员：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="6" cols="50" id="participants" name="participants"
							style="overflow:auto"></textarea>
					</td>
				</tr>

				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">备注：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="6" cols="50" id="remarks" name="remarks"
							style="overflow:auto"></textarea>
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
									<input id="Submit" type="submit" class="input_bg" value="保 存">
								</td>
								<td width="37%">
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

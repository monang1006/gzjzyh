<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>

	<head>
		<%@include file="/common/include/meta.jsp"%>
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js">
</script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript">
</script>
		<base target="_self">

		<title>任务评语</title>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
	</head>

	<body class=contentbodymargin oncontextmenu="return false;">


		<DIV id=contentborder align=center>

			<s:form id="mytable"
				action="/workinspect/worktodo/workTodo!view.action" method="POST">
				<input type="hidden" id="sendtaskId"
					name="tOsWorktaskSend.sendtaskId"
					value="${tOsWorktaskSend.sendtaskId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="20%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												任务评语
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="85%">
												
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
										<span class="wz">标题</span>
									</td>
									<td class="td1" colspan="3" align="left" >
									    ${model.TOsWorktask.worktaskTitle}
										<%--<input id="worktaskTitle" name="model.TOsWorktask.worktaskTitle" value="${model.TOsWorktask.worktaskTitle}" readonly="readonly"/>--%>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">完成等级</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<script type="text/javascript">
var type = '${model.TOsWorktask.commpleteLevel}';
var tName = "";
if (type == "0") {
	tName = "按时完成";
} else if (type == "1") {
	tName = "超期完成";
}

document.write(tName);
</script>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">评语内容</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.TOsWorktask.reviewsDemo}
										<%--<input id="reviewsDemo" name="model.TOsWorktask.reviewsDemo" value="${model.TOsWorktask.reviewsDemo}" readonly="readonly"/>--%>
									</td>
								</tr>
							</table>
							<br>
							<br>
							<table align="center" width="90%" border="0" cellspacing="0"
								cellpadding="00">
											<tr>
												<td width="33%" align="center">
													<input id="btn_close" type="button" class="input_bg"
														value="返回" onclick="javascript:window.close();" >
									</td>
								</tr>
						</table>
										</s:form>
										</DIV>
	</body>
</html>

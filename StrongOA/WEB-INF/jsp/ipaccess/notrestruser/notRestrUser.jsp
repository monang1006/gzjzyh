<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>设置不限IP用户</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<!--右键菜单脚本 -->
		<script type="text/javascript">
		$(document).ready(function(){
			$("#select").click(function(){
<%--			OpenWindow("",'400pt','230pt',window);--%>
			});
		});
</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
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
											<td>&nbsp;</td>
											<td width="50%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												设置不限IP用户
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td >
												<a class="Operation" href="#" onclick="loginIPAdd()"> <img
														src="<%=root%>/images/ico/shezhibuxianip.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">设 置&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="history.go(-1);">
													<img src="<%=root%>/images/ico/ht.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">返 回&nbsp;</span> </a>
											</td>
											<td width="5"></td>

										</tr>
									</table>
								</td>
							</tr>
						</table>
						<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="1" align="center" class="table1">
							<tr>
								<td width="20%" height="21" class="biao_bg1" align="right">
									<span class="wz">设置不限IP用户：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									<textarea rows="12" cols="50"></textarea>
									<input id="select" name="select" type="submit" class="input_bg"
										value="选 择">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>

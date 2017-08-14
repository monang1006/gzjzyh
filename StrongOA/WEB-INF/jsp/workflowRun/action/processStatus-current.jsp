<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>处理记录</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<!--页面样式 -->
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">

		<center>
			<TABLE align="center" border="0" cellspacing="1" cellpadding="0"
				width="100%">
				<TBODY>

					<c:forEach items="${handlerecords}" var="history">
						<tr>
							<td height="5"></td>
						</tr>
						<tr>
							<td>
								开始时间：&nbsp;
								<c:out value="${history[0]}" />
								<br>
								结束时间：&nbsp;
								<c:out value="${history[1]}" />
								<br>
								处&nbsp;理&nbsp;人&nbsp;：&nbsp;
								<c:out value="${history[2]}" />
								<br>
								处理意见：&nbsp;
								<c:out value="${history[3]}" />
								<br>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${total != '0' }">
						<tr>
							<td align="left">

								<hr color="gray" width="80%">

							</td>
						</tr>
					</c:if>
					<c:forEach items="${currentActor}" var="actors">
						<tr>
							<td height="5"></td>
						</tr>
						<tr>
							<td>
								当前状态：&nbsp;
								<c:if test="${actors[2]!='1' }">待处理</c:if>
								<c:if test="${actors[2]=='1' }">处理中</c:if>
								<br>
								开始时间：&nbsp;
								<c:out value="${actors[0]}" />
								<br>
								处&nbsp;理&nbsp;人&nbsp;：&nbsp;
								<c:out value="${actors[1]}" />
								&nbsp;&nbsp;
								<a href="#">催办</a>
								<br>
							</td>
						</tr>
					</c:forEach>

				</TBODY>
			</table>
		</center>
	</body>
</html>

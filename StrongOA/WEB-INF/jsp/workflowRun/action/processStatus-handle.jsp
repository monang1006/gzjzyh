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

					<c:if test="${total == '0'}">
						<tr>
							<td>
								暂无处理记录！
							</td>
						</tr>
					</c:if>
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

				</TBODY>
			</table>
		</center>
	</body>
</html>

<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.text.SimpleDateFormat,java.util.Date"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/meta.jsp"%>
<html>
	<head>
	</head>
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		String now = sdf.format(new Date());
		String groupId=request.getParameter("groupId");
	%>
	<body>
		<div id="footer">
			<s:if test="groupId!=null&&groupId!=\"\"">
				<a href="<%=root %>/wap/addressGroup!getAddressList.action?groupId=${groupId}">列表</a>&nbsp;|&nbsp;
			</s:if>
			<s:else>
					<a href="<%=root %>/wap/addressGroup!getAddressList.action?groupId=<%=groupId%>">列表</a>&nbsp;|&nbsp;
			</s:else>
			<a class="cur" href="<%=root%>/wap/login!getMainInfo.action">导航</a>&nbsp;|&nbsp;
			<a class="cur" href="#anchor1">置顶</a>
		</div>
		<p></p>
		<small>[<%=now%>]</small>
	</body>
</html>

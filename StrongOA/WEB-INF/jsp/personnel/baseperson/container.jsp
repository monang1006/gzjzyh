<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@page import="java.net.URLDecoder"%>
<HTML>
	<HEAD>
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
		<%
			String forward = request.getParameter("forward");
			String orgId = request.getParameter("orgId");
			String tables = request.getParameter("tables");
			String condition = request.getParameter("condition");
			condition = URLDecoder.decode(condition, "utf-8");
			if ("zwtj".equals(forward)) {
		%>
			<FRAMESET rows=40%,* id="frames" framespacing="1" border="1"
				frameborder="1" bordercolor="#d4d0c7">
		<%
			} else {
		%>
			<FRAMESET rows=65%,* id="frames" framespacing="1" border="1"
				frameborder="1" bordercolor="#d4d0c7">
		<%
			}
		%>
				<FRAME id="statistic" name=statistic marginWidth=0 marginHeight=0
					src="" frameBorder=0 scrolling=no>
				<FRAME id="detail" name=detail marginWidth=0 marginHeight=0
					src="<%=path%>/fileNameRedirectAction.action?toPage=/personnel/baseperson/temp.jsp?type=statistic"
					frameBorder=0 scrolling=no>
			</FRAMESET>
			<noframes></noframes>
			<script type="text/javascript">
				var orgId="<%=orgId%>";
				var tables="<%=tables%>";
				var condition="<%=condition%>";
				var forward="<%=forward%>";
				condition=encodeURI(encodeURI(condition));
				if(forward=="zwtj"){	//满足年限职务统计
					document.getElementById("statistic").src="<%=root%>/personnel/baseperson/person!getDutyStatistic.action?orgId="+orgId+"&tables="+tables+"&condition="+condition;
				}else{					//休假情况统计
					document.getElementById("statistic").src="<%=path%>/fileNameRedirectAction.action?toPage=/personnel/baseperson/person-container.jsp?orgId="+orgId+"&tables="+tables+"&condition="+condition;
				}
			</script>
</HTML>

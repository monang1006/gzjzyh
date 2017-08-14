<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<!-- saved from url=(0067)http://192.168.2.83:8080/chinaspis/personal/perspective_content.jsp -->
<HTML>
	<HEAD>
	
		<TITLE></TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>

	<FRAMESET cols=20%,* framespacing="1" border="1" frameborder="1" bordercolor="#d4d0c7">
		<FRAME name=articlescolumnTree marginWidth=0 marginHeight=0
			src="<%=root%>/infopub/articles/articles!tree.action?treeType=2" frameBorder=0 scrolling=no>
		<FRAME name=articlesList marginWidth=0 marginHeight=0
			src="" frameBorder=0 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
	<%
		String id = request.getParameter("columnId");
	 %>
	<script type="text/javascript">
		var columnId = '<%=id%>';
		articlesList.location = '<%=root%>/infopub/articles/columnArticles.action?columnId='+columnId+'&showtype=0';
	</script>
</HTML>

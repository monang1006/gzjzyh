<!--原信用社界面(样式)首页-->
<!--newIndex.ftl-->
<!--首页-->
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<%@include file="/common/include/rootPath.jsp"%>
<head>
<TITLE>
	<%
		ToaSysmanageBase sysTheme = request.getSession()
		.getAttribute("sysTheme")==null?null:(ToaSysmanageBase)request.getSession()
				.getAttribute("sysTheme");
		if(sysTheme!=null&&sysTheme.getBaseWindowsTitle()!=null&&sysTheme.getBaseWindowsTitle().length()>0){
	%>
		<%=sysTheme.getBaseWindowsTitle() %>
	<%}else{ %>
		思创数码科技股份有限公司协同办公软件
	<%} %>
</TITLE>
</head>
 		<FRAMESET border=0 frameSpacing=0 rows=87,* frameBorder=0 id="theTop">
		    <!--顶部导航条(如当前用户，退出操作等)-->
			<FRAME title=页面顶部及通用工具栏 name=perspective_top marginWidth=0 marginHeight=0 src="${F1}" frameBorder=0 noResize scrolling=no>
			<!--主工作区-->
			<!--
			<FRAME title=页面顶部及通用工具栏 name=perspective_main marginWidth=0 marginHeight=0 src="${F2}" frameBorder=0 noResize scrolling=yes>
			</FRAMESET>
			-->
			<FRAMESET border=0 cols=30,*>
			    <!--左侧一级导航条-->
				<FRAME title=一级菜单试图切换区 name=perspective_toolbar marginWidth=5 marginHeight=0 src="${F3}" frameBorder=0 noResize scrolling=no>
				<!--主框架-->
				<FRAME title="主框架内容区（分为菜单区及内容操作区）" name=perspective_main marginWidth=0 marginHeight=0 src="${F2}" frameBorder=0 scrolling=no>
			</FRAMESET>
		<noframes></noframes> 
</html>
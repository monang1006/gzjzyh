<!--工作区主界面-->
<!--首页/主框架工作区-->
<!--newIndex_main.ftl-->
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
	<FRAMESET cols=180,6,* name="title"  framespacing="0" border="0" frameborder="no">
	    <!--左则树型菜单-->
		<FRAME name=navigator_container marginWidth=0 marginHeight=0	
			src="${S1}" 
			frameBorder=0 scrolling=no  noresize="noresize">
		<!--中间-->	
		<FRAME name=personal_navigator_container marginWidth=0 marginHeight=0 
		 	src="${S2}"  
			 frameBorder=no scrolling=no  noresize="noresize">
        <!--工作区-->
		<FRAME name=actions_container marginWidth=0 marginHeight=0
			src="${S3}"
			 frameBorder=0 scrolling=no  noresize="noresize">
	</FRAMESET>
	<noframes></noframes>
</html>
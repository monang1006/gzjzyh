<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaMessage"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaMessageFolder"/>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>消息管理</title>
		<%@include file="/common/include/meta.jsp" %>
		<script type="text/javascript" src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
			function showshadow(i){
				window.mail_content_list.show(i);
			}
			function hiddenshadow(){
				window.mail_content_list.hidden();
			}
		</script>
	</head>
	<FRAMESET id="container" border="1" frameSpacing="1" rows="50%,*" frameBorder="1" bordercolor="#d4d0c7">
		<FRAME name="content_list" marginWidth="0" marginHeight="0" src="<%=path %>/sends/docSend!docsearch.action" frameBorder="0" scrolling="no">
		<FRAME name="main_content" marginWidth="0" marginHeight="0" src="<%=path %>/fileNameRedirectAction.action?toPage=sends/docSend-sendsinfo.jsp" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>

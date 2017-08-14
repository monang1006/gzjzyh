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
	<% 
		String folderId=request.getParameter("folderId");
		//System.out.println("folderId=="+folderId);
		String url = ""; 
		if(folderId!=null&&""!=folderId){
			url = path+"/message/message!list.action?folderId="+folderId;
		}else{
			url = path+"/message/message!list.action?folderId=recv";
			//url = "fileNameRedirectAction.action?toPage=message/message-context.jsp";
		}
		//System.out.println("url："+url);
		%>
	<FRAMESET id="container" border="1" frameSpacing="1" rows="74%,*" frameBorder="1" bordercolor="#d4d0c7">
		
		<FRAME name="msg_content_list" marginWidth="0" marginHeight="0" src="<%=url%>" frameBorder="0" scrolling="no">
		<FRAME name="msg_main_content" marginWidth="0" marginHeight="0" src="<%=path %>/fileNameRedirectAction.action?toPage=message/messageinfo.jsp" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>邮件管理</title>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
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
		String folderType=request.getParameter("folder");
		String sendid=request.getParameter("sendid");
	%>
	<FRAMESET id="container" border="1" frameSpacing="1" rows="70%,*" frameBorder="1" bordercolor="#d4d0c7">
		<%
			if("1".equals(folderType)){ 				//收件箱
			
		%>
			<FRAME name="mail_content_list" marginWidth="0" marginHeight="0" src="<%=root%>/mymail/mail.action?sendid=<%=sendid %>&type=view" frameBorder="0" scrolling="no">
		<%
			}else if("2".equals(folderType)){			//发件箱
		%>	
			<FRAME name="mail_content_list" marginWidth="0" marginHeight="0" src="<%=root%>/mymail/mail.action?sendid=<%=sendid %>&type=send" frameBorder="0" scrolling="no">
		<%
			}else if("3".equals(folderType)){			//草稿箱
		%>
			<FRAME name="mail_content_list" marginWidth="0" marginHeight="0" src="<%=root%>/mymail/mail.action?sendid=<%=sendid %>&type=drafts" frameBorder="0" scrolling="no">
		<%
			}else if("4".equals(folderType)){			//垃圾箱
		
		%>
			<FRAME name="mail_content_list" marginWidth="0" marginHeight="0" src="<%=root%>/mymail/mail.action?sendid=<%=sendid %>&type=rubbish" frameBorder="0" scrolling="no">
		<%	
			}else if("5".equals(folderType)){			//其他非基础文件夹
		%>
			<FRAME name="mail_content_list" marginWidth="0" marginHeight="0" src="<%=root%>/mymail/mail.action?sendid=<%=sendid %>&type=other" frameBorder="0" scrolling="no">
		<%	
			}else{ 
		%>
			<FRAME name="mail_content_list" marginWidth="0" marginHeight="0" src="fileNameRedirectAction.action?toPage=mymail/mail-context.jsp" frameBorder="0" scrolling="no">
		<%
			} 
		%>
		<FRAME name="mail_main_content" marginWidth="0" marginHeight="0" src="fileNameRedirectAction.action?toPage=mymail/mailinfo.jsp" frameBorder="0" scrolling="no">
	</FRAMESET>
	<noframes></noframes>
</html>

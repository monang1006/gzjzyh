<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
  	 String j_username = (String)request.getParameter("j_username");
  	 String j_password = (String)request.getParameter("j_password");
%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<style type="text/css">
			a{
				text-decoration: none;
			}
			a:link,a:visited{
				color: black;
			}
			body{
				font-size: 16px;
			}
		</style>
		<script type="text/javascript">
			function loginOA()
			{
				window.open("<%=root%>/toLogin.jsp");
			}
			function reset()
			{
				window.showModalDialog("<%=root%>/test.jsp?username=<%=j_username%>", window, 'help:no;status:no;scroll:no;dialogWidth:310px; dialogHeight:300px');
			}
			
			function openWindow(){
				var w = screen.availWidth-10;
				var h = screen.availHeight-35;
				var win = window.open("/oa/toLogin.jsp","_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", status=yes,					            menubar=no,scrollbars=no,location=no,resizable=no");
			}

			window.setTimeout("openWindow()",2000);
			
			//定时刷新工作区
			function checkmsg(){
				$.post("<%=path%>/worklist/workList!LoadToDoWork.action",{userName:"<%=j_username%>"},function(msg){
					document.getElementById("content").innerHTML=msg;
				});
			}
			
			/*function doLoginOA(){
				$.post("<%=root%>/worklist/workList!GenerateRandom.action",function(ret){
					if(ret){
						
					}
				});
			}*/
			
		</script>
  </head>
  <body scroll="auto" oncontextmenu="return false;">
  	<input type="hidden" id="j_username" value="<%=j_username%>" />
  	<input type="hidden" id="j_password" value="<%=j_password%>" />
  	<script type="text/javascript">
		checkmsg();
		setInterval("checkmsg();",60000);		
	</script>
    <div id="content" style="width:100%;height:80px;overflow-y:auto;" />
  </body>
</html>

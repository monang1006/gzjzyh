<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String rtx = session.getAttribute("rtxStart")==null?null:(String)session.getAttribute("rtxStart");
		String isPop = session.getAttribute("isPop")==null?null:(String)session.getAttribute("isPop");
	String userName = session.getAttribute("rtxLoginName")==null?null:(String)session.getAttribute("rtxLoginName");
%>
<HTML>
	<HEAD>
		<TITLE>版本</TITLE>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/popmsg/popMessage.js"></script>
		<script type="text/javascript">
			function checkmsg(){
			  	$.ajax({
			  		type:"post",
			  		dataType:"text",
			  		url:"<%=root%>/message/messageFolder!getPopMsg.action",
			  		data:"",
			  		success:function(msg){
			  			if(msg=="0"){
							
						}else{
						    var MSG1 = new CLASS_MSN_MESSAGE("message",
						                                     200,
						                                     160,
						                                     "消息提示：",
						                                     msg,
						                                     "点击进入收件箱",
						                                     null,
						                                     "<%=root%>/fileNameRedirectAction.action?toPage=message/message.jsp");
						    MSG1.speed  = 10; 
						    MSG1.step  = 5; 
						    MSG1.show();	
						}
					}
			  	});
			
			}
		</script>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<LINK href="css/toolbar.css" type=text/css rel=stylesheet>
		<style type="text/css">
			a:link,a:visited,a:hover,a:active{
			text-decoration:none;
		}
		</style>
	<BODY class=gtoolbarbodymargin>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
			<tr>
				<td width="30">
					&nbsp;
				</td>
				<td width="600">
					版本： V2.0
				</td>
				<td align="right">
					<img
						src="<%=path%>/common/frame/images/perspective_leftside/logo.jpg">
				</td>
				<td width="150" align="center">
					思创数码科技股份有限公司
				</td>
			</tr>
		</table>
		<script>
			checkmsg();
			setInterval("checkmsg();",60000);
			
			     var rtxStart = '<%=rtx%>';
			     if(rtxStart!=null && rtxStart!="null"){
			     	if(rtxStart == "yes"){
			     		$.getJSON("<%=root%>/im/iM!initLoginRtx.action",
								{userName:"<%=userName%>"},
								function(json){
									var status = json[0].status;
									if("ok" == status){
										var rtx = new ActiveXObject("RTXClient.RTXAPI");
										var objProp = rtx.GetObject("Property");
										var CMD_Name_Login = 2
										objProp.Value("RTXUserName") = json[0].userName;
										objProp.Value("LoginSessionKey")=json[0].sessionkey;
								　　 	objProp.Value("ServerAddress") = json[0].ip;
								　　 	objProp.Value("ServerPort") = json[0].port;
										
										rtx.Call(CMD_Name_Login, objProp);
									}else if("no" == status){
										alert("用户“"+json[0].userName+"”在Rtx中不存在，启动Rtx失败！");
									}else if("error" == status){
										alert("出现未知异常，启动Rtx失败。请与管理员联系。");
									}else{
										alert("不可能发生的异常！");
									}
								}
							);
			     	}
			     }
			
		</script>
	</BODY>
</HTML>
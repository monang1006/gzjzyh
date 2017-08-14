<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>

<%@ page import="rtx.RTXSvrApi"%> 
<%
	String rtx = session.getAttribute("rtxStart")==null?null:(String)session.getAttribute("rtxStart");
		String isPop = session.getAttribute("isPop")==null?null:(String)session.getAttribute("isPop");
	String userName = session.getAttribute("rtxLoginName")==null?null:(String)session.getAttribute("rtxLoginName");
		
		 RTXSvrApi rtxApi = new RTXSvrApi(); 
     String ip = rtxApi.getServerIP(); 
    String key= rtxApi.getSessionKey(userName); //这个GetSessionKey的方法就在RTX SDK下面JAVA例子RTXSvrApi.java里面有 
    System.out.println("***************************");
        System.out.println("***************************");
        System.out.println("***************************");
        System.out.println("***************************");
        System.out.println("***************************ip+++"+ip);
                System.out.println("***************************userName+++"+userName);
                                System.out.println("***************************key+++"+key);
%>
<HTML>
	<HEAD>
		<TITLE>版本</TITLE>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			function playSound(path){
				document.getElementById("new_sms").innerHTML="<object id='sms_sound' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' codebase='<%=path%>/common/flash/swflash.cab' width='0' height='0'><param name='movie' value='"+path+"'><param name=quality value=high><embed id='sms_sound' src='<%=path%>/1.swf' width='0' height='0' quality='autohigh' wmode='opaque' type='application/x-shockwave-flash' plugspace='http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash'></embed></object>";
			}
			function unloadSound(){
				var bgsnd = document.getElementById('sound');
			}
			function checkmsg(type){
				if(type=="login"){
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/message/messageFolder!getPopMsg.action?type=login",
				  		data:"",
				  		success:function(msg){
				  			if(msg=="0"){
								
							}else{
								playSound('<%=path%>/1.swf');
							    var MSG1 = new CLASS_MSN_MESSAGE("message",
							                                     200,
							                                     160,
							                                     "消息提示：",
							                                     msg,
							                                     "点击进入收件箱",
							                                     null,
							                                     "<%=root%>/fileNameRedirectAction.action?toPage=message/message.jsp");
							    MSG1.speed  = 50; 
							    MSG1.step  = 5; 
							    MSG1.show();
							}
						}
				  	});
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/message/messageFolder!getPopMsg.action?type='afterLogin'",
				  		data:"",
				  		success:function(msg){
				  			if(msg=="0"){
								
							}else{
								playSound('<%=path%>/2.swf');
							    var MSG1 = new CLASS_MSN_MESSAGE("message",
							                                     200,
							                                     160,
							                                     "消息提示：",
							                                     msg,
							                                     "点击进入收件箱",
							                                     null,
							                                     "<%=root%>/fileNameRedirectAction.action?toPage=message/message.jsp");
							    MSG1.speed  = 50; 
							    MSG1.step  = 5; 
							    MSG1.show();
							}
						}
				  	});
				 }
			}
/**			function checkmsg(){
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
			
			}*/
		</script>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<LINK href="css/toolbar.css" type=text/css rel=stylesheet>
		<style type="text/css">
			a:link,a:visited,a:hover,a:active{
			text-decoration:none;
		}
		</style>
	<BODY class=gtoolbarbodymargin>
	
		<script type="text/javascript" src="<%=path%>/common/js/popmsg/popMessage.js"></script>
	    <bgsound id="sound" loop="1" src="">
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
		function RtxSycn(){ 
    try{ 
        var key="<%=key%>";            //上面取到的sessionkey 
        var account="<%=userName%>";    //用户登陆名,当然在RTX里面也要有一个一样的用户名 
        var ip="<%=ip%>"; 

       var RTXCRoot = RTXAX.GetObject("KernalRoot");    //客户端SDK GetObject
  
        RTXCRoot.LoginSessionKey(ip,8000,account,key); 
       }catch(e){ 
       alert(e.message);
        alert("RTX未能成功登录，请重试或与管理员联系！"); 
       } 
} 
		
		
			//checkmsg('login');
			//setInterval("checkmsg('afterLogin');",120000);
			     var rtxStart = '<%=rtx%>';
			     if(rtxStart!=null && rtxStart!="null"){
			     	if(rtxStart == "yes"){
			     		$.getJSON("<%=root%>/im/iM!initLoginRtx.action",
								{userName:"<%=userName%>"},
								function(json){
									var status = json[0].status;
									if("ok" == status){
									RtxSycn();
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
			     /*else{
			     	alert("即时通讯软件未开启，请与管理员联系.");
			     }*/
			
		</script>
		<div id="new_sms"></div>
			   <OBJECT id=RTXAX data=data:application/x-oleobject;base64,fajuXg4WLUqEJ7bDM/7aTQADAAAaAAAAGgAAAA== classid=clsid:5EEEA87D-160E-4A2D-8427-B6C333FEDA4D VIEWASTEXT></OBJECT> 
		
   	</BODY>
</HTML>
<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String rtx = session.getAttribute("rtxStart")==null?null:(String)session.getAttribute("rtxStart");
		String isPop = session.getAttribute("isPop")==null?null:(String)session.getAttribute("isPop");
	String userName = session.getAttribute("rtxLoginName")==null?null:(String)session.getAttribute("rtxLoginName");
%>
<html>
  <head>
	<%@include file="/common/include/meta.jsp"%>
    <title>思创协同办公软件V2.0</title>
	<link href="<%=frameroot%>/css/yangshi.css" type="text/css" rel="stylesheet">
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
  </head>
    <SCRIPT language=javascript>
     var rtxStart = '<%=rtx%>';
      var isPop = '<%=isPop%>';
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
							window.close();
						}else if("error" == status){
							alert("出现未知异常，启动Rtx失败。请与管理员联系。");
							window.close();
						}else{
							alert("不可能发生的异常！");
							window.close();
						}
					}
				);
     	}
     }
	 var w = screen.availWidth-10;
	 var h = screen.availHeight-35;
	 if(isPop=="yes"){
		var win = window.open("<%=path%>/theme/theme!RefreshTop.action","_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", status=yes, menubar=no,scrollbars=no,location=no,resizable=no");
     	if(win != null){
		    window.opener = null;
			window.close();
	    }
	    if (win==null) {
			alert("")
		}
     }else{
     	window.location="<%=path%>/useronline/userLogin.action";
     }
    
</script>
  <body  >
    <div id="till">
    	检测到弹出窗口阻止程序。 
		要使用 StrongOA2.0，您的 Web 浏览器必须允许弹出窗口。关于允许弹出窗口的信息，请看您的弹出窗口阻止软件的说明。 
    </div>
  </body>
</html>

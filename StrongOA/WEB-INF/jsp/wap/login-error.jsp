<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@ page import="org.springframework.security.ui.rememberme.AbstractRememberMeServices" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
<title>首页</title> <meta content="m.taobao.com" name="author" /> 
<meta content="1 days" name="revisit-after" /> 
<link href="http://m.taobao.com/channel/chn/wap/index_v6.xhtml#nocheck" rel="canonical" />
<meta content="" name="keywords" /> 
<meta content="" name="description" />
<meta content="width=device-width; initial-scale=1.2; minimum-scale=1.0; maximum-scale=2.0" name="viewport" />
<link href="http://a.tbcdn.cn/mw/s/hi/tbtouch/images/touch-icon.png" rel="apple-touch-icon-precomposed" /> 
<meta content="1 days" name="revisit-after" /> 
<meta content="" name="keywords" /> 
<meta content="" name="description" />
<style type="text/css">
body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td{margin:0;padding:0;}body,button,input,select,textarea{font:12px/1.5 tahoma,arial,simsun,sans-serif;}h1,h2,h3,h4,h5,h6{font-size:100%;}address,cite,dfn,em,var{font-style:normal;}code,kbd,pre,samp{font-family:courier new,courier,monospace;}small{font-size:12px;}ul,ol,li{list-style:none;}a{text-decoration:none;}a:hover{text-decoration:underline;}sup{vertical-align:text-top;}sub{vertical-align:text-bottom;}legend{color:#000;}fieldset,img{border:0;}button,input,select,textarea{font-size:100%;}table{border-collapse:collapse;border-spacing:0;}
/* common */
html{ color: #000; background-color:#052c59; }
body{ font-family: "微软雅黑","Microsoft YaHei","Microsoft JhengHei",STHeiti,Georgia,"Times New Roman",Times,serif; font-size:14px; }
a{ color: #333; cursor:pointer; }
a:hover{ color:#000; }
.inline-block{ display: inline-block; zoom: 1; *display: inline; vertical-align: middle; }
.clearfix:after{ content: '\0020'; display: block; height: 0; clear: both; }
.clearfix{ *zoom: 1; }
.clear{ clear:both; height:0; line-height:0; font-size:0; width:100%; overflow:hidden; }
.fri{ float: right; }
.fle{ float: left; }
.cred{ color:#f00; }
a.ered{ color: #d71304; }
a.ered:hover{ color:#000; }
.tecen{ text-align:center; }
.tit{ font-size:14px; font-weight:bold; }
.dtmore{ font-size:12px; font-weight:normal; float:right; }
/* container */
#container{ position:relativee; width:540px; margin:0 auto; height:960px; background:url(<%=root%>/oa/image/login/bgimg.png) no-repeat center center; }
.login{ width:540px; margin:0 auto; height:473px; background:url(<%=root%>/oa/image/login/logbg.png) repeat-x center center; position:fixed; }
.logo{ padding-top:66px; text-align:center; }
.loginbar{ background:url(<%=root%>/oa/image/login/syhximg.png) no-repeat center 20px; padding-top:30px; }
.loginbar p{ margin:20px 0; }
p.logbts{ margin:0; height:20px; line-height:20px; text-align:center; color:#c00; }
p.logbarpa{ height:38px; padding-left:216px; background:url(<%=root%>/oa/image/login/yhmbg.png) no-repeat center top; }
p.logbarpa input{ width:194px; height:32px; line-height:32px; background:none; border:none; padding:3px; }
p.logbarpb{ height:38px; padding-left:216px; background:url(<%=root%>/oa/image/login/mimabg.png) no-repeat center top; }
p.logbarpb input{ width:194px; height:32px; line-height:32px; background:none; border:none; padding:3px; }
p.logbarpc{ padding-top:8px; padding-left:132px; margin:0; height:62px; background:url(<%=root%>/oa/image/login/logkimg.png) no-repeat center 1px; }
p.logbarpc input{ width:276px; height:40px; background:none; border:none; }
p.logbarpa1{ width:276px; padding-left:186px; height:40px; background:none; border:none; color:red }
/* nav */
.nav{ width:540px; height:137px; background:url(<%=root%>/oa/image/login/navtopbg.png) repeat-x center top; position:fixed; top:0; }
.con{ padding-top:137px; }
.dhjm{ padding:0 48px; }
.dhjm li{ overflow:hidden; float:left; width:222px; height:119px; padding-top:40px; text-align:center; }
.dhjm li a{ display:block; width:134px; height:119px; margin:0 auto; background:url(<%=root%>/oa/image/login/navlibg.png) no-repeat center center; text-align:center; }
</style>
</head>



<%
		//增加Cooike中读取用户名功能
		String username="";
		Cookie[] cookies = request.getCookies();
	 	if ((cookies != null) && (cookies.length != 0)) {
			 for (int i = 0; i < cookies.length; i++) {
	            if (AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY.equals(cookies[i].getName())) {
	            	username = cookies[i].getValue();
	            	break ;
	            }
	        }
	 	}
	%>
	<script language="javascript">
		function validlogin(){
			var loginpwd=document.getElementById("j_password").value;
			var loginuser=document.getElementById("j_username").value;
			if(loginpwd==""||loginuser==""){
				document.getElementById("showErrTip").innerHTML="用户名或密码不能为空";
				return;
			}else{
				document.getElementById("showErrTip").innerHTML="";
				document.getElementById("form1").submit();
			}
		}
	</script>


<body>
<div id="container">
  <div class="login">
    <div class="logo"><img src="<%=root%>/oa/image/login/logo.png" /></div>
    <div class="loginbar">
      <form name="form1" id="form1" method="post" action="<%=path%>/wap/login!validateLogin.action">
      <p class="logbarpa"><input type="text"  name="j_username" id="j_username" value="<%=username%>"/></p>
      <p class="logbarpb"><input type="password" name="j_password" id="j_password"/></p>
     </form>	
    </div>
      <p class="logbarpc"><input type="submit" class="submit" value="" onclick="javascript:validlogin()"/></p>
  	  <p class="logbarpa1" id="showErrTip"></p>
  </div>
</div>
</body>
</html>

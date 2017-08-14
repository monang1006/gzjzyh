<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.List,java.util.Date,org.springframework.security.userdetails.UserDetails,org.springframework.security.context.SecurityContextHolder,org.springframework.security.Authentication"%> 
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
<title>主页</title> <meta content="m.taobao.com" name="author" /> 
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
html{ color: #000; background:url(<%=root%>/oa/image/login/bgimg.png) #082c58 no-repeat center center; }
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
html,body{ height:100%; }
#container{ height:100%; }
#c{ background:url(<%=root%>/oa/image/login/bgimg.png) no-repeat center center; }
.login{ width:540px; margin:0 auto; height:473px; background:url(<%=root%>/oa/image/login/logbg.png) repeat-x center center; position:fixed; top:50%; left:50%; margin-top:-236px; margin-left:-270px; }
.logo{ padding-top:66px; text-align:center; }
.loginbar{ background:url(<%=root%>/oa/image/login/syhximg.png) no-repeat center 20px; padding-top:30px; }
.loginbar p{ margin:20px; }
p.logbarpa{ height:37px; padding-left:160px; background:url(<%=root%>/oa/image/login/yhmbg.png) no-repeat center top; }
p.logbarpa input{ width:205px; height:32px; line-height:32px; background:none; border:none; padding:3px; }
p.logbarpb{ height:37px; padding-left:160px; background:url(<%=root%>/oa/image/login/mimabg.png) no-repeat center top; }
p.logbarpb input{ width:205px; height:32px; line-height:32px; background:none; border:none; padding:3px; }
p.logbarpc{ padding-top:28px; padding-left:94px; margin:0; height:62px; background:url(<%=root%>/oa/image/login/logkimg.png) no-repeat center 21px; }
p.logbarpc input{ width:253px; height:47px; background:none; border:none; }
/* nav */
.nav{ width:100%; height:84px; background:url(<%=root%>/oa/image/login/navtopbg.png) repeat-x center top; position: fixed; top:0; }
.con{ padding-top:77px; }
.dhjm{ text-align:center; }
.dhjm td{ height:96px; padding-top:10px; }
.dhjm td a{ color:#fff; display:block; width:110px; height:90px; padding-top:6px; margin:0 auto; }
.dhjm td a img{ width:50px; }
.dhjm td a:hover{ color:#fff; text-decoration:none; }
p.dhjmpb{ height:22px; background:url(<%=root%>/oa/image/login/dhjpbg.png) no-repeat center center; }
.ntopcenter{height:36px; font-size:16px; color:#ffffff;width:100%;align:center }
.ntopbot{ padding:4px 4px 0; color:#aeb9c9; }
</style>
</head>
<%
List list=(List)request.getAttribute("list");
String userName= (String)request.getAttribute("aliasName");
if("".equals(userName)){
	Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
	UserDetails user = (UserDetails) currentUser.getPrincipal();
	userName = user.getUsername();
}

%>
<body>
<div id="container">
	
  <div class="nav">
  		<div class="ntopcenter"><img src="<%=root%>/oa/image/work/smile.png" width="25" height="25"></img>
			<u><b><%=userName%></b></u>，您好！
		</div>
  		<div class="ntopbot clearfix">
  			<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
  		</div>
  </div>
  <div class="con">
    <div class="dhjm ">
      <table width="100%">
      <%for(int i=0;i<list.size();i++){
			String[] gn=(String[])list.get(i);
			if(list.size()%3!=0&&list.size()==i+1){
		%>
      	<tr>
      		<td><a href="<%=gn[1]%>"><p><img src="<%=root%>/oa/image/work/<%=gn[2]%>" /></p><p class="dhjmpb"><%=gn[0]%></p></a></td>
            <td></td>
      	</tr>
      	<% 
			}else{
				++i;
				String[] gn1=(String[])list.get(i);
		%>
        <tr>
      		<td><a href="<%=gn[1]%>"><p><img src="<%=root%>/oa/image/work/<%=gn[2]%>" /></p><p class="dhjmpb"><%=gn[0]%></p></a></td>
            <td><a href="<%=gn1[1]%>"><p><img src="<%=root%>/oa/image/work/<%=gn1[2]%>" /></p><p class="dhjmpb"><%=gn1[0]%></p></a></td>
      	</tr>
      	<% 
			}
		}
		%>
        <%-- <tr>
      		<td><a href="#"><p><img src="resource/images/navlimg05.png" /></p><p></p></a></td>
            <td><a href="#"><p><img src="resource/images/navlimg06.png" /></p><p></p></a></td>
      	</tr>
        <tr>
      		<td><a href="#"><p><img src="resource/images/navlimg07.png" /></p><p></p></a></td>
            <td><a href="#"><p><img src="resource/images/navlimg08.png" /></p><p></p></a></td>
      	</tr>
        <tr>
      		<td><a href="#"><p><img src="resource/images/navlimg09.png" /></p><p></p></a></td>
            <td><a href="#"><p><img src="resource/images/navlimg10.png" /></p><p></p></a></td>
      	</tr>
        <tr>
      		<td><a href="#"><p><img src="resource/images/navlimg07.png" /></p><p></p></a></td>
            <td><a href="#"><p><img src="resource/images/navlimg08.png" /></p><p></p></a></td>
      	</tr>
        <tr>
      		<td><a href="#"><p><img src="resource/images/navlimg09.png" /></p><p></p></a></td>
            <td><a href="#"><p><img src="resource/images/navlimg10.png" /></p><p></p></a></td>
      	</tr>--%>
      </table>
    </div> 
  </div>
</div>
</body>
</html>

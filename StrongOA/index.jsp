<%@ page contentType="text/html; charset=utf-8" %>
<%
String path = request.getContextPath();
%>

<html>
  <head>
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <%--
    <SCRIPT language=javascript>
    if(self!=top){top.location=self.location;}
     if(parseInt(window.navigator.appVersion.split(";")[1].substr(5))<6){
      alert("请使用IE浏览器6.0以上版本");
      window.close();
    }
    //location="login.jsp";
	var w=800;//screen.availWidth-10;
	var h=550;//screen.availHeight-30;
	var win =window.open("<%=path%>/theme/theme!login.action", '', 'toolbar=0, scrollbars=0, resizable=0, width='+w+', height='+h+', top='+(screen.availHeight/2-h/2)+', left='+(screen.availWidth/2-w/2)+',location=no,status=no ');
    if(win != null){
    	window.opener = null;
		window.open('','_self');
		window.close();
    }
    if (win==null) {
		//alert("")
	}
	
</script>

 <script type="text/javascript">
		var w = screen.availWidth-10;
		var h = screen.availHeight-35;
		var win = window.open("<%=path%>/theme/theme!login.action","_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", status=yes, menubar=no,scrollbars=no,location=no,resizable=no");
		if(win != null){
	    	window.opener = null;
			window.open('','_self');
			window.close();
	    }
	    if (win==null) {
		}
	</script>
 --%>
 
 <script type="text/javascript">
 	function detectOS() {  
        var sUserAgent = navigator.userAgent;  
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");  
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");  
        if (isMac) return "Mac";  
        var isUnix = (navigator.platform == "X11") && !isWin && !isMac;  
        if (isUnix) return "Unix";  
        var isLinux = (String(navigator.platform).indexOf("Linux") > -1);  
        if (isLinux) return "Linux";  
        if (isWin) {  
            var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1;  
            if (isWin2K) return "Win2000";  
            var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 || sUserAgent.indexOf("Windows XP") > -1;  
            if (isWinXP) return "WinXP";  
            var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1;  
            if (isWin2003) return "Win2003";  
            var isWinVista= sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;  
            if (isWinVista) return "WinVista";  
            var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;  
            if (isWin7) return "Win7";  
        }  
        return "other";  
    }  
 	var browser={  
    versions:function(){  
        var u = navigator.userAgent, app = navigator.appVersion;  
        return{//移动终端浏览器版本信息   
               trident: u.indexOf('Trident') > -1,//IE内核   
               presto: u.indexOf('Presto') > -1,//opera内核   
               webKit: u.indexOf('AppleWebKit') > -1,//苹果、谷歌内核   
               gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核   
               mobile: !!u.match(/AppleWebKit.*Mobile.*/)||!!u.match(/AppleWebKit/),//是否为移动终端   
               ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),//ios终端   
              android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器   
               iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器   
               iPad: u.indexOf('iPad') > -1,//是否iPad   
               webApp: u.indexOf('Safari') == -1//是否web应该程序，没有头部与底部   
        };  
      }(),  
      language:(navigator.browserLanguage || navigator.language).toLowerCase()  
	}
	if(browser.versions.mobile && detectOS()=="other"){
		window.location="<%=path%>/waplogin.jsp";
	}else{
   <%
			String ip = request.getRemoteAddr();
			if(ip.trim().contains("14.1.0"))
			{
			%>
			  window.location="<%=path%>/indexs.shtml";
		<%
			}else
			{%>
			window.location="<%=path%>/index.shtml";
			<%}
			
		%>
  	  	//window.location="<%=path%>/theme/theme!login.action";
  	}
  	  <%--window.location="<%=path%>/theme/theme!login.action";--%>
  </script>
  <body  ONBLUR="self.close();">
    <div id="till">
    	<font color="red">检测到弹出窗口阻止程序。 
		要使用 StrongOA2.0，您的 Web 浏览器必须允许弹出窗口。关于允许弹出窗口的信息，请看您的弹出窗口阻止软件的说明。</font> 
    </div>
  </body>
</html>

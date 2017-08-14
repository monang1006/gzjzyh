<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaSystemmanageSystemLink"/>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<title>无标题文档</title>
<link href="<%=root %>/common/temp/css.css" rel="stylesheet" type="text/css" />
<link href="<%=frameroot%>/css/toolbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>

<style >
.bg {background: url(<%=path%>common/bgtdesktop/resource/images/index/top.jpg); background-repeat: no-repeat;background-position: left;}
</style>
</head>
<%
	String userName = (String) request.getSession().getAttribute(
			"userName");
	ToaSysmanageBase sysTheme = request.getSession()
		.getAttribute("sysTheme")==null?null:(ToaSysmanageBase)request.getSession()
				.getAttribute("sysTheme");
	String baseTitle="";
	String baseLogoPic="";
	int baseLogoWidth=0;
	int baseLogoHeight=0;
	if(sysTheme!=null){
		baseTitle = sysTheme.getBaseTitle();
		baseLogoPic = sysTheme.getBaseLogoPic();
		baseLogoWidth=sysTheme.getBaseLogoWidth()==null?0:sysTheme.getBaseLogoWidth();
		baseLogoHeight=sysTheme.getBaseLogoHeight()==null?0:sysTheme.getBaseLogoHeight();
	}
%>
<body>
	<div class="top"><img src="<%=path%>/common/bgtdesktop/resource/images/index/top.jpg" width="100%" height="201" /></div>
	<script type="text/javascript">
		$(document).ready(function(){
			var obj=document.getElementById("logos");	//顶部图标对象
			if(obj!=null&&obj!=undefined){
				obj.align="middle";
				var imageWidth="<%=baseLogoWidth%>"; 		//顶部图标高度
				var imageHeight="<%=baseLogoHeight%>";	//顶部图标高度
				if(imageWidth!=null&&imageWidth!=""&&imageWidth!="0"){
					obj.width=imageWidth;
				}
				if(imageHeight!=null&&imageHeight!=""&&imageHeight!="0"){
						obj.height=imageHeight;
				}
			}	
		});	
		
		function showCompanyDetail(){
			window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
		}
		function changePassword(){
			var rValue= OpenWindow("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", '350', '150', window);
		}

		function gotologin(){
			window.top.location="<%=path%>/j_spring_security_logout";
		}

		function jumpMenu(url){
		  var xx="<%=path%>/theme/theme!jump.action?url="+url;
		  window.parent.location=xx;
		}
	</script>
</body>
</html>
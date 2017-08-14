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

<script language="JavaScript">   //该代码在IE6下执行，png透明   
function correctPNG() // correctly handle PNG transparency in Win IE 5.5 & 6.     
{     
var arVersion = navigator.appVersion.split("MSIE")     
var version = parseFloat(arVersion[1])     
if ((version >= 5.5) && (document.body.filters))     
{     
for(var i=0; i<document.images.length; i++)     
{     
var img = document.images[i]     
var imgName = img.src.toUpperCase()     
if (imgName.substring(imgName.length-3, imgName.length) == "PNG")     
{     
var imgID = (img.id) ? "id='" + img.id + "' " : ""     
var imgClass = (img.className) ? "class='" + img.className + "' " : ""     
var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "     
var imgStyle = "display:inline-block;" + img.style.cssText     
if (img.align == "left") imgStyle = "float:left;" + imgStyle     
if (img.align == "right") imgStyle = "float:right;" + imgStyle     
if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle     
var strNewHTML = "<span " + imgID + imgClass + imgTitle     
+ " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"     
+ "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"     
+ "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>"     
img.outerHTML = strNewHTML     
i = i-1     
}     
}     
}     
}     
window.attachEvent("onload", correctPNG);     
</script>     


<style >
body {background:url(<%=path%>/common/bgtdesktop/resource/images/index/dtopbg.jpg) repeat-x left top; }
.dtoptab{ background:url(<%=path%>/common/bgtdesktop/resource/images/index/top.jpg) no-repeat right top; height:120px; }
.dtoptdtop{ height:22px; line-height:19px; text-align:right; }
.dtoptdtop span{ display:inline-block; height:19px; padding:0 16px; color:#fff; font-size:20px; font-weight:600; }
.dtoptdtop a{ color:#fff; font-size:20px;}
.dtoptdtop a:hover{ color:#fff;font-size:20px; }
.dtoptda,.dtoptdb,.dtoptdc{ width:76px; padding-top:7px; }
.dtoptda a,.dtoptdb a,.dtoptdc a{ display:block; width:76px; height:68px; text-indent:-2011em; }
.dtoptdc{ padding-right:22px; }
.dtoptda a{ background:url(<%=path%>/common/bgtdesktop/resource/images/index/zxbz01.jpg) no-repeat center center; }
.dtoptdb a{ background:url(<%=path%>/common/bgtdesktop/resource/images/index/xgmm01.jpg) no-repeat center center; }
.dtoptdc a{ background:url(<%=path%>/common/bgtdesktop/resource/images/index/tcxt01.jpg) no-repeat center center; }
.toppic{ margin-top:40px; margin-left:80px;}
</style>

	<script type="text/javascript">
		function changePassword(){
			var rValue= showModalDialog("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", 
		                                window,"dialogWidth:350pt;dialogHeight:150pt;status:no;help:no;scroll:no;");	
		}
		function gotoExit(){
			top.location="<%=path%>/j_spring_security_logout";
		}
		
		function alertHelpPage(){
			var sindex = getSysConsole().getSelectedIndex();
			var tabID = getSysConsole().getTabParam("ID",sindex);
			var url = getSysConsole().getTabObj(tabID,sindex);
			url=""+url;
			//alert("url:"+url);
			var arr = url.split("privilSyscode=");
			ihelpTreeId = arr[1];
			//alert("ihelpTreeId:"+ihelpTreeId);
			ihelpTreeId=""+ihelpTreeId;
			if(ihelpTreeId!="undefined"){
				var rValue= showModalDialog("<%=basePath%>/helpedit/helpedit!gethelpTreeId.action?ihelpTreeId="+ihelpTreeId, 
			                               window,"dialogWidth:750pt;dialogHeight:450pt;status:no;help:no;scroll:no;");
			}else{
				var rValue= showModalDialog("<%=basePath%>/helpedit/helpedit!gethelpTreeId.action?url="+url, 
                        window,"dialogWidth:750pt;dialogHeight:450pt;status:no;help:no;scroll:no;");
			}
		}
		</script>
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
<div class="top">
<table class="dtoptab" width="100%">
  <tr>
    <td valign="middle" class="dtoptdtop" colspan="4"><span style="background-color:#40a2ce;">欢迎您！<a href="#"><%=userName %></a></span></td>
  </tr>
  <tr>
    <td valign="top" style="padding-left:24px;"><img src="<%=path%>/common/bgtdesktop/resource/images/index/name.png"/></td>
    <td valign="top" class="dtoptda"><a title="在线帮助" href="#" onclick="alertHelpPage()">在线帮助（测试版）</a></td>
    <td valign="top" class="dtoptdb"><a title="修改密码" href="#" onclick="changePassword()">修改密码</a></td>
    <td valign="top" class="dtoptdc"><a title="退出系统" href="#" onclick="gotoExit()">退出系统</a></td>
  </tr>
</table>
</div>
 
<iFRAME name=personal_properties_content marginWidth=0 marginHeight=0  height="0" style="display:none"
			src="<%=path%>//fileNameRedirectAction.action?toPage=frame/MyLinuxbottom.jsp" frameBorder=0
			noResize scrolling=no>
</iFRAME>
</body>
</html>
<%@ page language="java" import="java.util.*"%>
<%
String sysCode = "001";
String path = request.getContextPath();
if(path.endsWith("/")) path="";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String root = path;
//String frameroot = path+"/frame/theme_gray";
String jsroot = path+"/common/js";
String frameroot= (String) request.getSession().getAttribute("frameroot"); 
if(frameroot==null||frameroot.equals("")||frameroot.equals("null"))
 	frameroot= "/frame/theme_gray";
String imgpath = path + "/images" + frameroot.substring(6);
String csspath = path + "/css" + frameroot.substring(6);

String version= (String) request.getSession().getAttribute("version"); 

String themePath = path + "/theme/theme_default";
String cssPath = themePath + "/css";
String imgPath = themePath + "/image";
String scriptPath = path + "/common/script";

frameroot=themePath;

request.setAttribute("themePath", themePath);
request.setAttribute("cssPath", cssPath);
request.setAttribute("imgPath", imgPath);
request.setAttribute("scriptPath", scriptPath);
request.setAttribute("root", root);
request.setAttribute("path", path);
%>
<script type="text/javascript">
  var scriptroot="<%=root%>"
  var frameroot="<%=frameroot%>";
  var jsroot="<%=jsroot%>";
  var imgpath="<%=imgpath%>";
  var csspath="<%=csspath%>";
  
  var path = "<%=path%>";
  var root = "<%=root%>";
  var themePath = "<%=themePath%>";
  var cssPath = "<%=cssPath%>";
  var imgPath = "<%=imgPath%>";
  var scriptPath = "<%=scriptPath%>";
</script>
<!-- ç»ä¸ãalert,confirmãé£æ ¼ add by dengzc -->
<!--<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>-->
<!--<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>-->

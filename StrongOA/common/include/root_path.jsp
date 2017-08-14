<%@ page language="java" import="java.util.*"%>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<%
String path = request.getContextPath();
if(path.endsWith("/")) path="";
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String httpPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String root = path;
//String frameroot = path+"/frame/theme_gray";
String jsroot = path+"/common/js";
String frameroot= (String) request.getSession().getAttribute("frameroot"); 
if(frameroot==null||frameroot.equals("")||frameroot.equals("null"))
 	frameroot= "/frame/theme_gray";
frameroot=path+frameroot;

String themePath = path + "/theme/theme_default";


%>
<script type="text/javascript">
  var scriptroot="<%=root%>";
  var contextPath="<%=root%>";
  var frameroot="<%=frameroot%>";
  var jsroot="<%=jsroot%>";
  var basePath = "<%=basePath%>";
  var httpPath = "<%=httpPath%>";
  var themePath = "<%=themePath%>";
  
</script>
<!-- 统一【alert,confirm】风格 add by dengzc -->
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<script type="text/javascript" src="<%=path%>/common/js/common/common.js"></script>


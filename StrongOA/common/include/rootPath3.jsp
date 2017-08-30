<%@ page language="java" import="java.util.*"%>
<jsp:directive.page import="com.strongit.oa.common.user.util.PrivilHelper"/>
<jsp:directive.page import="com.strongit.uums.bo.TUumsBasePrivil"/>
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

String version= (String) request.getSession().getAttribute("version"); 
String privilName = request.getParameter("privilSyscode");
if(privilName != null && privilName.length() > 0){
	TUumsBasePrivil privil = PrivilHelper.getPrivil("001-" + privilName);
	if(privil != null){
		privilName = privil.getPrivilName();
	} else {
		privilName = "";
	}
} else {
	privilName = "";
}
%>
<script type="text/javascript">
  var scriptroot="<%=root%>"
  var contextPath="<%=root%>"
  var frameroot="<%=frameroot%>";
  var jsroot="<%=jsroot%>";
  var basePath = "<%=basePath%>";
  var httpPath = "<%=httpPath%>";
  var themePath = "<%=themePath%>";
  
  var ssxs = 0;//showsize 系数
  if(frameroot=="/oa/frame/theme_blue_12"||
  	 frameroot=="/oa/frame/theme_red_12"||
  	 frameroot=="/oa/frame/theme_gray_12"||
  	 frameroot=="/oa/frame/theme_green_12"){
  	ssxs = 0.5;
  }else{
  	sxss = 0;
  }
</script>
<!-- 统一【alert,confirm】风格 add by dengzc -->
<script type="text/javascript" src="<%=path%>/common/js/common/common.js"></script>


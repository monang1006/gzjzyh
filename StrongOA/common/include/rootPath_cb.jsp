<!DOCTYPE html>
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
String frameroot1= (String) request.getSession().getAttribute("frameroot"); 
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

if(frameroot1==null||frameroot1.equals("")||frameroot1.equals("null"))
 	frameroot1= "/frame/theme_gray";
frameroot1=path+frameroot1;
request.setAttribute("themePath", themePath);
request.setAttribute("cssPath", cssPath);
request.setAttribute("imgPath", imgPath);
request.setAttribute("scriptPath", scriptPath);
request.setAttribute("root", root);
request.setAttribute("path", path);
%>
<script type="text/javascript" src="<%=scriptPath%>/jquery-1.4.1.min.js"></script>

<script type="text/javascript">
  var scriptroot="<%=root%>";
  var frameroot="<%=frameroot%>";
   var frameroot1="<%=frameroot%>";
  var jsroot="<%=jsroot%>";
  var imgpath="<%=imgpath%>";
  var csspath="<%=csspath%>";
  
  var path = "<%=path%>";
  var root = "<%=root%>";
  var themePath = "<%=themePath%>";
  var cssPath = "<%=cssPath%>";
  var imgPath = "<%=imgPath%>";
  var scriptPath = "<%=scriptPath%>";
  
$(document).ready(function(){
     $("#img_sousuo").mouseover(function () { this.src='<%=imgPath%>/sousuo_onfocus.gif'; } );
	 $("#img_sousuo").mouseout(function () { this.src='<%=imgPath%>/sousuo.gif'; } );
     $(".input_button_1").mouseover(function () { this.className='input_button_1_onfocus'; } );
	 $(".input_button_1").mouseout(function () { this.className='input_button_1'; } );	 
	 $(".information_list_choose_button2").mouseover(function () { this.className='information_list_choose_button2_onfocus'; } );
	 $(".information_list_choose_button2").mouseout(function () { this.className='information_list_choose_button2'; } );	
     $(".information_list_choose_button4").mouseover(function () { this.className='information_list_choose_button4_onfocus'; } );
	 $(".information_list_choose_button4").mouseout(function () { this.className='information_list_choose_button4'; } );
     $(".information_list_choose_button6").mouseover(function () { this.className='information_list_choose_button6_onfocus'; } );
	 $(".information_list_choose_button6").mouseout(function () { this.className='information_list_choose_button6'; } );
     $(".information_list_choose_button8").mouseover(function () { this.className='information_list_choose_button8_onfocus'; } );
	 $(".information_list_choose_button8").mouseout(function () { this.className='information_list_choose_button8'; } );
     $(".information_list_choose_button9").mouseover(function () { this.className='information_list_choose_button9_onfocus'; } );
	 $(".information_list_choose_button9").mouseout(function () { this.className='information_list_choose_button9'; } );
     $(".input_button_4").mouseover(function () { this.className='input_button_4_onfocus'; } );
	 $(".input_button_4").mouseout(function () { this.className='input_button_4'; } );	 
     $(".input_button_5").mouseover(function () { this.className='input_button_5_onfocus'; } );
	 $(".input_button_5").mouseout(function () { this.className='input_button_5'; } );	 
     $(".input_button_6").mouseover(function () { this.className='input_button_6_onfocus'; } );
	 $(".input_button_6").mouseout(function () { this.className='input_button_6'; } );
     $(".input_button_8").mouseover(function () { this.className='input_button_8_onfocus'; } );
	 $(".input_button_8").mouseout(function () { this.className='input_button_8'; } );

	 $(".information_list_choose_button4_1").mouseover(function () { this.className='information_list_choose_button4_1_onfocus'; } );
	 $(".information_list_choose_button4_1").mouseout(function () { this.className='information_list_choose_button4_1'; } );	 
 });
</script>

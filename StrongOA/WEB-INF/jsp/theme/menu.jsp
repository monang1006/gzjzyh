<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>

<html>
<head>
<title>accor.jsp</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/jqueryui.css">

<style type="text/css">
html, body {
font-size:12px;	
}


ul, li {
margin:0;padding:0;list-style-type:none;
}
li{
border-left:1px solid #ccc;
border-bottom:1px dashed #ccc;
border-right:1px solid #ccc;
line-height:30px;
cursor:pointer;
overflow:hidden;
}
a {
display:block;
}
a:hover {
background-color:#efefef;
}
</style>

<script type="text/javascript" src="<%=scriptPath %>/jquery-1.4.1.min.js"></script>
<script type="text/javascript" src="<%=scriptPath %>/jqueryui.js"></script>

		<script type="text/javascript">
function redirect(url,name)
{
 // parent.document.getElementById("main").src = url;
  //alert(url);
   parent.v_top.add({id:'tab_'+name, text:name, closeAble:true, url:url});
  //  var side2 = parent.document.getElementById("tabBodyNav_left");
  //  side2.style.cssText="position: absolute;display:none";
  //  var main = parent.document.getElementById("tabHeaderMain");
  //  main.style.cssText = "margin-left:36px";
    
}
</script>
</head>

<body>

<div style="width:400px; height:500px; border:1px solid #fff; float:left;">
<web:verticalnavigation id="accor1" nodes='${list}' fillSpace="true"/>
</div>



</body>
</html>

<script type="text/javascript">
function resize() {
	$('#accor1').accordion("resize");
}
</script>
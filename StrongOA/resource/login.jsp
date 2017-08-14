<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>

<% 		
String userName = "";
if(request.getSession().getAttribute("userName") != null){				
	userName = request.getSession().getAttribute("userName").toString();				
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<link type="text/css" href="<%=path%>/css/main.css" rel="stylesheet" />
<script language="javascript" src="<%=path%>/resource/jquery.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>login</title>

<script language="javascript">

var userName = "";
var flag = "false";
$(document).ready(function() {
	userName = "<%=userName%>";
	if(userName == null || userName == ""){
		$("#login").show();
		$("#logout").hide();
	}else{
		$("#login").hide();
		$("#logout").show();	
	}


});

function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function login(){
	var j_username = document.getElementById("j_username").value;
	var j_password = document.getElementById("j_password").value;
	j_username = trim(j_username);
	j_password = trim(j_password);
	if(j_username ==null || j_username==""){
		alert("请输入用户名！")
		document.getElementById("j_username").value="";
		return false;
	}
	if(j_password ==null || j_password==""){
		alert("请输入用户密码！")
		return false;
	}
	flag = "true";
	return true;

}

var refresh = setInterval('myrefresh()',4000); //指定3秒刷新一次

function myrefresh(){
	if(flag == "true"){
		window.parent.location.reload();
		clearInterval(refresh);
	}
}



function gotoOA(){
	$.post("<%=root%>/theme/theme!checkLogin.action",
	{},
	function(data){
		if(data == "false"){
			alert("此次会话已经结束,请重新登录!");
			//window.parent.location.reload();
		}else{		
			document.getElementById("form1").setAttribute("action","<%=path%>/theme/theme!RefreshTop.action") 
			document.getElementById("form1").setAttribute("target","_blank");
			form1.submit(); 		
		}
	});
}
	
function logout(){

	window.parent.location = "<%=path%>/j_spring_security_logout";

}


	
window.alert=function (txt){
	window.showModalDialog("<%=path%>/message.html",txt,
		"dialogWidth=500px;dialogHeight=200px;resizable=yes;status=no;help=no");

}
</script>
</head>
<body>

	<div class="clogin" id="login" style="display:block">
		<form action="<%=path%>/j_spring_security_check" target="_blank" onSubmit="return login();">
			<p class="cgn01"><input type="text" id="j_username" name="j_username" /></p>
			<p class="cgn02"><input type="password" id="j_password" name="j_password"/></p>
			<p class="cgn03"><input type="image" src="<%=path%>/resource/images/dzfnwh/dzfnw02.gif" width="188" height="30" /></p>
			<p class="cgn04"><a href="<%=path%>/install.jsp" target="_blank"><img align="absmiddle" src="<%=path%>/resource/images/dzfnwh/dcjxz.gif" /></a></p>
		</form>
	</div>
	
	<div class="clogin2" id="logout" style="display:none">
		
        	<p class="clogp2" align="center">欢迎您：<%=userName %></p>
        	<form id="form1" action="">
        	<p class="cgn03">
        		<input onclick="gotoOA()" type="image" src="<%=path%>/resource/images/dzfnwh/dzfnw021.gif" width="82" height="30" /> 
        		<input onclick="logout()" type="image" src="<%=path%>/resource/images/dzfnwh/dzfnw022.gif" width="82" height="30" />
        	</p>
			<p class="cgn04"><a href="<%=path%>/install.jsp" target="_blank"><img align="absmiddle" src="<%=path%>/resource/images/dzfnwh/dcjxz.gif" /></a></p>
        	</form>	
        
      </div>


</body>
</html>

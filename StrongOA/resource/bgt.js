var flag = "false";
var userName = "";
var contextPath = "";
$(document).ready(function() {	
	userName = document.getElementById("userName").value;
	contextPath = document.getElementById("contextPath").value;
	if(userName == null || userName == ""){
		$("#login").show();
		$("#logout").hide();
	}else{
		$("#login").hide();
		$("#logout").show();	
	}
});

$(function(){
var Cont = $(".contmlist03 dt b");
	Cont.mouseover(function(){
	var i = Cont.index(this);
	if( i==0 ){ $(this).parent().removeClass("contndt03"); }
	if( i==1 ){ $(this).parent().addClass("contndt03"); }
	$(".contmlist03 dd ul").eq(i).show().siblings("ul").hide();
	var Mdlhref = $(this).find("a").attr("href");
	$(".contmlist03 dt span a").attr("href",Mdlhref);
	});	   
});
  
$(function(){
	var Cont = $(".contmlist04 dt b");
	Cont.mouseover(function(){
	var i = Cont.index(this);
	if( i==0 ){ $(this).parent().removeClass("contndt04"); }
	if( i==1 ){ $(this).parent().addClass("contndt04"); }
	$(".contmlist04 dd>div").eq(i).show().siblings("div").hide();
	var Mdlhref = $(this).find("a").attr("href");
	$(".contmlist04 dt span a").attr("href",Mdlhref);
	});	   
});
  
$(function(){
	var Mdate = new Date();
    var Mnow = "";
    Mnow = Mdate.getFullYear()+"年"; 
    Mnow = Mnow + (Mdate.getMonth()+1)+"月"; 
    Mnow = Mnow + Mdate.getDate()+"日";
    var Week = ['日','一','二','三','四','五','六'];
    Mnow = Mnow + " 星期" + Week[Mdate.getDay()];
    $("#weltime").text(Mnow); 
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
		document.getElementById('j_username').focus();
		return false;
	}
	if(j_password ==null || j_password==""){
		alert("请输入用户密码！")
		document.getElementById('j_password').focus();
		return false;
	}
	 
// 	var isPop="yes";
//	$.post(contextPath+"/im/iM!checkImStart.action",
//		  {rtxStart:"yes",isPop:isPop,userName:$("#j_username").val(),password:$("#j_password").val()},
//		  function(data){
//		  	if(data == "ok"){
//		  		document.getElementById("form1").submit();	
//		  	}else{
//		  		alert("对不起,出现未知异常!");
//		  	}
//		  });//将是否启动rtx的表示写入session中
 	flag = "true";
	return true;

}

var refresh = setInterval('myrefresh()',5000); //指定5秒刷新一次

function myrefresh(){
	if(flag == "true"){
		window.location.reload();      
		clearInterval(refresh);
	}
}

function gotoOA(){
	$.post(contextPath+"/theme/theme!checkLogin.action",
	{},
	function(data){
		if(data == "false"){
			alert("此次会话已经结束,请重新登录!");
			//window.parent.location.reload();
		}else{		
			document.getElementById("form1").setAttribute("action",contextPath+"/theme/theme!RefreshTop.action") 
			document.getElementById("form1").setAttribute("target","_blank");
			form1.submit(); 		
		}
	});
}
	
function logout(){
	window.location.href = contextPath+"/j_spring_security_logout";
}

window.alert=function (txt){
	window.showModalDialog(contextPath+"/message.html",txt,
		"dialogWidth=500px;dialogHeight=200px;resizable=yes;status=no;help=no");
}

function showInfo(){
	window.showModalDialog(contextPath+"/baomi.html","",
		"dialogWidth=629px;dialogHeight=646px;resizable=yes;status=no;help=no;location=no;scrollbars=no");
}
	
/**
 * 登陆模板事件集合
 * BY:刘皙 2012年4月13日11:09:10
 */
var userName = "";
var contextPath = "";



var Cookies = {};

Cookies.set = function(name, value) {

    var Days = 3;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    var expires = exp;
    var argv = arguments;
    var argc = arguments.length;
    var path = (argc > 3) ? argv[3] : '/';
    var domain = (argc > 4) ? argv[4] : null;
    var secure = (argc > 5) ? argv[5] : false;
    document.cookie = name + "=" + escape(value) +
           ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
           ((path == null) ? "" : ("; path=" + path)) +
           ((domain == null) ? "" : ("; domain=" + domain)) +
           ((secure == true) ? "; secure" : "");
};

Cookies.get = function(name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    var j = 0;
    while (i < clen) {
        j = i + alen;
        if (document.cookie.substring(i, j) == arg)
            return Cookies.getCookieVal(j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0)
            break;
    }
    return null;
};

Cookies.clear = function(name) {
    if (Cookies.get(name)) {
        var expdate = new Date();
        expdate.setTime(expdate.getTime() - (86400 * 1000 * 1));
        Cookies.set(name, "", expdate);
    }
};

Cookies.getCookieVal = function(offset) {
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) {
        endstr = document.cookie.length;
    }
    return unescape(document.cookie.substring(offset, endstr));
};


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
	var preLoginName =  Cookies.get('loginName');
	if(typeof preLoginName != 'undefined' && preLoginName != null  ){
		$("#j_username").val(preLoginName);
	}
	if(document.getElementById("logintype").value=="CAkey"){
		document.getElementById("j_username").disabled = true;  
		document.getElementById("j_password").disabled = true;
		document.getElementById("logintype").disabled = true;
	}
	
});

/**
 * 初始化光标登陆用户名和密码
 */
function initPage(){
	var j_username = document.getElementById("j_username").value;
	if(j_username != null && j_username != ""){
		document.getElementById("j_password").focus();
	}else{
		document.getElementById('j_username').focus();
	}
}

function readUSBKEY(){
	   var rtn = ePass.GetSN();
	   if (rtn == "8001")
	   {
	   alert ("您还没有安装驱动");
	   }
	   else if (rtn == "8002")
	   {
	   alert ("您还没有插入key");
	   }
	   else if ( rtn == "8003")
	   {
	   alert ("您插入了多把key，请只插入一把key");
	   }
	   else {
	    document.getElementById("usbkey").value=rtn;
	    return true;	
	   }
	   return false;	
	}

function login(){ //登陆判断
	var w = screen.availWidth-10;
	var h = screen.availHeight-55;
	var j_username = document.getElementById("j_username").value;
	var j_password = document.getElementById("j_password").value;
	if(document.getElementById("logintype").value=="CAkey"){
		changeActionURL(document.getElementById("logintype"));
		return ;
	}
	if(document.getElementById("logintype").value=="usbkey"){
		if(readUSBKEY()){
			document.getElementById("loginForm").submit();	
			return;
		}
	}
	var j_username = document.getElementById("j_username").value;
	var j_password = document.getElementById("j_password").value;
	j_username = trim(j_username);
	j_password = trim(j_password);
	if(j_username ==null || j_username==""){
		alert("请输入用户名。")
		document.getElementById("j_username").value="";
		document.getElementById('j_username').focus();
		return false;
	}
	if(j_password ==null || j_password==""){
		alert("请输入用户密码。")
		document.getElementById('j_password').focus();
		return false;
	}
	var isPop="no";
		if(document.getElementById("isPop")){
				if(document.getElementById("isPop").value=="true"){
					isPop="yes";
				}
				$.post(contextPath+"/im/iM!checkImStart.action",
					  {rtxStart:"no",isPop:isPop,userName:$("#j_username").val(),password:$("#j_password").val()},
					  function(data){
					  	if(data == "ok"){
					  		Cookies.set('loginName',$("#j_username").val());
					  		document.getElementById("loginForm").submit();	
					  		
					  	}else{
					  		alert("对不起,出现未知异常!");
					  	}
					  });//将是否启动rtx的表示写入session中
				document.getElementById("loginForm").submit();	
		}
	
}

//验证控件是否可见
function isVisible(obj){
   if(obj==null) return false;
   return obj.offsetWidth>0&&obj.offsetHeight>0;
}

function showCompanyDetail(){
	window.showModalDialog(contextPath+"/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
}
function changeCaptcha(obj){
	var d = new Date();
	obj.src = contextPath+"/captcha/captchaImage.action?d=" + d;
}
/**
* 更换验证码，解决IE7下无法更新验证码问题
*/
function changeCaptcha(obj){
	var d = new Date();
	obj.src = contextPath+"/captcha/captchaImage.action?d=" + d;
}

/**
 * 根据是用户名验证还是USBKEY验证改变form提交路径
 */
function changeActionURL(selectObj) {
  var flag = selectObj.value;
	if(flag == 'username'){
	document.getElementById("j_username").disabled = false;
	document.getElementById("j_password").disabled = false;
		document.getElementById("loginForm").action = contextPath+"/j_spring_security_check";
	}else if(flag == 'usbkey') {
		document.getElementById("j_username").disabled = true;
		document.getElementById("j_password").disabled = true;
		document.getElementById("loginForm").action = contextPath+"/j_spring_security_usbkey_check";
		login();
	}else if(flag == 'CAkey'){
		document.getElementById("j_username").disabled = true;
		document.getElementById("j_password").disabled = true;
		/*var Auth_Content = "${original_data}";
		var DSign_Subject = document.getElementById("RootCADN").value;
		//alert("Auth_Content:"+Auth_Content+"\n"+DSign_Subject);
		if(Auth_Content==""){
			alert("认证原文不能为空!");
		}else{
			//控制证书为一个时，不弹出证书选择框
			JITDSignOcx.SetCertChooseType(1);
			JITDSignOcx.SetCert("SC","","","",DSign_Subject,"");
			var code = JITDSignOcx.GetErrorCode();
			if(code!=0){
				if(code != 5102){
					alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
				}
				return false;
			}else {
				 var temp_DSign_Result = JITDSignOcx.DetachSignStr("",Auth_Content);
				 //alert("//JITDSignOcx.GetErrorCode()==0\ntemp_DSign_Result:"+temp_DSign_Result);
				 if(code!=0){
				 	if(code != 5102){
						alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
					}
					return false;
				 }
			//如果Get请求，需要放开下面注释部分
			//	 while(temp_DSign_Result.indexOf('+')!=-1) {
			//		 temp_DSign_Result=temp_DSign_Result.replace("+","%2B");
			//	 }
				 document.getElementById("signed_data").value = temp_DSign_Result;
			}
		} */	
		//海泰方圆CA认证
		var Auth_Content = "${original_data}";
		var DSign_Subject = document.getElementById("RootCADN").value;
		if(Auth_Content==""){
			alert("认证原文不能为空!");
		}else{
	 
			var InitParam = "<?xml version=\"1.0\" encoding=\"gb2312\"?><authinfo><liblist><lib type=\"CSP\" version=\"1.0\" dllname=\"\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"SERfR01DQUlTLmRsbA==\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"U2h1dHRsZUNzcDExXzMwMDBHTS5kbGw=\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib><lib type=\"SKF\" version=\"1.1\" dllname=\"U0tGQVBJLmRsbA==\" ><algid val=\"SHA1\" sm2_hashalg=\"sm3\"/></lib></liblist></authinfo>";
	    JITDSignOcx.Initialize(InitParam);
	    if (JITDSignOcx.GetErrorCode() != 0) {
	    		alert("初始化失败，错误码："+JITDSignOcx.GetErrorCode()+" 错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
	        JITDSignOcx.Finalize();
	        return false;
	    }
			//控制证书为一个时，不弹出证书选择框
			JITDSignOcx.SetCertChooseType(1);
			JITDSignOcx.SetCert("SC","","","",DSign_Subject,"");
			if(JITDSignOcx.GetErrorCode()!=0){
				alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
				JITDSignOcx.Finalize();
				return false;
			}else {
				 var temp_DSign_Result = JITDSignOcx.DetachSignStr("",Auth_Content);
				 if(JITDSignOcx.GetErrorCode()!=0){
						alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
						JITDSignOcx.Finalize();
	 
						return false;
				 }
				 JITDSignOcx.Finalize();				
			//如果Get请求，需要放开下面注释部分
			//	 while(temp_DSign_Result.indexOf('+')!=-1) {
			//		 temp_DSign_Result=temp_DSign_Result.replace("+","%2B");
			//	 }
				document.getElementById("signed_data").value = temp_DSign_Result;
			}
		}
		document.getElementById("loginForm").action = contextPath+"/j_spring_security_ca_check";
		document.getElementById("original_jsp").value = Auth_Content;
		document.getElementById("loginForm").submit();
	}else if(flag == 'username'){
		document.getElementById("j_username").disabled = false;
		document.getElementById("j_password").disabled = false;
		document.getElementById("j_username").value = "anonymous";
		document.getElementById("j_password").value = "888888";
		document.getElementById("loginForm").action = contextPath+"/j_spring_security_check";
	}
}
function install(){
	window.open(contextPath+'/install.jsp','a');
}

function help(){
	window.open(contextPath+'/help.jsp','a');
}

window.alert=function (txt){
	window.showModalDialog(contextPath+"/message.jsp",txt,
		"dialogWidth=500px;dialogHeight=200px;resizable=yes;status=no;help=no");
}
function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

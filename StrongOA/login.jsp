<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>
		思创数码科技股份有限公司协同办公软件
		</title>
		<link href="<%=frameroot%>/css/yangshi.css" type="text/css" rel="stylesheet">
		<!--[if lt IE 7]>
 		<style type="text/css">
 			.logo img {behavior: url(<%=frameroot%>/images/perspective_toolbar/iepngfix.htc) }
 		</style>
		<![endif]-->
		<script language=javascript>
	function denglu(){
	    var w = screen.availWidth-10;
	    var h = screen.availHeight-35;
<%--	var h = screen.availHeight-55;--%>
<%--	var win = window.open("oa.jsp","_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", menubar=no,scrollbars=no,location=no,resizable=no");--%>
		var j_username = document.getElementById("j_username").value;
		var j_password = document.getElementById("j_password").value;
		if(j_username==""||j_password==""){
		 	alert("请输入用户名密码");
		 	return;
		 }
		 if(document.getElementById("j_captcha_response").value == ""){
			alert('请输入验证码！');
			return;
		}
		 //var j_username = document.getElementById("j_username").value;
		 //var j_password = document.getElementById("j_password").value;
		 //var j_captcha_response = document.getElementById("j_captcha_response").value;
		//var win = window.open("<%=path%>/j_spring_security_check?j_username="+j_username+"&j_password="+j_password+"&j_captcha_response="+j_captcha_response,"_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", status=yes, menubar=no,scrollbars=no,location=no,resizable=no");
		//if(win != null){
		//	window.close();
		//}
		
		document.getElementById("form1").submit();
		//window.close();
	}
	function showCompanyDetail(){
		window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
	}
	function changeCaptcha(obj){
		var d = new Date();
		obj.src = "<%=path %>/captcha/captchaImage.action?d=" + d;
	}
</script>
	</head>
	<body ONLOAD="window.document.forms[0].j_username.focus();">
		<div id="outer">
			<div id="middle">
				<div class="login">
					<p class="logo">
					<%
						String rootstr = path+"/frame/theme_blue";
						if(rootstr.equals(frameroot)){
					%>
						<s:if test="modle.baseLogoPic!=\"0\"&&modle.baseLogoPic!=\"\"&&modle.baseLogoPic!=null&&modle.baseLogoPic!=\"null\"">
        			<img id="logos" src="<%=frameroot%>/images/perspective_toolbar/<s:property value='modle.baseLogoPic'/>" width="32" height="39" />
								</s:if>
					<%
						}
					%>
						<s:if test="modle.baseTitle!=null &&modle.baseTitle.length()>0">
							${modle.baseTitle}
						</s:if>
						<s:else>
							思创数码科技股份有限公司协同办公软件
						</s:else>
					</p>
					<form id="form1" action="<%=path%>/j_spring_security_check" method="POST">
						<ul>
							<li>
								<label>
									用户名
									<input type="text" name="j_username" id="j_username" tabindex="1" class="inp" onkeypress="if(event.keyCode==13) document.all.j_password.focus();"/>
								</label>
							</li>
							<li>
								<label>
									密&nbsp;&nbsp;&nbsp;&nbsp;码
									<input type="password" name="j_password" id="j_password" tabindex="2" class="inp" onkeypress="if(event.keyCode==13) document.all.j_captcha_response.focus();"/>
								</label>
							</li>
							<li>
								<label>
									验证码
									<input type="text" name="j_captcha_response" id="j_captcha_response" tabindex="3" class="yz" onkeypress="if(event.keyCode==13) denglu();"/>
									<img src='<%=path%>/captcha/captchaImage.action' onclick='changeCaptcha(this);' alt="看不清?换一张" style="cursor: hand;">
								</label>
							</li>
							<li>
								<label>
									<input name="radiobutton" type="radio" value="radiobutton" checked="checked" tabindex="4" />
									普通登录
								</label>
								<label>
									<input type="radio" name="radiobutton" value="radiobutton" tabindex="5" />
									插入USBKEY
								</label>
							</li>
							<li>
								<input type="button" name="button" id="button" value="登　录" tabindex="6" class="btn" onclick="denglu()" />
								<span class="forgetpass"><a href="#">忘记密码？</a></span>
							</li>
						</ul>
					</form>
					<p class="copyright">
						思创数码科技股份有限公司
					</p>
				</div>
			</div>
		</div>
	</body>
</html>

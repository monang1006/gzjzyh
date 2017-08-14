<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.oa.im.cache.Cache"/>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
			String isPop = session.getAttribute("isPop") == null ? null
			: (String) session.getAttribute("isPop");
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>
		<s:if test="modle.baseWindowsTitle!=null &&modle.baseWindowsTitle.length()>0">
			${modle.baseWindowsTitle}
		</s:if>
		<s:else>
		思创数码科技股份有限公司协同办公软件
		</s:else>
		</title>
		
		<link href="<%=frameroot%>/css/yangshi.css" type="text/css" rel="stylesheet">
			</head>
		<!--[if lt IE 7]>
 		<style type="text/css">
 			.logo img {behavior: url(<%=frameroot%>/images/perspective_toolbar/iepngfix.htc) }
 		</style>
		<![endif]-->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT type="text/javascript">
		var sysset="${sysset}";
		//alert(sysset);
	function denglu(){
	    var w = screen.availWidth-10;
<%--	    var h = screen.availHeight-35;--%>
	var h = screen.availHeight-55;
<%--	var win = window.open("oa.jsp","_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", menubar=no,scrollbars=no,location=no,resizable=no");--%>
		var j_username = document.getElementById("j_username").value;
		var j_password = document.getElementById("j_password").value;
		//var j_captcha_response = document.getElementById("j_captcha_response").value;
		if(sysset=='2'){
			if(document.getElementById("logintype").options[1].selected) {
				if(!readUSBKEY()){
					return;
				}
			}
		}
		if(sysset=='1'){
			if(document.getElementById("logintype").options[0].selected) {
				if(!readUSBKEY()){
					return;
				}
			}
		}
		if(sysset=='3'||document.getElementById("logintype").value=="CAkey"){
			changeActionURL(document.getElementById("logintype"));
			return ;
		}
		String.prototype.trim=function()
		{
    	 	return this.replace(/(^\s*)(\s*$)/g,"");
		}

		if(document.getElementById("logintype").options[1]!=undefined&&document.getElementById("logintype").options[1].selected==true){
		}else{
			if(j_username==""||j_username==null||j_username=="null"||j_username=='0'||j_username.trim().length==0||j_password==""){
			 	alert("请输入用户名密码");
			 	return;
		 	}
		}
		
		 
		// if(document.getElementById("j_captcha_response").value == ""){
		//	alert('请输入验证码！');
		//	return;
		//}
		
		var isPop="no";
		if(document.getElementById("isPop").checked==true){
			isPop="yes";
		}
		 //var j_username = document.getElementById("j_username").value;
		 //var j_password = document.getElementById("j_password").value;
		//var win = window.open("<%=path%>/j_spring_security_check?j_username="+j_username+"&j_password="+j_password+"&j_captcha_response="+j_captcha_response,"_blank","top=0,left=0,toolbar=no,width="+w+",height="+h+", status=yes, menubar=no,scrollbars=no,location=no,resizable=no");
		//if(win != null){
		//	window.close();
		//}
		
		
		//window.close();
		<%--var chkRtx = $("#chkRtx");
		if(chkRtx.attr("checked") == true){
			$.post("<%=root%>/im/iM!checkImStart.action",
				  {rtxStart:"yes",isPop:isPop,userName:$("#j_username").val(),password:$("#j_password").val()},
				  function(data){
				  	if(data == "ok"){
				  		document.getElementById("form1").submit();	
				  	}else{
				  		alert("对不起,出现未知异常!");
				  	}
				  });//将是否启动rtx的表示写入session中	
		}else if(chkRtx.attr("checked") == false){--%>
		
/*
alert("<%=root%>/im/iM!checkImStart.action?rtxStart=no&isPop="+isPop
			+"&userName="+$("#j_username").val()+"&password="+$("#j_password").val());
alert("signed_data:"+document.getElementById("signed_data").value +"\n"
		+"original_jsp:"+document.getElementById("original_jsp").value +"\n"
		+"j_username:"+document.getElementById("j_username").value +"\n"
		+"j_password:"+document.getElementById("j_password").value +"\n"
		+"usbkey:"+document.getElementById("usbkey").value +"\n"
		+"isPop:"+document.getElementById("isPop").value +"\n"
		);
	*/		
			
			
			
			//$.post("<%=root%>/im/iM!checkImStart.action",
			//	  {rtxStart:"no",isPop:isPop,userName:$("#j_username").val(),password:$("#j_password").val()},
			//	  function(data){
				//  	if(data == "ok"){
				  		document.getElementById("form1").submit();	
				//  	}else{
				 // 		alert("对不起,出现未知异常!");
				 // 	}
			//	  });//将是否启动rtx的表示写入session中
		//}
	
	
	}
	
	//验证控件是否可见
	function isVisible(obj){
	   if(obj==null) return false;
	   return obj.offsetWidth>0&&obj.offsetHeight>0;
	}
	
	function showCompanyDetail(){
		window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
	}
	function changeCaptcha(obj){
		var d = new Date();
		obj.src = "<%=path%>/captcha/captchaImage.action?d=" + d;
	}
		/**
	* 更换验证码，解决IE7下无法更新验证码问题
	*/
	function changeCaptcha(obj){
		var d = new Date();
		obj.src = "<%=path%>/captcha/captchaImage.action?d=" + d;
	}
	
	/**
	 * 根据是用户名验证还是USBKEY验证改变form提交路径
	 */
	function changeActionURL(selectObj) {
	  //var flag = "";
	  //if (selectObj.options[0].selected) {
	  //  flag = "username";
	  //} else {
	  //  flag = "usbkey";
	  //}	   
	//alert(flag+"\n"+selectObj.value)
	  var flag = selectObj.value;
		if(flag == 'username'){
		document.getElementById("j_username").disabled = false;
		document.getElementById("j_password").disabled = false;
			document.getElementById("form1").action = "<%=path%>/j_spring_security_check";
		}else if(flag == 'usbkey') {
			document.getElementById("j_username").disabled = true;
			document.getElementById("j_password").disabled = true;
			document.getElementById("form1").action = "<%=path%>/j_spring_security_usbkey_check";
		}else if(flag == 'CAkey'){
			document.getElementById("j_username").disabled = true;
			document.getElementById("j_password").disabled = true;
			
			//alert("original_data:${original_data}");
/* 		var Auth_Content = "${original_data}";
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
	//加载海泰方圆CA验证				
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
	        document.getElementById("form1").action = "<%=path%>/j_spring_security_ca_check";
	        document.getElementById("original_jsp").value = Auth_Content;
			document.getElementById("form1").submit();
		}
	}	
	
	function install(){
		window.open('<%=path%>/install.jsp','a');
	}
	function init(){
		var isPop="<%=isPop%>";
		if(isPop=="yes"){
			document.getElementById("isPop").checked=true;
		}
		if(sysset=="3"){//如果默认采用CA认证
			changeActionURL(document.getElementById("logintype"));
			return ;
		}
		var j_username = '${j_username}';
		if(j_username != "" && j_username != "null"){
			if(isVisible(document.getElementById("j_password"))){
				document.getElementById("j_password").focus();
			}
		}else{
			if(isVisible(document.getElementById("j_username"))){
				document.getElementById("j_username").focus();
			}
			
		}
	}
	
	function help(){
		window.open('<%=path%>/help.jsp','a');
	}
	
	//添加到收藏夹
	function AddFavorite(sURL,sTitle){
		//var sURL = "<%=basePath%>theme/theme!login.action";
		//var sTitle = "OA系统";
		try{
        	window.external.addFavorite(sURL, sTitle);
    	}catch (e){
        	try{
            	window.sidebar.addPanel(sTitle, sURL, "");
	        }catch (e){
            		alert("加入收藏失败，请使用Ctrl+D进行添加");
        	}
    	}
	}
	//设为首页
	function SetHome(obj,vrl){
		try{
            obj.style.behavior='url(#default#homepage)';obj.setHomePage(vrl);
        }catch(e){
           if(window.netscape) {
               try {
                   netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
               } catch (e) {
                   alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");
               }
               var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
               prefs.setCharPref('browser.startup.homepage',vrl);
           }
       }
	}
</script>
	<!-- USBKEY functions -->
	<script language="vbscript">
function CheckKey()
	On Error Resume Next 
    ePass.GetLibVersion
    'Let detecte whether the ePass 1000ND Active Control loaded.
	'If we call any method and the Err.number be set to &H1B6, it 
	'means the ePass 1000 Safe Active Control had not be loaded.
    If Err.number = &H1B6 Then
        MsgBox "未安装 ePass 1000ND 的相关控件!"
        document.Form1.epsKeyNum.value="Bad"
        Exit function
    end if
    ePass.OpenDevice 1, ""
    If Err then
        MsgBox "未找到USB_KEY,请确认已插入USB_KEY!"
        document.Form1.epsKeyNum.value="Bad"
        ePass.CloseDevice
        CheckKey = false
        Exit function
    End if
    CheckKey = true
End function

function readUSBKEY()
	On Error Resume Next 
	If CheckKey() = false then
		readUSBKEY = false
		Exit function
	End If
    dim results
    dim epsFileSize
    dim epsFileContent
    dim epsFileID,epsFileOffSize,epsFileBytes
    epsFileContent = ""
    epsFileSize = "60"
    results = ""
	epsFileID = CLng("&H" & CStr("18"))
    epsFileOffSize = "1"
    epsFileBytes = "30"

	ePass.OpenFile 0,epsFileID
 

	epsFileSize = ePass.GetFileInfo(0,3,epsFileID,0)
    'get the key num 
    results = ePass.GetStrProperty(7,0,0) 
    
    results1 = ""
    results2 = ""
    results1 = Mid(results, 1, 8)
    results2 = Mid(results, 9, 8)
    
    results = results2 + results1


	//epsFileContent = ePass.Read(0,0,epsFileOffSize,epsFileBytes)
    //epsFileContent = left(epsFileContent,epsFileSize)
    ePass.CloseFile
    ePass.CloseDevice
    document.getElementById("usbkey").value=results
	//document.form.FileContent.value=epsFileContent
    readUSBKEY = true
End function
</script>
	
		<%
			String frameRed = path + "/frame/theme_red";
			if (frameRed.equals(frameroot)) {
		%>
		<body ONLOAD="init()">
		<table width="100%" height="100%" border="0" align="center"
			cellpadding="0" cellspacing="0">
			<tr><td>
		<form id="form1" action="<%=path%>/j_spring_security_check" method="POST">
		<input type="hidden" id="signed_data" name="signed_data" /> 
		<input type="hidden" id="original_jsp" name="original_jsp" /> 
		<input type="hidden" id="RootCADN" value="" width="30" />
		<table width="1002" height="577" border="0" align="center" valign="middle"
			cellpadding="0" cellspacing="0">
			<tr>
				<td width="1002" height="212" valign="top"
					background="<%=frameroot%>/images/load/load_01.jpg">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="17%" height="130">
								&nbsp;
							</td>
							<td width="83%">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td height="80">
								&nbsp;
							</td>
							<td>
								<div
									style="font-family: 黑体; font-size: 42px; font-weight: 900;	font-variant: small-caps; color: #000000; line-height:68px;">
										<s:if test="modle.baseTitle==null||(modle.baseTitle!=null&&modle.baseTitle.indexOf('.')!=-1)">
											江西省财政厅协同办公平台
										</s:if>
										<%--<s:elseif test="">					
				        					<img src="<%=frameroot%>/images/<s:property value='modle.baseTitle' />" width="264" height="39" align="middle"/>
				        				</s:elseif>
										--%><s:else>
											${modle.baseTitle}
										</s:else>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="191" background="<%=frameroot%>/images/load/load_02.jpg">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="71%">
								&nbsp;
							</td>
							<td width="29%">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="21%" height="32" class="gaybold">
											用户名
										</td>
										<td width="54%" align="left">
											<input type="text" value="${j_username }" name="j_username" id="j_username" tabindex="1" class="inp" onkeypress="if(event.keyCode==13) document.all.j_password.focus();"/>
											<input type="hidden" name="j_usbkey" id="usbkey" />
										</td>
										<td width="25%">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td class="gaybold">
											密&nbsp;&nbsp;&nbsp;&nbsp;码
										</td>
										<td align="left">
											<input type="password" name="j_password" id="j_password" tabindex="2" class="inp" onkeypress="if(event.keyCode==13) denglu();"/>
										</td>
										<td>
											&nbsp;
										</td>
									</tr>
									<%--<tr>
										<td height="30" class="gaybold">
											验证码
										</td>
										<td>
											<input type="text" name="j_captcha_response" id="j_captcha_response" tabindex="3" class="yz" onkeypress="if(event.keyCode==13) denglu();"/>
											<img src='<%=path%>/captcha/captchaImage.action' onclick='changeCaptcha(this);' alt="看不清?换一张" style="cursor: hand;vertical-align: middle;">
										</td>
										<td align="left">
											&nbsp;
										</td>
									</tr>
									--%><tr>
										<td height="30" class="gaybold">
											登录方式
										</td>
										<td>
											<select class="dlfs" id="logintype" tabindex="4" onchange="changeActionURL(this)" >
									            <s:if test="sysset==null||sysset==0">
													 <option value="username" selected="selected">普通登录</option>
												</s:if>
												<s:if test="sysset==1">
													  <option value="usbkey" selected="selected">USBKEY登录</option>
												</s:if>
												<s:if test="sysset==3">
													  <option value="CAkey" selected="selected">CA认证</option>
												</s:if>
												<s:if test="sysset==2">
													 <option value="username" selected="selected">普通登录</option>
									                 <option value="usbkey" >USBKEY登录</option>
									                 <option value="CAkey" >CA认证</option>
												</s:if>
						                 	 </select> 
										</td>
										<td align="left">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td colspan="3" height="30" class="gaybold">
											<%--<s:if test="systemset!=null&&systemset.rtxIsEnable==\"1\"">
												<s:if test="systemset!=null&&systemset.rtxIsDefault==\"1\"">
													<input type="checkbox" name="chkRtx" id="chkRtx" checked="checked" value="yes" tabindex="6"/>登录RTX
												</s:if>
												<s:else>
													<input type="checkbox" name="chkRtx" id="chkRtx" value="yes" tabindex="6"/>登录RTX
												</s:else>
											</s:if>
											<s:else>
												<input type="checkbox" name="chkRtx" id="chkRtx" value="yes" disabled="disabled" tabindex="6"/><font color="#E0E0E0">登录RTX</font>
											</s:else>
											&nbsp;&nbsp;--%>全屏显示<input type="checkbox" name="isPop" id="isPop" value="no" tabindex="6" />
										</td>	
									</tr>
									<tr>
										<td height="30" class="gaybold">
											&nbsp;
										</td>
										<td colspan="2">
											<img src="<%=frameroot%>/images/load/buttonbg.jpg" width="62" height="26" onclick="denglu()">
											<!-- <span class="forgetpass"><a href="#">忘记密码？</a> &nbsp;&nbsp;<a href="javascript:install();" >系统安装</a></span>-->
											
										</td>	
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="174">
					<img src="<%=frameroot%>/images/load/load_03.jpg" width="1002" height="174">
				</td>
			</tr>
			<tr>
				<td align="right">
					建议浏览分辨率：1280*800及以上
				</td>
			</tr>
			<tr>
				<td align="right">
					<a href="#" onclick="SetHome(this,window.location)">设为首页</a>&nbsp;|&nbsp;<a href="#" onclick="AddFavorite(window.location,document.title)">加入收藏夹</a>&nbsp;|&nbsp;<a href="#" onclick="install();">相关下载</a>&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			<tr align="right">
				<td>
					<img src="<%=frameroot%>/images/load/logo.jpg" width="26" height="18"> &nbsp;&nbsp;思创数码科技股份有限公司
				</td>
			</tr>
		</table>
		
		</form>
		</td>
		</tr>
		</table>
		<%
		} else {
		%>
		<body ONLOAD="init()" style="overflow: hidden">
		<div id="outer">
			<div id="middle">
				<div class="login">
					<table class="logo" cellpadding="0" cellspacing="0" border="0" width="82%" height="40">
						<tr>
							<td nowrap="nowrap" class="logo">
								<%
										String frameBule = path + "/frame/theme_blue";
										if (frameBule.equals(frameroot)) {
								%>
									<s:if test="modle.baseLogoPic!=\"0\"&&modle.baseLogoPic!=\"\"&&modle.baseLogoPic!=null&&modle.baseLogoPic!=\"null\"">
			        					<img id="logos" src="<%=frameroot%>/images/perspective_toolbar/<s:property value='modle.baseLogoPic'/>" width="32" height="39" />
									</s:if>
								<%
								}
								%>
								<s:if test="modle.baseTitle==null">
									思创数码科技股份有限公司协同办公软件
								</s:if>
								<s:elseif test="modle.baseTitle!=null&&modle.baseTitle.indexOf('.')!=-1">					
		        					<img src="<%=frameroot%>/images/<s:property value='modle.baseTitle' />" width="264" height="39" align="middle"/>
		        				</s:elseif>
								<s:else>
									${modle.baseTitle}
								</s:else>
								</td>
								<td width="*">&nbsp;</td>
								<td valign="bottom" align="right" nowrap="nowrap">
									<div style="margin-top: 0;vertical-align: bottom;position: absolute;top: 12%;left:87%"><font size="1"><%=version%></font></div>		
								</td>
						</tr>
					</table>
					<form id="form1" action="<%=path%>/j_spring_security_check" method="POST">
						<input type="hidden" id="signed_data" name="signed_data" /> 
						<input type="hidden" id="original_jsp" name="original_jsp" /> 
						<input type="hidden" id="RootCADN" value="" width="30" />
						<ul>
							<li>
								<label>
								
								</label>
							</li>
							<li>
								<label>
									用&nbsp; 户&nbsp;名
									<input type="text" value="${j_username }" name="j_username" id="j_username" tabindex="1" class="inp" onkeypress="if(event.keyCode==13) document.all.j_password.focus();"/>
								<input type="hidden" name="j_usbkey" id="usbkey" />
								</label>
							</li>
							<li>
								<label>
									密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码
									<input type="password" name="j_password" id="j_password" tabindex="2" class="inp" onkeypress="if(event.keyCode==13) denglu();"/>
								</label>
							</li><%--
							<li>
								<label>
									验证码
									<input type="text" name="j_captcha_response" id="j_captcha_response" tabindex="3" class="yz" onkeypress="if(event.keyCode==13) denglu();"/>
									<img src='<%=path%>/captcha/captchaImage.action' onclick='changeCaptcha(this);' alt="看不清?换一张" style="cursor: hand;vertical-align: bottom;">
								</label>
							</li>
							--%><li>
				               	<label>登录方式
				      				<select id="logintype" tabindex="4" onchange="changeActionURL(this)" >
							            <s:if test="sysset==0">
											 <option value="username" selected="selected">普通登录</option>
										</s:if>
										<s:if test="sysset==1">
											  <option value="usbkey" selected="selected">USBKEY登录</option>
										</s:if>
										<s:if test="sysset==3">
											  <option value="CAkey" selected="selected">CA认证</option>
										</s:if>
										<s:if test="sysset==2">
											 <option value="username" selected="selected">普通登录</option>
							                 <option value="usbkey" >USBKEY登录</option>
							                 <option value="CAkey" >CA认证</option>
										</s:if>
				                 	 </select> 
				                </label>
							</li>
							<li class="rtx" >
								<%--<s:if test="systemset!=null&&systemset.rtxIsEnable==\"1\"">
									<s:if test="systemset!=null&&systemset.rtxIsDefault==\"1\"">
										<input type="checkbox" name="chkRtx" id="chkRtx" checked="checked" value="yes" tabindex="6" />登录RTX
									</s:if>
									<s:else>
										<input type="checkbox" name="chkRtx" id="chkRtx" value="yes" tabindex="6" />登录RTX  <%=Cache.getIMName() %>
									</s:else>	
								</s:if>
								<s:else>
									<input type="checkbox" name="chkRtx" id="chkRtx" value="yes" disabled="disabled" tabindex="6"/>登录RTX</font>
								</s:else>
								&nbsp;&nbsp;
								--%>全屏显示<input type="checkbox" name="isPop" id="isPop" value="no" tabindex="6" />
							</li>
							<li>
								<input type="button" name="button" id="button" value="登　录" tabindex="6" class="btn" onclick="denglu()" />
								<span class="forgetpass"><a href="#">忘记密码？</a></span>
							</li>
						</ul>
					</form>
					<p class="copyright">
						思创数码科技股份有限公司
						<a href="javascript:install();" >系统安装</a>&nbsp;&nbsp;<a href="javascript:help();">系统帮助（测试版）</a>
					</p>
				</div>
			</div>
			<div style="position: absolute;width: 95%;top:90%;" align="right">
				<p>
					建议浏览分辨率：1024*768及以上
				</p>
				<p>
					<a href="#" onclick="SetHome(this,window.location)">设为首页</a>&nbsp;|&nbsp;
					<a href="#" onclick="AddFavorite(window.location,document.title)">加入收藏夹</a>&nbsp;|&nbsp;
					<a href="#" onclick="install();">相关下载</a>&nbsp;&nbsp;&nbsp;
				</p>
				<p>
					<img src="<%=frameroot%>/images/load/logo.jpg" width="26" height="18" align="middle">&nbsp;&nbsp;思创数码科技股份有限公司
				</p>
			</div>
		</div>
		
		<%
		}
		%>
		
		<s:if test="sysset==1">
			<!-- 加载Epass -->
			<OBJECT id="ePass" style="LEFT: 0px; TOP: 0px" height="0" width="0"
				codeBase=<%=root%>/uums/usbkey/install.cab
				classid="clsid:C7672410-309E-4318-8B34-016EE77D6B58" name="ePass"
				VIEWASTEXT>
			</OBJECT>
		</s:if>
		<s:if test="sysset==3">
			<!-- 加载吉大正元CA认证插件 -->
			<%-- <object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488" id="JITDSignOcx" width="0" codebase="<%=path %>/common/JITDSign/JITDSign.cab#version=2,0,24,13"></object> --%>
			<!-- 加载海泰方圆CA认证插件 -->
			<object classid="clsid:B0EF56AD-D711-412D-BE74-A751595F3633" id="JITDSignOcx" size="0" width="0" CODEBASE="<%=path %>/common/JITDSign/JITComVCTK_S.cab#version=2,1,0,3"></object>
		</s:if>
		<s:if test="sysset==2">
			<!-- 加载Epass -->
			<OBJECT id="ePass" style="LEFT: 0px; TOP: 0px" height="0" width="0"
				codeBase=<%=root%>/uums/usbkey/install.cab
				classid="clsid:C7672410-309E-4318-8B34-016EE77D6B58" name="ePass"
				VIEWASTEXT>
			</OBJECT>
			<!-- 加载吉大正元CA认证插件 -->
<%-- 			<object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488" id="JITDSignOcx" width="0" codebase="<%=path %>/common/JITDSign/JITDSign.cab#version=2,0,24,13"></object>
 --%>			
            <!-- 加载海泰方圆CA认证插件 -->
                <object classid="clsid:B0EF56AD-D711-412D-BE74-A751595F3633" id="JITDSignOcx" size="0" width="0" CODEBASE="<%=path %>/common/JITDSign/JITComVCTK_S.cab#version=2,1,0,3"></object>
		</s:if>
	</body>
</html>

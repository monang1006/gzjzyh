<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>账号注册</title>
<%@include file="/common/include/meta.jsp"%>
<LINK type=text/css rel=stylesheet
	href="<%=frameroot%>/css/properties_windows_add.css">
<script type="text/javascript" language="javascript"
	src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>
<script type="text/javascript" language="javascript"
	src="<%=root%>/uums/js/md5.js"></script>
<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css
	rel=stylesheet>

<script type="text/javascript">
//验证电话号码（包括手机号码）
String.prototype.Trim = function() {  
  var m = this.match(/^\s*(\S+(\s+\S+)*)\s*$/);  
  return (m == null) ? "" : m[1];  
}

String.prototype.isMobile = function() {  
  return (/^(?:13\d|15[89])-?\d{5}(\d{3}|\*{3})$/.test(this.Trim()));  
} 

String.prototype.isTel = function()
{
    //"兼容格式: 国家代码(2到3位)-区号(2到3位)-电话号码(7到8位)-分机号(3位)"
    //return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/.test(this.Trim()));
    return (/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(this.Trim()));
}
						
	function checkuser(){
	   if(document.getElementById("userLoginname").value == ""||document.getElementById("userLoginname").value==null){
			alert('用户登录名不能为空。');
        	document.getElementById("userLoginname").focus();
        	return;
	    }
		if(document.getElementById("isname").value == ""||document.getElementById("isname").value==null){
	       	 checkLoginName(document.getElementById('userLoginname'),'0');	
	    }else{
	       if(document.getElementById("isname").value!=document.getElementById("userLoginname").value){ 
	           checkLoginName(document.getElementById('userLoginname'),'0');   
	       }
	   	}
	}
	
	function testname(){
	        if(document.getElementById("userLoginname").value == ""||document.getElementById("userLoginname").value==null){
				alert('用户登录名不能为空。');
	        	document.getElementById("userLoginname").focus();
	        	return false;
		    }else{
		    	if(document.getElementById("isname").value == ""||document.getElementById("isname").value==null){
		       	 	return checkLoginName(document.getElementById('userLoginname'),'1');	
		       }else{
			       	var flag=document.getElementById("isname").value;
			        if(flag==document.getElementById("userLoginname").value){
			         	alert("合法登录名。");
			           	document.getElementById("userLoginname").focus();
			           	return true;
			       }
			       else{
			         	return checkLoginName(document.getElementById('userLoginname'),'1');
			       }
			   }
		  }
	 }
	 
	function checkLoginName(obj,flag){
		var isValidated = false;
			 $.ajax({
				url:obj.url,
				type:"post",
				async:false,
				data:{loginname:obj.value},
				success:function(logininfo){
					if(logininfo!="111"){
						alert("登录名不唯一。");
						obj.focus();
					}
					else{
						alert("合法登录名。");
						isValidated = true;
					}						
				},
				error:function(logininfo){
					alert("异步出错。");
				}					
			});
		return isValidated;
	}
			
	//校验手机号码
function checkMobile(mobile){
	var flag = true;
	var reg_mobile13= /^(13\d{9})$/;
	var reg_mobile159=/^(15\d{9})$/;
	var reg_mobile189=/^(18\d{9})$/;
	if(mobile!=""){
		if(!reg_mobile13.test(mobile) && !reg_mobile159.test(mobile) && !reg_mobile189.test(mobile)){
			flag = false;
		}
	}
	return flag;
}	

				function onsubmitform(){
					var numtest = /^\d+$/; 
					var Email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/ ;
					var userId = $("#userId").val();
					
					if(!testname()){
						return;
					}
			        /* if(document.getElementById("userLoginname").value == ""||document.getElementById("userLoginname").value==null){
			            alert('用户登录名不能为空。');
	        	        document.getElementById("userLoginname").focus();
	        	      	return;
	                } */
			        if(document.getElementById("userLoginname").value.length > 100){
			        	alert('用户登录名称过长。');
			        	document.getElementById("userLoginname").focus();
			        	return;
			        }
			        if(document.getElementById("userName").value == ""){
			       		alert('用户名称不能为空。');
			       		document.getElementById("userName").focus();
			  			return;
			        }
			        if(document.getElementById("userName").value.length > 100){
			        	alert('用户名称过长。');
			        	document.getElementById("userName").focus();
			        	return;
			        }
			       
			        if(document.getElementById("userPassword").value == ""){
			       		alert('用户密码不能为空。');
			       		document.getElementById("userPassword").focus();
			  			return;
			        }
			         if(document.getElementById("userPassword").value.length > 100){
			        	alert('用户密码过长。');
			        	document.getElementById("userPassword").focus();
			        	return;
			        }
			        
			        if(document.getElementById("userTel").value.length > 20){
			        	alert('联系电话过长。');
			        	document.getElementById("userTel").focus();
			        	return;
			        }
			        
			     if(document.getElementById("userTel").value!=null && document.getElementById("userTel").value!=""){ 
			        if(!document.getElementById("userTel").value.isMobile()&& !document.getElementById("userTel").value.isTel()){
			           alert("请输入正确的手机号码或电话号码。\n\n例如:13916752109或0712-3614072"); 
                      document.getElementById("userTel").focus();
                             return false;               
			          }
			          }
			         var mobile = document.getElementById("rest2").value;
			        if(!checkMobile(mobile)){
			        	alert("请输入正确的手机号码。\n\n例如:13916752109");
			        	return ;
			        } 
			      	if(document.getElementById("userAddr").value.length > 100){
			        	alert('联系地址过长！');
			        	document.getElementById("userAddr").focus();
			        	return;
			        }
	        
			      	var userId = $("#userId").val();
					if(userId == "" || document.getElementById("hasPasswordEdited").value == "1"){
						if("${md5Enable}" == "1"){	
							document.getElementById("userPassword").value = hex_md5(document.getElementById("userPassword").value);
						}
					}
					
		             //----------------------END-----------------------
					document.getElementById("usermanagesave").submit();
				
				}
				
				function password(){
				 	var isSupman=document.getElementById("isSupman").value;
					window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/usermanage/usermanage-setpassword.jsp?md5Enable=${md5Enable}&isSupman="+isSupman,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:280px');
				}
				
				function setpassword(userPassword){				
					document.getElementById("userPassword").value = userPassword ;
					document.getElementById("rePassword").value = userPassword ;
					document.getElementById("hasPasswordEdited").value = "1";
				}
				
				function refreshList(){
					window.dialogArguments.submitForm();
				} 
				
			</script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>账号信息</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;重&nbsp;置&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table> 
					<s:form id="usermanagesave" target="hiddenFrame"
						action="/policeregister/policeRegister!save.action" theme="simple">
						<input type="hidden" id="hasPasswordEdited"
							name="hasPasswordEdited" value="0" />
						<input type="hidden" id="oldPassword" name="oldPassword"
							value="${model.tuumsBaseUser.userPassword}" />
						<input type="hidden" id="userId" name="userId"
							value="${model.tuumsBaseUser.userId}">
						<input type="hidden" id="isname" name="isname"
							value="${model.tuumsBaseUser.userLoginname}">
						<input type="hidden" id="isSupman" name="isSupman"
							value="${isSupman}">
						<input type="hidden" id="orgId" name="model.tuumsBaseUser.orgId"
							value="${model.tuumsBaseUser.orgId}">
						<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;登录账号：</span>
								</td>
								<td class="td1" align="left" width="40%"><input
									id="userLoginname"
									url="<%=path%>/usermanage/usermanage!checkLoginName.action"
									name="model.tuumsBaseUser.userLoginname" type="text" size="44" maxLength="50"
									onkeyup="this.value=this.value.replace(/\s/g,'')"
									onafterpaste="this.value=this.value.replace(/\s/g,'')"
									value="${model.tuumsBaseUser.userLoginname}"></td>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;登录密码：</span>
								</td>
								<td class="td1" align="left"><input
									id="userPassword" name="model.tuumsBaseUser.userPassword" type="password"
									size="44" value="${model.tuumsBaseUser.userPassword}" readonly="readonly">
									&nbsp;<a href="javascript:void(0);" class="button"
									onclick="password()">设置密码</a><input id="rePassword" name="rePassword" type="hidden"
									value="${rePassword}"></td>
							</tr>
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;用户姓名：</span>
								</td>
								<td class="td1" align="left"><input
									id="userName" name="model.tuumsBaseUser.userName" type="text" maxLength="50"
									onkeyup="this.value=this.value.replace(/\s/g,'')"
									onafterpaste="this.value=this.value.replace(/\s/g,'')"
									size="44" value="${model.tuumsBaseUser.userName}"></td>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;所属单位：</span>
								</td>
								<td class="td1" align="left">
									<input
									id="orgName" name="orgName" type="text" size="44"
									maxLength="20" value="${orgName}">&nbsp;<a href="javascript:void(0);" class="button" onclick="selectOrg()">选择单位</a>
								</td>
							</tr>
							<tr style="display: none;">
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
								</td>
								<td class="td1" colspan="3" align="left"><input
									id="userSequence" name="model.tuumsBaseUser.userSequence" maxlength="10"
									value="${model.tuumsBaseUser.userSequence}" type="text" size="22">
									&nbsp;<font color="#999999">( 数值越小排名越前 )</font></td>
							</tr>
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz">手机号码：</span>
								</td>
								<td class="td1" align="left"><input id="rest2"
									name="model.tuumsBaseUser.rest2" type="text" size="44" maxLength="20"
									value="${model.tuumsBaseUser.rest2}"></td>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz">办公电话：</span>
								</td>
								<td class="td1" align="left"><input
									id="userTel" name="model.tuumsBaseUser.userTel" type="text" size="44"
									maxLength="20" value="${model.tuumsBaseUser.userTel}"></td>
							</tr>
							<tr>
								<td width="25%" height="21" class="biao_bg1" align="right">
									<span class="wz">联系地址：</span>
								</td>
								<td class="td1" colspan="3" align="left"><input
									id="userAddr" name="model.tuumsBaseUser.userAddr" type="text" size="44"
									value="${model.tuumsBaseUser.userAddr}"></td>
							</tr>
							<td class="table1_td"></td>
							<td></td>
							</tr>
						</table>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
					</s:form>
				</td>
			</tr>
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td>&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left"><strong>警官信息</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="onsubmitform();">&nbsp;保&nbsp;存&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close()">&nbsp;重&nbsp;置&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_z_r.gif" /></td>
													<td width="6"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<table width="100%" height="10%" border="0" cellpadding="0"
							cellspacing="0" align="center" class="table1">
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><strong>主办警官：</strong></span>
							</td>
							<td class="td1" align="left" width="40%"></td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><strong>协办警官：</strong></span>
							</td>
							<td class="td1" align="left"></td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left"><input
								id="userLoginname"
								url="<%=path%>/usermanage/usermanage!checkLoginName.action"
								name="model.tuumsBaseUser.userLoginname" type="text" size="44" maxLength="50"
								onkeyup="this.value=this.value.replace(/\s/g,'')"
								onafterpaste="this.value=this.value.replace(/\s/g,'')"
								value="${model.tuumsBaseUser.userLoginname}"></td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left"><input
								id="userPassword" name="model.tuumsBaseUser.userPassword" type="password"
								size="44" value="${model.tuumsBaseUser.userPassword}" readonly="readonly">
								<input id="rePassword" name="rePassword" type="hidden"
								value="${rePassword}">&nbsp;<a href="javascript:void(0);" class="button"
									onclick="password()" style="display:none;">设置密码</a></td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left"><input
								id="userName" name="model.tuumsBaseUser.userName" type="text" maxLength="50"
								onkeyup="this.value=this.value.replace(/\s/g,'')"
								onafterpaste="this.value=this.value.replace(/\s/g,'')"
								size="44" value="${model.tuumsBaseUser.userName}"></td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left">
								<input
								id="orgName" name="orgName" type="text" size="44"
								maxLength="20" value="${orgName}">
							</td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left"><input id="rest2"
								name="model.tuumsBaseUser.rest2" type="text" size="44" maxLength="20"
								value="${model.tuumsBaseUser.rest2}"></td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left"><input
								id="userTel" name="model.tuumsBaseUser.userTel" type="text" size="44"
								maxLength="20" value="${model.tuumsBaseUser.userTel}"></td>
						</tr>
						<tr>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left"><input id="rest2"
								name="model.tuumsBaseUser.rest2" type="text" size="44" maxLength="20"
								value="${model.tuumsBaseUser.rest2}"></td>
							<td width="25%" height="21" class="biao_bg1" align="right">
								<span class="wz">手机号码：</span>
							</td>
							<td class="td1" align="left"><input
								id="userTel" name="model.tuumsBaseUser.userTel" type="text" size="44"
								maxLength="20" value="${model.tuumsBaseUser.userTel}"></td>
						</tr>
						<tr>
							<td colspan="2" class="td1" align="left">
								<table style="width:100%;">
									<tr>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>警官证（正）</div></td>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>警官证（反）</div></td>
									</tr>
									<tr>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>身份证（正）</div></td>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>身份证（反）</div></td>
									</tr>
								</table>
							</td>
							<td class="td1" colspan="2" align="left">
								<table style="width:100%;">
									<tr>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>警官证（正）</div></td>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>警官证（反）</div></td>
									</tr>
									<tr>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>身份证（正）</div></td>
										<td width="50%" align="center"><img alt="" src="" style="width:100%;height:200px;"><div>身份证（反）</div></td>
									</tr>
								</table>
							</td>
						</tr>
						<td class="table1_td"></td>
						<td></td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
		</table>
	</DIV>
</body>
<iframe id="hiddenFrame" name="hiddenFrame" style="width:0px;height:0px;display:none;"></iframe>
</html>
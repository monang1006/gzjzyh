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
			alert('登录账号不能为空。');
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
				alert('登录账号不能为空。');
	        	document.getElementById("userLoginname").focus();
	        	return false;
		    }else{
		    	if(document.getElementById("isname").value == ""||document.getElementById("isname").value==null){
		       	 	return checkLoginName(document.getElementById('userLoginname'),'1');	
		       }else{
			       	var flag=document.getElementById("isname").value;
			        if(flag==document.getElementById("userLoginname").value){
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
						alert("登录账号不唯一。");
						obj.focus();
					}
					else{
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
			        	alert('登录账号过长，最大长度为100。');
			        	document.getElementById("userLoginname").focus();
			        	return;
			        }
			        if(document.getElementById("userName").value == ""){
			       		alert('用户姓名不能为空。');
			       		document.getElementById("userName").focus();
			  			return;
			        }
			        if(document.getElementById("userName").value.length > 100){
			        	alert('用户姓名过长，最大长度为100。');
			        	document.getElementById("userName").focus();
			        	return;
			        }
			       
			        if(document.getElementById("userPassword").value == ""){
			       		alert('用户密码不能为空。');
			       		document.getElementById("userPassword").focus();
			  			return;
			        }
			         if(document.getElementById("userPassword").value.length > 100){
			        	alert('用户密码过长，最大长度为100。');
			        	document.getElementById("userPassword").focus();
			        	return;
			        }
			         
			        if(document.getElementById("orgId").value == ""){
			       		alert('请选择所属单位。');
			  			return;
			        }
			        
			        if(document.getElementById("userTel").value.length > 20){
			        	alert('办公电话过长，最大长度为20。');
			        	document.getElementById("userTel").focus();
			        	return;
			        }
			        
			     if(document.getElementById("userTel").value!=null && document.getElementById("userTel").value!=""){ 
			        if(!document.getElementById("userTel").value.isMobile()&& !document.getElementById("userTel").value.isTel()){
			           alert("请输入正确的办公电话号码。\n\n例如:13916752109或0712-3614072"); 
                      document.getElementById("userTel").focus();
                             return false;               
			          }
			          }
			     	if(!validateElement("rest2", 20, "手机号码")){
						return ;
			        }
			        var mobile = document.getElementById("rest2").value;
			        if(!checkMobile(mobile)){
			        	alert("请输入正确的手机号码。\n\n例如:13916752109");
			        	return ;
			        }
			      	if(document.getElementById("userAddr").value.length > 100){
			        	alert('联系地址过长，最大长度为100！');
			        	document.getElementById("userAddr").focus();
			        	return;
			        }
	        
					//扩展信息
					if(!validateElement("ueMainName", 20, "主办警官姓名")){
						return ;
			        }
					if(!validateElement("ueHelpName", 20, "协办警官姓名")){
						return ;
			        }
					if(!validateElement("ueMainNo", 20, "主办警官警号")){
						return ;
			        }
					if(!validateElement("ueHelpNo", 20, "协办警官警号")){
						return ;
			        }
					if(!validateElement("ueMainId", 20, "主办警官身份证号")){
						return ;
			        }
					if(!validateElement("ueHelpId", 20, "协办警官身份证号")){
						return ;
			        }
					if(!validateElement("ueMainMobile", 20, "主办警官手机号码")){
						return ;
			        }
					var mobile = document.getElementById("ueMainMobile").value;
			        if(!checkMobile(mobile)){
			        	alert("请输入正确的主办警官手机号码。\n\n例如:13916752109");
			        	return ;
			        }
			        if(!validateElement("ueHelpMobile", 20, "协办警官手机号码")){
						return ;
			        }
			        var mobile = document.getElementById("ueHelpMobile").value;
			        if(!checkMobile(mobile)){
			        	alert("请输入正确的协办警官手机号码。\n\n例如:13916752109");
			        	return ;
			        }
			        
			        if(!validateElement("ueMainNo1", 200, "主办警官警官证正面")){
						return ;
			        }
					if(!validateElement("ueMainNo2", 200, "主办警官警官证反面")){
						return ;
			        }
					if(!validateElement("ueMainId1", 200, "主办警官身份证正面")){
						return ;
			        }
					if(!validateElement("ueMainId2", 200, "主办警官身份证反面")){
						return ;
			        }
					if(!validateElement("ueHelpNo1", 200, "协办警官警官证正面")){
						return ;
			        }
					if(!validateElement("ueHelpNo2", 200, "协办警官警官证反面")){
						return ;
			        }
					if(!validateElement("ueHelpId1", 200, "协办警官身份证正面")){
						return ;
			        }
					if(!validateElement("ueHelpId2", 200, "协办警官身份证反面")){
						return ;
			        }
			        var ueId = $("#ueId").val();
					if(ueId == "" || document.getElementById("hasPasswordEdited").value == "1"){
						if("${md5Enable}" == "1"){	
							document.getElementById("userPassword").value = hex_md5(document.getElementById("userPassword").value);
						}
					}
		             //----------------------END-----------------------
					document.getElementById("usermanagesave").submit();
				
				}
				
				function validateElement(elementId, maxLength, elementName){
					var isValidate = true;
					if(document.getElementById(elementId).value == ""){
			       		alert(elementName + '不能为空。');
			       		//document.getElementById(elementId).focus();
			       		return false;
			        }
			        if(document.getElementById(elementId).value.length > maxLength){
			        	alert(elementName + '过长，最大长度为' + maxLength + '。');
			        	//document.getElementById(elementId).focus();
			        	return false;
			        }
			        return isValidate;
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
				
				function tree(){
					var orgid = document.getElementById("orgId").value;
					window.showModalDialog("<%=path%>/policeregister/policeRegister!orgMoreTree.action?orgid="+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
				}
				
				function setOrg(orgId, orgName){
					document.getElementById("orgId").value = orgId;
					document.getElementById("userOrgName").value = orgName;
				}
				
				function openImageUpload(domElementId, windowTitle, nowImageUrl){
					if(document.getElementById(domElementId).value != "" && document.getElementById(domElementId).value != null){
						nowImageUrl = document.getElementById(domElementId).value;
					}
					var imageUrl = window.showModalDialog("<%=path%>/upload/fileUpload!imageUpload.action?imageUrl="+nowImageUrl,window,'help:no;status:no;scroll:no;dialogWidth:1200px; dialogHeight:800px');
					if(imageUrl != null && imageUrl != ""){
						document.getElementById(domElementId).value = imageUrl;
						document.getElementById(domElementId + "Tmp").src = "<%=path%>" + imageUrl;
					}
					//window.open("<%=path%>/policeregister/policeRegister!imageUpload.action?domElementId="+domElementId+"&flag="+(new Date())+"&imageUrl="+nowImageUrl, "", "width=1200,height=800,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no");
				}
				
				function setImageUpload(domElementId, imageUrl){
					if(imageUrl != null && imageUrl != ""){
						document.getElementById(domElementId).value = imageUrl;
						document.getElementById(domElementId + "Tmp").src = "<%=path%>" + imageUrl;
					}
				}
				
				function formReset(){
					$("#userLoginname").val("");
					$("#userPassword").val("");
					$("#userName").val("");
					$("#rest2").val("");
					$("#userTel").val("");
					$("#userAddr").val("");
					$("#userOrgName").val("");
					$("#orgId").val("");
					$("#ueMainName").val("");
					$("#ueMainId").val("");
					$("#ueMainNo").val("");
					$("#ueMainMobile").val("");
					$("#ueMainNo1").val("");
					$("#ueMainNo2").val("");
					$("#ueMainId1").val("");
					$("#ueMainId2").val("");
					$("#ueHelpName").val("");
					$("#ueHelpId").val("");
					$("#ueHelpNo").val("");
					$("#ueHelpMobile").val("");
					$("#ueHelpNo1").val("");
					$("#ueHelpNo2").val("");
					$("#ueHelpId1").val("");
					$("#ueHelpId2").val("");
					
					$("#ueMainNo1Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueMainNo2Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueMainId1Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueMainId2Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueHelpNo1Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueHelpNo2Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueHelpId1Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
					$("#ueHelpId2Tmp").attr("src", "<%=path%>/images/upload/defaultUpload.jpg");
				}
				
			</script>
</head>
<base target="_self" />
<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=center>
		<s:form id="usermanagesave" target="hiddenFrame"
						action="/policeregister/policeRegister!save.action" theme="simple">
		<input type="hidden" id="flag" name="flag" value="register" />
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>账号信息</strong></td>
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
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_l.gif" /></td>
													<td class="Operation_input" onclick="formReset()">&nbsp;重&nbsp;置&nbsp;</td>
													<td width="7"><img
														src="<%=frameroot%>/images/ch_h_r.gif" /></td>
													<td width="5"></td>
													<td width="8"><img
														src="<%=frameroot%>/images/ch_z_l.gif" /></td>
													<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
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
					<input type="hidden" id="ueMainNo1" name="model.ueMainNo1"
						value="${model.ueMainNo1}">
					<input type="hidden" id="ueMainNo2" name="model.ueMainNo2"
						value="${model.ueMainNo2}">
					<input type="hidden" id="ueMainId1" name="model.ueMainId1"
						value="${model.ueMainId1}">
					<input type="hidden" id="ueMainId2" name="model.ueMainId2"
						value="${model.ueMainId2}">
					<input type="hidden" id="ueHelpNo1" name="model.ueHelpNo1"
						value="${model.ueHelpNo1}">
					<input type="hidden" id="ueHelpNo2" name="model.ueHelpNo2"
						value="${model.ueHelpNo2}">
					<input type="hidden" id="ueHelpId1" name="model.ueHelpId1"
						value="${model.ueHelpId1}">
					<input type="hidden" id="ueHelpId2" name="model.ueHelpId2"
						value="${model.ueHelpId2}">
					<input type="hidden" id="ueId" name="model.ueId"
						value="${model.ueId}">
					<input id="rePassword" name="rePassword" type="hidden"
								value="${rePassword}">
					<table width="100%" height="10%" border="0" cellpadding="0"
						cellspacing="0" align="center" class="table1">
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;登录账号：</span>
							</td>
							<td class="td1" align="left" width="40%"><input
								id="userLoginname"
								url="<%=path%>/usermanage/usermanage!checkLoginName.action"
								name="model.tuumsBaseUser.userLoginname" type="text" size="44" maxLength="50"
								value="${model.tuumsBaseUser.userLoginname}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;登录密码：</span>
							</td>
							<td class="td1" align="left"><input
								id="userPassword" name="model.tuumsBaseUser.userPassword" type="password"
								size="44" value="${model.tuumsBaseUser.userPassword}" readonly="readonly">&nbsp;<a href="javascript:void(0);" class="button"
								onclick="password()">设置密码</a>
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;用户姓名：</span>
							</td>
							<td class="td1" align="left"><input
								id="userName" name="model.tuumsBaseUser.userName" type="text" maxLength="50"
								size="44" value="${model.tuumsBaseUser.userName}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;所属单位：</span>
							</td>
							<td class="td1" align="left">
								<input
								id="userOrgName" name="userOrgName" type="text" size="44"
								value="${userOrgName}" readOnly="readonly">&nbsp;<a href="javascript:void(0);" class="button" onclick="tree()">选择单位</a>
							</td>
						</tr>
						<tr style="display: none;">
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
							</td>
							<td class="td1" colspan="3" align="left"><input
								id="userSequence" name="model.tuumsBaseUser.userSequence" maxlength="10"
								value="${model.tuumsBaseUser.userSequence}" type="text" size="22">
								&nbsp;<font color="#999999">( 数值越小排名越前 )</font></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;手机号码：</span>
							</td>
							<td class="td1" align="left"><input id="rest2"
								name="model.tuumsBaseUser.rest2" type="text" size="44" maxLength="20"
								value="${model.tuumsBaseUser.rest2}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz">办公电话：</span>
							</td>
							<td class="td1" align="left"><input
								id="userTel" name="model.tuumsBaseUser.userTel" type="text" size="44"
								maxLength="20" value="${model.tuumsBaseUser.userTel}"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
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
				</td>
			</tr>
			<tr>
				<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="40" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30">&nbsp;</td>
										<td class="table_headtd_img"><img
											src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
										<td align="left" width="140"><strong>警官信息</strong></td>
										<td align="right">
											<table border="0" align="right" cellpadding="00"
												cellspacing="0">
												<tr>
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
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><strong>主办警官：</strong></span>
							</td>
							<td class="td1" align="left" width="40%"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><strong>协办警官：</strong></span>
							</td>
							<td class="td1" align="left"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left" width="40%"><input
								id="ueMainName"
								name="model.ueMainName" type="text" size="44" maxLength="50"
								value="${model.ueMainName}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官姓名：</span>
							</td>
							<td class="td1" align="left"><input
								id="ueHelpName" name="model.ueHelpName" type="text"
								size="44" maxLength="50" value="${model.ueHelpName}"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left" width="40%"><input
								id="ueMainNo" name="model.ueMainNo" type="text" maxLength="50"
								size="44" value="${model.ueMainNo}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;警官警号：</span>
							</td>
							<td class="td1" align="left">
								<input
								id="ueHelpNo" name="model.ueHelpNo" type="text" size="44"
								maxLength="50" value="${model.ueHelpNo}">
							</td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left" width="40%"><input id="ueMainId"
								name="model.ueMainId" type="text" size="44"
								value="${model.ueMainId}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;身份证号：</span>
							</td>
							<td class="td1" align="left"><input
								id="ueHelpId" name="model.ueHelpId" type="text" size="44" value="${model.ueHelpId}"></td>
						</tr>
						<tr>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;手机号码：</span>
							</td>
							<td class="td1" align="left" width="40%"><input id="ueMainMobile"
								name="model.ueMainMobile" type="text" size="44"
								value="${model.ueMainMobile}"></td>
							<td height="21" class="biao_bg1" align="right">
								<span class="wz"><font color="red">*</font>&nbsp;手机号码：</span>
							</td>
							<td class="td1" align="left"><input
								id="ueHelpMobile" name="model.ueHelpMobile" type="text" size="44" value="${model.ueHelpMobile}"></td>
						</tr>
						<tr>
							<!-- <td colspan="2" class="td1" align="center"> -->
							<td colspan="4" class="td1" align="center" style="height:20px;">
							</td>
						</tr>
						<tr>
							<!-- <td colspan="2" class="td1" align="center"> -->
							<td colspan="4" class="td1" align="center">
								<table style="width:100%;">
									<tr>
										<td align="center">
											<img id="ueMainNo1Tmp" onclick="openImageUpload('ueMainNo1', '主办警官警官证（正）', '${ueMainNo1Tmp }')" src="<%=path %>${ueMainNo1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainNo1', '主办警官警官证（正）', '${ueMainNo1Tmp }')">警官证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueMainNo2Tmp" onclick="openImageUpload('ueMainNo2', '主办警官警官证（反）', '${ueMainNo2Tmp }')" src="<%=path %>${ueMainNo2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainNo2', '主办警官警官证（反）', '${ueMainNo2Tmp }')">警官证（反）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpNo1Tmp" onclick="openImageUpload('ueHelpNo1', '协办警官警官证（正）', '${ueHelpNo1Tmp }')" src="<%=path %>${ueHelpNo1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpNo1', '协办警官警官证（正）', '${ueHelpNo1Tmp }')">警官证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpNo2Tmp" onclick="openImageUpload('ueHelpNo2', '协办警官警官证（反）', '${ueHelpNo2Tmp }')" src="<%=path %>${ueHelpNo2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpNo2', '协办警官警官证（反）', '${ueHelpNo2Tmp }')">警官证（反）</a></div>
										</td>
									</tr>
									<tr>
										<td align="center">
											<img id="ueMainId1Tmp" onclick="openImageUpload('ueMainId1', '主办警官身份证（正）', '${ueMainId1Tmp }')" src="<%=path %>${ueMainId1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainId1', '主办警官身份证（正）', '${ueMainId1Tmp }')">身份证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueMainId2Tmp" onclick="openImageUpload('ueMainId2', '主办警官身份证（反）', '${ueMainId2Tmp }')" src="<%=path %>${ueMainId2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueMainId2', '主办警官身份证（反）', '${ueMainId2Tmp }')">身份证（反）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpId1Tmp" onclick="openImageUpload('ueHelpId1', '协办警官身份证（正）', '${ueHelpId1Tmp }')" src="<%=path %>${ueHelpId1Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpId1', '协办警官身份证（正）', '${ueHelpId1Tmp }')">身份证（正）</a></div>
										</td>
										<td align="center">
											<img id="ueHelpId2Tmp" onclick="openImageUpload('ueHelpId2', '协办警官身份证（反）', '${ueHelpId2Tmp }')" src="<%=path %>${ueHelpId2Tmp }" style="width:200px;height:200px;cursor:pointer;">
											<div style="padding-top:10px; padding-bottom:20px;"><a href="javascript:void(0);" onclick="openImageUpload('ueHelpId2', '协办警官身份证（反）', '${ueHelpId2Tmp }')">身份证（反）</a></div>
										</td>
									</tr>
								</table>
							<!-- </td>
							<td class="td1" colspan="2" align="center"> -->
							</td>
						</tr>
					</table>
					<table id="annex" width="90%" height="10%" border="0"
						cellpadding="0" cellspacing="1" align="center" class="table1">
					</table>
				</td>
			</tr>
		</table>
		</s:form>
	</DIV>
</body>
<iframe id="hiddenFrame" name="hiddenFrame" style="width:0px;height:0px;display:none;"></iframe>
</html>
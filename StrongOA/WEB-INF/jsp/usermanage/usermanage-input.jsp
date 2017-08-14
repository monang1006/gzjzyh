<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>
			<s:if test="model.userId==null">
				新建用户
			</s:if>
			<s:else>
				编辑用户
			</s:else>
		</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_add.css">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript" language="javascript" src="<%=root%>/uums/js/md5.js"></script>
		<LINK href="<%=path%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		
		
		
		
		<script type="text/javascript">
		$(function(){
			//图片分辨率错误  提示
			var wrongInfo = '${wrongInfo}';
			if(wrongInfo!=null&&wrongInfo!=""){
				alert("上传图片的尺寸不是120*90，不符合要求，请重新上选择。");
			}
		}); 
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
						
			function testMessage(){
	   if(document.getElementById("userSyscode").value == ""||document.getElementById("userSyscode").value==null){
			alert('用户编号不能为空。');
	        	document.getElementById("userSyscode").focus();
	        	return;
	    }
	    else
	    {
	    if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('userSyscode'),'1');	
	       }
	     else{
	        if( document.getElementById("iscode").value==document.getElementById("userSyscode").value){
	          alert("合法编码。");
	        document.getElementById("userSyscode").focus();
	       }
	       else{
	         comparecode(document.getElementById('userSyscode'),'1');
	       }
	    	
	    }
	  }
	 }
 	function testCardId()  //检测身份证的合法性与唯一性
	{
	  if(document.getElementById("identification").value==null||document.getElementById("identification").value=="")
	  {
	       alert("请输入身份证号码。");
	       document.getElementById("identification").focus();
	       return;
	  }else 
	  {
	      if(document.getElementById("iscardid").value==null||document.getElementById("iscardid").value=="")
	     {
	        checkCardId('1');
	     } else{
	     if( document.getElementById("iscardid").value==document.getElementById("identification").value){
	          alert("合法身份证号码。");
	        document.getElementById("identification").focus();
	       }
	       else{
	         checkCardId('1');
	       }    
	     }   
	   
	  }
	} 
	function testcode(){
		if("${model.userIsdel}" == "1"){
			alert("[${model.userName}]已删除，不允许进行编辑保存操作。");
			return;
	   }
	   if(document.getElementById("userSyscode").value == ""||document.getElementById("userSyscode").value==null){
			alert('用户编号不能为空。');
	        	document.getElementById("userSyscode").focus();
	        	return;
	    }
		if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('userSyscode'),'0');	
	       }
	     else{
	       if(document.getElementById("iscode").value==document.getElementById("userSyscode").value){
	           checkuser(); 
	       }
	       else{
	         comparecode(document.getElementById('userSyscode'),'0');     
	       }
	        
	        }
	}
	
	
	function checkuser(){
	   if(document.getElementById("userLoginname").value == ""||document.getElementById("userLoginname").value==null){
			alert('用户登录名不能为空。');
	        	document.getElementById("userLoginname").focus();
	        	return;
	    }
		if(document.getElementById("isname").value == ""||document.getElementById("isname").value==null){
	       	 checkLoginName(document.getElementById('userLoginname'),'0');	
	       }
	     else{
	       if(document.getElementById("isname").value==document.getElementById("userLoginname").value){ 
	           checkCard1(); 
	       }
	       else{
	         checkLoginName(document.getElementById('userLoginname'),'0');   
	       }
	        
	        }
	}
	function checkCard1()   //验证身份证号码（编辑）
	      {
	        /* if(document.getElementById("identification").value==null||document.getElementById("identification").value=="")
	        {
	         alert("请输入身份证号码。");
	         document.getElementById("identification").focus();
	         return;
	        }  */
	        
	           if(document.getElementById("iscardid").value==null||document.getElementById("iscardid").value=="")
	          {
	             checkCardId('0');
	           } else{
	            if( document.getElementById("iscardid").value==document.getElementById("identification").value){
	               onsubmitform();
	            }
	         else{
	              checkCardId('0');
	          }    
	        }
	      }
	function checkCardId(flag)   //检测身份证号码的合法性
	{  
	   var cardId = document.getElementById("identification").value;
	   var userId = document.getElementById("userId").value;
	   var reg_cardId15 =/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;  //15位身份证号码验证正则表达式
       var reg_cardId18 =/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;  //18位身份证号码验证正则表达式
       if(cardId!=""){
         if(cardId!=null&& cardId.length==15)
         {
           if(!reg_cardId15.test(cardId))
           {
             alert("身份证号码格式输入有误！");
             return false;
            }
       
          }else if(cardId!=null && cardId.length==18)
           {
              if(!reg_cardId18.test(cardId))
                {
                  alert("身份证号码格式输入有误！");
                   return false;
                 }
           }else
           {
           alert("身份证号必须为15位或者18位！");
           return false;
           }
          } 
          compareCardId(document.getElementById("identification"),userId,flag);
          return true;
	
	
	}
	function compareCardId(obj,userId,flag)  //检测身份证号码的唯一性
	{
	   $.ajax({
					url:obj.url,
					type:"post",
					data:{rest4:obj.value,userId:userId},
					success:function(message){
						if(message!="111"){
							alert("身份证号码不唯一。");
							obj.focus();	
						}
						else{
						if(flag=='0')    //"0"是选择"保存"按钮，"1"是选择"检测唯一性"按钮
						onsubmitform();  //提交form
						else 
						alert("合法身份证号码。");
						} 
						
					},
					error:function(message){
						alert("异步出错!");
					}
					
				});
				
	   
	
	
	}
	function comparecode(obj,flag){	
				 $.ajax({
					url:obj.url,
					type:"post",
					data:{syscode:obj.value},
					success:function(message){
						if(message!="111"){
							alert("编码不唯一。");
							obj.focus();		
						}
						else{
						if(flag=='0')
						checkuser();
						else 
						alert("合法编码。");
						}
						
					},
					error:function(message){
						alert("异步出错!");
					}
					
				});
				
			}
			
			
		function testname(){		 
	        if(document.getElementById("userLoginname").value == ""||document.getElementById("userLoginname").value==null){
			alert('用户登录名不能为空。');
	        	document.getElementById("userLoginname").focus();
	        	return;
	       }
	   else
	    {
	    if(document.getElementById("isname").value == ""||document.getElementById("isname").value==null){
	       	 checkLoginName(document.getElementById('userLoginname'),'1');	
	       }
	     else{
	       var flag=document.getElementById("isname").value;
	        if(flag==document.getElementById("userLoginname").value){
	         alert("合法登录名。");
	           document.getElementById("userLoginname").focus();
	       }
	       else{
	         checkLoginName(document.getElementById('userLoginname'),'1');
	       }
	    	
	    }
	  }
	 }
	 
	
	function checkLoginName(obj,flag){	
				 $.ajax({
					url:obj.url,
					type:"post",
					data:{loginname:obj.value},
					success:function(logininfo){			
						if(logininfo!="111"){
							alert("登录名不唯一。");
							obj.focus();		
						}
						else{
						if(flag=='0')
						{
						checkCardId('0');}
						else 
						alert("合法登录名。");
						}						
					},
					error:function(logininfo){
						alert("异步出错。");
					}					
				});
				
			}
			
				//增加一人多岗之前的选择org
				//function tree(){
				//	window.showModalDialog("<%=path%>/usermanage/usermanage!getOrgTree.action",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:420px');
				//}
				//增加一人多岗之后的选择org
				function tree(){
					var orgid = document.getElementById("orgid").value;
					var userId = document.getElementById("userId").value;
					window.showModalDialog("<%=path%>/usermanage/usermanage!orgMoreTree.action?userId="+userId+"&orgid="+orgid,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
				}
                
		//设置领导领导联系人
		function Orgtree(){
			var url = "<%=path%>/organisemanage/orgmanage!chooseOrgManager.action";
			var id = document.getElementById("userAddrid").value;
			var userId=document.getElementById("userId").value;			
			if(id != ""){
				url += "?id=" + id;
			}
			var ret = window.showModalDialog(url,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			if(ret){			
				if(ret[0].split(",").length > 1){
					alert("领导联系人至多只能设置1人");
					return ;
				}
				var LxrId = ret[0].substring(1);
				if(userId==LxrId){
				 	alert("领导联系人不能选择自己");
					return ;
				}else {
				setRest2IdName(ret[0],ret[1]); 
				}								
			}
		}


		function setRest2IdName(id,name){
			document.getElementById("userAddrid").value = id;
			document.getElementById("userAddrname").value = name;
		}
      				
				
				function setusertype(){
					var sysmanagerid = document.getElementById("sysmanagerid").value ;
					var managerid = document.getElementById("managerid").value ;
					window.showModalDialog("<%=path%>/usermanage/usermanage!getUserType.action?sysmanagerid="+sysmanagerid+"&managerid="+managerid,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:420px');
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
					if(document.getElementById("userSyscode").value == ""){
			       		alert('用户编号不能为空。');
			       		document.getElementById("userSyscode").focus();
			  			return;
			        }
			        //if(!numtest.test(document.getElementById("userSyscode").value)){
			        //	alert('用户编号必须为数字！！！');
			        //	document.getElementById("userSyscode").focus();
			        //	return;
			        //}
			        if(document.getElementById("userLoginname").value == ""||document.getElementById("userLoginname").value==null){
			              alert('用户登录名不能为空。');
	        	           document.getElementById("userLoginname").focus();
	        	      return;
	                }
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
			       /*  if(document.getElementById("orgId").value == ""){
			       		alert('请选择组织机构。');
			  			return;
			        }*/
			         if(document.getElementById("orgNames").value == ""){
			       		alert('请选择组织机构！');
			  			return;
			        }
			        if(document.getElementById("userOrg").value == ""){
			       		alert('请选择默认机构！');
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
			      // if(document.getElementById("userAddr").value.length > 200){
			        	//alert('联系地址过长！');
			        	//document.getElementById("userAddr").focus();
			        	//return;
			        //}
			        if(document.getElementById("userEmail").value.length > 100){
			        	alert('E-mail地址过长。');
			        	document.getElementById("userEmail").focus();
			        	return;
			        }
			        if(document.getElementById("userEmail").value != ""){
				        if(!Email.test(document.getElementById("userEmail").value)){
				        	alert('E-mail地址有误。');
				        	document.getElementById("userEmail").focus();
				        	return;
				        }
				    }
				     var sequence=document.getElementById("userSequence").value;
               if(!numtest.test(sequence)){
		       alert('排序必须为数字。');
		      document.getElementById("userSequence").focus();
		        return;
	        }
	        
	         if(sequence.length>10){
	   	       alert('排序序号字符不能超过10位数。');
		       document.getElementById("userSequence").focus();
		        return;
	           }
    	
//      if(document.getElementById("userUsbkey").value == null || document.getElementById("userUsbkey").value == ""){
//    	alert("USB KEY不能为空，请输入USB KEY！");
//    	return false;
//    	}    		
					if(document.getElementById("hasPasswordEdited").value == "1"){
						if(${md5Enable} == "1"){	
							document.getElementById("userPassword").value = hex_md5(document.getElementById("userPassword").value);
						}
					}
					
				     var safe = document.getElementById("rest3").value;
				     //去掉安全级别的必填限制 dengzc 2011年1月6日10:45:24
				      /*if(safe == ""||safe==null){
			              alert('安全级别不能为空！');
	        	           document.getElementById("rest3").focus();
		        	      return;
		                }*/
		             if(safe != ""){
			             if(!numtest.test(safe)){
					       alert('安全级别必须为数字。');
					      document.getElementById("rest3").focus();
					        return;
				       	 }
				        
				         if(safe.length>3){
				   	       alert('安全级别字符不能超过3位数。');
					       document.getElementById("rest3").focus();
					        return;
				           }
						 if(!(parseInt(safe)>0&&parseInt(safe)<=100)){
						 	alert('安全级别字符应该在1-100之间。');
					       document.getElementById("rest3").focus();
					        return;
						 }
		             }   
		             //----------------------END-----------------------
					document.getElementById("usermanagesave").submit();
				
				}
				
				function setAreaCodeId(ids, names){
					document.getElementById("orgid").value = ids ;
					document.getElementById("orgNames").value = names ;
				}
				
				function setOrg(ids,names){
						//$("#userOrg").html("");
 						var p=document.getElementById("userOrg");    //st:  select的id
  						p.options.length=0;
  						
						var str = '<option  value="" >请选择</option>';
						if(ids){
							var id = ids.split(",");
							if(id.length==1){//当只有一个时默认显示它
								str = "";
							}
							var name = names.split(",");
							for (i = 0; i < id.length; i++) {
								str = str + "<option value=" + id[i]
										+">" + name[i] + "</option>";
							}
						}
						$("#userOrg").append(str);
				}
				
			/*	function setAreaCodeId(sid, name){
					document.getElementById("orgId").value = sid ;
					document.getElementById("orgName").value = name ;
				}*/
				
				function setUserType(sysmanagerid,managerid){
					document.getElementById("sysmanagerid").value = sysmanagerid ;
					document.getElementById("managerid").value = managerid ;
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
				function clearUSBKEY(){
					document.getElementById("userUsbkey").value="";
				}
				
				function setUSBKEY(){
					readUSBKEY();
					var userId=document.getElementById("userId").value;
					var currentUSBKEY=document.getElementById("userUsbkey").value;
					$.ajax({
						url:"<%=root%>/usermanage/usermanage!isUsbkeyExist.action",
						type:"post",
						data:{userId:userId,currentUSBKEY:currentUSBKEY},
						success:function(msg){
							if(msg!=""){
								alert(msg);
								document.getElementById("userUsbkey").value="";
							}
						},
						error:function(logininfo){
							alert("异步出错!");
						}
					});
				}
				
				
function CheckKey(){
   var rtn = ePass.GetSN();
   var ret = true;
   if (rtn == "8001")
   {
   alert ("您还没有安装驱动");
   ret = false;
   }
   else if (rtn == "8002")
   {
   alert ("您还没有插入key");
   ret = false;
   }
   else if ( rtn == "8003")
   {
   alert ("您插入了多把key，请只插入一把key");
   ret = false;
   }
   else {
   ret = true;
   }
   return ret;	
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
   else{
    	document.getElementById("userUsbkey").value=rtn;
	   //return rtn;	
   } 
}
function verification(img){
	//alert(img.value.lastIndexOf("."));
	var t = img.value.substr(img.value.lastIndexOf(".")+1,img.value.length);
	if(t==null||t=="") return;
	//if(t!="jpg"&&t!="JPG"&&t!="png"&&t!="PNG"){
	if(t!="png"&&t!="PNG"){
		alert("上传文件格式错误，请重新选择文件。");
		img.select();   
		document.selection.clear();
	}
}
	function deletePic(){
		if(confirm("确定要删除图片吗？")){
			var userId = $("#userId").val();
	   		$("#divpic").empty();
		   	$.post("<%=root%>/usermanage/usermanage!delPic.action?userId="+userId,function(data){
		   		if(data=="true"){
		   			alert("图片删除成功。")
		   		}else{
		   			alert("图片删除失败，请重新重新操作或与管理员联系。");
		   		}
		   	});
		}
	}
			</script>

		<!-- USBKEY functions
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
    document.getElementById("userUsbkey").value=results
	//document.form.FileContent.value=epsFileContent
End function
</script>
 -->
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
											<td class="table_headtd_img">
												<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
											</td>
											<td align="left">
												<strong>
												<s:if test="model.userId==null">
													新建用户
												</s:if>
												<s:else>
													编辑用户
												</s:else>
												</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
									                <tr>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									                 	<td class="Operation_input" onclick="testcode();">&nbsp;保&nbsp;存&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
									                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
									                 	<td class="Operation_input1" onclick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
								                  		<td width="6"></td>
									                </tr>
									            </table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<s:form id="usermanagesave" action="/usermanage/usermanage!save.action" theme="simple" enctype="multipart/form-data">
							<input type="hidden" id="hasPasswordEdited" name="hasPasswordEdited" value="0" />
							<input type="hidden" id="oldPassword" name="oldPassword" value="${model.userPassword}" />
							<input type="hidden" id="userId" name="userId"
								value="${model.userId}">
							<input type="hidden" id="extOrgId" name="extOrgId" value="${extOrgId}">
							<input type="hidden" id="iscode" name="iscode"
								value="${model.userSyscode}">
							<input type="hidden" id="isname" name="isname"
								value="${model.userLoginname}">
							<input type="hidden" id="iscardid" name="iscardid"
								value="${model.rest4}">
							<input type="hidden" id="isSupman" name="isSupman"
								value="${isSupman}">
							<input type="hidden" id="oldOrgid" name="oldOrgid"
								value="${model.orgId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="0" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;用户编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userSyscode"
											url="<%=path%>/usermanage/usermanage!compareCode.action"
											name="model.userSyscode" type="text" size="22" maxLength="21"
											value="${model.userSyscode}">&nbsp;
										<a  href="#" class="button" onclick="testMessage()">检测合法性</a>
									</td>
								</tr>
								<%--
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">用户是否删除：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.userIsdel" list="#{'0':'未删除','1':'已删除'}"
											listKey="key" listValue="value" style="width:11em" />
									</td>
								</tr>
								--%>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;登录账号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userLoginname"
											url="<%=path%>/usermanage/usermanage!checkLoginName.action"
											name="model.userLoginname" type="text" size="22" maxLength="50" onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')"
											value="${model.userLoginname}">&nbsp;
										<a  href="#" class="button" onclick="testname()">检测合法性</a>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;登录密码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userPassword" name="model.userPassword" type="password" size="22" value="${model.userPassword}" readonly="readonly">
										<input id="rePassword" name="rePassword" type="hidden" value="${rePassword}">
										<a  href="#" class="button" onclick="password()">设置密码</a>
									</td>
								</tr>								
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;用户姓名：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userName" name="model.userName" type="text" maxLength="50" onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')"
											size="22" value="${model.userName}">
									</td>
								</tr>
								<!--<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;所属机构：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<%--
										<input id="orgName" name="orgName" value="${orgName}" type="text" size="22" readonly="readonly">
										--%>
										<s:textfield id="orgName" name="orgName" size="22" readonly="true">
										</s:textfield>	
										<input id="orgId" name="model.orgId" value="${model.orgId}" type="hidden" size="22">
										<a  href="#" class="button" onclick="tree()">选择</a>
										<%--<input type="button" name="btnChooseBank" value="..." onclick="tree();" class="input_bg">--%>
									</td>
								</tr>
								-->
					<!--增加一人多岗--start-->
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;所属机构：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
										<input id="orgNames" name="orgNames"
											value="${orgNames}" type="text" size="44"
											readonly="readonly">
										<input id="orgid" name="orgid"
											value="${orgid}" type="hidden" size="22">
										<input type="button" name="btnChooseBank" value="..."
											onclick="tree();" class="input_bg">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;默认机构：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.orgId" id ="userOrg" list="userOrgList" listKey="orgId" listValue="orgName" headerKey="" headerValue="请选择"/>
									</td>
								</tr>
					<!--增加一人多岗--end-->										
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">用户类型：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.rest1"  list="#{'0':'个人','2':'处领导','1':'分管厅领导','3':'厅领导'}" listKey="key" listValue="value" style="width:11.6em"/> 
									</td>
								</tr>									
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">安全级别：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="rest3" name="model.rest3" type="text" maxLength="3" size="22" value="${model.rest3}">
										&nbsp;<font color="#999999">( 安全级别1~100 )</font>
									</td>
								</tr>								
								<%--
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">设置管理类型：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input type="hidden" id="sysmanagerid" name="sysmanagerid"
											value="${sysmanagerid}" onclick="setusertype();">
										<input type="hidden" id="managerid" name="managerid"
											value="${managerid}" onclick="setusertype();">
										<input type="button" name="btnChoose" value="设置管理类型"
											onclick="setusertype();" class="input_bg">
									</td>
								</tr>
								--%>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否启用：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="model.userId==null">
											默认启用
										</s:if>
										<s:else>
											<s:select name="model.userIsactive" list="#{'1':'开启','0':'关闭'}" listKey="key" listValue="value" style="width:11.6em"/>
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userSequence" name="model.userSequence" maxlength="10" value="${model.userSequence}" type="text" size="22">
										&nbsp;<font color="#999999">( 数值越小排名越前 )</font>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">USB_KEY：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userUsbkey" name="model.userUsbkey" type="password"
											size="22" value="${model.userUsbkey}" readOnly="true">&nbsp;
										<a  href="#" class="button" onclick="setUSBKEY()">设置USBKEY</a>
										<a  href="#" class="button" onclick="clearUSBKEY()">清空USBKEY</a>
										
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">公钥：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="pubkey" name="pubkey" type="text" size="22" value="${pubkey}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">电话：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userTel" name="model.userTel" type="text" size="22" maxLength="20" value="${model.userTel}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">手机号码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="rest2" name="model.rest2" type="text" size="22" maxLength="20" value="${model.rest2}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">领导联系人：</span>
									</td>
									<td class="td1" colspan="3" align="left"> 
									   	<input id="userAddrname" name="userAddrname" type="text" size="22" readonly="true" value="${userAddrname}">
										<input id="userAddrid" name="model.userAddr" type="hidden" maxLength="100" size="22" value="${model.userAddr}">
										<a  href="#" class="button" onclick="Orgtree();">设置</a>
									    <%--<input type="button" name="btnChooseBank" value="设置" onclick="Orgtree();" class="input_bg">--%>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">Email：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="userEmail" name="model.userEmail" type="text" maxLength="50" size="22" value="${model.userEmail}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">身份证号码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="identification"
										       url="<%=path%>/usermanage/usermanage!checkCardId.action"
										       name="model.rest4" type="text" maxLength="50" size="22" 
										       onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')"
										       value="${model.rest4 }">&nbsp;
										<a  href="#" class="button" onclick="testCardId()">检测合法性</a>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">职务：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="userDescription" name="model.userDescription" maxlength="1000" rows="4" cols="38">${model.userDescription }</textarea>
									</td>
								</tr>
								<security:authorize ifAllGranted="001-000200010006">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">签名图片：</span>
									</td>
									<td class="td1" align="left">
										<input id="upload" name="upload" type="file" class="upFileBtn" onchange="verification(this);"/>
									</td>
									<td class=td1 >
										<div id="divpic">
											<s:if test="#request.tempFilePath != null">
												<img src="${tempFilePath }" id="imageSrc"/>
												<a  class="button"  href="javascript:deletePic()">删除</a>
											</s:if>
										</div>
									</td>
								</tr>
								<tr>
								<tr>
									<td></td>
									<td colspan="3">
										<font color="#999999">上传图片类型为.PNG或.png， 尺寸为 120*90</font>
									</td>
								</tr>
								</security:authorize>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						</s:form>
					</td>
				</tr>
			</table>
			<!-- OBJECT id="ePass" style="LEFT: 0px; TOP: 0px" height="0" width="0"
				codeBase=/uums/usbkey/install.cab
				classid="clsid:C7672410-309E-4318-8B34-016EE77D6B58" name="ePass"
				VIEWASTEXT>
			</OBJECT -->
			<OBJECT id="ePass" style="LEFT: 0px; TOP: 0px" height="0" width="0" style="display:none"
				codeBase="<%=path%>/uums/usbkey/install.cab"
				classid="clsid:D6B4792E-2C8F-40AD-AFB8-28C5FC4D50E0" id="ePass"  name="ePass"
				VIEWASTEXT>
			</OBJECT>
		</DIV>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<link href="<%=frameroot%>/css/properties_windows_add.css"
	type="text/css" rel=stylesheet>
<script language="javascript"
	src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script type="text/javascript" src="<%=path%>/oa/js/myinfo/md5.js"></script>
<script type="text/javascript" src="<%=path%>/common/script/Password.js"></script>
<script src="<%=path%>/common/js/common/common.js"
	type="text/javascript"></script>
<title>个人设置</title>
<style>
.table_headtd_img {
	padding-left: 0px;
}

.biao_bg1 {
	width: 100px;
}
</style>

<script type="text/javascript">
		window.onload = function() 
		{ 
			document.getElementById('homeAddress').onkeyup = function() 
			{    
		    if(this.value.length > 64) 
		    	$("#homeAddress").val(this.value.substring(0,64));
			} 
		} 
			function testWrite(){
				document.getElementById("myForm").action="<%=path%>/myinfo/myInfo!sendMail.action";
				document.getElementById("myForm").submit();
			}
			//表单重置
		function resettAll(){
			document.getElementById("myForm").reset();
		}
		
			function chMobilePhone(src)
			{ 
			   if(/^13\d{9}$/g.test(src)||(/^15\d{9}$/g.test(src))||(/^18\d{9}$/g.test(src))){
			         return true;
			    }else{
			        return false;
			   }
			} 
			
			//匹配国内电话号码(0511-4405222 或 021-87888822)  
			function check_validate(value){ 
				//var reg = /^(\d{3,4})-(\d{7,8})/g; 
				 //var reg=/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)/;
				var reg=/(^(\d{3,4}-)?\d{7,8})$|(^1[3|4|5|8][0-9]{9})/;

				var re = value.match( reg ); 
				if(re!=null&&re.length>0){
					return true;
				}else{
					return false;
				}
			} 
			
			//验证QQ号码
			function check_qq(value){
				var reg=/^[1-9]\d{4,12}$/g;
				if(reg.test(value)){
					return true;
				}else{
					return false;
				}
			}
			
			
			// 判断输入是否是有效的电子邮件   
			function CheckEmail(str)   
			{   
			    var result=str.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/); 
			   // /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/; 
			    
			    if(result==null) return false;   
			    return true;   
			}   
			
			//去掉左空格
			function ltrim(s){ 
					return s.replace(/(^\s*)/g, "");
 			} 
 			
 			//去右空格; 
			function rtrim(s){ 
  				return s.replace(/(\s*$)/g, "");
 			} 
			
			//去掉字符串的左右空格
			function trim(s){ 
    			//s.replace(/(^\s*)|(\s*$)/g, "");
 				return rtrim(ltrim(s)); 
 			} 
 			 
 			//匹配中国邮政编码(6位)   
			function ispostcode(str)   
			{   
			 	var reg = /^\d{6}$/;
			    var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
			}  
			
			//小灵通验证
			function   PhoneCheck(str){
				var   reg1   =   /^\d{8}$/;
				if(!reg1.test(str))   
				{   
					return   false;   
				}   		
			} 
 	
 			
 			function checkNum(str){
 				var reg=/^[0-9]*$/;
 				var result=str.match(reg);   
			    if(result==null) return false;   
			    return true;   
 			}
 			
             //处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外  
           function banBackSpace(e){     
                     var ev = e || window.event;//获取event对象     
                     var obj = ev.target || ev.srcElement;//获取事件源     
      
                     var t = obj.type || obj.getAttribute('type');//获取事件源类型    
      
                   //获取作为判断条件的事件类型  
                    var vReadOnly = obj.getAttribute('readonly');  
                    var vEnabled = obj.getAttribute('enabled');  
             //处理null值情况  
           vReadOnly = (vReadOnly == null) ? false : vReadOnly;  
          vEnabled = (vEnabled == null) ? true : vEnabled;  
      
           //当敲Backspace键时，事件源类型为密码或单行、多行文本的，  
         //并且readonly属性为true或enabled属性为false的，则退格键失效  
             var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea")   
                && (vReadOnly==true || vEnabled!=true))?true:false;  
     
        //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效  
         var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")  
                ?true:false;          
      
         //判断  
                if(flag2){  
                     return false;  
                  }  
            if(flag1){     
                      return false;     
                    }     
                }  
  
//禁止后退键 作用于Firefox、Opera  
document.onkeypress=banBackSpace;  
//禁止后退键  作用于IE、Chrome  
document.onkeydown=banBackSpace;
 			
$(document).ready(function(){
	$.fn.password( {
		passwordInput : 'newPassword',
		checkInput : 'rePassword',
		strengthInfoText : 'infoText',
		strengthInfoBar : 'infoBar'
	});
});
 			
			function submitForm(){
				//var prsnName=trim(document.getElementById("prsnName").value);	//控制姓名为必填
				var prsnTel1=document.getElementById("prsnTel1");	//固定电话1
				var prsnTel2=document.getElementById("prsnTel2");	//固定电话2
				var prsnMobile1=document.getElementById("prsnMobile1");	//手机号码1
				var prsnMobile2=document.getElementById("prsnMobile2");	//手机号码2
				var prsnQq=document.getElementById("prsnQq");	//qq
				var prsnMail1=trim(document.getElementById("prsnMail1").value);	//电子邮件1
				var prsnMail2=trim(document.getElementById("prsnMail2").value);	//电子邮件2
				var newPassword=trim(document.getElementById("newPassword").value);
				var rePassword=trim(document.getElementById("rePassword").value);
				var homeZipcode=trim(document.getElementById("homeZipcode").value)	//邮政编号
				var prsnPhs=trim(document.getElementById("prsnPhs").value)			//小灵通
				//var defaultMail=trim(document.getElementById("defaultMail").value)	//默认电子邮件
				//var defaultMailPort=trim(document.getElementById("defaultMailPort").value)	//默认电子邮件端口
				//var defaultMailSys=document.getElementById("defaultMailSys").value;
				//var defaultMailUser=document.getElementById("defaultMailUser").value;
				//var defaultMail=document.getElementById("defaultMail").value;
				//var defaultMailPsw=document.getElementById("defaultMailPsw").value;
				//var defaultMailPort=document.getElementById("defaultMailPort").value;
				var oldPassword=document.getElementById("oldPassword").value;
				var prsnFax=document.getElementById("prsnFax").value;//传真
				
				//var tel=document.getElementById("tel");	//电话
				//var mailnum=trim(document.getElementById("mailnum").value)	//邮政编号
				//var fax=document.getElementById("fax").value;//传真
				
				//if(prsnName==null||prsnName==""||prsnName=="null"||prsnName==undefined){ //控制姓名为必填
					//alert("姓名不能为空，请输入姓名！");
					//document.getElementById("prsnName").focus();
					//return false;
				//}else 
				if(prsnPhs!=""&&prsnPhs!=null&&PhoneCheck(prsnPhs)==false){	//小灵通
					alert("小灵通号码格式有误，请重新输入任意8位数字。");
					document.getElementById("prsnPhs").focus();
					return false;
				}else if(prsnFax!=""&&prsnFax!=null&&check_validate(prsnFax)==false){	//传真
					alert("传真号码格式有误，请重新输入\n传真号码如：028-67519441、0839-8777222、028-6545124。");
					document.getElementById("prsnFax").focus();
					return false;
				//}else if(fax!=""&&fax!=null&&check_validate(fax)==false){	//传真
					//alert("传真号码格式有误，请重新输入\n传真号码如：028-67519441、0839-8777222、028-6545124");
					//document.getElementById("fax").focus();
					//return false;
				}else if(trim(prsnTel1.value)!=""&&trim(prsnTel1.value)!=null&&check_validate(trim(prsnTel1.value))==false){	//固定电话1
					alert("固定电话1格式有误，请重新输入\n电话号码如：028-67519441、0839-8777222、028-6545124。");
					prsnTel1.focus();
					return false;
				}else if(trim(prsnTel2.value)!=""&&trim(prsnTel2.value)!=null&&check_validate(trim(prsnTel2.value))==false){	//固定电话2
					alert("固定电话2格式有误，请重新输入\n电话号码如：028-67519441、0839-8777222、028-6545124。");
					prsnTel2.focus();
					return false;
				}else if(trim(prsnMobile1.value)!=""&&chMobilePhone(trim(prsnMobile1.value))==false){//手机号码1
					alert("手机号码1格式有误，请重新输入。");
					prsnMobile1.focus();
					return false;
				}else if(trim(prsnMobile2.value)!=""&&chMobilePhone(trim(prsnMobile2.value))==false){//手机号码2
					alert("手机号码2格式有误，请重新输入。");
					prsnMobile2.focus();
					return false;
				}else if(trim(prsnQq.value)!=""&&check_qq(trim(prsnQq.value))==false){				//qq
					alert("QQ号码只能是5～13位数字，请重新输入。");
					prsnQq.focus();
					return false;
				}else if(prsnMail1!=""&&CheckEmail(prsnMail1)==false){					//电子邮件1
					alert("电子邮件1格式有误，格式如：example@gmail.com。")
					document.getElementById("prsnMail1").focus();
					return false;
				}else if(prsnMail2!=""&&CheckEmail(prsnMail2)==false){					//电子邮件2
					alert("电子邮件2格式有误，格式如：example@gmail.com。")
					document.getElementById("prsnMail2").focus();
					return false;
				}else if(homeZipcode!=""&&ispostcode(homeZipcode)==false){				//邮政编号
					alert("邮政编号格式有误，邮政编码格式如：343205。")
					document.getElementById("homeZipcode").focus();
					return false;
				//}else if(defaultMail!=""&&CheckEmail(defaultMail)==false){					//电子邮件2
					//alert("默认邮件格式有误，格式如：example@gmail.com")
					//document.getElementById("prsnMail2").focus();
					//return false;
				//}
				//else if(trim(tel.value)!=""&&trim(tel.value)!=null&&check_validate(trim(tel.value))==false){	//电话
					//alert("电话格式有误，请重新输入\n电话号码如：028-67519441、0839-8777222、028-6545124");
					//tel.focus();
					//return false;
				//}
				//else if(defaultMailPort!=""&&checkNum(defaultMailPort)==false){					//电子邮件2
					//alert("默认邮件端口格式有误，只能输入数字！")
					//document.getElementById("defaultMailPort").focus();
					//return false;
				}else if(oldPassword==null||oldPassword==""||oldPassword=="null"){
			        alert("原始密码不能为空，请输入原始密码。");
					return false;
			//}else if(newPassword==null||newPassword==""||newPassword=="null"){
			        //alert("新密码不能为空，请输入新密码。");
					//return false;
			//}else if(rePassword==null||rePassword==""||rePassword=="null"){
			        //alert("确认密码不能为空，请输入确认密码。");
					//return false;
			}else if(newPassword!=rePassword){										//新密码和确认密码
					alert("新密码和确认密码不一致，请重新输入。");
					return false;
			//}else if(newPassword.length>6){
					//alert("新密码长度不能超过6位");
					//document.getElementById("newPassword").focus();
					//return false;
			// }else if(oldPassword.length>6){
					//alert("原始密码长度不能超过6位");
					//document.getElementById("oldPassword").focus();
					//return false;
			// }else if(rePassword.length>6){
					//alert("确认密码长度不能超过6位");
					////document.getElementById("rePassword").focus();
					//return false;
			 }
			
			
			
			//else if(trim(tel.value)!=""&&trim(tel.value)!=null&&check_validate(trim(tel.value))==false){
					///alert("电话格式有误，请重新输入\n电话号码如：028-67519441、0839-8777222、028-6545124");
					//return false;
				//}
				//else if(defaultMailUser.length>12){
					//alert("默认邮箱用户名长度不能超过12位");
					//document.getElementById("defaultMailUser").focus();
					//return false;
				//}
					
					
				/**else if(defaultMail==""){
					alert("默认Email不能为空,请您正确输入");
					document.getElementById("defaultMail").focus();
					return false;
				}else if(defaultMailUser==""){
					alert("默认邮箱用户名不能为空");
					document.getElementById("defaultMailUser").focus();
					return false;
				}else if(defaultMailSys==""){
					alert("发送服务器配置不能为空！");
					document.getElementById("defaultMailSys").focus();
					return false;
				}else if(defaultMailPsw==""){
					alert("默认邮箱密码不为空！");
					document.getElementById("defaultMailPsw").focus();
					return false;
				}else if(defaultMailPort==""){
					alert("发送服务器端口不为空！");
					document.getElementById("defaultMailPort").focus();
					return false;
				}else if(oldPassword==""){
					alert("请您填写原始密码后再进行修改个人信息");
					document.getElementById("oldPassword").focus();
					return false;
				}*/else{
			<%--		var file = document.getElementById("upload").value;
					if(file != ""){
						var index = file.lastIndexOf(".");
						var ext = file.substring(index+1, file.length);
						if(ext != "png" && ext != "PNG"){
							alert("图片类型不正确，请重新选择。");
							return;
						}
					}
					
					//创建临时img对象
					//因为仅仅用于测试上传的图片是否可用，因此隐藏（display:none）
					$('body').append("<img id='tempDom' style='display:none'/>");
					var imgObj = $('#tempDom')[0];
					try{
						//设置Image对象的滤镜效果，用于客户端装载图片（Image对象尺寸适应图像本身）
						imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);";
						//通过滤镜获取本地图片 ，如果图片本身不是可用的的资源则会出现异常，进入catch程序段
						imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = $('#upload').val();
						//如果上一步骤顺利通过，则可以通过下行代码获取图像尺寸
				//alert($(imgObj).width()+ "     " + $(imgObj).height());
						//测试完毕，移除临时DOM对象
						$(imgObj).remove();
					}catch(e){
						alert('您上传的图片格式不正确或已经损坏，请重新上传。');
						//清除file控件的value值
						//出于安全考虑，IE禁止通过JS代码改变file控件中资源的路径(这里即$('#upload').val())
						//下面代码通过另一种方式解决此问题：  将原file控件移除后，再载入一个除了value为空其它属性完全一样的全新的file控件
						var outerHTML = $('#upload')[0].outerHTML;
						var value = $('#upload').val();
						//先复制原控件其它属性，再替换原控件
						$('#upload').replaceWith(outerHTML.substring(0,outerHTML.indexOf(value)) + outerHTML.substring(outerHTML.indexOf(value) + value.length));
						$(imgObj).remove();
					    return false;
					}
				--%>	
					
					
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/myinfo/myInfo!chargePass.action",
						data:"md5pass="+hex_md5(oldPassword),
						success:function(msg){
							if(msg=="true"){
								if(newPassword!=""){
									document.getElementById("newPassword").value=hex_md5(newPassword);
								}
								//alert("保存成功。");
								$("#myForm").attr("action","<%=root%>/myinfo/myInfo!save.action");
								document.getElementById("myForm").submit();
							}else{
								alert("原始密码错误请您重新填写。");
							}
						}
					});
				}
			}
			$(document).ready(function(){
			
			
				$("#testmail").click(function(){
					if($("#defaultMail").val()==""||!CheckEmail($("#defaultMail").val())){
				        alert("默认Email不能为空或者Email格式不正常,请您正确输入。");
						$("#defaultMail").focus();
					}else if($("#defaultMailUser").val()==""){
						alert("默认邮箱用户名不能为空。");
						$("#defaultMailUser").focus();
					}else if($("#defaultMailPsw").val()==""){
						alert("默认邮箱密码不能为空。");
						$("#defaultMailPsw").focus();
					}else if($("#defaultMailSys").val()==""){
						alert("发送服务器配置不能为空。");
					  $("#defaultMailSys").focus();
					}else if($("#defaultMailPort").val()==""||!checkNum($("#defaultMailPort").val())){
						alert("默认端口为空或存在非数字。");
						$("#defaultMailPort").focus();
					}else{
						$.ajax({
							type:"post",
							dataType:"text",
							url:"<%=root%>/myinfo/myInfo!sendMail.action",
							data:"configModel.defaultMail="+$("#defaultMail").val()+"&configModel.defaultMailSsl="+$("#defaultMailSsl").val()
								  +"&configModel.defaultMailUser="+$("#defaultMailUser").val()+"&configModel.defaultMailPsw="+$("#defaultMailPsw").val()
								  +"&configModel.defaultMailSys="+$("#defaultMailSys").val()+"&configModel.defaultMailPort="+$("#defaultMailPort").val(),
							success:function(msg){
								if(msg=="true"){
									alert("邮件配置验证成功。");
								}else{
									alert("邮件配置验证失败,请您验证您的配置信息是否正确、网络连接是否正常。");
								}
							}
						});
					}
				});
			});
			
			function doEntry(obj){
				if(obj.value == "1"){
					//秘书代办
					var Height=600;
					var Width=700;
					var left=(screen.availWidth-10-Width)/2;
					var top=(screen.availHeight-30-Height)/2;
					var Url = "<%=root%>/workflowDelegation/action/processDelegation!input.action?type=add";
					var win = window.open(Url,'myinfo','height='+Height+', width='+Width+', top='+top+', left='+left+', toolbar=no, ' + 
		  							'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
				}
			}
			
			function deletePic(){
				if(confirm("确定要删除图片吗？")){
				   	$.post("<%=root%>/myinfo/myInfo!delPic.action",function(data){
				   		if(data=="true"){
				   			alert("图片删除成功。");
				   			window.location.reload();
				   		}else{
				   			alert("图片删除失败，请重新重新操作或与管理员联系。");
				   		}
				   	});
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
		</script>
<style>
#homeAddress {
	height: 100px;
}
</style>



</head>
<body class=contentbodymargin>
	<DIV id=contentborder align=center>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<s:form id="myForm" action="/myinfo/myInfo!input.action"
			enctype="multipart/form-data">
			<input type="hidden" id="prsnId" name="prsnId"
				value="${model.prsnId }">
			<input type="hidden" id="mailnum" name="mailnum"
				value="${model.mailnum }">
			<input type="hidden" name="delPics" id="delPics">
			<input type="hidden" id="userRest1 name="
				userRest1"
					value="${userRest1 }">

			<div align=left style="width: 95%;padding:5px;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table border="0" width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td class="table_headtd_img"><img
									src="<%=frameroot%>/images/ico/ico.gif">&nbsp;</td>
								<td align="left"><strong>个人设置</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_l.gif" /></td>
											<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
											<td width="7"><img
												src="<%=frameroot%>/images/ch_h_r.gif" /></td>
											<td width="5"></td>
											<td width="8"><img
												src="<%=frameroot%>/images/ch_z_l.gif" /></td>
											<td class="Operation_input1" onclick="resettAll();">&nbsp;重&nbsp;置&nbsp;</td>
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
			</div>
			<fieldset style="width: 95%">
				<legend>
					<span class="wz">个人设置 </span>
				</legend>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="biao_bg1" align="right"><span class="wz"><font
								color=red>*</font>&nbsp;姓名：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnName" name="prsnName"
							value="${model.prsnName}" type="text" size="25" maxlength="10"
							readonly="readonly">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">&nbsp;性别：&nbsp;</span>
						</td>

						<td class="td1">
							<%--<s:property value="prsnSex" />
								&nbsp;
								--%> &nbsp;&nbsp;<s:select cssStyle="width: 178px"
								list="#{'男':'0','女':'1'}" listKey="value" listValue="key"
								id="model.prsnSex" name="model.prsnSex"></s:select>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">昵称：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnNickname"
							name="prsnNickname" value="${model.prsnNickname}" type="text"
							size="25" maxlength="20">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">生日：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <strong:newdate
								name="model.prsnBirthday" id="prsnBirthday"
								dateform="yyyy-MM-dd" dateobj="${model.prsnBirthday}"
								width="178px" skin="whyGreen" isicon="true"></strong:newdate>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">小灵通：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnPhs" name="prsnPhs"
							value="${model.prsnPhs}" type="text" size="25">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">传真：&nbsp;</span>
						</td>
						<td " class="td1">&nbsp; <input id="prsnFax" name="prsnFax"
							value="${model.prsnFax}" type="text" size="25" maxlength="30">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">固定电话1：&nbsp;</span>
							<%--								 例如：028-67519441、0839-8777222、028-6545124--%>
						</td>
						<td class="td1">&nbsp; <input id="prsnTel1" name="prsnTel1"
							value="${model.prsnTel1}" type="text" size="25">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">固定电话2：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnTel2" name="prsnTel2"
							value="${model.prsnTel2}" type="text" size="25">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">手机号码1：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnMobile1"
							name="prsnMobile1" value="${model.prsnMobile1}" type="text"
							size="25">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">手机号码2：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnMobile2"
							name="prsnMobile2" value="${model.prsnMobile2}" type="text"
							size="25">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">MSN：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnMsn" name="prsnMsn"
							value="${model.prsnMsn}" type="text" size="25" maxlength="20">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">QQ：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnQq" name="prsnQq"
							value="${model.prsnQq}" type="text" size="25" maxlength="20">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">电子邮件1：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnMail1" name="prsnMail1"
							value="${model.prsnMail1}" type="text" size="25">
						</td>
						<td class="biao_bg1" align="right"><span class="wz">电子邮件2：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="prsnMail2" name="prsnMail2"
							value="${model.prsnMail2}" type="text" size="25">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right" valign="top"><span
							class="wz">家庭住址：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <!--  <input id="homeAddress" name="homeAddress"
									value="${model.homeAddress}" type="text" size="25"
									maxlength="60">--> <textarea id="homeAddress"
								name="homeAddress" cols="24" style="width:180px" maxlength="60">${model.homeAddress }</textarea>
						</td>
						<td class="biao_bg1" align="right"><span class="wz">邮政编码：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="homeZipcode"
							name="homeZipcode" value="${model.homeZipcode}" type="text"
							size="25" maxlength="20">
						</td>
					</tr>
					<tr>
						<td class="table_td"></td>
						<td></td>
					</tr>
				</table>
			</fieldset>
			<br>
			<!--  <fieldset style="width: 95%">
					<legend>
						<span class="wz">组织</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">
						<tr>

							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">机构名称：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="company" value="${model.company}" type="text"
									size="25" maxlength="25">
							</td>
							<td width="20%" class="biao_bg1" align="right">
								<span class="wz">职位：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="position" value="${model.position}" type="text"
									size="25" maxlength="25">
							</td>

						</tr>
						<tr>

							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">国家：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="state" value="${model.state}" type="text" size="25"
									maxlength="25">
							</td>
							<td width="20%" class="biao_bg1" align="right">
								<span class="wz">部门：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="dept" value="${model.dept}" type="text" size="25"
									maxlength="25">
							</td>

						</tr>
						<tr>

							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">省：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="province" value="${model.province}" type="text"
									size="25" maxlength="25">
							</td>
							<td width="20%" class="biao_bg1" align="right">
								<span class="wz">电话：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="tel" value="${model.tel}" type="text" size="25"
									maxlength="25">
							</td>

						</tr>
						<tr>

							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">城市：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="city" value="${model.city}" type="text" size="25"
									maxlength="25">
							</td>
							<td width="20%" class="biao_bg1" align="right">
								<span class="wz">传真：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input name="fax" value="${model.fax}" type="text" size="25"
									maxlength="30">
							</td>

						</tr>
					</table>
				</fieldset>-->
			<br>
			<fieldset style="width: 95%">
				<legend>
					<span class="wz">修改密码</span>
				</legend>
				<table width="100%" border="0" cellpadding="0" cellspacing="1">
					<tr>

						<td class="biao_bg1" align="right"><span class="wz"><font
								color=red>*</font>&nbsp;原始密码：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="oldPassword"
							name="oldPassword" type="password" size="25" maxlength="30">
						</td>
						<td class="biao_bg1" align="right"><span class="wz"
							style="margin:4px 4px 8px 0;">新密码：&nbsp;</span>
						</td>
						<%--<td  class="td1">
								&nbsp;
								<input id="newPassword" name="newPassword" type="password"
									size="25" maxlength="30">
							</td>
						--%>

						<td class="td1">&nbsp;<input id="newPassword"
							name="newPassword" type="password" size="25" maxlength="20">

						</td>

					</tr>
					<tr>
						<td class="biao_bg1" align="right"><span class="wz">确认密码：&nbsp;</span>
						</td>
						<td class="td1">&nbsp; <input id="rePassword"
							name="rePassword" type="password" size="25" maxlength="30">
						</td>
						<td width="120" height="36" class="biao_bg1"><span class="wz">&nbsp;&nbsp;密码强度：</span>
						</td>
						<td class="td1">

							<div
								style="clear: both; width: 70%;float:left; margin:4px 8px 8px 4px; height:7px; font-size:7px; background: gray">
								<div id="infoBar"></div>
							</div>
							<div id="infoText" style=" width: 20%; float:right"></div>
						</td>

					</tr>
				</table>
			</fieldset>
			<!--  
				<fieldset style="width: 95%">
					<legend>
						<span class="wz">默认工具设置</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">						
						<tr>
							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">手机：</span>
							</td>
							<td class="td1" colspan="3">
								&nbsp;
								<input id="newpass" name="newpass" type="text"
									size="25" maxlength="30">
							</td>
						</tr>
					</table>
				</fieldset>
				-->
			<!-- <fieldset style="width: 95%">
					<legend>
						<span class="wz">默认Email设置</span>
					</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						class="table1">
						<tr>
							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">Email：</span>
								<input id="prsnConfId" name="configModel.prsnConfId"
									value="${configModel.prsnConfId}" type="hidden" size="25">
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input id="defaultMail" name="configModel.defaultMail"
									value="${configModel.defaultMail}" type="text" size="25">
							</td>
							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">经SSL验证：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<s:select cssStyle="width: 150px" list="#{'否':'0','是':'1'}"
									listKey="value" listValue="key" id="defaultMailSsl"
									name="configModel.defaultMailSsl"></s:select>
							</td>
						</tr>
						<tr>
							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">邮箱用户名：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input id="defaultMailUser" name="configModel.defaultMailUser"
									value="${configModel.defaultMailUser}" type="text" size="25">
							</td>
							<td width="20%" height="21" class="biao_bg1" align="right">
								<span class="wz">邮箱密码：</span>
							</td>
							<td width="30%" class="td1">
								&nbsp;
								<input id="defaultMailPsw" name="configModel.defaultMailPsw"
									value="${configModel.defaultMailPsw}" type="password" size="25"
									maxlength="30">
							</td>
						</tr>
						<tr>
							<td width="20%" height="21" class="biao_bg1" align="right" title="请填写发送Email的服务器配置">
								<span class="wz">发送服务器：</span>
							</td>
							<td width="30%" class="td1" title="请填写发送Email的服务器配置">
								&nbsp;
								<input id="defaultMailSys" name="configModel.defaultMailSys"
									value="${configModel.defaultMailSys }" type="text" size="25"
									maxlength="30">
							</td>
							<td width="20%" height="21" class="biao_bg1" align="right" title="请填写发送Email的服务器端口">
								<span class="wz">发送端口：</span>
							</td>
							<td width="30%" class="td1" title="请填写发送Email的服务器端口">
								&nbsp;
								<input id="defaultMailPort" name="configModel.defaultMailPort"
									value="${configModel.defaultMailPort}" type="text" size="25"
									maxlength="30">
							</td>
						</tr>
						
					</table>
				</fieldset>-->
			<fieldset style="width: 95%;display:none;">
				<legend>
					<span class="wz">签名设置</span>
				</legend>
				<table width="100%" border="0" cellpadding="0" cellspacing="1">
					<tr>
						<td class="biao_bg1" align="right" title="只能上传.jpg和.png类型的图片">
							<span class="wz">图片上传：&nbsp;</span>
						</td>
						<td title="只能上传.jpg和.png类型的图片">&nbsp; <input id="upload"
							name="upload" type="file" class="upFileBtn"
							onchange="verification(this);" />
						</td>
						<td class="biao_bg1" align="right"><span class="wz">预览图片：&nbsp;</span>
						</td>

						<td class=td1>
							<div id="divpic">
								<s:if test="model.tempFilePath != null">
									<img src="${model.tempFilePath }" id="imageSrc" />
									<a class="button" href="javascript:deletePic()">删除</a>
								</s:if>
								<s:else>
										未上传图片,无预览
									</s:else>
							</div>
						</td>
					<tr>
						<td></td>
						<td colspan="2"><font color="#999999">上传图片类型为.PNG或.png，
								尺寸为 120*90</font>
						</td>

					</tr>
					</tr>
				</table>
			</fieldset>
			<%-- <table width="95%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="40%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									

									<!--  <td width="15%" align="right">
										<input id="testmail" name="testmail" type="button"
											class="input_bg" value="测试发邮件">
									</td>-->
									<td width="25%" height="34">
										&nbsp;
									</td>
									<td width="20%" align="center">
										<input id="save" name="save" type="button" class="input_bg"
											onclick="submitForm()" value="保存">
									</td>
									<td width="25%" height="34">
										&nbsp;
									</td>
									<td width="15%" align="left">
										<input name="button" type="reset" class="input_bg" value="重置">
									</td>
									
								</tr>
							</table>
						</td>
					</tr>
				</table> --%>
		</s:form>
	</DIV>
</body>
</html>

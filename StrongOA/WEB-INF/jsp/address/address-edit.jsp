<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>查看联系人</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height: 100%
}
</style>
	<script type="text/javascript">
		$(document).ready(function(){
			$("input,textarea").attr("readonly",true);
			var allEmail = $("#email").val();
			var default_Email = $("#default").val();
			
			if(""!=allEmail&&null!=allEmail&&"null"!=allEmail){
				var all = allEmail.split(",");
				for(var j=0;j<all.length;j++){
					if(all[j] == default_Email){
						$("#mailList").append("<option style=\"color: red;\" id=opt"+j+" value="+all[j]+" >"+all[j]+"</option>");
					}else{
						$("#mailList").append("<option id=opt"+j+" value="+all[j]+" >"+all[j]+"</option>");
					}
				}
			}
			
			var sex = $("#address_sex").val();
			if("1"==sex){
				$("#opt_man").attr("selected","selected");
			}else{
				$("#opt_woman").attr("selected","selected");
			}
		
			$("#btn_editaddress").click(function(){
				var name = $("#realname").val();
				$("#email").val(getEmail());
				var email = getEmail();
				var emails = email.split(",");
				var defaultEmail = getDefaultEmail();
				if(defaultEmail == ""){
					if(emails!= ""){
						$("#default").val(emails[0]);
					}
				}else{
					$("#default").val(getDefaultEmail());
				}
				if($.trim(name) == ""){
					alert("姓名不能为空，请填写。");
					return false;
				}
				//验证手机号码patrn1="^(13\d{9})$"patrn2="^(159\d{8})$" 
				var mobile1 = $.trim($("#mobile1").val());
				var mobile2 = $.trim($("#mobile2").val());
				var reg_mobile13= /^(13\d{9})$/;
				var reg_mobile159=/^(15\d{9})$/;
				var reg_mobile189=/^(18\d{9})$/;
				if(mobile1!=""){
					if(!reg_mobile13.test(mobile1) && !reg_mobile159.test(mobile1)&& !reg_mobile189.test(mobile1)){
						alert("手机1输入有误，请确认。");
						return false;
					}
				}
				if(mobile2!=""){
					if(!reg_mobile13.test(mobile2) && !reg_mobile159.test(mobile2)&& !reg_mobile189.test(mobile2)){
						alert("手机2输入有误，请确认。");
						return false;
					}
				}
				//验证电话号码patrn3="^(\d{7,8})$"patrn4="^(0\d{2,3}\-\d{6,8}(\-\d{1,4})?)$"
				var tel1 = $.trim($("#tel1").val());
				var tel2 = $.trim($("#tel2").val());
				var cotel1 = $.trim($("#cotel1").val());
				var cotel2 = $.trim($("#cotel2").val());
				var fax = $.trim($("#fax").val());
				var reg_tel1 = /^(\d{7,8})$/;
				var reg_tel2 = /^(0\d{2,3}\-\d{6,8}(\-\d{1,4})?)$/;
				if(""!=tel1){
					if(!reg_tel1.test(tel1) && !reg_tel2.test(tel1)){
						alert("电话号码输入有误，请确认。");
						return false;
					}
				}
				if(""!=tel2){
					if(!reg_tel1.test(tel2) && !reg_tel2.test(tel2)){
						alert("电话号码输入有误，请确认。");
						return false;
					}
				}
				if(""!=cotel1){
					if(!reg_tel1.test(cotel1) && !reg_tel2.test(cotel1)){
						alert("电话号码输入有误，请确认。");
						return false;
					}
				}
				if(""!=cotel2){
					if(!reg_tel1.test(cotel2) && !reg_tel2.test(cotel2)){
						alert("电话号码输入有误，请确认。");
						return false;
					}
				}
				if(""!=fax){
					if(!reg_tel1.test(fax) && !reg_tel2.test(fax)){
						alert("传真号码输入有误，请确认。");
						return false;
					}
				}
				//验证MSN账号
				var msn = $.trim($("#msn").val());
				var reg_msn=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
				if(""!=msn){
					if(!reg_msn.test(msn)){
						alert("MSN账号输入有误，请确认。");
						return false;
					}
				}
				//验证QQ号码
				var qq = $.trim($("#qq").val());
				if(isNaN(qq)){
					alert("QQ账号输入有误，请确认。");
					return false;
				}
				//验证邮编
				var zipcode = $.trim($("#zipcode").val());
				var reg_zipcode = /^[1-9]\d{5}$/;
				if(""!=zipcode){
					if(!reg_zipcode.test(zipcode)){
						alert("邮政编号输入有误，请确认。");
						return false;
					}
				}
				//验证主页
				var url = $.trim($("#url").val());
				var reg_url = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
				if(""!=url){
					if(!reg_url.test(url)){
						alert("主页地址输入有误，请确认。");
						return false;
					}
				}
				
				//验证爱好
				var likeThing =$("#likeThing").html();
				if(likeThing.length>512){
					alert("爱好内容过长，请控制在512个字之内。");
					$("#likeThing").html($("#likeThing").html().substring(0,512));
					return false;
				}
				
				//验证 地址 文本域长度
				var address = $("#address").val();
				if(address.length>128){
					alert("对不起，您的通讯录中地址信息过长，请控制在128个字以内！");
					return false;
				}
				
				//验证 附注信息 文本域长度
				var addressRemark = $("#addressRemark").val();
				if(addressRemark.length>1000){
					alert("对不起，您的通讯录中附注信息信息过长，请控制在1000个字以内！");
					return false;
				}
				
				$("form").submit();
			});
			
			$("#btnAdd").click(function(){
				var email = $("#newMail").val();
				var reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
				var i = 0;
				if($.trim(email) != ""){
					if(reg.test(email)){
						if(!isInArray(getEmail(),email)){
							$("#mailList").append("<option id=opt"+(++i)+" value="+email+" >"+email+"</option>");
						}
						$("#newMail").val("");
					}else{
						alert("Email输入有误，请确认。");
						$("#newMail").get(0).focus();
						return false;
					}
				}
			});
			
			$("#btnEdit").click(function(){
				if($("#mailList").val()!=null ){
					var ret=OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=address/address-editmail.jsp","230","150",window);
				}
			});
			
			$("#btnDel").click(function(){
				var mailList = $("#mailList").get(0);
				if($("#mailList").val()!=null){
					mailList.removeChild(mailList.options[mailList.selectedIndex]);
				}
			});
			
			$("#btnDefault").click(function(){
				var mailList = $("#mailList").get(0);
				if($("#mailList").val()!=null){
					$("select>option").each(function(){
						$(this).css("color",'');
					});
					var opt = mailList.options[mailList.selectedIndex];
					opt.style.color='red';
				}
				if((typeof opt)=="object"){
					alert("设置默认邮箱["+opt.value+"]成功！");
				}
			});
			
			var message = $(".actionMessage").text();
			if(message!=null && message!=""){
				//alert(message);
				window.returnValue = "sucess";
				window.close();
			}
			$("#btnCancel").click(function(){window.close();});
			
		});	
		
		function getDefaultEmail(){
			var ret = "";
				var mailList = $("#mailList").get(0);
				$("select>option").each(function(){
					var color = $(this).css("color");
					if(color && "red"==color){
						ret = this.text;
					}
				});
			return ret;
		}
		
		function isInArray(a,b){
			var array = a.split(",");
			for(i=0;i<array.length;i++){
				if(array[i] == b){
					return true;
				}else{
					continue;
				}
			}
			return false;
		}
		
		function getEmail(){
			var result = "";
			$("select>option").each(function(){//获取Email,存在的不再添加
				if("1"!=this.value && "0"!=this.value){
					result+=this.text+",";
				}
			});
			if(result.length>0){
				result = result.substring(0,result.length-1);
			}
			return result;
		}
		
	
	</script>
	</head>
	<base target="_self">
	<body class="contentbodymargin">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder cellpadding="0">
		<label style="display: none;"><s:actionmessage/></label>
			<form action="<%=root %>/address/address!edit.action" method="post">
				<input id="email" type="hidden" name="email" value="${oaAddress.allEmail }"/>
				<input id="default" type="hidden" name="default" value="${oaAddress.defaultEmail }"/>
				<input id="id" name="model.addrId" type="hidden" value="${oaAddress.addrId }"/>
				<input id="address_sex" name="address_sex" type="hidden" value="${oaAddress.sex }"/>
				<table border="0" width="100%" height="285" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr>
						<td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;查看联系人</td>
					</tr>
					<tr height="80%" >
						<td width="100%">
							<DIV class=tab-pane style="height: 280;" id=tabPane1>
								<SCRIPT type="text/javascript">
								tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
								</SCRIPT>
								<DIV class=tab-page id=tabPage1>
									<H2 class=tab>
										普通
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1">
										<tr height="50">
											<img src="<%=path%>/oa/image/address/inputEmail.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入姓名和Email地址<br>
											 <hr/>
										</tr>
									    <tr>
								          <td width="18%" height="21" class="biao_bg1" ><span class="wz">姓名(<font color='red'>*</font>)：</span> </td>
								          <td  class="td1"  >
											<input id="realname" name="model.name" maxlength="6" value="${oaAddress.name }" type="text" size="24"  >
								          </td>
								          <td width="15%" height="21" class="biao_bg1" align="left"><span class="wz">昵称：</span> </td>
								          <td  class="td1"  >
											<input name="model.nickname" maxlength="6" value="${oaAddress.nickname }" type="text" size="17"  >
								          </td>
									    </tr>
									    <tr>
								          <td width="18%" height="21" class="biao_bg1" align="left"><span class="wz">&nbsp;&nbsp;Email：</span> </td>
								          <td colspan="3" class="td1">
									            <input id="newMail" maxlength="32" type="text" style="width: 95%;" >
									      </td>
									      <%--<td>        
									            <input id="btnAdd" type="button"  value="  添    加" icoPath="<%=root%>/images/ico/queding.gif"  class="input_bg" ><br>
									       </td>--%>
								         </tr>
								         <tr>
								          	<td colspan="4" width="100%" height="42">
									            <select id="mailList"  multiple="multiple" size="6" style="width: 435px;">
									            </select> 
									         </td>
									          <%-- <td>   
									            <input id="btnEdit" type="button" value="  编    辑" icoPath="<%=root%>/images/ico/bianji.gif"  class="input_bg" ><br><br>
									            <input id="btnDel" type="button"  value="  删    除" icoPath="<%=root%>/images/ico/shanchu.gif"  class="input_bg"><br><br>
									            <input id="btnDefault" type="button"  value="  设为默认" icoPath="<%=root%>/images/ico/queding.gif"  class="input_bg">
									        </td>--%>
									    </tr>
									</table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage1" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage2>
									<H2 class=tab>
										个人
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
									<tr height="50">
											<img src="<%=path%>/oa/image/address/person.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入个人信息<br>
											 <hr/>
										</tr>
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">手&nbsp;&nbsp;&nbsp;机1：</span> </td>
								          <td width="30%" class="td1">
								            <input id="mobile1" name="model.mobile1" type="text" value="${oaAddress.mobile1 }" style="width: 74%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">性&nbsp;&nbsp;&nbsp;别：</span> </td>
								          <td width="30%" class="td1">
								            <select name="model.sex" style="width: 74%;" >
								            	<option id="opt_man" value="1">男</option>
								            	<option id="opt_woman" value="0">女</option>
								            </select>
								         </td>
								         
								    </tr>
								        
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">手&nbsp;&nbsp;&nbsp;机2：</span> </td>
								          <td width="30%" class="td1">
								            <input id="mobile2" name="model.mobile2" value="${oaAddress.mobile2 }" type="text" style="width: 74%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">生&nbsp;&nbsp;&nbsp;日：</span> </td>
								          <td width="30%" class="td1">
								          <strong:newdate name="model.birthday" dateobj="${oaAddress.birthday }" id="search2" width="74%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd"></strong:newdate>
								            
								         </td>
								    </tr>
								    
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;QQ：</span> </td>
								          <td width="30%" class="td1">
								            <input id="qq" name="model.qq" type="text" value="${oaAddress.qq }" style="width: 74%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">&nbsp;&nbsp;&nbsp;&nbsp;MSN：</span> </td>
								          <td width="30%" class="td1">
								            <input id="msn" name="model.msn" type="text" value="${oaAddress.msn }" style="width: 74%;" >
								         </td>
								    </tr>
								    
								    <tr>
								          <td width="12%" height="21" class="biao_bg1" ><span class="wz">主&nbsp;&nbsp;&nbsp;&nbsp;页：</span> </td>
								          <td width="80%" class="td1" colspan="3">
								            <input id="url" name="model.homepage" type="text" value="${oaAddress.homepage }" style="width: 85%;" maxlength="128" >  
								            <img src="<%=path%>/oa/image/address/http.bmp"/>
								         </td>
								    </tr>
								    
								    <tr>
								          <td width="12%" height="21" class="biao_bg1" ><span class="wz">爱&nbsp;&nbsp;&nbsp;&nbsp;好：</span> </td>
								          <td width="50%" class="td1" colspan="3">
								           <textarea id="likeThing" name="model.likeThing" cols="49" rows="4" >${oaAddress.likeThing }</textarea>
								         </td>
								    </tr>
								        
								    </table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage2" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage3>
									<H2 class=tab>
										家庭
									</H2>
									<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
								    <tr height="50">
											<img src="<%=path%>/oa/image/address/home.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入家庭信息<br>
											 <hr/>
										</tr>
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">国&nbsp;家：</span> </td>
								          <td width="30%" class="td1">
								            <input maxlength="25" name="model.country" value="${oaAddress.country }" type="text" style="width: 85%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">电&nbsp;话1：</span> </td>
								          <td width="30%" class="td1">
								            <input id="tel1" name="model.tel1" value="${oaAddress.tel1 }" type="text" value="" style="width: 85%;" >
								         </td>
								         
								    </tr>
								        
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">&nbsp;&nbsp;&nbsp;省：</span> </td>
								          <td width="30%" class="td1">
								            <input name="model.province" value="${oaAddress.province }" type="text" maxlength="25" style="width: 85%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">电&nbsp;话2：</span> </td>
								          <td width="30%" class="td1">
								            <input id="tel2" name="model.tel2" value="${oaAddress.tel2 }" type="text" style="width: 85%;" >
								         </td>
								    </tr>
								    
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">城&nbsp;市：</span> </td>
								          <td width="30%" class="td1">
								            <input maxlength="25" name="model.city" value="${oaAddress.city }" type="text" style="width: 85%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">传&nbsp;&nbsp;真：</span> </td>
								          <td width="30%" class="td1">
								            <input id="fax" name="model.fax" value="${oaAddress.fax }" type="text" style="width: 85%;">
								         </td>
								    </tr>
								    
								    
								    <tr>
								          <td width="12%" height="21" class="biao_bg1" ><span class="wz">地&nbsp;址：</span> </td>
								          <td width="50%" class="td1" colspan="3">
								           <textarea name="model.address" id="address" cols="52" rows="4" >${oaAddress.address }</textarea>
								         </td>
								    </tr>
								        
								    </table>
								<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage3" ) );</SCRIPT>

								</DIV>
								<DIV class=tab-page id=tabPage4>
									<H2 class=tab >
										单位
									</H2>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage4" ) );</SCRIPT>
									<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
								    <tr height="50">
											<img src="<%=path%>/oa/image/address/company.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入单位信息<br>
											 <hr/>
										</tr>
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">单&nbsp;位：</span> </td>
								          <td width="30%" class="td1">
								            <input maxlength="25" name="model.company" value="${oaAddress.company }" type="text" style="width: 85%;">
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">职&nbsp;&nbsp;务：</span> </td>
								          <td width="30%" class="td1">
								            <input maxlength="25" name="model.position" value="${oaAddress.position }" type="text" style="width: 85%;" >
								         </td>
								         
								    </tr>
								        
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">部&nbsp;门：</span> </td>
								          <td width="30%" class="td1">
								            <input maxlength="25" name="model.department" value="${oaAddress.department }" type="text" style="width: 85%;">
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">电&nbsp;话1：</span> </td>
								          <td width="30%" class="td1">
								            <input id="cotel1" name="model.coTel1" value="${oaAddress.coTel1 }" type="text" style="width: 85%;">
								         </td>
								    </tr>
								    
								    <tr>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">邮&nbsp;编：</span> </td>
								          <td width="30%" class="td1">
								            <input id="zipcode" name="model.zipcode" value="${oaAddress.zipcode }" type="text" style="width: 85%;" >
								         </td>
								          <td width="12%" height="21" class="biao_bg1"><span class="wz">电&nbsp;话2：</span> </td>
								          <td width="30%" class="td1">
								            <input id="cotel2" name="model.coTel2" value="${oaAddress.coTel2 }" type="text" style="width: 85%;" >
								         </td>
								    </tr>
								    
								    </table>
								</div>
								<DIV class=tab-page id=tabPage5>
									<H2 class=tab >
										其他
									</H2>
						        	<table border="0" width="100%" cellpadding="2" cellspacing="1" align="center">
						        	<tr height="50">
											<img src="<%=path%>/oa/image/address/other.bmp"/>&nbsp;&nbsp;&nbsp;&nbsp;
											在这里输入其他信息<br>
											 <hr/>
										</tr>
						        	<tr>
						            	<td width="15%" height="21" align="left" ><span class="wz">附注信息：</span> </td>
								    </tr>
								    <tr>
								          <td width="50%" class="td1" colspan="3">
								           <textarea name="model.addressRemark" id="addressRemark" cols="66" rows="8" >${oaAddress.addressRemark }</textarea>
								         </td>
								    </tr>
								    </table>
									<SCRIPT type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage5" ) );</SCRIPT>
								</div>
								</DIV>
							</DIV>
						</td>
					</tr>
				</table>
				<table cellspacing="1" cellpadding="0" border="0" style="position: absolute; top: 320px; left: 0px;">
					<tr>
						<td width="100%"></td>
						<td></td>
						<td><input type="button" id="btn_editaddress" style="display:none;"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"/>&nbsp;&nbsp;
							<input type="button" id="btnCancel"  class="input_bg" icoPath="<%=root%>/images/ico/quxiao.gif" value="  取消"/>
						</td>
					</tr>
				</table>
			</form>
		</DIV>
	</body>

</html>

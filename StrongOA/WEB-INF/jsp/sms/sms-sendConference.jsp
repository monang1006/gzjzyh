<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<title>发送短信</title>
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
		<script>
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
		
		$(document).ready(function() {
		
			//选择接收人
			$("#addPerson").click(function(){
				var ret=OpenWindow(this.url,"600","400",window);
			});
			
			//清空接收人
			$("#clearPerson").click(function(){
				$("#orgusername").val("");
				$("#orguserid").val("");
			});
			
			if("${model.smsId}"!=""&&"${model.smsId}"!=null){
				var smsForm = document.getElementById("smsForm");
				smsForm.smsCon.value = '${model.smsCon}';
				if("${model.smsRecvId}"!=""&&"${model.smsRecvId}"!=null&&"${model.smsRecvId}"!="null"){
					smsForm.recvUserIds.value = "${model.smsRecvId}";
					$("#orgusername").val("${model.smsRecver}");
				}else{
					$("#checknum").attr("checked",true);
					$("#addNumArea").show();
					$("#ownerNum").val("${model.smsRecvnum}");
				}
			}
			
			if(smsForm!=""&&smsForm!=null){
				onCharsChange(smsForm.smsCon);
			}
			
			//初始化短信回复	
			$.ajax({
				type:"post",
				dataType:"text",
				url:"<%=root%>/smsplatform/smsPlatform!isModuleParaOpen.action",
				data:"sendid="+ "${moduleCode}",
				success:function(msg){ 
					if("1"==msg){//开启
						
					
					}else if("0"==msg){//关闭
						alert("该模块状态为关闭，请在短信平台设置中开启！");
						window.close();
					}else if("no"==msg){//未配置模块
						alert("该模块未配置短信发送，请在短信平台设置中添加！");
						window.close();
					}else{
						alert("不能发送短信，请联系管理员！");
					}
				}
			});
			
			//短信回复内容
			 $("#answerNum").change(function(){
			 	var num = $("#answerNum").val();
			 	selectAnswerInput(num);
			 });
		});
		 var cap_max=200;//可发送的字数
		 function onCharsChange(varField){
		 	 var smsContent = document.getElementById("smsCon");
		 	 var charsmonitor1 = document.getElementById("charsmonitor1");
		 	 var charsmonitor2 = document.getElementById("charsmonitor2");
		     var leftChars = getLeftChars(varField);
		     if ( leftChars >= 0)
		     {
			 	//charsmonitor1.value=cap_max-leftChars;
			 	//charsmonitor2.value=leftChars;
			 	charsmonitor1.innerHTML=cap_max-leftChars;
			 	charsmonitor2.innerHTML=leftChars;
			 	return true;
		     }
		     else
		     {
		     	charsmonitor1.value=cap_max;
		     	charsmonitor2.value="0";
		     	window.alert("短信内容超过字数限制!");
		     	var len = smsContent.value.length + leftChars;
			 	smsContent.value = smsContent.value.substring(0, len);
			 	leftChars = getLeftChars(smsContent);
		     	if ( leftChars >= 0)
		     	{
				charsmonitor1.innerHTML=cap_max-leftChars;
				charsmonitor2.innerHTML=leftChars;
				}
		        return false;
		     }
		 }
		 
		 function getLeftChars(varField){
		    var cap = cap_max;
		    var leftchars = cap - varField.value.length;
		    return (leftchars);
		 }
		 
		 function CheckSend(){
			if(event.keyCode==10){
//				if(CheckForm()){
//					document.form1.submit();
					//alert("submit();");
//				}
			}
		 }
		 
		 //var reg_mobile= /^(1(3|4|5|8)\d{9})$/;
		 var reg_mobile= /^\d+$/;
		 //发送短信
		 function SendSms(){
		  	 var smsForm = document.getElementById("smsForm");
			 var recvUserIds = smsForm.recvUserIds.value;
			 var smsCon = smsForm.smsCon.value;
		 	 var addNum = document.getElementsByName("addNum");
		 	
		 	//短信字数是否超长
		 	if(!onCharsChange(smsForm.smsCon)){
		 		return;
		 	}
		 	
		 	//短信接受和自定义号码不能同时为空
		   	 if(recvUserIds==null|recvUserIds==""){
		   	 	 if($("#checknum").attr("checked")){
		   	 	 	if($("#ownerNum").val().length<0){
		   	 	 		alert("请输入自定义手机号码！");
		   	 	 		return false;
		   	 	 	}
		   	 	 }else{
		   	 	 	alert("请选择接收人！");
		   	 	 	return false;
		   	 	 }
		   	 }
		   	 if(!$("#checknum").attr("checked")){
		   	 	 if(recvUserIds==null|recvUserIds==""){
		   	 	 	alert("请选择接收人！");
		   	 	 	return false;
		   	 	 }
		   	 }


		   	 //验证自定义号码的输入是否合法
		 	if(addNum.length!=0){
		 		
			   	 for(var i=0;i<addNum.length;i++){
			   	 	var num = addNum[i].value;
			   	 	if(""==num|null==num){
			   	 		alert("输入的手机号码不能为空！");
			   	 		return ;
			   	 	}
			   	 	if(num!=""){
						if(!reg_mobile.test(num)){
							alert("输入的手机号码<"+num+">格式不对\n请确认后重新输入！");
							$("#ownerNum").focus();
							return ;
						}
					}
			   	 }
		 	}
		   	 
		   	 //验证textarea里的手机号码是否合法
		   	 if($("#checknum").attr("checked")){
		   	 	var userids = $("#ownerNum").val();
		   	 	//将所有"，"替换为","
			   	 	while (userids.indexOf("，")>0) {
			   	 		userids = userids.replace("，",",");
			   	 	}
		   	 	if(""==userids|null==userids){
		   	 		alert("请输入自定义手机号码！");
		   	 		return false;
		   	 	}else{
		   	 		var str = userids.substring(userids.length-1,userids.length);
	   	 			if(str==","){
	   	 				userids = userids.substring(0,userids.length-1);
	   	 			}
		   	 		var userId=userids.split(',');
		   	 		for(var i=0;i<userId.length;i++){
		   	 			if(!reg_mobile.test(userId[i])){
							alert("输入的手机号码格式不对\n请确认后重新输入！");
							return false;
						}
		   	 		}
		   	 	}
		   	 	$("#ownerNum").val(userids);
		   	 }else{
		   	 	$("#ownerNum").val("");
		   	 }
		   	 
		   	 if(smsCon==null|smsCon==""){
		   	 	alert("请输入短信内容！");
		   	 	return false;
		   	 }
		   	 smsForm.submit();
		   	 //submit.click();
		   	 returnValue ="reload";
		   	 window.close();
		 }
		 
		 
		 function changNum(obj){
		 	if(obj.checked){
			 	$("#addNumArea").show();
		 	}else{
		 		$("#addNumArea").hide();
		 	}
		 }
		 
		 //选中修改默认回复
		 function changRep(obj){
		 	if(obj.checked){
			 	$("#repArea").show();

			 	 var k =0;
				 <s:iterator value="means">
					var s = "<s:property value='moduleStateMean'/><s:property value='msgStateMean'/>";
					k++;
					$("#means"+k).val(s);
				 </s:iterator>
				 $("#answerNum").val(k);
				 selectAnswerInput(k);
				 
		 	}else{
		 		$("#repArea").hide();
		 	}
		 }
		 //是否需要回复
		 function changneedRep(obj){
		 	if(obj.checked){
			 	$("#repDiv").show();
			 	$("#needRep").val("1");
		 	}else{
		 		$("#repDiv").hide();
			 	$("#needRep").val("0");
		 	}
		 }
		 
		 //显示隐藏短信回复输入框
		 function selectAnswerInput(num){
		 	if(""!=num&&0!=num){
			 	var i;
				for(i=0;i<=num;i++){
					$("#tr_means"+i).show();
				}
				for(var j=i;j<=10;j++){
					$("#tr_means"+j).hide();
					$("#means"+j).val("");
				}
		 	}else if(num==0){
		 		for(var m=0;m<=10;m++){
					$("#tr_means"+m).hide();
					$("#means"+m).val("");
				}
		 	}
		 }
		 
		 
		</script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" >
	<input type="submit" id="submit" name="submit" value="" style="display: none">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center >
			<s:form action="sms!save.action" theme="simple" name="smsForm" id="smsForm">
			<input type="hidden" name="moduleCode" id="moduleCode" value="${moduleCode}">
				<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td height="30" colspan="2"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													发送手机短信
												</td>
												<td>
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">选择接收人(<font color=red>*</font>)：</span>
										</td>
										<td class="td1">
										<s:textarea title="双击选择接收人" cols="35" id="orgusername" name="receiverNames" ondblclick="addPerson.click();"  rows="4" readonly="true"></s:textarea>
										<input type="hidden" id="orguserid" name="recvUserIds" value="${recvUserIds}"></input>
										<input type="button" class="input_bg" id="addPerson" url="<%=root%>/address/addressOrg!tree.action" value="添加"/>&nbsp;<input type="button" class="input_bg" id="clearPerson" value="清空"/>
										</td>
									</tr>
									<tr>
										<td width="30%" height="21" class="biao_bg1" align="right">
											<span class="wz">自定义接收号码：</span>
										</td>
										<td class="td1">
										<input type="checkbox" name="checknum" id="checknum" value="1" onclick="changNum(this);">添加自定义号码
										<div id="addNumArea" style="display:none;" title="输入手机号码，用逗号分割">
											<font color="red">
												*在下面的输入框中输入手机号码（多个请用逗号分割）
												<br>如：13712345678,13787654321,13922221111
											</font>
											<s:textarea title="输入手机号码，用逗号分割" cols="30" id="ownerNum" name="ownerNum" rows="4" ></s:textarea>
										</div>
										</td>
									</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">短信内容(<font color=red>*</font>)：</span>
										</td>
										<td  class="td1">
											<textarea cols=35 name="smsCon" id="smsCon" rows=5 class="BigInput" wrap="on" onpaste="return onCharsChange(this);"
											 onKeyUp="return onCharsChange(this);" onkeypress="CheckSend()"></textarea>
											<font color="red">
											<br>*已输入 <span id="charsmonitor1">0</span> 
											字符，
											剩余 <span id="charsmonitor2">200</span> 
											字符，
											<br>每条短信200字</font>
										</td>
									</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">回复设置：</span>
										</td>
										<td  class="td1">
											<input type="checkbox" name="needRep" id="needRep" value="0" onclick="changneedRep(this);">需要用户回复
										</td>
									</tr>
									
									<tr id="repDiv" style="display:none;">
										<td width="25%" height="21" class="biao_bg1" align="center">
											<span class="wz">回复内容设置(<font color=red>*</font>)：</span>
										</td>
										<td  class="td1">
											<input type="checkbox" name="checkRep" id="checkRep" value="1" onclick="changRep(this);">修改默认回复
											<s:if test="dxhf==1">
												（原设置回复：<s:iterator value="means">[<s:property value='msgStateMean'/>]</s:iterator>）
											</s:if>
											<s:else>
												（默认回复：<s:iterator value="means">[<s:property value='moduleStateMean'/>]</s:iterator>）
											</s:else>
											<table id="repArea" style="display:none;">
												<tr>
													<td colspan="2">
														<font color="red">
															*以下显示的是短信接收者可选择回复的信息
														</font>
													
													<br></td>
												</tr>
												<tr>
													<td width="38%" class="biao_bg1">
														<span class="wz">短信答案个数：</span>
													<br><br></td>
													<td width="65%" class="td1">
														<select name="answerNum" id="answerNum" style="width:40%">
															<option value=0 selected>未配置默认答案</option>
															<option value=1>1</option>
															<option value=2>2</option>
															<option value=3>3</option>
															<option value=4>4</option>
															<option value=5>5</option>
															<option value=6>6</option>
															<option value=7>7</option>
															<option value=8>8</option>
															<option value=9>9</option>
															<option value=10>10</option>
														</select>
													<br><br></td>
												</tr>
												<tr id="tr_means1" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位0含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means1" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means2" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位1含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means2" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means3" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位2含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means3" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means4" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位3含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means4" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means5" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位4含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means5" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means6" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位5含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means6" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means7" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位6含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means7" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means8" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位7含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means8" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means9" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位8含义：</span> <br><br></td>
													<td width="75%" class="td1"> <input id="means9" type="text" name="means" value=""> <br><br></td>
												</tr>
												<tr id="tr_means10" style="width:100%; display:none;">
													<td width="25%" class="biao_bg1"> <span class="wz">状态位9含义：</span> <br><br></td>
													<td width="75%" class="td1"><input id="means10" type="text" name="means" value=""> <br><br></td>
												</tr>
											</table>
										</td>
									</tr>
									
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">指定发送时间：</span>
										</td>
										<td  class="td1">
											<strong:newdate name="smsSendDelay" id="smsSendDelay" width="50%" skin="whyGreen" isicon="true" mindate="%y-%M-%d %H:%m" dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
											 &nbsp;<font color="red">
															*不指定则立即发送
														</font>
										</td>
									</tr>
									</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="center">
						<td>
							<input type="button" class="input_bg" value="确定" onclick="SendSms();"/>
							<input type="button" class="input_bg" value="取消" onclick="window.close();" />
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>

</html>

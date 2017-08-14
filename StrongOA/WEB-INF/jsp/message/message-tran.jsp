<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>消息转发</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet"
			type="text/css">
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<style type="text/css">
			body{
			width:100%;
			margin : 0px;
			height: 100%
			}
			#inHtml{
				CURSOR: default; 
				width: 300px;
				height: 100px;
			}
		</style>	
	<script language="javascript">
		var ischange = false;
		
		var recvuserId = "${msgReceiverIds}";
		var msgCont = $("#con").text();
	$(document).ready(function(){
		$("#csbutton").click(function(){
			if($("#csbutton").val()=='添加抄送'){
				$("#csbutton").val('删除抄送');
			    document.getElementById("msgCc").value=document.getElementById("msgReceiver").value;
				$("#cstr").show();
			}else{
				$("#csbutton").val('添加抄送');
				document.getElementById("msgCc").value="";
				$("#cstr").hide();
			};

		});
		$("#fsxxhref").click(function(){
			if($("#fsxxtr").css("display")=='none'){
				$("#fsxxtr").show();
			}else{
				$("#fsxxtr").hide();
			}
		});
		$("#msgCcs").dblclick(function(){
			alert("添加抄送人员");
		});
		
		$("#dxhf").click(function(){
			if($("#dxhf").attr("checked")){
				$("#editsms").show();
			}else{
				$("#editsms").hide();
			}
			
		});
		
		//短信回复内容
		 $("#answerNum").change(function(){
		 	var num = $("#answerNum").val();
		 	selectAnswerInput(num);
		 });
		
		 //选中所有人
		 $("#setAllUser").click(function(){
			if($("#setAllUser").attr("checked")){
				$("#orguserid").val("alluser");
				$("#orgusername").val("所有人");
			}else{
				$("#orguserid").val("");
				$("#orgusername").val("");
			}
		 });
	});
	function CheckSend(){
			var oEditor = FCKeditorAPI.GetInstance('content');
            var acontent = oEditor.GetXHTML();
            $("#quicklyre").val(acontent);
			if($("#orguserid").val().length==0){
				alert("对不起，请您选择收信人!");
				$("#addPerson").focus();
				return false;
			}
			//验证发送短信
			if(!smsV()){
				return false;
			}
			 
			if($("#msgTitle").val().length==0){
				if(confirm("标题为空，继续发送，确定？")==true){
					//ajax提交操作
					$("#fsbutton").attr("disabled",true);
					document.msgForm.submit();
				}else{
					$("#msgTitle").focus();
				}
			}else{
				//ajax提交操作
				$("#fsbutton").attr("disabled",true);
				document.getElementById("ischarge").value="true";
				$("#msgForm").attr("action","<%=root%>/message/message!sendMessage.action");
				document.msgForm.submit();
				//alert("转发成功！");
			}
	}
	//选择接收人
	function addPerson(){
		var url="<%=root%>/address/addressOrg!tree.action?isShowAllUser=1";	
		if($("#setAllUser").attr("checked")){
			alert("您已选择所有人，如需重新选择，请先取消选择所有人！");
			return false;
		}
		var ret=OpenWindow(url,"600","400",window);
	}
	
	//清空接收人
	 function clickClear(){
		if($("#setAllUser").attr("checked")){
			$("#setAllUser").attr("checked",false);
		}
		$("#orgusername").val("");
		$("#orguserid").val("");
	}
	
	function callback(msg){
		if(msg=="true"){
			$("#ischarge").val("true");
			//alert("消息发送成功!");
			window.returnValue="true";
			window.close();
		}else if(msg=="attach"){
			alert("不能上传附件，超出消息空间大小（${BaseMsgSize/1024/1024}M）！");
			$("#fsbutton").attr("disabled",false);
		}else if(msg=="empty"){
			alert("消息发送失败！附件不存在或附件大小为0！");
			$("#fsbutton").attr("disabled",false);
		}else if(msg=="savefalse"){
			alert("消息已经发送成功！但发件箱保存失败!");
			$("#fsbutton").attr("disabled",false);
		}else{
			alert("消息发送失败！请您重新发送!");
			$("#fsbutton").attr("disabled",false);
		}
	}
	
	function aftersave(msg){
		if(msg=="true"){
			if(confirm("草稿保存成功，请问您是否要关闭当前窗口?")==true){
				window.close();
			}
		}else if(msg=="empty"){
			alert("消息保存失败，附件不存在或附件大小为0！");
		}else if(msg=="close"){
			window.returnValue="true";
			window.close();
		}else if(msg=="attach"){
			alert("不能上传附件，超出消息空间大小（${BaseMsgSize/1024/1024}M）！");
			$("#fsbutton").attr("disabled",false);
		}else{
			alert("消息保存失败！");
		}
	}
	
	var msgId = "${msgId}"; 
	
	function aftersaveWithMsgId(msg, newMsgId){
		msgId = newMsgId;
		aftersave(msg);
	}
	
	function aftersave(msg){
		if(msg=="true"){
			window.returnValue="true";
			if(confirm("草稿保存成功，请问您是否要关闭当前窗口?")==true){
				window.close();
			}
		}else if(msg=="empty"){
			alert("消息保存失败，附件不存在或附件大小为0！");
		}else if(msg=="close"){
			window.returnValue="true";
			window.close();
		}else if(msg=="attach"){
			alert("不能上传附件，超出消息空间大小（${BaseMsgSize/1024/1024}M）！");
		}else{
			alert("消息保存失败！");
		}
	}
	
	function saveDraft(ifclose){
		var oEditor = FCKeditorAPI.GetInstance('content');
        var acontent = oEditor.GetXHTML();
		if(recvuserId!=$("#orguserid").val()){
			ischange = true;
		}
//		alert("msgCont---->"+msgCont);
//		alert("acontent--->"+acontent);
		if(msgCont!=acontent){
			ischange = true;
		}
		if($("#ischarge").val()=="false"&&ischange==true){
			if(confirm("请问您是否要将其保存至草稿箱呢?")==true){
				$("#msgForm").attr("action","<%=root%>/message/message!save.action");
	            $("#msgContent").val(acontent);
	            $("#dxhf").val("0");
	            $("#isclose").val(ifclose);
				document.msgForm.submit();
				ischange = false;
				msgCont = acontent;
				recvuserId = $("#orguserid").val();
	        	//event.returnValue="您已经提交成功！是否要离开本页面？";
			}else{
				//event.returnValue="您是要不保存草稿就离开本页面呢？";
				if(ifclose=='0'){
					window.close();
				}
			}
		}else{
			//return true;
			if(ifclose=='0'){
				window.close();
			}
		}
	}
	function chageState(){
		alert("ddddd");
	}
	function charge(){
		if($("#ischarge").val()=="true"){
			return true;
		}else{
			setTimeout("charge()",1000); 
		}
	}
	
	//选中添加自定义号码
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
	
	function deldbobj(id){
		ischange = true;
		var obj=document.getElementById(id);
		var value=document.getElementById(id).value;
		obj.value=value+";";
		$("#div"+id).hide();
	}
	
	//短信发送记数
	var cap_max=70;//可发送的字数
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
		 
		 //验证短信发送
		 var reg_mobile= /^\d+$/;
		 var reg_Date= /^(\d{4})-(\d{2})-(\d{2})\s(\d{2}):(\d{2}):(\d{2})$/;		//日期格式
		 function smsV(){
		 	if($("#dxhf").attr("checked")){
		 		if($.trim($("#smsCon").val()).length==0){
		 			alert("请输入短信内容！")
		 			return false;
		 		}
		 		//发送时间
		 		if($.trim($("#smsSendDelay").val()).length!=0){
		 			if(!reg_Date.test($.trim($("#smsSendDelay").val()))){
								alert("输入的短信发送时间格式不对!\n请确认后重新输入！");
								return false;
							}
		 		}
		 		//短信回复答案
		 		if($("#checkRep").attr("checked")){
			 		if($("#answerNum").val()==0){
			 			alert("请设置回复答案！")
			 			return false;
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
								alert("输入的手机号码{"+userId[i]+"}格式不对!\n请确认后重新输入！");
								return false;
							}
			   	 		}
			   	 	}
			   	 	$("#ownerNum").val(userids);
			   	 }else{
			   	 	$("#ownerNum").val("");
			   	 }
			   	 
		 	}
		 	return true;
		 }
</script>
	</head>

	<body class="contentbodymargin">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
		<div id="contentborder" align="center">
			<form id="msgForm" name="msgForm"
				action="<%=root%>/message/message!sendMessage.action" method="post"
				enctype="multipart/form-data" target="myIframe">
				<input type="hidden" name="ischarge" id="ischarge" value="false">
				<input type="hidden" name="quicklyre" id="quicklyre">
				<input type="hidden" name="disLogo" id="disLogo" value="resend">
				<input type="hidden" name="msgId" id="msgId" value="${model.msgId }">
				<input type="hidden" name="isclose" id="isclose" value="">
				<input type="hidden" name="moduleCode" id="moduleCode" value="${moduleCode}">
				<input type="hidden" id="msgContent" name="model.msgContent" value="${model.msgContent}">
				<div id="con" style="display:none;">
					${model.msgContent}
				</div>

				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										消息转发
									</td>
									<td align="right">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="CheckSend();">&nbsp;发&nbsp;送&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="saveDraft('1');">&nbsp;存&nbsp;草&nbsp;稿&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
							                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
							                 	<td class="Operation_input1" onclick="saveDraft('0');">&nbsp;关&nbsp;闭&nbsp;</td>
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
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					class="table1">
					<tr>
						<td class="biao_bg1" width="10%" align="right" valign="top">
							<span class="wz"><font color=red>*</font>&nbsp;收件人：</span>
						</td>
						<td colspan="3" class="td1" width="90%">
							<s:textarea title="双击选择收件人" cols="95%" style="background-color:#ffffff" id="orgusername" name="msgReceiverNames" ondblclick="addPerson();"  rows="4" readonly="true"></s:textarea>
							<input type="hidden" id="orguserid" name="msgReceiverIds" value="${msgReceiverIds}"></input>
							<table width="100%">
								<tr width="100%">
									<td align="left" width="50%">
										<input type="checkbox" name="setAllUser" id="setAllUser" value="1">所有人&nbsp;
										<a id="addPerson"  href="#" class="button" onclick="addPerson()">添加</a>&nbsp;
										<a id="clearPerson" href="#" class="button" onclick="clickClear()">清空</a>
									</td>
									<td align="right" width="50%">
										<input type="checkbox" name="sfxyhz" id="sfxyhz" value="1" onchange="ischange=true;">
										需要回执&nbsp;
										<s:if test="hasSendRight==true">
											<input type="checkbox" name="dxhf" id="dxhf" value="1" onchange="ischange=true;">
											发送短信&nbsp;	
										</s:if>
										<s:else>
											<font color="gray" title="短信发送权限未开启">
											<input type="checkbox" name="dxhf1" id="dxhf1" value="1" disabled="disabled">
											发送短信&nbsp;
											</font>
										</s:else>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td > <br> </td></tr>
					<tr id="editsms" style="display:none"  >
						<td class="biao_bg1" align="right" valign="top">
							<span class="wz">短信内容：</span>
						</td>
						<td colspan="3" class="td1">
							<table>
								<tr>
									<td align="right">
										<textarea cols=95% name="smsCon" style="background-color:#ffffff" id="smsCon" rows=5 class="BigInput" wrap="on" onpaste="return onCharsChange(this);" 
														 onKeyUp="return onCharsChange(this);" style="margin: 3px;"></textarea>
														 <br>
										<font color="#999999">
											[当前 <span id="charsmonitor1">0</span>字，剩余 <span id="charsmonitor2">70</span>字符，每条短信70字]
										</font>
										&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								<!-- 
								<tr>
									<td>
										选择短信回复格式：<s:select name="" list="#{'':'是否阅读','1':'是否同意','2':'是否参加'}" listKey="key" listValue="value"/>
										&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>
								 -->
								<tr>
									<td>
										设置发送时间：<strong:newdate name="smsSendDelay" width="160px" id="smsSendDelay" skin="whyGreen" isicon="true" mindate="%y-%M-%d %H:%m" dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
										&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
								</tr>																
								<tr>
									<td >
										<input type="checkbox" name="checkRep" id="checkRep" value="1" onclick="changRep(this);">修改默认回复
										<s:if test="dxhf==1">
											（原设置回复：<s:iterator value="means">[<s:property value='msgStateMean'/>]</s:iterator>）
										</s:if>
										<s:else>
											（默认回复：<s:iterator value="means">[<s:property value='moduleStateMean'/>]</s:iterator>）
										</s:else>
									</td>
								</tr>
								<tr id="repArea" style="display:none;">
									<td >
										<table>
											<tr>
												<td colspan="2">
													<font color="#999999">
														以下显示的是短信接收者可选择回复的信息
													</font>
												
												</td>
											</tr>
											<tr>
												<td width="25%" class="biao_bg1">
													<span class="wz">短信答案个数：</span>
												</td>
												<td width="75%" class="td1">
													<select name="answerNum" id="answerNum" style="width:40%;background-color:#ffffff">
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
												</td>
											</tr>
											<tr id="tr_means1" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位0含义：</span> </td>
												<td width="75%" class="td1"> <input id="means1" style="background-color:#ffffff" type="text" name="means" value=""  maxlength="25"> </td>
											</tr>
											<tr id="tr_means2" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位1含义：</span> </td>
												<td width="75%" class="td1"> <input id="means2" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means3" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位2含义：</span> </td>
												<td width="75%" class="td1"> <input id="means3" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means4" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位3含义：</span> </td>
												<td width="75%" class="td1"> <input id="means4" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means5" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位4含义：</span> </td>
												<td width="75%" class="td1"> <input id="means5" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means6" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位5含义：</span> </td>
												<td width="75%" class="td1"> <input id="means6" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means7" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位6含义：</span> </td>
												<td width="75%" class="td1"> <input id="means7" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means8" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位7含义：</span> </td>
												<td width="75%" class="td1"> <input id="means8" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means9" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位8含义：</span> </td>
												<td width="75%" class="td1"> <input id="means9" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
											<tr id="tr_means10" style="width:100%; display:none;">
												<td width="25%" class="biao_bg1"> <span class="wz">状态位9含义：</span> </td> 
												<td width="75%" class="td1"> <input id="means10" style="background-color:#ffffff" type="text" name="means" value="" maxlength="25"> </td>
											</tr>
										</table>
									</td>
								</tr>																																																								
								<tr>
									<td>
										<input type="checkbox" name="checknum" id="checknum" value="1" onclick="changNum(this);">添加自定义号码
									</td>
								</tr>
								<tr id="addNumArea" style="display:none;" title="输入手机号码，用逗号分割">
									<td>
										<font color="#999999">
											在下面的输入框中输入手机号码（多个请用逗号分割如：137********,137********,139********）
										</font>
										<s:textarea title="输入手机号码，用逗号分割" cols="50" style="background-color:#ffffff" id="ownerNum" name="ownerNum" rows="4" ></s:textarea>
									</td>
								</tr>
					<tr><td > <br> </td></tr>
							</table>
						</td>
					</tr>
					<tr id=cstr style="display:none;">
						<td class="biao_bg1" width="15%">
							<span class="wz">抄&nbsp;&nbsp;送：</span>
						</td>
						<td colspan="3" class="td1" width="75%" id="msgCcs">
							<span class="wz">${model.msgReceiver }</span>
							<input type="hidden" name="model.msgCc" id="msgCc"
								style="width:100% " />
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" width="15%" align="right">
							<span class="wz">标&nbsp;&nbsp;题：</span>
						</td>
						<td colspan="3" class="td1" width="75%">
							<input type="text" name="msgTitle" id=msgTitle
								style="width:100%;background-color:#ffffff;border:1px solid #b3bcc3;" size="12px" value="${model.msgTitle }" />
						</td>
					</tr>
					<tr><td > <br> </td></tr>
					<tr>
						<td class="biao_bg1" align="right" valign="top">
							<span class="wz">附件：</span>
						</td>
						<td colspan="3" class="td1">
							<input type="file" onkeydown="return false;" name="file" class="multi required" style="width: 76%;" onchange="ischange=true;" />
							<s:if test="attachMentList!=null&&attachMentList.size()>0">
								<s:iterator value="attachMentList" status="statu" id="att">
									<div id=div<s:property value="attachId"/>>
										[<a onclick="deldbobj('<s:property value="attachId"/>');"
											href="#">删除</a>]
										<s:property value="attachName" />
									</div>
									<input type="hidden" name="dbobj"
										id="<s:property value="attachId"/>"
										value=<s:property value="attachId"/> />
								</s:iterator>
							</s:if>
							<font color="#999999">
							<script>
								var bms = ${BaseMsgSize/1024/1024};
								var ums = ${usedMsgSize};
								if(bms>0){
									//ums = parseInt(ums*100/1024/1024/bms);
									ums = parseFloat(ums*100/1024/1024/bms).toFixed(2);
									if(ums<100){
										document.writeln("您的空间共有"+bms+"M，已使用"+ums+"%");
									}else{
										document.writeln("您的空间共有"+bms+"M，已使用100%");
									}
								}else{
									bms=0;
									ums = 100;
									document.writeln("您的空间共有"+bms+"M，已使用"+ums+"%")
								}

							</script>
							</font>
						</td>
					</tr>
					<tr><td > <br> </td></tr>
					<tr>

						<td colspan="4" class="td1" width="100%">
							<script type="text/javascript"
								src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
							<script type="text/javascript">
						var msgc = document.getElementById("msgContent").value;
						var oFCKeditor = new FCKeditor( 'content' );
						oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
						oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
						oFCKeditor.Width = '100%' ;
                        oFCKeditor.Height = '350' ;
						oFCKeditor.Value = msgc;
						oFCKeditor.Create() ;
						msgCont = oFCKeditor.Value;
					</script>
						</td>
					</tr>
					<tr style="display: none">
						<td class="biao_bg1" width="15%"></td>
						<td colspan="3" class="td1" width="75%">
							<span class="wz"><a id="fsxxhref" href="#">发送选项</a> </span>
						</td>
					</tr>
					<tr id=fsxxtr style="display:none ">
						<td class="biao_bg1" width="15%"></td>
						<td colspan="3" class="td1" width="75%">
							<input type="checkbox" name="sfxyhz" id="sfxyhz" value="1">
							已读回执&nbsp;
						</td>
					</tr>

				</table>
			</form>
			<iframe name="myIframe" style="display:none"></iframe>
		</div>
	</body>
</html>

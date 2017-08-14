<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>发送即时消息</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				//发送
				$("#btnOK").click(function(){
					var content = $("#suggestionContent").val();
					if($.trim(content) == ""){
						alert("发送内容不能为空。");
						return ;
					}
					$.ajax({
						type:"post",
						url:$("form").attr("action"),
						data:{content:content,receiveId:$("#receiveId").val()},
						dataType:"json",
						success:function(data){
							if(data == "0"){
								alert("发送成功。");
								window.close();
							}else if(data == "-1"){
								alert("即时通讯软件未启用，发送失败。");
								return ;
							}else if(data == "-1"){
								alert("对不起,消息发送失败。");
								return ;
							}
						}
					});
				});
				
				//取消
				$("#btnCancel").click(function(){window.close();});
				
				var appSuggestionForm = document.getElementById("appSuggestionForm");
				
				var dictItemValueLength='${suggestionLength}';
				
				
				//appSuggestionForm.dictItemValue.value = $.trim(dictItemValue);
				
				//onCharsChangeLength(dictItemValueLength);
			});
			var cap_max=100;//可发送的字数
		 function onCharsChange(varField){
		 	 var suggestionContent = document.getElementById("suggestionContent");
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
		     	//window.alert("消息内容超过字数限制。");
		     	var len = suggestionContent.value.length + leftChars;
			 	suggestionContent.value = suggestionContent.value.substring(0, len);
			 	leftChars = getLeftChars(suggestionContent);
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
		 
		 
		  function onCharsChangeLength(varFieldLength){
		 	 var dictItemValue = document.getElementById("dictItemValue");
		 	 var charsmonitor1 = document.getElementById("charsmonitor1");
		 	 var charsmonitor2 = document.getElementById("charsmonitor2");
		     var leftChars = getLeftCharsLength(varFieldLength);
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
		     	window.alert("消息内容超过字数限制。");
		     	var len = dictItemValue.value.length + leftChars;
			 	dictItemValue.value = dictItemValue.value.substring(0, len);
			 	leftChars = getLeftChars(dictItemValue);
		     	if ( leftChars >= 0)
		     	{
				charsmonitor1.innerHTML=cap_max-leftChars;
				charsmonitor2.innerHTML=leftChars;
				}
		        return false;
		     }
		 }
		 
		  function getLeftCharsLength(varField){
		    var cap = cap_max;
		    var leftchars = cap - varField;
		    return (leftchars);
		 }
		</script>
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;" style="background-color:#ffffff">
<div style="height:100%;overflow: auto;">
		<form action="<%=root%>/im/iM!sendIM.action" method="post">
			<s:hidden id="receiveId" name="receiveId"></s:hidden>
			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="0" class="table1">
				<tr >
				<td colspan="3" class="table_headtd">
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										<strong>发送即时消息</strong>
									</td>
									<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
															<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" id="btnOK" >&nbsp;发&nbsp;送&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
									                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
														</tr>
													</table>
												</td>
								</tr>
							</table>
						</td>
				</tr>
			<tr>
				<td width="22%" height="21" class="biao_bg1" align="right" valign="top"><span class="wz"><font color='red'>*</font>&nbsp;内容：&nbsp;</span></td>
				<td  class="td1" colspan="2">
					<s:textarea cols="60" rows="19" name="content" style="background-color:#ffffff" id="suggestionContent"  onpropertychange="return onCharsChange(this);" onpaste="return onCharsChange(this);"></s:textarea>
					<br>
					
				</td>
			</tr>
			<tr>
				<td>
						</td>
				<td >
					<span class="wz" ><font color="#999999">
											已输入<font color="green"><span id="charsmonitor1">0</span></font>个字，剩余<font color="blue"><span id="charsmonitor2">100</span></font>个字，最多输入<font color="red">100</font>个字
										</font></span>
				</td>
			</tr>
		</table>
	</form>
</div>	
</body>
</html>
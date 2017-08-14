<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>


<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>

		<title>查看短信</title>
		<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height: 100%
}
.table1 td{
	height: 40px;
}
</style>
		<script>
		 var cap_max=140;//可发送的字数
		 function onCharsChange(varField){
		 	 var smsContent = document.getElementById("smsContent");
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
			  //  charsmonitor1.value=cap_max-leftChars;
		 	  //   charsmonitor2.value=leftChars;
		
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
					alert("submit();");
//				}
			}
		 }
		 
		 function delSms(){
		 	var id = "${model.smsId}";
			 var url = "<%=path%>/sms/sms!delete.action";
				if(confirm("删除此短信，确定？")){
						$.ajax({
							type:"post",
							url:url,
							data:{
								smsId:id
							},
							success:function(data){
								if(data!="" && data!=null){
									alert(data);					
								}else{
									returnValue ="reload";
		   							window.close();
								}
							},
							error:function(data){
								alert("对不起，操作异常"+data);
							}
					   });
					}
		 }
		 //重新发送
		 function reSend(){
		 	returnValue ="resend";
		   	window.close();
		 }
		</script>
	</head>
	<body class="contentbodymargin" >

		<DIV id=contentborder align=center >
			<form name="form1">
				<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查看手机短信</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="reSend();">重新发送</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="delSms();">&nbsp;删&nbsp;除&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
										<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">接收人：</span>
											</td>
											<td class="td1">
												<s:if test="model.smsRecvnum==null|model.smsRecvnum==''">
													<span class="wz">${model.smsRecver}&nbsp;<font color="gray">(该用户未设置手机号码)</font></span>
												</s:if>
												<s:else>
													<span class="wz">${model.smsRecver}&nbsp;(${model.smsRecvnum})</span>
												</s:else>
											</td>
										</tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">业务类型：</span>
											</td>
											<td class="td1">
												<s:if test="model.modelName==null|model.modelName==''|model.modelName=='null'">
													<span class="wz">短信平台</span>
												</s:if>
												<s:else>
													<span class="wz">${model.modelName}</span>
												</s:else>
											</td>
										</tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">短信内容：</span>
											</td>
											<td class="td1" style="word-break:break-all;line-height: 1.4">
												${model.smsCon}
											</td>
										</tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">发送时间：</span>
											</td>
											<td class="td1">
												<s:date name="model.smsSendTime" format="yyyy年MM月dd日 HH点mm分"/>
											</td>
										</tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">发送人：</span>
											</td>
											<td class="td1">
												<span class="wz">${senderName}</span>
											</td>
										</tr>
										<tr>
											<td width="25%" height="21" class="biao_bg1" align="right">
												<span class="wz">发送状态：</span>
											</td>
											<td class="td1">
												<script>
												var ret = '${model.smsState}';
												if(ret=='1'){
													document.write("已发送");
												}else{
													document.write("未发送");
												}
												
												</script>
											</td>
										</tr>
										<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">返回状态：</span>
											</td>
											<td class="td1">
												<script>
												var ret = '${model.smsServerRet}';
												if(ret=='1'){
													document.write("发送成功");
												}else{
													if(ret=="2"){
														document.write("发送失败");
													}else{
														document.write("未发送");
													}
												}
												</script>
											</td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</DIV>
	</body>

</html>

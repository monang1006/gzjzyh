<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
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
		 var cap_max=70;//可发送的字数
		 var reg_mobile= /^(?:13\d|15[89])-?\d{5}(\d{3}|\*{3})$/;
		 //发送短信
		 function SendSms(){
		  	var telephone = document.getElementById("telephone").value;
		  	var content = document.getElementById("content").value;
		  	if(telephone == ""){
		  		alert("手机号码不能为空！");
		  		return;
		  	}
		  	if(content == ""){
		  		alert("短信内容不能空！");
		  		return;
		  	}
		    if(telephone != "" && content != "")
		  	{
		  		if(!reg_mobile.test(telephone)){
					alert("输入的手机号码格式不对\n请确认后重新输入！");
					return;
				}
				if(content.length > cap_max){
					alert("短信内容不能超过" + cap_max + "个字符.");
					return ;
				}
				$.post("<%=path%>/mobile/mobile!send.action",
						{telephone:telephone,content:content,userName:'${userName}'},
						function(msg){
							if(msg == "true"){
      							alert("短信发送成功！！");
      						}else{
      							alert("短信发送失败！！");
      						}
						});
		  	}
		 }
		 
		</script>
	</head>
	<base target="_self"/>
	<body oncontextmenu="return false;" class="contentbodymargin" >
	<input type="submit" id="submit" name="submit" value="" style="display: none">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center >
			<s:form action="mobile!send.action" theme="simple" name="smsForm" id="smsForm">
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
												<td>&nbsp;</td>
												<td width="227">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													发送手机短信
												</td>
												<td>
												</td>
												<td width="290">
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
											<span class="wz">接收人：</span>
										</td>
										<td class="td1">
											<s:property value="userRealName"/>
										</td>
									</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">手机号码(<font color=red>*</font>)：</span>
										</td>
										<td class="td1">
											<input type="text" id="telephone" style="width: 50%;" name="telephone" value="<s:property value='telephone'/>"/>
										</td>
									</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">指定发送时间：</span>
										</td>
										<td  class="td1">
											<strong:newdate name="smsSendDelay" id="smsSendDelay" width="50%" skin="whyGreen" isicon="true" mindate="%y-%M-%d %H:%m" dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
											 &nbsp;不指定则立即发送
										</td>
									</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">短信内容(<font color=red>*</font>)：</span>
										</td>
										<td  class="td1">
											<textarea cols=30 name="content" id="content" rows=5 class="BigInput" wrap="on"></textarea>
											<br>每条短信70字
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
							<input type="button" class="input_bg" value="确  定" onclick="SendSms();"/>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>

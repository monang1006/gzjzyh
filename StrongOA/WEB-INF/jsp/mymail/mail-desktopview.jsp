<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>  
		<%@include file="/common/include/meta.jsp" %>
		<title>查看邮件</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
		<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#replay").click(function(){
					window.returnValue="reply";
					window.close();
				});
				$("#del").click(function(){
					if(confirm("您确定要删除此封邮件么？")==true){
					  	$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/mymail/mail!delete.action",
					  		data:"sendid="+$("#sendid").val()+"&type=notreal",
					  		success:function(msg){
					  			if(msg=="true"){
					  				//alert("删除成功！");
					  				window.returnValue="del";
					  				window.close();
								}else{
									alert("删除失败请您重新删除");
					  			}
					  		}
					  	});
					  }
				});
			   $("#more").click(function(){
			   		if($("#more").val()=="更多"){
			   			$("#more").val("还原");
			   			$("#allre").show();
			   			$("#receiver").hide();
			   		}else{
						$("#more").val("更多");
						$("#allre").hide();	   	
						$("#receiver").show();		
			   		}
			   });
			   $("#send").click(function(){
			   		if($("#quicklyre").val()==""){
			   			alert("快速回复内容不能为空！");
			   		}else{
			   			document.retell.submit();
			   		}
			   });
			   $("#remail").click(function(){
			   		window.returnValue="resend";
			   		window.close();
			   });
			});
			function callback(msg){
				if(msg=="true"){
					//alert("快速回复邮件发送成功！");
					window.close();
				}else if(msg=="savefalse"){
					alert("邮件已经发送成功！但发件箱保存失败");
				}else{
					alert("快速回复邮件发送失败！");
				}
			}
		</script>
	</head>
	<body  class="contentbodymargin">

		<div id="contentborder" align="center">
			<table  width="100%" border="0" cellpadding="0" cellspacing="1"  class="table1">
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">日&nbsp;&nbsp;期：</span></td>
					<td class="td1" width="75%"><span class="wz">${myMail.mailSendDate }</span></td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">发件人：</span></td>
					<td class="td1" width="75%"><span class="wz">${myMail.mailSender }</span></td>
 				</tr>
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">收件人：</span></td>
					<td class="td1" width="75%">
						<span class="wz" id="receiver">
							${showReceiver }
						</span>
						<s:if test="myMail.mailReceiver!=showReceiver">
						<div id="allre" style="display:none;">
							${myMail.mailReceiver }
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="more" id="more" class="button" value="更多">
						</s:if>
					</td>
 				</tr>
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">主&nbsp;&nbsp;题：</span></td>
					<td class="td1" width="75%"><span class="wz">${myMail.mailTitle }</span></td>
 				</tr>
 					<s:if test="#request.attList!=null">
						<tr>
							<td class="biao_bg1" width="15%"><span class="wz">附&nbsp;&nbsp;件：</span></td>
							<td class="td1" width="75%">
								<span class="wz">
									<s:iterator value="#request.attList" status="statu" id="att">
										<a href="../mymail/mailAtt!download.action?attachId=<s:property value="#att.attachId"/>" target="myIframe" ><s:property value="#att.attachName" /></a>;
									</s:iterator>
								</span>
							</td>
		 				</tr>
 					</s:if>
				<tr>
					<td class="td1"  width="100%" colspan="2"><iframe id="iFrame1" name="iFrame1" height="500px" width="100%" onload="this.height=iFrame1.document.body.scrollHeight" frameborder="0" src="<%=root %>/mymail/mail!showInfo.action?sendid=${myMail.mailId }"></iframe>
					</td>
				</tr>
				<tr>
					<td class="td1" width="100%" colspan="2">
						<span class="wz">快捷回复</span>
					</td>
				</tr>
				<tr>
					<td class="td1" width="95%" colspan="2">
						<div align="center">
							<form name="retell" id="retell" action="<%=root %>/mymail/mail!quickSend.action" method="post" target="myIframe">
								<input type="hidden" name="sendid" id="sendid" value="${myMail.mailId }">
								<input type="hidden" name="boxId" id="boxId" value="${boxId }">
								<textarea id="quicklyre" name="quicklyre" class="textareaenter"></textarea>
								<iframe name="myIframe" style="display:none"></iframe>
							</form>
						</div>
					</td>
				</tr>
				<tr>
					
					<td class="td1" width="100%" colspan="2">
						<input type="button" class="button" id="send" name="send" value="发送" /> &nbsp;&nbsp;
						<input type="button" class="button" id="del" name="del" value="删除"/>
 					</td>
				</tr>
			</table>
		</div>

		<iframe name="myIframe" style="display:none"></iframe>

	</body>
</html>

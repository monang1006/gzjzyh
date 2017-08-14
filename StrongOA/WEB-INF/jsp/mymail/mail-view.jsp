<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
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
					if(confirm("删除此邮件，确定？")==true){
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
			   		if($("#more").val()=="..."){
			   			$("#more").val("还原");
			   			$("#allre").show();
			   			$("#receiver").hide();
			   		}else{
						$("#more").val("...");
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
		<table width="100%" height="38px;"
			style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
			<tr>
				<td>&nbsp;</td>
				<td width="30%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					阅读邮件
				</td>
				<td width="70%">
					<table border="0" align="right" cellpadding="00" cellspacing="0">
		                <tr>
		                  <td width="*"></td>
		                  <s:if test="myMail.toaMailFolder.mailfolderType==1">
		                  <td><a class="Operation" href="#" onclick="$('#replay').click()"><img src="<%=root%>/images/ico/banli.gif" width="15" height="15" class="img_s">回复</a></td>
		                  <td width="5"></td>
		                  </s:if>
		                  <td><a class="Operation" href="#" onclick="$('#remail').click()"><img src="<%=root%>/images/ico/fasong.gif" width="15" height="15" class="img_s">转发</a></td>
		                  <td width="5"></td>
		                  <td><a class="Operation" href="#" onclick="$('#del').click()"><img src="<%=frameroot%>/images/perspective_leftside/shanchu.gif" width="15" height="15" class="img_s">删除</a></td>
		                  <td width="5"></td>
		                  <td><a class="Operation" href="#" onclick="window.close();"><img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">关闭</a></td>
		                  <td width="5"></td>
		                </tr>
		            </table>
				</td>
			</tr>
		</table>
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
						<input type="button" name="more" id="more" class="button" value="..." title="显示更多人员">
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
				<tr style="display: none">
					<td class="td1" width="100%" colspan="2">
						<span class="wz">快捷回复</span>
					</td>
				</tr>
				<tr style="display: none">
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
					
					<td  width="100%" colspan="2">
						<%--<input type="button" class="button" id="send" name="send" value="发送" /> &nbsp;&nbsp;
						--%>
						<table width="100%" height="38px;"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
								</td>
								<td width="70%">
									<table border="0" align="left" cellpadding="00" cellspacing="0">
						                <tr>
						                  <td width="*"></td>
						                  <s:if test="myMail.toaMailFolder.mailfolderType==1">
						                  <td><input type="button" class="Operation" id="replay" name="replay" style="width: 40px;height: 23px;"  value="回复"/></td>
						                  <td width="5"></td>
						                  </s:if>
						                  <td><input type="button" class="Operation" id="remail" name="remail" style="width: 40px;height: 23px;" value="转发"/></td>
						                  <td width="5"></td>
						                  <td><input type="button" class="Operation" id="del" name="del" style="width: 40px;height: 23px;"  value="删除"/></td>
						                  <td width="5"></td>
						                  <td><input type="button" class="Operation" id="close" name="close" style="width: 40px;height: 23px;"  value="关闭" onclick="window.close();"/></td>
						                  <td width="5"></td>
						                </tr>
						            </table>
								</td>
							</tr>
						</table>
 					</td>
				</tr>
			</table>
		</div>

		<iframe name="myIframe" style="display:none"></iframe>

	</body>
</html>

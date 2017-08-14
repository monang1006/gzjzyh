<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看消息</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet"
			type="text/css">
		<link href="<%=frameroot%>/css/properties_windows.css"
			rel="stylesheet" type="text/css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<style>
			a:link,a:visited,a:hover,a:active{ 
			color:blue;
			}
			</style>
		<script type="text/javascript">
	
			$(document).ready(function(){
				
			   $("#more").click(function(){
			   		if($("#more").html()=="更多"){
			   			$("#more").html("还原");
			   			$("#allre").show();
			   			$("#receiverShow").hide();
			   		}else{
						$("#more").html("更多");
						$("#allre").hide();	   	
						$("#receiverShow").show();		
			   		}
			   });
			   $("#send").click(function(){
			   		if($("#quicklyre").val()==""){
			   			alert("快速回复内容不能为空！");
			   		}else{
			   			document.retell.submit();			
			   		}
			   });
			});
			function del(){
				var forward ="";
				var tempStr = "";
				if("${model.toaMessageFolder.msgFolderName}"=="垃圾箱"){
					forward="real";
					tempStr = "彻底";
				}else{
					forward="notreal";
				}
				if(confirm(tempStr+"删除此消息，确定？")==true){
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/message/message!delete.action",
				  		data:"msgId="+$("#msgId").val()+"&forward="+forward,
				  		success:function(msg){
				  			if(msg=="true"){
				  				//alert("删除成功！");
				  				window.returnValue="del";
				  				window.close();
				  				window.opener.location.reload();
							}else{
								alert("删除失败请您重新删除!");
				  			}
				  		}
				  	});
				  }
			}
			
			function remail(){
		   		var msgId = "${model.msgId }";
		   		var newWin=OpenWindow('<%=root%>/message/message!tran.action?msgId='+msgId, '700', '446', window);
		   		window.close();
		   		//window.opener.resend(msgId);
			}
			function replay(){
				var sender = "${model.msgSender}";
				if(sender.indexOf("<id:system>")>0){
					alert("不能回复系统消息！")
				}else{
					var msgId = "${model.msgId }";
			   		var newWin=OpenWindow('<%=root%>/message/message!reply.action?msgId='+msgId, '700', '446', window);
			   		window.close();
			   		//window.opener.retell(msgId);
				}
			}
			function sendagain(){
			   		var msgId = "${model.msgId }";
			   		var newWin=OpenWindow('<%=root%>/message/message!input.action?moduleCode=<%=GlobalBaseData.SMSCODE_MESSAGE%>&msgId='+msgId, '700', '446', window);
			   		window.close();
			   		//window.opener.sendagain(msgId);
			}
			function callback(msg){
				if(msg=="true"){
					alert("快速回复消息发送成功！");
					window.close();
				}else if(msg=="savefalse"){
					alert("消息已经发送成功！但发件箱保存失败");
				}else{
					alert("快速回复消息发送失败！");
				}
			}
		</script>
	</head>
	<body class="contentbodymargin" onload="isReceipt()">
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										查看消息
									</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
											<s:if
												test="model.toaMessageFolder.msgFolderName!=null&&model.toaMessageFolder.msgFolderName==\"收件箱\"">
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="replay();">&nbsp;回&nbsp;复&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
											</s:if>
											<s:if
												test="model.toaMessageFolder.msgFolderName!=null&&model.toaMessageFolder.msgFolderName==\"发件箱\"">
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="sendagain();">&nbsp;再&nbsp;次&nbsp;发&nbsp;送&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
											</s:if>
											<s:else>
											</s:else>
											<s:if
												test="model.toaMessageFolder.msgFolderName!=null&&model.toaMessageFolder.msgFolderName==\"草稿箱\"">

											</s:if>
											<s:else>
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="remail();">&nbsp;转&nbsp;发&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
											</s:else>

											<s:if
												test="model.toaMessageFolder.msgFolderName!=null&&model.toaMessageFolder.msgFolderName==\"垃圾箱\"">
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="del();">&nbsp;彻&nbsp;底&nbsp;删&nbsp;除&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
											</s:if>
											<s:else>
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="del();">&nbsp;删&nbsp;除&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
											</s:else>
											<td width="5"></td>
											<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
						                 	<td class="Operation_input1" onclick="javascript:window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
						                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
											<td width="5"></td>
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
				<input type="hidden" id="sfxyhz" name="sfxyhz" value="${sfxyhz }" />
				<input type="hidden" id="isReturnBack" name="isReturnBack"
					value="${model.isReturnBack }" />
				<input id="receiver" name="Receiver" value="${model.msgReceiver}"
					type="hidden" />
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">日&nbsp;&nbsp;期：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz"> <s:date name="model.msgDate"
								format="yyyy-MM-dd HH:mm" /> </span>
					</td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">发件人：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz">${model.msgSender }</span>
					</td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">收件人：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz" id="receiverShow"> ${showReceiver } </span>
						<s:if test="model.msgReceiver!=showReceiver">
							<div id="allre" style="display: none;">
								<span class="wz">${model.msgReceiver }</span>
							</div>
						<!--  <input type="button" name="more" id="more" class="Operation"
								style="display: block;" value="更多">-->
		<a id="more" href="#" class="button"  style="display: block; width:73px; height:24px; line-height:24px; border:none; background:url(<%=root %>/frame/theme_red/images/add_bt.jpg) no-repeat center;FONT-SIZE: 14px; FONT-FAMILY: Tahoma, Verdana, Arial, Helvetica;text-align: center;" >更多</a>
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right">
						<span class="wz">标&nbsp;&nbsp;题：</span>
					</td>
					<td class="td1" width="75%">
						<span class="wz">${model.msgTitle }</span>
					</td>
				</tr>
				<s:if test="attachMentList!=null">
					<tr>
						<td class="biao_bg1" width="15%" align="right">
							<span class="wz">附&nbsp;&nbsp;件：</span>
						</td>
						<td class="td1" width="75%">
							<s:iterator value="attachMentList" status="statu" id="att">
								<div>
									<img src="<%=path%>/oa/image/mymail/yes.gif">
									<a
										href="../message/messageAtt!download.action?attachId=<s:property value="#att.attachId"/>"
										target="myIframe"><s:property value="#att.attachName" />
									</a>
								</div>
							</s:iterator>
						</td>
					</tr>
				</s:if>
				<tr>
					<td class="td1" width="100%" colspan="2" id="ArticleContent">
						<%--<iframe id="iFrame1" name="iFrame1" height="500px" width="100%"
							onload="this.height=iFrame1.document.body.scrollHeight"
							frameborder="0"
							src="<%=root%>/message/message!quicklyview.action?forward=showInfo&msgId=${model.msgId }"></iframe>
						--%>
						<div id="afficheDesc" style="display: none">
							${model.msgContent}
						</div>
						<script type="text/javascript">
				         document.getElementById("ArticleContent").innerHTML = document.getElementById('afficheDesc').innerText;
				        </script>
					</td>
				</tr>
				<tr style="display: none;">
					<td class="td1" width="100%" colspan="2">
						<span class="wz">快捷回复</span>
					</td>
				</tr>
				<tr style="display: none;">
					<td class="td1" width="95%" colspan="2">
						<div align="center">
							<form name="retell" id="retell"
								action="<%=root%>/message/message!quickSend.action"
								method="post" target="myIframe">
								<input type="hidden" name="msgId" id="msgId"
									value="${model.msgId}">
								<%--								<input type="hidden" name="boxId" id="boxId" value="${boxId }">--%>
								<textarea id="quicklyre" name="quicklyre"
									class="textareaenter wz"></textarea>
								<iframe name="myIframe" style="display: none"></iframe>
							</form>
						</div>
					</td>
				</tr>
				
			</table>
		</div>

		<iframe name="myIframe" style="display: none"></iframe>
		<SCRIPT type="text/javascript">
//			var receiver=document.getElementById("receiver").value
//			var array =receiver.split(",");
//			var length=array.length-1;   	
//			if(length>1){
//				document.getElementById("more").style.display="";
//			}
			
			function isReceipt(){	//回执弹出框
				var msgId=document.getElementById("msgId").value;
				var isReturnBack=document.getElementById("isReturnBack").value
				var isSendBack=document.getElementById("sfxyhz").value;
				if((isSendBack!=null&&isSendBack=="1")&&(isReturnBack==null||isReturnBack==""||isReturnBack=="0")){
					var rtn=OpenWindow('fileNameRedirectAction.action?toPage=message/message-receipt.jsp?msgId='+msgId, '250', '150', window);
					//alert(rtn);
				}				
			}
		</SCRIPT>
	</body>
</html>

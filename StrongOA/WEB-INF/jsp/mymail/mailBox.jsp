<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>邮件模块导航</title>
		<link href="<%=frameroot%>/css/treeview.css" rel="stylesheet" type="text/css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
		<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>
		<script type="text/javascript">
			var simpleTreeCollection;
			$(document).ready(function(){
				simpleTreeCollection = $('.simpleTree').simpleTree({
					drag:false,
					autoclose: false,
					afterClick:function(node){
						//$("#myMenu1").hide();
						//alert("text-"+$('span:first',node).text());
						if($('span:first',node).attr("id")=='son'){
							window.parent.mail_content.location="fileNameRedirectAction.action?toPage=mymail/getmail_container.jsp?sendid="+$('span:first',node).parent().attr("id")+"&folder="+$('span:first',node).parent().attr("foldertype");
						}
					},
					afterDblClick:function(node){
						//alert("text-"+$('span:first',node).text());
					},
					afterMove:function(destination, source, pos){
						//alert("destination-"+destination.attr('id')+" source-"+source.attr('id')+" pos-"+pos);
					},
					afterAjax:function()
					{
						//alert('Loaded');
					},
					afterContextMenu:function(node){
						if($('span:first',node).attr("id")=='son'){
							if($('span:first',node).parent().attr("foldertype")==5){
								  $($('span:first',node)).contextMenu('myMenu2', {
								  bindings: {
									'addf': function(t) {
										var boo=OpenWindow('<%=root%>/mymail/mailFolder!input.action?parentid='+$('span:first',node).parent().parent().parent().attr("id")+"&type=show", '350', '100', window);
										if(boo=="true"){
											window.location.reload();
										}
									},
									'editf': function(t) {
									  	$.ajax({
									  		type:"post",
									  		dataType:"text",
									  		url:"<%=root%>/mymail/mailFolder!edit.action",
									  		data:"type=charge&id="+$('span:first',node).parent().attr("id"),
									  		success:function(msg){
									  			if(msg=="true"){
									  				alert("基础邮件夹不允许修改！");
									  			}else if(msg=="noid"){
													alert("传入值异常！");
									  			}else{
									  				var boo=OpenWindow('<%=root%>/mymail/mailFolder!edit.action?type=show&id='+$('span:first',node).parent().attr("id"), '350', '100', window);
									  				if(boo=="true"){
									  					window.location.reload();
									  				}
									  			}
									  		}
									  	});
									},
									'delf': function(t) {
										if(confirm("删除邮件夹，邮件夹中的邮件将会一并删除,您是否还要继续删除？")==true){
											$.ajax({
												type:"post",
												dataType:"text",
												url:"<%=root%>/mymail/mailFolder!delete.action",
												data:"id="+$('span:first',node).parent().attr("id"),
												success:function(msg){
													if(msg=="true"){
														//alert("删除文件夹成功！");
														window.location.reload();
														window.parent.mail_content.location.reload();
													}else if(msg=="cant"){
														alert("基础邮件夹不允许删除！");
													}else if(msg=="noid"){
														alert("传入值错误，请您再试！");
													}else{
														alert("删除出现错误请您再试！");
													}
												}
											});
										}
									}
								  }
								});
							}else{
								$($('span:first',node)).contextMenu('myMenu4', {
								  bindings: {
									'addf': function(t) {
										var boo=OpenWindow('<%=root%>/mymail/mailFolder!input.action?parentid='+$('span:first',node).parent().parent().parent().attr("id")+"&type=show", '350', '100', window);
										if(boo=="true"){
											window.location.reload();
										}
									}
								  }
								});
							}
						}else{
							$($('span:first',node)).contextMenu('myMenu1', {
							  bindings: {
								'getmail': function(t) {
									window.parent.mail_content.showshadow("正在准备开始收取邮件...");
									$("#sendid").val($('span:first',node).parent().attr("id"));
									go();
								},
								'add': function(t) {
									var boo=OpenWindow('<%=root%>/mymail/mailBox!input.action?type=showinput', '400', '320', window);
									if(boo=="true"){
										//alert("邮箱新建成功！");
										window.location.reload();
									}
								},
								'edit': function(t) {
									var boo=OpenWindow('<%=root%>/mymail/mailBox!edit.action?type=showedit&id='+$('span:first',node).parent().attr("id"), '400', '320', window);
									if(boo=="true"){
										window.location.reload();
									}
								},
								'delete': function(t) {
									if(confirm("删除邮箱的同时也会将该邮箱中的邮件一同删除，您是否还要继续删除？")==true){
										$.ajax({
											type:"post",
											dataType:"text",
											url:"<%=root%>/mymail/mailBox!delete.action",
											data:"id="+$('span:first',node).parent().attr("id"),
											success:function(msg){
												if(msg=="true"){
													//alert("删除邮箱成功！");
													window.location.reload();
													window.parent.mail_content.location.reload();

												}else if(msg=="false"){
													alert("删除异常，请您再试！");
												}else if(msg=="noid"){
													alert("传入值错误，请您再试");
												}else{
													alert("所删除内容不存在！");
												}
											}
										});
									}
								}
							  }
							});
						}
					},
					animate:false
				});
				$("#rootSpan").contextMenu("myMenu3",{
					bindings:{
						'addroot':function(){
							var boo=OpenWindow('<%=root%>/mymail/mailBox!input.action?type=showinput', '400', '320', window);
							if(boo=="true"){
								//alert("邮箱新建成功！");
								window.location.reload();
							}
						}
					}
				});
			});
			
			function getmail(){
				$.ajax({
					type:"post",
					dataType:"text",
					url:"<%=root%>/mymail/mail!showStatus.action",
					success:function(msg){
						if(msg<=100||msg=="null"){
							window.parent.mail_content.mail_content_list.showshadow(msg);
							setTimeout("getmail()",1000);
						}else{
							//alert("ddd");
						}
					}
				});
			}
						
			
			function createXMLHttpRequest(){
				if (window.ActiveXObject) {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				}
				else if (window.XMLHttpRequest) {
					xmlHttp = new XMLHttpRequest();
				}
			}
			function go() {
				document.getForm.submit();
				createXMLHttpRequest();
				var url = "<%=root%>/mymail/mail!showStatus.action";
				xmlHttp.open("GET", url, true);
				xmlHttp.setRequestHeader("Content-Type", "text/xml;charset=gb2312");
				xmlHttp.onreadystatechange = goCallback;
				xmlHttp.send(null);
			}
			function goCallback(){
				if (xmlHttp.readyState==4) 
				{
					if (xmlHttp.status == 200) {
						setTimeout("pollServer()", 2000);
					}
				}
			}
			function pollServer() {
				createXMLHttpRequest();
				var url = "<%=root%>/mymail/mail!showStatus.action";
				xmlHttp.open("GET", url, true);
				xmlHttp.onreadystatechange = pollCallback;
				xmlHttp.send(null);
			}
			function pollCallback(){
				if (xmlHttp.readyState == 4) {
					if (xmlHttp.status == 200) {
						var total = parseInt(xmlHttp.responseXML.getElementsByTagName("total")[0].firstChild.data);
						var status= parseInt(xmlHttp.responseXML.getElementsByTagName("status")[0].firstChild.data);
						if(status=="null"){
							setTimeout("pollServer()", 2000);
						}else{
							if(total=="10000"){
								window.parent.mail_content.showshadow("正在准备开始收取邮件...");
								setTimeout("pollServer()", 2000);
							}else if(total=="-1"){
								window.parent.mail_content.hiddenshadow();
								alert("对不起收件错误，请您查看您的邮件配置!");
							}else if(total=="-2"){
								gotoAction()
							}else{
								window.parent.mail_content.showshadow("共有"+total+"封邮件，目前正在收取第"+status+"封邮件");
								setTimeout("pollServer()", 2000);
							}
						}
					}
				}
			} 
			function gotoAction(){
				if(window.parent.mail_content.mail_content_list!="undefined"){
					window.parent.mail_content.mail_content_list.location="<%=root%>/mymail/mail.action?sendid="+$("#sendid").val()+"&type=afterrec";
				}else{
					alert("邮件收取完毕请您查看！");
				}
			}
		</script>
	</head>
	 
	<body class=contentbodymargin oncontextmenu="return false;">
	<DIV id=contentborder align=left>
			<ul class="simpleTree" style="width:100%">
				<li class="root" id='root'><span style="cursor: hand;" id="rootSpan">邮箱管理</span>
					<ul>
					<s:iterator value="#request.mailBoxList" status="statu" id="mailBox">
					<li id='<s:property value="#mailBox.mailboxId"/>'><span><s:property value="#mailBox.mailboxUserName"/></span>
						<ul>
							<s:iterator value="#mailBox.toaMailFolders" id="folder">
								<li id='<s:property value="#folder.mailfolderId"/>' foldertype='<s:property value="#folder.mailfolderType"/>'>
									<span id="son"><s:property value="#folder.mailfolderName"/>(<s:property value="#folder.mailNum"/>)</span>
								</li>
							</s:iterator>
						</ul>
					</li>
					</s:iterator>
					</ul>
				</li>
			</ul>
		<div class="contextMenu" id="myMenu1">
			<ul>
				<li id="getmail"><img src="<%=root %>/oa/image/mymail/arrow_refresh.png" /><SPAN class="wz"> 收取邮件</SPAN></li>
				<li id="add"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz"> 新建邮箱</SPAN></li>
				<li id="delete"><img src="<%=root %>/oa/image/mymail/folder_delete.png" /><SPAN class="wz"> 删除邮箱</SPAN></li>
				<li id="edit"><img src="<%=root %>/oa/image/mymail/folder_edit.png" /><SPAN class="wz"> 邮箱属性</SPAN></li>
			</ul>
		</div>
		<div class="contextMenu" id="myMenu2">
			<ul>
				<li id="addf"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz"> 新建邮件夹</SPAN></li>
				<li id="editf"><img src="<%=root %>/oa/image/mymail/folder_edit.png" /><SPAN class="wz"> 邮件夹重命名</SPAN></li>
				<li id="delf"><img src="<%=root %>/oa/image/mymail/page_delete.png" /><SPAN class="wz"> 删除邮件夹</SPAN></li>
			</ul>
		</div>
		<div class="contextMenu" id="myMenu4">
			<ul>
				<li id="addf"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz"> 新建邮件夹</SPAN></li>
			</ul>
		</div>
		<div class="contextMenu" id="myMenu3">
			<ul>
				<li id="addroot"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz"> 新建邮箱</SPAN></li>
			</ul>
		</div>
		<form name=getForm id=getForm action="<%=root%>/mymail/mail!getMail.action" target="myIframe">
			<input type="hidden" name="sendid" id="sendid">
			<iframe name="myIframe" style="display:none"></iframe>
		</form>
		</DIV>
	</body>
</html>

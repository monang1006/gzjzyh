<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<title>消息模块导航</title>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
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
						//if($('span:first',node).attr("id")=='son'){
						//	window.parent.msg_content.location="fileNameRedirectAction.action?toPage=message/message-content.jsp?folderId="+$('span:first',node).parent().attr("id");
						//}
						window.parent.msg_content.msg_content_list.location="<%=path%>/message/message.action?folderId="+$('span:first',node).parent().attr("id");
						window.parent.msg_content.msg_main_content.personal_status_content.location="<%=root %>/fileNameRedirectAction.action?toPage=message/info.jsp";
						//alert("haha");
						//window.location.reload();
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
						if($('span:first',node).parent().attr("id").length<30){
							$($('span:first',node)).contextMenu('myMenu2', {
							  bindings: {
								'getlist': function(t) {
									window.parent.msg_content.msg_content_list.location="<%=path%>/message/message.action?folderId="+$('span:first',node).parent().attr("id");
									window.parent.msg_content.msg_main_content.personal_status_content.location="<%=root %>/fileNameRedirectAction.action?toPage=message/info.jsp";
								},
								//新建文件夹
								'add': function(t) {
									var boo=OpenWindow('<%=path%>/message/messageFolder!input.action', '400', '150', window);
									if(boo=="reload"){
										//alert("添加成功！");
										//window.location.reload();
										window.parent.location.reload();
									}
								},
								'edit': function(t) {
								},
								'delete': function(t) { }
							  }
							});
						}else{
							$($('span:first',node)).contextMenu('myMenu1', {
							  bindings: {
								'getlist': function(t) {
									/*window.parent.mail_content.showshadow("正在准备开始收取消息...");
									$("#sendid").val($('span:first',node).parent().attr("id"));
									go();*/
									window.parent.msg_content.msg_content_list.location="<%=path%>/message/message.action?folderId="+$('span:first',node).parent().attr("id");
									window.parent.msg_content.msg_main_content.personal_status_content.location="<%=root %>/fileNameRedirectAction.action?toPage=message/info.jsp";
								},
								//新建文件夹
								'add': function(t) {
									var boo=OpenWindow('<%=path%>/message/messageFolder!input.action', '400', '150', window);
									if(boo=="reload"){
										//alert("添加成功！");
										//window.location.reload();
										window.parent.location.reload();
									}
								},
								'edit': function(t) {
									if($('span:first',node).parent().attr("id").length<30){
										alert("不能修改基础文件夹!");
										return false;
									}
									var boo=OpenWindow('<%=path%>/message/messageFolder!input.action?msgFolderId='+$('span:first',node).parent().attr("id"), '400', '150', window);
									if(boo=="reload"){
										window.location.reload();
									}
								},
								'delete': function(t) {
										$.ajax({
											type:"post",
											dataType:"text",
											url:"<%=root%>/message/messageFolder!isNullFolder.action",
											data:"msgFolderId="+$('span:first',node).parent().attr("id"),
											success:function(msg){
												if(msg=="notNull"){
													if(confirm("文件夹“"+$('span:first',node).text()+"”存有内容，删除此文件夹及存有的内容，确定？")==true){
														$.ajax({
															type:"post",
															dataType:"text",
															url:"<%=root%>/message/messageFolder!delete.action",
															data:"msgFolderId="+$('span:first',node).parent().attr("id"),
															success:function(msg){
																if(msg=="true"){
																	//alert("文件夹删除成功！");
															//		window.location.reload();
															//		window.parent.mail_content.location.reload();
																window.parent.location.reload();
																}else if(msg=="false"){
																	alert("删除异常，请您再试！");
																}else if(msg=="noid"){
																	alert("传入值错误，请您再试!");
																}else if(msg=="baseFolder"){
																	alert("不能删除基础文件夹");
																}else{
																	alert("所删除内容不存在！");
																}
															}
														});
													}
												}else if(msg=="nullFolder"){
													if(confirm("删除文件夹“"+$('span:first',node).text()+"”，确定？")==true){
														$.ajax({
															type:"post",
															dataType:"text",
															url:"<%=root%>/message/messageFolder!delete.action",
															data:"msgFolderId="+$('span:first',node).parent().attr("id"),
															success:function(msg){
																if(msg=="true"){
																	//alert("文件夹删除成功！");
																//	window.location.reload();
																//	window.parent.mail_content.location.reload();
																window.parent.location.reload();
																}else if(msg=="false"){
																	alert("删除异常，请您再试！");
																}else if(msg=="noid"){
																	alert("传入值错误，请您再试!");
																}else if(msg=="notdel"){
																	alert("请先删除该文件夹下的消息");
																}else if(msg=="baseFolder"){
																	alert("不能删除基础文件夹");
																}else{
																	alert("所删除内容不存在！");
																}
															}
														});
													}
												}else if(msg=="noid"){
													alert("传入值错误，请您再试!");
												}else if(msg=="baseFolder"){
													alert("不能删除基础文件夹");
												}else{
													alert("所删除内容不存在！");
												}
											}
										});
								}
							  }
							});
						}
					},
					animate:false
					
				});
				//根目录右键
				$("#rootSpan").contextMenu("myMenu3",{
					bindings:{
						'addroot':function(){
							var boo=OpenWindow('<%=path%>/message/messageFolder!input.action', '400', '150', window);
									if(boo=="reload"){
										//alert("添加成功！");
										window.parent.location.reload();
									}
						}
					}
				});
				var nn = $('.cont>li');
				for(var i=0;i<nn.size();i++){
					if($(nn.get(i)).attr("id")!=null&&$(nn.get(i)).attr("id")!=""){
						id = $(nn.get(i)).attr("id");
						$.ajax({
							type:'post',
							dataType:"text",
							data:"msgFolderId="+id,
							url:'<%=root%>/message/messageFolder!getAll.action',
							success:function(res){
								var msg = res.split(",");
								//$("#"+msg[0]).append("<span id=\"Folder_"+id+"\"><font style=\"color: blue;\">（"+msg[1]+"/"+msg[2]+"）</font></span>");
								$("#"+msg[0]).append("<span id=\"Folder_"+id+"\">(<font>"+msg[1]+"</font>)</span>");
							}
						});
					}
				}
				
			});
			
			//刷新某个未读记数
			function freashUnread(folderId){
				$.ajax({
					type:'post',
					dataType:"text",
					data:"msgFolderId="+folderId,
					url:'<%=root%>/message/messageFolder!getAll.action',
					success:function(res){
						var msg = res.split(",");
						//$('span:eq(1)',$("#"+msg[0])).html("<font style=\"color: blue;\">（"+msg[1]+"/"+msg[2]+"）</font>");
						$('span:eq(1)',$("#"+msg[0])).html("(<font>"+msg[1]+"</font>)");
					}
				});
			}
			
			//刷新所有未读记数
			function freashAllUnread(){
				freashUnread("recv");
				freashUnread("send");
				freashUnread("draft");
				freashUnread("rubbish");
				
				<s:iterator value="#request.folderList" status="statu" id="folderList">
						freashUnread('<s:property value="#folderList.msgFolderId"/>');
				</s:iterator>
			}
			
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
								window.parent.mail_content.showshadow("正在准备开始收取消息...");
								setTimeout("pollServer()", 2000);
							}else if(total=="-1"){
								window.parent.mail_content.hiddenshadow();
								alert("对不起收件错误，请您查看您的消息配置!");
							}else if(total=="-2"){
								gotoAction()
							}else{
								window.parent.mail_content.showshadow("共有"+total+"条消息，目前正在收取第"+status+"条消息");
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
					alert("消息收取完毕请您查看!");
				}
			}
		</script>
	</head>
	 
	<body oncontextmenu="return false;" title="右键选择文件夹进行相关操作" style="background-color: #eeeff3;">
<%--	<DIV id=contentborder >--%>
	<DIV  >
			<div>
				</br>
			</div>
			<ul class="simpleTree" style="width:100%">
				<li class="root" id='root'><span style="cursor: hand;" id="rootSpan" title="点击右键可新建自定义文件夹">内部邮件</span>
					<ul class="cont">
					<li id='recv'><span>收件箱</span>
					</li>
					<li id='send'><span>发件箱</span>
					</li>
					<li id='draft'><span>草稿箱</span> 
					</li>
					<li id='rubbish'><span>垃圾箱</span> 
					</li>
					<s:iterator value="#request.folderList" status="statu" id="folderList">
						<li id='<s:property value="#folderList.msgFolderId"/>'>
							<span><s:property value="#folderList.msgFolderName"/></span>
						</li>
					</s:iterator>
					</ul>
				</li>
			</ul>
	<div class="contextMenu" id="myMenu1">
			<ul>
				<li id="getlist"><img src="<%=root %>/oa/image/mymail/arrow_refresh.png" /><SPAN class="wz">&nbsp;查看列表</SPAN> </li>
				<li id="add"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz">&nbsp;新建文件夹</SPAN> </li>
				<li id="edit"><img src="<%=root %>/oa/image/mymail/folder_edit.png" /><SPAN class="wz">&nbsp;文件夹重命名</SPAN> </li>
				<li id="delete"><img src="<%=root %>/oa/image/mymail/folder_delete.png" /><SPAN class="wz">&nbsp;删除文件夹</SPAN> </li>
			</ul>
		</div>
		<div class="contextMenu" id="myMenu2">
			<ul>
				<li id="getlist"><img src="<%=root %>/oa/image/mymail/arrow_refresh.png" /><SPAN class="wz">&nbsp;查看列表</SPAN> </li>
				<li id="add"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz">&nbsp;新建文件夹</SPAN> </li>
			</ul>
		</div>
		<div class="contextMenu" id="myMenu3">
			<ul>
				<li id="addroot"><img src="<%=root %>/oa/image/mymail/folder_add.png" /><SPAN class="wz">&nbsp;新建文件夹</SPAN> </li>
			</ul>
		</div>
		<form name=getForm id=getForm action="<%=root%>/mymail/mail!getMail.action" target="myIframe">
			<input type="hidden" name="sendid" id="sendid">
			<iframe name="myIframe" style="display:none"></iframe>
		</form>
		
	</DIV>
	</body>
</html>

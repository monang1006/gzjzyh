<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.im.cache.Cache"/>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>同步用户到-即时通讯软件</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/common/frame';
		</script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<style type="text/css">
			#contentborder{
				BORDER-RIGHT: #848284 1px solid;
				PADDING-RIGHT: 3px;
				BORDER-TOP: #848284 1px solid;
				PADDING-LEFT: 3px;
				BACKGROUND: white;
				PADDING-BOTTOM: 14px;
				OVERFLOW: auto;
				BORDER-LEFT: #848284 1px solid;
				PADDING-TOP: 0px;
				BORDER-BOTTOM: #848284 1px solid;
			}
			
		</style>
		
				<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
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
				$("form").submit();
				createXMLHttpRequest();
				var url = "<%=root%>/im/iM!oa2rtxstatus.action";
				xmlHttp.open("GET", url, true);
				xmlHttp.setRequestHeader("Content-Type", "text/xml;charset=gb2312");
				xmlHttp.onreadystatechange = goCallback;
				xmlHttp.send(null);
			}
			function goCallback(){
				if (xmlHttp.readyState==4) 
				{
					if (xmlHttp.status == 200) {
						show("正在开始同步用户数据...");
						setTimeout("pollServer()", 1000);
					}
				}
			}
			
			function pollServer() {
			
				createXMLHttpRequest();
				var url = "<%=root%>/im/iM!oa2rtxstatus.action";
				xmlHttp.open("GET", url, true);
				xmlHttp.onreadystatechange = pollCallback;
				xmlHttp.send(null);
			}
			function pollCallback(){
				if (xmlHttp.readyState == 4) {
					if (xmlHttp.status == 200) {
						var msg = xmlHttp.responseXML.getElementsByTagName("msg")[0].firstChild.data;
						if(msg!="ok"){
							show(msg);
							setTimeout("pollServer()", 1000);
						}else{
							show("从OA向Rtx同步数据完成!");
							$.post("<%=root%>/im/iM!removeAjaxMsg.action",
									function(data){
										setTimeout("hidden()", 2000);
										setTimeout("over()", 2000);
									});
							
							
						}
					}
				}
			} 
		
			function over(){
				location = "<%=root%>/address/addressOrg!systemUserTree.action";
			}
		
			$(document).ready(function(){
				$("#oa2rtx").click(function(){
					if(!confirm("数据同步可能需要花费很长时间，确定要继续操作吗？")){
						return;
					}
					//go();
					$.getJSON("<%=root%>/im/iM!findServerConfigBySyn.action?timestamp="+new Date(),
							  function(jsdata){
							  	if("error"!=jsdata){
							  		var ip = jsdata.ip;
							  		var port = jsdata.port;
							  		var rtxEnabled = jsdata.state;
							  		if(rtxEnabled == "1"){
								  		if(ip ==undefined || ip == "" || port == undefined || port == ""){
								  			show("系统检测到您还未配置服务器.请在“系统管理->即时通讯软件配置->服务器配置”中设置正确的IP地址和端口号!")
								  			setTimeout("hidden()", 10000);
								  		}else{
								  			show("同步OA用户数据中,请耐心等待...");
											$.post("<%=root%>/im/iM!oa2im.action",
												function(data){
													if(data == "ok"){
														show("从OA向<%=Cache.getIMName() %>同步数据完成!");
														setTimeout("hidden()", 2000);
														setTimeout("over()", 2000);
													}else{
														show("从OA向<%=Cache.getIMName() %>同步数据失败!请重试或与管理员联系!");
														setTimeout("hidden()", 2000);
													}
													
												});
								  		}
							  		}else{
							  			show("系统检测到即时通讯软件未启用.请在“系统管理->即时通讯软件配置->服务器配置”中开启即时通讯软件!")
								  		setTimeout("hidden()", 10000);
							  		}
							  	}
					});
					
				});
			});
		</script>
		
	</HEAD>
	<BODY class=contentbodymargin ><!-- oncontextmenu="return false;" -->
		<DIV id=contentborder align="center" style="overflow: auto;height: 100%;" cellpadding="0">
				<table width="100%"  height="100%;" border="0" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
					<tr height="5%;">
					
						<td  colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">&nbsp;&nbsp;&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;&nbsp;<strong>用户同步</strong></td>
					
					</tr>
					<tr height="85%" >
						<td width="100%">
							<table width="100%" align="center" style="height: 100%;width: 80%;" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
								<tr>
									<td>
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
											<iframe frameborder="0" src="<%=root %>/address/addressOrg!systree.action" width="100%"  height="100%"></iframe>
										</DIV>
									</td>
									<td width="30%" >
										<DIV id=contentborder style="background-color: white;text-align: center;" cellpadding="0">
									        <p>&nbsp; </p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>&nbsp;</p>
									        <p>
										       <form action="<%=root%>/im/iM!oa2rtx.action" method="post" target="myIframe"> 
										          <input id="oa2rtx" type="hidden"   class="input_bg" value=" 同步到<%=Cache.getIMName() %> " />
										          <a id="oa2rtx"  href="#" class="button8" onclick="$('#oa2rtx').click()">同步到<%=Cache.getIMName()%></a>&nbsp;
										          <iframe name="myIframe" style="display:none"></iframe>
										       </form>   
								            </p>
										</DIV>	
								  </td>
									<td>
										<DIV id=contentborder style="background-color: white;" cellpadding="0">
											<iframe frameborder="0" src="<%=root %>/im/iM.action" width="100%"  height="100%"></iframe>
										</DIV>
								  </td>
								</tr>
						  </table>
					  </td>
					</tr>
			  </table>
		</DIV>
	</BODY>
</HTML>

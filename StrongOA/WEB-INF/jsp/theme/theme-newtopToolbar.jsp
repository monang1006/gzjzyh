<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公共top区</title>
		<link href="<%=frameroot%>/css/ntoolbar.css" rel="stylesheet" type="text/css" />
		<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
		<SCRIPT language="javascript" src="<%=jsroot%>/menu/personMenu.js"></SCRIPT>	
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>
		<!--[if lt IE 7]>
 		<style type="text/css">
 			.logo_bg img {behavior: url(<%=frameroot%>/images/perspective_toolbar/iepngfix.htc) }
 		</style>
		<![endif]-->
		<style id="popupmanager">
		a:link,a:visited,a:hover,a:active{
			text-decoration:none;
		}
		.logo{
			font-family: "黑体";
			font-size: 32px;
			font-weight: bold;
			color: #FFFFFF;}
			
		.popupMenu {
			width: 100px;
			border: 1px solid #666666;
			background-color: #F9F8F7;
			padding: 1px;
		}
		
		.popupMenuTable { /*
			background-image: url(/images/popup/bg_menu.gif);
			*/
			background-repeat: repeat-y;
		}
		
		.popupMenuTable TD {
			font-family: MS Shell Dlg;
			font-size: 12px;
			cursor: default;
		}
		
		.popupMenuRow {
			height: 21px;
			padding: 1px;
		}
		
		.popupMenuRowHover {
			height: 21px;
			border: 1px solid #0A246A;
			background-color: #B6BDD2;
		}
		
		.popupMenuSep {
			background-color: #A6A6A6;
			height: 1px;
			width: expression(parentElement . offsetWidth-27);
			position: relative;
			left: 28;
		}
		</style>
		<script type="text/javascript">
		$(document).ready(function(){
			var obj=document.getElementById("logos");	//顶部图标对象
			if(obj!=null&&obj!=undefined){
				obj.align="middle";
				var imageWidth=document.getElementById("baseLogoWidth").value; 	//顶部图标高度
				var imageHeight=document.getElementById("baseLogoHeight").value;//顶部图标高度
				if(imageWidth!=""&&imageWidth!=null&&imageWidth!="null"&&imageWidth!=""){
					obj.width=imageWidth;
				}
				if(imageHeight!=""&&imageHeight!=null&&imageHeight!="null"&&imageHeight!=""){
						obj.height=imageHeight;
				}
			}
			$.post("<%=path%>/notify/notify!loginList.action",
		           function(data){
				    	if(data!=""){
				    		var arr=data.split(",");
					    	for (var i=0;i<arr.length;i++){
					    		var aId = arr[i];
					    		try{
						     	//	var rValue= window.open("<%=path%>/notify/notify!viewLogin.action?afficheId="+aId,aId,"width=650,height=500,top=100, left=350,toolbar=no,menubar=no, scrollbars=no, resizable=yes,location=no, status=no");
					    			var rValue= window.showModalDialog("<%=path%>/notify/notify!viewLogin.action?afficheId="+aId,aId,'dialogWidth:750px;dialogHeight:630px;help:no;status:no;scroll:no');
					    		}catch(e){
					    		}
					    	}
			    		}	
			    });	
			    
			    
			var btnCss = {"background-image": "url('"+$("#kjdhcd").attr("icoPath")+"')","background-repeat":"no-repeat"};
			$("#kjdhcd").css(btnCss);
		});	
		
		
		function showCompanyDetail(){
				window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
		}
		function gotologin(){
			window.top.location="<%=path%>/j_spring_security_logout";
		}
		function openLink(url,title,id){
			 top.perspective_content.redirect(url,title,id);
		}
		function portal(portalId,title){	
			//top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>/desktop/desktopWhole.action?defaultType=2&portalId="+portalId,title);
			var url="<%=basePath%>/desktop/desktopWhole.action?defaultType=2&portalId="+portalId;	
	            top.perspective_content.redirect(url,title,portalId);
		}
		
		//function mydesk(){	
		//	top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>/desktop/desktopWhole.action","个人桌面");
		//}
		
		function myemail(){	
			top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>/fileNameRedirectAction.action?toPage=mymail/mail_container.jsp";
		}
		function mymsg(){	
			top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>";
		}
		function myinfo(){	
			top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>personal_office/myinfo/myinfo_index.jsp";
		}
		function notify(){
			top.perspective_content.actions_container.personal_properties_content.location="<%=basePath%>integrated_Office/notify/notify_recvlist.jsp";
		}
		
		function bgtdesk(){	
			openLink("<%=basePath%>/desktop/desktopWhole!gotoBgtDesktop.action","办公门户");
		}
		
		function showStatus(){	//状态栏
			var value=document.getElementById("baseStatusbar").value;
			var yourwords;
			if(value==null||value=="")
				 yourwords ="oa办公系统";
			else 
			 	yourwords=value;
			window.status=yourwords;
			var speed = 1000;
			var control = 1;
			function flash(){
				if (control == 1){
					window.status=yourwords;
					control=0;
				} else{
					window.status=" ";
					control=1;
				}
				setTimeout("flash();",speed);
			}
			flash();
			return true;
		}
		
		function selectCommpass(){//全文检索
			var value=document.getElementById("textfield").value;
			value=ltrim(value);
			if(value=="" || value=="全文检索"){
				alert("请输入检索内容");
				return false;
			}
			//alert(value);
			value = encodeURIComponent(value);
			top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>/search/search!searchContent.action?searchContent="+value,"全文检索");
		}
		
		function changePassword(){
			var rValue= OpenWindow("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", '350', '150', window);
		}
		
		//去掉左边空格
		function ltrim(s){ 
		    return s.replace( /^\s*/,""); 
		} 
		function alertHelpPage(){
			//var sindex = getSysConsole().getSelectedIndex();
			//var surl = getSysConsole().getTabParam("URL",sindex);
			//if(surl==null){
			//	alert("对不起，您目前没有打开的页面。\n请先打开一个页面，再点击帮助，这样才能得到对应的帮助信息。");
		//	}else{
				//去掉surl前的"/oa",因为存在数据库中的链接没有/oa前缀
			//	surl = surl.substring(3);
			//	var surls = new Array();
			//	surls = surl.split("privilSyscode=");
			//	ihelpTreeId = surls[1];
				//alert(ihelpTreeId);
				var ihelpTreeId='';
				//将&替换成and，否则，url无法匹配，&后的值将丢失。
		//		var ihelpTreeId = ihelpTreeId.replace(/&/g, "and");
				var rValue= showModalDialog("<%=basePath%>/helpedit/helpedit!gethelpTreeId.action?ihelpTreeId="+ihelpTreeId, 
				                                window,"dialogWidth:750pt;dialogHeight:450pt;status:no;help:no;scroll:yes;");	
			//}
		}
		
		  var state = 0 ;
		  function hidemenu(){
		  	if(state == 0){
		  	    $("#side2").hide();
				parent.topTitle.rows='30,100%';
				state = 1;
				document.pic.src="<%=frameroot%>/images/jiantou3.jpg";
				document.pic.title="点击打开图片";
			}else if(state == 1){
			    $("#side2").show();
				parent.topTitle.rows='125,*';
				
				state = 0;
				document.pic.src="<%=frameroot%>/images/jiantou1.jpg";
				document.pic.title="点击隐藏图片";
			}
		  }
		  
		</script>
	</head>
	 
	<body class=gtoolbarbodymargin>
		<s:hidden id="baseStatusbar" name="modle.baseStatusbar"></s:hidden>
		<s:hidden id="baseLogoWidth" name="modle.baseLogoWidth"></s:hidden>
		<s:hidden id="baseLogoHeight" name="modle.baseLogoHeight"></s:hidden>
		<s:hidden id="baseStatusbarTime" name="modle.baseStatusbarTime"></s:hidden>
			
		<div id="side2" class="side2">
			<%
				String blueTheme=root+"/frame/theme_blue";
				String blueTheme12=root+"/frame/theme_blue_12";
				String grayTheme=root+"/frame/theme_gray";
				String grayTheme12=root+"/frame/theme_gray_12";
				String greenTheme=root+"/frame/theme_green";
				String greenTheme12=root+"/frame/theme_green_12";
				String redTheme=root+"/frame/theme_red";
				String redTheme12=root+"/frame/theme_red_12";
				if(frameroot!=null&&(frameroot.equals(blueTheme)||frameroot.equals(blueTheme12))){
			%>
				<table width="100%" height="90" style="background-image:url('../oa/image/topPic/testBlue.jpg');" border="0" cellpadding="00" cellspacing="0" class="logo_bg">
			<%}else if(frameroot!=null&&(frameroot.equals(grayTheme)||frameroot.equals(grayTheme12))){%>
				<table width="100%" height="90" style="background-image:url('../oa/image/topPic/testGray.jpg');" border="0" cellpadding="00" cellspacing="0" class="logo_bg">
			<%}else if(frameroot!=null&&(frameroot.equals(greenTheme)||frameroot.equals(greenTheme12))){%>
				<table width="100%" height="90" style="background-image:url('../oa/image/topPic/testGreen.jpg');" border="0" cellpadding="00" cellspacing="0" class="logo_bg">
			<%}else if(frameroot!=null&&(frameroot.equals(redTheme)||frameroot.equals(redTheme12))){%>
				<div style="background:url(../oa/image/topPic/dxhtopbg.gif) repeat-x center bottom;">
				<table width="100%" height="90" style="background-image:url('../oa/image/topPic/testRed.jpg');" border="0" cellpadding="00" cellspacing="0" class="logo_bg">
			<%}%>					
				<tr>
					<td valign="bottom">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
						<tr>
							<td align="right"  width="200" height="37" class="help">
								<img src="<%=path%>/images/ico/setgroup.gif" width="15" height="15" />
								用户：${user.userName}
								&nbsp;
							</td>
							<%--
							<td width="99" align="center" class="help" style="cursor: hand;" onclick="alertHelpPage()">
								<img src="<%=frameroot%>/images/perspective_toolbar/help.gif" width="15" height="15" />
								系统帮助
							</td>
							
							<td width="28" align="center" class="help" style="cursor: hand;">
								<img src="<%=frameroot%>/images/perspective_toolbar/set.gif" width="15" height="15" />
							</td>
							<td width="50" class="help"  style="cursor: hand;">
								测试版
							</td>
							--%>
							<td width="88" align="center" class="help" style="cursor: hand;">
								<img src="<%=frameroot%>/images/perspective_toolbar/about.gif" width="15" height="15" onclick="showCompanyDetail()" />
							<font onclick="changePassword()" style="cursor: hand;">
								修改密码
							</font>	
							</td>
							
							<td width="68" align="center" class="help" style="cursor: hand;">
								<img src="<%=frameroot%>/images/perspective_toolbar/exit.gif" width="15" height="15" onclick="gotologin()" />
							<font onclick="gotologin()" style="cursor: hand;">
								退出
							</font>
							</td>
							<s:if test="linkList!=null&&linkList.size()>0">
							<td width="80">
								<s:select list="linkList" listKey="linkUrl"
									listValue="systemName" id="systemUrl" name="systemLink" headerKey="" headerValue="请选择" onchange="jumpMenu(this.value)"/>
							</td>
							</s:if>
						</tr>
							

						</table>
					</td>
				</tr>
			</table>
	 		</div>
	 </div>
	 
 
	<div class="ltnavbg" style="position:relative;">
	 	 <div style="text-align:center; position:absolute; top:0; width:100%;line-height:1;">
		<a href="#" onClick="hidemenu();"><img style="vertical-align:top;" name=pic src="<%=frameroot%>/images/jiantou1.jpg" border="0"  title="点击隐藏图片"/></a>
		</div>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0"  class="nav">
						<tr>
							<td>
								<table width="400" height="20" border="0" cellpadding="00" cellspacing="0">
									<tr>
										<td width="32">&nbsp;</td>
										<td width="176">
										
											<!--
											<input id="kjdhcd" type="button" style="border: 0;height: 24px;width: 105px;cursor: pointer;"
												 icoPath="<%=path%>/common/images/kjdhsz.gif"  value="" height="24px" 
												 onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置','12345')">
											  -->
											  
						 					<font>欢迎访问本平台！今天是${serverTime}</font>
										</td>
									</tr>
								</table>
							</td>
							<td width="500" valign="bottom">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										
										<s:if test="#request.listPortal1!=null && #request.listPortal1.size()>0">
											<s:iterator id="ls" value="#request.listPortal1" status="ls">
												<td nowrap="nowrap" align="right">
													<s:if test="modle1.baseMenuIcon==\"0\"">
														<s:if test="rest1!=null&&rest1!=\"\"">
															<img src="<%=frameroot%>/images<s:property value="rest1" />" width="15" height="15" />						
														</s:if>
														<s:else>
															<img src="<%=frameroot%>/images/foder/file_folder.gif" width="15" height="15" />	
														</s:else>
													</s:if>
													<s:elseif test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
														<img src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>" width="15" height="15" />
													</s:elseif>										
												</td>
												<td nowrap="nowrap" height="28"> 
													<a href="#" onclick="portal('<s:property value="portalId" />','<s:property value="portalName" />')">
														<s:if test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
															<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px"><s:property value="portalName" /></span>
														</s:if>
														<s:else>
															<s:property value="portalName" />
														</s:else>
													</a>
												</td>
												<td nowrap="nowrap">
													&nbsp;&nbsp;
												</td>
											</s:iterator>
											<td nowrap="nowrap">
													&nbsp;&nbsp;
											</td>	
										</s:if>		
										<s:if test="#request.testList!=null && #request.testList.size()>0">
											<s:iterator id="ls" value="#request.testList" status="ls">
												<td nowrap="nowrap" align="right">
													<s:if test="modle1.baseMenuIcon==\"0\"">						
														<s:if test="rest1!=null&&rest1!=\"\"">
															<img src="<%=frameroot%>/images<s:property value="rest1" />" width="15" height="15" />						
														</s:if>
														<s:else>
															<img src="<%=frameroot%>/images/foder/file_folder.gif" width="15" height="15" />	
														</s:else>
													</s:if>
													<s:elseif test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
														<img src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>" width="15" height="15" />
													</s:elseif>										
												</td>
												<td nowrap="nowrap" height="28"> 
													<a href="#" onclick="top.perspective_content.redirect('<%=root%><s:property value="privilAttribute"/>','<s:property value="privilName" />','<s:property value="privilSyscode" />')">
														<s:if test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
															<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px"><s:property value="privilName" /></span>
														</s:if>
														<s:else>
															<s:property value="privilName" />
														</s:else>
													</a>
												</td>
												<td nowrap="nowrap">
													&nbsp;&nbsp;
												</td>
											</s:iterator>	
											<td width="*%">
												&nbsp;
											</td>
										</s:if>
										<td width="20%" align="left">
											&nbsp;&nbsp;
										<s:if test="#request.testList2.size()>0||#request.listPortal2.size()>0">
											<span id="goright" style="cursor:hand" onclick="showFav();"> <img src="<%=frameroot%>/images/perspective_toolbar/jiantou1.gif" width="13" height="9" /> </span>
										</s:if>
										</td>
										<td></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div id="divFavContent" style="display: none">
			<div class="popupMenu">
			    <s:iterator id="ls" value="#request.listPortal2" status="ls">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%" class="popupMenuTable">
						<tr height="22">
							<td class="popupMenuRow" onmouseover="this.className='popupMenuRowHover';" onmouseout="this.className='popupMenuRow';" id="popupWin_Menu_Setting">
								<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%">
									<tr>
										<td width="5%" align="right">					
												<s:if test="modle1.baseMenuIcon==\"0\"">
													<s:if test="rest1!=null&&rest1!=\"\"">
														<img src="<%=frameroot%>/images<s:property value="rest1" />" width="15" height="15" />						
													</s:if>
													<s:else>
														<img src="<%=frameroot%>/images/foder/file_folder.gif" width="15" height="15" />	
													</s:else>
												</s:if>
												<s:elseif test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">					
													<img src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>" width="15" height="15" />
												</s:elseif>										
										</td>
										<td style="cursor: hand" width="25%">
											<a href="#" onclick="top.perspective_content.redirect('<%=root%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId=<s:property value="portalId" />','<s:property value="portalName" />')">
												<s:if test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
													<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px"><s:property value="portalName" /></span>
												</s:if>
												<s:else>
													<s:property value="portalName" />
												</s:else>	
											</a>
										</td>
										<td width="*%">
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</s:iterator>
				<s:iterator id="ls" value="#request.testList2" status="ls">
					<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%" class="popupMenuTable">
						<tr height="22">
							<td class="popupMenuRow" onmouseover="this.className='popupMenuRowHover';" onmouseout="this.className='popupMenuRow';" id="popupWin_Menu_Setting">
								<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%">
									<tr>
										<td width="5%" align="right">					
												<s:if test="modle1.baseMenuIcon==\"0\"">
													<s:if test="rest1!=null&&rest1!=\"\"">
														<img src="<%=frameroot%>/images<s:property value="rest1" />" width="15" height="15" />						
													</s:if>
													<s:else>
														<img src="<%=frameroot%>/images/foder/file_folder.gif" width="15" height="15" />	
													</s:else>
												</s:if>
												<s:elseif test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
													<img src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>" width="15" height="15" />
												</s:elseif>										
										</td>
										<td style="cursor: hand" width="25%">
											<a href="#" onclick="top.perspective_content.redirect('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />','<s:property value="privilSyscode" />')">
												<s:if test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
													<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px"><s:property value="privilName" /></span>
												</s:if>
												<s:else>
													<s:property value="privilName" />
												</s:else>
											</a>
										</td>
										<td width="*%">
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</s:iterator>
			</div>
		</div>
		<SCRIPT type="text/javascript">
//状态栏显示
var control = 1;
var speed=document.getElementById("baseStatusbarTime").value; //状态栏切换时间
var value=document.getElementById("baseStatusbar").value; //状态栏字体
var yourwords;
if(value==null||value==""||value=="0")
		yourwords ="oa办公系统";
	else 
	 	yourwords=value;
if(!isNaN(speed)){
	setInterval("flashContent()",speed*1000);
}else{
	window.status=yourwords+"        机构:"+"${org.orgName}";;
}

function flashContent(){
	if (control == 1){
		window.status=yourwords+"        机构:"+"${org.orgName}";;
		control=0;
	}else{
		window.status=" "+"        机构:"+"${org.orgName}";;
		control=1;
	}
}	

function jumpMenu(url){
  var xx="<%=path%>/theme/theme!jump.action?url="+url;
 // alert(xx);
  window.parent.location=xx;
 // alert(window.parent.location);
  
	
}
</SCRIPT>
</div>
	</body>
</html>

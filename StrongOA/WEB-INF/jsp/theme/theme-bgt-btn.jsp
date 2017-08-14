<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase" />
<%@taglib prefix="web-menu" uri="/tags/web-menu"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>导航区域</title>
		<SCRIPT language="javascript" src="<%=jsroot%>/menu/personMenu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>

		<%
			String orgName = (String) request.getSession().getAttribute( "orgName");
			ToaSysmanageBase sysTheme = request.getSession().getAttribute( "sysTheme") == null ? null : (ToaSysmanageBase) request
					.getSession().getAttribute("sysTheme");
			int speed = 0;
			String value = "";
			if (sysTheme != null) {
				speed = sysTheme.getBaseStatusbarTime() == null ? 0 : sysTheme
				.getBaseStatusbarTime();
				value = sysTheme.getBaseStatusbar();
			}
			
			 String bangongluntan = (String)request.getAttribute("bangongluntan");
			 String gerenyouxiang = (String)request.getAttribute("gerenyouxiang");
			
			 String userName=(String)request.getAttribute("userName");
			 String password=(String)request.getAttribute("password");
			 
			 
			 String bangongluntanURL = "";
			 if(bangongluntan.equals("")){//办公论坛地址未配置
				 bangongluntanURL = "bangongluntanNOSet";
			 }else{
				 bangongluntanURL = bangongluntan;
			 }
			 
			 String gerenyouxiangURL = "";
			 if(gerenyouxiang.equals("")){//个人邮箱地址未配置
				 gerenyouxiangURL = "gerenyouxiangNOSet";
			 }else{
				 gerenyouxiangURL= gerenyouxiang;
			 }
			
		%>


		<style id="popupmanager">
		.input_bg {
			font-family: "宋体";
			background-image: url(<%=frameroot%>/images/perspective_leftside/input_bg.jpg);
			border: 1px solid #b8b8b8;
		}
		
		a:link,a:visited,a:hover,a:active{
			text-decoration:none;
		}
		.popupMenu {
			width: 100px;
			border: 1px solid #666666;
			background-color: #F9F8F7;
			padding: 1px;
		}
		
		.popupMenuTable { 
			/*
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
		
.nav{ width:100%; background:url(<%=root%>/common/bgtdesktop/resource/images/index/nav01.jpg) repeat-x top; height:32px; text-align:center;}
.navbox{  margin:auto auto; float:left;}
.navbox li span{ font-size:14px;width:114px; height:28px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/kj_off_03.jpg) no-repeat right;text-align:center; line-height:23px; display: block; float:left; margin-right:16px; margin-top:6px;cursor:pointer;}
.navbox li dd{ border-right：0; font-size:14px;width:114px; height:28px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/kj_off.jpg) no-repeat right;text-align:right; line-height:23px; display: block; float:left; margin-right:3px; margin-top:6px;cursor:pointer;}
.nav li{ float:left; width:95px; height:32px; line-height:30px; background:url(resource/images/index/nav02.jpg) top right no-repeat;}
.nav li a{ font-size:14px; color:#015baf; FILTER: dropshadow(color=#ffffff, offx=1, offy=1, positive=1); text-align:center;}
.nav li a:hover{ text-decoration:none; font-weight:bold; FILTER: dropshadow(color=#ffffff, offx=1, offy=1, positive=1);}

	</style>
		<script type="text/javascript">
		function openLink(url,title){
			top.bottomFrame.navigate(url,title);
		}
		
		function portal(portalId,title){	
			top.bottomFrame.navigate("<%=basePath%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId="+portalId,title);
		}
		
		function mydesk(){	
			top.bottomFrame.navigate("<%=basePath%>/desktop/desktopWhole.action","个人桌面");
		}
		
		function bgtdesk(){	
			top.bottomFrame.navigate("<%=basePath%>/desktop/desktopWhole!gotoBgtDesktop.action","办公门户");
		}
		
		function showStatus(){	//状态栏
			var value="<%=value%>";
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
		
		//去掉左边空格
		function ltrim(s){ 
		    return s.replace( /^\s*/,""); 
		} 
		function navigates(url,title) {
			top.bottomFrame.navigate("<%=basePath%>"+url,title);
		}
		
		function gotonavigates(code,title) {
			top.bottomFrame.navigate("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-bgtMenupage.jsp?privCode="+code,title);
		}
		
		function gotoUrl(url){
			document.getElementById(url).submit();
		}
		
		function gotomail(){
			top.bottomFrame.navigate('<%=gerenyouxiangURL%>?username='+$("#mailUserName").val()+'&password='+$("#mailUserPassword").val(),"个人邮箱");
		}
		//弹出“自办文流程表单”    BY：刘皙
		function gotozbw(){
			var rValue= window.open("<%=basePath%>/senddoc/sendDoc!input.action?workflowName=%25E8%2587%25AA%25E5%258A%259E%25E6%2596%2587%25E5%258A%259E%25E7%2590%2586\&formId=9050\&workflowType=3");
		}
		//弹出厅领导的“文件监控与统计”页面    BY：刘皙
		function gotoOrgwjcx(){
			top.bottomFrame.navigate("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-wenJianChaXun.jsp?mytype=org","文件监控与统计");
		}
		//弹出处长“文件监控与统计”页面    BY：刘皙
		function gotoDeptwjcx(){
			top.bottomFrame.navigate("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-wenJianChaXun.jsp?mytype=dept","文件监控与统计");
		}
		//弹出处长“文件监控与统计”页面    BY：刘皙
		function gotoUserwjcx(){
			top.bottomFrame.navigate("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-wenJianChaXun.jsp?mytype=user","文件查询");
		}
		
var state = 0 ;
  function hidemenu(){
  	if(state == 0){
		parent.title.rows='0,42,*';
		state = 1;
		document.pic.src="<%=root%>/images/ico/jiantou3.jpg";
		document.pic.title="点击打开菜单栏";
	}else if(state == 1){
		parent.title.rows='120,42,*';
		state = 0;
		document.pic.src="<%=root%>/images/ico/jiantou1.jpg";
		document.pic.title="点击隐藏菜单栏";
	}
  }
	</script>
	</head>
	
	<body style="background:url(<%=root%>/common/bgtdesktop/resource/images/index/nav01.jpg) repeat-x top; height:32px; text-align:center; margin:0;padding:0;">
		
		<div style="width:100%;">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td  align="center"><a href="#" onclick="hidemenu();"><img name=pic src="<%=root%>/images/ico/jiantou1.jpg" width="56" height="6" border="0"  title="点击隐藏菜单栏"/></a></td>
  </tr>
  </table>
			<table height="30" align="left" border="0" 
				cellpadding="0" cellspacing="0">
				<tr>
					<td align="left">
						<web-menu:strongmenu
							dealclass="com.strongit.tag.web.menu.MenuLeveNode"
							menuTagStyle="common/bgtdesktop/style.css" menus="${menus}"
							data="${menus2}" root="<%=frameroot%>" />
					</td>
					<td align="left">
					<div>
						<ul>
						<li style="float:right;">
						<span onclick="gotoUrl('phpform')" 
							style="color:#fff; width:114px; height:28px;margin: 0px 0px 0px 0px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/bt_09.jpg) no-repeat center; text-align:center; line-height:28px; display: block; float:left; margin-left:6px;  font-weight:bold; cursor:pointer;">
							&nbsp;干部论坛&nbsp;
						</span>
						<span onclick="gotomail('aspform')" class="menu-button"
							style="color:#fff; width:114px; height:28px;margin: 0px 0px 0px 0px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/bt_09.jpg) no-repeat center; text-align:center; line-height:28px; display: block; float:left; margin-left:6px;  font-weight:bold; cursor:pointer;">
							&nbsp;个人邮箱&nbsp;
						</span>
						</li>
						</ul>
					</div>
					</td>
			</tr>
			</table>
			<table height="30" align="right" border="0"  cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
					<div>
						<ul>
						<span class="menu-button" onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置')"
							style="color:#fff; width:114px; height:28px;margin: 0px 0px 0px 0px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/bt_09.jpg) no-repeat center; text-align:center; line-height:28px; display: block; float:left; margin-left:6px;  font-weight:bold; cursor:pointer;">
							&nbsp;菜单设置&nbsp;
						</span>
						<span class="menu-button"  onmouseover="showFav();" onclick="showFav();"
							style="color:#fff; width:114px; height:28px;margin: 0px 0px 0px 0px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/bt_09.jpg) no-repeat center; text-align:center; line-height:28px; display: block; float:left; margin-left:6px;  font-weight:bold; cursor:pointer;">
							&nbsp;常用菜单&nbsp;
						</span>
						<%--<span class="menu-button" onclick="gotologin();"
							style="color:#fff; width:114px; height:28px;margin: 6px 0px 0px 0px; background:url(<%=root%>/common/bgtdesktop/resource/images/index/bt_09.jpg) no-repeat center; text-align:center; line-height:28px; display: block; float:left; margin-left:6px;  font-weight:bold; cursor:pointer;">
							&nbsp;退出系统&nbsp;
						</span>
						--%>
						</ul>
					</div>
					</td>
					<td style="display:none">
					<form action="<%=bangongluntanURL%>" method="post" target="_blank" id="phpform">
					<input type="hidden" name="username" value="<%=userName%>" >
					<input type="hidden" name="password" value="<%=password%>" >
					</form>
					</td>
					<td style="display:none">
					<form action="<%=gerenyouxiangURL%>" method="post" target="_blank" id="aspform">
					<input type="hidden" id="mailUserName" name="username" value="<%=userName%>" >
					<input type="hidden" id="mailUserPassword" name="password" value="<%=password%>" >
					</form>
					</td>
				</tr>
			</table>
		</div>
					<%--
		<div class="nav">
			<div class="navbox" >
				<ul>
					<li style="font-size:12px">
						您有
						<font color="#FF0000">2</font>封新邮件，
						<font color="#FF0000">3</font>个待办文件，
						<font color="#FF0000">4</font>份新收文，
						<font color="#FF0000">8</font>条新消息
						<span onclick="">帮助</span>
						<span onclick="changePassword()">修改密码</span>
						<span onclick="gotologin()">退出</span>
						<span onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置')">常用菜单设置</span>
					<input type="button" class="input_bg" value="菜单设置" onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置')">
					</li>
						<dd onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置')">菜单设置</dd>
					<li style="width:350px; float:right;">
					<s:if test="#request.testList2.size()>0||#request.listPortal2.size()>0">
						<span id="goright" style="cursor:hand;" onmouseover="showFav();"> 
							 常用菜单
						</span>
					</s:if>
					</li>
				</ul>
			</div>
					--%>
					
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
														<img src="<%=frameroot%>/images<s:property value="rest1" />" width="16" height="16" />						
													</s:if>
													<s:else>
														<img src="<%=frameroot%>/images/foder/file_folder.gif" width="16" height="16" />	
													</s:else>
												</s:if>
												<s:elseif test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">					
													<img src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>" width="16" height="16" />
												</s:elseif>										
										</td>
										<td style="cursor: hand" width="25%">
											<a href="#" onclick="top.bottomFrame.navigate('<%=root%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId=<s:property value="portalId" />','<s:property value="portalName" />')">
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
									<tr style="line-height:30px;">
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
										<td style="cursor: hand" width="35%">
											<a href="#" onclick="top.bottomFrame.navigate('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />')">
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
		function showCompanyDetail(){
			window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
		}
		function changePassword(){
			var rValue= OpenWindow("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", '350', '150', window);
		}

		function gotologin(){
			window.top.location="<%=path%>/j_spring_security_logout";
		}

		function jumpMenu(url){
		  var xx="<%=path%>/theme/theme!jump.action?url="+url;
		  window.parent.location=xx;
		}
			
			
		//状态栏显示
		var control = 1;
		var speed="<%=speed%>"; //状态栏切换时间
		var value="<%=value%>"; //状态栏字体
		var yourwords;
		if(value==null||value==""||value=="0"||value=="null")
				yourwords ="oa办公系统";
			else 
				yourwords=value;
		if(!isNaN(speed)){
			setInterval("flashContent()",speed*1000);
		}else{
			window.status=yourwords+"        机构:<%=orgName%>";
		}
		
		function flashContent(){
			if (control == 1){
				window.status=yourwords+"        机构:<%=orgName%>";
				control=0;
			}else{
				window.status=" "+"        机构:<%=orgName%>";
				control=1;
			}
		}
	</SCRIPT>
	</body>
</html>

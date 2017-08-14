<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公共top区</title>
		<link href="<%=frameroot%>/css/toolbar.css" rel="stylesheet" type="text/css" />
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
});	

function showCompanyDetail(){
		window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
}
function gotologin(){
	window.top.location="<%=path%>/j_spring_security_logout";
}
function openLink(url,title){
	top.perspective_content.actions_container.personal_properties_toolbar.navigate(url,title);
}
function portal(portalId,title){	
	top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId="+portalId,title);
}

function mydesk(){	
	top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=basePath%>/desktop/desktopWhole.action","个人桌面");
}

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
</script>
	</head>
	 
	<body class=gtoolbarbodymargin>
			<s:hidden id="baseStatusbar" name="modle.baseStatusbar"></s:hidden>
			<s:hidden id="baseLogoWidth" name="modle.baseLogoWidth"></s:hidden>
			<s:hidden id="baseLogoHeight" name="modle.baseLogoHeight"></s:hidden>
			<s:hidden id="baseStatusbarTime" name="modle.baseStatusbarTime"></s:hidden>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td valign="top" class="btbg">
					<table width="100%" height="53" border="0" cellpadding="00" cellspacing="0" class="logo_bg">
						<tr>
							<td width="50" align="right">
								<s:if test="modle.baseLogoPic!=\"0\"&&modle.baseLogoPic!=\"\"&&modle.baseLogoPic!=null&&modle.baseLogoPic!=\"null\"">
        							<img id="logos" src="<%=frameroot%>/images/perspective_toolbar/<s:property value='modle.baseLogoPic'/>" width="36" height="39" />
								</s:if>
							</td>
							<td id="title" class="logo">
								<s:if test="modle.baseTitle==null">
        							思创数码科技股份有限公司协同办公软件
        						</s:if>
        						<s:elseif test="modle.baseTitle!=null&&modle.baseTitle.indexOf('.')!=-1">					
        							<img src="<%=frameroot%>/images/<s:property value='modle.baseTitle' />" width="264" height="39" align="middle"/>
        						</s:elseif>
								<s:else>
									<s:property value="modle.baseTitle" />
								</s:else>
							</td>
							<%
								String grayTheme=root+"/frame/theme_gray";
								if(frameroot!=null&&frameroot.equals(grayTheme)){
							 %>
							 	<td width="470" valign="middle" class="top_help_bg">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="45" height="37" class="help">
											用户：
										</td>
										<td width="80" class="help">
											${r"${user.userName}"}
										</td>
							<%
								}else{
							 %>
							 	<td width="550" valign="middle" class="top_help_bg">
							 	<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="50" height="37" class="help">
											用户：
										</td>
										<td width="100" class="help">
											${r"${user.userName}"}
										</td>
							 <%
							 }
							  %>
										<td width="29" align="center" class="help">
											<img src="<%=frameroot%>/images/perspective_toolbar/help.gif" width="14" height="14" />
										</td>
										<td width="42" class="help">
											<a href="<%=path %>/common/helpfiles/simpleHelp.doc">帮助</a>
										</td>
										<%--
										<td width="28" align="center" class="help" style="cursor: hand;">
											<img src="<%=frameroot%>/images/perspective_toolbar/about.gif" width="14" height="14" onclick="showCompanyDetail()" />
										</td>
										<td width="39" class="help" onclick="showCompanyDetail()" style="cursor: hand;">
											关于
										</td>
										--%>
										<td width="28" align="center" class="help" style="cursor: hand;">
											<img src="<%=frameroot%>/images/perspective_toolbar/about.gif" width="14" height="14" onclick="showCompanyDetail()" />
										</td>
										<td width="70" class="help" onclick="changePassword()" style="cursor: hand;">
											修改密码
										</td>
										<td width="28" align="center" class="help" style="cursor: hand;">
											<img src="<%=frameroot%>/images/perspective_toolbar/exit.gif" width="14" height="14" onclick="gotologin()" />
										</td>
										<td width="50" class="help" onclick="gotologin()" style="cursor: hand;">
											退出
										</td>
										<td width="80">
										<s:if test="linkList!=null&&linkList.size()>0">
											<s:select list="linkList" listKey="linkUrl"
												listValue="systemName" id="systemUrl" name="systemLink" headerKey="" headerValue="请选择" onchange="jumpMenu(this.value)"/>
										</s:if>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<%
			String pathstr=frameroot.substring(frameroot.lastIndexOf("/"));
			if(pathstr!=null&&pathstr.equals("/theme_red")){
			}else{
		 %>
		 <table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td bgcolor="#aca99a" height="1"></td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF" height="1"></td>
			</tr>
		</table>
		<%	
			}
	 	%>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#d4d0c8">
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="00" cellspacing="0" bgcolor="#d4d0c8" class="nav">
						<tr>
							<%--<td width="238">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="28" align="center">
											<img src="<%=frameroot%>/images/perspective_toolbar/tz.gif" width="16"
												height="16" />
										</td>
										<td width="60">
											通知通告
										</td>
										<td width="125">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							--%><td>
								<table width="333" height="20" border="0" cellpadding="00" cellspacing="0">
									<tr>
										<td width="32">&nbsp;</td>
										<td width="176">
											<input type="text" id="textfield"  name="textfield"  value="全文检索" onfocus="if (value =='全文检索'){value =''}" onblur="if (value ==''){value='全文检索'}"  onblur="if (value ==''){value='全文检索'}"  onkeypress="if(event.keyCode==13) selectCommpass();" class="keyword" style="width:100%">
										</td>
										<td width="10"><img src="<%=frameroot%>/images/perspective_toolbar/sousuo.gif" width="15" height="18" onclick="selectCommpass()" style="cursor: hand;"  title="单击搜索"></td>
										<td width="150" align="right">
											<input type="button" class="input_bg" value="快捷导航设置" onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置')">
										</td>
									</tr>
								</table>
							</td>
							<td width="500" valign="bottom">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<%--<td nowrap="nowrap" align="right">
											<s:if test="modle1.baseMenuIcon==\"0\"">
												<s:if test="rest1!=null&&rest1!=\"\"">
													<img src="<%=frameroot%>/images<s:property value="rest1" />" width="16" height="16" />						
												</s:if>
												<s:else>
												<!-- 	<img src="<%=frameroot%>/images/foder/file_folder.gif" width="16" height="16" />	-->
												</s:else>
											</s:if>
											<s:elseif test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
												<img src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>" width="16" height="16" />
											</s:elseif>						
										</td>
										<!--  
										<td nowrap="nowrap" height="28"> 
											<a href="#" onclick="mydesk()">
											<s:if test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
												<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px">个人桌面</span>
											</s:if>
											<s:else>
												个人桌面
											</s:else>
											</a>
										</td>
										-->
										<td nowrap="nowrap">
												&nbsp;&nbsp;
										</td>--%>
										<s:if test="#request.listPortal1!=null && #request.listPortal1.size()>0">
											<s:iterator id="ls" value="#request.listPortal1" status="ls">
												<td nowrap="nowrap" align="right">
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
												<td nowrap="nowrap" height="28"> 
													<a href="#" onclick="openLink('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />')">
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
						<tr>
							<%
							if(pathstr!=null&&pathstr.equals("/theme_red")){
								
							 %>
							<td height="1" colspan="3" bgcolor="#DBDBDB"></td>
							<%
							}else{
							 %>
							
							<td height="1" colspan="3" bgcolor="#aca99a"></td>
							<%
							} %>
						</tr>
						<tr>
							<td height="1" colspan="3" bgcolor="#FFFFFF"></td>
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
											<a href="#" onclick="top.perspective_content.actions_container.personal_properties_toolbar.navigate('<%=root%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId=<s:property value="portalId" />','<s:property value="portalName" />')">
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
											<a href="#" onclick="top.perspective_content.actions_container.personal_properties_toolbar.navigate('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />')">
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
	window.status=yourwords+"        机构:"+"${r"${org.orgName}"}";;
}

function flashContent(){
	if (control == 1){
		window.status=yourwords+"        机构:"+"${r"${org.orgName}"}";;
		control=0;
	}else{
		window.status=" "+"        机构:"+"${r"${org.orgName}"}";;
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
	</body>
</html>

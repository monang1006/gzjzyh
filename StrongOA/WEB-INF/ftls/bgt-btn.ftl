<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<%@taglib prefix="web-menu" uri="/tags/web-menu"%>
<%@include file="/common/include/${jsppath}.jsp"%>
<html>
  <head>
  	<title>导航区域</title>
	<link href="<%=root %>/common/bgtdesktop/css.css" rel="stylesheet" type="text/css" />
	<SCRIPT language="javascript" src="<%=jsroot%>/menu/personMenu.js"></SCRIPT>	

<%
		String orgName=(String)request.getSession().getAttribute("orgName");
		ToaSysmanageBase sysTheme = request.getSession()
			.getAttribute("sysTheme")==null?null:(ToaSysmanageBase)request.getSession()
					.getAttribute("sysTheme");
		int speed=0;
		String value="";
		if(sysTheme!=null){
			speed=sysTheme.getBaseStatusbarTime()==null?0:sysTheme.getBaseStatusbarTime();
			value=sysTheme.getBaseStatusbar();
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
	</script>
  </head>
  <body style="background:url(<%=root %>/common/bgtdesktop/resource/images/index/nav01.jpg) repeat-x top; height:32px; text-align:center;">
  <div>
  	<table width="25%" height="30" align="left" border="0" cellpadding="00" style="background:url(<%=root %>/common/bgtdesktop/resource/images/index/nav02.jpg) top right no-repeat;"
					cellspacing="0">
					<tr>
						<td  align="left">
							<web-menu:strongmenu dealclass="com.strongit.tag.web.menu.MenuNode" menuTagStyle="/common/bgtdesktop/style.css"
								 menus="${r"${menus}"}" data="${r"${menus2}"}" root="<%=frameroot%>"/>
						</td>
					</tr>
				</table>
  </div>


<div class="nav" align="left">
	
 <ul>
	
 	<li>
 	<table>
 		<tr>
 		<td nowrap="nowrap" align="right">
									<s:if test="modle1.baseMenuIcon==\"0\"">
										<s:if test="rest1!=null&&rest1!=\"\"">
											<img src="<%=frameroot%>/images<s:property value="rest1" />"
												width="16" height="16" />
										</s:if>
										<s:else>
											<img src="<%=frameroot%>/images/foder/file_folder.gif"
												width="16" height="16" />
										</s:else>
									</s:if>
									<s:elseif
										test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
										<img
											src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>"
											width="16" height="16" />
									</s:elseif>
								</td>
								<td nowrap="nowrap" height="28">
									<a href="#" style="color: #015baf"
										onclick="bgtdesk()">
										<s:if
											test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
											<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px">
												&nbsp;&nbsp;&nbsp;&nbsp;
												办公门户
												&nbsp;&nbsp;&nbsp;&nbsp;
											</span>
											</s:if>
											<s:else>
												&nbsp;&nbsp;&nbsp;&nbsp;办公门户&nbsp;&nbsp;&nbsp;&nbsp;
											</s:else>
											</a>
										</td>
										<td nowrap="nowrap">
												&nbsp;
						</td>
 		</tr>
 	</table>
 	</li>
 	
 			<s:if test="#request.listPortal1!=null && #request.listPortal1.size()>0">
				<s:iterator id="ls" value="#request.listPortal1" status="ls">
				<li>
			 	<table>
			 		<tr>
					<td nowrap="nowrap" align="right">
						<s:if test="modle1.baseMenuIcon==\"0\"">
							<s:if test="rest1!=null&&rest1!=\"\"">
								<img src="<%=frameroot%>/images<s:property value="rest1" />"
									width="16" height="16" />
							</s:if>
							<s:else>
								<img src="<%=frameroot%>/images/foder/file_folder.gif"
									width="16" height="16" />
							</s:else>
						</s:if>
						<s:elseif
							test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
							<img
								src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>"
								width="16" height="16" />
						</s:elseif>
					</td>
					<td nowrap="nowrap" height="28">
						<a href="#" style="color: #015baf"
							onclick="portal('<s:property value="portalId" />','<s:property value="portalName" />')">
							<s:if
								test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
								<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<s:property value="portalName" />
									&nbsp;&nbsp;&nbsp;&nbsp;
								</span>
							</s:if> 
							<s:else>
								<s:property value="portalName" />
							</s:else>
						</a>
					</td>
					<td nowrap="nowrap">
						&nbsp;
					</td>
					</tr>
			 	</table>
			 	</li>
				</s:iterator>
			</s:if>
			<s:if test="#request.testList!=null && #request.testList.size()>0">
				<s:iterator id="ls" value="#request.testList" status="ls">
				<li>
			 	<table>
			 		<tr>
					<td nowrap="nowrap" align="right">
						<s:if test="modle1.baseMenuIcon==\"0\"">
							<s:if test="rest1!=null&&rest1!=\"\"">
								<img src="<%=frameroot%>/images<s:property value="rest1" />"
									width="16" height="16" />
							</s:if>
							<s:else>
								<img src="<%=frameroot%>/images/foder/file_folder.gif"
									width="16" height="16" />
							</s:else>
						</s:if>
						<s:elseif
							test="modle1.baseMenuIcon!=null&&modle1.baseMenuIcon!=\"1\"">
							<img
								src="<%=frameroot%>/images/<s:property value="modle1.baseMenuIcon"/>"
								width="16" height="16" />
						</s:elseif>
					</td>
					<td nowrap="nowrap" height="28">
						<a href="#" style="color: #015baf"
							onclick="openLink('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />')">
							<s:if
								test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
								<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<s:property value="privilName" />
									&nbsp;&nbsp;&nbsp;&nbsp;
								</span>
							</s:if> 
							<s:else>
								<s:property value="privilName" />
							</s:else>
						</a>
					</td>
					<td nowrap="nowrap">
						&nbsp;&nbsp;
					</td>
					</tr>
			 	</table>
			 	</li>
				</s:iterator>
				<td width="*%">
					&nbsp;
				</td>
			</s:if>
			
	<li style="text-align:left">
		<s:if test="#request.testList2.size()>0||#request.listPortal2.size()>0">
			<span id="goright" style="cursor:hand;" onclick="showFav();">
				&nbsp;
				<img src="<%=path%>/frame/theme_blue/images/perspective_toolbar/jiantou1.gif"
					width="13" height="9" /> 
				&nbsp;
			</span>
		</s:if>
 	</li>
		 	
	<li>
		<table width="100%" height="15" border="0" cellpadding="00"
			cellspacing="0">
			<tr>
				<td align="left">&nbsp;&nbsp;
					<input type="button" class="input_bg" value="导航设置"
						onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','导航设置')">&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	</li>
	
 </ul>
</div>
	
	
	<%--快捷菜单  更多按钮内容页面 --%>
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
										<a href="#" onclick="window.top.bottomFrame.navigate('<%=root%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId=<s:property value="portalId" />','<s:property value="portalName" />')">
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
										<a href="#" onclick="window.top.bottomFrame.navigate('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />')">
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
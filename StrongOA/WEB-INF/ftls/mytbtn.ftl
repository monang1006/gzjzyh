<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<%@taglib prefix="web-menu" uri="/tags/web-menu"%>
<%@include file="/common/include/${jsppath}.jsp"%>
<html>
  <head>
  	<title>导航区域</title>
  	<link href="<%=root%>/common/temp/css.css" rel="stylesheet"
			type="text/css" />
	<link href="<%=frameroot%>/css/toolbar.css" rel="stylesheet" type="text/css" />
	<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
	<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
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
	<SCRIPT language="javascript" src="<%=jsroot%>/menu/personMenu.js"></SCRIPT>	
	<style id="popupmanager">
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
			windowWork.navigate(url,title);
		}
		
		function portal(portalId,title){	
			windowWork.navigate("<%=basePath%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId="+portalId,title);
		}
		
		function mydesk(){	
			windowWork.navigate("<%=basePath%>/desktop/desktopWhole.action","个人桌面");
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
  <body>
	<table width="100%" height="100%">
		<tr>
			<td width="40%" valign="bottom">
				<table width="100%" height="20" align="left" border="0" cellpadding="00"
					cellspacing="0">
					<tr>
						<td  align="left">
							<web-menu:strongmenu dealclass="com.strongit.tag.web.menu.MenuNode" menus="${r"${menus}"}" data="${r"${menus2}"}" root="<%=frameroot%>"/>
						</td>
					</tr>
				</table>
			</td>
			<td width="20%" valign="bottom" align="right">
				<table width="100%" height="20" border="0" cellpadding="00"
					cellspacing="0">
					<tr>
						<td align="right">
							<input type="button" class="input_bg" value="快捷导航设置"
								onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','快捷导航设置')">&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td width="40%" align="right"> 
				<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" >
					<tr valign="bottom">
						<%--<td nowrap="nowrap" align="right">
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
							<a href="#" onclick="mydesk()" style="color: #FFFFFF">
								 <s:if
									test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
									<span style="font-size:<s:property value="systemset.fastMenuFontSize"/>px">个人桌面</span>
								</s:if> 
								<s:else>
									个人桌面
								</s:else> 
							</a>
						</td>
						<td nowrap="nowrap">
							&nbsp;&nbsp;
						</td>--%>
						<s:if
							test="#request.listPortal1!=null && #request.listPortal1.size()>0">
							<s:iterator id="ls" value="#request.listPortal1" status="ls">
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
									<a href="#" style="color: #FFFFFF"
										onclick="portal('<s:property value="portalId" />','<s:property value="portalName" />')">
										<s:if
											test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
											<span
												style="font-size:<s:property value="systemset.fastMenuFontSize"/>px"><s:property
													value="portalName" />
											</span>
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
						<s:if
							test="#request.testList!=null && #request.testList.size()>0">
							<s:iterator id="ls" value="#request.testList" status="ls">
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
									<a href="#" style="color: #FFFFFF"
										onclick="openLink('<%=root%><s:property value="privilAttribute" />','<s:property value="privilName" />')">
										<s:if
											test="systemset!=null&&systemset.fastMenuFontSize!=null&&systemset.fastMenuFontSize!=\"\"">
											<span
												style="font-size:<s:property value="systemset.fastMenuFontSize"/>px"><s:property
													value="privilName" />
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
							</s:iterator>
							<td width="*%">
								&nbsp;
							</td>
						</s:if>
						<td width="20%" align="left">
							&nbsp;&nbsp;
							<s:if
								test="#request.testList2.size()>0||#request.listPortal2.size()>0">
								<span id="goright" style="cursor:hand" onclick="showFav();">
									<img
										src="<%=frameroot%>/images/perspective_toolbar/jiantou1.gif"
										width="13" height="9" /> </span>
							</s:if>
						</td>
						<td></td>
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
﻿<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/newrootPath.jsp"%>
<html>
	<HEAD>
		<TITLE>一级菜单</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link 	type=text/css rel=stylesheet href="<%=frameroot%>/css/left_toolbar.css" type=text/css rel=stylesheet>
		<link  	type=text/css rel=stylesheet href="<%=frameroot%>/css/stly.css">
		<!--扩展菜单弹出脚本-->
		<script language="javascript" src="<%=jsroot%>/menu/personMenu.js"></SCRIPT>
		<!--扩展菜单弹出样式-->
		<style id="popupmanager">
.popupMenu {
	width: 100px;
	border: 1px solid #666666;
	background-color: #F9F8F7;
	padding: 1px;
}

.popupMenuTable { /*
	background-image: url(<%=path%>/common/frame/images/popup/bg_menu.gif);
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
	width: expression(parentElement .   offsetWidth-27);
	position: relative;
	left: 28;
}

</style>
	<BODY class=leftsidetoolbarbodymargin style="margin-top: 5">
	<s:form action="" id="mytable" theme="simple">
	<s:hidden id="priviparent" name="priviparent"></s:hidden>
	<s:hidden id="baseDefaultStartMenu" name="modle1.baseDefaultStartMenu"></s:hidden>
		<div id="navPlane" >
			<div id="ipt">
				<ul>
					<s:iterator id="" value="privilList" status="ls">
						<s:if test="#ls.count<=6">
						<li id="nav_<s:property value="privilSyscode"/>" align="center" class="bt_bg">
							<a id="href_<s:property value="privilSyscode"/>" href="#" class="cd"
								onclick="showMenu('nav_<s:property value="privilSyscode"/>');">
								<s:if test="systemset!=null&&systemset.manaMenuFontSize!=null&&systemset.manaMenuFontSize!=\"\"">
									<span><s:property value="privilName"/></span>
								</s:if>
								<s:else>
									<s:property value="privilName"/>
								</s:else>
								<%--<s:property value="privilName"/>--%>
							</a>
						</li>
						</s:if>
					</s:iterator>	
				</ul>
			</div>
			<div id="scrolldown" align="center">
			<s:if test="privilList!=null && privilList.size()>6">
				<span id="goright" style="cursor:hand"  onclick="showFav();">
					<img src="<%=frameroot%>/images/perspective_toolbar/jiantou3.gif" width="9" height="13">
				</span>
			</s:if>
			</div>
		</div>
		</s:form>
	</BODY>
	
	<div id="divFavContent" style="display: none">
		<div class="popupMenu">
		<s:iterator id="" value="privilList" status="ls">
		<s:if test="#ls.count>6">
			<table cellspacing="0" cellpadding="0" border="0" width="100%"
				height="100%" class="popupMenuTable">
				<tr height="22">
					<td class="popupMenuRow"
						onmouseover="this.className='popupMenuRowHover';"
						onmouseout="this.className='popupMenuRow';"
						id="popupWin_Menu_Setting">
						<table cellspacing="0" cellpadding="0" border="0" width="100%"
							height="100%">
							<tr>
								<td width="28">
									&nbsp;
								</td>
								<td id="<s:property value="privilSyscode"/>" class="cd2" menuhref="showMenu('nav_<s:property value="privilSyscode"/>');"
									style="cursor: hand">
									<%--<s:if test="systemset!=null&&systemset.manaMenuFontSize!=null&&systemset.manaMenuFontSize!=\"\"">
										<span style="font-size:<s:property value="systemset.manaMenuFontSize"/>px"><s:property value="privilName"/></span>
									</s:if>
									<s:else>
										<s:property value="privilName"/>
									</s:else>--%>
									<s:property value="privilName"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</s:if>
		</s:iterator>
		</div>
	</div>
	<script>
<%--$(document).ready(function(){--%>
	//当前选择的导航
	var select_nav="";
	var nav_ids="nav_"+document.getElementById("priviparent").value;
	//var nav_ids=document.getElementById("baseDefaultStartMenu").value;
	selectShow(nav_ids) ;
<%--});--%>
//展现二级以下菜单
   
<%--    setTimeout("setvalue()",10);--%>
<%--    	--%>
<%--    function setvalue(){--%>
<%-- 		if(nav_ids!="")--%>
<%--    	selectShow(nav_ids);--%>
<%--    }--%>

    function selectShow(nav_ids){
	    	//点击状态变化
	   if (nav_ids != select_nav) {
	        var navArr = document.getElementsByTagName("li");
	        var navArrHref = document.getElementsByTagName("a");
	        for (var i=0; i<navArr.length; i++) {
	        	if (navArr[i].id == nav_ids){
	        		navArr[i].className = "bt_bg";
	        		navArrHref[i].className="cd";
	        	} else {
	        		navArr[i].className ="bt_bg2";
	        		navArrHref[i].className="cd2";
	        	}
	        }
	    }
          //当前导航赋值
        select_nav = nav_ids;
    }
    
    function showMenu(nav_id) {
    	//未选中的导航按钮进行变化
    	if (nav_id != select_nav) {
	    	//点击状态变化
	        var navArr = document.getElementsByTagName("li");
	        var navArrHref = document.getElementsByTagName("a");
	        for (var i=0; i<navArr.length; i++) {
	        	if (navArr[i].id == nav_id){
	        		navArr[i].className = "bt_bg";
	        		navArrHref[i].className="cd";
	        	} else {
	        		navArr[i].className ="bt_bg2";
	        		navArrHref[i].className="cd2";
	        	}
	        }
        }
        
        //功能导航栏更新
        if (nav_id != select_nav) {
        	var parentid = nav_id.substring(4, nav_id.length);
        	if(windowMenu!=undefined){
        		var pageModelId=windowMenu.getPageModelId();
        		//parent.perspective_content.navigator_container.navigator_content.location = "<%=path%>/fileNameRedirectAction.action?toPage=frame/perspective_content/navigator_container/navigator_content.jsp?parentid="+parentid;
        		windowMenu.location = "<%=path%>/theme/theme!RefreshPrivilist.action?priviparent="+parentid+"&pageModelId="+pageModelId;
		}
        }
        
        //当前导航赋值
        select_nav = nav_id;
    }  
</script>
</HTML>

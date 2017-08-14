<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<HEAD>
		<TITLE>一级菜单</TITLE>
		<LINK href="<%=path %>/common/frame/css/left_toolbar.css" type=text/css rel=stylesheet>
		<LINK href="css/stly.css" type=text/css rel=stylesheet>
		<!--扩展菜单弹出样式-->
		<style id="popupmanager">
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
	width: expression(parentElement .   offsetWidth-27);
	position: relative;
	left: 28;
}

</style>
		<!--扩展菜单弹出脚本-->
		<SCRIPT language="javascript"
			src="<%=path%>/common/frame/js/personMenu.js"></SCRIPT>
		<script>
	//当前选择的导航
	var select_nav="nav_111";
	var defalutMenu="nav_111";
//展现二级以下菜单
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
        	parent.perspective_content.navigator_container.navigator_content.location = "perspective_content/navigator_container/navigator_content.jsp?parentid="+parentid;
        }
        
        //当前导航赋值
        select_nav = nav_id;
    }  
    function getDaohang(){
    	showMenu(defalutMenu);
    }  
</script>
	<BODY class=leftsidetoolbarbodymargin onload="getDaohang()">
		<div id="navPlane" >
			<div id="ipt">
				<ul>	
					<li id="nav_111" align="center" class="bt_bg">
						<a id="href_111" href="#" class="cd"
							onclick="showMenu('nav_111');">领导办公</a>
					</li>
					<li id="nav_112" align="center" class="bt_bg2">
						<a id="href_112" href="#" class="cd2"
							onclick="showMenu('nav_112');">个人办公</a>
					</li>
					<li id="nav_113" align="center" class="bt_bg2">
						<a id="href_113" href="#" class="cd2"
							onclick="showMenu('nav_113');">综合办公</a>
					</li>
					<li id="nav_114" align="center" class="bt_bg2">
						<a id="href_114" href="#" class="cd2"
							onclick="showMenu('nav_114');">行政办公</a>
					</li>
					<li id="nav_115" align="center" class="bt_bg2">
						<a id="href_115" href="#" class="cd2"
							onclick="showMenu('nav_115');">系统管理</a>
					</li>
				</ul>
			</div>
			<div id="scrolldown" align="center">
			<span id="goright" style="cursor:hand"  onclick="showFav();">
				<img src="images/perspective_toolbar/jiantou3.gif" width="9" height="13">
			</span>
			</div>
		</div>
	</BODY>
	<div id="divFavContent" style="display: none">
		<div class="popupMenu">
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
								<td class="cd2" onclick="alert('请在onclik事件中添加链接');"
									style="cursor: hand">
									个性设置
								</td>
							</tr>
						</table>
					</td>
				</tr>

			</table>
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
								<td class="cd2" onclick="alert('请在onclik事件中添加链接');"
									style="cursor: hand">
									个性设置
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
</HTML>

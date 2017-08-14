<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<TITLE>思创数码科技股份有限公司协同办公软件</TITLE>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet"type="text/css" />
		<script type="text/javascript" src="<%=scriptPath%>/jquery.js"></script>
        <script type="text/javascript" src="<%=scriptPath%>/jquery.easing.js"></script>
        <script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
<style type="text/css">
	#side1 {float:right;}
	#side1_1 {float:left;}
	#side1_2 {float:right;}
	#side1_2 ul {list-style:none;margin:0px;float:right;}
	#side1_2 ul li {margin:0px 1px;float:left; }
	#side1_3 {float:right;}
	.interval {width:1px; color:#333; }
	
	#divFloatBg{width:63px;height:70px;position:absolute;left:3px;top:1px;background:url(<%=themePath%>/image/daohang/slideItembg.png) no-repeat;cursor:pointer;}  
	
	.topMenuDiv { width:440px;height:65px;overflow:hidden;  
	   margin:0 -210px; position: absolute; top:0px;z-index:99999;left:50%;
	}
	.topMenuDiv_top {position:relative;width:410px;top:0;overflow:hidden;height:205;float:left;
		left:15px;
	}
	.menuItem{width:45px;height:65px;background:none;float:left;position:relative;margin-right:10px;cursor:hand;}
	.menuItemIcon{WIDTH:32px;height:32px;margin-left:15px;margin-top:6px;cursor:pointer;background:url(<%=themePath%>/image/daohang/menutopicons.png) no-repeat;}
    .slideItemText{padding-left:10px;padding-right:3px;text-align:center;font-family:"微软雅黑","verdana";font-size:11px;color:#000;margin-top:2px;font-weight:bold;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;width:45px;}
	.menuItemEnd{clear:left}
	.menuNavSpan_Expand {background:url(<%=themePath%>/image/daohang/menutopicons.png) -287px -0 no-repeat;}
	.menuNavSpan_Contraction {background:url(<%=themePath%>/image/daohang/menutopicons.png) -259px 0 no-repeat;}
	
	html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:0px 0px 77px 0px; overflow:hidden;}
    html,body {height:100%;}
    
    .main { position:relative; height:100%; overflow:hidden;top:25px}
	.main iframe { height:100%; width:100%; background:#fff; position:absolute; left:0; top:0;}
	
</style>
<script type="text/javascript">
//导航按钮层数
var MENU_MAX = 2
//顶部菜单最大高度
var TOP_MENU_MAX_HEIGHT = 71+66*MENU_MAX;  

function changeSystem(system){
	if(system.value != null && system.value != ""){
		window.top.location = system.value;
	}
}
	
$(document).ready(function(){
      //导航块移动
    $(".menuItem").hover(function() {
    	topMenuNavMove(this);
    }, function(){});
    //菜单展开或者收缩
    $("#topMenuContr").bind("click", function() {
    	if ($(".topMenuDiv").height() > 70) {
    		$(this).removeClass("menuNavSpan_Contraction");
    		$(this).addClass("menuNavSpan_Expand");
    		topMenuContraction();
    	} else {
    		$(this).removeClass("menuNavSpan_Expand");
    		$(this).addClass("menuNavSpan_Contraction");
	        topMenuExpand();
	    }
    });
    
    //光标不在菜单区域时，如果菜单时展开的则收缩。
    $(".topMenuDiv").hover(function() {}, function() {
    	if ($(".topMenuDiv").height() > 70) {
	    	topMenuContraction();
	    }
    });

     $("#morebtn").click(function(e) {
    	//alert("screen.availWidth="+screen.availWidth+";document.body.clientWidth="+document.body.clientWidth);
		oPopup.show(document.body.clientWidth-150, 76, width, height, document.body); 
     });
     
     $("#morebtn").mouseover(function () { $("#morebtn img").attr("src","<%=themePath%>/image/pic_down.gif"); } );
	 $("#morebtn").mouseout(function () { $("#morebtn img").attr("src","<%=themePath%>/image/icon_link.gif"); } );
	 
	 
	 $("#help").mouseover(function () { $("#help img").attr("src","<%=themePath%>/image/pic_help_onfocus.jpg");$("#help p").css("color","white");});
	 $("#help").mouseout(function () {  $("#help img").attr("src","<%=themePath%>/image/pic_help.jpg");$("#help p").css("color","#d6e4e9");});
	 
	 $("#about").mouseover(function () { $("#about img").attr("src","<%=themePath%>/image/pic_about_onfocus.jpg"); $("#about p").css("color","white");});
	 $("#about").mouseout(function () { $("#about img").attr("src","<%=themePath%>/image/pic_about.jpg"); $("#about p").css("color","#d6e4e9");});
	 
	 $("#out").click(function(){ if(confirm("您确定退出吗？")){parent.location="<%=root%>/j_spring_security_logout";} });
	 $("#out").mouseover(function () { $("#out img").attr("src","<%=themePath%>/image/pic_out_onfocus.jpg"); $("#out p").css("color","white");});
	 $("#out").mouseout(function () { $("#out img").attr("src","<%=themePath%>/image/pic_out.jpg"); $("#out p").css("color","#d6e4e9");});
	 
	 
	 $("#system").click(function(e){
		 var nowTime = new Date();
	 	 var ret = OpenModalWindow("<%=root%>/fileNameRedirectAction.action?toPage=/frame/main_list.jsp?fla="+nowTime.getTime(),window,'help:no;status:no;scroll:no;dialogWidth:800px; dialogHeight:400px');
	 	 if(ret&&ret!=''){
	 	 	window.location = "<%=root%>/ipp/syslogin/action/sysLogined!goToSite.action?gotoSiteId="+ret;
	 	 }
		 
	});
});

function topMenuNavMove(_this) {
    $this=$(_this);
    $("#divFloatBg").show();
    $("#divFloatBg").each(function(){$.dequeue(this, "fx");}).animate({                
        top: $this.position().top + ($this.css("margin-top") == "auto" ? -3 : 2),
        left: $this.position().left+4
    },600, 'easeOutExpo');
}

/**
* 菜单收缩
*/
function topMenuContraction() {
	$("#topMenuContr").removeClass("menuNavSpan_Contraction");
    $("#topMenuContr").addClass("menuNavSpan_Expand");
   	
	$(".topMenuDiv").each(function() {$.dequeue(this, "fx")}).animate({
		height:70
	} , 0);
	if ($("#divFloatBg").offset().top >= 67 ) {
	    $("#divFloatBg").hide();
	}
	$(".topMenuDiv").css("background", "");
}

/**
* 菜单展开
*/
function topMenuExpand() {
	var backgroundv = 272-66*MENU_MAX
	$(".topMenuDiv").css("background", "url(<%=themePath%>/image/daohang/menu_expand_bg.jpg) 0 -"+backgroundv+"px no-repeat");
	$(".topMenuDiv").each(function() {$.dequeue(this, "fx")}).animate({
   		height: TOP_MENU_MAX_HEIGHT
   	} , 0);
}
function redirect(url,title,id){
   document.getElementById("mainIframe").contentWindow.redirect(url,title,id);
}
</script>

	</head>
	<body>
	
	<div class="topMenuDiv">
		<div class="topMenuDiv_top">
			<div id="divFloatBg"></div>

			<div class="menuItem" style="margin-top:5px;" onclick="redirect('<%=basePath%>/desktop/desktopWhole.action','个人桌面', '123')">
			    <div class="menuItemIcon" style="background-position:0 0;"></div>
				<div class="slideItemText">门户</div>
			</div>
			 
			<div class="menuItem" style="margin-top:5px;">
			    <div class="menuItemIcon" style="background-position:-111px -0;"></div>
				<div class="slideItemText">流程</div>
			</div>

			 
			<div class="menuItem" style="margin-top:5px;">
			    <div class="menuItemIcon" style="background-position:-315px 0;"></div>
				<div class="slideItemText">费用管控</div>
			</div>
			 
			<div class="menuItem" style="margin-top:5px;">
			    <div class="menuItemIcon" style="background-position:-37px -74px;"></div>
				<div class="slideItemText">报表</div>
			</div>

			 
			<div class="menuItem"  style="margin-top:5px;">
			    <div class="menuItemIcon" style="background-position:-222px -0;"></div>
				<div class="slideItemText">人事</div>
			</div>
			 
			<div class="menuItem"  style="margin-top:5px;">
			    <div class="menuItemIcon" style="background-position:-74px -0;"></div>
				<div class="slideItemText">知识</div>
			</div>

			 
			<div class="menuItem" style="margin-top:5px;">
			    <div class="menuItemIcon" style="background-position:-37px -0;"></div>
				<div class="slideItemText">协助</div>
			</div>
			 
			<div class="menuItem">
			    <div class="menuItemIcon" style="background-position:-148px -0;"></div>
				<div class="slideItemText">客户</div>
			</div>

			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-185px -0;"></div>
				<div class="slideItemText">项目</div>
			</div>
			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-37px -37;"></div>
				<div class="slideItemText">资产</div>
			</div>

			 
			<div class="menuItem">
			    <div class="menuItemIcon" style="background-position:-74px -37px;"></div>
				<div class="slideItemText">会议</div>
			</div>
			 
			<div class="menuItem">
			    <div class="menuItemIcon" style="background-position:-111px -37px;"></div>
				<div class="slideItemText">通信</div>
			</div>

			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-148px -37px;"></div>
				<div class="slideItemText">日程</div>
			</div>
			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-185px -37px;"></div>
				<div class="slideItemText">车辆</div>
			</div>

			 
			<div class="menuItem">
			    <div class="menuItemIcon" style="background-position:-222px -37px;"></div>
				<div class="slideItemText">相册</div>
			</div>
			 
			<div class="menuItem">
			    <div class="menuItemIcon" style="background-position:-148px -74px;"></div>
				<div class="slideItemText">计划</div>
			</div>

			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-0 -74px;"></div>
				<div class="slideItemText">信息</div>
			</div>
			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-74px -74px;"></div>
				<div class="slideItemText">设置</div>
			</div>

			 
			<div class="menuItem" >
			    <div class="menuItemIcon" style="background-position:-111px -74px;"></div>
				<div class="slideItemText">绩效</div>
			</div>
			
		</div>
		<div style="cursor:hand;width:27px;height:55px;float:left;position:relative;top:5px;left:0px;" id="topMenuContr" class="menuNavSpan_Expand"></div>
		</div>
	
	
	
		<div class="bg_header">
			<div id="header">
				<div class="header_active">
					<div class="header_name">
						思创数码科技股份有限公司协同办公软件
					</div>
				</div>
				<%-- 
				<div class="header_Function">
					<div class="header_Function_icon ">
						<ul>
							<li class="header_system_line" style="width:260px;align:left; padding:0;margin-top:6px;">
								<p style="float:left;color:#444;height:16px;line-height:1;padding:7px 0 1px 3px;"><span>当前站点：</span><b></b></p>
								<p style="float:right; height:20px; width:65px; padding-right:2px;">
									<button id="system" class="input_button_4"><img src="<%=themePath%>/image/ico_change.gif" style="margin:0 5px -5px 0 !important;"/>切换</button>
								</p>
							</li>
							
						</ul>
					</div>
				</div>
				--%>
			</div>
			<div class="kkmenu">
			<div id="side1">
				<div id="side1_2">
					<ul>
					<li id="help" onclick="alert('显示帮助信息。');">
					<img src="<%=themePath%>/image/pic_help.jpg" />帮助
					</li>
					<li id="about"  onclick="alert('显示关于信息。');">
					<img src="<%=themePath%>/image/pic_about.jpg" />关于
					</li>
					<li id="out" >
					<img src="<%=themePath%>/image/pic_out.jpg" />退出
					</li>
				    </ul>
				</div>
			<div id="side1_3" style="margin:0 !important;margin-Top:-24px;margin-Right:150px;" 
			onclick="redirect('/ipp/personalcenter/action/personal!info.action','个人信息','00070001')">
					<img src="<%=themePath%>/image/pic_person.gif"/>&nbsp;&nbsp;
			</div>				
			</div>
			<div style="margin-Top:1px!important;margin-Top:-23px;">&nbsp;&nbsp;
				<select id="sysChange" class="formin">
					<option value="">-- 系统切换 --</option>
					<option value="oa">OA</option>
					<option value="…">…</option>
				</select>	
			 </div>
				<br style="clear:both"/>
			</div>
		</div>

		<div class="main" id="main">
		   <iframe width="100%" id="mainIframe" name="mainIframe"  frameborder="0" scrolling="no" src='<%=path%>/theme/theme!getFirstLevelPrivil.action?sysSysCode=<%=sysCode%>'></iframe>
		</div>
		<div id="mask_top"></div>
	</body>
</html>
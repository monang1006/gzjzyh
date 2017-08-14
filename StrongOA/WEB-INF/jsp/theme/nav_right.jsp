<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>main_tree</title>
		<link href="<%=themePath%>/css/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; }
			html,body { height:100%; }
		</style>

		<script type="text/javascript" src="<%=scriptPath%>/jquery-1.4.1.min.js"></script>
		<script language="javascript" src="<%=scriptPath%>/component.js"></script>
		<script type="text/javascript">
			function autoAdaptWindow(){
				var w1 = $("#main_tree").outerWidth();
				return w1 - 5;
			}
		<%--	
			function addTab(id, name, url){
				if(v_top.isExist(id)){
					v_top.activeId(id);
				}else{
					v_top.add({id:id, text:name, closeAble:true, url:url, lazy:false, highlight:true});
				}
			}
		--%>
function addTab(id, name, url){
	//table页个数 
	//var tableNum = $("ul li");
	if(id != null || id != ""){
		if(url.indexOf("?")!=-1){
			url += "&privilSyscode="+id;
		}else{
			url += "?privilSyscode="+id;
		}
	}
	if(v_top.isExist(id)){
	
		$('.tabHead').each(function(index) {
			if(name.indexOf($(this).text()) != -1){
				v_top.activeId(id);
				$('.tabBodyWraper').each(function(index) {
					if($(this).css("display") == 'block'){
						var win = $(this).children('iframe')[0].contentWindow ||
											$(this).children('iframe')[0].window;

						win.location = url;
					}
				});
			}
		});
	}else{
		var tableNum =  $("#tabHeadContainer ul li");
		if(tableNum.length>8){
			alert("超过最大标签，请关闭别的标签！");
			return;
		}
		if(name=="首页"){
			v_top.add({id:id, text:name, closeAble:false, url:url, lazy:false, highlight:true});
		} else {
			v_top.add({id:id, text:name, closeAble:true, url:url, lazy:false, highlight:true});
		}
	}
}
			
			
		function refreshWorkByTitle(sUrl,sTitle,id){
			if(id=="undefined" || id==undefined){id=sTitle;}
	    	addTab(id,sTitle,sUrl);
        }
            
  function initDoc(){
  addTab('','首页','<%=path%>/desktop/desktopWhole!gotoWebDesktop.action?webType=webFtabSW');
  <security:authorize ifAllGranted="001-000800040001">
  addTab('111','信息采编首页','<%=path%>/xxbs/action/index.action');
  </security:authorize>
	//$.post("<%=root%>/desktop/desktopWhole!getDesktopPortalList.action",
   // function(data){
    //	var arr=data.split("|");
    //	if(data.indexOf("HTTP 错误")==-1){
	 //   	for(var i=0;i<arr.length;i++){
	//    		var info=arr[i].split("#");
	//    		if(info[1]!=null&&info[1]!=""){	
	 //   			addTab(info[1],info[0],'<%=path%>/desktop/desktopWhole.action?defaultType=2&portalId='+info[1]);
	 //   		}else{
	 ///   			addTab('',info[0],'<%=path%>/desktop/desktopWhole.action');
	 //   		}
	//    	}
    ///	}else{
    //		refreshWorkByTitle('<%=path%>/common/error/403.jsp',"无权访问");
    //	}
	//	try{
	//		msg2.StartListen();
	//	}catch(e){
	//	}
	//	if(window.top.opener != null && window.top.document.URL.indexOf("ptl=yes")!=-1)eval(unescape(tmp_.text))
	//});
}
            
            
            
function activeRefresh(){
	$('.tabBodyWraper').each(function(index) {
		if($(this).css("display") == 'block'){
			var win = $(this).children('iframe')[0].contentWindow ||
								$(this).children('iframe')[0].window;
			//alert(win.location)
			win.location.reload();
		}
	});
}

function activeGoBack(){
	$('.tabBodyWraper').each(function(index) {
		if($(this).css("display") == 'block'){
			var win = $(this).children('iframe')[0].contentWindow ||
								$(this).children('iframe')[0].window;
			win.history.back();
		}
	});	
}

function closeWork(){
	var flag = true;
	$('.tabHead').each(function(index) {
		if(flag && $(this).hasClass('menubg')&&(!$(this).hasClass('menubg2'))){
			$(this).find('.tabHeadClose').click();
			flag = false;
		}
	});
}


		</script>
	</head>

	<body onload="initDoc();">
		<div style="position:absolute; height:28px; left:0px; top:0px; right:0; z-index:99; width:100%;background:url(<%=themePath%>/image/top_nav/bg_nav_left_new_03.jpg) repeat-x; ">
			<div class="top_nav" id="top_nav" style="float: left;display:inline;width:90%">
				<div id="tabHeadContainer" class="tabHeadContainer" style="padding-left: 5px;width:90%">
				</div>
			</div>
			<div style="display:inline;float:right;width: 60px;margin-top: 6px;">
				<img src="<%=frameroot%>/image/tab/newtree-back.jpg" title="当前工作区后退"
					onClick="activeGoBack();"
					onMouseOver="this.src='<%=frameroot%>/image/tab/Back_o.jpg'"
					onMouseOut="this.src='<%=frameroot%>/image/tab/newtree-back.jpg'" />
				<img src="<%=frameroot%>/image/tab/newtree_refresh.gif"
					title="刷新当前工作区" onClick="activeRefresh()"
					onMouseOver="this.src='<%=frameroot%>/image/tab/refresh_z_03.gif'"
					onMouseOut="this.src='<%=frameroot%>/image/tab/newtree_refresh.gif'" />
				<img src="<%=frameroot%>/image/tab/newtree_close.gif" title="关闭当前工作区"
					onClick="closeWork()"
					onMouseOver="this.src='<%=frameroot%>/image/tab/close1.gif'"
					onMouseOut="this.src='<%=frameroot%>/image/tab/newtree_close.gif'" />
			</div>
		</div> 
		<div id="main_tree" style="position:absolute;top:28px; height:95%; bottom:0px; right:0px; overflow:hidden; z-index:9; width:100%">
			
		</div>
		<web:htabpanel var="v_top" autoAdaptFunc="autoAdaptWindow"
			tabHeadRenderer="tabHeadContainer" tabHeadWidth="80" showMenu="false"
			tabBodyRenderer="main_tree"></web:htabpanel>
			
	</body>
	
<script>
$(document).ready(function() {
	$('.tabHeadWraper').dblclick(function(){
		closeWork();
	}) ;
});
</script>
</html>
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib prefix="c" uri="/tags/c.tld"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=themePath%>/css/global.css" rel="stylesheet"
			type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<title>body</title>
		<style type="text/css">
             html { -webkit-box-sizing:border-box; -moz-box- :border-box; box-sizing:border-box; padding:0}
             html,body { height:100%;}
             .side { position:relative; height:100%; float:left; margin-right:0 !important; margin-right:-3px; width:31px; border-right:1px solid #ffffff;}
             .side2 { position: absolute; height:100%; background:#dce6ef; overflow:hidden;  float:left; margin-right:0 !important; margin-right:-3px; width:170px; z-index:999; display:none; border:1px solid #ffffff; padding:1px; left:32px;}
             .side3 { position: relative; left:0px;}
             .main { position:relative; overflow:hidden; height:100%; margin-left:36px; _margin-left:0; z-index:2; border-left:1px solid #0a5a9e;}
             .main2 {margin-left:206px;}
             .main iframe { height:100%; width:100%; background:#fff; position:absolute; left:0; top:0;}
             .left_nav_two_ul {background:url(<%=themePath%>/image/colors_03.jpg) no-repeat;}
             .left_nav_two_li {background:url(<%=themePath%>/image/colors_03.jpg) no-repeat;}
        </style>

		<script type="text/javascript"
			src="<%=scriptPath%>/jquery-1.4.1.min.js"></script>
		<script language="javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript">
		function displaySubPrivil(index, text, id, privilCode){
			var subDiv = $("#" + id);
			if(subDiv.attr("flag") != "true"){
				subDiv.attr("flag", "true");
				//subDiv.accordion({fillSpace: true, datweb:[{"id":"0012","name":"工作处理","url":"javascript:redirect('/demonull','工作处理');","pid":"-1"},{"id":"00120001","name":"新建工作","url":"javascript:redirect('/demo/fileNameRedirectAction.action?toPage=/usermanage/usermanage-content.jsp','新建工作');","pid":"0012"},{"id":"00120002","name":"在办工作","url":"javascript:redirect('/demo/fileNameRedirectAction.action?toPage=work/work-doingmain.jsp','在办工作');","pid":"0012"},{"id":"00120003","name":"待办工作","url":"javascript:redirect('/demo/fileNameRedirectAction.action?toPage=work/work-todomain.jsp','待办工作');","pid":"0012"},{"id":"00120004","name":"主办工作","url":"javascript:redirect('/demo/fileNameRedirectAction.action?toPage=work/work-hostedbymain.jsp','主办工作');","pid":"001"},{"id":"00120005","name":"已办工作","url":"javascript:redirect('/demo/fileNameRedirectAction.action?toPage=work/work-processedmain.jsp','已办工作');","pid":"0012"}]});
				$.ajax({
	                type: "post",
	              // url: "<%=path%>/security/initModuleNewTree!getSubPrivilMenu2.action",
	              //  data: {privilCode: privilCode, sysSysCode: "<%=sysCode%>"},
	                 url: "<%=path%>/theme/theme!getSubPrivilMenu2.action",
	                data: {priviparent: privilCode, sysSysCode: "<%=sysCode%>"},
	                async: false,
	                dataType: "json",
	                success: function(msg){
	                	subDiv.accordion({ icons: { 'header': '<%=themePath%>/image/colors_03.jpg', 'headerSelected': '<%=themePath%>/image/colors_03.jpg' } ,headerNavigation: true, autoHeight: true, hideIcons:true, hideDiv: true, fillSpace: true, data:msg});
	                },
	                error: function(e) {
	                	alert(e); 
	                }
	            });
			}
			$("#side2").show();
			subDiv.accordion("resize");
			return true;
		}
		
		function redirect(url, name, id){
		   //parent.v_top.add({id:'tab_'+name, text:name, closeAble:true, url:url});
		   var $side2 = $("#side2");
		   if(!$side2.hasClass("side3")){
		   		$("#side2").hide();
		   }
		   if(id == "00450001"){
		   		window.showModalDialog("http://218.64.17.200:1188/sempDT/", window,"dialogWidth=1000px;dialogHeight=600px;resizable=no;status=no;help=no;location=no");
		   }else{
		   		document.mainIframe.addTab(id, name, url);
		   }
		}

		function displayQuickMenu(){
			h_left.reset();
			$("#side2").show();
			$("#_quickMenu").show();
			$("#_quickMenu").accordion("resize");
			return true;
		}
		
		function autoAdaptWindow(){
			var h1 = $("#side2").outerHeight();
			return h1 - 10;
		}
		
		</script>

	</head>
	<body>
		<div id="side" class="side">			
			<div class="top_nav_left" id="tabHeaderNav_left" style="padding-top: 10px;">
			</div>
		</div>

		<div id="side2" class="side2" style="height:100%">
			<div class="tree_menu" id="tree_menu">
				<div class="tree_menu_unlock" id="menu_lock" title="锁定"></div>
				<div id="subMenu" style="width:100%;height:100%;">
					
				</div>
			</div>
		</div>

		<web:vtabpanel var="h_left" tabHeadRenderer="tabHeaderNav_left"
			showMenu="false" tabHeadHeight="100" autoAdaptFunc="autoAdaptWindow"
			tabBodyRenderer="subMenu">
		</web:vtabpanel>

	
	</body>
</html>
<script>
$(document).ready(function(){
	 
	$.fn.containsDom = function(ele){
		var parentEle = ele;
		while(!parentEle.attr("id") || parentEle.attr("id") != $(this).attr("id")){
			if(parentEle.length < 1 || parentEle[0].tagName == "BODY"){
				return false;
			}
			parentEle = parentEle.parent();
		}
		return true;
	}

	$("#menu_lock").click(function(){
		$("#side2").toggleClass("side3");
		$("#main").toggleClass("main2");
		$(this).toggleClass("tree_menu_lock").toggleClass("tree_menu_unlock");
		if($(this).hasClass("tree_menu_lock")){
			$(this).attr("title", "解锁");
		}else{
			$(this).attr("title", "锁定");
		}
	});
	
	$(document).bind("mouseover", function(e){
		var hidenEle = $("#side2");
		if(!hidenEle.hasClass("side3") && !hidenEle.containsDom($(e.target)) && hidenEle.attr("hiddenFlag") == "1"){
			hidenEle.attr("hiddenFlag", "2");
			hidenEle.hide();
		}
	});
	
	$("#side2").bind("mouseover", function(e){
		$(this).attr("hiddenFlag", "1");
	});

	//var h1 = $("#side").outerHeight();
	//var h2 = $("#quick").outerHeight();
	//h_left.setHeight(h1 - h2 - 10);

	<c:forEach items="${privilList}" var="privil">
		$("#subMenu").append($("<div id='_" + '<c:out value="${privil.privilSyscode}"/>' + "' style='display:none;width:100%;height:100%;'></div>"));
		<c:choose>
		<c:when test="${privil.rest4 == '1'}">
			h_left.add({id:'<c:out value="${privil.privilSyscode}"/>', text:'<font color="red"><c:out value="${privil.privilName}"/></font>', closeAble: false, callback:displaySubPrivil, callbackargs:'<c:out value="${privil.privilSyscode}"/>', hightlight:false, lazy:false});
		</c:when>
		<c:otherwise>
			h_left.add({id:'<c:out value="${privil.privilSyscode}"/>', text:'<c:out value="${privil.privilName}"/>', closeAble: false, callback:displaySubPrivil, callbackargs:'<c:out value="${privil.privilSyscode}"/>', hightlight:false, lazy:false});
		</c:otherwise>
		</c:choose>
	</c:forEach>

	
});
</script>

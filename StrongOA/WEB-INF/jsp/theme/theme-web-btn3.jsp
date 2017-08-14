<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<%@taglib prefix="s" uri="/struts-tags"%>
	<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase" />
	<%@taglib prefix="web-menu" uri="/tags/web-menu"%>
	<%@ taglib uri="/tags/security" prefix="security"%>
	<%@include file="/common/include/rootPath.jsp"%>
		<title>导航区域</title>
		<SCRIPT language="javascript" src="<%=jsroot%>/menu/personMenu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/jquery-1.10.2.min.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=frameroot %>/webFtabforRB/css/dhmain.css" />
		<script language="javascript" src="<%=root %>/frame/theme-web/webFtabforRB/jquery.js"></script>
<style type="text/css">	
.childern1{ font-weight:bold; background-image:url(<%=root %>/frame/theme-web/webFtabforRB/images/jiantou.gif); background-position-y:bottom; background-position-x:center; background-repeat:no-repeat; padding-bottom:8px;}		
</style>

<style>
.box{
	width:96%;
	height:30px;
	position:relative;
	margin:0 auto;
	overflow:hidden;
	padding-top: 3px;
	display: block;}
.unit{
	position:absolute;
	width:100000px;
	left:0px;
	top:0px;
	display: block;
	font-style:normal;
	}
.bta{
	float:left;
	display: block;
	}
.btb{
	float:right;
	display: block;
	}
.unit a{
	display:block;
	padding:0 5px;
	float:left;
	margin-right:10px;
	text-align: center;
	background: url(<%=frameroot %>/webFtabforRB/images/fengge.jpg) no-repeat right center;}


	
#box2{
	width:92%;
	height:30px;
	position:relative;
	margin:0 auto;
	overflow:hidden;
	padding-top: 3px;
	display: block;}
.unit2{
	position:absolute;
	width:100000px;
	left:0px;
	top:0px;
	display: block;
	font-style:normal;
	}
#bta2{
	float:left;
	display: block;
	}
#btb2{
	float:right;
	display: block;
	}
.unit2 a{
	display:block;
	padding:0 5px;
	float:left;
	margin-right:10px;
	text-align: center;
	background: url(<%=frameroot %>/webFtabforRB/images/fengge.jpg) no-repeat right center;}					
			
#processcomtent{
display: block;
}

</style>



		<%
			String orgName = (String) request.getSession().getAttribute( "orgName");
	
			String uName = (String) request.getSession().getAttribute(
			"userName");

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
		
<script type="text/javascript">
$(function(){
	 $(".bta").click(function(){
		var current = $('.navlihov').text();
		var i =  $("div[type=" + current + "] .unit").css("left");	
		var i = parseInt(i.substring(0,i.length-2));
		var tempWidth=0;
		$("div[type=" + current + "] .unit a").each(function(){
		   if(!$(this).is(":hidden")){
				tempWidth  += $(this).width();
				tempWidth  += 20;
				
			}
		});
		var unitWidth = tempWidth-20;
		var boxWidth = $("div[type=" + current + "] .box").width();
		var leftWidth=unitWidth-boxWidth;
		if(i<0){
		  var i = i+Math.ceil(leftWidth/2);
	     $(".unit").animate({"left":i},200); 
	     }
    });
 $(".btb").click(function(){
		var current = $('.navlihov').text();
		var i =  $("div[type=" + current + "] .unit").css("left");		
		var i = parseInt(i.substring(0,i.length-2));
		var tempWidth=0;
		$("div[type=" + current + "] .unit a").each(function(){
		   if(!$(this).is(":hidden")){
				tempWidth  += $(this).width();
				tempWidth  += 20;
				
			}
		});
		
		var unitWidth = tempWidth-20;
		var boxWidth = $("div[type=" + current + "] .box").width();
		var leftWidth=unitWidth-boxWidth;
		if(leftWidth+i>0){
			var i = i-Math.ceil(leftWidth/2);
	        $(".unit").animate({"left":i},200);
        }
	
    })
    $("#bta2").click(function(){	
  		var current = $('.navlihov').text();
		var i =  $("div[type=" + current + "] .unit2").css("left");	
		var i = parseInt(i.substring(0,i.length-2));
		var tempWidth=0;
		$("div[type=" + current + "] .unit2 a").each(function(){
		   if(!$(this).is(":hidden")){
				tempWidth  += $(this).width();
				tempWidth  += 20;
				
			}
		});
		var unitWidth = tempWidth-20;
		var boxWidth = $("div[type=" + current + "] #box2").width();
		var leftWidth=unitWidth-boxWidth;
		if(i<0){
		  var i = i+Math.ceil(leftWidth/8);
	     $(".unit2").animate({"left":i},200); 
	     }
    });
    $("#btb2").click(function(){	debugger;
        var current = $('.navlihov').text();
		var i =  $("div[type=" + current + "] .unit2").css("left");		
		var i = parseInt(i.substring(0,i.length-2));
		var tempWidth=0;
		$("div[type=" + current + "] .unit2 a").each(function(){
		   if(!$(this).is(":hidden")){
				tempWidth  += $(this).width();
				tempWidth  += 20;
				
			}
		});
		
		var unitWidth = tempWidth-20;
		var boxWidth = $("div[type=" + current + "] #box2").width();
		var leftWidth=unitWidth-boxWidth;
		if(leftWidth+i>0){
			var i = i-Math.ceil(leftWidth/8);
	        $(".unit2").animate({"left":i},200);
        }
    })
});



		
	var ybsyParm = "收文办件登记";
	<security:authorize ifAllGranted="001-00060023">
		ybsyParm="";
	</security:authorize>
		
		$(document).ready(function() {
			var width = screen.availWidth;
			//alert(width);
			if(width > 1024){
				$("#toolbar").css("width","1252px");
				$("#zph").attr("id","kph"); $("#zpc").attr("id","kpc");
			}else{
			    $("#toolbar").css("width","984px");
				$("#kph").attr("id","zph"); $("#kpc").attr("id","zpc"); 
			}

		});
		<security:authorize ifAllGranted="001-00060001">
		$.post("<%=path%>/notify/notify!loginList.action",
		           function(data){
				    	if(data!=""){
				    		var arr=data.split(",");
					    	for (var i=0;i<arr.length;i++){
					    		var aId = arr[i];
					     	//	var rValue= window.open("<%=path%>/notify/notify!viewLogin.action?afficheId="+aId,aId,"width=650,height=500,top=100, left=350,toolbar=no,menubar=no, scrollbars=no, resizable=yes,location=no, status=no");
					    		var rValue= window.showModalDialog("<%=path%>/notify/notify!viewLogin.action?afficheId="+aId,aId,'dialogWidth:750px;dialogHeight:630px;help:no;status:no;scroll:no');
					    	}
			    		}	
			    });	
		</security:authorize>
		
		//切换宽窄屏
		$(function(){
		  $(".navrsp01 a").click(function(){
		    var nrspa = $(this).text();
			if( nrspa == "切换到宽屏" ){ 
				$(this).text("切换到窄屏"); 
				$("#zph").attr("id","kph"); $("#zpc").attr("id","kpc");
				//工作区切换宽窄屏
				top.bottomFrame.frames(0).changeWeb(1);
				
			}
			if( nrspa == "切换到窄屏" ){
			 	$(this).text("切换到宽屏"); 
			 	$("#kph").attr("id","zph"); $("#kpc").attr("id","zpc"); 
			 	//工作区切换宽窄屏
			 	top.bottomFrame.frames(0).changeWeb(2);
		 
			 }
		  });		   
		});
		
		//显示隐藏TOP区
		$(function(){
		  $(".top").hover(function(){		  		
		      $(".tplsqh").show();
			},function(){
	
				$(".tplsqh").hide();
				});
		  $(".tplsqh span").click(function(){
		  	hidemenu();
			$(".banner").toggle();
			$(".tplsqh").toggleClass("topqhyc");
			$(".top").toggleClass("tophov");
		  });
		})
		
		
		
		//当前位置变字体：二级菜单
		$(function(){	
		  $(".navlinks div a").click(function(){
			  $(this).addClass("childern1").siblings(".navlinks div a").removeClass("childern1");
			});	   
		});
		
		//一级菜单
		$(function(){
		$(".navlinks div").eq(0).show().siblings("div").hide();
		$(".nav li").eq(0).addClass("navlihov").siblings("li").removeClass("navlihov");
		  $(".nav li").click(function(){
			  var i = $(".nav li").index(this);
			  $(this).addClass("navlihov").siblings("li").removeClass("navlihov");
			  $(".navlinks div").eq(i).show().siblings("div").hide();
			  $(".navlinks div a").removeClass("childern1");
			});	   
		});
		
		
		
		function changePassword(){
			var rValue= showModalDialog("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", 
										window,"dialogWidth:350pt;dialogHeight:150pt;status:no;help:no;scroll:no;");	
		}
		function gotoExit(){
			top.location="<%=path%>/j_spring_security_logout";
		}
		
		function alertHelpPage(){
			var sindex = getSysConsole().getSelectedIndex();
			var tabID = getSysConsole().getTabParam("ID",sindex);
			var url = getSysConsole().getTabObj(tabID,sindex);
			url=""+url;
			//alert("url:"+url);
			var arr = url.split("privilSyscode=");
			ihelpTreeId = arr[1];
			//alert("ihelpTreeId:"+ihelpTreeId);
			ihelpTreeId=""+ihelpTreeId;
			if(ihelpTreeId!="undefined"){
				var rValue= showModalDialog("<%=basePath%>/helpedit/helpedit!gethelpTreeId.action?ihelpTreeId="+ihelpTreeId, 
										   window,"dialogWidth:750pt;dialogHeight:450pt;status:no;help:no;scroll:no;");
			}else{
				var rValue= showModalDialog("<%=basePath%>/helpedit/helpedit!gethelpTreeId.action?url="+url, 
						window,"dialogWidth:750pt;dialogHeight:450pt;status:no;help:no;scroll:no;");
			}
		}
		function refreshWorkByTitle(url,title){
		   
			top.bottomFrame.refreshWorkByTitle(url,title);
		}
		//新建流程
		function creatProcess(workflowName,formId,workflowType){
			var width=screen.availWidth-10;
	   		var height=screen.availHeight-30;
	   		var title = encodeURI(encodeURI(workflowName));
	   		//alert(title);
	   		WindowOpen("<%=root%>/senddoc/sendDoc!input.action?workflowName="+title+"&formId="+formId+"&workflowType="+workflowType,"",width, height);			
		}
		
		function portal(portalId,title){	
			top.bottomFrame.navigate("<%=basePath%>/desktop/desktopWhole.action?defaultType=2&operate=view&portalId="+portalId,title);
		}
		
		function mydesk(){	
			top.bottomFrame.navigate("<%=basePath%>/desktop/desktopWhole.action","个人桌面");
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
			top.bottomFrame.navigate("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-webMenupage.jsp?privCode="+code,title);
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
		  	parent.title.rows='66,*';
			state = 1;
		  }else if(state == 1){
			parent.title.rows='167,*';
			state = 0;
		  }
		}
		
		
		
	</script>
	
	</head>
	
	<body>
		<div id="hd">
		  <div id="hdtopbg">
		    <div id="kph">
		      <div class="top">
		        <div class="tplsqh" style="display:none"><span></span></div>
		        <div class="banner">
		          <div class="welmenu fri">
		            <div class="welmt">
		              <p>欢迎您：<%=uName %></p>
		              <input class="welmtc" type="button" onclick="gotoExit();"/>
		            </div>
		            <div class="welmb">
		              <span class="welmbsp01"><a title="在线帮助" href="#" onclick="alertHelpPage()">在线帮助</a></span>
		              <span class="welmbsp02"><a title="修改密码" href="#" onclick="changePassword()">修改密码</a></span>            </div>
		          </div>
		        </div>
		        <div class="nav">
		          <div class="navga">
		            <ul>	             
					  	<li type="index"><a href="#" onclick="refreshWorkByTitle('<%=path%>/desktop/desktopWhole!gotoWebDesktop.action?webType=webFtabforRB','办公门户')">首页</a></li>
					<!--  
					  <security:authorize ifAllGranted="001-00020001">	             
					  	<li>发文处理</li>
					  </security:authorize>
					  <security:authorize ifAllGranted="001-00020002">	 
					  	<li>收文处理</li>
					  </security:authorize>
					  <security:authorize ifAllGranted="001-00010001">	 
		              	<li>我的流程</li>
		              </security:authorize>
		              <security:authorize ifAllGranted="001-00010002">	 
		              	<li>日程管理</li>
		              </security:authorize>
		              <security:authorize ifAllGranted="001-00010003">	
		              	<li><a href="#" onclick="top.bottomFrame.navigate('<%=path%>/fileNameRedirectAction.action?toPage=message/message.jsp','办公门户')">内部邮件</a></li>
		              </security:authorize>
		              <security:authorize ifAllGranted="001-00040002">	 
		             	<li>信息发布</li>
		              </security:authorize>
		              <security:authorize ifAllGranted="001-00010007">	 
		              	<li>文件柜</li>
		              </security:authorize>
		              <security:authorize ifAllGranted="001-00010010">	 
		              	<li>工作日志</li>
		              </security:authorize>
		              <security:authorize ifAllGranted="001-00020008">	 
		              	<li>会议管理</li>
		              </security:authorize>			              
		              <security:authorize ifAllGranted="001-00010001">	 
		              	<li>新建流程</li>
		              </security:authorize>
		             --> 
		             	<li>新建流程</li>
		             	<security:authorize ifAllGranted="001-00020001">	             
					  		<li>发文处理</li>
					  	</security:authorize>
					  	<security:authorize ifAllGranted="001-00020002">	 
					  		<li>收文处理</li>
					  	</security:authorize>
		             	<li>我的工作</li>
		             	<security:authorize ifAllGranted="001-00090002">
		             	<li>自由流</li>
		             	</security:authorize>
		             	<li>个人事务</li>
		             	<li>资料中心</li>
		             	<security:authorize ifAllGranted="001-00040002">
		             	<li>信息发布</li>
		                </security:authorize>
		               <security:authorize ifAllGranted="001-00240033">
		             	<li>会议通知</li>
		             	</security:authorize>
		               <security:authorize ifAllGranted="001-00070002">	 
		             	<li>发文传输</li>
		             	</security:authorize>
		             	<security:authorize ifAllGranted="001-00070003">	 
		             	<li>收文传输</li>
		             	</security:authorize>
		                <security:authorize ifAllGranted="001-00080001">
		             	<li>信息采编</li>
		             	</security:authorize>
		                <security:authorize ifAllGranted="001-00080002">
		             	<li>采编基础数据</li>
		             	</security:authorize>
		             	<security:authorize ifAllGranted="001-00020003">
		             	<li>档案管理</li>
		             	</security:authorize>
		             	<!-- 
		             	<li>论坛交流</li>
		             	 -->
		             								
		            </ul>
		          </div> 
		          <div class="navlinks" id="toolbarcontent">
					 <!-- 首页 -->
					 <div type="index">欢迎来到南昌市人民政府办公厅综合协同办公平台！今天是${serverTime}</div>
					 <!-- 新建流程 -->
					 <div type="新建流程">
					     <i id="btb2"><img src="<%=frameroot %>/webFtabforRB/images/zuo.png" style="cursor: hand;"></i>
                          <i id="bta2"><img src="<%=frameroot %>/webFtabforRB/images/you.png" style="cursor: hand;"></i>
					        <i id="box2">
                             
                              <i id="processcomtent" class="unit2" moveIndex="0">
								      <script type="text/javascript">
										var processcomtent = document.getElementById("toolbarcontent");
										function ref_process(){
											$.ajax({ type:"post",
													url:'<%=path%>/theme/theme!showProcess.action?'+new Date(),
													data:{},
													success:function(response){
														if(response==""){
														}else{
															$("#processcomtent").html(response);
					
														}
													},
													error:function(data){
														//alert("对不起，操作异常"+data);
													}
											   });
										}
										
														
									</script> </i>
						<!-- 
					 	<a href="#" onclick="creatProcess('发文流程','82','2')">&nbsp发文流程&nbsp</a>|
					 	<a href="#" onclick="creatProcess('收文流程','83','3')">&nbsp收文流程&nbsp</a>|
					 	<a href="#" onclick="creatProcess('意见征询','61','16318')">&nbsp意见征询&nbsp</a>|
					 	<a href="#" onclick="creatProcess('请示与报告流程','107','273952')">&nbsp请示报告&nbsp</a>|
					 	<a href="#" onclick="creatProcess('门户网站信息发布流程','121','10531')">&nbsp信息报送&nbsp</a>|
					 	 -->
					 	 
					 	 </i>
					    
					 </div>
					  <!-- 发文处理 -->
					  <security:authorize ifAllGranted="001-00020001">
					 <div type="发文处理">
					 	<security:authorize ifAllGranted="001-000200010006">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=2','办公门户')">&nbsp公文草拟&nbsp</a>|
					 	</security:authorize> 
					 	<security:authorize ifAllGranted="001-000200010007">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!workflowDraft.action?workflowType=2','办公门户')">&nbsp公文草稿&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010003">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listHosted.action?workflowType=2','办公门户')">&nbsp已拟公文&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010004">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listprocessed.action?state=2&workflowType=2','办公门户')">&nbsp已审公文&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010008">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listtodo.action?workflowType=2','办公门户')">&nbsp公文审核&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010009">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listprocessed.action?state=1&workflowType=2','办公门户')">&nbsp办结公文&nbsp</a>|
					 	</security:authorize>
					 	<!--<security:authorize ifAllGranted="001-000200010010">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person&mytype=user&workflowType=2','办公门户')">&nbsp公文查询&nbsp</a>|
					 	</security:authorize>-->
					 	<security:authorize ifAllGranted="001-000200010010">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!searchWorkflow.action?workflowType=2','办公门户')">&nbsp公文查询&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010012">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/docdis/docDis.action','办公门户')">&nbsp公文分发&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010011">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!repeal.action?state=2&workflowType=2&formId=t_oa_senddoc&privilSyscode=000200010011','办公门户')">&nbsp发文回收站&nbsp</a>|
					 	</security:authorize>	
					 </div>
					 </security:authorize>
					 <!-- 收文处理 -->
				  	<security:authorize ifAllGranted="001-00020002">	
					 <div type="收文处理">
					 	<security:authorize ifAllGranted="001-000200020007">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createWorkflow.action?workflowType=3','办公门户')">&nbsp来文登记&nbsp</a>|
					 	</security:authorize> 
					 	<security:authorize ifAllGranted="001-000200020008">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!draft.action?workflowType=3&formId=t_oarecvdoc&handleKind=2&privilSyscode=000200020008','办公门户')">&nbsp来文草稿&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020009">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listHosted.action?workflowType=3','办公门户')">&nbsp主办来文&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020010">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>//work/work!listprocessed.action?state=2&workflowType=3','办公门户')">&nbsp已办来文&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020012">	 
					 		<%--<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todo.action?workflowType=3','待办文件')">&nbsp待办来文&nbsp</a>|--%>
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listtodo.action?workflowType=3&type=sign','办公门户')">&nbsp待办来文&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020011">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listprocessed.action?state=1&workflowType=3','办公门户')">&nbsp办结来文&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020013">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person&mytype=user&workflowType=3','办公门户')">&nbsp来文查询&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020006">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=historyquery/historyquery-report.jsp','办公门户')">&nbsp收文统计&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200010014">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/docafterflow/docafterflow!showGetList.action','办公门户')">&nbsp公文签收&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000200020015">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!repeal.action?state=2&workflowType=3&formId=t_oarecvdoc&privilSyscode=000200020015','办公门户')">&nbsp收文回收站&nbsp</a>|
					 	</security:authorize>
					 </div>
					 </security:authorize>
					 
					 <!-- 我的工作 -->
					 <div type="我的工作">
					   <i class="btb"><img src="<%=frameroot %>/webFtabforRB/images/zuo.png" style="cursor: hand;"></i>
                          <i class="bta"><img src="<%=frameroot %>/webFtabforRB/images/you.png" style="cursor: hand;"></i>
					        <i class="box">
                              <i class="unit" moveIndex="0">
									    <security:authorize ifAllGranted="001-000900010010">	
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!workflowDraft.action','办公门户')">&nbsp流程草稿&nbsp</a>
										</security:authorize>
									 	<security:authorize ifAllGranted="001-000900010013">	 
									 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listtodo.action?type=sign','办公门户')">&nbsp待办工作&nbsp</a>
									 	</security:authorize> 	
										<security:authorize ifAllGranted="001-000900010025">	 
									 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todo.action?workflowType=3&type=notsign&formId=t_oarecvdoc','办公门户')">&nbsp待签收文件&nbsp</a>
									 	</security:authorize> 	
									 	<security:authorize ifAllGranted="001-000900010011">	
										<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listHosted.action','办公门户')">&nbsp我的请求&nbsp</a>
										</security:authorize>
									 	<security:authorize ifAllGranted="001-000900010012">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listprocessed.action?state=0&excludeWorkflowTypeName='+ybsyParm,'办公门户')">&nbsp已办工作&nbsp</a>
										</security:authorize> 
										<security:authorize ifAllGranted="001-000900010014">	
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listprocessed.action?state=1','办公门户')">&nbsp办结工作&nbsp</a>
										</security:authorize> 
										<security:authorize ifAllGranted="001-000900010015">
									 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDelegation/action/processDelegation.action','办公门户')">&nbsp工作委派&nbsp</a>
									 	</security:authorize>
										<security:authorize ifAllGranted="001-000900010016">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person','办公门户')">&nbsp流程查询与监控&nbsp</a>
										</security:authorize>
										<security:authorize ifAllGranted="001-000900010018">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/traceDoc/traceDoc.action','办公门户')">&nbsp重要文件跟踪&nbsp</a>
										</security:authorize>
										<security:authorize ifAllGranted="001-000900010020">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listRepeal.action','办公门户')">&nbsp公文回收站&nbsp</a>
							           	</security:authorize>
										<security:authorize ifAllGranted="001-000100010007">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!entrustWorkflow.action?assignType=0','办公门户')">&nbsp已委派流程&nbsp</a>
									 	</security:authorize>
										<security:authorize ifAllGranted="001-000100010021">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=/theme/theme-wenJianChaXun.jsp?mytype=dept','办公门户')">&nbsp处内文件&nbsp</a>
									 	</security:authorize>
									 	<security:authorize ifAllGranted="001-000100010023">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=/theme/theme-wenJianChaXun.jsp?mytype=org','办公门户')">&nbsp厅内文件&nbsp</a>
									 	</security:authorize>
									 	<security:authorize ifAllGranted="001-000900010026">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listMytodo.action?state=0','办公门户')">&nbsp我的在办文件&nbsp</a>
									 	</security:authorize>
									 	<security:authorize ifAllGranted="001-000900010027">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listMytodo.action?state=1','办公门户')">&nbsp我的办结文件&nbsp</a>
									 	</security:authorize>
									 	<security:authorize ifAllGranted="001-000900010028">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listRepeal.action?state=6','办公门户')">&nbsp我的取消文件&nbsp</a>
									 	</security:authorize>
									 	<security:authorize ifAllGranted="001-000900010029">
											<a href="#" onclick="refreshWorkByTitle('<%=path%>/work/work!listReturn.action','办公门户')">&nbsp我的退回文件&nbsp</a>
									 	</security:authorize>
									 	
						  </i>
                        </i>
					 </div>
					  <!-- 自由流 -->
					  <security:authorize ifAllGranted="001-00090002">
					 <div type="自由流 ">
					    <security:authorize ifAllGranted="001-000900020001">	
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!createFwWorkflow.action','办公门户')">&nbsp新建流程&nbsp</a>|
						</security:authorize>
					 	<security:authorize ifAllGranted="001-000900020002">	 
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/freedomworkflow/freedomWorkflow.action','办公门户')">&nbsp待办流程&nbsp</a>|
					 	</security:authorize> 	
					 	<security:authorize ifAllGranted="001-000900020003">	
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/freedomworkflow/freedomWorkflow!doneList.action','办公门户')">&nbsp已办流程&nbsp</a>|
						</security:authorize>
					 	
					 </div>
					 </security:authorize>
					 <!-- 个人事务 -->
					 <div type="个人事务">
					     <i class="btb"><img src="<%=frameroot %>/webFtabforRB/images/zuo.png" style="cursor: hand;"></i>
                          <i class="bta"><img src="<%=frameroot %>/webFtabforRB/images/you.png" style="cursor: hand;"></i>
					        <i class="box">
                              <i class="unit" moveIndex="0">
								 	<security:authorize ifAllGranted="001-000100060001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/sms/sms.action','办公门户')">&nbsp手机短信&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100030001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=message/message.jsp','办公门户')">&nbsp内部邮件&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAnyGranted="001-00010011,001-000100050006">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/suggestion/approvalSuggestion.action','办公门户')">&nbsp常用意见&nbsp</a>
								 	</security:authorize>
								 	 <security:authorize ifAllGranted="001-000100040001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=address/address-personal.jsp','办公门户')">&nbsp个人通讯录&nbsp</a>
								 	</security:authorize>
								 	 <security:authorize ifAllGranted="001-000100040003">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=/publiccontact/publicContact-showContent.jsp','办公门户')">&nbsp公共联系人&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000400010002">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/notify/notify!mylist.action','办公门户')">&nbsp公告发布&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100050001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/myinfo/myInfo!input.action','办公门户')">&nbsp个人设置&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100020001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/calendar/calendar.action','办公门户')">&nbsp个人日程&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100100001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/worklog/workLog.action','办公门户')">&nbsp个人日志&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100070001">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/prsnfldr/privateprsnfldr/prsnfldrFolder!content.action','办公门户')">&nbsp个人文件柜&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100070002">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=prsnfldr/privateprsnfldr/prsnfldrFolder-sharecontent.jsp','办公门户')">&nbsp共享文件柜&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100070003">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=prsnfldr/publicprsnfldr/publicPrsnfldrFolder-content.jsp','办公门户')">&nbsp公共文件柜&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100070004">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=prsnfldr/departmentprsnfldr/departmentPrsnfldrFolder-content.jsp','办公门户')">&nbsp部门文件柜&nbsp</a>
								 	</security:authorize>
								 	<security:authorize ifAllGranted="001-000100070005">
								 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=prsnfldr/agencyprsnfldr/agencyPrsnfldrFolder-content.jsp','办公门户')">&nbsp机构文件柜&nbsp</a>
								 	</security:authorize>
					 	      </i>
                        </i>
					 </div>
					 <!-- 资料中心 -->
					 <div type="资料中心">
					 	<security:authorize ifAllGranted="001-000400010001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/notify/notify.action','办公门户')">&nbsp通知公告&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000100040002">
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=address/addressOrg.jsp','办公门户')">&nbsp系统通讯录&nbsp</a>|
					 	</security:authorize>
						<security:authorize ifAllGranted="001-000100100002">
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/worklog/workLog.action?operateType=all','办公门户')">&nbsp日志查询&nbsp</a>|
					 	</security:authorize>
						<security:authorize ifAllGranted="001-000500030002">
					 		<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=doctemplate/doctempItem/doctempItemContent.jsp','办公门户')">&nbsp公文模板&nbsp</a>|
					 	</security:authorize>
					 	<security:authorize ifAllGranted="001-000500040008">
					 	<a href="#" onclick="refreshWorkByTitle('<%=path%>/viewmodel/viewModel.action','办公门户')">&nbsp首页管理&nbsp</a>|
					 	</security:authorize>  
						<!--<a href="#" onclick="refreshWorkByTitle('<%=path%>/infopub/articles/columnArticles.action?columnId=ff8080813002b86d0130032b82fd0405&showtype=0','领导讲话')" >&nbsp领导讲话&nbsp</a>|
						<a href="#" onclick="refreshWorkByTitle('<%=path%>/infopub/articles/columnArticles.action?columnId=4428e48a34c20f88013575ec3a136d0f&showtype=0','工作简报')" >&nbsp工作简报&nbsp</a>|
						
						 <a href="#" onclick="" >&nbsp厅发公文&nbsp</a>|
						<a href="#" onclick="refreshWorkByTitle('<%=path%>/infopub/articles/columnArticles.action?columnId=ff8080813002b86d0130030a7636023e&showtype=0','每周动态')" >&nbsp每周动态&nbsp</a>|
						<a href="http://www.bookan.com.cn/jxlib/index.aspx" target="_blank">&nbsp电子期刊&nbsp</a>|
						<a href="http://pds.sslibrary.com/library.jsp?username=ssgpjxtsg" target="_blank">&nbsp电子图书&nbsp</a>| 
						
						<a href="<%=path%>/install.jsp" target="_blank">&nbsp相关下载&nbsp</a>|
						-->
					 </div> 
					  <!-- 信息发布 -->
					  <security:authorize ifAllGranted="001-00040002">
					   <div type="信息发布">
					   	<security:authorize ifAllGranted="001-000400020006">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=infopub/articles/articles-showcontent.jsp','办公门户')">&nbsp信息查询&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000400020001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=infopub/articles/articles-content.jsp','办公门户')">&nbsp信息采集&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000400020004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=infopub/articles/columnArticles-content.jsp','办公门户')">&nbsp信息发布&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000400020002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=infopub/column/columnContent.jsp','办公门户')">&nbsp栏目管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000400020003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/infopub/articles/articles!reclist.action','办公门户')">&nbsp回收站&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000400020005">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/infopubTemplate/infoTemplate.action','办公门户')">&nbsp信息模板&nbsp</a>|
						</security:authorize>
						
					   </div>
					  </security:authorize>
				 <!-- 会议通知-->
				 <security:authorize ifAllGranted="001-00240033">
					 <div type="会议通知">
					 	<security:authorize ifAllGranted="001-002400330001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/noticeconference/noticeConference.action?state=0','办公门户')">&nbsp发送通知&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-002400330002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/noticeconference/clientConference!container.action','办公门户')">&nbsp会议报名&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-002400330003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/noticeconference/noticeConference!handleConference.action','办公门户')">&nbsp已办通知&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-002400330004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/noticeconference/noticeConferenceType.action','办公门户')">&nbsp类型管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-002400330005">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/noticeconference/noticeConference.action?state=1','办公门户')">&nbsp已发会议通知&nbsp</a>|
						</security:authorize>
					 </div> 
			 </security:authorize>
					  <!-- 发文传输 -->
					 <security:authorize ifAllGranted="001-00070002">	
					 <div type="发文传输">
					 	<security:authorize ifAllGranted="001-000700020001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/sends/transDoc.action','办公门户')">&nbsp新建发文&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700020002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/sends/transDoc!sign.action','办公门户')">&nbsp公文签章&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700020003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/sends/docSend!doclist.action','办公门户')">&nbsp公文分发&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700020004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/sends/docSend.action','办公门户')">&nbsp已发公文&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700020005">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=agencygroup/agencyGroup-personal.jsp','办公门户')">&nbsp发文组维护&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700020006">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=sends/docSend-content.jsp','办公门户')">&nbsp发文档案中心&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700020007">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=sends/docSend-content.jsp','办公门户')">&nbsp拒签公文&nbsp</a>|
						</security:authorize>					
					 </div> 
					</security:authorize>	
					
					  <!-- 收文传输 -->
					 <security:authorize ifAllGranted="001-00070003">	
					 <div type="收文传输">
					 	<security:authorize ifAllGranted="001-000700030001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/receives/recvDoc!todo.action','办公门户')">&nbsp待处理文件&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700030002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/receives/recvDoc!received.action','办公门户')">&nbsp已收公文&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700030003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/receives/recvDoc!rejected.action','办公门户')">&nbsp拒收文件&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700030004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=receives/archive/archiveDoc-content.jsp','办公门户')">&nbsp收文档案中心&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000700030005">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/sends/docSend!printPassword.action','办公门户')">&nbsp打印密码&nbsp</a>|
						</security:authorize>
												
					 </div> 
					</security:authorize>	
					
				  <!-- 信息采编-->
					 <security:authorize ifAllGranted="001-00080001">	
					 <div type="信息采编">
					 	<security:authorize ifAllGranted="001-000800010001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/handling!content.action','办公门户')">&nbsp信息处理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800010002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/release.action','办公门户')">&nbsp期刊发布&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800010003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/invitation.action','办公门户')">&nbsp约稿管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800010004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/infoReport.action','办公门户')">&nbsp通报管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800010005">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/bulletin.action','办公门户')">&nbsp通知公告管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800010006">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/statistics!point.action','办公门户')">&nbsp采用统计&nbsp</a>|
						</security:authorize>
												
					 </div> 
					</security:authorize>	
					
					  <!-- 采编基础数据-->
					 <security:authorize ifAllGranted="001-00080002">	
					 <div type="采编基础数据">
					 	<security:authorize ifAllGranted="001-000800020001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/journal.action','办公门户')">&nbsp期刊管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800020002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/wordTemplate.action','办公门户')">&nbsp期刊模板管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800020003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/column.action','办公门户')">&nbsp栏目管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800020004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/issue.action','办公门户')">&nbsp期号管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800020007">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/piece.action?flag=guoban','办公门户')">&nbsp呈国办管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000800020008">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/xxbs/action/piece.action','办公门户')">&nbsp呈阅件管理&nbsp</a>|
						</security:authorize>						
					 </div> 
					</security:authorize>
					<!-- 档案管理 -->
					<div type="tempFile">
					 	<security:authorize ifAllGranted="001-000200030001">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=archive/tempfile/tempFile-content.jsp','办公门户')">&nbsp资料中心&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000200030002">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=archive/archivefolder/folderContent.jsp?moduletype=pige','办公门户')">&nbsp归档确认&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000200030003">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=archive/archivefolder/folderContent.jsp?moduletype=manage','办公门户')">&nbsp案卷管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000200030004">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=archive/sort/sortContent.jsp','办公门户')">&nbsp类目管理&nbsp</a>|
						</security:authorize>
						<security:authorize ifAllGranted="001-000200030006">
							<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=archive/filesearch/fileSearch-search.jsp','办公门户')">&nbsp搜索查询&nbsp</a>|
						</security:authorize>					
					 </div> 	
					
		          </div>  
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
					
		<SCRIPT type="text/javascript">
		function showCompanyDetail(){
			window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
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
		
		function reLoadDate() {	
			//刷新导航栏二级菜单
			//ref_toolbar();
			//刷新导航栏新建流程二级菜单
			ref_process();	
		}
		reLoadDate();
		setInterval("reLoadDate()", 3600 * 1000);
		
		
		
	</SCRIPT>
	
    <div style=”width:0;height:0”>
		 <iframe id="OfficeCon" src='<%=path%>/common/OfficeControl/emptyOfficeControl.jsp'  
										frameborder="0" scrolling="no" height="1px" width="100%" align="top">				
										</iframe>
				</div>
	
	</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ include file="/common/include/rootPath.jsp"%>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<jsp:directive.page import="com.strongit.oa.message.MessageFolderManager"/>

<%
	MessageFolderManager messageFolderManager = (MessageFolderManager)ServiceLocator.getService("messageFolderManager");
	String unreadCount = messageFolderManager.getUnreadCount("recv","");

%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>门户桌面</title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/bgtdesktop/css.css" />
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
	<link type="text/css" rel="stylesheet" href="<%=root %>/frame/theme-web/webFtab/css/dhmain.css" />
	<script language="javascript" src="<%=root %>/frame/theme-web/webFtab/jquery.js"></script>
	
	<script type="text/javascript">
	
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
	
		function changePassword(){
			var rValue= showModalDialog("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", 
		                                window,"dialogWidth:350pt;dialogHeight:150pt;status:no;help:no;scroll:no;");	
		}
		function todoWork(thename){
			var aName=document.getElementById("mywork");
			navigate('<%=path%>/senddoc/sendDoc!hostedWorkflow.action',aName.innerHTML);
		}
		function gotoExit(){
			top.location="<%=path%>/j_spring_security_logout";
		}
		
				//催办 
		function urge(instanceId){
			//alert(instanceId);
			var insId = instanceId.split("$")[0];
			OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-urgchoose.jsp?instanceId="+insId,400, 300, window);
		}
		
		//查看办理记录
		function view(instanceId,taskId){
			//alert(instanceId);
			//alert(taskId);
			var insId = instanceId.split("$")[0];
			var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+insId+"&taskId="+taskId+"&type=processurgency",'Cur_workflowView',width, height, "办理记录");
		
		}
		
		function sendMsg(userNames){
			//alert(userNames);
			if(userNames != ""){
				var userArr = userNames.split(",");
				if(userArr.length>1){
				
					var ret = OpenWindow(
							scriptroot
									+ "/fileNameRedirectAction.action?toPage=/desktop/desktopChooseRev.jsp", 400, 300, new Array(userNames));
					var userName = ret.split("_")[0];
					var userLoginName = ret.split("_")[1];
					top.location.href = "bigant://sendmsg/?receiver="+userLoginName+"&receivername="+userName;

				}else{
					var userName = userArr[0].split("_")[0];
					var userLoginName = userArr[0].split("_")[1];
					top.location.href = "bigant://sendmsg/?receiver="+userLoginName+"&receivername="+userName;
				}

			}
		}
		
		//文件检索
		function search(){
			var processName = encodeURI(encodeURI($("#processNameTEXT").val()));
			//alert(processName);
			navigate('<%=path%>/work/work!listprocessed.action?state=2&businessName=' + processName ,'公文检索');
			
		}
		
		//页面bottomFrame页面刷新		
		function refreshWorkByTitle(url,title){
			window.parent.refreshWorkByTitle(url,title)
		}
		
		//页面bottomFrame页面刷新		
		function navigate(url,title){
			top.bottomFrame.navigate(url,title);
		}
		
		function openLink(url,title){
			top.bottomFrame.navigate(url,title);
		}
				
		function openMore(obj){
			if(typeof(obj.previousSibling.url)=="undefined"||obj.previousSibling.url=="undefined"||obj.previousSibling.url==""){
				return;
			}
			var url = "<%=path%>/"+obj.previousSibling.url;
			var title = obj.previousSibling.innerText;
			window.parent.refreshWorkByTitle(url,title)
		}

		
		//去除多余的"更多"链接
		function replaceMore(html){
//			return html.replace("更多</a>","</a>");
			return html.replace("<IMG SRC=\""+scriptroot+"/oa/image/more.gif\" BORDER=\"0\" /></a>","</a>");
		}
		
		//切换我的办文和办理统计；
		$(function(){
			$(".rightboxbar2 span").eq(1).click(function(){
			  $(this).addClass("rtbxsping").siblings("span").removeClass("rtbxsping");
			  $(".rtbdlist").eq(1).show().siblings("div").hide();
			  $(".rightbox2").removeClass("rightbox3");
			  $("#gwjkytj").show();
			  $("#wdbw").hide();
			});	
			$(".rightboxbar2 span").eq(0).click(function(){
			  $(this).addClass("rtbxsping").siblings("span").removeClass("rtbxsping");
			  $(".rtbdlist").eq(0).show().siblings("div").hide();
			  $(".rightbox2").addClass("rightbox3");
			  $("#wdbw").show();
			  $("#gwjkytj").hide();
			});
		});
		
		
		
		function gotologin(){
			window.top.location="<%=path%>/j_spring_security_logout";
		}
		
		
		//日程模块-万年历调用的方法
		function mouseoverToShow(date,element,evt){
			var calID = "drag_"+document.getElementById("calDeskTopID").value;
			var calbody = document.getElementById(calID);
			var contentborder = document.getElementById("cont");
			
			//获取日程元素在页面中的坐标
			  var x=calbody.offsetLeft;   
			  var y=calbody.offsetTop;   
			  var objParent=calbody.parentNode; 
			  while(objParent.tagName.toUpperCase()!=   "BODY"){   
				  x+=objParent.offsetLeft;   
				  y+=objParent.offsetTop;   
				  objParent = objParent.parentNode;   
			  }   
			var mosP = mouseCoords(evt);
			//alert("鼠标在模块中的坐标：\n mosP.x="+mosP.x+"\n mosP.y="+mosP.y);
			if((parseInt(x)+parseInt(mosP.x)+270)>parseInt(document.body.clientWidth)){
				$('#detail').css('left',parseInt(x)+parseInt(mosP.x)-270);
				$('#detail').css('top',parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop));
				$('#detail').css('background',"url('<%=path%>/oa/image/calendar/calendar_destop_bgL.GIF') no-repeat");
				$('#detailFrame').attr("src","<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date);
				
				//$('detail').style.left=parseInt(x)+parseInt(mosP.x)-270;
				//$('detail').style.top=parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop);
				//$('detail').style.background = "url('<%=path%>/oa/image/calendar/calendar_destop_bgL.GIF') no-repeat";
				//$('detailFrame').src="<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date ;
			}else{
				$('#detail').css('left',parseInt(x)+parseInt(mosP.x));
				$('#detail').css('top',parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop));
				$('#detail').css('background',"url('<%=path%>/oa/image/calendar/calendar_destop_bgR.GIF') no-repeat");
				$('#detailFrame').attr("src","<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date);
				
			    //$('detail').style.left=parseInt(x)+parseInt(mosP.x);
			    //$('detail').style.top=parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop);
			    //$('detail').style.background = "url('<%=path%>/oa/image/calendar/calendar_destop_bgR.GIF') no-repeat";
			    //$('detailFrame').src="<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date ;
			}
			
			$('#detail').css("display","");
			 //$('detail').style.display = "";
		}
		
		function mouseCoords(ev){
			ev  = ev || window.event;
			if(ev.pageX || ev.pageY){
				return {x:ev.pageX, y:ev.pageY};
			}
			return {
				x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
				y:ev.clientY + document.body.scrollTop  - document.body.clientTop
			};
		}
		
		
		function reloadPage(){
			window.location.reload();
		}
		//切换宽窄屏
		function changeWeb(value){
			if(value == "1"){
				$("#zph").attr("id","kph"); 
			}else{
			 	$("#kph").attr("id","zph"); 
			}
		}
		
	</script>
	</head>

	<body scroll="yes">
	<div id="detail" class="UserCard" style="display:none" >
		<table width="100%" height="100%" style="margin-left: 3px;margin-right: 3px;margin-top: 3px;">
			<tr>
				<td align="left" width="18px">
				</td>
				<td align="center" valign="bottom">
					<iframe id="detailFrame" src="" 
						width="88%" height="110px" border="0" > 
					</iframe>
				</td>
				<td width="22px"></td>
			</tr>
			<tr>
				<td></td>
				<td valign="top" align="center" >
					<a href="#" onclick="detail.style.display='none'">关闭</a>
				</td>
				<td width="22px"></td>
			</tr>
		</table>
	</div>
		
	<div id="kpc">
	  <div id="cont" class="clearfix">
	    <div class="conlef fle">
	      <div class="conld02">
	        <dl class="conldlist">
	          <dt class="tit">通知公告<a style="font-size:12px; font-weight:normal; padding-left:100px;" href="#" onclick="refreshWorkByTitle('<%=path%>/notify/notify.action','办公门户')">更多>></a></dt>
	          <dd style="height:195px; overflow:hidden;">
	            <div class="cnrglists fle" type="1" id="notifycontent"> 
	                    <script type="text/javascript">
									var notifycontent = document.getElementById("notifycontent");
									function ref_dbnf(){
										$.ajax({ type:"post",
											url:'<%=path%>/notify/notify!showTableDesktop.action?'+new Date(),
											data:{showNum:"9",subLength:"12",sectionFontSize:"14"},
											success:function(response){
												if(response==""){
												}else{
							                		notifycontent.innerHTML=replaceMore(response);
												}
											},
											error:function(data){
												//alert("对不起，操作异常"+data);
											}
									   });
									   }
						</script> 
	            </div>
	          </dd>
	        </dl>
	      </div>
	      <div class="conld03" id="drag_402882a031c2e8ce0131c2ee583f0023">
	        <dl class="conldlist">
	          <dt class="tit">个人日程<a style="font-size:12px; font-weight:normal; padding-left:100px;" href="#"  onclick="refreshWorkByTitle('<%=path%>/calendar/calendar.action','办公门户')">更多>></a></dt>
				<dd style="height:199px; overflow:hidden;">
				<input type="hidden" id="calDeskTopID" value="402882a031c2e8ce0131c2ee583f0023" />
	            <div class="cnrglists fle" type="1" id="calendarcontent"> 
	                    <script type="text/javascript">
									var calendarcontent = document.getElementById("calendarcontent");
									function ref_dbpc(){
										$.ajax({ type:"post",
											url:'<%=path%>/calendar/calendar!showTableDesktop.action?'+new Date(),
											data:{showNum:"9",subLength:"11",sectionFontSize:"14"},
											success:function(response){
												if(response==""){
												}else{
							                		calendarcontent.innerHTML=replaceMore(response);
												}
											},
											error:function(data){
												//alert("对不起，操作异常"+data);
											}
									   });
									   }
						</script> 
	            </div>
	          </dd>
	        </dl>
	      </div>
	      <!-- 
	      <div class="conld04">
	        <dl class="conldlist">
	          <dt class="tit"><a href="#">万年历</a></dt>
	          <dd style="height:200px; overflow:hidden;">
	            <div><iframe id="daycontent" src='<%=path%>/calendar/calendar!desktop.action?inputType=red'  frameborder="0"
														scrolling="no" height="200px" width="100%" align="top"> </iframe></div>
	          </dd>
	        </dl>
	      </div>
	       -->
	       
	       <div class="conld05">
		        <div class="qkts">
		          <p class="qktspa fle"><a href="http://www.bookan.com.cn/jxlib/index.aspx" target="_blank">电子期刊</a></p>
		          <p class="qktspb fri"><a href="http://pds.sslibrary.com/library.jsp?username=ssgpjxtsg" target="_blank">电子图书</a></p>
		        </div>
		        <div class="dtq">
		          <iframe src="http://m.weather.com.cn/m/pn12/weather.htm?id=101240101T " width="220" height="110" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe>
		        </div>
		    </div>
	       
	    </div>
	    <div class="conrig fri">
	      <div class="conrigdb clearfix">
	        <div class="cnrgblef fle">
              <div class="conrigdt">
	          <div style="position:absolute; z-index:9999; top:6px; left:100px; right:12px;"><span style="float:right;"><a style="font-size:12px; color:#FFF; font-weight:normal;" href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todoWorkflow.action?workflowType=-116153&type=sign','待办提醒')">更多>></a></span><span style="font-size:14px; color:#FFF; "><a style="color:#FFF; font-weight:normal;" herf="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todoWorkflow.action?workflowType=-116153&type=sign','待办提醒')"><strong id="dbcount">0</strong></a> 条</span></div>
	        <div class="crsyj"><div></div></div>
	        <dl>
	          <dt class="tit">待办提醒 </dt>
	          <dd class="clearfix" style="height:193px; overflow:hidden;">
	            <div class="cnrglists" type="1" id="dbsycontent"> 
	                    <script type="text/javascript">
									var dbsycontent = document.getElementById("dbsycontent");
									function ref_dbsy(){
										$.ajax({ type:"post",
											url:'<%=path%>/senddoc/sendDoc!showTableTodo.action?'+new Date(),
											data:{ type:"sign",showDate:"1",showCreator:"1",showNum:"7",subLength:"15",sectionFontSize:"14",workflowType:"-116153" },
											success:function(response){
												if(response==""){
												}else{
							                		dbsycontent.innerHTML=replaceMore(response);
	
												}
											},
											error:function(data){
												//alert("对不起，操作异常"+data);
											}
									   });
									   }
						</script> 
	            </div>
	          </dd>
	        </dl>
	      </div>
	          <div class="cnrgbl01">
	            <dl class="cnrgblist">
	              <dt class="tit"><span class="dtmore"><a href="#" onclick="refreshWorkByTitle('<%=path%>/traceDoc/traceDoc.action','办公门户')">更多&gt;&gt;</a></span>重要文件跟踪</dt>
		             <dd style="height:193px; overflow:hidden;">
		              	<div type="1" id="zywjcontent">
		              	   <script type="text/javascript">
										var zywjcontent = document.getElementById("zywjcontent");
										function ref_zywj(){
											$.ajax({ type:"post",
													url:'<%=path%>/traceDoc/traceDoc!showTableDesktop.action?'+new Date(),
													data:{showCreator:"1",showDate:"1",showNum:"7",subLength:"18",sectionFontSize:"14"},
													success:function(response){
														if(response==""){
														}else{
									                		zywjcontent.innerHTML = replaceMore(response);
														}
													},
													error:function(data){
														//alert("对不起，操作异常"+data);
													}
											   });
										}
							</script> 
		
						</div>
		              </dd>
	            </dl>
	          </div>
	          <div class="cnrgbl02">
	            <dl class="cnrgblist">
	              <dt class="tit"><span class="dtmore"><a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!processedWorkflow.action?state=0&filterSign=1&excludeWorkflowType=370020,413460','办公门户')">更多&gt;&gt;</a></span>已办事宜</dt>
	              <dd style="height:194px; overflow:hidden;">
	                  <div class="content" type="1" id="zbwjcontent"> 
	                        <script type="text/javascript">
								var zbwjcontent = document.getElementById("zbwjcontent");
								function ref_zbwj(){
									$.ajax({ type:"post",
											url:'<%=path%>/senddoc/sendDoc!showTableDesktopDoneWork.action?'+new Date(),
											data:{ processStatus:"0",showCreator:"1",showDate:"1",showNum:"7",subLength:"15",sectionFontSize:"14",filterSign:"1"},
											success:function(response){
												if(response==""){
												}else{
													var zbwjcontentHtml = response.replace("<div class=\"select\">","<div class=\"select\" style=\"display:none\">")
							                		zbwjcontent.innerHTML = replaceMore(response);	
												}
											},
											error:function(data){
												//alert("对不起，操作异常"+data);
											}
									   });
								}
										
							</script> 
	
	                   </div>
	              </dd>
	            </dl>
	          </div>
	        </div>
	        <div class="cnrgbrig fri">
	          <div class="cnrgbr02 cnrgbr04">
                <div class="crgbrsyj"><div></div></div>	
                  <dl>
                    <dt class="tit">公文检索</dt>
                    <dd class="cnrgbr2dd clearfix">	
                      <p class="searp"><input type="text" id= "processNameTEXT" class="inptext" /><input type="button" class="inpbutt" onclick="search();"/></p>
                    </dd>
                  </dl>
                <div class="crgbrxyj"><div></div></div>	
              </div>
              <div class="cnrgbr02 cnrgbr03">
	            <div class="crgbrsyj"><div></div></div>	
	            <dl>
	              <dt class="tit"><span class="dcdsz"><a href="#" onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','办公门户')">菜单设置</a></span>快捷菜单</dt>
				  <!--   <dd class="cnrgbr3dd clearfix" style=" min-height:270px; height:auto!important; height:270px;overflow:hidden;">	-->
				   <dd class="cnrgbr3dd clearfix" style="height:270px; overflow:hidden;">
	                <ul>
	                	<s:iterator id="ufl" value="userFMList" status="ufl">
	                	
	                	<s:if test="#ufl.privilName=='新建流程'">                	
							 <li class="cnrddli01"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:if> 
						<s:elseif test="#ufl.privilName=='手机短信'">
							<li class="cnrddli02"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='流程查询'">
							<li class="cnrddli03"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='办结事宜'">
							<li class="cnrddli04"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='系统通讯录'">
							<li class="cnrddli05"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='意见管理'">
							<li class="cnrddli06"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						<s:elseif test="#ufl.privilName=='个人日志'">
							<li class="cnrddli07"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='常用下载'">
							<li class="cnrddli08"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:elseif>
						
						<s:else>
							<li><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:if test="privilName.length()>4"><s:property value="privilName.substring(0,4)" />...</s:if><s:else><s:property value="privilName" /></s:else></a></li>
						</s:else>
						</s:iterator>
					<!-- 
	                  <li class="cnrddli01"><a href="#">新建流程</a></li>
	                  <li class="cnrddli02"><a href="#">手机短信</a></li>
	                  <li class="cnrddli03"><a href="#">流程查询</a></li>
	                  <li class="cnrddli04"><a href="#">办结事宜</a></li>
	                  <li class="cnrddli05"><a href="#">系统通讯录</a></li>
	                  <li class="cnrddli06"><a href="#">意见管理</a></li>
	                  <li class="cnrddli07"><a href="#">个人日志</a></li>
	                  <li class="cnrddli08"><a href="#">常用下载</a></li>
	                   -->                  
	                </ul>
	              </dd>
	            </dl>
	            <div class="crgbrxyj"><div></div></div>
	          </div>
          <div class="cnrgbr01">
            <h3>消息提醒</h3>
            <p>您有 <span><strong id="unread"><%=unreadCount%></strong></span> 条未阅读通知，请及时<a href="#" onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=message/message.jsp','办公门户')">查看</a></p>
          </div>
	          <div class="cnrgbr02">
	            <div class="crgbrsyj"></div>	
	            <dl>
	              <dt class="tit">办件统计</dt>
		              <dd class="cnrgbr2dd" id="bjtj" style="height:92px; overflow:hidden;">	
		                <ul>
		                  <li><a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todoWorkflow.action?workflowType=-116153&type=sign','办公门户')">
		                  		我的待办文件<span> <strong id="dbsySize">0</strong></span> 件
		                  		</a></li>
		                  <li><a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!processedWorkflow.action?state=0&filterSign=1&excludeWorkflowType=370020,413460','办公门户')">
		                  		我的在办文件<span> <strong id="zbwjSize">0</strong></span> 件
		                  		</a></li>
		                  <li><a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!processedWorkflow.action?state=1&filterSign=1','办公门户')">
		                  		我的办结文件  <span> <strong id="bjwjSize">0</strong></span> 件
		                  		</a></li>
		                  <!-- 
		                  <li>我的退回文件 <span>0</span> 件</li>
		                   -->
		                                    
		                    <script type="text/javascript">
								function ref_showWorkTotal(){
								   $.ajax({ type:"post",
										url:'<%=path%>/senddoc/sendDocDesktop!showWorkTotal.action?'+new Date(),
										data:{},
										success:function(response){
											if(response==""){
											}else{
												//signSize:9;notsignSize:6;endSize:11;noendSize:18
												var workGroup = response.split(";");
												var dbsySize = workGroup[0].split(":")[1];
												var bjwjSize = workGroup[2].split(":")[1];
												var zbwjSize = workGroup[3].split(":")[1];
												$("#dbsySize").html(dbsySize);//待办文件
												$("#dbcount").html(dbsySize);//待办文件
												$("#zbwjSize").html(zbwjSize);//在办文件
												$("#bjwjSize").html(bjwjSize);//办结文件
											}
										},
										error:function(response){
											//alert("对不起，操作异常"+data);
										}
								   });
								}
							</script>
		                   
		                </ul>
		              </dd>
	            </dl>
	            <div class="crgbrxyj"><div></div></div>	
	          </div>
	          
	          <div style="margin-top:12px;">
		          <a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person','办公门户')">
		          	<img src="<%=root %>/frame/theme-web/webFtab/images/lcjcx.jpg" />
		          </a>
	          </div>
	          
	        </div>
	      </div>
	    </div>
	  </div>
	</div>

	<div id="ft">
	  <p>江西省财政厅 版权所有</p>
	  <p>分辨率：标屏（1024×768），宽屏（1280×768） </p>
	</div>
	<script>
		function reLoadDate() {
	
			//刷新代办提醒
			ref_dbsy();
			//刷新重要文件跟踪
			ref_zywj()
			//刷新在办跟踪
			ref_zbwj();
			//刷新通知公告
			ref_dbnf();
			//刷新个人日程
			ref_dbpc();
			//刷新办件统计
			ref_showWorkTotal();
	
		}
		reLoadDate();
		setInterval("reLoadDate()", 3600 * 1000);
	</script>
</body>
</html>

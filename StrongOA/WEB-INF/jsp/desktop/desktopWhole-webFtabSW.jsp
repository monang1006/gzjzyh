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
	<link type="text/css" rel="stylesheet" href="<%=root %>/frame/theme-web/webFtabSW/css/dhmain.css" />
	

	
	<script type="text/javascript">
	
		$(document).ready(function() {
			var width = screen.availWidth;
			var height = $(window).height(); 
			$("#flag").css("height",height-80);
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
		function refreshWorkByTitle(url,title,id){
			window.parent.refreshWorkByTitle(url,title,id);
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
		
		//重要文件跟踪切换
		$(function(){
          var Cnrgb = $(".cnrgbl01 dt b");
	      Cnrgb.mouseover(function(){
	      var i = Cnrgb.index(this);
	      $(this).addClass("cnr01bing").siblings("b").removeClass("cnr01bing");
	      $(".cnrgbl01 dd>div").eq(i).show().siblings("div").hide();
	      var Cnhref = $(this).find("a").attr("href");
	      $(".cnrgbl01 dt span a").attr("href",Cnhref);
	    });	   
       });
		
		$(function(){
          var Cnrgb2 = $(".cnrgbl02 dt b");
	      Cnrgb2.mouseover(function(){
	      var i = Cnrgb2.index(this);
	      $(this).addClass("cnr01bing").siblings("b").removeClass("cnr01bing");
	      $(".cnrgbl02 dd>div").eq(i).show().siblings("div").hide();
	      var Cnhref2 = $(this).find("a").attr("href");
	      $(".cnrgbl02 dt span a").attr("href",Cnhref2);
	    });	   
       });
//tc		
$(function(){
	$(".dzrlidjs dd img").click(function(){
      $("#pplayer2").hide();
	  $("#pplayer").show();
	  $(".tcgb span").click(function(){ $("#pplayer").hide(); });
	});
	$(".dzrlidjs dt b:eq(1)").click(function(){
	  $("#pplayer").hide();
	  $("#pplayer2").show();
	  $(".tcgb span").click(function(){ $("#pplayer2").hide(); });
	});
});
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
	
	      <div class="conrigdb clearfix">
	        <div class="cnrgblef">
	         	          <div class="cnrgbl01">
	            <dl class="cnrgblist">
	              <dt class="tit">
          				<span class="dtmore"></span>
          				<b class="cnr01bing"><a href="javascript:refreshWorkByTitle('<%=path%>/receives/recvDoc!todo.action','待处理文件','000700030001')" >待处理文件</a></b>
          				<b><a href="javascript:refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todo.action?workflowflag=1&formId=t_oarecvdoc&isReceived=0','待签收文件','000700010001')" >转办待签收文件</a></b>
          				<b><a href="javascript:refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todo.action?workflowflag=0&formId=t_oarecvdoc&isReceived=0','待签收文件','000700040001')" >意见征询待签收文件</a></b>
           		  </dt>
		             <dd style=" overflow:auto;" id="flag">
		                <div class="content" id="dbsycontent"> 
		                <script type="text/javascript">
										var dbsycontent = document.getElementById("dbsycontent");
										function ref_dbsy(){
											$.ajax({ type:"post",
												url:'<%=path%>/receives/recvDoc!showDesktopDcl.action',
												data:{ showDate:"1",showCreator:"1",showNum:"7",subLength:22,sectionFontSize:"14"},
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
		              	<div id="dblwcontent" style="display: none">			               
		              	   <script type="text/javascript">
										var dblwcontent = document.getElementById("dblwcontent");
										function ref_zbdblw(){
											$.ajax({ type:"post",
												url:'<%=path%>/senddoc/sendDoc!showTableTodoZBSign.action?'+new Date(),
												data:{ showDate:"1",showCreator:"1",showNum:"7",subLength:22,sectionFontSize:"14"},
												success:function(response){
													if(response==""){
													}else{
								                		dblwcontent.innerHTML=replaceMore(response);	
													}
												},
												error:function(data){
													//alert("对不起，操作异常"+data);
												}
										   });
									   }
							</script> 
						</div>	
						<div id="dbhycontent" style="display: none">							
							 <script type="text/javascript">
										var dbhycontent = document.getElementById("dbhycontent");
										function ref_yjzxdblw(){
											$.ajax({ type:"post",
												url:'<%=path%>/senddoc/sendDoc!showTableTodoZQSign.action?'+new Date(),
												data:{ showDate:"1",showCreator:"1",showNum:"7",subLength:22,sectionFontSize:"14"},
												success:function(response){
													if(response==""){
													}else{
								                		dbhycontent.innerHTML=replaceMore(response);	
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
	          <script >
	          
				function chakan(){
					OpenWindow('<%=root%>/fileNameRedirectAction.action?toPage=/leadershipactivities/departmentinvited/departmentInvited-dataView.jsp', 843, 303, window);
				}
	          	
	          </script>
	            
	          </div>
	        </div>
	        <!--<div class="cnrgbrig fri">
              <div class="cnrgbr02 cnrgbr05">
	            <div class="crgbrsyj"></div>	
	            
	            <div class="crgbrxyj"><div></div></div>	
	          </div>
              <div class="cnrgbr02 cnrgbr03">
	            <div class="crgbrsyj"><div></div></div>	
	            <dl>
	              <dt class="tit"><span class="dcdsz"><a href="#" onclick="openLink('<%=root%>/shortcutmenu/fastMenu.action?param=person','办公门户')">菜单设置</a></span>快捷菜单</dt>
				     <dd class="cnrgbr3dd clearfix" style=" min-height:270px; height:auto!important; height:270px;overflow:hidden;">	
				   <dd class="cnrgbr3dd clearfix" style="height:233px; overflow:hidden;">
	                <ul>
	                	<s:iterator id="ufl" value="userFMList" status="ufl">
	                	
	                	<s:if test="#ufl.privilName=='新建流程'">                	
							 <li class="cnrddli01"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:if> 
						<s:elseif test="#ufl.privilName=='手机短信'">
							<li class="cnrddli02"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='流程查询'">
							<li class="cnrddli03"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='办结事宜'">
							<li class="cnrddli04"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='系统通讯录'">
							<li class="cnrddli05"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='意见管理'">
							<li class="cnrddli06"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						<s:elseif test="#ufl.privilName=='个人日志'">
							<li class="cnrddli07"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						
						<s:elseif test="#ufl.privilName=='常用下载'">
							<li class="cnrddli08"><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:elseif>
						
						<s:else>
							<li><a href="#" onclick="navigate('<%=root%><s:property value="privilAttribute" />','办公门户')"><s:property value="privilName" /></a></li>
						</s:else>
						</s:iterator>                
	                </ul>
	              </dd>
	            </dl>
	            <div class="crgbrxyj"><div></div></div>
	          </div>
	        </div>-->
	      </div>
	    </div>
	  </div>
	</div>

<!-- 	<div id="ft">
	 	<p>思创数码 版权所有</p>
	  <p>分辨率：标屏（1024×768），宽屏（1280×768） </p>
	</div>-->
	<script>
	try{
		function reLoadDate() {
	
			//刷新待办事宜
			ref_dbsy();
			
			//刷新转办待办来文
			ref_zbdblw();
			//刷新意见征询待办来文
			ref_yjzxdblw();
			//刷新待办会议
			ref_dbhy();
			//刷新已办事宜
			ref_ybsy();
			//刷新已办来文
			ref_yblw();
			//刷新已办会议
			ref_ybhy();
			//刷新办件统计
			ref_bjtj();

	
		}
		
		function ref_bjtj(){
			$.ajax({ type:"post",
				url:'<%=path%>/senddoc/sendDocDesktop!showWorkTotal.action?'+new Date(),
				data:{},
				success:function(response){
					if(response==""){
						
					}else{
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
		reLoadDate();
		setInterval("reLoadDate()", 3600 * 1000);
	}catch(e){
	}
	</script>
	<div id="pplayer" style="display:none;">
 <div class="pplaym">
   <div class="tcgb"><span></span></div>
    <div class="pplymcol">
  <h3>本日活动初步安排&nbsp;&nbsp;<span style="font-size:14px; font-weight:normal;">10月22日（星期一）</span></h3>
  <div class="pplymcta">
  <table width="100%">
    <tr>
      <th width="14%" valign="middle">王常委</th>
      <td width="86%">
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      </tr>
    <tr>
      <th valign="middle">李常委</th>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合集集合集合集合</p>
        <p>下午5：30集合</p>
      </td>
      </tr>
      <tr>
      <th width="14%" valign="middle">刘常委</th>
      <td width="86%">
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      </tr>
    <tr>
      <th valign="middle">孙常委</th>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合集集合集合集合</p>
        <p>下午5：30集合</p>
      </td>
      </tr>
  </table>
  </div>
    </div>
 </div>
</div>
<div id="pplayer2" style="display:none">
 <div class="pplaym">
   <div class="tcgb"><span></span></div>
    <div class="pplymcol">
  <h3>本周活动初步安排</h3>
  <div class="pplymcta">
  <table width="100%">
    <tr>
      <th width="6%">&nbsp;</th>
      <th width="13%">10月22日（星期一）</th>
      <th width="13%">10月23日（星期二）</th>
      <th width="13%">10月24日（星期三）</th>
      <th width="13%">10月25日（星期四）</th>
      <th width="14%">10月26日（星期五）</th>
      <th width="14%">10月27日（星期六）</th>
      <th width="14%">10月28日（星期日）</th>
    </tr>
    <tr>
      <th>王常委</th>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>
        <p>上午8：30集集合集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集集合集合合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <th>李常委</th>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合集集合集合集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合集合集合集合</p>
      </td>
      <td>
        <p>上午8：30集合</p>
        <p>中午12：38会见</p>
        <p>下午2：30集合</p>
        <p>下午5：30集合集合集合集合</p>
      </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
  </div>
    </div>
 </div>
</div>
</body>
</html>

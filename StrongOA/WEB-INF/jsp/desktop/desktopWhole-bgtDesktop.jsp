<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ page language="java" import="com.strongit.oa.common.user.model.User"%>
<%
	User user = null;
	String userName = "";
	String rest1 = "";
	String deptId = "";
	String zbURL = "#";//在办任务URL
	String yqwbjURL = "#";//逾期未办结任务URL

	if (request.getAttribute("user") != null) {
		user = (User) request.getAttribute("user");
		userName = user.getUserName();
		rest1 = user.getRest1();
	}
	if (rest1 == null || rest1.equals("")) {
		rest1 = "0";
	}
	if (request.getAttribute("deptId") != null) {
		deptId = (String) request.getAttribute("deptId");
	}
	if (rest1.equals("1")||rest1.equals("3")) {//当前用户为厅领导或分管厅领导
		zbURL = path
				+ "/work/work!deptColumnChar.action?state=2&workflowType=3";
		yqwbjURL = path
				+ "/work/work!deptColumnChar.action?state=2&workflowType=3&processTimeout=1";
	} else {
		if (rest1.equals("2")) {//当前用户为处领导
			zbURL = path + "/work/work!personColumnChar.action?deptId="
					+ deptId + "&state=2&workflowType=3";
			yqwbjURL = path
					+ "/work/work!personColumnChar.action?deptId="
					+ deptId
					+ "&state=2&workflowType=3&processTimeout=1";
		}
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>门户桌面</title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/common/bgtdesktop/css.css" />
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<style type="text/css">
div.UserCard {
	position: absolute;
	z-index: 999;
	width: 270px;
	height: 143px;
}
BODY {
	margin-top: 2px;
	margin-right: 5px;
	margin-left: 0px;
}
#title {
	border-bottom: 1px solid #bbb;
	font-size: 12px;
	padding: 0 0 10px;
}
#title h1 {
	font-size: 16px;
}
#title #intro {
}
.col_div {
	float: left;
	margin: 0px 0px 0px 0px;
}
.drag_div, .modbox {
	width: 100%;
	margin: 0px 5px 5px 5px auto;
	border: 1px solid #999;
	padding: 0px;
}
.drag_header {
	height: 22px
}
.drag_content {
	height: 40px;
	padding: 5px;
}
.drag_editor {
	background: #EBEBEB;
	padding: 10px;
	color: #333;
}
.no_drag {
	height: 0px;
	overflow: hidden;
	padding: 0;
	border: 0;
}
.btitle {
	display: inline;
}
.imglink {
	cursor: hand;
	margin-right: 2px;
}
.imglinkgray {
	cursor: hand;
	margin-right: 2px;
	filter: alpha(opacity = 50);
	filter: gray();
}
#popupImgMenuID {
	border: 1px solid #A2C7D9;
	background-color: #F1F7F9;
	padding: 5px;
	width: 200px;
}
</style>
	<script type="text/javascript">
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
		//点击通知公告，显示列表
		function noticeList(){
			var aName=document.getElementById("noticeName");
			navigate('<%=path%>/notify/notify!list.action',aName.innerHTML);
		}
		//点击领导活动，显示列表
		function leaderList(){
			var aName=document.getElementById("leaderactive");
			navigate('<%=path%>/infopub/articles/columnArticles!list.action?columnId=8a818b89335cb9a201335ccfff75000b&promulgate=promulgate',aName.innerHTML);
		}
		//政府文件列表
		function govList(){
			var aName=document.getElementById("govFile");
			navigate('<%=path%>/infopub/articles/columnArticles!list.action?columnId=8a818b89335cb9a201335ccf5ac90005&promulgate=promulgate',aName.innerHTML);
		}
		//文件下载列表
		function downList(){
			var aName=document.getElementById("fileDown");
			navigate('<%=path%>/infopub/articles/columnArticles!list.action?columnId=8a818b89335cb9a201335cd118090019&promulgate=promulgate',aName.innerHTML);
		}
		//日程安排列表
		function calendarList(){
			var aName=document.getElementById("calendar");
			window.parent.refreshWorkByTitle('<%=path%>/calendar/calendar!searchlist.action?inputType=edit',"日程安排");
		}
		//会议安排
		function meetingList(){
			var aName=document.getElementById("meeting");
			navigate('<%=path%>/meetingroom/meetingApply!list.action',aName.innerHTML);
		}
		//待办文件
		function todoList(){
			var aName=document.getElementById("todoFile");
			navigate('<%=path%>/senddoc/sendDoc!todoWorkflow.action',aName.innerHTML);
		}
		//政府要闻
		function newsList(){
			var aName=document.getElementById("govNews");
			navigate('<%=path%>/infopub/articles/columnArticles!list.action?columnId=8a818b89335cb9a201335ccf28110003&promulgate=promulgate',aName.value);
		}
		//重要文件跟踪
		function importantFileList(){
			var aName=document.getElementById("importantFile");
			navigate('<%=path%>/traceDoc/traceDoc.action',aName.innerHTML);
		}
		//在办文件跟踪
		function gotodoFileList(){
			var aName=document.getElementById("gotodoFile");
			navigate('<%=path%>/traceDoc/traceDoc!doingDocList.action',aName.innerHTML);
		}
		
		
		function openMore(obj){
			if(typeof(obj.previousSibling.url)=="undefined"||obj.previousSibling.url=="undefined"||obj.previousSibling.url==""){
				return;
			}
			var url = "<%=path%>/"+obj.previousSibling.url;
			var title = obj.previousSibling.innerText;
			window.parent.refreshWorkByTitle(url,title)
		}
		
		function refreshWorkByTitle(url,title){
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
		
		
		function reloadBgtDiv(){
			alert("1reloadBgtDiv");
			window.location.reload();
			alert("2reloadBgtDiv");
		}
		
		function gotologin(){
			window.top.location="<%=path%>/j_spring_security_logout";
		}
		
		
			//日程模块-万年历调用的方法
		function mouseoverToShow(date,element,evt){
			var calID = "drag_"+document.getElementById("calDeskTopID").value;
			var calbody = document.getElementById(calID);
			var contentborder = document.getElementById("contentborder");
			
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
				$('#detail').css('top',parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop)-220);
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
	</script>
	</head>

	<body scroll="yes" style="background-color: white;">
<div id="detail" class="UserCard" style="display: none">
      <table width="100%" height="100%" style="margin-left: 3px; margin-right: 3px; margin-top: 3px;">
    <tr>
          <td align="left" width="18px"></td>
          <td align="center" valign="bottom"><iframe id="detailFrame" src="" width="88%" height="110px" border="0"> </iframe></td>
          <td width="22px"></td>
        </tr>
    <tr>
          <td></td>
          <td  height="25px" valign="top" align="center" onclick="detail.style.display='none'">关闭</td>
          <td width="22px"></td>
        </tr>
  </table>
    </div>
<div id="contentborder" onscroll="detail.style.display='none';" class="d1002">
      <table width="100%" cellpadding="0" cellspacing="0">
      <tr>
    
    <td width="241" valign="top"><div class="left241">
          <%--<div class="huany">
      <dl>
        <dt class="name">欢迎您&nbsp;&nbsp;   <b style="color:#c02102;"><%=userName%></b>
			<div class="navbox" style="padding-right:53px;">
				<ul>
					<li style="font-size:12px">
					</li>
					<li>
						<span onclick="">帮助</span>
						<span onclick="changePassword()">修改密码</span>
						
						<span onclick="gotologin()">退出</span>
						
					</li>
				</ul>
			</div>
        
        </dt>
        </dl>
      </div>
      --%>
          <security:authorize ifAnyGranted="001-000100050007">
        <div class="weituo"> <img src="<%=path%>/common/bgtdesktop/resource/images/index/weituo.jpg"
										onclick="refreshWorkByTitle('<%=path%>/myinfo/myInfo!inputWorkEntrust.action','工作委托')" /> </div>
      </security:authorize>
          <security:authorize ifNotGranted="001-000100050007">
        <div class="weituo"> <img src="<%=path%>/common/bgtdesktop/resource/images/index/weituo.jpg"
										onclick="refreshWorkByTitle('<%=path%>/workflowDelegation/action/processDelegation.action','工作委托')" /> </div>
      </security:authorize>
          <div class="leftbox">
        <dl>
              <dd>
            <div id="drag_402882a031c2e8ce0131c2ee583f0023">
                  <div class="bbox02" type="1" id="calframe">
                <input type="hidden" id="calDeskTopID" value="402882a031c2e8ce0131c2ee583f0023" />
                <iframe id="daycontent" src='<%=path%>/calendar/calendar!desktop.action' frameborder="0"
													scrolling="no" height="280px" width="100%" align="top"> </iframe>
                <script type="text/javascript">
					/*
					//url:'<%=path%>/desktop/desktopWhole!calendar.action',
								var rcapcontent = document.getElementById("rcapcontent");
								var ref_rcap = function(){
									$.ajax({ type:"post",
										url:'/calendar/calendar!calForDesktop.action',
										data:{},
										success:function(response){
											alert(response)
											rcapcontent.innerHTML=response;
										},
										error:function(response){
											alert("对不起，操作异常:\n"+response);
										}
								   });
								   }
					*/
					
					</script> 
              </div>
                </div>
          </dd>
            </dl>
      </div>
          <div>
          <dl>
        <dd> <br />
            </dd>
      </dl>
          <div>
          <div>
          <dl>
        <dd> <br />
            </dd>
      </dl>
          <div>
        <div class="leftbox" style="display: none">
              <dl>
            <dt class="left_title"> <a href="#" onclick="refreshWorkByTitle(this.url,'征求意见')"
															url="<%=path%>/senddoc/sendDoc!todo.action?workflowType=116153&type=sign&formId=t_oa_yjzx">征求意见</a> </dt>
            <dd class="left_con">
                  <div class="content" type="1" id="zqyjcontent"> 
                <script type="text/javascript">
								/*
								var zqyjcontent = document.getElementById("zqyjcontent");
								var ref_zqyj = function(){
									$.ajax({ type:"post",
										url:'<%=path%>/senddoc/sendDoc!getTodoByRedType.action?'+new Date(),
										data:{ type:"sign",showNum:"5",subLength:"8",workflowType:"116153" },
										success:function(response){
											if(response==""){
											}else{
						                		zqyjcontent.innerHTML=replaceMore(response);
						                	
											}
										},
										error:function(data){
											//alert("对不起，操作异常"+data);
										}
								   });
								   }
								   */
					</script> 
              </div>
                </dd>
          </dl>
            </div>
      </div></td>
      <td valign="top">
    
    <div class="right736">
          <table width="100%" height="690" cellpadding="0" cellspacing="0">
          <tr>
        
        <td valign="top"><div class="rigbox491 fle">
            <div class="bluebox">
              <div style="height: 32px;">
                <div class="bbox01 fle" url="/senddoc/sendDoc!todo.action?workflowType=3,370020,413460&type=notsign&formId=t_oarecvdoc"> 待签收文件 </div>
                <a href="#" onclick="openMore(this)"> <img src="<%=path%>/common/bgtdesktop/resource/images/index/blue_02.jpg" width="99" height="32" style="float: right;" /> </a> </div>
             <%-- <div class="bluelef fle"></div>--%>
              <dl>
                <dd class="left_con">
                  <div class="content" type="1" id="dqsycontent"> 
                    <script type="text/javascript">
						var dqsycontent = document.getElementById("dqsycontent");
						var ref_dqsy = function(){
							$.ajax({ type:"post",
									url:'<%=path%>/senddoc/sendDoc!getTodoByRedType.action?'+new Date(),
									data:{ type:"notsign",showNum:"5",subLength:"40" },
									success:function(response){
										if(response==""){
										}else{
											dqsycontent.innerHTML=replaceMore(response);
											
											//设置待签收文件数量
											var dqsySize =  $("#dqsycontent").children(".listsize").val();
											$("#dqsySize").html(dqsySize);
										}
									},
									error:function(response){
										//alert("对不起，操作异常"+data);
									}
							   });
						}
					</script> 
                  </div>
                </dd>
              </dl>
              <%--<div class="bluerig fri"></div>--%>
            </div>
            
      <%--公文登记 000200090006  登记人员的待办提醒中显示出登记类型的数据--%>
      <%String todoType = ""; %>
      <security:authorize ifAnyGranted="001-000200090006">
        <%todoType = ""; %>
      </security:authorize>
      <security:authorize ifNotGranted="001-000200090006">
        <%todoType = ",370020"; %>
      </security:authorize>
            
            <div class="midbox169" style="margin-bottom: 4px;">
              <div class="box169bar">
                <div class="bartoplef fle" 
				url="/work/work!listtodo.action?workflowType=&handleKind=&type=sign&formId=&excludeWorkflowType=116153<%=todoType%>">  <span>待办提醒  </span>
				</div>
				
                <%--<div class="bartoplef fle" url="/senddoc/sendDoc!hostedWorkflow.action"><span>在办文件</span></div>--%>
                <div class="bartoprig fri" onclick="openMore(this)"> <a href="#">更多>></a> </div>
              </div>
              <div class="midmid">
                <table align="left" style="width: 100%;">
                  <tr align="left">
                    <td width="5" valign="top" align="left"><div class="reboxlef fle"></div></td>
                    <td valign="top" align="left"><div class="reboxmid fle">
                        <dd class="left_con">
						<div class="content" type="1" id="dbsycontent"> 
							<script type="text/javascript">
										var dbsycontent = document.getElementById("dbsycontent");
										var ref_dbsy = function(){
											$.ajax({ type:"post",
												url:'<%=path%>/senddoc/sendDoc!getTodoByRedType.action?'+new Date(),
												data:{ type:"sign",showNum:"5",subLength:"70",excludeWorkflowType:"116153<%=todoType%>" },
												success:function(response){
													if(response==""){
													}else{
														dbsycontent.innerHTML=replaceMore(response);
														//设置待办文件数量
														var dbsySize =  $("#dbsycontent").children(".listsize").val();
														$("#dbsySize").html(dbsySize);
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
                      </div></td>
                    <td width="5" valign="top" align="left"><div class="reboxrig fri"></div></td>
                  </tr>
                </table>
              </div>
            </div>

			<div class="bluebox">
              <div style="height: 32px;">
                <div class="bbox01 fle" url="/work/work!listprocessed.action?state=0&workflowType=&handleKind=&filterSign=1&excludeWorkflowType=370020,413460"> <span>在办跟踪</span> </div>
                <a href="#" onclick="openMore(this)"> <img src="<%=path%>/common/bgtdesktop/resource/images/index/blue_02.jpg" width="99"
															height="32" style="float: right;" /> </a> </div>
				<%--<div class="bluelef fle"></div>--%>
              <dl>
                <dd class="left_con">
                  <div class="content" type="1" id="zbwjcontent"> 
                            <script type="text/javascript">
								var zbwjcontent = document.getElementById("zbwjcontent");
								var ref_zbwj = function(){
									$.ajax({ type:"post",
											//url:'<%=path%>/senddoc/sendDocDesktop!showDesktopHostByWork.action?'+new Date(),
											url:'<%=path%>/senddoc/sendDoc!showDesktopDoneWork.action?'+new Date(),
											data:{ processStatus:"0",showCreator:"1",showNum:"5",subLength:"70",filterSign:"1",excludeWorkflowType:"370020,413460"},
											success:function(response){
												if(response==""){
												}else{
													var zbwjcontentHtml = response.replace("<div class=\"select\">","<div class=\"select\" style=\"display:none\">")
													zbwjcontent.innerHTML = replaceMore(zbwjcontentHtml);
													
													//设置在办文件数量
													var zbwjSize =  $("#zbwjcontent").children(".listsize").val();
													$("#zbwjSize").html(zbwjSize);
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
              <%--<div class="bluerig fri"></div>--%>
            </div>

            <div class="midbox169">
              <div class="box169bar">
                <div class="bartoplef fle" url="/work/work!listprocessed.action?state=1&workflowType=&handleKind=&filterSign=1&excludeWorkflowType=370020"> <span>办结事宜</span> </div>
                <div class="bartoprig fri" onclick="openMore(this)"> <a href="#">更多>></a> </div>
              </div>
              <div class="midmid">
                <table align="left" style="width: 100%;">
                  <tr align="left">
                    <td width="5" valign="top" align="left"><div class="reboxlef fle"></div></td>
                    <td valign="top" align="left"><div class="reboxmid fle">
                        <dl>
                          <dd class="left_con">
                            <div class="content" type="1" id="bjwjcontent" > 
                              <script type="text/javascript">
									var bjwjcontent = document.getElementById("bjwjcontent");
									var ref_bjwj = function(){
										$.ajax({ type:"post",
												url:'<%=path%>/senddoc/sendDoc!showDesktopDoneWork.action?'+new Date(),
												data:{ processStatus:"1",showNum:"5",subLength:"70",filterSign:"1",showDate:"1",excludeWorkflowType:"370020"},
												success:function(response){
													if(response==""){
													}else{
								                		bjwjcontent.innerHTML = replaceMore(response);
								                		
									                	//设置在办文件数量
								                		var bjwjSize =  $("#bjwjcontent").children(".listsize").val();
								                		$("#bjwjSize").html(bjwjSize);
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
                      </div></td>
                    <td width="5" valign="top" align="left"><div class="reboxrig fri"></div></td>
                  </tr>
                </table>
              </div>
            </div>
          </div></td>
          <td width="237" valign="top">
        
        <div class="rig237 fri">
              <div class="rightbox">
            <div class="rightboxbar"> 重要文件跟踪 <a style="margin-left: 20px;" href="#" onclick="refreshWorkByTitle('<%=path%>/traceDoc/traceDoc.action','重要文件跟踪')">更多</a> </div>
            <div>
                  <dl>
                <dd class="left_con">
                      <div class="bbox02" type="1" id="zywjcontent"> 
                    <script type="text/javascript">
								var zywjcontent = document.getElementById("zywjcontent");
								var ref_zywj = function(){
									$.ajax({ type:"post",
											url:'<%=path%>/traceDoc/traceDoc!showDesktop.action?'+new Date(),
											data:{ showNum:"5",subLength:"13" },
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
         </div>
			<div class="rightbox rightbox2 rightbox3">
				<div class="rightboxbar rightboxbar2">
					<span class="rtbxsping">我的办文&nbsp;</span>
					<%if(!(rest1).equals("0")){ %>
					<span>公文统计</span>
					<%} %>
					<%--<span style="display:none;">办理统计</span>--%>
				</div>
            	<div>
                  <dl>
	                <dd id="bltj" style="display: none;">
	                      <div class="left_con">
	                    <table>
                          <tr>
	                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=zbURL%>','正在办理的收文')">正在办理的收文<strong style="color:red" id="total">0</strong>件</a></td>
	                        </td>
                          </tr>
                          <tr>
	                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=yqwbjURL%>','逾期未办结的收文')">逾期未办结的收文<strong style="color:red" id="timeouttotal">0</strong>件</a></td>
	                     	</td>
                           </tr>
	                       <tr>
	                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/documentHandle!limitTime.action','公文限时办理情况')">收文限时办理情况 </td>
	                        </td>
	                       </tr>
	                    </table>
	                  </div>
	               </dd>
				  <dd id="gwjkytj" style="display: none;">
					<div class="left_con">
						<table>
							<tr>
								<td>
									<img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" />
									<a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person&mytype=dept&state=0&excludeWorkflowType=370020&privilSyscode=000200140001','在办文件')">在办文件<strong id="doingCount" style="color:red">0</strong>件</a>
								</td>
								</td>
							</tr>
							<tr>
								<td>
									<img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" />
									<a href="#" onclick="refreshWorkByTitle('<%=path%>/bgt/documentview/documentView!mainFrameOrg.action?mytype=dept','待办文件')">待办文件<strong id="todoCount" style="color:red">0</strong>件</a>
								</td>
								</td>
							</tr>
							<tr>
								<td>
									<img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" />
									<a href="#" onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person&mytype=dept&state=1&excludeWorkflowType=370020&privilSyscode=000200140002','办结文件')">办结文件<strong id="doneCount" style="color:red">0</strong>件</a>
								</td>
								</td>
							</tr>
              				<%-- 
							<tr>
								<td>
									<img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" />
									<a href="#"
										onclick="refreshWorkByTitle('<%=path%>/workflowDesign/action/documentHandle!limitTime.action','公文限时办理情况')">公文限时办理情况
								</td>
								</td>
							</tr>
							--%>
						</table>
					</div>
				</dd>
                <dd id="wdbw">
                      <div> </div>
                      <div class="left_con"> 
                    <script type="text/javascript">
						var ref_showWorkTotal = function(){
						   $.ajax({ type:"post",
								url:'<%=path%>/senddoc/sendDocDesktop!showWorkTotal.action?'+new Date(),
								data:{},
								success:function(response){
									if(response==""){
									}else{
										//signSize:9;notsignSize:6;endSize:11;noendSize:18
										var workGroup = response.split(";");
										var dbsySize = workGroup[0].split(":")[1];
										var dqsySize = workGroup[1].split(":")[1];
										var bjwjSize = workGroup[2].split(":")[1];
										var zbwjSize = workGroup[3].split(":")[1];
										$("#dqsySize").html(dqsySize);//待签收文件
										$("#dbsySize").html(dbsySize);//待办文件
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
                    <table>
                          <tr>
                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todo.action?workflowType=3,370020,413460&type=notsign&formId=t_oarecvdoc','待签收文件')">待签收文件<strong style="color:red" id="dqsySize">0</strong>件</a></td>
                      </tr>
                          <tr>
                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!todoWorkflow.action?excludeWorkflowType=116153,370020&type=sign','待办文件')">待办文件<strong style="color:red" id="dbsySize">0</strong>件</a></td>
                      </tr>
                          <tr>
                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!processedWorkflow.action?state=0&filterSign=1&excludeWorkflowType=370020,413460','在办文件')">在办文件<strong style="color:red" id="zbwjSize">0</strong>件</a></td>
                      </tr>
                          <tr>
                        <td><img src="<%=path%>/common/bgtdesktop/resource/images/index/nymtdicon.gif" /> <a href="#" onclick="refreshWorkByTitle('<%=path%>/senddoc/sendDoc!processedWorkflow.action?state=1&filterSign=1&excludeWorkflowType=370020','办结文件')">办结文件<strong style="color:red" id="bjwjSize">0</strong>件</a></td>
                      </tr>
                        </table>
                  </div>
                    </dd>
              </dl>
                </div>
            <div></div>
          </div>
              <script type="text/javascript">
				 var ref_bwtj = function(){
					var rest1 = "<%=rest1%>";
					if(rest1 != "0"){
							$(".rightboxbar2 span").eq(0).html("我的办文&nbsp;");
							$.post("<%=path%>/work/work!showProcessedTotalCount.action",
									{},
									function(data){
										if(data!=""){
											var total = data.split(";")[0];
											$("#total").html(total.split(":")[1]);
											var timeouttotal = data.split(";")[1];
											$("#timeouttotal").html(timeouttotal.split(":")[1]);
										}
									});
							$(".rightboxbar2 span").eq(1).show();
					}else{
					  $("#wdbw").show();
					}	
				 }
				 var ref_gwjkytj = function(){
			 			$.ajax({ type:"post",
			 					url:'<%=path%>/bgt/documentview/documentView!getDoingDocCout.action?'+new Date(),
			 					data:{ mytype:"dept",excludeWorkflowType:"370020"},
			 	 				async:false,
			 					success:function(response){
			 						if(response==""){
			 						}else{
										$("#doingCount").html(response);
			 						}
			 					},
			 					error:function(data){
			 						//alert("对不起，操作异常"+data);
			 					}
			 			   });
			 			$.ajax({ type:"post",
			 				url:'<%=path%>/bgt/documentview/documentView!getTodoDocCount.action?'+new Date(),
			 				data:{ mytype:"dept"},
			 				async:false,
			 				success:function(response){
			 					if(response==""){
			 					}else{
			 						$("#todoCount").html(response);
			 					}
			 				},
			 				error:function(data){
			 					//alert("对不起，操作异常"+data);
			 				}
			 		   });
			 			$.ajax({ type:"post",
			 				url:'<%=path%>/bgt/documentview/documentView!getDoneDocCount.action?'+new Date(),
			 				data:{ mytype:"dept",excludeWorkflowType:"370020"},
			 				async:false,
			 				success:function(response){
			 					if(response==""){
			 					}else{
			 						$("#doneCount").html(response);
			 					}
			 				},
			 				error:function(data){
			 					//alert("对不起，操作异常"+data);
			 				}
			 		   });
			 		}
            </script>
              <div class="rightbox">
            <div class="rightboxbar"> 投票调查 <a href="#" onclick="refreshWorkByTitle('<%=path%>/vote/vote!list.action','投票调查')">更多</a> </div>
            <div>
                  <dl>
                <dd class="left_con">
                      <div class="content" type="1" id="tpdccontent"> 
                    <script type="text/javascript">
							var tpdccontent = document.getElementById("tpdccontent");
							var ref_tpdc = function(){
								$.ajax({ type:"post",
										url:'<%=path%>/vote/vote!showDesktop.action?' + new Date(),
									data : {
										showNum : "5",
										subLength : "15"
									},
									success : function(response) {
										if (response == "") {
										} else {
											tpdccontent.innerHTML = replaceMore(response);
										}
									},
									error : function(data) {
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
            </div>
          </td>
          </tr>
      </table>
        </div>
      </td>
      </tr>
  </table>
    </div>
<div class="bottom"> 版权所有：南昌市人民政府办公厅综合协同办公平台 <br />
      技术支持：思创数码科技股份有限公司 <br />
      建议使用IE7.0及以上版本 建议分辨率 1280*800或以上 </div>
<script>
	function reLoadDate() {
		//ref_rcap(); 
		//ref_showWorkTotal();
		ref_dqsy();
		ref_dbsy();
		ref_zbwj();
		ref_bjwj();
		ref_zywj();
		ref_gwjkytj();
		ref_tpdc();
		ref_bwtj();
		//ref_zqyj();
	}
	reLoadDate();
	setInterval("reLoadDate()", 3600 * 1000);
</script>
</body>
</html>

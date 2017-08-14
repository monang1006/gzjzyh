<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>   
   <title>无标题文档</title>
   <% String path = request.getContextPath(); %>
	<link href="<%=path %>/leaderStat4documents/css/dmain.css" type="text/css" rel="stylesheet" />
	<script language="javascript" src="<%=path %>/leaderStat4documents/js/jquery.js"></script>
	<style type="text/css">
	div.UserCard {
			position: absolute;
			z-index: 999;
			width: 270px;
			height: 143px;
		}
		</style>
	<script language="javascript">
	$(function(){
		$("#sendChartdiv1").show();
		$("#processedChartdiv1").show();
		$("#processingChartdiv").show();
		leaderManageOrgs();
		
	    var Mtop = $(".mtop dt span");
		Mtop.mouseover(function(){
		  var wi = Mtop.index(this);
		  $(this).addClass("mtopsping").siblings("span").removeClass("mtopsping");
		  $(".mtop dd div").eq(wi).show().siblings("div").hide();
		});
		
		var dm02 = $(".dm02 dt div span");
		dm02.mouseover(function(){
			var wi = dm02.index(this);
			$(this).addClass("dm02sping").siblings("span").removeClass("dm02sping");
			$(".dm02 dd div").eq(wi).show().siblings("div").hide();
		});
		
		var dmrid03 = $(".dmrid03 dd th p");
		dmrid03.mouseover(function(){
			var wi = dmrid03.index(this);
			$(".dmrid03 dd th .dmridping").removeClass("dmridping");
			$(this).addClass("dmridping");
			$(".dmrid03 dd div").eq(wi).show().siblings("div").hide();
		});
		
		var dm03 = $(".dm03 dt div span");
		dm03.mouseover(function(){
			var wi = dm03.index(this);
			$(this).addClass("dm02sping").siblings("span").removeClass("dm02sping");
			$(".dm03 dd div").eq(wi).show().siblings("div").hide();
		});
		
		var dmrid04 = $(".dmrid04 dd th p");
		dmrid04.mouseover(function(){
			var wi = dmrid04.index(this);
			$(".dmrid04 dd th .dmridping").removeClass("dmridping");
			$(this).addClass("dmridping");
			$(".dmrid04 dd div").eq(wi).show().siblings("div").hide();
		});
		
		var dmrid05 = $(".dmrid05 dd th p");
		dmrid05.mouseover(function(){
			var wi = dmrid05.index(this);
			$(".dmrid05 dd th .dmridping").removeClass("dmridping");
			$(this).addClass("dmridping");
			$(".dmrid05 dd div").eq(wi).show().siblings("div").hide();
		});
		
		getReceiveCount();
		getProcessedCount();
		getProcessingCount();
		sendDocRegistSta();
	  });
	  
	  //收文登记统计
	  var getReceiveCount = function(){
	  	$.ajax({ type:"post",
 					url:'<%=path%>/leaderStat4documents/leaderStat4documents!getReceiveCount.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
							var strs = new Array();
							strs = response.split(",");
							for(i=0; i<strs.length; i++){
								var strs2 = new Array();
								strs2 = strs[i].toString().split("-");								
								$("#receive"+i.toString()).html(strs2[0]);
								var j = i+4;
								$("#receive"+j.toString()).html(strs2[1]);
								var m = j+4;
								var n = parseInt(strs2[0]) + parseInt(strs2[1]);
								$("#receive"+m.toString()).html(n);
								if(i==1){
									$("#swdj").html(strs2[0]);
									$("#yfb").html(strs2[1]);
								}
							}
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
	  }
	  
	  //办结文件统计
	  var getProcessedCount = function(){
	  	$.ajax({ type:"post",
 					url:'<%=path%>/leaderStat4documents/leaderStat4documents!getProcessedCount.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
							var strs = new Array();
							strs = response.split(",");
							for(i=0; i<strs.length; i++){
								var strs2 = new Array();
								strs2 = strs[i].split("-");								
								$("#processed"+i.toString()).html(strs2[0]);
								var j = i+4;
								$("#processed"+j.toString()).html(strs2[1]);
								var k = j+4;
								$("#processed"+k.toString()).html(strs2[2]);
								var m = k+4;
								var n = parseInt(strs2[0]) + parseInt(strs2[1]) + parseInt(strs2[2]);
								$("#processed"+m.toString()).html(n);
								if(i==1){
									$("#bjwj").html(n);
									$("#bj0").html(strs2[0]);
									$("#bj1").html(strs2[1]);
									$("#bj2").html(strs2[2]);
								}
							}
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
	  }
	  
	  //在办文件统计
	  var getProcessingCount = function(){
	  	$.ajax({ type:"post",
 					url:'<%=path%>/leaderStat4documents/leaderStat4documents!getProcessingCount.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
							var strs = new Array();
							strs = response.split("-");
							var m = 0;
							for(i=0; i<strs.length; i++){							
								$("#processing"+i.toString()).html(strs[i]);
								m = m + parseInt(strs[i]);
								$("#zb"+i.toString()).html(strs[i]);				
							}
							$("#processing3").html(m);
							$("#zbzs").html(m);
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
	  }
	  
	  //发文登记统计
	  var sendDocRegistSta = function(){
	  	$.ajax({ type:"post",
 					url:'<%=path%>/senddocRegistSta/sendDocRegistSta!getRegistDocsNum.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
							var strs = new Array();
							strs = response.split(";");
							for(i=0; i<strs.length; i++){							
								$("#sendRegist"+i.toString()).html(strs[i]);
								var j = i+4;
								$("#sendRegist"+j.toString()).html(strs[i]);
								if(i == 1){
									$("#fwdj").html(strs[i]);
								}						
							}							
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
	  
	   //发文登记统计(柱状图)
	   $.ajax({ type:"post",
 					url:'<%=path%>/senddocRegistSta/sendDocRegistSta!wjtj4ajax.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
 							var strs = new Array();
 							strs = response.split("-");
 							for(i=0; i<strs.length; i++){
 								var chart = new FusionCharts("<%=path%>/common/js/FusionCharts/Charts/MSColumn2D.swf", "ChartId", "100%", "100%", "0", "0");					
								chart.setDataXML(strs[i]);
	   							chart.render("sendChartdiv"+i.toString());
							}						
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
	   //办结统计(柱状图)
	   $.ajax({ type:"post",
 					url:'<%=path%>/leaderStat4documents/leaderStat4documents!getProcessedCountByOrg.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
 							var strs = new Array();
 							strs = response.split("-");
 							for(i=0; i<strs.length; i++){
 								var chart = new FusionCharts("<%=path%>/common/js/FusionCharts/Charts/MSColumn2D.swf", "ChartId", "100%", "100%", "0", "0");					
								chart.setDataXML(strs[i]);
	   							chart.render("processedChartdiv"+i.toString());
							}						
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
 		//在办统计(柱状图)
 		 $.ajax({ type:"post",
 					url:'<%=path%>/leaderStat4documents/leaderStat4documents!getProcessingCountByOrg.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
							var chart = new FusionCharts("<%=path%>/common/js/FusionCharts/Charts/MSColumn2D.swf", "ChartId", "100%", "100%", "0", "0");					
							chart.setDataXML(response);
   							chart.render("processingChartdiv");					
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					}
 			   });
	   
	  }
	  
	  //领导首页顶部 所分管处室文件统计
	  var leaderManageOrgs = function(){
	  	$.ajax({ type:"post",
 					url:'<%=path%>/leaderStat4documents/leaderStat4documents!getLeaderManageOrgs.action',
 	 				async:false,
 					success:function(response){
 						if(response==""){
 						}else{
							var strs = new Array();
							strs = response.split(";"); // 以";"分割为各处室统计数据
							if(strs!=null){
								var recvDoc = encodeURI(encodeURI(encodeURI("收文")));
								var selfDoc = encodeURI(encodeURI(encodeURI("办文")));
								var sendDoc = encodeURI(encodeURI(encodeURI("发文")));
								var pageHeadTitel = "<span class='mtopsping'>[";
								var pageHeadContent = "<div>";
								for(i=0; i<strs.length; i++){
									var strs2 = new Array();
									strs2 = strs[i].split(",");	//strs2[0]为处室名称 
									var roomName = encodeURI(encodeURI(encodeURI(strs2[0].toString())));
									if(i==0){
										pageHeadTitel = pageHeadTitel + strs2[0] + "]公文办理情况统计</span>";
									}else{
										pageHeadTitel = pageHeadTitel + "<span>[" + strs2[0] + "]公文办理情况统计</span>";
										pageHeadContent = pageHeadContent + "<div style='display:none'>"
									}
									var strs3 = new Array();
									strs3 = strs2[1].split("-");	//收文登记统计
									var waitDoc = parseInt(strs3[0])-parseInt(strs3[1]);
									var strs4 = new Array();
									strs4 = strs2[2].split("-");	//办结文件统计
									var totalStrs4 = parseInt(strs4[0]) + parseInt(strs4[1]) + parseInt(strs4[2]);
									var strs5 = new Array();
									strs5 = strs2[3].split("-");	//在办文件统计
									var totalStrs5 = parseInt(strs5[0]) + parseInt(strs5[1]) + parseInt(strs5[2]);
									var strs6 = new Array();
									strs6 = strs2[4].split("-");	//发文登记统计										
									pageHeadContent =  pageHeadContent +
								//						"<p>1、本周收文登记文件总数<span>"+strs3[0]+"</span>件，已分办文件<span><a href=\"javascript:openMore('<%=path%>/senddoc/sendDoc!processed.action?state=2&workflowType=370020,413460&formId=t_oarecvdoc&showSignUserInfo=1','已分办文件');\">"+strs3[1]+"</a></span>件；</p>"+
														"<p>1、上周已分办文件<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=0&excludeWorkflowType=370020&privilSyscode=000200140001&fromLeaderDesktopPage=1&isFenban=1&roomId="+strs2[5]+"','已分办文件');\"><font color='red'>"+strs3[1]+
														  "</font></a></span>件,待签收文件<span><a href=\"javascript:openMore('<%=path%>/senddoc/sendDoc!todo.action?workflowType=3,370020,413460&type=notsign&formId=t_oarecvdoc&fromLeaderDesktopPage=1&roomId="+strs2[5]+"','待签收文件');\"><font color='red'>"+strs3[2]+"</font></a></span>件；</p>"+
												          "<p>2、上周办结文件总数<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=1&doneYear=-1&excludeWorkflowType=370020&privilSyscode=000200140002&fromLeaderDesktopPage=1&roomId="+strs2[5]+"','办结文件');\"><font color='red'>"+totalStrs4+
												          "</font></a></span>件，其中自办文<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=1&doneYear=-1&excludeWorkflowType=370020&privilSyscode=000200140002&fromLeaderDesktopPage=1&processWorkflowNames="+selfDoc+"&roomId="+strs2[5]+"','办结自办文');\"><font color='red'>"+strs4[0]+
												          "</font></a></span>件，收文<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=1&doneYear=-1&excludeWorkflowType=370020&privilSyscode=000200140002&fromLeaderDesktopPage=1&processWorkflowNames="+recvDoc+"&roomId="+strs2[5]+"','办结收文');\"><font color='red'>"+strs4[1]+
												          "</font></a></span>件，发文<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=1&doneYear=-1&excludeWorkflowType=370020&privilSyscode=000200140002&fromLeaderDesktopPage=1&processWorkflowNames="+sendDoc+"&roomId="+strs2[5]+"','办结发文');\"><font color='red'>"+strs4[2]+
												          "</font></a></span>件；</p>"+
												          "<p>3、上周编号发文总数<span><a href=\"javascript:openMore('<%=path%>/senddocRegist/sendDocRegist.action?type=search&searchRoom="+roomName+"&fromLeaderDesktopPage=1&noDocCode=0','编号发文');\"><font color='red'>"+strs6[0]+
												          "</font></a></span>件，未编号发文总数<span><a href=\"javascript:openMore('<%=path%>/senddocRegist/sendDocRegist.action?type=search&searchRoom="+roomName+"&fromLeaderDesktopPage=1&noDocCode=1','未编号发文');\"><font color='red'>"+strs6[1]+
												          "</font></a></span>件；</p>"+
												          "<p>4、当前在办文件总数<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=0&excludeWorkflowType=370020&privilSyscode=000200140001&fromLeaderDesktopPage=1&roomId="+strs2[5]+"','在办文件');\"><font color='red'>"+totalStrs5+
												          "</font></a></span>件，其中自办文<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=0&excludeWorkflowType=370020&privilSyscode=000200140001&processWorkflowNames="+selfDoc+"&fromLeaderDesktopPage=1&roomId="+strs2[5]+"','在办自办文');\"><font color='red'>"+strs5[0]+
												          "</font></a></span>件，收文<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=0&excludeWorkflowType=370020&privilSyscode=000200140001&processWorkflowNames="+recvDoc+"&fromLeaderDesktopPage=1&roomId="+strs2[5]+"','在办收文');\"><font color='red'>"+strs5[1]+
												          "</font></a></span>件，发文<span><a href=\"javascript:openMore('<%=path%>/workflowDesign/action/processMonitor!monitorList.action?model=person&mytype=dept&state=0&excludeWorkflowType=370020&privilSyscode=000200140001&processWorkflowNames="+sendDoc+"&fromLeaderDesktopPage=1&roomId="+strs2[5]+"','在办发文');\"><font color='red'>"+strs5[2]+
												          "</font></a></span>件。</p>"+
       													 "</div>";

								}
						//		pageHeadTitel = pageHeadTitel + "<span>全厅公文办理情况统计</span>";
								$("#pageHeadTitel").html(pageHeadTitel);
						//		pageHeadContent = pageHeadContent + "<div style='display:none'>"+
						//						          "<p>1、上周收文登记件<span id='swdj'>0</span>件，已分办文件<span id='yfb'>0</span>件；</p>"+
						//						          "<p>2、上周办结文件总数<span id='bjwj'>0</span>件，其中自办文<span id='bj0'>0</span>件，收文<span id='bj1'>0</span>件，发文<span id='bj2'>0</span>件；</p>"+
						//						          "<p>3、上周发文登记文件总数<span id='fwdj'>0</span>件；</p>"+
						//						          "<p>4、当前在办文件总数<span id='zbzs'>0</span>件，其中自办文<span id='zb0'>0</span>件，收文<span id='zb1'>0</span>件，发文<span id='zb2'>0</span>件。</p>"+
						//						        "</div>"
								$("#pageHeadContent").html(pageHeadContent);
							}
 						}
 					},
 					error:function(data){
 						//alert("对不起，操作异常"+data);
 					//	$("#pageHeadTitel").html("<span class='mtopsping'>全厅公文办理情况统计</span>");
					//			$("#pageHeadContent").html("<div>"+
					//							          "<p>1、上周收文登记件<span id='swdj'>0</span>件，已分办文件<span id='yfb'>0</span>件；</p>"+
					//							          "<p>2、上周办结文件总数<span id='bjwj'>0</span>件，其中自办文<span id='bj0'>0</span>件，收文<span id='bj1'>0</span>件，发文<span id='bj2'>0</span>件；</p>"+
					//							          "<p>3、上周发文登记文件总数<span id='fwdj'>0</span>件；</p>"+
					//							          "<p>4、当前在办文件总数<span id='zbzs'>0</span>件，其中自办文<span id='zb0'>0</span>件，收文<span id='zb1'>0</span>件，发文<span id='zb2'>0</span>件。</p>"+
					//							        "</div>");
 					}
 			   });
	  }
	  //去除多余的“更多”链接
		function replaceMore(html){
//			return html.replace("更多</a>","</a>");
			return html.replace("<IMG SRC=\"<%=path%>/oa/image/more.gif\" BORDER=\"0\" /></a>","</a>");
		}
	  	//日程模块-万年历调用的方法
		function mouseoverToShow(date,element,evt){
			var calID = "drag_"+document.getElementById("calDeskTopID").value;
			var calbody = document.getElementById(calID);
			var contentborder = document.getElementById("contentborder");
			
			//获取日程元素在页面中的坐标
			/*   
			*/   
			  var x=calbody.offsetLeft-20;   
			  var y=calbody.offsetTop-470;   
			  var objParent=calbody.parentNode; 
			  while(objParent.tagName.toUpperCase()!= "BODY"){  
				  x+=objParent.offsetLeft;   
				  y+=objParent.offsetTop;   
				  objParent = objParent.parentNode;   
			  } 
			  /*   
			 var x=calbody.offsetLeft;   
			 var y=calbody.offsetTop; 
			alert("鼠标在模块中的坐标：\n mosP.x="+mosP.x+"\n mosP.y="+mosP.y
					+"\n left="+(parseInt(x)+parseInt(mosP.x)-270)
					+"\n top="+(parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop)));
			*/ 
			var mosP = mouseCoords(evt);
			
			if((parseInt(x)+parseInt(mosP.x))>parseInt(document.body.clientWidth)){
				$('#detail').css('left',(parseInt(x)+parseInt(mosP.x)-270));
				$('#detail').css('top',(parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop)));
				$('#detail').css('background',"url('<%=path%>/oa/image/calendar/calendar_destop_bgL.GIF') no-repeat");
				$('#detailFrame').attr("src","<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date);
			}else{
				$('#detail').css('left',parseInt(x)+parseInt(mosP.x));
				$('#detail').css('top',parseInt(y)+parseInt(mosP.y)-parseInt(contentborder.scrollTop));
				$('#detail').css('background',"url('<%=path%>/oa/image/calendar/calendar_destop_bgR.GIF') no-repeat");
				$('#detailFrame').attr("src","<%=path%>/calendar/calendar!showDesktopDetail.action?model.calStartTime="+date);
				
			}
			//alert($('#detail').attr("style"))
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
		
		function refreshWorkByTitle(url,title){
			window.parent.refreshWorkByTitle(url,title)
		}
		
		function openMore(url,title){
			window.parent.refreshWorkByTitle(url,title)
		}
	</script>
  </head>
  <body  scroll="yes" onresize="detail.style.display='none';">
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
<div id="contentborder" onscroll="detail.style.display='none';">
<div id="dcon">

  <div class="dmain clearfix">
    <table width="100%">
      <tr>
        <td valign="top">
            <div class="mtop">
    <dl>
      <dt id="pageHeadTitel">      

      </dt>
      <dd id="pageHeadContent">

      </dd>
    </dl>
  </div>
          <div class="dmlef">
      <div class="dm01 clearfix">
        <dl>
          <dt><b class="dtmore"><div onclick="openMore('<%=path %>/work/work!listtodo.action?workflowType=&handleKind=&type=sign&formId=&excludeWorkflowType=116153','待办提醒')"> <a href="#">更多>></a> </div></b><span>待办提醒</span></dt>
          <dd id='dbsycontent'>
            <script type="text/javascript">
								var dbsycontent = document.getElementById("dbsycontent");
								var ref_dbsy = function(){
									$.ajax({ type:"post",
										url:'<%=path%>/senddoc/sendDoc!getTodoByRedType.action?'+new Date(),
										data:{ type:"sign",showNum:"5",subLfength:"30" },
										success:function(response){
											if(response==""){
											}else{
						                		//dbsycontent.innerHTML=response;
						                		dbsycontent.innerHTML=replaceMore(response);
						                	
						                		//设置待办文件数量
						                		//var dbsySize =  $("#dbsycontent").children(".listsize").val();
						                		//$("#dbsySize").html(dbsySize);
											}
										},
										error:function(data){
											//alert("对不起，操作异常"+data);
										}
								   });
								   }
								   ref_dbsy(); 
					</script>
          </dd>
        </dl>
      </div>
    </div>
        </td>
        <td width="12">&nbsp;</td>
        <td width="237" valign="top">
          <div class="dmrig">
      <div class="dmrid01" align="center"><a href="#"><img src="<%=path%>/leaderStat4documents/images/dzfimg02.jpg" 
      										onclick="refreshWorkByTitle('<%=path%>/myinfo/myInfo!inputWorkEntrust.action','工作委托')"/></a></div>
      <div class="dmrid01" align="center"><a href="#"><img src="<%=path%>/leaderStat4documents/images/dzfimg002.jpg" 
      										onclick="refreshWorkByTitle('<%=path%>/fileNameRedirectAction.action?toPage=desktop/desktopWhole-bgtDesktopForLeader3.jsp','全厅公文统计')"/></a></div>
      <div class="dmrid02">
        <dl>
          <dt>日程安排</dt>
          <dd> 
          <div id="drag_402882a031c2e8ce0131c2ee583f0023">
			<div type="1" id="calframe">
				<input type="hidden" id="calDeskTopID" value="402882a031c2e8ce0131c2ee583f0023"/>
				<iframe id="daycontent" src='<%=path%>/calendar/calendar!desktop.action' frameborder="0" scrolling="no" height="285px" width="225px" align="top" >
				</iframe>
			</div>
          </div>
          </dd>
        </dl>
      </div>
    </div>
        </td>
      </tr>
    </table>   
  </div>
</div>
</div>
</body>
</html>
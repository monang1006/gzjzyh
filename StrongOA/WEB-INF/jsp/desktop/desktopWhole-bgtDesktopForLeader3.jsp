<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>   
   <title>无标题文档</title>
   <% String path = request.getContextPath(); %>
	<link href="<%=path %>/leaderStat4documents/css/dmain.css" type="text/css" rel="stylesheet" />
	<script language="javascript" src="<%=path %>/leaderStat4documents/js/jquery.js"></script>
	<script language="JavaScript" src="<%=path %>/common/js/FusionCharts/JSClass/FusionCharts.js"></script>
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
	//	$("#sendChartdiv1").show();
	//	$("#processedChartdiv1").show();
	//	$("#processingChartdiv").show();
		
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
								var n = parseInt(strs2[0]) - parseInt(strs2[1]);						
								$("#receive"+i.toString()).html(strs2[1]);
								var j = i+4;
								$("#receive"+j.toString()).html(n);
								var m = j+4;								
								$("#receive"+m.toString()).html(strs2[0]);
								if(i==1){
									$("#swdj").html(strs2[1]);
									$("#yfb").html(n);
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
								var strs2 = new Array();
								strs2 = strs[i].split("-");					
								$("#sendRegist"+i.toString()).html(strs2[0]);
								var j = i+4;
								$("#sendRegist"+j.toString()).html(strs2[1]);
								var m = j+4;
								var n = parseInt(strs2[0]) + parseInt(strs2[1]);
								$("#sendRegist"+m.toString()).html(n);
								if(i == 1){
									$("#fwdj").html(n);
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
	  		
	</script>	
  </head>
  <body scroll="yes">
  	<div id="dcon">
  		<div class="dmain clearfix">
  			<table width="100%">
  				<tr>
  					<td valign="top">
  						<div class="dmlef">
  							<div class="dm02">
					        <dl class="dmdls">
					          <dt><div><span>本日</span><span class="dm02sping">上周</span><span>本月</span><span>本年</span></div><b>办结文件统计</b></dt>
					          <dd>
					          	<div style="display:none" id="processedChartdiv0"  align="center">
								</div>
								<div  id="processedChartdiv1"  align="center">
								</div>
								<div style="display:none" id="processedChartdiv2"  align="center">
								</div>
								<div style="display:none" id="processedChartdiv3"  align="center">
							  </dd>
					        </dl>
					      </div>
					      <div class="dm03">
					        <dl class="dmdls">
					          <dt><div><span>本日</span><span class="dm02sping">上周</span><span>本月</span><span>本年</span></div><b>发文登记统计</b></dt>
					          <dd>
					          	<div style="display:none" id="sendChartdiv0"  align="center">
								</div>
								<div  id="sendChartdiv1"  align="center">
								</div>
								<div style="display:none" id="sendChartdiv2"  align="center">
								</div>
								<div style="display:none" id="sendChartdiv3"  align="center">
								</div>
							  </dd>
					        </dl>
					      </div>
					      <div class="dm04">
					        <dl class="dmdls">
					          <dt><div></div><b>在办文件统计</b></dt>
					          <dd>
					          	<div  id="processingChartdiv"  align="center">
								</div>
					          </dd>
					        </dl>
					      </div>
  						</div>
  					</td>
  					<td width="12">&nbsp;</td>
  					<td width="237" valign="top">
  						<div class="dmrig">
  							<div class="dmrid03">
						        <dl class="dmridls">
						          <dt>收文登记统计</dt>
						          <dd>
						            <table width="100%">
						              <tr>
						                <th><p>本日</p></th>
						                <th><p class="dmridping">上周</p></th>
						                <th><p>本月</p></th>
						                <th><p>本年</p></th>
						              </tr>
						            </table>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已分发的文件<span id="receive0">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 待签收的文件<span id="receive4">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 退回的文件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="receive8">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div>
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已分发的文件<span id="receive1">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 待签收的文件<span id="receive5">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 退回的文件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="receive9">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已分发的文件<span id="receive2">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 待签收的文件<span id="receive6">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 退回的文件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="receive10">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已分发的文件<span id="receive3">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 待签收的文件<span id="receive7">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 退回的文件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="receive11">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						          </dd>
						        </dl>
						      </div>
						      <div class="dmrid04">
						        <dl class="dmridls">
						          <dt>办结文件统计</dt>
						          <dd>
						            <table width="100%">
						              <tr>
						                <th><p>本日</p></th>
						                <th><p class="dmridping">上周</p></th>
						                <th><p>本月</p></th>
						                <th><p>本年</p></th>
						              </tr>
						            </table>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 自办文<span id="processed0">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 收文<span id="processed4">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 发文<span id="processed8">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon04.gif" /> 呈阅件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="processed12">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div>
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 自办文<span id="processed1">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 收文<span id="processed5">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 发文<span id="processed9">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon04.gif" /> 呈阅件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="processed13">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 自办文<span id="processed2">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 收文<span id="processed6">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 发文<span id="processed10">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon04.gif" /> 呈阅件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="processed14">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 自办文<span id="processed3">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 收文<span id="processed7">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 发文<span id="processed11">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon04.gif" /> 呈阅件<span></span></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="processed15">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						          </dd>
						        </dl>
						      </div>
						      <div class="dmrid05">
						        <dl class="dmridls">
						          <dt>发文登记统计</dt>
						          <dd>
						            <table width="100%">
						              <tr>
						                <th><p>本日</p></th>
						                <th><p class="dmridping">上周</p></th>
						                <th><p>本月</p></th>
						                <th><p>本年</p></th>
						              </tr>
						            </table>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已编号发文<span id="sendRegist0">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 未编号发文<span id="sendRegist4">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="sendRegist8">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div>
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已编号发文<span id="sendRegist1">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 未编号发文<span id="sendRegist5">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="sendRegist9">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已编号发文<span id="sendRegist2">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 未编号发文<span id="sendRegist6">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="sendRegist10">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						            <div style="display:none">
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 已编号发文<span id="sendRegist3">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 未编号发文<span id="sendRegist7">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p></p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="sendRegist11">0</span></p></td>
						                </tr>
						              </table>
						            </div>
						          </dd>
						        </dl>
						      </div>
						      <div class="dmrid06">
						        <dl class="dmridls">
						          <dt>在办文件统计</dt>
						          <dd>
						            <div>
						              <table width="100%">
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon01.gif" /> 自办文<span id="processing0">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon02.gif" /> 收件<span id="processing1">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon03.gif" /> 发件<span id="processing2">0</span></p></td>
						                </tr>
						                <tr>
						                  <td><p><img src="<%=path%>/leaderStat4documents/images/dicon04.gif" /> 呈阅件</p></td>
						                </tr>
						                <tr>
						                  <td><p class="dmrdsplast">总计：<span id="processing3">0</span></p></td>
						                </tr>
						              </table>
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
  </body>
</html>

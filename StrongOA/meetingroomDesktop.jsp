<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
	<%@ taglib uri="/tags/c.tld" prefix="c"%>
	<%@include file="/common/include/rootPath.jsp"%>
	<html>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>南昌市人民政府办公厅办公自动化系统</title>
		<link href="<%=path%>/ncbgt/css.css" rel="stylesheet" type="text/css" />
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'>
		</script>
		<base target="_self">
			<style>
div.detailCard {
	position: absolute;
	z-index: 999;
	width: 380px;
	background: url(../ncbgt/images/bg_yl_11.gif) repeat-y top left;
}

div.detailCon2 {
	min-height: 150px;
	height: auto !important;
	height: 150px;
	line-height: 200%;
	padding: 15px;
	font-size: 16px;
	text-align: left;
	width: 380px;
	height: 300px;
	background: url(../ncbgt/images/bg_yl_03.gif) no-repeat top left;
}

div.detailCon3 {
	width: 380px;
	height: 13px;
	background: url(../ncbgt/images/bg_yl_06.gif) no-repeat left;
}

div.detailCon {
	
}
</style>
			</HEAD>

			<BODY>
				<table width="1002" border="0" cellpadding="0" cellspacing="0"
					class="center">
					<tr>
						<td>
							<img src="<%=path%>/ncbgt/images/nc_index_01.jpg" width="1002"
								height="181" />
						</td>
					</tr>
				</table>
				<table width="1002" border="0" cellpadding="0" cellspacing="0"
					class="center">
					<tr>
						<td height="43" valign="top"
							background="<%=path%>/ncbgt/images/nc_index_02.jpg">
							<table width="95%" border="0" cellpadding="0" cellspacing="0"
								class="center">
								<tr>
									<td width="120" height="38" class="menu line">
										<a href="../indexs.shtml">首 页</a>
									</td>
									<td width="120" class="menu line">
										<a href="../notice.shtml">通知公告</a>
									</td>
									<td class="menu line">
										<a href="../column.shtml?key=402882e03bdf2ef2013bdf6c20750005">每日领导动态
										</a>
									</td>
									<td width="120" class="menu line">
										<a href="../column.shtml?key=402882e03bdf2ef2013bdf6cd32b0009">专项活动</a>
									</td>
									<td class="menu line">
										<a href="../column.shtml?key=402882e03bdf2ef2013bdf6d04ab000b">政务公开-公开类公文</a>
									</td>
									<td width="120" class="menu line">
										<a href="../column.shtml?key=402882e03bdf2ef2013bdf6d5423000d">每日要情</a>
									</td>
									<td class="menu">
										<a href="../column.shtml?key=402882e03bdf2ef2013bdf6dc01c000f">常务工作会议纪要</a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td background="<%=path%>/ncbgt/images/nc_index_03.jpg"
							height="30">
							<table width="1002" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="475" height="30" align="right" class="time">
										<span>欢迎来到南昌市人民政府办公厅办公自动化系统!<script
												language="javascript">
var Mdate = new Date();
var Mnow = "";
Mnow = Mdate.getFullYear() + "年";
Mnow = Mnow + (Mdate.getMonth() + 1) + "月";
Mnow = Mnow + Mdate.getDate() + "日";
var Week = [ '日', '一', '二', '三', '四', '五', '六' ];
document.write("<span>今天是:" + Mnow + "</span>")
</script> </span>
									</td>
									<td width="527" align="center">
										<table width="0" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
													<img src="<%=path%>/ncbgt/images/search_08.jpg" width="22"
														height="27" />
												</td>
												<form action="../search.shtml" method="get">
													<td width="200" align="center" valign="middle">
														<label>
															<input name="newTitle" id="newTitle" type="text"
																class="input" />
														</label>
													</td>
													<td valign="middle">
														<input type="image"
															src="<%=path%>/ncbgt/images/search_13.jpg" width="57"
															height="20" style="vertical-align: middle" />
													</td>
												</form>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>

				<div class="center md">

					<table width="983" border="0" cellpadding="0" cellspacing="0"
						class="center">
						<tr>
							<td>
								<table background="<%=path%>/ncbgt/images/nc_index_09.jpg"
									width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="135" height="36" align="center"
											background="<%=path%>/ncbgt/images/nc_index_07.jpg"
											class="white_title">
											当前位置
										</td>
										<td>
											<span style="padding-left: 20px;"><a
												href="../indexs.shtml">首页</a> >> <a
												href="javascript:window.location.reload();">会议室安排</a> </span>
										</td>
										<td width="71">
											<a href="#"><img
													src="<%=path%>/ncbgt/images/nc_index_102.jpg" width="71"
													height="37" /> </a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td
								style="border-left: 1px solid #d1ca7a; border-right: 1px solid #d1ca7a;"
								height="500" valign="top">
								<div class="news_titile">
									<a name="1" id="1"></a>

									<div id="detail" class="detailCard" style="display: none;"
										onclick="hideDetail()" title="点击文字关闭提示窗口">
									</div>
									<script language="javascript" type="text/javascript">
					<c:forEach items="${l}" var="meetingRoom" varStatus="status">
					 if(${status.count}==1){
						  var t = "<table align=\"center\" width=\"60%\"><tr><td align=\"center\" style=\"height: 100px;\">";
						  var t =t+ "<img src=\"../${meetingRoom[8]}\" value=\"会议室名称：${meetingRoom[1]}\"";
						  var t = t +" id=\"${meetingRoom[0]}\" type=\"button\" onMouseOver=\"viewDetail('${meetingRoom[0]}');\"/><br/><span style=\"font-size:16px; \">${meetingRoom[1]}</span></td>";
						  document.write(t);
					 }else if(${status.count}%2!=0){
						  var t = "<tr><td align=\"center\" style=\"height: 100px;\">";
						  var t =t+ "<img src=\"../${meetingRoom[8]}\" value=\"会议室名称：${meetingRoom[1]}\"";
						  var t = t +" id=\"${meetingRoom[0]}\" type=\"button\" onMouseOver=\"viewDetail('${meetingRoom[0]}');\"/><br/><span style=\"font-size:16px; \">${meetingRoom[1]}</span></td>";
						  document.write(t);
					 }else if(${status.count}%2==0){
						  var t = "<td align=\"center\" style=\"height: 100px;\"><img src=\"../${meetingRoom[8]}\" value=\"会议室名称：${meetingRoom[1]}\"";
						  var t = t +" id=\"${meetingRoom[0]}\" type=\"button\"  onMouseOver=\"viewDetail('${meetingRoom[0]}');\"/><br/><span style=\"font-size:16px; \">${meetingRoom[1]}</span></td></tr>";
						  document.write(t);
					 }
					 
					 </c:forEach>
					 if('${l}'!=null){
						 document.write("</table>");
					 }
					 </script>
								</div>

								</div>

								<div align="right" style="margin-right: 100px;">

								</div>
							</td>

						</tr>

						<tr>

							<td
								style="border-left: 1px solid #d1ca7a; border-right: 1px solid #d1ca7a;"
								height="26" align="center";>
								【
								<a href="javascript:shutwin();">关闭页面</a>】 【
								<a href="#1">回到顶部</a>】
								<!--　【 字体：<a href="javascript:doZoom(20)"><font size="4">大</font></a>&nbsp;&nbsp; 
				 <a href="javascript:doZoom(16)"><font size="3">中</font></a>&nbsp;&nbsp; 
				 <a href="javascript:doZoom(14)"><font size="2">小</font></a> 】-->
								<script type="text/javascript" language="javascript">
function doZoom(size) {
	document.getElementById('contmas').style.fontSize = size + 'px'
}
</script>
								<script type="text/javascript" language="javascript">
<!-- Begin
                                if (window.print) {
                                    document.write('【<a href="#" onClick="javascript:window.print()"><font size="2">打印本文</font></a>】 ');
                                    }
                                 // End -->
                                 function viewDetail(obj){
                                	var t = document.getElementById(obj);
									//var x=t.offsetLeft;   
			  						//var y=t.offsetTop;
										var x=	event.x;
			  							var y=  event.y;
									$.ajax({
								  		type:"post",
								  		dataType:"text",
								  		url:"<%=root%>/meetingroom/meetingroomDesktop!getMeetingRoom.action",
								  		data:"mrId="+obj,
								  		success:function(msg){
								  			if(msg!=null&msg!=""){
							  			
												var srtHtml = "<div class=\"detailCon\" style=\"width: 380px; \" onclick=\"hideDetail();\">";
												srtHtml=srtHtml+"<div class=\"detailCon2\">";
												srtHtml=srtHtml+msg;
												srtHtml=srtHtml+"</div>";
												srtHtml=srtHtml+"<div class=\"detailCon3\"></div>";
								  				$('#detail').html(srtHtml+"</div>");
												$('#detail').css("left",parseInt(x-390)); 
												$('#detail').css("top",parseInt(y)-50); 
												$('#detail').css("display","");
												
								  			}else{
								  			}
								  		}
								  	});
								}
								
								//隐藏DIV提示
								function hideDetail(){
									$('#detail').css("display","none"); 
									$('.active').attr('class','text');
								}
                            </script>
								<script language="JavaScript">
function shutwin() {
	window.close();
	return;
}
</script>
							</td>
						</tr>
						<tr>
							<td>
								<img src="<%=path%>/images/ny_11.jpg" width="983" height="6" />
							</td>
						</tr>
					</table>

					<div class="foot center">
						版权所有 南昌市人民政府办公厅
						<br />
						技术支持 思创数码科技股份有限公司
					</div>
				</div>

			</body>
	</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>会议室申请情况统计</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												会议室使用情况统计
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="70%">
											</td>
											<td width="5"></td>			
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
								<table>
									<tr>
										<td align="right">
										<font color="blue">&nbsp;&nbsp;*按会议室查看：</font>
										</td>
										<td>
										<s:select list="roomList" listKey="mrId" listValue="mrName" headerKey="" headerValue="所有会议室"
											id="room" name=""/>
										<input type="button" class="input_bg" value="查询" onclick="refByroom();">
										</td>
									</tr>
									<tr>
										<td align="right">
										<font color="blue">&nbsp;&nbsp;*按部门查看： </font>
										</td>
										<td>
										<s:select list="depaList" headerKey="" headerValue="所有部门"
											id="depa" name=""/>
									
										<input type="button" class="input_bg" value="查询" onclick="refBydepa();">
										</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td>
									<iframe id="pieFrame" src="<%=path%>/oa/image/meetingroom/DrawPie.jpg" frameborder="0" scrolling="no" height="500px" width="750px" align="top" style="border-left:0px solid #CCCCCC;">				
									</iframe>
								</td>
							</tr>
							<tr>
								<td>
									<iframe id="barFrame" src="<%=path%>/oa/image/meetingroom/DrawBar.jpg" frameborder="0" scrolling="no" height="500px" width="750px" align="top" style="border-left:0px solid #CCCCCC;">				
									</iframe>
								</td>
							</tr>
							<%--<tr>
								<td>
									<img id="pieJpg" alt="统计图片" src="<%=path %>/oa/image/meetingroom/DrawPie.jpg"/>
									<img id="barJpg"  alt="统计图片" src="<%=path %>/oa/image/meetingroom/DrawBar.jpg"/>
								</td>
							</tr>
					--%></table>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript"> 
		
		function refByroom(){
			var room = $("#room").val();
			var url = "<%=path%>/meetingroom/meetingApply!rePrintChart.action?mrId="+room;
			refImg(url);
		}
		function refBydepa(){
			var depa = encodeURI(encodeURI($("#depa").val()));
			var url = "<%=path%>/meetingroom/meetingApply!rePrintChart.action?depa="+depa;
			refImg(url);
		}
		
		//刷新图片frame
		function refImg(actionUrl){
			$.ajax({
		  		type:"post",
		  		dataType:"text",
		  		url:actionUrl,
		  		data:"",
		  		success:function(msg){
		  		
			  			if("reload"==msg){
							var pieFrame = document.frames["pieFrame"];
							pieFrame.location.reload();
							var barFrame = document.frames["barFrame"];
							barFrame.location.reload()
			  			}else{
			  				alert("统计查询出错，请重新尝试！");
			  			}
			  			
		  		}
		  	});
			
			//$("#pieFrame").attr("src","<%=path %>/oa/image/meetingroom/DrawPie1.jpg");
		}
		
		</script>
	</BODY>
</HTML>

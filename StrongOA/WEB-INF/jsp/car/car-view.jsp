<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>查看车辆</title>
	<%@include file="/common/include/meta.jsp" %>
		
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<base target="_self">
	</head>
	<body oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<input type="hidden" id="carId" name="carId"
								value="${model.carId}">
						
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						查看车辆
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			<table align="center" width="90%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr>
					<td rowspan="1" colspan="4"  height="40" class="td1" align="center">
						<span class="wz">车辆信息</span>
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">车牌号：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;${model.carno}
					</td>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">车型：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
				     	${cartype2}
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">可乘人数：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						&nbsp;${model.takenumber}
					</td>
					<td  height="21" class="biao_bg1" align="right">
						<span class="wz">品牌：&nbsp;</span>
					</td>
					<td  class="td1" align="left">
					     ${model.carbrand}
					</td>
				
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">购置日期：&nbsp;</span>
					</td>
					<td colspan="1" class="td1">
						&nbsp;<s:date name="model.buydate" format="yyyy-MM-dd"/>
					</td>
					<td  height="21" class="biao_bg1" align="right">
						<span class="wz">状态：&nbsp;</span>
					</td>
					<td  class="td1" align="left">
					     ${status2}
					</td>
				
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">司机：&nbsp;</span>
					</td>
					<td colspan="1" class="td1">
						 ${model.driver}
					</td>
					<td  height="21" class="biao_bg1" align="right">
						
					</td>
					<td  class="td1" align="left">
					    
					</td>
				
				</tr>
				<tr>
					<td rowspan="1" colspan="1"  height="170" class="biao_bg1" align="right">
						<span class="wz">车辆图片：&nbsp;</span>
					</td>
					<td rowspan="1" colspan="3" align="center" class="biao_bg1">
					<div id="carImgPre" style="margin-left:5px;margin-right:5px;margin-top:5px;margin-bottom:3px">
					</div>
					<script>
							var carImg = "${model.img}";
								if(""!=carImg&&null!=carImg){
									var url = "<%=path%>/car/car!viewImg.action?carId=${model.carId}";
									$("#carImgPre").html("<img src=\""+url+"\" width=\"460px\" height=\"180px\">");
									$("#carImgPre").css("display","");
								}else{
									var url = "<%=path%>/oa/image/car/nophoto.jpg";
									$("#carImgPre").html("<img src=\""+url+"\" width=\"460px\" height=\"180px\">");
									$("#carImgPre").css("display","");
								}
					</script>
					</td>
					
				</tr>

				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">车辆描述：&nbsp;</span>
					</td>
					<td class="biao_bg1" colspan="3" align="left">
						<textarea  id="cardescription" name="model.cardescription" readonly style="width:469px;height:150px;">${model.cardescription}</textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center" height="21" >
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="window.close();">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>



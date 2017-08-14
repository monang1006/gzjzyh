<html>
	<head>
		<title>查看车辆信息</title>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<base target="_self">
	</head>
	<body oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<input type="hidden" id="carId" name="carId"
								value="${carmodel.carId}">
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
						&nbsp;&nbsp;${carmodel.carno}
					</td>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">车型：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${cartype}
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">可乘人数：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						&nbsp;&nbsp;${carmodel.takenumber}
					</td>
					<td  height="21" class="biao_bg1" align="right">
						<span class="wz">状态：&nbsp;</span>
					</td>
					<td  class="td1" align="left">
					   &nbsp;&nbsp;${carstatus}
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">购置日期：&nbsp;</span>
					</td>
					<td colspan="1" class="td1">
						&nbsp;&nbsp;<s:date name="carmodel.buydate" format="yyyy-MM-dd"/>
					</td>
					<td colspan="1" class="biao_bg1">
					</td>
					<td colspan="1" class="td1">
					</td>
				</tr>
				<tr>
					<td rowspan="1" colspan="1"  height="170" class="biao_bg1" align="right">
						<span class="wz">车辆图片：&nbsp;</span>
					</td>
					<td rowspan="1" colspan="3" align="left" class="biao_bg1">
						<img id="carImg" src="<%=path%>/oa/image/car/nophoto.jpg" width="487" height="179">
					<script>
						var carImg = "${carmodel.img}";
						if(""!=carImg&&null!=carImg){
							var url = "<%=path%>/car/car!viewImg.action?carId=${carmodel.carId}";
							$("#carImg").attr("src",url)
						}else{
							var url = "<%=path%>/oa/image/car/nophoto.jpg";
							$("#carImg").attr("src",url)
						}
					</script>	
					</td>
				</tr>

				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">车辆说明：&nbsp;</span>
					</td>
					<td class="biao_bg1" colspan="3" align="left">
						<textarea  id="cardescription" name="carmodel.cardescription" readonly style="width:100%;height:100px;">${carmodel.cardescription}</textarea>
					</td>
				</tr>
			</table>
            <s:form  theme="simple" id="carApplyForm" action="/car/carApply!carchaxun.action?">
			<table width="90%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">
												车辆${carmodel.carno}审批情况列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="5">
											</td>
											
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="carApply" width="100%" height="364px"
							wholeCss="table1" property="applicantId" isCanDrag="false"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img
											src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" id="img_sousuo" style="cursor: hand;" title="单击搜索"
											width="17" height="16">
									</td>
									<td  class="biao_bg1" style="display:none">
										<input id="carId" name="carId" value="${carId}" type="text" title="车辆ID" class="search">
									</td>
									<td  class="biao_bg1" style="display:none">
										<input id="carno" name="carno" value="${carno}" type="text" title="车牌号" class="search">
									</td>
									<td width="15%" class="biao_bg1">
									  <s:textfield name="applier"  cssClass="search" title="请输入申请人"></s:textfield>
									</td>
									<td width="25%" class="biao_bg1">
									   <s:textfield name="destination"  cssClass="search" title="请输入目的地"></s:textfield>
									</td>
									<td width="21%" class="biao_bg1">
										<strong:newdate id="starttime" name="starttime"  dateobj="${starttime}" isicon="true" dateform="yyyy-MM-dd HH:mm" 
										    width="100%" />
									</td>
									<td width="21%" class="biao_bg1">
										<strong:newdate id="endtime" name="endtime"  dateobj="${endtime}" isicon="true" dateform="yyyy-MM-dd HH:mm" 
										    width="100%" />
									</td>
								     <td width="13%" align="center" class="biao_bg1"><s:select name="model.applystatus" list="#{'3':'已批准','0':'待提交','1':'审批中','4':'已撤销','':'所有状态'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
									<td class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="applicantId"
								showValue="applier" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="申请人" property="applier" showsize="6"
								showValue="applier" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="目的地" property="destination" showsize="10"
								showValue="destination" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="起始时间" property="starttime" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
								showValue="starttime" isCanDrag="true"  width="21%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束时间" property="endtime" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
								showValue="endtime" isCanDrag="true" width="21%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexEnumCol caption="申请状态" property="applystatus" mapobj="${applyStstusMap}"
								showValue="applystatus" isCanDrag="true" width="13%" isCanSort="true"></webflex:flexEnumCol>
							</webflex:flexTable>
						</td>
				</tr>
			</table>
			</s:form>
		   
			<table>
				<tr>
					<td class="td1"  align="center" height="21">
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="window.close();">
					</td>
				</tr>

			</table>
		</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("Form").submit();
        });     
      });
</script>


	</body>
</html>



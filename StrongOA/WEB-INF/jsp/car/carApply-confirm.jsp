<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaCarApplicant"/>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>车辆使用登记</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		
		<script>
			var isConfirm = "${model.isConfirm}";
			if("<%=com.strongit.oa.bo.ToaCarApplicant.CARAPPLY_ISCONFIRM%>"==isConfirm){
				isConfirm = "<font color='green'>已登记</font>";
			}else{
				isConfirm = "<font color='red'>未登记</font>";
			}
			
			function isNumber(oNum) {
	          if(!oNum) return false;
	          //var strP=/^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*)|(\+?[0]+))$/；
			  var strP=/^(([0-9]+.[0-9]*[1-9]+)|([1-9]+[0-9]*.[1-9]+)|([1-9][0-9]*)|([0-9]*)|(\+?[0]+))$/;
	          //var strP=/^((\+?[1-9][0-9]*)|(\+0.[0-9]*)|(\+?[1-9][0-9]*.[0-9]*)|(\+?[0]))$/;
	          if(!strP.test(oNum)) return false;
	          try{
	            if(parseFloat(oNum)!=oNum) return false;
	          }catch(ex){
	            return false;
	          }
	          return true;
	       } 
			
			function saveConfirm(){
				$("#driver").val($.trim($("#driver").val()));
				var driver = $("#driver").val();
				if(driver==""||driver==null||driver=="undefine"){
					alert("驾驶员 不能为空！");
					$("#driver").focus();
					return;
				}
			
				$("#userDepart").val($.trim($("#userDepart").val()));
				var userDepart = $("#userDepart").val();
				if(userDepart==""||userDepart==null||userDepart=="undefine"){
					alert("科室/单位 不能为空！");
					$("#userDepart").focus();
					return;
				}
			
				$("#distance").val($.trim($("#distance").val()));
				var distance = $("#distance").val();
				if(!isNumber(distance)){
					alert("里程数输入格式错误，请输入整数或小数！");
					$("#distance").focus();
					return;
				}
				$("#gasCost").val($.trim($("#gasCost").val()));
				var gasCost = $("#gasCost").val();
				if(!isNumber(gasCost)){
					alert("油费输入格式错误，请输入整数或小数！");
					$("#gasCost").focus();
					return;
				}
				$("#bridgeCost").val($.trim($("#bridgeCost").val()));
				var bridgeCost = $("#bridgeCost").val();
				if(!isNumber(bridgeCost)){
					alert("路桥费输入格式错误，请输入整数或小数！");
					$("#bridgeCost").focus();
					return;
				}
				$("#pullCost").val($.trim($("#pullCost").val()));
				var pullCost = $("#pullCost").val();
				if(!isNumber(pullCost)){
					alert("停车费用输入格式错误，请输入整数或小数！");
					$("#pullCost").focus();
					return;
				}
				$("#cleanCost").val($.trim($("#cleanCost").val()));
				var cleanCost = $("#cleanCost").val();
				if(!isNumber(cleanCost)){
					alert("洗车费用输入格式错误，请输入整数或小数！");
					$("#cleanCost").focus();
					return;
				}
				$("#otherCost").val($.trim($("#otherCost").val()));
				var otherCost = $("#otherCost").val();
				if(!isNumber(otherCost)){
					alert("其他费用输入格式错误，请输入整数或小数！");
					$("#otherCost").focus();
					return;
				}
				
				$("#costNotes").text($.trim($("#costNotes").text()));
				var costNotes = $("#costNotes").text();
				if(costNotes.length>1000){
					alert("费用说明输入过长，请控制在1000字以内！");
					$("#costNotes").focus();
					return;
				}
				
				applyform.submit();
			}

		$(document).ready(function(){
			var message = $(".actionMessage").text();
			if(message!=null && message!=""){
				if(message=="success"){
					returnValue='reload';
					window.close();
				}else{
					alert("登记保存出错，请重新登记！");
				}
			}
		});
			
		</script>
	</head>
	<base target="_self">
	<body oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						车辆使用登记
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
		<s:form id="applyform" action="/car/carApply!confirmSave.action">
		<label id="actionMessage" style="display:none;"><s:actionmessage/></label>
		<input type="hidden" id="applicantId" name="model.applicantId" value="${model.applicantId}">
		<input type="hidden" id="carId" name="model.toaCar.carId" value="${model.toaCar.carId}">
		<input type="hidden" id="applierId" name="model.applierId" value="${model.applierId}">
		<input type="hidden" id="applier" name="model.applier" value="${model.applier}">
		<input type="hidden" id="passengernumber" name="model.passengernumber" value="${model.passengernumber}">
		<input type="hidden" id="applytime" name="model.applytime" value="${model.applytime}">
		<input type="hidden" id="starttime" name="model.starttime" value="${model.starttime}">
		<input type="hidden" id="endtime" name="model.endtime" value="${model.endtime}">
		<input type="hidden" id="destination" name="model.destination" value="${model.destination}">
		<input type="hidden" id="applyreason" name="model.applyreason" value="${model.applyreason}">
		<input type="hidden" id="approvalsuggestion" name="model.approvalsuggestion" value="${model.approvalsuggestion}">
		<input type="hidden" id="applystatus" name="model.applystatus" value="${model.applystatus}">
		<input type="hidden" id="caruser" name="model.caruser" value="${model.caruser}">
		<input type="hidden" id="needdriver" name="model.needdriver" value="${model.needdriver}">
		<input type="hidden" id="isConfirm" name="model.isConfirm" value="${model.isConfirm}">
		
			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr>
					<td rowspan="1" colspan="4"  height="30" class="td1" align="center">
						<span class="wz">车辆申请单</span>
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">用车人：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${model.caruser}
					</td>
					
					  <td colspan="1" width="20%" class="biao_bg1" align="right">
					    <span class="wz">申请状态：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
					    &nbsp;${applystatus2}
					</td>
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">申请人：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${model.applier}
					</td>
					<td colspan="1"  width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">车牌号：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${model.toaCar.carno}
					</td>
				    
				</tr>
				
				<tr>
				    <td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">申请时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					&nbsp;<s:date name="model.applytime" format="yyyy-MM-dd HH:mm"/>
					</td>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">乘客人数：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						&nbsp;&nbsp;${model.passengernumber}
					</td>
					 
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">出车时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					&nbsp;<s:date name="model.starttime" format="yyyy-MM-dd HH:mm"/>
						
					</td>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">回车时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					&nbsp;<s:date name="model.endtime" format="yyyy-MM-dd HH:mm"/>
					</td>
			
				</tr>
				<tr>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">目的地：&nbsp;</span>
					</td>
					<td colspan="2"  class="td1" align="left">
						&nbsp;&nbsp;${model.destination}
					</td>
				     <td colspan="1"  height="21" class="td1" align="center">
						<input id="needdriver" name="model.needdriver"  value="0" type="checkbox" disabled="disabled">自驾
						<script>
							var needdriver = "${model.needdriver}";
							if(needdriver=="1"){
								$("#needdriver").attr("checked",true);
								$("#needdriver").val("1");
							}
						</script>
						
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">事&nbsp;由：&nbsp;</span>
					</td>
					<td class="td1" colspan="4" align="left">
						&nbsp;&nbsp;${model.applyreason}
					</td>
				</tr>
				<tr >
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">审批意见：&nbsp;</span>
					</td>
					<td class="td1" colspan="4" align="left">
						&nbsp;&nbsp;${model.approvalsuggestion}
					</td>
				</tr>
				
				<tr><td colspan="4" height="30" class="td1" align="center"><b> 车辆使用登记 （<script>window.document.write(isConfirm);</script>）</b></td></tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">驾驶员：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="driver" name="model.driver" type="text" style="width:85%;" value="${model.driver}" >
					</td>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">用车处室/单位：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="userDepart" name="model.userDepart" type="text" style="width:85%;" value="${model.userDepart}" >
					</td>
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">里程数：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="distance" name="model.distance" type="text" style="width:85%;" value="${model.distance}" >公里
					</td>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">油费：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="gasCost" name="model.gasCost" type="text" style="width:85%;" value="${model.gasCost}" >元
					</td>
				</tr>
				<tr>
					<td colspan="1"  width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">路桥费：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="bridgeCost" name="model.bridgeCost" type="text" style="width:85%;" value="${model.bridgeCost}" >元
					</td>
				    <td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">停车费：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="pullCost" name="model.pullCost" type="text" style="width:85%;" value="${model.pullCost}" >元
					</td>
				</tr>
				<tr>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">洗车费：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="cleanCost" name="model.cleanCost" type="text" style="width:85%;" value="${model.cleanCost}" >元
					</td>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">其他费用：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="otherCost" name="model.otherCost" type="text" style="width:85%;" value="${model.otherCost}" >元
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right" valign="top">
						<span class="wz">费用说明：</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<textarea  id="costNotes" name="model.costNotes" style="width:515px;height:120px;">${model.costNotes}</textarea>
					</td>
				</tr>


				<tr>
					<td class="td1" colspan="5" align="center" height="21">
						<input name="Submit" type="button" class="input_bg" value="确认登记" onclick="saveConfirm();">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="Submit2" type="button" class="input_bg" value="取消关闭" onclick="window.close();">
					</td>
				</tr>
			</table>
			</s:form>
		</DIV>
	</body>
</html>

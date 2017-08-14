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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>编辑车辆申请</title>
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
	<body onload="setFoc();" oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center> 
		<s:form id="applyform" action="/car/carApply!editsave.action">
		<input type="hidden" id="applicantId" name="model.applicantId"
								value="${model.applicantId}">
	    <input type="hidden" id="applierId" name="model.applierId"
								value="${model.applierId}">
		<input type="hidden" id="applier" name="model.applier"
								value="${model.applier}">	
		<input type="hidden" id="applytime" name="model.applytime"
								value="${model.applytime}">	
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						编辑车辆申请
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
					<td rowspan="1" colspan="6"  height="60" class="td1" align="center">
						<span class="wz">车辆申请单</span>
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="18%" height="21" class="biao_bg1" align="right">
						<span class="wz">用车人(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" width="18%" class="td1" align="left">
						<input id="caruser" name="model.caruser" type="text" style="width:100%;" value="${model.caruser}" >
					</td>
					<td colspan="1"  width="16%" height="21" class="biao_bg1" align="right">
						<span class="wz">车牌号：&nbsp;</span>
					</td>
					<td colspan="1" width="18%" class="td1" align="left">
						<input id="carno" name="model.toaCar.carno" type="text" style="width:100%;" value="${model.toaCar.carno}" disabled>
					</td>
					<td colspan="1" width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">申请人：&nbsp;</span>
					</td>
					<td colspan="1" width="15%" class="td1" align="left">
						<input id="applier2"  type="text" style="width:100%;" value="${model.applier}" disabled>
					</td>
					<td colspan="2" class="td1" align="left" style="display:none">
					   <input id="carId" name="model.toaCar.carId" type="text"  value="${model.toaCar.carId}" style="display:none">
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">出车时间(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1">
						<strong:newdate id="stime" name="stime" width="100%" dateobj="${model.starttime}"
							dateform="yyyy-MM-dd HH:mm"></strong:newdate>
					</td>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">回车时间(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1">
						<strong:newdate id="etime" name="etime" width="100%" dateobj="${model.endtime}"
							dateform="yyyy-MM-dd HH:mm"></strong:newdate>
					</td>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">随车人数(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="passengernumber" name="model.passengernumber" value="${model.passengernumber}" type="text" style="width:100%;">
					</td>
				</tr>
				<tr>
				   <td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">目的地(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="4"  class="td1" align="left">
						<input id="destination" name="model.destination" value="${model.destination}" type="text" style="width:99%;">
					</td>
					<td colspan="1"  class="td1" align="center">
						<input id="needdriver" name="model.needdriver"  value="0" type="checkbox" onclick="setNeedDriver(this)">自驾
						
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
						<span class="wz">事&nbsp;由(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td class="td1" colspan="5" align="left">
						<textarea  id="applyreason" name="model.applyreason"
							style="width:99%;height:120px;">${model.applyreason }</textarea>
					</td>
				</tr>
				<tr id="approvalrecord" style="display:none">
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">审批意见：&nbsp;</span>
					</td>
					<td class="td1" colspan="5" align="left">
						<textarea  id="approvalsuggestion" name="model.approvalsuggestion" readonly 
							style="width:99%;height:150px;">${model.approvalsuggestion }</textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="6" align="center" height="21">
					    <input name="Submit" type="button" class="input_bg" value="保存" onclick="saveApply();">
						<input name="Submit2" type="button" class="input_bg" value="取消" onclick="window.close();">
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
<script language="javascript">

 function setFoc(){
  document.getElementById("caruser").focus();
 }
 
 function setNeedDriver(obj){
		 	if("1"==obj.value){
		 		obj.value = "0";
		 	}else{
		 		obj.value = "1";
		 	}
		 }

	function isDigits(port){
  					if($.trim(port) == ""){
  						return false;
  					}else{
	  					var exp = /^\d+$/;
	  					return exp.test(port);
  					}
  				}

function saveApply(){
   var inputDocument=document;
    if(inputDocument.getElementById("caruser").value.length==null || inputDocument.getElementById("caruser").value.length==""){
    	alert("用车人不能为空！");
    	inputDocument.getElementById("caruser").focus();
    	return false;
    }
     if(inputDocument.getElementById("caruser").value.length>32){
    	alert("用车人不能超出32个字符！");
    	inputDocument.getElementById("caruser").focus();
    	return false;
    }
     if(inputDocument.getElementById("stime").value.length==null || inputDocument.getElementById("stime").value.length==""){
    	alert("出车时间不能为空！");
    	inputDocument.getElementById("stime").focus();
    	return false;
    }
  if(inputDocument.getElementById("etime").value.length==null || inputDocument.getElementById("etime").value.length==""){
    	alert("回车时间不能为空！");
    	inputDocument.getElementById("etime").focus();
    	return false;
    }
    
     if(inputDocument.getElementById("passengernumber").value.length>3){
    	alert("随车人数不能超过3位数！");
    	inputDocument.getElementById("passengernumber").focus();
    	return false;
    }
    
    if(!isDigits($("#passengernumber").val())){
						alert("随车人数不能为空且只能为数字！");
						inputDocument.getElementById("passengernumber").focus();
						return ;
					}
					
      if(inputDocument.getElementById("destination").value.length==null || inputDocument.getElementById("destination").value.length==""){
    	alert("目的地不能为空！");
    	inputDocument.getElementById("destination").focus();
    	return false;
    }
     if(inputDocument.getElementById("destination").value.length>80){
    	alert("目的地不能超出80个字符！");
    	inputDocument.getElementById("destination").focus();
    	return false;
    }  
      
    if(inputDocument.getElementById("applyreason").value.length==null || inputDocument.getElementById("applyreason").value.length==""){
    	alert("事由不能为空！");
    	inputDocument.getElementById("applyreason").focus();
    	return false;
    }
    if(inputDocument.getElementById("applyreason").value.length>1000){
    	alert("事由不能超出1000个字符！");
    	inputDocument.getElementById("applyreason").focus();
    	return false;
    }
    
	applyform.submit();
}

</script>
	</body>
</html>

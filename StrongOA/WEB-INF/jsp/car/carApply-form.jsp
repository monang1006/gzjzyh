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
		<title>车辆审批</title>
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
		
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		
		<script type="text/javascript">
		String.prototype.trim = function() {
					                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
					                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
					                return strTrim;
					            }
			            
		function goBack(){
	       var isStart = $("#isStartWorkflow").val();
	       window.location = "<%=root%>/car/carApply!todo.action";
		}
	      
	   	function isDigits(port){
  					if($.trim(port) == ""){
  						return false;
  					}else{
	  					var exp = /^\d+$/;
	  					return exp.test(port);
  			}
  		}
	//转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2string(stime){
         	var arrsDate1=stime.split('-');
         	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
         	var arrsDate2=stime.split(' ');
         	stime=arrsDate2[0]+""+arrsDate2[1];
         	var arrsDate3=stime.split(':');
         	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
         	return stime;
         }
		
		
		var date_format= /^(\d{4}-\d{2}-\d{2} \d{2}:\d{2})$/;
		//验证开始时间，结束时间
		function valiDate(){
			var startTime = $("#stime").val().trim();
			var endTime = $("#etime").val().trim();
			
			if(""==startTime|null==startTime){
				alert("请输入开始时间");
				$("#stime").focus();
				document.getElementById("saveok").value="0"; //表示页面数据不正确
				return false;
			}
			if(""==endTime|null==endTime){
				alert("请输入结束时间");
				$("#etime").focus();
				document.getElementById("saveok").value="0"; //表示页面数据不正确
				return false;
			}
			if(!date_format.test(startTime)){
				alert("输入的开始时间<"+startTime+">格式不对\n请确认后重新输入！");
				document.getElementById("saveok").value="0"; //表示页面数据不正确
				return ;
			}
			if(!date_format.test(endTime)){
				alert("输入的结束时间<"+endTime+">格式不对\n请确认后重新输入！");
				document.getElementById("saveok").value="0"; //表示页面数据不正确
				return ;
			}
			
			if(date2string(startTime)>=date2string(endTime)){
				alert("开始时间不能比结束时间晚！");
				$("#stime").focus();
				document.getElementById("saveok").value="0"; //表示页面数据不正确
				return false;
			}
			return true;
		}
		//取出数字前的0
		function formatNumZore(num){
			if(num.indexOf("0")==0){
				num = num.replace("0","");
				return formatNumZore(num);
			}else{
				return num;
			}
		}  	
	      
	   // 保存审批信息
    function save_approval(){
   
    var inputDocument=document;
    if(inputDocument.getElementById("caruser").value.length==null || inputDocument.getElementById("caruser").value.length==""){
    	alert("用车人不能为空！");
    	document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("caruser").focus();
    	return ;
    }
  
     if(inputDocument.getElementById("caruser").value.length>32){
    	alert("用车人不能超出32个字符！");
    	document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("caruser").focus();
    	return ;
    }
    
         var carId=document.getElementById("carId").value;
      if (carId=="" || carId==null){
         alert("请选择车辆！");
         document.getElementById("saveok").value="0"; //表示页面数据不正确
         return ;
      }
    
		if(!valiDate()){
	    	return false;
	    }     

	 var numb = $("#passengernumber").val();
	 numb = formatNumZore(numb);
	 inputDocument.getElementById("passengernumber").value = numb
     if(inputDocument.getElementById("passengernumber").value.length>3){
    	alert("随车人数不能超过3位数！");
    	 document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("passengernumber").focus();
    	return ;
    }
    
      if(!isDigits($("#passengernumber").val())){
						alert("随车人数不能为空且只能为数字！");
						 document.getElementById("saveok").value="0"; //表示页面数据不正确
						inputDocument.getElementById("passengernumber").focus();
						return ;
					}
    
      if(inputDocument.getElementById("destination").value.length==null || inputDocument.getElementById("destination").value.length==""){
    	alert("目的地不能为空！");
    	 document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("destination").focus();
    	return ;
    }
     if(inputDocument.getElementById("destination").value.length>80){
    	alert("目的地不能超出80个字符！");
    	 document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("destination").focus();
    	return ;
    }  
      
    if(inputDocument.getElementById("applyreason").value.length==null || inputDocument.getElementById("applyreason").value.length==""){
    	alert("事由不能为空！");
    	 document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("applyreason").focus();
    	return ;
    }
    if(inputDocument.getElementById("applyreason").value.length>1000){
    	alert("事由不能超出1000个字符！");
    	 document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("applyreason").focus();
    	return ;
    }
    
      if(inputDocument.getElementById("approvalsuggestion").value.length>1000){
    	alert("审批意见不能超出1000个字符！");
    	 document.getElementById("saveok").value="0"; //表示页面数据不正确
    	inputDocument.getElementById("approvalsuggestion").focus();
    	return ;
      }
      
           document.getElementById("saveok").value="1";  //表示页面数据正确
	       approvalform.submit();
	   }   
	      
	      //提交下一步处理人
	      function submitApproval() {
		       save_approval();  // 保存审批信息
		       if (document.getElementById("saveok").value=="1"){
			        var formId = $("#formId").val();
			        var bussinessId = $("#bussinessId").val();
			        var taskId = $("#taskId").val();
			        var returnValue ="";
					var instanceId = $("#instanceId").val();
					var contextPath = "<%=root%>/car/carApply";
			        returnValue = OpenWindow("<%=root%>/car/carApply!nextstep.action?bussinessId="+bussinessId+
						                                 "&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&fromPath="+contextPath, 550, 500, window);
				    if (returnValue == "OK") {
						 window.location = "<%=root%>/car/carApply!todo.action";
					} 
		        }
	      }
	 
	     function setNeedDriver(obj){
		 	if("1"==obj.value){
		 		obj.value = "0";
		 	}else{
		 		obj.value = "1";
		 	}
		 }
	    //流程回退到指定步
       function backToPreDefNode(){
         var formId = $("#formId").val();
         var bussinessId = $("#bussinessId").val();
         var taskId = $("#taskId").val();
         var applicantId=$("#applicantId").val();
         var width=screen.availWidth-310;;
         var height=screen.availHeight-230;
         var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=return", 
                                   width, height, window);
         
         if(ReturnStr!=null && ReturnStr!=''){  
              var returnNode=ReturnStr;
              window.location = "<%=root%>/car/carApply!backToPreDefNode.action?taskId="+taskId+"&bussinessId="+bussinessId+"&formId="+formId+"&returnNode="+returnNode+"&applicantId="+applicantId;
          }
      }
      
      //查看流程图
      function workflowView(){      
          var width=screen.availWidth-10;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/car/carApply!PDImageView.action?instanceId="+$("#instanceId").val(), 
                                   width, height, window);
      }
      
    function car_chaxun(){
      var carId=document.getElementById("carId").value;
      if (carId=="" || carId==null){
         alert("请选择车辆！");
         return false;
      }
      OpenWindow('<%=path%>/car/carApply!carchaxun.action?carId='+carId,'520pt','500pt','carchaxunWindow');
   }
      
</script>
	</head>
	<body oncontextmenu="return false;">
	<DIV id=contentborder align=center>
  <s:form id="approvalform" action="/car/carApply!approvalsave.action">
	     <s:hidden id="formId" name="formId"></s:hidden>
		<s:hidden id="applicantId" name="applicantId"></s:hidden>
		<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
		<s:hidden id="taskId" name="taskId"></s:hidden>
		<s:hidden id="instanceId" name="instanceId"></s:hidden>
		<s:hidden id="isStartWorkflow" name="isStartWorkflow"></s:hidden>
		
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<input type="hidden" id="applicantId" name="model.applicantId"
								value="${model.applicantId}">
	    <input type="hidden" id="applierId" name="model.applierId"
								value="${model.applierId}">
		<input type="hidden" id="applier" name="model.applier"
								value="${model.applier}">	
		<input type="hidden" id="applytime" name="model.applytime"
								value="${model.applytime}">
		<input type="hidden" id="applystatus" name="model.applystatus"
								value="${model.applystatus}">				
		
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						车辆审批
					</td>
					<td width="*">
						&nbsp;
					</td>
					<td >
						<a class="Operation" href="javascript:submitApproval();"><img
								src="<%=root%>/images/ico/shenhe.gif" width="15"
						   height="15" class="img_s" >审批&nbsp;</a>
					</td>
					<td >
						<a class="Operation" href="javascript:backToPreDefNode();"><img
								src="<%=root%>/images/ico/message2.gif" width="15"
						   height="15" class="img_s">退回&nbsp;</a>
					</td>
				    <td >
					    <a class="Operation" href="#" onclick="workflowView();"><img
								src="<%=root%>/images/ico/chakan.gif" width="15"
											height="15" class="img_s">处理状态&nbsp;</a>
					</td>
					<td >
						<a class="Operation" href="javascript:goBack();"><img
								src="<%=root%>/images/ico/ht.gif" width="15"
								height="15" class="img_s">返回&nbsp;</a>
					</td>
					<td width="5%">
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
					<td rowspan="1" colspan="4"  height="42" class="td1" align="center">
						<span class="wz">车辆申请单</span>
					</td>
					<td rowspan="1" colspan="1"  height="42" class="td1" align="center" style="display:none">
						<input id="saveok" name="saveok" type="text"  value="" >
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">用车人(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="caruser" name="model.caruser" type="text" style="width:100%;" value="${model.caruser}" >
					</td>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">申请人：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="applier2"  type="text" style="width:100%;" value="${model.applier}" disabled>
					</td>
				</tr>
				<tr>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">车牌号：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="carno" name="carno" type="text" style="width:100%;" value="${model.toaCar.carno}" disabled>
					</td>
					<td colspan="1"  height="21" class="biao_bg1" align="right">
						<span class="wz">更换车辆：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					    <s:select list="carList" style="width:70%" listKey="carId" listValue="carno"  headerKey="" headerValue=""
							id="carId" name="model.toaCar.carId" />
						<input name="carchaxun" type="button" class="input_bg" style="color:#FF6600" value="查看" title="查看本车信息及安排情况" onclick="car_chaxun();">
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
				</tr>
				<tr>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">随车人数(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="passengernumber" name="model.passengernumber" value="${model.passengernumber}" type="text" style="width:100%;">
					</td>
					<td colspan="2"  class="td1" align="center">
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
				   <td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">目的地(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="3"  class="td1" align="left">
						<input id="destination" name="model.destination" value="${model.destination}" type="text" style="width:100%;">
					</td>
					
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">事&nbsp;由(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea  id="applyreason" name="model.applyreason"
							style="width:100%;height:120px;">${model.applyreason }</textarea>
					</td>
				</tr>
				<tr id="approvalrecord" >
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">审批意见：&nbsp;</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea  id="approvalsuggestion" name="model.approvalsuggestion" 
							style="width:100%;height:120px;">${model.approvalsuggestion }</textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center" height="21">
					   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					    <input name="Submit1" type="button" class="input_bg" value="&nbsp;保&nbsp;存&nbsp;" onclick="save_approval();">
					    <input name="Submit4" type="button" class="input_bg" value="&nbsp;返&nbsp;回&nbsp;" onclick="goBack();">
					</td>
				</tr>
			</table>
			</s:form>
			<table align="left">
                <tr>
                  <td align="left">
                       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FF6600">*</font> 单击<font color="#FF6600">查看</font>按钮，可以查看车辆信息及其安排用车情况。
                  </td>
                </tr>
           </table>
		</DIV>

	</body>
</html>

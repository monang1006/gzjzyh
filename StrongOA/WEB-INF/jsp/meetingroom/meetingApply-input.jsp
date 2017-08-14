<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<title>会议室申请单</title>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
			
		<script>
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
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
		
		
		var date_mobile= /^(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})$/;
		//验证开始时间，结束时间
		function valiDate(){
			var startTime = $("#startTime").val().trim();
			var endTime = $("#endTime").val().trim();
			
			if(""==startTime|null==startTime){
				alert("请输入开始时间");
				$("#startTime").focus();
				return false;
			}
			if(""==endTime|null==endTime){
				alert("请输入结束时间");
				$("#endTime").focus();
				return false;
			}
			if(!date_mobile.test(startTime)){
				alert("输入的开始时间<"+startTime+">格式不对\n请确认后重新输入！");
				$("#startTime").focus();
				return ;
			}
			if(!date_mobile.test(endTime)){
				alert("输入的结束时间<"+endTime+">格式不对\n请确认后重新输入！");
				$("#endTime").focus();
				return ;
			}
			
			if(date2string(startTime)>=date2string(endTime)){
				alert("开始时间不能比结束时间晚！");
				$("#startTime").focus();
				return false;
			}
			return true;
		}
		
         //选择会议室
		function selectRoom(){
			if(!valiDate()){//验证时间
				return ;
			}
			var a = OpenWindow("<%=path%>/meetingroom/meetingApply!selectRoom.action?mrId="+$("#mrId").value+"&model.maAppstarttime="+$("#startTime").val()+"&model.maAppendtime="+$("#endTime").val(),'500pt','400pt','roomlist')
			if(undefined!=a){
				var roomInfo = a.split(",");
				$("#mrId").val(roomInfo[0]);
				$("#mrName").val(roomInfo[1]);
				
				$("#trTimeEdit").css("display","none");
				$("#appstartTime").html($("#startTime").val());
				$("#appendTime").html($("#endTime").val());
				$("#trTimeRead").css("display","");
				
			/*
				$("input[name='model.maAppstarttime']") .attr("readonly", "readonly");
				$("input[name='model.maAppstarttime']") .attr("onclick", "");
				$("input[name='model.maAppendtime']") .attr("readonly", "readonly");
				$("input[name='model.maAppendtime']") .attr("onclick", "");
				alert($("#timeHtml").html());
			*/
			}
		}
		//重新设置时间
		function resetTime(){
			if($("#mrId").val()!=""){
				$("#mrId").val("");
				$("#mrName").val("");
				
				$("#trTimeEdit").css("display","");
				$("#trTimeRead").css("display","none");
		/*	
				$("input[name='model.maAppstarttime']") .attr("readonly", "");
				$("input[name='model.maAppstarttime']") .attr("onclick", "WdatePicker({dateFmt;'yyyy-MM-dd HH:mm:ss'})");
				$("input[name='model.maAppendtime']") .attr("readonly", "");
				$("input[name='model.maAppendtime']") .attr("onclick", "WdatePicker({dateFmt;'yyyy-MM-dd HH:mm:ss'})");
		*/	
			}
		}
		
		
		function saveForm(){
		 	if(""==$("#mrId").val()|null==$("#mrId").val()){
		 		alert("请选择要申请的会议室！");
		 		$("#selectR").focus();
		 		return;
		 	}
		 	if(""==$("#maMeetingdec").val().trim()|null==$("#maMeetingdec").val().trim()){
		 		alert("请输入会议议题！");
		 		$("#maMeetingdec").focus();
		 		return;
		 	}
		 	if(""==$("#maEmcee").val().trim()|null==$("#maEmcee").val().trim()){
		 		alert("请输入会议的主持人！");
		 		$("#maEmcee").focus();
		 		return;
		 	}
		 	if($("#description").val().length>1000){
		 		alert("说明内容过长，请控制在1000字以内！");
		 		$("#description").focus();
		 		return;
		 	}
		 	
			form.submit();
		}
		
		
		</script>
	</head>
	<base target="_self">
	<body oncontextmenu="return false;" >
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<s:form name="form" action="/meetingroom/meetingApply!save.action" method="post" enctype="multipart/form-data">
	<label id="actionMessage" style="display:none;"><s:actionmessage/></label>
	<input type="hidden" id="maId" name="model.maId" value="${model.maId}">
	<input type="hidden" id="mrId" name="model.toaMeetingroom.mrId" value="${model.toaMeetingroom.mrId}">
		<input type="hidden" id="topOrgcode" name="model.topOrgcode"
								value="${model.topOrgcode}">
								
			<input type="hidden" id="departmentId" name="model.departmentId"
								value="${model.departmentId}">
		<DIV id=contentborder align=center>
			<table width="100%" height="38px;"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>
					&nbsp;
					</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						会议室申请单
					</td>
					<td width="70%">
						&nbsp;
					</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr id="trTimeEdit">
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz" style="width:100%">开始使用时间(<font color=red>*</font>)：</span>
					</td>
					<td class="td1" align="left">
						<strong:newdate id="startTime" name="model.maAppstarttime" dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.maAppstarttime}"
							width="175" />
					</td>
					<td height="21" class="biao_bg1" width="27%" align="right">
						<span class="wz">结束使用时间(<font color=red>*</font>)：</span>
					</td>
					<td class="td1" align="left">
						<strong:newdate id="endTime" name="model.maAppendtime" dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.maAppendtime}" width="175" />
					</td>
				</tr>
				<tr id="trTimeRead" style="display:none;">
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span id="appstart" class="wz" style="width:100%">开始使用时间(<font color=red>*</font>)：</span>
					</td>
					<td class="td1" align="left" id="appstartTime" width="25%" >
					</td>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">结束使用时间(<font color=red>*</font>)：</span>
					</td>
					<td class="td1" align="left" id="appendTime" width="25%" >
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" width="25%" class="biao_bg1"
						align="right">
						<span class="wz">选择会议室(<font color=red>*</font>)：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="mrName" name="model.toaMeetingroom.mrName" type="text" size="30"
							value="${model.toaMeetingroom.mrName }" readonly="readonly">
							
						<input id="selectR" type="button" class="input_bg" value="..." title="选择会议室" onclick="selectRoom();">
						<input type="button" class="input_bg" value="重设" title="重新选择时间" onclick="resetTime();">
					</td>
				</tr>
				<tr>
					<td colspan="1" height="21" width="25%" class="biao_bg1"
						align="right">
						<span class="wz">会议议题(<font color=red>*</font>)：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						<input id="maMeetingdec" name="model.maMeetingdec" type="text" size="30" maxlength="2000"
							value="${model.maMeetingdec}" >
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" width="30%" align="right">
						<span class="wz">会议主持人(<font color=red>*</font>)：</span>
					</td>
					<td class="td1" align="left">
						<input type="text" id="maEmcee"  value="${model.maEmcee }" maxlength="122" name="model.maEmcee"/>
					</td>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">发起部门：</span>
					</td>
					<td class="td1" align="left">
						<%--<input type="text" name="model.maDepartment" id="maDepartment" size="30" value="${model.maDepartment}" readonly="readonly" />
						--%>
						${model.maDepartment}
					</td>
				</tr>
				
				<tr>
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">说明：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="10" cols="65" id="description" name="model.maRemark" 
							style="overflow: auto;">${model.maRemark }</textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit" type="button" class="input_bg" value="保 存" onclick="saveForm();">
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</DIV>
		<script type="text/javascript">
			$(document).ready(function() {
				var message = $(".actionMessage").text();
				if(message!=null && message!=""){
					if(message.indexOf("error")>-1){
						 alert("无法根据给出路径找到图片,请重新选择图片!");
					}else{
						returnValue='reload';
						window.close();
					}
				}
				
				
			});
		</script>
	</s:form>
	</body>
</html>

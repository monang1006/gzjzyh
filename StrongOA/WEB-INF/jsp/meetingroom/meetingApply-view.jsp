<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaMeetingroomApply"/>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
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
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
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
			}
		}
		//重新设置时间
		function resetTime(){
			if($("#mrId").val()!=""){
				$("#mrId").val("");
				$("#mrName").val("");
				
				$("#trTimeEdit").css("display","");
				$("#trTimeRead").css("display","none");
			}
		}
		//状态转换
		function formatState(state){
			if("<%=ToaMeetingroomApply.APPLY_ALLOW%>"==state){
				return "<font color='green'>审批通过</font>";
			}else if("<%=ToaMeetingroomApply.APPLY_DISALLOW%>"==state){
				return "<font color='red'>审批驳回</font>";
			}else if("<%=ToaMeetingroomApply.APPLY_END%>"==state){
				return "使用结束";
			}else if("<%=ToaMeetingroomApply.APPLY_NEW%>"==state){
				return "<font color='orange'>未审批</font>";
			}
			
		}
		
		//提交表单
		function saveApp(){
			if(""==$("#mrId").val()|null==$("#mrId").val()){
		 		alert("请选择要申请的会议室！");
		 		$("#selectR").focus();
		 		return;
		 	}
		 	if(""==$("#maMeetingdec").val()|null==$("#maMeetingdec").val()){
		 		alert("请输入会议议题！");
		 		$("#maMeetingdec").focus();
		 		return;
		 	}
		 	if(""==$("#maEmcee").val()|null==$("#maEmcee").val()){
		 		alert("请输入会议的主持人！");
		 		$("#maEmcee").focus();
		 		return;
		 	}
		 	
		 	if($("#description").val().length>1000){
		 		alert("说明内容过长，请控制在1000字以内！");
		 		$("#description").focus();
		 		return;
		 	}
			var appState = document.getElementsByName("applyState");
			for(var i=0;i<appState.length;i++){
				if(appState[i].checked){
					$("#maState").val(appState[i].value);
				}
			}
			form.submit();
		}
		
		
		
		</script>
	</head>
	<base target="_self">
	<body oncontextmenu="return false;" onload="initMenuT();">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<s:form name="form" action="/meetingroom/meetingApply!save.action" method="post" enctype="multipart/form-data">
	<label id="actionMessage" style="display:none;"><s:actionmessage/></label>
	<input type="hidden" id="maId" name="model.maId" value="${model.maId}">
	<input type="hidden" id="mrId" name="model.toaMeetingroom.mrId" value="${model.toaMeetingroom.mrId}">
	<input type="hidden" id="maState" name="model.maState" value="${model.maState}">
		<DIV id=contentborder align=center>
			<table width="100%" height="38px;"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td >
					&nbsp;
					</td>
					<td width="40%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						会议室申请单
					</td>
					<td width="60%">
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
					<td height="21" class="biao_bg1" width="25%" align="right">
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
						<input id ="resetT" type="button" class="input_bg" value="重设" title="重新选择时间" onclick="resetTime();">
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
					<td height="21" class="biao_bg1" width="25%" align="right">
						<span class="wz">会议主持人(<font color=red>*</font>)：</span>
					</td>
					<td class="td1" align="left">
						<input type="text" id="maEmcee" size="30" value="${model.maEmcee }" maxlength="122" name="model.maEmcee"/>
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
				<s:if test="inputType=='yes'">
					<tr>
						<td height="21" class="biao_bg1" width="25%" align="right">
							<span class="wz">申请状态：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input type="radio" id="APPLY_NEW" name="applyState" value = "0" onclick=" ">未审批&nbsp;&nbsp;
							<input type="radio" id="APPLY_ALLOW" name="applyState" value = "1" onclick=" "> 审批通过&nbsp;&nbsp;
							<input type="radio" id="APPLY_DISALLOW" name="applyState" value = "2" onclick=" ">审批驳回&nbsp;&nbsp;
							<input type="radio" id="APPLY_END" name="applyState" value = "3" onclick=" ">使用结束&nbsp;&nbsp;
							<script>
								//初始化申请状态
								var maState ="${model.maState }";
								if("3"==maState){
									$("#APPLY_END").attr("checked",true);
								}else if("2"==maState){
									$("#APPLY_DISALLOW").attr("checked",true);
								}else if("1"==maState){
									$("#APPLY_ALLOW").attr("checked",true);
								}else {
									$("#APPLY_NEW").attr("checked",true);
								}
							</script>
						</td>
					</tr>
					<tr>
						<td id="btnArea" class="td1" colspan="4" align="center">
							<input name="Submit" type="button" class="input_bg" value="保 存" onclick="saveApp();">
							<input name="Submit2" type="button" class="input_bg" value="关 闭" onclick="window.close();">
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td height="21" class="biao_bg1" width="25%" align="right">
							<span class="wz">申请状态：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<script>
								var maState ="${model.maState }";
								document.writeln(formatState(maState));
							</script>
						</td>
					</tr>
					<tr>
						<td id="btnArea" class="td1" colspan="4" align="center">
							<input name="Submit2" type="button" class="input_bg" value="关 闭" onclick="window.close();">
						</td>
					</tr>
				</s:else>
			</table>
			<br>
			<s:if test="clashList.size>0">
			<table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
				<tr>
					<td class="td1" align="center">
			<webflex:flexTable name="myTable" width="100%" height="364px" wholeCss="table1" property="0" isCanDrag="true"
					isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${clashList}">
				<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					<div align="left" style="height: 20px;" ><font color="red" class="wz">以下申请与该申请有冲突：</font></div>
					<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
					<webflex:flexTextCol caption="会议议题" valuepos="0" valueshowpos="1" width="25%" isCanDrag="true" isCanSort="true" onclick="swichApp(this.value)" showsize="18"></webflex:flexTextCol>
					<webflex:flexTextCol caption="申请人" valuepos="2" valueshowpos="2" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
					<webflex:flexDateCol caption="申请时间" valuepos="8" valueshowpos="8" width="15%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="20"></webflex:flexDateCol>
					<webflex:flexDateCol caption="开始使用时间" valuepos="3" valueshowpos="3" width="15%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="20"></webflex:flexDateCol>
					<webflex:flexDateCol caption="结束使用时间" valuepos="4" valueshowpos="4" width="15%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="20"></webflex:flexDateCol>
					<webflex:flexTextCol caption="状态" valuepos="5" valueshowpos="javascript:formatState(5);" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						  	
				</table>
			</webflex:flexTable>
					</td>
				</tr>
			</table>
			</s:if>
			<s:else>
				<table width="100%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
					<tr>
						<td class="td1" align="center">
							<div align="left"><font color="green" class="wz">该申请没有冲突</font></div>
						</td>
					</tr>
				</table>
			</s:else>
		</DIV>
	</s:form>
		<script type="text/javascript">
	var sMenu = new Menu();
	function initMenuT(){
		sMenu.registerToDoc(sMenu);
		var item = null;
		item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","swichApp",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
		sMenu.addShowType("ChangeWidthTable");
	    registerMenu(sMenu);
	}
	//切换申请单
	function swichApp(maId){
		if(""==maId|undefined==maId){
			maId = getValue();
		}
		returnValue="swich,"+maId;
		window.close();
	}
$(document).ready(function() {
	
				var message = $(".actionMessage").text();
				if(message!=null && message!=""){
					if(message.indexOf("error")>-1){
						 alert("保存出错！");
					}else{
						returnValue='reload';
						window.close();
					}
				}
				
				
				//如果不能编辑
				if("yes"!="${inputType}"){
					$("input[name^='model']") .attr("readonly", "readonly");
					$("input[name^='model']") .attr("onclick", ";");
					$("input[value^='...']") .attr("onclick", ";");
					$("textarea[name^='model']") .attr("readonly", "readonly"); 
					$("#selectR").css("display","none");
					$("#resetT").css("display","none");
				}
				
});
		</script>
	</body>
</html>

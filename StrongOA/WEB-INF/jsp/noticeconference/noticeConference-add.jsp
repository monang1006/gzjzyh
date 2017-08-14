<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
 
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<base target=_self>
		<title>新增会议通知</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/dxxk.css" />
					<style type="text/css">
html {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
	padding: 0px 0px 40px 0px;
	overflow: hidden;
}

html,body {
	height: 100%;
}

#nobr br {
	display: none;
}

.information_list_choose_pagedownnew {
	position: absolute;
	width: 100%;
	text-align: right;
	left: 0;
	right: 0;
	bottom: 0;
	overflow: hidden;
	background: url(../image/wzfbbbg.gif) repeat-x center bottom;
}

.information_list span {
	display: inline;
	float: none;
	text-align: left;
	padding-right: 0px;
}

.information_list {
	border-collapse: collapse;
	border-spacing: 0;
}
</style>
  
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.js">
</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/easyui-1.3/jquery.easyui.min.js">
</script>
	 
		<script type="text/javascript" src="<%=scriptPath%>/common.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/global.js">
</script>
		<script type="text/javascript"
			src="<%=scriptPath%>/Message_${locale}.js">
</script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js">
</script>
		<script type="text/javascript"
			src="<%=scriptPath%>/My97DatePicker/WdatePicker.js">
</script>

		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js">
</script>
		<script language="javascript" src="<%=path%>/upload/jquery.blockUI.js">
</script>



	</head>
	<base target="_self" />
	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js">
</script>
		<div class="information_top" style="margin-top:70px;">
			<div class="windows_title">
				新建会议通知
				<div class="information_list_choose_pagedownnew" align="right">
					<tr align="right">
						<td align="right" width="10%">
							<input type="button" class="information_list_choose_button9"
								value="保存草稿" id="save" />
							<input type="button" class="information_list_choose_button9"
								value="发送通知" id="send" />
						</td>
					</tr>
				</div>
			</div>

		</div>
		<div class="information_out" id="information_out">
			<div id="tabHead" class="top_nav">
			</div>
			<s:form id="formid" name="formid"
				action="/noticeconference/noticeConference!save.action"
				theme="simple" enctype="multipart/form-data" target="hideFrame">
				<iframe id="hideFrame" name="hideFrame"
					style="width: 0; height: 0; display: none;"></iframe>
				<!--<web:htabpanel var="t" tabHeadRenderer="tabHead"
					tabBodyRenderer="tabBody" showMenu="true" tabHeadWidth="65">
					<web:tab id="tab2" text="基本信息" closeAble="false" />
					  <web:tab id="tab1" text="通知正文" closeAble="false" />
				</web:htabpanel>-->
				<div id="tabBody">
					<!--<div id="_tab1">
						<input type="hidden" id="depId" name="depId" value="">
						<input type="hidden" id="needChildren" name="needChildren"
							value="0" />

						<s:hidden name="isExitWord" id="isExitWord"></s:hidden>
						<s:hidden name="state" id="state"></s:hidden>
						<s:hidden name="userName" id="userName"></s:hidden>
						<s:hidden name="wordConferTitle" id="wordConferTitle"></s:hidden>
						<s:hidden name="wordConferTime" id="wordConferTime"></s:hidden>
						<s:hidden name="wordConferContent" id="wordConferContent"></s:hidden>
						<s:hidden name="wordConferenceInvite" id="wordConferenceInvite"></s:hidden>
						<s:hidden name="wordConferAttend" id="wordConferAttend"></s:hidden>
						<s:hidden name="wordConferAddr" id="wordConferAddr"></s:hidden>
						<s:hidden name="wordConferTerm" id="wordConferTerm"></s:hidden>
						<s:file id="wordDoc" name="wordDoc" cssStyle="display:none;"></s:file>

						<table class="information_list" style="width: 99%; height: 600px;"
							cellspacing="0" cellpadding="0" style="vertical-align: top;">
								<tr>
							<td>
								<textarea id="contence" name="contence"
									style="display: none">												 
											</textarea>
								<IFRAME ID="eWebEditor1"
									src="<%=path%>/common/ewebeditor/ewebeditor.htm?id=contence&style=coolblue"
									frameborder="0" scrolling="no" width=100%; height=500px;></IFRAME>
							</td>
						</tr>
						</table>
					</div>-->
					
					<div id="_tab2">
						<br>
						<table class="information_list" style="width: 99%" cellspacing="0"
							cellpadding="0">
							<tr>
								<td align="right">
									<font color="#FF0000">*</font> 会议标题：
								</td>
								<td colspan="3" class="dxktslx">
									<input class="dxkinptext" id="conferenceName"
										name="model.conferenceTitle" type="text" style="width: 96%"
										value="${model.conferenceTitle}" required="true"
										requiredMsg="getText(errors_required, ['会议名称'])" />
								</td>
								
							</tr>
							<tr>
							 	<td align="right" width="200px">
									 会议地点：
								</td>
								<td class="dxktslx" width="450px">

									<input class="dxkinptext" id="conferenceAddr"
										name="model.conferenceAddr" type="text" style="width: 248px"
										value="${model.conferenceAddr}" required="true"
										requiredMsg="getText(errors_required, ['会议地点'])" />
								</td>
								<td align="right" width="200px">
									 会议时间：
								</td>
								<td class="dxktslx"  width="450px">
									<input class="dxkinptext" id="conferenceStime"
										class="information_out_input readOnly" style="width: 42%"
										readonly="readonly" title="单击选择时间"
										name="model.conferenceStime" type="text"										 
										required="true" 
										requiredMsg="getText(errors_required, ['会议开始时间'])" />
									<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false"
										id="conferenceStime" />
									-
									<input class="dxkinptext" id="conferenceEtime"
										class="information_out_input readOnly" style="width: 42%"
										readonly="readonly" title="单击选择时间"
										name="model.conferenceEtime" type="text"
										value="<s:date name="model.conferenceEtime" format="yyyy-MM-dd HH:mm" />"
										required="true"
										requiredMsg="getText(errors_required, ['会议结束时间'])" />
									<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false"
										id="conferenceEtime" />
								</td>
							</tr>
							<tr>
								<td align="right">
									 报名截止时间：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="conferenceRegendtime"
										class="information_out_input readOnly" readonly="readonly"
										title="单击选择时间" style="width: 248px"
										name="model.conferenceRegendtime" type="text" onfocus="endTime()"
										value="<s:date name="model.conferenceRegendtime" format="yyyy-MM-dd HH:mm" />"
										required="true"
										requiredMsg="getText(errors_required, ['报名截止时间'])" />
									<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false"
										id="conferenceRegendtime" />
								</td>
							   <td align="right">
									 会议类型：
								</td>
								<td class="dxktslx">
									<s:select name="conTypeId" readonly="readonly"
										list="#request.typeMap" listKey="key" listValue="value"
										style="width:89%" />
								</td>
							</tr>
							<tr>

							</tr>
							<tr>
								<!--  <td align="right">
									<font color="#FF0000"></font> 主&nbsp;持&nbsp;人：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="conferenceHost"
										name="model.conferenceHost" type="text" style="width: 248px"
										value="${model.conferenceHost}" required="true"
										requiredMsg="getText(errors_required, ['主持人'])" />
								</td>-->
								<td align="right">
									<font color="#FF0000"></font> 参会人员：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="conferenceAttenusers"
										name="model.conferenceAttenusers" type="text"
										style="width: 248px" value="${model.conferenceAttenusers}"
										required="true" digits="true"
										requiredMsg="getText(errors_required, ['参会人员'])" digitsMsg="getText(errors_digits, ['参会人员'])" />
								</td>
								<td align="right">
									<font color="#FF0000"></font> 召开会议领导：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="conferenceleader"
										name="model.conferenceAttenleaders" type="text"
										style="width:89%" required="true"
										requiredMsg="getText(errors_required, ['召开会议领导'])" />
								</td>
							</tr>

							<tr>
								<td align="right">
									<font color="#FF0000"></font> 联&nbsp;系&nbsp;人：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="conferenceUser"
										name="model.conferenceUser" type="text" style="width: 248px"
										value="${model.conferenceUser}" required="true"
										requiredMsg="getText(errors_required, ['联系人'])" />
								</td>
								<td align="right">
									<font color="#FF0000"></font> 联系电话：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="conferenceUsertel"
										name="model.conferenceUsertel" type="text"
										style="width: 89%" value="${model.conferenceUsertel}"
										required="false" 
										phone="true" 
										required="false" phoneMsg="message_user_telephone"/>
								</td>
							</tr>
							<tr>
								<td align="right">
									<font color="#FF0000"></font> 承办单位：
								</td>
								<td colspan="3">
									<textarea class="dxkttar" id="conferenceUndertaker"
										name="model.conferenceUndertaker" rows="2" style="width: 96%"></textarea>
								</td>

							</tr>
							<tr>
								<td valign="middle" align="right">
									通知接收单位：
								</td>
								<td colspan="3">
									<textarea class="dxkttar" rows="5" style="width: 96%" readonly="true"
										byteMaxLength="250" name='orgNames' id="orgNames" 
										onClick="addDept();"></textarea>
									<input type="hidden" name="depIds" id="depIds" />
									<!-- 初始化已选人员使用 -->
									<input id="handleactor" name="handleactor" type="hidden" />
								</td>
							</tr>
							<tr>
								<td valign="bottom" align="right">
									上传会议材料：
								</td>
								<td colspan="3" style="font-size: 12px; font-weight: bolder;">
									<div style="float: left; width: 330px">
										<input type="file" id="file" name="attachs" size="37"
											id="attachs" class="multi" />
									</div>
									<font color="#FF0000">（附件类型：DOC|DOCX|PDF）</font>
								</td>
							</tr>
						</table>
					</div>

				</div>
			</s:form>
		</div>

		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="formid"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js">
</script>
<script type="text/javascript">
    var   message_user_telephone="请输入正确的电话号码(电话或者手机).";
//人员选择界面调用的方法，返回人员信息
function setSelectedData(selectedData) {

	var returnValue = selectedData.join("|");
	//returnValue:返回的人员信息
	//格式:off808081335428e7013354ca2cc00042,省政府|u402882a0324d112b01324d26b5960005$ff808081335428e7013354ca2cc00042,鹿心社
	//说明:以"|"分割，'off808081335428e7013354ca2cc00042,省政府'表示一条数据  ,off808081335428e7013354ca2cc00042以'o'开头，表示发给部门的
	//u402882a0324d112b01324d26b5960005$ff808081335428e7013354ca2cc00042以'u'开头，表示发给个人的，'$'后面的表示用户所在部门ID

	//初始化已选人员使用
	document.getElementsByName("handleactor")[0].value = returnValue;
	var temp = "";
	var temp_id = "";
	if (returnValue != "") {
		for ( var i = 0; i < selectedData.length; i++) {
			if (i > 0) {
				temp += ",";
				temp_id += ",";
			}
			var name = selectedData[i].split(",")[1];
			var id = selectedData[i].split(",")[0];
			temp += name;
			if (id.indexOf('$') > 0) {
				var uid = id.split("$")[0];
				temp_id += "4|" + uid.substring(1);
			} else {
				temp_id += "1|" + id.substring(1);
			}
		}
	}
	$("#orgNames").val(temp);
	$("#depIds").val(temp_id);

}

/**
 * 添加下发单位
 * 
 * */

function addDept() {
var recvOrg = "";
var recvOrgId = "";
	///  var url = "<%=path%>/noticeconference/noticeConference!assignDeptListTree.action";	 
	var ret = selectOrg();
 
	if (!ret) {
		return;
	}
	
	var orgArr;
	var orgIdArr;
	var orgId;
	if (ret[0].indexOf("，") != -1) {
		orgArr = ret[0].split("，");
		orgIdArr = ret[1].split("，");
		if (orgArr != null) {
			for ( var i = 0; i < orgArr.length; i++) {
				if (recvOrg.indexOf(orgArr[i]) == -1) {
					recvOrg += orgArr[i] + ";";
					recvOrgId += orgIdArr[i] + ",";
				}
			}
		}
	} else {
	  
		if (recvOrg.indexOf(ret[0]) == -1) {
			recvOrg += ret[0] + ";";
			recvOrgId += ret[1] + ",";
		}
	}

	//if (recvOrg) {
		$("#orgNames").val(recvOrg);
		$("#depIds").val(recvOrgId);
	//}
}

function selectOrg() {
	var deptIds = $("#depIds").val();
	var ret = window
			.showModalDialog(
					scriptroot
							+ "/noticeconference/noticeConference!orgTree.action?depIds="
							+ deptIds,
					window,
					'dialogWidth:420pt ;dialogHeight:370pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
	if (ret)
		return ret;
}
/**
 * 清除下发单位
 * 
 * */
function clearDept() {
	$("#conferenceSendDeptNames").val("");
	$("#conferenceSendDeptCodes").val("");
	recvOrg = "";
	recvOrgId = "";
}

var message_user_telephone = "请输入正确的电话号码，号码前需要加区号.";
var message_user_customReg = "请输入正确的邮政编码.";
//匹配中国邮政编码(6位)   
function ispostcode(str) {
	var reg = /^\d{6}$/;
	var result = str.match(reg);
	if (result == null)
		return false;
	return true;
}
$("#save").click(function() {
  if (validator.form()){
  $(this).attr("disabled",true);
  $("#send").attr("disabled",true);
  $('#formid').form('submit',{
						   url: '<%=root%>/noticeconference/noticeConference!save.action',
						   onSubmit: function(){
							return validata1() ;
						},
						success: function(data){
							 window.close();  
						}						
			 });  
			 
			 }

return ;
	if (validator.form()) {
	
		var url='<%=root%>/noticeconference/noticeConference!save.action';
	 
		
		
		var ret = TANGER_OCX_OBJ.SaveToUrl(url,
	                                           "wordDoc",
	                                           "",
	                                           "newdoc.doc",
	                                           "formid"); 
		if(ret=="ok"){
			alert("会议通知草稿保存成功！！！");
			window.close();
		}else{
			return false;
		}	 
	}

});
$("#send")
		.click(
				function() {
					var ids = $("#depIds").val();
					var orgNames=$("#orgNames").val();
					if (orgNames == "") {
						alert("请先选择通知接收单位！");
						$("#depIds").val("");
						return;
					}
					var flag = validata1();
					if(flag==true){
					if (confirm("确定发送会议通知？")) {
					 $(this).attr("disabled",true);
                    $("#save").attr("disabled",true);
					 $('#formid').form('submit',{
						   url: '<%=root%>/noticeconference/noticeConference!saveAndSend.action',
						   
						success: function(data){				      
							 window.close();  
							 window.dialogArguments.callback1(1,'');  
						}						
			 });  

				return ;
					
					    if (validator.form()) {
	
									var url='<%=root%>/noticeconference/noticeConference!saveAndSend.action';
									var ret = TANGER_OCX_OBJ.SaveToUrl(url,
								                                           "wordDoc",
								                                           "",
								                                           "newdoc.doc",
								                                           "formid"); 
									if(ret=="ok"){
										alert("会议通知草稿保存成功！！！");
										window.close();
									}else{
										return false;
									}
								 
								}			
					 
					}
					}
				});

function validata1() {
	//return validator.form();
	//alert("qqqqqqqqqqqqq");
	if (validator.form()){
	if ($("#conferenceName").val() == "") {
		alert("【会议标题】不能为空");
		 $("#save").attr("disabled",false);
         $("#send").attr("disabled",false);
		return false;
	}
	if ($("#conferenceName").val().length > 50) {
		alert("【会议标题】长度不能超过50");
		 $("#save").attr("disabled",false);
         $("#send").attr("disabled",false);
		return false;
	}
 
	/* if ($("#conferenceAddr").val() == "") {
		alert("【会议地点】不能为空");
		$("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
		return false;
	} */
	if ($("#conferenceAddr").val().length >100) {
		alert("【会议地点】长度不能超过100");
		$("#save").attr("disabled",false);
    	$("#send").attr("disabled",false);
		return false;
	}
	/* if ($("#conferenceStime").val() == "") {
		alert("【开始日期】不能为空");
		$("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
		return false;
	} */
	
	if ($("#conferenceAttenusers").val().length >4) {
		alert("【参会人员】不能超过4位数");
		$("#save").attr("disabled",false);
    	$("#send").attr("disabled",false);
		return false;
	}
	
	if ($("#conferenceleader").val().length >500) {
		alert("【召开会议领导】长度不能超过500");
		$("#save").attr("disabled",false);
    	$("#send").attr("disabled",false);
		return false;
	}
	
	if ($("#conferenceUser").val().length >15) {
		alert("【联系人】长度不能超过15");
		$("#save").attr("disabled",false);
    	$("#send").attr("disabled",false);
		return false;
	}
	
	
	if ($("#conferenceUndertaker").val().length >100) {
		alert("【承办单位】长度不能超过100");
		$("#save").attr("disabled",false);
    	$("#send").attr("disabled",false);
		return false;
	}
	/*if ($("#conferenceEtime").val() == "") {
		alert("【结束日期】不能为空");
		$("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
		return false;
	}*/
	/* if ($("#conferenceRegendtime").val() == "") {
		alert("【报名截止日期】不能为空");
		$("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
		return false;
	} */

	/*
	if($("#conferenceleader").val() == ""){
	alert("【出席领导】不能为空");
	   return false;
	}
	if($("#conferenceHost").val() == ""){
	alert("【主持人】不能为空");
	return false;
	}
	
	if($("#conferenceUndertaker").val() == ""){
	alert("【承办单位】不能为空");
	   return false;
	}
	
	if($("#conferenceUser").val() == ""){
	alert("【联系人】不能为空");
	return false;
	}
	if($("#conferenceUsertel").val() == ""){
	alert("【联系人电话】不能为空");
	return false;
	}
	if($("#conferenceAttenusers").val() == ""){
	alert("【参会人数】不能为空");
	return false;
	}	 
	 */
	//开始日期必须比结束日期早，报名截止日期要比会议开始日期早，需要控制 验证。	
	//Bug序号： 0000004550 
	var conferenceStime = new Date($("#conferenceStime").val().replace(/-/g,
			"/"));//开始日期
	var conferenceEtime = new Date($("#conferenceEtime").val().replace(/-/g,
			"/"));//结束日期
	var conferenceRegendtime = new Date($("#conferenceRegendtime").val()
			.replace(/-/g, "/"));//截止报名日期
	if (conferenceEtime.getTime() - conferenceStime.getTime() < 0) {
		alert("【开始时间】必须比【结束时间】早");
		$("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
		return false;
	}
	if (conferenceStime.getTime() - conferenceRegendtime.getTime() < 0) {
		alert("【报名截止时间】必须比【开始时间】早");
		$("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
		return false;
	}
	}else{
	 $("#save").attr("disabled",false);
     $("#send").attr("disabled",false);
	   return false;
	}
	return true;

}

function endTime(){
	var conferenceStime = $("#conferenceStime").val();
	if(conferenceStime!=""){
		$("#conferenceRegendtime").val(conferenceStime);
	}
}


</script>

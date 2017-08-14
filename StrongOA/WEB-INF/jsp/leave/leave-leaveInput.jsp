<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/nrootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<base target=_self>
		<title><s:if test="model.preschid == null">新建</s:if> <s:else>修改</s:else>
		</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=themePath%>/css/dxxk.css" />
		<script type="text/javascript" src="<%=root%>/uums/js/md5.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global.js"></script>
		<script type="text/javascript"
			src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript"
			src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<style type="text/css">
html,body {
	height: 100%;
	overflow: hidden;
}
</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="information_top">
			<div class="windows_title">
				请假理由
			</div>
		</div>
		<div class="information_out" id="information_out"
			style="overflow-y: scroll">
			<form id="myform"  action="<%=root%>/leave/leave!cancleLeave.action" name="myform" method="post" enctype="multipart/form-data">
				<input type="hidden" id="reportingId" name="model.reportingId"
					value="${reportingId}">
				<input type="hidden" id="state" name="state"
					value="${state}">
				<div class="dxxk">
					<div class="dxxkt" style="width: 400">
						<table width="100%">
							<tr>
								<td align="right" width="80">
									<span></span>会议名称：
								</td>
								<td  class="dxktslx">
									<input class="dxkinptext" id="conferenceName" name="conferenceName"
										type="text" byteMaxLength="250" readonly="readonly"
										
										value="${conName}" 
										 />
								</td>
							</tr> 
							<tr>
								<td align="right">
									会议时间：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="begintime"
										class="information_out_input readOnly" readonly="readonly"
										title="单击选择时间" name="begintime" type="text"
										value="<s:date name="conStime" format="yyyy-MM-dd HH:mm" />" 
										 />
									-
									<input class="dxkinptext" id="endtime"
										class="information_out_input readOnly" readonly="readonly"
										title="单击选择时间" name="endtime" type="text"
										value="<s:date name="conEtime" format="yyyy-MM-dd HH:mm" />" 
										 />
								</td>
							</tr>
							<tr>
								<td align="right" width="80">
									<span></span>请假人：
								</td>
								<td  class="dxktslx">
									<input class="dxkinptext" id="perName" name="perName"
										type="text" byteMaxLength="250" readonly="readonly"
										
										value="${model.conferee.personName}" 
										 />
								</td>
							</tr> 
							<tr>
								<td align="right">
									<span><font color="red">*</font></span>请假时间：
								</td>
								<td class="dxktslx">
									<input class="dxkinptext" id="leaveBegintime"
										class="information_out_input readOnly" 
										readonly="readonly" title="单击选择时间"
										name="leaveBegintime" type="text"
										required="true"
										requiredMsg="getText(errors_required, ['请假开始时间'])" />
									<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false"
										id="leaveBegintime" />
									-
									<input class="dxkinptext" id="leaveEndtime"
										class="information_out_input readOnly" 
										readonly="readonly" title="单击选择时间"
										name="leaveEndtime" type="text"
										required="true"
										requiredMsg="getText(errors_required, ['请假结束时间'])" />
									<web:datetime format="yyyy-MM-dd HH:mm" readOnly="false"
										id="leaveEndtime" />
								</td>
							</tr>													
							<tr>
								<td class="dxktslx" valign="top" align="right">
									<span><font color="red">*</font></span>请假原由：
								</td>
								<td >
									<textarea class="dxkttar" rows="3" byteMaxLength="250"
										id="leavereason" name="leavereason"
										required="true"
										byteMaxLengthMsg="'请确保输入的长度不大于250位(一个中文字算2位).'"></textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>
		</div>
		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="提交" name="save" id="save" />
			<input type="button" class="information_list_choose_button9"
				value="取消" name="cancle" id="cancle" />
		</div>
		</form>
		<div id="mask"></div>
		<web:validator errorDisplayContainer="information_out"
			errorElement="div" submitTip="true" name="validator" formId="myform"></web:validator>
	</body>
</html>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>
<script type="text/javascript">
var s='${reportingId}';

var ss='${state}';
//alert(ss);
	$("#save").click(function(){
		var reportingId='${reportingId}';
		var state='${state}';
		var leaveBegintime=$("#leaveBegintime").val();
		var leaveEndtime=$("#leaveEndtime").val();
		var leavereason=$("#leavereason").val();
	 	if(validator.form()){
	 		if ($("#leaveBegintime").val() == "") {
				alert("【开始日期】不能为空");
				return false;
			}
			if ($("#leaveEndtime").val() == "") {
				alert("【结束日期】不能为空");
				return false;
			}
			//if ($("#leavereason").val() == "") {
			//	alert("【请假原由】不能为空");
			//	return false;
			//}
			var cStime = new Date($("#begintime").val().replace(/-/g,
				"/"));//会议开始日期
			var cEtime = new Date($("#endtime").val().replace(/-/g,
				"/"));//会议结束日期
			//开始日期必须比结束日期早，报名截止日期要比会议开始日期早，需要控制 验证。	
			var lStime = new Date($("#leaveBegintime").val().replace(/-/g,
				"/"));//开始日期
			var lEtime = new Date($("#leaveEndtime").val().replace(/-/g,
				"/"));//结束日期
			
			if (lEtime.getTime() - lStime.getTime() < 0) {
				alert("【请假开始时间】必须比【请假时间】早");
				return false;
			}
				
			if ( lStime.getTime()-cStime.getTime() < 0) {
				alert("【请假开始时间】必须在会议开始之后");
				return false;
			}
			if ( lStime.getTime()-cEtime.getTime() > 0) {
				alert("【请假开始时间】必须在会议结束之前！");
				return false;
			}
			if ( lEtime.getTime()-cStime.getTime() > 0) {
				alert("【请假结束时间】必须在会议开始之后！");
				return false;
			}
			if ( lEtime.getTime()-cEtime.getTime() > 0) {
				alert("【请假结束时间】必须在会议结束之前！");
				return false;
			}
			
			 $.post("<%=root%>/leave/leave!cancleLeave.action",{
				"reportingId":reportingId,
				"state":state,
				"leaveBegintime":leaveBegintime,
				"leaveEndtime":leaveEndtime,
				"leavereason":leavereason
			},function(data){
				if(data=="0"){
					window.close();
					window.dialogArguments.callback();
				}
				
			});
		}
	});
	
	$("#cancle").click(function(){
		window.close();
	});
	
</script>
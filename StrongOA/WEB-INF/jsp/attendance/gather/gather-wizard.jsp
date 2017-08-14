<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<title>提交流程向导</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    	<script src="<%=path%>/oa/js/workflow/workflow.js" type="text/javascript"></script>
    	<style media="screen" type="text/css">
    		.hand {cursor:pointer;}
    	</style>
    <script type="text/javascript">
        var step = 1;
		//初始化默认的一些信息,如默认的提醒内容和默认的标题
		function initialDefaultInfo(workflowName){
			var strNow = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%>";
			if($("#businessName").val() == ""){
				$("#businessName").val(workflowName);
			}
			//默认的提醒内容
	        $("#handlerMes").val("考勤汇总管理转交提醒：\n"+$("#businessName").val()+"("+strNow+")");
		}
    </script>
	</head>
	<base target="_self">
	<body class="contentbodymargin" scroll="auto">
	<s:form id="form" name="form" action="/attendance/gather/gather!handleWorkflow.action" method="post">
	<!-- 电子表单模板id -->
	<s:hidden id="formId" name="formId"></s:hidden>
	<!-- 标示调用消息接口的模块类型 -->
	<input type="hidden" name="moduleType" value="<%=GlobalBaseData.MSG_GZCL %>"/>
	<!-- 提醒内容:定义在标签中,详见：RemindTag.java
	<input type="hidden" name="handlerMes" id="handlerMes" /> -->
	<!-- 业务数据标题 -->
	<s:hidden id="businessName" name="businessName"></s:hidden>
	<!-- 业务数据ID:兼容从草稿箱中提交 -->
	<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
	<!-- 迁移线名称 -->
	<s:hidden id="transitionName" name="transitionName"></s:hidden>
	<!-- 任务处理人 -->
	<s:hidden id="strTaskActors" name="strTaskActors"></s:hidden>
	<!-- 提醒方式 -->
	<s:hidden id="remindType" name="remindType"></s:hidden>
	<!-- 电子表单数据 -->
	<s:hidden id="formData" name="formData"></s:hidden>
	<s:hidden id="workId" name="workId"></s:hidden>
    <s:hidden name="dictId"></s:hidden>
		<div id="contentborder">
			<div id="step1"
				style="padding: 15px 15px 15px 15px; font-size: 12px;">
				<fieldset>
					<legend> 
						第一步：选择流程 
					</legend>
					<div style="padding: 15px 15px 15px 15px; text-align: left;">
						请选择工作处理流程：
						<div>
							<hr>
						</div>
						<div>
							<s:iterator value="workflows" status="status">
								<input type="radio"  onchange="changeIframe();"  id="<s:property value="workflows[#status.index][1]" />" name="workflowName" value="<s:property value="workflows[#status.index][0]"/>">
								<s:property value="workflows[#status.index][0]" />&nbsp;&nbsp;&nbsp;&nbsp;<span class="hand"
									onclick="workFlowView('<s:property value="workflows[#status.index][1]" />');">预览</span>
								<br>
							</s:iterator>
						</div>
					</div>
				</fieldset>
			</div>
			<div id="step2" style="padding: 15px 15px 15px 15px; font-size: 12px;display:none;">
				<fieldset>
					<legend>
						第二步：下一步处理
					</legend>
					<div style="padding: 15px 15px 15px 15px;text-align: left;">
						<fieldset>
							<legend>
								选择步骤
							</legend>
							<div style="padding: 15px 15px 15px 15px; text-align: left;">
								
								<div id="nextstep">
								&nbsp;
								</div>
							</div>
						</fieldset>
						<br>
						<fieldset>
							<legend>
								选择下一步处理人员
							</legend>
								<iframe id="chooseUser" name="chooseUser" frameborder="0" width="100%" title="选择处理人员"></iframe>
							<div id="users" style="padding: 15px 15px 15px 15px;">
							</div>
						</fieldset>
						<br>
						<fieldset>
						<legend>
							填写意见
						</legend>
						<div style="padding: 15px 15px 15px 15px; text-align: left;">
							<div>
								<b>我的意见：</b>
								 <select id="yj" style="width:300px;">
									<option value="0">&lt;选择以前的意见&gt;</option>
									<s:iterator value="ideaLst">
										<option value="${dictItemName }">${dictItemName }</option>
									</s:iterator>
								</select>
								<span class="hand" onclick="javascript:workFlowView();">点击查看流程图</span>
							</div>
							<div>
								<textarea cols="72" id="suggestion" name="suggestion" rows="5" wrap="on"></textarea>
							</div>
						</div>
					</fieldset>
				</div>
				<!-- 提醒方式标签 -->
		 		<strong:remind isDisplayContent="false" isDisplayInfo="false" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
			</fieldset>
		</div>
			<div style="width: 100%;" align="center">
				<!-- 第一次进入此页面,只显示下一步按钮和取消按钮 -->
				<div id="btn_first" style="display: block;">
					<span><input type="button" id="next" value="下一步" onclick="nextStep();" class="input_bg"></span>
					<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
				</div>
				<!-- 选择流程以后,点击下一步 -->
				<div id="btn_second" style="display: none;">
					<span><input type="button" id="last" value="上一步" onclick="lastStep();" class="input_bg"></span>
					<span><input type="button" id="next" value="完成" onclick="doSubmit();" class="input_bg"></span>
					<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
				</div>
				<!-- 选择好迁移线,并选择好任务处理人 -->
				<div id="btn_three" style="display: none;">
					<span><input type="button" id="last" value="上一步" onclick="lastStep();" class="input_bg"></span>
					<span><input type="button" id="finish" value="完成" onclick="doSubmit();" class="input_bg"></span>
					<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
				</div>
				
			</div>
		</div>
		</s:form>
	</body>
</html>
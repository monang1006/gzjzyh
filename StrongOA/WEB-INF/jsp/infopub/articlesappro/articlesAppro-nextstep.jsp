<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>提交下一步流程</title>
    <link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"  type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script src="<%=path%>/oa/js/workflow/workflow.js" type="text/javascript"></script>
    <script type="text/javascript">    
    	var step = 2; //相当于senddoc-wizard.jsp中的第二步页面,为了代码重用,这里初始化为2 
    </script>
  </head>
  <base target="_self">
  <body class="contentbodymargin" scroll="auto">
		<s:form id="form" name="form" action="/infopub/articlesappro/articlesAppro!handleNextStep.action" method="post">
			  <!-- 标示调用消息接口的模块类型 -->
			  <input type="hidden" name="moduleType" value="<%=GlobalBaseData.MSG_GZCL %>"/>
			  <!-- 流程实例id,用于查看流程图 -->
			  <s:hidden id="instanceId" name="instanceId"></s:hidden>
			  <!-- 电子表单模板id -->
			  <s:hidden id="formId" name="formId"></s:hidden>
			  <!-- 任务id -->
			  <s:hidden id="taskId" name="taskId"></s:hidden>
			  <!-- 迁移线名称 -->
			  <s:hidden id="transitionName" name="transitionName"></s:hidden>
			  <!-- 任务处理人 -->
			  <s:hidden id="strTaskActors" name="strTaskActors"></s:hidden>
			  <!-- 提醒方式 -->
			  <s:hidden id="remindType" name="remindType"></s:hidden>
			  <!-- 电子表单数据 -->
			  <s:hidden id="formData" name="formData"></s:hidden>
			  <!-- 完整的电子表单数据,含未绑定到数据库字段的控件内容,录入审批意见. 用于将数据库传输到档案中心.详见控件#GetData-->
			  <s:hidden id="fullFormData" name="fullFormData"></s:hidden>
			  <!-- 字典类主键 -->
			  <s:hidden name="dictId"></s:hidden>
   			  <div id="contentborder">
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
								选择人员
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

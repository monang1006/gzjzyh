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
    	var step = 2; //相当于work-wizard.jsp中的第二步页面,为了代码重用,这里初始化为2 
    </script>
  </head>
  <base target="_self">
  <body class="contentbodymargin" scroll="auto">
		<s:form id="form" name="form" action="/work/work!handleNextStep.action" method="post">
			  <!-- 标示调用消息接口的模块类型 -->
			  <input type="hidden" name="moduleType" value="<%=GlobalBaseData.MSG_GZCL %>"/>
			  <!-- 流程实例id,用于查看流程图 -->
			  <s:hidden id="instanceId" name="instanceId"></s:hidden>
			  <!-- 多个任务id以","分隔 -->
			  <s:hidden id="taskIds" name="taskIds"></s:hidden>
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
				<!-- 字典类主键 -->
			    <s:hidden name="dictId"></s:hidden>			  
   			  <div id="contentborder">
			      <div id="step2" style="padding: 15px 15px 15px 15px; font-size: 12px;">
			        <fieldset>
			          <legend>
			            第一步：下一步处理
			          </legend>
									<div style="padding: 15px 15px 15px 15px;text-align: left;">
			            <fieldset>
			              <legend>
			                选择步骤
			              </legend>              
			              <div style="padding: 15px 15px 15px 15px; text-align: left;">
			                <div>
			                  请选择提交步骤：&nbsp;&nbsp;&nbsp;&nbsp;
			                  <span style="cursor:hand;" onclick="javascript:viewPDImage();">点击查看流程图</span>
			                </div>
			                <div>
			                  <hr>
			                </div>
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
           				<div id="users" style="padding: 15px 15px 15px 15px;">
						</div>
			            </fieldset>
			          </div>
			      </fieldset>
			    </div>
			    
	  <!-- 提醒方式标签 -->
	  <strong:remind defaultRemindContent="工作处理转交提醒：${businessName}" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
      <div id="step3"  style="padding: 15px 15px 15px 15px; font-size: 12px;display:none;">
        <fieldset>
          <legend>
            	第二步：填写处理意见
          </legend>
          <div style="padding: 15px 15px 15px 15px; text-align: left;">
            <div>
              <b>我的意见：</b>
              <select id="yj" style="width:200px;">
					<option value="0">&lt;选择以前的意见&gt;</option>
					<s:iterator value="ideaLst">
						<option value="${dictItemName }">${dictItemName }</option>
					</s:iterator>
				</select>
              &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:hand;" onclick="javascript:annal();">处理记录</span>
            </div>
            <div>
              <textarea cols="53" id="suggestion" name="suggestion" rows="10" wrap="on"></textarea>
            </div>
          </div>
        </fieldset>
      </div>
        <!-- 第一次进入此页面,只显示下一步按钮和取消按钮 -->
		<div align="center" id="btn_second" style="display: block;">
			<span><input type="button" id="next" value="下一步" onclick="nextStep();" class="input_bg"></span>
			<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
		</div>
		<!-- 选择迁移线以后,点击下一步 -->
		<div align="center" id="btn_three" style="display: none;">
			<span><input type="button" id="last" value="上一步" onclick="lastStep();" class="input_bg"></span>
			<span><input type="button" id="finish" value="完成" onclick="doSubmit();" class="input_bg"></span>
			<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
		</div>
    </div>
    </s:form>
  </body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
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
    	//初始化默认的一些信息,如默认的提醒内容和默认的标题
		function initialDefaultInfo(){
			var strNow = "<%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%>";
			var workflowName = $("#workflowName").val();
			if($("#businessName").val() == ""){
				$("#businessName").val(workflowName);
			}
			var path = '<%=request.getParameter("fromPath")%>';
			var module = "工作流";
			if(path.indexOf("senddoc")!=-1){
				module = "发文管理";
			}else if(path.indexOf("recvdoc")!=-1){
				module = "收文管理";
			}else if(path.indexOf("inspect")!=-1){
				module = "督察督办";
			}
			//默认的提醒内容
	        $("#handlerMes").val(module+"转交提醒：\n"+$("#businessName").val()+"("+strNow+")");
		}
    </script>
  </head>
  <base target="_self">
  <body class="contentbodymargin" scroll="auto">
		<form id="form" name="form" action="<%=request.getParameter("fromPath")%>!handleWorkflow.action" method="post">
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
			  <!-- 业务数据标题 -->
			 <s:hidden id="businessName" name="businessName"></s:hidden>
			  <s:hidden id="workflowName" name="workflowName"></s:hidden>
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
				              <select id="yj" style="width:400px;">
									<option value="0">&lt;选择以前的意见&gt;</option>
									<s:iterator value="ideaLst">
										<option value="${dictItemName }">${dictItemName }</option>
									</s:iterator>
								</select>&nbsp;&nbsp;
								<%--<span style="cursor:hand;" onclick="javascript:viewPDImage();">点击查看流程图</span>
				              &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:hand;" onclick="javascript:annal();">处理记录</span>--%>
				            </div>
				            <div>
				              <textarea cols="72" id="suggestion" name="suggestion" rows="5" wrap="on"></textarea>
				            </div>
				          </div>
				        </fieldset>
			          </div>
					  <!-- 提醒方式标签 -->
					  <strong:remind isDisplayContent="false" isDisplayInfo="false" defaultRemindContent="工作流转交提醒：${businessName}" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
			      </fieldset>
			    </div>
		        <!-- 第一次进入此页面,只显示下一步按钮和取消按钮 -->
				<div align="center" id="btn_second" style="display: block;">
					<span><input type="button" id="next" value="完成" onclick="doSubmit();" class="input_bg"></span>
					<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
				</div>
				<!-- 选择迁移线以后,点击下一步 -->
				<div align="center" id="btn_three" style="display: none;">
					<span><input type="button" id="last" value="上一步" onclick="lastStep();" class="input_bg"></span>
					<span><input type="button" id="finish" value="完成" onclick="doSubmit();" class="input_bg"></span>
					<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
				</div>
    </div>
    </form>
  </body>
</html>

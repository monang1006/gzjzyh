<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程处理单</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		<script type="text/javascript">
			//查看时所有控件都要设为只读
			function initial(){
				$("#taskId").val("");//通过流程实例id得到意见,避免通过任务id去获取
				formReader = getFormReader();
				var ret = formReader.SetFormReadOnly(true);// 设置表单只读
			}
		</script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<form id="form" action="<%=request.getParameter("fromPath") %>!save.action" method="post">
			<!-- 业务数据名称 -->
			<s:hidden id="businessName" name="businessName"></s:hidden>
			<!-- 电子表单模板id -->
			<s:hidden id="formId" name="formId"></s:hidden>
			<!-- 任务id -->
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<!-- 流程实例id -->
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<s:hidden id="formAction" name="formAction"></s:hidden>
		</form>
		<div id="silverlightControlHost" style="position:relative;width: 100%;height: 100%">
		     	<object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%" id="plugin">
				  <param name="source" value="<%=path%>/FormReader/StrongFormReader.xap"/>
				  <param name="onError" value="onSilverlightError" />
				  <param name="onLoad" value="onSilverlightLoad" />
				  <param name="background" value="white" />
				  <param name="minRuntimeVersion" value="4.0.50401.0" />
				  <param name="autoUpgrade" value="true" />
				  <a href="<%=path %>/detection/lib/Silverlight.exe" style="text-decoration:none">
		 			  <img src="<%=path %>/detection/images/SLMedallion_CHS.png" alt="Get Microsoft Silverlight" style="border-style:none"/>
				  </a>
			    </object><iframe id="_sl_historyFrame" style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
		  </div>
	</body>
</html>

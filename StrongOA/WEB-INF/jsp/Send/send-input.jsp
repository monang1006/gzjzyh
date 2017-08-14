<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看表单</title>
		<LINK href="<%=path%>/frame/theme_gray/css/properties_windows.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
		    .tabletitle {
		      FILTER:progid:DXImageTransform.Microsoft.Gradient(
		                            gradientType = 0, 
		                            startColorStr = #ededed, 
		                            endColorStr = #ffffff);
		    }
		    
		    .hand {
		      cursor:pointer;
		    }
		</style>

		<script type="text/javascript">
			//查看时所有控件都要设为只读
			function initial(){
				$("#taskId").val("");//通过流程实例id得到意见,避免通过任务id去获取
				formReader = getFormReader();
				var ret = formReader.SetFormReadOnly(true);// 设置表单只读
				//将“查看原表单按钮”不设置为只读,"Button_viewformdata"为“查看原表单按钮”特有的name
				var control = formReader.GetFormControl("Button_viewformdata");
				if(control!=null){
					control.SetProperty ('Visible',false);
				}
				/*
			 * 上传领导批示单不再使用附件控件形式，去掉对附件控件为空的判断 yanjian 2012-02-24 12:56
			 * */
			 if(formReader.GetFormControl("doneSuggestion_content")){
				 if(formReader.GetFormControl("doneSuggestion_content").Value!=null
				 		&& formReader.GetFormControl("doneSuggestion_content").Value != "")
				 	{
				 		formReader.SetFormTabVisibility("attachName",true);
				 		formReader.SetFormTabVisibility("doneSuggestion",true);
				 	}
			 }
			}
			function errordo(){
					alert("系统处理该数据时发生异常,请联系管理员！");
	  				window.close();
			}
			
			function error(){
					alert("您不能处理该数据，该数据已经被处理！");
	  				window.close();
			}
			
			function success(){
				window.opener.reloadPage();//刷新父页面
  				window.close();
			}
			
			function send(){
				var width=600;
        	    var height=120;
				var msg = OpenWindow("<%=root%>/Send/send!upload.action?sendbean.senddocId="+$("#pkFieldValue").val(),width, height, window);
				if(msg == "success"){
	  				success();
	  			}else {
	  				if(msg == "error"){
	  					error();
	  				}else{
		  				if(msg == "errordo"){
		  					errordo();
		  				}
	  				}
	  			}
				/*			
				$.ajax({
					  		type:"post",
					  		dataType:"text",
					  		url:"<%=root%>/Send/send!save.action?sendbean.senddocId="+$("#pkFieldValue").val(),
					  		data:"1=1",
					  		success:function(msg){
					  			if(msg == "success"){
					  				success();
					  			}else {
					  				if(msg == "error"){
					  					error();
					  				}else{
					  					errordo();
					  				}
					  			}
					  		}
			  	});
			  	*/
			}
	    </script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;"
		onunload="resumeConSignTask();">
		<form id="form"
			action="<%=root%>/workflowDesign/action/processMonitor!save.action"
			method="post">
			<!-- 流程名称 -->
			<s:hidden id="workflowName" name="workflowName" value=""></s:hidden>
			<!-- 任务id -->
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<!-- 流程实例ID -->
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<!-- 电子表单模板id -->
			<s:hidden id="formId" name="formId"></s:hidden>
			<!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName" value="T_OA_SENDDOC"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName" value="SENDDOC_ID"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- 用户名为空 防止脚本保存 -->
			<s:hidden id="userName" value=""></s:hidden>
			<!-- PDF正文信息描述 -->
			<s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
			<!-- 电子表单数据 -->
			<s:hidden id="formData" name="formData"></s:hidden>

			<s:hidden id="formAction" name="formAction"></s:hidden>
		</form>
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0"
				cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="40">
						<table align="right">
							<tr>
								<td id="lingdaopishi">
									<a class="Operation" href="javascript:send();"> &nbsp;<img
											src="<%=root%>/images/ico/songshen.gif" width="15"
											height="15" class="img_s">发送&nbsp;</a>

								</td>
								<td width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<div id="silverlightControlHost"
							style="position:relative;width: 100%;height: 100%">
							<object data="data:application/x-silverlight-2,"
								type="application/x-silverlight-2" width="100%" height="100%"
								id="plugin">
								<param name="source"
									value="<%=path%>/FormReader/StrongFormReader.xap" />
								<param name="onError" value="onSilverlightError" />
								<param name="onLoad" value="onSilverlightLoad" />
								<param name="background" value="white" />
								<param name="minRuntimeVersion" value="4.0.50401.0" />
								<param name="autoUpgrade" value="true" />
								<a href="<%=path%>/detection/lib/Silverlight.exe"
									style="text-decoration:none"> <img
										src="<%=path%>/detection/images/SLMedallion_CHS.png"
										alt="Get Microsoft Silverlight" style="border-style:none" /> </a>
							</object>
							<iframe id="_sl_historyFrame"
								style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
						</div>
					</td>
				</tr>
				<tr>
				</tr>
			</table>
		</div>
	</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>
			CA认证
		</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			var Auth_Content = "${original_data}";//得到认证原文
			if(Auth_Content != "") {
				$("#contentborder").show();
				var DSign_Subject = document.getElementById("RootCADN").value;
				JITDSignOcx.SetCertChooseType(1);
				JITDSignOcx.SetCert("SC","","","",DSign_Subject,"");
				var code = JITDSignOcx.GetErrorCode();
				if(code!=0){
					if(code != 5102){
						alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
					}
					window.close();
				} else {
					 var temp_DSign_Result = JITDSignOcx.DetachSignStr("",Auth_Content);
					 if(JITDSignOcx.GetErrorCode()!=0){
							alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
							window.close();
					 }
					 //处理认证
					 $.post("<%=root%>/senddoc/sendDoc!doCASign.action",
					 	{original_jsp:Auth_Content,signed_data:temp_DSign_Result},function(ret){
					 	if(ret == "0"){
					 		$("#CASignInfo").val(Auth_Content + "," + temp_DSign_Result);
					 		$("form").submit();
					 	} else {
					 		alert("CA认证失败，请检查您的证书是否有效。");
					 		window.close();
					 	} 
					 });
				}	 
			} else {
				$("form").submit();
			}
			
		});
	</script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<DIV id=contentborder align=center style="display: none;">
			请稍后,正在CA认证中...
		</DIV>
		<s:form action="/senddoc/sendDoc!input.action">
			<s:hidden name="pluginInfo" id="pluginInfo"></s:hidden>		
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<input type="hidden" id="RootCADN" value="" width="30" />
			<s:hidden id="CASignInfo" name="CASignInfo"></s:hidden>
			<s:hidden id="workflowName" name="workflowName"></s:hidden>
			<s:hidden id="type" name="type"></s:hidden>
		</s:form>	
		<!-- 加载吉大正元CA认证插件 -->
		<object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488" id="JITDSignOcx" width="0" codebase="<%=path %>/common/JITDSign/JITDSign.cab#version=2,0,24,13"></object>		
	</body>
</html>
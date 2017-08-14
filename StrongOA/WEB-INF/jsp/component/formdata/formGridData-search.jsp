<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>我的请求</title>
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
    <script type="text/javascript">
    	 //得到表单模板对象
		function getFormPlugin() {
			return document.getElementById("plugin").Content;
		}
				
		//得到表单读写器
		function getFormReader() {
			return getFormPlugin().FormReader;
		}
		//控件加载完成之后调用此事件
		function onSilverlightLoad() {
			formReader = getFormReader();
			var url = formReader.FormServiceAddress + ".action";
			formReader.FormServiceAddress = url;
		}
    	//装载模板
		function loadFormTemplate() {
			var actionUri = basePath + "senddoc/eFormTemplate.action";
			document.getElementById("formAction").value = "LoadFormTemplate";
			if (formReader.LoadFormTemplate(actionUri, "form", "loadFormTemplateRequestCompleted")) {
				//方法调用成功
			} else {
				//调用失败
			}
		}
    </script>
  </head>
  <body class="contentbodymargin" scroll="no">
  <form id="form" name="form" method="post">
			<s:hidden name="formId" value="15050"></s:hidden>
			<input id="formAction" type="hidden" name="formAction" />
		</form>
    <div id="contentborder"  align="center">
          <div id="silverlightControlHost"
			style="position:relative;height: 100%">
			<object data="data:application/x-silverlight-2,"
				type="application/x-silverlight-2" width="100%" height="100%"
				id="plugin">
				<param name="source"
					value="<%=path%>/GridFormReader/StrongFormReader.xap" />
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
      
    </div>
  </body>
</html>

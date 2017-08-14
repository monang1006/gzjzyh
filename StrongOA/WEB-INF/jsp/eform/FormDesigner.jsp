<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>电子表单设计器V2.0</title>
	<%@include file="/common/include/meta.jsp" %>
	<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
	<script type="text/javascript">
        function onSilverlightError(sender, args) {
            var appSource = "";
            if (sender != null && sender != 0) {
              appSource = sender.getHost().Source;
            }
            
            var errorType = args.ErrorType;
            var iErrorCode = args.ErrorCode;

            if (errorType == "ImageError" || errorType == "MediaError") {
              return;
            }

            var errMsg = "Unhandled Error in Silverlight Application " +  appSource + "\n" ;

            errMsg += "Code: "+ iErrorCode + "    \n";
            errMsg += "Category: " + errorType + "       \n";
            errMsg += "Message: " + args.ErrorMessage + "     \n";

            if (errorType == "ParserError") {
                errMsg += "File: " + args.xamlFile + "     \n";
                errMsg += "Line: " + args.lineNumber + "     \n";
                errMsg += "Position: " + args.charPosition + "     \n";
            }
            else if (errorType == "RuntimeError") {           
                if (args.lineNumber != 0) {
                    errMsg += "Line: " + args.lineNumber + "     \n";
                    errMsg += "Position: " +  args.charPosition + "     \n";
                }
                errMsg += "MethodName: " + args.methodName + "     \n";
            }

            throw new Error(errMsg);
        }

        function getFormPlugin() {
            return document.getElementById("plugin").Content;
        }

        function getFormDesigner() {
            return getFormPlugin().FormDesigner;
        }

        function SaveFormTemplateRequestCompleted(bResult, strResult) {
            if (bResult) {
                alert("成功：" + strResult);
            }
            else {
                alert("错误：" + strResult);
            }
        }

		//mode="Save" / "SaveAs"
        function SaveFormTemplate(mode) {
            var formDesigner = getFormDesigner();
            var bRet = formDesigner.SaveFormTemplate("LoadFormTemplateRequestCompleted");
        }

        function LoadFormTemplateRequestCompleted(bResult, strResult) {
            if (bResult) {
                alert("成功：" + strResult);
            }
            else {
                alert("错误：" + strResult);
            }
        }

        function LoadFormTemplate() {
            var formDesigner = getFormDesigner();
            var bRet = formDesigner.LoadFormTemplate("LoadFormTemplateRequestCompleted");
        }
        
        //控件加载完成之后调用此事件
		function onSilverlightLoad(){
			var formDesigner = getFormDesigner();
			var url=formDesigner.FormServiceAddress + ".action";
			formDesigner.FormTemplateSaveCommand="SaveFormTemplate";
			formDesigner.FormTemplateLoadCommand="LoadFormTemplate";
			formDesigner.FormServiceAddress=url;
		}
    </script>
  </head>
  
  <body>
    <form id="form1" runat="server" style="height:100%">
	    <div id="silverlightControlHost">
	        <object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%" id="plugin">
			  <param name="source" value="<%=path%>/FormDesigner/StrongFormDesigner.xap"/>
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
    </form>
  </body>
</html>

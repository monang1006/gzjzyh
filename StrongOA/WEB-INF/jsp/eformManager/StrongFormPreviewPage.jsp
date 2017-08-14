<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" >

<head>
    <title>表单预览</title>
    <style type="text/css">
    html, body {
	    height: 100%;
	    overflow: auto;
    }
    body {
	    padding: 0;
	    margin: 0;
    }
    #silverlightControlHost {
	    height: 100%;
	    text-align:center;
    }
        #Text1
        {
            width: 629px;
        }
    </style>
    <script type="text/javascript" src="Silverlight.js"></script>
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

            var errMsg = "Unhandled Error in Silverlight Application " + appSource + "\n";

            errMsg += "Code: " + iErrorCode + "    \n";
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
                    errMsg += "Position: " + args.charPosition + "     \n";
                }
                errMsg += "MethodName: " + args.methodName + "     \n";
            }

            throw new Error(errMsg);
        }

        function getFormPlugin() {
            return document.getElementById("plugin").Content;
        }

        function getFormReader() {
            return getFormPlugin().FormReader;
        }

        function onSilverlightLoad(sender, args) {
            var formReader = getFormReader();
            var xmlFormTemplate = window.dialogArguments.document.getElementById("xmlFormTemplate").value;
            var ret = formReader.SetFormTemplate(xmlFormTemplate);
            formReader.SetFormReadOnly(true);// 设置表单只读
        }

    </script>
</head>
<body>    
    <div id="silverlightControlHost">
     	<div id="silverlightControlHost" style="position:relative;height: 100%">
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
	  </div>
</body>
</html>

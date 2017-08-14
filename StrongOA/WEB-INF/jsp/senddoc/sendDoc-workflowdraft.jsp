<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="strong" uri="/tags/web-workflow"%>
<jsp:directive.page import="com.strongit.oa.globalconfig.GlobalConfigService"/>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaGlobalConfig"/>
<%@include file="/common/include/rootPath.jsp"%>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<%
	//得到来文草稿公文签收功能是否启用
	GlobalConfigService globalConfigService = (GlobalConfigService)ServiceLocator.getService("globalConfigService");
	ToaGlobalConfig toaGlobalConfig = globalConfigService.getToaGlobalConfig("plugins_getDocs");
	String plugins_getDocs = "1";
	if(toaGlobalConfig != null){//0:不启用，1：启用
		if(toaGlobalConfig.getGlobalValue() != null){
			plugins_getDocs = toaGlobalConfig.getGlobalValue();
		}
	}
%>
<html>
	<head>
		<title>流程草稿</title>
		<%@include file="/common/include/meta.jsp"%>
		<script type="text/javascript">
			function OpenWindow(Url, Width, Height, WindowObj) {
				var ReturnStr = showModalDialog(Url, 
				                                WindowObj, 
				                                "dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
				                                "status:no;help:no;scroll:no;");
				return ReturnStr;
			}
			function getDocs(){
				var rValue=OpenWindow('<%=root%>/docafterflow/docafterflow.action', '500', '400', window);
				if(rValue=="true"){
					window.location.reload();
				}
			}
		</script>
		<style media="screen" type="text/css">
	    .tabletitle {
	      FILTER:progid:DXImageTransform.Microsoft.Gradient(
	                            gradientType = 0, 
	                            startColorStr = #ededed, 
	                            endColorStr = #ffffff);
	    }
    
    </style>
	</head>
	<body oncontextmenu="return false;" style="margin-top:5px;" >
			<strong:workflow title="总计" isPop="false" workflowType="${workflowType}"
			 href="/senddoc/sendDoc!draft.action?handleKind=${handleKind}" 
			 typeList="${workflowTypeList}" workflowMap="${workflowMap}"/>
			 <script type="text/javascript">
			var plugins_getDocs="<%=plugins_getDocs%>";
			$(document).ready(function(){
				if(plugins_getDocs == "1"){
					$("#getDocs").show();
				}
		 	 });
			   /*
			  function lbst(){//列表视图
			  	window.location.href="<%=path%>/work/work!hostedby.action";
			  }
			  */		
		</script>
	</body>
</html>

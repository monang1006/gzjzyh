<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="strong" uri="/tags/web-workflow"%>
<%--<%@include file="/common/include/rootPath.jsp"%>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
--%><html>
	<head>
		<title>新建自由流程</title>
		<%@include file="/common/include/meta.jsp"%>
		<style media="screen" type="text/css">
	    .tabletitle {
	      FILTER:progid:DXImageTransform.Microsoft.Gradient(
	                            gradientType = 0, 
	                            startColorStr = #ededed, 
	                            endColorStr = #ffffff);
	    }
    
    </style>
	</head>
	<body>
		<strong:workflow title="总计" freedomWorkflow="true"  href="/freedomworkflow/freedomWorkflow!input.action?" count="${count}" typeList="${workflowTypeList}" workflowMap="${workflowMap}"/><%--
		<script type="text/javascript">
		$(document).ready(function(){
					var flag = "<%=request.getParameter("workflowType")== null?"none":""%>";	
			  		var content = $("#contentborder table:eq(0) TBODY tr:eq(0)").html();
			  		content = content+"<td align=\"right\" class=\"tabletitle\" style=\"display:"+flag+";\">"
			  		+"<input style=height:20px type=button class=Operation onclick=lbst() value=返回列表>"
                  	+"</td>";
			  		$("#contentborder table:eq(0) TBODY tr:eq(0)").html(content);
			  });
			   function lbst(){//列表视图
			  	window.location.href="<%=path%>/senddoc/sendDoc!draft.action?workflowType=${workflowType}&formId=${formId}&handleKind=${handleKind}";
			  }	
		</script>
	--%></body>
</html>

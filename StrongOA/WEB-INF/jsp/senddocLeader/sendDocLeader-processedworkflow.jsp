<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="strong" uri="/tags/web-workflow"%>
<%@include file="/common/include/rootPath.jsp"%>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<html>
	<head>
		<title>已办事宜</title>
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
	<body  style="margin-top:5px;" >
		<strong:workflow title="总计" isPop="false" href="/senddocLeader/sendDocLeader!processed.action?handleKind=${handleKind}&state=${state}&filterSign=${filterSign}" typeList="${workflowTypeList}" workflowMap="${workflowMap}"/>
		<script type="text/javascript">
		/*  */
			 $(document).ready(function(){
			  		var content = $("#contentborder table:eq(0) TBODY tr:eq(0)").html();
			  		content = content+"<td align=\"right\" class=\"tabletitle\">"
			  		+"<input style=height:24px type=button class=Operation onclick=lbst() value=列表视图>"
                  	+"</td>";
			  		$("#contentborder table:eq(0) TBODY tr:eq(0)").html(content);
			  });
			  
			  function lbst(){//列表视图
			  	window.location.href="<%=path%>/senddocLeader/sendDocLeader!listprocessed.action?state=${state}&workflowType=${workflowType}&handleKind=${handleKind}&filterSign=${filterSign}&excludeWorkflowType=${excludeWorkflowType}";
			  }		
			
	   </script>
	</body>
</html>

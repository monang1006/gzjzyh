<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="strong" uri="/tags/web-workflow"%>
<%@include file="/common/include/rootPath.jsp"%>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<html>
	<head>
		<title>公文回收站</title>
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
		<strong:workflow title="总计" isPop="false" href="/senddoc/sendDoc!repeal.action?state=${state}" typeList="${workflowTypeList}" workflowMap="${workflowMap}"/>
		<script type="text/javascript">
		$(document).ready(function(){
	  		var content = $("#contentborder table:eq(0) TBODY tr:eq(0)").html();
	  		content = content+"<td width=\"4\"><img src=\"<%=frameroot%>/images/bt_l.jpg\"/></td>"
	  		+"<td class=\"Operation_list\" width=\"6\" onclick=\"lbst();\">"
	  		+"<img src=\"<%=root%>/images/operationbtn/Statistics_view.png\"/>&nbsp;列&nbsp;表&nbsp;视&nbsp;图&nbsp;</td>"
	  		+"<td width=\"4\"><img src=\"<%=frameroot%>/images/bt_r.jpg\"/></td>"
          	+"<td width=\"5\"></td>";
	  		$("#contentborder table:eq(0) TBODY tr:eq(0)").html(content);
	  });
	  
	  function lbst(){//列表视图
	  	window.location.href="<%=path%>/work/work!listRepeal.action?state=${state}&workflowType=${workflowType}&handleKind=${handleKind}&filterSign=${filterSign}&excludeWorkflowType=${excludeWorkflowType}";
	  }		
			  
	   </script>
	</body>
</html>

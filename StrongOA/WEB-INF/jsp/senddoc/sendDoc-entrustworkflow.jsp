<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="strong" uri="/tags/web-workflow"%>
<%@include file="/common/include/rootPath.jsp"%>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<html>
	<head>
		<title>
			<%
				String title = "我委派的流程";
				String assignType = (String)request.getAttribute("assignType");
				if("0".equals(assignType)){
					title = "我委派的流程";
				}else if("1".equals(assignType)){
					title = "我指派的流程";
				}
			%>
		</title>
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
	<body oncontextmenu="return false;" style="margin-top:5px;" >
		<strong:workflow title="<%=title %>" isPop="false" href="/senddoc/sendDoc!entrust.action?assignType=${assignType}" typeList="${workflowTypeList}" workflowMap="${workflowMap}"/>
		 <script type="text/javascript">
		 /*
			$(document).ready(function(){
			  		var content = $("#contentborder table:eq(0) TBODY tr:eq(0)").html();
			  		content = content+"<td align=\"right\" class=\"tabletitle\"><input type=\"button\" value=\"列表视图\" onclick=\"lbst();\"></td>";
			  		$("#contentborder table:eq(0) TBODY tr:eq(0)").html(content);
			  });
			  function lbst(){//列表视图
			    var assignType ="<%=assignType%>"; 
			    var url = "";
			    if(assignType == "0"){
			    	url = "<%=path%>/work/work!entrustlist.action";
			    }else{
			    	url = "<%=path%>/work/work!assignlist.action";
			    }
			  	window.location.href=url;
			  }		
		*/
		</script>
	</body>
</html>

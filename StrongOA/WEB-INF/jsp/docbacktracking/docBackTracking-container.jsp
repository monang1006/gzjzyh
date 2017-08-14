<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>公文补录</title>

		<%@include file="/common/include/meta.jsp"%>
		<title>流程草稿</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function showDocList(workflowName,formId,workflowType){
			var url = "<%=path%>/docbacktracking/docBackTracking!gridview.action?1=1";
			var param = ("&workflowName="+encodeURI(encodeURI(workflowName)));
			var param = param + ("&formId="+encodeURI(encodeURI(formId)));
			var param = param + ("&workflowType="+encodeURI(encodeURI(workflowType)));
			$("#gridview").attr("src", (url + param));
		}
		
	</script>
	</head>

	<body class="contentbodymargin"> 
		<div id="contentborder" >
			<table style="width: 100%;height: 100%;" border="0">
				<tr style="height: 100%;">
					<td style="width: 20%">
						<div>
							<iframe name="tree" id="tree"
								src="<%=path%>/docbacktracking/docBackTracking!tree.action"
								style="width: 20%;height: 100%;" />
						</div>
					</td>
					<td style="width: 20%">
						<div>
							<iframe name="gridview" id="gridview"
								src="<%=path%>/docbacktracking/docBackTracking!doclist.action"
								style="width: 80%;height: 100%;" />
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>

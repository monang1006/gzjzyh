<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<script language="JavaScript"
	src="<%=path%>/common/js/commontab/service.js"></script>
<html>
	<head>
		<base target="_self"/>
		<%@include file="/common/include/meta.jsp"%>
		<title>审批意见信息</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<style media="screen" type="text/css">
		.tabletitle {
			FILTER: progid : DXImageTransform.Microsoft.Gradient ( 
		                             gradientType = 0,
		                              startColorStr = #ededed, 
		                              endColorStr =   #ffffff );
		}
		.hand {
			cursor: pointer;
		}
		td,th {overflow: hidden;text-overflow: ellipsis;white-space: nowrap; line-height:19px;}
		</style>
	<script type="text/javascript">
	//编辑
	function edit(aiId){
			var width=700;
			var height=400;
			var param = "?1=1&approveinfo.aiId="+aiId;
			var arg = window.showModalDialog("<%=root%>/docbacktracking/docBackTracking!editapproveinfo.action"+param, 'a', "dialogWidth ="+width+"px;dialogHeight = "+height+"px;help=0",window);
			if(arg && arg=="OK"){
				$("#myform").submit();
			}
	}
	</script>
	</head>
	<body class="contentbodymargin" >
		<div id="contentborder" align="center">
		<s:form id="myform" action="/docbacktracking/docBackTracking!approveinfolist.action">
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
		</s:form>
		${tableString }
		</div>
	</body>
</html>

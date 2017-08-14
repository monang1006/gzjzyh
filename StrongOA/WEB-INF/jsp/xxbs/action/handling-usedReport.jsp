<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>每日采用通报</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>

<style type="text/css">
#rTb{
	font-size:22px;
	text-align:center;
	font-weight:bold;
	line-height:80px;
}
.rOrg{
	font-size:18px;
	font-weight:bold;
	line-height:60px;
}
.rTitle{
	padding: 0 0 0 20px;
	font-size:14px;
	line-height:28px;
}
</style>
</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 20px;">
					<div id="rTb">每日采用通报</div>
					
					<s:iterator value="reports">
					<s:if test="top.isFirst==true">
					<div class="rOrg"><s:property value="orgName"/></div>
					</s:if>
					<div class="rTitle">
					<strong>·</strong>
					<s:property value="pubTitle"/> (采用得分：<s:property value="useScore"/> <s:if test="remarkScore!=null">批示得分：<s:property value="remarkScore"/></s:if>)</div>
					</s:iterator>
					
				</div>
				</div>
	</body>
</html>
<script type="text/javascript">

var fTitle = function(val, opts, obj) {
	var u = (obj.scoreUse == undefined) ? "" : obj.scoreUse;
	var i = (obj.scoreInstruction == undefined) ? "" : obj.scoreInstruction;
	val = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+val + "&nbsp;&nbsp;("+u+","+i+")";
	return val;
};

var fOrgName = function(val, opts, obj){
	val = val + "  (上报：${publishNum} &nbsp;&nbsp;采用：${useNum})";
	return val;
};


$(function(){
		

});

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

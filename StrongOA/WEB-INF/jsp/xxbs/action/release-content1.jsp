<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>期刊发布</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
<style type="text/css">
.relJournal{
	font-size:20pt;
	text-align:center;
	font-weight:bold;
	line-height:80px;
}
.relColumn{
	font-size:18pt;
	text-align:center;
	font-weight:bold;
	line-height:70px;
}
.relTitle{
	font-family:"楷体";
	font-size:16pt;
	text-indent: 32pt;
	font-weight:bold;
	text-align:center;
}
.relContent{
	font-family:"仿宋";
	font-size:16pt;
	text-indent: 32pt;
	font-weight:normal;
	line-height:140%;
}
.relOrg{
	font-family:"楷体";
	font-size:15pt;
	text-align:right;
	padding: 10px 10px 20px 0;
}
</style>
</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out" style="height:25px;padding:0px 0 0px 0;border-left: 0px solid #666">

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding-button: 1px; padding-left: 40px">
					<table style="background:#fff;margin:0 0 0 10px;padding:20px 10px;border:1px solid #666;" width="880px" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td class="relJournal">
							<span id="relJournal">${issue.TInfoBaseJournal.jourName}</span>
							第(<span id=relNumber>${issue.issNumber}</span>)期
							<span id="relDate"><s:date name="relDate" format="yyyy年MM月dd日" /></span>
							${orgName1}
							</td>
						</tr>
						<tr>
							<td>
							<s:iterator value="releases" status="status">
								<br/>
								<div class="relTitle" id="relTitle<s:property value='#status.count'/>">
								<s:property value="pubTitle"/>
								</div>		
								<div class="relContent" id="relContent<s:property value='#status.count'/>">
								<s:property value="pubEditContent"/>
								</div>			
							</s:iterator>
							</td>
						</tr>
					</table>
				</div>
				</div>



	</body>
</html>


<script type="text/javascript">


function reloadData(){
	parent.propertiesTree.reloadData();
}


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

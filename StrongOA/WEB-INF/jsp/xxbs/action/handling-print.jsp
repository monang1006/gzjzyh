<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>报送信息</title>

		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/ckeditor/ckeditor.js"></script>
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
	font-family:"仿宋_GB2312";
	font-size:16pt;
	text-indent: 32pt;
	font-weight:bold;
	line-height:20px;
}
.relContent{
	padding-top:30px;
	font-family:"仿宋_GB2312";
	font-size:16pt;
	text-indent: 32pt;
	font-weight:normal;
	line-height:140%;
}
.relOrg{
	font-family:"楷体_GB2312";
	font-size:15pt;
	text-align:left;
	padding: 10px 10px 20px 0;
	line-height:20px;
}

.relOrg1{
	font-family:"楷体_GB2312";
	font-size:15pt;
	padding: 10px 10px 20px 0;
	line-height:10px;
}
</style>
		
		
		<style type="text/css" media=print>
.noprint{display : none }
</style>
	</head>
	<script type="text/javascript">
	$(function(){
	var obj = window.dialogArguments;
	var pubTitle = obj.pubTitle;
	var pubEditContent = obj.pubEditContent;
	pubEditContent = pubEditContent.replace(/\n/g,"<br/>");
	var submitDate = obj.submitDate;
	submitDate = submitDate.substring(0,16);
	var orgName = obj.orgName;
	$("#title").html(pubTitle);
	$("#content").append(pubEditContent);
	$("#org").html(orgName);
	$("#date").html(submitDate);
	});
	</script>
	<body>
	<div class="main_up_out" >
				<div id="menu_top" style="width:auto;" class="noprint">
					<ul>
						<li>
							<button id="allInfo" class="input_button_6" onclick="preview()" ><img src="<%=themePath%>/image/ico_view.gif"/>打印</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>
	</div>
	
		<table  align="center"  style="background:#fff;margin:0 0 0 10px;padding:20px 10px; width="680px"  cellpadding="0"
			<tr>
				<td colspan="2" align="center" class="relTitle"><strong id="title"></strong></td>
			</tr>
			<tr>
				<td colspan="2" id="content" class="relContent"></td>
			</tr>
			<tr>
				<td align="right" class="relOrg1" width="500px">上报单位：</td>
				<td id="org" align="right" class="relOrg" width="200px"></td>
				
			</tr>
			<tr>
			<td align="right" class="relOrg1" width="500px">上报时间：</td>
				<td id="date" align="right" class="relOrg" width="200px"></td>
			</tr>
		</table>
	</body>
</html>
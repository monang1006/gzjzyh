<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
<head>
<title>打印申请</title>
<%@include file="/common/include/meta.jsp"%>

<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
<script src="<%=path%>/common/js/jquery/jquery-1.6.2.min.js"
	type="text/javascript"></script>
<script src="<%=path%>/common/js/jqprint/jquery.jqprint.js"
	type="text/javascript"></script>
<script type="text/javascript">

function photoPrint(){
	window.print();
}
</script>
</head>
<base target="_self" />
<body class=contentbodymargin>
<input style="width:100px;height:26px;" type="button" value="打印" onclick="photoPrint()"></input>
<input style="width:100px;height:26px;" type="button" value="关闭" onclick="window.close();"></input>
<table style="width:200mm;height:280mm;">
	<tr>
		<td align="center">
			<img id="ueMainNo1Tmp" src="<%=path %>${ueMainNo1Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>主办警官警官证（正）</div>
		</td>
		<td align="center">
			<img id="ueMainNo2Tmp" src="<%=path %>${ueMainNo2Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>主办警官警官证（反）</div>
		</td>
	</tr>
	<tr>
		<td align="center">
			<img id="ueMainId1Tmp" src="<%=path %>${ueMainId1Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>主办警官身份证（正）</div>
		</td>
		<td align="center">
			<img id="ueMainId2Tmp" src="<%=path %>${ueMainId2Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>主办警官身份证（反）</div>
		</td>
	</tr>
	<tr>
		<td align="center">
			<img id="ueHelpNo1Tmp" src="<%=path %>${ueHelpNo1Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>协办警官警官证（正）</div>
		</td>
		<td align="center">
			<img id="ueHelpNo2Tmp" src="<%=path %>${ueHelpNo2Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>协办警官警官证（反）</div>
		</td>
	</tr>
	<tr>
		<td align="center">
			<img id="ueHelpId1Tmp" src="<%=path %>${ueHelpId1Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>协办警官身份证（正）</div>
		</td>
		<td align="center">
			<img id="ueHelpId2Tmp" src="<%=path %>${ueHelpId2Tmp }" style="width:90mm;height:50mm;cursor:pointer;">
			<div>协办警官身份证（反）</div>
		</td>
	</tr>
</table>
</body>
</html>
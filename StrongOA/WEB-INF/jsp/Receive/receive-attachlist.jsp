<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>附件列表</title>
		<%@include file="/common/include/meta.jsp"%>
		<style type="text/css">
<!--
body,table{
	font-size:16px;
}
table{
	table-layout:fixed;
	empty-cells:show; 
	border-collapse: collapse;
	margin:0 auto;
}
td{
	height:20px;
}
h1,h2,h3{
	font-size:16px;
	margin:0;
	padding:0;
}

.title { background: #FFF; border: 1px solid #9DB3C5; padding: 1px; width:90%;margin:20px auto; }
	.title h1 { line-height: 31px; text-align:center;  background: #2F589C url(th_bg2.gif); background-repeat: repeat-x; background-position: 0 0; color: #FFF; }
		.title th, .title td { border: 1px solid #CAD9EA; padding: 5px; }


/*这个是借鉴一个论坛的样式*/
table.t1{
	border:1px solid #cad9ea;
	color:#666;
}
table.t1 th {
	background-image: url(th_bg1.gif);
	background-repeat::repeat-x;
	height:30px;
}
table.t1 td,table.t1 th{
	border:1px solid #cad9ea;
	padding:0 1em 0;
}
table.t1 tr.a1{
	background-color:#f5fafe;
}

 .tabletitle {
	      FILTER:progid:DXImageTransform.Microsoft.Gradient(
	                            gradientType = 0, 
	                            startColorStr = #ededed, 
	                            endColorStr = #ffffff);
	    }
    
-->
</style>
		<script type="text/javascript">
function download(id){
	document.getElementById("download").src="<%=root%>/Receive/receive!downLoadAttach.action?docAttach.fileId="+id;	
}
function back(){
	location="<%=root%>/Receive/receive!query.action";
}
</script>
	</head>

	<body onload="window.focus();">
		${table }
	</body>
	<iframe id="download" style="display: none;"></iframe>
</html>


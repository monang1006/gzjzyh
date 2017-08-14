<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>已报信息</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		
	</head>

	<body>
						<table id="list"></table>
					<div id="pager"></div>
	
	</body>
</html>
<script type="text/javascript">
$(function(){
	$("#list").jqGrid({
	    url:'${root}/xxbs/info/submit!listSubmitted.action',
	    colModel :[ 
	      {label:'pubId',name:'pubId', width:55, hidden:true}, 
	      {label:'信息标题',name:'pubTitle', width:90}, 
	      {label:'信息类型',name:'pubInfotype', 
				width:80, align:'center'}, 
	      {label:'是否约稿',name:'pubIsappoint', 
				width:80, align:'center'}, 
	      {label:'添加时间',name:'pubDate', width:80, align:'center'}
	    ],
	    sortname: 'pubTitle',
	    grouping:true, 
	    groupingView : { 
	       groupField : ['pubDate'],
	       groupOrder: ['desc'],
	       groupDataSorted : false
	    }
	});	
});
</script>
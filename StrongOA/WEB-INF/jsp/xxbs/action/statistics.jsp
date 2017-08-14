<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>采用统计</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
				<div id="menu_top" style="width:auto;">
					<ul>
						<li>
						机构类型：<s:select cssClass="formin" id="isOrg" name="orgType"
									list="#{'':'所有部门','0':'省政府各有关部门','1':'各设区政府','2':'各县（市区）政府','3':'驻外办'}" listKey="key" listValue="value" />
						排序：<s:radio name="useStatus" list="#{\"4\":'本月',\"2\":'当日',\"3\":'本年', \"1\":'采用总数'}" value="%{useStatus}"/>
						</li>
					</ul>

					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">
					<table id="list"></table>
					<div id="pager"></div>
				</div>
				</div>
	
	<input type="hidden" value="${orgType}" name="orgType" id="orgType"/>
	</body>
</html>
<script type="text/javascript">

$(function(){
	
	$("#orgType").val($("#isOrg option:selected").val());
	var orgType = $("#orgType").val();
	$("#list").jqGrid({
		url:'${root}/xxbs/action/statistics!showList.action?useStatus=${useStatus}&orgType='+orgType,
		colModel :[ 			
			{label:'报送单位',name:'orgName'},
			{label:'当日',name:'byDay', align:'center',sortable:false, width:60},
			{label:'本月',name:'byMonth', align:'center',sortable:false, width:60},
			{label:'本年',name:'byYear', align:'center', sortable:false, width:60},
			{label:'采用总数',name:'total', align:'center', sortable:false, width:60},
			],
		gridComplete: gl.resize,
		multiselect: false,
		sortable: false,
		sortorder: 'asc'
	});
});


function reloadData(){
	$('#list').trigger("reloadGrid");
}

var infotype = function(el, cellval, opts) {
	temp = "";
	if(el == 0){
		temp = "普通信息";
	}
	else if (el == 1) {
		temp = "涉密信息";
	}
	return temp;
};

var isYes = function(el, cellval, opts) {
	temp = "";
	if (el == "1") {
		temp = "<img src='<%=themePath%>/image/ico_hook.gif' title='是'>";
	}
	return temp;
};

$("input[name='useStatus']").click(function(){
	location = "${root}/xxbs/action/statistics.action?useStatus="+$(this).val()+"&orgType="+$("#isOrg option:selected").val();
});

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

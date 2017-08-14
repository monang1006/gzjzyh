<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>栏目管理</title>

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
							<button id="save" class="input_button_4"><img src="<%=themePath%>/image/ico_view.gif"/>归档</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 1px;">

				<table id="list"></table>
					<div id="pager"></div>
				</div>
				</div>

<div id="mask"></div>
	</body>
</html>
<script type="text/javascript">

var showDia = function(id){
	var url = "<%=root%>/xxbs/action/unsaveMail!view.action?toId="+id;
	gl.showDialog(url,600,450);
};

var onCellSelect = function(rowid, iCol ,el){
	if(iCol > 1){
		showDia(rowid);
	}
};

var selectUser = function(el, cellval, opts){
	return "<a href='javascript:selectU(\""+opts.mailAddress+"\");'><font color='blue'>[选择用户]</font></a>";
};

function selectU(mail){
	var url = "<%=root%>/xxbs/action/unsaveMail!selectNotMailUser.action?email="+mail;
	var ret = gl.showDialog(url,600,400);
	gl.msg(ret, "归档成功");
}


$(function(){
	
	$("#list").jqGrid({
		url:'${root}/xxbs/action/unsaveMail!showList.action',
		colModel :[ 
			{label:'id',name:'id', hidden:true},
			{label:'主题',name:'subject',sortable:false,width:300},
			{label:'邮件地址',name:'mailAddress',sortable:false},
			{label:'发送时间',name:'sentDate',align:'center',sortable:false},
			{label:'映射用户',formatter:selectUser,align:'center',sortable:false}
			],
		gridComplete: gl.resize,
		sortname:'sentDate',
		onCellSelect: onCellSelect
	});

});

var save = function(){
	$.get("<%=root%>/xxbs/action/unsaveMail!saveToPublish.action", function(response){
		if(response == "success"){
			gl.msg(response, "归档成功");
			reloadData();
		}
	});
};
$("#save").click(save);

function reloadData(){
	$('#list').trigger("reloadGrid");
}


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

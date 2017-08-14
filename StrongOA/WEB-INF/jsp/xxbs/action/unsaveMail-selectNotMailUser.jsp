<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>用户信息</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jqgrid/jqgrid.js"></script>
		<style type="text/css">
			html { -webkit-box-sizing:border-box; -moz-box-sizing:border-box; box-sizing:border-box; padding:40px 0px 40px 0px; overflow:hidden;}
			html,body { height:100%;}
		</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
		<div class="windows_title">
			请选择用户
		</div>
		<div class="information_out" id="information_out">
			<div id="gridDiv" class="grid" style="width:auto; display:block; padding: 0px;">
				<table id="list"></table>
				<div id="pager"></div>
			</div>
		</div>

		<div class="information_list_choose_pagedown">
			<input type="button" class="information_list_choose_button9"
				value="关闭" name="cancel" id="cancel" />
		</div>


	</body>
</html>
<script type="text/javascript">
	
$(function(){
	$("#list").jqGrid({
	    url:'${root}/xxbs/action/unsaveMail!showNotMailUser.action',
	    colModel :[ 
	      {label:'userId',name:'userId', hidden:true}, 
	      {label:'用户名',name:'userName'}
	    ],
	    multiselect:false,
	    onSelectRow: selectRow,
	    gridComplete: gl.resize
	});
	
	$("#cancel").click(function(){
		window.close();
	});

});

var selectRow = function(id){
	var user = $("#list").jqGrid("getCell", id, "userName");
	if(confirm("确定把邮箱地址[${email}]保存到用户["+user+"]，并归档？")){
		$.get("<%=root%>/xxbs/action/unsaveMail!saveMailToUserAndPublish.action?email=${email}&toId="+id, function(response){
			if(response == "success"){
				window.returnValue = response;
				window.close();
			}
		});
	}
};

</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>打印预览</title>

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
		 <style type="text/css">
		 *{ margin:0; padding:0; }
		 html,body{ background-color:#fff; }
         .dcytjm{ padding:22px; }
		 .dcytjm table{ border-collapse:collapse; border-spacing:0; }
		 .dcytjm th,.dcytjm td{ height:26px; padding:0 2px; border:1px solid #b7b7b7; font-size:14px; color:#333; }
		 .dcytjm td{ color:#333; font-size:16px;}
		 .dcytjm th{ background-color:#f5f7f8; }
		 .dmenutop{ padding:8px 22px; background:#e5eef7; border-bottom:1px solid #b7b7b7; }
        </style>
        <style type="text/css" media=print>
.noprint{display : none }
</style>
		
		
		<style type="text/css" media=print>
.noprint{display : none }
</style>
	</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out">
					<div id="menu_top" style="width:auto;" class="noprint">
					<ul>
						<li>
							<button id="adopt" class="input_button_4" onclick="preview()"><img src="<%=themePath%>/image/ico_view.gif"/>打印</button>
						</li>
					</ul>

					<br style="clear:both" />
				</div>
				<div class="dcytjm" id="read">
				</div>
				</div>
	
	</body>
</html>
<script type="text/javascript">
var obj = window.dialogArguments;
var checkId = obj.checkId;
var ids = "";
for(var i =0 ;i<checkId.length;i++){
	ids = ids+checkId[i]+","
}
ids = ids.substring(0,ids.length-1);
var data = {toId:ids};
var action = "<%=root%>/xxbs/action/handling!print1.action";
$.post(action,data,function(ret){
	$("#read").html(ret);
});

	
</script>

<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

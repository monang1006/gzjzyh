<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		
	%>
<html>
	<head>
		<title>文号生成器</title>
		<style type="text/css">
		.ui-tab-content{padding-left:0px; padding-right:0px;}
		</style>
		
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=path %>/common/js/autocode/style.css"/>   
		<script type="text/javascript" src="<%=path %>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/autocode/ui.tab.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			var tab;
			$(document).ready(function(){
				tab = new $.fn.tab({
					tabList:"#demo1 .ui-tab-container .ui-tab-list li",
					contentList:"#demo1 .ui-tab-container .ui-tab-content"
				});
				$("#btn_triggle").click(function(){
					tab.triggleTab(1);
				});
				//$("ul li:nth-child(2)").attr("disabled","disabled");
				//$("ul li:nth-child(3)").attr("disabled","disabled");
				//$("ul li:nth-child(4)").attr("disabled","disabled");
				document.frames["getcodeF"].location.reload();
			});	
			
			var id = "${id}";
			
			function beforshow(){
				var getcodeF = document.frames["getcodeF"];
				if(getcodeF.ruletype.value!=0){
					return true;
				}else{
					alert("请选择一个编号规则！")
					return false;
				}
			}
			
			function forclick(index){
				var selvalue=$("#tmptxt").val();
				
				if (index==1)
				{
					$("#leavecode").attr("src","<%=path %>/autocode/autoCode!getReservedCode.action?selCode="+selvalue+"&d="+new Date())
				
				}else if (index==2){
					$("#reversecode").attr("src","<%=path %>/autocode/autoCode!getRecCode.action?selCode="+selvalue+"&d="+new Date())
				
				}else if (index==3){
					$("#usedcode").attr("src","<%=path %>/autocode/autoCode!getUsedCode.action?selCode="+selvalue+"&d="+new Date())
				}	
			}
			
		</script>
	</head>
	<body class="contentbodymargin" style="padding:0px;">
		<center>
			<div id="demo1">
		 		<div class="ui-tab-container" style="padding-right:0px;">
					<ul class="clearfix ui-tab-list">
						<li>生成文号</li>
						<li>预留文号</li>
						<li>回收文号</li>
						<li>已用文号</li>
					</ul>
					<div class="ui-tab-bd">
						<input type="hidden" id="tmptxt">
						<div class="ui-tab-content">
							<iframe id="getcodeF" src="<%=path %>/autocode/autoCode!getCode.action?id=${id}" style="margin-left: -10px;"  width="100%" height="600" scrolling="no" marginheight="0" marginwidth="0" frameborder="0"></iframe>
						</div>
						<div class="ui-tab-content" style="display:none">
							<iframe id="leavecode" style="margin-left: -15px;" src="" width="100%" height="600" scrolling="no" marginheight="0" marginwidth="0" frameborder="0"></iframe>
						</div>
						<div class="ui-tab-content" style="display:none">
							<iframe id ="reversecode" style="margin-left: -15px;" src="" width="100%" height="600" scrolling="no" marginheight="0" marginwidth="0" frameborder="0"></iframe>
						</div>
						<div class="ui-tab-content" style="display:none">
							<iframe id ="usedcode" style="margin-left: -15px;" src="" width="100%" height="600" scrolling="no" marginheight="0" marginwidth="0" frameborder="0"></iframe>
						</div>
					</div>
				</div>
			</div>
		</center>
	</body>
</html>
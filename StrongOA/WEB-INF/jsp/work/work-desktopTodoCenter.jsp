<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
	
	

	String showNum = request.getParameter("showNum");
	String subLength = request.getParameter("subLength");
	String showCreator = request.getParameter("showCreator");// 1：显示
	String showDate = request.getParameter("showDate");//	1：显示

	String jjList = request.getParameter("jjList");
	String cpList = request.getParameter("cpList");
	String cyList = request.getParameter("cyList");
	String dbList = request.getParameter("dbList");
	String blockId = request.getParameter("blockId");
	
%>
<html>
	<head>
		<title>待办列表</title>
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=root%>/oa/css/desktop/style.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<style type="text/css">
		body, table, tr, td, div{
				background-color:white;
			    margin:0px;
			}
		.tdTab{
			font-family: 
		}
		.tdTab:hover{
			cursor: hand;
		}
		.linkDiv{
			font-family: "宋体";
			font-size: 12;
		}
		.linkDiv a:visited{
			color:#999;
			font-family: "宋体";
			font-size: 12;
		}
	</style>
	<script type="text/javascript">
	
	$(document).ready(function() {
	
		$(".tdTab").click(function(){
				var id = this.id;
				$("#"+id).html("<img src=\"<%=frameroot%>/images/ico/ico.gif\" width=\"9\" height=\"9\"><b> "+$("#"+id).text()+"</b>");
				$("#"+id+"_div").show();
				loadCon(id+"_div");
				$.each( ["jj","sp","cy","db"], function(i, tdTabid){
				  if(id!=tdTabid){
				  	$("#"+tdTabid).html($("#"+tdTabid).text());
				  	$("#"+tdTabid+"_div").hide();
				  }
				}); 
		});
	
	
		$("#jj_div").html("<div class=\"linkdiv\" style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\" ><img src='<%=path%>/oa/image/desktop/loading.gif'/>加载内容...</div>");
		loadCon("jj_div");
		
		
		
		function loadCon(divID){
			$("#"+divID).html("<div class=\"linkdiv\" style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\" ><img src='<%=path%>/oa/image/desktop/loading.gif'/>加载内容...</div>")
			$.ajax({
				type:"post",
				dataType:"text",
				url:"<%=root%>/senddoc/sendDoc!getTodoListByType.action",
				data:"listType="+ divID+"&showNum=<%=showNum%>"+"&subLength=<%=subLength%>"+"&showCreator=<%=showCreator%>"+"&showDate=<%=showDate%>"+"&blockId=<%=blockId%>",
				success:function(msg){ 
					if(""!=msg){
						$("#"+divID).html(msg);
					}else{
						if("jj_div"==divID){
							$("#"+divID).html("<div class=\"linkdiv\" style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\">没有待办急件</div>");
						}else if("sp_div"==divID){
							$("#"+divID).html("<div class=\"linkdiv\ style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\">没有待办审批件</div>");
						}else if("cy_div"==divID){
							$("#"+divID).html("<div class=\"linkdiv\ style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\">没有待办呈阅件</div>");
						}else if("db_div"==divID){
							$("#"+divID).html("<div class=\"linkdiv\ style=\"border-bottom:1px #DDDDEE dotted;padding:3px;\">没有待办事宜</div>");
						}
					}
				}
			});
		}
	});
	
	</script>
	</head>
	<body>
		<table width="100%">
			<tr>
				<td width="22%" class="tdTab" id="jj" align="center"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#FFCCCC,endColorStr=#FFFFFF);">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"/>
					<b>急件（<%=jjList %>）</b>
				</td>
				<td width="24%" class="tdTab"  id="sp" align="center"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#edeeee,endColorStr=#FFCCFF);">
					审批件（<%=cpList %>）
				</td>
				<td width="24%" class="tdTab"  id="cy" align="center"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#edeeef,endColorStr=#FFFF99);">
					呈阅件（<%=cyList %>）
				</td>
				<td width="30%" class="tdTab"  id="db" align="center"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#edeefa,endColorStr=#B0fff6);">
					待办事宜（<%=dbList %>）
				</td>
			</tr>
		</table>
		<div style="overflow: auto">
		<div id='jj_div' class="drag_content">
			<div class="linkdiv" title="" style="">
				急件列表
			</div>
		</div>

		<div id='sp_div' class="drag_content" style="display:none;">
			<div class="linkdiv" title="">
				审批件列表
			</div>
		</div>

		<div id='cy_div' class="drag_content" style="display:none">
			<div class="linkdiv" title="">
				呈阅件列表
			</div>
		</div>

		<div id='db_div' class="drag_content" style="display:none">
			<div class="linkdiv" title="">
				待办事宜列表
			</div>
		</div>
		</div>
	</body>
</html>

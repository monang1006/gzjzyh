<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>期刊发布</title>

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
	font-family:"楷体";
	font-size:16pt;
	text-indent: 32pt;
	font-weight:bold;
}
.relContent{
	font-family:"仿宋";
	font-size:16pt;
	text-indent: 32pt;
	font-weight:normal;
	line-height:140%;
}
.relOrg{
	font-family:"楷体";
	font-size:15pt;
	text-align:right;
	padding: 10px 10px 20px 0;
}
</style>
</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out" style="height:25px;padding:0px 0 0px 0;border-left: 0px solid #666">
				<div id="menu_top" style="width:auto;">
<form method="post" enctype="multipart/form-data">
				<input type="file" name="upload" style="width:0;height:0"/>
					<ul>
					<li>
							<button type="button" id="look" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>生成清样</button>
						</li>
						</ul>
</form>
					
					<br style="clear:both" />
				</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding-button: 1px;">
					<table  align="center" style="background:#fff;margin:0 0 0 10px;padding:20px 10px;border:1px solid #666;" width="680px" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td class="relJournal">
							<span id="relJournal">${issue.TInfoBaseJournal.jourName}</span>
							<span id=relNumber>第${issue.issNumber}期</span>
							<span id="relDate"><s:date name="relDate" format="yyyy年M月d日" /></span>
							</td>
						</tr>
						<tr>
							<td>
							<s:iterator value="releases" status="status">
								<s:if test="top.isFirstColumn==true">
								<div class="relColumn" id="relColumn<s:property value='#status.count'/>"><s:property value="colName"/>
								
								</div>
								</s:if>
								<div class="relTitle"><span id="relTitle<s:property value='#status.count'/>"><s:property value="pubTitle"/></span>
									<span class="relContent" id="relContent<s:property value='#status.count'/>"><s:property value="pubEditContent"/></span>
									<s:if test="%{isPublish!=\"1\"}">
									<input type="button" id="edit" name="edit"  onclick="edit('<s:property value="pubId"/>')" value="修改"/>
									<input type="button" id="delete" name="delete"  onclick="del('<s:property value="pubId"/>')" value="撤销"/>
									<input type="button" id="up" name="up"  onclick="up('<s:property value="pubId"/>')" value="上移"/>
									<input type="button" id="down" name="down"  onclick="down('<s:property value="pubId"/>')" value="下移"/>
									</s:if>
								</div>						
								<div class="relOrg" id="relOrg<s:property value='#status.count'/>"><s:property value="orgName"/></div>
							</s:iterator>
							</td>
						</tr>
					</table>
				</div>
				</div>


		<input type="hidden" value="${toId }" id="toId" name="toId"/>
	</body>
</html>


<script type="text/javascript">

$("#look").click(function(){
	var toId = $("#toId").val();
	window.location.href="<%=path%>/xxbs/action/release!content.action?toId="+toId+"&flag=0";
});

function edit(pubId){
	var url = "<%=root%>/xxbs/action/handling!view.action?toId="+pubId;
	var ret = gl.showDialog(url,1200,800);
	if(ret="success"){
		window.parent.propertiesList.location.reload(true);
	}
}

function up(pubId){
	var time = new Date();
	$.get("<%=root%>/xxbs/action/handling!upPublish.action?toId="+pubId+"&flag=0&time="+time.getTime(),function(ret){
		if(ret=="success"){
			window.parent.propertiesList.location.reload(true);
		}
		else{
			alert("已经在第一篇的文章不能移动!")
		}
	});	
}

function down(pubId){
	var time = new Date();
	$.get("<%=root%>/xxbs/action/handling!downPublish.action?toId="+pubId+"&flag=0&time="+time.getTime(),function(ret){
		if(ret=="success"){
			window.parent.propertiesList.location.reload(true);
		}
		else{
			alert("已经在最后一篇的文章不能移动!")
		}
	});	
}

function del(pubId){
	var time = new Date();
	$.get("<%=root%>/xxbs/action/handling!cancelAdopt.action?toId="+pubId+"&flag=0&time="+time.getTime(),function(ret){
		if(ret="success"){
			window.parent.propertiesList.location.reload(true);
		}
	});	
}

	

function reloadData(){
	parent.propertiesTree.reloadData();
}


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

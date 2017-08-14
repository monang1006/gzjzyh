<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>发布通报</title>

		<%@include file="/common/include/meta.jsp"%>

		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/component.css" />
		<link rel="stylesheet" type="text/css" href="<%=themePath%>/css/global.css" />
		
		<script type="text/javascript" src="<%=scriptPath%>/global1.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/common.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/search.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/Message_${locale}.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/jquery.tooltip.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/component-min.js"></script>
		<script type="text/javascript" src="<%=scriptPath%>/My97DatePicker/WdatePicker.js"></script>
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
				<s:form id="myform" name="myform" method="post" enctype="multipart/form-data"
					action="/xxbs/action/infoReport.action">
				<input type="hidden" id="rpTitle" name="rpTitle1" value="${tbTitle}"/>
				<input type="file" name="upload" style="width:0;height:0"/>
					<ul>
						<li>
							<button id="publish" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>保存通报</button>
						</li>
						<li>
							<button id="return" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>返回列表</button>
						</li>
					</ul>
</s:form>

					<br style="clear:both" />
				</div>
<div id="rcDiv">
<object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" 
codebase="<%=root%>/OfficeControl.cab#version=5,0,1,8" width="100%" height="100%">
<param name="MakerCaption" value="思创数码科技股份有限公司">
<param name="MakerKey" value="5C1FF1F1177246B272DB34DD8ADA318222D19F65">
<param name="ProductCaption" value="思创数码科技股份有限公司某某测试用户"> 
<param name="ProductKey" value="47F694B3B843BCE9197CF1CE615F832DF6F19232">
<SPAN STYLE="color:red">该网页需要控件浏览.浏览器无法装载所需要的文档控件.请检查浏览器选项中的安全设置.</SPAN>
</object>

</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding-button: 1px;">
					<table style="display:none;background:#fff;margin:0 0 0 10px;padding:20px 10px;border:1px solid #666;" width="680px" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td class="relJournal" id="relJournal"> ${tbTitle} </td>
						</tr>
						<tr>
							<td>
<s:iterator value="reports" status="status">
<s:if test="top.isFirst==true">
<div class="rOrg" id="relOrg<s:property value='#status.count'/>"><s:property value="orgName"/> (单位总分：<s:property value="orgScore"/>)</div>
</s:if>
<div class="rTitle" id="relTitle<s:property value='#status.count'/>"><s:property value="pubTitle"/> <s:date name="%{useDate}" format="yyyy-MM-dd HH:mm" /> (采用分：<s:property value="useScore"/> <s:if test="remarkScore!=0">批示分：<s:property value="remarkScore"/></s:if> <s:if test="plusScore!=0">加分：<s:property value="plusScore"/></s:if> 总分：<s:property value="infoScore"/>)</div>
</s:iterator>
							</td>
						</tr>
					</table>
				</div>
				</div>



	</body>
</html>
<script type="text/javascript">
	
$(function(){
	$("#rcDiv").height($(window).height()-35);
	var officeObj = TANGER_OCX;
	officeObj.TitleBar = false;
	officeObj.Menubar = true;
	
	var getType = function(){
		var type = "";
		if(officeObj.GetWPSVer() != 100){
			type = "WPS.Document";
		}
		if(officeObj.GetOfficeVer() != 100){
			type = "Word.Document";
		}
		return type;
	};
	
	var buildText = function(){
		officeObj.CreateNew(getType());
		officeObj.AddTemplateFromURL("<%=root%>/temp_report_title.doc");
		officeObj.SetBookmarkValue("maintitle", $("#relJournal").text().trim());
		
		for(var i=1; i<=$(".rTitle").size(); i++){
			officeObj.AddTemplateFromURL("<%=root%>/temp_report.doc");
			var doc = officeObj.ActiveDocument;
			officeObj.SetBookmarkValue("title",$("#relTitle"+i).text());
			if($("#relOrg"+i).text() != ""){
				officeObj.SetBookmarkValue("org", "\r\n"+$("#relOrg"+i).text());
			}
			else{
				doc.BookMarks("org").Range.Delete();
			}
			doc.BookMarks("title").Delete();
			doc.BookMarks("org").Delete();
		}
	};
	
	buildText();
	
	$("#publish").click(function(){
		officeObj.PublishAsHTMLToURL("<%=root%>/xxbs/action/infoReport!save.action", "upload", "rpTitle="+encodeURIComponent('${tbTitle}'), null, null);
	});
	
	$("#return").click(function(){
		location = "<%=root%>/xxbs/action/infoReport.action";
 	});
	
});

function reloadData(){
	parent.propertiesTree.reloadData();
}


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

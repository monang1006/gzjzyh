<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>编辑通报</title>

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
</head>

	<body>
		<script type="text/javascript" src="<%=scriptPath%>/wait.js"></script>
			<div class="main_up_out" style="height:25px;padding:0px 0 0px 0;border-left: 0px solid #666">
				<div id="menu_top" style="width:auto;">
				<s:form id="myform" name="myform" method="post" enctype="multipart/form-data"
					action="/xxbs/action/infoReport.action">
				<input type="hidden" name="rpId" value="${model.rpId}"/>
				通报标题：<input  type="text" id="rpTitle" name="rpTitle" value="${model.rpTitle}"/>
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
	

	if(officeObj.GetOfficeVer() != 100){
		officeObj.OpenFromURL("<%=root%>/xxbs/action/infoReport!officeStream.action?toId=${toId}", null, "Word.Document");		
	}
	else if(officeObj.GetWPSVer() != 100){
		officeObj.CreateNew("WPS.Document");
		officeObj.AddTemplateFromURL("<%=root%>/xxbs/action/infoReport!officeStream.action?toId=${toId}");		
	}

	
	$("#publish").click(function(){
		var rpTitle = $("#rpTitle").val();
		rpTitle = encodeURIComponent(rpTitle);
		officeObj.PublishAsHTMLToURL("<%=root%>/xxbs/action/infoReport!save.action?op=edit", "upload", "rpTitle="+rpTitle, null, null);
	});
	
	$("#return").click(function(){
		location = "<%=root%>/xxbs/action/infoReport.action";
 	});
	
});



</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

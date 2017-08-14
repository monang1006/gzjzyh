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
<form method="post" enctype="multipart/form-data">
				<input type="file" name="upload" style="width:0;height:0"/>
					<ul>
<s:if test="%{isPublish!=\"1\"}">
						<li>
							<button type="button" id="prePublish" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>保存预发布</button>
						</li>
						<li>
							<button type="button" id="cancelPrePublish" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>撤销预发布</button>
						</li>
						<li>
							<button type="button" id="publish" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>正式发布</button>
						</li>
</s:if>
<s:if test="%{isPublish==\"1\"}">
<input type="hidden" name="selView" value="2"/>
						<li id="showPrintPreview" style="display: none;">
							<button type="button" id="printPreview" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>打印预览</button>
						</li>
						<s:if test="%{open!=\"1\"}">
						<li>
							<button type="button" id="cancelPrePublish" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>撤销发布</button>
						</li>
						</s:if>
</s:if>
						<li id="showPrint" >
							<button type="button" id="print" class="input_button_6"><img src="<%=themePath%>/image/ico_view.gif"/>打印</button>
						</li>
</form>
					</ul>
					<br style="clear:both" />
				</div>
<div id="rcDiv">
<object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" 
codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=5,0,1,8" width="100%" height="100%">
<param name="MakerCaption" value="思创数码科技股份有限公司">
<param name="MakerKey" value="5C1FF1F1177246B272DB34DD8ADA318222D19F65">
<param name="ProductCaption" value="南昌市政府办公厅">
<param name="ProductKey" value="FD6357E9840E880F0B72EAC5357E7303379848AB">
<SPAN STYLE="color:red">该网页需要控件浏览.浏览器无法装载所需要的文档控件.请检查浏览器选项中的安全设置.</SPAN>
</object>
</div>

				<div id="gridDiv" class="grid" style="width:auto; display:block; padding-button: 1px;">
					<table style="display:none;background:#fff;margin:0 0 0 10px;padding:20px 10px;border:1px solid #666;" width="680px" border="0" cellpadding="0"
						cellspacing="1" class="main_search_border">
						<tr>
							<td class="relJournal">
							<span id="relJournal">${issue.TInfoBaseJournal.jourName}</span>
							<span id=relNumber>${issue.issNumber}</span>
							<span id="relDate"><s:date name="relDate" format="yyyy年M月d日" /></span>
							</td>
						</tr>
						<tr>
							<td>
							<s:iterator value="releases" status="status">
								<s:if test="top.isFirstColumn==true">
								<div class="relColumn" id="relColumn<s:property value='#status.count'/>"><s:property value="colName"/></div>
								</s:if>
								<div class="relTitle"><span id="relTitle<s:property value='#status.count'/>"><s:property value="pubTitle"/></span>
									<span class="relContent" id="relContent<s:property value='#status.count'/>"><s:property value="pubEditContent"/></span>
								</div>						
								<div class="relOrg" id="relOrg<s:property value='#status.count'/>"><s:property value="orgName"/></div>
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
	officeObj.FileSave = true;
	officeObj.FileSaveAs = true;
	
	
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
	
	var openType = function(){
		var type = null;
		if(officeObj.GetOfficeVer() != 100){
			type = "Word.Document";
		}
		return type;
	};
	
	var buildText = function(){
		officeObj.CreateNew(getType());
		officeObj.ActiveDocument.PageSetup.LeftMargin= 82;
		officeObj.ActiveDocument.PageSetup.RightMargin= 73;
		officeObj.ActiveDocument.PageSetup.TopMargin= 72;
		officeObj.ActiveDocument.PageSetup.BottomMargin= 54;
		officeObj.AddTemplateFromURL("<%=root%>/xxbs/action/wordTemplate!officeStream.action?toId=${wtId}-header");
		officeObj.SetBookmarkValue("issue_number", $("#relNumber").text().trim());
		officeObj.SetBookmarkValue("issue_date", $("#relDate").text().trim());
				
		for(var i=1; i<=$(".relTitle").size(); i++){
			officeObj.AddTemplateFromURL("<%=root%>/xxbs/action/wordTemplate!officeStream.action?toId=${wtId}-body");
			officeObj.SetBookmarkValue("column",$("#relColumn"+i).text().trim());
			officeObj.SetBookmarkValue("title",$("#relTitle"+i).text().trim());
			officeObj.SetBookmarkValue("content", $("#relContent"+i).text().trim());
			officeObj.SetBookmarkValue("org",$("#relOrg"+i).text().trim());
			var doc = officeObj.ActiveDocument;
			doc.BookMarks("column").Delete();
			doc.BookMarks("title").Delete();
			doc.BookMarks("content").Delete();
			doc.BookMarks("org").Delete();
		}
		officeObj.ActiveDocument.Application.Selection.GoTo(1,1);
		officeObj.AddPicFromURL("<%=root%>/mr.jpg", true,0,0,3,100,0);
		officeObj.ActiveDocument.Shapes(1).Width=595.2;
		officeObj.ActiveDocument.Shapes(1).Height=371;
	};

	if("${isPublish}" == "1"){
			officeObj.ToolBars = false;
			officeObj.CreateNew(getType());
			officeObj.AddTemplateFromURL("<%=root%>/xxbs/action/release!officeStream.action?toId=${toId}");
			officeObj.ActiveDocument.PageSetup.LeftMargin= 82;
			officeObj.ActiveDocument.PageSetup.RightMargin= 73;
			officeObj.ActiveDocument.PageSetup.TopMargin= 72;
			officeObj.ActiveDocument.PageSetup.BottomMargin= 54;
			officeObj.SetReadOnly(true);
			
			officeObj.Menubar = true;
			officeObj.FileNew = false;
			officeObj.FileOpen = false;
			officeObj.FileClose = false;
			officeObj.FileSave = true;
			officeObj.FileSaveAs = true;
			officeObj.FilePrint = true;
			officeObj.FilePrintPreview = true;
			officeObj.FilePageSetup = false;
			officeObj.FileProperties = false;
			officeObj.FullScreenMode = true;
			
	}
	//预发布
	else if("${isPublish}" == "2"){
		officeObj.OpenFromURL("<%=root%>/xxbs/action/release!officeStream.action?toId=${toId}",null,openType());
		officeObj.FullScreenMode = true;
	}
	//未发布
	else if("${isPublish}" == "0"){
		buildText();
		
		officeObj.FullScreenMode = true;
	}
	
	$("#prePublish").click(function(){
		officeObj.SaveToURL("<%=root%>/xxbs/action/release!save.action?toId=${toId}&isPublish=2", "upload");
		parent.propertiesTree.reloadData();
		location.reload();
	});
	
	$("#cancelPrePublish").click(function(){
		officeObj.SaveToURL("<%=root%>/xxbs/action/release!save.action?toId=${toId}&isPublish=0", "upload");
		parent.propertiesTree.reloadData();
		location.reload();
	});
	
	$("#publish").click(function(){
		officeObj.SaveToURL("<%=root%>/xxbs/action/release!save.action?toId=${toId}&isPublish=1", "upload");
		parent.propertiesTree.reloadData();
		location.reload();
	});
	
	$("#printPreview").click(function(){
		officeObj.CreateNew(getType());
		officeObj.AddTemplateFromURL("<%=root%>/xxbs/action/release!officeStream.action?toId=${toId}");
		officeObj.ActiveDocument.Shapes(1).Delete();
		officeObj.SetReadOnly(true);
		officeObj.PrintPreview();
		$("#showPrintPreview").hide();
		$("#showPrint").css("display", "block");
	});
	
	$("#print").click(function(){
		var isPublish = "${isPublish}";
		try{
			officeObj.ActiveDocument.Shapes(1).Delete();
		}catch(err){
			
		}
		    officeObj.SetReadOnly(false);
			officeObj.PrintOut(true);
			officeObj.ActiveDocument.Application.Selection.GoTo(1,1);
			officeObj.AddPicFromURL("<%=root%>/mr.jpg", true,0,0,3,100,0);
			officeObj.ActiveDocument.Shapes(1).Width=595.2;
			officeObj.ActiveDocument.Shapes(1).Height=371;
			if(isPublish==1){
			officeObj.SetReadOnly(true);
			}
			
		
	});
	


});

function reloadData(){
	parent.propertiesTree.reloadData();
}


</script>
<script type="text/javascript" src="<%=scriptPath%>/stopwait.js"></script>

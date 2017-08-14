<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>信息处理</title>

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
	<div class="main_up_out" style="height:25px;padding:0px 0 0px 0;border-left: 0px solid #666">
				<div id="menu_top" style="width:auto;">
				<s:form id="myform" name="myform" method="post" enctype="multipart/form-data"
					action="/xxbs/action/infoReport.action">
				<input type="hidden" name="rpId" value="${model.rpId}"/>
				通报标题：<input type="text" id="rpTitle" name="rpTitle" value="${model.rpTitle}"/>
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
<param name="ProductCaption" value="江西省政府办公厅">
<param name="ProductKey" value="6FF53935850C053DB500DE8375B967E3C9255A1D">
<SPAN STYLE="color:red">该网页需要控件浏览.浏览器无法装载所需要的文档控件.请检查浏览器选项中的安全设置.</SPAN>
</object>
</div>
</div>
			<%
			int i=1;
			String m = "";
			if(i==1){
				m="一";
			}
			if(i==2){
				m="二";
			}
			if(i==3){
				m="三";
			}
			if(i==4){
				m="四";
			}
			if(i==5){
				m="五";
			}
			%>
			
			
			
		<table style="display: none">
			<tr>
			<td>
			
				<span id="chTitle">${year }年${month }月份信息采用情况通报</span><br/>
			各县（区）人民政府，各开发区（新区）管委会，市政府各部门：
			<span id="chBody">${year }年${month }月份，<s:if test="app[5]!=0">市政府办公厅采用信息<s:property value="app[5]"/>条。</s:if><s:if test="app[5]!=0">其中，<s:if test="app[2]!=0">采用各县区政务信息<s:property value="app[2]"/>条，</s:if> </s:if><s:if test="app[0]!=0">采用各部门信息<s:property value="app[0]"/>条，</s:if>   <s:if test="app[3]!=0">采用各驻外办事处信息<s:property value="app[3]"/>条，</s:if> <!--  <s:if test="app[4]!=0">采用秘书处信息<s:property value="app[4]"/>条。</s:if>-->	<s:if test="app[6]!=0">省政府领导批示信息<s:property value="app[6]"/>条。</s:if>现予通报。</span>
			<span id="chDate">${year }年${month }月</span>
			<span id="chTop">${year }年${month }月份政务信息采用情况</span>
			</td>
			</tr>
			
			
			<tr>
		<td id="bigTitle1">
			<s:if test="tongbao2.size()>0"><%=m %>、各设区政府信息采用情况
			<%i++;
			if(i==1){
				m="一";
			}
			if(i==2){
				m="二";
			}
			if(i==3){
				m="三";
			}
			if(i==4){
				m="四";
			}
			if(i==5){
				m="五";
			}%>
			</s:if>
			
		</td>
		</tr>
		<s:iterator value="tongbao2" var="data" status="status">
			<tr>
			<td>
			<span id="relOutline1,<s:property value='#status.count'/>"><s:if test="#data[0][3]!=0"><s:property value="#data[0][1]"/>　　　</s:if>
			</span><br/>
<s:iterator value="#data[1]" var="data1" status="st">
            <span id="relContent1,<s:property value='#status.count'/>,<s:property value='#st.count'/>,number"><s:property value='#st.count'/>. </span><span id="relContent1,<s:property value='#status.count'/>,<s:property value='#st.count'/>,titlelist"><s:property value="#data1[0]"/></span><span id="relContent1,<s:property value='#status.count'/>,<s:property value='#st.count'/>,flag"><s:property value="#data1[1]"/></span>
</s:iterator>
			
			</td>
			
			</tr>
			
		</s:iterator>
			<tr>
		<td id="bigTitle2">
			<s:if test="tongbao1.size()>0"><%=m %>、各部门信息采用情况
			<%i++;
			if(i==1){
				m="一";
			}
			if(i==2){
				m="二";
			}
			if(i==3){
				m="三";
			}
			if(i==4){
				m="四";
			}
			if(i==5){
				m="五";
			}%>
			</s:if>
			
		</td>
		</tr>
		<s:iterator value="tongbao1" var="data" status="status">
			<tr>
			<td>
			<span id="relOutline2,<s:property value='#status.count'/>"><s:if test="#data[0][3]!=0"><s:property value="#data[0][1]"/>　　　</s:if>
			</span><br/>		
<s:iterator value="#data[1]" var="data1" status="st">
          <span id="relContent2,<s:property value='#status.count'/>,<s:property value='#st.count'/>,number"><s:property value='#st.count'/>. </span><span id="relContent2,<s:property value='#status.count'/>,<s:property value='#st.count'/>,titlelist"><s:property value="#data1[0]"/></span><span id="relContent2,<s:property value='#status.count'/>,<s:property value='#st.count'/>,flag"><s:property value="#data1[1]"/></span>
</s:iterator>
			</td>
			
			</tr>
			
		</s:iterator>
		
		
		<tr>
		<td id="bigTitle4">
			<s:if test="tongbao4.size()>0"><%=m %>、各驻外办信息采用情况
			<%i++;
			if(i==1){
				m="一";
			}
			if(i==2){
				m="二";
			}
			if(i==3){
				m="三";
			}
			if(i==4){
				m="四";
			}
			if(i==5){
				m="五";
			}%>
			</s:if>
			
		</td>
		</tr>
		
		<s:iterator value="tongbao4" var="data" status="status">
			<tr >
			<td>
			<span id="relOutline4,<s:property value='#status.count'/>"><s:if test="#data[0][3]!=0"><s:property value="#data[0][1]"/>　　　</s:if>
			</span><br/>
<s:iterator value="#data[1]" var="data1" status="st">
            <span id="relContent4,<s:property value='#status.count'/>,<s:property value='#st.count'/>,number"><s:property value='#st.count'/>. </span><span id="relContent4,<s:property value='#status.count'/>,<s:property value='#st.count'/>,titlelist"><s:property value="#data1[0]"/></span><span id="relContent4,<s:property value='#status.count'/>,<s:property value='#st.count'/>,flag"><s:property value="#data1[1]"/></span>
</s:iterator>
			
			</td>
			
			</tr>
			
		</s:iterator>
		<tr>
		<td id="bigTitle5">
			<s:if test="tongbao5.size()>0"><%=m %>、各秘书处信息采用情况
			<%i++;
			if(i==1){
				m="一";
			}
			if(i==2){
				m="二";
			}
			if(i==3){
				m="三";
			}
			if(i==4){
				m="四";
			}
			if(i==5){
				m="五";
			}%>
			</s:if>
			
		</td>
		</tr>
		<s:iterator value="tongbao5" var="data" status="status">
			<tr>
			<td>
			<span id="relOutline5,<s:property value='#status.count'/>"><s:if test="#data[0][3]!=0"><s:property value="#data[0][1]"/>　　　</s:if>
			</span><br/>
			
			
<s:iterator value="#data[1]" var="data1" status="st">
            <span id="relContent5,<s:property value='#status.count'/>,<s:property value='#st.count'/>,number"><s:property value='#st.count'/>. </span><span id="relContent5,<s:property value='#status.count'/>,<s:property value='#st.count'/>,titlelist"><s:property value="#data1[0]"/></span><span id="relContent5,<s:property value='#status.count'/>,<s:property value='#st.count'/>,flag"><s:property value="#data1[1]"/></span>
</s:iterator>
			
			</td>
			
			</tr>
			
		</s:iterator>
		
		<tr>
			<td>
				1.标有“★”符号的为领导在《每日要情》上批示的信息；
				2.标有“☆”符号的为领导对呈阅件批示的信息；
				3.标有“□”符号的为领导对呈阅件批转的信息；
				4.标有“●”符号的为国务院办公厅“政府情况交流”采用的信息；
				5.标有“△”符号的为国务院办公厅约稿并采用为“专报”的信息；
				6.标有“▽”符号的为国务院办公厅约稿并采用为“综合、要情”的信息；
				7.标有“◣”符号的为国务院办公厅约稿采用并被国务院领导批示的信息；
				8.标有“▲”符号的为国务院办公厅采用为“专报”并被国务院领导批示的信息；
				9.标有“▼”符号的为国务院办公厅采用为“综合、要情”并被国务院领导批示的信息；
				10.标有“○”符号的为国务院办公厅采用为“综合、要情”的信息；
				11.标有“◇”符号的为领导在《江西政务》上批示的信息；
				12.标有“◆”符号的为领导在《江西政务》增刊上批示的信息；
				13.标有“要情”的为《每日要情》信息刊物采用信息；
				14.标有“送阅”的为直接领导阅知的信息；
				15.标有“专报”的为呈送给省政府主要领导的专报信息。
			</td>
		</tr>
		</table>
	</body>
	
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
		officeObj.ActiveDocument.PageSetup.LeftMargin= 91;
		officeObj.ActiveDocument.PageSetup.RightMargin= 91;
		officeObj.ActiveDocument.PageSetup.TopMargin= 72;
		officeObj.ActiveDocument.PageSetup.BottomMargin= 72;
		officeObj.AddTemplateFromURL("<%=root%>/temp_report_head.doc");
		officeObj.SetBookmarkValue("ch_title", $.trim($("#chTitle").text()));
		officeObj.SetBookmarkValue("ch_body", $.trim($("#chBody").text()));
		officeObj.SetBookmarkValue("ch_date", $.trim($("#chDate").text()));
		officeObj.SetBookmarkValue("ch_top", $.trim($("#chTop").text()));
		
		for(var i=1;i<=5;i++){
		   if(document.getElementById("bigTitle"+i)){
			   officeObj.AddTemplateFromURL("<%=root%>/temp_report_bigtitle.doc");
			   officeObj.SetBookmarkValue("big_title", $.trim( document.getElementById("bigTitle"+i).innerHTML));
			   var doc1 = officeObj.ActiveDocument;
			   doc1.Sections(1).Footers(1).PageNumbers.Add(1,true);
			   doc1.Sections(1).Headers(1).Range.Borders(-3).LineStyle = 0;
			   doc1.BookMarks("big_title").Delete();
			   for(var j=1;j<1000;j++){
				   if(document.getElementById("relOutline"+i+","+j)){
					   officeObj.AddTemplateFromURL("<%=root%>/temp_report_ct.doc");
					   var doc2 = officeObj.ActiveDocument;
					   officeObj.SetBookmarkValue("ct_outline",$.trim(document.getElementById("relOutline"+i+","+j).innerHTML));
					   doc2.BookMarks("ct_outline").Delete();
					   for(var n=1;n<1000;n++){
						   if(document.getElementById("relContent"+i+","+j+","+n+",number")){
							   officeObj.AddTemplateFromURL("<%=root%>/temp_report_cd.doc");
							   var doc3 = officeObj.ActiveDocument;
							   officeObj.SetBookmarkValue("ct_number",document.getElementById("relContent"+i+","+j+","+n+",number").innerHTML);
							   officeObj.SetBookmarkValue("ct_titlelist",document.getElementById("relContent"+i+","+j+","+n+",titlelist").innerHTML);
								officeObj.SetBookmarkValue("ct_flag",document.getElementById("relContent"+i+","+j+","+n+",flag").innerHTML);
								doc3.BookMarks("ct_titlelist").Delete(); 
								doc3.BookMarks("ct_number").Delete(); 
								doc3.BookMarks("ct_flag").Delete(); 
						   }else{
							   n=10000
						   }
					   }
					   
				   }else{
					   j=10000;
				   }
			   }
		   }
		}	
		//officeObj.AddTemplateFromURL("<%=root%>/temp_report_foot.doc");
	};
	buildText();
	

	
	$("#publish").click(function(){
		var rpTitle = $("#rpTitle").val();
		if(rpTitle.length>200){
			alert("通报标题过长!");
			return false;
		}
		if(rpTitle==""){
			alert("通报标题不能为空!");
			return false;
		}
		rpTitle = encodeURIComponent(rpTitle);
		officeObj.PublishAsHTMLToURL("<%=root%>/xxbs/action/infoReport!save.action", "upload", "rpTitle="+rpTitle, null, null);
	});
	
	$("#return").click(function(){
		location = "<%=root%>/xxbs/action/infoReport.action";
 	});
});

</script>
</html>



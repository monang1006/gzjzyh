<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT type="text/javascript">
			var serach;
			var temp;
		  	//统计方法
			function query(){
		  		serach = $("#serach").val();
		  		temp = $("input[name='P']:checked").val();
		  		$("#reporttongji").val("1");
			   //window.parent.frames[1].onsub($("#serach").val(),$("input[name='P']:checked").val(),$("#title").val());   
			   window.frames[0].onsub($("#serach").val(),$("input[name='P']:checked").val(),$("#title").val());
			}
		  	function exportReport(exportType){
			    if($("#reporttongji").val()!="1"){
			       alert("报表没有数据!");
			       return ;
			    }
			    if(serach=="fasong"){
					document.getElementById("myTableForm").action ="<%=root%>/sends/docSend!docsearchs.action";   
				}else{
					document.getElementById("myTableForm").action ="<%=root%>/sends/docSend!getBorrowReports.action";   
				}
				if(temp=="title"){
					document.getElementById("docModel.docTitle").value=$("#title").val();
					document.getElementById("docModel.docCode").value="";
				}else{
					document.getElementById("docModel.docCode").value=$("#title").val();
					document.getElementById("docModel.docTitle").value="";
				}
				document.getElementById("exportType").value=exportType;
				document.getElementById("myTableForm").submit();
			}
		  	function print(){
				if($("#reporttongji").val()!="1"){
			       alert("报表没有数据!");
			       return ;
			    }
			    var url="";
				var reportType="${param.reportType}";
				var orgId="${param.orgId}";
					
					if(serach=="fasong"){
						url ="<%=root%>/sends/docSend!docsearchs.action?exportType=print";  
					}else{
						url ="<%=root%>/sends/docSend!getBorrowReports.action?exportType=print";   
					}
					if(temp=="title"){
						docTitle=$("#title").val();
						url = url + "&docModel.docTitle="+docTitle;
					}else{
						docCode=$("#title").val();
						url = url + "&docModel.docCode="+docCode;
					}
				document.getElementById("applets").innerHTML='<APPLET CODE="PrinterApplet.class" NAME="printerApplet"'+
				'CODEBASE="<%=root%>/applets" ARCHIVE="jasperreports-3.0.0-applet.jar"'+
				'WIDTH="0" HEIGHT="0">'+
				'<PARAM NAME=CODE VALUE="PrinterApplet.class">'+
				'<PARAM NAME=CODEBASE VALUE="<%=root%>/applets">'+
				'<PARAM NAME=ARCHIVE VALUE="jasperreports-3.0.0-applet.jar">'+
				'<PARAM NAME="type"'+
				'	VALUE="application/x-java-applet;version=1.2.2">'+
				'<PARAM NAME="scriptable" VALUE="false">'+
				'<PARAM  id="REPORT_URL" NAME="REPORT_URL"'+
				'	value="'+url+'">'+
				'</APPLET>';
				try{
					printerApplet.btnPrintActionPerformed();
				}catch(e){ 
					//alert("你没有安装JRE,请安装JRE1.6!");
					if(confirm("你没有安装JRE,是否去下载安装JRE1.6?")){
						window.open ('<%=root%>/sends/docSend!downLoad.action','newwindow');
					}
				}
			}
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow:hidden;BACKGROUND:#ffffff">
			<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
							<table width="100%" height="40">
								<tr>
									<td>&nbsp;</td>
									<td width="20%" align="left">
								   		<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">&nbsp;
								   		公文查询
								 	<br></td>
								 	
								 	<s:form id="myTableForm" action="/sends/docSend!docsearchs.action">
								 	<input type="hidden" id="exportType" name=exportType />
								 	<input type="hidden" id="reportType" name="reportType" />
									<input type="hidden" id="docModel.docTitle" name="docModel.docTitle" value=""/>
									<input type="hidden" id="docModel.docCode" name="docModel.docCode" value="" />
									
									<input type="hidden" id="reporttongji" name="reporttingji">
								 	
									<td align="right">
								    <SPAN style="color: blue">查询范围:</SPAN>
								    
								    <SELECT id="serach">
								    	<option value="fasong">发送文件</option>
								    	<option value="jieshou">接收文件</option>
								    </SELECT>
								    
								    <SPAN style="color: blue">查询要素：</SPAN>
								    
								    <INPUT type="radio" name="P" value="title" checked>文件标题
								    <INPUT type="radio" name="P" value="fontSize">文件字号
								    
								    <SPAN style="color: blue">查询内容：</SPAN>
								    
								   	<input id="title" name="tetle" type="text">
									<input id="column" name="column" type="button"
											class="input_bg" value="查询" onclick="query()">
											
									<input id="column4" name="column4" type="button"
											class="input_bg" value="打印" onclick="print()">
											
									<input id="column2" name="column2" type="button" class="input_bg" value="导出成EXCEL"
											onclick="exportReport('excel')">
											
									<input id="column3" name="column3" type="button"
											class="input_bg" value="导出成PDF" onclick="exportReport('pdf')">
										<div id="applets" style="display: none">	
									</td>
									</s:form>
								</tr>
							</table>
					</td>
				</tr>
			</table>
			<iframe id='SearchContent1' style="display:" name='SearchContent1'
				src='<%=root%>/fileNameRedirectAction.action?toPage=sends/docSearchResult.jsp'
				frameborder=0 scrolling=auto width='100%' height='89%'></iframe>
	</body>
</HTML>
<br>
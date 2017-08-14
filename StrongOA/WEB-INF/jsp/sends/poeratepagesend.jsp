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
		  	//统计方法
			function query(){
			   if($("#reportDate").val()==""){
				   alert("请选择统计开始时间！");
				   return;
			   }
			     
			    if($("#reportEndDate").val()==""){
				   alert("请选择统计结束时间！");
				   return;
			   }
			    var reportDate = new Date($("#reportDate").val().replace(/-/g,"/"));
			    var reportEndDate = new Date($("#reportEndDate").val().replace(/-/g,"/"));
			    if(reportDate.getTime()>reportEndDate.getTime()){
			    	alert("结束时间不能比开始时间早！")
			    	return;
			    }
			   $("#reporttongji").val("1");
			    window.frames[1].onsub($("input[name='P']:checked").val(),$("#title").val(),$("#reportDate").val(),$("#reportEndDate").val());  
			}	
			
			function exportReport(exportType){
			    if($("#reporttongji").val()!="1"){
			       alert("报表没有数据!");
			       return ;
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
				
					var reportDate=document.getElementById("reportDate").value;
					var reportEndDate=document.getElementById("reportEndDate").value;
					url="<%=root%>/sends/docSend!getBorrowReportSend.action?exportType=print&reportType="+reportType+"&reportDate="+reportDate+"&reportEndDate="+reportEndDate;
				
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
						<s:form id="myTableForm" action="/sends/docSend!getBorrowReportSend.action">
							<input type="hidden" id="exportType" name=exportType />
							<input type="hidden" id="reportType" name="reportType" />
							
							<input type="hidden" id="reporttongji" name="reporttingji">
							<table width="100%" height="40">
								<tr>
									<td width="5%" align="center">
										<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
									<br></td>
									<td width="10%" align="left">
								   		发文统计报表
								 	<br></td>
									<td align="right">
								    统计日期：
								    <strong:newdate id="reportDate"  width="100px;"  name="reportDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportDate}"  title="统计日期"/> --
								<strong:newdate id="reportEndDate"  width="100px;"  name="reportEndDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportEndDate}"  title="统计日期"/>
												
									<SPAN style="color: blue">查询要素：</SPAN>
								    
								    <INPUT type="radio" name="P" value="title" checked>文件标题
								    <INPUT type="radio" name="P" value="fontSize">文件字号
								    
								    <SPAN style="color: blue">查询内容：</SPAN>
								    
								   	<input id="title" name="title" type="text">
												
									<input id="column" name="column" type="button"
											class="input_bg" value="统计" onclick="query()">
									<input id="column4" name="column4" type="button"
											class="input_bg" value="打印" onclick="print()">
											
									<input id="column2" name="column2" type="button" class="input_bg" value="导出成EXCEL"
											onclick="exportReport('excel')">
											
									<input id="column3" name="column3" type="button"
											class="input_bg" value="导出成PDF" onclick="exportReport('pdf')">
										<div id="applets" style="display: none">	
									</td>
								</tr>
							</table>
						</s:form>
							<iframe id="annexFrame" style="display:none"></iframe>
					</td>
				</tr>
			</table>
			<iframe id='SearchContent1' style="display:" name='SearchContent1'
				src='<%=root%>/fileNameRedirectAction.action?toPage=sends/reportpagesend.jsp'
				frameborder=0 scrolling=auto width='100%' height='89%'></iframe>
	</body>
</HTML>
<br>
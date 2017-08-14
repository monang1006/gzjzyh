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
			   $("#reporttongji").val("1");
			   window.parent.frames[1].onsub("",$("#reportDate").val(),$("#reportEndDate").val());   
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
					url="<%=root%>/senddocRegist/sendDocRegist!getBorrowReport.action?exportType=print&reportType="+reportType+"&reportDate="+reportDate+"&reportEndDate="+reportEndDate;
				
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
					alert("你没有安装JRE,请安装JRE1.6!");
				}
			}
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow:hidden;BACKGROUND:#ffffff">
			<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate1/WdatePicker.js"></script>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="/senddocRegist/sendDocRegist!getBorrowReport.action">
							<input type="hidden" id="exportType" name=exportType />
							<input type="hidden" id="reportType" name="reportType" />
							
							<input type="hidden" id="reporttongji" name="reporttingji">
							<table width="100%" height="40">
								<tr>
									<td width="5%" align="center">
										<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
									<br></td>
									<td width="20%" align="left">
								   		发文登记查询
								 	<br></td>
									<td align="right">
								    统计日期：
								    <strong:newdate id="reportDate"  width="100px;"  name="reportDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportDate}"  title="统计日期"/> --
								<strong:newdate id="reportEndDate"  width="100px;"  name="reportEndDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportEndDate}"  title="统计日期"/>
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
	</body>
</HTML>
><br>
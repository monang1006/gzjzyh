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
			var unitORall;  //单位发文 所有发文标识
		  	//统计方法
			function query(temp){
		  		var reportEndDate;
			   if($("input[name='P']:checked").val()=="A"){
				   reportEndDate = $("#reportEndDate").val();
			   }else if($("input[name='P']:checked").val()=="B"){
				    reportEndDate = $("#reportEndDate1").val();
			   }else{
				    reportEndDate = $("#reportEndDate2").val();
			   }
			   if(reportEndDate==null||reportEndDate==""){
				   alert("请输入日期！");
				   return;
			   }
			   unitORall = temp;
			   $("#reporttongji").val("1");
			   //window.parent.frames[1].onsub(reportEndDate,temp);  
			   window.frames[0].onsub(reportEndDate,temp);
			}	
		  	function temp(temp){
		  		if(temp==0){
		  			$("#tempes").attr("style","display: none;");
		  			$("#reportEndDate2").val("");
		  			$("#tempe").attr("style","display: none;");
		  			$("#reportEndDate1").val("");
		  			$("#temp").attr("style","");
		  		}else if(temp==1){
		  			$("#tempes").attr("style","display: none;");
		  			$("#reportEndDate2").val("");
		  			$("#temp").attr("style","display: none;");
		  			$("#reportEndDate").val("");
		  			$("#tempe").attr("style","");
		  		}else{
		  			$("#temp").attr("style","display: none;");
		  			$("#reportEndDate").val("");
		  			$("#tempe").attr("style","display: none;");
		  			$("#reportEndDate1").val("");
		  			$("#tempes").attr("style","");
		  		}
		  	}
		  	function exportReport(exportType){
			    if($("#reporttongji").val()!="1"){
			       alert("报表没有数据!");
			       return ;
			    }
			    var reportEndDate;
			   if($("input[name='P']:checked").val()=="A"){
				   reportEndDate = $("#reportEndDate").val();
			   }else if($("input[name='P']:checked").val()=="B"){
				    reportEndDate = $("#reportEndDate1").val();
			   }else{
				    reportEndDate = $("#reportEndDate2").val();
			   }
			   if(reportEndDate==null||reportEndDate==""){
				   alert("请输入日期！");
				   return;
			   }
			    document.getElementById("reportTime").value=reportEndDate;
				document.getElementById("unitORall").value=unitORall;
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
				
					  var reportEndDate;
					   if($("input[name='P']:checked").val()=="A"){
						   reportEndDate = $("#reportEndDate").val();
					   }else if($("input[name='P']:checked").val()=="B"){
						    reportEndDate = $("#reportEndDate1").val();
					   }else{
						    reportEndDate = $("#reportEndDate2").val();
					   }
					var reportTime=reportEndDate;
					var unitORall=unitORall;
					url="<%=root%>/sends/docSend!docSearchReport.action?exportType=print&reportTime="+reportTime+"&unitORall="+unitORall;
				
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
								   		统计报表
								 	<br></td>
								 	
								 	<s:form id="myTableForm" action="/sends/docSend!docSearchReport.action">
								 	<input type="hidden" id="reportTime" name="reportTime" />
									<input type="hidden" id="unitORall" name="unitORall"/>
									<input type="hidden" id="exportType" name=exportType />
									<input type="hidden" id="reportType" name="reportType" />
									<input type="hidden" id="reporttongji" name="reporttingji">
									
									<td align="right">
								    <SPAN style="color: blue">请选择报表类型:</SPAN>
								    <INPUT type="radio" name="P" value="A" onclick="temp(0)">年报
								    <INPUT type="radio" name="P" value="B" onclick="temp(1)">月报
								    <INPUT type="radio" name="P" value="C" onclick="temp(2)" checked>日报
								    
								    <SPAN style="color: blue">请选择日期：</SPAN>
								    
								    <SPAN id="tempes" style="display: ">
								    <strong:newdate id="reportEndDate2"  width="100px;"  name="reportEndDate2"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportEndDate2}"  title="统计日期"/>
									</SPAN>
									 <SPAN id="temp" style="display: none;">
								    <strong:newdate id="reportEndDate"  width="100px;"  name="reportEndDate"
												dateform="yyyy" isicon="true" dateobj="${reportEndDate}"  title="统计日期"/>
									</SPAN>
									 <SPAN id="tempe" style="display: none;">
									<strong:newdate id="reportEndDate1"  width="100px;"  name="reportEndDate1"
												dateform="yyyy-MM" isicon="true" dateobj="${reportEndDate1}"  title="统计日期"/>
									</SPAN>
												
									<input id="column" name="column" type="button"
											class="input_bg" value="单位发文" onclick="query(0)">
									<input id="column" name="column" type="button"
											class="input_bg" value="所有发文" onclick="query('all')">
											
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
			<table><tr><td style="color: red;height: 10px"></td></tr></table>
			<iframe id='ReportResult' style="display:" name='ReportResult'
				src='<%=root%>/fileNameRedirectAction.action?toPage=sends/docReportResult.jsp'
				frameborder=0 scrolling=auto width='100%' height='89%'></iframe>
	</body>
</HTML>
<br>
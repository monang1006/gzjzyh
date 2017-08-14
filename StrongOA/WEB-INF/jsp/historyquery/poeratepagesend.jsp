<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<!--右键菜单脚本 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT type="text/javascript">
			//统计方法
			function query(){
			<%--
			   if($("#reportDate").val()==""){
				   alert("请选择统计开始时间！");
				   return;
			   }
			    if($("#reportEndDate").val()==""){
				   alert("请选择统计结束时间！");
				   return;
			   }
			 --%>
			 	var s=$("#reportDate").val();
			 	var end=$("#reportEndDate").val();
			 	if(s>end&&s!=""&&end!=""){
			 		alert("结束日期需大于开始日期！");
			 		return;
			 	}
			 
			   $("#reporttongji").val("1");
			   window.parent.frames[1].onsub("",$("#reportDate").val(),$("#reportEndDate").val(),$("#docTitle").val());   
			}	
			
			function exportReport(exportType){
			    if($("#reporttongji").val()!="1"){
			       alert("报表没有数据!");
			       return ;
			    }
			     if(window.parent.frames[1].document.getElementById("totalNum").value==0){
			    	alert("报表内容为空!");
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
			    if(window.parent.frames[1].document.getElementById("totalNum").value==0){
			    	alert("报表内容为空!");
			       	return ;
			    }
			    var url="";
				var reportType="${param.reportType}";
				var orgId="${param.orgId}";
				var reportDate=document.getElementById("reportDate").value;
				var reportEndDate=document.getElementById("reportEndDate").value;
				var docTitle=document.getElementById("docTitle").value;
				url="<%=root%>/historyquery/query!getBorrowReportSend.action?exportType=print&reportType="+reportType+"&reportDate="+reportDate+"&reportEndDate="+reportEndDate+"&docTitle="+docTitle;
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
			
			//导出EXCEL数据 
			function exportExcels(){
			    if($("#reporttongji").val()!="1"){
					       alert("报表没有数据!");
					       return ;
					    }
			    if(window.parent.frames[1].document.getElementById("totalNum").value==0){
			    	alert("报表内容为空!");
			       	return ;
			    }
				var reportDate=$("#reportDate").val();
				 var reportEndDate=$("#reportEndDate").val();
				 var docTitle= $("#docTitle").val();
				 var tt=encodeURI(docTitle);
			     document.getElementById('tempframe').src="<%=root%>/historyquery/query!getBorrowReportSend.action?exportType=excel&reportDate="
						+ reportDate
						+ "&reportEndDate="
						+ reportEndDate
						+ "&docTitle="
						+ encodeURI(docTitle);
			}
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow: hidden; BACKGROUND: #ffffff">
		<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate1/WdatePicker.js"></script>
		<iframe scr='' id='tempframe' name='tempframe' style='display: none'></iframe>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
			<tr>
				<td height="60" >
					<s:form id="myTableForm" action="/historyquery/query!getBorrowReportSend.action">
						<input type="hidden" id="exportType" name=exportType />
						<input type="hidden" id="reportType" name="reportType" />
						<input type="hidden" id="reporttongji" name="reporttingji">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="3" class="table_headtd">
									 <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>收文统计报表</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="query();"><img src="<%=root%>/images/operationbtn/statistic.png"/>&nbsp;统&nbsp;计&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="print();"><img src="<%=root%>/images/operationbtn/printer.png"/>&nbsp;打&nbsp;印&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="exportReport('excel');"><img src="<%=root%>/images/operationbtn/Export_to_excel.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;EXCEL&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="exportReport('pdf');"><img src="<%=root%>/images/operationbtn/Export_to_pdf.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;PDF&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
															<div id="applets" style="display: none">
															</div>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="95%" align="right" border="0" cellpadding="00" cellspacing="0">
										<tr>
											<td>
												收文字号:
												<input id="docTitle" name="docTitle" type="text" size="25" maxlength="16">
											</td>
											<td>
												统计日期:
												<strong:newdate id="reportDate" width="115px;" name="reportDate" dateform="yyyy-MM-dd"
													isicon="true" dateobj="${reportDate}" title="统计日期" />
												--
												<strong:newdate id="reportEndDate" width="115px;" name="reportEndDate"
													dateform="yyyy-MM-dd" isicon="true" dateobj="${reportEndDate}" title="统计日期" />
											</td>
											<td width="50"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</s:form>
					<iframe id="annexFrame" style="display: none"></iframe>
				</td>
			</tr>
		</table>
	</body>
</HTML>
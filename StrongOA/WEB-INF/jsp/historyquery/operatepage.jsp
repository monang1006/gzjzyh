<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT type="text/javascript">
		  	function setValue(cdtion,infoSetValue,groupByFiled){
		     	$("#condition").val(encodeURI(cdtion));
		     	$("#infoSetValue").val(infoSetValue);
		     	$("#groupByFiled").val(groupByFiled);
		   	}
		   	
			function query(){
				var url= scriptroot+'/historyquery/query!initialQuery.action?module=recordQuery';
				OpenWindow(url,480,450,window);	
			}	
			
			function exportReport(exportType){
				if(document.getElementById("infoSetValue").value==""){
					alert("报表没有数据！");
					return;
				}
				document.getElementById("exportType").value=exportType;
				document.getElementById("myTableForm").submit();
			}
			
			function print(){
				var infoSetValue=document.getElementById("infoSetValue").value;
				if(infoSetValue==""){
					alert("报表没有数据！");
					return;
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
				'	value="<%=root%>/historyquery/query!generateReport.action?exportType=print&infoSetValue='+infoSetValue+'">'+
				'</APPLET>';
				try{
					printerApplet.btnPrintActionPerformed();
				}catch(e){   
					alert("你没有安装JRE,请安装JRE1.6!");
				}
			}
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin"
		style="overflow:hidden;BACKGROUND:#ffffff">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<s:form id="myTableForm"
						action="/historyquery/query!generateReport.action">
						<input type="hidden" id="condition" name="condition"
							value="${condition}" />
						<input type="hidden" id="groupByFiled" name="groupByFiled"
							value="${groupByFiled}" />
						<input type="hidden" id="module" name="module" value="recordQuery" />
						<input type="hidden" id="infoSetValue" name="infoSetValue" />
						<input type="hidden" id="exportType" name=exportType />
						<table width="100%" height="40">
							<tr>
								<td>&nbsp;</td>
								<td width="10%" align="left">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									综合查询
								</td>
								<td align="right">
									<input id="column" name="column" type="button" class="input_bg"
										value="复合查询" onclick="query()">
									<input id="column4" name="column4" type="button"
										class="input_bg" value="打印"
										onclick="print()">
									<input id="column2" name="column2" type="button"
										class="input_bg" value="导出成EXCEL"
										onclick="exportReport('excel')">
									<input id="column3" name="column3" type="button"
										class="input_bg" value="导出成PDF" onclick="exportReport('pdf')">
									<div id="applets" style="display: none">
									</div>
								</td>
							</tr>
						</table>
					</s:form>
				</td>
			</tr>
		</table>
	</body>
</HTML>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
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
				//var pageNo = window.parent.frames[1].getPageNo();
				document.getElementById("exportType").value="html";
				$("form").submit();
			}	
			
			function exportReport(exportType){
				var totalCount = window.parent.frames[1].document.getElementById("totalCount").value;
				if(totalCount == "0"){
					alert("报表没有数据！");
					return;
				}
				document.getElementById("exportType").value=exportType;
				$("form").submit();
			}
			//打印
			function print(){
				var totalCount = window.parent.frames[1].document.getElementById("totalCount").value;
				if(totalCount == "0"){
					alert("报表没有数据！");
					return;
				}
				var url = $("form").attr("action");
				url += "?definitionId="+$("#definitionId").val();
				url += "&workflowCode="+encodeURI(encodeURI($("#workflowCode").val()));
				url += "&workflowTitle="+encodeURI(encodeURI($("#workflowTitle").val()));
				url += "&exportType=print";
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
	<body class="contentbodymargin"
		style="overflow:hidden;BACKGROUND:#ffffff">
		<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			style="vertical-align: top;">
			<tr>
				<td height="100%">
					<s:form id="myTableForm" action="/report/reportDefine!workflow.action" target="SearchContent2">
					<%--<s:hidden id="pageNo" name="pageReport.pageNo"></s:hidden>--%>
					<s:hidden id="definitionId" name="definitionId"></s:hidden>
					<s:hidden id="exportType" name="exportType"></s:hidden>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
						 <table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>${definitionName }</strong>
												</td>
												
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												  <td>
												<span class="wz">流程标题：</span>
									           <input type="text" id="workflowTitle" name="workflowTitle" size="15" maxlength="40">&nbsp;
												</td>
												  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="query();"><img src="<%=root%>/images/operationbtn/query.png"/>&nbsp;查&nbsp;询&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="print();"><img src="<%=root%>/images/operationbtn/printer.png"/>&nbsp;打&nbsp;印&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="exportReport('excel');"><img src="<%=root%>/images/operationbtn/Export_to_excel.png"/>&nbsp;导出成EXCEL&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="exportReport('pdf');"><img src="<%=root%>/images/operationbtn/Export_to_pdf.png"/>&nbsp;导出成PDF&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                   <td width="2%"></td>
												</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
						
					</s:form>
				</td>
			</tr>
		</table>
		</DIV>
	</body>
</HTML>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@ include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT type="text/javascript">
			//统计方法
			function query(){
			   $("#reporttongji").val("1");
			   window.parent.frames[1].onsub("",$("#qszt").val(),$("#recvNum").val(),$("#workTitle").val(),$("#docNumber").val(),$("#departSigned").val(),$("#zbcs").val(),$("#csqs").val(),$("#recvstartTime").val(),$("#recvendTime").val(),$("#recvStartNum").val(),$("#recvEndNum").val());   
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
				var qszt=$("#qszt").val();
				var recvNum=$("#recvNum").val();
				var workTitle=$("#workTitle").val();
				var docNumber=$("#docNumber").val();
				var departSigned=$("#departSigned").val();
				var zbcs=$("#zbcs").val();
				var csqs=$("#csqs").val();
				var recvstartTime=$("#recvstartTime").val();
				var recvendTime=$("#recvendTime").val();
				var recvStartNum=$("#recvStartNum").val();
				var recvEndNum=$("#recvEndNum").val();
				url="<%=root%>/historyquery/query!getDjcxReport.action?exportType=print&reportType="+reportType+"&qszt="+qszt+"&recvNum="+
						recvNum+"&workTitle="+workTitle+"&docNumber="+docNumber+"&departSigned="+departSigned+"&zbcs="+zbcs+"&csqs="+
						csqs+"&recvstartTime="+recvstartTime+"&recvendTime="+recvendTime+"&recvStartNum="+recvStartNum+"&recvEndNum="+recvEndNum;
				document.getElementById("applets").innerHTML='<APPLET CODE="PrinterApplet.class" NAME="printerApplet"'+
						'CODEBASE="<%=root%>/applets" ARCHIVE="jasperreports-3.0.0-applet.jar"'+'WIDTH="0" HEIGHT="0">'+
						'<PARAM NAME=CODE VALUE="PrinterApplet.class">'+
						'<PARAM NAME=CODEBASE VALUE="<%=root%>/applets">'+
						'<PARAM NAME=ARCHIVE VALUE="jasperreports-3.0.0-applet.jar">'+
						'<PARAM NAME="type"'+
						' VALUE="application/x-java-applet;version=1.2.2">'+
						'<PARAM NAME="scriptable" VALUE="false">'+
						'<PARAM  id="REPORT_URL" NAME="REPORT_URL"'+
						' value="'+url+'">'+
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
				var qszt=$("#qszt").val();
				var recvNum=$("#recvNum").val();
				var workTitle=$("#workTitle").val();
				var docNumber=$("#docNumber").val();
				var departSigned=$("#departSigned").val();
				var zbcs=$("#zbcs").val();
				var csqs=$("#csqs").val();
				var recvstartTime=$("#recvstartTime").val();
				var recvendTime=$("#recvendTime").val();
				var recvStartNum=$("#recvStartNum").val();
				var recvEndNum=$("#recvEndNum").val();
				document.getElementById('tempframe').src="<%=root%>/historyquery/query!exportdjcxExcels.action?qszt="+qszt+"&recvNum="+
						encodeURI(recvNum)+"&workTitle="+encodeURI(workTitle)+"&docNumber="+encodeURI(docNumber)+"&departSigned="+
						encodeURI(departSigned)+"&zbcs="+encodeURI(zbcs)+"&csqs="+encodeURI(csqs)+"&recvstartTime="+recvstartTime+"&recvendTime="+
						recvendTime+"&recvStartNum="+encodeURI(recvStartNum)+"&recvEndNum="+encodeURI(recvEndNum);
				//document.getElementById('tempframe').src="<%=root%>/historyquery/query!exportExcels.action";
			}
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow: hidden; BACKGROUND: #ffffff">
		<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate1/WdatePicker.js"></script>
		<iframe scr='' id='tempframe' name='tempframe' style='display: none'></iframe>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
			<tr>
				<td height="100%">
					<s:form id="myTableForm" action="/historyquery/query!getDjcxReport.action">
						<input type="hidden" id="exportType" name=exportType />
						<input type="hidden" id="reportType" name="reportType" />
						<input type="hidden" id="reporttongji" name="reporttingji" value="1">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr height="40">
								<td width="30%">
									&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
									登记查询
								</td>
								<td width="70%">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td>
												<a class="Operation" href="javascript:query();">
													<img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" class="img_s">
													查询&nbsp;</a>
											</td>
											<td width="5">
											</td>
											<td>
												<a class="Operation" href="javascript:print();">
													<img src="<%=root%>/images/ico/printer.gif" width="15" height="15" class="img_s">
													打印&nbsp;</a>
												<div id="applets" style="display: none">
												</div>
											</td>
											<td width="5">
											<td>
												<a class="Operation" href="javascript:exportExcels();">
													<img src="<%=root%>/images/ico/daochu.gif" width="15" height="15" class="img_s">
													导出为EXCEL&nbsp;</a>
											</td>
											<td width="5">
												&nbsp;
											</td>
									</table>
								</td>
							</tr>
							<tr>
								<td align="left" colspan="2">
									<table width="95%" border="0" align="right" cellpadding="0" cellspacing="0">
										<tr>
											<td>
												签收状态:
												<s:select name="qszt" id="qszt" list="#{'':'选状态','0':'未签收','1':'已签收'}" listKey="key" 
													listValue="value" style="width:30%" />
											</td>
											<td>
												收文类型:
												<s:select list="levelList" listKey="dictItemName" listValue="dictItemName" headerKey=""
													headerValue="选择类型" id="recvNum" name="recvNum" style="width:40%" />
											</td>
											<td>
												收文编号:
												<input id="recvStartNum" name="recvStartNum" type="text" size="16" class="required">
												-
												<input id="recvEndNum" name="recvEndNum" type="text" size="16" class="required">
											</td>
										</tr>
										<tr>
											<td>
												文件标题:
												<input id="workTitle" name="workTitle" type="text" size="35" maxlength="16">
											</td>
											<td>
												来文字号:
												<input id="docNumber" name="docNumber" type="text" size="16" maxlength="16">
											</td>
											<td>
												来文单位:
												<input id="departSigned" name="departSigned" type="text" size="23" maxlength="16">
											</td>
										</tr>
										<tr>
											<td>
												主办处室:
												<input id="zbcs" name="zbcs" type="text" size="18" class="required">
												<input id="tempfileDepartment" name="tempfileDepartment" type="hidden">
												<input name="gorscelet" type="button" class="input_bg" value="选 择" onclick="departscelet('zbcs')">
											</td>
											<td>
												处室签收:
												<input id="csqs" name="csqs" type="text" size="18" class="required">
											</td>
											<td>
												来文日期:
												<strong:newdate id="recvstartTime" width="115px;" name="recvstartTime"
													dateform="yyyy-MM-dd" isicon="true" dateobj="${recvstartTime}" title="来文日期" />
												--
												<strong:newdate id="recvendTime" width="115px;" name="recvendTime" 
													dateform="yyyy-MM-dd" isicon="true" dateobj="${recvendTime}" title="来文日期" />
											</td>
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

	<script type="text/javascript">
	var deptbudget_basedata_width = 300;
	var deptbudget_basedata_height = 360;
	function departscelet(xx) {
		var objId = "tempfileDepartment";
		var objName = xx;
		var URLStr = scriptroot + "/historyquery/query!orgtree.action?objId=" + objId + "&objName="
				+ objName + "&moduletype=wpige";
		var sty = "dialogWidth:" + deptbudget_basedata_width + "px;dialogHeight:"
				+ deptbudget_basedata_height + "px;status:no";
		showModalDialog(URLStr, window, sty);
	}
</script>
</HTML>

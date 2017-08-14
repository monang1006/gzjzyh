<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%String mark = request.getParameter("Mark"); //跳转标识%>
<HTML>
	<HEAD>
		<TITLE>report</TITLE>
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
		<STYLE type="text/css">
		body{
		   overflow: auto;
		}
		</STYLE>
		<SCRIPT type="text/javascript">
		  	//统计方法
			function query(){
		  		serach = $("#serach").val();
		  		temp = $("input[name='P']:checked").val();
		  		$("#reporttongji").val("1");
			   //window.parent.frames[1].onsub($("#serach").val(),$("input[name='P']:checked").val(),$("#title").val());   
			   window.frames[0].onsub($("#serach").val(),$("input[name='P']:checked").val(),$("#title").val());
			}
		  	function exportReport(exportType){
		  		var mark = window.frames[0].document.getElementById("mark").value;
		  		$("#mark").val(mark);
			    if(window.frames[0].document.getElementById("reporttongji").value!="1"){
			       alert("报表没有数据!");
			       return ;
			    }
				document.getElementById("exportType").value=exportType;
				document.getElementById("year1").value=document.getElementById("year").value;
				document.getElementById("startTime").value=document.getElementById("selectTime").value;
				document.getElementById("yearGw1").value=document.getElementById("yearGw").value;
				document.getElementById("selectDept1").value=document.getElementById("selectDept").value;
				//alert(document.getElementById("selectDept1").value);
				document.getElementById("myTableForm").submit();
				
			}
			function fresh(){
			     var url = "<%=root%>/report/reportDoc!mark.action?mark=<%=mark%>";
			     var mark=<%=mark%>;
			    if("1"==mark){
			    var yearGw=document.getElementById("yearGw").value;
			    var selectDept=document.getElementById("selectDept").value;
			       $("#SearchContent1").attr("src",url+"&yearGw="+yearGw+"&selectDept="+encodeURI(encodeURI(selectDept)));
			    }else{
			    var selectTime=document.getElementById("selectTime").value;
			    var year=document.getElementById("year").value;
			    if(year==null||year==""){
			      alert("请先填写年度选项!");
			      return;
			    }
			    $("#SearchContent1").attr("src",url+"&selectTime="+selectTime+"&year="+year);}
			}
		  	function print(){
		  		var mark = window.frames[0].document.getElementById("mark").value;
		  		$("#mark").val(mark);
				if(window.frames[0].document.getElementById("reporttongji").value!="1"){
			       alert("报表没有数据!");
			       return ;
			    }
			    var url="";
				var reportType="${param.reportType}";
				var orgId="${param.orgId}";
				var mark = document.getElementById("mark");
				url ="<%=root%>/report/reportDoc!mark.action?mark=mark"; 
				
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
			<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	</HEAD>
	<body class="contentbodymargin" style="overflow:hidden;BACKGROUND:#ffffff">
	    <table border="0" cellpadding="0" cellspacing="0" style="height: 100%;width: 100%;">
			<tr style="height: 12%;">
			    <td >
		<% if(!"1".equals(mark)){%> 
		<table border="0" cellpadding="0" cellspacing="1"
						class="table1" style="background: #FFFFFF; border: 0px; margin-left: 20px;">
						&nbsp;&nbsp;&nbsp;
						<tr>
						<td >
						年度：
						<strong:newdate name="startDate" id="year" skin="whyGreen"
					isicon="true" dateform="yyyy" title="年度" width="150"></strong:newdate>
						季度： 
						<select id="selectTime" style="width:100px;">
						<option value="1">第一季</option> 
						<option value="2">第二季</option> 
						<option value="3" >第三季</option> 
						<option value="4">第四季</option> 
						</select>						
						&nbsp;
						<img src="<%=frameroot%>/images/sousuo.gif" id="img_sousuo"
					      width="17" height="16" style="cursor: hand;" title="单击搜索" onclick="fresh();" >
					     </td> 
					</td>
					</tr>
					</table>
					<% }else{%>
					<table width="98%" border="0" cellpadding="0" cellspacing="1"
						class="table1" style="background: #FFFFFF; border: 0px; margin-left: 20px;">
						&nbsp;&nbsp;&nbsp;
						<tr>
						<td >
						年月：
						<strong:newdate name="yearGw" id="yearGw" skin="whyGreen"
					isicon="true" dateform="yyyy-MM" title="年月份" width="150"></strong:newdate>
						处室： 
						<s:select name="selectDept" id="selectDept" list="allDept" listKey="department" listValue="department" headerKey="" headerValue="--请选择--" />
					
						&nbsp;
						<img src="<%=frameroot%>/images/sousuo.gif" id="img_sousuo1"
					      width="17" height="16" style="cursor: hand;" title="单击搜索" onclick="fresh();" >
					     </td> 
					</td>
					</tr>
					</table>
					<% }%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
							<table width="100%" height="40">
								<tr>
									<td>&nbsp;</td>
									<td width="20%" align="left">
								   	<br></td>
								 	<s:form id="myTableForm" action="/report/reportDoc!mark.action">
								 	<input type="hidden" id="exportType" name="exportType" />
								 	<input type="hidden" id="reportType" name="reportType" />
									<input type="hidden" id="year1" name="year" />
									<input type="hidden" id="startTime" name="selectTime" />
									<input type="hidden" id="yearGw1" name="yearGw" />
									<input type="hidden" id="selectDept1" name="selectDept" />
									<input type="hidden" id="reporttongji" name="reporttingji">
									<input type="hidden" id="mark" name="mark" />
									
									<td align="right">
											
									 <!--  <input id="column4" name="column4" type="button"
											class="input_bg" value="打印" onclick="exportReport('print')">--> 
											
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
			</div>
			</td></tr>
			<tr style="height: 88%;"><td >
				<iframe id='SearchContent1' style="display:" name='SearchContent1'
					src='<%=root%>/report/reportDoc!mark.action?mark=<%=mark%>'
					frameborder=0 scrolling=auto width='100%' height='100%'></iframe>
			    </td>
			</tr>
		</table>
	</body>
</HTML>
<br>
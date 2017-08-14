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
				var type = document.getElementById("serach").value;
		  		var reportBeginDate = new Date($("#reportBeginDate").val().replace(/-/g,"/"));
		  		var reportEndDate = new Date($("#reportEndDate").val().replace(/-/g,"/"));
		  		if(reportBeginDate.getTime()-reportEndDate.getTime()>0){
		  			alert("开始日期应比结束日期早！");
		  			return;
		  		}
			   window.parent.frames[1].onsub($("#reportBeginDate").val(),$("#reportEndDate").val(),$("#serach").val(),type);   
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
								   		收发统计
								 	<br></td>
									<td align="right">
								    
								    <SPAN style="color: blue">起始日期：</SPAN>
								    <strong:newdate id="reportBeginDate"  width="100px;"  name="reportBeginDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportBeginDate}"  title="起始日期"/>
									 <SPAN style="color: blue">截止日期：</SPAN>
								    <strong:newdate id="reportEndDate"  width="100px;"  name="reportEndDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportEndDate}"  title="截止日期"/>
												
									 <SPAN style="color: blue">统计要素：</SPAN>	
									 <SELECT id="serach">
								    	<option value="receive">收文</option>
								    	<option value="send">发文</option>
								    </SELECT>		
									<input id="column" name="column" type="button"
											class="input_bg" value="统计" onclick="query()">
									</td>
								</tr>
							</table>
					</td>
				</tr>
			</table>
	</body>
</HTML>
><br>
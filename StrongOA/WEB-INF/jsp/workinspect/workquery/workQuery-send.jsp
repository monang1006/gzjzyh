<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<HTML>
	<HEAD>
		<TITLE>我的发送件查询_已不用</TITLE>
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
			<%
		       String reportType=(String)request.getParameter("reportType");
			 %>
		<SCRIPT type="text/javascript">
	
		
		
		
		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin" style="overflow:hidden;BACKGROUND:#ffffff">
			<script language="javascript" type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%"  style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<s:form id="myTableForm" action="">
							<input type="hidden" id="exportType" name=exportType />
							<input type="hidden" id="reporttongji" name="reporttingji"/>
							<input type="hidden" id="reportType" name="reportType" value="<%=reportType %>"/>
							<table width="100%" height="25" <td height="40"
								<tr>
									<td width="50%" >
								 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
								   标 &nbsp; 题: <input typte="text"  id="person" name="person" value="${person}" style="width:250px">
								    </tr>
								    <td width="50%" >
							 	   工作来源: <input typte="text"  id="ownerperson" name="ownerperson" value="${ownerperson}" style="width:250px">
							 	   </td>
							 	   </tr>
							 	   <tr>
							 	   <td width="50%">
							 	    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 
							 	   任务编号: <input typte="text"  id="ownerperson" name="ownerperson" value="${ownerperson}" style="width:250px">
							 	   </td>
							 	   <td width="50%">
							              办理状态: <select name="status" id="status" style="width:100px">
							 	                <option>全部</option>
							 	    			<option>已办结</option>
							 	    			<option>待签收</option>
							 	    			<option>办理中</option>
							 	    	   </select>
							 	    	
							 	    </td>
							 	    </tr>
								 <tr>
								 	<td width="50%">
								 	 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; 
								   		发送人: <input typte="text"  id="person" name="person" value="${person}" style="width:250px">
								   </td>
								   <td width="50%">
								 		紧急程度: <s:select name="selectTaskType" list="#{'1':'紧急','0':'快速','1':'普通'}" listKey="key"
											listValue="value" cssStyle="width:100px" onchange='$("#img_sousuo").click();'/>
									</td>
								</tr>
								<tr>
									<td width="50%">
									 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; 
								  		办理时限: <strong:newdate id="reportEndDate"  width="120px;"  name="reportEndDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${Date}"  title="统计日期"/>
									</td>
									  <td width="50%">
								   		发送时间: <strong:newdate id="reportDate"  width="120px;"  name="reportDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportDate}"  title="统计日期"/> --
												<strong:newdate id="reportEndDate"  width="120px;"  name="reportEndDate"
												dateform="yyyy-MM-dd" isicon="true" dateobj="${reportEndDate}"  title="统计日期"/>
								   </td>
								<tr>
							 	  </td>
							 	  </tr>
							 	  <tr></tr>
							 	  <tr  width="20%">
							 	   <td  align="right">
									<input id="column" name="column" type="button"
											class="input_bg" value="查询" onclick="query()" style="width:60px">
									<input id="column" name="column" type="button"
											class="input_bg" value="取消" onclick="query()" style="width:60px">
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
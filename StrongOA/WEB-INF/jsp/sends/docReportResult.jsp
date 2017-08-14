<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
			<style type="text/css">
			#contentborder {
				BORDER-RIGHT: #DBDBDB  1px solid;
				PADDING-RIGHT: 1px;
				PADDING-LEFT: 1px;
				BACKGROUND: white;
				PADDING-BOTTOM: 10px;
				OVERFLOW: auto;
				BORDER-LEFT: #DBDBDB 1px solid;
				WIDTH: 100%;
				BORDER-BOTTOM: #DBDBDB 1px solid;
				POSITION: absolute;
				HEIGHT: 100%;
				margin-left: 1px;
			}
			</style>
	</HEAD>
	<script language="javascript">
	   window.name="targetWindow";
	  
	function onsub(date,temp){
		document.getElementById("reportTime").value=date;
		document.getElementById("unitORall").value=temp;
		document.getElementById("myTableForm").submit();
	}
	</script>
	<body class="contentbodymargin">
	 <DIV id=contentborder align=center>
			<s:form id="myTableForm" action="/sends/docSend!docSearchReport.action">
			<input type="hidden" id="reportTime" name="reportTime" />
			<input type="hidden" id="unitORall" name="unitORall"/>
			<table>
				<tr>
					<td>
						设置要统计的统计日期后，点击单位发文或所有发文按钮将展现统计结果
					</td>
				</tr>
			</table>
			</s:form>
		</div>
	</body>
</HTML>

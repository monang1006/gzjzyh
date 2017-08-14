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
	   function onsub(exportType,beginTime,endTime,orgId,reportType){
			document.getElementById("exportType").value=exportType;
			
			document.getElementById("orgId").value=orgId;
			document.getElementById("reportType").value=reportType;
			if(reportType=="date"){
			document.getElementById("begindate").value=beginTime;
			document.getElementById("enddate").value=endTime;
			document.getElementById("myTableForm").submit();
			}else{
			document.getElementById("begindate").value=beginTime;
			document.forms[0].action="<%=path%>/attendance/report/attendReport!getYearReport.action"
			document.getElementById("myTableForm").submit();
		}
	}
	</script>
	<body class="contentbodymargin">
	 <DIV id=contentborder align=center>
			<s:form id="myTableForm" action="/attendance/report/attendReport!dateReport.action">
			<input type="hidden" id="exportType" name=exportType />
			<input type="hidden" id="begindate" name=begindate />
			<input type="hidden" id="enddate" name=enddate />
			<input type="hidden" id="reportType" name=reportType />
			<input type="hidden" id="orgId" name=orgId />
			<input type="hidden" id="totalNum" name="totalNum" />
			<table>
				<tr>
					<td>
						<script type="text/javascript">
							var reportType="${param.reportType}";
							if(reportType=="date"){
								document.write("设置要统计的统计时间段后，点击统计按钮将展现统计结果");
							}else{
								document.write("设置要统计的统计年份，点击统计按钮将展现统计结果");
							}
						</script>
					</td>
				</tr>
			</table>
			</s:form>
		</div>
	</body>
</HTML>

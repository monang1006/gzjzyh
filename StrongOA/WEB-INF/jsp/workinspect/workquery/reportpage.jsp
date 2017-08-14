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
	function onsub(exportType,reportType,worktaskTitle,worktaskUnitName,worktaskNo,managetState,worktaskUser,worktaskEmerlevel,worktaskEtime,startTime,endTime){
		document.getElementById("exportType").value=exportType;
		document.getElementById("reportType").value=reportType;
		document.getElementById("worktaskTitle").value=worktaskTitle;
		document.getElementById("worktaskUnitName").value=worktaskUnitName;
		document.getElementById("worktaskNo").value=worktaskNo;
		document.getElementById("managetState").value=managetState;
		document.getElementById("worktaskUser").value=worktaskUser;
		document.getElementById("worktaskEmerlevel").value=worktaskEmerlevel;
		document.getElementById("worktaskEtime").value=worktaskEtime;
		document.getElementById("startTime").value=startTime;
		document.getElementById("endTime").value=endTime;
		document.getElementById("myTableForm").submit();
	}
	</script>
	<body class="contentbodymargin">
	 <DIV id=contentborder align=center>
			<s:form id="myTableForm" action="/workinspect/workquery/workQuery!getBorrowReport.action">
			<input type="hidden" id="exportType" name=exportType />
			<input type="hidden" id="reportType" name=reportType />
			<input type="hidden" id=worktaskTitle name=worktaskTitle />
			<input type="hidden" id="worktaskUnitName" name=worktaskUnitName />
			<input type="hidden" id="worktaskNo" name=worktaskNo />
			<input type="hidden" id="managetState" name=managetState />
			<input type="hidden" id="worktaskUser" name=worktaskUser />
			<input type="hidden" id="worktaskEmerlevel" name=worktaskEmerlevel />
			<input type="hidden" id="worktaskEtime" name=worktaskEtime />
			<input type="hidden" id="startTime" name=startTime />
			<input type="hidden" id="endTime" name=endTime />
			<table>
				<tr>
					<td align="center">
						输入您的查询条件后，点击查询按钮将展现统计结果
					</td>
				</tr>
			</table>
			</s:form>
		</div>
	</body>
</HTML>

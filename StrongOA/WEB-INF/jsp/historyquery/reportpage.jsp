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
	   function submitSearch(cdtion,infoSetValue,groupByFiled){
	     	$("#condition").val(encodeURI(cdtion));
	     	$("#infoSetValue").val(infoSetValue);
	     	$("#groupByFiled").val(groupByFiled);
	     	$("#myTableForm").submit();	
	   }

	</script>
	<body class="contentbodymargin">
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" theme="simple" method="get"
				target="targetWindow"
				action="/historyquery/query!generateReport.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<input type="hidden" id="condition" name="condition"
								value="${condition}" />
							<input type="hidden" id="groupByFiled" name="groupByFiled"
								value="${groupByFiled}" />
							<input type="hidden" id="module" name="module"
								value="recordQuery" />
							<input type="hidden" id="infoSetValue" name="infoSetValue" />
							<input type="hidden" id="currentPage" name="currentPage"
								value="0" />
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td>
							点击复合查询按钮，设置查询条件后将展现查询结果
						</td>
					</tr>
				</table>
			</s:form>
		</div>
	</body>
</HTML>

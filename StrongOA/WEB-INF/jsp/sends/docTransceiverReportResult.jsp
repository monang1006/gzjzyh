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
	  
	function onsub(BeginDate,EndDate,serach,temp){
		document.getElementById("startDate").value=BeginDate;
		document.getElementById("endDate").value=EndDate;
		document.getElementById("Identification").value=serach;
		document.getElementById("myTableForm").submit();
		//window.document.frames[1].location.reload();
		//window.parent.frames[1].location.reload();
	}
	$(document).ready(function(){
		//window.parent.location.reload();
	});
	</script>
	<body class="contentbodymargin">
	 <DIV id=contentborder align=center>
			<s:form id="myTableForm" action="/sends/docSend!reportchart.action">
			<input type="hidden" id="startDate" name=startDate />
			<input type="hidden" id="endDate" name=endDate />
			<input type="hidden" id="Identification" name=Identification />
				<table>
					<tr>
						<td>
							<iframe id="barFrame" name="barFrame"  src="<%=root%>/oa/image/docSend/DrawBar.jpg?<%=System.currentTimeMillis() %>" frameborder="0" scrolling="no" height="500px" width="950px" align="top" style="border-left:0px solid #CCCCCC;">				
							</iframe>
						</td>
					</tr>
				</table>
			</s:form>
		</div>
	</body>
</HTML>

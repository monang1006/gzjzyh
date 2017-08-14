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
	  
	function onsub(exportType,qszt,recvNum,workTitle,docNumber,departSigned,zbcs,csqs,recvstartTime,recvendTime,recvStartNum,recvEndNum){
	   // window.parent.frames[0].document.getElementById("reporttongji").value="1";
		document.getElementById("exportType").value=exportType;
		document.getElementById("qszt").value=qszt;
		document.getElementById("recvNum").value=encodeURI(recvNum);
		document.getElementById("workTitle").value=encodeURI(workTitle);
		document.getElementById("docNumber").value=encodeURI(docNumber);
		document.getElementById("departSigned").value=encodeURI(departSigned);
		document.getElementById("zbcs").value=encodeURI(zbcs);
		document.getElementById("csqs").value=encodeURI(csqs);
		document.getElementById("recvstartTime").value=recvstartTime;
		document.getElementById("recvendTime").value=recvendTime;
		document.getElementById("recvStartNum").value=recvStartNum;
		document.getElementById("recvEndNum").value=recvEndNum;
		document.getElementById("myTableForm").submit();
	}
	</script>
	<body class="contentbodymargin" onload="onsub('','','','','','','','','','','','');">
	 <DIV id=contentborder align=center>
			<s:form id="myTableForm" action="/historyquery/query!getDjcxReport.action">
				<input type="hidden" id="exportType" name=exportType />
				<input type="hidden" id="qszt" name=qszt />
				<input type="hidden" id="recvNum" name=recvNum />
				<input type="hidden" id="workTitle" name=workTitle />
				<input type="hidden" id="docNumber" name=docNumber />
				<input type="hidden" id="departSigned" name=departSigned />
				<input type="hidden" id="zbcs" name=zbcs />
				<input type="hidden" id="csqs" name=csqs />
				<input type="hidden" id="recvstartTime" name=recvstartTime />
				<input type="hidden" id="recvendTime" name=recvendTime />
				<input type="hidden" id="recvStartNum" name=recvStartNum />
				<input type="hidden" id="recvEndNum" name=recvEndNum />
				<table>
				
				</table>
			</s:form>
		</div>
	</body>
</HTML>

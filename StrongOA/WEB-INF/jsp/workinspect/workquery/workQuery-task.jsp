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
		<LINK type=text/css rel=stylesheet
			href="<%=path%>/common/css/gd.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT type="text/javascript">
		  	//统计方法
		  	$(document).ready(function(){ 
			  	$("#yj").change(function(){
		        	var value = $(this).val();
		        	if(value != "0"){
		        		$("#depart").val(value);
		        	}
	       		 });
       			}
   			 );
   		</SCRIPT>
	</HEAD>
	<body class="contentbodymargin">
		<div class="gd_name">
				<div class="gd_name_left">任务办理情况统计</div>
			  <br style="clear:both;"/>
			</div>
		<DIV id=contentborder align=center style="OVERFLOW: hidden;">
		<s:form id="myTableForm"
				action="/workinspect/workquery/workQuery!getTaskHadle.action"
					target="SearchContent1">
			<input type="hidden" id="reporttongji" name="reporttingji"/>
			<input type="hidden" id="exportType" name=exportType />
			<input type="hidden" id="reportType" name=reportType value="${reportType}"/>
			<input type="hidden" id="org" name=org />
			<input type="hidden" id="currentPage" name=currentPage />
			<table width="100%" height="15" class="gd_fslist" align="center">
							<tr>
								<td width="35%" height="43" align="right" >机构：</td>
								<td >
									<select id="yj" class="select_big">
									<option value="0">&lt;选择科室&gt;</option>
									<s:iterator value="orgList">
										<option value="${orgName }">${orgName }</option>
									</s:iterator>
									</select>
									<input type="hidden" id="depart" name="depart">
								</td>
								</tr>
			</table>
				</s:form>
			<div class="gd_search_big">
				<div class="gd_search_icon">
					<input id="column" name="column"  type="button" class="input_cx" value=" " onclick="onsub()"/>
					<%-- <input class="input_qx" name="" type="button" value=" " />--%>
					<input id="column2" name="column2" type="button" class="input_excle" value=""
													onclick="exportReport('excel')">
													
				      <input id="column3" name="column3" type="button"
													class="input_pdf" value="" onclick="exportReport('pdf')">
				</div>
			</div>
			<div class="gd_search_sm">
			  <br style="clear:both;"/>
			</div>
				<iframe id='SearchContent1' style="display: " name='SearchContent1'
				src='<%=root%>/fileNameRedirectAction.action?toPage=workinspect/workquery/reportpage2.jsp'
				frameborder=0  scrolling='auto' width='100%' height='60%'></iframe>
		</div>
		
		<script type="text/javascript">
			function onsub() {
	    $("#reporttongji").val("1");
		document.getElementById("exportType").value = "";
		document.getElementById("reportType").value = $("#reportType").val();
		document.getElementById("org").value = $("#depart").val();
		document.getElementById("currentPage").value = "0";
		document.getElementById("myTableForm").submit();
	}
	function goPages(page) {
		$('#currentPage').val(page);
		document.getElementById("myTableForm").submit();
	}
	//导出报表
	function exportReport(exportType){
	    if($("#reporttongji").val()!="1"){
	       alert("报表没有数据!");
	       return ;
	    }
		document.getElementById("exportType").value=exportType;
		document.getElementById("myTableForm").submit();
	}	
</script>
	</body>
</HTML>

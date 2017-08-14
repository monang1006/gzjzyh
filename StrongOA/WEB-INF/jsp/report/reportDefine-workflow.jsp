<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<HTML>
	<HEAD>
		<TITLE>查看报表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			//页面跳转
			function gotoPage(no){
				if(no!=null&&no!=""){
					$("#pageNo").val(no);
					//修改无法分页查询BUG 申仪玲 2012/1/4
					//$("#workflowCode").val(window.parent.frames[0].document.getElementById("workflowCode").value);
					//$("#workflowTitle").val(window.parent.frames[0].document.getElementById("workflowTitle").value);
					$("form").submit();
				}
			}
			
			function getPageNo(){
				return $("#pageNo").val();
			}
			
			function setCookie(c_name,value,expiredays){
				var exdate = new Date();
				exdate.setDate(exdate.getDate() + expiredays);
				document.cookie = c_name + "=" + escape(value) + ((expiredays==null) ? "" : ";expires=" + exdate.toGMTString());
			}
			
			function doSetCookie() {
				var pageSize = $("#pageSize").val();
				var columnWidth = $("#columnWidth").val();
				if(isNaN(pageSize)){
					alert("每页显示记录数设置错误。");
					return ;
				}
				var re = /^[1-9]+[0-9]*]*$/;
				if (!re.test(pageSize)){
					alert("每页显示记录数必须为正整数。"); 
					return;
				} 
				if(isNaN(columnWidth)){
					alert("列宽设置错误。");
					return ;
				}
				//保留7天
				setCookie("jasperreport_pagesize",pageSize,7);
				setCookie("jasperreport_columnwidth",columnWidth,7);
				//alert("设置成功.");
				$("form").submit();
			}
		</script>
		<s:form action="/report/reportDefine!workflow.action">
			<s:hidden id="pageNo" name="pageReport.pageNo"></s:hidden>
			<s:hidden name="definitionId"></s:hidden>
			<s:hidden id="workflowCode" name="workflowCode"></s:hidden>
			<s:hidden id="workflowTitle" name="workflowTitle"></s:hidden>
			<s:hidden id="totalCount" name="pageReport.totalCount"></s:hidden>
			<s:if test="pageReport.pageNo>1">
					<a href='javascript:gotoPage(1)'><img src='<%=path %>/oa/image/query/first.GIF' border='0'></a>
					<a href='javascript:gotoPage(${pageReport.pageNo-1})'><img src='<%=path %>/oa/image/query/previous.GIF' border='0'></a>
			</s:if>
			<s:else>
				<img src='<%=path %>/oa/image/query/first_grey.GIF' border='0'>
				<img src='<%=path %>/oa/image/query/previous_grey.GIF' border='0'>
			</s:else>
			<s:if test="pageReport.pageNo<=pageReport.totalPages-1">
					<a href='javascript:gotoPage(${pageReport.pageNo+1})'><img src='<%=path %>/oa/image/query/next.GIF' border='0'></a>
					<a href='javascript:gotoPage(${pageReport.totalPages })'><img src='<%=path %>/oa/image/query/last.GIF' border='0'></a>
			</s:if>
			<s:else>
				<img src='<%=path %>/oa/image/query/next_grey.GIF' border='0'>
				<img src='<%=path %>/oa/image/query/last_grey.GIF' border='0'>
			</s:else>
			当前&nbsp;
			${pageReport.pageNo }/<s:if test="pageReport.totalPages==0">1</s:if><s:else>${pageReport.totalPages }</s:else>
			&nbsp;每页&nbsp;<s:textfield id="pageSize" name="pageReport.pageSize" size="2"></s:textfield>条
			&nbsp;每列宽度&nbsp;<s:textfield id="columnWidth" name="columnWidth" size="2" />
			&nbsp;<input type="button" class="input_bg" onclick="doSetCookie();" value="设 置"/>
			&nbsp;共<b><s:property value="pageReport.totalCount"/></b>条记录
		</s:form>
		${html }

<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%
	String cnNameWord="";
	cnNameWord=(String)request.getAttribute("wapUserName");
	if(cnNameWord!=null&&!"".equals(cnNameWord)){
		cnNameWord=java.net.URLEncoder.encode(cnNameWord,"utf-8"); 
		cnNameWord=java.net.URLEncoder.encode(cnNameWord,"utf-8"); 
	}
	if(cnNameWord==null){
		cnNameWord="";
	}
	String cnSearchTitle="";
	cnSearchTitle=(String)request.getAttribute("businessTitle");
	if(cnSearchTitle!=null&&!"".equals(cnSearchTitle)){
		cnSearchTitle=java.net.URLEncoder.encode(cnSearchTitle,"utf-8"); 
		cnSearchTitle=java.net.URLEncoder.encode(cnSearchTitle,"utf-8"); 
	}
	if(cnSearchTitle==null){
		cnSearchTitle="";
	}
%>
<div class="eblue">
第<c:out value="${pageWorkflow.pageNo}"/>/<c:out value="${pageWorkflow.totalPages}"/>页&nbsp;共<c:out value="${pageWorkflow.totalCount}"/>条&nbsp;
<c:if test="${pageWorkflow.pageNo>1}">
	<A href="<%=root%>/wap/work.action?listMode=10&businessName=${businessName}&businessTitle=<%=cnSearchTitle %>&userName=${userName}&worktype=${worktype}&currentPage=1&wapUserName=<%=cnNameWord %>">首页</A>&nbsp;
</c:if>
<c:if test="${pageWorkflow.pageNo>1}">
	<A href="<%=root%>/wap/work.action?listMode=10&businessName=${businessName}&businessTitle=<%=cnSearchTitle %>&userName=${userName}&worktype=${worktype}&currentPage=<c:out value='${pageWorkflow.prePage}'/>&wapUserName=<%=cnNameWord %>">上一页</A>&nbsp;
</c:if>
<c:if test="${pageWorkflow.pageNo<pageWorkflow.totalPages}">
	<A href="<%=root%>/wap/work.action?listMode=10&businessName=${businessName}&businessTitle=<%=cnSearchTitle %>&userName=${userName}&worktype=${worktype}&currentPage=<c:out value='${pageWorkflow.nextPage}'/>&wapUserName=<%=cnNameWord %>">下一页</A>&nbsp;
</c:if>
<c:if test="${pageWorkflow.pageNo<pageWorkflow.totalPages}">
	<A href="<%=root%>/wap/work.action?listMode=10&businessName=${businessName}&businessTitle=<%=cnSearchTitle %>&userName=${userName}&worktype=${worktype}&currentPage=<c:out value='${pageWorkflow.totalPages}'/>&wapUserName=<%=cnNameWord %>">尾页</A>&nbsp;
</c:if></div>
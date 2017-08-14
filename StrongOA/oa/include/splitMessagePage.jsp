<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<div class="eblue">
<%
	String cnSearchTitle="";
	cnSearchTitle=(String)request.getAttribute("searchTitle");
	if(cnSearchTitle!=null&&!"".equals(cnSearchTitle)){
		cnSearchTitle=java.net.URLEncoder.encode(cnSearchTitle,"utf-8"); 
		cnSearchTitle=java.net.URLEncoder.encode(cnSearchTitle,"utf-8"); 
	}
	if(cnSearchTitle==null){
		cnSearchTitle="";
	}
%>
第<c:out value="${androidpage.pageNo}"/>/<c:out value="${androidpage.totalPages}"/>页&nbsp;共<c:out value="${androidpage.totalCount}"/>条&nbsp;
<c:if test="${androidpage.pageNo>1}">
	<A href="<%=root%>/wap/message!waprecvlist.action?listMode=10&searchTitle=<%=cnSearchTitle %>&userName=${userName}&currentPage=1">首页</A>&nbsp;
</c:if>
<c:if test="${androidpage.pageNo>1}">
	<A href="<%=root%>/wap/message!waprecvlist.action?listMode=10&searchTitle=<%=cnSearchTitle %>&userName=${userName}&currentPage=<c:out value='${androidpage.prePage}'/>">上一页</A>&nbsp;
</c:if>

<c:if test="${androidpage.pageNo<androidpage.totalPages}">
	<A href="<%=root%>/wap/message!waprecvlist.action?listMode=10&searchTitle=<%=cnSearchTitle %>&userName=${userName}&currentPage=<c:out value='${androidpage.nextPage}'/>">下一页</A>&nbsp;
</c:if>
<c:if test="${androidpage.pageNo<androidpage.totalPages}">
	<A href="<%=root%>/wap/message!waprecvlist.action?listMode=10&searchTitle=<%=cnSearchTitle %>&userName=${userName}&currentPage=<c:out value='${androidpage.totalPages}'/>">尾页</A>&nbsp;
</c:if>
</div>
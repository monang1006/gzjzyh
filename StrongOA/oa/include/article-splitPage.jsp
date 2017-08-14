<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<div class="eblue">
第<c:out value="${articlpage.pageNo}"/>/<c:out value="${articlpage.totalPages}"/>页&nbsp;共<c:out value="${articlpage.totalCount}"/>条&nbsp;
<c:if test="${articlpage.pageNo>1}">
	<A href="<%=root%>/wap/articles!getColumnArticleList.action?columnId=${columnId}&currentPage=1">首页</A>&nbsp;
</c:if>
<c:if test="${articlpage.pageNo>1}">
	<A href="<%=root%>/wap/articles!getColumnArticleList.action?columnId=${columnId}&currentPage=<c:out value='${articlpage.prePage}'/>">上一页</A>&nbsp;
</c:if>
<c:if test="${articlpage.pageNo<articlpage.totalPages}">
	<A href="<%=root%>/wap/articles!getColumnArticleList.action?columnId=${columnId}&currentPage=<c:out value='${articlpage.nextPage}'/>">下一页</A>&nbsp;
</c:if>
<c:if test="${articlpage.pageNo<articlpage.totalPages}">
	<A href="<%=root%>/wap/articles!getColumnArticleList.action?columnId=${columnId}&currentPage=<c:out value='${articlpage.totalPages}'/>">尾页</A>&nbsp;
</c:if>
</div>

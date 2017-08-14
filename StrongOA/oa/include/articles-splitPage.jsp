<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<c:if test="${psize>1}">
&nbsp;&nbsp;&nbsp;&nbsp;第${pagenow }/${psize }页
<a href="<%=root%>/wap/articles!showColumnArticl.action?pagenow=1&columnId=${columnId}&columnArticleId=${columnArticleId}&currentPage=${currentPage}">首页</a>&nbsp;&nbsp;
<c:if test="${pagenow>1}">
<a href="<%=root%>/wap/articles!showColumnArticl.action?pagenow=${pagenow-1}&columnId=${columnId}&columnArticleId=${columnArticleId}&currentPage=${currentPage}">上一页</a>
&nbsp;&nbsp;
</c:if>
<c:if test="${pagenow<psize}">
	<a href="<%=root%>/wap/articles!showColumnArticl.action?pagenow=${pagenow+1}&columnId=${columnId}&columnArticleId=${columnArticleId}&currentPage=${currentPage}">下一页</a>
	&nbsp;&nbsp;
</c:if>
<a href="<%=root%>/wap/articles!showColumnArticl.action?pagenow=${psize}&columnId=${columnId}&columnArticleId=${columnArticleId}&currentPage=${currentPage}">尾页</a>
</c:if>

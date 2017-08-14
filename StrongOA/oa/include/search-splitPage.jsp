<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
第<c:out value="${page.pageNo}"/>/<c:out value="${page.totalPages}"/>页&nbsp;共<c:out value="${page.totalCount}"/>条&nbsp;
<c:if test="${page.pageNo>1}">
	<A href="<%=root%>/wap/addressOrg!toChooseUsers.action?forward=${forward}&chooseType=${chooseType}&groupId=${groupId}&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&currentUserId=${currentUserId}&searchOrgId=${searchOrgId}&searchUserName=${searchUserName}&searchCurrentPage=1">首页</A>&nbsp;
</c:if>
<c:if test="${page.pageNo>1}">
	<A href="<%=root%>/wap/addressOrg!toChooseUsers.action?forward=${forward}&chooseType=${chooseType}&groupId=${groupId}&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&currentUserId=${currentUserId}&searchOrgId=${searchOrgId}&searchUserName=${searchUserName}&searchCurrentPage=<c:out value='${page.prePage}'/>">上一页</A>&nbsp;
</c:if>
<c:if test="${page.pageNo<page.totalPages}">
	<A href="<%=root%>/wap/addressOrg!toChooseUsers.action?forward=${forward}&chooseType=${chooseType}&groupId=${groupId}&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&currentUserId=${currentUserId}&searchOrgId=${searchOrgId}&searchUserName=${searchUserName}&searchCurrentPage=<c:out value='${page.nextPage}'/>">下一页</A>&nbsp;
</c:if>
<c:if test="${page.pageNo<page.totalPages}">
	<A href="<%=root%>/wap/addressOrg!toChooseUsers.action?forward=${forward}&chooseType=${chooseType}&groupId=${groupId}&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&currentUserId=${currentUserId}&searchOrgId=${searchOrgId}&searchUserName=${searchUserName}&searchCurrentPage=<c:out value='${page.totalPages}'/>">尾页</A>&nbsp;
</c:if>

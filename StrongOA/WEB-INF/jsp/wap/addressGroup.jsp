<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html>
	<head>
		<title>系统通讯录</title>
		<link rel="stylesheet" type="text/css"
			href="<%=root%>/oa/css/wap/style.css"></link>
	</head>
	<body>
		<jsp:include flush="true" page="/oa/include/navigation.jsp"></jsp:include>
		<font color="red"><c:out value="${message}"/></font>
		<div class=list>
			<div class="sec">
				<security:authorize ifAnyGranted="001-000100040001">
					<font color="gray">个人通讯录</a></font>&nbsp;
				</security:authorize>
				<security:authorize ifAnyGranted="001-000100040002">
					<a href="<%=root %>/wap/addressOrg!getOrguserlist.action">系统通讯录</a>
				</security:authorize>
			</div>
			<div class="sec">
				<a class="button1" href="<%=root%>/fileNameRedirectAction.action?toPage=wap/addGroup.jsp?groupId=${groupId}">新建联系组</a>
				<s:if test="groupId!=null&&groupId!=\"\""><a class="button1" href="<%=root%>/wap/addressOrg!toChooseUsers.action?chooseType=import&groupId=${groupId}">导入系统人员</a></s:if>
			</div>
			<form action="<%=root%>/wap/addressGroup!getAddressList.action" method="post">
				<div class=sec>
					<table style="width:100%">
						<tr>
							<td>
								<s:select list="#request.list" id="groupId" name="groupId"
									listKey="addrGroupId" listValue="addrGroupName" headerKey=""
									headerValue="请选择联系组" cssStyle="width:60%"></s:select>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" id="name" name="name" value="${name}" title="输入姓名" style="width:58%"/>
								<input type="submit" value="查询" class="button"/>
							</td>
						</tr>
					</table>
				</div>
			</form>
			<c:if test="${page.result!=null}">
				<span class="gray"> <jsp:include flush="true"
						page="/oa/include/person-splitPage.jsp"></jsp:include> </span>
			</c:if>
		</div>
		<c:forEach items="${page.result}" var="dataRow" varStatus="status">
			<div class="sec">
				<c:out value="${status.index+1}"/>.<strong><c:out value="${dataRow.groupName}" /></strong>
				&nbsp;<c:out value="${dataRow.name}" />:<c:choose><c:when test="${dataRow.sex==\"1\"}">女</c:when><c:otherwise>男</c:otherwise></c:choose><br/>
				单位:<c:out value="${dataRow.department}" /><br/>
				电话:<c:out value="${dataRow.mobile1}" /><br/>
				手机:<c:out value="${dataRow.tel1}" /><br/>
				邮箱:<c:out value="${dataRow.defaultEmail}" /><br/>
			</div>
		</c:forEach>
		<jsp:include flush="true" page="/oa/include/bottom.jsp"></jsp:include>
	</body>
</html>

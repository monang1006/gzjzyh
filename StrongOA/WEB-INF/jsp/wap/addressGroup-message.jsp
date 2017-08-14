<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html>
	<head>
		<title>导入系统人员</title>
		<link rel="stylesheet" type="text/css"
			href="<%=root%>/oa/css/wap/style.css"></link>
	</head>
	<body>
		<jsp:include flush="true" page="/oa/include/navigation.jsp"></jsp:include>
		<div class="gridlist">
			<div class="sec">
				<table style="width:100%">
					<tr>
						<td>
							<form action="<%=root%>/wap/addressOrg!toChooseUsers.action" method="post">
								<input type="hidden" id="chooseType" name="chooseType" value="${chooseType}"/>
								<c:out value="${message}" />
								<input type="hidden" id="groupId" name="groupId" value="${groupId}"/>
								<input type="hidden" id="searchOrgId" name="searchOrgId" value="${searchOrgId}"/>
								<input type="hidden" id="searchUserName" name="searchUserName" value="${searchUserName}"/>
								<input type="hidden" id="searchCurrentPage" name="searchCurrentPage" value="${searchCurrentPage}"/>
								<input type="submit" value="返回" class="button"/>
							</form>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<jsp:include flush="true" page="/oa/include/person-bottom.jsp"></jsp:include>
	</body>
</html>

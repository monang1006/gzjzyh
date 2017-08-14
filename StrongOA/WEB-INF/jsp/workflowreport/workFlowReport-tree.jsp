<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程展示树</title>
		<link href="<%=frameroot%>/css/dtree1.css" type="text/css"
			rel="stylesheet">
		<script language="javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js" type="text/javascript"></script>
	</head>
	<script language="javascript" type="text/javascript">
	    function getOrgUser(orgId) {
	       parent.project_work_content.location = scriptroot+ "/workflowreport/workFlowReport!reportpage.action?processTypeId="+orgId;
	    }
	</script>
	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" width="10%" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				height="2">
				<tr height="2">
					<td height="2"></td>
				</tr>
			</table>
			<table width="95%" border="0" cellpadding="0" cellspacing="0"
				class="table_lb">
				<tr class="tableTRB">
					<td class="table_bk">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
                                var imageRootPath='<%=root%>/workflow';
                                var valueArray = new Array();
                                d = new dTree('d');
                                d.add('c0','-1','流程类型','','','','','2');
							    <c:forEach items="${getAllProcessTypeList}" var="instance" varStatus="status">
							        d.add('g<c:out value="${instance[0]}"/>','c0','<c:out value="${instance[1]}"/>','javascript:getOrgUser(<c:out value="${instance[0]}"/>);','','', '<%=root%>/workflow/images/tree/folder_closed.gif','2');
							    </c:forEach>
	                            document.write(d);
                            </script>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
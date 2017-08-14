<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="java.net.URLDecoder"/>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>预览流程图</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css" rel="stylesheet">
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<div id="contentborder" align="center">
			<div id="info" name="info" class="UserCard" style="display: none">
				<table width="100%" height="100%">
					<tr>
						<td height="10"></td>
						<td width="5"></td>
					</tr>
					<tr>
						<td align="right" height="160">
							<iframe name="nodeinfo" id="nodeinfo" width="83%" height="100%" border="0"></iframe>
						</td>
						<td></td>
					</tr>
					<tr>
						<td align="right" height="10">
							<a href="#" onclick="changeDisplay()">关闭</a>
						</td>
						<td></td>
					</tr>
				</table>
			</div>

			<table width="98%">
				<tr>
					<td height="5"></td>
				</tr>
			</table>
			     <table width="98%">
        <tr>
          <td align="center">
            <%
            	String workflowId = request.getParameter("workflowId");
            	String workflowName = request.getParameter("workflowName");
            	Long id = 0L;
            	if(workflowId != null){
            		id = Long.parseLong(workflowId);
            	}
            	if(workflowName != null && !"".equals(workflowName)){
            		workflowName = URLDecoder.decode(workflowName,"utf-8");
            	}
            %>
            <strongit:processimage workflowid="<%=id%>" processName="<%=workflowName %>"/>  
          </td>
        </tr>
      </table>
		</div>
	</body>
</html>

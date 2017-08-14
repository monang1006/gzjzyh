<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>预览流程图</title>
    <link href="<%=frameroot%>/css/windows.css" type="text/css"
      rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
  </head>
  <body class="contentbodymargin">
    <div id="contentborder" align="center">
      <div id="info" name="info" class="UserCard" style="display: none">
        <table width="100%" height="100%">
          <tr>
            <td height="10"></td>
            <td width="5"></td>
          </tr>
          <tr>
            <td align="right" height="160">
              <iframe name="nodeinfo" id="nodeinfo" width="83%" height="100%"
                border="0"></iframe>
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
            %>
            <strongit:processimage workflowid="<%=Long.parseLong(workflowId)%>"/>  
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>

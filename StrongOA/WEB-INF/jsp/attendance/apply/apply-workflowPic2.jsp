<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>查看流程图</title>
    <link href="<%=frameroot%>/css/windows.css" type="text/css"
      rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script> 
  </head>
  <body class="contentbodymargin">
    <div id="contentborder" align="center">
      <table width="98%">
        <tr>
          <td height="5"></td>
        </tr>
      </table>
           <table width="98%">
        <tr>
          <td align="center">
            <%
            String tokenInstanceId = (String) request.getAttribute("token");
            System.out.println(tokenInstanceId);
            %>
            <strongit:processImagePopToken token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" />
          </td>
        </tr>
      </table>
      <input type="hidden" name="processId"
              value="${model[0].processInstanceId }">
    </div>
  </body>
</html>
<%@ page contentType="text/html; charset=utf-8" language="java" import="java.util.*" errorPage="" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文件查询</title>
</head>
<frameset cols="180,*" style="border-spacing:1; border:1; border-color:#d4d0c7; ">
  <frame name="privil_tree" marginwidth="0" marginheight="0" src="<%=path %>/fileNameRedirectAction.action?toPage=theme/theme-wenJianChaXunTree.jsp?mytype=<%=request.getParameter("mytype")%>" frameborder="0" scrolling="no" />
  <frame name="privil_content" marginwidth="0" marginheight="0" src="<%=path %>/fileNameRedirectAction.action?toPage=theme/theme-bgtMenuContent.jsp" frameborder="0" scrolling="no" />
<noframes>
<body>
对不起，您的浏览器不支持框架，请升级到IE8或以上版本。
</body>
</noframes>
</frameset>
</html>
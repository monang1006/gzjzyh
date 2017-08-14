<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head><title>查看投票记录</title></head>
<body>
 <iframe frameborder="0" border="0" scrolling="auto" width="100%" height="100%" src="<%=path%>/vote/voteLog!list.action?vid=${param.vid}"></iframe>
</body>
</html>
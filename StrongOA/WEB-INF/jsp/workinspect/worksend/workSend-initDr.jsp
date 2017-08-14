<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	
		<title>刷新</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
	
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" >
	<DIV id="msgdiv" style="display: none;">
		</DIV>
		<script type="text/javascript">
			location = '<%=path%>/workinspect/worksend/workSend!getPersonal.action';
		</script>
	</body>
</html>

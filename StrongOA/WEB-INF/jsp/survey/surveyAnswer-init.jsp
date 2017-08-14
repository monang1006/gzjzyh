<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	
		<title>投票</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
	
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" >
	<DIV id="msgdiv" style="display: none;">
			<s:actionmessage />
		</DIV>
		<script type="text/javascript">
			
			//alert("document.getElementById(\"msgdiv\"):"+document.getElementById("msgdiv").children[0].children[0].children[0].innerHTML);
			window.dialogArguments.location = '<%=path%>/survey/survey!listSucc.action';
			window.close();
		</script>
	</body>
</html>

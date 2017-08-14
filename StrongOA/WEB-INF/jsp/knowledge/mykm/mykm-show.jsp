<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>知识收藏查看</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
	</head>
	<body >
	<div>
	<iframe id="mykm" src='${model.mykmUrl}' frameborder="0" scrolling="auto" height="100%" width="100%" align="top" style="overflow: visible;border:0px solid #CCCCCC;">				
	</div>
	</iframe>
	</body>
	<SCRIPT>
		var mykmUrl = "${model.mykmUrl}";
		if(mykmUrl!=""){
			if (mykmUrl.substring(0, 4)=="http"||mykmUrl.substring(0, 3)=="www") {// 判断是否是外部链接
					$("#mykm").attr("src",mykmUrl);
			}else{
					$("#mykm").attr("src","<%=path%>/"+mykmUrl);
			}
		}
	</SCRIPT>
</html>

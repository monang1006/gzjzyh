<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
	</HEAD>
	<body class="contentbodymargin">
		<DIV id=contentborder align=center style="OVERFLOW: hidden;">
			<iframe id='SearchContent' style="display:" name='SearchContent'
				src='<%=root%>/fileNameRedirectAction.action?toPage=senddocRegist/poeratepage.jsp'
				frameborder=0 scrolling=auto width='100%' height='7%'></iframe>
			<hr>
			<iframe id='SearchContent1' style="display:" name='SearchContent1'
				src='<%=root%>/fileNameRedirectAction.action?toPage=senddocRegist/reportpage.jsp'
				frameborder=0 scrolling='auto' width='100%' height='89%'></iframe>
			<hr>	
		</div>
	</body>
</HTML>

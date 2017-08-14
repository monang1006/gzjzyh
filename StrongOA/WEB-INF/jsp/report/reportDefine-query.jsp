<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
	</HEAD>
	<body class="contentbodymargin">
		<DIV id=contentborder align=center style="OVERFLOW: hidden;">
			<iframe id='SearchContent1' style="display:" name='SearchContent1'
				src='<%=path %>/report/reportDefine!queryPage.action?definitionId=<%=request.getParameter("formId") %>'
				frameborder=0 scrolling=auto width='100%' height='10%'></iframe>
			<hr>
			<iframe id='SearchContent2' style="display:" name='SearchContent2'
				src='<%=path %>/report/reportDefine!workflow.action?definitionId=<%=request.getParameter("formId") %>'
				frameborder=0 scrolling=auto width='100%' height='90%'></iframe>
			<hr>	
		</div>
	</body>
</HTML>

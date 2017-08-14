<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<TITLE>套红页面内容</TITLE>
	</HEAD>
	<FRAMESET cols="20%,80% frameborder="no"  order="0" framespacing="0">
		
		<FRAME name="project_work_tree" marginWidth=0 marginHeight=0
			src="<%=root%>/docredtemplate/docredtype/docRedType!tree.action" frameBorder=0
			scrolling=no>

		<FRAME name="project_work_content" marginWidth=0 marginHeight=0
			src="<%=root%>/docredtemplate/docreditem/docRedItem!init.action"
			frameBorder=1 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>


<!--二级菜单主页面(树型结构区域)-->
<!--newIndex_actions_container.ftl-->
<!--首页/主框架工作区/树型结构区域-->
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<HEAD>
		<TITLE>操作容器</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<META content="MSHTML 6.00.2900.3354" name=GENERATOR>
	</HEAD>
	<FRAMESET border=0 frameSpacing=0 rows=100%,* frameBorder=0>
	    <!--树型菜单工具栏-->
		<FRAME name=personal_properties_toolbar marginWidth=0 marginHeight=0 
			src="${S1}"
		 	frameBorder=0 noResize scrolling=no>
		 <!--树型菜单-->
		<FRAME name=personal_properties_content marginWidth=0 marginHeight=0
			src="${S2}" frameBorder=0
			noResize scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

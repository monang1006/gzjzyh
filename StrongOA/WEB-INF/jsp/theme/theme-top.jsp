<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>
		<s:if test="modle.baseWindowsTitle!=null &&modle.baseWindowsTitle.length()>0">
			${modle.baseWindowsTitle}
		</s:if>
		<s:else>
		协同办公软件
		</s:else>
		</TITLE>
		<%@include file="/common/include/meta.jsp"%>		
	</HEAD>
	<s:form action="" id="mytable" theme="simple">
		<s:hidden id="baseWindowsTitle" name="modle.baseWindowsTitle"></s:hidden>
		<FRAMESET border=0 frameSpacing=0 name="topTitle" rows=125,* frameBorder=0 id="theTop">
			<FRAME title=页面顶部及通用工具栏 name=perspective_top marginWidth=0 marginHeight=0 src="<%=path%>//theme/theme!RefreshToolbar.action?pageModelId=402882192af071c8012af0c1f9d50008" frameBorder=0 noResize scrolling=no>
			<FRAMESET border=0 cols=30,*>
				<FRAME title=一级菜单试图切换区 name=perspective_toolbar marginWidth=5 marginHeight=0 src="<%=path%>//theme/theme!RefreshLeftside.action?pageModelId=402882192af071c8012af0c7d8b3000a" frameBorder=0 noResize scrolling=no>
				<FRAME title="主框架内容区（分为菜单区及内容操作区）" name=perspective_content marginWidth=0 marginHeight=0 src="<%=path%>//fileNameRedirectAction.action?toPage=frame/perspective_content.jsp" frameBorder=0 scrolling=no>
			</FRAMESET>
		</FRAMESET>
		<noframes></noframes>
	</s:form>
</HTML>

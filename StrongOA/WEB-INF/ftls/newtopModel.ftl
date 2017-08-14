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
		思创数码科技股份有限公司协同办公软件
		</s:else>
		</TITLE>
		<%@include file="/common/include/meta.jsp"%>		
	</HEAD>

	<s:form action="" id="mytable" theme="simple">
		<s:hidden id="baseWindowsTitle" name="modle.baseWindowsTitle"></s:hidden>
		<FRAMESET border="0" frameSpacing="0" rows="87,*" frameBorder="0" id="theTop">
		<FRAME title="页面顶部及通用工具栏" name="perspective_top" marginWidth="0" marginHeight="0" src="${F1}" frameBorder="0" noResize scrolling="no"/>
			
	<FRAME title="一级菜单试图切换区" name="perspective_content" marginWidth="5" marginHeight="0" src="<%=path%>/theme/theme!getFirstLevelPrivil.action" frameBorder="0" noResize scrolling="no"/>
				
		</FRAMESET>
		<noframes></noframes>
	</s:form>
</HTML>

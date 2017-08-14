<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<TITLE>页面模板管理</TITLE>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
		</script>
	</HEAD>
	<FRAMESET cols="25%,80%" frameborder="no" border="0" framespacing="0">
		<FRAME name="project_work_tree" marginWidth=0 marginHeight=0 src="<%=root%>/viewmodel/viewModelPage!tree.action?modelId=${modelId }" frameBorder=0 scrolling=no>
		<FRAME name="project_work_content" marginWidth=0 marginHeight=0 src="<%=root %>/fileNameRedirectAction.action?toPage=viewmodel/viewModelPage-context.jsp" frameBorder=1 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
</HTML>

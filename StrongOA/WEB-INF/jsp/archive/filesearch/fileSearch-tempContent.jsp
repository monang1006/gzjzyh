<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<TITLE>档案管理</TITLE>

	</HEAD>
	<FRAMESET cols="20%,80%" frameborder="no" border="0" framespacing="0">
			<FRAME name="project_work_tree" id="project_work_tree" marginWidth=0 marginHeight=0
				src=""
				frameBorder=0 scrolling=no>

		<FRAME name="project_work_content" id="project_work_content" marginWidth=0 marginHeight=0
			src=""
			frameBorder=1 scrolling=no>
	</FRAMESET>
	<noframes></noframes>
			<SCRIPT type="text/javascript">
				function urlcodetree(){
				var url="<%=root%>/archive/filesearch/fileSearch!temptree.action?"+
					      "treeValue="+"${treeValue }"+
					      "&groupType="+"${groupType }"+
					      "&fileNo="+"${fileNo }"+
					      "&fileTitle="+"${fileTitle }"+
					      "&fileAuthor="+"${fileAuthor }"+
					      "&fileFolder="+"${fileFolder }"+
					      "&filepage="+"${filepage }"+
					      "&fileType="+"${fileType }"+
					      "&orgId=${orgId }&disLogo=tempfile&fileDate=${fileDate }";
					      document.getElementById("project_work_tree").src=encodeURI(url);
				}
				function urlcodecontent(){
						var url="<%=root%>/archive/filesearch/fileSearch!filelist.action?"+
					      "treeValue="+"${treeValue }"+
					      "&groupType="+"${groupType }"+
					      "&fileNo="+"${fileNo }"+
					      "&fileTitle="+"${fileTitle }"+
					      "&fileAuthor="+"${fileAuthor }"+
					      "&fileFolder="+"${fileFolder }"+
					      "&filepage="+"${filepage }"+
					      "&fileType="+"${fileType }"+
					      "&orgId=${orgId }&disLogo=tempfile&fileDate=${fileDate }";
					       document.getElementById("project_work_content").src=encodeURI(url);
				}
				urlcodetree();
				urlcodecontent();
				
		</SCRIPT>
</HTML>
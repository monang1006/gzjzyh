<!--左则树型菜单-->
<!--newIndex_navigator_menu.ftl-->
<!--首页/主框架工作区/树型结构区域/菜单区域-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->

<HTML>
	<HEAD>
		<TITLE>导航器内容</TITLE>
		<script type="text/javascript">
			var imageRootPath='<%=frameroot%>';
		</script>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check_new.js"></SCRIPT>
		
	<SCRIPT type="text/javascript" language="java">
		var fontsize="${r"${systemset.manaMenuFontSize}"}";
		if(fontsize!=null&&fontsize!=""){
			fontsize=fontsize+"px";
		}else{
			fontsize="12px";
		}
		function navigates(url,title){
		        window.parent.parent.actions_container.navigator_toolbar.navigate("<%=path%>"+url,title);
			//window.parent.parent.actions_container.personal_properties_toolbar.navigate("<%=path%>"+url,title);
		}
		
		function getPageModelId(){
			return document.getElementById("pageModelId").value;
		}
		
		function showMenu(){
			if(window.top.perspective_toolbar!=undefined){
				window.top.perspective_toolbar.showMenu("nav_${r"${priviparent}"}");
			}
		}
	</SCRIPT>
	<STYLE type="text/css">
		a:link,a:visited,a:hover,a:active{
			font-family: 	"宋体";
			font-size: 		expression(fontsize);
			color: 			#000000;
			text-decoration:none;
		}	
	</STYLE>
	</HEAD>
	<BODY class=contentbodymargin onload="setTimeout('showMenu()',1)">
			<s:hidden id="pageModelId" name="pageModelId"></s:hidden>
		<DIV id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="95%"
				height="100%">
				<tr>
					<td valign=top>
						<tree:strongtree title="${r"${priviparentname}"}" check="false" dealclass="com.strongit.oa.theme.DealTreeNode" data="${r"${privilList}"}" target="project_work_content" />
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

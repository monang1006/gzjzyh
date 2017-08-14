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
		<SCRIPT src="<%=jsroot%>/mztree_check/mztreeview_check.js"></SCRIPT>
		
	<SCRIPT type="text/javascript" language="java">
		var fontsize="${systemset.manaMenuFontSize}";
		if(fontsize!=null&&fontsize!=""){
			fontsize=fontsize+"px";
		}else{
			fontsize="12px";
		}
		function navigates(url,title){
			//alert("2222");
			window.parent.parent.actions_container.personal_properties_toolbar.navigate("<%=path%>"+url,title);
		}
		//弹出自办文流程表单    BY：刘皙
		function gotozbw(){
			var rValue= window.open("<%=basePath%>/senddoc/sendDoc!input.action?workflowName=%25E8%2587%25AA%25E5%258A%259E%25E6%2596%2587%25E5%258A%259E%25E7%2590%2586\&formId=9050\&workflowType=3");
		}
		//弹出文件查询页面    BY：刘皙
		function gotoOrgwjcx(){
			var rValue= window.open("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-wenJianChaXun.jsp?mytype=org");
		}
		//弹出文件查询页面    BY：刘皙
		function gotoDeptwjcx(){
			var rValue= window.open("<%=basePath%>/fileNameRedirectAction.action?toPage=theme/theme-wenJianChaXun.jsp?mytype=dept");
		}
		
		function getPageModelId(){
			return document.getElementById("pageModelId").value;
		}
		
		function showMenu(){
			try{
			//alert("1111");
			if(window.top.perspective_toolbar!=undefined){
				window.top.perspective_toolbar.showMenu("nav_${priviparent}");
			}
			}catch(e){
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
						<tree:strongtree title="${priviparentname}" check="false" dealclass="com.strongit.oa.theme.DealTreeNode" data="${privilList}" target="project_work_content" />
					</td>
				</tr>
			</table>
		</DIV>
	</BODY>
</HTML>

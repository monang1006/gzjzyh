<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<% 
	String depLogo=request.getParameter("depLogo");
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加字典</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/toolbar.css" type="text/css" rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=root%>/common/scripts/validator.js" language="javascript"></script>
		<script src="<%=root%>/common/scripts/handleEnter.js" language="javascript"></script>
		<script src="<%=root%>/common/scripts/common.js" language="javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' type="text/javascript" src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" type="text/javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<style>
		 .table_headtd1{
		 	height: 41px;
		  background-color: #eeeff3;
		 }
		 #contentborder1 {
			OVERFLOW: auto;
			WIDTH: 100%;
			POSITION: absolute;
			HEIGHT: 100%;
			
		}
		</style>
	<script type="text/javascript">
		function showchange(){
		     var type=$("#treeType").val();
		     //parent.project_work_tree.location = "<%=root%>/archive/tempfile/tempFile!tree.action?treeType="+type+"&depLogo=<%=depLogo%>";
		     document.getElementById("project_work_tree").src= "<%=root%>/archive/tempfile/tempFile!tree.action?treeType="+type+"&depLogo=<%=depLogo%>";
	    }
	</script>
	</head>
	<BODY class="contentbodymargin">
		<input type="hidden" id="depLogo" name="depLogo" value="${depLogo}">
		<div id=contentborder1  style="background-color: #eeeff3;">
			<table border="0" cellspacing="0" width="100%" bordercolordark="#FFFFFF" bordercolorlight="#000000" bordercolor="#333300" cellpadding="2" align="center" >
				<tr style="background-color: #eeeff3;">
					<td class="table_headtd1">
						<table>
							<tr>
								<td class="table_headtd_img" style="padding-left:11px">
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left" style="font-size: 14px;" >
									<strong>选择视图</strong>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td width="72%">
						<s:select id="treeType" name="treeType"
							list="#{'按年份分组':'4','按文档类型分组':'1'}" listKey="value"
							listValue="key" cssStyle="width:100%;" cssClass="wztext"
							onchange="showchange();" ></s:select>
						<!-- OA5.0发版中不包含此功能
						<s:select id="treeType" name="treeType"
							list="#{'按年份分组':'4','按文档类型分组':'1','按所属卷（盒）号分组':'2'}" listKey="value"
							listValue="key" cssStyle="width:100%;" cssClass="wztext"
							onchange="showchange();" ></s:select>
						 -->
					</td>
					<%-- <td width="2%">
						<img src="<%=root%>/images/ico/search1.jpg" onclick="showchange();">
					</td>--%>
				</tr>
			</table>
			<iframe id="project_work_tree" name="project_work_tree" marginWidth=0 marginHeight=0 width="100%" height="80%"
				src="<%=root%>/archive/tempfile/tempFile!tree.action?treeType=<%=request.getParameter("treeType")%>"
				frameBorder=0 scrolling=no>
		</div>
	</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@	include file="/common/include/meta.jsp"%>
<HTML>
	<HEAD>
		<TITLE>请选择岗位</TITLE>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" >
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
			  <tr>
				  <td >
				  	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="postId" isCanDrag="true" isCanFixUpCol="true" 
				     clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty"  collection="${postList}" showSearch="false">
						<webflex:flexCheckBoxCol caption="选择" property="postId" showValue="postName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
						<webflex:flexTextCol caption="岗位名称" property="postName" showValue="postName" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						<webflex:flexTextCol caption="岗位说明" property="postDescription" showValue="postDescription" width="60%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
				  	</webflex:flexTable>
				  </td>
			  </tr>
			</table>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>
	</BODY>
</HTML>

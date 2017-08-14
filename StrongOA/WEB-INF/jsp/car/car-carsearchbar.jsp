<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<html>
	<head>
		<title>车辆筛选</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<!--右键菜单样式 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
	</HEAD>
  
  <body>
  <div id=contentborder >
    <table height="100%" width="100%"  style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
	    <tr>
		    <td>
		    	&nbsp;&nbsp;
    			<img src="<%=frameroot%>/images/ico/ico.gif">
    			&nbsp;&nbsp;
    			<font class=wz>选择车辆 切换模式：</font>
		    </td>
		</tr>
		<tr align="left">
		    <td>
    			<input type="button" class="input_bg" value="根据车辆">
    			<input type="button" class="input_bg" value="根据时间">
		    </td>
	    </tr>
    </table>    
  </div>
  </body>
</html>

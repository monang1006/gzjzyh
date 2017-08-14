<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
	<link href="<%=frameroot%>/css/properties_windows.css"
    	 type="text/css" rel="stylesheet">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
	function downLoad(){
		window.open ('<%=root%>/doc/sends/dianziqianzhang.rar','window');
	}
	</script>
  </head>
  
  <body>
  <table>
	  <tr>
	  	  <td width="93%">&nbsp;</td>
		  <td width="7%">
		  	<a class="Operation" style="color: #FF3030;" width="15" height="15" href="javascript:downLoad()">下载电子签章</a>
		  </td>
	  </tr>
  </table>
  </body>
</html>

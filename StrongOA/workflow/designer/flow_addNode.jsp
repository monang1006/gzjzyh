<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
<HEAD>
<html:base/>
<TITLE> 节点增加 </TITLE>	
<link type="text/css" rel="StyleSheet" href="css/main.css" />
<LINK href="<%=request.getContextPath()%>/common/frame/css/properties_windows.css" type=text/css rel=stylesheet>
<SCRIPT LANGUAGE="JavaScript">

function addNode(){
   var opener = window.dialogArguments;
   var nodeName = document.getElementById("nodeName").value;
   window.returnValue = nodeName;
   window.close();
}
</SCRIPT> 
</HEAD>
<BODY align = "center">
<table>
	<tr>
		<td>
			请输入节点类型
		</td>	
	</tr>
	<tr>
		<td>
			<input type = "text" id = "nodeName">&nbsp;&nbsp;<input type="button" class="input_bg" value="确定" onclick="addNode()">
		</td>
	</tr>
</table>
</BODY>
</HTML>

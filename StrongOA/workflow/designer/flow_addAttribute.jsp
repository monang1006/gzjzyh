<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
<HEAD>
<html:base/>
<TITLE> 属性增加 </TITLE>
<link type="text/css" rel="StyleSheet" href="css/main.css" />
<LINK href="<%=request.getContextPath()%>/common/frame/css/properties_windows.css" type=text/css rel=stylesheet>
<SCRIPT LANGUAGE="JavaScript">

function addAttribute(){
   var opener = window.dialogArguments;
   var attributeName = document.getElementById("attributeName").value;
   var attributeValue = document.getElementById("attributeValue").value;
   window.returnValue = attributeName + "|" + attributeValue;
   window.close();
}
</SCRIPT> 
</HEAD>
<BODY align = "center">
<table>
	<tr>
		<td>
			请输入属性名称
		</td>	
	</tr>
	<tr>
		<td>
			<input type = "text" id = "attributeName">
		</td>
	</tr>
	<tr>
		<td>
			请输入属性值
		</td>	
	</tr>
	<tr>
		<td>
			<input type = "text" id = "attributeValue">&nbsp;&nbsp;<input type="button" class="input_bg" value="确定" onclick="addAttribute()">
		</td>
	</tr>	
</table>
</BODY>
</HTML>

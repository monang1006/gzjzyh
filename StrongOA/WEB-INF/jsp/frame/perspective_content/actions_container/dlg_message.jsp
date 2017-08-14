<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>短消息提示</title>
<SCRIPT LANGUAGE="JavaScript" src="<%=jsroot%>/commontab/dlg_service.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function window.onload(){
	var argu = window.dialogArguments;
	if (argu != null){
		MESSAGE.innerText = argu;
	}
	adjustWin();
}
function btn_ok_onclick(){
	window.close();
}
//与点击右上方关闭一样
function btn_cancel_onclick(){
	window.close();
}

//-->
</SCRIPT>
</head>

<body leftmargin="2" topmargin="2" bgcolor=#F8F8F8>
<img src="<%=frameroot%>/images/tab/title-message.gif" width="64" height="13"> 
<table width="100%" height="10" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="65" bgcolor="#EBEBEB"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td width="2"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td bgcolor="#F74809"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td width="2"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td bgcolor="#0B61B4"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
  </tr>
</table>
<br>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="65" align="center" valign="top" style="padding-bottom:10pt"><img src="<%=frameroot%>/images/tab/left-message.gif" width="52" height="49"></td>
    <td width="1" bgcolor="#9D9D9D"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td valign="top" style="padding-left:10pt;font-size:14">
	<TEXTAREA style="height:100;background-color:#F8F8F8;width:100%;border:0;word-break:break-all;overflow:visible" readonly id="MESSAGE"></TEXTAREA>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right" style="padding-right:15pt"><input type="image" onclick="btn_ok_onclick()" src="<%=frameroot%>/images/tab/qiehuan.gif" width="114" height="22">
	<input type="image" onclick="btn_cancel_onclick()" src="<%=frameroot%>/images/tab/quxiao.gif" width="49" height="21">
    </td>
  </tr>
</table>
</body>
</html>

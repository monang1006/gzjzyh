<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>输入框</title>
<SCRIPT LANGUAGE="JavaScript" src="<%=jsroot%>/commontab/dlg_service.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function window.onload(){
	window.returnValue = null;
	//数组{prompt,value}
	var argu = window.dialogArguments;
	if (argu != null){
		if (argu[2] == true){
			PANEL.innerHTML = '<input id="DATA" type="password" style="width:100%;border:solid 1 gray">';
			DATA.setActive();
			DATA.focus();
		}
		if (argu[0] != null){
			PROMPT.innerText = argu[0];
		}
		if (argu[1] != null){
			DATA.value = argu[1];
		}
	}
	//在填充了数据后再调整窗口，因为body内容可能发生了变化
	adjustWin();
}
function btn_ok_onclick(){
	window.returnValue = DATA.value;
	window.close();
}
//与点击右上方关闭一样
function btn_cancel_onclick(){
	window.close();
}

//-->
</SCRIPT>
<BGSOUND src="<%=path%>/common/sound/prompt.wav"/>
</head>

<body leftmargin="2" topmargin="2" bgcolor=#F8F8F8>
<img src="<%=frameroot%>/images/tab/title-prompt.gif" width="64" height="13"> 
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
    <td width="65" align="center" valign="top"><img src="<%=frameroot%>/images/tab/left-prompt.gif" width="46" height="48"></td>
    <td width="1" bgcolor="#9D9D9D"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td valign="top" style="padding-left:10pt;padding-bottom:12pt;word-break:break-all;font-size:14"><p id="PROMPT">请输入数据：</p>
      <p id="PANEL"> 
        <input id="DATA" type="text" style="width:100%;border:solid 1 gray">
      </p></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="right" style="padding-right:15pt"><input type="image" src="<%=frameroot%>/images/tab/queding.gif" width="49" height="21" onclick="btn_ok_onclick()"> 
      <input type="image" src="<%=frameroot%>/images/tab/quxiao.gif" width="49" height="21"  onclick="btn_cancel_onclick()"> </td>
  </tr>
</table>
</body>
</html>


<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>信息确认</title>
<SCRIPT LANGUAGE="JavaScript" src="<%=jsroot%>/commontab/dlg_service.js"></SCRIPT>
<style type="text/css">
.scroll{
	 scrollbar-face-color: #eeeeee; 
	 scrollbar-shadow-color: #9F9F9F; 
	 scrollbar-highlight-color: #eeeeee; 
	 scrollbar-3dlight-color: #9F9F9F; 
	 scrollbar-darkshadow-color: #FFFFFF; 
	 scrollbar-track-color: #FFFFFF;
	 scrollbar-arrow-color: #000000;  

}


.button {
	width:73px; height:24px; line-height:24px; border:none; background:url(<%=frameroot%>/images/add_bt.jpg) no-repeat center;
	FONT-SIZE: 14px; 
	font-family: "宋体";
	text-align: center;
	cursor: pointer;
}

.Operation_input{
	background:url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;
	font-weight: bold;
	color:white;
	cursor: pointer;
}

.Operation_input1{
	background:url(<%=frameroot%>/images/ch_z_m.gif) repeat-x;
	font-weight: bold;
	cursor: pointer;
	color:#454953;
}
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--
function window.onload(){
	window.returnValue = false;
	var argu = window.dialogArguments;
	if (argu != null){
		CONFIRM.innerText = argu;
	}
	adjustWin();
	if(window.dialogHeight == window.screen.height+"px"){
		CONFIRM.style.height = window.screen.height-130;
		CONFIRM.style.overflow = "auto";
	}
}
function btn_ok_onclick(){
	window.returnValue = true;
	window.close();
}
//与点击右上方关闭一样
function btn_cancel_onclick(){
	window.close();
}

//-->
</SCRIPT>
<BGSOUND src="<%=path%>/common/sound/confirm.wav"/>
</head>

<body leftmargin="2" topmargin="2" bgcolor=#F8F8F8>
<img src="<%=frameroot%>/images/tab/title-confirm.gif" width="65" height="14"> 
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
    <td width="65" align="center" valign="top" style="padding-bottom:10pt"><img src="<%=frameroot%>/images/tab/left-confirm.gif" width="42" height="57"></td>
    <td width="1" bgcolor="#9D9D9D"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td valign="top" style="padding-left:10pt;font-size:14">
	<TEXTAREA style="height:100%;background-color:#F8F8F8;width:100%;border:0;word-break:break-all;overflow:visible;FONT-SIZE: 14px; " readonly id="CONFIRM" class="scroll"></TEXTAREA>
	</td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<!--
  <tr>
    <td align="right" style="padding-right:15pt">
    	<input type="image" onclick="btn_ok_onclick()" tabindex=1 src="<%=frameroot%>/images/tab/queding.gif" width="49" height="21"> 
      <input type="image" onclick="btn_cancel_onclick()" tabindex=2 src="<%=frameroot%>/images/tab/quxiao.gif" width="49" height="21"></td>
  </tr>
   -->
   <tr align="right" >
	   <td>
		<table  border="0" cellspacing="0" cellpadding="0">
		  	<tr align="right" >
	      	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	      	<td class="Operation_input" onclick="btn_ok_onclick();">&nbsp;确&nbsp;定&nbsp;</td>
	      	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
	      		<td width="5"></td>
	      	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
	      	<td class="Operation_input1" onclick="btn_cancel_onclick();">&nbsp;取&nbsp;消&nbsp;</td>
	      	<td width="6"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
	      		<td width="5"></td>
	      		</tr>
	     </table>
	   </td>
	</tr>
</table>
</body>
</html>

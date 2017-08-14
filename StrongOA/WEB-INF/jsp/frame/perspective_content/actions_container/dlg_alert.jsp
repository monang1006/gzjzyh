<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>信息提示</title>
<SCRIPT LANGUAGE="JavaScript" src="<%=jsroot%>/commontab/dlg_service.js"></SCRIPT>
<style type="text/css">
.scroll{
	 scrollbar-face-color: #eeeeee; 
	 scrollbar-shadow-color: #9F9F9F; 
	 scrollbar-highlight-color: #eeeeee; 
	 scrollbar-3dlight-color: #9F9F9F; 
	 scrollbar-darkshadow-color: #FFFFFF; 
	 scrollbar-track-color: #F8F8F8;
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
	var argu = window.dialogArguments;
	if (argu != null){
		//根据消息级别显示不同的图标
		if (argu[0] != null){
			switch(argu[0]){
				case "0":
					FLAG_TITLE.src = "<%=frameroot%>/images/tab/title-info.gif";
					FLAG_ICON.src = "<%=frameroot%>/images/tab/left-info.gif";
					break;
				case "1":
					FLAG_TITLE.src = "<%=frameroot%>/images/tab/title-warning.gif";
					FLAG_ICON.src = "<%=frameroot%>/images/tab/left-warning.gif";
					break;
				case "2":
					FLAG_TITLE.src = "<%=frameroot%>/images/tab/title-error.gif";
					FLAG_ICON.src = "<%=frameroot%>/images/tab/left-error.gif";
					break;
				default:
					FLAG_TITLE.src = "<%=frameroot%>/images/tab/title-info.gif";
					FLAG_ICON.src = "<%=frameroot%>/images/tab/left-info.gif";
			}
		}
		if (argu[1] != null){
			ALERT.innerText = argu[1];
		}
	}
	adjustWin();
	if(window.dialogHeight == window.screen.height+"px"){
		ALERT.style.height = window.screen.height-130;
		ALERT.style.overflow = "auto";
	}
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
<BGSOUND id=sudAlert src="<%=path%>/common/sound/alert.wav"/>
</head>

<body leftmargin="2" topmargin="2" bgcolor=#F8F8F8>
<img id="FLAG_TITLE" src="<%=frameroot%>/images/tab/title-info.gif" width="64" height="13"> 
<table width="100%" height="10" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td style="width:65" bgcolor="#EBEBEB"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td width="2"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td bgcolor="#F74809"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td width="2"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td bgcolor="#0B61B4"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
  </tr>
</table>
<br>
<table style="width:100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td style="width:65" align="center" valign="top" style="padding-bottom:10pt"><img id="FLAG_ICON" src="<%=frameroot%>/images/tab/left-info.gif" ></td>
    <td style="width:1" bgcolor="#9D9D9D"><img src="<%=frameroot%>/images/tab/spacer.gif" width="1" height="1"></td>
    <td valign="top" style="padding-left:10pt;font-size:14;height:100%;">
	<TEXTAREA style="height:100%;background-color:#F8F8F8;width:100%;border:0;word-break:break-all;overflow:visible;FONT-SIZE: 14px; " readonly id="ALERT" class="scroll"></TEXTAREA>
	</td>
  </tr>
</table>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
   <tr align="right" >
	   <td>
		<table  border="0" cellspacing="0" cellpadding="0">
		  	<tr align="right" >
	      	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
	      	<td class="Operation_input" onclick="btn_ok_onclick();">&nbsp;确&nbsp;定&nbsp;</td>
	      	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
	      		<td width="5"></td>
	      		</tr>
	     </table>
	   </td>
	</tr>
</table>
</body>
</html>

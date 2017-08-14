<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<head>
<title>系统安装与检测document.all("chk_tt").innerHTML=Acx</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}
body {
	font-size: 14px;
	font-family: "\5b8b\4f53", sans-serif;
	line-height: 1.6;
	color: #000;
	background: #fff;
}
a {
	color: #333;
	text-decoration: none;
}
a:hover {
	color: #000;
	text-decoration: none;
}
img {
	border: none;
}
ul, ol {
	list-style: none;
}
table {
	border-collapse: collapse;
	border-spacing: 0;
	width: 100%;
}
h1, h2, h3, h4, h5, h6 {
	font-size: 100%;
}
h3 {
	color: #0158aa;
}
#container {
	width: 1002px;
	margin: 0 auto;
}
.cdtit {
	font-size: 22px;
	height: 56px;
	line-height: 56px;
	text-align: center;
	border-bottom: 2px solid #1162af;
}
.clefttd {
	width: 246px;
	background-color: #fafafa;
	border: 1px solid #ccc;
	border-right: 3px solid #f0f0f0;
}
.clumn {
	font-size: 14px;
	padding: 6px;
	background-color: #fff;
}
.clumntit {
	font-weight: bold;
	border-bottom: 1px dashed #ccc;
	padding: 6px 10px;
}
.clumn a {
	height: 24px;
	line-height: 24px;
	color: #106d98;
	text-decoration: underline;
}
.clumn a:hover {
	color: #000;
	text-decoration: underline;
}
.clumn dd {
	padding-left: 6px;
}
.clumn dd li {
	padding-left: 16px;
}
.clumn dd li.clundlg {
	padding-left: 32px;
}
.crigtd {
	border: 1px solid #ccc;
}
.content {
	padding: 20px 28px;
}
.content h3 {
	font-size: 14px;
	padding: 2px 0;
}
.contej {
	padding-left: 16px;
	font-weight: bold;
}
.contsj {
	font-weight: bold;
	padding: 4px 0;
	padding-left: 32px;
}
.content img {
	margin: 8px 0;
}
.contabdk th, .contabdk td {
	border: 1px solid #ccc;
	padding: 0 4px;
}
</style>
<script language="VBScript">
	'检测是否安装了所有需要注册的组件'
	Function checkAcxs()
		checkActivex "RTXClient.RTXAPI" ,"即时通讯" ,"chk_rtx"
		checkActivex "Msxml2.XMLHTTP" ,"电子表单" ,"chk_forminput"
	End Function

	'检测是否安装了组件'
	Function checkActivex(acxName,aliasName,outObj)
		On Error Resume Next
		Set Acx = CreateObject(acxName) 
		Dim Msg
		Msg = ""
		If Not IsObject(Acx) Then
			Msg = aliasName
		End If
		If Msg <> "" Then
			document.all(outObj).innerHTML = "<font color=red>ㄨ</font>您没有安装["& Msg & "]组件，请先点上边链接下载安装"
		Else
			document.all(outObj).innerHTML = "<font color=green>√</font>["& aliasName & "]组件安装成功！"
		End If
	End Function
</script>
</head>

<body>
<div id="container">
  <h2 align=center class="cdtit"> ≡插件安装与常见问题≡ </h2>
  <table>
    <tr>
      <td valign="top" class="clefttd"><div class="clumn">
          <p class="clumntit"> 目录 </p>
          <dl class="holder1" id="catalog-holder-0">
            <dd> · <a href=#download class='nslog:1274' name="STAT_ONCLICK_UNSUBMIT_CATALOG">文件链接</a>
              <ol>
                <li class="clundlg"> <a href="#1_1">下载说明</a> </li>
                <li class="clundlg"> <a href="#1_2">下载列表</a> </li>
              </ol>
            </dd>
            </dd>
          </dl>
          <dl class="holder2" id="catalog-holder-2-0">
          </dl>
        </div></td>
      <td valign="top" class="crigtd"><div class="content">
          <p align=left> <a name="download" /></p>
          <h3>◆一、终端 / 客户机要求</h3>
          <ol>
            <li>操作系统：Microsoft WindowsXP SP3、Microsoft  Windows Vista、Microsoft Windows 7； </li>
            <li>IE浏览器：7.0及以上版本； （注：请使用WindowsXP的用户务必升级IE浏览器，具体下载地址详见【二、文件链接 - 下载列表】）</li>
            <li>电脑需安装Microsoft Office 2003或以上版本。 </li>
          </ol>
          <p>注：由于网上下载的破解版本的操作系统及Office安装程序可能存在文件缺失的情况，在实际使用过程中将导致OA系统的访问出现异常，因此建议客户机尽量使用正版操作系统及Office软件的安装介质进行安装。 
          </p>
          </p>
          <h3> ◆二、文件链接 </h3>
          <ol>
            <li class="contej"> ·下载说明 <a name="1_1"></a> </li>
            <li>
              <p style="text-indent: 2em;"> 如果没有出现自动安装界面，您可以点击以下链接，下载相应的安装程序，手动安装。 如果打开链接时不能正常下载，您可以在链接上击右键，选择" <font color=green>目标另存为(<u>A</u>)...</font>" </p>
            </li>
            <li> 下载列表 <a name="1_2"></a> </li>
          </ol>
          <table width="91%" style="height: 134" class="contabdk">
            <tr align=center>
              <th> 序号 </th>
              <th> 链接下载 </th>
              <th> 文件说明 </th>
            </tr>
            <tr>
              <td align="center"> 1</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/StartUtil.zip"
											target="_blank">南昌市政府办公自动化初始化工具V1.1.zip</a></td>
              <td> 初始化工具</td>
            </tr>
            <tr>
              <td align="center"> 2 </td>
              <td><a style="color: #06F; text-decoration: underline;"
											href="<%=path%>/detection/lib/OA_InitializationTool.zip">OA系统浏览器初始化工具</a><a style="color: #06F; text-decoration: underline;"
											href="<%=path%>/detection/lib/OA_InitializationTool.zip"></a></td>
              <td>IE浏览器OA控件安装工具</td>
            </tr>
            <tr>
              <td align="center"> 3</td>
              <td><a style="color: #06F; text-decoration: underline;"
											href="<%=path%>/detection/lib/IE8-WindowsXP-x86-CHS.exe"></a><a style="color: #06F; text-decoration: underline;"
											href="<%=path%>/detection/lib/IE8-WindowsXP-x86-CHS.exe">Internet Explorer 8 for WinXP简体中文版</a></td>
              <td>IE8.0升级程序包</td>
            </tr>
            <tr class="tr_bg">
              <td align="center"> 4</td>
              <td><a style="color: #06F; text-decoration: underline;"
											href="<%=path%>/detection/lib/office2007_setup.zip">office2007 简体中文版</a></td>
              <td>OFFICE程序安装包</td>
            </tr>
            <tr>
              <td align="center"> 5</td>
              <td><a style="color: #06F; text-decoration: underline;"
											href="<%=path%>/detection/lib/UKEY_drivers.zip">UKEY驱动</a></td>
              <td> 系统登录UKEY驱动程序</td>
            </tr>
            <tr>
              <td align="center"> 6</td>
              <td><a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/BigAntClient_Setup.zip"
											target="_blank">即时通讯软件</a></td>
              <td> 内部即时通讯软件</td>
            </tr>
            <tr>
              <td align="center"> 7</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/SignatureScan.zip"
											target="_blank">签章软件</a></td>
              <td> 金格签章控件</td>
            </tr>
            <tr>
              <td align="center"> 8</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/PDF_View.zip"
											target="_blank">PDF浏览软件</a></td>
              <td> PDF文件浏览器</td>
            </tr>
            <tr>
              <td align="center"> 9</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/KANcSetup.zip"
											target="_blank">网络版杀毒软件</a></td>
              <td> 金山杀毒网络版</td>
            </tr>
            <tr>
              <td align="center"> 10</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/LanSecS.zip"
											target="_blank">内网行为管理软件</a></td>
              <td> 内部行为管理软件-LanSecS</td>
            </tr>
            <tr>
              <td align="center"> 11</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/wrar401sc.exe"
											target="_blank">压缩软件</a></td>
              <td> 压缩软件-WinRAR</td>
            </tr>
            <tr>
              <td align="center"> 12</td>
              <td> <a style="color: #06F; text-decoration: underline;" href="<%=path%>/detection/lib/SouGuo_Input.zip"
											target="_blank">系统输入法</a></td>
              <td> 搜狗：拼音、五笔输入法</td>
            </tr>
            
          </table>
          <h3>&nbsp;</h3>
<h3>&nbsp;</h3>
<ol>
</ol>
        </div></td>
    </tr>
  </table>
</div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<head>
<title>系统安装与检测</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
}
body {
	font-size: 12px;
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
            <dd> · <a href=#notice class='nslog:1274' name="STAT_ONCLICK_UNSUBMIT_CATALOG">注意事项</a>
              <ol>
                <li class="clundlg"> <a href="#2_1">对本地资源的访问</a> </li>
                <li class="clundlg"> <a href="#2_2">IE相关设置</a> </li>
              </ol>
            </dd>
            <dd> · <a href=#install class='nslog:1274' name="STAT_ONCLICK_UNSUBMIT_CATALOG">组件的安装步骤</a>
              <ol>
                <li class="clundlg"> <a href="#3_1">字体安装</a> </li>
              </ol>
            </dd>
          </dl>
          <dl class="holder2" id="catalog-holder-2-0">
          </dl>
        </div></td>
      <td valign="top" class="crigtd"><div class="content">
          <p align=left> <a name="download" /> </p>
          <h3> ◆一、文件链接 </h3>
          <ol>
            <li class="contej"> ·下载说明 <a name="1_1"></a> </li>
            <li>
              <p style="text-indent: 2em;"> 如果没有出现自动安装界面，您可以点击以下链接，下载相应的安装程序，手动安装。" </p>
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
              <td align="center"> 1 </td>
              <td><a style="color: #06F; text-decoration: underline;" id="attachDownLoad"
											href="#" onclick="download(1)">仿宋GB2312字体下载</a></td>
              <td> 期刊模版字体 </td>
            </tr>
            <tr>
              <td align="center"> 2 </td>
               <td><a style="color: #06F; text-decoration: underline;" id="attachDownLoad"
											href="#" onclick="download(2)">楷体GB2312字体下载</a></td>
              <td> 期刊模版字体 </td>
            </tr>
          </table>
          <a name="notice" />
          <h3> ◆二、注意事项 </h3>
          <ol>
            <li>
              <p class="contej"> ·对本地资源的访问 </p>
              <a name="2_1"></a> <br />
              <p style="text-indent: 2em;"> 系统中的操作，将会访问本地的注册表文件（设置配置信息）或读写本地文件（将服务器的文件缓存到本地，以提高效率）。所以，对于本系统所在域内， <font color=green>如果弹出ActiveX安全提示，询问是否允许活动内容，请选择"是"，</font>以便系统顺利运行。 </p>
              <br />
              <div style="text-align: center"> <img src="images\downloadHelp\activex.jpg" width="429" height="221" /> </div>
            </li>
            <li>
              <p class="contej"> ·IE相关设置 </p>
              <a name="2_2"></a> <br />
              <p style="text-indent: 2em;"> 默认情况下，系统会自动将其所在网址设置为可信任站点，以便执行对本地资源的访问。如果仍出现"Automation服务器不能创建对象"的错误，请修改浏览器的安全设置（降低IE的级别），以使它能够顺利运行。 </p>
            </li>
          </ol>
          <p> <a name="install" /> </p>
          <h3> ◆三、组件的安装步骤 </h3>
          <blockquote>
            <h4 class="contej"> 1、仿宋GB2312字体安装 <a name="3_1"></a> </h4>
            <blockquote>
              <h5 class="contsj"> 1.1 、双击运行运行下载的仿宋GB2312.ttf文件 </h5>
              <div>
                <div>
                  <div style="text-align: center"> <img src="images\downloadHelp\fontInstall.jpg" width="800" height="700" /> </div>
                </div>
              </div>
              <h5 class="contsj"> 1.2、等进度条走完了，安装便成功了 </h5>
            </blockquote>
          </blockquote>
          <p> <a name="check" id="check" /> </p>
          <ol>
          </ol>
        </div></td>
    </tr>
  </table>
</div>
<script>

//下载附件
function download(op){
	window.location = "<%=root%>/xxbs/action/submit!officeStream1.action?op="+op;
}
</script>
</body>
</html>

<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>

<HTML>
<HEAD>
<TITLE>eWebEditor ： 输入框调用上传文件对话框示例</TITLE>
<META http-equiv=Content-Type content="text/html; charset=GBK">
<link rel='stylesheet' type='text/css' href='example.css'>
</HEAD>
<BODY>

<p><b>导航 ： <a href="default.jsp">示例首页</a> &gt; 输入框调用上传文件对话框示例</b></p>
<p>通过使用这个功能，您可以在任意的输入框中调用编辑器自带的上传文件功能，上传文件和浏览服务器上原已上传的文件，并返回输入框上传文件的路径文件名。您可以在一个网页的任意多个地方调用，支持分类型上传及缩略图调用，并可在编辑器后台设置。一行调用代码，即可为您的网站加入上传文件功能。</p>
<p>点击下面的“上传”按钮，看一下效果，不同的按钮允许上传的文件类型及文件大小不同。</p>


<script language="javascript">

//创建一个编辑器实例，只用于上传接口，所以此处是隐藏的
function EWEBeditor_Create(s_ID, s_Style){
	document.write('<input type="hidden" name="'+s_ID+'" value="">'
		+'<iframe id="'+s_ID+'__Iframe" src="../ewebeditor.htm?id='+s_ID+'&style='+s_Style+'" width=0 height=0 style="display:none" frameborder="0" scrolling="no"></iframe>');
}

/*
参数说明： 
s_Type: 上传文件类型，可用值为"image","flash","media","file"
	image: 图片
	media: 媒体
	flash: Flash
	file: 附件

s_Mode: 上传接口对话框模式
	0:常规模式
	1:常规设置+单文件上传
	2:常规设置+多文件上传(默认)

s_LinkID_SavePathFileName : 文件上传后，用于接收上传文件路径文件名的表单名，返回包含路径的文件名
s_LinkID_SaveFileName : 返回上传文件的文件名
s_LinkID_OriginalFileName : 返回原文件名
*/
function EWEBeditor_ShowDialog(s_ID, s_Type, s_Mode, s_LinkID_SavePathFileName, s_LinkID_SaveFileName, s_LinkID_OriginalFileName){
	var editor = document.getElementById(s_ID+'__Iframe').contentWindow;
	editor.openUploadDialog(s_Type, s_Mode, s_LinkID_SavePathFileName, s_LinkID_SaveFileName, s_LinkID_OriginalFileName)
}

//以下"coolblue",值可以依据实际需要修改为您的样式名,通过此样式的后台设置来达到控制允许上传文件类型及文件大小
EWEBeditor_Create("eWebEditor1", "coolblue");


</script>


<FORM method="post" name="myform" action="">

1. 此示例允许上传图片类型文件：<br>
上传文件：<input type="text" id="d_image_savepath" size="50"> 
保存文件名：<input type="text" id="d_image_savefile" size="20"> 
源文件名：<input type="text" id="d_image_original" size="20"> 
<input type="button" value="上传图片..." onclick="EWEBeditor_ShowDialog('eWebEditor1', 'image', '2', 'd_image_savepath', 'd_image_savefile', 'd_image_original')">
<br><br>

2. 此示例允许上传Flash类型文件：<br>
上传文件：<input type="text" id="d_flash_savepath" size="50"> 
保存文件名：<input type="text" id="d_flash_savefile" size="20"> 
源文件名：<input type="text" id="d_flash_original" size="20"> 
<input type="button" value="上传Flash..." onclick="EWEBeditor_ShowDialog('eWebEditor1', 'flash', '2', 'd_flash_savepath', 'd_flash_savefile', 'd_flash_original')">
<br><br>

3. 此示例允许上传媒体类型文件：<br>
上传文件：<input type="text" id="d_media_savepath" size="50"> 
保存文件名：<input type="text" id="d_media_savefile" size="20"> 
源文件名：<input type="text" id="d_media_original" size="20"> 
<input type="button" value="上传媒体..." onclick="EWEBeditor_ShowDialog('eWebEditor1', 'media', '2', 'd_media_savepath', 'd_media_savefile', 'd_media_original')">
<br><br>

4. 此示例允许上传附件类型文件：<br>
上传文件：<input type="text" id="d_file_savepath" size="50"> 
保存文件名：<input type="text" id="d_file_savefile" size="20"> 
源文件名：<input type="text" id="d_file_original" size="20"> 
<input type="button" value="上传文件..." onclick="EWEBeditor_ShowDialog('eWebEditor1', 'file', '2', 'd_file_savepath', 'd_file_savefile', 'd_file_original')">
<br><br>

<INPUT type=button value="查看源文件" onclick="location.replace('view-source:'+location)"> 

</FORM>

</BODY>
</HTML>

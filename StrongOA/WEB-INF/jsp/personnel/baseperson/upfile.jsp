<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="com.jspsmart.upload.SmartUpload"%> 
<html> 
<head>
<%
    String path=request.getContextPath();
	String id=request.getParameter("id");
%>
<SCRIPT>
var id="<%=id%>";
function preview(path,filenum)
{
		var urlStr = path.value;
		var suburlStr=urlStr.substring(urlStr.lastIndexOf(".")+1);//得到文件类型
		var flag=true;
		switch(suburlStr){
		case "jpg" : flag=true; break;
		case "JPG" : flag=true; break;
		case "jpeg": flag=true; break;
		case "JPEG": flag=true; break;
		case "bmp": flag=true; break;
		case "BMP": flag=true; break;
		case "gif": flag=true; break;
		case "GIF": flag=true; break;
		case "png": flag=true; break;
		case "PNG": flag=true; break;
		case "psd": flag=true; break;
		case "PSD": flag=true; break;
		case "dxf": flag=true; break;
		case "DXF": flag=true; break;
		case "cdr": flag=true; break;
		case "CDR": flag=true; break;
		default: flag=false;break;
		}
		if(flag)
		{
			window.parent.document.getElementById(id).value=urlStr;
			document.all.form1.action="<%=path%>/company/upload.jsp?ids=<%=id%>&type="+suburlStr;
			document.all.form1.submit();
			return true;
		}
		else 
		{
			alert("上传的文件中包含非图片文件！请选择图片文件！");
			document.getElementById("file").outerHTML = document.getElementById("file").outerHTML;
			return false;
		}

	}
	</SCRIPT>
</head>      
<body>   
<form action="<%=path%>/company/upload.jsp?ids=<%=id%>" id="form1" name="form1" encType="multipart/form-data"  method="post" target="hidden_frame" >    
    <input type="file" id="file" name="file" onchange="preview(this,0);" style="align:left">   
    <iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>    
</form>    
</body>    
</html>    




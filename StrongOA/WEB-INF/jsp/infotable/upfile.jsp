<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="java.net.URL,java.util.*"%>
<%@ page import="com.strongit.oa.util.UUIDGenerator"%> 
<html> 
<head>
<%
	String id=request.getParameter("id");
	String type=request.getParameter("type");
	String name=request.getParameter("name")==null?"":new String((request.getParameter("name")).trim().getBytes("ISO8859_1"),"utf-8");
	UUIDGenerator gen = new UUIDGenerator();
	String filename = gen.generate().toString();
	URL   urlpath   =  this.getClass().getClassLoader().getResource("/");
	String   strPath   =   urlpath.getPath();
	strPath = strPath.substring(1);
	//strPath = strPath.replaceAll("\\\\","/");
%>
<SCRIPT>
var id="<%=id%>";
function preview(path,filenum,type)
{
		var urlStr = path.value;
		var suburlStr=urlStr.substring(urlStr.lastIndexOf(".")+1);//得到文件类型
		var suburlname=urlStr.substring(urlStr.lastIndexOf("\\")+1);//得到文件名称
		var flag=true;
		var type="<%=type%>";
		if(type==null||type=="null"||type==""||type=="imgs"){
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
		}
		if(flag)
		{
			var url="<%=strPath%>uploadfile/<%=filename%>."+suburlStr;
			window.parent.document.getElementById(id).value="uploadfile/<%=filename%>."+suburlStr;
			//document.getElementById("form1").action="<%=root%>/fileNameRedirectAction.action?toPage=infotable/upload.jsp?type="+suburlStr+"&url="+url+"&id="+id+"&name=<%=name%>&type=<%=type%>";
			document.getElementById("form1").action="<%=root%>/oa/js/infotable/upload.jsp?type="+suburlStr+"&url="+url+"&id="+id+"&name=<%=name%>&type=<%=type%>";
			document.getElementById("form1").submit();
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
<body class=contentbodymargin bgcolor="#F5F6F7">  
<form action="" id="form1" name="form1" encType="multipart/form-data"  method="post" target="hidden_frame" >    
    <input type="file" id="file" name="file"  onchange="preview(this,0,'<%=type%>');">&nbsp;<font color="red">*</font>   
    <iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>    
</form>    
</body>    
</html>    




<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="com.jspsmart.upload.*"%>     
<%    
    String id=request.getParameter("id");
    
    String name=request.getParameter("name")==null?"":new String((request.getParameter("name")).trim().getBytes("ISO8859_1"),"utf-8");
    
    String url=request.getParameter("url");
    
    String type=request.getParameter("type");
    
    //新建一个SmartUpload对象    
    SmartUpload su = new SmartUpload();    
   
    //上传初始化    
    su.initialize(pageContext);    
   
    // 设定上传限制    
    //1.限制每个上传文件的最大长度。    
    su.setMaxFileSize(10000000);    
   
    //2.限制总上传数据的长度。    
    su.setTotalMaxFileSize(20000000);    
   
    //3.设定允许上传的文件（通过扩展名限制）,仅允许doc,txt文件。    
    su.setAllowedFilesList("gif,jpg,jpeg,bmp,png,psd,dxf,cdr,GIF,JPG,JPEG,BMP,PNG,PSD,DXF,CDR,txt,doc,xls");  
        
    //4.设定禁止上传的文件（通过扩展名限制）,禁止上传带有exe,bat,jsp,htm,html扩展名的文件和没有扩展名的文件。    
    try {    
        su.setDeniedFilesList("exe,bat,jsp,htm,html");    
   
        //上传文件    
        su.upload();    
        //将上传文件保存到指定目录    
         //su.save("/uploadfile/",su.SAVE_VIRTUAL); 
         File file = su.getFiles().getFile(0);
         if (!file.isMissing()){
         	file.saveAs(url, su.SAVE_PHYSICAL);
         }
                   
  
    } catch (Exception e) {   
        e.printStackTrace();  
    }   
%>    
<script type="text/javascript">
var type="<%=type%>";
if(type==null||type=="null"||type==""||type=="imgs"){
	window.parent.parent.resizepic("<%=id%>","<%=name%>");
}
</script>


<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.io.OutputStream"/>
<jsp:directive.page import="java.io.File"/>
<jsp:directive.page import="java.io.FileInputStream"/>
<jsp:directive.page import="java.io.IOException"/>
<%
	String path = request.getContextPath();
	response.reset();
	response.setContentType("application/octet-stream");
	OutputStream output = null;
	FileInputStream fis = null;
	try{
		output = response.getOutputStream();
		String type = ".doc";
		String docType = request.getParameter("docType");
		if("1".equals(docType)){
			type = ".doc";
		}else if("2".equals(docType)){
			type = ".xls";
		}else if("3".equals(docType)){
			type = ".ppt";
		}else if("4".equals(docType)){
			type = ".vsd";
		}else if("5".equals(docType)){
			type = ".mpp";
		}else if("6".equals(docType)){
			type = ".wps";
		}
		String rootPath = session.getServletContext().getRealPath("/empty"+type);
		File file = new File(rootPath);
		if(!file.exists()){
			rootPath = session.getServletContext().getRealPath("/empty.doc");
			file = new File(rootPath);
		}
		System.out.println("打开文档:"+rootPath);
		fis = new FileInputStream(file);
		int len = 0;
		byte[] b = new byte[8192];
		while((len=fis.read(b))!=-1){
			output.write(b,0,len);
		}
		output.flush();
		response.flushBuffer();
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception ex){
		ex.printStackTrace();
	} finally {
		if(output!=null){
			try {
				output.close();
			} catch (IOException e) {
			}
		}
		if(fis != null){
			try {
				fis.close();
			} catch (IOException e) {
			}
		}
	}
%>
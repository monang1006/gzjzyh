package com.strongit.gzjzyh.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.util.FileKit;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.创建DiskFileItemFactory对象，配置缓存用
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		// 2.创建 ServletFileUpload对象
		ServletFileUpload servletFileUpload = new ServletFileUpload(
				diskFileItemFactory);
		// 3.设置文件名称编码
		servletFileUpload.setHeaderEncoding("utf-8");
		// 4.开始解析文件
		List<String> uploadFilePaths = new ArrayList<String>(0);
		try {
			List<FileItem> items = servletFileUpload.parseRequest(request);
			for (FileItem fileItem : items) {
				if (!fileItem.isFormField()) {
					// 1.获取文件名称
					String name = fileItem.getName();
					// 2.获取文件的实际内容
					InputStream is = fileItem.getInputStream();
					// 3.保存文件
					String uploadFilePath = FileKit.saveFile(name, is);
					uploadFilePaths.add(uploadFilePath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(JSONObject.toJSONString(uploadFilePaths));
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

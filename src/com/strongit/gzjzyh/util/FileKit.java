package com.strongit.gzjzyh.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class FileKit {

	public final static String getProjectPath() {
		String ProjectPath = FileKit.class.getClassLoader().getResource("")
				.toString();
		ProjectPath = StringKit.replaceSlash(ProjectPath);
		int i = 0;
		while (ProjectPath.indexOf("//") != -1 && i < 20) {
			ProjectPath = ProjectPath.replaceAll("\\Q//\\E", "/");
			i++;
		}
		ProjectPath = ProjectPath.replaceAll("\\Qfile:\\E", "");
		if (ProjectPath.indexOf(":") != -1)
			ProjectPath = ProjectPath.substring(1);
		if (ProjectPath.indexOf("WEB-INF") != -1)
			ProjectPath = ProjectPath.substring(0,
					ProjectPath.indexOf("WEB-INF") - 1);
		return ProjectPath;
	}
	
	public final static String getNewFileRelativePath(String fileName){
		Date now = new Date();
		String folderName = TimeKit.formatDate(now, "yyyyMMdd");
		String folderPath = FileKit.getProjectPath() + "/upload/" + folderName;
		File folder = new File(folderPath);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String newFileName = new Date().getTime() + fileName;
		String filePath = "/upload/" + folderName + "/" + newFileName;
		return filePath;
	}
	
	public final static String relativePath2AbsolutePath(String relativePath){
		return FileKit.getProjectPath() + relativePath;
	}
	
	public final static String saveFile(String originalFileName, InputStream is){
		String filePath = null;
		Date now = new Date();
		String folderName = TimeKit.formatDate(now, "yyyyMMdd");
		String folderPath = FileKit.getProjectPath() + "/upload/" + folderName;
		String newFileName = new Date().getTime() + originalFileName;
		filePath = "/upload/" + folderName + "/" + newFileName;
		if(FileKit.saveFile(is, folderPath, newFileName)){
			filePath = "/upload/" + folderName + "/" + newFileName;
		}
		return filePath;
	}
	
	public final static boolean saveFile(InputStream is, String destFilePath, String destFileName){
		boolean isSaved = true;
		FileOutputStream os = null;
		try{
			if (is != null) {
				File folder = new File(destFilePath);
				if(!folder.exists()){
					folder.mkdirs();
				}
				os = new FileOutputStream(destFilePath + "/" + destFileName);
				int b = 0;
				while ((b = is.read()) != -1) {
					os.write(b);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			isSaved = false;
		}finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return isSaved;
	}

}

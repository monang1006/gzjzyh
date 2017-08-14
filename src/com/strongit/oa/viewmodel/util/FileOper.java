package com.strongit.oa.viewmodel.util;

import java.io.File;

/**
 * <p>Title: FileOper.java</p>
 * <p>Description: 文件操作类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-06-18
 * @version  1.0
 */
public class FileOper {
	
	public static boolean delFile(String path){
		try{
			File file = new File(path);
			System.out.println(file.getName());
			if(file.delete()==true){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String args[]){
		String path = "E:/tomcat/webapps/oa/WEB-INF/jsp/theme";
		System.out.println(path.substring(path.indexOf("WEB-INF/jsp")+"WEB-INF/jsp".length()+1,path.length()));
	}
}

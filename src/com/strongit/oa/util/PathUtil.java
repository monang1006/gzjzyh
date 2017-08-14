package com.strongit.oa.util;

import java.io.File;

import org.apache.commons.io.IOUtils;

import com.strongit.oa.im.IMListener;

/**
 * 存储工程绝对路径.
 * @modify 
 * 		增加存储即时通讯监听对象 2010年10月14日9:14:42
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-4-22 下午03:16:27
 * @version  2.0.2.3
 * @classpath com.strongit.oa.util.PathUtil
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class PathUtil {

	public static String rootPath;
	
	public static IMListener iMListener ;//即时通讯软件监听对象

	public static String getRootPath() {
		if(!rootPath.endsWith(File.separatorChar + "")){//weblogic集群与单点的路径存在差异
			rootPath = rootPath + File.separatorChar;
		}
		return rootPath;
	}

	public static void setRootPath(String rootPath) {
		PathUtil.rootPath = rootPath;
	}

	public static IMListener getIMListener() {
		return iMListener;
	}

	public static void setIMListener(IMListener listener) {
		iMListener = listener;
	}
	
	public static String wordPath()
	{
		String path = PathUtil.class.getResource("/").getPath();
		if(path.startsWith("/"))
		{
			path = path.substring(1);
		}
		path = path.replace("classes/", "");
		path = path + "wordfile/";
		
		if(IOUtils.DIR_SEPARATOR == IOUtils.DIR_SEPARATOR_UNIX)
		{
			path = IOUtils.DIR_SEPARATOR_UNIX + path;
		}
		return path;
	}
}

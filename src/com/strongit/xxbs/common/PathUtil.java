package com.strongit.xxbs.common;

import org.apache.commons.io.IOUtils;

public class PathUtil
{
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

	public static String wordTemplatePath()
	{
		String path = PathUtil.class.getResource("/").getPath();
		
		if(path.startsWith("/"))
		{
			path = path.substring(1);
		}
		path = path.replace("classes/", "");
		path = path + "word-template/";
		
		if(IOUtils.DIR_SEPARATOR == IOUtils.DIR_SEPARATOR_UNIX)
		{
			path = IOUtils.DIR_SEPARATOR_UNIX + path;
		}
		return path;
	}
	
	public static String files()
	{
		String path = PathUtil.class.getResource("/").getPath();
		
		
		if(path.startsWith("/"))
		{
			path = path.substring(1);
		}
		path = path.replace("classes/", "");
		path = path + "util/";
		
		if(IOUtils.DIR_SEPARATOR == IOUtils.DIR_SEPARATOR_UNIX)
		{
			path = IOUtils.DIR_SEPARATOR_UNIX + path;
		}
		return path;
	}
	
	public static String upload()
	{
		String path = PathUtil.class.getResource("/").getPath();
		
		
		if(path.startsWith("/"))
		{
			path = path.substring(1);
		}
		path = path.replace("classes/", "");
		path = path + "upload/";
		
		if(IOUtils.DIR_SEPARATOR == IOUtils.DIR_SEPARATOR_UNIX)
		{
			path = IOUtils.DIR_SEPARATOR_UNIX + path;
		}
		return path;
	}

}

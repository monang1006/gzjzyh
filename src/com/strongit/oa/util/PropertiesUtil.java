package com.strongit.oa.util;
import java.io.FileOutputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class PropertiesUtil {
	
	private static Properties prop = null;
	
	private static Properties otherProp = null;
	
	public final static String fileName = "im.properties";
	
	/**
	 * 是否需要启动Rtx
	 */
	public final static String RTXSTART = "rtx.start";
	
	/**
	 * author:dengzc
	 * description:读取配置文件
	 * modifyer:
	 * description:
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getProperty(String key)throws Exception{
		if(prop == null){
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));			
		}
		return prop.getProperty(key);
	}

	/**
	 * author:dengzc
	 * description:读取配置文件
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public static Properties getProperties()throws Exception{
		if(prop == null){
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));			
		}
		return prop;
	}

	/**
	 * author:dengzc
	 * description:保存配置信息
	 * modifyer:
	 * description:
	 * @param prop
	 * @throws Exception
	 */
	public static void saveProp(Properties prop)throws Exception{
		ClassPathResource resource = new ClassPathResource(fileName);
		FileOutputStream fos = new FileOutputStream(resource.getFile().getPath());
		prop.store(fos, "RtxServer Config Info");
		fos.close();
	}
	
	/**
	 * author:dengzc
	 * description:读取指定的配置文件
	 * modifyer:
	 * description:
	 * @param key
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String getProperty(String key,String fileName)throws Exception{
		if(otherProp == null){
			otherProp = PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));			
		}
		return otherProp.getProperty(key,"");
	}

	/**
	 * 读取指定的配置文件
	 * @author:邓志城
	 * @date:2009-7-15 上午10:33:53
	 * @param fileName
	 * @return 返回指定的配置文件描述信息
	 * @throws Exception
	 */
	public static Properties getPropertiesWithFileName(String fileName)throws Exception{
		return PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));	
	}
	
	/**
	 * author:qibh
	 * description:读取指定的配置文件
	 * modifyer:
	 * description:
	 * @param key
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String getPropertyByFileName(String key,String newFileName)throws Exception{
		otherProp = PropertiesLoaderUtils.loadProperties(new ClassPathResource(newFileName));			
		return otherProp.getProperty(key,"");
	}
}

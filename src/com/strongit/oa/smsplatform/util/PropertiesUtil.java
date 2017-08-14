/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.smsplatform.util;
import java.io.FileOutputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class PropertiesUtil {
	
	public final static String mobile="mobile.mobile";			//移动运营商信息

	public final static String unicom="mobile.unicom";			//联通运营商信息
	
	public final static String telecom="mobile.telecom";		//电信运营商信息
	private static Properties prop = null;
	private static String fileName = "proConfig.properties";
	
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
//		System.out.println(prop.getProperty(key));
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
	public static void saveProp(Properties prop,String description)throws Exception{
		ClassPathResource resource = new ClassPathResource(fileName);
		FileOutputStream fos = new FileOutputStream(resource.getFile().getPath());
		prop.store(fos, description);
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
		if(prop == null){
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));			
		}
		return prop.getProperty(key);
	}
	
}

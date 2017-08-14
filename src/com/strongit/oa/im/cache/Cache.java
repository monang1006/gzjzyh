package com.strongit.oa.im.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.im.config.IMConfigManager;
import com.strongit.oa.im.rtx.RtxBaseService;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongmvc.service.ServiceLocator;

/**
 * 即时通讯接口配置缓存设置.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-6-1 下午03:19:38
 * @version  2.0.2.3
 * @classpath com.strongit.oa.im.cache.Cache
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class Cache {

	private static Map<String, ToaImConfig> config = Collections.synchronizedMap(new HashMap<String, ToaImConfig>());	//缓存即时通讯软件配置
	
	private static String KEY = "key";
	
	private static Map<String, AbstractBaseService> baseService = Collections.synchronizedMap(new HashMap<String, AbstractBaseService>());	//缓存即时通讯软件配置
	
	private static String KEY_CLASS = "key_class";

	/**
	 * 将配置信息加入缓存中.
	 * @author:邓志城
	 * @date:2010-6-1 下午03:19:08
	 * @param imConfig
	 */
	public static void put(ToaImConfig imConfig) {
		config.put(KEY, imConfig);
		AbstractBaseService service = null;
		try {
			service = (AbstractBaseService)Class.forName(imConfig.getImconfigClassName()).newInstance();//加载处理类
		} catch (Exception e) {
			service = new RtxBaseService();
		} 
		baseService.put(KEY_CLASS, service);
	}
	
	/**
	 * 从缓存中读取配置信息.
	 * @author:邓志城
	 * @date:2010-6-1 下午03:19:23
	 * @return
	 */
	public static ToaImConfig get() {
		ToaImConfig imConfig = config.get(KEY);
		if(imConfig == null){
			IMConfigManager manager = (IMConfigManager)ServiceLocator.getService("iMConfigManager");
			imConfig = manager.getConfig();
			put(imConfig);
		}
		return imConfig ;
	}

	/**
	 * 得到系统中配置的即时通讯软件名称.
	 * <P>箬未配置返回空字符串.</P>
	 * @author:邓志城
	 * @date:2010-6-9 上午08:51:14
	 * @return IM名称.
	 */
	public static String getIMName() {
		ToaImConfig imConfig = get();
		if(imConfig == null){
			return "";
		}
		String imName = imConfig.getRest1();
		if(imName == null){
			return "";
		}
		return imName ;
	}
	
	/**
	 * 得到服务处理类
	 * @author:邓志城
	 * @date:2010-6-1 下午03:47:05
	 * @return
	 */
	public static AbstractBaseService getService() {
		get();
		return baseService.get(KEY_CLASS);
	}
	
}

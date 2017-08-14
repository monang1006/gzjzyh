package com.strongit.oa.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Util 处理实体对象
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 16, 2012 11:00:24 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.util.JObjectUtil
 */
public class JObjectUtil {
	/**
	 * 将对象转换为map
	 * 
	 * @author 严建
	 * @param obj
	 * @return
	 * @createTime Mar 16, 2012 11:08:49 AM
	 */
	public static Map beanToMap(Object obj) {
		Map map = null;
		try {
			map = new HashMap();
			recursiveAccess(map, obj.getClass(), obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 递归访问对象obj的类结构，并且将属性名和属性值存放于map中
	 * 
	 * @author 严建
	 * @param map
	 * @param cls
	 * @param obj
	 * @throws Exception
	 * @createTime Mar 16, 2012 11:07:46 AM
	 */
	@SuppressWarnings("unchecked")
	public static void recursiveAccess(Map map, Class cls, Object obj)
			throws Exception {
		if (map == null) {
			throw new NullPointerException("map is not null");
		}
		if (cls == null) {
			throw new NullPointerException("cls is not null");
		}
		if (obj == null) {
			throw new NullPointerException("obj is not null");
		}
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (!map.containsKey(field.getName())) {
				map.put(field.getName(), field.get(obj));
			}
		}
		if (cls.getSuperclass() != null) {
			recursiveAccess(map, cls.getSuperclass(), obj);
		}
	}
}

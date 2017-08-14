/**
 * 线程变量工具类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Aug 19, 2010 8:04:54 AM
 * @version 1.0
 * @classpath com.strongmvc.util.ThreadLocalUtils
 */
package com.strongit.oa.util;

import java.util.Map;

import org.apache.commons.collections.FastHashMap;

public class ThreadLocalUtils {
	private static ThreadLocal local = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new FastHashMap();
		}
	};

	public static Object put(Object key, Object value) {
		return getThreadLocalMap().put(key, value);
	}

	public static Object get(Object key) {
		return getThreadLocalMap().get(key);
	}

	public static Object remove(Object key) {
		return getThreadLocalMap().remove(key);
	}

	public static Map getThreadLocalMap() {
		return ((Map) local.get());
	}

	public static boolean containsKey(Object key) {
		return getThreadLocalMap().containsKey(key);
	}

	public static boolean containsValue(Object value) {
		return getThreadLocalMap().containsValue(value);
	}

	public static void clear() {
		getThreadLocalMap().clear();
	}

}

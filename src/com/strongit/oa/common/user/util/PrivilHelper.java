package com.strongit.oa.common.user.util;

import java.util.HashMap;
import java.util.Map;

import com.strongit.uums.bo.TUumsBasePrivil;

/**
 * 权限信息静态辅助类，缓存功能
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Jan 4, 2009  11:02:12 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.uums.util.PrivilHelper
 */
public class PrivilHelper {
	
	private static Map<String, TUumsBasePrivil> privilMap = new HashMap<String, TUumsBasePrivil>();
	
	public static boolean checkHas(String mapkey) {
		if(privilMap.containsKey(mapkey)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void addPrivil(String mapkey, TUumsBasePrivil privil){
		privilMap.put(mapkey, privil);
	}
	
	public static TUumsBasePrivil getPrivil(String mapkey){
		return privilMap.get(mapkey);
	}
	
	public static void removePrivil(String mapkey){
		privilMap.remove(mapkey);
	}
	
	public static void clearPrivil(){
		privilMap.clear();
	}
	
}
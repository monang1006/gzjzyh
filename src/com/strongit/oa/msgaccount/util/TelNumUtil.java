package com.strongit.oa.msgaccount.util;

import java.util.HashMap;
import java.util.Map;

public class TelNumUtil {
	private static Map<String,String> map;
	static{
		map=new HashMap<String,String>();
		//初始化联通用户所在号码段
		map.put("130", "mobile.unicom");
		map.put("131", "mobile.unicom");
		map.put("132", "mobile.unicom");
		map.put("134", "mobile.unicom");
		map.put("155", "mobile.unicom");
		map.put("185", "mobile.unicom");
		map.put("186", "mobile.unicom");
		//初始化移动用户所在号码段
		map.put("135", "mobile.mobile");
		map.put("136", "mobile.mobile");
		map.put("137", "mobile.mobile");
		map.put("138", "mobile.mobile");
		map.put("139", "mobile.mobile");
		map.put("150", "mobile.mobile");
		map.put("151", "mobile.mobile");
		map.put("152", "mobile.mobile");
		map.put("156", "mobile.mobile");
		map.put("157", "mobile.mobile");
		map.put("158", "mobile.mobile");
		map.put("159", "mobile.mobile");
		map.put("187", "mobile.mobile");
		map.put("188", "mobile.mobile");
		//初始化电信用户所在号码段
		map.put("180", "mobile.telecom");
		map.put("189", "mobile.telecom");
		map.put("153", "mobile.telecom");
		map.put("133", "mobile.telecom");
	}
	public static String getMobileOper(String number){
		if(number!=null){
			try{	
				return map.get(number.substring(0, 3));
			}catch(Exception e){
				return null;
			}
		}else{
			return null;
		}

	}
}

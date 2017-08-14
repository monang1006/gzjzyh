package com.strongit.oa.autoencoder.util;

import java.util.HashMap;
import java.util.Map;

public class NumberUtil {
	
	public final static int numbers = 0;
	
	public final static int chinese = 1;
	
	public final static int bigNumber = 2;
	
	public int type=NumberUtil.numbers;
	
	private static Map<String,String> map;
	
	public NumberUtil(int type){
		
		map = new HashMap<String,String>();
		
		if(type==chinese){
			map.put("0", "零");
			map.put("1", "壹");
			map.put("2", "贰");
			map.put("3", "叁");
			map.put("4", "肆");
			map.put("5", "伍");
			map.put("6", "陆");
			map.put("7", "柒");
			map.put("8", "捌");
			map.put("9", "玖");
		}else if(type==bigNumber){
			map.put("0", "零");
			map.put("1", "一");
			map.put("2", "二");
			map.put("3", "三");
			map.put("4", "四");
			map.put("5", "五");
			map.put("6", "六");
			map.put("7", "七");
			map.put("8", "八");
			map.put("9", "九");
		}else{
			map.put("0", "0");
			map.put("1", "1");
			map.put("2", "2");
			map.put("3", "3");
			map.put("4", "4");
			map.put("5", "5");
			map.put("6", "6");
			map.put("7", "7");
			map.put("8", "8");
			map.put("9", "9");
		}
		
	}
	
	public String getNumberByType(String number){
		String rNumber = "";
		if(number==null){
			return null;
		}else{
			for(int i=0;i<number.length();i++){
				rNumber = rNumber + map.get(String.valueOf(number.charAt(i)));
			}
			return rNumber;
		}
	}
	
	public static void main(String args[]){
		System.out.println(new NumberUtil(NumberUtil.chinese).getNumberByType("12345"));
	}

}

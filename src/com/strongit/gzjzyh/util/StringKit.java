/**
 * 
 */
package com.strongit.gzjzyh.util;

import org.springframework.util.StringUtils;

public class StringKit {
	
	public static String replaceSpecialChar(String str){
		if(StringUtils.hasLength(str)){
			str = str.replaceAll("<|>", "");
		}
		return str;
	}
	
	public static String replaceSlash(String str) {
		String temp = "";
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				temp = temp + str.substring(n, i) + "/";
				n = i + 1;
			}
		}
		if (n < str.length())
			temp = temp + str.substring(n);
		return temp;
	}

}

package com.strongit.oa.address.util;

public class StringUtil {


	/**
	 * 以字符分隔字符串。
	 * @author:邓志城
	 * @date:2009-12-9 上午10:09:38
	 * @param str 需要分隔的字符串
	 * @param c 分隔符，一般是逗号
	 * @return 分割后的字符串数组
	 */
	public static String[] getSplitArray(String str,char c){
		int count = 0;
		for(int i=0;i<str.length();i++){
			char q = str.charAt(i);
			if(',' == q){
				count++;
			}
		}
		String[] newArray = new String[count+1]; 
		for(int j=0,k=0;j<str.length();j++){
			if(null == newArray[k]){
				newArray[k] = "";
			}
			if(',' == str.charAt(j)){
				k++;
				if(j==(str.length()-1)){
					newArray[k] = "";
				}
			}else{
				newArray[k] = newArray[k]+String.valueOf(str.charAt(j));
			}
		}
		return newArray;
	}

	
	
	
	
	public static void main(String[] args) {
//		getSplitArray("", ',');
		
		String test="锦娣,王,王锦娣,,wangjd@strongit.com.cn,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,7,";
		String[] arry=test.split(",");
		System.out.println();
		
	}

}
